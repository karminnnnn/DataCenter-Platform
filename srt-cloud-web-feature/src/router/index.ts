import { createRouter, createWebHistory, createWebHashHistory, RouteRecordRaw } from 'vue-router'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import store from '@/store'
import { i18n } from '@/i18n'
import { pathToCamel } from '@/utils/tool'

// 配置 NProgress，禁用加载时的旋转图标。
NProgress.configure({ showSpinner: false })

// 定义常量路由，这些路由在应用启动时就存在，不会根据用户权限动态改变。
const constantRoutes: RouteRecordRaw[] = [
	{
		path: '/redirect',
		component: () => import('../layout/index.vue'),
		children: [
			{
				path: '/redirect/:path(.*)',
				component: () => import('../layout/components/Router/Redirect.vue')
			}
		]
	},
	{
		path: '/login',
		component: () => import('../views/login/index.vue')
	},
	{
		path: '/404',
		component: () => import('../views/404.vue')
	}
]

// 定义异步路由，这些路由需要根据用户权限动态生成。
const asyncRoutes: RouteRecordRaw = {
	path: '/',
	component: () => import('../layout/index.vue'),
	redirect: '/home',
	children: [
		{
			path: '/home',
			name: 'Home',
			component: () => import('../views/home.vue'),
			meta: {
				title: i18n.global.t('router.home'),
				affix: true
			}
		},
		{
			path: '/profile/password',
			name: 'ProfilePassword',
			component: () => import('../views/profile/password.vue'),
			meta: {
				title: i18n.global.t('router.profilePassword'),
				cache: true
			}
		},
		{
			path: '/profile/info',
			name: 'UserInfo',
			component: () => import('../views/profile/info.vue'),
			meta: {
				title: i18n.global.t('router.userInfo'),
				cache: true
			}
		},
		{
			path: '/data-assets/resource/db-resource',
			name: 'DataAssetsDbResource',
			component: () => import('../views/data-assets/resource/db-resource.vue'),
			meta: {
				title: '数据库表',
				cache: true
			}
		},
		{
			path: '/data-assets/resource/api-resource',
			name: 'DataAssetsApiResource',
			component: () => import('../views/data-assets/resource/api-resource.vue'),
			meta: {
				title: 'API',
				cache: false
			}
		},
		{
			path: '/data-assets/resource/file-resource',
			name: 'DataAssetsFileResource',
			component: () => import('../views/data-assets/resource/file-resource.vue'),
			meta: {
				title: '文件',
				cache: false
			}
		}
	]
}

// 配置常量菜单
// 定义常量菜单，这些菜单项在侧边栏中始终存在，不受用户权限影响。
export const constantMenu = [
	{
		id: 1000,
		name: 'Demo',
		url: null,
		openStyle: 0,
		icon: 'icon-windows',
		children: [
			{
				id: 1001,
				name: 'Icon 图标',
				url: 'demo/icons/index',
				openStyle: 0,
				icon: 'icon-unorderedlist'
			},
			{
				id: 1002,
				name: '二维码生成',
				url: 'demo/qrcode/index',
				openStyle: 0,
				icon: 'icon-unorderedlist'
			},
			{
				id: 1003,
				name: '页面打印',
				url: 'demo/printJs/index',
				openStyle: 0,
				icon: 'icon-unorderedlist'
			},
			{
				id: 1004,
				name: '图片裁剪',
				url: 'demo/cropper/index',
				openStyle: 0,
				icon: 'icon-unorderedlist'
			},
			{
				id: 1005,
				name: '富文本编辑器',
				url: 'demo/wangeditor/index',
				openStyle: 0,
				icon: 'icon-unorderedlist'
			}
		]
	}
]

// 定义错误路由，用于处理未匹配到的路由路径。
export const errorRoute: RouteRecordRaw = {
	path: '/:pathMatch(.*)',
	redirect: '/404'
}

// 创建路由实例，使用哈希模式。
export const router = createRouter({
	history: createWebHashHistory(),
	routes: constantRoutes
})

// 白名单列表
// 定义白名单，包含不需要登录就能访问的路径。
const whiteList = ['/login']

// 路由加载前
// 路由加载前的全局前置守卫，用于处理用户登录状态、权限等。
router.beforeEach(async (to, from, next) => {
	NProgress.start()

	// token存在的情况
	if (store.userStore.token) {
		if (to.path === '/login') {
			next('/home')
		} else {
			// 用户信息不存在，则重新拉取用户等信息
			if (!store.userStore.user.id) {
				await store.userStore.getUserInfoAction()
				await store.userStore.getAuthorityListAction()
				await store.appStore.getDictListAction()
				await store.appStore.getProjectListAction()
				await store.appStore.getSysUserListAction()
				await store.appStore.getSysOrgListAction()
				const menuRoutes = await store.routerStore.getMenuRoutes()

				// 根据后端菜单路由，生成KeepAlive路由
				const keepAliveRoutes = getKeepAliveRoutes(menuRoutes, [])
				//console.log("keepAliveRoutes"+JSON.stringify(keepAliveRoutes))

				// 添加菜单路由
				asyncRoutes.children?.push(...keepAliveRoutes)
				router.addRoute(asyncRoutes)

				// 错误路由
				router.addRoute(errorRoute)

				// 保存路由数据
				store.routerStore.setRoutes(constantRoutes.concat(asyncRoutes))

				next({ ...to, replace: true })
			} else {
				next()
			}
		}
	} else {
		// 没有token的情况下，可以进入白名单
		if (whiteList.indexOf(to.path) > -1) {
			next()
		} else {
			next('/login')
		}
	}
})

// 路由加载后
// 路由加载后的全局后置钩子，用于关闭加载进度条等操作。
router.afterEach(() => {
	NProgress.done()
})

// 获取扁平化路由，将多级路由转换成一级路由
// 定义获取扁平化路由的函数，将多级嵌套的路由转换成一级路由。
export const getKeepAliveRoutes = (rs: RouteRecordRaw[], breadcrumb: string[]): RouteRecordRaw[] => {
	const routerList: RouteRecordRaw[] = []

	rs.forEach((item: any) => {
		if (item.meta.title) {
			breadcrumb.push(item.meta.title)
		}

		if (item.children && item.children.length > 0) {
			routerList.push(...getKeepAliveRoutes(item.children, breadcrumb))
		} else {
			item.meta.breadcrumb.push(...breadcrumb)
			routerList.push(item)
		}

		breadcrumb.pop()
	})
	return routerList
}

// 加载vue组件
const layoutModules = import.meta.glob('/src/views/**/*.vue')

// 根据路径，动态获取vue组件
// 定义根据路径动态获取 Vue 组件的函数。
const getDynamicComponent = (path: string): any => {
	const component = layoutModules[`/src/views/${path}.vue`]
	if (!component) {
		console.error('组件不存在，路径为：', path)
	}
	return component
}

// 根据菜单列表，生成路由数据
// 定义根据菜单列表生成路由数据的函数，用于根据后端返回的菜单数据生成路由配置
export const generateRoutes = (menuList: any): RouteRecordRaw[] => {
	const routerList: RouteRecordRaw[] = []

	menuList.forEach((menu: any) => {
		let component
		let path
		if (menu.children && menu.children.length > 0) {
			component = () => import('@/layout/index.vue')
			path = '/p/' + menu.id
		} else {
			component = getDynamicComponent(menu.url)
			path = '/' + menu.url
		}
		const route: RouteRecordRaw = {
			path: path,
			name: pathToCamel(path),
			component: component,
			children: [],
			meta: {
				title: menu.name,
				icon: menu.icon,
				id: '' + menu.id,
				cache: true,
				_blank: menu.openStyle === 1,
				breadcrumb: []
			}
		}

		// 有子菜单的情况
		if (menu.children && menu.children.length > 0) {
			route.children?.push(...generateRoutes(menu.children))
		}

		routerList.push(route)
	})

	return routerList
}
