import { createRouter, createWebHistory } from "vue-router";

import LoginView from "@/components/login_register/LoginView.vue";
import RegisterView from "@/components/login_register/RegisterView.vue";
import NullVue from "@/NullVue.vue";

// 登陆成功以后用户主界面
import MainView from "@/components/main/MainView.vue";
import AsideBar from "@/components/main/AsideBar.vue";
import NewsView from "@/components/main/news/NewsView.vue";
import TutorialView from "@/components/main/tutorial/TutorialView.vue";
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: "/", redirect: "/login" },
    { path: "/login", component: LoginView },
    { path: "/register", component: RegisterView },
    {
      path: "/main",
      redirect:"/main/news",// 重定向到乒乓球资讯界面
      component: MainView,
      children: [
        // 配置子路由
        {
          path: "news",
          name:"news",
          components: {
            MainContent: NewsView,
            AsideBar:AsideBar
          },
        },
        {
          path: 'tutorial',
          name:'tutorial',
          components: {
            MainContent: TutorialView,
            AsideBar:NullVue
          }
        }
      ],
    },
  ],
});

export default router;
