<template>
  <view class="page">
    <view class="hero">
      <view class="title">联系客服</view>
      <view class="desc">需要人工协助、订单跟进或问题反馈时，可通过以下方式联系平台。</view>
    </view>
    <view class="panel">
      <view class="panel-title">联系方式</view>
      <view class="copy">服务热线：400-800-1234
工作时间：09:00 - 21:00
客服邮箱：support@gametech.local</view>
      <button class="action" @click="makePhoneCall">拨打客服电话</button>
      <button class="ghost" @click="copyLine('support@gametech.local')">复制客服邮箱</button>
    </view>
    <view class="panel">
      <view class="panel-title">温馨提示</view>
      <view class="copy">涉及订单争议、验收驳回、工程师拒单等场景，请优先保留订单号和沟通记录，方便平台快速处理。</view>
    </view>
  </view>
</template>

<script>
export default {
  methods: {
    makePhoneCall() {
      uni.makePhoneCall({ phoneNumber: '4008001234' })
    },
    copyLine(value) {
      uni.setClipboardData({ data: value })
    }
  }
}
</script>

<style>
.page{min-height:100vh;padding:24rpx;background:#f7f9fd}.hero{padding:34rpx 30rpx;border-radius:30rpx;background:linear-gradient(135deg,#16a34a,#22c55e);color:#fff}.title{font-size:42rpx;font-weight:700}.desc{margin-top:16rpx;font-size:26rpx;line-height:1.7}.panel{margin-top:22rpx;padding:28rpx;border-radius:26rpx;background:#fff}.panel-title{font-size:30rpx;font-weight:700;color:#0f172a}.copy{margin-top:16rpx;color:#475569;font-size:28rpx;line-height:1.9;white-space:pre-wrap}.action,.ghost{margin-top:18rpx;border-radius:999rpx}.action{background:#16a34a;color:#fff}.ghost{background:#e2e8f0;color:#0f172a}
</style>
