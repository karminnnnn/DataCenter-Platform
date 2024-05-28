import { defineStore } from 'pinia'
import { RouteLocationNormalizedLoaded } from 'vue-router'

export const tabsStore = defineStore('tabsStore', {
	state: () => ({
		visitedViews: [] as any[],
		cachedViews: [] as any[]
	}),
	actions: {
		addView(view: RouteLocationNormalizedLoaded) {
			if (this.visitedViews.some(value => value.path === view.path)) {
				return
			}
			this.visitedViews.push(
				Object.assign({}, view, {
					title: view.meta?.title || 'unknown'
				})
			)
		},
		addCachedView(view: RouteLocationNormalizedLoaded) {
			if (this.cachedViews.includes(view.name)) {
				return
			}

			if (view.meta.cache) {
				this.cachedViews.push(view.name)
			}
		},
		delView(view: RouteLocationNormalizedLoaded) {
			for (const [i, v] of this.visitedViews.entries()) {
				if (v.path === view.path) {
					this.visitedViews.splice(i, 1)
					break
				}
			}

			this.delCachedView(view).then()
		},
		delCachedView(view: RouteLocationNormalizedLoaded) {
			return new Promise(resolve => {
				const index = this.cachedViews.indexOf(view.name)
				if (index > -1) {
					this.cachedViews.splice(index, 1)
				}
				resolve([...this.cachedViews])
			})
		},
		delOthersViews(view: RouteLocationNormalizedLoaded) {
			this.visitedViews = this.visitedViews.filter(v => {
				return v.meta.affix || v.path === view.path
			})

			const index = this.cachedViews.indexOf(view.name)
			if (index > -1) {
				this.cachedViews = this.cachedViews.slice(index, index + 1)
			} else {
				this.cachedViews = []
			}
		},
		delAllViews() {
			this.visitedViews = this.visitedViews.filter(tab => tab.meta.affix)
			this.cachedViews = []
		}
	}
})

// import { defineStore } from 'pinia'：从 Pinia 库中导入 defineStore 函数，用于定义一个新的状态管理模块。
// import { RouteLocationNormalizedLoaded } from 'vue-router'：从 Vue Router 中导入 RouteLocationNormalizedLoaded 类型，表示已加载的路由位置信息。
// export const tabsStore = defineStore('tabsStore', { ... })：使用 defineStore 函数定义了一个名为 tabsStore 的状态管理模块。
// 	定义了模块的 state，其中包含了 visitedViews 和 cachedViews 两个状态，初始值分别为空数组。
// 		visitedViews 用于存储已访问的标签页信息。
// 		cachedViews 用于存储缓存的标签页信息。
// actions: { ... }：定义了模块的 actions，用于定义各种操作。
// 	addView(view: RouteLocationNormalizedLoaded)：添加一个标签页。
// 		如果已存在相同路径的标签页，则不做任何操作。
// 		否则，将新标签页信息添加到 visitedViews 中。
// 	addCachedView(view: RouteLocationNormalizedLoaded)：添加一个需要缓存的标签页。
// 		如果标签页已经在缓存列表中，则不做任何操作。
// 		如果标签页需要缓存，则将其名称添加到 cachedViews 中。
// 	delView(view: RouteLocationNormalizedLoaded)：删除一个标签页。
// 		遍历 visitedViews，找到对应路径的标签页并删除。
// 		调用 delCachedView 方法删除对应的缓存标签页。
// 	delCachedView(view: RouteLocationNormalizedLoaded)：删除一个缓存的标签页。
// 		返回一个 Promise，在异步删除缓存标签页后 resolve 缓存列表。
// 	delOthersViews(view: RouteLocationNormalizedLoaded)：删除除当前标签页外的其他标签页。
// 		只保留固定标签页（affix 属性为 true）和当前标签页。
// 	delAllViews()：删除所有标签页，保留固定标签页。
// 		这样就定义了一个包含标签页相关状态和操作的状态管理模块 tabsStore，可以在应用中使用该模块来管理标签页。
