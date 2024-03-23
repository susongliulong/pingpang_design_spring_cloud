<template>
  <div id="content" class="item clear" v-for="(item, index) in articles">
    <div style="font-weight: bold">{{ item.title }}</div>
    <div class="left" style="margin: 10px 20px">
      <img :src="item.img" style="width: 200px; height: 120px" />
    </div>
    <div class="left" style="width: 400px; height: 124px; overflow: hidden">
      {{ item.mainContent }}
    </div>
    <div id="item_footor" class="left">
      <el-button type="primary" size="small" style="background-color: #e6f0fd">
        <span style="color: #056de8"
          ><el-icon>
            <CaretTop /> </el-icon
          >赞同{{ item.likes }}</span
        >
      </el-button>
      <el-button type="primary" size="small" style="background-color: #e6f0fd">
        <i style="color: #056de8"
          ><el-icon>
            <CaretBottom /> </el-icon
          >踩{{ item.dislikes }}</i
        >
      </el-button>
      <el-button type="primary" size="small" style="background-color: #e6f0fd">
        <i style="color: #056de8"
          ><el-icon>
            <View /> </el-icon
          >评论</i
        >
      </el-button>
      <el-button type="primary" size="small" style="background-color: #e6f0fd">
        <i style="color: #056de8"
          ><el-icon>
            <Star /> </el-icon
          >收藏{{ item.watches }}</i
        >
      </el-button>
      <el-button type="primary" size="small" style="background-color: #e6f0fd">
        <i style="color: #056de8"
          ><el-icon>
            <Share /> </el-icon
          >分享</i
        >
      </el-button>
    </div>
  </div>
</template>
<script setup lang="ts">
import { ref } from "vue";
import { onBeforeMount, onMounted } from "vue";

// 文章描述信息
interface Article {
  title: String;
  img: String;
  mainContent: String;
  likes: number;
  dislikes: number;
  watches: number; // 用户收藏量
}

// 文章信息
const articles = ref<Article[]>([{}]);
</script>
<style scoped>
#content {
  margin: 0 20px;
  width: 60%;
  /* border: 1px solid #ebebeb; */
  float: left;
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
</style>
