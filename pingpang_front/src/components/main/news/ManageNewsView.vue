<template>
  <div id="content">
    <h1>资讯管理</h1>
    <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
      <el-tab-pane :label="`全部(` + allNumber + ')'" name="tabs5">
      </el-tab-pane>
      <el-tab-pane :label="`全部可见(` + allSeeableNumber + ')'" name="tabs2">
      </el-tab-pane>
      <el-tab-pane :label="`仅我可见(` + meSeeableNumber + ')'" name="tabs1">
      </el-tab-pane>
      <el-tab-pane :label="`审核(` + auditNumber + ')'" name="tabs0">
      </el-tab-pane>
      <el-tab-pane :label="`草稿(` + draftNumber + ')'" name="tabs3">
      </el-tab-pane>
      <el-tab-pane :label="`回收站(` + recycleBinNumber + ')'" name="tabs4">
      </el-tab-pane>
    </el-tabs>

    <div id="serach_news">
      <el-row>
        <el-col :span="4">
          <el-date-picker
            v-model="startTime"
            type="date"
            size="small"
            placeholder="起始时间"
            style="width: 100%"
          />
        </el-col>
        <el-col :span="4" :offset="1">
          <el-date-picker
            v-model="endTime"
            type="date"
            size="small"
            placeholder="结束时间"
            style="width: 100%"
          />
        </el-col>
        <el-col :span="4" :offset="1">
          <el-select
            v-model="category"
            value-key=""
            placeholder="选择分类"
            clearable
            size="small"
            filterable
            @change=""
            style="width: 100%"
          >
            <el-option
              v-for="item in interests"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            >
            </el-option>
          </el-select>
        </el-col>

        <el-col :span="6" :offset="1">
          <!-- 搜索框 -->
          <el-input
            v-model="searchContent"
            placeholder="请输入关键词"
            size="small"
            clearable
            @change=""
            style="width: 100%"
          ></el-input>
        </el-col>
        <el-col :span="2" :offset="1">
          <el-button type="primary" size="small" @click="initialArticles"
            >搜索</el-button
          >
        </el-col>
      </el-row>
    </div>
    <!-- 分页条 -->
    <div id="pagination">
      <el-pagination
        :hide-on-single-page="true"
        size="small"
        background
        layout="prev, pager, next"
        @current-change="newsPage"
        :current-page="currentPage"
        :total="newsNumber"
      />
    </div>

    <div id="main_content">
      <div
        class="article_item clear"
        v-for="(article, index) in articles"
        :key="index"
      >
        <div id="img" class="left" v-if="article.basicInformation.coverImage">
          <img
            :src="article.basicInformation.coverImage"
            alt=""
            style="width: 160px; height: 120px; margin-left: 10px"
          />
        </div>
        <div
          id="item_content"
          class="right clear"
          style="max-width: 70%; overflow-x: hidden"
        >
          <div class="clear">
            <span style="float: left; font-size: 14px; font-weight: bold">
              {{ article.basicInformation.title }}</span
            >
          </div>
          <p>
            <span style="font-size: 12px; color: red"
              >{{ article.basicInformation.publishTime.substring(0,10)+" "+article.basicInformation.publishTime.substring(11) }}
            </span>
          </p>
          <p
            style="
              color: #fc5556;
              background-color: #fff5f2;
              font-size: 13px;
              margin: 15px 0px;
              font-weight: bold;
            "
          >
            {{ article.name }}
          </p>
          <p style="color: #999ab2; font-size: 13px">
            <span
              >评论量：<span>{{
                article.basicInformation.comments
              }}</span></span
            >
            &nbsp;&nbsp;
            <span
              >收藏量：<span>{{
                article.basicInformation.collects
              }}</span></span
            >
            &nbsp;&nbsp;
            <span>浏览量:{{ article.basicInformation.pageView }}</span>
            &nbsp;&nbsp;

            <span
              >点赞量：<span> {{ article.basicInformation.likes }}</span></span
            >
            &nbsp;&nbsp;
          </p>

          <div class="right">
            <el-button
              type="danger"
              size="mini"
              @click="drop(article.basicInformation.id, index)"
              >删除文章</el-button
            >
            <el-button
              type="primary"
              size="mini"
              @click="edit(article.basicInformation.id, index)"
              >编辑文章</el-button
            >
            <el-button
              type="primary"
              size="mini"
              @click="preview(article.basicInformation.id, index)"
              >预览文章</el-button
            >
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script lang="ts" setup>
import { ref, onBeforeMount, watch } from "vue";
import type { TabsPaneContext } from "element-plus";
import { useRouter, useRoute } from "vue-router";
import axios from "axios";
import gatewayUrl from "@/global";
import { ElMessageBox, ElMessage } from "element-plus";
import router from "@/router";

interface Interest {
  id: bigint;
  name: string;
  hots: number;
}
// 展示文章的描述性信息
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

  categoryId: bigint; // 资讯分类
}
interface News {
  basicInformation: Basicinformation;
  name: string;
}

onBeforeMount(() => {
  const userString = localStorage.getItem("user");
  if (userString != null) {
    user = JSON.parse(userString).user;
    initialInterests();
    initialArticles();
    initialTabs();
  }
});

let user;
// 获取所有的文章分类
const interests = ref<Interest[]>([]); // 文章分类信息

// 文章描述信息
const articles = ref<News[]>([]);

const activeName = ref("tabs5");
// 标签行信息
const allNumber = ref(0);
const allSeeableNumber = ref(0);
const meSeeableNumber = ref(0);
const auditNumber = ref(0);
const draftNumber = ref(0);
const recycleBinNumber = ref(0);

// 搜索信息
const startTime = ref<Date>();
const endTime = ref<Date>();
const category = ref<number>();
const searchContent = ref<string>();

// 分页条信息
const newsNumber = ref<number>();
const currentPage = ref<number>(1);
// 监听值的变化
watch(activeName, (value) => {
  switch (value) {
    case "tabs0":
      newsNumber.value = auditNumber.value;
      break;
    case "tabs1":
      newsNumber.value = meSeeableNumber.value;
      break;
    case "tabs2":
      newsNumber.value = allSeeableNumber.value;
      break;
    case "tabs3":
      newsNumber.value = draftNumber.value;
      break;
    case "tabs4":
      newsNumber.value = recycleBinNumber.value;
      break;
    default:
      newsNumber.value = allNumber.value;
  }
});

function initialInterests() {
  axios({
    method: "get",
    url: gatewayUrl + "/news/interests",
  }).then((resp) => {
    interests.value = resp.data.data;
  });
}

// 初始化标签行信息
function initialTabs() {
  axios({
    method: "get",
    url: gatewayUrl + "/news/tabs",
  }).then((resp) => {
    const array = resp.data.data;
    auditNumber.value = array[0];
    meSeeableNumber.value = array[1];
    allSeeableNumber.value = array[2];
    draftNumber.value = array[3];
    recycleBinNumber.value = array[4];
    allNumber.value = array[0] + array[1] + array[2] + array[3] + array[4];
    newsNumber.value = allNumber.value; // 默认情况下
  });
}

// 分页查询文章信息
function newsPage(page: number) {
  currentPage.value = page;
  initialArticles();
}

// 查询指定文章信息
function initialArticles() {
  axios({
    method: "get",
    url: gatewayUrl + "/news/manage_keyWord",
    params: {
      startTime: startTime.value,
      endTime: endTime.value,
      category: category.value,
      searchContent: searchContent.value,
      activeName: activeName.value, // 删选依据之一
      page: currentPage.value,
    },
  }).then((resp) => {
    articles.value = resp.data.data;
  });
}

const getCategory = (categoryId: bigint, index: number) => {
  axios({
    method: "get",
    url: gatewayUrl + "/news/category",
    params: {
      categoryId: categoryId,
    },
  }).then((resp) => {
    articles.value[index].name = resp.data.data;
  });
};

const drop = (newsId: bigint, index: number) => {
  ElMessageBox.confirm(
    "确定要删除文章，删除的文章将放入回收站。回收站最长保存时间为30天",
    "Warning",
    {
      confirmButtonText: "是",
      cancelButtonText: "否",
      type: "warning",
    }
  )
    .then(() => {
      axios({
        method: "delete",
        url: gatewayUrl + "/news/delete",
        params: {
          newsId: newsId,
        },
      }).then((resp) => {
        ElMessage({
          type: resp.data.code == 200 ? "success" : "error",
          message: resp.data.message,
        });
        initialArticles();
      });
    })
};

const edit = (newsId: bigint, index: number) => {
  router.push({
    path: "/main/write_news",
    query: {
      newsId: newsId,
    },
  });
};
// 预览新闻
const preview = (newsId: bigint, index: number) => {
  localStorage.setItem(
    "news",
    JSON.stringify(articles.value[index].basicInformation)
  );
  router.push("/main/news_message");
};

/**
 * 查看筛选文章信息
 */
const handleClick = (tab: TabsPaneContext, event: Event) => {
  initialArticles();
};
</script>

<style scoped>
.demo-tabs > .el-tabs__content {
  padding: 32px;
  color: #6b778c;
  font-size: 32px;
  font-weight: 600;
}

#main_content {
  margin-top: 20px;
}

#pagination {
  margin: 10px 0px;
}
.article_item {
  border: 1px solid #dddddd;
  margin-top: 20px;
  background-color: #ffffff;
}
</style>
