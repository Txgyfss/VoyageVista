<template>

  <div class="layout">
    <ASidebar />
  </div>
  <div class="content">
    <div>
      <h2>美食详情 - {{ food.name }}</h2>
      <p>位置: {{ food.location }}</p>
      <p>城市: {{ food.city }}</p>
      <p>类型: {{ food.type }}</p>
      <p v-if="food.rating == '' || food.rating == null">暂无评分</p>
      <p v-else>{{ starRating }}</p>
      <h3>套餐管理</h3>
      <ul>
        <li v-for="foodpackage in packages" :key="foodpackage.packageId">
          <p>套餐名称: {{ foodpackage.packageName }}</p>
          <p>价格: {{ foodpackage.price }}</p>
          <p>描述: {{ foodpackage.description }}</p>
          <button @click="editPackage(package)">编辑套餐</button>
          <button @click="deletePackage(package.packageId)">删除套餐</button>
        </li>
      </ul>

      <h4>{{ isEditingPackage ? '编辑套餐' : '添加套餐' }}</h4>
      <form @submit.prevent="isEditingPackage ? updatePackage() : addPackage()">
        <input v-model="formPackage.packageName" placeholder="套餐名称" required />
        <input v-model="formPackage.price" type="number" placeholder="价格" required />
        <textarea v-model="formPackage.description" placeholder="描述"></textarea>
        <button type="submit">{{ isEditingPackage ? '保存修改' : '添加套餐' }}</button>
      </form>
    </div>
  </div>

</template>

<script>
import axios from 'axios';

export default {
  name: 'PackageManagement',
  data() {
    return {
      food: {},
      packages: [],
      formPackage: {
        packageName: '',
        price: '',
        description: ''
      },
      isEditingPackage: false,
      selectedPackageId: null
    };
  },
  methods: {
    fetchFoodDetails() {
      const foodId = this.$route.params.id;
      axios.get(`http://localhost:8080/api/foods/${foodId}`)
        .then(response => {
          this.food = response.data;
        })
        .catch(error => {
          console.error("获取美食详情失败:", error);
        });
    },
    fetchPackages() {
      const foodId = this.$route.params.id;
      axios.get(`http://localhost:8080/api/foods/${foodId}/packages`)
        .then(response => {
          this.packages = response.data;
        })
        .catch(error => {
          console.error("获取套餐信息失败:", error);
        });
    },
    addPackage() {
      const foodId = this.$route.params.id;
      axios.post(`http://localhost:8080/api/foods/${foodId}/packages`, this.formPackage)
        .then(response => {
          this.packages.push(response.data);
          this.resetPackageForm();
          alert("套餐添加成功！");
        })
        .catch(error => {
          console.error("添加套餐失败:", error);
        });
    },
    editPackage(packageItem) {
      this.formPackage = { ...packageItem };
      this.isEditingPackage = true;
      this.selectedPackageId = packageItem.packageId;
    },
    updatePackage() {
      axios.put(`http://localhost:8080/api/packages/${this.selectedPackageId}`, this.formPackage)
        .then(response => {
          const index = this.packages.findIndex(p => p.packageId === this.selectedPackageId);
          this.packages.splice(index, 1, response.data);
          this.resetPackageForm();
          alert("套餐更新成功！");
        })
        .catch(error => {
          console.error("更新套餐失败:", error);
        });
    },
    deletePackage(packageId) {
      axios.delete(`http://localhost:8080/api/packages/${packageId}`)
        .then(() => {
          this.packages = this.packages.filter(p => p.packageId !== packageId);
          alert("套餐删除成功！");
        })
        .catch(error => {
          console.error("删除套餐失败:", error);
        });
    },

    resetPackageForm() {
      this.formPackage = {
        packageName: '',
        price: '',
        description: ''
      };
      this.isEditingPackage = false;
      this.selectedPackageId = null;
    }
  },
  mounted() {
    this.fetchFoodDetails();
    this.fetchPackages();
  }
}
</script>

<style scoped>
/* 与RoomManagement.vue的样式类似 */
ul {
  list-style-type: none;
  padding: 0;
}

li {
  margin-bottom: 20px;
  border: 1px solid #ddd;
  padding: 10px;
}

input,
textarea {
  width: 100%;
  padding: 10px;
  margin-bottom: 10px;
}

button {
  padding: 10px 20px;
  margin: 10px 0;
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 16px;
}

button:hover {
  background-color: #45a049;
}
</style>
