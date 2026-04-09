<template>
  <view class="page">
    <view class="hero">
      <view class="title">资源中心</view>
      <view class="desc">把安装包、脚本和部署材料集中到一处，统一记录下载轨迹。</view>
      <view class="hero-row">
        <view class="stat">
          <view class="stat-value">{{ list.length }}</view>
          <view class="stat-label">当前资源</view>
        </view>
        <view class="stat">
          <view class="stat-value">{{ totalDownloads }}</view>
          <view class="stat-label">总下载量</view>
        </view>
      </view>
    </view>

    <view class="filter-row">
      <view class="chip" :class="{ active: currentFilter === 'all' }" @click="setFilter('all')">全部</view>
      <view class="chip" :class="{ active: currentFilter === 'free' }" @click="setFilter('free')">免费</view>
      <view class="chip" :class="{ active: currentFilter === 'paid' }" @click="setFilter('paid')">付费</view>
    </view>

    <view v-for="item in list" :key="item.id" class="card">
      <view class="card-head">
        <view>
          <view class="name">{{ item.title }}</view>
          <view class="tag-row">
            <text class="tag category">{{ item.categoryName }}</text>
            <text class="tag" :class="item.free ? 'free' : 'paid'">{{ item.free ? '免费' : `￥${item.price}` }}</text>
            <text v-if="item.downloaded" class="tag owned">已下载</text>
          </view>
        </view>
        <view class="download-chip">{{ item.downloadCount }}</view>
      </view>
      <view class="desc2">{{ item.description }}</view>
      <view class="card-foot">
        <view class="meta">已累计下载 {{ item.downloadCount }} 次</view>
        <view class="button-row">
          <button class="ghost" @click="openDetail(item.id)">查看详情</button>
          <button class="action" @click="download(item.id)">下载资源</button>
        </view>
      </view>
    </view>
    <view v-if="error" class="error">{{ error }}</view>
  </view>
</template>

<script>
import { request, requireLogin } from '../../utils/request'

export default {
  data() {
    return {
      sourceList: [],
      list: [],
      currentFilter: 'all',
      error: ''
    }
  },
  computed: {
    totalDownloads() {
      return this.sourceList.reduce((sum, item) => sum + (item.downloadCount || 0), 0)
    }
  },
  onLoad() {
    this.load()
  },
  methods: {
    async load() {
      try {
        this.sourceList = await request('/api/front/resources')
        this.applyFilter()
      } catch (error) {
        this.error = error.message
      }
    },
    setFilter(type) {
      this.currentFilter = type
      this.applyFilter()
    },
    applyFilter() {
      if (this.currentFilter === 'free') {
        this.list = this.sourceList.filter((item) => item.free)
        return
      }
      if (this.currentFilter === 'paid') {
        this.list = this.sourceList.filter((item) => !item.free)
        return
      }
      this.list = this.sourceList
    },
    openDetail(id) {
      uni.navigateTo({ url: `/pages/resource/detail?id=${id}` })
    },
    async download(id) {
      this.error = ''
      if (!requireLogin()) {
        return
      }
      try {
        await request(`/api/front/resources/${id}/download`, 'POST')
        uni.showToast({ title: '已记录下载', icon: 'none' })
        await this.load()
      } catch (error) {
        this.error = error.message
      }
    }
  }
}
</script>

<style>
.page { padding: 24rpx; background: linear-gradient(180deg, #eef4ff 0%, #f8fafc 28%, #f4f7fb 100%); min-height: 100vh; }
.hero { padding: 34rpx 30rpx; border-radius: 30rpx; background: linear-gradient(135deg, #111827, #2563eb); color: #fff; }
.title { font-size: 42rpx; font-weight: 700; }
.desc { margin-top: 16rpx; font-size: 26rpx; opacity: 0.9; line-height: 1.6; }
.hero-row { display: flex; gap: 18rpx; margin-top: 26rpx; }
.stat { flex: 1; padding: 20rpx; border-radius: 22rpx; background: rgba(255,255,255,0.12); }
.stat-value { font-size: 34rpx; font-weight: 700; }
.stat-label { margin-top: 8rpx; font-size: 22rpx; opacity: 0.9; }
.filter-row { display: flex; gap: 16rpx; margin-top: 24rpx; }
.chip { padding: 14rpx 24rpx; border-radius: 999rpx; background: #e2e8f0; color: #334155; font-size: 24rpx; }
.chip.active { background: #2563eb; color: #fff; }
.card { margin-top: 20rpx; padding: 28rpx; border-radius: 26rpx; background: #fff; box-shadow: 0 18rpx 46rpx rgba(15, 23, 42, 0.05); }
.card-head { display: flex; justify-content: space-between; gap: 16rpx; }
.name { font-size: 32rpx; font-weight: 700; color: #0f172a; }
.tag-row { display: flex; gap: 12rpx; margin-top: 14rpx; }
.tag { padding: 8rpx 16rpx; border-radius: 999rpx; font-size: 22rpx; }
.category { background: #f1f5f9; color: #475569; }
.free { background: #dcfce7; color: #15803d; }
.paid { background: #dbeafe; color: #1d4ed8; }
.owned { background: #ede9fe; color: #7c3aed; }
.download-chip { min-width: 72rpx; height: 72rpx; border-radius: 24rpx; background: #eff6ff; text-align: center; line-height: 72rpx; color: #2563eb; font-size: 28rpx; font-weight: 700; }
.desc2 { margin-top: 18rpx; color: #475569; font-size: 26rpx; line-height: 1.7; }
.card-foot { display: flex; justify-content: space-between; align-items: center; gap: 20rpx; margin-top: 22rpx; }
.meta { color: #64748b; font-size: 24rpx; }
.button-row { display: flex; gap: 12rpx; }
.ghost { margin: 0; padding: 0 24rpx; background: #e2e8f0; color: #0f172a; border: none; border-radius: 999rpx; }
.action { margin: 0; padding: 0 28rpx; background: linear-gradient(135deg, #2563eb, #1d4ed8); color: #fff; border: none; border-radius: 999rpx; }
.error { margin-top: 24rpx; color: #b91c1c; }
</style>
