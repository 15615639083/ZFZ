<template>
  <view class="page">
    <view class="title">服务市场</view>
    <view class="desc">提交需求后生成订单，后台可人工派单给工程师。</view>
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
import { request, setLogin } from '../../utils/request'

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
  },
  methods: {
    async loginDemo() {
      const data = await request('/api/public/auth/login', 'POST', {
        account: 'demo_user',
        password: '123456'
      })
      setLogin(data)
      uni.showToast({ title: '已登录演示账号', icon: 'none' })
    },
    async submit() {
      this.error = ''
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
.page { padding: 32rpx; background: #f8fafc; min-height: 100vh; }
.title { font-size: 38rpx; font-weight: 700; }
.desc { margin-top: 16rpx; color: #6b7280; font-size: 28rpx; }
.panel, .result { margin-top: 24rpx; padding: 28rpx; border-radius: 20rpx; background: #fff; }
.label { margin-top: 16rpx; margin-bottom: 10rpx; color: #334155; font-size: 26rpx; }
input, textarea { width: 100%; min-height: 88rpx; padding: 20rpx; border-radius: 16rpx; background: #f8fafc; box-sizing: border-box; }
textarea { min-height: 200rpx; }
.action, .ghost { margin-top: 20rpx; border-radius: 999rpx; }
.action { background: #2563eb; color: #fff; }
.ghost { background: #e2e8f0; color: #0f172a; }
.result-title { font-size: 32rpx; font-weight: 700; margin-bottom: 12rpx; }
.error { margin-top: 24rpx; color: #b91c1c; }
</style>
