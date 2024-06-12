<template>
	<div class="common-layout">
		<el-container>
			<!-- 左侧 -->
			<el-aside width="30%">
				<el-card body-style="height: calc(100vh - 170px )">
					<el-form :inline="true" :model="state.queryForm" @keyup.enter="getDataList()">
						<el-form-item size="small">
							<el-input v-model="state.queryForm.datatableName" placeholder="表名"></el-input>
						</el-form-item>
						<el-form-item size="small">
							<el-input v-model="state.queryForm.remarks" placeholder="注释"></el-input>
						</el-form-item>
						<el-form-item size="small">
							<el-button type="primary" @click="getDataList()">查询</el-button>
						</el-form-item>
						<el-form-item size="small">
							<el-button type="primary" @click="addOrUpdateTable()">新增</el-button>
						</el-form-item>
						<el-form-item size="small">
							<el-button type="danger" @click="deleteBatchHandle()">删除</el-button>
						</el-form-item>
					</el-form>
					<el-table
						v-loading="state.dataListLoading"
						:data="state.dataList"
						border
						style="width: 100%"
						max-height="calc(100vh - 400px )"
						highlight-current-row
						@selection-change="selectionChangeHandle"
						@current-change="handleCurrentChange"
					>
						<!-- <fast-table-project-column prop="projectId" label="所属项目" header-align="center" align="center"></fast-table-project-column> -->
						<el-table-column type="selection" header-align="center" align="center" width="50"></el-table-column>
						<el-table-column prop="databaseName" label="所属数据库" header-align="center" align="center"></el-table-column>
						<el-table-column prop="datatableName" label="表名" header-align="center" align="center"></el-table-column>
						<el-table-column prop="remarks" label="注释" header-align="center" align="center"></el-table-column>
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

					<!-- <div>
						<el-scrollbar style="height: calc(100vh - 220px )">
							<div style="height: 100%;" class="DataTableTreeDiv">
								<div>
									<el-input v-model="filterNodeText" placeholder="search" />
									<br><br>
								</div>
								<div>
									<el-button type="primary" @click="createTable">建表</el-button><br><br>
								</div>
								<el-tree
								ref="treeRef"
								:data="treeList"
								@node-contextmenu="ckRightOption"
								@node-click="treeNodeCk"
								default-expand-all
								node-key="id"
								:filter-node-method="filterNode"
								>
									<template #default="{ node, data }">
										<div class="dataTable-tree-node">
											<span>
												layer
												<img v-if="data.type==2" src="/src/assets/database.png"/>
												table
												<img v-if="data.type==3" src="/src/assets/table.png"/>
												<span style="margin-left: 8px;">{{ data.name }}&emsp;{{ data.description }}</span>
											</span>
										</div>
									</template>
								</el-tree>
							</div>
						</el-scrollbar>
					</div> -->
				</el-card>
			</el-aside>
			<!-- 右侧主区域 -->
			<el-container>
				<el-header>
					<el-card body-style="height: calc(100vh - 170px )">
						<el-tabs v-model="activeName" class="demo-tabs" @tab-change="handleClick" :before-leave="tabBeforeLeave">
							<el-tab-pane label="表信息" name="tableInfo">
								<!-- 弹窗 同步结果 -->
								<ods-table-info ref="odsTableInfoRef"></ods-table-info>
							</el-tab-pane>
							<el-tab-pane label="数据预览" name="tableData">
								<ods-tabledata-info ref="odsTableDataInfoRef"></ods-tabledata-info>
							</el-tab-pane>
							<el-tab-pane label="接入日志" name="tableLog">
								<!-- 弹窗 同步结果 -->
								<ods-task-detail ref="odsTaskDetailRef"></ods-task-detail>
							</el-tab-pane>
							<el-tab-pane label="sql执行" name="tableSql">
								<ods-sql ref="odsSqlRef"></ods-sql>
							</el-tab-pane>
						</el-tabs>
					</el-card>
				</el-header>
			</el-container>
		</el-container>
	</div>

	<!-- 弹窗, 表新增 / 修改 -->
	<odsTableAddOrUpdate ref="odsTableAddOrUpdateRef" @refreshDataList="getDataList()"></odsTableAddOrUpdate>
</template>

<script setup lang="ts" name="Data-integrateOdsIndex">
import { useCrud } from '@/hooks'
import { ElMessage, ElMessageBox } from 'element-plus/es'
import { reactive, ref, onMounted, watch } from 'vue'
import { IHooksOptions } from '@/hooks/interface'
import { getColumnInfoApi, getTableDataApi } from '@/api/data-integrate/ods'
// import { useAccessApi } from '@/api/data-integrate/access'
import { getTableDataBySql } from '@/api/data-integrate/database'
// import { listMiddleDbTreeApi,getTableInfoApi} from '@/api/data-integrate/database'
import OdsTaskDetail from './ods-task-detail.vue'
import OdsTableInfo from './ods-tabel-info.vue'
import OdsSql from './ods-sql.vue'
import odsTabledataInfo from './ods-tabledata-info.vue'
import odsTableAddOrUpdate from './ods-table-add-or-update.vue'
import { ro } from 'element-plus/es/locale'

const state: IHooksOptions = reactive({
	dataListUrl: '/data-integrate/ods/page',
	deleteUrl: '/data-integrate/ods',
	primaryKey: 'datatableId',
	queryForm: {
		datatableName: '',
		remarks: '',
	},
	currentRow: {},
	tableColumns: [],
	tableData: [],
	tableDataHeader: {}
})

// // 左侧的树形结构
// onMounted(()=>{
// 	getTreeList()
// })
// const treeRef = ref()
// const filterNodeText = ref('')
// const treeList = ref([])
// /**
//  * 获取数仓树
//  */
// const getTreeList = () => {
// 	listMiddleDbTreeApi().then(res=>{
// 		treeList.value = res.data[0].children
// 	})
// }

// watch(filterNodeText, (val) => {
// 	treeRef.value!.filter(val)
// })

// /**
//  * 节点筛选
//  */
// const filterNode = (value: string, data: Tree) => {
// 	if (!value) return true
// 	return data.label.includes(value) || data.label.includes(value.toUpperCase()) || data.label.includes(value.toLowerCase())
// }

// const infoView = ref(false)
// const currentNode = ref({})
// const treeNodeCk = (e, data, n, t) => {
// 	//关闭右键菜单
// 	// OptionCardClose()
// 	//如果是table
// 	if(data.data.type == 3) {

// 		activeName.value = 'tableInfo'
// 		// state.currentRow = row
// 		currentNode.value = data.data
// 		state.tableColumns = []
// 		state.tableData = []
// 		state.tableDataHeader = {}
// 		getColumnInfoApi(data.data.tableName).then(res => {
// 			state.tableColumns = res.data
// 		})

// 		// console.log(data.data)
// 		// infoView.value = true

// 		// tableInfoRef.value.resetFields()
// 		// columns.value = []
// 		//获取表和字段信息
// 		// getTableInfoApi(data.data.name).then(res => {
// 		// 	if(res.data) {
// 		// 		Object.assign(tableInfo, res.data)
// 		// 		tableInfo.tableName = data.data.name
// 		// 		tableInfo.tableCn = data.data.description
// 		// 		columns.value = res.data.columns
// 		// 		isDetail.value = true
// 		// 	} else {
// 		// 		isDetail.value = false
// 		// 	}
// 		// })

// 	} else {
// 		currentNode.value = data.data
// 		// infoView.value = false
// 		//置空模型信息
// 		// tableInfoRef.value.resetFields()
// 		// columns.value = []
// 	}
// }

// 左侧
const activeName = ref('')
const tabBeforeLeave = () => {
	if (!state.currentRow.datatableName) {
		ElMessage({
			message: '请在左侧选择要查看的表',
			type: 'warning'
		})
		return false
	}
}

const odsTableAddOrUpdateRef = ref()
const addOrUpdateTable = (row?: any) => {
	odsTableAddOrUpdateRef.value.init()
}

// 右侧
const odsTableInfoRef = ref()
const odsTaskDetailRef = ref()
const odsSqlRef = ref()
const odsTableDataInfoRef = ref()
/* tab切换 */
const handleClick = (name: TabPaneName) => {
	if (name == 'tableData') {
		odsTableDataInfoRef.value.init(state.currentRow.datatableId)
		//查询选中的表数据
		// getTableDataApi(state.currentRow.tableName).then(res => {
		// 	state.tableDataHeader = res.data.columns
		// 	state.tableData = res.data.rows
		// 	odsTableDataInfoRef.value.init(state.tableDataHeader, state.tableData)
		// })
	} else if (name == 'tableLog') {
		odsTaskDetailRef.value.init(state.currentRow.projectId, state.currentRow.datatableName)
	} else if (name == 'tableSql') {
		odsSqlRef.value.init(state.currentRow.projectId, state.currentRow.databaseId)
	}
}

/* 表切换 */
const handleCurrentChange = row => {
	if (!row) {
		return
	}
	activeName.value = 'tableInfo'
	state.currentRow = row
	state.tableColumns = []
	state.tableData = []
	state.tableDataHeader = {}
	// console.log("查看分页返回的信息")
	// console.log(row)
	odsTableInfoRef.value.init(state.currentRow)
	// getColumnInfoApi(row.datatableName).then(res => {
	// 	state.tableColumns = res.data
	// 	odsTableInfoRef.value.init(state.currentRow)
	// })
}

const { getDataList, selectionChangeHandle, sizeChangeHandle, currentChangeHandle, deleteBatchHandle } = useCrud(state)
</script>

<style>
.demo-tabs > .el-tabs__content {
	padding: 18px;
	color: #6b778c;
}
</style>
