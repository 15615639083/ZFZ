<template>
  <view class="page">
    <view class="hero">
      <view class="hero-top">
        <view>
          <view class="title">{{ detail.realName || '工程师详情' }}</view>
          <view class="desc">{{ detail.serviceTags || '暂未设置服务标签' }}</view>
        </view>
        <view class="score">{{ detail.rating || 0 }} 分</view>
      </view>
      <view class="hero-stats">
        <view class="hero-stat">
          <view class="hero-value">{{ detail.hourlyPrice || 0 }}</view>
          <view class="hero-label">参考时薪</view>
        </view>
        <view class="hero-stat">
          <view class="hero-value">{{ detail.completedOrderCount || 0 }}</view>
          <view class="hero-label">完成单数</view>
        </view>
      </view>
    </view>

    <view class="panel">
      <view class="panel-title">技能栈</view>
      <view class="content">{{ detail.skills || '暂无技能说明' }}</view>
    </view>

    <view class="panel">
      <view class="panel-title">服务介绍</view>
      <view class="content">{{ detail.intro || '该工程师暂未填写详细介绍。' }}</view>
    </view>

    <view class="panel">
      <view class="panel-title">案例展示</view>
      <view class="content">{{ detail.caseExamples || '暂未补充案例展示。' }}</view>
    </view>

    <view class="panel action-panel">
      <button class="action" @click="goOrder">立即发起服务</button>
      <button class="ghost" @click="goApply">我也想入驻</button>
    </view>

    <view v-if="error" class="error">{{ error }}</view>
  </view>
</template>

<script>
import { request } from '../../utils/request'

export default {
  data() {
    return {
      id: '',
      detail: {},
      error: ''
    }
  },
  onLoad(query) {
    this.id = query.id || ''
    this.load()
  },
  methods: {
    async load() {
      if (!this.id) {
        this.error = '缺少工程师ID'
        return
      }
      try {
        this.detail = await request(`/api/front/engineers/${this.id}`)
      } catch (error) {
        this.error = error.message
      }
    },
    goOrder() {
      uni.navigateTo({
        url: `/pages/service/index?engineerId=${this.detail.id}&engineerName=${encodeURIComponent(this.detail.realName || '')}`
      })
    },
    goApply() {
      uni.navigateTo({ url: '/pages/engineer/apply' })
    }
  }
}
</script>

<style>
.page { padding: 24rpx; background: linear-gradient(180deg, #ecfdf5 0%, #f8fafc 32%, #f4f7fb 100%); min-height: 100vh; }
.hero { padding: 34rpx 30rpx; border-radius: 30rpx; background: linear-gradient(135deg, #064e3b, #059669 65%, #34d399); color: #fff; box-shadow: 0 28rpx 64rpx rgba(5, 150, 105, 0.16); }
.hero-top { display: flex; justify-content: space-between; gap: 18rpx; align-items: flex-start; }
.title { font-size: 42rpx; font-weight: 700; }
.desc { margin-top: 14rpx; font-size: 26rpx; line-height: 1.6; opacity: 0.92; }
.score { padding: 14rpx 20rpx; border-radius: 999rpx; background: rgba(255,255,255,0.14); font-size: 24rpx; font-weight: 700; }
.hero-stats { display: flex; gap: 16rpx; margin-top: 26rpx; }
.hero-stat { flex: 1; padding: 20rpx; border-radius: 22rpx; background: rgba(255,255,255,0.12); }
.hero-value { font-size: 34rpx; font-weight: 700; }
.hero-label { margin-top: 8rpx; font-size: 22rpx; }
.panel { margin-top: 22rpx; padding: 28rpx; border-radius: 26rpx; background: #fff; box-shadow: 0 18rpx 46rpx rgba(15, 23, 42, 0.05); }
.panel-title { font-size: 30rpx; font-weight: 700; color: #0f172a; }
.content { margin-top: 16rpx; color: #475569; font-size: 28rpx; line-height: 1.8; }
.action-panel { display: flex; gap: 16rpx; }
.action, .ghost { flex: 1; border-radius: 999rpx; }
.action { background: linear-gradient(135deg, #059669, #10b981); color: #fff; }
.ghost { background: #e2e8f0; color: #0f172a; }
.error { margin-top: 20rpx; color: #b91c1c; }
</style>
