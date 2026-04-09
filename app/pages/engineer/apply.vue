<template>
  <view class="page">
    <view class="hero">
      <view class="title">工程师入驻</view>
      <view class="desc">先提交基础材料和擅长方向，平台审核通过后进入工程师市场。</view>
    </view>
    <view class="panel">
      <view class="label">真实姓名</view>
      <input v-model="form.realName" placeholder="请输入真实姓名" />
      <view class="label">证件掩码</view>
      <input v-model="form.idCardMask" placeholder="例如 4403********1234" />
      <view class="label">技能标签</view>
      <input v-model="form.skills" placeholder="例如 Unity, C#, Linux" />
      <view class="label">服务标签</view>
      <input v-model="form.serviceTags" placeholder="例如 游戏安装, BUG 修复" />
      <view class="label">参考时薪</view>
      <input v-model="form.hourlyPrice" placeholder="例如 199.00" />
      <view class="label">简介</view>
      <textarea v-model="form.intro" placeholder="介绍你的项目经验和擅长方向"></textarea>
      <view class="label">案例展示</view>
      <textarea v-model="form.caseExamples" placeholder="填写代表案例、项目成果或处理过的问题"></textarea>
      <button class="action" :disabled="loading" @click="submit">{{ loading ? '处理中...' : '提交申请' }}</button>
      <button class="ghost" :disabled="loading" @click="loadCurrent">查看我的申请状态</button>
    </view>
    <view v-if="current.id" class="result">
      <view class="result-title">当前申请</view>
      <view>姓名：{{ current.realName }}</view>
      <view>状态：{{ current.verificationStatus }}</view>
      <view>技能：{{ current.skills }}</view>
      <view>服务：{{ current.serviceTags }}</view>
      <view>参考时薪：{{ current.hourlyPrice }}</view>
      <view>在线状态：{{ current.onlineStatus }}</view>
      <view>完成单数：{{ current.completedOrderCount }}</view>
      <view>备注：{{ current.verificationRemark || '暂无' }}</view>
    </view>
    <view v-if="error" class="error">{{ error }}</view>
    <view class="tips panel">
      <view class="result-title">申请指引</view>
      <view>1. 先到“我的-资料维护”完善真实姓名和联系方式。</view>
      <view>2. 准备技能标签、服务标签和案例简介。</view>
      <view>3. 提交后等待后台审核，审核通过后会出现在工程师市场。</view>
    </view>
  </view>
</template>

<script>
import { ensureLogin, request } from '../../utils/request'

export default {
  data() {
    return {
      current: {},
      error: '',
      loading: false,
      form: {
        realName: '',
        idCardMask: '',
        skills: '',
        serviceTags: '',
        intro: '',
        caseExamples: '',
        hourlyPrice: '0.00',
        certificates: ''
      }
    }
  },
  onLoad() {
    this.loadCurrent()
  },
  methods: {
    validateForm() {
      if (!this.form.realName.trim()) {
        this.error = '请输入真实姓名'
        return false
      }
      if (!this.form.skills.trim()) {
        this.error = '请输入技能标签'
        return false
      }
      if (!this.form.serviceTags.trim()) {
        this.error = '请输入服务标签'
        return false
      }
      if (Number(this.form.hourlyPrice || 0) < 0) {
        this.error = '参考时薪不能小于 0'
        return false
      }
      return true
    },
    async submit() {
      this.error = ''
      if (!ensureLogin({ message: '提交工程师申请前需要先登录账号' })) {
        return
      }
      if (!this.validateForm()) {
        return
      }
      this.loading = true
      uni.showLoading({ title: '提交中', mask: true })
      try {
        const profile = await request('/api/front/user/me')
        if (!profile.realName || !(profile.phone || profile.email)) {
          this.error = '请先到资料维护页完善真实姓名，以及手机号或邮箱'
          uni.hideLoading()
          this.loading = false
          uni.showModal({
            title: '资料未完善',
            content: this.error,
            success: (res) => {
              if (res.confirm) {
                uni.navigateTo({ url: '/pages/user/profile' })
              }
            }
          })
          return
        }
      } catch (error) {
        this.error = error.message
        uni.hideLoading()
        this.loading = false
        return
      }
      try {
        await request('/api/front/engineer/apply', 'POST', {
          ...this.form,
          hourlyPrice: Number(this.form.hourlyPrice || 0)
        })
        uni.hideLoading()
        this.loading = false
        uni.showToast({ title: '申请已提交', icon: 'none' })
        await this.loadCurrent()
      } catch (error) {
        uni.hideLoading()
        this.loading = false
        this.error = error.message
      }
    },
    async loadCurrent() {
      this.error = ''
      if (!ensureLogin({ message: '查看申请状态前需要先登录账号' })) {
        return
      }
      this.loading = true
      uni.showLoading({ title: '加载中', mask: true })
      try {
        this.current = (await request('/api/front/engineer/me')) || {}
      } catch (error) {
        this.error = error.message
      } finally {
        uni.hideLoading()
        this.loading = false
      }
    }
  }
}
</script>

<style>
.page { padding: 24rpx; background: linear-gradient(180deg, #ecfdf5 0%, #f8fafc 30%, #f4f7fb 100%); min-height: 100vh; }
.hero { padding: 34rpx 30rpx; border-radius: 30rpx; background: linear-gradient(135deg, #064e3b, #059669 65%, #34d399); color: #fff; }
.title { font-size: 42rpx; font-weight: 700; }
.desc { margin-top: 16rpx; font-size: 26rpx; line-height: 1.6; opacity: 0.92; }
.panel, .result { margin-top: 24rpx; padding: 28rpx; border-radius: 26rpx; background: #fff; box-shadow: 0 18rpx 46rpx rgba(15, 23, 42, 0.05); }
.label { margin-top: 16rpx; margin-bottom: 10rpx; color: #334155; font-size: 26rpx; }
input, textarea { width: 100%; min-height: 88rpx; padding: 20rpx; border-radius: 16rpx; background: #f8fafc; box-sizing: border-box; }
textarea { min-height: 200rpx; }
.action, .ghost { margin-top: 20rpx; border-radius: 999rpx; }
.action { background: linear-gradient(135deg, #059669, #10b981); color: #fff; }
.ghost { background: #e2e8f0; color: #0f172a; }
.result-title { font-size: 32rpx; font-weight: 700; margin-bottom: 12rpx; }
.error { margin-top: 24rpx; color: #b91c1c; }
</style>
