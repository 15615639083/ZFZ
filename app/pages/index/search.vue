<template>
  <view class="page">
    <view class="search-box">
      <input v-model="keyword" placeholder="搜索资源、工程师或服务关键词" confirm-type="search" @confirm="search" />
      <button @click="search">搜索</button>
    </view>

    <view class="section">
      <view class="section-title">资源结果</view>
      <view v-for="item in resources" :key="`r-${item.id}`" class="card" @click="openResource(item.id)">
        <view class="name">{{ item.title }}</view>
        <view class="meta">{{ item.categoryName }} / {{ item.free ? '免费' : `￥${item.price}` }}</view>
      </view>
      <view v-if="!resources.length" class="empty">暂无资源结果</view>
    </view>

    <view class="section">
      <view class="section-title">工程师结果</view>
      <view v-for="item in engineers" :key="`e-${item.id}`" class="card" @click="openEngineer(item.id)">
        <view class="name">{{ item.realName }}</view>
        <view class="meta">{{ item.serviceTags }}</view>
      </view>
      <view v-if="!engineers.length" class="empty">暂无工程师结果</view>
    </view>
  </view>
</template>

<script>
import { request } from '../../utils/request'

export default {
  data() {
    return { keyword: '', resources: [], engineers: [] }
  },
  onLoad(query) {
    this.keyword = query.keyword || ''
    if (this.keyword) this.search()
  },
  methods: {
    async search() {
      const data = await request(`/api/front/search?keyword=${encodeURIComponent(this.keyword)}`)
      this.resources = data.resources || []
      this.engineers = data.engineers || []
    },
    openResource(id) { uni.navigateTo({ url: `/pages/resource/detail?id=${id}` }) },
    openEngineer(id) { uni.navigateTo({ url: `/pages/engineer/detail?id=${id}` }) }
  }
}
</script>

<style>
.page{min-height:100vh;padding:24rpx;background:#f7f9fd}.search-box{display:flex;gap:12rpx}.search-box input{flex:1;min-height:88rpx;padding:0 20rpx;border-radius:999rpx;background:#fff}.search-box button{border-radius:999rpx;background:#2563eb;color:#fff}.section{margin-top:28rpx}.section-title{font-size:32rpx;font-weight:700;color:#0f172a}.card{margin-top:14rpx;padding:24rpx;border-radius:22rpx;background:#fff}.name{font-size:28rpx;font-weight:700;color:#0f172a}.meta{margin-top:10rpx;color:#64748b;font-size:24rpx}.empty{margin-top:14rpx;color:#94a3b8;font-size:24rpx}
</style>
