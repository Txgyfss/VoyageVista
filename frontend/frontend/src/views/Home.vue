<template>
  <div class="layout">
    <UserSidebar />
  </div>
  <div class="content">
    <h1>云梦澜途</h1>

    <dr />
    <dr />
    <!-- 当前时间显示 -->
    <h3>当前时间: {{ currentTime }}</h3>

    <!-- 地点选择 -->
    <div class="location-selection">
      <label for="location">选择地点:</label>
      <select v-model="selectedLocation" @change="fetchWeather">
        <option value="" disabled>请选择地点 </option>
        <option v-for="city in uniqueCities" :key="city" :value="city">{{ city }}</option>
      </select>

      <!-- 推荐景点按钮 -->
      <button @click="filterOpenAttractions" :disabled="!selectedLocation">景点推荐</button>
    </div>

    <!-- 天气和穿衣指南容器 -->
    <div class="weather-container" v-if="weatherInfo">
      <h3>当前天气</h3>
      <p>温度: {{ convertToCelsius(weatherInfo.main.temp) }}°C</p>
      <p>天气状况: {{ weatherInfo.weather[0].description }}</p>
      <p>风速: {{ weatherInfo.wind.speed }} m/s</p>

      <!-- 穿衣指南 -->
      <h4>穿衣指南</h4>
      <p>{{ clothingAdvice }}</p>
    </div>

    <!-- 当前开放景点推荐容器 -->
    <div class="attractions-container" v-if="filteredAttractions.length">
      <h3>推荐当前开放的景点</h3>
      <div class="attractions-list">
        <div class="attraction-card" v-for="attraction in filteredAttractions" :key="attraction.attractionId"
          @click="viewAttractionDetails(attraction.attractionId)">
          <img :src="getImageUrl(attraction.imageUrl)" alt="景点图片" width="100" />
          <p><strong>{{ attraction.name }}</strong></p>
          <p>开放时间: {{ attraction.openingStart }} - {{ attraction.openingEnd }}</p>
          <p>位置: {{ attraction.location }}</p>
        </div>
      </div>
    </div>

    <!-- 资讯推荐容器 -->
    <div class="news-container">
      <h3>资讯推荐</h3>
      <div class="news-list">
        <div class="news-card" :class="{ expanded: expandedNewsIndex === index }"
          v-for="(news, index) in sortedNewsList.slice(0, 5)" :key="news.newsId" @click="toggleDetails(index)">
          <h4>{{ news.title }} <span v-if="news.pinned">[置顶]</span></h4>
          <p><strong>类型: </strong>{{ news.category }}</p>
          <p v-if="expandedNewsIndex === index">{{ news.content }}</p>
          <p v-else>点击展开详情</p>
          <p>发布时间: {{ news.publishedAt }}</p>
        </div>
      </div>



      <!-- 两个轮播容器 -->
      <div class="carousel-container">
        <div class="carousel" v-for="carousel in carousels" :key="carousel.id">
          <img :src="carousel.images[currentImageIndex]" alt="轮播图" />
        </div>
      </div>
    </div>
  </div>
</template>


<script>
import UserSidebar from "../components/UserSidebar.vue";
import axios from "axios";
import home1 from '@/images/home1.jpg';
import home2 from '@/images/home2.jpg';
import home3 from '@/images/home3.jpg';
import home4 from '@/images/home4.jpg';

import hom1 from '@/images/hom1.jpg';
import hom2 from '@/images/hom2.jpg';
import hom3 from '@/images/hom3.jpg';
import hom4 from '@/images/hom4.jpg';
export default {
  name: "Home",
  components: {
    UserSidebar
  },
  data() {
    return {
      currentTime: new Date().toLocaleString(), // 当前时间
      selectedLocation: '', // 选中的地点
      weatherInfo: null, // 天气信息
      clothingAdvice: '', // 穿衣建议
      attractions: [], // 景点信息
      filteredAttractions: [], // 当前时间开放且选定城市的景点
      newsList: [], // 资讯信息
      currentImageIndex: 0,
      carousels: [
        {
          id: 1,
          images: [home1, home2, home3, home4]
        },
        {
          id: 2,
          images: [hom1, hom2, hom3, hom4]
        }
      ]
    };
  },
  mounted() {
    this.startImageRotation();
    this.fetchAttractions();
    this.fetchNews();
    this.updateTime();
  },
  computed: {
    sortedNewsList() {
      // 先按置顶，再按发布时间排序
      const pinnedNews = this.newsList.filter(news => news.pinned);
      const nonPinnedNews = this.newsList.filter(news => !news.pinned);
      nonPinnedNews.reverse();
      return [...pinnedNews, ...nonPinnedNews];
    },
    uniqueCities() {
      return [...new Set(this.attractions.map(attraction => attraction.city))];
    }
  },
  methods: {
    startImageRotation() {
      setInterval(() => {
        this.currentImageIndex = (this.currentImageIndex + 1) % 4; // 轮播 4 张图片
      }, 3000); // 每 3 秒切换一次图片
    },
    getImageUrl(imageUrl) {
      return `http://localhost:8080${imageUrl}`;
    },
    // 更新时间
    updateTime() {
      setInterval(() => {
        this.currentTime = new Date().toLocaleString();
      }, 1000);
    },
    // 获取天气信息
    fetchWeather() {
      if (!this.selectedLocation) return;
      axios.get(`http://localhost:8080/api/weather/${this.selectedLocation}`)
        .then(response => {
          this.weatherInfo = response.data;
          this.provideClothingAdvice(this.weatherInfo.main.temp);
        })
        .catch(error => console.error("获取天气信息失败", error));
    },
    toggleDetails(index) {
      if (this.expandedNewsIndex === index) {
        this.expandedNewsIndex = null; // 如果已展开则关闭
      } else {
        this.expandedNewsIndex = index; // 展开点击的资讯
      }
    },
    // 根据温度提供穿衣建议
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
    // 获取景点数据
    fetchAttractions() {
      axios.get("http://localhost:8080/api/attractions")
        .then(response => {
          this.attractions = response.data;
        })
        .catch(error => console.error("获取景点数据失败", error));
    },
    // 筛选当前城市且时间开放的景点
    filterOpenAttractions() {
      if (!this.selectedLocation) return;
      const now = new Date();
      const currentHour = now.getHours();
      this.filteredAttractions = this.attractions.filter(attraction => {
        const [startHour] = attraction.openingStart.split(':').map(Number);
        const [endHour] = attraction.openingEnd.split(':').map(Number);
        return attraction.city === this.selectedLocation && currentHour >= startHour && currentHour <= endHour;
      });
    },
    // 获取资讯信息
    fetchNews() {
      axios.get("http://localhost:8080/api/news")
        .then(response => {
          this.newsList = response.data;
        })
        .catch(error => console.error("获取资讯失败", error));
    },
    viewAttractionDetails(attractionId) {
      this.$router.push({ name: "AttractionDetails", params: { id: attractionId } });
    },
    logout() {
      this.$router.push('/login');
    },
    convertToCelsius(temp) {
      return (temp - 273.15).toFixed(2); // 温度转换为摄氏度
    }
  }
};
</script>

<style scoped>
/* 主体样式 */
.content {
  width: 100%;
  padding: 20px;
}

/* 天气容器样式 */
.weather-container {
  margin-top: 20px;
  padding: 15px;
  background-color: #e0f7fa;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

/* 景点列表样式 */
.attractions-container {
  margin-top: 20px;
  padding: 15px;
  background-color: #f1f8e9;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.attractions-list {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
}

.attraction-card {
  width: 200px;
  padding: 10px;
  background-color: white;
  border-radius: 10px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  cursor: pointer;
  transition: transform 0.3s ease;
}

.attraction-card:hover {
  transform: scale(1.05);
}

/* 资讯容器样式 */
.news-container {
  margin-top: 20px;
  padding: 15px;
  background-color: #fffde7;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.news-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.news-card {
  padding: 10px;
  background-color: white;
  border-radius: 10px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease;
}

.news-card:hover {
  transform: scale(1.02);
}

h1,
h3,
h4 {
  color: #4CAF50;
}

button {
  margin-top: 20px;
  padding: 10px;
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

button:disabled {
  background-color: #cccccc;
  cursor: not-allowed;
}

select {
  margin: 10px 0;
  padding: 5px;
}

ul {
  list-style-type: none;
  padding: 0;
}

li {
  padding: 5px 0;
  cursor: pointer;
}

/* 内容部分样式 */
.content {
  margin-left: 150px;
  /* 留出侧边栏的空间 */
  flex-grow: 1;
  /* 让内容部分占据剩余的页面宽度 */
  padding: 20px;
  width: 600px;
  overflow-y: auto;
}

.carousel-container {
  position: fixed;
  top: 10%;
  right: 0;
  display: flex;
  flex-direction: column;
  gap: 20px;
  justify-content: flex-start;
  margin-right: 60px;
  /* 调整右边距 */
}

.carousel {
  width: 480px;
  height: 300px;
  overflow: hidden;
  position: relative;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.carousel img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.5s ease;
}

/* 新闻卡片展开样式 */
.news-card {
  padding: 10px;
  background-color: white;
  border-radius: 10px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease, max-height 0.5s ease;
  cursor: pointer;
}

.news-card:hover {
  transform: scale(1.02);
}

.news-card p {
  margin: 5px 0;
}

.news-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

/* 展开详情时的动态颜色变化 */
.news-card.expanded {
  background-color: #e0f7fa;
  /* 青色背景 */
}

.carousel img:hover {
  transform: scale(1.1);
  /* 图片放大效果 */
}
</style>
