<template>
  <div id="main_content" class="item clear" v-for="(item, index) in articles" @click="newsMessage(item)">
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
</template>
<script setup lang="ts">
import axios from "axios";
import { ref} from "vue";
import { onBeforeMount, onMounted } from "vue";
import { useRouter } from "vue-router";
import { gatewayUrl } from "@/global";

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

onBeforeMount(() => {
  // 根据用户感兴趣的话题加载文章信息
  const userString = localStorage.getItem("user");
  if (userString != null) {
    const interests = JSON.parse(userString).interests;
    initialArticles(interests);
  }
});

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
function updateNewsMessage(news:Article) {
  axios({
    method: 'post',
    url: gatewayUrl + '/news/update',
    data: news
  }).then(resp => {
    
  })
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
    path: '/main/news_message',
    query: {
      newsId: news.id
    }
  })
}
</script>
<style scoped>
#main_content{
  margin-bottom: 10px;
  width: 640px;
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
