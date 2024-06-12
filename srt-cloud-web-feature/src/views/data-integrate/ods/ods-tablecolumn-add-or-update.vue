<template>
	<el-dialog v-model="visible" :title="!dataForm.oldfieldname ? '新增' : '修改'" :close-on-click-modal="false">
		<el-form ref="dataFormRef" :model="dataForm" :rules="dataRules" label-width="100px" @keyup.enter="submitHandle()">
			<el-form-item label="字段名称" prop="fieldName">
				<el-input v-model="dataForm.fieldName" placeholder="字段名称"></el-input>
			</el-form-item>
			<el-form-item label="注释" prop="remarks">
				<el-input type="textarea" :rows="2" v-model="dataForm.remarks" placeholder="注释"></el-input>
			</el-form-item>
			<el-form-item label="字段类型" prop="fieldTypeName">
				<el-select v-model="dataForm.fieldTypeName" placeholder="请选择">
					<el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value"> </el-option>
				</el-select>
			</el-form-item>
			<el-form-item label="字段长度" prop="displaySize">
				<el-input-number v-model="dataForm.displaySize" :max="9999" placeholder="字段长度"></el-input-number>
			</el-form-item>
			<el-form-item label="小数位数" prop="scaleSize">
				<el-input-number v-model="dataForm.scaleSize" :max="99" placeholder="小数位数"></el-input-number>
			</el-form-item>
			<el-form-item label="默认值" prop="defaultValue">
				<el-input v-model="dataForm.defaultValue" placeholder="默认值"></el-input>
			</el-form-item>
			<el-form-item label="是否主键" prop="pk">
				<el-radio-group v-model="dataForm.pk">
					<el-radio :label="true">是</el-radio>
					<el-radio :label="false">否</el-radio>
				</el-radio-group>
			</el-form-item>
			<el-form-item label="是否自主递增" prop="autoIncrement">
				<el-radio-group v-model="dataForm.autoIncrement">
					<el-radio :label="true">是</el-radio>
					<el-radio :label="false">否</el-radio>
				</el-radio-group>
			</el-form-item>
			<el-form-item label="是否为空" prop="nullable">
				<el-radio-group v-model="dataForm.nullable">
					<el-radio :label="true">是</el-radio>
					<el-radio :label="false">否</el-radio>
				</el-radio-group>
			</el-form-item>
		</el-form>
		<template #footer>
			<el-button @click="visible = false">取消</el-button>
			<el-button type="primary" @click="submitHandle()">确定</el-button>
		</template>
	</el-dialog>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { ElMessage } from 'element-plus/es'
import { useOdsColumnInfoApi, useOdsColumnInfoSubmitApi } from '@/api/data-integrate/ods'

const emit = defineEmits(['refreshDataList'])

const visible = ref(false)
const dataFormRef = ref()

const dataForm = reactive({
	fieldId: null,
	fieldName: '',
	remarks: '',
	fieldTypeName: '',
	displaySize: 10,
	scaleSize: 3,
	defaultValue: '',
	pk: false,
	autoIncrement: false,
	nullable: false,
	datatableId: '',
	datatableName:'',
	oldfieldname:''
})

// 定义字段类型多选框 options 数据
const options = reactive([
	{ value: 'INT', label: 'INT' },
	{ value: 'BIGINT', label: 'BIGINT' },
	{ value: 'CHAR', label: 'CHAR' },
	{ value: 'VARCHAR', label: 'VARCHAR' },
	{ value: 'FLOAT', label: 'FLOAT' },
	{ value: 'STRING', label: 'STRING' },
	{ value: 'DATE', label: 'DATE' },
	{ value: 'DATETIME', label: 'DATETIME' },
	{ value: 'TIME', label: 'TIME' },
	{ value: 'BOOLEN', label: 'BOOLEN' },
	{ value: 'TEXT', label: 'TEXT' }
])

//初始化
const init = (datatableId:any, row?: any) => {
	visible.value = true

	// 重置表单数据
	if (dataFormRef.value) {
		dataFormRef.value.resetFields()
	}

	// 清空 dataForm 对象的所有字段
	Object.assign(dataForm, {
		fieldId: null,
		fieldName: '',
		remarks: '',
		fieldTypeName: '',
		displaySize: 10,
		scaleSize: 3,
		defaultValue: '',
		pk: false,
		autoIncrement: false,
		nullable: false,
		datatableId: '',
		datatableName:'',
		oldfieldname:''
	})

	dataForm.datatableId = datatableId
	// console.log("看看row获取的信息")
	// console.log(row)
	if (row) {
		getTableColumnInfo(row.fieldName, row.datatableId)
		// console.log("调用get接口")
	}
}


const getTableColumnInfo = (fieldName: any, datatableId: number) => {
	useOdsColumnInfoApi(fieldName, datatableId).then(res => {
		Object.assign(dataForm, res.data)
		dataForm.oldfieldname = res.data.fieldName
	})

	// console.log("看看字段部分get接口获取的数据")
	// console.log(dataForm)
}

const dataRules = ref({
	fieldName: [{ required: true, message: '必填项不能为空', trigger: 'blur' }],
	fieldTypeName: [{ required: true, message: '必填项不能为空', trigger: 'blur' }],
	displaySize: [{ required: true, message: '必填项不能为空', trigger: 'blur' }],
	scaleSize: [{ required: true, message: '必填项不能为空', trigger: 'blur' }],
	pk: [{ required: true, message: '必填项不能为空', trigger: 'blur' }],
	autoIncrement: [{ required: true, message: '必填项不能为空', trigger: 'blur' }],
	nullable: [{ required: true, message: '必填项不能为空', trigger: 'blur' }]
})

// 表单提交
const submitHandle = () => {
	dataFormRef.value.validate((valid: boolean) => {
		if (!valid) {
			return false
		}

		console.log('提交的表单')
		console.log(dataForm)

		useOdsColumnInfoSubmitApi(dataForm, dataForm.oldfieldname).then(() => {
			ElMessage.success({
				message: '操作成功',
				duration: 500,
				onClose: () => {
					visible.value = false
					emit('refreshDataList')
				}
			})
		})
	})
}

defineExpose({
	init
})
</script>
