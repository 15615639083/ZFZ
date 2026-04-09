<template>
  <view class="page">
    <view class="hero">
      <view class="title">{{ detail.serviceTitle || '订单详情' }}</view>
      <view class="desc">订单号：{{ detail.orderNo || '-' }}</view>
    </view>
    <view class="panel stats">
      <view class="stat-box">
        <view class="stat-value">{{ detail.status || '-' }}</view>
        <view class="stat-label">订单状态</view>
      </view>
      <view class="stat-box">
        <view class="stat-value">{{ detail.payStatus || '-' }}</view>
        <view class="stat-label">支付状态</view>
      </view>
    </view>
    <view class="panel">
      <view class="panel-title">需求说明</view>
      <view class="content">{{ detail.requirementDesc || '暂无需求描述。' }}</view>
      <view class="sub">金额：{{ detail.price || 0 }}</view>
      <view class="sub">派单备注：{{ detail.dispatchRemark || '暂无' }}</view>
      <view class="sub">交付备注：{{ detail.deliveryNote || '暂无' }}</view>
      <view class="sub">交付附件：{{ detail.deliveryAttachmentUrl || '暂无' }}</view>
      <view class="sub reject" v-if="detail.rejectReason">驳回/退回原因：{{ detail.rejectReason }}</view>
      <view class="sub dispute" v-if="detail.disputeReason">仲裁原因：{{ detail.disputeReason }}</view>
    </view>
    <view class="panel action-panel">
      <button class="action" @click="pay" v-if="detail.payStatus !== 'paid'">立即支付</button>
      <button class="ghost" @click="confirm" v-if="detail.status === 'waiting_acceptance'">确认完成</button>
    </view>
    <view class="panel" v-if="detail.status === 'waiting_acceptance'">
      <view class="panel-title">验收处理</view>
      <textarea v-model="rejectReason" placeholder="若未通过验收，请填写驳回原因"></textarea>
      <button class="danger" @click="rejectAcceptance">驳回验收</button>
    </view>
    <view v-if="error" class="error">{{ error }}</view>
  </view>
</template>

<script>
import { request, requireLogin } from '../../utils/request'

export default {
  data() {
    return { id: '', detail: {}, rejectReason: '', error: '' }
  },
  onLoad(query) {
    this.id = query.id || ''
    this.load()
  },
  methods: {
    async load() {
      if (!requireLogin()) return
      try {
        this.detail = await request(`/api/front/service-orders/${this.id}`)
      } catch (error) {
        this.error = error.message
      }
    },
    async pay() {
      await request(`/api/front/service-orders/${this.id}/pay`, 'POST')
      await this.load()
    },
    async confirm() {
      await request(`/api/front/service-orders/${this.id}/confirm`, 'POST')
      await this.load()
    },
    async rejectAcceptance() {
      await request(`/api/front/service-orders/${this.id}/reject`, 'POST', {
        reason: this.rejectReason || '交付结果不符合预期，请继续处理'
      })
      await this.load()
    }
  }
}
</script>

<style>
.page { padding: 24rpx; background: linear-gradient(180deg, #eff6ff 0%, #f8fafc 34%, #f4f7fb 100%); min-height: 100vh; }
.hero { padding: 34rpx 30rpx; border-radius: 30rpx; background: linear-gradient(135deg, #172554, #1d4ed8 62%, #60a5fa); color: #fff; }
.title { font-size: 42rpx; font-weight: 700; }
.desc { margin-top: 16rpx; font-size: 24rpx; opacity: 0.92; }
.panel { margin-top: 22rpx; padding: 28rpx; border-radius: 26rpx; background: #fff; box-shadow: 0 18rpx 46rpx rgba(15, 23, 42, 0.05); }
.stats { display: flex; gap: 16rpx; }
.stat-box { flex: 1; padding: 20rpx; border-radius: 22rpx; background: #f8fafc; text-align: center; }
.stat-value { font-size: 30rpx; font-weight: 700; color: #0f172a; }
.stat-label { margin-top: 8rpx; font-size: 22rpx; color: #64748b; }
.panel-title { font-size: 30rpx; font-weight: 700; color: #0f172a; }
.content { margin-top: 16rpx; color: #334155; font-size: 28rpx; line-height: 1.8; }
.sub { margin-top: 12rpx; color: #64748b; font-size: 24rpx; }
.reject { color: #b45309; }
.dispute { color: #b91c1c; }
.action-panel { display: flex; gap: 16rpx; }
.action, .ghost { flex: 1; border-radius: 999rpx; }
.action { background: linear-gradient(135deg, #2563eb, #1d4ed8); color: #fff; }
.ghost { background: #e2e8f0; color: #0f172a; }
textarea { width: 100%; min-height: 140rpx; margin-top: 16rpx; padding: 18rpx; border-radius: 16rpx; background: #f8fafc; box-sizing: border-box; }
.danger { margin-top: 14rpx; border-radius: 999rpx; background: #b91c1c; color: #fff; }
.error { margin-top: 20rpx; color: #b91c1c; }
</style>
