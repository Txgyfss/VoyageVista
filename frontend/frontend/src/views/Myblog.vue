<template>
  <div class="layout">
    <UserSidebar />
  </div>
  <div class="content">
    <div class="blog-container">
      <h1>我的博客</h1>

      <!-- 发布或编辑博客表单 -->
      <div class="form-container">
        <h2>{{ isEditing ? '编辑博客' : '发布新博客' }}</h2>
        <input v-model="blogTitle" placeholder="标题" class="input-title" />
        <textarea v-model="blogContent" placeholder="正文内容" class="input-content"></textarea>

        <!-- 图片上传及预览 -->
        <label for="blogImage" class="upload-label">上传图片:</label>
        <input type="file" @change="handleFileUpload" class="input-file" />
        <div v-if="filePreview" class="image-preview">
          <img :src="filePreview" alt="图片预览" width="100" />
        </div>

        <!-- 标签多选框 -->
        <label>选择标签:</label>
        <div class="tags-checkboxes">
          <label><input type="checkbox" value="自然探险" v-model="selectedTags" /> 自然探险</label>
          <label><input type="checkbox" value="民族文化" v-model="selectedTags" /> 民族文化</label>
          <label><input type="checkbox" value="徒步天堂" v-model="selectedTags" /> 徒步天堂</label>
          <label><input type="checkbox" value="美食之旅" v-model="selectedTags" /> 美食之旅</label>
          <label><input type="checkbox" value="自然奇观" v-model="selectedTags" /> 自然奇观</label>
          <label><input type="checkbox" value="历史足迹" v-model="selectedTags" /> 历史足迹</label>
        </div>

        <!-- 是否公开 -->
        <label for="isPublic">是否公开:</label>
        <input type="checkbox" v-model="isPublic" />

        <!-- 提交按钮 -->
        <button @click="saveBlog" class="submit-button">{{ isEditing ? '更新博客' : '发布博客' }}</button>
      </div>

      <!-- 博客列表 -->
      <div v-if="blogs.length > 0" class="blog-list">
        <div v-for="blog in blogs" :key="blog.blogId" class="blog-item">
          <img v-if="blog.imageUrl" :src="getImageUrl(blog.imageUrl)" alt="博客图片" width="100" />
          <h2>{{ blog.title }}</h2>
          <p>{{ blog.content }}</p>
          <p>标签: {{ blog.tags.join(', ') }}</p>
          <button @click="editBlog(blog)" class="edit-button">编辑</button>
          <button @click="deleteBlog(blog.blogId)" class="delete-button">删除</button>
        </div>
      </div>
      <p v-else>你还没有发布博客</p>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import { jwtDecode } from 'jwt-decode'; // 确保正确引入 jwt-decode 库

export default {
  data() {
    return {
      blogTitle: '',
      blogContent: '',
      selectedTags: [],
      isPublic: true,
      blogs: [],
      userId: null,
      isEditing: false,
      blogId: null,
      file: null, // 保存上传的图片文件
      filePreview: null // 保存图片的预览URL
    };
  },
  methods: {
    // 获取用户的博客
    fetchMyBlogs() {
      axios.get(`http://localhost:8080/api/blogs/my/${this.userId}`)
        .then(response => {
          this.blogs = response.data;
        })
        .catch(error => {
          console.error('获取我的博客失败:', error);
        });
    },
    getUserIdFromToken() {
      const token = localStorage.getItem('token'); // 从本地存储中获取 JWT token
      if (token) {
        try {
          const decodedToken = jwtDecode(token); // 解码 JWT token
          this.userId = decodedToken.userId; // 假设 JWT 中包含 userId
        } catch (error) {
          console.error("JWT 解码失败:", error);
          this.redirectToLogin(); // 如果解码失败，重定向到登录页面
        }
      } else {
        console.error("未找到 JWT token");
        this.redirectToLogin(); // 如果未找到 token，重定向到登录页面
      }
    },
    // 处理图片文件上传
    handleFileUpload(event) {
      this.file = event.target.files[0];
      if (this.file) {
        this.filePreview = URL.createObjectURL(this.file); // 生成图片预览的URL
      }
    },
    // 发布或编辑博客
    saveBlog() {
      // 表单验证
      if (!this.blogTitle || !this.blogContent) {
        alert("请填写标题和正文内容");
        return;
      }

      const blogData = new FormData();  // 使用 FormData 以支持文件上传
      blogData.append("title", this.blogTitle);
      blogData.append("content", this.blogContent);
      blogData.append("isPublic", this.isPublic);
      blogData.append("userId", this.userId);  // 假设你有 userId
      if (this.file) {
        blogData.append("file", this.file);  // 添加图片文件
      }

      // 将 selectedTags 转换为逗号分隔的字符串
      blogData.append("tags", this.selectedTags.join(','));

      if (this.isEditing) {
        // 更新博客，使用 PUT 请求
        axios.put(`http://localhost:8080/api/blogs/${this.blogId}`, blogData)
          .then(() => {
            this.resetForm();
            this.fetchMyBlogs();
          })
          .catch(error => {
            console.error('更新博客失败:', error);
          });
      } else {
        // 创建新博客，使用 POST 请求
        axios.post(`http://localhost:8080/api/blogs`, blogData)
          .then(() => {
            this.resetForm();
            this.fetchMyBlogs();
          })
          .catch(error => {
            console.error('发布博客失败:', error);
          });
      }
    },
    getImageUrl(imageUrl) {
      return `http://localhost:8080${imageUrl}`;
    },
    // 编辑博客
    editBlog(blog) {
      this.isEditing = true;
      this.blogId = blog.blogId;
      this.blogTitle = blog.title;
      this.blogContent = blog.content;
      if (Array.isArray(blog.tags)) {
        this.selectedTags = blog.tags;
      } else if (typeof blog.tags === 'string') {
        this.selectedTags = blog.tags.split(',');
      } else {
        this.selectedTags = [];
      }
      this.isPublic = blog.isPublic;
    },
    // 删除博客
    deleteBlog(blogId) {
      if (confirm('确定要删除该博客吗？')) {
        axios.delete(`http://localhost:8080/api/blogs/${blogId}`)
          .then(() => {
            this.fetchMyBlogs();
          })
          .catch(error => {
            console.error('删除博客失败:', error);
          });
      }
    },
    // 重置表单
    resetForm() {
      this.blogTitle = '';
      this.blogContent = '';
      this.selectedTags = [];
      this.isPublic = true;
      this.isEditing = false;
      this.blogId = null;
      this.file = null;
      this.filePreview = null; // 清除图片预览
    }
  },
  mounted() {
    this.getUserIdFromToken();  // 获取用户 ID
    this.fetchMyBlogs();
  }
};
</script>

<style scoped>
.blog-container {
  width: 800px;
  margin: 0 auto;
  padding: 20px;
  background-color: #f5f5f5;
  border-radius: 10px;
}

.form-container {
  padding: 20px;
  background-color: white;
  border-radius: 10px;
  margin-bottom: 30px;
}

.input-title,
.input-content {
  width: 100%;
  padding: 10px;
  margin-bottom: 15px;
  border: 1px solid #ddd;
  border-radius: 5px;
}

.input-file {
  margin-bottom: 10px;
}

.image-preview {
  margin-bottom: 15px;
}

.tags-checkboxes label {
  display: inline-block;
  margin-right: 10px;
}

.submit-button {
  padding: 10px 20px;
  background-color: #00796B;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.submit-button:hover {
  background-color: #004D40;
}

.blog-list {
  margin-top: 20px;
}

.blog-item {
  background-color: #fff;
  border: 1px solid #ddd;
  padding: 15px;
  border-radius: 10px;
  margin-bottom: 15px;
}

.edit-button,
.delete-button {
  margin-right: 10px;
  padding: 5px 10px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.edit-button {
  background-color: #4CAF50;
  color: white;
}

.delete-button {
  background-color: #E53935;
  color: white;
}

.edit-button:hover,
.delete-button:hover {
  opacity: 0.8;
}
</style>
