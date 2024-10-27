<template>
  <div class="layout">
    <UserSidebar />
  </div>
  <div class="content">
    <div class="food-container">
      <!-- 搜索框 -->
      <div class="search-box">
        <input v-model="searchQuery" placeholder="搜索美食" class="search-input" />
        <button @click="searchFoods" class="search-button">搜索</button>
      </div>

      <!-- 筛选条件 -->
      <div class="filter-box">
        <label for="foodType">美食类型:</label>
        <select v-model="selectedFoodType" class="dropdown">
          <option value="">所有类型</option>
          <option v-for="type in uniqueFoodTypes" :key="type" :value="type">{{ type }}</option>
        </select>

        <label for="location">地点:</label>
        <select v-model="selectedLocation" class="dropdown">
          <option value="">所有地点</option>
          <option v-for="city in uniqueCities" :key="city" :value="city">{{ city }}</option>
        </select>

        <label for="minPrice">最低套餐价格:</label>
        <input type="number" v-model="minPrice" placeholder="最低价格" class="price-input" />

        <label for="maxPrice">最高套餐价格:</label>
        <input type="number" v-model="maxPrice" placeholder="最高价格" class="price-input" />

        <button @click="filterFoods" class="filter-button">筛选</button>
        <button @click="resetFilters" class="reset-button">重置</button>
      </div>

      <!-- 排序 -->
      <div class="sort-box">
        <label for="sortField">排序方式:</label>
        <select v-model="sortField" class="dropdown">
          <option value="rating">评分</option>
          <option value="price">套餐价格</option>
        </select>
        <button @click="sortFoods" class="sort-button">排序</button>
      </div>

      <!-- 美食列表 -->
      <div class="food-list">
        <div class="row">
          <div v-for="food in filteredFoods" :key="food.foodId" @click="viewDetails(food.foodId)" class="food-card">
            <img :src="getImageUrl(food.imageurl)" alt="美食图片" class="food-image" @error="handleImageError" />
            <p><strong>美食名: </strong>{{ food.name }}</p>
            <p><strong>评分:</strong>
              <span class="stars">
                <span v-for="star in 5" :key="star" class="star">
                  <span :style="getStarStyle(star, food.rating)">★</span>
                </span>
              </span>
              ({{ food.rating ? food.rating.toFixed(1) : '暂无评分' }})
            </p>
            <p><strong>地点: </strong>{{ food.location }}</p>
            <p><strong>类型: </strong>{{ food.type }}</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      foods: [],
      filteredFoods: [],
      searchQuery: '',
      selectedFoodType: '',
      selectedLocation: '',
      minPrice: '',
      maxPrice: '',
      sortField: 'rating',
    };
  },
  mounted() {
    this.fetchFoods();
  },
  computed: {
    uniqueCities() {
      return [...new Set(this.foods.map((food) => food.city))];
    },
    uniqueFoodTypes() {
      return [...new Set(this.foods.map((food) => food.type))];
    },
  },
  methods: {
    getImageUrl(imageurl) {
      return `http://localhost:8080${imageurl}`;
    },

    handleImageError(event) {
      event.target.src = 'src/images/1.jpg';
    },
    async fetchFoods() {
      try {
        const response = await axios.get('http://localhost:8080/api/foods');
        this.foods = response.data;
        this.filteredFoods = this.foods;
      } catch (error) {
        console.error('获取美食数据失败:', error);
      }
    },
    searchFoods() {
      this.filteredFoods = this.foods.filter((food) =>
        food.name.toLowerCase().includes(this.searchQuery.toLowerCase())
      );
    },
    filterFoods() {
      this.filteredFoods = this.foods.filter((food) => {
        return (
          (!this.selectedFoodType || food.type === this.selectedFoodType) &&
          (!this.selectedLocation || food.city === this.selectedLocation) &&
          (!this.minPrice || (food.packages && food.packages.some((pkg) => pkg.price >= this.minPrice))) &&
          (!this.maxPrice || (food.packages && food.packages.some((pkg) => pkg.price <= this.maxPrice)))
        );
      });
    },

    resetFilters() {
      this.searchQuery = '';
      this.selectedFoodType = '';
      this.selectedLocation = '';
      this.minPrice = '';
      this.maxPrice = '';
      this.filteredFoods = this.foods;
    },
    sortFoods() {
      if (this.sortField === 'rating') {
        this.filteredFoods.sort((a, b) => b.rating - a.rating);
      } else if (this.sortField === 'price') {
        this.filteredFoods.sort((a, b) => {
          const aMinPrice = Math.min(...a.packages.map((pkg) => pkg.price));
          const bMinPrice = Math.min(...b.packages.map((pkg) => pkg.price));
          return aMinPrice - bMinPrice;
        });
      }
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
    viewDetails(foodId) {
      this.$router.push({ name: 'FoodPackage', params: { id: foodId } });
    },
  },
};
</script>

<style scoped>
.food-container {
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
.dropdown {
  padding: 5px;
  font-size: 14px;
  width: 150px;
  border: 1px solid #4DB6AC;
  border-radius: 5px;
  background-color: #E0F7FA;
  transition: background-color 0.3s ease, transform 0.3s ease;
}

.search-input:hover,
.price-input:hover,
.dropdown:hover {
  background-color: #B2EBF2;
  transform: scale(1.05);
}

.search-input:focus,
.price-input:focus,
.dropdown:focus {
  outline: none;
  background-color: #80DEEA;
  border-color: #00796B;
  transform: scale(1.1);
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
}

.search-button:hover,
.filter-button:hover,
.reset-button:hover,
.sort-button:hover {
  background-color: #004D40;
  transform: translateY(-3px);
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.2);
}

.food-list {
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

.food-card {
  width: 300px;
  background-color: #E0F2F1;
  padding: 15px;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.food-card:hover {
  transform: translateY(-10px);
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
}

.food-image {
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
  margin-left: 100px;
  flex-grow: 1;
  padding: 20px;
  width: 1300px;
  overflow-y: auto;
}
</style>
