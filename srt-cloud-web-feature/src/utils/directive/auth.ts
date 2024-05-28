import type { App } from 'vue'
import store from '@/store'

export function auth(app: App) {
	// 权限验证
	app.directive('auth', {
		mounted(el, binding) {
			if (!store.userStore.authorityList.some((v: string) => v === binding.value)) {
				el.parentNode.removeChild(el)
			}
		}
	})
}

// import type { App } from 'vue'：从 Vue 中导入 App 类型，用于指定 Vue 应用的类型。
// import store from '@/store'：从项目的 store 目录下导入 Vuex 的 store 实例，用于获取用户权限信息。
// export function auth(app: App)：导出一个名为 auth 的函数，该函数接受一个 App 类型的参数 app，表示 Vue 应用实例。
// app.directive('auth', {：在 Vue 应用实例上注册一个全局指令，指令名称为 auth，用于权限验证。
// mounted(el, binding) {：指令的钩子函数 mounted，在绑定元素插入父节点时触发。
// if (!store.userStore.authorityList.some((v: string) => v === binding.value)) {：通过访问 Vuex store 中的 userStore 模块的 authorityList 属性，检查当前用户的权限是否包含指令绑定的值。如果不包含，表示用户没有权限，则从 DOM 中移除该元素。
// 整体而言，这段代码实现了一个 Vue 指令，用于根据用户的权限动态显示或隐藏元素，以实现简单的权限控制功能。
