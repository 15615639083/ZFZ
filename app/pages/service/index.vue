<template>
  <view class="page">
    <view class="hero">
      <view class="title">服务下单</view>
      <view class="desc">提交环境信息、目标结果和预算，由平台统一派单并跟踪进度。</view>
      <view class="hero-engineer" v-if="engineerName">当前意向工程师：{{ engineerName }}</view>
    </view>
    <view class="panel">
      <view class="label">目标工程师</view>
      <input v-model="form.engineerId" placeholder="工程师档案ID" />
      <input v-model="engineerName" placeholder="工程师名称（展示用）" />
      <view class="label">服务标题</view>
      <input v-model="form.serviceTitle" placeholder="例如：Windows 服务端部署" />
      <view class="label">需求描述</view>
      <textarea v-model="form.requirementDesc" placeholder="请描述你的环境、问题和期望结果"></textarea>
      <view class="label">预算</view>
      <input v-model="form.price" placeholder="例如：199.00" />
      <button class="action" @click="submit">提交订单</button>
      <button class="ghost" @click="loginDemo">演示账号登录后下单</button>
    </view>
    <view v-if="latestOrder.orderNo" class="result">
      <view class="result-title">下单成功</view>
      <view>订单号：{{ latestOrder.orderNo }}</view>
      <view>状态：{{ latestOrder.status }}</view>
      <view>支付状态：{{ latestOrder.payStatus }}</view>
    </view>
    <view v-if="error" class="error">{{ error }}</view>
  </view>
</template>

<script>
import { loginDemoUser, request, requireLogin } from '../../utils/request'

export default {
  data() {
    return {
      engineerName: '',
      latestOrder: {},
      error: '',
      form: {
        engineerId: '',
        serviceTitle: '',
        requirementDesc: '',
        price: '199.00'
      }
    }
  },
  onLoad(query) {
    if (query.engineerId) {
      this.form.engineerId = query.engineerId
    }
    if (query.engineerName) {
      this.engineerName = decodeURIComponent(query.engineerName)
    }
    if (query.serviceTitle) {
      this.form.serviceTitle = decodeURIComponent(query.serviceTitle)
    }
  },
  methods: {
    async loginDemo() {
      await loginDemoUser()
      uni.showToast({ title: '已登录演示账号', icon: 'none' })
    },
    async submit() {
      this.error = ''
      if (!requireLogin()) {
        return
      }
      try {
        this.latestOrder = await request('/api/front/service-orders', 'POST', {
          engineerId: Number(this.form.engineerId),
          serviceTitle: this.form.serviceTitle,
          requirementDesc: this.form.requirementDesc,
          price: Number(this.form.price)
        })
      } catch (error) {
        this.error = error.message
      }
    }
  }
}
</script>

<style>
.page { padding: 24rpx; background: linear-gradient(180deg, #eff6ff 0%, #f8fafc 30%, #f4f7fb 100%); min-height: 100vh; }
.hero { padding: 34rpx 30rpx; border-radius: 30rpx; background: linear-gradient(135deg, #172554, #1d4ed8 62%, #60a5fa); color: #fff; }
.title { font-size: 42rpx; font-weight: 700; }
.desc { margin-top: 16rpx; font-size: 26rpx; line-height: 1.6; opacity: 0.92; }
.hero-engineer { margin-top: 22rpx; display: inline-flex; padding: 12rpx 22rpx; border-radius: 999rpx; background: rgba(255,255,255,0.14); font-size: 24rpx; }
.panel, .result { margin-top: 24rpx; padding: 28rpx; border-radius: 26rpx; background: #fff; box-shadow: 0 18rpx 46rpx rgba(15, 23, 42, 0.05); }
.label { margin-top: 16rpx; margin-bottom: 10rpx; color: #334155; font-size: 26rpx; }
input, textarea { width: 100%; min-height: 88rpx; padding: 20rpx; border-radius: 16rpx; background: #f8fafc; box-sizing: border-box; }
textarea { min-height: 200rpx; }
.action, .ghost { margin-top: 20rpx; border-radius: 999rpx; }
.action { background: linear-gradient(135deg, #2563eb, #1d4ed8); color: #fff; }
.ghost { background: #e2e8f0; color: #0f172a; }
.result-title { font-size: 32rpx; font-weight: 700; margin-bottom: 12rpx; }
.error { margin-top: 24rpx; color: #b91c1c; }
</style>
