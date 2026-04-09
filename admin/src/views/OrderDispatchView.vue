<template>
  <main class="page">
    <h1>订单派单</h1>
    <div class="toolbar">
      <button class="action" @click="load">刷新</button>
      <select v-model="statusFilter" @change="load">
        <option value="all">全部</option>
        <option value="pending_dispatch">待派单</option>
        <option value="assigned">已指派</option>
        <option value="processing">处理中</option>
        <option value="completed">已完成</option>
      </select>
    </div>
    <p v-if="error" class="error">{{ error }}</p>
    <div class="summary">
      <span>订单总数：{{ list.length }}</span>
      <span>待派单：{{ pendingCount }}</span>
      <span>已完成：{{ completedCount }}</span>
    </div>
    <div v-for="item in list" :key="item.id" class="card">
      <strong>{{ item.serviceTitle }}</strong>
      <p>订单号：{{ item.orderNo }}</p>
      <p>状态：{{ item.status }} / 支付：{{ item.payStatus }}</p>
      <p>{{ item.requirementDesc }}</p>
      <button class="detail" @click="detail(item.id)">查看详情</button>
      <div class="ops">
        <select v-model="engineerIds[item.id]">
          <option value="">选择工程师</option>
          <option v-for="engineer in engineers" :key="engineer.id" :value="engineer.id">
            {{ engineer.realName }} / {{ engineer.serviceTags }}
          </option>
        </select>
        <button @click="dispatch(item.id)">派单</button>
      </div>
    </div>
    <div v-if="current" class="detail-card">
      <h2>订单详情</h2>
      <p>订单号：{{ current.orderNo }}</p>
      <p>标题：{{ current.serviceTitle }}</p>
      <p>状态：{{ current.status }}</p>
      <p>支付：{{ current.payStatus }}</p>
      <p>用户ID：{{ current.userId }}</p>
      <p>工程师档案ID：{{ current.engineerId || '-' }}</p>
      <p>金额：{{ current.price }}</p>
      <p>需求：{{ current.requirementDesc }}</p>
      <p>派单备注：{{ current.dispatchRemark || '-' }}</p>
      <p>交付备注：{{ current.deliveryNote || '-' }}</p>
      <div class="ops status-ops">
        <select v-model="statusDraft.status">
          <option value="assigned">已指派</option>
          <option value="processing">处理中</option>
          <option value="waiting_acceptance">待验收</option>
          <option value="completed">已完成</option>
          <option value="cancelled">已取消</option>
        </select>
        <input v-model="statusDraft.deliveryNote" placeholder="交付备注/流转备注" />
        <button @click="updateStatus">更新状态</button>
      </div>
      <div class="ops status-ops">
        <input v-model="disputeReason" placeholder="如需平台仲裁，请填写仲裁原因" />
        <button class="danger" @click="dispute">发起仲裁</button>
      </div>
      <p v-if="current.rejectReason">退回原因：{{ current.rejectReason }}</p>
      <p v-if="current.disputeReason">仲裁原因：{{ current.disputeReason }}</p>
    </div>
  </main>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { createDispute, dispatchOrder, fetchApprovedEngineers, fetchOrderDetail, fetchOrders, updateOrderStatus } from '../api'

const list = ref([])
const engineers = ref([])
const error = ref('')
const statusFilter = ref('all')
const engineerIds = reactive({})
const current = ref(null)
const statusDraft = reactive({ status: 'assigned', deliveryNote: '' })
const disputeReason = ref('')

const pendingCount = computed(() => list.value.filter((item) => item.status === 'pending_dispatch').length)
const completedCount = computed(() => list.value.filter((item) => item.status === 'completed').length)

async function load() {
  error.value = ''
  try {
    const [orders, approvedEngineers] = await Promise.all([fetchOrders(statusFilter.value), fetchApprovedEngineers()])
    list.value = orders
    engineers.value = approvedEngineers
    list.value.forEach((item) => {
      engineerIds[item.id] = item.engineerId || ''
    })
  } catch (err) {
    error.value = err.message
  }
}

async function dispatch(id) {
  if (!engineerIds[id]) {
    error.value = '请先选择工程师'
    return
  }
  await dispatchOrder(id, {
    engineerId: Number(engineerIds[id]),
    dispatchRemark: 'manual dispatch from admin panel'
  })
  await load()
}

async function detail(id) {
  current.value = await fetchOrderDetail(id)
  statusDraft.status = current.value.status || 'assigned'
  statusDraft.deliveryNote = current.value.deliveryNote || ''
  disputeReason.value = current.value.disputeReason || ''
}

async function updateStatus() {
  if (!current.value) return
  await updateOrderStatus(current.value.id, {
    status: statusDraft.status,
    deliveryNote: statusDraft.deliveryNote
  })
  await detail(current.value.id)
  await load()
}

async function dispute() {
  if (!current.value) return
  await createDispute(current.value.id, { reason: disputeReason.value || '订单存在争议，申请管理员介入' })
  await detail(current.value.id)
  await load()
}

onMounted(load)
</script>

<style scoped>
.page { padding: 32px; }
.toolbar { display: flex; gap: 12px; align-items: center; }
.action, button { padding: 10px 14px; border-radius: 10px; border: none; background: #2563eb; color: #fff; }
select { min-width: 280px; padding: 10px 12px; border-radius: 10px; border: 1px solid #cbd5e1; }
.summary { display: flex; gap: 16px; margin-top: 16px; color: #475569; }
.card { margin-top: 16px; padding: 18px; border-radius: 14px; background: #fff; }
.detail { margin-top: 10px; background: #475569; }
.detail-card { margin-top: 20px; padding: 18px; border-radius: 14px; background: #fff; }
.status-ops { margin-top: 16px; }
.status-ops input { min-width: 240px; padding: 10px 12px; border-radius: 10px; border: 1px solid #cbd5e1; }
.ops { display: flex; gap: 10px; align-items: center; }
.danger { background: #b91c1c; }
.error { color: #b91c1c; }
</style>
