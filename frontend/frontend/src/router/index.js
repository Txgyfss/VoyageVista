import * as Router from 'vue-router';
import StartPage from '@/views/StartPage.vue';
import Login from '@/views/Login.vue';
import Register from '@/views/Register.vue';
import ForgotPassword from '@/views/ForgotPassword.vue';
import Home from '@/views/Home.vue';
import Admin from '@/views/Admin.vue';
import UserManagement from '@/views/UserManagement.vue';
import AttractionManagement from '@/views/AttractionManagement.vue';
import HotelManagement from '@/views/HotelManagement.vue';
import FoodManagement from '@/views/FoodManagement.vue';
import GuideManagement from '@/views/GuideManagement.vue';
import BlogManagement from '@/views/BlogManagement.vue';
import NewsManagement from '@/views/NewsManagement.vue';
import PersonalCenter from '@/views/PersonalCenter.vue';
import TicketManagement from '../views/TicketManagement.vue';
import RoomManagement from '../views/RoomManagement.vue';
import PackageManagement from '@/views/PackageManagement.vue'; // 引入套餐管理页面
import Attractions from '@/views/Attractions.vue';
import AttractionDetails from '@/views/AttractionDetails.vue';
import News from '@/views/News.vue'; // 引入资讯查看页面
import Hotel from '@/views/Hotel.vue';
import HotelBooking from '@/views/HotelBooking.vue';
import Food from '@/views/Food.vue';
import FoodPackage from '@/views/FoodPackage.vue';
import Orders from '@/views/Orders.vue';
import Guide from '@/views/Guides.vue';
import Myguide from '@/views/Myguide.vue';
import Blog from '@/views/Blog.vue';
import Myblog from '@/views/Myblog.vue';

// 定义路由配置
const routes = [

    {
        path: '/blog',  // 美食管理页面的路由路径
        name: 'Blog',
        component: Blog
    },
    {
        path: '/myblog',  // 美食管理页面的路由路径
        name: 'Myblog',
        component: Myblog
    },
    {
        path: '/guides',  // 美食管理页面的路由路径
        name: 'Guide',
        component: Guide
    },
    {
        path: '/myguide',  // 美食管理页面的路由路径
        name: 'Myguide',
        component: Myguide
    },
    {
        path: '/',
        name: 'start',
        component: StartPage
    },
    {
        path: '/login',
        name: 'login',
        component: Login
    },
    {
        path: '/register',
        name: 'register',
        component: Register
    },
    {
        path: '/forgotPassword',
        name: 'forgotPassword',
        component: ForgotPassword
    },
    {
        path: '/home',
        name: 'home',
        component: Home
    },
    {
        path: '/foods-management',  // 美食管理页面的路由路径
        name: 'FoodManagement',
        component: FoodManagement
    },
    {
        path: '/foods-management/:id/packages',  // 套餐管理页面的路由路径，动态匹配美食ID
        name: 'PackageManagement',
        component: PackageManagement,
        props: true  // 允许通过props传递路由参数
    },
    {
        path: '/foods',  // 美食管理页面的路由路径
        name: 'Food',
        component: Food
    },
    {
        path: '/foods/:id/packages',  // 套餐管理页面的路由路径，动态匹配美食ID
        name: 'FoodPackage',
        component: FoodPackage,
        props: true  // 允许通过props传递路由参数
    },
    {
        path: '/admin',
        name: 'admin',
        component: Admin
    },
    {
        path: '/attractions/:id/tickets',
        name: 'TicketManagement',
        component: TicketManagement // 添加票型管理页面路由
    },
    {
        path: '/hotels/:id/rooms',
        name: 'RoomManagement',
        component: RoomManagement,
        props: true // 通过props传递路由参数
    },
    {
        path: '/news',
        name: 'News',
        component: News,
    },
    {
        path: '/hotels',
        name: 'Hotel',
        component: Hotel,
    },
    {
        path: '/hotels/:id',
        name: 'HotelBooking',
        component: HotelBooking,
    },
    {
        path: '/orders',  // 美食管理页面的路由路径
        name: 'Orders',
        component: Orders,
    },
    { path: '/attractions', component: Attractions },
    { path: '/attractions/:id', component: AttractionDetails, name: 'AttractionDetails' },
    { path: '/user-management', component: UserManagement },
    { path: '/attraction-management', component: AttractionManagement },
    { path: '/hotel-management', component: HotelManagement },
    { path: '/food-management', component: FoodManagement },
    { path: '/guide-management', component: GuideManagement },
    { path: '/blog-management', component: BlogManagement },
    { path: '/news-management', component: NewsManagement },
    { path: '/personal-center', component: PersonalCenter }
];

// 创建路由实例
const router = Router.createRouter({
    history: Router.createWebHistory(import.meta.env.BASE_URL),
    routes
});

// 导航守卫
/*router.beforeEach((to, from, next) => {
    const loggedIn = !!localStorage.getItem('user');
    const isAdmin = localStorage.getItem('role') === 'ADMIN';

    if (to.path === '/admin' && !isAdmin) {
        console.log('非管理员用户，重定向到首页');
        next('/home');
    } else if (!loggedIn && to.path !== '/login') {
        console.log('用户未登录，重定向到登录页面');
        next('/login');
    } else {
        next();
    }
});*/

export default router;
