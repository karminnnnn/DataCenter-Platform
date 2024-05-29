<template>
	<el-card>
		<el-form ref="dataFormRef" :model="dataForm" label-width="200px" @keyup.enter="handleDataForm()">
			<el-form-item prop="username" :label="'用户'">
				<el-input v-model="dataForm.userName" placeholder="用户" disabled></el-input>
			</el-form-item>
			<el-form-item prop="realName" :label="'真名'">
				<el-input v-model="dataForm.realName" placeholder="真名"></el-input>
			</el-form-item>

			<el-form-item prop="avatar" :label="'头像'">
				<el-avatar src="dataForm.avatar"></el-avatar>
			</el-form-item>

			<el-form-item prop="gender" :label="'性别'">
				<el-radio-group v-model="dataForm.gender">
					<el-radio :label="0">男</el-radio>
					<el-radio :label="1">女</el-radio>
					<el-radio :label="2">未知</el-radio>
				</el-radio-group>
			</el-form-item>

			<el-form-item prop="org" :label="'组织'">
				<el-input v-model="dataForm.org" placeholder="组织" disabled></el-input>
			</el-form-item>

			<el-form-item prop="superAdmin" :label="'是否是超级管理员'">
				<el-radio-group v-model="dataForm.superAdmin">
					<el-radio disabled :label="0">否</el-radio>
					<el-radio disabled :label="1">是</el-radio>
				</el-radio-group>
			</el-form-item>

			<el-form-item prop="status" :label="'状态'">
				<el-radio-group v-model="dataForm.status">
					<el-radio disabled :label="0">停用</el-radio>
					<el-radio disabled :label="1">正常</el-radio>
				</el-radio-group>
			</el-form-item>

			<el-form-item>
				<el-button type="primary" @click="handleDataForm">{{ $t('confirm') }}</el-button>
			</el-form-item>
		</el-form>
	</el-card>
</template>

<script setup lang="ts" name="ProfilePassword">
import { onMounted, reactive, ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import store from '@/store'
import { useI18n } from 'vue-i18n'
// import { validatePassword } from '@/utils/validate'
import { updatePasswordApi } from '@/api/sys/user'
import { updateUserInfoApi, useUserInfoApi } from '@/api/sys/user'
import { ElMessage } from 'element-plus'
import { closeTab } from '@/utils/tabs'

const router = useRouter()
const route = useRoute()
const { t } = useI18n()
const dataFormRef: any = ref(null)

const dataForm = reactive({
	// password: '',
	// newPassword: '',
	// confirmPassword: ''
	NetID: '',
	userName: '',
	realName: '',
	gender: '',
	org: '',
	superAdmin: '',
	status: '',
	avatar: ''
})

// const dataRules = ref({
// 	password: [{ required: true, message: t('required'), trigger: 'blur' }],
// 	newPassword: [{ required: true, validator: validatePassword, trigger: 'blur' }],
// 	confirmPassword: [{ required: true, message: t('required'), trigger: 'blur' }]
// })

onMounted(async () => {
	try {
		// 调用API并获取数据
		const response = await useUserInfoApi()
		console.log(response)
		dataForm.NetID = response.data.id
		dataForm.userName = response.data.username
		dataForm.realName = response.data.realName
		dataForm.gender = response.data.gender
		console.log(dataForm.gender)
		dataForm.org = response.data.platformName
		dataForm.status = response.data.status
		dataForm.superAdmin = response.data.superAdmin
		dataForm.avatar = response.data.avatar
	} catch (error) {
		console.error('Failed to fetch user info:', error)
	}
})

const handleDataForm = () => {
	dataFormRef.value.validate((valid: boolean) => {
		if (!valid) {
			return false
		}

		// 修改密码
		updateUserInfoApi(dataForm).then(() => {
			ElMessage.success('修改成功')
			// 关闭当前tab
			closeTab(router, route)
		})
	})
}
</script>
