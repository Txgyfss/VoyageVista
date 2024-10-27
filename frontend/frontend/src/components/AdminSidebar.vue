<template>
  <div class="adminsidebar">
    <nav>
      <ul>
        <li><router-link to="/Admin">首页</router-link></li>
        <li><router-link to="/user-management">用户管理</router-link></li>
        <li><router-link to="/attraction-management">景点管理</router-link></li>
        <li><router-link to="/hotel-management">酒店管理</router-link></li>
        <li><router-link to="/food-management">美食管理</router-link></li>
        <li><router-link to="/guide-management">攻略管理</router-link></li>
        <li><router-link to="/blog-management">博客管理</router-link></li>
        <li><router-link to="/news-management">资讯管理</router-link></li>
        <li><router-link to="/personal-center">个人中心</router-link></li>
        <li><router-link to="/">退出登录</router-link></li>
      </ul>
    </nav>
  </div>
</template>

<script>
export default {
  name: 'adminSidebar',
}
</script>

<style scoped>
.adminsidebar {
  width: 200px;
  background-color: #f4f4f4;
  padding: 20px;
}

.adminsidebar ul {
  list-style-type: none;
  padding: 0;
}

.adminsidebar ul li {
  margin-bottom: 10px;
}

.adminsidebar ul li a {
  text-decoration: none;
  color: #333;
}

.adminsidebar ul li a:hover {
  text-decoration: underline;
}

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
