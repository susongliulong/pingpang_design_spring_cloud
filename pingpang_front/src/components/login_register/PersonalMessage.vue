<template>
  <div class="clear" style="margin-left: 100px;">
    <div id="register" class="left">
      <p id="title">账号信息修改</p>
      <br />
      <el-form
        :ref="ruleFormRef"
        :model="ruleForm"
        :rules="rules"
        label-width="auto"
        class="demo-ruleForm"
        size="small"
        status-icon
      >
        <el-row>
          <el-col :span="6">
            <el-form-item label="用户头像">
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
          </el-col>
          <el-col :span="12" :offset="4">
            <el-form-item label="昵称" prop="name">
              <el-input v-model="ruleForm.name" />
            </el-form-item>
            <el-form-item label="手机号" prop="telephone">
              <el-input v-model="ruleForm.telephone" />
            </el-form-item>
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="ruleForm.email" />
            </el-form-item>
            <el-form-item label="密码" prop="password">
              <el-input v-model="ruleForm.password" type="password" />
            </el-form-item>
            <el-form-item label="确认密码" prop="confirm">
              <el-input v-model="ruleForm.confirm" type="password" />
            </el-form-item>
            <el-form-item label="球龄" prop="playYears">
              <el-radio-group v-model="ruleForm.playYears">
                <el-radio label="1月" value="1" />
                <el-radio label="3月" value="3" />
                <el-radio label="1年" value="12" />
                <el-radio label="5年" value="60" />
                <el-radio label="10年" value="120" />
              </el-radio-group>
            </el-form-item>
            <el-form-item label="感兴趣话题" prop="interests">
              <el-select
                v-model="ruleForm.interests"
                multiple
                placeholder="选择感兴趣话题"
              >
                <el-option
                  v-for="item in interest_options"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="验证码" prop="checkcode">
              <el-input
                v-model="ruleForm.checkcode"
                style="width: 100px; margin-right: 20px"
              /><img src="" alt="" style="width: 90px; height: 32px" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="19">
            <el-form-item style="margin-left:300px;">
              <el-button type="primary" @click="submitForm(ruleFormRef)">
                确认修还
              </el-button>
              <el-button @click="resetForm(ruleFormRef)" type="danger">重新输入</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from "vue";
import type { FormInstance, FormRules } from "element-plus";
import { ElMessage } from "element-plus";
import { Plus } from "@element-plus/icons-vue";
import type { UploadProps } from "element-plus";

interface RuleForm {
  name: string;
  telephone: string;
  email: string;
  password: string;
  confirm: string;
  playYears: number;
  interests: number[];
  checkcode: string;
}

const formSize = ref("default");
const ruleFormRef = ref<FormInstance>();
const ruleForm = reactive<RuleForm>({
  name: "liu",
  telephone: "17812345678",
  email: "123Rhhh@163.com",
  password: "123456Hh@",
  confirm: "123456Hh@",
  playYears: 2,
  interests: [],
  checkcode: "",
});
// 图像上传
const imageUrl = ref("");

const interest_options = [
  {
    value: "1",
    label: "乒乓球资讯",
  },
  {
    value: "2",
    label: "乒乓球入门",
  },
  {
    value: "3",
    label: "乒乓球交流",
  },
  {
    value: "4",
    label: "提高球技",
  },
];

const rules = reactive<FormRules<RuleForm>>({
  telephone: [
    {
      required: true,
      message: "输入手机号",
      trigger: "change",
    },
  ],
  email: [
    {
      required: true,
      message: "输入正确的邮箱地址",
      trigger: "blur",
    },
  ],
  password: [
    {
      required: true,
      message: "密码不能为空",
      trigger: "blur",
    },
  ],
  checkcode: [
    {
      required: true,
      message: "验证码不能为空",
      trigger: "blur",
    },
  ],
  playYears: [
    {
      required: true,
      message: "选择球龄",
      trigger: "blur",
    },
  ],
});

const submitForm = async (formEl: FormInstance | undefined) => {
  if (!formEl) return;
  await formEl.validate((valid, fields) => {
    if (valid) {
      console.log("submit!");
    } else {
      console.log("error submit!", fields);
    }
  });
};

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
.avatar-uploader .avatar {
  width: 178px;
  height: 178px;
  display: block;
}

#register {
  border: 1px solid black;
  background-color: white;
  padding: 20px;
}

#register #title {
  font-weight: bolder;
  font-size: 30px;
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
