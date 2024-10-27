<template>
  <div class="layout">
    <UserSidebar />
  </div>
  <div class="content">
    <div class="hotel-container">

      <!-- 搜索框 -->
      <div class="search-box">
        <input v-model="searchQuery" placeholder="搜索酒店名" class="search-input" />
        <button @click="searchHotels" class="search-button">搜索</button>
      </div>

      <!-- 筛选条件 -->
      <div class="filter-box">
        <label for="hotelType">酒店类型:</label>
        <select v-model="selectedHotelType" class="dropdown">
          <option value="">所有类型</option>
          <option v-for="type in uniqueHotelTypes" :key="type" :value="type">{{ type }}</option>
        </select>

        <label for="roomType">房型:</label>
        <select v-model="selectedRoomType" class="dropdown">
          <option value="">所有房型</option>
          <option v-for="roomType in uniqueRoomTypes" :key="roomType" :value="roomType">{{ roomType }}</option>
        </select>

        <label for="location">地点:</label>
        <select v-model="selectedLocation" class="dropdown">
          <option value="">所有地点</option>
          <option v-for="city in uniqueCities" :key="city" :value="city">{{ city }}</option>
        </select>

        <label for="minPrice">最低价格:</label>
        <input type="number" v-model="minPrice" placeholder="最低价格" class="price-input" />

        <label for="maxPrice">最高价格:</label>
        <input type="number" v-model="maxPrice" placeholder="最高价格" class="price-input" />

        <label for="checkInDate">入住日期:</label>
        <input type="date" v-model="checkInDate" class="date-input" />

        <label for="checkOutDate">退房日期:</label>
        <input type="date" v-model="checkOutDate" class="date-input" />

        <button @click="filterHotels" class="filter-button">筛选</button>
        <button @click="resetFilters" class="reset-button">重置</button>
      </div>

      <!-- 排序 -->
      <div class="sort-box">
        <label for="sortField">排序方式:</label>
        <select v-model="sortField" class="dropdown">
          <option value="rating">评分</option>
          <option value="price">价格</option>
        </select>
        <button @click="sortHotels" class="sort-button">排序</button>
      </div>

      <!-- 酒店列表 -->
      <div class="hotel-list">
        <div class="row">
          <div v-for="hotel in filteredHotels" :key="hotel.hotelId" @click="viewDetails(hotel.hotelId)"
            class="hotel-card">
            <img :src="getImageUrl(hotel.imageUrl)" alt="酒店图片" class="hotel-image" @error="handleImageError" />
            <p><strong>酒店名: </strong>{{ hotel.name }}</p>
            <p><strong>评分:</strong>
              <span class="stars">
                <span v-for="star in 5" :key="star" class="star">
                  <span :style="getStarStyle(star, hotel.rating)">★</span>
                </span>
              </span>
              ({{ hotel.rating ? hotel.rating.toFixed(1) : '暂无评分' }})
            </p>
            <p><strong>地点: </strong>{{ hotel.location }}</p>
            <p><strong>城市: </strong>{{ hotel.city }}</p>
            <p><strong>类型: </strong>{{ hotel.type }}</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import UserSidebar from '@/components/UserSidebar.vue';
import axios from 'axios';

export default {
  data() {
    return {
      hotels: [],
      filteredHotels: [],
      searchQuery: '',
      selectedHotelType: '',
      selectedRoomType: '',
      selectedLocation: '',
      minPrice: '',
      maxPrice: '',
      checkInDate: '',
      checkOutDate: '',
      sortField: 'rating',
    };
  },
  mounted() {
    this.fetchHotels();
  },
  computed: {
    uniqueCities() {
      const cities = this.hotels.map((hotel) => hotel.city);
      return [...new Set(cities)];
    },
    uniqueHotelTypes() {
      const types = this.hotels?.map((hotel) => hotel.type) || [];
      return [...new Set(types)];
    },
    uniqueRoomTypes() {
      const roomTypes = this.hotels?.flatMap((hotel) => hotel.rooms?.map((room) => room.roomType) || []) || [];
      return [...new Set(roomTypes)];
    },
  },
  methods: {
    getImageUrl(imageUrl) {
      return `http://localhost:8080${imageUrl}`;
    },
    async fetchHotels() {
      try {
        const response = await axios.get('http://localhost:8080/api/hotels');
        this.hotels = response.data;
        this.filteredHotels = this.hotels;
      } catch (error) {
        console.error('获取酒店数据失败:', error);
      }
    },
    searchHotels() {
      this.filteredHotels = this.hotels.filter((hotel) =>
        hotel.name.toLowerCase().includes(this.searchQuery.toLowerCase())
      );
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
    filterHotels() {
      this.filteredHotels = this.hotels.filter((hotel) => {
        return (
          (!this.selectedHotelType || hotel.type === this.selectedHotelType) &&
          (!this.selectedRoomType || hotel.rooms.some((room) => room.roomType === this.selectedRoomType)) &&
          (!this.selectedLocation || hotel.city === this.selectedLocation) &&
          (!this.minPrice || hotel.rooms.some((room) => room.price >= this.minPrice)) &&
          (!this.maxPrice || hotel.rooms.some((room) => room.price <= this.maxPrice))
        );
      });
    },
    resetFilters() {
      this.searchQuery = '';
      this.selectedHotelType = '';
      this.selectedRoomType = '';
      this.selectedLocation = '';
      this.minPrice = '';
      this.maxPrice = '';
      this.checkInDate = '';
      this.checkOutDate = '';
      this.filteredHotels = this.hotels;
    },
    sortHotels() {
      this.filteredHotels.sort((a, b) => {
        if (this.sortField === 'rating') {
          return b.rating - a.rating;
        } else if (this.sortField === 'price') {
          const aMinPrice = Math.min(...a.rooms.map((room) => room.price));
          const bMinPrice = Math.min(...b.rooms.map((room) => room.price));
          return aMinPrice - bMinPrice;
        }
      });
    },
    viewDetails(hotelId) {
      this.$router.push({ name: 'HotelBooking', params: { id: hotelId } });
    },
  },
};
</script>

<style scoped>
.hotel-container {
  padding: 20px;
}

.search-box,
.filter-box,
.sort-box {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
}

.search-input,
.price-input,
.date-input,
.dropdown {
  padding: 5px;
  font-size: 14px;
  width: 150px;
  border: 1px solid #4DB6AC;
  border-radius: 5px;
  background-color: #E0F7FA;
  transition: background-color 0.3s ease, transform 0.3s ease;
  height: 50px;
}

.search-input:hover,
.price-input:hover,
.date-input:hover,
.dropdown:hover {
  background-color: #B2EBF2;
  transform: scale(1.05);
}

.search-input:focus,
.price-input:focus,
.date-input:focus,
.dropdown:focus {
  outline: none;
  background-color: #80DEEA;
  border-color: #00796B;
  transform: scale(1.1);
  height: 50px;
}

.search-button,
.filter-button,
.reset-button,
.sort-button {
  padding: 10px 20px;
  background-color: #00796B;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease, transform 0.3s ease;
  height: 50px;
}

.search-button:hover,
.filter-button:hover,
.reset-button:hover,
.sort-button:hover {
  background-color: #004D40;
  transform: translateY(-3px);
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.2);

}

.hotel-list {
  width: 1000px;
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
}

.row {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
}

.hotel-card {
  width: 300px;
  background-color: #E0F2F1;
  padding: 15px;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.hotel-card:hover {
  transform: translateY(-10px);
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
}

.hotel-image {
  width: 100%;
  border-radius: 10px;
  margin-bottom: 10px;
}

.stars {
  display: inline-block;
}

.star {
  font-size: 20px;
  display: inline-block;
  color: lightgray;
}

/* 内容部分样式 */
.content {
  margin-left: 80px;
  flex-grow: 1;
  padding: 20px;
  width: 1300px;
  overflow-y: auto;
}
</style>
