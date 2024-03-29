<template>
    <div class="">
        <div class="box" id="create_article">
            <p>创作中心</p>
            <div class="first clear">

                <div class="item" @click="writeTutorial">
                    <a href="">
                        <Edit
                            style="width: 30px; height: 30px; display: block;margin-left: 15px;margin-bottom: 10px;" />
                        <span>发布教程</span>
                    </a>
                </div>

                <div class="item" @click="router.push('/main/write_match')">
                    <a href="">
                        <Odometer
                            style="width: 30px; height: 30px; display: block;margin-left: 15px;margin-bottom: 10px;" />
                        <span>发布赛事</span>
                    </a>
                </div>

                <div class="item" @click="router.push('/main/write_news')">
                    <a href="">
                        <Message
                            style="width: 30px; height: 30px; display: block;margin-left: 15px;margin-bottom: 10px;" />
                        <span>发布资讯</span>
                    </a>
                </div>
            </div>
            <p style="font-size: 14px; color: black;margin-left: 20px;">开启你的创作之旅</p>
            <p style="font-size: 12px; color: #056de8;margin-left: 20px;margin-bottom: 20px;">感受乒乓球的魅力</p>

            <div class="second">
                <el-button type="primary" size="small" style="height: 40px;">
                    <span>总点赞数{{ userMessage.fans }}</span>
                </el-button>

                <el-button type="primary" size="small" style="height: 40px;">
                    <span>总关注数{{ userMessage.focus }}</span>
                </el-button>

                <el-button type="primary" size="small" style="height: 40px;">
                    <span>总粉丝数{{ userMessage.fans }}</span>
                </el-button>
            </div>

            <div style="margin-top: 30px;">
                <el-button type="primary" size="small" style="height: 40px;width: 324px;" @click="router.push('/main/write_news')">
                    <span>进入创作中心</span>
                </el-button>
            </div>
        </div>

        <div class="box">
            <p>赛事推荐</p>
            <div v-for="(item, index) in matches" :index="index" class="item">
                <div style="font-weight: bold;">{{ item.name }}</div>
                <p>比赛时间：{{ item.time }}</p>
                <p>比赛场地：{{ item.address }}</p>
                <p>比赛最低积分要求：{{ item.minPoints }} 积分奖励：{{ item.awrards }}</p>
            </div>
        </div>
    </div>
</template>
<script setup lang="ts">
import { ref, reactive } from "vue"
import { useRouter } from "vue-router";

//用户基本信息
interface UserMessage {
    img: String,
    fans: number,
    focus: number,
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


const userMessage = ref<UserMessage>({
    img: "background.png",
    fans: 1000,
    focus: 1000,
})

const matches = ref<Match[]>([{
    matchId: 123,
    name: "计算机学院将举办新生杯大赛",
    address: "学校体育楼球馆",
    time: new Date(),
    minPoints: 24,// 最低积分要求
    awrards: 48 // 积分奖励
}])

const router = useRouter();

function writeTutorial(){
    router.push('/main/write_tutorial')
}



</script>
<style scoped>
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
    margin-right: 50px;
}

.box .first .item:last-child {
    margin-right: 0px;
}
</style>
