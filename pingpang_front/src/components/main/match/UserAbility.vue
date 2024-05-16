<template>
  <h1>为各项技术进行自我评分</h1>
  <el-button type="primary" size="default" @click="generate"
    >更新个人能力</el-button
  >

  <div id="content">
    <el-tabs
      v-model="activeName"
      class="tabs"
      @tab-click="handleClick"
      stretch="true"
    >
      <el-tab-pane label="发球能力" name="first" class="pane">
        <div
          class="slider-demo-block"
          v-for="(serve, index) in serves"
          :index="index"
        >
          <span class="demonstration">{{ serve }}</span>
          <el-slider v-model="ability.serve[index]" show-input />
        </div>
      </el-tab-pane>

      <el-tab-pane label="接发球能力" name="second" class="pane">
        <div
          class="slider-demo-block"
          v-for="(serve, index) in returns"
          :index="index"
        >
          <span class="demonstration">{{ serve }}</span>
          <el-slider v-model="ability.return[index]" show-input />
        </div>
      </el-tab-pane>
      <el-tab-pane label="进攻能力" name="third" class="pane">
        <div
          class="slider-demo-block"
          v-for="(serve, index) in asserts"
          :index="index"
        >
          <span class="demonstration">{{ serve }}</span>
          <el-slider v-model="ability.assert[index]" show-input />
        </div>
      </el-tab-pane>
      <el-tab-pane label="防守能力" name="fourth" class="pane">
        <div
          class="slider-demo-block"
          v-for="(serve, index) in defends"
          :index="index"
        >
          <span class="demonstration">{{ serve }}</span>
          <el-slider v-model="ability.defend[index]" show-input />
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>
<script lang="ts" setup>
import { ref, onBeforeMount } from "vue";
import type { TabsPaneContext } from "element-plus";
import axios from "axios";
import gatewayUrl from "@/global";
import { ElMessage } from "element-plus";
import { useRoute } from "vue-router";

interface Ability {
  serve: number[];
  return: number[];
  assert: number[];
  defend: number[];
}

const serves = ref<String[]>([]);
const returns = ref<String[]>([]);
const asserts = ref<String[]>([]);
const defends = ref<String[]>([]);
const route = useRoute();
const activeName = ref("first");

const ability = ref<Ability>({
  serve: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
  return: [0, 0, 0, 0, 0, 0, 0, 0],
  assert: [0],
  defend: [0],
});
let tabs = new Set(); // 记录选手查看过的界面
let user: any;
let firstJoin: number = 0;

onBeforeMount(() => {
  const userString = localStorage.getItem("user");
  if (userString != null) {
    user = JSON.parse(userString).user;
    const value = route.query.firstJoin;
    tabs.add("发球能力");
    if (value !== undefined) {
      firstJoin = 1;
    }
    initialAbilitys();
  }
});

function initialAbilitys() {
  axios
    .get(gatewayUrl + "/userAbility/ability?name=" + "发球能力")
    .then((resp) => {
      serves.value = resp.data.data;
    });

  axios
    .get(gatewayUrl + "/userAbility/ability?name=" + "接发球能力")
    .then((resp) => {
      returns.value = resp.data.data;
    });

  axios
    .get(gatewayUrl + "/userAbility/ability?name=" + "进攻能力")
    .then((resp) => {
      asserts.value = resp.data.data;
    });

  axios
    .get(gatewayUrl + "/userAbility/ability?name=" + "防守能力")
    .then((resp) => {
      defends.value = resp.data.data;
    });
}

/**
 * 更新个人能力
 */
function generate() {
  // 在更新之前进行校验
  if (firstJoin == 1) {
    // 此时用户需要编辑所有的能力值，来完成初始化
    if (tabs.size < 4) {
      let array = [];
      for (let name of ["发球能力", "接发球能力", "进攻能力", "防守能力"]) {
        if (!tabs.has(name)) {
          array.push(name);
        }
      }

      let warnMessage = "首次参赛需要完成" + array.join(",")+"的填写。";
      ElMessage.warning(warnMessage);
      return;
    }
  } else {
    axios({
      method: "post",
      url: gatewayUrl + "/userAbility/post",
      params: {
        serves: ability.value.serve,
        returns: ability.value.return,
        asserts: ability.value.assert,
        defends: ability.value.defend,
        userId: user.id,
      },
    }).then((resp) => {
      ElMessage.success(resp.data.message);
    });
  }
}

const handleClick = (tab: TabsPaneContext, event: Event) => {
  tabs.add(tab.props.label);
};
</script>

<style scoped>
#content {
  width: 800px;
}
.tabs > .el-tabs__content {
  padding: 30px 30px;
  color: #6b778c;
  font-size: 32px;
  font-weight: 600;
}

.slider-demo-block {
  max-width: 600px;
  display: flex;
  align-items: center;
}
.slider-demo-block .el-slider {
  margin-top: 0;
  margin-left: 12px;
}
.slider-demo-block .demonstration {
  font-size: 18px;
  color: red;
  line-height: 44px;
  flex: 1;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  margin-bottom: 0;
}
.slider-demo-block .demonstration + .el-slider {
  flex: 0 0 70%;
}
</style>
