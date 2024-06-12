import { IHooksOptions } from '@/hooks/interface'
import service from '@/utils/request'
import { onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import axios from 'axios'

export const useCrud = (options: IHooksOptions) => {
	// 定义了一个默认的配置选项对象，包含了 CRUD 相关的各种默认值
	const defaultOptions: IHooksOptions = {
		createdIsNeed: true,
		dataListUrl: '',
		isPage: true,
		deleteUrl: '',
		primaryKey: 'id',
		exportUrl: '',
		queryForm: {},
		dataList: [],
		order: '',
		asc: false,
		page: 1,
		limit: 10,
		total: 0,
		pageSizes: [10, 20, 50, 100, 200],
		dataListLoading: false,
		dataListSelections: []
	}

	// 定义了一个函数，用于将传入的配置选项与默认配置选项进行合并。
	const mergeDefaultOptions = (options: any, props: any): IHooksOptions => {
		for (const key in options) {
			if (!Object.getOwnPropertyDescriptor(props, key)) {
				props[key] = options[key]
			}
		}
		return props
	}

	// 覆盖默认值
	// 函数将传入的配置选项与默认配置合并，得到最终的状态对象 state。
	const state = mergeDefaultOptions(defaultOptions, options)

	// 在组件挂载后执行的钩子函数，用于初始化数据列表。
	onMounted(() => {
		if (state.createdIsNeed) {
			query()
		}
	})

	// 定义了一个查询数据列表的函数，用于发起 HTTP 请求获取数据列表。
	const query = (preFun: any) => {
		if (!state.dataListUrl) {
			return
		}

		state.dataListLoading = true
		console.log(state.dataListUrl)
		console.log(state)
		service
			.get(state.dataListUrl, {
				params: {
					order: state.order,
					asc: state.asc,
					page: state.isPage ? state.page : null,
					limit: state.isPage ? state.limit : null,
					...state.queryForm
				}
			})
			.then((res: any) => {
				//console.log(preFun)
				// zrx 前置执行
				if (preFun && typeof preFun === 'function' && typeof preFun.nodeType !== 'number' && state.isPage) {
					preFun(res.data.list)
				}
				state.dataList = state.isPage ? res.data.list : res.data
				state.total = state.isPage ? res.data.total : 0
			})
			.finally(() => {
				state.dataListLoading = false
			})
	}

	// 定义了一个获取数据列表的函数，用于重置页码并重新查询数据列表。
	const getDataList = preFun => {
		state.page = 1
		query(preFun)
	}

	// 定义了一个处理每页显示数量变化的函数。
	const sizeChangeHandle = (val: number, preFun) => {
		state.page = 1
		state.limit = val
		query(preFun)
	}

	// 定义了一个处理当前页码变化的函数。
	const currentChangeHandle = (val: number, preFun) => {
		state.page = val
		query(preFun)
	}

	// 多选
	// 定义了一个处理多选项变化的函数。
	const selectionChangeHandle = (selections: any[]) => {
		console.log('selection change')
		state.dataListSelections = selections.map((item: any) => state.primaryKey && item[state.primaryKey])
	}

	// 排序
	// 定义了一个处理排序变化的函数。
	const sortChangeHandle = (data: any) => {
		const { prop, order } = data

		if (prop && order) {
			state.order = prop
			state.asc = order === 'ascending'
		} else {
			state.order = ''
		}

		query()
	}

	// 定义了一个处理删除单条记录的函数。
	const deleteHandle = (key: number | string) => {
		if (!state.deleteUrl) {
			return
		}

		ElMessageBox.confirm('确定进行删除操作?', '提示', {
			confirmButtonText: '确定',
			cancelButtonText: '取消',
			type: 'warning'
		})
			.then(() => {
				service.delete(state.deleteUrl + '/' + key).then(() => {
					ElMessage.success('删除成功')

					query()
				})
			})
			.catch(() => {})
	}

	// 定义了一个处理批量删除记录的函数。
	const deleteBatchHandle = (key?: number | string) => {
		let data: any[] = []
		if (key) {
			data = [key]
		} else {
			data = state.dataListSelections ? state.dataListSelections : []

			if (data.length === 0) {
				ElMessage.warning('请选择删除记录')
				return
			}
		}

		ElMessageBox.confirm('确定进行删除操作?', '提示', {
			confirmButtonText: '确定',
			cancelButtonText: '取消',
			type: 'warning'
		})
			.then(() => {
				if (state.deleteUrl) {
					service.delete(state.deleteUrl, { data }).then(() => {
						ElMessage.success('删除成功')

						query()
					})
				}
			})
			.catch(() => {})
	}

	// 定义了一个处理下载文件的函数，返回一个 Promise 对象。
	const downloadHandle = (url: string, filename?: string, method: string = 'GET', params: any): Promise<any> => {
		return axios({
			responseType: 'blob',
			url: url,
			method: method,
			data: params ? params : {}
		})
			.then((res: any): any => {
				// 创建a标签
				const down = document.createElement('a')
				// 文件名没传，则使用时间戳
				down.download = filename || new Date().getTime().toString()
				// 隐藏a标签
				down.style.display = 'none'

				// 创建下载url
				let binaryData = []
				binaryData.push(res.data)
				down.href = URL.createObjectURL(new Blob(binaryData))

				// 模拟点击下载
				document.body.appendChild(down)
				down.click()

				// 释放URL
				URL.revokeObjectURL(down.href)
				// 下载完成移除
				document.body.removeChild(down)
			})
			.catch(err => {
				ElMessage.error(err.message)
			})
	}

	// 返回包含上述函数的对象，供组件中调用
	return {
		getDataList,
		sizeChangeHandle,
		currentChangeHandle,
		selectionChangeHandle,
		sortChangeHandle,
		deleteHandle,
		deleteBatchHandle,
		downloadHandle
	}
}
