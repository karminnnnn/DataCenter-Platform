import { defineStore } from 'pinia'
import { ITheme } from '@/store/theme/interface'
import cache from '@/utils/cache'
import { useDictTypeAllApi } from '@/api/sys/dict'
import { currentUserProjectsApi } from '@/api/data-integrate/project'
import { listAllUserApi } from '@/api/sys/user'
import { listAllOrgApi } from '@/api/sys/orgs'

export const appStore = defineStore('appStore', {
	state: () => ({
		// sidebar 是否展开
		sidebarOpened: cache.getSidebarOpened(),
		// 国际化
		language: cache.getLanguage(),
		// 组件大小
		componentSize: cache.getComponentSize(),
		// 字典列表
		dictList: [],
		//当前用户的项目列表
		projectList: [],
		//部门列表
		orgList: [],
		//用户列表
		sysUserList: [],
		// 主题
		theme: cache.getTheme()
	}),
	actions: {
		setSidebarOpened() {
			this.sidebarOpened = !this.sidebarOpened
			cache.setSidebarOpened(this.sidebarOpened)
		},
		setLanguage(locale: string) {
			this.language = locale
			cache.setLanguage(locale)
		},
		setComponentSize(size: string) {
			this.componentSize = size
			cache.setComponentSize(size)
		},
		async getDictListAction() {
			const { data } = await useDictTypeAllApi()
			this.dictList = data || []
		},
		async getProjectListAction() {
			const { data } = await currentUserProjectsApi()
			this.projectList = data || []
		},
		async getSysUserListAction() {
			const { data } = await listAllUserApi()
			this.sysUserList = data || []
		},
		async getSysOrgListAction() {
			const { data } = await listAllOrgApi()
			this.orgList = data || []
		},
		setTheme(theme: ITheme) {
			this.theme = theme
			cache.setTheme(theme)
		}
	}
})

// import { defineStore } from 'pinia'：从 Pinia 库中导入 defineStore 函数，用于定义一个新的状态管理模块。
// import { ITheme } from '@/store/theme/interface'：导入主题接口类型，用于定义主题相关的状态。
// import cache from '@/utils/cache'：导入自定义的缓存工具模块，用于获取和设置一些常用的应用配置信息。
// import { useDictTypeAllApi } from '@/api/sys/dict'、import { currentUserProjectsApi } from '@/api/data-integrate/project'、
// 	import { listAllUserApi } from '@/api/sys/user'、import { listAllOrgApi } from '@/api/sys/orgs'：
// 	导入一些接口函数，用于获取字典列表、当前用户的项目列表、用户列表和部门列表等数据。
// export const appStore = defineStore('appStore', { ... })：使用 defineStore 函数定义了一个名为 appStore 的状态管理模块。
// state: () => ({ ... })：定义了模块的 state，其中包含了应用的各种状态。
// 	sidebarOpened：侧边栏是否展开，初始值从缓存中获取。
// 	language：国际化语言，初始值从缓存中获取。
// 	componentSize：组件大小，初始值从缓存中获取。
// 	dictList、projectList、orgList、sysUserList：分别是字典列表、当前用户的项目列表、部门列表和用户列表，初始值为空数组。
// 	theme：主题，初始值从缓存中获取。
// actions: { ... }：定义了模块的 actions，用于定义各种操作。
// 	setSidebarOpened()：用于设置侧边栏展开状态，并将新状态保存到缓存中。
// 	setLanguage(locale: string)：用于设置国际化语言，并将新语言保存到缓存中。
// 	setComponentSize(size: string)：用于设置组件大小，并将新大小保存到缓存中。
// 	getDictListAction()、getProjectListAction()、getSysUserListAction()、getSysOrgListAction()：分别用于异步获取字典列表、当前用户的项目列表、用户列表和部门列表，并将获取到的数据保存到对应的状态中。
// 	setTheme(theme: ITheme)：用于设置主题，并将新主题保存到缓存中。
