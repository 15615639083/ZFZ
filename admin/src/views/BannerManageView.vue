<template>
  <main class="page">
    <h1>Banner 配置</h1>
    <form class="form" @submit.prevent="submit">
      <input v-model="form.title" placeholder="标题" />
      <input v-model="form.imageUrl" placeholder="图片 URL" />
      <input v-model="form.path" placeholder="跳转路径，例如 /pages/resource/index" />
      <input v-model="form.sortOrder" placeholder="排序值" />
      <select v-model="form.status">
        <option value="active">active</option>
        <option value="disabled">disabled</option>
      </select>
      <button>{{ form.id ? '保存修改' : '新增 Banner' }}</button>
    </form>
    <div v-for="item in list" :key="item.id" class="card">
      <strong>{{ item.title }}</strong>
      <p>{{ item.path }}</p>
      <p>{{ item.imageUrl }}</p>
      <p>排序：{{ item.sortOrder }} / 状态：{{ item.status }}</p>
      <button class="edit" @click="edit(item)">编辑</button>
    </div>
  </main>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { createBanner, fetchBanners, updateBanner } from '../api'

const list = ref([])
const form = reactive({ id: null, title: '', imageUrl: '', path: '', sortOrder: '0', status: 'active' })

async function load() {
  list.value = await fetchBanners()
}

async function submit() {
  const payload = { ...form, sortOrder: Number(form.sortOrder || 0) }
  if (form.id) {
    await updateBanner(form.id, payload)
  } else {
    await createBanner(payload)
  }
  form.id = null
  form.title = ''
  form.imageUrl = ''
  form.path = ''
  form.sortOrder = '0'
  form.status = 'active'
  await load()
}

function edit(item) {
  form.id = item.id
  form.title = item.title
  form.imageUrl = item.imageUrl
  form.path = item.path
  form.sortOrder = String(item.sortOrder || 0)
  form.status = item.status || 'active'
}

onMounted(load)
</script>

<style scoped>
.page { padding: 32px; }
.form { display: grid; gap: 12px; max-width: 700px; }
input, select, button { padding: 12px; border-radius: 10px; border: 1px solid #cbd5e1; }
button { background: #2563eb; color: #fff; border: none; }
.card { margin-top: 16px; padding: 18px; border-radius: 14px; background: #fff; }
.edit { margin-top: 8px; }
</style>
