<template>
	<el-card>
		<div class="databaseDivClass">
			<el-form :inline="true" :model="datasource_state.queryForm" @keyup.enter="datasource_useCrud.getDataList()">
				<el-form-item>
				  <el-input v-model="datasource_state.queryForm.name" placeholder="名称"></el-input>
				</el-form-item>
				<el-form-item>
				  <fast-select v-model="datasource_state.queryForm.databaseType" dict-type="database_type" placeholder="数据库类型" clearable></fast-select>
				</el-form-item>
				<el-form-item>
				  <el-input v-model="datasource_state.queryForm.databaseName" placeholder="库名(服务名)"></el-input>
				</el-form-item>
				<!-- <el-form-item>
				  <el-input v-model="datasource_state.queryForm.databaseSchema" placeholder="schema"></el-input>
				</el-form-item>
				<el-form-item>
					<fast-select v-model="datasource_state.queryForm.status" dict-type="database_status" placeholder="状态" clearable></fast-select>
				</el-form-item> -->
				<!-- <el-form-item>
					<fast-select v-model="state.queryForm.isRtApprove" dict-type="yes_or_no" placeholder="是否支持实时接入" clearable></fast-select>
				</el-form-item> -->
				<!-- <el-form-item>
					<fast-project-select v-model="state.queryForm.projectId" placeholder="所属项目" clearable></fast-project-select>
				</el-form-item> -->
				<el-form-item>
					<el-button @click="datasource_useCrud.getDataList()">查询</el-button>
				</el-form-item>

				<!-- v-auth="'data-integrate:database:save'" -->
				<el-form-item>
					<el-button type="primary" @click="addOrUpdateHandle_datasource()">新增</el-button>
				</el-form-item>
				<!-- v-auth="'data-integrate:database:delete'" -->
				<el-form-item>
					<el-button type="danger" @click="datasource_state.deleteBatchHandle()">删除</el-button>
				</el-form-item>
				<!---->

			</el-form>
			<el-table v-loading="datasource_state.dataListLoading" :data="datasource_state.dataList" border style="width: 100%" max-height="calc(100vh - 400px )" @selection-change="datasource_useCrud.selectionChangeHandle">
				<el-table-column type="selection" header-align="center" align="center" width="50"></el-table-column>
				<el-table-column prop="name" label="名称" header-align="center" align="center" show-overflow-tooltip></el-table-column>
				<fast-table-column prop="databaseType" label="数据库类型" dict-type="database_type"></fast-table-column>
				<el-table-column prop="databaseIp" label="数据源IP" header-align="center" align="center" show-overflow-tooltip></el-table-column>
				<el-table-column prop="databasePort" label="数据源端口" header-align="center" align="center"></el-table-column>
				<el-table-column prop="userName" label="数据源用户名" header-align="center" align="center" show-overflow-tooltip></el-table-column>
				<el-table-column prop="password" label="数据源密码" header-align="center" align="center" show-overflow-tooltip></el-table-column>
				<fast-table-org-column prop="orgId" label="所属平台" header-align="center" align="center"></fast-table-org-column>
				<fast-creator-column prop="creator" label="创建者" header-align="center" align="center"></fast-creator-column>
				<el-table-column prop="createTime" label="创建时间" header-align="center" align="center" width="160" show-overflow-tooltip></el-table-column>
				
				<el-table-column label="操作" fixed="right" header-align="center" align="center" width="200">
					<template #default="scope">
						<!-- v-auth="'data-integrate:database:update'" -->
						<el-button type="primary" link @click="addOrUpdateHandle_datasource(scope.row.id)">编辑</el-button>
						<el-button type="primary" link @click="database_tables(scope.row.id)">数据库</el-button>
						<el-button type="primary" link @click="test_datasource(scope.row)">测试</el-button>
						<!-- v-auth="'data-integrate:database:delete'" -->
						<el-button type="danger" link @click="datasource_useCrud.deleteBatchHandle(scope.row.id)">删除</el-button>
					</template>
				</el-table-column>
			</el-table>
			<el-pagination
				:current-page="datasource_state.page"
				:page-sizes="datasource_state.pageSizes"
				:page-size="datasource_state.limit"
				:total="datasource_state.total"
				layout="total, sizes, prev, pager, next, jumper"
				@size-change="datasource_useCrud.sizeChangeHandle"
				@current-change="datasource_useCrud.currentChangeHandle"
			>
			</el-pagination>
			

			<!--数据库-->
			<div class="drawerClass" style="height:100%">
				<el-drawer
				    v-model="database_state.database_drawer"
						size="100%"
						:before-close="databasedrawerClose"
				    :direction="database_state.direction"
				  >
				    <template #header>
      					<h2>查看数据库</h2>
    				</template>
					<!-- Mine -->
					<el-form :inline="true" :model="database_state.queryForm" @keyup.enter="database_useCrud.getDataList()">
						<el-form-item>
						  <el-input v-model="database_state.queryForm.name" placeholder="数据库名称"></el-input>
						</el-form-item>
						<el-form-item>
							<fast-select v-model="database_state.queryForm.status" dict-type="database_status" placeholder="状态" clearable></fast-select>
						</el-form-item>
						<!-- <el-form-item>
							<fast-select v-model="state.queryForm.isRtApprove" dict-type="yes_or_no" placeholder="是否支持实时接入" clearable></fast-select>
						</el-form-item> -->
						<!-- <el-form-item>
							<fast-project-select v-model="state.queryForm.projectId" placeholder="所属项目" clearable></fast-project-select>
						</el-form-item> -->
						<el-form-item>
						  <el-input v-model="database_state.queryForm.creatorName" placeholder="创建者名称"></el-input>
						</el-form-item>
						<el-form-item>
							<el-button @click="database_useCrud.getDataList()">查询</el-button>
						</el-form-item>
						<el-form-item>
							<!-- v-auth="'data-integrate:database:save'" -->
							<el-button type="primary" @click="addOrUpdateHandle_database()">新增</el-button>
						</el-form-item>
						<el-form-item>
							<!-- v-auth="'data-integrate:database:delete'" -->
							<el-button type="danger" @click="database_useCrud.deleteBatchHandle()">删除</el-button>
						</el-form-item>
					</el-form>
					<el-table v-loading="database_state.dataListLoading" :data="database_state.dataList" border style="width: 100%" max-height="calc(100vh - 400px )" @selection-change="database_useCrud.selectionChangeHandle">
						<el-table-column type="selection" header-align="center" align="center" width="50"></el-table-column>
						<el-table-column prop="databaseName" label="数据库名称" header-align="center" align="center" show-overflow-tooltip></el-table-column>
						<fast-table-column prop="status" label="状态" dict-type="database_status"></fast-table-column>
						<el-table-column prop="synStatus" label="同步状态" header-align="center" align="center" show-overflow-tooltip></el-table-column>
						<el-table-column prop="version" label="版本" header-align="center" align="center" show-overflow-tooltip></el-table-column>
						<el-table-column prop="deleted" label="是否删除" header-align="center" align="center" show-overflow-tooltip></el-table-column>
						<fast-creator-column prop="creatorName" label="创建者" header-align="center" align="center"></fast-creator-column>
						<el-table-column prop="createTime" label="创建时间" header-align="center" align="center" width="160" show-overflow-tooltip></el-table-column>
						<!-- <fast-creator-column prop="updater_name" label="更新者" header-align="center" align="center"></fast-creator-column>
						<el-table-column prop="update_time" label="更新时间" header-align="center" align="center" width="160" show-overflow-tooltip></el-table-column> -->
						
						<el-table-column label="操作" fixed="right" header-align="center" align="center" width="200">
							<template #default="scope">
								<!-- v-auth="'data-integrate:database:update'" -->
								<el-button type="primary" link @click="addOrUpdateHandle_database(scope.row.id)">编辑</el-button>
								<!-- <el-button type="primary" link @click="datatable_tables(scope.row.id)">数据库表</el-button> -->
								<el-button type="primary" link @click="test_database(scope.row)">测试</el-button>
								<!-- v-auth="'data-integrate:database:delete'" -->
								<el-button type="danger" link @click="database_useCrud.deleteBatchHandle(scope.row.id)">删除</el-button>
							</template>
						</el-table-column>
					</el-table>
					<el-pagination
						:current-page="database_state.page"
						:page-sizes="database_state.pageSizes"
						:page-size="database_state.limit"
						:total="database_state.total"
						layout="total, sizes, prev, pager, next, jumper"
						@size-change="database_useCrud.sizeChangeHandle"
						@current-change="database_useCrud.currentChangeHandle"
					>
					</el-pagination>
					<!-- Mine -->
				</el-drawer>
			</div>


			<!-- 数据库表 -->
			<!-- Mine -->
			<div class="drawerClass" style="height:100%">
				<el-drawer
				    v-model="datatable_state.datatable_drawer"
						size="100%"
						:before-close="datatabledrawerClose"
				    :direction="datatable_state.direction"
				  >
				  	<template #header>
      					<h2>查看数据库表</h2>
    				</template>
					<el-form :inline="true" :model="datatable_state.queryForm" @keyup.enter="datatable_useCrud.getDataList()">
								<el-form-item>
						  <el-input v-model="datatable_state.queryForm.name" placeholder="名称"></el-input>
						</el-form-item>
						<el-form-item>
						  <fast-select v-model="datatable_state.queryForm.databaseType" dict-type="database_type" placeholder="数据库类型" clearable></fast-select>
						</el-form-item>
						<el-form-item>
						  <el-input v-model="datatable_state.queryForm.databaseName" placeholder="库名(服务名)"></el-input>
						</el-form-item>
						<el-form-item>
						  <el-input v-model="datatable_state.queryForm.databaseSchema" placeholder="schema"></el-input>
						</el-form-item>
						<el-form-item>
							<fast-select v-model="datatable_state.queryForm.status" dict-type="database_status" placeholder="状态" clearable></fast-select>
						</el-form-item>
						<!-- <el-form-item>
							<fast-select v-model="state.queryForm.isRtApprove" dict-type="yes_or_no" placeholder="是否支持实时接入" clearable></fast-select>
						</el-form-item> -->
						<!-- <el-form-item>
							<fast-project-select v-model="state.queryForm.projectId" placeholder="所属项目" clearable></fast-project-select>
						</el-form-item> -->
						<el-form-item>
							<el-button @click="datatable_useCrud.getDataList()">查询</el-button>
						</el-form-item>
						<el-form-item>
							<el-button v-auth="'data-integrate:database:save'" type="primary" @click="addOrUpdateHandle()">新增</el-button>
						</el-form-item>
						<el-form-item>
							<el-button v-auth="'data-integrate:database:delete'" type="danger" @click="datatable_useCrud.deleteBatchHandle()">删除</el-button>
						</el-form-item>
					</el-form>
					<el-table v-loading="datatable_state.dataListLoading" :data="datatable_state.dataList" border style="width: 100%" max-height="calc(100vh - 400px )" @selection-change="datatable_useCrud.selectionChangeHandle">
						<el-table-column type="selection" header-align="center" align="center" width="50"></el-table-column>
						<el-table-column prop="name" label="名称" header-align="center" align="center" show-overflow-tooltip></el-table-column>
						<fast-table-column prop="databaseType" label="数据库类型" dict-type="database_type"></fast-table-column>
						<el-table-column prop="databaseIp" label="主机ip" header-align="center" align="center" show-overflow-tooltip></el-table-column>
						<el-table-column prop="databasePort" label="端口" header-align="center" align="center"></el-table-column>
						<el-table-column prop="databaseName" label="库名(服务名)" header-align="center" align="center" show-overflow-tooltip></el-table-column>
						<el-table-column prop="databaseSchema" label="schema" header-align="center" align="center" show-overflow-tooltip></el-table-column>
						<fast-table-column prop="status" label="状态" dict-type="database_status"></fast-table-column>
						<fast-table-org-column prop="orgId" label="所属机构" header-align="center" align="center"></fast-table-org-column>
						<fast-creator-column prop="creator" label="创建者" header-align="center" align="center"></fast-creator-column>
						<el-table-column prop="createTime" label="创建时间" header-align="center" align="center" width="160" show-overflow-tooltip></el-table-column>
						<el-table-column label="操作" fixed="right" header-align="center" align="center" width="200">
							<template #default="scope">
								<el-button v-auth="'data-integrate:database:update'" type="primary" link @click="addOrUpdateHandle(scope.row.id)">编辑</el-button>
								<!-- <el-button type="primary" link @click="datatable_tables(scope.row.id)">数据库表</el-button> -->
								<!-- <el-button type="primary" link @click="test(scope.row)">测试</el-button> -->
								<el-button v-auth="'data-integrate:database:delete'" type="danger" link @click="datatable_useCrud.deleteBatchHandle(scope.row.id)">删除</el-button>
							</template>
						</el-table-column>
					</el-table>
					<el-pagination
						:current-page="datatable_state.page"
						:page-sizes="datatable_state.pageSizes"
						:page-size="datatable_state.limit"
						:total="datatable_state.total"
						layout="total, sizes, prev, pager, next, jumper"
						@size-change="datatable_useCrud.sizeChangeHandle"
						@current-change="datatable_useCrud.currentChangeHandle"
					>
					</el-pagination>
				</el-drawer>
				<!-- Mine -->

			</div>
		</div>


		<!-- 弹窗, 新增 / 修改 -->
		<add-or-update ref="addOrUpdateRef_datasource" @refreshDataList="datasource_useCrud.getDataList"></add-or-update>
		<add-or-update_database ref="addOrUpdateRef_database" @refreshDataList="datasource_useCrud.getDataList"></add-or-update_database>
		<!-- <add-or-update-datasource ref="addOrUpdateRef_datasource" @refreshDataList="datasource_useCrud.getDataList"></add-or-update-datasource> -->
	</el-card>
</template>

<script setup lang="ts" name="Data-integrateDatabaseIndex">
import { useCrud } from '@/hooks'
import { reactive, ref, computed, onMounted } from 'vue'
import AddOrUpdate from './add-or-update.vue'
import AddOrUpdate_database from './add-or-update-database.vue'
import { IHooksOptions } from '@/hooks/interface'
import { ElMessage } from 'element-plus/es'
import { testOnline, testOnline_datasource, testOnline_database/*getTablesById, getTableDataBySql*/ } from '@/api/data-integrate/database'

// 数据源
const datasource_state: IHooksOptions = reactive({
	dataListUrl: '/data-integrate/datasource/page',
	deleteUrl: '/data-integrate/datasource',

	// Mine
	// dataListUrl: '/data-integrate/datasource/page',
	// deleteUrl: '/data-integrate/datasource',

	// 搜索框查询, 没啥问题
	queryForm: {
		name: '', 
		databaseType: 1, 
		databaseName: '', 
		databaseSchema: '', // 空
		status: 1, // 空
		isRtApprove: 1, 
		projectId: 1
	},

	// // Mine 应该用这个
	// queryForm_v3: {
	// 	name: '', 
	// 	datasourceType: '', 
	// 	datasourceSchema: '', // 我也不知道是啥
	// 	status: '', 
	// 	// isRtApprove: '', // 是否支持实时接入？ 
	// 	platformName: ''
	// },

	// // Mine
	// queryForm_v2: {
	// 	DataSourceID: '',
	// 	DataSourceName: '',
	// 	IP: '',
	// 	Port: '',
	// 	UserName: '',
	// 	Password: '',
	// 	PlatformID: '',
	// 	DatabaseType: ''
	// },

	// direction: 'rtl',
	databaseId: '', 
	// sqlDataHeader: {},
	// sqlData: [],
	tableData: [] // 好像没啥用
})

// 数据库
const database_state: IHooksOptions = reactive({
	dataListUrl: '/data-integrate/database/page',
	deleteUrl: '/data-integrate/database',

	// Mine
	datasource_id: '',
	dataListUrl_v2: '/data-integrate/database/page',
	deleteUrl_v2: '/data-integrate/database',
	database_drawer: false,

	// queryForm: {
	// 	datasourceId: '',
	// 	name: '', 
	// 	databaseType: '', 
	// 	databaseName: '', 
	// 	databaseSchema: '',
	// 	status: '', 
	// 	isRtApprove: '', 
	// 	projectId: ''
	// },

	// 这个才对
	queryForm: {
		datasourceId: '',
		name: '', 
		status: '',
		creatorName: '',
	},

	direction: 'rtl',
	databaseId: '', 
	// sqlDataHeader: {},
	// sqlData: [],
	tableData: []
})

// 数据表
const datatable_state: IHooksOptions = reactive({
	dataListUrl: '/data-integrate/datasource/page',
	deleteUrl: '/data-integrate/datasource',

	// Mine
	datasource_id: '',
	database_id: '',
	dataListUrl_v2: '/data-integrate/datatable/page',
	deleteUrl_v2: '/data-integrate/datatable',
	datatable_drawer: false,

	queryForm: {
		name: '', 
		databaseType: '', 
		databaseName: '', 
		databaseSchema: '',
		status: '', 
		isRtApprove: '', 
		projectId: ''
	},

	// 这个才对
	queryForm_v3: {
		name: '',
		databaseID: ''
	},

	// Mine
	queryForm_v2: {
		DatatableID: '',
		DatatableName: '',
		DatabaseID: '',
	},

	// database_drawer: false,
	direction: 'rtl',
	databaseId: '', 
	// sqlDataHeader: {},
	// sqlData: [],
	tableData: []
})

const addOrUpdateRef_datasource = ref()
const addOrUpdateHandle_datasource = (id?: number) => {
	addOrUpdateRef_datasource.value.init(id)
}

const addOrUpdateRef_database = ref()
const addOrUpdateHandle_database = (id?: number) => {
	addOrUpdateRef_database.value.init(id)
}


// 测试连接
// database是空
const test_datasource = (row) => {
	// console.log(row)
	// row['aaaaa'] = 'aaaaa'
	// console.log(row['aaaaa'])
	testOnline_datasource(row).then(() => {
		ElMessage.success({
			message: '测试连接成功',
			duration: 500,
			onClose: () => {
				datasource_useCrud.getDataList()
			}
		})
	})
}
const test_database = (row) => {
	const test_form = {
		datasource_id: datatable_state.datasource_id,
		database_name: row['databaseName'],
	}
	console.log(test_form)
	testOnline_database(test_form).then(() => {
		ElMessage.success({
			message: '测试连接成功',
			duration: 500,
			onClose: () => {
				datasource_useCrud.getDataList()
			}
		})
	})
}

const database_tables = (id) => {
	database_state.database_drawer = true
	database_state.databaseId = id

	// Mine
	database_state.datasource_id = id
	database_state.queryForm.datasourceId = id

	// getTablesById(id).then(res => {
	// 	state.tableData = res.data
	// })

	// 记得前面 v3 要改掉
	database_state.queryForm.datasourceId = id
	database_useCrud.getDataList()
}

const datatable_tables = (id) => {
	datatable_state.datatable_drawer = true
	datatable_state.databaseId = id

	// Mine
	datatable_state.datasource_id = database_state.datasource_id
	datatable_state.database_id = id

	// getTablesById(id).then(res => {
	// 	state.tableData = res.data
	// })

	// 记得前面 v3 要改掉
	datatable_state.queryForm.databaseID = id
	datatable_useCrud.getDataList()
}

// const sql = ref('')
// const runSql = () => {
// 	if (!sql.value.trim()) {
// 		ElMessage.error('请输入sql')
// 		return
// 	}
// 	getTableDataBySql(state.databaseId, {"sql" : sql.value}).then(res=>{
// 		state.sqlDataHeader = res.data.columns
// 		state.sqlData = res.data.rows
// 	})
// }

// const search = ref('')
// const filterTableData = computed(() =>
//   datasource_state.tableData.filter(
//     (data) =>
//       !search.value ||
//       data.tableName && data.tableName.toLowerCase().includes(search.value.toLowerCase()) ||
// 			data.remarks && data.remarks.toLowerCase().includes(search.value.toLowerCase())
//   )
// )

const databasedrawerClose = (done: () => void) => {
	// search.value=''
	// sql.value=''
	// database_state.sqlData = []
	// database_state.sqlDataHeader = []
	database_state.tableData = []
	done()
}

// before-close钩子。这个钩子函数接收一个参数，通常命名为done，它是一个回调函数。
// 当抽屉准备关闭，但在实际关闭之前，Vue会调用这个钩子函数。如果在这个函数中执行了done()，
// 那么它告诉Vue，关闭抽屉前的所有操作都已完成，可以继续执行关闭动作
const datatabledrawerClose = (done: () => void) => {
	// search.value=''
	// sql.value=''
	// datatable_state.sqlData = []
	// datatable_state.sqlDataHeader = []
	datatable_state.tableData = []
	done()
}

const datasource_useCrud = useCrud(datasource_state)
const database_useCrud = useCrud(database_state)
const datatable_useCrud = useCrud(datatable_state)
// const { getDataList, selectionChangeHandle, sizeChangeHandle, currentChangeHandle, deleteBatchHandle } = useCrud(datatable_state)

// defineExpose({
// 	// init()
// })
</script>

<style>
	.databaseDivClass {
		height: calc(100vh - 170px );
		position: relative;
		overflow: hidden;
	}
	.databaseDivClass > .drawerClass > div {
		height: 100%;
		position: absolute !important;
		overflow: hidden;
	}
	
	.tableMain {
		display : flex;
		
	}
	.tableMain div:nth-child(1){
		flex : 1;
	}
	.tableMain div:nth-child(2){
		flex : 2;
	}
</style>