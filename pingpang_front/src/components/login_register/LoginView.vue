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
                <img :src="checkCodeImg" alt="" style="width: 90px;height:40px;" @click="generateCode">
            </el-col>
        </el-row>
        <input type="checkbox" style="width: 25px;" v-model="agree">
        我已经阅读并且同意<a href="otherUrl">爱乒乓协议</a>
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
import { gatewayUrl, userAccountUrl } from "@/global"
import { useRouter } from "vue-router";


// 模型数据
const account = ref('19860205976');
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
    
    axios({
        url:gatewayUrl+'/code/get',
        method: 'get',
        responseType:'blob',
    }).then((resp) => {
        const blob = new Blob([resp.data], { type: resp.data.type }); // 保存文件流为 Blob
        const imageUrl = URL.createObjectURL(blob); // 转换 Blob 为 URL
        checkCodeImg.value = imageUrl;
    })
}

// 登录验证
const login = () => {
    if (agree.value <= 0) {
        return;
    } else {
        axios({
        url:gatewayUrl+'/user/login',
        method: 'get',
        params: {
            account: account.value,
            password: password.value,
            checkCode: checkCode.value,
            agree:agree.value == true?1:0
        }
    }).then(resp => {
        alert(resp.data.message);
        if (resp.data.code == 200) {
            const user = resp.data.data;
            localStorage.setItem("user", JSON.stringify(user));
            router.push("/main")
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
