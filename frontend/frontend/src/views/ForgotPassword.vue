<template>
  <div>
    <h2>忘记密码</h2>
    <form @submit.prevent="handleForgotPassword">
      <div>
        <label for="email">邮箱:</label>
        <input type="email" v-model="email" required />
      </div>
      <button type="submit">发送重置密码邮件</button>
    </form>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'ForgotPassword',
  data() {
    return {
      email: ''
    };
  },
  methods: {
    handleForgotPassword() {
      // 使用 axios 发送 POST 请求到后端忘记密码 API
      axios.post('/forgot-password', { email: this.email })
        .then(response => {
          alert("重置密码的指示已发送到您的邮箱");
        })
        .catch(error => {
          console.error("请求失败:", error);
          alert("请求失败，请检查您的邮箱地址是否正确");
        });
    }
  }
}
</script>

<style scoped>
div {
  margin: 20px;
}

label {
  display: block;
  margin-bottom: 5px;
}

input {
  margin-bottom: 10px;
  padding: 8px;
  width: 100%;
  box-sizing: border-box;
}

button {
  padding: 10px 15px;
  background-color: #4CAF50;
  color: white;
  border: none;
  cursor: pointer;
}

button:hover {
  background-color: #45a049;
}
</style>
