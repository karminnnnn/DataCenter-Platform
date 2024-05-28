<template>
	<div>
		<el-button type="primary" round @click="runSql()">执行sql</el-button>
		<br />
		<el-input style="margin-top: 20px; width: 100%" v-model="sql" :rows="2" type="textarea" placeholder="请输入sql语句" />
		<p style="margin-top: 20px">若超过100条，只显示前100条数据！</p>
		<el-table :data="state.sqlData" style="margin-left: 50px; margin-top: 20px; width: 90%">
			<el-table-column
				:show-overflow-tooltip="true"
				width="100px"
				:prop="index"
				:label="item"
				v-for="(item, index) in state.sqlDataHeader"
				:key="index"
			>
			</el-table-column>
		</el-table>
	</div>
</template>

<script setup lang="ts" name="odsSql">
import { reactive, ref } from 'vue'
import { IHooksOptions } from '@/hooks/interface'
import { ElMessage, ElMessageBox } from 'element-plus/es'
import { getTableDataBySql } from '@/api/data-integrate/database'

const state: IHooksOptions = reactive({
	deleteUrl: '',
	projectId: '',
	databaseId: '',
	sqlDataHeader: {},
	sqlData: []
})

const init = (projectId: number) => {
	state.projectId = projectId
}

// sql语句执行
const sql = ref('')
const runSql = () => {
	if (!sql.value.trim()) {
		ElMessage.error('请输入sql')
		return
	}
	getTableDataBySql(state.databaseId, { sql: sql.value }).then(res => {
		state.sqlDataHeader = res.data.columns
		state.sqlData = res.data.rows
	})
}

defineExpose({
	init
})
</script>
