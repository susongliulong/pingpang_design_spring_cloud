<template>
    <div class="clear">

        <div class="left" style="margin-right: 10px;">
            <img src="../../assets/login/pingpang_avator.png" alt="" style="width: 90px; height: 90px;">
            <div style="position: relative;top: 240px;margin-right: 140px;">
                <p style="font-size: 40px; font-weight: bolder;color: black;">感受</p>
                <p style="font-size: 40px;color:black">乒乓球的魅力</p>
            </div>
        </div>

        <div id="register" class="left">
            <p id="title">账号注册</p>
            <p style="font-size: 12px;color: gray">已有账号，<a href="" style="font-size: 16px;">登录</a></p>
            <br>
            <el-form ref="dataRef" :model="ruleForm" :rules="rules" label-width="auto" class="demo-ruleForm"
                size="small" status-icon>
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
                    <el-select v-model="ruleForm.interests" multiple placeholder="选择感兴趣话题">
                        <el-option v-for="item in interest_options" :key="item.value" :label="item.label"
                            :value="item.value" />
                    </el-select>
                </el-form-item>
                <el-form-item label="验证码" prop="checkcode">
                    <el-input v-model="ruleForm.checkcode" style="width: 100px;margin-right: 20px;" /><img src="" alt=""
                        style="width: 90px;height: 32px;">
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="submitForm(ruleFormRef)">
                        创建账号
                    </el-button>
                    <el-button @click="resetForm(ruleFormRef)">重新输入</el-button>
                </el-form-item>
            </el-form>
        </div>

    </div>
</template>

<script setup lang="ts">
import { ref, reactive } from "vue"
import type { FormInstance, FormRules } from 'element-plus'

interface RuleForm {
    name: string,
    telephone: string,
    email: string,
    password: string,
    confirm: string,
    playYears: number,
    interests: number[],
    checkcode: string
}

const formSize = ref('default')
const ruleFormRef = ref<FormInstance>()
const ruleForm = reactive<RuleForm>({
    name: 'liu',
    telephone: '17812345678',
    email: '123Rhhh@163.com',
    password: "123456Hh@",
    confirm: "123456Hh@",
    playYears: 2,
    interests: [],
    checkcode: ""
})

const interest_options = [
    {
        value: "1",
        label: "乒乓球资讯"
    },
    {
        value: "2",
        label: "乒乓球入门"
    },
    {
        value: "3",
        label: "乒乓球交流"
    },
    {
        value: "4",
        label: "提高球技"
    },
]

const rules = reactive<FormRules<RuleForm>>({
    telephone: [
        {
            required: true,
            message: '输入手机号',
            trigger: 'change',
        }
    ],
    email: [
        {
            required: true,
            message: '输入正确的邮箱地址',
            trigger: 'blur',
        }
    ],
    password: [
        {
            required: true,
            message: '密码不能为空',
            trigger: 'blur',
        }
    ],
    checkcode: [
        {
            required: true,
            message: '验证码不能为空',
            trigger: 'blur',
        }
    ],
    playYears: [
        {
            required: true,
            message: '选择球龄',
            trigger: 'blur',
        }
    ]
})

const submitForm = async (formEl: FormInstance | undefined) => {
    if (!formEl) return
    await formEl.validate((valid, fields) => {
        if (valid) {
            console.log('submit!')
        } else {
            console.log('error submit!', fields)
        }
    })
}

const resetForm = (formEl: FormInstance | undefined) => {
    if (!formEl) return
    formEl.resetFields()
}


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
