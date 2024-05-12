<template>
	<div class="login-container">
		<div class="login-intro">
			<h1>{{ $t('app.title') }}</h1>
			<div class="desc">
				{{ $t('app.description') }}
			</div>
			<div class="login-bg"><img src="@/assets/login.png" /></div>
		</div>
		<div class="login-form">
			<account v-if="loginType === 'account'"></account>
			<mobile v-if="loginType === 'mobile'"></mobile>
			<div class="login-more">
				<el-button type="info" link @click="loginSwitch('account')">{{ $t('app.accountSignIn') }}</el-button>
				<el-button type="info" link @click="loginSwitch('mobile')">{{ $t('app.mobileSignIn') }}</el-button>
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import Account from './account.vue'
import Mobile from './mobile.vue'

// 登录类型
const loginType = ref('account')
const loginSwitch = (type: string) => {
	loginType.value = type
}
</script>

<style lang="scss" scoped>
.login-container {
	display: flex;
	justify-content: space-evenly;
	align-items: center;
	height: 100vh;
}
.login-intro {
	display: flex;
	flex-direction: column;
	width: 520px;
	flex: 0 1 auto;
}
.login-intro h1 {
	color: var(--el-color-primary);
}
.login-intro .desc {
	color: rgb(113, 115, 112);
	line-height: 32px;
	padding: 15px 0;
}
.login-bg img {
	width: 520px;
}
.login-form {
	background-color: #fff;
	flex: 0 1 auto;
	padding: 40px;
	border-radius: 6px;
	box-shadow: 1px 1px 8px #aaa6a6;
	box-sizing: border-box;

	:deep(.el-input) {
		height: 45px;
		margin-top: 5px;
		.el-input__inner {
			padding: 10px 15px 10px 5px;
			height: 45px;
			line-height: 45px;
			color: #666;
			font-size: 16px;
		}
	}
}

.login-more {
	display: flex;
	justify-content: space-evenly;
	padding-top: 25px;
	width: 200px;
	margin: 0 auto;
}
@media only screen and (max-width: 992px) {
	.login-intro {
		display: none;
	}
}
@media only screen and (max-width: 768px) {
	.login-container {
		background: #fff;
	}
	.login-intro {
		display: none;
	}
	.login-form {
		flex: 0 1 auto;
		border-radius: 0;
		box-shadow: none;
	}
	.login-captcha {
		:deep(.el-input) {
			width: 150px;
		}
	}
}
</style>


这个页面是一个简单的登录页面，通过用户名密码登录和手机号登录两种方式，用户可以根据需要选择登录方式。

这段代码是一个登录页面的 Vue 组件，用于展示一个登录表单，包含了账号密码登录和手机号登录两种方式，并提供了切换登录方式的功能。以下是逐行的解释：

<!-- <template> 标签包裹了组件的模板部分，定义了登录页面的结构。
<div class="login-container"> 是登录页面的主要容器，包含了登录介绍和登录表单部分。
<div class="login-intro"> 包含了登录页面的标题、描述和背景图片。
<div class="login-form"> 包含了登录表单部分，根据 loginType 的值显示不同的登录方式。
<account v-if="loginType === 'account'"></account> 根据 loginType 的值，显示账号密码登录表单组件。
<mobile v-if="loginType === 'mobile'"></mobile> 根据 loginType 的值，显示手机号登录表单组件。
<div class="login-more"> 包含了切换登录方式的按钮组。
<el-button type="info" link @click="loginSwitch('account')">{{ $t('app.accountSignIn') }}</el-button> 切换到账号密码登录方式的按钮。
<el-button type="info" link @click="loginSwitch('mobile')">{{ $t('app.mobileSignIn') }}</el-button> 切换到手机号登录方式的按钮。
<script setup> 部分使用了 Vue 3 的 <script setup> 语法，定义了 loginType 变量和 loginSwitch 方法。
loginType 是一个响应式变量，用于控制当前显示的登录方式。
loginSwitch 方法用于切换登录方式，接受一个参数 type，根据参数值修改 loginType 的值。
<style lang="scss" scoped> 部分定义了组件的样式，包括整体布局和响应式布局。
这个页面主要用于用户登录，提供了账号密码登录和手机号登录两种方式，并提供了切换登录方式的功能，用户可以根据需要选择不同的登录方式进行登录。 -->





