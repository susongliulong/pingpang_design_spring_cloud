<template>
  <div class="clear">
    <!-- 编辑发布赛事 -->
    <div id="header">
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
    </div>
    <div id="leftBar" class="left">
      <!-- 赛事基础信息 -->
      <el-form
        ref="matchRef"
        :model="match"
        status-icon
        label-width="120px"
        size="small"
      >
        <el-form-item label="赛事标题" prop="match.title">
          <el-input
            v-model="match.match.title"
            type="text"
            autocomplete="off"
          />
        </el-form-item>
        <el-form-item label="比赛级别" prop="match.title">
          <el-input
            v-model.number="match.match.maxNumber"
            placeholder="输入人数"
            size="small"
          ></el-input>
        </el-form-item>
        <el-form-item label="比赛地址" prop="match.address">
          <el-input
            v-model="match.match.address"
            type="text"
            autocomplete="off"
          />
        </el-form-item>
        <el-form-item label="比赛描述" prop="match.desc">
          <el-input
            v-model="match.match.description"
            type="text"
            autocomplete="off"
          />
        </el-form-item>

        <el-form-item label="比赛开始时间" prop="match.matchStartTime">
          <el-date-picker
            v-model="match.match.matchStartTime"
            type="datetime"
            size="small"
          >
          </el-date-picker>
        </el-form-item>

        <el-form-item label="报名截止时间" prop="match.signUpEndTime">
          <el-date-picker
            v-model="match.match.signUpEndTime"
            type="datetime"
            size="small"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item label="最低积分要求" prop="match.minPoints">
          <el-input
            v-model.number="match.match.minPoints"
            type="text"
            autocomplete="off"
          />
        </el-form-item>
        <el-form-item label="积分奖励" prop="match.awards">
          <el-input
            v-model="match.match.awards"
            type="text"
            autocomplete="off"
          />
        </el-form-item>
        <el-form-item label="联系方式" prop="match.contact">
          <el-input
            v-model="match.match.contact"
            type="text"
            autocomplete="off"
          />
        </el-form-item>
        <el-form-item>
          <el-button @click="resetForm(matchRef)" type="danger">重置</el-button>
        </el-form-item>
      </el-form>
      <el-divider />

      <div id="matches">
        <h2>已发布赛事</h2>
        <div class="match_box" v-for="(item, index) in matches" :index="index">
          <div style="font-weight: bold">{{ item.name }}</div>
          <p>比赛时间：{{ item.time.toLocaleDateString() }}</p>
          <p>比赛场地：{{ item.address }}</p>
          <p>
            比赛最低积分要求：{{ item.minPoints }} 积分奖励：{{ item.awrards }}
          </p>
        </div>
      </div>
    </div>

    <div id="markdown" class="left">
      <MdEditor v-model="match.content"></MdEditor>
    </div>
  </div>
</template>
<script setup lang="ts">
import { onBeforeMount, ref } from "vue";
import { ElMessage, type FormInstance, type FormRules } from "element-plus";

import { MdEditor } from "md-editor-v3";
import axios from "axios";
import gatewayUrl from "@/global";

interface Match {
  matchId: bigint;
  title: string;
  minPoints: number;
  playerNumber: number;
  holder: string;
  maxNumber: number;
  awards: number;
  description: string;
  matchStartTime: Date;
  signUpEndTime: Date;
  address: string;
  contact: string;
  userId: bigint;
}

interface MatchDTO {
  match: Match;
  content: string;
}

onBeforeMount(() => {
  const userString = localStorage.getItem("user");
  if (userString != null) {
    const user = JSON.parse(userString).user;
    match.value.match.userId = user.id;
  }
})

const match = ref<MatchDTO>({ match: {} });
const matchRef = ref<FormInstance>(); // 表单信息
// 比赛信息
const matches = ref<Match[]>([]);

// 提交表单信息
const submit = () => {
  axios({
    method: "post",
    url: gatewayUrl + "/match/post",
    data: match.value,
  }).then((resp) => {
    if (resp.data.code == 200) {
      ElMessage.success(resp.data.message);
    } else {
      ElMessage.error("发布赛事失败");
    }
  });
};

// 重置表单信息
const resetForm = (formEl: FormInstance | undefined) => {
  if (!formEl) return;
  formEl.resetFields();
};
</script>
<style scoped>
#header {
  margin-bottom: 20px;
}
#leftBar {
  margin-left: -40px;
  width: 30%;
  padding: 10px;
  background-color: white;
}
#leftBar #matches {
  margin-left: 40px;
  /* display: block; */
}

#leftBar #matches .match_box {
  margin-top: 20px;
  border: 1px solid red;
  padding: 10px;
}

#markdown {
  margin-left: 20px;
  width: 70%;
}
</style>
