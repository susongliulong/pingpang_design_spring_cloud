<template>
    <div class="content center">
        <h1>爱乒乓</h1>
        <el-input v-model="account" style="margin-bottom: 20px;" placeholder="请输入邮箱或者手机号" @change=""></el-input>
        <el-input v-model="password" style="margin-bottom: 20px;" type="password" placeholder="请输入账号密码" clearable
            @change=""></el-input>

        <el-row style="margin-bottom: 20px;">
            <el-col :span="12">
                <el-input v-model="checkCode" style="width: 200px;" placeholder="请输入验证码" @change="" />
            </el-col>
            <el-col :offset="7" :span="4">
                <img src="" alt="" style="width: 70px; height: 40px;">
            </el-col>
        </el-row>
        <input type="checkbox" style="width: 25px;" v-model="agree">
        我已经阅读并且同意<a href="otherUrl">爱乒乓协议</a>
        </input>
        <br>
        <span v-if="agree==0" style="color: red; font-size: 12px;">请先勾选同意协议</span>
        <br>
        <el-button type="primary" style="width: 350px;" @click="">登录</el-button>
        <router-link to="/register">没有账号，注册</router-link>
        <br>
    </div>
</template>
<script setup lang="ts">
import {ref,watch} from "vue"

// 模型数据
const account = ref('');
const password = ref('');
const checkCode = ref('');
const agree = ref(-1);





</script>
<style scoped>
.content{
    text-align: center;
    width: 350px;
    height: 338px;
    padding: 20px;
    background-color: #ffffff;
    /* border: 1px solid black; */
}

.el-input {
    --el-input-bg-color: #e8f0fe;
    --el-input-height:40px;
}



</style>
