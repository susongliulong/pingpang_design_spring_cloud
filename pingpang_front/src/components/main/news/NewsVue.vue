<template>
  <div class="clear">
    <!-- 编辑发布新闻 -->
    <div id="header">
      <!-- 教程所属分类 -->
      <el-select
        v-model="category"
        placeholder="选择资讯分类"
        style="width: 150px"
      >
        <el-option label="入门级别" value="入门级别"></el-option>
        <el-option label="业余级别" value="业余级别"></el-option>
        <el-option label="专业级别" value="专业级别"></el-option>
        <el-option label="介于入门和专业" value="介于入门和专业"></el-option>
      </el-select>

      <el-button
        type="primary"
        size="default"
        @click=""
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
      <!-- 新闻基础信息 -->
      <el-form
        ref="newsRef"
        :model="news"
        status-icon
        label-width="120px"
        size="small"
      >
        <el-form-item label="新闻标题" prop="basicInformation.title">
          <el-input
            v-model="news.basicInformation.title"
            type="text"
            autocomplete="off"
          />
        </el-form-item>

        <el-form-item label="封面照片" prop="basicInformation.coverImage">
          <el-upload
            class="avatar-uploader"
            action="https://run.mocky.io/v3/9d059bf9-4660-45f2-925d-ce80ad6c4d15"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
            :before-upload="beforeAvatarUpload"
          >
            <img v-if="imageUrl" :src="imageUrl" class="avatar" />
            <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
          </el-upload>
        </el-form-item>

        <el-form-item>
          <el-button @click="resetForm(newsRef)" type="danger">重置</el-button>
        </el-form-item>
      </el-form>
      <el-divider/>
      <div id="news">
        <h2>已发布资讯</h2>
        <el-card
        style="margin-top:10px"
          :body-style="{ padding: '0px' }"
          v-for="(news, index) in newses"
          :index="index">
          <img
            src="https://shadow.elemecdn.com/app/element/hamburger.9cf7b091-55e9-11e9-a976-7f4d0b07eef6.png"
            style="width: 120px; height: 120px; display: block"
          />
          <span>{{ news.basicInformation.title }}</span>
          <div>
            <div style="font-size: 12px; color: #999">
              {{ news.basicInformation.publishTime }}
            </div>
            <span style="font-size: 12px; color: #999">点赞量：</span
            ><span>{{ news.basicInformation.likes }}&nbsp;&nbsp;</span>
            <span style="font-size: 12px; color: #999">收藏量：</span
            ><span>{{ news.basicInformation.collects }}&nbsp;&nbsp;</span>
            <span style="font-size: 12px; color: #999">评论量：</span
            ><span>{{ news.basicInformation.comments }}</span>
          </div>
        </el-card>
      </div>
    </div>

    <div id="markdown" class="left">
      <MdEditor v-model="content"></MdEditor>
    </div>
  </div>
</template>
<script setup lang="ts">
import { ref } from "vue";
import type { FormInstance } from "element-plus";

import { ElMessage } from "element-plus";
import { Plus } from "@element-plus/icons-vue";
import type { UploadProps } from "element-plus";

import { MdEditor } from "md-editor-v3";

interface Basicinformation {
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

// 新闻信息
interface News {
  newsId: bigint;
  content: string;
  basicInformation: Basicinformation;
}

// 分类信息
const category = ref("");
const content = ref(""); //markdown文章信息
const news = ref<News>({ basicInformation: {} });
const newsRef = ref<FormInstance>(); // 表单信息
const newses = ref<News[]>([
  {
    basicInformation: {
      publishTime: new Date(),
      likes: 123,
      collects: 123,
      comments: 123,
      title: "震惊！张继科复出了",
    },
  },
]);

// 图像上传
const imageUrl = ref("");

// 提交表单信息
const submitForm = (formEl: FormInstance | undefined) => {};

// 重置表单信息
const resetForm = (formEl: FormInstance | undefined) => {
  if (!formEl) return;
  formEl.resetFields();
};

// 图像上传
const handleAvatarSuccess: UploadProps["onSuccess"] = (
  response,
  uploadFile
) => {
  imageUrl.value = URL.createObjectURL(uploadFile.raw!); // 生成image的地址
};

// 图像上传之前进行校验
const beforeAvatarUpload: UploadProps["beforeUpload"] = (rawFile) => {
  if (rawFile.type !== "image/jpeg") {
    ElMessage.error("Avatar picture must be JPG format!");
    return false;
  } else if (rawFile.size / 1024 / 1024 > 2) {
    ElMessage.error("Avatar picture size can not exceed 2MB!");
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
  max-width: 30%;
  padding: 10px;
  background-color: white;
}
#leftBar #news {
  margin-left: 50px;
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
