import store from '@/store'

// 关闭tab
export const closeTab = (router: any, tab: any) => {
	if (tab.meta && tab.meta.affix) {
		return
	}

	store.tabsStore.delView(tab)
	toLastView(router, store.tabsStore.visitedViews, tab)
}

// 关闭其他tabs
export const closeOthersTabs = (router: any, tab: any) => {
	router.push(tab)
	store.tabsStore.delOthersViews(tab)
}

// 关闭全部tabs
export const closeAllTabs = (router: any, tab: any) => {
	store.tabsStore.delAllViews()
	toLastView(router, store.tabsStore.visitedViews, tab)
}

// 跳转到最后一个tab
export const toLastView = (router: any, visitedViews: any[], view: any) => {
	const latestView = visitedViews.slice(-1)[0]
	if (latestView) {
		router.push(latestView.fullPath)
	} else {
		if (view.name === 'Home') {
			router.replace({ path: '/redirect' + view.fullPath })
		} else {
			router.push('/')
		}
	}
}

// import store from '@/store'：导入 Vuex 的 store 实例。
// export const closeTab = (router: any, tab: any) => { ... }：导出一个名为 closeTab 的函数，用于关闭标签页。
// 	如果标签页的 meta 中包含 affix 属性，则直接返回，不做任何操作（这通常表示该标签页是固定的，不能关闭）。
// 	调用 store.tabsStore.delView(tab) 方法，从标签页列表中删除指定的标签页。
// 	调用 toLastView(router, store.tabsStore.visitedViews, tab) 方法，将页面跳转到上一个标签页或首页。
// export const closeOthersTabs = (router: any, tab: any) => { ... }：导出一个名为 closeOthersTabs 的函数，用于关闭除指定标签页外的其他标签页。
// 	使用 router.push(tab) 方法，将页面跳转到指定的标签页。
// 	调用 store.tabsStore.delOthersViews(tab) 方法，删除除指定标签页外的所有其他标签页。
// export const closeAllTabs = (router: any, tab: any) => { ... }：导出一个名为 closeAllTabs 的函数，用于关闭所有标签页。
// 	调用 store.tabsStore.delAllViews() 方法，删除所有标签页。
// 	调用 toLastView(router, store.tabsStore.visitedViews, tab) 方法，将页面跳转到上一个标签页或首页。
// export const toLastView = (router: any, visitedViews: any[], view: any) => { ... }：导出一个名为 toLastView 的函数，用于将页面跳转到最后一个标签页。
// 	获取标签页列表中的最后一个标签页 latestView。
// 	如果存在最后一个标签页，则使用 router.push(latestView.fullPath) 方法将页面跳转到该标签页的路径。
// 	如果不存在最后一个标签页，则根据当前页面的名称跳转到对应的首页或重定向页。
// 	这些函数主要用于管理页面的标签页，包括关闭标签页、关闭其他标签页、关闭所有标签页以及跳转到最后一个标签页等操作。
