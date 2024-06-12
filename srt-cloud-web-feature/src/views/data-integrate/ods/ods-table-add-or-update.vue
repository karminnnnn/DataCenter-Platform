<template>
	<el-dialog v-model="visible" :title="!dataForm.datatableId ? '新增' : '修改'" :close-on-click-modal="false">
		<el-form ref="dataFormRef" :model="dataForm" :rules="dataRules" label-width="100px" @keyup.enter="submitHandle()">
			<el-form-item label="所属数据库" prop="databaseId">
				<el-select v-model="dataForm.databaseId" placeholder="请选择" :disabled="judge" @change="updateDatabaseName" style="width: 100%;">
					<el-option v-for="(item, index) in databaseList" :key="item.id" :label="`[${item.id}]${item.name}`" :value="item.id"></el-option>
				</el-select>
			</el-form-item>
			<el-form-item label="表名" prop="datatableName">
				<el-input v-model="dataForm.datatableName" placeholder="字段名称"></el-input>
			</el-form-item>
			<el-form-item label="注释" prop="remarks">
				<el-input type="textarea" :rows="2" v-model="dataForm.remarks" placeholder="注释"></el-input>
			</el-form-item>
		</el-form>
		<template #footer>
			<el-button @click="visible = false">取消</el-button>
			<el-button type="primary" @click="submitHandle()">确定</el-button>
		</template>
	</el-dialog>
</template>

<script setup lang="ts">
import { STATEMENT_OR_BLOCK_KEYS } from '@babel/types'
import { reactive, ref } from 'vue'
import { useOdsSubmitApi, useOdsApi } from '@/api/data-integrate/ods'
import databases from '@/views/data-development/production/databases.vue'
import { listDatabase } from '@/api/data-integrate/database'
import { ElMessage } from 'element-plus/es'

const emit = defineEmits(['refreshDataList'])

const visible = ref(false)
const dataFormRef = ref()

const judge = ref(false)
const dataForm = reactive({
	dataAccessId: null,
	databaseId: null,
	databaseName:'',
	datatableId: null,
	datatableName: '',
	projectId: null,
	remarks: '',
	recentlySyncTime: '',
})

//初始化
const init = (row?: any) => {
	visible.value = true

	// 重置表单数据
	if (dataFormRef.value) {
		dataFormRef.value.resetFields()
	}

	// 清空 dataForm 对象的所有字段
	Object.assign(dataForm, {
		dataAccessId: null,
		databaseId: null,
		databaseName:'',
		datatableId: null,
		datatableName: '',
		projectId: null,
		remarks: '',
		recentlySyncTime: '',
	})

	getDatabaseList()
	if (row) {
		// getProperty(row)
		getTableInfo(row.datatableId)
		judge.value = true
	}
}

// 修改数据表时获取信息
const getTableInfo = (id: number) => {
	useOdsApi(id).then(res => {
		Object.assign(dataForm, res.data)
		console.log("获取特定数据表信息")
		console.log(dataForm)
	})
}

//获取数据库列表
const databaseList = ref([])
const getDatabaseList = () => {
	return listDatabase().then(res => {
		databaseList.value = res.data
	})
}

// 更新 databaseName
const updateDatabaseName = () => {
  const databaseListArray = databaseList.value
  const selectedDatabase = databaseListArray.find(item => item.id === dataForm.databaseId)
  if (selectedDatabase) {
    dataForm.databaseName = selectedDatabase.name
  }
}

// 表单验证
const dataRules = ref({
	datatableName: [{ required: true, message: '必填项不能为空', trigger: 'blur' }]
})

// 表单提交
const submitHandle = () => {
	dataFormRef.value.validate((valid: boolean) => {
		if (!valid) {
			return false
		}

		console.log('新增修改数据表提交的表单')
		console.log(dataForm)

		useOdsSubmitApi(dataForm).then(() => {
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
