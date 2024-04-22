<template>
    <div class="">
        <div id="header" class="clear">
            <div class="left">
                <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
                    <el-tab-pane label="资讯信息" name="news"></el-tab-pane>
                    <el-tab-pane label="乒乓球教程" name="tutorial"></el-tab-pane>
                    <el-tab-pane label="比赛信息" name="match"></el-tab-pane>
                </el-tabs>
            </div>

            <!-- 搜索框 -->
            <div id="search" class="left">
                <el-autocomplete popper-class="my-autocomplete" v-model="keyWord" :fetch-suggestions="querySearch"
                    placeholder="搜索最新资讯" @select="handleSelect" :trigger-on-focus="false">
                </el-autocomplete>
                <button v-on:click="searchArticles">搜索</button>
            </div>

            <div class="right">
                <span id="img" @click="router.push('/main/register')"><i><img :src="userMessage.avatar" style="width: 40px; height: 40px;"></i></span>
                <span @click="router.push('/main')"><i class="el-icon-edit" style="font-size: 24px;"></i><a href="javaScript:void(0);">进行创作</a></span>
                <span><i class="el-icon-message" style="font-size: 24px;"></i><a href="">消息</a></span>
            </div>
        </div>

        <div id="main" class="clear">
            <div id="content">
                <RouterView name="MainContent"></RouterView>
            </div>
            <div id="aside">
                <router-view name="AsideBar">

                </router-view>
            </div>
        </div>
    </div>
</template>
<script setup lang="ts">
import { ref ,watch} from "vue"
import { onBeforeMount, onMounted } from "vue";
import type { TabsPaneContext } from 'element-plus'
import { useRouter,useRoute } from "vue-router"
import axios from "axios";
import gatewayUrl from "@/global";

//用户基本信息
interface UserMessage {
    avatar: String,
    fans: number,
    focus: number,
}

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

// 推荐关键词列表
interface LinkItem {
    value: String
    link: String
}

// 赛事信息
interface Match {
    matchId: bigint,
    name: string,
    address: string,
    time: Date,
    minPoints: number// 最低积分要求
    awrards: number // 积分奖励
}

const router = useRouter();
const route = useRoute();
const keyWord = ref('')
const userMessage = ref<UserMessage>({});
const links = ref<LinkItem[]>([])
const matches = ref<Match[]>([{
    matchId: 123,
    name: "计算机学院将举办新生杯大赛",
    address: "学校体育楼球馆",
    time: new Date(),
    minPoints: 24,// 最低积分要求
    awrards: 48 // 积分奖励
}])

// tabs初始时选中的栏目
const activeName = ref('news')



onBeforeMount(() => {
    const routerName = router.currentRoute.value.name;
    if (routerName !== null && routerName !==undefined) {
        activeName.value = routerName.toString();

        // 跟新照片数据
        const userString=localStorage.getItem("user")
        if (userString != null) {
            const user = JSON.parse(userString).user;
            userMessage.value.avatar = user.avatar;
        }
    }
})



// tabs栏目选中的时候信息跳转
const handleClick = (tab: TabsPaneContext) => {
    router.push("/main/"+tab.props.name);
}


let timeout: any;

// 在搜索框键入关键词以后进行搜索推荐
const querySearch = (queryString: string, cb: (arg: any) => void) => {
    links.value = loadAll();
    const results = links.value
    clearTimeout(timeout)
    timeout = setTimeout(() => {
        cb(results)
    }, 3000 * Math.random())
}


// 加载后台的关键数据
function loadAll() {
    // 加载后台关键词信息
    let links: LinkItem[] =[];
    axios.get(gatewayUrl + "/news/simpleKeyWord", { params: { keyWord: keyWord.value } }).then(resp => {
        resp.data.data.forEach((item: LinkItem) => {
            links.push(item);
        });
    })
    return links;
}

// 关键词在输入框回显
const handleSelect = (item: LinkItem) => {
    console.log(item)
}

// 点击文章搜索搜索文章
const searchArticles = () => {
    router.push({
        path: "/main/news",
        query: {
            keyword: keyWord.value
        }
    })
}

// 展示文章的详情信息
function newsMessage(news:Article) {
    console.log(news);
}

</script>
<style scoped>
/* tab是标签页属性设置 */
.demo-tabs>.el-tabs__content {
    padding: 32px;
    color: #6b778c;
    font-size: 32px;
    font-weight: 600;
}

#header {
    position: fixed;
    z-index: 5;
    width: 100%;
    height: 50px;
    background-color: #ffffff;
    margin-bottom: 10px;
}

#header .left {
    margin-right: 60px;
}

#header .left span {
    float: left;
    margin: 0 20px;
}

#header .right span {
    float: right;
    margin: 0 20px;
}
#header .right #img:hover{
    cursor: pointer;
}

/* 搜索框内容 */
#search{
    margin-top: 8px;
}

#search input {
    height: 30px;
    width: 400px;
    margin-right: 10px;
    background-color: #f5f5f5;
    border-radius: 15px;
    border-color: #ebebeb;
    border: none;
    text-align: center;
    outline: none;
}

#search button {
    margin-left: 10px;
    height: 30px;
    width: 60px;
    border: 1px solid #056de8;
    border-radius: 15px;
    color: white;
    background-color: #056de8;
}

.my-autocomplete {
    li {
        line-height: normal;
        padding: 7px;

        .name {
            text-overflow: ellipsis;
            overflow: hidden;
        }

        .addr {
            font-size: 12px;
            color: #b4b4b4;
        }

        .highlighted .addr {
            color: #ddd;
        }
    }
}


#main {
    position: relative;
    z-index: 1;
    top: 56px;
    width: 100%;
    /* height: 800px; */
}

#content {
  margin: 10px 0px;
  float: left;
}

#aside {
    margin-top: 10px;
    width: 30%;
    float: right;
}

#create_article .first {
    margin-bottom: 20px;
}


.box {
    margin-bottom: 20px;
    padding: 10px;
    /* margin-left: 20px; */
    height: 300px;
    background-color: #ffffff;
    border: 1px solid #e6f0fd;
}

.box .first .item {
    float: left;
    margin-right: 30px;
}

.box .first .item:last-child {
    margin-right: 0px;
}
</style>
