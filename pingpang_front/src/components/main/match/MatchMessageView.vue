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
            {{ match.playerNumber }}
          </p>
          <p style="font-weight: bold">参赛选手</p>
        </span>
      </el-col>
    </el-row>
    <br />
    <el-row>
      <el-col :offset="8" :span="6" style="font-size: 12px; font-weight: bold">
        <span>报名截止时间：</span>
        <span>{{ match.signUpEndTime.toString().substring(0, 10) }}</span>
      </el-col>

      <el-col :span="4">
        <!-- v-if -->
        <span v-if="signUp">已报名</span>
        <el-button v-else type="danger" size="default" @click="confirmSignUp"
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

  <!-- 确认报名之后显示信息确认框 -->
  <el-dialog v-model="dialogFormVisible" title="确认报名信息" width="500" show-off="false">
    <el-form :model="match">
      <el-form-item label="赛事名称">
        <el-input v-model="match.description" autocomplete="off" readonly />
      </el-form-item>

      <el-form-item label="比赛地址">
        <el-input v-model="match.address" autocomplete="off" readonly />
      </el-form-item>

      <el-form-item label="比赛时间">
        <el-input v-model="match.matchStartTime" autocomplete="off" readonly />
      </el-form-item>

      <el-form-item label="选手昵称">
        <el-input v-model="nickname" autocomplete="off" />
      </el-form-item>

      <el-form-item label="选手联系方式">
        <el-input v-model="contact" autocomplete="off" />
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="dialogFormVisible = false">关闭</el-button>
        <el-button type="primary" @click="ensure(match)"> 确认报名 </el-button>
      </div>
    </template>
  </el-dialog>
</template>
<script setup lang="ts">
import { ref, onBeforeMount } from "vue";
import axios from "axios";
import gatewayUrl from "@/global";
import { useRoute } from "vue-router";
import { ElMessage, ElMessageBox } from "element-plus";
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

// 确认报名之后，选手的昵称和联系方式
const contact = ref("");
const nickname = ref("");
let user: any;
function getUserById(id: bigint) {
  axios.get(gatewayUrl + "/user/getUserById?id=" + id).then((resp) => {
    nickname.value = resp.data.data.name;
    contact.value = resp.data.data.telephone;
    user.value = resp.data.data;
  });
}

onBeforeMount(() => {
  const matchString = localStorage.getItem("match");
  const userString = localStorage.getItem("user");
  if (matchString != null && userString != null) {
    match.value = JSON.parse(matchString);
    user = JSON.parse(userString).user;
    // 根据user的id查询电话号码
    getUserById(user.id);
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
// 选手确认报名

function confirmSignUp() {
  // 查看选手上次填写问卷时间，并判断选手是否需要更新问卷
  axios
    .get(gatewayUrl + "/userAbility/isUpdateUserAbility?userId=" + user.id)
    .then((resp) => {
      const result = resp.data.data;
      // 提示选手更新问卷
      if (result == 0) {
        ElMessage.info("请先更新个人能力值");
        // 用户首次参加比赛，需要更新能力值
        router.push({
          path: '/main/user_ability',
          query: {
            firstJoin:1
          },
        })
      } else if (result == 1) {
        ElMessageBox.confirm(
          "距离上一次更新个人能力数值已过去7天，是否更新个人能力值？",
          "Warning",
          {
            confirmButtonText: "是",
            cancelButtonText: "否",
            type: "warning",
          }
        )
          .then(() => {
            // 跳转到更新界面进行更新
            ElMessage.info("请先更新个人能力值");
            router.push("/main/user_ability");
          })
          .catch((res) => {
            dialogFormVisible.value = true;
          });
      } else {
        dialogFormVisible.value = true;
      }
    });
}

/**
 * 确认报名
 * @param match 选择报名的赛事
 */
function ensure(match: Match) {
  axios({
    method: "post",
    url: gatewayUrl + "/signUp/sign",
    data: {
      userId: route.query.userId,
      matchId: match.matchId,
      nickname: nickname.value,
      contact: contact.value,
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
// 确认报名之后显示对话框
const dialogFormVisible = ref(false);
</script>
<style scoped></style>
