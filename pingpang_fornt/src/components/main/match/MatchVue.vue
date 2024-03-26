<template>
  <div class="clear">
    <!-- 编辑发布赛事 -->
    <div id="header">
      <!-- 赛事所属分类 -->
      <el-select v-model="category" placeholder="选择分类" style="width: 150px">
        <el-option label="入门级别" value="入门级别"></el-option>
        <el-option label="业余级别" value="业余级别"></el-option>
        <el-option label="专业级别" value="专业级别"></el-option>
        <el-option label="介于入门和专业" value="介于入门和专业"></el-option>
      </el-select>

      <el-button
        type="primary"
        size="default"
        @click=""
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
        <el-form-item label="赛事标题" prop="basicInformation.title">
          <el-input
            v-model="match.basicInformation.title"
            type="text"
            autocomplete="off"
          />
        </el-form-item>
        <el-form-item label="比赛级别" prop="basicInformation.title">
          <el-select v-model="match.level" placeholder="选择级别" size="small">
            <el-option label="入门级别" value="入门级别"></el-option>
            <el-option label="业余级别" value="业余级别"></el-option>
            <el-option label="专业级别" value="专业级别"></el-option>
            <el-option
              label="介于入门和专业"
              value="基于入门和专业"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="比赛地址" prop="address">
          <el-input v-model="match.address" type="text" autocomplete="off" />
        </el-form-item>
        <el-form-item label="比赛描述" prop="desc">
          <el-input v-model="match.desc" type="text" autocomplete="off" />
        </el-form-item>
        <el-form-item label="最低积分要求" prop="minPoints">
          <el-input v-model="match.minPoints" type="text" autocomplete="off" />
        </el-form-item>
        <el-form-item label="积分奖励" prop="awards">
          <el-input v-model="match.awards" type="text" autocomplete="off" />
        </el-form-item>
        <el-form-item label="联系方式" prop="contact">
          <el-input v-model="match.contact" type="text" autocomplete="off" />
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
      <MdEditor v-model="content"></MdEditor>
    </div>
  </div>
</template>
<script setup lang="ts">
import { reactive, ref } from "vue";
import type { FormInstance, FormRules } from "element-plus";

import { MdEditor } from "md-editor-v3";

interface Basicinformation {
  id: bigint;
  title: string;
  coverImage: string;
  publishTime: Date;

  likes: number;
  comments: number;
  collects: number; // 收藏量
  pageView: number; // 浏览量
  state: number;
}

// 赛事信息
interface Match {
  time: Date;
  address: string;
  playNumber: string;
  desc: string;
  minPoints: number;
  awards: number; // 积分奖励
  contact: string;
  basicInformation: BasicInformation;
}

// 分类信息
const category = ref("");
const content = ref(""); //markdown文章信息
const match = ref<Match>({ basicInformation: {} });
const matchRef = ref<FormInstance>(); // 表单信息
// 比赛信息
const matches = ref<Match[]>([
  {
    matchId: 123,
    name: "计算机学院将举办新生杯大赛",
    address: "学校体育楼球馆",
    time: new Date(),
    minPoints: 24, // 最低积分要求
    awrards: 48, // 积分奖励
  },
]);

// 提交表单信息
const submitForm = (formEl: FormInstance | undefined) => {};

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
