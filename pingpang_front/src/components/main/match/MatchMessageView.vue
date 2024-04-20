<template>
  <div class="match-box">
    <el-row>
      <el-col :span="8">
        <h4>{{ match.address }}</h4>
      </el-col>

      <el-col :span="4">
        <span style="background-color: #ffe6d2; font-size: 12px"
          ><span style="">报名人数限制：</span
          ><span>{{ match.maxNumber }}</span></span
        >
      </el-col>

      <el-col :span="4">
        <span style="color: white; background-color: #c7000b; font-size: 12px"
          ><span>最低积分要求：</span><span>{{ match.minPoints }}</span></span
        >
      </el-col>

      <el-col :span="4">
        <h3 style="color: #c7000b">
          积分奖励：<span>{{ match.awards }}</span>
        </h3>
      </el-col>
    </el-row>

    <el-row>
      <el-col :span="14">
        <span style="font-size: 12px">{{ match.description }}</span>
      </el-col>

      <el-col :span="4" style="background-color: #f2f5fc" align="center">
        <span>
          <p style="font-size: large; font-weight: bold">
            <el-input
              v-model="match.playerNumber"
              size="small"
              clearable
              readonly
              style="border: none; font-size: large; font-weight: bold"
            />
          </p>
          <p style="font-weight: bold">参赛选手</p>
        </span>
      </el-col>
    </el-row>
    <el-row>
      <el-col :offset="8" :span="6" style="font-size: 12px; font-weight: bold">
        <span>报名截止时间：</span>
        <span>{{ match.signUpEndTime.toString().substring(0, 10) }}</span>
      </el-col>

      <el-col :span="4">
        <!-- v-if -->
        <span v-if="signUp">已报名</span>
        <el-button v-else
          type="danger"
          size="default"
          @click="ensure(match)"
          >确认报名</el-button
        >
        <p style="font-size: 12px">
          剩余{{
            Math.floor(
              (new Date(match.signUpEndTime).getTime() - new Date().getTime()) /
                (24 * 60 * 60 * 1000)
            )
          }}
          天
        </p>
      </el-col>
    </el-row>
  </div>
  <md-preview v-model="content" style="margin-top: 20px; width: 1000px">
  </md-preview>
</template>
<script setup lang="ts">
import { ref, onBeforeMount } from "vue";
import axios from "axios";
import gatewayUrl from "@/global";
import { useRoute } from "vue-router";
import { ElMessage } from "element-plus";
import { MdPreview } from "md-editor-v3";
import { useRouter } from "vue-router";

// 赛事信息
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
}
const match = ref<Match>({});
const route = useRoute();
const router = useRouter();
const signUp = ref<boolean>();
onBeforeMount(() => {
  const matchString = localStorage.getItem("match");
  if (matchString != null) {
    match.value = JSON.parse(matchString);
    getMatchMessage(match.value.matchId);
    isSignUp(match.value.matchId);
  }
});

const content = ref("");

// 获取赛事的详细信息
function getMatchMessage(matchId: bigint) {
  axios.get(gatewayUrl + "/match/matchMessage/" + matchId).then((resp) => {
    content.value = resp.data.data;
  });
}

// 确认报名
function ensure(match: Match) {
  axios({
    method: "post",
    url: gatewayUrl + "/signUp/sign",
    data: {
      userId: route.query.userId,
      matchId: match.matchId,
    },
  }).then((resp) => {
    if (resp.data.code == 200) {
      ElMessage.success("报名成功");
      updatePlayerNumber();
      router.push("/main/match");
    } else {
      ElMessage.error(resp.data.message);
    }
  });
}

// 对于已经报名的比赛，显示已经报名
async function isSignUp(matchId: bigint) {
  const { data } = await axios.get(gatewayUrl + "/signUp/isSignUp", {
    params: {
      userId: route.query.userId,
      matchId: matchId,
    },
  });
  signUp.value = data.data;
}

function updatePlayerNumber() {
  match.value.playerNumber++;
}
</script>
<style scoped></style>
