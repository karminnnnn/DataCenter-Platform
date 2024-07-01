<template>
    <el-dialog v-model="visible" :title="'详情'" :close-on-click-modal="false" destroy-on-close center :before-close="handleBeforeClose">
        <el-table :data="dataList" stripe style="width: 100%">
            <el-table-column prop="ability" label=""/>
            <el-table-column prop="my" label="我的水平" width="180" />
            <el-table-column prop="class" label="班级平均水平" width="180" />
            <el-table-column prop="grade" label="年级平均水平" width="180"/>
        </el-table>
    </el-dialog>
</template>

<script setup lang="ts">
import { da } from 'element-plus/es/locale';
import { reactive, ref } from 'vue'
const visible = ref(false)
const dataList = ref([])

const pair = {
  "PubCoursePerf": "公必绩点",
  "EleCoursePerf": "专选绩点",
  "MandSpecCoursePerf": "专必绩点",
  "JdIdeaPolAwards": "党建思政获奖",
  "DiscCompAwards": "学科竞赛获奖",
  "ArtCompAwards": "艺术比赛获奖",
  "SportCompAwards": "体育比赛获奖",
  "EntrepCompAwards": "实践创业竞赛获奖",
  "AcademicAwards": "学术成果获奖",
  "HighLevelPubs": "高水平论文发表",
  "VolServHours": "志愿服务时长",
  "Patents": "专利发明",
  "SoftCopyInventions": "软件著作权发明",
  "MonographsPub": "专著出版"
};

const init = (data) => {
    console.log(data)
    visible.value = true
    for (const key in data[0]) {
        // console.log(key)
        const item = {
            ability: pair[key],
            my: data[0][key],
            class: data[1][key],
            grade: data[2][key]
        }
        dataList.value.push(item)
    }
}

const cleanDataList = () => {
    dataList.value = []
}
const handleBeforeClose = (done) => {
  cleanDataList();
  // 调用 done 来关闭对话框
  done();
};

defineExpose({
	init
})
</script>