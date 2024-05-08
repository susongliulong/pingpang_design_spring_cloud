<template>
  <div class="clear">
    <!-- 编辑发布新闻 -->
    <div id="header" class="clear">
      <!-- 教程所属分类 -->
      <el-select
        v-model="news.categoryId"
        placeholder="选择资讯分类"
        style="width: 150px"
      >
        <el-option
          v-for="(item, index) in interests"
          :label="item.name"
          :value="item.id"
        />
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

      <el-button
        type="primary"
        size="default"
        @click="manageNews"
        style="margin-left: 20px;float: right;"
        >资讯管理</el-button
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
        <el-form-item label="新闻标题" prop="title">
          <el-input v-model="news.title" type="text" autocomplete="off" />
        </el-form-item>

        <el-form-item label="封面照片" prop="imageUrl">
          <el-upload
            class="avatar-uploader"
            :action="gatewayUrl + '/newsImage/upload'"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
            :before-upload="beforeAvatarUpload"
          >
            <img v-if="news.imageUrl" :src="news.imageUrl" class="avatar" />
            <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
          </el-upload>
        </el-form-item>

        <el-form-item>
          <el-button @click="resetForm(newsRef)" type="danger">重置</el-button>
        </el-form-item>
      </el-form>
      <el-divider />
      <div id="news">
        <h2>已发布资讯</h2>
        <el-card
          style="margin-top: 10px"
          :body-style="{ padding: '0px' }"
          v-for="(news, index) in newses"
          :index="index"
        >
          <span>{{ news.title }}</span>
          <div>
            <div style="font-size: 12px; color: #999">
              {{ news.publishTime.toString().substring(0, 10) }}
            </div>
            <span style="font-size: 12px; color: #999">点赞量：</span
            ><span>{{ news.likes }}&nbsp;&nbsp;</span>
            <span style="font-size: 12px; color: #999">收藏量：</span
            ><span>{{ news.collects }}&nbsp;&nbsp;</span>
            <span style="font-size: 12px; color: #999">评论量：</span
            ><span>{{ news.comments }}</span>
          </div>
        </el-card>
      </div>
    </div>

    <div id="markdown" class="left" >
      <MdEditor v-model="news.content" @onUploadImg="onUploadImg"></MdEditor>
    </div>
  </div>
</template>
<script setup lang="ts">
import { onBeforeMount, ref } from "vue";
import type { FormInstance } from "element-plus";

import { ElMessage } from "element-plus";
import { Plus } from "@element-plus/icons-vue";
import type { UploadProps } from "element-plus";
import { ElForm } from "element-plus";

import { MdEditor } from "md-editor-v3";
import axios from "axios";
import gatewayUrl from "@/global";
import router from "@/router";
import { useRoute } from "vue-router";

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

  categoryId: bigint; // 资讯分类
}

interface News{
  id: bigint;
  title: string;
  imageUrl: string;
  categoryId: bigint;
  content: string;
  authorId: bigint;
}

const newsRef = ref<FormInstance>(); // 表单信息
const news = ref<News>({});
const newses = ref<Basicinformation[]>([]); // 个人已发布新闻资讯

const route = useRoute();

onBeforeMount(() => {
  getInterests();
  const userString = localStorage.getItem("user");
  if (userString != null) {
    const userId = JSON.parse(userString).user.id;
    news.value.authorId = userId;
    authorOfNews(userId);// 上传作者的id信息
    getNewsesByUserId(userId);
  }
  const newsId:bigint = route.query.newsId;
  if (newsId !== undefined) {
    getNewsById(newsId);
  }
})

function getNewsById(newsId:bigint) {
  axios({
    method: 'get',
    url: gatewayUrl + '/newsMessage/newsMessage',
    params: {
      newsId:newsId 
    }
  }).then(resp => {
    news.value = resp.data.data;
  })
}

// 查看用户已经发布的信息
function getNewsesByUserId(userId: bigint) {
  axios({
    method: 'get',
    url: gatewayUrl + '/newsMessage/newsMessages',
    params: {
      userId:userId
    }
  }).then(resp => {
    newses.value = resp.data.data;
  })
}

// 确认提交信息
const submit = () => {
  axios({
    method:'post',
    url: gatewayUrl + '/newsMessage/post',
    data: news.value
  }).then(resp => {
    if (resp.data.code == 200) {
      const newsString = JSON.stringify(resp.data.data);
      localStorage.setItem("news", newsString);
      router.push("/main/news_message");
      ElMessage.success("资讯发布成功");
    } else {
      ElMessage.error("资讯发布失败");
    }
  })
}

// 重置表单信息
const resetForm = (formEl: FormInstance | undefined) => {
  if (!formEl) return;
  formEl.resetFields();
};

// 图像上传
function authorOfNews(userId:bigint) {
  axios({
    method: 'post',
    url:gatewayUrl+'/newsImage/authorOfNews/'+userId
  }).then(resp => {

  })
}
const handleAvatarSuccess: UploadProps["onSuccess"] = (
  response,
  uploadFile
) => {
  news.value.imageUrl = gatewayUrl + "/newsImage/download?fileName=" + response.data; // 生成image的地址
};

// 图像上传之前进行校验
const beforeAvatarUpload: UploadProps["beforeUpload"] = (rawFile) => {
  if (rawFile.type !== "image/jpeg" && rawFile.type!=="image/png") {
    ElMessage.error("图像格式必须为jpg和png格式");
    return false;
  } else if (rawFile.size / 1024 / 1024 > 2) {
    ElMessage.error("Avatar picture size can not exceed 2MB!");
    return false;
  }
  return true;
};

// 获取所有的资讯分类
interface Interest{
  name:string;
  id: number;
}
const interests = ref<Interest[]>([]);

function getInterests() {
  axios.get(gatewayUrl + '/news/interests').then(
    resp => {
      interests.value = resp.data.data;
    }
  )
}

// markdown编辑器点击上传图片
const onUploadImg = async (files, callback) => {
  const res = await Promise.all(
    files.map((file) => {
      return new Promise((rev, rej) => {
        const form = new FormData();
        form.append('file', file);

        axios
          .post(gatewayUrl+"/newsImage/upload", form, {
            headers: {
              'Content-Type': 'multipart/form-data'
            }
          })
          .then((res) => rev(res))
          .catch((error) => rej(error));
      });
    })
  );
  // Approach 1
  callback(res.map((item) => gatewayUrl + '/newsImage/download?fileName=' + item.data.data));
}

function manageNews() {
  router.push({
    path: '/main/manage_news',
    query: {
      userId: news.value.id
    }
  })
}

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
