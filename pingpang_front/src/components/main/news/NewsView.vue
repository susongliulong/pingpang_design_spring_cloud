<template>
  <el-scrollbar
    style="height: calc(100vh - 60px)"
    @scroll="scrollToLoadNews"
    ref="scrollBarRef"
  >
    <div
      id="main_content"
      class="item clear"
      v-for="(item, index) in articles"
      @click="newsMessage(item)"
    >
      <div style="font-weight: bold">{{ item.title }}</div>
      <div class="left" style="margin: 10px 20px">
        <img :src="item.coverImage" style="width: 200px; height: 120px" />
      </div>
      <div class="left" style="width: 400px; height: 124px; overflow: hidden">
        {{ item.mainContent }}
      </div>
      <div id="item_footor" class="left">
        <el-button
          class="el_button_white"
          type="primary"
          size="small"
          :id="'likeButton' + index"
          @click="like(index)"
        >
          <span style="color: black"
            ><el-icon> <CaretTop /> </el-icon>赞同{{ item.likes }}</span
          >
        </el-button>
        <el-button class="el_button_white" type="primary" size="small">
          <i style="color: #056de8"> 浏览量：{{ item.pageView }}</i>
        </el-button>
        <el-button class="el_button_white" type="primary" size="small">
          <i style="color: #056de8"
            ><el-icon> <View /> </el-icon>评论数：{{ item.comments }}</i
          >
        </el-button>
        <el-button
          class="el_button_white"
          type="primary"
          size="small"
          @click="collect(index)"
          :id="'collectButton' + index"
        >
          <i style="color: black"
            ><el-icon> <Star /> </el-icon>收藏{{ item.collects }}</i
          >
        </el-button>
      </div>
    </div>
  </el-scrollbar>
</template>
<script setup lang="ts">
import axios from "axios";
import { onBeforeMount, ref, watch} from "vue";
import { useRouter, useRoute } from "vue-router";
import { gatewayUrl } from "@/global";
import { ElScrollbar } from "element-plus";

// 文章描述信息
interface Article {
  id: bigint;
  title: String;
  coverImage: String;
  mainContent: String;
  likes: number;
  comments: number;
  collects: number; // 用户收藏量
  pageView: number; // 浏览量
}

// 文章信息
const articles = ref<Article[]>([]);
const router = useRouter();
const route = useRoute();
const interests = JSON.parse(localStorage.getItem("user")).interests;

onBeforeMount(() => {
  initialArticles(interests);
})

// 监听路由参数变化
watch(()=> route.query.keyword, (newKeyWord) => {
  if (newKeyWord != undefined && newKeyWord.length>0) {
    initialArticlesByKeyWord(newKeyWord.toString());
  }
})

// 根据keyword进行查询
function initialArticlesByKeyWord(keyWord: String) {
  axios
    .get(gatewayUrl + "/news/keyWord", { params: { keyword: keyWord } })
    .then((resp) => {
      articles.value = resp.data.data;
    });
}
// 初始化资讯信息,其中interests为用户感兴趣的话题，用来向用户推荐感兴趣的信息
function initialArticles(interests: number[]) {
  axios({
    url: gatewayUrl + "/news/overview",
    method: "get",
    params: {
      integers: interests,
    },
  }).then((resp) => {
    articles.value = resp.data.data;
  });
}

// 评论相关操作
function updateNewsMessage(news: Article) {
  axios({
    method: "post",
    url: gatewayUrl + "/news/update",
    data: news,
  }).then((resp) => {});
}
function like(index: number) {
  const likeButton = document.getElementById("likeButton" + index);
  if (likeButton?.classList.contains("el_button_white")) {
    likeButton.classList.remove("el_button_white");
    articles.value[index].likes++;
  } else {
    likeButton?.classList.add("el_button_white");
    articles.value[index].likes--;
  }
  updateNewsMessage(articles.value[index]);
}

function collect(index: number) {
  const collectButton = document.getElementById("collectButton" + index);
  if (collectButton?.classList.contains("el_button_white")) {
    collectButton.classList.remove("el_button_white");
    articles.value[index].collects++;
  } else {
    collectButton?.classList.add("el_button_white");
    articles.value[index].collects--;
  }
  updateNewsMessage(articles.value[index]);
}

function newsMessage(news: Article) {
  localStorage.setItem("news", JSON.stringify(news));
  router.push({
    path: "/main/news_message",
    query: {
      newsId: news.id,
    },
  });
}

// 当下拉框滑动到底部的时候，请求下一页的内容
const scrollBarRef = ref();
let page: number = 1;
const scrollToLoadNews = (scroll: {
  scrollLeft: number;
  scrollTop: number;
}): void => {
  let wrapRef = scrollBarRef.value.wrapRef;
  let poor = wrapRef.scrollHeight - wrapRef.clientHeight;
  if (scroll.scrollTop + 2 >= poor) {
    // 滑动条到达底部，此时发起异步请求
    page++;
    getNewsByPage(page); // 加载新的文章资讯
  }
};
function getNewsByPage(page: number) {
  axios({
    method: "get",
    url: gatewayUrl + "/news/overview",
    params: {
      page: page,
    },
  }).then((resp) => {
    articles.value = articles.value.concat(resp.data.data);
  });
}
</script>
<style scoped>
#main_content {
  margin-bottom: 10px;
  width: 640px;
}

#main_content:hover {
  cursor: pointer;
  box-shadow: 5px 5px 5px -4px red;
}
.item {
  padding: 10px;
  border: 1px solid #e6f0fd;
  background-color: #ffffff;
}

.item #item_footor button {
  margin-right: 46px;
}

.item #item_footor button:last-child {
  margin-right: 0px;
}
.el_button_white {
  background-color: white;
}
.el_button_green {
  background-color: blue;
}
</style>
