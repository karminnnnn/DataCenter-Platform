import { defineStore } from 'pinia'
import { useAccountLoginApi, useMobileLoginApi, useLogoutApi } from '@/api/auth'
import { useUserInfoApi } from '@/api/sys/user'
import cache from '@/utils/cache'
import { useAuthorityListApi } from '@/api/sys/menu'

export const userStore = defineStore('userStore', {
	state: () => ({
		// 用户信息
		user: {
			id: '',
			username: '',
			avatar: ''
		},
		// 权限列表
		authorityList: [],
		// 登录token
		token: cache.getToken()
	}),
	actions: {
		setUser(val: any) {
			this.user = val
		},
		setToken(val: any) {
			this.token = val
			cache.setToken(val)
		},
		// 账号登录
		async accountLoginAction(loginForm: any) {
			const { data } = await useAccountLoginApi(loginForm)
			this.setToken(data.access_token)
		},
		// 手机号登录
		async mobileLoginAction(loginForm: any) {
			const { data } = await useMobileLoginApi(loginForm)
			this.setToken(data.access_token)
		},
		// 获取用户信息
		async getUserInfoAction() {
			const { data } = await useUserInfoApi()
			this.setUser(data)
		},
		// 获取权限
		async getAuthorityListAction() {
			const { data } = await useAuthorityListApi()
			this.authorityList = data || []
		},
		// 用户退出
		async logoutAction() {
			await useLogoutApi()

			// 移除 token
			this.setToken(null)
		}
	}
})

// import { defineStore } from 'pinia'：从 Pinia 库中导入 defineStore 函数，用于定义一个新的状态管理模块。
// import { useAccountLoginApi, useMobileLoginApi, useLogoutApi } from '@/api/auth'：导入账号登录、手机号登录和退出登录的 API 函数。
// import { useUserInfoApi } from '@/api/sys/user'：导入获取用户信息的 API 函数。
// import cache from '@/utils/cache'：导入缓存工具，用于保存和获取登录的 token。
// import { useAuthorityListApi } from '@/api/sys/menu'：导入获取用户权限列表的 API 函数。
// export const userStore = defineStore('userStore', { ... })：使用 defineStore 函数定义了一个名为 userStore 的状态管理模块。
// 	定义了模块的 state，其中包含了 user、authorityList 和 token 三个状态。
// 		user 用于存储用户信息，初始值为一个对象，包含 id、username 和 avatar 三个属性。
// 		authorityList 用于存储用户的权限列表，初始值为空数组。
// 		token 用于存储登录的 token，初始值从缓存中获取。
// 	actions: { ... }：定义了模块的 actions，用于定义各种操作。
// 		setUser(val: any)：设置用户信息。
// 		setToken(val: any)：设置登录的 token，并将新 token 保存到缓存中。
// 		accountLoginAction(loginForm: any)：账号登录操作。
// 			调用 useAccountLoginApi(loginForm) 函数进行账号登录。
// 			设置登录成功后返回的 access_token 为新的 token。
// 		mobileLoginAction(loginForm: any)：手机号登录操作。
// 			调用 useMobileLoginApi(loginForm) 函数进行手机号登录。
// 			设置登录成功后返回的 access_token 为新的 token。
// 		getUserInfoAction()：获取用户信息操作。
// 			调用 useUserInfoApi() 函数获取用户信息。
// 			设置获取到的用户信息为当前用户信息。
// 		getAuthorityListAction()：获取用户权限列表操作。
// 			调用 useAuthorityListApi() 函数获取用户权限列表。
// 			设置获取到的权限列表为当前用户的权限列表。
// 		logoutAction()：用户退出登录操作。
// 			调用 useLogoutApi() 函数进行退出登录。
// 			移除缓存中的 token。