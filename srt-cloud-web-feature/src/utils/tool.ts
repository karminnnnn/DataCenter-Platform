import type { App, Plugin } from 'vue'

// 把路径转换成驼峰命名
// 将传入的字符串路径转换为驼峰命名的字符串，并返回转换后的结果
export const pathToCamel = (path: string): string => {
	return path.replace(/\/(\w)/g, (all, letter) => letter.toUpperCase())
}

// 是否外链
// 判断传入的字符串是否为外部链接，返回布尔值
export const isExternalLink = (url: string): boolean => {
	return /^(https?:|\/\/|http?:|\/\/|^{{\s?apiUrl\s?}})/.test(url)
}

// 替换外链参数
// 替换传入的字符串中的特定参数，返回替换后的结果
export const replaceLinkParam = (url: string): string => {
	return url.replace('{{apiUrl}}', constant.apiUrl)
}

// 转换文件大小格式
// 将传入的文件大小（字节数）转换为可读的文件大小格式（如 KB、MB、GB），返回转换后的结果
export const convertSizeFormat = (size: number): string => {
	const unit = ['Bytes', 'KB', 'MB', 'GB', 'TB', 'PB']
	let index = Math.floor(Math.log(size) / Math.log(1024))
	let newSize = size / Math.pow(1024, index)

	// 保留的小数位数
	return newSize.toFixed(2) + ' ' + unit[index]
}

// 获取svg图标(id)列表
// 获取页面中所有 SVG 图标的 ID 列表，返回 ID 列表数组。
export const getIconList = (): string[] => {
	const rs: string[] = []
	const list = document.querySelectorAll('svg symbol')
	for (let i = 0; i < list.length; i++) {
		rs.push(list[i].id)
	}
	return rs
}

// 获取字典Label
// 根据传入的字典列表和字典类型及值，返回对应的字典标签（如果找不到，则返回字典值本身）
export const getDictLabel = (dictList: any[], dictType: string, dictValue: string) => {
	const type = dictList.find((element: any) => element.dictType === dictType)
	if (type) {
		const val = type.dataList.find((element: any) => element.dictValue === dictValue + '')
		if (val) {
			return val.dictLabel
		} else {
			return dictValue
		}
	} else {
		return dictValue
	}
}

// 获取字典数据列表
// 根据传入的字典列表和字典类型，返回对应字典类型的数据列表
export function getDictDataList(dictList: any[], dictType: string) {
	const type = dictList.find((element: any) => element.dictType === dictType)
	if (type) {
		return type.dataList
	} else {
		return []
	}
}

// 获取项目名称
// 根据传入的项目列表和项目ID，返回对应的项目名称（如果找不到，则返回项目ID）
export const getValByProjectId = (projectList: any[], projectId: number) => {
	const project = projectList.find((element: any) => element.id === projectId)
	if (project) {
		const projectVal = project.name
		if (projectVal) {
			return projectVal
		} else {
			return projectId
		}
	} else {
		return projectId
	}
}

// 获取部门名称
// 根据传入的部门列表和部门ID，返回对应的部门名称（如果找不到，则返回空字符串）
export const getValByOrgId = (orgList: any[], orgId: number) => {
	const org = orgList.find((element: any) => element.id === orgId)
	if (org) {
		const orgVal = org.name
		if (orgVal) {
			return orgVal
		} else {
			return ''
		}
	} else {
		return orgId
	}
}

// 获取用户名称
// 根据传入的用户列表和用户ID，返回对应的用户名（如果找不到，则返回用户ID）
export const getNameByUserId = (sysUserList: any[], userId: number) => {
	const user = sysUserList.find((element: any) => element.id === userId)
	if (user) {
		const username = user.username
		if (username) {
			return username
		} else {
			return userId
		}
	} else {
		return userId
	}
}

// 全局组件安装
// 全局组件安装函数，用于将组件安装到 Vue 应用实例中，并可以设置组件的别名
export const withInstall = <T>(component: T, alias?: string) => {
	const comp = component as any
	comp.install = (app: App) => {
		app.component(comp.name || comp.displayName, component)
		if (alias) {
			app.config.globalProperties[alias] = component
		}
	}
	return component as T & Plugin
}
