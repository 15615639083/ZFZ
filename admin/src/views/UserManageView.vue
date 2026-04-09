<template>
  <main class="page">
    <h1>用户管理</h1>
    <button class="action" @click="load">刷新</button>
    <p v-if="error" class="error">{{ error }}</p>
    <div class="table">
      <div class="row head">
        <span>ID</span>
        <span>用户名</span>
        <span>真实姓名</span>
        <span>手机号</span>
        <span>邮箱</span>
        <span>角色</span>
        <span>状态</span>
        <span>操作</span>
      </div>
      <div class="row" v-for="item in list" :key="item.id">
        <span>{{ item.id }}</span>
        <span>{{ item.username }}</span>
        <span>{{ item.realName || '-' }}</span>
        <span>{{ item.phone || '-' }}</span>
        <span>{{ item.email || '-' }}</span>
        <span>{{ item.role }}</span>
        <span>{{ item.status }}</span>
        <span class="ops">
          <button class="mini" @click="detail(item.id)">详情</button>
          <button class="mini ghost" @click="toggle(item)">{{ item.status === 'disabled' ? '启用' : '禁用' }}</button>
        </span>
      </div>
    </div>
    <div v-if="current" class="detail-card">
      <h2>用户详情</h2>
      <p>ID：{{ current.id }}</p>
      <p>用户名：{{ current.username }}</p>
      <p>真实姓名：{{ current.realName || '-' }}</p>
      <p>手机号：{{ current.phone || '-' }}</p>
      <p>邮箱：{{ current.email || '-' }}</p>
      <p>角色：{{ current.role }}</p>
      <p>状态：{{ current.status }}</p>
    </div>
  </main>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { fetchUserDetail, fetchUsers, updateUserStatus } from '../api'

const list = ref([])
const current = ref(null)
const error = ref('')

async function load() {
  error.value = ''
  try {
    list.value = await fetchUsers()
  } catch (err) {
    error.value = err.message
  }
}

async function detail(id) {
  error.value = ''
  try {
    current.value = await fetchUserDetail(id)
  } catch (err) {
    error.value = err.message
  }
}

async function toggle(item) {
  error.value = ''
  try {
    await updateUserStatus(item.id, { status: item.status === 'disabled' ? 'active' : 'disabled' })
    if (current.value && current.value.id === item.id) {
      current.value = await fetchUserDetail(item.id)
    }
    await load()
  } catch (err) {
    error.value = err.message
  }
}

onMounted(load)
</script>

<style scoped>
.page { padding: 32px; }
.action { padding: 10px 14px; border-radius: 10px; border: none; background: #2563eb; color: #fff; }
.table { margin-top: 20px; border-radius: 14px; overflow: hidden; background: #fff; }
.row { display: grid; grid-template-columns: 70px 1fr 1fr 1fr 1.2fr 90px 90px 150px; gap: 12px; padding: 14px 16px; border-bottom: 1px solid #e2e8f0; }
.head { background: #eff6ff; font-weight: 700; }
.ops { display: flex; gap: 8px; }
.mini { padding: 6px 10px; border: none; border-radius: 8px; background: #2563eb; color: #fff; }
.mini.ghost { background: #475569; }
.detail-card { margin-top: 20px; padding: 18px; border-radius: 14px; background: #fff; }
.error { color: #b91c1c; }
</style>
