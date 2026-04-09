<template>
  <view class="page">
    <view class="hero">
      <view class="title">账号中心</view>
      <view class="desc">先注册用户，再维护个人资料，之后再申请成为工程师。</view>
    </view>

    <view class="tabs">
      <view class="tab" :class="{ active: mode === 'login' }" @click="mode = 'login'">登录</view>
      <view class="tab" :class="{ active: mode === 'register' }" @click="mode = 'register'">注册</view>
    </view>

    <view class="panel" v-if="mode === 'login'">
      <view class="label">账号</view>
      <input v-model="loginForm.account" placeholder="用户名/手机号/邮箱" />
      <view class="label">密码</view>
      <input v-model="loginForm.password" type="password" placeholder="请输入密码" />
      <button class="action" @click="submitLogin">登录</button>
      <button class="ghost" @click="fillDemo">填充演示账号</button>
    </view>

    <view class="panel" v-else>
      <view class="label">用户名</view>
      <input v-model="registerForm.username" placeholder="请输入用户名" />
      <view class="label">手机号</view>
      <input v-model="registerForm.phone" placeholder="请输入手机号" />
      <view class="label">邮箱</view>
      <input v-model="registerForm.email" placeholder="请输入邮箱" />
      <view class="label">密码</view>
      <input v-model="registerForm.password" type="password" placeholder="请输入密码" />
      <button class="action" @click="submitRegister">注册并登录</button>
    </view>

    <view v-if="error" class="error">{{ error }}</view>
  </view>
</template>

<script>
import { request, setLogin } from '../../utils/request'

export default {
  data() {
    return {
      mode: 'login',
      error: '',
      loginForm: {
        account: '',
        password: ''
      },
      registerForm: {
        username: '',
        phone: '',
        email: '',
        password: ''
      }
    }
  },
  methods: {
    validateLogin() {
      if (!this.loginForm.account.trim()) {
        this.error = '请输入账号'
        return false
      }
      if (!this.loginForm.password.trim()) {
        this.error = '请输入密码'
        return false
      }
      return true
    },
    validateRegister() {
      if (!this.registerForm.username.trim()) {
        this.error = '请输入用户名'
        return false
      }
      if (this.registerForm.username.trim().length < 3) {
        this.error = '用户名至少 3 位'
        return false
      }
      if (!this.registerForm.phone.trim() && !this.registerForm.email.trim()) {
        this.error = '手机号和邮箱至少填写一个'
        return false
      }
      if (this.registerForm.phone.trim() && !/^1\d{10}$/.test(this.registerForm.phone.trim())) {
        this.error = '手机号格式不正确'
        return false
      }
      if (this.registerForm.email.trim() && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(this.registerForm.email.trim())) {
        this.error = '邮箱格式不正确'
        return false
      }
      if (!this.registerForm.password.trim()) {
        this.error = '请输入密码'
        return false
      }
      if (this.registerForm.password.trim().length < 6) {
        this.error = '密码至少 6 位'
        return false
      }
      return true
    },
    finishAuth(message) {
      uni.showToast({ title: message, icon: 'none' })
      setTimeout(() => {
        uni.switchTab({ url: '/pages/user/index' })
      }, 300)
    },
    fillDemo() {
      this.loginForm.account = 'demo_user'
      this.loginForm.password = '123456'
    },
    async submitLogin() {
      this.error = ''
      if (!this.validateLogin()) {
        return
      }
      try {
        const data = await request('/api/public/auth/login', 'POST', this.loginForm)
        setLogin(data)
        this.finishAuth('登录成功')
      } catch (error) {
        this.error = error.message
      }
    },
    async submitRegister() {
      this.error = ''
      if (!this.validateRegister()) {
        return
      }
      try {
        const data = await request('/api/public/auth/register', 'POST', this.registerForm)
        setLogin(data)
        this.finishAuth('注册成功')
      } catch (error) {
        this.error = error.message
      }
    }
  }
}
</script>

<style>
.page { padding: 24rpx; background: linear-gradient(180deg, #eef4ff 0%, #f8fafc 36%, #f4f7fb 100%); min-height: 100vh; }
.hero { padding: 34rpx 30rpx; border-radius: 30rpx; background: linear-gradient(135deg, #0f172a, #1d4ed8 62%, #60a5fa); color: #fff; }
.title { font-size: 42rpx; font-weight: 700; }
.desc { margin-top: 16rpx; font-size: 26rpx; line-height: 1.6; opacity: 0.92; }
.tabs { display: flex; gap: 16rpx; margin-top: 24rpx; }
.tab { flex: 1; padding: 18rpx 0; border-radius: 999rpx; text-align: center; background: #e2e8f0; color: #334155; }
.tab.active { background: #2563eb; color: #fff; }
.panel { margin-top: 22rpx; padding: 28rpx; border-radius: 26rpx; background: #fff; box-shadow: 0 18rpx 46rpx rgba(15, 23, 42, 0.05); }
.label { margin-top: 16rpx; margin-bottom: 10rpx; color: #334155; font-size: 26rpx; }
input { width: 100%; min-height: 88rpx; padding: 20rpx; border-radius: 16rpx; background: #f8fafc; box-sizing: border-box; }
.action, .ghost { margin-top: 20rpx; border-radius: 999rpx; }
.action { background: linear-gradient(135deg, #2563eb, #1d4ed8); color: #fff; }
.ghost { background: #e2e8f0; color: #0f172a; }
.error { margin-top: 20rpx; color: #b91c1c; }
</style>
