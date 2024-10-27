<template>

  <div class="layout">
    <ASidebar />
  </div>
  <div class="content">
    <div class="blog-management">
      <h2>博客管理</h2>

      <!-- 搜索框，用于按标题或正文搜索博客 -->
      <input v-model="searchKeyword" placeholder="通过标题或正文搜索博客">
      <button @click="searchBlogs" :disabled="!searchKeyword">搜索</button>
      <button @click="resetSearch">重置</button>

      <!-- 博客列表 -->
      <table v-if="filteredBlogs.length > 0">
        <thead>
          <tr>
            <th>标题</th>
            <th>正文</th>
            <th>作者</th>
            <th>标签</th>
            <th>发布时间</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="blog in sortedBlogs" :key="blog.blogId">
            <td>{{ blog.title }}</td>
            <td>{{ blog.content.substring(0, 100) }}...</td> <!-- 只显示正文的前100个字符 -->
            <td>{{ blog.username }}</td>
            <td>{{ blog.tags && blog.tags.length ? blog.tags.join(', ') : '无标签' }}</td>
            <td>{{ new Date(blog.createdAt).toLocaleString() }}</td>
            <td>
              <button @click="deleteBlog(blog.blogId)">删除</button>
            </td>
          </tr>
        </tbody>
      </table>

      <!-- 当没有博客时，显示提示信息 -->
      <p v-else>没有符合条件的博客。</p>
    </div>
  </div>

</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      searchKeyword: '',     // 搜索关键词
      blogs: [],             // 全部博客列表
    };
  },
  computed: {
    // 根据关键词过滤博客
    filteredBlogs() {
      let filtered = [...this.blogs];

      // 根据标题或正文关键词过滤
      if (this.searchKeyword) {
        filtered = filtered.filter(blog =>
          blog.title.toLowerCase().includes(this.searchKeyword.toLowerCase()) ||
          blog.content.toLowerCase().includes(this.searchKeyword.toLowerCase())
        );
      }

      return filtered;
    },

    // 按照发布时间排序博客
    sortedBlogs() {
      return this.filteredBlogs.sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt));
    }
  },
  methods: {
    // 获取所有博客
    fetchBlogs() {
      axios.get('http://localhost:8080/api/blogs/public')
        .then(response => {
          this.blogs = response.data;
          console.log("后端返回的博客数据:", response.data);
        })
        .catch(error => {
          console.error("获取博客失败:", error);
        });
    },

    // 搜索博客
    searchBlogs() {
      // 这里调用了 computed 属性中的 filteredBlogs 来实现搜索
    },

    // 重置搜索
    resetSearch() {
      this.searchKeyword = '';
      this.fetchBlogs();  // 重新获取所有博客
    },

    // 删除博客
    deleteBlog(blogId) {
      if (confirm('确定要删除该博客吗？')) {
        axios.delete(`http://localhost:8080/api/blogs/${blogId}`)
          .then(() => {
            alert('博客已删除');
            this.fetchBlogs();  // 更新博客列表
          })
          .catch(error => {
            console.error('删除博客失败:', error);
          });
      }
    }
  },
  mounted() {
    this.fetchBlogs(); // 组件加载时获取博客
  }
};
</script>

<style scoped>
h2 {
  color: #4CAF50;
}

table {
  width: 100%;
  border-collapse: collapse;
}

th,
td {
  border: 1px solid #ddd;
  padding: 8px;
  text-align: left;
}

th {
  background-color: #f4f4f4;
}

button {
  margin-right: 10px;
}
</style>
