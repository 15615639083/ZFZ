<template>
  <main class="page">
    <h1>管理员登录</h1>
    <p>初始化账号可用 `platform_admin / 123456`。</p>
    <form class="form" @submit.prevent="submit">
      <input v-model="form.account" placeholder="账号" />
      <input v-model="form.password" type="password" placeholder="密码" />
      <button>登录</button>
    </form>
    <p v-if="token" class="success">已登录，token 已保存到浏览器本地。</p>
    <p v-if="error" class="error">{{ error }}</p>
  </main>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { login, setCurrentUser, setToken } from '../api'

const router = useRouter()
const form = reactive({ account: 'platform_admin', password: '123456' })
const error = ref('')
const token = ref('')

async function submit() {
  error.value = ''
  try {
    const data = await login(form)
    if (data.role !== 'admin') {
      throw new Error('当前账号不是管理员')
    }
    setToken(data.token)
    setCurrentUser(data)
    token.value = data.token
    router.push('/')
  } catch (err) {
    error.value = err.message
  }
}
</script>

<style scoped>
.page { padding: 32px; }
.form { display: grid; gap: 12px; max-width: 360px; margin-top: 16px; }
input, button { padding: 12px; border-radius: 10px; border: 1px solid #cbd5e1; }
button { background: #2563eb; color: #fff; border: none; }
.success { color: #15803d; }
.error { color: #b91c1c; }
</style>
