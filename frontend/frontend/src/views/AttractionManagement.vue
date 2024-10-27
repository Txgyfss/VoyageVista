<template>

  <div class="layout">
    <ASidebar />
    `
    <div class="content">
      <div class="admin-container">

        <main class="content">
          <router-view></router-view> <!-- 动态加载对应页面的内容 -->
        </main>
      </div>
      <div class="container">

        <div class="left-sidebar">
          <h3>景点列表</h3>
          <button @click="fetchAttractions">刷新景点列表</button>
          <p v-if="!attractions.length">暂无景点</p>
          <ul v-else>
            <li v-for="attraction in attractions" :key="attraction.attractionId">
              <img :src="getImageUrl(attraction.imageUrl)" alt="景点图片" width="100" @error="handleImageError" />
              <p>名称: {{ attraction.name }}</p>
              <p>位置: {{ attraction.location }}</p>
              <p>类型: {{ Array.isArray(attraction.type) ? attraction.type.join(', ') : attraction.type }}</p>
              <p>开放时间: {{ attraction.openingStart }} - {{ attraction.openingEnd }}</p>
              <p>城市: {{ attraction.city }}</p>
              <p>游玩时长: {{ attraction.duration }} 分钟</p>
              <p>目标观众: {{ Array.isArray(attraction.targetAudience) ? attraction.targetAudience.join(', ') :
                attraction.targetAudience }}</p>
              <button @click="editAttraction(attraction)">编辑</button>
              <button @click="deleteAttraction(attraction.attractionId)">删除</button>
              <!-- 添加详情按钮，点击后跳转到票型管理页面 -->
              <button @click="viewDetails(attraction.attractionId)">详情</button>
            </li>
          </ul>
          <p v-if="attractions.length">当前有 {{ attractions.length }} 个景点</p>
        </div>

        <div class="right-sidebar">
          <h3>添加/编辑景点</h3>
          <form @submit.prevent="handleSubmit">
            <input v-model="formAttraction.name" placeholder="景点名称" required />
            <input v-model="formAttraction.location" placeholder="地理位置" required />

            <!-- 景点类型多选 -->
            <label>景点类型:</label>
            <div class="checkbox-container" v-for="type in attractionTypes" :key="type">
              <input type="checkbox" :value="type" v-model="formAttraction.type" /> {{ type }}
            </div>

            <!-- 目标观众多选 -->
            <label>目标观众:</label>
            <div class="checkbox-container" v-for="audience in targetAudiences" :key="audience">
              <input type="checkbox" :value="audience" v-model="formAttraction.targetAudience" /> {{ audience }}
            </div>

            <!-- 开放时间 -->
            <label>开放时间:</label>
            <input type="time" v-model="formAttraction.openingStart" required /> 到
            <input type="time" v-model="formAttraction.openingEnd" required />

            <textarea v-model="formAttraction.description" placeholder="景点描述"></textarea>
            <input v-model="formAttraction.city" placeholder="所在城市" required />
            <input v-model="formAttraction.duration" type="number" placeholder="游玩时长（分钟）" required />

            <!-- 图片上传 -->
            <label>上传景点图片:</label>
            <input type="file" @change="handleFileUpload" />

            <button type="submit">{{ isEditing ? '保存修改' : '添加景点' }}</button>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {

  name: 'AttractionManagement',
  data() {
    return {
      formAttraction: {
        name: '',
        location: '',
        type: [],
        openingStart: '',
        openingEnd: '',
        description: '',
        city: '',
        duration: '',
        targetAudience: [],
        imageUrl: ''
      },
      attractions: [],
      isEditing: false,
      editingId: null,
      attractionTypes: ['自然风光', '娱乐休闲', '现代都市', '历史文化'],  // 景点类型
      targetAudiences: ['情侣约会', '亲子游', '老人游', '朋友聚会', '独自出游'],  // 目标观众
      selectedFile: null  // 存储图片文件

    };
  },
  methods: {
    viewDetails(attractionId) {
      // 跳转到TicketManagement页面，传递景点ID
      this.$router.push({ name: 'TicketManagement', params: { id: attractionId } });
    },
    handleSubmit() {
      const formData = new FormData();
      formData.append('name', this.formAttraction.name);
      formData.append('location', this.formAttraction.location);
      formData.append('type', this.formAttraction.type.join(', '));
      formData.append('openingStart', this.formAttraction.openingStart);
      formData.append('openingEnd', this.formAttraction.openingEnd);
      formData.append('description', this.formAttraction.description);
      formData.append('city', this.formAttraction.city);
      formData.append('duration', this.formAttraction.duration);
      formData.append('targetAudience', this.formAttraction.targetAudience.join(', '));
      if (this.selectedFile) {
        formData.append('image', this.selectedFile);
      }

      if (this.isEditing) {
        this.updateAttraction(formData);
      } else {
        this.addAttraction(formData);
      }
    },

    getImageUrl(imageUrl) {
      // 将 imageUrl 转换为完整的 URL
      return `http://localhost:8080${imageUrl}`;
    },

    handleImageError(event) {
      console.error('图片加载失败: ', event.target.src);
      event.target.src = 'src/images/1.jpg'; // 可以设置一个默认图片
    },
    handleFileUpload(event) {
      this.selectedFile = event.target.files[0];
    },
    fetchAttractions() {
      axios.get('http://localhost:8080/api/attractions')
        .then(response => {
          console.log("后端返回的数据:", response.data);  // 添加这行以查看后端返回的数据
          this.attractions = response.data;
        })
        .catch(error => {
          console.error("获取景点列表失败:", error);
        });
    },
    // 编辑景点，将景点信息填充到表单
    editAttraction(attraction) {
      this.formAttraction = {
        name: attraction.name,
        location: attraction.location,
        type: attraction.type.split(', '), // 将类型转换为数组
        openingStart: attraction.openingStart,
        openingEnd: attraction.openingEnd,
        description: attraction.description,
        city: attraction.city,
        duration: attraction.duration,
        targetAudience: attraction.targetAudience.split(', '), // 将目标观众转换为数组
        imageUrl: attraction.imageUrl
      };
      this.isEditing = true;
      this.editingId = attraction.attractionId; // 设置编辑的景点ID
    },

    addAttraction(formData) {
      axios.post('http://localhost:8080/api/attractions', formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      })
        .then(response => {
          this.attractions.push(response.data);
          this.resetForm();
          alert("景点添加成功！");
        })
        .catch(error => {
          console.error("添加景点失败:", error);
        });
    },
    updateAttraction(formData) {
      axios.put(`http://localhost:8080/api/attractions/${this.editingId}`, formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      })
        .then(response => {
          const index = this.attractions.findIndex(a => a.attractionId === this.editingId);
          this.attractions.splice(index, 1, response.data);
          this.resetForm();
          alert("景点修改成功！");
        })
        .catch(error => {
          console.error("更新景点失败:", error);
        });
    },
    deleteAttraction(attractionId) {
      axios.delete(`http://localhost:8080/api/attractions/${attractionId}`)
        .then(response => {
          this.attractions = this.attractions.filter(attraction => attraction.attractionId !== attractionId);
        })
        .catch(error => {
          console.error("删除景点失败:", error);
        });
    },
    resetForm() {
      this.formAttraction = {
        name: '',
        location: '',
        type: [],
        openingStart: '',
        openingEnd: '',
        description: '',
        city: '',
        duration: '',
        targetAudience: [],
        imageUrl: ''
      };
      this.isEditing = false;
      this.editingId = null;
      this.selectedFile = null;
    }
  },
  mounted() {
    this.fetchAttractions();
  }
}
</script>

<style scoped>
.container {
  display: flex;
  justify-content: space-between;
  /* 确保左右两栏保持一定距离 */
}

.left-sidebar {
  width: 45%;
  /* 调整左边栏的宽度 */
  margin-right: 20px;
}

.right-sidebar {
  width: 50%;
  /* 调整右边栏的宽度 */
}

/* 增加图片的显示尺寸，确保图片能更好展示 */
img {
  display: block;
  max-width: 100%;
  /* 确保图片不会超出容器的宽度 */
  height: auto;
  margin-bottom: 10px;
}

/* 增加按钮的显示效果 */
button {
  padding: 10px 15px;
  background-color: #4CAF50;
  color: white;
  border: none;
  cursor: pointer;
  font-size: 16px;
  /* 调整字体大小，让按钮看起来更美观 */
}

button:hover {
  background-color: #45a049;
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

img {
  display: block;
  margin-bottom: 10px;
}

/* 对齐选框和选项 */
.checkbox-container {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}

.checkbox-container input {
  margin-right: 10px;
}

.layout {
  display: flex;
  /* 使ASidebar和content水平排列 */
}

.container {
  display: flex;
  /* 使left-sidebar和right-sidebar水平排列 */
  justify-content: space-between;
  /* 控制子元素之间的距离 */
  padding: 20px;
  gap: 20px;
  /* 可选：子元素之间的间距 */
}

.left-sidebar {
  width: 30%;
  /* 设置左侧栏宽度 */
}

.right-sidebar {
  width: 65%;
  /* 设置右侧栏宽度 */
}

.ASidebar {
  width: 200px;
  /* 根据需要调整ASidebar的宽度 */
  flex-shrink: 0;
  /* 防止ASidebar缩小 */
}
</style>