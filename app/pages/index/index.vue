<template>
  <view class="page">
    <view class="hero-card">
      <view class="hero-copy">
        <view class="hero-badge">{{ hero.badgeText || '专业工程师在线待命' }}</view>
        <view class="hero-title">{{ hero.title || '纸飞资 · 云服务' }}</view>
        <view class="hero-desc">{{ hero.subtitle || '高效解决您的软件安装、部署、优化与技术疑难。' }}</view>
        <button class="hero-btn" @click="go(hero.buttonPath || hero.path || '/pages/service/index')">{{ hero.buttonText || '立即下单' }}</button>
      </view>
      <view class="hero-visual">
        <image class="hero-image" :src="hero.image || defaultHeroImage" mode="aspectFill"></image>
      </view>
    </view>

    <view class="quick-grid">
      <view class="quick-item" @click="go('/pages/index/about')">
        <image class="quick-icon" src="/static/home/icon-about.svg" mode="aspectFit"></image>
        <view class="quick-text">平台简介</view>
      </view>
      <view class="quick-item" @click="go('/pages/index/help')">
        <image class="quick-icon" src="/static/home/icon-help.svg" mode="aspectFit"></image>
        <view class="quick-text">帮助中心</view>
      </view>
      <view class="quick-item" @click="go('/pages/service/index')">
        <image class="quick-icon" src="/static/home/icon-service.svg" mode="aspectFit"></image>
        <view class="quick-text">在线服务</view>
      </view>
      <view class="quick-item" @click="go('/pages/index/contact')">
        <image class="quick-icon" src="/static/home/icon-contact.svg" mode="aspectFit"></image>
        <view class="quick-text">联系客服</view>
      </view>
    </view>

    <view class="notice-card">
      <view>
        <view class="notice-title">欢迎关注“纸飞资”官方服务号</view>
        <view class="notice-copy">获取最新软件版本及高效工具，第一时间接收平台活动通知。</view>
      </view>
      <button class="notice-btn" @click="showToast('后续接入公众号跳转')">立即关注</button>
    </view>

    <view class="section">
      <view class="section-title">热门服务</view>
      <view class="service-grid">
        <view class="service-card" @click="goService('软件安装')">
          <view class="service-icon blue">下</view>
          <view class="service-name">软件安装</view>
        </view>
        <view class="service-card" @click="goService('系统重装')">
          <view class="service-icon purple">重</view>
          <view class="service-name">系统重装</view>
        </view>
        <view class="service-card" @click="goService('电脑加速')">
          <view class="service-icon cyan">速</view>
          <view class="service-name">电脑加速</view>
        </view>
        <view class="service-card" @click="goService('数据恢复')">
          <view class="service-icon green">数</view>
          <view class="service-name">数据恢复</view>
        </view>
      </view>
    </view>

    <view class="section">
      <view class="section-head">
        <view class="section-title">在线服务</view>
        <view class="section-more" @click="go('/pages/engineer/index')">更多工程师</view>
      </view>
      <view class="engineer-card" v-for="item in featuredEngineers" :key="item.id">
        <view class="engineer-main">
          <view class="avatar-wrap">
            <view class="avatar">{{ item.realName ? item.realName.slice(0, 1) : '工' }}</view>
            <view class="avatar-badge">认证</view>
          </view>
          <view class="engineer-info">
            <view class="engineer-top">
              <view class="engineer-name">软件工程师：{{ item.realName }}</view>
              <view class="engineer-price">￥{{ item.hourlyPrice || 30 }}<text class="price-unit">起</text></view>
            </view>
            <view class="engineer-meta">{{ item.rating || 5 }} 分 | 已完成 {{ item.completedOrderCount || 0 }} 单</view>
            <view class="engineer-tags">{{ item.skills }}</view>
          </view>
        </view>
        <view class="engineer-footer">
          <view class="skill-chip" v-for="tag in splitTags(item.serviceTags)" :key="`${item.id}-${tag}`">{{ tag }}</view>
          <button class="order-btn" @click="goOrder(item)">立即下单</button>
        </view>
      </view>
      <view v-if="!featuredEngineers.length" class="empty-copy">暂无工程师展示</view>
    </view>

    <view class="section">
      <view class="section-head">
        <view class="section-title">轮播推荐</view>
        <view class="section-more" @click="go('/pages/resource/index')">更多资源</view>
      </view>
      <scroll-view class="banner-scroll" scroll-x>
        <view class="banner-item" v-for="item in banners" :key="item.title" @click="go(item.path)">
          <image class="banner-image" :src="item.image" mode="aspectFill"></image>
          <view class="banner-mask">
            <view class="banner-title">{{ item.title }}</view>
            <view class="banner-copy">{{ item.path }}</view>
          </view>
        </view>
      </scroll-view>
    </view>

    <view class="section">
      <view class="section-head">
        <view class="section-title">推荐资源</view>
        <view class="section-more" @click="go('/pages/resource/index')">全部资源</view>
      </view>
      <view class="resource-card" v-for="item in featuredResources" :key="item.id" @click="openResource(item.id)">
        <view class="resource-top">
          <view>
            <view class="resource-title">{{ item.title }}</view>
            <view class="resource-meta">{{ item.categoryName }} · {{ item.free ? '免费' : `￥${item.price}` }}</view>
          </view>
          <view class="download-badge">{{ item.downloadCount || 0 }}</view>
        </view>
        <view class="resource-desc">{{ item.description || '暂无资源说明' }}</view>
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
      banners: [],
      hero: {},
      featuredResources: [],
      featuredEngineers: [],
      defaultHeroImage: 'https://dummyimage.com/480x320/2448ff/ffffff&text=GameTech',
      error: ''
    }
  },
  onLoad() {
    this.load()
  },
  methods: {
    async load() {
      try {
        const [data, resources, engineers] = await Promise.all([
          request('/api/front/index'),
          request('/api/front/resources'),
          request('/api/front/engineers')
        ])
        this.banners = data.banner || []
        this.hero = data.heroBanner || {}
        this.featuredResources = (resources || []).slice(0, 3)
        this.featuredEngineers = (engineers || []).slice(0, 3)
      } catch (error) {
        this.error = error.message
      }
    },
    splitTags(value) {
      return (value || '').split(/[，,\s]+/).filter(Boolean).slice(0, 3)
    },
    goService(title) {
      uni.navigateTo({ url: `/pages/service/index?serviceTitle=${encodeURIComponent(title)}` })
    },
    goOrder(item) {
      uni.navigateTo({
        url: `/pages/service/index?engineerId=${item.id}&engineerName=${encodeURIComponent(item.realName || '')}`
      })
    },
    openResource(id) {
      uni.navigateTo({ url: `/pages/resource/detail?id=${id}` })
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
  padding: 24rpx 24rpx 40rpx;
  background: linear-gradient(180deg, #f6f8ff 0%, #ffffff 26%, #f6f9fd 100%);
}

.hero-card {
  display: flex;
  justify-content: space-between;
  gap: 24rpx;
  padding: 34rpx 30rpx;
  border-radius: 34rpx;
  background: linear-gradient(135deg, #2448ff 0%, #3657ff 48%, #1db6ff 100%);
  color: #fff;
  box-shadow: 0 28rpx 70rpx rgba(37, 99, 235, 0.24);
}

.hero-copy {
  flex: 1;
}

.hero-badge {
  display: inline-flex;
  padding: 10rpx 18rpx;
  border-radius: 999rpx;
  background: rgba(255, 255, 255, 0.16);
  font-size: 22rpx;
}

.hero-title {
  margin-top: 24rpx;
  font-size: 54rpx;
  font-weight: 700;
  line-height: 1.15;
}

.hero-desc {
  margin-top: 18rpx;
  font-size: 26rpx;
  line-height: 1.7;
  opacity: 0.92;
}

.hero-visual {
  width: 220rpx;
  display: flex;
  align-items: center;
}

.hero-image {
  width: 100%;
  height: 220rpx;
  border-radius: 28rpx;
}

.hero-btn {
  margin-top: 22rpx;
  min-width: 180rpx;
  border-radius: 999rpx;
  background: rgba(255,255,255,0.96);
  color: #2145ff;
  font-weight: 700;
}

.quick-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 14rpx;
  margin-top: 24rpx;
}

.quick-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 14rpx;
  padding: 18rpx 8rpx;
}

.quick-icon {
  width: 94rpx;
  height: 94rpx;
  border-radius: 50%;
}

.quick-text {
  font-size: 24rpx;
  color: #334155;
}

.notice-card {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 20rpx;
  margin-top: 18rpx;
  padding: 24rpx 26rpx;
  border-radius: 24rpx;
  background: #fff;
  box-shadow: 0 18rpx 40rpx rgba(15, 23, 42, 0.05);
}

.notice-title {
  font-size: 28rpx;
  font-weight: 700;
  color: #0f172a;
}

.notice-copy {
  margin-top: 10rpx;
  font-size: 24rpx;
  line-height: 1.6;
  color: #64748b;
}

.notice-btn {
  min-width: 164rpx;
  border-radius: 999rpx;
  background: linear-gradient(135deg, #2448ff, #1db6ff);
  color: #fff;
}

.section {
  margin-top: 30rpx;
}

.section-head {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 16rpx;
  margin-bottom: 18rpx;
}

.section-title {
  font-size: 38rpx;
  font-weight: 700;
  color: #0f172a;
}

.section-more {
  font-size: 24rpx;
  color: #64748b;
}

.service-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16rpx;
}

.service-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 16rpx;
  padding: 24rpx 12rpx;
  border-radius: 24rpx;
  background: #fff;
  box-shadow: 0 18rpx 40rpx rgba(15, 23, 42, 0.05);
}

.service-icon {
  width: 86rpx;
  height: 86rpx;
  border-radius: 50%;
  text-align: center;
  line-height: 86rpx;
  color: #fff;
  font-size: 28rpx;
  font-weight: 700;
}

.service-name {
  font-size: 24rpx;
  color: #334155;
}

.engineer-card,
.resource-card {
  margin-top: 18rpx;
  padding: 26rpx;
  border-radius: 26rpx;
  background: #fff;
  box-shadow: 0 18rpx 40rpx rgba(15, 23, 42, 0.05);
}

.engineer-main {
  display: flex;
  gap: 18rpx;
}

.avatar-wrap {
  position: relative;
}

.avatar {
  width: 92rpx;
  height: 92rpx;
  border-radius: 28rpx;
  background: linear-gradient(135deg, #2448ff, #7a5cff);
  text-align: center;
  line-height: 92rpx;
  color: #fff;
  font-size: 32rpx;
  font-weight: 700;
}

.avatar-badge {
  position: absolute;
  right: -6rpx;
  bottom: -8rpx;
  padding: 6rpx 10rpx;
  border-radius: 999rpx;
  background: linear-gradient(135deg, #22c55e, #16a34a);
  color: #fff;
  font-size: 18rpx;
}

.engineer-info {
  flex: 1;
  min-width: 0;
}

.engineer-top,
.resource-top {
  display: flex;
  justify-content: space-between;
  gap: 16rpx;
}

.engineer-name,
.resource-title {
  font-size: 30rpx;
  font-weight: 700;
  color: #0f172a;
}

.engineer-price {
  color: #4338ca;
  font-size: 40rpx;
  font-weight: 700;
}

.price-unit {
  font-size: 22rpx;
  font-weight: 500;
}

.engineer-meta,
.engineer-tags,
.resource-meta,
.resource-desc {
  margin-top: 10rpx;
  color: #64748b;
  font-size: 24rpx;
  line-height: 1.6;
}

.engineer-meta {
  color: #475569;
}

.engineer-footer {
  display: flex;
  align-items: center;
  gap: 12rpx;
  margin-top: 18rpx;
  flex-wrap: wrap;
}

.skill-chip {
  padding: 10rpx 16rpx;
  border-radius: 999rpx;
  background: #eef2ff;
  color: #4338ca;
  font-size: 22rpx;
}

.order-btn {
  margin-left: auto;
  min-width: 170rpx;
  border-radius: 999rpx;
  background: linear-gradient(135deg, #2448ff, #1db6ff);
  color: #fff;
}

.banner-scroll {
  white-space: nowrap;
}

.banner-item {
  position: relative;
  display: inline-block;
  width: 520rpx;
  height: 248rpx;
  margin-right: 18rpx;
  border-radius: 28rpx;
  overflow: hidden;
}

.banner-image {
  width: 100%;
  height: 100%;
}

.banner-mask {
  position: absolute;
  left: 0;
  right: 0;
  bottom: 0;
  padding: 22rpx;
  background: linear-gradient(180deg, rgba(15, 23, 42, 0) 0%, rgba(15, 23, 42, 0.72) 100%);
  color: #fff;
}

.banner-title {
  font-size: 28rpx;
  font-weight: 700;
}

.banner-copy {
  margin-top: 8rpx;
  font-size: 22rpx;
  opacity: 0.9;
}

.download-badge {
  min-width: 70rpx;
  height: 70rpx;
  border-radius: 24rpx;
  background: #eef2ff;
  text-align: center;
  line-height: 70rpx;
  color: #4338ca;
  font-size: 26rpx;
  font-weight: 700;
}

.empty-copy,
.error {
  margin-top: 20rpx;
  color: #94a3b8;
  font-size: 24rpx;
}

.error {
  color: #b91c1c;
}
</style>
