

import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import axios from 'axios'
import qs from 'qs'
import JSONBIG from 'json-bigint'

// import Element-ui
import ElementPlus from "element-plus";
import "element-plus/dist/index.css";

// 导入elementUi图标
import * as ElementPlusIconsVue from "@element-plus/icons-vue";
const app = createApp(App)
// axios 全局配置，解决前端传送的参数类型为数组的情况
axios.defaults.paramsSerializer = function(params) {
  return qs.stringify(params, {arrayFormat: 'repeat', allowDots: true})
};
// 处理超过16位的长整数进行处理
axios.defaults.transformResponse = [
  function (data) {
    const json = JSONBIG({
      storeAsString: true,
    });
    const res = json.parse(data);
    return res;
  },
];



// 定位
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component);
}

app.use(router)
app.use(ElementPlus);

app.mount('#app')
