<template>
  <el-row>
    <el-col :span="2">
      <img src="" alt="照片" style="width: 60px; height: 60px" />
    </el-col>
    <el-col :offset="1" :span="10">
      <el-input
        v-model="commentContent"
        placeholder="良言一句三冬暖"
        size="normal"
        @change=""
      >
      </el-input>
    </el-col>
    <el-col :span="2" :offset="1">
      <el-button type="primary" size="default" @click="">发布</el-button>
    </el-col>
  </el-row>

  <div class="comment" v-for="(comment, index) in comments" :index="index">
    <div id="userMessage" class="clear">
      <div class="avator left">
        <img src="" alt="" style="width: 60px; height: 60px" />
      </div>
      <div class="content left">
        <p>
          <span style="font-size: bold">{{ comment.user.name }} </span>
          <span style="color: red">积分&nbsp;&nbsp;</span
          >{{ comment.user.points }}
        </p>
        <p style="font-size: 14px">{{ comment.content }}</p>
        <div class="helperMessage">
          <span>{{ comment.publishTime }}</span>
          <span
            ><el-con> <Like /> </el-con> {{ comment.likes }}</span
          >
          <span
            ><el-con> <Dislike /> </el-con> {{ comment.dislikes }}</span
          >
          <span
            class="reply"
            @click="
              showReplyWindow = !showReplyWindow;
              comment.isReplyWindowOpen = !comment.isReplyWindowOpen;
            "
            >&nbsp;&nbsp;回复</span
          >
        </div>
      </div>
    </div>
    <!-- 其他人回复user的评论 -->
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
            <span style="font-size: bold">{{ subComment.user.name }} </span>
            <span style="color: red">积分&nbsp;</span
            >{{ subComment.user.points }}
            <span>&nbsp;&nbsp;&nbsp;</span>
            <span v-if="subComment.replyToId !== comment.user.id"
              ><span style="font-size: 12px; color: red">回复&nbsp;&nbsp;</span
              ><span>@{{ subComment.replyToName }}&nbsp;&nbsp;</span>
              <span style="font-size: 12px">{{
                subComment.content
              }}</span></span
            >
            <span style="font-size: 12px" v-else>{{ subComment.content }}</span>
          </div>
          <div class="helperMessage">
            <span>{{ comment.publishTime }}</span>
            <span
              ><el-con> <Like /> </el-con> {{ comment.likes }}</span
            >
            <span
              ><el-con> <Dislike /> </el-con> {{ comment.dislikes }}</span
            >
            <span
              class="reply"
              @click="
                showReplyWindow = !showReplyWindow;
                comment.isReplyWindowOpen = !comment.isReplyWindowOpen;
              "
              >&nbsp;&nbsp;回复</span
            >
          </div>
        </div>
      </div>
      <!-- 查看更多的评论 -->
      <div id="footer">
        <span v-if="comment.number >= 10 && hidePagination">
          共<span>{{ comment.number }}</span
          >条评论，<span id="watchMore" @click="hidePagination = false"
            >点击查看</span
          ></span
        >
        <div v-if="!hidePagination" class="comment-pagination">
          <el-row>
            <el-col :span="13">
              <el-pagination
                v-model:current-page="currentPage"
                :page-size="pageSize"
                :small="false"
                :background="true"
                layout="total, prev, pager, next"
                :total="comment.number"
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
              />
            </el-col>

            <el-col :span="1" :offset="1">
              <el-button type="text" size="small" @click="hidePagination = true"
                style="margin-top: 5px">收起评论</el-button
              >
            </el-col>
          </el-row>
        </div>
      </div>
      <div
        v-if="showReplyWindow && comment.isReplyWindowOpen"
        id="writeComment"
      >
        <el-row>
          <el-col :span="2">
            <img src="" alt="照片" style="width: 60px; height: 60px" />
          </el-col>
          <el-col :offset="1" :span="10">
            <el-input
              v-model="commentContent"
              placeholder="良言一句三冬暖"
              size="normal"
              @change=""
            >
            </el-input>
          </el-col>
          <el-col :span="2" :offset="1">
            <el-button type="primary" size="default" @click="">发布</el-button>
          </el-col>
        </el-row>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import { ref, onBeforeMount } from "vue";
interface User {
  id: bigint;
  name: string;
  avatar: string;
  points: number;
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
// 显示回复窗口
const showReplyWindow = ref<Boolean>(false);

// 分页条相关属性
const hidePagination = ref(true);
const currentPage = ref<number>(5);
const pageSize = ref(10); // 每一页中包含评论的数量
const total = ref(1000);

const user = ref<User>({ id: 1234, name: "追风", avator: "hdh", points: 12 });

const comments = ref<Comment[]>([
  {
    user: { id: 1234, name: "追风", avator: "hdh", points: 12 },
    number: 100,
    isReplyWindowOpen: false,
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
        number: 100,
        isReplyWindowOpen: false,
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
        number: 100,
        isReplyWindowOpen: false,
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
    number: 150,
    isReplyWindowOpen: false,
    id: 123,
    content: "哥笑了，但不白笑，祝高三学子高考顺利，金榜题名！",
    publishTime: "2024-12-23 14:57:00",
    likes: 1234,
    dislikes: 1234,
  },
]);

// 钩子函数
onBeforeMount(() => {
  
})

// 初始化前10条评论信息，每条评论下面包含三条子评论
function initialComments() {
  
}

// 评论分页条相关方法
const handleSizeChange = (val: number) => {
  console.log(`${val} items per page`);
};
const handleCurrentChange = (val: number) => {
  console.log(`current page: ${val}`);
};
</script>
<style scoped>
.comment-pagination{
  margin-top: 20px;
}

.comment {
  background-color: white;
  margin-top: 20px;
  padding: 10px;
  border-bottom: 2px solid red;
}
.comment:last-child{
  margin-bottom: 0px;
}
.content {
  margin-left: 20px;
}
.subComment {
  margin-left: 20px;
  margin-top: 20px;
}
.helperMessage {
  font-size: 12px;
  color: #9499a0;
}
.helperMessage .reply:hover {
  color: #2e58ff;
  cursor: pointer;
}
#footer {
  font-size: 12px;
  color: #9499a0;
}

#footer #watchMore:hover {
  color: #2e58ff;
  cursor: pointer;
}
#writeComment {
  margin-top: 20px;
}
</style>
