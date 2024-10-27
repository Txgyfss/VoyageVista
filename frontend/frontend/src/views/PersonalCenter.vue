<template>
  <div class="content">
    <div class="personal-center">
      <h2>个人中心</h2>

      <div v-if="user">
        <img :src="getImageUrl(user.avatarUrl)" alt="头像" class="avatar" @error="handleImageError" />
        <form @submit.prevent="uploadAvatar" class="form-section">
          <label for="avatar">更改头像:</label>
          <input type="file" @change="onFileChange" class="input-file" />
          <button type="submit" class="btn-primary">上传头像</button>
        </form>

        <form @submit.prevent="updateUserInfo" class="form-section">
          <label>用户名:</label>
          <input v-model="user.username" class="input-text" />

          <label>邮箱:</label>
          <input v-model="user.email" class="input-text" />

          <label>电话:</label>
          <input v-model="user.phoneNumber" class="input-text" />

          <button type="submit" class="btn-primary">保存修改</button>
        </form>

        <form @submit.prevent="changePassword" class="form-section">
          <label>原密码:</label>
          <input type="password" v-model="oldPassword" class="input-text" />

          <label>新密码:</label>
          <input type="password" v-model="newPassword" class="input-text" />

          <button type="submit" class="btn-danger">修改密码</button>
        </form>

        <p v-if="feedback" class="feedback">{{ feedback }}</p>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import { jwtDecode } from 'jwt-decode';  // 引入 jwt-decode 库

export default {
  data() {
    return {
      user: null,              // 用户信息
      oldPassword: '',         // 原密码
      newPassword: '',         // 新密码
      feedback: '',            // 用户反馈信息
      avatarFile: null,        // 上传的头像文件
      userId: null,            // 登录用户ID
    };
  },
  mounted() {
    this.getUserIdFromToken(); // 获取用户ID
    this.fetchUser(); // 加载用户信息
  },
  methods: {
    fetchUser() {
      if (this.userId) {
        axios.get(`http://localhost:8080/api/personal-center/${this.userId}`)
          .then(response => {
            this.user = response.data; // 使用 DTO 中的用户数据
            console.log("获取到的用户数据:", response.data); // 打印获取到的用户数据
          })
          .catch(error => {
            console.error('获取用户信息失败', error);
            this.handleFetchError(error);
          });
      }
    },

    // 从JWT令牌获取用户ID
    getUserIdFromToken() {
      const token = localStorage.getItem('token');  // 从本地存储获取JWT令牌
      if (token) {
        try {
          const decodedToken = jwtDecode(token);      // 解码JWT令牌
          this.userId = decodedToken.userId;          // 假设你的JWT中包含userId
        } catch (error) {
          console.error("JWT解码失败", error);
          this.redirectToLogin();                     // 如果解码失败，重定向到登录页面
        }
      } else {
        console.error("JWT token not found");
        this.redirectToLogin();                       // 如果找不到token，重定向到登录页面
      }
    },
    // 获取用户信息
    fetchUser() {
      if (this.userId) {
        axios.get(`http://localhost:8080/api/personal-center/${this.userId}`)
          .then(response => {
            console.log("获取到的用户数据:", response.data);  // 打印获取到的用户数据
            this.user = response.data;               // 成功获取用户信息
          })
          .catch(error => {
            console.error('获取用户信息失败', error);
            this.handleFetchError(error);            // 处理获取用户信息失败
          });
      }
    },
    // 生成图片的完整URL
    getImageUrl(imageUrl) {
      return `http://localhost:8080${imageUrl}`;  // 假设头像路径存储在 `/image/user/` 目录
    },
    // 处理图片加载错误
    handleImageError(event) {
      console.error('图片加载失败: ', event.target.src);
      event.target.src = 'src/images/1.jpg';  // 设置一个默认头像
    },
    // 重定向到登录页面
    redirectToLogin() {
      this.$router.push('/login');                    // 假设你有一个登录页面的路由
    },
    // 处理获取用户信息失败
    handleFetchError(error) {
      if (error.response && error.response.status === 401) {
        console.error("未授权，重定向到登录页面");
        this.redirectToLogin();                      // 如果未授权，重定向到登录
      } else {
        console.error("未知错误", error);
      }
    },
    // 更新用户信息
    updateUserInfo() {
      axios.put(`http://localhost:8080/api/personal-center/${this.userId}`, this.user)
        .then(() => {
          this.feedback = '用户信息更新成功';
        })
        .catch(error => {
          console.error('更新用户信息失败', error);
          this.feedback = '更新用户信息失败';
        });
    },
    // 更改密码
    changePassword() {
      if (this.newPassword === this.oldPassword) {
        this.feedback = '新密码不能与原密码相同';
        return;
      }
      axios.put(`http://localhost:8080/api/personal-center/change-password/${this.userId}`, null, {
        params: {
          oldPassword: this.oldPassword,
          newPassword: this.newPassword
        }
      })
        .then(() => {
          this.feedback = '密码修改成功';
        })
        .catch(error => {
          console.error('修改密码失败', error);
          this.feedback = '修改密码失败: 原密码不正确';
        });
    },
    // 处理头像文件上传
    onFileChange(event) {
      this.avatarFile = event.target.files[0];
    },
    // 上传头像
    uploadAvatar() {
      if (this.avatarFile) {
        const formData = new FormData();
        formData.append('avatar', this.avatarFile);

        axios.post(`http://localhost:8080/api/personal-center/upload-avatar/${this.userId}`, formData, {
          headers: {
            'Content-Type': 'multipart/form-data'
          }
        })
          .then(() => {
            this.feedback = '头像上传成功';
            this.fetchUser();  // 上传成功后重新获取用户信息，更新头像
          })
          .catch(error => {
            console.error('头像上传失败', error);
            this.feedback = '头像上传失败';
          });
      } else {
        this.feedback = '请选择头像文件';
      }
    }
  }
};
</script>

<style scoped>
/* 全局青色系配色方案 */
:root {
  --primary-color: #00bcd4;
  /* 青色主色 */
  --primary-hover-color: #00acc1;
  --danger-color: #e74c3c;
  --danger-hover-color: #c0392b;
  --background-color: #e0f7fa;
  /* 青色背景 */
  --text-color: #006064;
  /* 深青色文本 */
  --white-color: #ffffff;
}

/* 页面主体样式 */
.personal-center {
  max-width: 600px;
  margin: 40px auto;
  padding: 20px;
  background-color: var(--background-color);
  /* 青色背景 */
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  text-align: center;
}

/* 标题样式 */
h2 {
  color: var(--text-color);
  /* 青色文本 */
  margin-bottom: 30px;
  font-size: 28px;
}

/* 头像样式 */
.avatar {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  margin-bottom: 20px;
  object-fit: cover;
  border: 3px solid var(--primary-color);
  /* 青色边框 */
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.2);
  transition: transform 0.3s ease;
}

.avatar:hover {
  transform: scale(1.05);
  /* 鼠标悬停时头像放大 */
}

/* 表单部分 */
.form-section {
  margin-bottom: 30px;
  text-align: left;
}

label {
  display: block;
  margin: 10px 0 5px;
  font-weight: bold;
  color: var(--text-color);
  /* 青色文本 */
}

.input-text,
.input-file {
  display: block;
  width: 100%;
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 8px;
  margin-bottom: 15px;
  transition: border-color 0.3s ease;
}

.input-text:focus,
.input-file:focus {
  border-color: var(--primary-color);
  outline: none;
}

/* 按钮样式 */
button {
  padding: 12px 25px;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.3s ease, box-shadow 0.3s ease;
}




/* 主按钮（上传/保存） */
.btn-primary {
  background-color: var(--primary-color);
  color: var(--white-color);
  background-color: #4caf50;
  color: white;
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease, transform 0.3s ease;

}

.btn-primary:hover {
  background-color: var(--primary-hover-color);
  box-shadow: 0 4px 12px rgba(0, 188, 212, 0.4);
  background-color: #388e3c;
  transform: translateY(-3px);
  /* 青色阴影 */
}

/* 危险按钮（修改密码） */
.btn-danger {
  background-color: var(--danger-color);
  color: var(--white-color);
  background-color: #4caf50;
  color: white;
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease, transform 0.3s ease;
}

.btn-danger:hover {
  background-color: var(--danger-hover-color);
  box-shadow: 0 4px 12px rgba(231, 76, 60, 0.4);
  background-color: #388e3c;
  transform: translateY(-3px);
  /* 青色阴影 */
  /* 红色阴影 */
}

/* 用户反馈信息 */
.feedback {
  margin-top: 20px;
  font-size: 16px;
  color: var(--primary-color);
  font-weight: bold;
}

p {
  margin-top: 20px;
  color: var(--primary-color);
  /* 青色 */
}

/* 内容部分样式 */
.content {
  margin-left: 0px;
  flex-grow: 1;
  padding: 0px;
  width: 1300px;
  overflow-y: auto;
}
</style>