<template>
	<el-dialog v-model="visible" :title="!dataForm.id ? '新增' : '修改'" :close-on-click-modal="false">
		<el-form ref="dataFormRef" :model="dataForm" :rules="dataRules" label-width="120px" @keyup.enter="submitHandle()">
			<el-form-item prop="orgId" label="所属机构">
				<el-tree-select
					clearable
					v-model="dataForm.orgId"
					:data="orgList"
					check-strictly
					value-key="id"
					:props="{ label: 'name', children: 'children' }"
					style="width: 100%"
				/>
			</el-form-item>
			<el-form-item label="数据表ID" prop="DatatableID">
				<el-input v-model="dataForm.DatatableID" placeholder="数据表ID"></el-input>
			</el-form-item>
			<el-form-item label="数据表名称" prop="DatatableName">
				<el-input v-model="dataForm.DatatableName" placeholder="数据表名称"></el-input>
			</el-form-item>
			<el-form-item label="数据库ID" prop="DatabaseID">
				<el-input v-model="dataForm.DatabaseID" placeholder="数据库ID"></el-input>
			</el-form-item>
			<el-form-item label="数据库名称" prop="DatabaseName">
				<el-input v-model="dataForm.DatabaseName" placeholder="数据库名称"></el-input>
			</el-form-item>
		</el-form>
		<template #footer>
			<el-button @click="visible = false">取消</el-button>
			<!--<el-button type="primary" @click="test()">测试</el-button>-->
			<el-button type="primary" @click="submitHandle()">确定</el-button>
		</template>
	</el-dialog>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { ElMessage } from 'element-plus/es'
import { useDatatableApi_v2, useDatatableSubmitApi_v2 /*testOnline*/ } from '@/api/data-integrate/database'
import { useOrgListApi } from '@/api/sys/orgs'

const emit = defineEmits(['refreshDataList'])

const visible = ref(false)
const dataFormRef = ref()
const orgList = ref([])

const dataForm = reactive({
	// orgId: '',
	// name: '',
	// databaseType: '',
	// databaseIp: '',
	// databasePort: '',
	// databaseName: '',
	// databaseSchema: '',
	// userName: '',
	// password: '',
	// newPassword: '******',
	// jdbcUrl: '',
	// projectId: ''

	DatatableID: '',
	DatatableName: '',
	DatabaseID: '',
	DatabaseName: ''
})

const init = (id?: number) => {
	visible.value = true
	dataForm.id = ''
	// dataForm.newPassword = '******'

	// 重置表单数据
	if (dataFormRef.value) {
		dataFormRef.value.resetFields()
	}

	//获取部门列表
	useOrgListApi().then(res => {
		orgList.value = res.data
	})

	if (id) {
		getDatabase(id)
	}
}

// const pwdChange = (newPwd) => {
// 	dataForm.password = newPwd
// }

const getDatabase = (id: number) => {
	useDatatableApi_v2(id).then(res => {
		Object.assign(dataForm, res.data)
	})
}

const dataRules = ref({
	// orgId: [{ required: true, message: '必填项不能为空', trigger: 'blur' }],
	// name: [{ required: true, message: '必填项不能为空', trigger: 'blur' }],
	// databaseType: [{ required: true, message: '必填项不能为空', trigger: 'blur' }],
	// databaseIp: [{ required: true, message: '必填项不能为空', trigger: 'blur' }],
	// databasePort: [{ required: true, message: '必填项不能为空', trigger: 'blur' }],
	// databaseName: [{ required: true, message: '必填项不能为空', trigger: 'blur' }],
	// databaseSchema: [{ required: true, message: '必填项不能为空', trigger: 'blur' }],
	// userName: [{ required: true, message: '必填项不能为空', trigger: 'blur' }],
	// password: [{ required: true, message: '必填项不能为空', trigger: 'blur' }],
	// projectId: [{ required: true, message: '必填项不能为空', trigger: 'blur' }]

	DatatableID: [{ required: true, message: '必填项不能为空', trigger: 'blur' }],
	DatatableName: [{ required: true, message: '必填项不能为空', trigger: 'blur' }],
	DatabaseID: [{ required: true, message: '必填项不能为空', trigger: 'blur' }],
	DatabaseName: [{ required: true, message: '必填项不能为空', trigger: 'blur' }]
})

// 表单提交
const submitHandle = () => {
	dataFormRef.value.validate((valid: boolean) => {
		if (!valid) {
			return false
		}

		useDatatableSubmitApi_v2(dataForm).then(() => {
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

// // 测试连接
// const test = () => {
// 	dataFormRef.value.validate((valid: boolean) => {
// 		if (!valid) {
// 			return false
// 		}
// 		testOnline(dataForm).then(() => {
// 			ElMessage.success({
// 				message: '测试连接成功',
// 				duration: 500,
// 				onClose: () => {
// 					emit('refreshDataList')
// 				}
// 			})
// 		})
// 	})
// }

defineExpose({
	init
})
</script>
