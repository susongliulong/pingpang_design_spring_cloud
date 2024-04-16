<template>
  <div id="content">
    <div id="tabs">
      <!-- 显示导航条 -->
      <el-form
        :inline="true"
        :model="searchMessage"
        class="search-tabs"
        size="small"
      >
        <el-form-item label="关键词">
          <el-input v-model="searchMessage.keyWord" placeholder="输入关键词" />
        </el-form-item>
        <el-form-item label="比赛级别" style="width: 200px">
          <el-select
            v-model.number="searchMessage.level"
            placeholder="比赛级别"
          >
            <el-option label="入门级" value="入门级" />
            <el-option label="业余级" value="业余级" />
            <el-option label="介于业余和专业" value="介于业余和专业" />
            <el-option label="专业级" value="专业级" />
            <el-option label="大师级" value="大师级" />
          </el-select>
        </el-form-item>
        <el-form-item label="最低积分要求" style="width: 120px">
          <el-input v-model="searchMessage.minPoints" />
        </el-form-item>
        <el-form-item label="比赛开始时间" class="date-picker">
          <el-date-picker v-model="searchMessage.matchStartTime" type="date" />
        </el-form-item>
        <el-form-item label="报名截止时间" class="date-picker">
          <el-date-picker v-model="searchMessage.signupEndTime" type="date" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submit">查询</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div id="match-box">
      <div class="match-box" v-for="(match, index) in matches" :index="index">
        <el-row>
          <el-col :span="8">
            <h4>{{ match.title }}</h4>
          </el-col>

          <el-col :span="4">
            <span style="background-color: #ffe6d2;font-size: 12px"><span style="">比赛级别：</span><span>{{ match.level }}</span></span>
          </el-col>

          <el-col :span="4">
            <span style="color:white;background-color: #c7000b;font-size: 12px"><span>最低积分要求：</span><span>{{ match.minPoints }}</span></span>
          </el-col>

          <el-col :span="4">
            <h3 style="color: #c7000b;">积分奖励：<span>{{ match.points }}</span></h3>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="14">
            <span style="font-size: 12px;">{{ match.desc }}</span>
          </el-col>

          <el-col :span="4" style="background-color: #f2f5fc;" align="center">
            <span>
              <p style="font-size: large;font-weight: bold">{{ match.playerNumber }}</p>
              <p style="font-weight: bold">参赛选手</p>
            </span>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="6" style="font-size:12px">
            <span>举办方：</span>
            <span>{{ match.holder }}</span>
          </el-col>

          <el-col :offset="8" :span="6" style="font-size:12px;font-weight: bold;">
            <span>报名截止时间：</span>
            <span>{{ match.signupEndTime }}</span>
          </el-col>

          <el-col :span="4">
            <el-button type="danger" size="default" @click=""
              >立即报名</el-button
            >
            <p style="font-size:12px;">剩余6天6小时</p>
          </el-col>
        </el-row>
      </div>
    </div>

    <div id="match-pagination">
      <!-- 赛事信息导航条 -->
    </div>
  </div>
</template>
<script setup lang="ts">
import { ref, reactive } from "vue";
// 搜索关键词信息
interface SearchMessage {
  keyWord: string;
  level: number; // 比赛级别
  minPoints: number; // 最低积分要求
  matchStartTime: Date; // 比赛开始时间
  signupEndTime: Date; // 报名结束时间
}
// 赛事信息
interface Match {
  title: string;
  minPoints: number;
  playerNumer: number;
  holder: string;
  level: string;
  points: number;
  desc: string;
  matchStartTime: Date;
  signupEndTime: Date;
  matchLocation: string;
}
const searchMessage = ref<SearchMessage>({});
const matches = ref<Match[]>([
  {
    title: "一年一度新生杯决赛",
    minPoints: 0,
    playerNumber: 150,
    holder: "计算机学院",
    level: "新手",
    points: 12,
    desc: "新生杯决赛是每年新生开学之后举行的一场跨学院的乒乓球联赛",
    matchStartTime: new Date(),
    signupEndTime: new Date(),
    matchLocation: "广东工业大学计算机学院",
  },
  {
    title: "生物医药学院院内部选拔赛",
    minPoints: 0,
    playerNumber: 60,
    holder: "生物医药学院",
    level: "新手",
    points: 12,
    desc: "生物医药学院内部选拔赛旨在筛选出乒乓球运动种子选手参加后续赛事",
    matchStartTime: new Date(),
    signupEndTime: new Date(),
    matchLocation: "广东工业大学生物医药学院",
  },
]);

// 根据关键词信息查询
const submit = () => {};
</script>
<style scoped>
#content {
  position: absolute;
  margin-top: 10px;
}
.match-box {
  padding:20px;
  background-color: white;
  border: 1px solid red;
  margin-bottom: 10px;
}
.match-box .el-row{
  margin-bottom: 15px;
}
.el-input {
  width: 120px;
}
.date-picker {
  width: 200px;
}
</style>
