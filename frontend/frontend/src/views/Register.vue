<template>
  <div class="register-page">
    <div class="register-container">
      <h2 class="register-title">注册</h2>
      <form @submit.prevent="handleRegister" class="register-form">
        <div class="form-group">
          <label for="username">用户名:</label>
          <input type="text" v-model="username" @blur="debouncedCheckUsername" required />
          <p v-if="usernameError" style="color: red;">{{ usernameError }}</p>
        </div>
        <div class="form-group">
          <label for="email">邮箱:</label>
          <input type="email" v-model="email" required />
        </div>
        <div class="form-group">
          <label for="password">密码:</label>
          <input type="password" v-model="password" @input="validatePassword" required />
          <p v-if="passwordError" style="color: red;">{{ passwordError }}</p>
        </div>
        <div class="form-group">
          <label for="confirmPassword">确认密码:</label>
          <input type="password" v-model="confirmPassword" required />
          <p v-if="passwordMismatchError" style="color: red;">{{ passwordMismatchError }}</p>
        </div>
        <div class="form-group">
          <label for="role">选择身份:</label>
          <select v-model="role" required>
            <option disabled value="">请选择身份</option>
            <option value="USER">普通用户</option>
            <option value="ADMIN">管理员</option>
          </select>
        </div>
        <button type="submit" class="register-button" :disabled="!isFormValid">注册</button>
        <br>
        <button type="button" class="reset-button" @click="resetForm">重置</button>
      </form>
      <p>
        已有账号？ <a @click.prevent="goToLogin" href="#">点击登录</a>
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
import _ from 'lodash';  // 确保您已经安装 lodash: npm install lodash

export default {
  name: 'Register',
  data() {
    return {
      username: '',
      email: '',
      password: '',
      confirmPassword: '',
      role: '',
      usernameError: '',
      passwordError: '',
      passwordMismatchError: '',
      isFormValid: false
    }
  },
  methods: {
    checkUsername() {
      if (this.username) {
        axios.get(`http://localhost:8080/api/check-username/${this.username}`)
          .then(response => {
            if (response.data.exists) {
              this.usernameError = "用户名已存在";
            } else {
              this.usernameError = '';
            }
            this.validateForm();
          })
          .catch(error => {
            console.error("检查用户名失败:", error);
          });
      }
    },
    validatePassword() {
      if (this.password.length < 8 || !/\d/.test(this.password) || !/[a-zA-Z]/.test(this.password)) {
        this.passwordError = "密码必须包含8-16个字符，并且包含字母和数字";
      } else {
        this.passwordError = '';
      }
      this.validateForm();
    },
    validateForm() {
      this.passwordMismatchError = this.password !== this.confirmPassword ? "密码不匹配" : '';
      this.isFormValid = !this.usernameError && !this.passwordError && !this.passwordMismatchError && this.role;
    },
    handleRegister() {
      if (!this.isFormValid) {
        alert("请先修正表单中的错误");
        return;
      }

      const userData = {
        username: this.username,
        email: this.email,
        password: this.password,
        role: this.role
      };

      axios.post('http://localhost:8080/api/register', userData)
        .then(response => {
          alert("注册成功");
          this.$router.push('/login');  // 跳转到登录页面
        })
        .catch(error => {
          console.error("注册失败:", error);
          alert("注册失败，请检查控制台错误信息");
        });
    },
    resetForm() {
      this.username = '';
      this.email = '';
      this.password = '';
      this.confirmPassword = '';
      this.role = '';
      this.usernameError = '';
      this.passwordError = '';
      this.passwordMismatchError = '';
      this.isFormValid = false;
    },
    goToLogin() {
      this.$router.push('/login');
    }
  },
  mounted() {
    this.debouncedCheckUsername = _.debounce(this.checkUsername, 500);
  },
  watch: {
    confirmPassword() {
      this.validateForm();
    },
    role() {
      this.validateForm();
    }
  }
}
</script>

<style scoped>
.register-page {
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

.register-page::before {
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

.register-container {
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

.register-title {
  font-size: 2rem;
  font-weight: bold;
  text-align: center;
  margin-bottom: 20px;
}

.register-form {
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
