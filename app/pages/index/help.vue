<template>
  <view class="page">
    <view class="hero">
      <view class="title">帮助中心</view>
      <view class="desc">这里整理平台常见问题和操作说明。</view>
    </view>
    <view class="panel" v-for="item in faqs" :key="item.q">
      <view class="panel-title">{{ item.q }}</view>
      <view class="copy">{{ item.a }}</view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      faqs: [
        { q: '如何下单？', a: '选择工程师或服务分类，填写需求并支付后，后台会安排派单。' },
        { q: '如何申请成为工程师？', a: '先在“我的-资料维护”完善资料，再进入工程师申请页面提交信息。' },
        { q: '资源下载后还能继续找人处理吗？', a: '可以，资源详情页和首页都可直接跳转到服务下单流程。' }
      ]
    }
  }
}
</script>

<style>
.page{min-height:100vh;padding:24rpx;background:#f7f9fd}.hero{padding:34rpx 30rpx;border-radius:30rpx;background:linear-gradient(135deg,#7a4dff,#b072ff);color:#fff}.title{font-size:42rpx;font-weight:700}.desc{margin-top:16rpx;font-size:26rpx;line-height:1.7}.panel{margin-top:22rpx;padding:28rpx;border-radius:26rpx;background:#fff}.panel-title{font-size:30rpx;font-weight:700;color:#0f172a}.copy{margin-top:16rpx;color:#475569;font-size:28rpx;line-height:1.8}
</style>
