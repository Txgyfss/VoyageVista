<template>

  <div class="layout">
    <ASidebar />
  </div>
  <div class="content">
    <div>
      <img :src="getImageUrl(attraction.imageUrl)" alt="景点图片" width="200" @error="handleImageError" />
      <h2>景点详情 - {{ attraction.name }}</h2>
      <p>位置: {{ attraction.location }}</p>
      <p>类型: {{ Array.isArray(attraction.type) ? attraction.type.join(', ') : attraction.type }}</p>
      <p>开放时间: {{ attraction.openingStart }} - {{ attraction.openingEnd }}</p>
      <p>城市: {{ attraction.city }}</p>
      <p>游玩时长: {{ attraction.duration }} 分钟</p>
      <h3>票型管理</h3>
      <ul>
        <li v-for="ticket in tickets" :key="ticket.ticketId">
          <p>票型名称: {{ ticket.ticketType }}</p>
          <p>票价: {{ ticket.price }}</p>
          <p>描述: {{ ticket.description }}</p>
          <button @click="editTicket(ticket)">编辑票型</button>
          <button @click="deleteTicket(ticket.ticketId)">删除票型</button>
        </li>
      </ul>

      <h4>{{ isEditingTicket ? '编辑票型' : '添加票型' }}</h4>
      <form @submit.prevent="isEditingTicket ? updateTicket() : addTicket()">
        <input v-model="formTicket.ticketType" placeholder="票型名称" required />
        <input v-model="formTicket.price" type="number" placeholder="票价" required />
        <textarea v-model="formTicket.description" placeholder="票型描述"></textarea>
        <button type="submit">{{ isEditingTicket ? '保存修改' : '添加票型' }}</button>
      </form>
    </div>
  </div>

</template>

<script>
import axios from 'axios';

export default {
  name: 'TicketManagement',
  data() {
    return {
      attraction: {}, // 存储景点信息
      tickets: [], // 存储票型信息
      formTicket: {
        ticketType: '',
        price: '',
        description: ''
      },
      isEditingTicket: false,
      selectedTicketId: null
    };
  },
  methods: {
    getImageUrl(imageUrl) {
      // 将 imageUrl 转换为完整的 URL
      return `http://localhost:8080${imageUrl}`;
    },
    fetchAttractionDetails() {
      const attractionId = this.$route.params.id;
      // 获取景点详情
      axios.get(`http://localhost:8080/api/attractions/${attractionId}`)
        .then(response => {
          this.attraction = response.data;
        })
        .catch(error => {
          console.error("获取景点详情失败:", error);
        });
    },
    fetchTickets() {
      const attractionId = this.$route.params.id;
      // 获取票型信息
      axios.get(`http://localhost:8080/api/attractions/${attractionId}/tickets`)
        .then(response => {
          this.tickets = response.data;
        })
        .catch(error => {
          console.error("获取票型信息失败:", error);
        });
    },
    addTicket() {
      const attractionId = this.$route.params.id;
      axios.post(`http://localhost:8080/api/attractions/${attractionId}/tickets`, this.formTicket)
        .then(response => {
          this.tickets.push(response.data);
          this.resetTicketForm();
          alert("票型添加成功！");
        })
        .catch(error => {
          console.error("添加票型失败:", error);
        });
    },
    editTicket(ticket) {
      this.formTicket = { ...ticket };
      this.isEditingTicket = true;
      this.selectedTicketId = ticket.ticketId;
    },
    updateTicket() {
      axios.put(`http://localhost:8080/api/tickets/${this.selectedTicketId}`, this.formTicket)
        .then(response => {
          const index = this.tickets.findIndex(t => t.ticketId === this.selectedTicketId);
          this.tickets.splice(index, 1, response.data);
          this.resetTicketForm();
          alert("票型更新成功！");
        })
        .catch(error => {
          console.error("更新票型失败:", error);
        });
    },
    deleteTicket(ticketId) {
      axios.delete(`http://localhost:8080/api/tickets/${ticketId}`)
        .then(() => {
          this.tickets = this.tickets.filter(t => t.ticketId !== ticketId);
          alert("票型删除成功！");
        })
        .catch(error => {
          console.error("删除票型失败:", error);
        });
    },
    resetTicketForm() {
      this.formTicket = {
        ticketType: '',
        price: '',
        description: ''
      };
      this.isEditingTicket = false;
      this.selectedTicketId = null;
    }
  },
  mounted() {
    this.fetchAttractionDetails();
    this.fetchTickets();
  }
}
</script>

<style scoped>
/* 设置整体布局 */
.container {
  display: flex;
  justify-content: space-between;
  padding: 20px;
  background-color: #f9f9f9;
}

/* 设置左侧和右侧的间距 */
.left-sidebar,
.right-sidebar {
  width: 48%;
}

/* 设置票型列表的样式 */
ul {
  list-style-type: none;
  padding: 0;
}

li {
  background-color: #fff;
  margin-bottom: 15px;
  padding: 15px;
  border-radius: 10px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

/* 样式化按钮 */
button {
  padding: 10px 20px;
  margin: 10px 0;
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 16px;
  transition: background-color 0.3s ease;
}

button:hover {
  background-color: #45a049;
}

button.edit {
  background-color: #2196F3;
}

button.delete {
  background-color: #f44336;
}

/* 调整表单输入框样式 */
input,
textarea {
  width: 100%;
  padding: 10px;
  margin-bottom: 15px;
  border-radius: 5px;
  border: 1px solid #ddd;
  font-size: 16px;
}

input[type="file"] {
  padding: 5px;
}

input:focus,
textarea:focus {
  outline: none;
  border-color: #4CAF50;
  box-shadow: 0 0 5px rgba(0, 0, 0, 0.1);
}

/* 标题样式 */
h2,
h3,
h4 {
  color: #333;
  font-family: 'Roboto', sans-serif;
  margin-bottom: 20px;
}

/* 表单的按钮样式 */
form button {
  width: 100%;
  background-color: #4CAF50;
  padding: 10px 0;
  color: white;
  border: none;
  border-radius: 5px;
  font-size: 18px;
  cursor: pointer;
}

form button:hover {
  background-color: #45a049;
}

/* 页面主体背景 */
body {
  background-color: #f5f5f5;
  font-family: 'Roboto', sans-serif;
  color: #333;
}
</style>
