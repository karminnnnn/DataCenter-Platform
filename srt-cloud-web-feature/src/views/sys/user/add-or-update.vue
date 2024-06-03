<template>
	<el-dialog v-model="visible" :title="!dataForm.id ? '新增' : '修改'" :close-on-click-modal="false" draggable>
		<el-form ref="dataFormRef" :model="dataForm" :rules="dataRules" label-width="120px" @keyup.enter="submitHandle()">
			<el-form-item prop="username" label="用户名">
				<el-input v-model="dataForm.username" placeholder="用户名"></el-input>
			</el-form-item>
			<el-form-item prop="realName" label="姓名">
				<el-input v-model="dataForm.realName" placeholder="姓名"></el-input>
			</el-form-item>
			<el-form-item prop="password" label="新密码">
				<el-input v-model="dataForm.password" type="password" placeholder="密码"></el-input>
			</el-form-item>
			<el-form-item prop="orgId" label="所属平台">
				<el-select v-model="dataForm.orgId" placeholder="所属平台" style="width: 100%">
					<el-option v-for="org in orgList" :key="org.id" :label="org.name" :value="org.id"> </el-option>
				</el-select>
			</el-form-item>
			<el-form-item prop="roleId" label="所属角色">
				<el-select v-model="dataForm.roleId" placeholder="所属角色" style="width: 100%">
					<el-option v-for="role in roleList" :key="role.id" :label="role.name" :value="role.id"> </el-option>
				</el-select>
			</el-form-item>
			<el-form-item prop="gender" label="性别">
				<fast-radio-group v-model="dataForm.gender" dict-type="user_gender"></fast-radio-group>
			</el-form-item>
			<el-form-item prop="superAdmin" label="是否超级管理员">
				<el-radio v-model="dataForm.superAdmin" :label="0">否</el-radio>
				<el-radio v-model="dataForm.superAdmin" :label="1">是</el-radio>
			</el-form-item>
			<el-form-item prop="status" label="状态">
				<fast-radio-group v-model="dataForm.status" dict-type="user_status"></fast-radio-group>
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
import { useOrgListApi } from '@/api/sys/orgs'
import { useUserApi, useUserSubmitApi } from '@/api/sys/user'
import { useRoleListApi } from '@/api/sys/role'

const emit = defineEmits(['refreshDataList'])

const visible = ref(false)
const orgList = ref([])
// const orgList = ref<any[]>([])
const roleList = ref<any[]>([])
const dataFormRef = ref()

const dataForm = reactive({
	id: '',
	username: '',
	realName: '',
	password: '',
	orgId: '',
	orgName: '',
	roleId: '',
	avatar: null,
	gender: 0,
	superAdmin: 0,
	status: 1
})

const init = (id?: number) => {
	visible.value = true
	dataForm.id = ''

	// 重置表单数据
	if (dataFormRef.value) {
		dataFormRef.value.resetFields()
	}

	// id 存在则为修改
	if (id) {
		getUser(id)
	}

	getOrgList()
	getRoleList()
}

// 获取平台列表
const getOrgList = () => {
	return useOrgListApi().then(res => {
		orgList.value = res.data
	})
}

// 获取角色列表
const getRoleList = () => {
	return useRoleListApi().then(res => {
		roleList.value = res.data
	})
}

// 获取信息
const getUser = (id: number) => {
	useUserApi(id).then(res => {
		Object.assign(dataForm, res.data)
	})
}

const dataRules = ref({
	username: [{ required: true, message: '必填项不能为空', trigger: 'blur' }],
	realName: [{ required: true, message: '必填项不能为空', trigger: 'blur' }],
	password: [{ required: true, message: '必填项不能为空', trigger: 'blur' }],
	orgId: [{ required: true, message: '必填项不能为空', trigger: 'blur' }],
	roleId: [{ required: true, message: '必填项不能为空', trigger: 'blur' }],
	gender: [{ required: true, message: '必填项不能为空', trigger: 'blur' }],
	superAdmin: [{ required: true, message: '必填项不能为空', trigger: 'blur' }],
	status: [{ required: true, message: '必填项不能为空', trigger: 'blur' }]
})

// 表单提交
const submitHandle = () => {
	dataFormRef.value.validate((valid: boolean) => {
		if (!valid) {
			return false
		}

		console.log('用户管理提交的表单')
		console.log(dataForm)

		useUserSubmitApi(dataForm).then(() => {
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
