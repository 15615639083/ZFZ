const API_BASE = '/api'

const TOKEN_KEY = 'gametech_admin_token'
const USER_KEY = 'gametech_admin_user'

export function getToken() {
  return window.localStorage.getItem(TOKEN_KEY) || ''
}

export function setToken(token) {
  window.localStorage.setItem(TOKEN_KEY, token)
}

export function getCurrentUser() {
  const raw = window.localStorage.getItem(USER_KEY)
  return raw ? JSON.parse(raw) : null
}

export function setCurrentUser(user) {
  window.localStorage.setItem(USER_KEY, JSON.stringify(user))
}

export function clearToken() {
  window.localStorage.removeItem(TOKEN_KEY)
  window.localStorage.removeItem(USER_KEY)
}

export async function request(path, options = {}) {
  const headers = {
    'Content-Type': 'application/json',
    ...(options.headers || {})
  }
  const token = getToken()
  if (token) {
    headers.Authorization = `Bearer ${token}`
  }

  const response = await fetch(`${API_BASE}${path}`, {
    ...options,
    headers
  })
  const data = await response.json()
  if (!response.ok || data.code !== 200) {
    throw new Error(data.message || 'request failed')
  }
  return data.data
}

export function login(payload) {
  return request('/public/auth/login', {
    method: 'POST',
    body: JSON.stringify(payload)
  })
}

export function fetchApprovedEngineers() {
  return request('/admin/engineers').then((list) => list.filter((item) => item.verificationStatus === 'approved'))
}

export function fetchDashboard() {
  return request('/admin/dashboard/overview')
}

export function fetchBanners() {
  return request('/admin/banners')
}

export function createBanner(payload) {
  return request('/admin/banners', {
    method: 'POST',
    body: JSON.stringify(payload)
  })
}

export function updateBanner(id, payload) {
  return request(`/admin/banners/${id}`, {
    method: 'POST',
    body: JSON.stringify(payload)
  })
}

export function fetchUsers() {
  return request('/admin/users')
}

export function fetchUserDetail(id) {
  return request(`/admin/users/${id}`)
}

export function updateUserStatus(id, payload) {
  return request(`/admin/users/${id}/status`, {
    method: 'POST',
    body: JSON.stringify(payload)
  })
}

export function fetchPendingEngineers() {
  return request('/admin/engineers/pending')
}

export function reviewEngineer(id, payload) {
  return request(`/admin/engineers/${id}/review`, {
    method: 'POST',
    body: JSON.stringify(payload)
  })
}

export function fetchResources() {
  return request('/admin/resources')
}

export function fetchResourceDownloadLogs() {
  return request('/admin/resources/download-logs')
}

export function createResource(payload) {
  return request('/admin/resources', {
    method: 'POST',
    body: JSON.stringify(payload)
  })
}

export function updateResource(id, payload) {
  return request(`/admin/resources/${id}`, {
    method: 'POST',
    body: JSON.stringify(payload)
  })
}

export function fetchOrders(status = 'all') {
  return request(`/admin/service-orders?status=${encodeURIComponent(status)}`)
}

export function dispatchOrder(id, payload) {
  return request(`/admin/service-orders/${id}/dispatch`, {
    method: 'POST',
    body: JSON.stringify(payload)
  })
}

export function fetchOrderDetail(id) {
  return request(`/admin/service-orders/${id}`)
}

export function updateOrderStatus(id, payload) {
  return request(`/admin/service-orders/${id}/status`, {
    method: 'POST',
    body: JSON.stringify(payload)
  })
}

export function createDispute(id, payload) {
  return request(`/admin/service-orders/${id}/dispute`, {
    method: 'POST',
    body: JSON.stringify(payload)
  })
}
