<template>
  <view class="page">
    <view class="hero">
      <view class="title">资料维护</view>
      <view class="desc">个人账号资料完善后，再去申请工程师入驻。</view>
    </view>

    <view class="panel">
      <view class="label">用户名</view>
      <input v-model="form.username" placeholder="请输入用户名" />
      <view class="label">真实姓名</view>
      <input v-model="form.realName" placeholder="请输入真实姓名" />
      <view class="label">手机号</view>
      <input v-model="form.phone" placeholder="请输入手机号" />
      <view class="label">邮箱</view>
      <input v-model="form.email" placeholder="请输入邮箱" />
      <view class="label">头像链接</view>
      <input v-model="form.avatar" placeholder="可填写头像 URL" />
      <button class="action" @click="submit">保存资料</button>
    </view>

    <view v-if="error" class="error">{{ error }}</view>
  </view>
</template>

<script>
import { request, requireLogin } from '../../utils/request'

export default {
  data() {
    return {
      form: {
        username: '',
        realName: '',
        phone: '',
        email: '',
        avatar: ''
      },
      error: ''
    }
  },
  onLoad() {
    this.load()
  },
  methods: {
    validateForm() {
      if (!this.form.username.trim()) {
        this.error = '请输入用户名'
        return false
      }
      if (this.form.username.trim().length < 3) {
        this.error = '用户名至少 3 位'
        return false
      }
      if (!this.form.realName.trim()) {
        this.error = '请输入真实姓名'
        return false
      }
      if (!this.form.phone.trim() && !this.form.email.trim()) {
        this.error = '手机号和邮箱至少填写一个'
        return false
      }
      if (this.form.phone.trim() && !/^1\d{10}$/.test(this.form.phone.trim())) {
        this.error = '手机号格式不正确'
        return false
      }
      if (this.form.email.trim() && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(this.form.email.trim())) {
        this.error = '邮箱格式不正确'
        return false
      }
      return true
    },
    async load() {
      if (!requireLogin()) {
        return
      }
      try {
        const data = await request('/api/front/user/me')
        this.form.username = data.username || ''
        this.form.realName = data.realName || ''
        this.form.phone = data.phone || ''
        this.form.email = data.email || ''
        this.form.avatar = data.avatar || ''
      } catch (error) {
        this.error = error.message
      }
    },
    async submit() {
      this.error = ''
      if (!this.validateForm()) {
        return
      }
      try {
        await request('/api/front/user/me', 'POST', this.form)
        uni.showToast({ title: '资料已保存', icon: 'none' })
        setTimeout(() => {
          uni.navigateBack({ delta: 1 })
        }, 300)
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
.panel { margin-top: 22rpx; padding: 28rpx; border-radius: 26rpx; background: #fff; box-shadow: 0 18rpx 46rpx rgba(15, 23, 42, 0.05); }
.label { margin-top: 16rpx; margin-bottom: 10rpx; color: #334155; font-size: 26rpx; }
input { width: 100%; min-height: 88rpx; padding: 20rpx; border-radius: 16rpx; background: #f8fafc; box-sizing: border-box; }
.action { margin-top: 20rpx; border-radius: 999rpx; background: linear-gradient(135deg, #2563eb, #1d4ed8); color: #fff; }
.error { margin-top: 20rpx; color: #b91c1c; }
</style>
