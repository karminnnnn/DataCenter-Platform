<!-- <template>
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
</template> -->

<template>
	<div class="login-container">
		<el-card class="login-card">
			<div class="flex-container">
				<div class="left">
					<img src="@/assets/logos.png" />
					<h1 class="login-title">                </h1>
					<h1 class="login-title">{{ $t('app.title') }}</h1>
				</div>
				<div class="right">
					<div class="login-form">
						<account v-if="loginType === 'account'"></account>
						<!--<mobile v-if="loginType === 'mobile'"></mobile>-->
						<register v-if="loginType === 'register'"></register>
						<div class="login-more">
							<el-button type="info" link @click="loginSwitch('account')">{{ $t('app.accountSignIn') }}</el-button>
							<el-button type="info" link @click="loginSwitch('register')">{{ '注册' }}</el-button>
						</div>
					</div>			
				</div>	
			</div>
		</el-card>
	</div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import Account from './account.vue'
import Mobile from './mobile.vue'
import Register from './register.vue'

// 登录类型
const loginType = ref('account')
const loginSwitch = (type: string) => {
	loginType.value = type
}
</script>

<style lang="scss" scoped>
// .login-container {
// 	display: flex;
// 	justify-content: space-evenly;
// 	align-items: center;
// 	height: 100vh;
// }

// mine
.login-container::before {
  content: "";
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: url('@/assets/school.png') no-repeat center center fixed;
  background-size: cover;
  -webkit-background-size: cover;
  opacity: 0.8; /* 设置透明度为0.5，根据需要调整 */
  z-index: -1; /* 确保伪元素在.login-container之下 */
}
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  // background-color: #a7bcab;
  position: relative;
}
.login-card {
  width: auto;
  border-radius: 6px;
  background-color: #24572c;
}
.login-title {
  text-align: center;
  margin-bottom: 20px;
  font-size: 60px;
  color: #fff;
}
.flex-container {
  display: flex;
  justify-content: space-between;
  // align-items: flex-start;
  // align-items: center; /* 修改为center以垂直居中 */
  width: 100%;
}
.left {
	width: 480px;
	flex: 1;
	margin-top: 50px;

	text-align: center; /* 水平居中图片 */
  	/* 使用Flexbox在垂直方向上居中图片 */
  	display: flex;
 	flex-direction: column;
 	align-items: center;
  	justify-content: center;
  	height: 100%; /* 根据需要设置高度 */
  	/* 其他样式 */
}
.left img {
  width: 300px; /* 设置图片宽度，根据实际情况调整 */
  height: auto; /* 高度自适应，保持图片比例 */
  /* 如果需要设置最大宽度，可以使用max-width */
  /* max-width: 100%; */
  /* 其他样式，如边框等 */
}
.right {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  /* 根据需要添加其他样式 */
}


.login-intro {
	display: flex;
	flex-direction: column;
	width: 520px;
	flex: 0 1 auto;
}
// .login-intro h1 {
// 	color: var(--el-color-primary);
// }
.login-intro h1 {
  color: #008000; /* 这是一个典型的绿色 */
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
