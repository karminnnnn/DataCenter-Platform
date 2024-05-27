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
				<el-form-item label="数据库ID" prop="id">
					<el-input v-model="dataForm.id" placeholder="数据库ID" disabled></el-input>
				</el-form-item>
				<el-form-item label="数据库名称" prop="database_name">
					<el-input v-model="dataForm.database_name" placeholder="数据库名称"></el-input>
				</el-form-item>
				<el-form-item label="数据源ID" prop="datasource_id">
					<el-input v-model="dataForm.datasource_id" placeholder="数据源ID" disabled></el-input>
				</el-form-item>
				<el-form-item label="数据源名称" prop="datasource_name">
					<el-input v-model="dataForm.datasource_name" placeholder="数据源名称" disabled></el-input>
				</el-form-item>
				<el-form-item label="同步状态" prop="syn_status">
					<el-input v-model="dataForm.syn_status" placeholder="同步状态" disabled></el-input>
				</el-form-item>
				<el-form-item label="状态" prop="status">
					<el-input v-model="dataForm.status" placeholder="状态" disabled></el-input>
				</el-form-item>
				<el-form-item label="版本" prop="version">
					<el-input v-model="dataForm.version" placeholder="状态"></el-input>
				</el-form-item>
				<el-form-item label="是否删除" prop="deleted">
					<el-input v-model="dataForm.deleted" placeholder="状态"></el-input>
				</el-form-item>
				<el-form-item label="创建者" prop="creator_name">
					<el-input v-model="dataForm.creator_name" placeholder="创建者"></el-input>
				</el-form-item>
				<el-form-item label="创建时间" prop="create_time">
					<el-input v-model="dataForm.create_time" placeholder="创建时间"></el-input>
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
import { useDatabaseApi_v2, useDatabaseSubmitApi_v2,/*testOnline*/ } from '@/api/data-integrate/database'
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

	// Mine
	id: '1',
	database_name: '1',
	datasource_id: '1',
	datasource_name: '1',
	syn_status: '1',
	status: '1',
	version: '1',
	deleted: '1',
	creator_name: '1',
	create_time: '1',
	updater: '1',
	updater_name: '1',
	update_time: '1',
	})

const init = (id?: number) => {
	console.log('hello')
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
	useDatabaseApi_v2(id).then(res => {
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

	id: [{ required: true, message: '必填项不能为空', trigger: 'blur' }],
	database_name: [{ required: true, message: '必填项不能为空', trigger: 'blur' }],
	datasource_id: [{ required: true, message: '必填项不能为空', trigger: 'blur' }],
	datasource_name: [{ required: true, message: '必填项不能为空', trigger: 'blur' }],
	status: [{ required: true, message: '必填项不能为空', trigger: 'blur' }],
	version: [{ required: true, message: '必填项不能为空', trigger: 'blur' }],
	deleted: [{ required: true, message: '必填项不能为空', trigger: 'blur' }],
	creator_name: [{ required: true, message: '必填项不能为空', trigger: 'blur' }],
	create_time: [{ required: true, message: '必填项不能为空', trigger: 'blur' }],
})

// 表单提交
const submitHandle = () => {
	dataFormRef.value.validate((valid: boolean) => {
		if (!valid) {
			return false
		}

		useDatabaseSubmitApi_v2(dataForm).then(() => {
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
