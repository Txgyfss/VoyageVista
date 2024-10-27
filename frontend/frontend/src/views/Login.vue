<template>
  <div class="login-page">
    <div class="login-container">
      <h2 class="login-title">登录</h2>
      <form @submit.prevent="handleLogin" class="login-form">
        <div class="form-group">
          <label for="identity">身份:</label>
          <select v-model="identity">
            <option value="user">用户</option>
            <option value="admin">管理员</option>
          </select>
        </div>
        <div class="form-group">
          <label for="username">用户名或邮箱:</label>
          <input type="text" v-model="username" required />
        </div>
        <div class="form-group">
          <label for="password">密码:</label>
          <input type="password" v-model="password" required />
        </div>
        <button type="submit" class="login-button">登录</button>
      </form>
      <p>
        没有账号？ <a @click.prevent="goToRegister" href="#">点击注册</a>
      </p>
    </div>

    <div class="carousel-container">
      <div class="carousel">
        <img src="@/images/start1.png" alt="Image 1" />
        <img src="@/images/start2.png" alt="Image 2" />
        <img src="@/images/start3.png" alt="Image 3" />
        <img src="@/images/start4.png" alt="Image 4" />
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'Login',
  data() {
    return {
      identity: 'user',
      username: '',
      password: ''
    };
  },
  methods: {
    handleLogin() {
      const loginData = {
        login: this.username,
        password: this.password,
        identity: this.identity
      };

      axios.post('http://localhost:8080/api/login', loginData, {
        headers: {
          'Content-Type': 'application/json'
        }
      })
        .then(response => {
          const token = response.data.token;
          const userRole = response.data.role;  // 获取角色信息
          localStorage.setItem('token', token);  // 存储JWT

          if (userRole === 'ADMIN') {
            alert("登录成功，欢迎管理员！");
            this.$router.push('/admin');// 跳转到管理员页面
          } else if (userRole === 'USER') {
            alert("登录成功，欢迎用户！");
            this.$router.push('/home');  // 跳转到普通用户首页
          } else {
            alert("出问题了！！");
          }
        })
        .catch(error => {
          console.error("登录失败:", error);
          alert("登录失败，请检查您的用户名和密码");
        });
    },
    goToRegister() {
      this.$router.push('/register');
    }
  }
}
</script>

<style scoped>
.login-page {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 100vh;
  /* 设置为视口高度，保证页面全屏 */
  width: 100vw;
  /* 设置为视口宽度，保证页面宽度覆盖整个页面 */
  background-image: url('../src/images/4423.jpg_wh300.jpg');
  /* 背景图片路径 */
  background-size: cover;
  /* 确保背景图片覆盖整个页面 */
  background-position: center;
  /* 背景图片居中显示 */
  background-repeat: no-repeat;
  /* 防止背景重复 */
  position: relative;
  overflow: hidden;
  /* 防止溢出 */
}

.login-page::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(255, 255, 255, 0.5);
  /* 50% 透明度 */
  z-index: 0;
}

.login-container {
  position: relative;
  z-index: 1;
  max-width: 400px;
  margin: auto;
  padding: 20px;
  background-color: rgba(255, 255, 255, 0.4);
  /* 白色背景，透明度80% */
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.login-title {
  font-size: 2rem;
  font-weight: bold;
  text-align: center;
  margin-bottom: 20px;
}

.login-form {
  display: flex;
  flex-direction: column;
}

.form-group {
  margin-bottom: 15px;
}

input,
select {
  padding: 10px;
  border-radius: 5px;
  border: 1px solid #ccc;
  width: 100%;
  box-sizing: border-box;
}

input:focus {
  outline: none;
  box-shadow: 0 0 10px rgba(0, 172, 193, 0.8);
  /* 青色光效果 */
  border-color: #00acc1;
  /* 青色边框 */
}

button {
  padding: 10px 15px;
  background-color: #00838f;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

button:hover {
  background-color: #00838f;
}

p {
  text-align: center;
  margin-top: 15px;
}

a {
  color: #007BFF;
  cursor: pointer;
  transition: color 0.3s ease;
}

a:hover {
  color: #0056b3;
  text-decoration: underline;
}

/* 旋转柱形轮播器的样式 */
.carousel-container {
  width: 300px;
  height: 300px;
  perspective: 1000px;
  /* 3D 视角 */
  position: relative;
  margin-left: 20px;
}

.carousel {
  width: 100%;
  height: 100%;
  position: absolute;
  transform-style: preserve-3d;
  animation: rotateCylinder 10s infinite linear;
}

.carousel img {
  width: 300px;
  height: 200px;
  object-fit: cover;
  /* 裁剪图片 */
  position: absolute;
  top: 0;
  left: 0;
  transform-origin: 50% 50%;
  /* 以中心为原点旋转 */
  border-radius: 10px;
}

/* 定义每张图片在柱体上的位置 */
.carousel img:nth-child(1) {
  transform: rotateY(0deg) translateZ(400px);
  /* 将图片向外推 */
}

.carousel img:nth-child(2) {
  transform: rotateY(90deg) translateZ(400px);
}

.carousel img:nth-child(3) {
  transform: rotateY(180deg) translateZ(400px);
}

.carousel img:nth-child(4) {
  transform: rotateY(270deg) translateZ(400px);
}

/* 旋转柱形轮播的动画 */
@keyframes rotateCylinder {
  from {
    transform: rotateY(0deg);
  }

  to {
    transform: rotateY(360deg);
  }
}
</style>
