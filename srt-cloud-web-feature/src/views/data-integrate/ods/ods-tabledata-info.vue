<template>
	<div>
		<el-form :inline="true" :model="state.queryForm" @keyup.enter="getDataList()">
			<el-form-item>
				<el-input v-model="state.queryForm.keyWord" placeholder="请输入查找关键词"></el-input>
			</el-form-item>
			<el-form-item>
				<el-button @click="getDataList()">查询</el-button>
			</el-form-item>
			<el-form-item>
				<el-button type="primary" @click="addOrUpdateHandle(null)">新增</el-button>
			</el-form-item>
		</el-form>
		<p>若超过50条，只显示前50条数据！</p>
		<el-table v-loading="state.dataListLoading" :data="state.dataList" style="margin-top: 20px; width: 90%" height="510" @selection-change="selectionChangeHandle">
			<el-table-column
				:show-overflow-tooltip="true"
				width="100px"
				:prop="item"
				:label="item"
				v-for="(item, index) in tableDataHeader"
				:key="index"
			>
			</el-table-column>
			<el-table-column label="操作" fixed="right" header-align="center" align="center" width="150">
				<template #default="scope">
					<el-button type="primary" link @click="addOrUpdateHandle(scope.row)">编辑</el-button>
					<el-button type="primary" link @click="deleteOdsTableData(scope.row)">删除</el-button>
				</template>
			</el-table-column>
		</el-table>
		<el-pagination
			:current-page="state.page"
			:page-sizes="state.pageSizes"
			:page-size="state.limit"
			:total="state.total"
			layout="total, sizes, prev, pager, next, jumper"
			@size-change="sizeChangeHandle"
			@current-change="currentChangeHandle"
		>
		</el-pagination>
	</div>

	<odsTableDataAddOrUpdate ref="addOrUpdateRef" @refreshDataList="getDataList()"></odsTableDataAddOrUpdate>
</template>

<script setup lang="ts" name="OdsTableDataInfo">
import { useCrud } from '@/hooks'
import { reactive, ref, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus/es'
import { IHooksOptions } from '@/hooks/interface'
import { useOdsTableDataHeaderInfoApi, deleteOdsTableDataApi } from '@/api/data-integrate/ods'
import odsTableDataAddOrUpdate from './ods-tabledata-add-or-update.vue'

const state: IHooksOptions = reactive({
	createdIsNeed: false,
	dataListUrl: '/data-integrate/ods/tabledata/page',
	deleteUrl: '',
	queryForm: {
		datatableId:'',
		keyWord: ''
	}
})

const dataForm = reactive({
	datatableId: null,
	rows:[{}]
})

const tableDataHeader = ref({})
const init = (datatableId?: any) => {
	// 清零数据
	state.queryForm.datatableId = ''
	tableDataHeader.value = {};
	// 重置dataform数据 dataform数据清零
	Object.keys(dataForm).forEach(key => {
    if (!['datatableId', 'rows'].includes(key)) {
			delete dataForm[key]
		}
	})
	dataForm.rows = [{}]

	// console.log("————看看datatableId")
	// console.log(state.queryForm.datatableId)
	// console.log("————看看表头数据tableDataHeader")
	// console.log(tableDataHeader.value)

	state.queryForm.datatableId = datatableId
	gettablecolumns(datatableId)	
	getDataList()

	// console.log("看看动态表单的row")
	// console.log(dataForm)
	// console.log(JSON.stringify(dataForm))
	// console.log("看看datatableId")
	// console.log(state.queryForm.datatableId)
	// console.log("看看表头数据tableDataHeader")
	// console.log(tableDataHeader.value)
}

// 获取表头数据
const gettablecolumns = (datatableId: number) => {
	return useOdsTableDataHeaderInfoApi(datatableId).then(res => {
		tableDataHeader.value = res.data
		// console.log("看看表头数据")
		// console.log(tableDataHeader.value)
	})
}

const deleteOdsTableData = (row: any) => {
	dataForm.rows = [row]
	dataForm.datatableId = state.queryForm.datatableId

	// console.log("看看动态表单的row")
	// console.log(dataForm)
	// console.log(JSON.stringify(dataForm))

	ElMessageBox.confirm('确定进行删除操作?', '提示', {
		confirmButtonText: '确定',
		cancelButtonText: '取消',
		type: 'warning'
	})
	.then(() => {
		deleteOdsTableDataApi(dataForm).then(() => {
			ElMessage.success({
				message: '操作成功',
				duration: 500,
				onClose: () => {
					state.dataListLoading = true
					setTimeout(() => {
						getDataList()
					}, 1000);
				}
			})
		})
	})
	.catch(() => {})	
}


const addOrUpdateRef = ref()
const addOrUpdateHandle = (row?: any) => {
	addOrUpdateRef.value.init(tableDataHeader.value, state.queryForm.datatableId, row)
}

const { getDataList, selectionChangeHandle, sizeChangeHandle, currentChangeHandle, deleteBatchHandle } = useCrud(state)

defineExpose({
	init
})
</script>
