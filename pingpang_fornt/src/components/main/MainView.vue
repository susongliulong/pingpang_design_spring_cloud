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
                    placeholder="尝试全站搜索" @select="handleSelect" :trigger-on-focus="false">
                </el-autocomplete>
                <button v-on:click="searchArticles">搜索</button>
            </div>

            <div class="right">
                <span><i><img :src=userMessage.img style="width: 40px; height: 40px;"></i></span>
                <span @click="router.push('/main')"><i class="el-icon-edit" style="font-size: 24px;"></i><a href="javaScript:void(0);">进行创作</a></span>
                <span><i class="el-icon-message" style="font-size: 24px;"></i><a href="">消息</a></span>
            </div>
        </div>

        <div id="main" class="clear">

            <div >
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
import { ref } from "vue"
import { onBeforeMount, onMounted } from "vue";
import type { TabsPaneContext } from 'element-plus'
import { useRouter } from "vue-router"

//用户基本信息
interface UserMessage {
    img: String,
    fans: number,
    focus: number,
}

// 文章描述信息
interface Article {
    title: String,
    img: String,
    mainContent: String,
    likes: number,
    dislikes: number,
    watches: number, // 用户收藏量
}

// 推荐关键词列表
interface LinkItem {
    value: string
    link: string
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

const router = useRouter()
const keyWord = ref('')
const userMessage = ref<UserMessage>({
    img: "background.png",
    fans: 1000,
    focus: 1000,
})
const articles = ref<Article[]>([{}])
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
    }
})

onMounted(() => {
    links.value = loadAll()
})

// tabs栏目选中的时候信息跳转
const handleClick = (tab: TabsPaneContext) => {
    router.push("/main/"+tab.props.name);
}

//  初始化文章信息
function init() {

}

let timeout: any;

// 在搜索框键入关键词以后进行搜索推荐
const querySearch = (queryString: string, cb: (arg: any) => void) => {
    const results = queryString
        ? links.value.filter(createFilter(queryString))
        : links.value

    clearTimeout(timeout)
    timeout = setTimeout(() => {
        cb(results)
    }, 3000 * Math.random())
}

// 过滤函数
const createFilter = (queryString: string) => {
    return (restaurant: LinkItem) => {
        return (
            restaurant.value.toLowerCase().indexOf(queryString.toLowerCase()) === 0
        )
    }
}

// 加载后台的关键数据
function loadAll() {
    return [
        { value: '张继科', link: 'https://github.com/vuejs/vue' },
        { value: '马龙', link: 'https://github.com/ElemeFE/element' },
        { value: '樊振东', link: 'https://github.com/ElemeFE/cooking' },
        { value: '梁靖坤', link: 'https://github.com/ElemeFE/mint-ui' },
        { value: '许昕', link: 'https://github.com/vuejs/vuex' },
        { value: '刘国梁', link: 'https://github.com/vuejs/vue-router' },
        { value: '王皓', link: 'https://github.com/babel/babel' },
    ]
}

// 关键词在输入框回显
const handleSelect = (item: LinkItem) => {
    console.log(item)
}

// 点击文章搜索搜索文章
const searchArticles = () => {

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
    height: 800px;
}



#aside {
    width: 30%;
    float: left;
    margin-left: 10px;
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
