<template>
  <div class="">
    <!-- 通过:label来实现默认初选 -->

    <el-container>
      <el-aside width="200px" style="max-height: calc(100vh)">
        <el-radio-group v-model="isCollapse" style="margin-bottom: 10px">
          <el-radio-button :value="false">详细面板</el-radio-button>
          <el-radio-button :value="true">简要面板</el-radio-button>
        </el-radio-group>
        <el-menu
          :default-active="activeMenuItem"
          class="el-menu-vertical"
          :collapse="isCollapse"
        >
          <el-sub-menu v-for="(menu, index) in menus" :index="menu.index">
            <template #title>
              <span>{{ menu.title }}</span>
            </template>
            <el-menu-item
              v-for="(item, index2) in menu.menus"
              :index="item.index"
              @click="handleClick(item, index)"
            >
              <span>{{ item.title }}</span>
            </el-menu-item>
          </el-sub-menu>
        </el-menu>
      </el-aside>

      <el-scrollbar style="height: calc(100vh)">
        <el-main>
          <div id="tutorial-desc" v-if="tutorial.tutorialId != null">
            <h1>{{ tutorial.basicInformation.title }}</h1>
            <span
              >分类：<span style="color: red">{{ category }}</span></span
            ><span></span>
            <p>
              <span
                >作者：<span style="font-size: 12px; color: azure">{{
                  tutorialUser.name
                }}</span></span
              >&nbsp;&nbsp;&nbsp;
            </p>
            <span
              >发表时间：{{
                tutorial.basicInformation.publishTime
                  .toString()
                  .substring(0, 10) +
                " " +
                tutorial.basicInformation.publishTime.toString().substring(11)
              }}</span
            >
          </div>
          <MdPreview :model-value="tutorial.content"></MdPreview>
          <!-- 放置评论界面 -->
          <CommentVue 
          @publish-comment="publishComment"  
          v-if="tutorial.tutorialId!=null" 
          :articleId="tutorial.tutorialId"/>
        </el-main>
      </el-scrollbar>
    </el-container>
  </div>
</template>
<script setup lang="ts">
import { ref, onBeforeMount, nextTick, onMounted } from "vue";
import CommentVue from "@/components/comment/CommentVue.vue";

import { ElMessage, ElScrollbar } from "element-plus";

import { MdPreview, MdCatalog } from "md-editor-v3";
import "md-editor-v3/lib/style.css";
import axios from "axios";
import gatewayUrl from "@/global";

// 按照elementUI要求设置菜单项
interface Menuitem {
  index: bigint;
  title: string;
  href: string;
}

interface Menu {
  index: bigint;
  title: string;
  menus: Menuitem[];
}

interface Basicinformation {
  authorId: bigint;
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

// 教程信息
interface Tutorial {
  tutorialId: bigint;
  level: number; //分类等级
  levelName: string;
  suitableObject: string;
  content: string;
  basicInformation: Basicinformation; // 基础信息
}
interface User {
  id: bigint;
  name: string;
  avatar: string;
  points: number; // 用户的积分
}

const menus = ref<Menu[]>([]);

const isCollapse = ref(false);
const user = ref<User>();
onBeforeMount(() => {
  const userString = localStorage.getItem("user");
  if (userString != null) {
    user.value = JSON.parse(userString).user;
  }
  initialMenus();
});

onMounted(() => {});

nextTick(() => {});

// 初始化一级菜单和二级菜单信息
const activeMenuItem = ref("");
const initialMenus = () => {
  axios({
    method: "get",
    url: gatewayUrl + "/tutorial/menus",
  }).then((resp) => {
    menus.value = resp.data.data;
    activeMenuItem.value = menus.value[0].menus[0].index.toString();
    handleClick(menus.value[0].menus[0], 0);
  });
};

// 点击某一个子菜单的时候，显示对应的文章信息
const tutorial = ref<Tutorial>({});
const category = ref(""); // 分类信息
const handleClick = (item: Menuitem, index: number) => {
  // 根据item.index查找后台教程信息
  axios
    .get(gatewayUrl + "/tutorial/tutorial?tutorialId=" + item.index)
    .then((resp) => {
      tutorial.value = resp.data.data;
      category.value = menus.value[index].title;
      getAuthor(tutorial.value.basicInformation.authorId);
      
    });
};

// 查询作者信息
const tutorialUser = ref<any>();
function getAuthor(userId: bigint) {
  axios.get(gatewayUrl + "/tutorial/user?userId=" + userId).then((resp) => {
    tutorialUser.value = resp.data.data;
  });
}
// --------------------------------------发布评论相关--------------------------------------------------------
interface User {
  id: bigint;
  name: string;
  avatar: string;
  points: number; // 用户的积分
}

interface Comment {
  user: User; // 评论发布者
  number: number; // 该评论下包含子评论的数量
  isReplyWindowOpen: Boolean;
  replyToId: bigint;
  replyToName: string;
  id: bigint;
  content: string;
  publishTime: string;
  likes: number;
  dislikes: number;
  comments: Comment[]; // 嵌套评论
}
const comments = ref<Comment[]>([]); // 评论数据

/**
 * 在主窗口回复评论
 * @param content
 */
const publishComment = (content: string) => {
  
};
</script>
<style scoped>
.el-menu-vertical:not(.el-menu--collapse) {
  width: 178px;
  min-height: 400px;
  max-height: 600px;
}
.el-main {
  --el-main-padding: 0px;
  --el-main-width: 600px;
}
</style>
