<template>
	<el-dialog v-model="visible" :title="!dataForm.id ? '新增' : '修改'" :close-on-click-modal="false" >
		<el-form ref="dataFormRef" :model="dataForm" :rules="dataRules" label-width="100px" @keyup.enter="submitHandle()">
			<el-form-item label="名称" prop="name" label-width="auto">
				<el-input v-model="dataForm.name" placeholder="名称"></el-input>
			</el-form-item>
			<el-form-item label="规则类型" prop="ruleId" label-width="auto">
				<el-select v-model="dataForm.ruleId"
									 clearable
									 filterable 
									 :disabled="!!dataForm.id"
				           placeholder="请选择">
				  <el-option v-for="(item,index) in qualityRuleList"
				             :key="item.id"
				             :label="`[${item.engName}]${item.name}`"
				             :value="item.id"></el-option>
				</el-select>
			</el-form-item>
			<el-form-item label="检测字段" prop="metadataIds" label-width="auto" v-if="!!dataForm.ruleId && !dataForm.id">
				<el-tree-select v-model="dataForm.metadataIds" :multiple="dataForm.ruleId != 9"  :props="props" :data="metadataList" clearable :load="loadNode" :lazy="true">
					<template #default="{ node, data }">
						 <div>
							 <span>
								 <!-- 如果是目录-->
								 <img v-if="data.icon=='/src/assets/folder.png'" src="/src/assets/folder.png"/>
								 <img v-if="data.icon=='/src/assets/database.png'" src="/src/assets/database.png"/>
								 <img v-if="data.icon=='/src/assets/table.png'" src="/src/assets/table.png"/>
								 <img v-if="data.icon=='/src/assets/column.png'" src="/src/assets/column.png"/>
								 <img v-if="data.icon=='/src/assets/model.png'" src="/src/assets/model.png"/>
								 <span style="margin-left: 8px;">{{ data.name }}&emsp;{{ data.code }}</span>
							 </span>
						 </div>
					</template>
				</el-tree-select>
			</el-form-item>
			<el-form-item label="检测字段" prop="metadataStrs" label-width="auto" v-if="!!dataForm.ruleId && dataForm.id">
				<span>{{dataForm.metadataStrs}}</span>
			</el-form-item>
			<el-form-item label="判断方式" prop="param.uniqueType" label-width="auto" v-if="dataForm.ruleId == 1">
				<fast-select v-model="dataForm.param.uniqueType" dict-type="quality_unique_type" placeholder="判断方式" clearable></fast-select>
			</el-form-item>
			<el-form-item label="字段长度" prop="param.columnLength" label-width="auto" v-if="dataForm.ruleId == 7">
				<el-input-number v-model="dataForm.param.columnLength" :min="1" :max='99999999' placeholder="字段长度"></el-input-number>
			</el-form-item>
			<el-form-item label="关联字段" prop="param.columnMetaId" label-width="auto" v-if="dataForm.ruleId == 9 && !dataForm.id">
				<el-tree-select v-model="dataForm.param.columnMetaId" :props="props" :data="metadataList" clearable :load="loadNode" :lazy="true">
					<template #default="{ node, data }">
						 <div>
							 <span>
								 <!-- 如果是目录-->
								 <img v-if="data.icon=='/src/assets/folder.png'" src="/src/assets/folder.png"/>
								 <img v-if="data.icon=='/src/assets/database.png'" src="/src/assets/database.png"/>
								 <img v-if="data.icon=='/src/assets/table.png'" src="/src/assets/table.png"/>
								 <img v-if="data.icon=='/src/assets/column.png'" src="/src/assets/column.png"/>
								 <img v-if="data.icon=='/src/assets/model.png'" src="/src/assets/model.png"/>
								 <span style="margin-left: 8px;">{{ data.name }}&emsp;{{ data.code }}</span>
							 </span>
						 </div>
					</template>
				</el-tree-select>
			</el-form-item>
			<el-form-item label="关联字段" prop="relMetadataStr" label-width="auto" v-if="dataForm.ruleId == 9 && dataForm.id">
				<span>{{dataForm.relMetadataStr}}</span>
			</el-form-item>
			<el-form-item label="更新时长(s)" prop="param.timeLength" label-width="auto" v-if="dataForm.ruleId == 10">
				<el-input-number v-model="dataForm.param.timeLength" :min="1" :max='99999999' placeholder="更新时长(s)"></el-input-number>
			</el-form-item>
			<el-form-item label="任务类型" prop="taskType" label-width="auto">
				<fast-select v-model="dataForm.taskType" dict-type="quality_config_task_type" placeholder="任务类型" clearable></fast-select>
			</el-form-item>
			<el-form-item label="cron表达式" prop="cron" label-width="auto" v-if="dataForm.taskType == 2">
				<el-input v-model="dataForm.cron"  placeholder="cron表达式" clearable></el-input>
			</el-form-item>
			<el-form-item label="备注" prop="note" label-width="auto">
				<el-input type="textarea" v-model="dataForm.note"></el-input>
			</el-form-item>
	    </el-form>
		<template #footer>
			<el-button @click="visible = false">取消</el-button>
			<el-button type="primary" @click="submitHandle()">确定</el-button>
		</template>
	</el-dialog>
</template>

<script setup lang="ts">
import { reactive, ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus/es'
import { useQualityConfigApi, useQualityConfigSubmitApi, listQualityRuleApi } from '@/api/data-governance/qualityConfig'
import { listChildApi,listKeywordApi } from '@/api/data-governance/metadata'

//获取质量规则列表
onMounted(()=>{
	listQualityRuleApi().then(res => {
		qualityRuleList.value = res.data
	})
	listKeywordApi('').then(res=> {
		metadataList.value = res.data
	})
})

const emit = defineEmits(['refreshDataList'])

const visible = ref(false)
const dataFormRef = ref()

const dataForm = reactive({
	name: '',
	categoryId: '',
	ruleId: '',
	metadataIds: [],
	metadataStrs	: '',
	param: {
		//1-单字段唯一 2-组合字段唯一
		uniqueType: '',
		//字段长度 ruleId为7
		columnLength: 1,
		//关联字段 ruleId为9
		columnMetaId: '',
		//及时性 ruleId为10
		timeLength: 1
	},
	relMetadataStr: '',
	taskType: '',
	cron: '',
	note: '',
	
})

const qualityRuleList = ref([])

const props = {
  label: 'name',
  children: 'children',
  isLeaf: 'leaf',
  disabled: 'disabled'
}
const metadataList = ref([])
const init = (id?: number, categoryId: number) => {
	visible.value = true
	dataForm.id = ''
	
	// 重置表单数据
	if (dataFormRef.value) {
		dataFormRef.value.resetFields()
	}
	dataForm.metadataIds = []
	dataForm.param = {
		//1-单字段唯一 2-组合字段唯一
		uniqueType: '',
		//字段长度 ruleId为7
		columnLength: 1,
		//关联字段 ruleId为9
		columnMetaId: '',
		//及时性 ruleId为10
		timeLength: 1
	}
	dataForm.categoryId = categoryId

	if (id) {
		getQualityConfig(id)
	}
}

const getQualityConfig = (id: number) => {
	useQualityConfigApi(id).then(res => {
		Object.assign(dataForm, res.data)
	})
}

const dataRules = ref({
	name:  [{ required: true, message: '必填项不能为空', trigger: 'blur' }],
	ruleId:  [{ required: true, message: '必填项不能为空', trigger: 'blur' }],
	metadataIds:  [{ required: true, message: '必填项不能为空', trigger: 'blur' }],
	metadataStrs:  [{ required: true, message: '必填项不能为空', trigger: 'blur' }],
	relMetadataStr:  [{ required: true, message: '必填项不能为空', trigger: 'blur' }],
	taskType:  [{ required: true, message: '必填项不能为空', trigger: 'blur' }],
	cron:  [{ required: true, message: '必填项不能为空', trigger: 'blur' }],
	param: {
		uniqueType: [{ required: true, message: '必填项不能为空', trigger: 'blur' }],
		columnLength: [{ required: true, message: '必填项不能为空', trigger: 'blur' }],
		columnMetaId: [{ required: true, message: '必填项不能为空', trigger: 'blur' }],
		timeLength: [{ required: true, message: '必填项不能为空', trigger: 'blur' }],
	}
})

// 表单提交
const submitHandle = () => {
	dataFormRef.value.validate((valid: boolean) => {
		if (!valid) {
			return false
		}
		
		//如果不是唯一校验，长度校验，关联一致性检验 和 及时性， param 设置为 null
		if(dataForm.ruleId != 1 && dataForm.ruleId != 7 && dataForm.ruleId != 9 && dataForm.ruleId != 10) {
			dataForm.param = null
		}
		if(dataForm.ruleId == 9) {
			//如果是新增，改变为数组
			if(!dataForm.id) {
				let metadataIds = []
				metadataIds.push(dataForm.metadataIds)
				dataForm.metadataIds = metadataIds
			}
		}
		dataForm.cron = dataForm.taskType == 2 ? dataForm.cron : null
		useQualityConfigSubmitApi(dataForm).then(() => {
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

const loadNode = async (node: Node, resolve: (data: Tree[]) => void) => {
  if (node.level == 0) {
    return resolve(metadataList.value)
  }
  if (node.level >= 1) {
	if(node.data.children) {
		resolve(node.data.children)
	} else {
		const {data} = await listChildApi(node.data.id)
		resolve(data)
	}
  }
}


defineExpose({
	init
})
</script>
