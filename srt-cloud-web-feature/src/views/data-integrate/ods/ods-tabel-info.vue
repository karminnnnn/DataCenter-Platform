<template>
	<el-tag size="large" effect="dark" round style="margin-bottom: 15px"> 表属性 </el-tag>
	<el-button type="primary" style="margin-left: 15px" @click="addOrUpdateTable(column_state.currentRow)">修改</el-button>
	<el-descriptions :column="1" direction="horizontal" style="blockMargin">
		<el-descriptions-item label="表名">{{ column_state.currentRow.datatableName }}</el-descriptions-item>
		<el-descriptions-item label="注释">{{ column_state.currentRow.remarks }}</el-descriptions-item>
		<el-descriptions-item label="数据接入任务"><el-button type="primary" link @click="getAccessInfoHandler()">查看</el-button></el-descriptions-item>
		<el-descriptions-item label="最近同步时间">{{
			column_state.currentRow.recentlySyncTime ? column_state.currentRow.recentlySyncTime : '暂无'
		}}</el-descriptions-item>
	</el-descriptions>
	<br />
	<el-tag size="large" effect="dark" round style="margin-bottom: 15px">字段信息</el-tag>
	<el-form :inline="true" :model="column_state.queryForm" @keyup.enter="column_usecrud.getDataList()">
		<el-form-item>
			<el-input v-model="column_state.queryForm.fieldName" placeholder="属性名称"></el-input>
		</el-form-item>
		<el-form-item>
			<el-button @click="column_usecrud.getDataList()">查询</el-button>
		</el-form-item>
		<el-form-item>
			<el-button type="primary" @click="addOrUpdateColumn(column_state.queryForm.datatableId, null)">新增</el-button>
		</el-form-item>
	</el-form>
	<el-table v-loading="column_state.dataListLoading" :data="column_state.dataList" height="355" style="width: 100%" @selection-change="column_usecrud.selectionChangeHandle">
		<el-table-column prop="fieldName" label="名称" header-align="center" align="center" />
		<el-table-column prop="remarks" label="注释" header-align="center" align="center" />
		<el-table-column prop="fieldTypeName" label="类型" header-align="center" align="center" />
		<el-table-column prop="displaySize" label="长度" header-align="center" align="center" />
		<el-table-column prop="scaleSize" label="小数位数" header-align="center" align="center" />
		<el-table-column prop="defaultValue" label="默认值" header-align="center" align="center" />
		<el-table-column prop="pk" label="是否主键" header-align="center" align="center" />
		<el-table-column prop="autoIncrement" label="是否递增" header-align="center" align="center" />
		<el-table-column prop="nullable" label="是否可为空" header-align="center" align="center" />
		<el-table-column label="操作" fixed="right" header-align="center" align="center" width="150">
			<template #default="scope">
				<el-button type="primary" link @click="addOrUpdateColumn(column_state.queryForm.datatableId, scope.row)">编辑</el-button>
				<el-button type="primary" link @click="deleteColumn(column_state.queryForm.datatableId, scope.row.fieldName)">删除</el-button>
			</template>
		</el-table-column>
	</el-table>
	<el-pagination
		:current-page="column_state.page"
		:page-sizes="column_state.pageSizes"
		:page-size="column_state.limit"
		:total="column_state.total"
		layout="total, sizes, prev, pager, next, jumper"
		@size-change="column_usecrud.sizeChangeHandle"
		@current-change="column_usecrud.currentChangeHandle"
	>
	</el-pagination>

	<!-- 数据接入 详情 -->
	<info ref="infoRef"></info>

	<!-- 弹窗, 表字段新增 / 修改 -->
	<odsColumnAddOrUpdate ref="odsColumnAddOrUpdateRef" @refreshDataList="column_usecrud.getDataList()"></odsColumnAddOrUpdate>

	<!-- 弹窗, 表新增 / 修改 -->
	<odsTableAddOrUpdate ref="odsTableAddOrUpdateRef" @refreshDataList="column_usecrud.getDataList()"></odsTableAddOrUpdate>
</template>

<script setup lang="ts" name="odsTableInfoIndex">
import { useCrud } from '@/hooks'
import { reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus/es'
import { IHooksOptions } from '@/hooks/interface'
import { useAccessApi } from '@/api/data-integrate/access'
import info from '../access/info.vue'
import odsColumnAddOrUpdate from './ods-tablecolumn-add-or-update.vue'
import odsTableAddOrUpdate from './ods-table-add-or-update.vue'
import { ro } from 'element-plus/es/locale'
import { deleteOdsColumnInfoApi } from '@/api/data-integrate/ods';

const column_state: IHooksOptions = reactive({
	dataListUrl: '/data-integrate/ods/column-info/page',
	deleteUrl: '/data-integrate/ods/column-info',
	queryForm: {
		datatableId: '',
		fieldName: ''
	},
	currentRow: {},
})

const init = (currentRow?: any) => {
	column_state.currentRow = currentRow
	column_state.queryForm.datatableId = currentRow.datatableId
	// column_state.tableColumns = tableColumns
	// console.log('看看有没有datatableId')
	// console.log(column_state.queryForm.datatableId)
	column_usecrud.getDataList()
}

const infoRef = ref()
/* 查看表的数据接入任务 */
const getAccessInfoHandler = () => {
	if (!column_state.currentRow.dataAccessId) {
		ElMessage({
			message: '没有查询到该表对应的数据接入任务',
			type: 'warning'
		})
		return
	}
	useAccessApi(column_state.currentRow.dataAccessId).then(res => {
		if (!res.data) {
			ElMessage({
				message: '该任务已被删除，若要恢复，请联系管理员处理',
				type: 'warning'
			})
			return
		}
		infoRef.value.init(column_state.currentRow.dataAccessId)
	})
}

// 新增修改
const odsColumnAddOrUpdateRef = ref()
const addOrUpdateColumn = (datatableId:any, row?: any) => {
	odsColumnAddOrUpdateRef.value.init(datatableId, row)
	// console.log('看看有没有datatableId')
	// console.log(column_state.queryForm.datatableId)
}

// 删除
const deleteColumn = (datatableId: number, fieldName: any) => {
	// console.log("看看删除接口参数对不对")
	// console.log(datatableId)
	// console.log(fieldName)
	ElMessageBox.confirm('确定进行删除操作?', '提示', {
		confirmButtonText: '确定',
		cancelButtonText: '取消',
		type: 'warning'
	})
	.then(() => {
		deleteOdsColumnInfoApi(datatableId, fieldName).then(() => {
			ElMessage.success({
				message: '操作成功',
				duration: 500,
			})
			setTimeout(() => {
				column_usecrud.getDataList()
			}, 1000);
		})
	})
	.catch(() => {})
}

const odsTableAddOrUpdateRef = ref()
const addOrUpdateTable = (row?: any) => {
	odsTableAddOrUpdateRef.value.init(row)
}

defineExpose({
	init
})

const column_usecrud = useCrud(column_state)
// const { getDataList, selectionChangeHandle, sizeChangeHandle, currentChangeHandle, deleteBatchHandle } = useCrud(state)
</script>
