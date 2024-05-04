<template>
  <div class="clear" style="margin-left: 100px">
    <div id="register" class="left">
      <p id="title">
        账号信息修改<span class="right"
          ><el-button type="danger" size="default" @click="logout"
            >注销账号</el-button
          >
        </span>
      </p>
      <br />
      <el-form
        ref="dataRef"
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
                :action="gatewayUrl + '/avatar/upload'"
                :show-file-list="false"
                :on-success="handleAvatarSuccess"
                :before-upload="beforeAvatarUpload"
              >
                <img
                  v-if="ruleForm.avatar"
                  :src="ruleForm.avatar"
                  class="avatar"
                />
                <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
              </el-upload>
            </el-form-item>
          </el-col>
          <el-col :span="12" :offset="4">
            <el-form-item label="昵称" prop="name">
              <el-input v-model="ruleForm.name" />
            </el-form-item>
            <el-form-item label="手机号" prop="telephone">
              <el-input v-model="ruleForm.telephone" readonly />
            </el-form-item>
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="ruleForm.email" readonly />
            </el-form-item>
            <el-form-item label="密码" prop="password">
              <el-input v-model="ruleForm.password" type="password" />
            </el-form-item>
            <el-form-item label="确认密码" prop="confirm">
              <el-input v-model="ruleForm.confirm" type="password" />
            </el-form-item>
            <el-form-item label="球龄" prop="playYears">
              <el-radio-group v-model="ruleForm.playYears">
                <el-radio label="1月" :value="1" />
                <el-radio label="3月" :value="3" />
                <el-radio label="1年" :value="12" />
                <el-radio label="5年" :value="60" />
                <el-radio label="10年" :value="120" />
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
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="19">
            <el-form-item style="margin-left: 300px">
              <el-button type="primary" @click="submitForm(dataRef)">
                确认修还
              </el-button>
              <el-button @click="resetForm(dataRef)" type="danger"
                >重新输入</el-button
              >
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onBeforeMount } from "vue";
import type { FormInstance, FormRules } from "element-plus";
import { ElMessage, ElMessageBox } from "element-plus";
import { Plus } from "@element-plus/icons-vue";
import type { UploadProps } from "element-plus";
import axios from "axios";

import { gatewayUrl } from "@/global";
import router from "@/router";

interface RuleForm {
  id: bigint;
  name: string;
  avatar: string;
  telephone: string;
  email: string;
  password: string;
  confirm: string;
  playYears: number;
  interests: any[];
  checkcode: string;
}

const dataRef = ref<FormInstance>();
const ruleForm = ref<RuleForm>({
  id: 123,
  name: "liu",
  avatar: "",
  telephone: "17812345678",
  email: "123Rhhh@163.com",
  password: "123456Hh@",
  confirm: "123456Hh@",
  playYears: 2,
  interests: [],
  checkcode: "",
});

const interest_options = [
  {
    value: 28,
    label: "赛事快讯",
  },
  {
    value: 29,
    label: "娱乐八卦",
  },
  {
    value: 30,
    label: "明星秀",
  },
  {
    value: 31,
    label: "乒器快讯",
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
      required: false,
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

// 回显用户的个人信息
onBeforeMount(() => {
  const userString = localStorage.getItem("user");
  if (userString != null) {
    const user = JSON.parse(userString).user;
    ruleForm.value = user;
    ruleForm.value.password = "";
    axios.get(gatewayUrl + "/user/interests/" + user.id).then((resp) => {
      ruleForm.value.interests = resp.data.data;
    });
  } else {
    // 跳转到登录界面
  }
});

// 用户修改个人信息之后，提交
const submitForm = async (formEl: FormInstance | undefined) => {
  if (!formEl) return;
  await formEl.validate((valid, fields) => {
    if (valid) {
      if (ruleForm.value.password == ruleForm.value.confirm) {
        // 发送请求
        axios({
          url: gatewayUrl + "/user/update",
          method: "post",
          data: ruleForm.value,
        }).then((resp) => {
          if (resp.data.code == 200) {
            ElMessage.success("账号信息修改成功");
            router.push("/main");
          } else {
            ElMessage.error("账号信息修改错误");
          }
        });
      } else {
        ElMessage.error("两次输入的密码不一致");
      }
    } else {
      ElMessage.error("按要求重新输入");
    }
  });
};

const resetForm = (formEl: FormInstance | undefined) => {
  if (!formEl) return;
  formEl.resetFields();
};

// 图像上传成功以后，后台返回头像的名称
const handleAvatarSuccess: UploadProps["onSuccess"] = (
  response,
  uploadFile
) => {
  console.log(response, uploadFile);
  ruleForm.value.avatar =
    gatewayUrl + "/avatar/download?filename=" + response.data;
};

// 图像上传之前进行校验
const beforeAvatarUpload: UploadProps["beforeUpload"] = (rawFile) => {
  if (rawFile.type !== "image/jpeg") {
    ElMessage.error("照片格式为jpeg");
    return false;
  } else if (rawFile.size / 1024 / 1024 > 2) {
    ElMessage.error("照片大小应该小于2MB!");
    return false;
  }
  return true;
};

/**
 * 注销账号
 */
function logout() {
  ElMessageBox.confirm("确定要注销账号", "Warning", {
    confirmButtonText: "是",
    cancelButtonText: "否",
    type: "warning",
  })
    .then(() => {
      axios({
        method: "delete",
        url: gatewayUrl + "/user/logout",
        params: {
          id: ruleForm.value.id,
        },
      }).then((resp) => {
        ElMessage({
          type: resp.data.code == 200 ? "success" : "error",
          message: resp.data.message,
        });
        router.push("/main");
        localStorage.removeItem("user");
        resetForm(dataRef.value);
      });
    })
    .catch(() => {
      ElMessage({
        type: "info",
        message: "取消成功",
      });
    });
}
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
