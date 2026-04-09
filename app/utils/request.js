const BASE_URL = 'http://127.0.0.1:8090'
const TOKEN_KEY = 'gametech_app_token'
const USER_KEY = 'gametech_app_user'

export function getToken() {
  return uni.getStorageSync(TOKEN_KEY) || ''
}

export function setLogin(data) {
  uni.setStorageSync(TOKEN_KEY, data.token)
  uni.setStorageSync(USER_KEY, data)
}

export function getLoginUser() {
  return uni.getStorageSync(USER_KEY) || null
}

export function clearLogin() {
  uni.removeStorageSync(TOKEN_KEY)
  uni.removeStorageSync(USER_KEY)
}

export function requireLogin() {
  if (getToken()) {
    return true
  }
  uni.showToast({ title: '请先登录', icon: 'none' })
  return false
}

export function ensureLogin(options = {}) {
  if (getToken()) {
    return true
  }
  uni.showModal({
    title: '提示',
    content: options.message || '当前操作需要先登录，是否前往登录/注册？',
    success: (res) => {
      if (res.confirm) {
        uni.navigateTo({ url: '/pages/auth/index' })
      }
    }
  })
  return false
}

export function loginDemoUser() {
  return request('/api/public/auth/login', 'POST', {
    account: 'demo_user',
    password: '123456'
  }).then((data) => {
    setLogin(data)
    return data
  })
}

export function request(url, method = 'GET', data) {
  return new Promise((resolve, reject) => {
    const headers = { 'Content-Type': 'application/json' }
    const token = getToken()
    if (token) {
      headers.Authorization = `Bearer ${token}`
    }
    uni.request({
      url: `${BASE_URL}${url}`,
      method,
      data,
      header: headers,
      timeout: 15000,
      success: (res) => {
        const body = res.data || {}
        if (res.statusCode !== 200 || body.code !== 200) {
          if (body.message === 'account is disabled') {
            clearLogin()
            uni.showModal({
              title: '账号已禁用',
              content: '当前账号已被禁用，请联系平台管理员处理。',
              showCancel: false,
              success: () => {
                uni.navigateTo({ url: '/pages/auth/index' })
              }
            })
          }
          reject(new Error(body.message || 'request failed'))
          return
        }
        resolve(body.data)
      },
      fail: (error) => reject(new Error(error.errMsg || 'network request failed'))
    })
  })
}
