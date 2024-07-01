<template>
    <el-dialog v-model="visible" :title="'详情'" :close-on-click-modal="false" destroy-on-close center :before-close="handleBeforeClose">
        <el-table :data="dataList" stripe style="width: 100%">
            <el-table-column fixed prop="ability" label="" width="120"/>
            <el-table-column prop="class0" label="本科生一年级1班" width="100" />
            <el-table-column prop="class1" label="本科生一年级2班" width="100" />
            <el-table-column prop="class2" label="本科生一年级3班" width="100" />
               <el-table-column prop="class3" label="本科生一年级" width="100" />
            <el-table-column prop="class4" label="本科生二年级1班" width="100" />
            <el-table-column prop="class5" label="本科生二年级2班" width="100" />
            <el-table-column prop="class6" label="本科生二年级3班" width="100" />
               <el-table-column prop="class7" label="本科生二年级" width="100" />
            <el-table-column prop="class8" label="本科生三年级1班" width="100" />
            <el-table-column prop="class9" label="本科生三年级2班" width="100" />
           <el-table-column prop="class10" label="本科生三年级3班" width="100" />
              <el-table-column prop="class11" label="本科生三年级" width="100" />
           <el-table-column prop="class12" label="本科生四年级1班" width="100" />
           <el-table-column prop="class13" label="本科生四年级2班" width="100" />
           <el-table-column prop="class14" label="本科生四年级3班" width="100" />
              <el-table-column prop="class15" label="本科生四年级" width="100" />
           <el-table-column prop="class16" label="研究生一年级1班" width="100" />
           <el-table-column prop="class17" label="研究生一年级2班" width="100" />
           <el-table-column prop="class18" label="研究生一年级3班" width="100" />
              <el-table-column prop="class19" label="研究生一年级" width="100" />
           <el-table-column prop="class20" label="研究生二年级1班" width="100" />
           <el-table-column prop="class21" label="研究生二年级2班" width="100" />
           <el-table-column prop="class22" label="研究生二年级3班" width="100" />
              <el-table-column prop="class23" label="研究生二年级" width="100" />
           <el-table-column prop="class24" label="研究生三年级1班" width="100" />
           <el-table-column prop="class25" label="研究生三年级2班" width="100" />
           <el-table-column prop="class26" label="研究生三年级3班" width="100" />
              <el-table-column prop="class27" label="研究生三年级" width="100" />
        </el-table>
    </el-dialog>
</template>

<script setup lang="ts">
import { da } from 'element-plus/es/locale';
import { reactive, ref } from 'vue'
const visible = ref(false)
const dataList = ref([])

const pair = {
  "pubCoursePerf": "公必绩点",
  "eleCoursePerf": "专选绩点",
  "mandSpecCoursePerf": "专必绩点",
  "jdIdeaPolAwards": "党建思政获奖",
  "discCompAwards": "学科竞赛获奖",
  "artCompAwards": "艺术比赛获奖",
  "sportCompAwards": "体育比赛获奖",
  "entrepCompAwards": "实践创业竞赛获奖",
  "academicAwards": "学术成果获奖",
  "highLevelPubs": "高水平论文发表",
  "volServHours": "志愿服务时长",
  "patents": "专利发明",
  "softCopyInventions": "软件著作权发明",
  "monographsPub": "专著出版"
};

const init = (data) => {
    console.log(data)
    visible.value = true
    for (const key in data[0]) {
        // console.log(key)
        const item = {
            ability: pair[key],
            class0: data[0][key],
            class1: data[1][key],
            class2: data[2][key],
            class3: data[3][key],
            class4: data[4][key],
            class5: data[5][key],
            class6: data[6][key],
            class7: data[7][key],
            class8: data[8][key],
            class9: data[9][key],
            class10: data[10][key],
            class11: data[11][key],
            class12: data[12][key],
            class13: data[13][key],
            class14: data[14][key],
            class15: data[15][key],
            class16: data[16][key],
            class17: data[17][key],
            class18: data[18][key],
            class19: data[19][key],
            class20: data[20][key],
            class21: data[21][key],
            class22: data[22][key],
            class23: data[23][key],
            class24: data[24][key],
            class25: data[25][key],
            class26: data[26][key],
            class27: data[27][key]
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