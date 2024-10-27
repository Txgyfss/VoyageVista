<template>

  <div class="layout">
    <ASidebar />

    <div class="content">
      <div class="container">
        <div class="left-sidebar">
          <h3>美食列表</h3>
          <button @click="fetchFoods" class="refresh-button">刷新美食列表</button>
          <p v-if="!foods.length">暂无美食</p>
          <ul v-else>
            <li v-for="food in foods" :key="food.foodId" class="food-item">
              <img :src="getimageurl(food.imageurl)" alt="美食图片" width="100" @error="handleImageError" />
              <p>名称: {{ food.name }}</p>
              <p>位置: {{ food.location }}</p>
              <p>城市: {{ food.city }}</p>
              <p>类型: {{ food.type }}</p>

              <p v-if="food.rating == '' || food.rating == null">暂无评分</p>
              <p v-else>{{ starRating }}</p>
              <button @click="editFood(food)" class="edit-button">编辑</button>
              <button @click="deleteFood(food.foodId)" class="delete-button">删除</button>
              <button @click="viewFoodDetails(food.foodId)" class="details-button">详情</button>
            </li>
          </ul>
          <p v-if="foods.length">当前有 {{ foods.length }} 道美食</p>
        </div>

        <div class="right-sidebar">
          <h3>{{ isEditing ? '编辑美食' : '添加美食' }}</h3>
          <form @submit.prevent="isEditing ? updateFood() : addFood()">
            <input v-model="formFood.name" placeholder="美食名称" required class="input-field" />
            <input v-model="formFood.location" placeholder="美食位置" required class="input-field" />
            <input v-model="formFood.city" placeholder="所在城市" required class="input-field" />

            <!-- 美食类型选择框 -->
            <div>
              <label>美食类型:</label>
              <select v-model="formFood.type" class="dropdown">
                <option value="">请选择</option>
                <option value="特色小吃">特色小吃</option>
                <option value="特色餐厅">特色餐厅</option>
                <option value="高档餐厅">高档餐厅</option>
                <option value="当地特产">当地特产</option>
              </select>
            </div>

            <label>上传美食图片:</label>
            <input type="file" @change="handleFileUpload" class="input-file" />
            <button type="submit" class="submit-button">{{ isEditing ? '保存修改' : '添加美食' }}</button>
          </form>
        </div>
      </div>
    </div>

  </div>


</template>

<script>
import axios from 'axios';

export default {
  name: 'FoodManagement',
  data() {
    return {
      formFood: {
        name: '',
        location: '',
        city: '',
        type: '',
        imageurl: ''
      },
      foods: [],
      isEditing: false,
      editingId: null,
      selectedFile: null
    };
  },
  methods: {
    getimageurl(imageurl) {

      console.log("图片链接:", imageurl);
      return `http://localhost:8080${imageurl}`;
    },
    handleFileUpload(event) {
      this.selectedFile = event.target.files[0];
    },
    fetchFoods() {
      axios.get('http://localhost:8080/api/foods')
        .then(response => {
          this.foods = response.data;
          console.log("后端返回的美食数据:", response.data);
        })
        .catch(error => {
          console.error("获取美食列表失败:", error);
        });
    },
    addFood() {
      const formData = new FormData();
      formData.append('name', this.formFood.name);
      formData.append('location', this.formFood.location);
      formData.append('city', this.formFood.city);
      formData.append('type', this.formFood.type);
      if (this.selectedFile) {
        formData.append('image', this.selectedFile);
      }

      axios.post('http://localhost:8080/api/foods', formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      })
        .then(response => {
          this.foods.push(response.data);
          this.resetForm();
          alert("美食添加成功！");
        })
        .catch(error => {
          console.error("添加美食失败:", error);
        });
    },
    editFood(food) {
      this.formFood = { ...food };
      this.isEditing = true;
      this.editingId = food.foodId;
    },
    updateFood() {
      const formData = new FormData();
      formData.append('name', this.formFood.name);
      formData.append('location', this.formFood.location);
      formData.append('city', this.formFood.city);
      formData.append('type', this.formFood.type);
      if (this.selectedFile) {
        formData.append('image', this.selectedFile);
      }

      axios.put(`http://localhost:8080/api/foods/${this.editingId}`, formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      })
        .then(response => {
          const index = this.foods.findIndex(f => f.foodId === this.editingId);
          this.foods.splice(index, 1, response.data);
          this.resetForm();
          alert("美食更新成功！");
        })
        .catch(error => {
          console.error("更新美食失败:", error);
        });
    },
    handleImageError(event) {
      console.error('图片加载失败: ', event.target.src);
      event.target.src = 'src/images/1.jpg'; // 可以设置一个默认图片
    },
    deleteFood(foodId) {
      axios.delete(`http://localhost:8080/api/foods/${foodId}`)
        .then(() => {
          this.foods = this.foods.filter(f => f.foodId !== foodId);
          alert("美食删除成功！");
        })
        .catch(error => {
          console.error("删除美食失败:", error);
        });
    },
    resetForm() {
      this.formFood = {
        name: '',
        location: '',
        city: '',
        type: '',
        imageurl: ''
      };
      this.isEditing = false;
      this.editingId = null;
      this.selectedFile = null;
    },
    viewFoodDetails(foodId) {
      this.$router.push({ name: 'PackageManagement', params: { id: foodId } });
    }
  },
  mounted() {
    this.fetchFoods();
  }
}
</script>

<style scoped>
/* 样式与HotelManagement.vue类似 */
.container {
  display: flex;
  justify-content: space-between;
  padding: 20px;
  gap: 30px;
}

.left-sidebar {
  width: 45%;
  margin-right: 20px;
}

.right-sidebar {
  width: 50%;
}

button {
  padding: 10px 15px;
  background-color: #4CAF50;
  color: white;
  border: none;
  cursor: pointer;
  font-size: 16px;
  border-radius: 5px;
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
  border-radius: 5px;
  background-color: #fff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

img {
  display: block;
  margin-bottom: 10px;
}

input,
textarea {
  width: 100%;
  padding: 10px;
  margin-bottom: 10px;
  border-radius: 5px;
  border: 1px solid #ddd;
}

input[type="file"] {
  padding: 5px;
}

select {
  padding: 10px;
  margin-bottom: 10px;
  border-radius: 5px;
  border: 1px solid #ddd;
}

.details-button {
  background-color: #2196F3;
}

.details-button:hover {
  background-color: #1976D2;
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
