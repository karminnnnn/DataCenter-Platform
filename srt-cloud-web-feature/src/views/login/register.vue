<template>
	<el-form ref="registerFormRef" :model="registerForm" :rules="registerRules" @keyup.enter="onRegister">
		<!--<div class="login-title">{{ $t('app.signIn') }}</div>-->
		<el-form-item prop="username">
			<el-input v-model="registerForm.username" :prefix-icon="User" :placeholder="'学号/工号'"></el-input>
		</el-form-item>
		<el-form-item prop="password">
			<el-input v-model="registerForm.password" :prefix-icon="Lock" show-password :placeholder="$t('app.password')"></el-input>
		</el-form-item>
		<el-form-item prop="confirmPassword">
			<el-input v-model="registerForm.confirmPassword" :prefix-icon="Unlock" show-password :placeholder="'确认' + $t('app.password')"></el-input>
		</el-form-item>
		<el-form-item prop="captcha" class="login-captcha">
			<el-input v-model="registerForm.captcha" :placeholder="$t('app.captcha')" :prefix-icon="Key"></el-input>
			<!-- <img :src="captchaBase64" @click="onCaptcha" /> -->
		</el-form-item>
		<el-form-item class="login-button">
			<el-button type="success" @click="onRegister()">{{ '注册' }}</el-button>
		</el-form-item>
	</el-form>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { User, Lock, Key, Unlock } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import store from '@/store'
import { useCaptchaApi } from '@/api/auth'
import { useRouter } from 'vue-router'
import { useI18n } from 'vue-i18n'

const router = useRouter()
const { t } = useI18n()
const registerFormRef = ref()
const captchaBase64 = ref()

const registerForm = reactive({
	username: '',
	password: '',
	confirmPassword: '',
	key: '',
	captcha: ''
})

const registerRules = ref({
	username: [{ required: true, message: t('required'), trigger: 'blur' }],
	password: [{ required: true, message: t('required'), trigger: 'blur' }],
	confrimPassword: [{ required: true, message: t('required'), trigger: 'blur' }],
	captcha: [{ required: true, message: t('required'), trigger: 'blur' }]
})

onMounted(() => {
	// onCaptcha()
})

const onCaptcha = async () => {
	const { data } = await useCaptchaApi()
	registerForm.key = data.key
	captchaBase64.value = data.image
}

const onRegister = () => {
	console.log(registerForm.password)
	console.log(registerForm.confirmPassword)
	if (registerForm.password != registerForm.confirmPassword) {
		ElMessage.error('两次密码输入不一致！')
		return false
	}
	registerFormRef.value.validate((valid: boolean) => {
		if (!valid) {
			return false
		}

		// 用户注册
		store.userStore
			.accountRegisterAction(registerForm)
			.then(() => {
				console.log('Good!!!!')
				ElMessage({
					message: '注册成功！',
					type: 'success'
				})
				router.push({ path: '/home' })
			})
			.catch(() => {
				console.log('why????')
				// onCaptcha()
			})
	})

	// TODO
}
</script>

<style lang="scss" scoped>
.login-title {
	display: flex;
	justify-content: center;
	margin-bottom: 35px;
	font-size: 24px;
	color: #444;
	letter-spacing: 4px;
}
.login-captcha {
	:deep(.el-input) {
		width: 350px;
	}
}
.login-captcha img {
	width: 150px;
	height: 40px;
	margin: 5px 0 0 10px;
	cursor: pointer;
}
.login-button {
	:deep(.el-button--success) {
		margin-top: 10px;
		width: 100%;
		height: 45px;
		font-size: 18px;
		letter-spacing: 8px;
	}
}
</style>
