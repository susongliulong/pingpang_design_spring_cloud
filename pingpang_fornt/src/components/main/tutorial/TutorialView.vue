<template>
  <div class="">
    <!-- 通过:label来实现默认初选 -->

    <el-container>
      <el-aside width="200px">
        <el-radio-group v-model="isCollapse" style="margin-bottom: 10px">
          <el-radio-button :value="false">详细面板</el-radio-button>
          <el-radio-button :value="true">简要面板</el-radio-button>
        </el-radio-group>

        <el-menu default-active="abc" class="el-menu-vertical" :collapse="isCollapse">
          <el-sub-menu v-for="(menu, index) in menus" :index="menu.index">
            <template #title>
              <span>{{ menu.title }}</span>
            </template>
            <el-menu-item v-for="(item, index2) in menu.items" :index="item.index" @click="handleClick(item)">
              <span>{{ item.title }}</span>
            </el-menu-item>
          </el-sub-menu>
        </el-menu>
      </el-aside>
      <el-main>
        <MdPreview :model-value="text"></MdPreview>
        <!-- 放置评论界面 -->
        <CommentVue></CommentVue>
      </el-main>
    </el-container>


  </div>
</template>
<script setup lang="ts">
import { ref, onBeforeMount } from "vue"
import CommentVue from "@/components/comment/CommentVue.vue";
// markDown依赖
// 参考链接https://imzbf.github.io/md-editor-v3/en-US/demo
import { MdEditor } from 'md-editor-v3';
import { MdPreview, MdCatalog } from 'md-editor-v3';
import 'md-editor-v3/lib/style.css';

// 按照elementUI要求设置菜单项
interface Menuitem {
  index: string,
  title: string,
  href: string
}

interface Menu {
  index: string,
  title: string,
  items: Menuitem[]
}

const menus = ref<Menu[]>([
  {
    index: "1",
    title: "入门级教程",
    items: [
      {
        index: "1-1",
        title: "认识乒乓球",
        href: "url"
      },
      {
        index: "1-2",
        title: "乒乓球的历史",
        href: "url"
      },
    ]
  },
  {
    index: "2",
    title: "入门级教程",
    items: [
      {
        index: "2-1",
        title: "认识乒乓球",
        href: "url"
      },
      {
        index: "2-2",
        title: "乒乓球的历史",
        href: "url"
      },
    ]
  }
])

const isCollapse = ref(false)

const text = "## 二级标题"

onBeforeMount(() => {
  initialMenus();
})

// 初始化一级菜单和二级菜单信息
const initialMenus = () => {

}


// 点击某一个子菜单的时候，显示对应的文章信息
const handleClick = (item: Menuitem) => {
  console.log(item);
}



</script>
<style scoped>
.el-menu-vertical:not(.el-menu--collapse) {
  width: 178px;
  min-height: 400px;
  max-height: 600px;
}
.el-main {
    --el-main-padding: 0px;
    --el-main-width:600px;
}

</style>
