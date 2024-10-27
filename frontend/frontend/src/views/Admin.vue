<template>
  <div>
    <div class="layout">
      <ASidebar />
    </div>
  </div>

</template>

<script>
export default {
  name: 'Admin',
}
</script>

<style scoped>
.admin-container {
  display: flex;
}

.sidebar {
  width: 200px;
  background-color: #f4f4f4;
  padding: 20px;
}

.sidebar ul {
  list-style-type: none;
  padding: 0;
}

.sidebar ul li {
  margin-bottom: 10px;
}

.sidebar ul li a {
  text-decoration: none;
  color: #333;
}

.sidebar ul li a:hover {
  text-decoration: underline;
}

.content {
  flex-grow: 1;
  padding: 20px;
}
</style>
