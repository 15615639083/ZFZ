<template>
  <view class="page">
    <view class="title">资源中心</view>
    <view class="desc">当前接入资源列表和下载计数。</view>
    <view v-for="item in list" :key="item.id" class="card">
      <view class="name">{{ item.title }}</view>
      <view class="meta">{{ item.categoryName }} / {{ item.free ? '免费' : item.price }}</view>
      <view class="meta">下载量 {{ item.downloadCount }}</view>
      <view class="desc2">{{ item.description }}</view>
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
        this.list = await request('/api/front/resources')
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
.card { margin-top: 24rpx; padding: 28rpx; border-radius: 20rpx; background: #fff; }
.name { font-size: 32rpx; font-weight: 700; }
.meta, .desc2 { margin-top: 12rpx; color: #475569; font-size: 26rpx; }
.error { margin-top: 24rpx; color: #b91c1c; }
</style>
