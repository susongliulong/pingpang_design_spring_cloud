<template>
  <div class="clear">
    <!-- 编辑发布赛事 -->
    <div id="header">
      <!-- 教程所属分类 -->
      <el-select v-model="category" placeholder="选择级别" style="width:150px;">
        <el-option label="入门级别" value="入门级别"></el-option>
        <el-option label="业余级别" value="业余级别"></el-option>
        <el-option label="专业级别" value="专业级别"></el-option>
        <el-option label="介于入门和专业" value="介于入门和专业"></el-option>
      </el-select>

      <el-button type="primary" size="default" @click="" style="margin-left: 20px;">发布</el-button>
      <el-button type="primary" size="default" @click="" style="margin-left: 20px;">存为草稿</el-button>
      
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
        <el-form-item label="教程级别" prop="pass">
          <el-select
            v-model="tutorial.level"
            placeholder="选择级别"
            size="small"
          >
            <el-option label="入门级别" value="入门级别"></el-option>
            <el-option label="业余级别" value="业余级别"></el-option>
            <el-option label="专业级别" value="专业级别"></el-option>
            <el-option
              label="介于入门和专业"
              value="基于入门和专业"
            ></el-option>
          </el-select>
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
          <el-button @click="resetForm(tutorialRef)">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div id="markdown" class="left">
      <MdEditor v-model="content"></MdEditor>
    </div>
  </div>
</template>
<script setup lang="ts">
import { reactive, ref } from "vue";
import type { FormInstance, FormRules } from "element-plus";

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

// 教程信息
interface Tutorial {
  tutorialId: bigint;
  level: string;
  suitableObject: string;
  content: string;
  basicInformation: Basicinformation;
}

// 分类信息
const category = ref("");
const content = ref(""); //markdown文章信息
const tutorial = ref<Tutorial>({ basicInformation: {} });
const tutorialRef = ref<FormInstance>(); // 表单信息

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
#header{
    margin-bottom:20px;
}
#leftBar {
  margin-left: -50px;
  padding: 10px;
  background-color: white;
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
