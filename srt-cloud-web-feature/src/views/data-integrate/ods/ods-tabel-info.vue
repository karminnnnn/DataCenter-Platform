<template>
    <el-tag
        size="large"
        effect="dark"
        round
        style="margin-bottom: 15px;">
        表属性
    </el-tag>
    <el-button type="primary" style="margin-left: 15px;" @click="addOrUpdateTable(state.currentRow)">修改</el-button>
    <el-descriptions
        :column="1"
        direction="horizontal"
        style="blockMargin">
        <el-descriptions-item label="表名">{{state.currentRow.tableName}}</el-descriptions-item>
        <el-descriptions-item label="注释">{{state.currentRow.remarks}}</el-descriptions-item>
        <el-descriptions-item label="数据接入任务"><el-button type="primary" link @click="getAccessInfoHandler()">查看</el-button></el-descriptions-item>
        <el-descriptions-item label="最近同步时间">{{state.currentRow.recentlySyncTime?state.currentRow.recentlySyncTime:'暂无'}}</el-descriptions-item>
    </el-descriptions>
    <br />
    <el-tag
        size="large"
        effect="dark"
        round
        style="margin-bottom: 15px; "
        >
        字段信息
    </el-tag>
        <el-form :inline="true" :model="state.queryForm" @keyup.enter="getDataList()">
            <el-form-item>
            <el-input v-model="state.queryForm.fieldName" placeholder="属性名称"></el-input>
            </el-form-item>
            <el-form-item>
                <el-button @click="getDataList()">查询</el-button>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="addOrUpdateColumn(null)">新增</el-button>
            </el-form-item>
        </el-form>
        <el-table v-loading="state.dataListLoading" :data="state.tableColumns" height="355" style="width: 100%" @selection-change="selectionChangeHandle">
            <el-table-column prop="fieldName" label="名称" header-align="center" align="center"/>
            <el-table-column prop="remarks" label="注释" header-align="center" align="center"/>
            <el-table-column prop="fieldTypeName" label="类型" header-align="center" align="center"/>
            <el-table-column prop="displaySize" label="长度" header-align="center" align="center"/>
            <el-table-column prop="scaleSize" label="小数位数" header-align="center" align="center"/>
            <el-table-column prop="defaultValue" label="默认值" header-align="center" align="center"/>
            <el-table-column prop="pk" label="是否主键" header-align="center" align="center"/>
            <el-table-column prop="autoIncrement" label="是否递增" header-align="center" align="center"/>
            <el-table-column prop="nullable" label="是否可为空" header-align="center" align="center"/>
            <el-table-column label="操作" fixed="right" header-align="center" align="center" width="150">
                <template #default="scope">
                    <el-button type="primary" link @click="addOrUpdateColumn(scope.row)">编辑</el-button>
                    <el-button type="primary" link @click="deleteBatchHandle(scope.row.fieldId)">删除</el-button>
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

    <!-- 数据接入 详情 -->
	<info ref="infoRef"></info>

    <!-- 弹窗, 表字段新增 / 修改 -->
	<odsColumnAddOrUpdate ref="odsColumnAddOrUpdateRef" @refreshDataList="getDataList"></odsColumnAddOrUpdate>

    <!-- 弹窗, 表新增 / 修改 -->
	<odsTableAddOrUpdate ref="odsTableAddOrUpdateRef" @refreshDataList="getDataList"></odsTableAddOrUpdate>
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

const state: IHooksOptions = reactive({
    createdIsNeed: false,
    dataListUrl: '/data-integrate/ods/tableinfo/page',
    deleteUrl:'',
    queryForm: {
        datatableId: '',
        fieldName: '',
    },
    currentRow: {},
    tableColumns: []
})

const init = (currentRow?:any, tableColumns?:any) => {
    state.currentRow = currentRow
    state.queryForm.datatableId = currentRow.datatableId
    state.tableColumns = tableColumns
    console.log("看看有没有labelname")
    console.log(tableColumns)
}



const infoRef = ref()
/* 查看表的数据接入任务 */
const getAccessInfoHandler = () =>  {
	if(!state.currentRow.dataAccessId) {
		ElMessage({
		    message: '没有查询到该表对应的数据接入任务',
		    type: 'warning',
		  })
		return
	}
	useAccessApi(state.currentRow.dataAccessId).then(res=> {
		if(!res.data) {
			ElMessage({
			    message: '该任务已被删除，若要恢复，请联系管理员处理',
			    type: 'warning',
			  })
				return
		}
		infoRef.value.init(state.currentRow.dataAccessId)
	})
}

// 新增修改
const odsColumnAddOrUpdateRef = ref()
const addOrUpdateColumn = (row?:any) => {
	odsColumnAddOrUpdateRef.value.init(row)
}

const odsTableAddOrUpdateRef = ref()
const addOrUpdateTable = (row?:any) => {
    odsTableAddOrUpdateRef.value.init(row)
}


defineExpose({
	init
})

const { getDataList, selectionChangeHandle, sizeChangeHandle, currentChangeHandle, deleteBatchHandle } = useCrud(state)

</script>