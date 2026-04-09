<template>
  <view class="page">
    <view class="hero">
      <view class="title">工程师工作台</view>
      <view class="desc">已审核通过的工程师可在这里查看接单概况。</view>
    </view>
    <view v-if="data.profile" class="panel stats">
      <view class="stat-box"><view class="stat-value">{{ data.totalAssignedOrders }}</view><view class="stat-label">总接单</view></view>
      <view class="stat-box"><view class="stat-value">{{ data.processingOrders }}</view><view class="stat-label">处理中</view></view>
      <view class="stat-box"><view class="stat-value">{{ data.completedOrders }}</view><view class="stat-label">已完成</view></view>
    </view>
    <view class="panel" v-if="data.profile">
      <view class="panel-title">工程师资料</view>
      <view class="content">{{ data.profile.realName }} / {{ data.profile.serviceTags }}</view>
      <view class="sub">技能：{{ data.profile.skills }}</view>
      <view class="sub">参考时薪：{{ data.profile.hourlyPrice }}</view>
    </view>
    <view class="panel" v-if="data.profile">
      <view class="panel-title">近期订单</view>
      <view class="filters">
        <view class="chip" :class="{ active: status === 'all' }" @click="setStatus('all')">全部</view>
        <view class="chip" :class="{ active: status === 'assigned' }" @click="setStatus('assigned')">已指派</view>
        <view class="chip" :class="{ active: status === 'processing' }" @click="setStatus('processing')">处理中</view>
        <view class="chip" :class="{ active: status === 'waiting_acceptance' }" @click="setStatus('waiting_acceptance')">待验收</view>
        <view class="chip" :class="{ active: status === 'completed' }" @click="setStatus('completed')">已完成</view>
      </view>
      <view v-for="item in orders" :key="item.id" class="order-card">
        <view class="order-title">{{ item.serviceTitle }}</view>
        <view class="sub">{{ item.orderNo }} / {{ item.status }}</view>
        <view class="sub work">工作内容：{{ item.requirementDesc }}</view>
        <view class="actions" v-if="item.status !== 'completed' && item.status !== 'cancelled'">
          <button class="mini" @click="setProcessing(item.id)">开始处理</button>
          <button class="mini secondary" @click="requestAcceptance(item.id)">请求验收</button>
          <button class="mini danger" @click="rejectOrder(item.id)">拒单</button>
        </view>
        <textarea v-model="deliveryDrafts[item.id]" placeholder="填写交付备注、处理结果或说明"></textarea>
        <input v-model="attachmentDrafts[item.id]" placeholder="交付附件 URL（可选）" />
        <button class="save" @click="saveDelivery(item.id)">保存交付备注</button>
      </view>
      <view v-if="!orders.length" class="sub">暂无接单记录</view>
    </view>
    <view v-if="error" class="error">{{ error }}</view>
  </view>
</template>

<script>
import { request, requireLogin } from '../../utils/request'

export default {
  data() {
    return { data: {}, orders: [], deliveryDrafts: {}, attachmentDrafts: {}, status: 'all', error: '' }
  },
  onLoad() {
    this.load()
  },
  methods: {
    async load() {
      if (!requireLogin()) return
      try {
        const [workbench, orders] = await Promise.all([
          request('/api/front/engineer/workbench'),
          request(`/api/front/engineer/orders?status=${encodeURIComponent(this.status)}`)
        ])
        this.data = workbench
        this.orders = orders
        this.orders.forEach((item) => {
          this.deliveryDrafts[item.id] = item.deliveryNote || ''
          this.attachmentDrafts[item.id] = item.deliveryAttachmentUrl || ''
        })
      } catch (error) {
        this.error = error.message
      }
    },
    setStatus(status) {
      this.status = status
      this.load()
    },
    async setProcessing(id) {
      await request(`/api/front/engineer/orders/${id}/status`, 'POST', {
        status: 'processing',
        deliveryNote: this.deliveryDrafts[id] || '',
        deliveryAttachmentUrl: this.attachmentDrafts[id] || ''
      })
      await this.load()
    },
    async requestAcceptance(id) {
      await request(`/api/front/engineer/orders/${id}/status`, 'POST', {
        status: 'waiting_acceptance',
        deliveryNote: this.deliveryDrafts[id] || '',
        deliveryAttachmentUrl: this.attachmentDrafts[id] || ''
      })
      await this.load()
    },
    async saveDelivery(id) {
      await request(`/api/front/engineer/orders/${id}/status`, 'POST', {
        status: 'processing',
        deliveryNote: this.deliveryDrafts[id] || '',
        deliveryAttachmentUrl: this.attachmentDrafts[id] || ''
      })
      uni.showToast({ title: '交付备注已保存', icon: 'none' })
      await this.load()
    },
    async rejectOrder(id) {
      const reason = this.deliveryDrafts[id] || '当前时间无法处理，申请退回订单池'
      await request(`/api/front/engineer/orders/${id}/reject`, 'POST', { reason })
      uni.showToast({ title: '已拒单', icon: 'none' })
      await this.load()
    }
  }
}
</script>

<style>
.page { padding: 24rpx; background: linear-gradient(180deg, #ecfdf5 0%, #f8fafc 34%, #f4f7fb 100%); min-height: 100vh; }
.hero { padding: 34rpx 30rpx; border-radius: 30rpx; background: linear-gradient(135deg, #064e3b, #059669 65%, #34d399); color: #fff; }
.title { font-size: 42rpx; font-weight: 700; }
.desc { margin-top: 16rpx; font-size: 24rpx; opacity: 0.92; }
.panel { margin-top: 22rpx; padding: 28rpx; border-radius: 26rpx; background: #fff; }
.stats { display: flex; gap: 16rpx; }
.filters { display: flex; flex-wrap: wrap; gap: 12rpx; margin-top: 18rpx; }
.chip { padding: 10rpx 18rpx; border-radius: 999rpx; background: #e2e8f0; color: #334155; font-size: 22rpx; }
.chip.active { background: #059669; color: #fff; }
.stat-box { flex: 1; padding: 20rpx; border-radius: 22rpx; background: #f8fafc; text-align: center; }
.stat-value { font-size: 30rpx; font-weight: 700; color: #0f172a; }
.stat-label { margin-top: 8rpx; font-size: 22rpx; color: #64748b; }
.panel-title { font-size: 30rpx; font-weight: 700; color: #0f172a; }
.content { margin-top: 16rpx; color: #334155; font-size: 28rpx; }
.sub { margin-top: 10rpx; color: #64748b; font-size: 24rpx; }
.work { line-height: 1.7; }
.order-card { margin-top: 16rpx; padding: 20rpx; border-radius: 18rpx; background: #f8fafc; }
.order-title { font-size: 28rpx; font-weight: 700; color: #0f172a; }
.actions { display: flex; gap: 12rpx; margin-top: 14rpx; }
textarea { width: 100%; min-height: 140rpx; margin-top: 14rpx; padding: 18rpx; border-radius: 16rpx; background: #fff; box-sizing: border-box; }
input { width: 100%; min-height: 88rpx; margin-top: 12rpx; padding: 16rpx 18rpx; border-radius: 16rpx; background: #fff; box-sizing: border-box; }
.mini, .save { border-radius: 999rpx; }
.mini { flex: 1; background: #2563eb; color: #fff; }
.secondary { background: #0f766e; color: #fff; }
.danger { background: #b91c1c; color: #fff; }
.save { margin-top: 12rpx; background: #111827; color: #fff; }
.error { margin-top: 20rpx; color: #b91c1c; }
</style>
