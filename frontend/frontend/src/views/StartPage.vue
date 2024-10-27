<template>
  <div class="start-page">
    <h6 class="title">Welcome to Voyage Vista
    </h6>
    <h1 class="title">欢迎来到云梦澜途</h1>
    <div class="button-group">
      <button @click="goToLogin" class="btn">登录</button>
      <button @click="goToRegister" class="btn">注册</button>
    </div>
  </div>
</template>

<script>
export default {
  methods: {
    goToLogin() {
      this.$router.push({ name: 'login' });
    },
    goToRegister() {
      this.$router.push({ name: 'register' });
    }
  }
}
</script>

<style scoped>
.start-page {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 100vh;
  width: 100vw;
  background-image: url('../src/images/4423.jpg_wh300.jpg');
  background-size: cover;
  background-position: left center;
  background-repeat: no-repeat;
  /* 删除或调整以下两行，查看图片效果 */
  position: relative;
  overflow: hidden;
}

.start-page::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(255, 255, 255, 0.3);
  /* 30%的透明度 */
}

.title {
  font-size: 3rem;
  color: #00838f;
  margin-bottom: 20px;
  text-transform: uppercase;
  letter-spacing: 2px;
  font-weight: bold;
  animation: fadeIn 3s ease-in-out infinite alternate;
  z-index: 1;
  position: relative;
}

.button-group {
  display: flex;
  gap: 20px;
  z-index: 1;
}

.btn {
  background-color: #00acc1;
  color: white;
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  font-size: 1.2rem;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.btn:hover {
  background-color: #007c91;
}

.btn:active {
  background-color: #004d60;
}

@keyframes fadeIn {
  0% {
    opacity: 0;
    transform: translateY(-20px);
  }

  100% {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>
