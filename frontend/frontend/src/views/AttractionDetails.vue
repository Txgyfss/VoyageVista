<template>
  <div>
    <div class="layout">
      <UserSidebar />
    </div>
    <div class="content">
      <div class="details">
        <img :src="getImageUrl(attraction.imageUrl)" alt="景点图片" class="attraction-image" />
        <h2 class="attraction-title">{{ attraction.name }}</h2>
        <p><strong>位置:</strong> {{ attraction.location }}</p>
        <p><strong>类型:</strong> {{ attraction.type }}</p>
        <p><strong>开放时间:</strong> {{ attraction.openingStart }} - {{ attraction.openingEnd }}</p>
        <p><strong>目标观众:</strong> {{ attraction.targetAudience }}</p>
        <p><strong>评分:</strong>
          <span class="stars">
            <span v-for="star in 5" :key="star" class="star">
              <span :style="getStarStyle(star, attraction.rating)">★</span>
            </span>
          </span>
          ({{ attraction.rating ? attraction.rating.toFixed(1) : '暂无评分' }})
        </p>

        <!-- 天气信息 -->
        <div v-if="weatherInfo" class="weather-info">
          <h3>当前天气</h3>
          <p><strong>温度:</strong> {{ convertToCelsius(weatherInfo.main.temp) }}°C</p>
          <p><strong>天气状况:</strong> {{ weatherInfo.weather[0].description }}</p>
          <p><strong>风速:</strong> {{ weatherInfo.wind.speed }} m/s</p>

          <!-- 穿衣指南 -->
          <h4>穿衣指南</h4>
          <p>{{ clothingAdvice }}</p>
        </div>

        <!-- 用户评分 -->
        <h3>评分</h3>
        <div class="stars-input">
          <span v-for="star in 5" :key="star" class="star" @click="setRating(star, $event.offsetX)">
            <span :style="getStarStyle(star, newRating)">★</span>
          </span>
        </div>
        <button class="submit-button" @click="submitComment">提交评分</button>

        <!-- 景点票型 -->
        <h3>景点票型</h3>
        <ul class="tickets-list">
          <li v-for="ticket in tickets" :key="ticket.ticketId" class="ticket-item">
            <p><strong>票型:</strong> {{ ticket.ticketType }}</p>
            <p><strong>价格:</strong> {{ ticket.price }} 元</p>
            <p><strong>描述:</strong> {{ ticket.description }}</p>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import { jwtDecode } from 'jwt-decode';

export default {
  name: "AttractionDetails",
  data() {
    return {
      attraction: {},
      newRating: 0,
      tickets: [],
      weatherInfo: null,
      clothingAdvice: '',
      userId: null,
    };
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
    fetchAttractionDetails() {
      const attractionId = this.$route.params.id;
      axios.get(`http://localhost:8080/api/attractions/${attractionId}`).then((response) => {
        this.attraction = response.data;
        this.fetchWeather(this.attraction.city);
      });
    },
    submitComment() {
      const attractionId = this.$route.params.id;
      const comment = {
        rating: this.newRating,
        entityType: "attraction",
        entityId: attractionId,
        userId: this.userId
      };
      axios.post(`http://localhost:8080/api/comments/add`, comment)
        .then(() => {
          this.newRating = 0;
        })
        .catch(error => {
          console.error('提交失败:', error);
        });
    },
    fetchTickets() {
      const attractionId = this.$route.params.id;
      axios.get(`http://localhost:8080/api/attractions/${attractionId}/tickets`).then((response) => {
        this.tickets = response.data;
      });
    },
    fetchWeather(city) {
      axios.get(`http://localhost:8080/api/weather/${city}`)
        .then((response) => {
          this.weatherInfo = response.data;
          this.provideClothingAdvice(this.weatherInfo.main.temp);
        })
        .catch((error) => {
          console.error('获取天气信息失败:', error);
        });
    },
    provideClothingAdvice(temp) {
      if (temp <= 10 + 273.15) {
        this.clothingAdvice = '天气较冷，请穿厚外套。';
      } else if (temp <= 20 + 273.15) {
        this.clothingAdvice = '天气较凉爽，建议穿轻便的外套或毛衣。';
      } else if (temp <= 30 + 273.15) {
        this.clothingAdvice = '天气温暖，可以穿短袖或薄外套。';
      } else {
        this.clothingAdvice = '天气炎热，建议穿轻便的夏装。';
      }
    },
    getImageUrl(imageUrl) {
      return `http://localhost:8080${imageUrl}`;
    },
    convertToCelsius(temp) {
      return (temp - 273.15).toFixed(2);
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
          WebkitTextFillColor: 'transparent'
        };
      } else {
        return { color: 'lightgray' };
      }
    },
    setRating(star, offsetX) {
      const isHalfStar = offsetX < 15;
      const rating = isHalfStar ? star - 0.5 : star;
      this.newRating = rating;
    },
  },
  mounted() {
    this.fetchAttractionDetails();
    this.getUserIdFromToken();
    this.fetchTickets();
  },
};
</script>

<style scoped>
.details {
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 10px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  max-width: 800px;
  margin: 0 auto;
}

.attraction-image {
  border-radius: 10px;
  transition: transform 0.3s ease;
  width: 100%;
  max-width: 400px;
}

.attraction-image:hover {
  transform: scale(1.05);
}

.attraction-title {
  margin-top: 20px;
  color: #2c3e50;
  text-align: center;
}

.weather-info {
  background-color: #e0f7fa;
  padding: 10px;
  border-radius: 10px;
  margin-top: 20px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.stars-input .star {
  font-size: 30px;
  cursor: pointer;
  transition: color 0.3s ease;
}

.stars-input .star:hover {
  color: gold;
}

.submit-button {
  margin-top: 20px;
  padding: 10px 20px;
  background-color: #4caf50;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease, transform 0.3s ease;
}

.submit-button:hover {
  background-color: #388e3c;
  transform: translateY(-3px);
}

.tickets-list {
  margin-top: 20px;
}

.ticket-item {
  background-color: #f1f8e9;
  padding: 15px;
  margin-bottom: 10px;
  border-radius: 8px;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.ticket-item:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}
</style>
