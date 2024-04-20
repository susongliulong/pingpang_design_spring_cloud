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
        <el-form-item label="最低参赛人数" style="width: 200px">
          <el-select
            v-model.number="searchMessage.playerNumber"
            placeholder="最低参赛人数"
          >
            <el-option label="10" :value="10" />
            <el-option label="20" :value="20" />
            <el-option label="40" :value="40" />
            <el-option label="60" :value="60" />
            <el-option label="80" :value="80" />
            <el-option label="100" :value="100" />
          </el-select>
        </el-form-item>
        <el-form-item label="最低积分要求" style="width: 120px">
          <el-input v-model.number="searchMessage.minPoints" />
        </el-form-item>
        <el-form-item label="比赛开始时间" class="date-picker">
          <el-date-picker v-model="searchMessage.matchStartTime" type="date" />
        </el-form-item>
        <el-form-item label="报名截止时间" class="date-picker">
          <el-date-picker v-model="searchMessage.signUpEndTime" type="date" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submit">查询</el-button>
        </el-form-item>
      </el-form>
    </div>
    <el-pagination
      background
      layout="prev, pager, next"
      :total="pages"
      :hide-on-single-page="true"
      :page-size="10"
      :small="true"
      @current-change="getMatchesByPage"
      style="margin-bottom: 10px"
    />
    <div id="match-box">
      <el-scrollbar style="height: calc(100vh - 60px)">
        <div class="match-box" v-for="(match, index) in matches" :index="index" @click="signUp(match.match)">
          <el-row>
            <el-col :span="8">
              <h4>{{ match.match.address }}</h4>
            </el-col>

            <el-col :span="4">
              <span style="background-color: #ffe6d2; font-size: 12px"
                ><span style="">报名人数限制：</span
                ><span>{{ match.match.maxNumber }}</span></span
              >
            </el-col>

            <el-col :span="4">
              <span
                style="color: white; background-color: #c7000b; font-size: 12px"
                ><span>最低积分要求：</span
                ><span>{{ match.match.minPoints }}</span></span
              >
            </el-col>

            <el-col :span="4">
              <h3 style="color: #c7000b">
                积分奖励：<span>{{ match.match.awards }}</span>
              </h3>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="14">
              <span style="font-size: 12px">{{ match.match.description }}</span>
            </el-col>

            <el-col :span="4" style="background-color: #f2f5fc" align="center">
              <span>
                <p style="font-size: large; font-weight: bold">
                  {{ match.match.playerNumber }}
                </p>
                <p style="font-weight: bold">参赛选手</p>
              </span>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="6" style="font-size: 12px">
              <span>发起者：</span>
              <span>{{ match.holder }}</span>
            </el-col>

            <el-col
              :offset="8"
              :span="6"
              style="font-size: 12px; font-weight: bold"
            >
              <span>报名截止时间：</span>
              <span>{{
                match.match.signUpEndTime.toString().substring(0, 10)
              }}</span>
            </el-col>

            <el-col :span="4">
              <span v-if="match.signUp">已报名</span>
              <el-button v-else
                type="danger"
                size="default"
                @click="signUp(match.match)"
                >立即报名</el-button
              >
              <p style="font-size: 12px">
                剩余{{
                  Math.floor(
                    (new Date(match.match.signUpEndTime).getTime() -
                      new Date().getTime()) /
                      (24 * 60 * 60 * 1000)
                  )
                }}
                天
              </p>
            </el-col>
          </el-row>
        </div>
      </el-scrollbar>
    </div>

    <div id="match-pagination">
      <!-- 赛事信息导航条 -->
    </div>
  </div>
</template>
<script setup lang="ts">
import { ref, onBeforeMount } from "vue";
import axios from "axios";
import { ElScrollbar, ElPagination, ElMessage } from "element-plus";
import gatewayUrl from "@/global";
import { useRouter } from "vue-router";
// 搜索关键词信息
interface SearchMessage {
  userId: bigint;
  keyWord: string;
  playerNumber: number; // 比赛级别
  minPoints: number; // 最低积分要求
  matchStartTime: Date; // 比赛开始时间
  signUpEndTime: Date; // 报名结束时间
}
// 赛事信息
interface Match {
  id: bigint;
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
}

interface MatchVo {
  match: Match;
  holder: String;
  signUp: boolean;
}

const searchMessage = ref<SearchMessage>({});
const matches = ref<MatchVo[]>([]);
const pages = ref<number>();
let user: any;
const router = useRouter();

// 根据关键词信息查询
const submit = () => {
  getMatchesByPage(1);
};

onBeforeMount(() => {
  const userString = localStorage.getItem("user");
  if (userString != null) {
    user = JSON.parse(userString).user;
    searchMessage.value.userId = user.id;
  }
  getMatchesByPage(1);
});

function getMatchesByPage(value: number) {
  axios({
    method: "post",
    url: gatewayUrl + "/match/pages",
    data: searchMessage.value,
  }).then((resp) => {
    pages.value = resp.data.data;
  });

  axios({
    method: "post",
    url: gatewayUrl + "/match/matchMessage/" + value,
    data: searchMessage.value,
  }).then((resp) => {
    matches.value = resp.data.data;
  });
}

// 报名参加比赛
function signUp(match: Match) {
  if (match.maxNumber - match.playerNumber < 1) {
    ElMessage.error("报名人数已满，报名失败");
  } else if (user.points < match.minPoints) {
    ElMessage.error("积分低于最低积分条件");
  } else {
    // 跳转到报名详情页
    localStorage.setItem("match", JSON.stringify(match));
    router.push({
      path: '/main/matchMessage',
      query: {
        userId: user.id
      }
    });
  }
}
</script>
<style scoped>
#content {
  position: absolute;
  margin-top: 10px;
}
.match-box {
  padding: 20px;
  background-color: white;
  border: 1px solid red;
  margin-bottom: 10px;
}
.match-box .el-row {
  margin-bottom: 15px;
}
.el-input {
  width: 120px;
}
.date-picker {
  width: 200px;
}
</style>
