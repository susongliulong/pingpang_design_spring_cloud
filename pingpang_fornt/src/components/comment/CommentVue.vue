<template>
  <div class="comment" v-for="(comment, index) in comments" :index="index">
    <div id="userMessage" class="clear">
      <div class="avator left">
        <img src="" alt="" style="width: 60px; height: 60px" />
      </div>
      <div class="content left">
        <p>{{ comment.user.name }} 积分{{ comment.user.points }}</p>
        <p>{{ comment.content }}</p>
        <div class="helperMessage">
          <span>{{ comment.publishTime }}</span>
          <span
            ><el-con> <Like /> </el-con> {{ comment.likes }}</span
          >
          <span
            ><el-con> <Dislike /> </el-con> {{ comment.dislikes }}</span
          >
          <span @click="showReplyWindow = !showReplyWindow">回复</span>
        </div>
      </div>
    </div>
    <div id="mainBody">
      <div
        class="subComment clear"
        v-for="(subComment, index) in comment.comments"
        :index="index"
      >
        <div class="avator left">
          <img src="" alt="" style="width: 30px; height: 30px" />
        </div>
        <div class="content left">
          <div>
            {{ subComment.user.name }} 拥有积分{{ comment.user.points }}
            <span v-if="subComment.replyToId !== comment.user.id"
              >回复<span>@ {{ subComment.replyToName }}</span>
              <span>{{ subComment.content }}</span></span
            >
            <span v-else>{{ subComment.content }}</span>
          </div>
          <div class="helperMessage">
            <span>{{ comment.publishTime }}</span>
            <span
              ><el-con> <Like /> </el-con> {{ comment.likes }}</span
            >
            <span
              ><el-con> <Dislike /> </el-con> {{ comment.dislikes }}</span
            >
            <span @click="showReplyWindow = !showReplyWindow">回复</span>
          </div>
        </div>
      </div>
      <div id="footer">共79条评论，点击查看</div>
      <div v-if="showReplyWindow" id="writeComment">
        <img src="" alt="" style="width: 40px; height: 40px" />
        <el-input
          v-model="commentContent"
          :placeholder="'回复' + comment.user.name"
          size="normal"
          @change=""
        >
        </el-input>
        <el-button type="primary" size="default" @click="">发布</el-button>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import { ref, reactive } from "vue";
interface User {
  id: bigint;
  name: string;
  avatar: string;
  points: number;
}

interface Comment {
  user: User; // 评论发布者
  replyToId: bigint;
  replyToName: string;
  id: bigint;
  content: string;
  publishTime: string;
  likes: number;
  dislikes: number;
  comments: Comment[]; // 嵌套评论
}

const showReplyWindow = ref<Boolean>(false);

const user = ref<User>({ id: 1234, name: "追风", avator: "hdh", points: 12 });

const comments = ref<Comment[]>([
  {
    user: { id: 1234, name: "追风", avator: "hdh", points: 12 },
    id: 123,
    replyToId: 0,
    replyToName: "",
    content: "哥笑了，但不白笑，祝高三学子高考顺利，金榜题名！",
    publishTime: "2024-12-23 14:57:00",
    likes: 1234,
    dislikes: 1234,
    comments: [
      {
        user: { id: 1234, name: "追风", avator: "hdh", points: 12 },
        replyToId: 1234,
        replyToName: "追风",
        id: 123,
        content: "哥笑了，但不白笑，祝高三学子高考顺利，金榜题名！",
        publishTime: "2024-12-23 14:57:00",
        likes: 1234,
        dislikes: 1234,
      },
      {
        user: { id: 1235, name: "追风", avator: "hdh", points: 12 },
        replyToId: 1235,
        replyToName: "追风",
        id: 123,
        content: "哥笑了，但不白笑，祝高三学子高考顺利，金榜题名！",
        publishTime: "2024-12-23 14:57:00",
        likes: 1234,
        dislikes: 1234,
      },
    ],
  },
  {
    user: { id: 1235, name: "追风", avator: "hdh", points: 12 },
    id: 123,
    content: "哥笑了，但不白笑，祝高三学子高考顺利，金榜题名！",
    publishTime: "2024-12-23 14:57:00",
    likes: 1234,
    dislikes: 1234,
  },
]);
</script>
<style scoped>

</style>
