<template>
	<el-dialog v-model="visible" :title="!dataForm.datatableId ? '新增' : '修改'" :close-on-click-modal="false">
		<el-form ref="dataFormRef" :model="dataForm" :rules="dataRules" label-width="100px" @keyup.enter="submitHandle()">
			<el-form-item label="表名" prop="datatableName">
				<el-input v-model="dataForm.datatableName" placeholder="字段名称"></el-input>
			</el-form-item>
			<el-form-item label="注释" prop="remarks">
				<el-input type="textarea" :rows="2" v-model="dataForm.remarks" placeholder="注释"></el-input>
			</el-form-item>
		</el-form>
		<template #footer>
			<el-button @click="visible = false">取消</el-button>
			<el-button type="primary" @click="submitHandle()">确定</el-button>
		</template>
	</el-dialog>
</template>

<script setup lang="ts">
import { STATEMENT_OR_BLOCK_KEYS } from '@babel/types'
import { reactive, ref } from 'vue'
import { useOdsSubmitApi, useOdsApi } from '@/api/data-integrate/ods'

const visible = ref(false)
const dataFormRef = ref()

const dataForm = reactive({
	datatableId: null,
	dataAccessId: null,
	projectId: null,
	datatableName: '',
	remarks: '',
	recentlySyncTime: '',
	databaseId: null
})

//初始化
const init = (row?: any) => {
	visible.value = true

	// 重置表单数据
	if (dataFormRef.value) {
		dataFormRef.value.resetFields()
	}

	// 清空 dataForm 对象的所有字段
	Object.assign(dataForm, {
		datatableId: null,
		dataAccessId: null,
		projectId: null,
		datatableName: '',
		remarks: '',
		recentlySyncTime: '',
		databaseId: null
	})

	if (row) {
		// getProperty(row)
		getTableInfo(row.datatableId)
	}
}

// const getProperty = (row: any) => {
// 	// console.log("aaaaaaaaaaaaaaa")
// 	// console.log(row)
// 	dataForm.datatableName = row.datatableName
// 	dataForm.remarks = row.remarks
// 	// console.log("aaaaaaaaaaaaaaa")
// 	// console.log(dataForm)
// }

const getTableInfo = (id: number) => {
	useOdsApi(id).then(res => {
		Object.assign(dataForm, res.data)
	})
}

const formatDate = (date: Date): string => {
	const year = date.getFullYear()
	const month = String(date.getMonth() + 1).padStart(2, '0')
	const day = String(date.getDate()).padStart(2, '0')
	const hours = String(date.getHours()).padStart(2, '0')
	const minutes = String(date.getMinutes()).padStart(2, '0')
	const seconds = String(date.getSeconds()).padStart(2, '0')

	return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
}

// 表单验证
const dataRules = ref({
	datatableName: [{ required: true, message: '必填项不能为空', trigger: 'blur' }]
})

// 表单提交
const submitHandle = () => {
	dataFormRef.value.validate((valid: boolean) => {
		if (!valid) {
			return false
		}

		const now = new Date()
		const formattedTime = formatDate(now)
		dataForm.recentlySyncTime = formattedTime

		console.log('提交的表单')
		console.log(dataForm)

		// useOdsSubmitApi(dataForm).then(() => {
		// 	ElMessage.success({
		// 		message: '操作成功',
		// 		duration: 500,
		// 		onClose: () => {
		// 			visible.value = false
		// 			emit('refreshDataList')
		// 		}
		// 	})
		// })
	})
}

defineExpose({
	init
})
</script>
