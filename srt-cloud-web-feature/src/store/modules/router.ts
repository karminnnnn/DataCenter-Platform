import { defineStore } from 'pinia'
import { useMenuNavApi } from '@/api/sys/menu'
import { generateRoutes, constantMenu } from '@/router'
import { RouteRecordRaw } from 'vue-router'

export const routerStore = defineStore('routerStore', {
	state: () => ({
		menuRoutes: [] as RouteRecordRaw[],
		routes: [] as RouteRecordRaw[]
	}),
	actions: {
		async getMenuRoutes() {
			const { data } = await useMenuNavApi()
			const routes = generateRoutes(data)

			this.menuRoutes.push(...routes)

			// 常量菜单（去除）
			/* const constantRoutes = generateRoutes(constantMenu)
			this.menuRoutes.push(...constantRoutes) */

			return this.menuRoutes
		},
		setRoutes(routers: RouteRecordRaw[]) {
			this.routes = routers
		}
	}
})

// import { defineStore } from 'pinia'：从 Pinia 库中导入 defineStore 函数，用于定义一个新的状态管理模块。
// import { useMenuNavApi } from '@/api/sys/menu'：导入从后端获取菜单数据的 API 函数。
// import { generateRoutes, constantMenu } from '@/router'：导入路由生成函数和常量菜单数据。
// import { RouteRecordRaw } from 'vue-router'：导入 Vue Router 中的路由记录类型。
// export const routerStore = defineStore('routerStore', { ... })：使用 defineStore 函数定义了一个名为 routerStore 的状态管理模块。
// 	定义了模块的 state，其中包含了 menuRoutes 和 routes 两个状态，初始值分别为空数组。
// 	menuRoutes 用于存储动态生成的菜单路由。
// 	routes 用于存储完整的路由配置，包括动态生成的菜单路由和静态路由。
// actions: { ... }：定义了模块的 actions，用于定义各种操作。
// 	getMenuRoutes()：异步获取菜单数据，并生成菜单路由。
// 		使用 useMenuNavApi() 函数获取菜单数据。
// 		调用 generateRoutes(data) 函数生成菜单路由，并将生成的路由添加到 menuRoutes 中。
// 		返回生成的菜单路由。
// 	setRoutes(routers: RouteRecordRaw[])：设置完整的路由配置。
// 		接收一个 RouteRecordRaw 类型的路由配置数组作为参数。
// 		将参数中的路由配置设置为 routes 状态的值。
// 这样就定义了一个包含菜单路由和完整路由配置的状态管理模块 routerStore，可以在应用中使用该模块来管理路由相关的状态和操作。
