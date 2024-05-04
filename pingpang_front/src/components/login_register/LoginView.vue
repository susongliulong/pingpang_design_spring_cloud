<template>
    <div class="content center">
        <h1>爱乒乓</h1>
        <el-input v-model="account" style="margin-bottom: 20px;" placeholder="请输入邮箱或者手机号" @change="" @blur="generateCode"></el-input>
        <el-input v-model="password" style="margin-bottom: 20px;" type="password" placeholder="请输入账号密码"  show-password clearable
            @change=""></el-input>

        <el-row style="margin-bottom: 20px;">
            <el-col :span="12">
                <el-input v-model="checkCode" style="width: 200px;" placeholder="请输入验证码" @change="" clearable/>
            </el-col>
            <el-col :offset="7" :span="4">
                <img :src="checkCodeImg" alt="" style="width: 90px;height:40px;" @click="generateCode">
            </el-col>
        </el-row>
        <input type="checkbox" style="width: 25px;" v-model="agree">
        我已经阅读并且同意<a :href="gatewayUrl+'/user/view/privacy'">爱乒乓协议</a>
        </input>
        <br>
        <span v-if="agree==0" style="color: red; font-size: 12px;">请先勾选同意协议</span>
        <br>
        <el-button type="primary" style="width: 350px;" @click="login">登录</el-button>
        <router-link to="/register">没有账号，注册</router-link>
        <br>
    </div>
</template>
<script setup lang="ts">
import { ref, onBeforeMount } from "vue"
import axios from "axios"
import { gatewayUrl } from "@/global"
import { useRouter } from "vue-router";
import { ElMessage,ElMessageBox } from "element-plus";
import JSCrypt from 'jsencrypt';


// 模型数据
const account = ref('1734563');
const password = ref('123456');
const checkCode = ref('');
const agree = ref(-1);
const checkCodeImg = ref('');


// 路由数据
const router=useRouter();

onBeforeMount(()=>{
    generateCode();
})

// 生成图片验证码
const generateCode = () => {
    checkCodeImg.value = gatewayUrl + "/code/get?number=" + Math.ceil(Math.random() * 1000000 / 10) + "&account=" + account.value;
}

// 登录验证
const login = () => {
    if (agree.value <= 0) {
        agree.value = 0;
        return;
    } else {
        const encrypt = new JSCrypt();
        encrypt.setPublicKey("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDNNFmwudkEwWXM63ClWIvxcyPxU6cX2PP54Gho2zVxN2XRDr88jUybbHn3HgwELF7E1e/5YDbz+b2E1I0+R7qlXw8WVvQpcPGeCGvb6N9HrP95bjJQ/RvtsEEsqhBATxKvkV5lXOKvRApPyh05IpwHXqhfsn/1LvG7Rthx6qHgVwIDAQAB");
        const encryptedPassword= encrypt.encrypt(password.value);
        axios({
        url:gatewayUrl+'/user/login',
        method: 'get',
        params: {
            account: account.value,
            password: encryptedPassword,
            checkCode: checkCode.value,
            agree: agree.value == true?1:0
        }
    }).then(resp => {

        // 根据登录结果进行跳转
        if (resp.data.code == 200) {
            ElMessage({
                type: 'success',
                message:resp.data.message
            });
            const user = resp.data.data;
            localStorage.setItem("user", JSON.stringify(user));
            router.push("/main")
        } else {
            ElMessage({
                type: 'warning',
                message:resp.data.message
            })
        }
    })
    }
}


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
