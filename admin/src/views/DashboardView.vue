<template>
  <main class="page">
    <h1>运营控制台</h1>
    <p>先用管理员账号登录，再查看平台当前 MVP 数据。</p>
    <button class="action" @click="load">刷新数据</button>
    <p v-if="error" class="error">{{ error }}</p>
    <div class="grid">
      <section v-for="item in cards" :key="item.label" class="card">
        <div class="label">{{ item.label }}</div>
        <div class="value">{{ item.value }}</div>
      </section>
    </div>
  </main>
</template>

<script setup>
import { onMounted, ref, computed } from 'vue'
import { fetchDashboard } from '../api'

const overview = ref({
  totalUsers: 0,
  totalEngineers: 0,
  pendingEngineerReviews: 0,
  totalResources: 0,
  totalOrders: 0,
  pendingDispatchOrders: 0
})
const error = ref('')

const cards = computed(() => [
  { label: '用户数', value: overview.value.totalUsers },
  { label: '认证工程师', value: overview.value.totalEngineers },
  { label: '待审核工程师', value: overview.value.pendingEngineerReviews },
  { label: '资源数', value: overview.value.totalResources },
  { label: '订单数', value: overview.value.totalOrders },
  { label: '待派单', value: overview.value.pendingDispatchOrders }
])

async function load() {
  error.value = ''
  try {
    overview.value = await fetchDashboard()
  } catch (err) {
    error.value = err.message
  }
}

onMounted(load)
</script>

<style scoped>
.page {
  padding: 32px;
  font-family: Arial, sans-serif;
}

.action {
  margin-top: 12px;
  padding: 10px 16px;
  border: none;
  border-radius: 10px;
  background: #2563eb;
  color: #fff;
  cursor: pointer;
}

.grid {
  margin-top: 24px;
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
  gap: 16px;
}

.card {
  padding: 20px;
  border-radius: 16px;
  background: #fff;
  box-shadow: 0 10px 30px rgba(15, 23, 42, 0.08);
}

.label {
  color: #64748b;
}

.value {
  margin-top: 10px;
  font-size: 28px;
  font-weight: 700;
}

.error {
  color: #b91c1c;
}
</style>
