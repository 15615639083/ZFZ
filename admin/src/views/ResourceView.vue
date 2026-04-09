<template>
  <main class="page">
    <h1>资源管理</h1>
    <form class="form" @submit.prevent="submit">
      <input v-model="form.title" placeholder="资源标题" />
      <input v-model="form.categoryName" placeholder="分类" />
      <input v-model="form.coverUrl" placeholder="封面图 URL（用于首页 Banner / 详情封面）" />
      <input v-model="form.price" placeholder="价格，例如 19.90" />
      <textarea v-model="form.description" placeholder="描述"></textarea>
      <textarea v-model="form.detailContent" placeholder="详细内容介绍，可填写更完整的资源说明"></textarea>
      <button>{{ form.id ? '保存修改' : '新增资源' }}</button>
    </form>
    <p v-if="error" class="error">{{ error }}</p>
    <div v-for="item in list" :key="item.id" class="card">
      <strong>{{ item.title }}</strong>
      <p>{{ item.categoryName }} / {{ item.price }}</p>
      <p>下载量：{{ item.downloadCount }}</p>
      <p>{{ item.description }}</p>
      <p class="detail-copy">{{ item.detailContent || '暂无详细内容' }}</p>
      <button class="edit" @click="edit(item)">编辑</button>
    </div>
    <section class="logs">
      <div class="logs-head">
        <h2>下载日志</h2>
        <button @click="load">刷新日志</button>
      </div>
      <div v-for="log in logs" :key="log.id" class="log-item">
        <strong>{{ log.resourceTitle || `资源 #${log.resourceId}` }}</strong>
        <p>用户：{{ log.userId }} / IP：{{ log.downloadIp || 'unknown' }}</p>
        <p>{{ log.createdAt }}</p>
      </div>
    </section>
  </main>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { createResource, fetchResourceDownloadLogs, fetchResources, updateResource } from '../api'

const list = ref([])
const logs = ref([])
const error = ref('')
const form = reactive({
  id: null,
  title: '',
  categoryName: '',
  description: '',
  detailContent: '',
  coverUrl: '',
  price: '0.00',
  free: true,
  status: 'published'
})

async function load() {
  error.value = ''
  try {
    const [resources, downloadLogs] = await Promise.all([fetchResources(), fetchResourceDownloadLogs()])
    list.value = resources
    logs.value = downloadLogs
  } catch (err) {
    error.value = err.message
  }
}

async function submit() {
  const payload = {
    ...form,
    price: Number(form.price || 0),
    free: Number(form.price || 0) === 0
  }
  if (form.id) {
    await updateResource(form.id, payload)
  } else {
    await createResource(payload)
  }
  form.id = null
  form.title = ''
  form.categoryName = ''
  form.description = ''
  form.detailContent = ''
  form.coverUrl = ''
  form.price = '0.00'
  await load()
}

function edit(item) {
  form.id = item.id
  form.title = item.title
  form.categoryName = item.categoryName
  form.description = item.description || ''
  form.detailContent = item.detailContent || ''
  form.coverUrl = item.coverUrl || ''
  form.price = String(item.price || 0)
}

onMounted(load)
</script>

<style scoped>
.page { padding: 32px; }
.form { display: grid; gap: 12px; max-width: 600px; }
input, textarea, button { padding: 12px; border-radius: 10px; border: 1px solid #cbd5e1; }
button { background: #2563eb; color: #fff; border: none; }
textarea { min-height: 100px; }
.card { margin-top: 16px; padding: 18px; border-radius: 14px; background: #fff; }
.detail-copy { color: #64748b; }
.edit { margin-top: 8px; }
.logs { margin-top: 28px; }
.logs-head { display: flex; justify-content: space-between; align-items: center; gap: 16px; }
.log-item { margin-top: 12px; padding: 16px; border-radius: 12px; background: #fff; }
.error { color: #b91c1c; }
</style>
