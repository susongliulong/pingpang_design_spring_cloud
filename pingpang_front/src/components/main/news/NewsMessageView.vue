<template>
  <div class="">
    <h1>{{ news.title }}</h1>
    <span
      >分类：<span style="color: red">{{ category.name }}</span></span
    ><span
      >热度：<span style="color: red">{{ category.hots }}</span></span
    >
    <p style="font-size: 12px; color: azure">
      <span>作者：{{ author }}</span
      >&nbsp;&nbsp;&nbsp;<span
        >发表时间：{{
          news.publishTime.substring(0, 10) +
          " " +
          news.publishTime.substring(11)
        }}</span
      >&nbsp;&nbsp;&nbsp;<span>收藏量：{{ news.collects }}</span
      >&nbsp;&nbsp;&nbsp;<span>点赞量：{{ news.likes }}</span>
    </p>
    <MdPreview
      :model-value="mainContent"
      style="margin-top: 40px; width: 75%"
    ></MdPreview>
  </div>
</template>
<script setup lang="ts">
import { ref, reactive } from "vue";
import { useRoute } from "vue-router";
import { MdPreview } from "md-editor-v3";
import "md-editor-v3/lib/style.css";
import axios from "axios";

import { gatewayUrl } from "@/global";

const router = useRoute();
const newsString = localStorage.getItem("news");
const news = JSON.parse(newsString);
const mainContent = ref("");
const author = ref("");
const category = ref("");

newsMessage(news);

// 初始化资讯详细信息
function newsMessage(news: any) {
  axios({
    method: "get",
    url: gatewayUrl + "/news/message/" + news.id,
  }).then((resp) => {
    mainContent.value = resp.data.data.content;
  });

  axios({
    method: "get",
    url: gatewayUrl + "/news/interestName/" + news.categoryId,
  }).then((resp) => {
    category.value = resp.data.data;
  });

  axios({
    method: "get",
    url: gatewayUrl + "/news/author/" + news.authorId,
  }).then((resp) => {
    author.value = resp.data.data;
  });
}
</script>
<style scoped></style>
