<template>
  <h1 style="margin-top: 40px">评论界面：</h1>
  <el-row>
    <el-col :span="2">
      <!-- 展示登录之后的照片 -->
      <img :src="user.avatar" alt="照片" style="width: 60px; height: 60px" />
    </el-col>
    <el-col :offset="1" :span="10">
      <el-input
        v-model="commentContent"
        placeholder="良言一句三冬暖,恶语伤人十月寒"
        size="normal"
      >
      </el-input>
    </el-col>
    <el-col :span="2" :offset="1">
      <el-button type="primary" size="default" @click="publishComment"
        >发布</el-button
      >
    </el-col>
  </el-row>

  <div class="comment" v-for="(comment, index) in comments" :index="index">
    <div id="userMessage" class="clear">
      <div class="avator left">
        <img
          :src="comment.user.avatar"
          alt=""
          style="width: 60px; height: 60px"
        />
      </div>
      <div class="content left">
        <p>
          <span style="font-size: bold">{{ comment.user.name }} </span>
          &nbsp;&nbsp;
          <span style="color: red">积分&nbsp;&nbsp;</span
          >{{ comment.user.points }}
        </p>
        <p style="font-size: 14px">{{ comment.content }}</p>
        <div class="helperMessage">
          <span>{{ comment.publishTime.substring(0,10)+" "+ comment.publishTime.substring(11,19) }}</span>
          &nbsp;&nbsp;&nbsp;
          <span :id="'like'+index" class="icon" @click="like(index,undefined)"
            >点赞量： {{ comment.likes }}</span
          >&nbsp;&nbsp;&nbsp;
          <span class="icon" @click="dislike(index,undefined)" :id="'dislike'+index"
            >拉踩量： {{ comment.dislikes }} </span
          >
          <span
            class="reply"
            @click="
              showReplyWindow = !showReplyWindow;
            comment.isReplyWindowOpen = !comment.isReplyWindowOpen;
            openReply(comment, index, comment);
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
        v-for="(subComment, index2) in comment.comments"
        :index="index2"
        @mouseover="mainCommentIndex=index"
      >
        <div class="avator left">
          <img :src="subComment.user.avatar" alt="" style="width: 30px; height: 30px" />
        </div>
        <div class="content left">
          <div>
            <span style="font-size: bold">{{ subComment.user.name }} </span>
            &nbsp;&nbsp;&nbsp;
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
            <span>{{ subComment.publishTime.substring(0,10)+" "+ subComment.publishTime.substring(11,19) }}</span>
            &nbsp;&nbsp;&nbsp;
            <span :id="'like'+index+index2" class="icon" @click="like(index,index2)"
              > 点赞量：{{ subComment.likes }}</span
            >&nbsp;&nbsp;&nbsp;
            <span class="icon" @click="dislike(index,index2)" :id="'dislike'+index+index2"
              >拉踩量：{{ subComment.dislikes }} </span
            >
            <span
              class="reply"
              @click="
                showReplyWindow = !showReplyWindow;
                comment.isReplyWindowOpen = !comment.isReplyWindowOpen;
                openReply(comment, index, subComment);
              "
              >&nbsp;&nbsp;回复</span
            >
          </div>
        </div>
      </div>
      <!-- 查看更多的评论 -->
      <div id="footer">
        <span v-if="comment.number > 5 && hidePagination">
          共<span>{{ comment.number }}</span
          >条评论，<span id="watchMore" @click="hidePagination = false"
            >点击查看</span
          ></span
        >
        <div v-if="!hidePagination" class="comment-pagination">
          <el-row>
            <el-col :span="13">
              <el-pagination
                :page-size="pageSize"
                :small="false"
                :background="true"
                layout="total, prev, pager, next"
                :total="comment.number"
                @mouseover="mainCommentIndex=index"
                @current-change="handleCurrentChange"
              />
            </el-col>

            <el-col :span="1" :offset="1">
              <el-button
                type="text"
                size="small"
                @click="hidePagination = true"
                style="margin-top: 5px"
                >收起评论</el-button
              >
            </el-col>
          </el-row>
        </div>
      </div>
    </div>
    <div v-if="showReplyWindow && comment.isReplyWindowOpen" id="writeComment">
      <el-row>
        <el-col :span="2">
          <img :src="user.avatar" alt="照片" style="width: 60px; height: 60px" />
        </el-col>
        <el-col :offset="1" :span="10">
          <el-input
            v-model="replyCommentContent"
            placeholder="良言一句三冬暖，恶语伤人十日寒"
            size="normal"
            @change=""
          >
          </el-input>
        </el-col>
        <el-col :span="2" :offset="1">
          <el-button type="primary" size="default" @click="reply"
            >发布</el-button
          >
        </el-col>
      </el-row>
    </div>
  </div>
</template>
<script setup lang="ts">
import gatewayUrl from "@/global";
import axios from "axios";
import {
  ref,
  onBeforeMount,
  defineEmits,
  defineProps,
  toRefs,
  onMounted,
  watch
} from "vue";
import { ElMessage } from "element-plus";

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
// 显示回复窗口
const showReplyWindow = ref<Boolean>(false);

// 分页条相关属性
const hidePagination = ref(true);
const pageSize = ref(5); // 每一页中包含评论的数量
const commentContent = ref(""); // 在文章底部回复评论

const replyCommentContent = ref("");// 回复他人的评论
// 钩子函数
onBeforeMount(() => {
  const userString = localStorage.getItem("user");
  if (userString != null) {
    user.value = JSON.parse(userString).user;
    initialComments(articleId.value,1);// 查询每个评论第一页下的评论数目
  }
});

const props = defineProps({
  articleId: BigInt,
});
const { articleId } = toRefs(props);
const comments = ref<Comment[]>([]);
const user = ref<User>();

// 监听articleId变化
watch(articleId, (newValue) => {
  initialComments(newValue, 1);
})

/**
 * 根据文章id初始化评论信息
 * @param articleId
 */
function initialComments(articleId: bigint,pageNumber:number) {
  axios({
    method: "get",
    url: gatewayUrl + "/comment/comments/pages",
    params: {
      articleId: articleId,
      pageNumber:pageNumber
    },
  }).then((resp) => {
    comments.value = resp.data.data;
  });
}

/**
 * 发送评论事件
 */
const emit = defineEmits(["publish-comment"]);
function publishComment() {
  emit("publish-comment", commentContent.value);
  axios({
    method: "post",
    url: gatewayUrl + "/comment/post",
    data: {
      userId: user.value?.id,
      articleId: articleId.value,
      content: commentContent.value,
    },
  }).then((resp) => {
    
    if (resp.data.code == 200) {
      ElMessage.success("发布评论成功");
      commentContent.value = "";
      comments.value.push(resp.data.data);
      comments.value.reverse();
    }
    else ElMessage.error("发布评论失败");
  });
}

/**
 * 回复某位用户的评论
 * @param comment
 */
const mainComment = ref<Comment>();
const subComment = ref<Comment>();
const index = ref<number>(0);

/**
 * 打开回复的评论框
 * @param comment
 * @param index
 * @param subComment
 */
function openReply(comment: Comment, i: number, sc: Comment) {
  // 在comment评论下新增一条评论
  // 新增评论的回复对象在subComment中
  mainComment.value = comment;
  subComment.value = sc;
  index.value = i;
  commentContent.value = "";
}

function reply() {
  axios({
    method: "post",
    url: gatewayUrl + "/comment/replyOthers",
    data: {
      articleId: articleId.value,
      commentId: mainComment.value?.id,
      userId: user.value?.id,
      content: replyCommentContent.value,
      replyToId: subComment.value?.user.id,
      replyToName: subComment.value?.user.name,
    },
  }).then((resp) => {
    // 后端返回的参数为Comment类型
    console.log(resp.data.data);
    const comment: Comment = resp.data.data;
    showReplyWindow.value = false;
    comments.value[index.value].isReplyWindowOpen = false;
    comments.value[index.value].comments.push(comment);
    replyCommentContent.value = "";
    ElMessage.success("回复成功");
  });
}

// 评论分页条相关方法
const mainCommentIndex = ref<number>(0);
const handleCurrentChange = (index: number) => {
  //  展示新的评论
  axios({
    method: "get",
    url: gatewayUrl + "/comment/comments/pages/"+comments.value[mainCommentIndex.value].id,
    params: {
      articleId: articleId.value,
      pageNumber: index
    },
  }).then((resp) => {
    comments.value[mainCommentIndex.value].comments = resp.data.data;
  });
};

/**
 * 点赞评论
 * @param mainCommentIndex 
 * @param subCommentIndex 
 */
// 在进行点赞之后再次点击即为取消操作
function operate(comment: Comment) {
  axios({
    method: 'post',
    url: gatewayUrl + '/comment/like',
    data:comment
  }).then(resp => {
    
  })
}
/**
 * 点赞操作
 * @param mainCommentIndex 
 * @param subCommentIndex 
 */
const like = (mainCommentIndex: number, subCommentIndex: number) => {
  let ele: HTMLElement | null;
  // 获取评论
  let comment = comments.value[mainCommentIndex];
  if (subCommentIndex == undefined) {
    ele = document.getElementById("like" + mainCommentIndex );  
  } else {
    ele = document.getElementById("like" + mainCommentIndex + subCommentIndex)
    comment=comment.comments[subCommentIndex];
  }
  if (ele?.getAttribute("class")?.includes("like")) {
    // 此时应当为取消点赞操作
    ele.setAttribute("class", "icon");
    comment.likes = comment.likes - 1;
  } else {
    ele?.setAttribute("class", "like");
    comment.likes = comment.likes + 1;
  }
  // 最后发送post请求，进行更新操作
  operate(comment);
}
/**
 * 拉踩操作
 */
const dislike = (mainCommentIndex: number, subCommentIndex: number) => {
  let ele: HTMLElement | null;
  // 获取评论
  let comment = comments.value[mainCommentIndex];
  if (subCommentIndex == undefined) {
    ele = document.getElementById("dislike" + mainCommentIndex );  
  } else {
    ele = document.getElementById("dislike" + mainCommentIndex + subCommentIndex)
    comment=comment.comments[subCommentIndex];
  }
  if (ele?.getAttribute("class")?.includes("dislike")) {
    // 此时应当为取消拉踩
    ele.setAttribute("class", "icon");
    comment.dislikes = comment.dislikes - 1;
  } else {
    ele?.setAttribute("class", "dislike");
    comment.dislikes = comment.dislikes + 1;
  }
  // 最后发送post请求，进行更新操作
  operate(comment);
}


</script>
<style scoped>
.like{
  color: #2e58ff;
  cursor: pointer;
}
.dislike{
  color:red;
  cursor:pointer;
}
.comment-pagination {
  margin-top: 20px;
}

.comment {
  background-color: white;
  margin-top: 20px;
  padding: 10px;
  border-bottom: 2px solid red;
}
.comment:last-child {
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
.icon:hover{
  color: #2e58ff;
  cursor: pointer;
}
#writeComment {
  margin-top: 20px;
}
</style>
