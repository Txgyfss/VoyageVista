<template>
  <div class="layout">
    <UserSidebar />
  </div>
  <div class="content">
    <div class="news-container">
      <!-- 搜索框 -->
      <div class="search-box">
        <input v-model="searchQuery" placeholder="请输入关键词搜索" class="search-input" />
        <select v-model="searchType" class="dropdown">
          <option value="title">搜索标题</option>
          <option value="content">搜索正文</option>
          <option value="all">全选</option>
        </select>
        <button @click="searchNews" class="search-button">搜索</button>
      </div>

      <!-- 按分类筛选 -->
      <div class="category-filter">
        <label for="category">按分类筛选:</label>
        <select v-model="selectedCategory" id="category" class="dropdown">
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
        <select v-model="sortField" id="sortField" class="dropdown">
          <option value="publishedAt">发布时间</option>
          <option value="updatedAt">修改时间</option>
        </select>

        <label for="sortOrder">排序方式:</label>
        <select v-model="sortOrder" id="sortOrder" class="dropdown">
          <option value="asc">升序</option>
          <option value="desc">降序</option>
        </select>

        <button @click="sortNews" class="sort-button">排序</button>
      </div>

      <!-- 资讯列表 -->
      <div class="news-list">
        <div class="row" v-if="sortedNewsList && sortedNewsList.length">
          <div v-for="news in sortedNewsList" :key="news.newsId" :class="['news-item', news.category]"
            :style="getCategoryStyle(news.category)" class="news-card">
            <h3>{{ news.title }} <span class="category-label">{{ news.category }}</span>
              <span v-if="news.Pinned" class="pinned-label">[置顶]</span> <!-- 置顶标识 -->
            </h3>
            <!-- 点击按钮展开/收起详细内容 -->
            <p v-if="news.showFullContent">{{ news.content }}</p>
            <p v-else>{{ news.content.substring(0, 100) }}...</p>
            <button @click="toggleContent(news)" class="toggle-button">
              {{ news.showFullContent ? '收起详情' : '展开详情' }}
            </button>
            <p>分类: {{ news.category }}</p>
            <p>发布时间: {{ news.publishedAt }}</p>
            <p>修改时间: {{ news.updatedAt }}</p>
          </div>
        </div>
        <p v-else>暂无资讯</p>
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
      searchQuery: '',
      searchType: 'all',
      selectedCategory: '',
      sortField: 'publishedAt',
      sortOrder: 'asc',
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
    async fetchNews() {
      try {
        const response = await axios.get('http://localhost:8080/api/news');
        this.newsList = response.data.map(news => ({
          ...news,
          showFullContent: false, // 初始状态为不显示完整内容
        }));
        this.filteredNewsList = this.newsList;
      } catch (error) {
        console.error('获取资讯失败', error);
      }
    },
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
    sortNews() {
      this.filteredNewsList.sort((a, b) => {
        const comparison = new Date(a[this.sortField]) - new Date(b[this.sortField]);
        return this.sortOrder === 'asc' ? comparison : -comparison;
      });
    },
    filterByCategory(newsList) {
      if (this.selectedCategory) {
        return newsList.filter(news => news.category === this.selectedCategory);
      }
      return newsList;
    },
    toggleContent(news) {
      news.showFullContent = !news.showFullContent; // 切换显示详细内容
    },
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
.news-container {
  width: 1000px;
  margin: 0 auto;
  padding: 20px;
}

.search-box,
.category-filter,
.sort-box {
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
.sort-button {
  padding: 10px 20px;
  background-color: #00796B;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease, transform 0.3s ease;
}

.search-button:hover,
.sort-button:hover {
  background-color: #004D40;
  transform: translateY(-3px);
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.2);
}

.news-list {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
}

.row {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
}

.news-card {
  width: 450px;
  background-color: #E0F2F1;
  padding: 15px;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.news-card:hover {
  transform: translateY(-10px);
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
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
</style>
