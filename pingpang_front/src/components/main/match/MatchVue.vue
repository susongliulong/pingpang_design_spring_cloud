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
        @click="saveDraft"
        style="margin-left: 20px"
        >存为草稿</el-button
      >
    </div>

    <div id="main" class="clear">
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
            <el-button @click="resetForm(matchRef)" type="danger"
              >重置</el-button
            >
          </el-form-item>
        </el-form>
        <el-divider />
      </div>

      <div id="markdown" class="right">
        <MdEditor v-model="match.content" @onUploadImg="onUploadImg"></MdEditor>
      </div>
    </div>

    <div id="footer" class="clear">
      <div id="matches" class="left">
        <h2>已发布赛事</h2>
        <div class="match_box" v-for="(item, index) in matches" :index="index">
          <div style="font-weight: bold">赛事描述：{{ item.description }}</div>
          <p>
            比赛时间：{{
              item.matchStartTime.substring(0, 10) +
              " " +
              item.matchStartTime.substring(11)
            }}
          </p>
          <p>比赛场地：{{ item.address }}</p>
          <p>
            比赛最低积分要求：{{ item.minPoints }} 积分奖励：{{ item.awards }}
          </p>
          <p>报名人数：{{ item.playerNumber }}</p>
          <div>
            <el-button
              type="primary"
              size="default"
              @click="generate(item.matchId)"
              >生成小组赛名单</el-button
            >
          </div>
        </div>
      </div>

      <!-- 已报名参赛 -->
      <div id="signups" class="right">
        <h2>已报名赛事</h2>
        <div
          class="sign_up_box"
          v-for="(signUp, index) in signUps"
          :index="index"
        >
          <p>赛事描述：{{ signUp.match.description }}</p>
          <p>
            比赛时间:{{
              signUp.match.matchStartTime.substring(0, 10) +
              " " +
              signUp.match.matchStartTime.substring(11)
            }}&nbsp;&nbsp;&nbsp;报名时间：{{
              signUp.signUp.signUpTime.substring(0, 10)
            }}
          </p>
          <p>
            参赛人数：{{
              signUp.match.playerNumber
            }}&nbsp;&nbsp;&nbsp;参赛状态：<span v-if="signUp.signUp.state == 1"
              >已报名未参赛</span
            ><span v-else> 参赛</span>
          </p>
          <p v-if="signUp.signUp.state == 2">
            比赛排名：{{ signUp.signUp.rankness }} /
            {{ signUp.match.playerNumber }}
          </p>
          <p v-if="signUp.signUp.state == 2">积分奖励：{{ signUp.awards }}</p>
          <div ><el-button type="danger" size="default" @click="cancelSignUp(signUp.match.matchId)">取消报名</el-button>
          </div>
        </div>
      </div>
    </div>

    <!-- 生成小组赛选手并且查看回显 -->
    <el-dialog v-model="dialogTableVisible" title="小组分组名单" width="800">
      <div v-for="(group, index) in groups" :index="index">
        <p>第{{ index+1 }}个小组分组情况：</p>
        <el-table :data="group">
          <el-table-column property="nickname" label="选手昵称" width="150" />
          <el-table-column property="contact" label="联系方式" width="200" />
        </el-table>
      </div>
      <div id="footer" style="text-align: center"><el-button type="primary" size="default" @click="dialogTableVisible=false">确认</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script setup lang="ts">
import { onBeforeMount, ref } from "vue";
import {
  ElMessage,
  type FormInstance,
  type FormRules,
  ElMessageBox,
} from "element-plus";

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

interface SignUpMessage {
  userId: bigint;
  matchId: bigint;
  signUpTime: String;
  state: number;
  rankness: number; // 排名
  contact: String; // 联系方式
}

interface SignUp {
  match: Match;
  signUp: SignUpMessage;
  awards: number;
}

interface MatchDTO {
  match: Match;
  content: string;
}

// 小组赛分组
const groups = ref<SignUp[]>([]);
let user: any;

const match = ref<MatchDTO>({ match: {} });
const matchRef = ref<FormInstance>(); // 表单信息
// 比赛信息
const matches = ref<Match[]>([]);

// 报名信息
const signUps = ref<Object[]>([]);
const dialogTableVisible = ref(false);
onBeforeMount(() => {
  const userString = localStorage.getItem("user");
  if (userString != null) {
    user = JSON.parse(userString).user;
    const matchString = localStorage.getItem("currentMatch");
    if (matchString != null) {
      match.value = JSON.parse(matchString);
    }
    match.value.match.userId = user.id;
    getMatchesByUserId(user.id);
    getSignUps(user.id);
  }
});

/**
 * 取消报名
 */

function cancelSignUp(matchId: bigint) {
  axios({
    method:'delete',
    url: gatewayUrl + '/signUp/cancel',
    params: {
      matchId: matchId,
      userId:user.id
    }
  }).then(resp => {
    ElMessage.success(resp.data.message);
    getSignUps(user.id);
  })
}

function getSignUps(userId: bigint) {
  axios({
    method: "get",
    url: gatewayUrl + "/signUp/signUps",
    params: {
      userId: userId,
    },
  }).then((resp) => {
    signUps.value = resp.data.data;
  });
}

// 提交表单信息
const submit = () => {
  ElMessageBox.confirm(
    "你确定要发布赛事，赛事一旦发布需要练习管理员才能删除",
    "Warning",
    {
      confirmButtonText: "是",
      cancelButtonText: "否",
      type: "warning",
    }
  ).then(() => {
    axios({
      method: "post",
      url: gatewayUrl + "/match/post",
      data: match.value,
    }).then((resp) => {
      ElMessage({
        type: resp.data.code == 200 ? "success" : "error",
        message: resp.data.message,
      });
      // 查询已经发布的赛事
    });
  });
};

// 重置表单信息
const resetForm = (formEl: FormInstance | undefined) => {
  if (!formEl) return;
  formEl.resetFields();
};

// 上传照片
// markdown编辑器点击上传图片
const onUploadImg = async (files, callback) => {
  const res = await Promise.all(
    files.map((file) => {
      return new Promise((rev, rej) => {
        const form = new FormData();
        form.append("file", file);

        axios
          .post(gatewayUrl + "/match/uploadImg?userId=" + user.id, form, {
            headers: {
              "Content-Type": "multipart/form-data",
            },
          })
          .then((res) => rev(res))
          .catch((error) => rej(error));
      });
    })
  );
  // Approach 1
  callback(
    res.map(
      (item) =>
        gatewayUrl +
        "/match/downloadImg?fileName=" +
        item.data.data +
        "&userId=" +
        user.id
    )
  );
};
// 查询已经发布赛事信息
function getMatchesByUserId(id: bigint) {
  axios({
    method: "get",
    url: gatewayUrl + "/match/matches?userId=" + id,
  }).then((resp) => {
    matches.value = resp.data.data;
  });
}

// 存为草稿
function saveDraft() {
  localStorage.setItem("currentMatch", JSON.stringify(match.value));
  ElMessage.success("存为草稿成功");
}

// 每隔5分钟保存一次
setInterval(() => {
  if (match.value.content != null) {
    saveDraft();
  }
}, 5 * 60 * 1000);

// 生成小组赛名单
function generate(matchId: bigint) {
  axios.get(gatewayUrl + "/signUp/generate?matchId=" + matchId).then((resp) => {
    groups.value = resp.data.data;
    if(groups.value!=null){
      dialogTableVisible.value = true;
      ElMessage.success("小组赛分组名单分组成功");
    }
  });
}
</script>
<style scoped>
#header {
  margin-bottom: 20px;
}
#leftBar {
  /* padding: 10px; */
  margin-left: -20px;
  background-color: white;
}

#footer #matches .match_box {
  margin-top: 20px;
  max-width: 600px;
  border: 1px solid red;
  padding: 10px;
}

#footer .sign_up_box {
  border: 1px solid red;
  padding: 10px;
  margin-top: 20px;
}

#markdown {
  margin-left: 20px;
  width: 70%;
}
</style>
