<template>
	<el-dialog v-model="visible" :title="!state.row ? '新增' : '修改'" :close-on-click-modal="false">
		<el-form ref="dataFormRef" :model="dataForm" label-width="100px" @keyup.enter="submitHandle()">
			<el-form-item v-for="(label, key) in state.tableDataHeader" :key="key" :label="label" :prop="label">
				<el-input v-model="dataForm.rows[key]"></el-input>
			</el-form-item>
		</el-form>
		<template #footer>
			<el-button @click="visible = false">取消</el-button>
			<el-button type="primary" @click="submitHandle">确定</el-button>
		</template>
	</el-dialog>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { ElMessage } from 'element-plus/es'
import { IHooksOptions } from '@/hooks/interface'

const emit = defineEmits(['refreshDataList'])

const visible = ref(false)
const dataFormRef = ref()
const dataForm = reactive({
	datatableId: null,
	rows: []
})

const state: IHooksOptions = reactive({
	createdIsNeed: false,

	tableDataHeader: {},
	row: null
})

const init = (tableDataHeader?: any, row?: any) => {
	visible.value = true

	// 重置表单数据
	if (dataFormRef.value) {
		dataFormRef.value.resetFields()
	}

	// 表单属性设置 同时表单清零
	if (tableDataHeader) {
		for (const key in tableDataHeader) {
			dataForm.rows[key] = ''
		}
	}
	state.tableDataHeader = tableDataHeader
	state.row = null

	// console.log("动态表单")
	// console.log(dataForm)

	if (row) {
		getProperty(row)
	}
}

const getProperty = (row?: any) => {
	state.row = row
	for (const key in row) {
		dataForm.rows[key] = String(row[key])
	}
}

// 表单提交
const submitHandle = () => {
	dataFormRef.value.validate((valid: boolean) => {
		if (!valid) {
			return false
		}

		console.log('动态表单提交的form')
		console.log(dataForm)

		// usePropertySubmitApi(dataForm).then(() => {
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
