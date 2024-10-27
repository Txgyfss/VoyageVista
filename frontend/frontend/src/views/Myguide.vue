<template>
  <div class="layout">
    <UserSidebar />
  </div>
  <div class="content">
    <div class="guide-form-container">
      <h2>{{ isEditing ? '编辑攻略' : '创建攻略' }}</h2>
      <input v-model="guideTitle" placeholder="标题" class="input-box" />
      <textarea v-model="guideContent" placeholder="正文内容" class="textarea-box"></textarea>

      <!-- 开销标签 -->
      <label for="expenseTag" class="label">选择开销标签:</label>
      <select v-model="selectedExpenseTag" class="dropdown">
        <option v-for="tag in expenseTags" :key="tag.tagId" :value="tag.tagId">{{ tag.name }}</option>
      </select>

      <!-- 行程标签 -->
      <label for="itineraryTag" class="label">选择行程标签:</label>
      <select v-model="selectedItineraryTag" class="dropdown">
        <option v-for="tag in itineraryTags" :key="tag.tagId" :value="tag.tagId">{{ tag.name }}</option>
      </select>

      <!-- 爱好标签 -->
      <label for="hobbyTag" class="label">选择爱好标签:</label>
      <select v-model="selectedHobbyTag" class="dropdown">
        <option v-for="tag in hobbyTags" :key="tag.tagId" :value="tag.tagId">{{ tag.name }}</option>
      </select>

      <!-- 其他表单元素 -->
      <label for="expense" class="label">人均开销:</label>
      <input v-model="guideExpense" type="number" class="input-box" />
      <label for="days" class="label">天数:</label>
      <input v-model="guideDays" type="number" class="input-box" />
      <label for="isPublic" class="label">是否公开:
        <input v-model="isPublic" type="checkbox" checked class="checkbox" /></label>

      <button @click="saveGuide" class="submit-button">{{ isEditing ? '更新' : '保存' }}</button>
      <button v-if="isEditing" @click="resetForm" class="cancel-button">取消编辑</button>

      <h2>我的攻略</h2>
      <ul v-if="userGuides.length > 0" class="guide-list">
        <li v-for="guide in userGuides" :key="guide.guideId" class="guide-item">
          <h3>{{ guide.title }}</h3>
          <p>{{ guide.content }}</p>
          <button @click="editGuide(guide)" class="edit-button">编辑</button>
          <button @click="deleteGuide(guide.guideId)" class="delete-button">删除</button>
        </li>
      </ul>
      <p v-else>您尚未发布任何攻略。</p>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import { jwtDecode } from 'jwt-decode'; // 确保正确引入 jwt-decode 库

export default {
  data() {
    return {
      guideTitle: '',
      guideContent: '',
      guideExpense: 0,
      guideDays: 1,
      userId: null,
      guideId: null,
      isPublic: true,
      selectedExpenseTag: null,
      selectedItineraryTag: null,
      selectedHobbyTag: null,
      expenseTags: [],
      itineraryTags: [],
      hobbyTags: [],
      userGuides: [],
      isEditing: false,
      currentGuideId: null,
    };
  },
  mounted() {
    this.getUserIdFromToken();
    this.fetchTags();
    this.fetchUserGuides();
  },
  methods: {
    getUserIdFromToken() {
      const token = localStorage.getItem('token');
      if (token) {
        try {
          const decodedToken = jwtDecode(token);
          this.userId = decodedToken.userId;
        } catch (error) {
          this.redirectToLogin();
        }
      } else {
        this.redirectToLogin();
      }
    },
    redirectToLogin() {
      this.$router.push('/login');
    },
    fetchTags() {
      axios.get('http://localhost:8080/api/tags?category=开销')
        .then(response => {
          this.expenseTags = response.data;
        });

      axios.get('http://localhost:8080/api/tags?category=行程')
        .then(response => {
          this.itineraryTags = response.data;
        });

      axios.get('http://localhost:8080/api/tags?category=爱好')
        .then(response => {
          this.hobbyTags = response.data;
        });
    },
    fetchUserGuides() {
      axios.get(`http://localhost:8080/api/guides/my?userId=${this.userId}`)
        .then(response => {
          this.userGuides = response.data;
        })
        .catch(error => {
          console.error('获取用户攻略失败:', error);
        });
    },
    saveGuide() {
      const guideData = {
        title: this.guideTitle,
        content: this.guideContent,
        expensePerPerson: this.guideExpense,
        days: this.guideDays,
        isPublic: this.isPublic,
        userId: this.userId,
        tagIds: [this.selectedExpenseTag, this.selectedItineraryTag, this.selectedHobbyTag]
      };

      if (this.isEditing && this.currentGuideId) {
        axios.put(`http://localhost:8080/api/guides/${this.currentGuideId}`, guideData)
          .then(() => {
            alert('攻略已更新');
            this.isEditing = false;
            this.resetForm();
            this.fetchUserGuides();
          })
          .catch(error => {
            console.error('更新攻略失败:', error);
          });
      } else {
        axios.post('http://localhost:8080/api/guides', guideData)
          .then(() => {
            alert('攻略已保存');
            this.resetForm();
            this.fetchUserGuides();
          })
          .catch(error => {
            console.error('保存攻略失败:', error);
          });
      }
    },

    editGuide(guide) {
      this.isEditing = true;
      this.currentGuideId = guide.guideId;
      this.guideTitle = guide.title;
      this.guideContent = guide.content;
      this.guideExpense = guide.expensePerPerson;
      this.guideDays = guide.days;
      this.isPublic = guide.isPublic;
      this.selectedExpenseTag = guide.tagIds.find(tagId => this.expenseTags.some(tag => tag.tagId === tagId)) || null;
      this.selectedItineraryTag = guide.tagIds.find(tagId => this.itineraryTags.some(tag => tag.tagId === tagId)) || null;
      this.selectedHobbyTag = guide.tagIds.find(tagId => this.hobbyTags.some(tag => tag.tagId === tagId)) || null;
    },

    deleteGuide(guideId) {
      if (confirm('确定要删除该攻略吗？')) {
        axios.delete(`http://localhost:8080/api/guides/${guideId}`)
          .then(() => {
            alert('攻略已删除');
            this.fetchUserGuides();
          })
          .catch(error => {
            console.error('删除攻略失败:', error);
          });
      }
    },

    resetForm() {
      this.guideTitle = '';
      this.guideContent = '';
      this.guideExpense = 0;
      this.guideDays = 1;
      this.isPublic = true;
      this.selectedExpenseTag = null;
      this.selectedItineraryTag = null;
      this.selectedHobbyTag = null;
      this.isEditing = false;
      this.currentGuideId = null;
    }
  }
};
</script>

<style scoped>
.guide-form-container {
  padding: 20px;
  background-color: #e0f7fa;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.input-box,
.dropdown,
.textarea-box {
  display: block;
  width: 100%;
  margin-bottom: 15px;
  padding: 10px;
  border: 1px solid #00796b;
  border-radius: 5px;
  transition: all 0.3s ease;
}

.input-box:hover,
.dropdown:hover,
.textarea-box:hover {
  border-color: #004d40;
}

.label {
  margin-bottom: 5px;
  display: block;
  color: #00796b;
}

.checkbox {
  margin: 10px 0;
}

.submit-button,
.cancel-button {
  background-color: #00796b;
  color: white;
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease, transform 0.3s ease;
}

.submit-button:hover,
.cancel-button:hover {
  background-color: #004d40;
  transform: translateY(-3px);
}

.guide-list {
  margin-top: 20px;
}

.guide-item {
  background-color: #e0f2f1;
  padding: 15px;
  margin-bottom: 20px;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.guide-item:hover {
  transform: translateY(-10px);
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
}

.edit-button,
.delete-button {
  background-color: #4caf50;
  color: white;
  padding: 10px 15px;
  margin-right: 10px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease, transform 0.3s ease;
}

.edit-button:hover,
.delete-button:hover {
  background-color: #388e3c;
  transform: translateY(-3px);
}
</style>
