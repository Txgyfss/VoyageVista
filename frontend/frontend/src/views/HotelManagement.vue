<template>

  <div class="layout">
    <ASidebar />
  </div>
  <div class="content">
    <div class="container">
      <div class="left-sidebar">
        <h3>酒店列表</h3>
        <button @click="fetchHotels" class="refresh-button">刷新酒店列表</button>
        <p v-if="!hotels.length">暂无酒店</p>
        <ul v-else>
          <li v-for="hotel in hotels" :key="hotel.hotelId" class="hotel-item">
            <img :src="getImageUrl(hotel.imageUrl)" alt="酒店图片" width="100" @error="handleImageError" />
            <p>名称: {{ hotel.name }}</p>
            <p>位置: {{ hotel.location }}</p>
            <p>城市: {{ hotel.city }}</p>
            <p>类型: {{ hotel.type }}</p>

            <p v-if="hotel.rating == '' || hotel.rating == null">暂无评分</p>
            <p v-else>{{ starRating }}</p>
            <button @click="editHotel(hotel)" class="edit-button">编辑</button>
            <button @click="deleteHotel(hotel.hotelId)" class="delete-button">删除</button>
            <button @click="viewHotelDetails(hotel.hotelId)" class="details-button">详情</button>
          </li>
        </ul>
        <p v-if="hotels.length">当前有 {{ hotels.length }} 家酒店</p>
      </div>

      <div class="right-sidebar">
        <h3>{{ isEditing ? '编辑酒店' : '添加酒店' }}</h3>
        <form @submit.prevent="isEditing ? updateHotel() : addHotel()">
          <input v-model="formHotel.name" placeholder="酒店名称" required class="input-field" />
          <input v-model="formHotel.location" placeholder="酒店位置" required class="input-field" />
          <input v-model="formHotel.city" placeholder="所在城市" required class="input-field" />

          <!-- 酒店类型单选框 -->
          <div>
            <label>酒店类型:</label>
            <select v-model="formHotel.type" class="dropdown">
              <option value="">请选择</option>
              <option value="旅店">旅店</option>
              <option value="经济型">经济型</option>
              <option value="商务型">商务型</option>
              <option value="豪华型">豪华型</option>
            </select>
          </div>

          <label>上传酒店图片:</label>
          <input type="file" @change="handleFileUpload" class="input-file" />
          <button type="submit" class="submit-button">{{ isEditing ? '保存修改' : '添加酒店' }}</button>
        </form>
      </div>
    </div>
  </div>

</template>

<script>
import axios from 'axios';

export default {
  name: 'HotelManagement',
  computed: {
    starRating() {
      let rating = parseInt(this.formHotel.rating);

      if (!rating) {
        return '';
      }

      let fullStars = Math.floor(rating / 2).toString().padStart(5, '★');
      let halfStar = rating % 2 ? '½' : '';
      let emptyStars = ' '.repeat(5 - fullStars.length - halfStar.length);

      return `${fullStars}${halfStar}${emptyStars}`;
    }
  },
  data() {
    return {
      formHotel: {
        name: '',
        location: '',
        city: '',
        type: '',
        imageUrl: ''
      },
      hotels: [],
      isEditing: false,
      editingId: null,
      selectedFile: null
    };
  },
  methods: {
    getImageUrl(imageUrl) {
      return `http://localhost:8080${imageUrl}`;
    },
    handleFileUpload(event) {
      this.selectedFile = event.target.files[0];
    },
    fetchHotels() {
      axios.get('http://localhost:8080/api/hotels')
        .then(response => {
          this.hotels = response.data;
        })
        .catch(error => {
          console.error("获取酒店列表失败:", error);
        });
    },
    addHotel() {
      const formData = new FormData();
      formData.append('name', this.formHotel.name);
      formData.append('location', this.formHotel.location);
      formData.append('city', this.formHotel.city);
      formData.append('type', this.formHotel.type);
      if (this.selectedFile) {
        formData.append('image', this.selectedFile);
      }

      axios.post('http://localhost:8080/api/hotels', formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      })
        .then(response => {
          this.hotels.push(response.data);
          this.resetForm();
          alert("酒店添加成功！");
        })
        .catch(error => {
          console.error("添加酒店失败:", error);
        });
    },
    editHotel(hotel) {
      this.formHotel = { ...hotel };
      this.isEditing = true;
      this.editingId = hotel.hotelId;
    },
    updateHotel() {
      const formData = new FormData();
      formData.append('name', this.formHotel.name);
      formData.append('location', this.formHotel.location);
      formData.append('city', this.formHotel.city);
      formData.append('type', this.formHotel.type);
      if (this.selectedFile) {
        formData.append('image', this.selectedFile);
      }

      axios.put(`http://localhost:8080/api/hotels/${this.editingId}`, formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      })
        .then(response => {
          const index = this.hotels.findIndex(h => h.hotelId === this.editingId);
          this.hotels.splice(index, 1, response.data);
          this.resetForm();
          alert("酒店更新成功！");
        })
        .catch(error => {
          console.error("更新酒店失败:", error);
        });
    },
    deleteHotel(hotelId) {
      axios.delete(`http://localhost:8080/api/hotels/${hotelId}`)
        .then(() => {
          this.hotels = this.hotels.filter(h => h.hotelId !== hotelId);
          alert("酒店删除成功！");
        })
        .catch(error => {
          console.error("删除酒店失败:", error);
        });
    },
    resetForm() {
      this.formHotel = {
        name: '',
        location: '',
        city: '',
        type: '',
        imageUrl: ''
      };
      this.isEditing = false;
      this.editingId = null;
      this.selectedFile = null;
    },
    viewHotelDetails(hotelId) {
      this.$router.push({ name: 'RoomManagement', params: { id: hotelId } });
    }
  },
  mounted() {
    this.fetchHotels();
  }
}
</script>

<style scoped>
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
</style>
