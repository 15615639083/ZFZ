<template>
  <view class="page">
    <view class="hero">
      <view class="title">{{ detail.title || '资源详情' }}</view>
      <view class="tag-row">
        <text class="tag category">{{ detail.categoryName || '未分类' }}</text>
        <text class="tag" :class="detail.free ? 'free' : 'paid'">{{ detail.free ? '免费' : `￥${detail.price || 0}` }}</text>
        <text v-if="detail.downloaded" class="tag owned">已下载</text>
      </view>
      <view class="desc">{{ detail.description || '暂无资源说明。' }}</view>
    </view>

    <view class="panel stats">
      <view class="stat-box">
        <view class="stat-value">{{ detail.downloadCount || 0 }}</view>
        <view class="stat-label">下载量</view>
      </view>
      <view class="stat-box">
        <view class="stat-value">{{ detail.status || 'published' }}</view>
        <view class="stat-label">状态</view>
      </view>
    </view>

    <view class="panel">
      <view class="panel-title">资源说明</view>
      <view class="content">{{ detail.description || '当前资源还没有补充更详细的说明。' }}</view>
      <view class="detail-content">{{ detail.detailContent || '后台尚未维护更完整的资源详情内容。' }}</view>
    </view>

    <view class="panel">
      <view class="panel-title">相关推荐</view>
      <view v-for="item in related" :key="item.id" class="related-card" @click="openRelated(item.id)">
        <view class="related-title">{{ item.title }}</view>
        <view class="related-meta">{{ item.categoryName }} / {{ item.free ? '免费' : `￥${item.price}` }}</view>
      </view>
      <view v-if="!related.length" class="empty-copy">暂无相关推荐</view>
    </view>

    <view class="panel action-panel">
      <button class="action" @click="download">立即下载</button>
      <button class="ghost" @click="goService">需要技术协助</button>
    </view>

    <view v-if="error" class="error">{{ error }}</view>
  </view>
</template>

<script>
import { request, requireLogin } from '../../utils/request'

export default {
  data() {
    return {
      id: '',
      detail: {},
      related: [],
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
        this.error = '缺少资源ID'
        return
      }
      try {
        const [detail, related] = await Promise.all([
          request(`/api/front/resources/${this.id}`),
          request(`/api/front/resources/${this.id}/related`)
        ])
        this.detail = detail
        this.related = related
      } catch (error) {
        this.error = error.message
      }
    },
    async download() {
      this.error = ''
      if (!requireLogin()) {
        return
      }
      try {
        await request(`/api/front/resources/${this.id}/download`, 'POST')
        uni.showToast({ title: '已记录下载', icon: 'none' })
        await this.load()
      } catch (error) {
        this.error = error.message
      }
    },
    goService() {
      uni.navigateTo({
        url: `/pages/service/index?serviceTitle=${encodeURIComponent(this.detail.title || '资源部署协助')}`
      })
    },
    openRelated(id) {
      uni.navigateTo({ url: `/pages/resource/detail?id=${id}` })
    }
  }
}
</script>

<style>
.page { padding: 24rpx; background: linear-gradient(180deg, #eef4ff 0%, #f8fafc 32%, #f4f7fb 100%); min-height: 100vh; }
.hero { padding: 34rpx 30rpx; border-radius: 30rpx; background: linear-gradient(135deg, #111827, #2563eb); color: #fff; }
.title { font-size: 42rpx; font-weight: 700; line-height: 1.3; }
.tag-row { display: flex; gap: 12rpx; margin-top: 18rpx; }
.tag { padding: 8rpx 16rpx; border-radius: 999rpx; font-size: 22rpx; }
.category { background: rgba(255,255,255,0.14); color: #fff; }
.free { background: #dcfce7; color: #15803d; }
.paid { background: #dbeafe; color: #1d4ed8; }
.owned { background: #ede9fe; color: #7c3aed; }
.desc { margin-top: 18rpx; font-size: 26rpx; line-height: 1.7; opacity: 0.92; }
.panel { margin-top: 22rpx; padding: 28rpx; border-radius: 26rpx; background: #fff; box-shadow: 0 18rpx 46rpx rgba(15, 23, 42, 0.05); }
.stats { display: flex; gap: 16rpx; }
.stat-box { flex: 1; padding: 20rpx; border-radius: 22rpx; background: #f8fafc; text-align: center; }
.stat-value { font-size: 34rpx; font-weight: 700; color: #0f172a; }
.stat-label { margin-top: 8rpx; font-size: 22rpx; color: #64748b; }
.panel-title { font-size: 30rpx; font-weight: 700; color: #0f172a; }
.content { margin-top: 16rpx; color: #475569; font-size: 28rpx; line-height: 1.8; }
.detail-content { margin-top: 18rpx; padding-top: 18rpx; border-top: 1rpx solid #e2e8f0; color: #334155; font-size: 26rpx; line-height: 1.9; white-space: pre-wrap; }
.related-card { margin-top: 16rpx; padding: 22rpx; border-radius: 20rpx; background: #f8fafc; }
.related-title { font-size: 28rpx; font-weight: 700; color: #0f172a; }
.related-meta { margin-top: 10rpx; color: #64748b; font-size: 24rpx; }
.empty-copy { margin-top: 18rpx; color: #94a3b8; font-size: 24rpx; }
.action-panel { display: flex; gap: 16rpx; }
.action, .ghost { flex: 1; border-radius: 999rpx; }
.action { background: linear-gradient(135deg, #2563eb, #1d4ed8); color: #fff; }
.ghost { background: #e2e8f0; color: #0f172a; }
.error { margin-top: 20rpx; color: #b91c1c; }
</style>
