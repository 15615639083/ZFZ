<template>
  <main class="page">
    <h1>Banner 配置</h1>
    <form class="form" @submit.prevent="submit">
      <input v-model="form.badgeText" placeholder="角标文案，例如 专业工程师在线待命" />
      <input v-model="form.title" placeholder="标题" />
      <input v-model="form.subtitle" placeholder="副标题/描述" />
      <input v-model="form.imageUrl" placeholder="图片 URL" />
      <input v-model="form.path" placeholder="跳转路径，例如 /pages/resource/index" />
      <input v-model="form.buttonText" placeholder="按钮文案，例如 立即下单" />
      <input v-model="form.buttonPath" placeholder="按钮跳转路径，例如 /pages/service/index" />
      <input v-model="form.sortOrder" placeholder="排序值" />
      <select v-model="form.status">
        <option value="active">active</option>
        <option value="disabled">disabled</option>
      </select>
      <button>{{ form.id ? '保存修改' : '新增 Banner' }}</button>
    </form>
    <div v-for="item in list" :key="item.id" class="card">
      <strong>{{ item.title }}</strong>
      <p>{{ item.subtitle || '-' }}</p>
      <p>{{ item.path }}</p>
      <p>{{ item.imageUrl }}</p>
      <p>排序：{{ item.sortOrder }} / 状态：{{ item.status }}</p>
      <div class="ops">
        <button class="edit" @click="edit(item)">编辑</button>
        <button class="edit ghost" @click="toggle(item)">{{ item.status === 'active' ? '停用' : '启用' }}</button>
        <button class="edit ghost" @click="move(item, -1)">上移</button>
        <button class="edit ghost" @click="move(item, 1)">下移</button>
      </div>
    </div>
  </main>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { createBanner, fetchBanners, quickUpdateBanner, updateBanner } from '../api'

const list = ref([])
const form = reactive({ id: null, badgeText: '', title: '', subtitle: '', imageUrl: '', path: '', buttonText: '', buttonPath: '', sortOrder: '0', status: 'active' })

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
  form.badgeText = ''
  form.title = ''
  form.subtitle = ''
  form.imageUrl = ''
  form.path = ''
  form.buttonText = ''
  form.buttonPath = ''
  form.sortOrder = '0'
  form.status = 'active'
  await load()
}

function edit(item) {
  form.id = item.id
  form.badgeText = item.badgeText || ''
  form.title = item.title
  form.subtitle = item.subtitle || ''
  form.imageUrl = item.imageUrl
  form.path = item.path
  form.buttonText = item.buttonText || ''
  form.buttonPath = item.buttonPath || ''
  form.sortOrder = String(item.sortOrder || 0)
  form.status = item.status || 'active'
}

async function toggle(item) {
  await quickUpdateBanner(item.id, {
    ...item,
    status: item.status === 'active' ? 'disabled' : 'active'
  })
  await load()
}

async function move(item, delta) {
  await quickUpdateBanner(item.id, {
    ...item,
    sortOrder: Number(item.sortOrder || 0) + delta
  })
  await load()
}

onMounted(load)
</script>

<style scoped>
.page { padding: 32px; }
.form { display: grid; gap: 12px; max-width: 700px; }
input, select, button { padding: 12px; border-radius: 10px; border: 1px solid #cbd5e1; }
button { background: #2563eb; color: #fff; border: none; }
.card { margin-top: 16px; padding: 18px; border-radius: 14px; background: #fff; }
.ops { display: flex; gap: 8px; flex-wrap: wrap; margin-top: 8px; }
.edit { margin-top: 0; }
.ghost { background: #475569; }
</style>
