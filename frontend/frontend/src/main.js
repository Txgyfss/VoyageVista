
import './assets/main.css'
import axios from 'axios'
import { createApp } from 'vue'
import App from './App.vue'
import router from './router' // 确保导入你的路由配置
import * as Vue from 'vue'
import UserSidebar from '@/components/UserSidebar.vue'; // 导入UserSidebar组件
import AdminSidebar from '@/components/AdminSidebar.vue'; // 导入UserSidebar组件
// 创建Vue应用实例
const app = createApp(App)
app.config.compilerOptions.isCustomElement = tag => tag.startsWith('v-');
// 设置 Axios 的全局基础 URL
axios.defaults.baseURL = 'http://localhost:8080/api';  // 后端 API 的基础 URL
// 全局注册组件
app.component('UserSidebar', UserSidebar);
app.component('ASidebar', AdminSidebar);
// 全局配置axios
app.config.globalProperties.$axios = axios
// 引入全局样式文件
import './assets/style.css';

// 导入 Admin 侧边栏组件
//import AdminSidebar from './components/AdminSidebar.vue';

// 注册全局组件
//Vue.component('admin-sidebar', AdminSidebar);

// 导入 User 侧边栏组件
//import UserSidebar from './components/UserSidebar.vue';

// 注册全局组件
//Vue.component('user-sidebar', UserSidebar);

// 使用路由
app.use(router)

// 挂载应用
app.mount('#app')

