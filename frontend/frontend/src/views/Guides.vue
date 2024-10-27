<template>
  <div class="layout">
    <UserSidebar />
  </div>
  <div class="content">
    <div class="guide-container">
      <!-- 搜索框，用于按标题、内容或作者搜索攻略 -->
      <div class="search-box">
        <input v-model="searchKeyword" placeholder="通过标题、内容或作者搜索攻略" class="search-input" />
        <button @click="searchguides" :disabled="!searchKeyword" class="search-button">搜索</button>
        <button @click="resetSearch" class="reset-button">重置</button>
      </div>

      <!-- 标签筛选 -->
      <div class="filter-box">
        <label>选择开销标签:</label>
        <select v-model="selectedTag1" class="dropdown">
          <option value="">所有标签</option>
          <option v-for="tag in expenseTags" :key="tag.tagId" :value="tag.name">{{ tag.name }}</option>
        </select>

        <label>选择行程标签:</label>
        <select v-model="selectedTag2" class="dropdown">
          <option value="">所有标签</option>
          <option v-for="tag in itineraryTags" :key="tag.tagId" :value="tag.name">{{ tag.name }}</option>
        </select>

        <label>选择爱好标签:</label>
        <select v-model="selectedTag3" class="dropdown">
          <option value="">所有标签</option>
          <option v-for="tag in hobbyTags" :key="tag.tagId" :value="tag.name">{{ tag.name }}</option>
        </select>

        <!-- 人均开销筛选 -->
        <div>
          <label>按人均开销筛选:</label>
          <input v-model.number="minExpense" type="number" placeholder="最低开销" class="price-input" />
          <input v-model.number="maxExpense" type="number" placeholder="最高开销" class="price-input" />
        </div>

        <!-- 排序方式 -->
        <label>排序方式:</label>
        <select v-model="sortOrder" class="dropdown">
          <option value="latest">发布时间</option>
        </select>
      </div>

      <!-- “我的攻略”按钮 -->
      <button @click="navigateToMyGuide" class="my-guide-button">我的攻略</button>

      <!-- 空状态提示 -->
      <div v-if="filteredguides.length === 0">
        <p>没有符合条件的攻略</p>
      </div>

      <!-- 展示过滤后的攻略列表 -->
      <div class="guide-list" v-else>
        <div class="row">
          <div v-for="guide in filteredguides" :key="guide.guideId" class="guide-item">
            <h2>{{ guide.title }}</h2>
            <p>作者: {{ guide.username }}</p>
            <p>发布时间: {{ new Date(guide.createdAt).toLocaleString() }}</p>
            <p>人均开销: {{ guide.expensePerPerson }}</p>
            <p>标签: {{ guide.tags && guide.tags.length ? guide.tags.join(', ') : '无标签' }}</p>
            <!-- 显示或隐藏详细内容 -->
            <p v-if="guide.isExpanded">{{ guide.content }}</p>
            <button @click="toggleContent(guide)" class="expand-button">
              {{ guide.isExpanded ? '收起' : '展开详细内容' }}
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      searchKeyword: '',
      selectedTag1: '',
      selectedTag2: '',
      selectedTag3: '',
      minExpense: null,
      maxExpense: null,
      sortOrder: 'latest',
      guides: [],
      expenseTags: [
        { tagId: 1, name: '穷游' },
        { tagId: 2, name: '正常' },
        { tagId: 2, name: '该省省该花花' },
        { tagId: 3, name: '富游' }
      ],
      itineraryTags: [
        { tagId: 4, name: '特种兵' },
        { tagId: 5, name: '休闲游' },
        { tagId: 6, name: '度假' }
      ],
      hobbyTags: [
        { tagId: 7, name: '自然景观' },
        { tagId: 8, name: '人文历史' },
        { tagId: 8, name: '美食探索' },
        { tagId: 9, name: '都市风光' }
      ]
    };
  },
  computed: {
    filteredguides() {
      let filteredguides = [...this.guides];

      if (this.searchKeyword) {
        filteredguides = filteredguides.filter(guide =>
          guide.title.includes(this.searchKeyword) ||
          guide.content.includes(this.searchKeyword) ||
          (guide.username && guide.user.username.includes(this.searchKeyword))
        );
      }

      if (this.selectedTag1) {
        filteredguides = filteredguides.filter(guide =>
          guide.tags && guide.tags.some(tag => tag === this.selectedTag1));
      }
      if (this.selectedTag2) {
        filteredguides = filteredguides.filter(guide =>
          guide.tags && guide.tags.some(tag => tag === this.selectedTag2));
      }
      if (this.selectedTag3) {
        filteredguides = filteredguides.filter(guide =>
          guide.tags && guide.tags.some(tag => tag === this.selectedTag3));
      }

      if (this.minExpense !== null) {
        filteredguides = filteredguides.filter(guide => guide.expensePerPerson >= this.minExpense);
      }
      if (this.maxExpense !== null) {
        filteredguides = filteredguides.filter(guide => guide.expensePerPerson <= this.maxExpense);
      }

      if (this.sortOrder === 'latest') {
        filteredguides.sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt));
      }

      return filteredguides;
    }
  },
  methods: {
    searchguides() {
      if (!this.searchKeyword) {
        alert("请输入搜索关键词");
        return;
      }

      axios.get(`http://localhost:8080/api/guides/search?keyword=${this.searchKeyword}`)
        .then(response => {
          this.guides = response.data;
        })
        .catch(error => {
          console.error("搜索出错:", error);
          alert("搜索过程中发生了错误，请稍后再试。");
        });
    },

    resetSearch() {
      this.searchKeyword = '';
      this.selectedTag1 = '';
      this.selectedTag2 = '';
      this.selectedTag3 = '';
      this.minExpense = null;
      this.maxExpense = null;
      this.fetchguides();
    },

    fetchguides() {
      axios.get('http://localhost:8080/api/guides/public')
        .then(response => {
          // 在每个攻略对象上添加 isExpanded 属性
          this.guides = response.data.map(guide => ({ ...guide, isExpanded: false }));
        })
        .catch(error => {
          console.error("获取攻略出错:", error);
          alert("获取攻略数据时发生了错误，请稍后再试。");
        });
    },

    navigateToMyGuide() {
      this.$router.push('/myguide');
    },

    toggleContent(guide) {
      // 切换详细内容的显示状态
      guide.isExpanded = !guide.isExpanded;
    }
  },
  mounted() {
    this.fetchguides();
  }
};
</script>

<style scoped>
.guide-container {
  width: 1200px;
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
.dropdown,
.price-input {
  padding: 5px;
  font-size: 14px;
  width: 150px;
  border: 1px solid #4DB6AC;
  border-radius: 5px;
  background-color: #E0F7FA;
  transition: background-color 0.3s ease, transform 0.3s ease;
}

.search-input:hover,
.dropdown:hover,
.price-input:hover {
  background-color: #B2EBF2;
  transform: scale(1.05);
}

.search-input:focus,
.dropdown:focus,
.price-input:focus {
  outline: none;
  background-color: #80DEEA;
  border-color: #00796B;
  transform: scale(1.1);
}

.search-button,
.reset-button,
.my-guide-button,
.expand-button {
  padding: 10px 20px;
  background-color: #00796B;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease, transform 0.3s ease;
}

.search-button:hover,
.reset-button:hover,
.my-guide-button:hover,
.expand-button:hover {
  background-color: #004D40;
  transform: translateY(-3px);
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.2);
}

.guide-list {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
}

.row {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
}

.guide-item {
  width: 550px;
  background-color: #E0F2F1;
  padding: 15px;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.guide-item:hover {
  transform: translateY(-10px);
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
}

.content {
  margin-left: 80px;
  flex-grow: 1;
  padding: 20px;
  width: 1300px;
  overflow-y: auto;
}
</style>
