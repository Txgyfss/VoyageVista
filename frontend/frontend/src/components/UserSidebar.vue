<!-- src\components\UserSidebar.vue -->
<template>
  <div class="sidebar">
    <ul>
      <li><router-link to="/home" active-class="active">主页面</router-link></li>
      <li><router-link to="/attractions" active-class="active">景点一览</router-link></li>
      <li><router-link to="/hotels" active-class="active">酒店预订</router-link></li>
      <li><router-link to="/foods" active-class="active">美食品鉴</router-link></li>
      <li><router-link to="/guides" active-class="active">旅游攻略</router-link></li>
      <li><router-link to="/blog" active-class="active">旅游博客</router-link></li>
      <li><router-link to="/news" active-class="active">资讯</router-link></li>
      <li><router-link to="/personal-center" active-class="active">个人中心</router-link></li>
      <li><router-link to="/orders" active-class="active">我的订单</router-link></li>
      <li><router-link to="/" active-class="active">退出登录</router-link></li>
    </ul>
  </div>
</template>

<script>
export default {
  name: "Sidebar",
};
</script>

<style scoped>
.sidebar {
  width: 15%;
  /* 自适应宽度 */
  background-color: rgba(0, 172, 193, 0.3);
  /* 青色系背景，透明度30% */
  padding: 20px;
  position: fixed;
  /* 固定在页面 */
  left: 0;
  /* 确保侧边栏在最左边 */
  top: 0;
  height: 100vh;
  /* 占据全屏高度 */
  overflow-y: auto;
}

ul {
  list-style: none;
  padding: 0;
}

li {
  margin-bottom: 10px;
}

a {
  text-decoration: none;
  color: #006064;
  padding: 10px;
  display: block;
  border-radius: 5px;
  transition: background-color 0.3s ease, box-shadow 0.3s ease;
  text-align: center;
  /* 新增 */
}

a:hover {
  color: #00acc1;
  /* 鼠标悬停时的文字颜色 */
  box-shadow: 0 0 8px rgba(0, 172, 193, 0.8);
  /* 悬停时发青色光 */
}

a.active {
  background-color: rgba(0, 172, 193, 0.2);
  /* 点击后背景变化 */
  color: white;
  /* 激活状态文字变白 */
}

a.active:hover {
  background-color: rgba(0, 172, 193, 0.4);
  /* 悬停时的激活背景变化 */
}
</style>
