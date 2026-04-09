<template>
  <view class="page">
    <view class="hero">
      <view class="title">工程师市场</view>
      <view class="desc">展示能力、统一接单、由平台审核身份和服务资料。</view>
      <view class="hero-foot">
        <view class="hero-stat">
          <view class="hero-value">{{ list.length }}</view>
          <view class="hero-label">在线展示</view>
        </view>
        <view class="hero-note">从“我的”进入工程师申请</view>
      </view>
    </view>
    <view v-for="item in list" :key="item.id" class="card">
      <view class="row">
        <view>
          <view class="name">{{ item.realName }}</view>
          <view class="status">{{ item.onlineStatus || 'offline' }}</view>
        </view>
        <view class="rating">{{ item.rating || 0 }} 分</view>
      </view>
      <view class="meta-block">
        <view class="label">技能栈</view>
        <view class="meta">{{ item.skills }}</view>
      </view>
      <view class="meta-block">
        <view class="label">服务标签</view>
        <view class="meta">{{ item.serviceTags }}</view>
      </view>
      <view class="stat-row">
        <view class="stat-item">
          <view class="stat-value">{{ item.hourlyPrice || 0 }}</view>
          <view class="stat-label">参考时薪</view>
        </view>
        <view class="stat-item">
          <view class="stat-value">{{ item.completedOrderCount || 0 }}</view>
          <view class="stat-label">已完成单数</view>
        </view>
      </view>
      <view class="desc2">{{ item.intro || '该工程师暂未填写简介。' }}</view>
      <view class="button-row">
        <button class="ghost" @click="openDetail(item.id)">查看详情</button>
        <button class="action" @click="goOrder(item)">找他处理</button>
      </view>
    </view>
    <view v-if="error" class="error">{{ error }}</view>
  </view>
</template>

<script>
import { request } from '../../utils/request'

export default {
  data() {
    return {
      list: [],
      error: ''
    }
  },
  onLoad() {
    this.load()
  },
  methods: {
    async load() {
      try {
        this.list = await request('/api/front/engineers')
      } catch (error) {
        this.error = error.message
      }
    },
    openDetail(id) {
      uni.navigateTo({ url: `/pages/engineer/detail?id=${id}` })
    },
    goOrder(item) {
      uni.navigateTo({
        url: `/pages/service/index?engineerId=${item.id}&engineerName=${encodeURIComponent(item.realName)}`
      })
    }
  }
}
</script>

<style>
.page { padding: 24rpx; background: linear-gradient(180deg, #ecfdf5 0%, #f8fafc 30%, #f4f7fb 100%); min-height: 100vh; }
.hero { padding: 34rpx 30rpx; border-radius: 30rpx; background: linear-gradient(135deg, #064e3b, #059669 60%, #34d399); color: #fff; box-shadow: 0 28rpx 64rpx rgba(5, 150, 105, 0.18); }
.title { font-size: 42rpx; font-weight: 700; }
.desc { margin-top: 16rpx; font-size: 26rpx; opacity: 0.92; line-height: 1.6; }
.hero-foot { display: flex; justify-content: space-between; align-items: center; gap: 18rpx; margin-top: 26rpx; }
.hero-stat { padding: 18rpx 22rpx; border-radius: 22rpx; background: rgba(255,255,255,0.14); }
.hero-value { font-size: 34rpx; font-weight: 700; }
.hero-label { margin-top: 8rpx; font-size: 22rpx; }
.hero-note { padding: 14rpx 18rpx; border-radius: 999rpx; background: rgba(255,255,255,0.14); font-size: 22rpx; }
.card { margin-top: 22rpx; padding: 28rpx; border-radius: 26rpx; background: #fff; box-shadow: 0 18rpx 46rpx rgba(15, 23, 42, 0.05); }
.row { display: flex; justify-content: space-between; gap: 16rpx; }
.name { font-size: 32rpx; font-weight: 700; color: #0f172a; }
.status { margin-top: 10rpx; display: inline-block; padding: 8rpx 14rpx; border-radius: 999rpx; background: #ecfdf5; color: #059669; font-size: 20rpx; }
.rating { padding: 12rpx 20rpx; height: fit-content; border-radius: 999rpx; background: #eff6ff; color: #1d4ed8; font-size: 24rpx; font-weight: 700; }
.meta-block { margin-top: 18rpx; }
.label { font-size: 22rpx; color: #94a3b8; }
.meta, .desc2 { margin-top: 8rpx; color: #475569; font-size: 26rpx; line-height: 1.7; }
.stat-row { display: flex; gap: 16rpx; margin-top: 18rpx; }
.stat-item { flex: 1; padding: 18rpx; border-radius: 20rpx; background: #f8fafc; }
.stat-value { font-size: 30rpx; font-weight: 700; color: #0f172a; }
.stat-label { margin-top: 8rpx; font-size: 22rpx; color: #64748b; }
.button-row { display: flex; gap: 12rpx; margin-top: 22rpx; }
.ghost { flex: 1; background: #e2e8f0; color: #0f172a; border: none; border-radius: 999rpx; }
.action { flex: 1; margin-top: 0; background: linear-gradient(135deg, #2563eb, #0ea5e9); color: #fff; border: none; border-radius: 999rpx; }
.error { margin-top: 24rpx; color: #b91c1c; }
</style>
