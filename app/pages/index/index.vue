<template>
  <view class="page">
    <view class="hero">
      <view class="title">{{ heroTitle }}</view>
      <view class="desc">{{ heroDesc }}</view>
    </view>
    <view class="banner" v-for="item in banners" :key="item.title" @click="go(item.path)">
      <view class="banner-title">{{ item.title }}</view>
      <view class="banner-path">{{ item.path }}</view>
    </view>
    <view class="grid">
      <view class="card" @click="go('/pages/resource/index')">资源中心</view>
      <view class="card" @click="go('/pages/service/index')">服务市场</view>
      <view class="card" @click="go('/pages/engineer/index')">工程师</view>
      <view class="card" @click="go('/pages/user/index')">我的</view>
    </view>
    <view v-if="error" class="error">{{ error }}</view>
  </view>
</template>

<script>
import { request } from '../../utils/request'

export default {
  data() {
    return {
      heroTitle: 'GameTech Platform',
      heroDesc: '资源分发与技术服务平台基础骨架',
      banners: [],
      error: ''
    }
  },
  onLoad() {
    this.load()
  },
  methods: {
    async load() {
      try {
        const data = await request('/api/front/index')
        this.banners = data.banner || []
        this.heroDesc = '游戏资源、安全下载与工程师技术服务对接'
      } catch (error) {
        this.error = error.message
      }
    },
    go(url) {
      uni.navigateTo({ url })
    }
  }
}
</script>

<style>
.page {
  min-height: 100vh;
  padding: 32rpx;
  background: #f5f7fb;
}

.hero {
  padding: 48rpx 32rpx;
  border-radius: 24rpx;
  background: linear-gradient(135deg, #1d4ed8, #0f172a);
  color: #fff;
}

.title {
  font-size: 40rpx;
  font-weight: 700;
}

.desc {
  margin-top: 16rpx;
  font-size: 26rpx;
  opacity: 0.9;
}

.grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 24rpx;
  margin-top: 32rpx;
}

.banner {
  margin-top: 24rpx;
  padding: 28rpx;
  border-radius: 20rpx;
  background: #fff;
}

.banner-title {
  font-size: 32rpx;
  font-weight: 700;
}

.banner-path {
  margin-top: 8rpx;
  color: #64748b;
  font-size: 24rpx;
}

.card {
  padding: 40rpx 24rpx;
  border-radius: 20rpx;
  background: #fff;
  text-align: center;
  font-size: 30rpx;
  font-weight: 600;
  color: #111827;
}

.error {
  margin-top: 24rpx;
  color: #b91c1c;
}
</style>
