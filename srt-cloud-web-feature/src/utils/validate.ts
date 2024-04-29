import { i18n } from '@/i18n'

export const isExternalLink = (path: string): boolean => {
	return /^(https?:|mailto:|tel:)/.test(path)
}

export const validateEmail = (rule: any, value: any, callback: (e?: Error) => any) => {
	const reg =
		/^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/

	if (reg.test(value)) {
		callback()
	} else {
		callback(new Error(i18n.global.t('error.email')))
	}
}

export const validatePassword = (rule: any, value: any, callback: (e?: Error) => any) => {
	if (value.length < 4) {
		callback(new Error(i18n.global.t('error.password', { len: 4 })))
	} else {
		callback()
	}
}

export const validateMobile = (mobile: string): boolean => {
	return /^1[3456789]\d{9}$/.test(mobile)
}


// import { i18n } from '@/i18n'：从 i18n 模块中导入 i18n 对象，用于国际化处理。
// export const isExternalLink = (path: string): boolean => { ... }：判断传入的路径是否为外部链接（以 http、https、mailto、tel 开头），返回布尔值。
// export const validateEmail = (rule: any, value: any, callback: (e?: Error) => any) => { ... }：用于验证邮箱格式的函数。
// 	使用正则表达式验证邮箱格式，如果格式正确则调用 callback()，否则调用 callback(new Error(i18n.global.t('error.email')))，其中 i18n.global.t('error.email') 是获取国际化处理后的错误信息。
// export const validatePassword = (rule: any, value: any, callback: (e?: Error) => any) => { ... }：用于验证密码格式的函数。
// 	如果密码长度小于 4，则调用 callback(new Error(i18n.global.t('error.password', { len: 4 })))，否则调用 callback()，其中 i18n.global.t('error.password', { len: 4 }) 是获取国际化处理后的错误信息，包含密码长度。
// export const validateMobile = (mobile: string): boolean => { ... }：用于验证手机号格式的函数。
// 	使用正则表达式验证手机号格式，返回布尔值。