<template>
  <div class="shell">
    <aside class="sidebar">
      <div class="brand">GameTech Admin</div>
      <router-link to="/">控制台</router-link>
      <router-link to="/engineers">工程师审核</router-link>
      <router-link to="/resources">资源管理</router-link>
      <router-link to="/orders">订单派单</router-link>
      <router-link to="/login">登录</router-link>
    </aside>
    <main class="content">
      <router-view />
    </main>
  </div>
</template>

<style scoped>
.shell {
  min-height: 100vh;
  display: grid;
  grid-template-columns: 220px 1fr;
  background: #0f172a;
}

.sidebar {
  padding: 24px 18px;
  display: flex;
  flex-direction: column;
  gap: 12px;
  background: #111827;
}

.brand {
  margin-bottom: 12px;
  font-size: 20px;
  font-weight: 700;
  color: #fff;
}

.sidebar a {
  padding: 10px 12px;
  border-radius: 10px;
  color: #cbd5e1;
  text-decoration: none;
}

.sidebar a.router-link-active {
  background: #2563eb;
  color: #fff;
}

.content {
  min-width: 0;
  background: #f8fafc;
}

@media (max-width: 900px) {
  .shell {
    grid-template-columns: 1fr;
  }

  .sidebar {
    flex-direction: row;
    flex-wrap: wrap;
  }
}
</style>
