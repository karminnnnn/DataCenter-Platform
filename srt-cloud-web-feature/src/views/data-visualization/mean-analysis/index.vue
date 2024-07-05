<template>
	<div class="common-layout">
		<el-container>
			<!-- 左侧 -->
			<el-aside width="20%">
				<el-card body-style="height: calc(100vh - 170px )">
          <template #header>
            <div class="card-header">
              <span>选择能力</span>
            </div>
          </template>
          <el-checkbox-group v-model="checkList" @change="refreshChart">
            <el-checkbox label="公必绩点" value="公必绩点">{{ '（课程业绩）公必绩点' }}</el-checkbox>
            <el-checkbox label="专选绩点" value="专选绩点">{{ '（课程业绩）专选绩点' }}</el-checkbox>
            <el-checkbox label="专必绩点" value="专必绩点">{{ '（课程业绩）专必绩点' }}</el-checkbox>
            <el-checkbox label="党建思政获奖" value="党建思政获奖">{{ '（综合竞赛）党建思政获奖' }}</el-checkbox>
            <el-checkbox label="学科竞赛获奖" value="学科竞赛获奖">{{ '（专业竞赛）学科竞赛获奖' }}</el-checkbox>
            <el-checkbox label="艺术比赛获奖" value="艺术比赛获奖">{{ '（综合竞赛）艺术比赛获奖' }}</el-checkbox>
            <el-checkbox label="体育比赛获奖" value="体育比赛获奖">{{ '（综合竞赛）体育比赛获奖' }}</el-checkbox>
            <el-checkbox label="实践创业竞赛获奖" value="实践创业竞赛获奖">{{ '（综合竞赛）实践创业竞赛获奖' }}</el-checkbox>
            <el-checkbox label="学术成果获奖" value="学术成果获奖">{{ '（专业竞赛）学术成果获奖' }}</el-checkbox>
            <el-checkbox label="高水平论文发表" value="高水平论文发表">{{ '（论文）高水平论文发表' }}</el-checkbox>
            <el-checkbox label="志愿服务时长" value="志愿服务时长">{{ '（社会服务）志愿服务时长' }}</el-checkbox>
            <el-checkbox label="专利发明" value="专利发明">{{ '（知识产权）专利发明' }}</el-checkbox>
            <el-checkbox label="软件著作权发明" value="软件著作权发明">{{ '（知识产权）软件著作权发明' }}</el-checkbox>
            <el-checkbox label="专著出版" value="专著出版">{{ '（专著）专著出版' }}</el-checkbox>
          </el-checkbox-group>
          <div class="login-more">
            <el-button type="success" size="large" @click="detailHandle()">  查看详情  </el-button>
          </div>

          <!-- <el-checkbox-group v-model="checkList" @change="Log">
            <el-checkbox label="Value A" value="Value A" />
            <el-checkbox label="Value B" value="Value B" />
            <el-checkbox label="Value C" value="Value C" />
            <el-checkbox label="Value disabled" value="Value disabled" disabled />
            <el-checkbox
              label="Value selected and disabled"
              value="Value selected and disabled"
              disabled
            />
          </el-checkbox-group> -->
				</el-card>
			</el-aside>
			<!-- 右侧主区域 -->
			<el-container width="60%">
				<el-header>
					<el-card body-style="height: calc(100vh - 170px )">
            <v-chart class="chart" :option="option" autoresize/>
					</el-card>
				</el-header>
			</el-container>
		</el-container>
  </div>

  <detail ref="detailRef"></detail>
</template>

<script lang="ts" setup>
import { use } from 'echarts/core';
import { CanvasRenderer } from 'echarts/renderers';
import { RadarChart } from 'echarts/charts';
import {
  TitleComponent,
  TooltipComponent,
  LegendComponent,
} from 'echarts/components';
import VChart, { THEME_KEY } from 'vue-echarts';
import { ref, provide, reactive, onMounted } from 'vue';
import Detail from './detail.vue';
import { ElMessage } from 'element-plus/es';
import { IHooksOptions } from '@/hooks/interface';
import { useCrud } from '@/hooks/index'


const detailRef = ref()
const detailHandle = () => {
  detailRef.value.init(state.dataList)
}

const pair = {
  "公必绩点": "pubCoursePerf",
  "专选绩点": "eleCoursePerf",
  "专必绩点": "mandSpecCoursePerf",
  "党建思政获奖": "jdIdeaPolAwards",
  "学科竞赛获奖": "discCompAwards",
  "艺术比赛获奖": "artCompAwards",
  "体育比赛获奖": "sportCompAwards",
  "实践创业竞赛获奖": "entrepCompAwards",
  "学术成果获奖": "academicAwards",
  "高水平论文发表": "highLevelPubs",
  "志愿服务时长": "volServHours",
  "专利发明": "patents",
  "软件著作权发明": "softCopyInventions",
  "专著出版": "monographsPub"
};
const gradeAndClass = [
    '本科生一年级1班',
    '本科生一年级2班',
    '本科生一年级3班',
    '本科生一年级',
    '本科生二年级1班',
    '本科生二年级2班',
    '本科生二年级3班',
    '本科生二年级',
    '本科生三年级1班',
    '本科生三年级2班',
    '本科生三年级3班',
    '本科生三年级',
    '本科生四年级1班',
    '本科生四年级2班',
    '本科生四年级3班',
    '本科生四年级',
    '研究生一年级1班',
    '研究生一年级2班',
    '研究生一年级3班',
    '研究生一年级',
    '研究生二年级1班',
    '研究生二年级2班',
    '研究生二年级3班',
    '研究生二年级',
    '研究生三年级1班',
    '研究生三年级2班',
    '研究生三年级3班',
    '研究生三年级',
    ];
const numClass = 28;
const checkList = ref([])

const changeChart = (checkItem, engCheckItem) => {

  console.log(state.dataList)

  // 最高水平
  option.value.radar.indicator.push({
    name: checkItem,
    max: state.dataList[numClass-1][engCheckItem]
  });

  for (let i = 0; i < numClass; i++) {
      // option.value.series[0]['data'][i]['value'] = []
      option.value.series[0]['data'][i]['value'].push(
      state.dataList[i][engCheckItem]
    )
  }
}

const refreshChart = () => {
  console.log(checkList.value)
  console.log(option.value.radar.indicator)
  console.log(state.dataList)

  if (checkList.value.length > 0) {
    // 清空indicator数组
    option.value.radar.indicator = [];
    for (let i = 0; i < numClass; i++) {
      option.value.series[0]['data'][i]['value'] = []
    }

    for (let i = 0; i < checkList.value.length; i++) {
      const checkItem = checkList.value[i];
      changeChart(checkItem, pair[checkItem])
      console.log(checkItem)
      console.log(option.value.radar.indicator)
      console.log(option.value.series)
    }
  }
  else {
    console.log("No check")
    // ElMessage.error("请至少选择一个维度！")
  }
}

const state: IHooksOptions = reactive({
	dataListUrl: '/data-integrate/data-visualization/study-analysis/page',
	deleteUrl: '/data-integrate/data-visualization/study-analysis',
  queryForm: {
    NetId: '0',
  },
	tableData: [] // 好像没啥用
})


use([
  CanvasRenderer,
  RadarChart,
  TitleComponent,
  TooltipComponent,
  LegendComponent,
]);

const option = ref({
  title: {
    text: '平均学业能力'
  },
  legend: {
    data: gradeAndClass,
    type: 'scroll',
    orient: 'vertical',
    right: 50,
    top: 20,
    bottom: 20,
    // selected: {
    //   "本科生一年级1班": false,
    //   "本科生一年级2班": false,
    //   "本科生一年级3班": false,
    //   "本科生一年级": false,
    //   "本科生二年级1班": false,
    //   "本科生二年级2班": false,
    //   "本科生二年级3班": false,
    //   "本科生二年级": false,
    //   "本科生三年级1班": false,
    //   "本科生三年级2班": false,
    //   "本科生三年级3班": false,
    //   "本科生三年级": false,
    //   "本科生四年级1班": false,
    //   "本科生四年级2班": false,
    //   "本科生四年级3班": false,
    //   "本科生四年级": false,
    //   "研究生一年级1班": false,
    //   "研究生一年级2班": false,
    //   "研究生一年级3班": false,
    //   "研究生一年级": false,
    //   "研究生二年级1班": false,
    //   "研究生二年级2班": false,
    //   "研究生二年级3班": false,
    //   "研究生二年级": false,
    //   "研究生三年级1班": false,
    //   "研究生三年级2班": false,
    //   "研究生三年级3班": false,
    //   "研究生三年级": false
    // }
  },
  radar: {
    shape: 'circle',
    indicator: []
  },
  series: [
    {
      name: 'Budget vs spending',
      type: 'radar',
      data: [
        { value: [], name: "本科生一年级1班", itemStyle: { color: '#FF4D4D' } },
        { value: [], name: "本科生一年级2班", itemStyle: { color: '#FF6F00' } },
        { value: [], name: "本科生一年级3班", itemStyle: { color: '#FFD700' } },
        { value: [], name: "本科生一年级", itemStyle: { color: '#98FB98' } },
        { value: [], name: "本科生二年级1班", itemStyle: { color: '#87CEFA' } },
        { value: [], name: "本科生二年级2班", itemStyle: { color: '#00BFFF' } },
        { value: [], name: "本科生二年级3班", itemStyle: { color: '#8A2BE2' } },
        { value: [], name: "本科生二年级", itemStyle: { color: '#DC143C' } },
        { value: [], name: "本科生三年级1班", itemStyle: { color: '#E74C3C' } },
        { value: [], name: "本科生三年级2班", itemStyle: { color: '#90EE90' } },
        { value: [], name: "本科生三年级3班", itemStyle: { color: '#FFDB58' } },
        { value: [], name: "本科生三年级", itemStyle: { color: '#5C6BC0' } },
        { value: [], name: "本科生四年级1班", itemStyle: { color: '#FF69B4' } },
        { value: [], name: "本科生四年级2班", itemStyle: { color: '#FF8C00' } },
        { value: [], name: "本科生四年级3班", itemStyle: { color: '#ADFF2F' } },
        { value: [], name: "本科生四年级", itemStyle: { color: '#85144B' } },
        { value: [], name: "研究生一年级1班", itemStyle: { color: '#FF4500' } },
        { value: [], name: "研究生一年级2班", itemStyle: { color: '#6A5ACD' } },
        { value: [], name: "研究生一年级3班", itemStyle: { color: '#708090' } },
        { value: [], name: "研究生一年级", itemStyle: { color: '#F08080' } },
        { value: [], name: "研究生二年级1班", itemStyle: { color: '#FF6347' } },
        { value: [], name: "研究生二年级2班", itemStyle: { color: '#FFA07A' } },
        { value: [], name: "研究生二年级3班", itemStyle: { color: '#20B2AA' } },
        { value: [], name: "研究生二年级", itemStyle: { color: '#48D1CC' } },
        { value: [], name: "研究生三年级1班", itemStyle: { color: '#C71585' } },
        { value: [], name: "研究生三年级2班", itemStyle: { color: '#191970' } },
        { value: [], name: "研究生三年级3班", itemStyle: { color: '#FFD700' } },
        { value: [], name: "研究生三年级", itemStyle: { color: '#FF69B4' } }
      ]
    }
  ]
});

const stateUseCrud = useCrud(state)
</script>



<style lang="scss" scoped>
.card-header {
  padding: 10px;            /* 内边距 */
  /* background-color: #f8f9fa; 背景色 */
  font-size: 17px;          /* 字体大小 */
  font-weight: bold;        /* 字体加粗 */
  color: #000000;               /* 字体颜色 */
  display: flex;            /* 弹性布局 */
  justify-content: space-between; /* 两端对齐 */
  align-items: center;      /* 垂直居中 */
};
.login-more {
	display: flex;
	justify-content: space-evenly;
	padding-top: 25px;
	width: 160px;
	margin: 0 auto;
}
</style>