<template>
  <view class="page">
    <view class="hero">
      <view class="title">GameTech Platform</view>
      <view class="desc">资源分发与技术服务平台基础骨架</view>
    </view>
    <view class="grid">
      <view class="card" @click="go('/pages/resource/index')">资源中心</view>
      <view class="card" @click="go('/pages/service/index')">服务市场</view>
      <view class="card" @click="go('/pages/engineer/index')">工程师</view>
      <view class="card" @click="go('/pages/user/index')">我的</view>
    </view>
  </view>
</template>

<script>
export default {
  methods: {
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

.card {
  padding: 40rpx 24rpx;
  border-radius: 20rpx;
  background: #fff;
  text-align: center;
  font-size: 30rpx;
  font-weight: 600;
  color: #111827;
}
</style>
