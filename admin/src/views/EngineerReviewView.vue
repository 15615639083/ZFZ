<template>
  <main class="page">
    <h1>工程师审核</h1>
    <button class="action" @click="load">刷新</button>
    <p v-if="error" class="error">{{ error }}</p>
    <div v-for="item in list" :key="item.id" class="card">
      <div class="head">
        <div>
          <strong>{{ item.realName }}</strong>
          <p class="sub">用户ID：{{ item.userId }}</p>
        </div>
        <span class="status">{{ item.verificationStatus }}</span>
      </div>
      <p>技能：{{ item.skills }}</p>
      <p>证件掩码：{{ item.idCardMask || '未填写' }}</p>
      <p>服务标签：{{ item.serviceTags }}</p>
      <p>参考时薪：{{ item.hourlyPrice || 0 }}</p>
      <p>证书材料：{{ item.certificates || '未上传' }}</p>
      <p>{{ item.intro || '暂无简介' }}</p>
      <textarea v-model="remarks[item.id]" placeholder="驳回原因或审核备注"></textarea>
      <div class="ops">
        <button @click="submit(item.id, 'approved')">通过</button>
        <button class="ghost" @click="submit(item.id, 'rejected')">驳回</button>
      </div>
    </div>
  </main>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { fetchPendingEngineers, reviewEngineer } from '../api'

const list = ref([])
const error = ref('')
const remarks = reactive({})

async function load() {
  error.value = ''
  try {
    list.value = await fetchPendingEngineers()
    list.value.forEach((item) => {
      remarks[item.id] = item.verificationRemark || ''
    })
  } catch (err) {
    error.value = err.message
  }
}

async function submit(id, status) {
  await reviewEngineer(id, {
    status,
    remark: remarks[id] || (status === 'approved' ? '审核通过' : '资料不完整')
  })
  await load()
}

onMounted(load)
</script>

<style scoped>
.page { padding: 32px; }
.action, button { padding: 10px 14px; border-radius: 10px; border: none; background: #2563eb; color: #fff; }
.card { margin-top: 16px; padding: 18px; border-radius: 14px; background: #fff; }
.head { display: flex; justify-content: space-between; gap: 16px; }
.sub { margin: 6px 0 0; color: #64748b; }
.status { padding: 6px 10px; border-radius: 999px; background: #fef3c7; color: #b45309; height: fit-content; }
.card textarea { width: 100%; min-height: 88px; margin: 10px 0; padding: 12px; border-radius: 10px; border: 1px solid #cbd5e1; box-sizing: border-box; }
.ops { display: flex; gap: 10px; }
.ghost { background: #475569; }
.error { color: #b91c1c; }
</style>
