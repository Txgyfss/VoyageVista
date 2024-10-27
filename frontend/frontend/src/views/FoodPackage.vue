<template>
  <div class="layout">
    <UserSidebar />
  </div>
  <div class="content">
    <div class="food-package-container">
      <h2>{{ food.name }}</h2>
      <img :src="getimageurl(food.imageurl)" alt="美食图片" class="food-image" @error="handleImageError" />
      <p><strong>位置:</strong> {{ food.location }}</p>
      <p><strong>评分:</strong>
        <span class="stars">
          <span v-for="star in 5" :key="star" class="star">
            <span :style="getStarStyle(star, food.rating)">★</span>
          </span>
        </span>
        ({{ food.rating ? food.rating.toFixed(1) : '暂无评分' }})
      </p>
      <!-- 套餐列表 -->
      <div class="package-list">
        <h3>套餐</h3>
        <ul>
          <li v-for="pkg in food.packages" :key="pkg.packageId" class="package-item">
            <p><strong>套餐名:</strong> {{ pkg.packageName }}</p>
            <p><strong>价格:</strong> {{ pkg.price }} 元</p>
            <p><strong>描述:</strong> {{ pkg.description }}</p>
            <button @click="purchasePackage(pkg.packageId)" class="buy-button">购买套餐</button>
          </li>
        </ul>
      </div>

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

        <h3>给美食打分</h3>
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
import { jwtDecode } from 'jwt-decode';

export default {
  data() {
    return {
      food: {},
      comments: [],
      newCommentText: '',
      rating: 0,
      newRating: 0,
      userId: null,
      newComment: '',
      showAllComments: false,  // 控制是否显示所有评论
    };
  },
  mounted() {
    this.getUserIdFromToken();
    this.fetchFoodDetails();
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
    getimageurl(imageurl) {
      return `http://localhost:8080${imageurl}`;
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
    async fetchFoodDetails() {
      const foodId = this.$route.params.id;
      const response = await axios.get(`http://localhost:8080/api/foods/${foodId}`);
      console.log("后端返回的美食数据:", response.data);
      this.food = response.data;
    },
    async fetchComments() {
      const foodId = this.$route.params.id;
      const response = await axios.get(`http://localhost:8080/api/comments/food/${foodId}`);
      this.comments = response.data;
    },
    async submitComment() {
      const foodId = this.$route.params.id;
      const commentData = {
        entityType: 'food',
        entityId: foodId,
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
        this.fetchFoodDetails();
      } catch (error) {
        alert('评论提交失败');
      }
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
    async purchasePackage(packageId) {
      try {
        const requestData = {
          userId: this.userId,
          packageId: packageId
        };
        const response = await axios.post('http://localhost:8080/api/foodOrders/purchase', null, {
          params: requestData
        });
        alert('购买成功！');
      } catch (error) {
        alert('购买失败');
      }
    },
  },
};
</script>

<style scoped>
.food-package-container {
  padding: 20px;
  max-width: 800px;
  margin: 0 auto;
  background-color: #e0f7fa;
  border-radius: 10px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

h2 {
  color: #00796b;
  text-align: center;
}

.food-image {
  display: block;
  margin: 0 auto 20px;
  border-radius: 10px;
}

.package-list ul {
  list-style-type: none;
  padding: 0;
}

.package-item {
  background-color: #f1f8e9;
  margin-bottom: 10px;
  padding: 15px;
  border-radius: 10px;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
}

.package-item:hover {
  background-color: #e0f7fa;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.15);
}

.buy-button {
  padding: 10px;
  background-color: #00796b;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease, transform 0.3s ease;
}

.buy-button:hover {
  background-color: #004d40;
  transform: translateY(-3px);
}

.rating-section h3 {
  color: #00796b;
}

.stars-input .star {
  font-size: 30px;
  cursor: pointer;
  color: lightgray;
}

.stars-input .star:hover {
  color: gold;
}

.comment-item {
  background-color: #f1f8e9;
  padding: 15px;
  border-radius: 10px;
  margin-bottom: 10px;
}

.comment-textarea {
  width: 100%;
  height: 100px;
  margin-top: 10px;
  padding: 10px;
  border-radius: 5px;
  border: 1px solid #ddd;
}

.submit-button {
  margin-top: 10px;
  padding: 10px 20px;
  background-color: #00796b;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease, transform 0.3s ease;
}

.submit-button:hover {
  background-color: #004d40;
  transform: translateY(-3px);
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
</style>
