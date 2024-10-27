<template>
  <div class="layout">
    <UserSidebar />
  </div>
  <div class="content">
    <div class="blog-container">
      <h1>博客广场</h1>

      <!-- 搜索框 -->
      <div class="search-box">
        <input v-model="searchKeyword" placeholder="通过标题、正文或标签搜索博客" class="search-input" />
        <button @click="searchBlogs" class="search-button">搜索</button>
        <button @click="resetSearch" class="reset-button">重置</button>
      </div>

      <!-- 标签筛选框 -->
      <div class="filter-box">
        <label for="tagFilter">筛选标签:</label>
        <select v-model="selectedTag" @change="filterByTag" class="dropdown">
          <option value="">全部</option>
          <option v-for="tag in allTags" :key="tag" :value="tag">{{ tag }}</option>
        </select>
      </div>

      <!-- 博客列表 -->
      <div class="blog-list" v-if="filteredBlogs.length > 0">
        <div class="row">
          <div v-for="blog in filteredBlogs" :key="blog.blogId" class="blog-item">
            <img v-if="blog.imageUrl" :src="getimageUrl(blog.imageUrl)" alt="博客图片" class="blog-image" />
            <h2>{{ blog.title }}</h2>
            <!-- 点击按钮显示更多内容 -->
            <p v-if="blog.showFullContent">{{ blog.content }}</p>
            <p v-else>{{ blog.content.substring(0, 30) }}...</p>
            <button @click="toggleContent(blog)" class="toggle-button">
              {{ blog.showFullContent ? '收起' : '展开更多' }}
            </button>
            <p>作者: {{ blog.username }}</p>
            <p>标签: {{ blog.tags.join(', ') }}</p>
            <p>发布时间: {{ new Date(blog.createdAt).toLocaleString() }}</p>

          </div>
        </div>
      </div>
      <p v-else>暂无公开博客</p>

      <!-- 跳转到我的博客按钮 -->
      <button @click="navigateToMyBlog" class="my-blog-button">我的博客</button>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      searchKeyword: '', // 搜索关键词
      blogs: [], // 所有公开博客
      allTags: [], // 所有标签
      selectedTag: '', // 当前选中的标签
    };
  },
  computed: {
    filteredBlogs() {
      const keyword = this.searchKeyword.toLowerCase();
      return this.blogs.filter(blog => {
        const matchesKeyword =
          blog.title.toLowerCase().includes(keyword) ||
          blog.content.toLowerCase().includes(keyword) ||
          blog.tags.join(',').toLowerCase().includes(keyword);

        const matchesTag = this.selectedTag
          ? blog.tags.includes(this.selectedTag)
          : true;

        return matchesKeyword && matchesTag;
      });
    }
  },
  methods: {
    getimageUrl(imageUrl) {
      return `http://localhost:8080${imageUrl}`;
    },
    fetchPublicBlogs() {
      axios.get('http://localhost:8080/api/blogs/public')
        .then(response => {
          this.blogs = response.data.map(blog => ({
            ...blog,
            showFullContent: false, // 初始状态为不显示完整内容
          }));
          this.extractTags();
        })
        .catch(error => {
          console.error('获取博客失败:', error);
        });
    },
    extractTags() {
      const tagsSet = new Set();
      this.blogs.forEach(blog => {
        blog.tags.forEach(tag => tagsSet.add(tag));
      });
      this.allTags = Array.from(tagsSet);
    },
    toggleContent(blog) {
      blog.showFullContent = !blog.showFullContent; // 切换显示和隐藏状态
    },
    searchBlogs() { },
    filterByTag() { },
    resetSearch() {
      this.searchKeyword = '';
      this.selectedTag = '';
    },
    navigateToMyBlog() {
      this.$router.push('/myblog');
    }
  },
  mounted() {
    this.fetchPublicBlogs();
  }
};
</script>

<style scoped>
.blog-container {
  width: 1400px;
  margin: 0 auto;
  padding: 20px;
}

.search-box,
.filter-box {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
}

.search-input,
.dropdown {
  padding: 5px;
  font-size: 14px;
  width: 150px;
  border: 1px solid #4DB6AC;
  border-radius: 5px;
  background-color: #E0F7FA;
  transition: background-color 0.3s ease, transform 0.3s ease;
}

.search-input:hover,
.dropdown:hover {
  background-color: #B2EBF2;
  transform: scale(1.05);
}

.search-input:focus,
.dropdown:focus {
  outline: none;
  background-color: #80DEEA;
  border-color: #00796B;
  transform: scale(1.1);
}

.search-button,
.reset-button {
  padding: 10px 20px;
  background-color: #00796B;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease, transform 0.3s ease;
}

.search-button:hover,
.reset-button:hover {
  background-color: #004D40;
  transform: translateY(-3px);
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.2);
}

.blog-list {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
}

.row {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
}

.blog-item {
  width: 550px;
  background-color: #E0F2F1;
  padding: 15px;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.blog-item:hover {
  transform: translateY(-10px);
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
}

.blog-image {
  width: 100%;
  border-radius: 10px;
  margin-bottom: 10px;
}

.my-blog-button {
  padding: 10px 20px;
  background-color: #00796B;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease, transform 0.3s ease;
}

.my-blog-button:hover {
  background-color: #004D40;
  transform: translateY(-3px);
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.2);
}

.toggle-button {
  background-color: #00796B;
  color: white;
  padding: 5px 10px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease, transform 0.3s ease;
}

.toggle-button:hover {
  background-color: #004D40;
  transform: translateY(-3px);
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.2);
}

.content {
  margin-left: 80px;
  flex-grow: 1;
  padding: 20px;
  width: 1300px;
  overflow-y: auto;
}
</style>
