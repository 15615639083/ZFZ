<template>
  <view class="page">
    <view class="hero">
      <view class="hero-top">
        <view class="avatar">{{ profile.username ? profile.username.slice(0, 1).toUpperCase() : 'G' }}</view>
        <view class="hero-info">
          <view class="title">{{ profile.username || '未登录用户' }}</view>
          <view class="desc">{{ profile.role ? `当前角色：${profile.role}` : '先登录体验完整订单流程' }}</view>
        </view>
      </view>
      <view class="hero-actions">
        <button class="action" @click="goAuth">登录/注册</button>
        <button class="ghost light" @click="logout">退出</button>
      </view>
    </view>

    <view class="panel stats">
      <view class="stat-box">
        <view class="stat-value">{{ profile.id || 0 }}</view>
        <view class="stat-label">用户ID</view>
      </view>
      <view class="stat-box">
        <view class="stat-value">{{ orders.length }}</view>
        <view class="stat-label">订单数</view>
      </view>
      <view class="stat-box">
        <view class="stat-value">{{ paidCount }}</view>
        <view class="stat-label">已支付</view>
      </view>
    </view>

    <view class="panel">
      <view class="panel-head">
        <view class="panel-title">账户资料</view>
        <view class="panel-link" @click="goProfile">资料维护</view>
      </view>
      <view v-if="profile.id" class="profile">
        <view>用户名：{{ profile.username }}</view>
        <view>真实姓名：{{ profile.realName || '未完善' }}</view>
        <view>角色：{{ profile.role }}</view>
        <view>手机号：{{ profile.phone }}</view>
        <view>邮箱：{{ profile.email || '未填写' }}</view>
      </view>
      <view v-else class="empty-copy">请先注册或登录账号。</view>
    </view>

    <view class="panel">
      <view class="panel-head">
        <view class="panel-title">工程师入驻状态</view>
        <view class="panel-link" @click="goEngineerApply">去申请</view>
      </view>
      <view v-if="engineerStatus.id" class="engineer-card">
        <view class="engineer-row">
          <view class="engineer-name">{{ engineerStatus.realName }}</view>
          <view class="engineer-badge" :class="engineerStatus.verificationStatus">{{ engineerStatus.verificationStatus }}</view>
        </view>
        <view class="engineer-copy">技能：{{ engineerStatus.skills }}</view>
        <view class="engineer-copy">服务：{{ engineerStatus.serviceTags }}</view>
        <view class="engineer-copy">备注：{{ engineerStatus.verificationRemark || '暂无审核备注' }}</view>
        <button v-if="engineerStatus.verificationStatus === 'approved'" class="mini workbench" @click="goWorkbench">进入工作台</button>
      </view>
      <view v-else class="empty-copy">你还没有提交工程师入驻申请，先完善个人资料再申请更稳妥。</view>
    </view>

    <view class="panel">
      <view class="panel-head">
        <view class="panel-title">我的订单</view>
        <view class="panel-link" @click="loadOrders">刷新</view>
      </view>
      <view class="order-filters">
        <view class="filter-chip" :class="{ active: orderFilter === 'all' }" @click="setOrderFilter('all')">全部</view>
        <view class="filter-chip" :class="{ active: orderFilter === 'pending_dispatch' }" @click="setOrderFilter('pending_dispatch')">待派单</view>
        <view class="filter-chip" :class="{ active: orderFilter === 'processing' }" @click="setOrderFilter('processing')">处理中</view>
        <view class="filter-chip" :class="{ active: orderFilter === 'completed' }" @click="setOrderFilter('completed')">已完成</view>
      </view>
      <view v-for="item in orders" :key="item.id" class="order-card">
        <view class="order-title">{{ item.serviceTitle }}</view>
        <view>订单号：{{ item.orderNo }}</view>
        <view>状态：{{ item.status }} / 支付：{{ item.payStatus }}</view>
        <view>金额：{{ item.price }}</view>
        <view class="panel-link detail-link" @click="openOrder(item.id)">查看详情</view>
        <view class="order-actions">
          <button class="mini" @click="pay(item.id)">支付</button>
          <button class="mini secondary" @click="confirm(item.id)">确认完成</button>
        </view>
      </view>
      <view v-if="!orders.length" class="empty-copy">暂时没有订单，去服务市场下第一单吧。</view>
    </view>

    <view class="panel">
      <view class="panel-head">
        <view class="panel-title">已下载资源</view>
        <view class="panel-link" @click="loadDownloads">刷新</view>
      </view>
      <view v-for="item in downloads" :key="item.id" class="download-card" @click="openResource(item.id)">
        <view class="download-title">{{ item.title }}</view>
        <view class="download-meta">{{ item.categoryName }} / {{ item.free ? '免费' : `￥${item.price}` }}</view>
        <view class="download-copy">{{ item.description || '已记录下载，可继续查看详情。' }}</view>
      </view>
      <view v-if="!downloads.length" class="empty-copy">暂时没有下载记录，去资源中心看看。</view>
      <view v-if="error" class="error">{{ error }}</view>
    </view>
  </view>
</template>

<script>
import { clearLogin, request } from '../../utils/request'

export default {
  data() {
    return {
      profile: {},
      engineerStatus: {},
      orders: [],
      downloads: [],
      orderFilter: 'all',
      error: ''
    }
  },
  computed: {
    paidCount() {
      return this.orders.filter((item) => item.payStatus === 'paid').length
    }
  },
  onShow() {
    this.loadProfile()
    this.loadEngineerStatus()
    this.loadOrders()
    this.loadDownloads()
  },
  methods: {
    goAuth() {
      uni.navigateTo({ url: '/pages/auth/index' })
    },
    async loadProfile() {
      this.error = ''
      try {
        this.profile = await request('/api/front/user/me')
      } catch (error) {
        this.error = error.message
      }
    },
    async loadOrders() {
      this.error = ''
      try {
        this.orders = await request(`/api/front/service-orders?status=${encodeURIComponent(this.orderFilter)}`)
      } catch (error) {
        this.error = error.message
      }
    },
    setOrderFilter(status) {
      this.orderFilter = status
      this.loadOrders()
    },
    async loadEngineerStatus() {
      try {
        this.engineerStatus = (await request('/api/front/engineer/me')) || {}
      } catch (error) {
        this.engineerStatus = {}
      }
    },
    async loadDownloads() {
      try {
        this.downloads = await request('/api/front/resources/downloads/me')
      } catch (error) {
        this.downloads = []
      }
    },
    async pay(id) {
      try {
        await request(`/api/front/service-orders/${id}/pay`, 'POST')
        await this.loadOrders()
      } catch (error) {
        this.error = error.message
      }
    },
    async confirm(id) {
      try {
        await request(`/api/front/service-orders/${id}/confirm`, 'POST')
        await this.loadOrders()
      } catch (error) {
        this.error = error.message
      }
    },
    logout() {
      clearLogin()
      this.profile = {}
      this.engineerStatus = {}
      this.orders = []
      this.downloads = []
      uni.showToast({ title: '已退出', icon: 'none' })
    },
    goProfile() {
      uni.navigateTo({ url: '/pages/user/profile' })
    },
    goEngineerApply() {
      uni.navigateTo({ url: '/pages/engineer/apply' })
    },
    goWorkbench() {
      uni.navigateTo({ url: '/pages/engineer/workbench' })
    },
    openOrder(id) {
      uni.navigateTo({ url: `/pages/service/detail?id=${id}` })
    },
    openResource(id) {
      uni.navigateTo({ url: `/pages/resource/detail?id=${id}` })
    }
  }
}
</script>

<style>
.page { padding: 24rpx; background: linear-gradient(180deg, #eff6ff 0%, #f8fafc 34%, #f4f7fb 100%); min-height: 100vh; }
.hero { padding: 34rpx 30rpx; border-radius: 30rpx; background: linear-gradient(135deg, #0f172a, #1d4ed8 62%, #60a5fa); color: #fff; box-shadow: 0 28rpx 64rpx rgba(29, 78, 216, 0.18); }
.hero-top { display: flex; align-items: center; gap: 22rpx; }
.avatar { width: 94rpx; height: 94rpx; border-radius: 30rpx; background: rgba(255,255,255,0.16); text-align: center; line-height: 94rpx; font-size: 34rpx; font-weight: 700; }
.hero-info { flex: 1; }
.title { font-size: 38rpx; font-weight: 700; }
.desc { margin-top: 12rpx; font-size: 24rpx; opacity: 0.92; }
.hero-actions { display: flex; gap: 16rpx; margin-top: 24rpx; }
.panel { margin-top: 22rpx; padding: 28rpx; border-radius: 26rpx; background: #fff; box-shadow: 0 18rpx 46rpx rgba(15, 23, 42, 0.05); }
.stats { display: flex; gap: 16rpx; }
.stat-box { flex: 1; padding: 20rpx; border-radius: 22rpx; background: #f8fafc; text-align: center; }
.stat-value { font-size: 34rpx; font-weight: 700; color: #0f172a; }
.stat-label { margin-top: 8rpx; font-size: 22rpx; color: #64748b; }
.action, .ghost { flex: 1; margin-top: 0; border-radius: 999rpx; }
.action { background: #fff; color: #1d4ed8; }
.ghost { background: rgba(255,255,255,0.14); color: #fff; }
.ghost.light { background: rgba(255,255,255,0.14); color: #fff; }
.panel-head { display: flex; justify-content: space-between; align-items: center; gap: 16rpx; }
.panel-title { font-size: 30rpx; font-weight: 700; color: #0f172a; }
.panel-link { color: #2563eb; font-size: 24rpx; }
.order-filters { display: flex; flex-wrap: wrap; gap: 12rpx; margin-top: 18rpx; }
.filter-chip { padding: 10rpx 18rpx; border-radius: 999rpx; background: #e2e8f0; color: #334155; font-size: 22rpx; }
.filter-chip.active { background: #2563eb; color: #fff; }
.profile { margin-top: 20rpx; color: #334155; font-size: 28rpx; line-height: 1.8; }
.engineer-card { margin-top: 20rpx; padding: 24rpx; border-radius: 20rpx; background: #f8fafc; }
.engineer-row { display: flex; justify-content: space-between; gap: 16rpx; align-items: center; }
.engineer-name { font-size: 30rpx; font-weight: 700; color: #0f172a; }
.engineer-badge { padding: 8rpx 16rpx; border-radius: 999rpx; font-size: 22rpx; background: #e2e8f0; color: #334155; }
.engineer-badge.approved { background: #dcfce7; color: #15803d; }
.engineer-badge.pending { background: #fef3c7; color: #b45309; }
.engineer-badge.rejected { background: #fee2e2; color: #b91c1c; }
.engineer-copy { margin-top: 12rpx; color: #475569; font-size: 26rpx; line-height: 1.7; }
.order-card { margin-top: 20rpx; padding: 24rpx; border-radius: 20rpx; background: #f8fafc; color: #334155; }
.order-title { font-size: 30rpx; font-weight: 700; margin-bottom: 10rpx; }
.detail-link { margin-top: 12rpx; }
.order-actions { display: flex; gap: 16rpx; margin-top: 16rpx; }
.mini { flex: 1; border-radius: 999rpx; background: #2563eb; color: #fff; }
.secondary { background: #0f766e; }
.workbench { margin-top: 16rpx; }
.download-card { margin-top: 20rpx; padding: 24rpx; border-radius: 20rpx; background: #f8fafc; }
.download-title { font-size: 30rpx; font-weight: 700; color: #0f172a; }
.download-meta { margin-top: 10rpx; font-size: 24rpx; color: #2563eb; }
.download-copy { margin-top: 12rpx; color: #64748b; font-size: 24rpx; line-height: 1.7; }
.empty-copy { margin-top: 20rpx; color: #94a3b8; font-size: 24rpx; }
.error { margin-top: 16rpx; color: #b91c1c; }
</style>
