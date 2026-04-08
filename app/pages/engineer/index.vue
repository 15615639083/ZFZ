<template>
  <view class="page">
    <view class="title">工程师</view>
    <view class="desc">选择合适的工程师，查看擅长领域和服务标签。</view>
    <view v-for="item in list" :key="item.id" class="card">
      <view class="row">
        <view class="name">{{ item.realName }}</view>
        <view class="rating">评分 {{ item.rating }}</view>
      </view>
      <view class="meta">技能：{{ item.skills }}</view>
      <view class="meta">服务：{{ item.serviceTags }}</view>
      <view class="meta">参考时薪：{{ item.hourlyPrice }}</view>
      <view class="desc2">{{ item.intro || '该工程师暂未填写简介。' }}</view>
      <button class="action" @click="goOrder(item)">找他处理</button>
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
    goOrder(item) {
      uni.navigateTo({
        url: `/pages/service/index?engineerId=${item.id}&engineerName=${encodeURIComponent(item.realName)}`
      })
    }
  }
}
</script>

<style>
.page { padding: 32rpx; background: #f5f7fb; min-height: 100vh; }
.title { font-size: 38rpx; font-weight: 700; }
.desc { margin-top: 16rpx; color: #6b7280; font-size: 28rpx; }
.card { margin-top: 24rpx; padding: 28rpx; border-radius: 20rpx; background: #fff; }
.row { display: flex; justify-content: space-between; gap: 16rpx; }
.name { font-size: 32rpx; font-weight: 700; }
.rating, .meta, .desc2 { margin-top: 12rpx; color: #475569; font-size: 26rpx; }
.action { margin-top: 20rpx; background: #2563eb; color: #fff; border: none; border-radius: 999rpx; }
.error { margin-top: 24rpx; color: #b91c1c; }
</style>
