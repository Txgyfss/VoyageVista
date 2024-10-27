<template>

  <div class="layout">
    <ASidebar />
  </div>
  <div class="content">
    <div class="guide-management">
      <h2>攻略管理</h2>

      <!-- 搜索框，用于按标题、内容或作者搜索攻略 -->
      <input v-model="searchKeyword" placeholder="通过标题、内容或作者搜索攻略">
      <button @click="searchGuides" :disabled="!searchKeyword">搜索</button>
      <button @click="resetSearch">重置</button>

      <!-- 攻略列表 -->
      <table v-if="filteredGuides.length > 0">
        <thead>
          <tr>
            <th>标题</th>
            <th>内容</th>
            <th>作者</th>
            <th>标签</th>
            <th>人均开销</th>
            <th>天数</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="guide in filteredGuides" :key="guide.guideId">
            <td>{{ guide.title }}</td>
            <td>{{ guide.content }}</td>
            <td>{{ guide.username }}</td>
            <td>{{ guide.tags && guide.tags.length ? guide.tags.join(', ') : '无标签' }}</td>
            <td>{{ guide.expensePerPerson }}</td>
            <td>{{ guide.days }}</td>
            <td>
              <button @click="deleteGuide(guide.guideId)">删除</button>
            </td>
          </tr>
        </tbody>
      </table>

      <!-- 当没有攻略时，显示提示信息 -->
      <p v-else>没有符合条件的攻略。</p>
    </div>
  </div>


</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      searchKeyword: '',     // 搜索关键词
      guides: [],            // 全部攻略列表
    };
  },
  computed: {
    filteredGuides() {
      let filtered = [...this.guides];

      // 根据关键词过滤（搜索标题、内容、作者）
      if (this.searchKeyword) {
        filtered = filtered.filter(guide =>
          guide.title.includes(this.searchKeyword) ||
          guide.content.includes(this.searchKeyword) ||
          guide.username.includes(this.searchKeyword)
        );
      }

      return filtered;
    }
  },
  methods: {
    // 获取所有攻略，调用 /api/guides/dto
    fetchGuides() {
      axios.get('http://localhost:8080/api/guides/dto')
        .then(response => {
          this.guides = response.data;
          console.log("后端返回的攻略数据:", response.data);
        })
        .catch(error => {
          console.error("获取攻略失败:", error);
        });
    },

    // 搜索攻略，调用 /api/guides/search?keyword=
    searchGuides() {
      axios.get(`http://localhost:8080/api/guides/search?keyword=${this.searchKeyword}`)
        .then(response => {
          this.guides = response.data;
        })
        .catch(error => {
          console.error("搜索出错:", error);
        });
    },

    // 重置搜索
    resetSearch() {
      this.searchKeyword = '';
      this.fetchGuides();  // 重新获取所有攻略
    },

    // 删除攻略，调用 /api/guides/{id}
    deleteGuide(guideId) {
      if (confirm('确定要删除该攻略吗？')) {
        axios.delete(`http://localhost:8080/api/guides/${guideId}`)
          .then(() => {
            alert('攻略已删除');
            this.fetchGuides();  // 更新攻略列表
          })
          .catch(error => {
            console.error('删除攻略失败:', error);
          });
      }
    }
  },
  mounted() {
    this.fetchGuides(); // 组件加载时获取攻略
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
