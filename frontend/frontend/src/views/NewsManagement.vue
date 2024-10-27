<template>

  <div class="layout">
    <ASidebar />
  </div>
  <div class="content">
    <div class="admin-container">
      <admin-sidebar></admin-sidebar> <!-- 使用 kebab-case 标识符 -->
      <!-- 左侧资讯一览及搜索 -->
      <div class="left-sidebar">
        <h2>资讯列表</h2>

        <!-- 刷新按钮 -->
        <button @click="fetchNews">刷新资讯列表</button>

        <!-- 搜索框 -->
        <div class="search-box">
          <input v-model="searchQuery" placeholder="请输入关键词搜索" />
          <select v-model="searchType">
            <option value="title">搜索标题</option>
            <option value="content">搜索正文</option>
            <option value="all">全选</option>
          </select>
          <button @click="searchNews">搜索</button>
        </div>

        <!-- 按分类筛选 -->
        <div class="category-filter">
          <label for="category">按分类筛选:</label>
          <select v-model="selectedCategory" id="category">
            <option value="">所有分类</option>
            <option value="WEATHER">天气</option>
            <option value="ACTIVITY">活动</option>
            <option value="OFFER">优惠</option>
            <option value="NOTICE">通知</option>
          </select>
        </div>

        <!-- 排序功能 -->
        <div class="sort-box">
          <label for="sortField">排序字段:</label>
          <select v-model="sortField" id="sortField">
            <option value="publishedAt">发布时间</option>
            <option value="updatedAt">修改时间</option>
          </select>

          <label for="sortOrder">排序方式:</label>
          <select v-model="sortOrder" id="sortOrder">
            <option value="asc">升序</option>
            <option value="desc">降序</option>
          </select>

          <button @click="sortNews">排序</button>
        </div>

        <!-- 资讯列表，置顶资讯在最上 -->
        <ul v-if="sortedNewsList && sortedNewsList.length">
          <li v-for="news in sortedNewsList" :key="news.newsId" :class="['news-item', news.category]"
            :style="getCategoryStyle(news.category)">
            <h3>{{ news.title }} <span class="category-label">{{ news.category }}</span>
              <span v-if="news.Pinned" class="pinned-label">[置顶]</span> <!-- 置顶标识 -->
            </h3>
            <p>{{ news.content }}</p>
            <p>分类: {{ news.category }}</p>
            <p>发布时间: {{ news.publishedAt }}</p>
            <p>修改时间: {{ news.updatedAt }}</p>
            <button @click="editNews(news)">编辑</button>
            <button @click="deleteNews(news.newsId)">删除</button>
          </li>
        </ul>
        <p v-else>暂无资讯</p>
      </div>

      <!-- 右侧发布/编辑资讯 -->
      <div class="right-sidebar">
        <h2>{{ isEditing ? '编辑资讯' : '发布资讯' }}</h2>
        <form @submit.prevent="handleSubmit">
          <input v-model="form.title" placeholder="标题" required />
          <textarea v-model="form.content" placeholder="内容" required></textarea>

          <label for="category">分类:</label>
          <select v-model="form.category" id="category">
            <option value="WEATHER">天气</option>
            <option value="ACTIVITY">活动</option>
            <option value="OFFER">优惠</option>
            <option value="NOTICE">通知</option>
          </select>

          <label for="pinned">是否置顶:</label>
          <select v-model="form.pinned" id="pinned">
            <option :value="true">是</option>
            <option :value="false">否</option>
          </select>



          <label for="publishedAt">发布时间:</label>
          <input type="datetime-local" v-model="form.publishedAt" required />

          <button type="submit">{{ isEditing ? '保存修改' : '提交' }}</button>
        </form>
      </div>

      <!-- 成功提示 -->
      <div v-if="message" class="message-box">
        <p>{{ message }}</p>
        <button @click="message = ''">关闭</button>
      </div>
    </div>
  </div>

</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      newsList: [],
      filteredNewsList: [],
      form: {
        title: '',
        content: '',
        publishedAt: '',
        category: 'NOTICE',
        Pinned: false,
      },
      searchQuery: '',
      searchType: 'all',
      selectedCategory: '',
      sortField: 'publishedAt',
      sortOrder: 'asc',
      isEditing: false,
      editingNewsId: null,
      message: '',
    };
  },
  mounted() {
    this.fetchNews();
  },
  computed: {
    sortedNewsList() {
      if (!this.filteredNewsList || !this.filteredNewsList.length) {
        return [];
      }

      let sorted = this.filterByCategory(this.filteredNewsList);
      const pinnedNews = sorted.filter(news => news.Pinned);
      const nonPinnedNews = sorted.filter(news => !news.Pinned);

      return [...pinnedNews, ...nonPinnedNews]; // 置顶的资讯排在最前面
    }
  },
  methods: {
    // 获取所有资讯
    async fetchNews() {
      try {
        const response = await axios.get('http://localhost:8080/api/news');
        this.newsList = response.data;
        console.log("后端返回的数据:", response.data);
        this.filteredNewsList = this.newsList;
        console.log("filteredNewsList中的数据:", this.filteredNewsList);  // 检查filteredNewsList是否正确赋值
      } catch (error) {
        console.error('获取资讯失败', error);
      }
    },
    // 搜索资讯
    searchNews() {
      this.filteredNewsList = this.newsList.filter(news => {
        if (this.searchType === 'title') {
          return news.title.includes(this.searchQuery);
        } else if (this.searchType === 'content') {
          return news.content.includes(this.searchQuery);
        } else {
          return news.title.includes(this.searchQuery) || news.content.includes(this.searchQuery);
        }
      });
    },
    // 排序资讯
    sortNews() {
      this.filteredNewsList.sort((a, b) => {
        const comparison = new Date(a[this.sortField]) - new Date(b[this.sortField]);
        return this.sortOrder === 'asc' ? comparison : -comparison;
      });
    },
    // 返回按分类筛选后的资讯列表
    filterByCategory(newsList) {
      if (this.selectedCategory) {
        return newsList.filter(news => news.category === this.selectedCategory);
      }
      return newsList;
    },


    // 提交表单
    async handleSubmit() {
      const url = this.isEditing ? `http://localhost:8080/api/news/${this.editingNewsId}` : 'http://localhost:8080/api/news';
      const method = this.isEditing ? 'put' : 'post';

      console.log('提交的数据: ', this.form);  // 检查提交的数据是否包含 Pinned

      try {
        await axios[method](url, this.form);
        this.message = this.isEditing ? '资讯更新成功！' : '资讯发布成功！';
        this.fetchNews();
        this.resetForm();
      } catch (error) {
        console.error(`${this.isEditing ? '更新' : '发布'}资讯失败`, error);
      }
    },
    handlePinnedChange() {
      console.log('置顶状态变化: ', this.form.Pinned); // 检查是否改变
    },
    // 编辑资讯
    editNews(news) {
      this.isEditing = true;
      this.editingNewsId = news.newsId;
      this.form = { ...news };
    },
    // 删除资讯
    async deleteNews(newsId) {
      try {
        await axios.delete(`http://localhost:8080/api/news/${newsId}`);
        this.message = '资讯删除成功！';
        this.fetchNews();
      } catch (error) {
        console.error('删除资讯失败', error);
      }
    },
    // 重置表单
    resetForm() {
      this.form = {
        title: '',
        content: '',
        publishedAt: '',
        category: 'NOTICE',
        Pinned: false,
      };
      this.isEditing = false;
      this.editingNewsId = null;
    },
    // 返回不同分类的样式
    getCategoryStyle(category) {
      const categoryColors = {
        WEATHER: 'lightblue',
        ACTIVITY: 'lightgreen',
        OFFER: 'lightcoral',
        NOTICE: 'lightgoldenrodyellow',
      };
      return { border: `2px solid ${categoryColors[category]}` };
    }
  }
};
</script>

<style scoped>
.container {
  display: flex;
  justify-content: space-between;
}

.left-sidebar {
  width: 45%;
  margin-right: 20px;
}

.right-sidebar {
  width: 50%;
}

ul {
  list-style-type: none;
  padding: 0;
}

li {
  margin-bottom: 20px;
  border: 1px solid #ddd;
  padding: 10px;
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

.message-box {
  position: fixed;
  bottom: 20px;
  right: 20px;
  background-color: #4CAF50;
  color: white;
  padding: 10px;
  border-radius: 5px;
}

.message-box button {
  background: none;
  border: none;
  color: white;
  cursor: pointer;
}

.news-item.WEATHER {
  border: 2px solid lightblue;
}

.news-item.ACTIVITY {
  border: 2px solid lightgreen;
}

.news-item.OFFER {
  border: 2px solid lightcoral;
}

.news-item.NOTICE {
  border: 2px solid lightgoldenrodyellow;
}

.category-label {
  font-size: 12px;
  color: gray;
  margin-left: 10px;
}

.pinned-label {
  font-size: 12px;
  color: red;
  margin-left: 10px;
}
</style>
