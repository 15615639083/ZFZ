<template>
  <view class="page">
    <view class="title">我的</view>
    <view class="desc">当前先提供演示账号登录和个人信息读取。</view>
    <view class="panel">
      <button class="action" @click="loginDemo">登录演示账号</button>
      <button class="ghost" @click="loadProfile">读取我的资料</button>
      <button class="ghost" @click="logout">退出登录</button>
      <view v-if="profile.id" class="profile">
        <view>用户ID：{{ profile.id }}</view>
        <view>用户名：{{ profile.username }}</view>
        <view>角色：{{ profile.role }}</view>
        <view>手机号：{{ profile.phone }}</view>
      </view>
      <view v-if="error" class="error">{{ error }}</view>
    </view>
  </view>
</template>

<script>
import { clearLogin, request, setLogin } from '../../utils/request'

export default {
  data() {
    return {
      profile: {},
      error: ''
    }
  },
  methods: {
    async loginDemo() {
      try {
        const data = await request('/api/public/auth/login', 'POST', {
          account: 'demo_user',
          password: '123456'
        })
        setLogin(data)
        uni.showToast({ title: '登录成功', icon: 'none' })
      } catch (error) {
        this.error = error.message
      }
    },
    async loadProfile() {
      this.error = ''
      try {
        this.profile = await request('/api/front/user/me')
      } catch (error) {
        this.error = error.message
      }
    },
    logout() {
      clearLogin()
      this.profile = {}
      uni.showToast({ title: '已退出', icon: 'none' })
    }
  }
}
</script>

<style>
.page { padding: 32rpx; background: #f8fafc; min-height: 100vh; }
.title { font-size: 38rpx; font-weight: 700; }
.desc { margin-top: 16rpx; color: #6b7280; font-size: 28rpx; }
.panel { margin-top: 24rpx; padding: 28rpx; border-radius: 20rpx; background: #fff; }
.action, .ghost { margin-top: 16rpx; border-radius: 999rpx; }
.action { background: #2563eb; color: #fff; }
.ghost { background: #e2e8f0; color: #0f172a; }
.profile { margin-top: 24rpx; color: #334155; font-size: 28rpx; line-height: 1.8; }
.error { margin-top: 16rpx; color: #b91c1c; }
</style>
