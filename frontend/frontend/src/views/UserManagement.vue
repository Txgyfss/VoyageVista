<template>

  <div class="layout">
    <ASidebar />
  </div>
  <div class="content">
    <div class="blog-management">
      <div class="user-management">
        <h2>用户管理</h2>
        <p>在这里你可以查看、编辑、删除用户信息。</p>

        <table>
          <thead>
            <tr>
              <th>ID</th>
              <th>用户名</th>
              <th>邮箱</th>
              <th>电话</th>
              <th>角色</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="user in users" :key="user.userId">
              <td>{{ user.userId }}</td>
              <td>{{ user.username }}</td>
              <td>{{ user.email }}</td>
              <td>{{ user.phoneNumber }}</td>
              <td>{{ user.role }}</td>
              <td>
                <button @click="editUser(user)">编辑</button>
                <button @click="deleteUser(user.userId)">删除</button>
              </td>
            </tr>
          </tbody>
        </table>

        <!-- 编辑用户表单 -->
        <div v-if="selectedUser">
          <h3>编辑用户</h3>
          <form @submit.prevent="updateUser">
            <label>用户名:</label>
            <input v-model="selectedUser.username" />

            <label>邮箱:</label>
            <input v-model="selectedUser.email" />

            <label>电话:</label>
            <input v-model="selectedUser.phoneNumber" />

            <label>角色:</label>
            <select v-model="selectedUser.role">
              <option value="USER">普通用户</option>
              <option value="ADMIN">管理员</option>
            </select>

            <button type="submit">保存</button>
          </form>
        </div>
        <!-- 创建新用户表单 -->
        <div>
          <h3>创建新用户</h3>
          <form @submit.prevent="createUser">
            <label>用户名:</label>
            <input v-model="newUser.username" />

            <label>邮箱:</label>
            <input v-model="newUser.email" />

            <label>电话:</label>
            <input v-model="newUser.phoneNumber" />

            <label>密码:</label>
            <input type="password" v-model="newUser.password" />

            <label>角色:</label>
            <select v-model="newUser.role">
              <option value="USER">普通用户</option>
              <option value="ADMIN">管理员</option>
            </select>

            <button type="submit">创建用户</button>
          </form>
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
      users: [],           // 存储用户列表
      selectedUser: null,  // 被选中的用户
      newUser: {           // 创建用户时的数据，确保所有字段被初始化
        username: '',
        email: '',
        phoneNumber: '',
        password: '',
        role: 'USER'
      }
    };
  },

  methods: {
    // 获取所有用户
    fetchUsers() {
      axios.get('http://localhost:8080/api/user-management')
        .then(response => {
          this.users = response.data;
          console.log("后端返回的攻略数据:", response.data);
        })
        .catch(error => {
          console.error('获取用户失败', error);
        });
    },
    // 创建新用户
    createUser() {
      axios.post('http://localhost:8080/api/user-management', this.newUser)
        .then(response => {
          this.fetchUsers();  // 刷新用户列表
          this.resetNewUser();  // 重置表单
        })
        .catch(error => {
          console.error('创建用户失败', error);
        });
    },
    // 重置创建用户表单
    resetNewUser() {
      this.newUser = {
        username: '',
        email: '',
        phoneNumber: '',
        password: '',
        role: 'USER'
      };
    },
    // 编辑用户
    editUser(user) {
      this.selectedUser = { ...user };
    },
    // 更新用户信息
    updateUser() {
      axios.put(`http://localhost:8080/api/user-management/${this.selectedUser.userId}`, this.selectedUser)
        .then(response => {
          this.selectedUser = null;  // 清空选择
          this.fetchUsers();         // 刷新用户列表
        })
        .catch(error => {
          console.error('更新用户失败', error);
        });
    },
    // 删除用户
    deleteUser(userId) {
      axios.delete(`http://localhost:8080/api/user-management/${userId}`)
        .then(() => {
          this.fetchUsers();  // 刷新用户列表
        })
        .catch(error => {
          console.error('删除用户失败', error);
        });
    }
  },
  mounted() {
    // 组件加载时获取用户列表
    this.fetchUsers();
  }
};
</script>

<style scoped>
h2 {
  color: #4CAF50;
}

p {
  font-size: 16px;
}

table {
  width: 100%;
  border-collapse: collapse;
}

th,
td {
  border: 1px solid #ddd;
  padding: 8px;
  text-align: left;
}

th {
  background-color: #f4f4f4;
}

button {
  margin-right: 10px;
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
