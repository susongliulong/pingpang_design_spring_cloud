<template>
  <div class="clear" style="margin-left: 100px; margin-top: 20px">
    <div class="left" style="margin-right: 10px">
      <img
        src="../../assets/login/pingpang_avator.png"
        alt=""
        style="width: 90px; height: 90px"
      />
      <div style="position: relative; top: 240px; margin-right: 140px">
        <p style="font-size: 40px; font-weight: bolder; color: black">感受</p>
        <p style="font-size: 40px; color: black">乒乓球的魅力</p>
      </div>
    </div>

    <div id="register" class="left">
      <p id="title">账号注册</p>
      <p style="font-size: 12px; color: gray">
        已有账号，<router-link to="/login">登录</router-link>
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
        <el-form-item label="昵称" prop="name">
          <el-input v-model="ruleForm.name" />
        </el-form-item>
        <el-form-item label="手机号" prop="telephone">
          <el-input v-model="ruleForm.telephone" @blur="generateCode"/>
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
        <el-form-item label="验证码" prop="checkCode">
          <el-input
            v-model="ruleForm.checkCode"
            style="width: 100px; margin-right: 20px"
          /><img :src="checkCodeImg" alt="" style="width: 90px; height: 32px" @click="generateCode"/>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm(dataRef)">
            创建账号
          </el-button>
          <el-button @click="resetForm(dataRef)">重新输入</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive ,onBeforeMount} from "vue";
import type { FormInstance, FormRules } from "element-plus";
import { gatewayUrl } from "@/global";
import axios from "axios";

import { useRouter } from "vue-router";

interface RuleForm {
  name: string;
  telephone: string;
  email: string;
  password: string;
  confirm: string;
  playYears: number;
  interests: number[];
  checkCode: string;
}

const dataRef = ref<FormInstance>();
const ruleForm = ref<RuleForm>({
  name: "liu",
  telephone: "17812345678",
  email: "123Rhhh@163.com",
  password: "123456Hh@",
  confirm: "123456Hh@",
  playYears: 2,
  interests: [],
  checkCode: "",
});

const checkCodeImg=ref('')

const router = useRouter();

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
  playYears: [
    {
      required: true,
      message: "选择球龄",
      trigger: "blur",
    },
  ],
});

onBeforeMount(()=>{
    generateCode();
})

// 生成图片验证码
const generateCode = () => {
  checkCodeImg.value = gatewayUrl + "/code/get?number=" + Math.ceil(Math.random() * 10000000)+"&account="+ruleForm.value.telephone;
}

const submitForm = async (formEl: FormInstance | undefined) => {
  if (!formEl) return;
  await formEl.validate((valid, fields) => {
    if (valid) {
      if (ruleForm.value.password == ruleForm.value.confirm) {
        // 验证通过时发送post请求
        axios({
          url: gatewayUrl + "/user/register",
          method: "post",
          data: ruleForm.value,
        }).then((resp) => {
          if (resp.data.code == 200) {
            alert("账号注册成功");
            router.push("/login"); // 跳转到登录界面
          } else {
            alert(resp.data.message);
          }
        });
      } else {
          alert("两次密码输入不相同，重新输入");
          return;
      }
    } else {
      alert("请按要求重新输入");
    }
  });
};

const resetForm = (formEl: FormInstance | undefined) => {
  if (!formEl) return;
  formEl.resetFields();
};
</script>

<style>
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
