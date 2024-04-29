declare module '*.svg'
declare module '*.png'
declare module '*.jpg'
declare module '*.gif'
declare module '*.scss'
declare module '*.ts'
declare module '*.js'

declare module '*.vue' {
	import type { DefineComponent } from 'vue'
	const component: DefineComponent<{}, {}, any>
	export default component
}

// 这段代码是 TypeScript 中的模块声明语句，用于告诉 TypeScript 编译器如何处理特定类型的模块