<template>

  <div class="layout">
    <ASidebar />
  </div>
  <div class="content">
    <div>
      <h2>酒店详情 - {{ hotel.name }}</h2>
      <p>位置: {{ hotel.location }}</p>
      <p>城市: {{ hotel.city }}</p>
      <p>类型: {{ hotel.type }}</p>
      <p>评分：{{ hotel.rating ? hotel.rating.toFixed(1) : '暂无评分' }}</p>

      <h3>房型管理</h3>
      <ul>
        <li v-for="room in rooms" :key="room.roomId">
          <p>房型名称: {{ room.roomType }}</p>
          <p>价格: {{ room.price }}</p>
          <p>描述: {{ room.description }}</p>
          <button @click="editRoom(room)">编辑房型</button>
          <button @click="deleteRoom(room.roomId)">删除房型</button>
        </li>
      </ul>

      <h4>{{ isEditingRoom ? '编辑房型' : '添加房型' }}</h4>
      <form @submit.prevent="isEditingRoom ? updateRoom() : addRoom()">
        <input v-model="formRoom.roomType" placeholder="房型名称" required />
        <input v-model="formRoom.price" type="number" placeholder="价格" required />
        <textarea v-model="formRoom.description" placeholder="描述"></textarea>
        <button type="submit">{{ isEditingRoom ? '保存修改' : '添加房型' }}</button>
      </form>
    </div>
  </div>

</template>

<script>
import axios from 'axios';

export default {
  name: 'RoomManagement',
  data() {
    return {
      hotel: [],
      fromHotel: {
        name: '',
        location: '',
        city: '',
        type: '',
        rating: '',
      },
      rooms: [],
      formRoom: {
        roomType: '',
        price: '',
        description: '',
        // 初始化 rating 避免 undefined 错误
      },
      isEditingRoom: false,
      selectedRoomId: null
    };
  },
  computed: {
    starRating() {
      // 检查 hotel 是否有 rating 属性，避免未加载时访问 undefined
      if (!this.hotel || this.hotel.rating == null || this.hotel.rating === '') {
        return '';
      }

      // 确保 rating 是有效的数值
      let rating = parseInt(this.hotel.rating);
      if (isNaN(rating) || rating < 0) {
        return ''; // 处理无效 rating
      }

      // 计算 fullStars 和 halfStar
      let fullStarsCount = Math.floor(rating / 2);
      let halfStar = rating % 2 ? '½' : '';

      // 确保 fullStarsCount 不会超出 5
      let fullStars = '★'.repeat(Math.min(fullStarsCount, 5));
      let emptyStars = ' '.repeat(5 - fullStars.length - halfStar.length);

      return `${fullStars}${halfStar}${emptyStars}`;
    }
  },
  methods: {
    fetchHotelDetails() {
      const hotelId = this.$route.params.id;
      axios.get(`http://localhost:8080/api/hotels/${hotelId}`)
        .then(response => {
          this.hotel = response.data;
          console.log("后端返回的酒店数据:", response.data);
        })
        .catch(error => {
          console.error("获取酒店详情失败:", error);
        });
    },
    fetchRooms() {
      const hotelId = this.$route.params.id;
      axios.get(`http://localhost:8080/api/hotels/${hotelId}/rooms`)
        .then(response => {
          this.rooms = response.data;
        })
        .catch(error => {
          console.error("获取房型信息失败:", error);
        });
    },
    addRoom() {
      const hotelId = this.$route.params.id;
      axios.post(`http://localhost:8080/api/hotels/${hotelId}/rooms`, this.formRoom)
        .then(response => {
          this.rooms.push(response.data);
          this.resetRoomForm();
          alert("房型添加成功！");
        })
        .catch(error => {
          console.error("添加房型失败:", error);
        });
    },
    editRoom(room) {
      this.formRoom = { ...room };
      this.isEditingRoom = true;
      this.selectedRoomId = room.roomId;
    },
    updateRoom() {
      axios.put(`http://localhost:8080/api/rooms/${this.selectedRoomId}`, this.formRoom)
        .then(response => {
          const index = this.rooms.findIndex(r => r.roomId === this.selectedRoomId);
          this.rooms.splice(index, 1, response.data);
          this.resetRoomForm();
          alert("房型更新成功！");
        })
        .catch(error => {
          console.error("更新房型失败:", error);
        });
    },
    deleteRoom(roomId) {
      axios.delete(`http://localhost:8080/api/rooms/${roomId}`)
        .then(() => {
          this.rooms = this.rooms.filter(r => r.roomId !== roomId);
          alert("房型删除成功！");
        })
        .catch(error => {
          console.error("删除房型失败:", error);
        });
    },
    resetRoomForm() {
      this.formRoom = {
        roomType: '',
        price: '',
        description: ''
      };
      this.isEditingRoom = false;
      this.selectedRoomId = null;
    }
  },
  mounted() {
    this.fetchHotelDetails();
    this.fetchRooms();
  }
}
</script>

<style scoped>
/* 同HotelManagement.vue样式 */
ul {
  list-style-type: none;
  padding: 0;
}

li {
  margin-bottom: 20px;
  border: 1px solid #ddd;
  padding: 10px;
}

input,
textarea {
  width: 100%;
  padding: 10px;
  margin-bottom: 10px;
}

button {
  padding: 10px 20px;
  margin: 10px 0;
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 16px;
}

button:hover {
  background-color: #45a049;
}
</style>
