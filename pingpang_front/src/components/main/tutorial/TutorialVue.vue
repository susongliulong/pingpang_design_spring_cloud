<template>
  <div class="clear">
    <!-- 编辑发布赛事 -->
    <div id="header">
      <!-- 教程所属分类 -->
      <el-select
        v-model="tutorial.level"
        placeholder="选择分类"
        style="width: 150px"
      >
        <el-option
          v-for="(item, index) in categories"
          :index="index"
          :label="item.category"
          :value="item.id"
        ></el-option>
      </el-select>

      <el-button
        type="primary"
        size="default"
        @click="submit"
        style="margin-left: 20px"
        >发布</el-button
      >
      <el-button
        type="primary"
        size="default"
        @click=""
        style="margin-left: 20px"
        >存为草稿</el-button
      >
    </div>
    <div id="leftBar" class="left">
      <!-- 教程基础信息 -->
      <el-form
        ref="tutorialRef"
        :model="tutorial"
        status-icon
        label-width="120px"
        size="small"
      >
        <el-form-item label="教程标题" prop="basicInformation.title">
          <el-input
            v-model="tutorial.basicInformation.title"
            type="text"
            autocomplete="off"
          />
        </el-form-item>
        <el-form-item label="适合对象" prop="suitableObject">
          <el-select
            v-model="tutorial.suitableObject"
            placeholder="适合对象"
            size="small"
          >
            <el-option label="新手" value="新手"></el-option>
            <el-option label="入门" value="入门"></el-option>
            <el-option label="会正手攻球" value="会正手攻球"></el-option>
            <el-option label="会反手攻球" value="会反手攻球"></el-option>
            <el-option label="会发侧旋球" value="会发侧旋球"></el-option>
            <el-option label="会发下旋球" value="会发下旋球"></el-option>
            <el-option label="业余" value="业余"></el-option>
            <el-option label="专业" value="专业"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="封面照片" prop="basicInformation.coverImage">
          <el-upload
            class="avatar-uploader"
            :action="gatewayUrl + '/tutorial/upload?userId=' + user.id"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
            :before-upload="beforeAvatarUpload"
          >
            <img
              v-if="tutorial.basicInformation.coverImage"
              :src="tutorial.basicInformation.coverImage"
              class="avatar"
            />
            <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
          </el-upload>
        </el-form-item>

        <el-form-item>
          <el-button @click="resetForm(tutorialRef)">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div id="markdown" class="left">
      <MdEditor v-model="tutorial.content"></MdEditor>
    </div>
  </div>
  <h1>已发表文章：</h1>
  <div id="tutorials">
    <div
      class="tutorial-box"
      v-for="(item, index) in tutorials"
      :index="index"
      @click="updateTutorial(item.basicInformation.id)"
    >
      <div class="clear">
        <div class="left" style="margin-right: 100px">
          <img
            :src="item.basicInformation.coverImage"
            alt=""
            style="widows: 100px; height: 100px"
          />
        </div>
        <div class="left">
          <h4>{{ item.basicInformation.title }}</h4>
          <p>
            所属分类：{{ item.levelName }} &nbsp;&nbsp;&nbsp; 适合对象：{{
              item.suitableObject
            }}
          </p>
          <p>
            <el-button
              type="primary"
              size="default"
              @click="updateTutorial(item.basicInformation.id)"
              >修改</el-button
            >
          </p>
        </div>
      </div>
      <div>
        浏览量：{{ item.basicInformation.pageView }}&nbsp;&nbsp;&nbsp;
        点赞量：{{ item.basicInformation.likes }}
      </div>
      <div>
        发布时间：{{
          item.basicInformation.publishTime.toString().substring(0, 10)
        }}
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import { onBeforeMount, ref } from "vue";
import axios from "axios";
import type { FormInstance } from "element-plus";
import { useRouter } from "vue-router";
import { ElMessageBox, ElMessage } from "element-plus";
import { Plus } from "@element-plus/icons-vue";
import type { UploadProps } from "element-plus";

import { MdEditor } from "md-editor-v3";
import gatewayUrl from "@/global";

interface Basicinformation {
  authorId: bigint;
  id: bigint;
  title: string;
  coverImage: string;
  publishTime: Date;

  likes: number;
  comments: number;
  collects: number; // 收藏量
  pageView: number; // 浏览量
  state: number;
}

// 教程信息
interface Tutorial {
  tutorialId: bigint;
  level: number; //分类等级
  levelName: string;
  suitableObject: string;
  content: string;
  basicInformation: Basicinformation; // 基础信息
}

let user: any;
const tutorial = ref<Tutorial>({ basicInformation: {} });
const tutorialRef = ref<FormInstance>(); // 表单信息
const router = useRouter();

// 检查用户登录状态，同时判断用户是否有资格发表教程
onBeforeMount(() => {
  const userString = localStorage.getItem("user");
  if (userString != null) {
    user = JSON.parse(userString).user;
    // 将判断条件复杂化
    if (user.playYears >= 12) {
      // 用户球龄在一年之上的时候，允许用户发表教程
      tutorial.value.basicInformation.authorId = user.id;
      getCategories();
      getTutorialsById(user.id);
    } else {
      ElMessageBox.alert("你当前资历不够，先提升自己球技");
      router.push("/main/news");
    }
  }
});
// 获取所有的文章分类信息
interface Category {
  id: bigint;
  category: string;
}
const categories = ref<Category[]>([]);
function getCategories() {
  axios.get(gatewayUrl + "/tutorial/category").then((resp) => {
    categories.value = resp.data.data;
  });
}

// 获取用户发表的教程信息，仅仅展示一部分信息
const tutorials = ref<Tutorial[]>([]);
function getTutorialsById(userId: bigint) {
  axios({
    method: "get",
    url: gatewayUrl + "/tutorial/" + userId,
  }).then((resp) => {
    tutorials.value = resp.data.data;
  });
}

// 用户点击某条教程信息之后，对教程信息进行回显
function updateTutorial(id: bigint) {
  // 获取根据id查询文章信息
  axios.get(gatewayUrl + "/tutorial/tutorial?tutorialId=" + id).then((resp) => {
    window.scrollTo({
      top: 0,
      behavior: "smooth",
    });
    tutorial.value = resp.data.data;
  });
}

// 提交表单信息
function submit() {
  axios({
    method: "post",
    url: gatewayUrl + "/tutorial/post",
    data: tutorial.value,
  }).then((resp) => {
    if (resp.data.code == 200) {
      ElMessage.success(resp.data.message);
    } else {
      ElMessage.error(resp.data.message);
    }
  });
}

// 重置表单信息
const resetForm = (formEl: FormInstance | undefined) => {
  if (!formEl) return;
  tutorial.value.content = "";
  formEl.resetFields();
};

// 图像上传
const handleAvatarSuccess: UploadProps["onSuccess"] = (response) => {
  tutorial.value.basicInformation.coverImage =
    gatewayUrl +
    "/tutorial/download?fileName=" +
    response.data +
    "&userId=" +
    user.id;
};

// 图像上传之前进行校验
const beforeAvatarUpload: UploadProps["beforeUpload"] = (rawFile) => {
  if (rawFile.type !== "image/jpeg" && rawFile.type !== "image/png") {
    ElMessage.error("封面照片格式应当为jpg或者png");
    return false;
  } else if (rawFile.size / 1024 / 1024 > 2) {
    ElMessage.error("照片大小不超过2MB");
    return false;
  }
  return true;
};
</script>
<style scoped>
/* 图像上传 */
.avatar-uploader .avatar {
  width: 178px;
  height: 178px;
  display: block;
}
#header {
  margin-bottom: 20px;
}
#leftBar {
  margin-left: -50px;
  padding: 10px;
  background-color: white;
}
.tutorial-box {
  border: 1px solid red;
  margin-top: 20px;
}
#markdown {
  margin-left: 20px;
  width: 70%;
}
</style>
<style>
/* 图像上传样式 */
.avatar-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  text-align: center;
}
</style>
