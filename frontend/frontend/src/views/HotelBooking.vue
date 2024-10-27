<template>
  <div class="layout">
    <UserSidebar />
  </div>
  <div class="content">
    <div class="booking-container">
      <h2 class="hotel-name">{{ hotel.name }}</h2>
      <img :src="getimageUrl(hotel.imageUrl)" alt="酒店图片" width="200" @error="handleImageError" />
      <p><strong>位置:</strong> {{ hotel.location }}</p>
      <p><strong>评分:</strong>
        <span class="stars">
          <span v-for="star in 5" :key="star" class="star">
            <span :style="getStarStyle(star, hotel.rating)">★</span>
          </span>
        </span>
        ({{ hotel.rating ? hotel.rating.toFixed(1) : '暂无评分' }})
      </p>

      <!-- 房型选择 -->
      <div class="room-selection">
        <label for="roomType">选择房型:</label>
        <select v-model="selectedRoomId" class="dropdown">
          <option v-for="room in hotel.rooms" :key="room.roomId" :value="room.roomId">
            {{ room.roomType }} - {{ room.price }}元
          </option>
        </select>
      </div>

      <!-- 日期选择 -->
      <div class="date-selection">
        <label for="checkInDate">入住日期:</label>
        <input type="date" v-model="checkInDate" class="date-input" />

        <label for="checkOutDate">退房日期:</label>
        <input type="date" v-model="checkOutDate" class="date-input" />
      </div>

      <!-- 订单确认 -->
      <button @click="confirmBooking" class="confirm-button">确认订单</button>

      <!-- 用户评论和评分 -->
      <div class="rating-section">
        <h3>用户评论<span v-if="comments.length">（{{ comments.length }} 条）</span></h3>


        <ul class="comments-list">
          <li v-for="comment in visibleComments" :key="comment.commentId" class="comment-item">
            <p><strong>评分:</strong>
              <span class="stars">
                <span v-for="star in 5" :key="star" class="star">
                  <span :style="getStarStyle(star, comment.rating)">★</span>
                </span>
              </span>
              ({{ comment.rating ? comment.rating.toFixed(1) : '暂无评分' }})
            </p>
            <p><strong>评论:</strong> {{ comment.commentText }}</p>
            <p><small>用户ID: {{ comment.userId }}</small></p>
          </li>
        </ul>
        <button v-if="comments.length > 2" @click="toggleComments" class="toggle-comments-button">
          {{ showAllComments ? '收起评论' : '展开全部评论' }}
        </button>

        <!-- 添加评论 -->
        <h3>给酒店打分</h3>
        <div class="stars-input">
          <span v-for="star in 5" :key="star" class="star" @click="setRating(star, $event.offsetX)">
            <span :style="getStarStyle(star, newRating)">★</span>
          </span>
        </div>

        <h4>添加评论</h4>
        <textarea v-model="newComment" placeholder="写下您的评论" class="comment-textarea"></textarea>
        <button @click="submitComment" class="submit-button">提交评论</button>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import { jwtDecode } from 'jwt-decode';  // 需要安装 jwt-decode 库

export default {
  data() {
    return {
      hotel: {},
      selectedRoomId: '',
      checkInDate: '',
      checkOutDate: '',
      rating: 0,
      userId: null,
      newRating: 0,
      comments: [],
      newComment: '',
      showAllComments: false,  // 控制是否显示所有评论
    };
  },
  mounted() {
    this.fetchHotelDetails();
    this.getUserIdFromToken();
    this.fetchComments();
  },
  computed: {
    visibleComments() {
      // 如果 showAllComments 为 true，显示全部评论，否则只显示前两条
      return this.showAllComments ? this.comments : this.comments.slice(0, 2);
    }
  },
  methods: {
    toggleComments() {
      // 切换显示全部评论和收起评论
      this.showAllComments = !this.showAllComments;
    },
    getimageUrl(imageUrl) {
      return `http://localhost:8080${imageUrl}`;
    },
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
    async fetchHotelDetails() {
      const hotelId = this.$route.params.id;
      const response = await axios.get(`http://localhost:8080/api/hotels/${hotelId}`);
      this.hotel = response.data;
      console.log("后端返回的酒店数据:", response.data);
    },
    async confirmBooking() {
      const bookingData = {
        userId: this.userId,
        hotelId: this.hotel.hotelId,
        roomId: this.selectedRoomId,
        checkIn: this.checkInDate,
        checkOut: this.checkOutDate,
      };
      try {
        const response = await axios.post('http://localhost:8080/api/bookings', bookingData);
        alert('订单确认成功！');
      } catch (error) {
        alert('订单确认失败');
      }
    },
    async submitComment() {
      const hotelId = this.$route.params.id;
      const commentData = {
        entityType: 'hotel',
        entityId: hotelId,
        userId: this.userId,
        rating: this.newRating,
        commentText: this.newComment,
      };
      try {
        await axios.post('http://localhost:8080/api/comments/add', commentData, {
          headers: {
            'Content-Type': 'application/json'
          }
        });
        this.newComment = '';
        this.newRating = 0;
        this.fetchComments();
      } catch (error) {
        alert('评论提交失败');
      }
    },
    async fetchComments() {
      const hotelId = this.$route.params.id;
      const response = await axios.get(`http://localhost:8080/api/comments/hotel/${hotelId}`);
      this.comments = response.data;
    },
    setRating(star, offsetX) {
      const isHalfStar = offsetX < 15;
      const rating = isHalfStar ? star - 0.5 : star;
      this.newRating = rating;
    },
    getStarStyle(star, rating) {
      const fullStars = Math.floor(rating);
      const partialStar = rating - fullStars;
      if (star <= fullStars) {
        return { color: 'gold' };
      } else if (star === fullStars + 1 && partialStar > 0) {
        return {
          color: 'gold',
          background: `linear-gradient(90deg, gold ${partialStar * 100}%, lightgray ${partialStar * 100}%)`,
          WebkitBackgroundClip: 'text',
          WebkitTextFillColor: 'transparent',
        };
      } else {
        return { color: 'lightgray' };
      }
    },
  },
};
</script>

<style scoped>
.booking-container {
  padding: 20px;
  max-width: 800px;
  margin: 0 auto;
  background-color: #f9f9f9;
  border-radius: 10px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.hotel-name {
  text-align: center;
  font-size: 24px;
  color: #2c3e50;
}

.room-selection,
.date-selection,
.rating-section {
  margin-bottom: 20px;
}

.dropdown,
.date-input {
  padding: 10px;
  margin: 10px 0;
  font-size: 16px;
  border: 1px solid #ddd;
  border-radius: 5px;
}

.confirm-button,
.submit-button {
  background-color: #4caf50;
  color: white;
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease, transform 0.3s ease;
}

.confirm-button:hover,
.submit-button:hover {
  background-color: #388e3c;
  transform: translateY(-3px);
}

.comments-list {
  list-style-type: none;
  padding: 0;
}

.comment-item {
  background-color: #f1f8e9;
  padding: 15px;
  margin-bottom: 10px;
  border-radius: 8px;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.comment-item:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

.stars-input .star {
  font-size: 30px;
  cursor: pointer;
  transition: color 0.3s ease;
}

.stars-input .star:hover {
  color: gold;
}

.comment-textarea {
  width: 100%;
  height: 100px;
  padding: 10px;
  font-size: 16px;
  border: 1px solid #ddd;
  border-radius: 5px;
  margin-bottom: 20px;
}

/* 评论列表 */
.comments-list {
  list-style-type: none;
  padding: 0;
}

.comment-item {
  background-color: #f1f8e9;
  padding: 15px;
  margin-bottom: 10px;
  border-radius: 8px;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.comment-item:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

/* 展开/收起按钮 */
.toggle-comments-button {
  background-color: #4caf50;
  color: white;
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease, transform 0.3s ease;
}

.toggle-comments-button:hover {
  background-color: #388e3c;
  transform: translateY(-3px);
}
</style>
