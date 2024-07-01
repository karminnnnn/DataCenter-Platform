<template>
	<el-dialog v-model="visible" :title="!state.row ? '新增' : '修改'" :close-on-click-modal="false">
		<el-form ref="dataFormRef" :model="dataForm" label-width="100px" @keyup.enter="submitHandle()">
			<!-- <el-form-item v-for="(label, key) in tableDataHeaderRef" :key="key" :label="label" :prop="label">
				<el-input v-model="dataForm.rows[key]"></el-input>
			</el-form-item> -->
			<div v-for="(label, key) in tableDataHeaderRef" :key="key">
                <el-form-item :label="label" :prop="key">
                    <el-input v-model="dataForm.rows[0][key]"></el-input>
                </el-form-item>
            </div>
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
import { useOdsTableDataPutApi, useOdsTableDataPostApi } from '@/api/data-integrate/ods'

const emit = defineEmits(['refreshDataList'])

const visible = ref(false)
const dataFormRef = ref()
const dataForm = reactive({
	datatableId: null,
	rows: [{}]
})

const state: IHooksOptions = reactive({
	row: null
})

const tableDataHeaderRef = ref({})
const init = (tableDataHeader: any, datatableId: any, row?: any) => {
	visible.value = true

	// 以下为重置数据部分
	// 重置dataform数据 dataform数据清零
	if (dataFormRef.value) {
		dataFormRef.value.resetFields()
	}
	// 删除dataForm中多余的属性
	Object.keys(dataForm).forEach(key => {
    if (!['datatableId', 'rows'].includes(key)) {
			delete dataForm[key]
		}
	})
	// 重置表头
	tableDataHeaderRef.value = {}
	// 重置表单
	dataForm.rows = [{}]

	// 表头设置 同时表单清零
	// if (tableDataHeader) {
	// 	for (const key in tableDataHeader) {
	// 		dataForm.rows[key] = ''
	// 	}
	// }
	// 初始化rows数组
    if (tableDataHeader) {
        for (const key in tableDataHeader) {
            if (tableDataHeader.hasOwnProperty(key)) {
                dataForm.rows[0][key] = '';  // 初始化空值
            }
        }
    }
	state.row = null

	// 开始赋值
	tableDataHeaderRef.value = tableDataHeader
	dataForm.datatableId = datatableId
	
	// 若为修改 对表单赋值
	if (row) {
		getProperty(row)
	}

	console.log("动态表单")
	console.log(dataForm)
}

const getProperty = (row?: any) => {
	// state.row赋值 用于判断标题
	state.row = row
	// 将 row 中的数据赋值给 dataForm.rows
	// for (const key in row) {
	// 	dataForm.rows[key] = String(row[key])
	// }
	for (const key in row) {
        if (row.hasOwnProperty(key)) {
            dataForm.rows[0][key] = String(row[key]);
        }
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
		console.log(JSON.stringify(dataForm))

		if(!state.row) {
			console.log("调用post新增")
			useOdsTableDataPostApi(dataForm).then(() => {
				ElMessage.success({
					message: '操作成功',
					duration: 500,
					onClose: () => {
						visible.value = false
						setTimeout(() => {
							emit('refreshDataList')
						}, 1000);
					}
				})
			})
		}
		else {
			console.log("调用put修改")
			useOdsTableDataPutApi(dataForm).then(() => {
				ElMessage.success({
					message: '操作成功',
					duration: 500,
					onClose: () => {
						visible.value = false
						setTimeout(() => {
							emit('refreshDataList')
						}, 1000);
					}
				})
			})
		}
	})
}

defineExpose({
	init
})
</script>
