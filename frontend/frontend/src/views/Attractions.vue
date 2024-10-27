<template>
  <div class="layout">
    <UserSidebar />
  </div>
  <div class="content">
    <div class="container">
      <div class="filters">
        <input v-model="searchQuery" placeholder="搜索景点名" class="search-box" />
        <select v-model="selectedLocation" class="dropdown">
          <option value="">所有地点</option>
          <option v-for="city in uniqueCities" :key="city" :value="city">{{ city }}</option>
        </select>
        <select v-model="selectedType" class="dropdown">
          <option value="">所有类型</option>
          <option value="自然风光">自然风光</option>
          <option value="娱乐休闲">娱乐休闲</option>
          <option value="现代都市">现代都市</option>
          <option value="历史文化">历史文化</option>
        </select>
        <select v-model="selectedAudience" class="dropdown">
          <option value="">所有人群</option>
          <option value="情侣约会">情侣约会</option>
          <option value="亲子游">亲子游</option>
          <option value="老人游">老人游</option>
          <option value="朋友聚会">朋友聚会</option>
          <option value="独自出游">独自出游</option>
        </select>
        <button @click="filterAttractions" class="filter-button">筛选</button>
        <button @click="resetFilters" class="reset-button">取消</button>
        <button @click="sortByRating" class="sort-button">按评分排序</button>
        <button @click="resetSort" class="reset-button">重置排序</button>
      </div>

      <!-- 大容器，宽度1200px -->
      <div class="attractions-list">
        <div class="row">
          <div v-for="attraction in filteredAttractions" :key="attraction.attractionId"
            @click="viewDetails(attraction.attractionId)" class="card">
            <img :src="getImageUrl(attraction.imageUrl)" alt="景点图片" class="card-image" />
            <p><strong>名称: </strong>{{ attraction.name }}</p>
            <p><strong>位置: </strong>{{ attraction.location }}</p>
            <p><strong>类型: </strong>{{ attraction.type.join(', ') }}</p>
            <p><strong>开放时间: </strong>{{ attraction.openingStart }} - {{ attraction.openingEnd }}</p>
            <p><strong>目标观众: </strong>{{ attraction.targetAudience.join(', ') }}</p>
            <p><strong>评分:
              </strong><span class="stars">
                <span v-for="star in 5" :key="star" class="star">
                  <span :style="getStarStyle(star, attraction.rating)">★</span>
                </span>
              </span>
              ({{ attraction.rating ? attraction.rating.toFixed(1) : '暂无评分' }})
            </p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "Attraction",
  data() {
    return {
      attractions: [],
      searchQuery: "",
      selectedLocation: "",
      selectedType: "",
      selectedAudience: "",
      filteredAttractions: [],
      isSortedByRating: false, // 记录是否已经按评分排序
    };
  },
  computed: {
    uniqueCities() {
      const cities = this.attractions.map((attraction) => attraction.city);
      return [...new Set(cities)];
    },
  },
  methods: {
    fetchAttractions() {
      axios.get("http://localhost:8080/api/attractions").then((response) => {
        this.attractions = response.data.map(attraction => {
          return {
            ...attraction,
            type: Array.isArray(attraction.type) ? attraction.type : [attraction.type],  // 确保 type 是数组
            targetAudience: Array.isArray(attraction.targetAudience) ? attraction.targetAudience : [attraction.targetAudience]  // 确保 targetAudience 是数组
          };
        });
        this.filteredAttractions = this.attractions;
      });
    },
    filterAttractions() {
      this.filteredAttractions = this.attractions.filter((attraction) => {
        return (
          (!this.searchQuery || attraction.name.includes(this.searchQuery)) &&
          (!this.selectedLocation || attraction.city === this.selectedLocation) &&
          (!this.selectedType || attraction.type.some(type => type.includes(this.selectedType))) &&
          (!this.selectedAudience || attraction.targetAudience.some(audience => audience.includes(this.selectedAudience)))
        );
      });

      if (this.isSortedByRating) {
        this.sortByRating();
      }
    },
    resetFilters() {
      this.searchQuery = "";
      this.selectedLocation = "";
      this.selectedType = "";
      this.selectedAudience = "";
      this.filteredAttractions = this.attractions;
      if (this.isSortedByRating) {
        this.sortByRating();
      }
    },
    sortByRating() {
      this.filteredAttractions = this.filteredAttractions.sort((a, b) => b.rating - a.rating);
      this.isSortedByRating = true;
    },
    resetSort() {
      this.filteredAttractions = [...this.attractions].sort((a, b) => a.attractionId - b.attractionId);
      this.isSortedByRating = false;
      this.filterAttractions();
    },
    getImageUrl(imageUrl) {
      return `http://localhost:8080${imageUrl}`;
    },
    viewDetails(attractionId) {
      console.log("景点ID:", attractionId);
      this.$router.push({ name: "AttractionDetails", params: { id: attractionId } });
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
  },
  mounted() {
    this.fetchAttractions();
  },

};
</script>

<style scoped>
/* 容器样式 */
.container {
  padding: 20px;
}

.filters {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
}

.search-box,
.dropdown {
  padding: 10px;
  font-size: 16px;
  width: 150px;
  border: 1px solid #4DB6AC;
  border-radius: 5px;
  background-color: #E0F7FA;
}

.filter-button,
.reset-button,
.sort-button {
  padding: 10px 20px;
  background-color: #00796B;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.filter-button:hover,
.reset-button:hover,
.sort-button:hover {
  background-color: #004D40;
}

.reset-button {
  background-color: #D32F2F;
}

.attractions-list {
  width: 1200px;
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
}

.row {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
}

.card {
  width: 300px;
  background-color: #E0F2F1;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  padding: 15px;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.card:hover {
  transform: translateY(-10px);
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
}

.card-image {
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
  cursor: pointer;
}

.star span {
  display: inline-block;
  width: 100%;
  height: 100%;
  position: relative;
}

/* 内容部分样式 */
.content {
  margin-left: 120px;
  flex-grow: 1;
  padding: 20px;
  width: 1000px;
  overflow-y: auto;
}

.search-box,
.dropdown {
  padding: 10px;
  font-size: 16px;
  width: 150px;
  border: 1px solid #4DB6AC;
  border-radius: 5px;
  background-color: #E0F7FA;
  transition: background-color 0.3s ease, transform 0.3s ease;
}

.search-box:hover,
.dropdown:hover {
  background-color: #B2EBF2;
  transform: scale(1.05);
}

.search-box:focus,
.dropdown:focus {
  outline: none;
  background-color: #80DEEA;
  border-color: #00796B;
  transform: scale(1.1);
}

.filter-button,
.reset-button,
.sort-button {
  padding: 10px 20px;
  background-color: #00796B;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease, transform 0.3s ease, box-shadow 0.3s ease;
}

.filter-button:hover,
.reset-button:hover,
.sort-button:hover {
  background-color: #004D40;
  transform: translateY(-3px);
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.2);
}

.reset-button {
  background-color: #D32F2F;
}

.sort-button {
  background-color: #388E3C;
}

button:focus {
  outline: none;
  transform: scale(1.1);
}

button:active {
  transform: translateY(2px);
}
</style>
