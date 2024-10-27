<template>
  <div class="layout">
    <UserSidebar />
  </div>
  <div class="content">
    <div class="orders-container">
      <!-- 搜索框 -->
      <div class="search-box">
        <input v-model="searchQuery" placeholder="搜索订单" class="search-input" />
        <button @click="searchOrders" class="search-button">搜索</button>
      </div>

      <!-- 筛选条件 -->
      <div class="filter-box">
        <label for="orderType">订单类型:</label>
        <select v-model="selectedOrderType" class="dropdown">
          <option value="">所有类型</option>
          <option value="hotel">酒店</option>
          <option value="food">美食</option>
        </select>

        <label for="location">城市:</label>
        <select v-model="selectedCity" class="dropdown">
          <option value="">所有城市</option>
          <option v-for="city in uniqueCities" :key="city" :value="city">{{ city }}</option>
        </select>

        <button @click="filterOrders" class="filter-button">筛选</button>
        <button @click="resetFilters" class="reset-button">重置</button>
      </div>

      <!-- 排序 -->
      <div class="sort-box">
        <label for="sortField">排序方式:</label>
        <select v-model="sortField" class="dropdown">
          <option value="totalPrice">按金额</option>
          <option value="orderDate">按时间</option>
        </select>
        <button @click="sortOrders" class="sort-button">排序</button>
      </div>

      <!-- 订单列表 -->
      <div class="order-list">
        <div class="row">
          <div v-for="order in filteredOrders" :key="order.orderId" class="order-card">
            <div v-if="order.orderType === 'hotel'">
              <h3>酒店订单</h3>
              <p>酒店名: {{ order.name }}</p>
              <p>城市: {{ order.city }}</p>
              <p>下单日期: {{ order.orderDate }}</p>
              <p>总价: {{ order.totalPrice }}</p>
              <p v-if="order.nights">入住天数: {{ order.nights }} 天</p>
            </div>
            <div v-if="order.orderType === 'food'">
              <h3>美食订单</h3>
              <p>美食名: {{ order.name }}</p>
              <p>城市: {{ order.city }}</p>
              <p>订单日期: {{ order.orderDate }}</p>
              <p>总价: {{ order.totalPrice }}</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import { jwtDecode } from 'jwt-decode';

export default {
  name: 'orders',
  data() {
    return {
      orders: [],
      filteredOrders: [],
      searchQuery: '',
      selectedOrderType: '',
      selectedCity: '',
      sortField: 'totalPrice',
      userId: null,
    };
  },
  mounted() {
    this.getUserIdFromToken();
    this.fetchOrders();
  },
  computed: {
    uniqueCities() {

      const cities = this.orders.flatMap(order => {
        if (order.city) {
          return [order.city];
        }
        return [];
      });
      console.log("后端返回的城市数据:", [...new Set(cities)]);
      return [...new Set(cities)];
    }
  },
  methods: {
    getUserIdFromToken() {
      const token = localStorage.getItem('token');
      if (token) {
        try {
          const decodedToken = jwtDecode(token);
          this.userId = decodedToken.userId;
        } catch (error) {
          console.error("JWT 解码失败:", error);
          this.redirectToLogin();
        }
      } else {
        this.redirectToLogin();
      }
    },
    redirectToLogin() {
      this.$router.push('/login');
    },
    async fetchOrders() {
      try {
        const response = await axios.get(`http://localhost:8080/api/orders?userId=${this.userId}`);
        this.orders = response.data;
        this.filteredOrders = this.orders;
      } catch (error) {
        console.error('获取订单数据失败:', error);
      }
    },
    searchOrders() {
      this.filteredOrders = this.orders.filter(order => {
        if (order.orderType === 'hotel') {
          return order.hotel.name.toLowerCase().includes(this.searchQuery.toLowerCase());
        } else if (order.orderType === 'food') {
          return order.foodPackage.food.name.toLowerCase().includes(this.searchQuery.toLowerCase());
        }
        return false;
      });
    },
    filterOrders() {
      this.filteredOrders = this.orders.filter(order => {
        const matchesType = !this.selectedOrderType || order.orderType === this.selectedOrderType;
        const matchesCity =
          !this.selectedCity ||
          (order.orderType === 'hotel' && order.hotel.city === this.selectedCity) ||
          (order.orderType === 'food' && order.foodPackage.food.city === this.selectedCity);
        return matchesType && matchesCity;
      });
    },
    sortOrders() {
      this.filteredOrders.sort((a, b) => {
        if (this.sortField === 'totalPrice') {
          return b.totalPrice - a.totalPrice;
        } else if (this.sortField === 'orderDate') {
          return new Date(b.orderDate) - new Date(a.orderDate);
        }
      });
    },
    resetFilters() {
      this.searchQuery = '';
      this.selectedOrderType = '';
      this.selectedCity = '';
      this.filteredOrders = this.orders;
    }
  }
};
</script>

<style scoped>
.orders-container {
  width: 1000px;
  margin: 0 auto;
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
.dropdown:hover {
  background-color: #B2EBF2;
  transform: scale(1.05);
}

.search-input:focus,
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

.order-list {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
}

.row {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
}

.order-card {
  width: 450px;
  background-color: #E0F2F1;
  padding: 15px;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.order-card:hover {
  transform: translateY(-10px);
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
}
</style>
