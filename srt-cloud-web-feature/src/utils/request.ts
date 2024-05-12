import axios from 'axios'
import qs from 'qs'
import { ElMessage } from 'element-plus'
import store from '@/store'
import cache from '@/utils/cache'

// axios实例
const service = axios.create({
	baseURL: import.meta.env.VITE_API_URL as any,
	timeout: 600000,
	headers: { 'Content-Type': 'application/json;charset=UTF-8' }
})

// 请求拦截器
service.interceptors.request.use(
	(config: any) => {
		const userStore = store.userStore
		if (userStore?.token) {
			config.headers.Authorization = userStore.token
		}

		config.headers['Accept-Language'] = cache.getLanguage()

		// 追加时间戳，防止GET请求缓存
		if (config.method?.toUpperCase() === 'GET') {
			config.params = { ...config.params, t: new Date().getTime() }
		}

		if (Object.values(config.headers).includes('application/x-www-form-urlencoded')) {
			config.data = qs.stringify(config.data)
		}

		return config
	},
	error => {
		return Promise.reject(error)
	}
)

// 响应拦截器
service.interceptors.response.use(
	response => {
		console.log(response)
		if (response.status !== 200) {
			return Promise.reject(new Error(response.statusText || 'Error'))
		}

		const res = response.data
		console.log("res:")
		console.log(res)
		// 响应成功
		if (res.code === 0) {
			console.log(res.code)
			return res
		}

		// 错误提示
		ElMessage.error(res.msg)

		// 没有权限，如：未登录、登录过期等，需要跳转到登录页
		if (res.code === 401) {
			store.userStore?.setToken('')
			location.reload()
		}

		return Promise.reject(new Error(res.msg || 'Error'))
	},
	error => {
		ElMessage.error(error.message)
		return Promise.reject(error)
	}
)

// 导出 axios 实例
export default service




// import axios from 'axios'：导入 Axios 库，用于发送 HTTP 请求。
// import qs from 'qs'：导入 qs 库，用于序列化和解析 URL 查询字符串。
// import { ElMessage } from 'element-plus'：导入 Element Plus 库中的 ElMessage 组件，用于显示消息提示。
// import store from '@/store'：导入 Vuex 的 store 实例，用于在请求拦截器中获取用户令牌。
// import cache from '@/utils/cache'：导入自定义的缓存工具，用于获取语言设置信息。
// const service = axios.create({ ... })：创建一个名为 service 的 Axios 实例，设置了 baseURL、超时时间和请求头等默认配置。
// 请求拦截器：通过 service.interceptors.request.use(...) 注册了一个请求拦截器，用于在发送请求前的操作，如设置请求头、处理请求参数等。
// 	检查是否存在用户令牌，如果存在，则将其添加到请求头中的 Authorization 字段中。
// 	设置请求头中的 Accept-Language 字段为缓存中保存的语言设置。
// 	对于 GET 请求，追加一个时间戳参数，防止请求被缓存。
// 	如果请求头中包含 'application/x-www-form-urlencoded'，则对请求数据进行序列化。
// 响应拦截器：通过 service.interceptors.response.use(...) 注册了一个响应拦截器，用于在接收到响应后的操作，如处理响应数据、显示消息提示等。
// 	检查响应的状态码是否为 200，如果不是，则抛出一个带有状态信息的错误。
// 	检查响应数据中的 code 字段，如果为 0，则表示响应成功，直接返回响应数据。
// 	如果 code 不为 0，则使用 ElMessage 组件显示错误消息，并根据不同的 code 做不同的处理，如刷新页面、跳转到登录页等。
// 	导出默认的 Axios 实例 service，其他模块可以直接导入并使用该实例发送请求。