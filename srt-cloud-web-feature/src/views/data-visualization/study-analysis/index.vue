<template>
	<div class="common-layout">
		<el-container>
			<!-- 左侧 -->
			<el-aside width="20%">
				<el-card body-style="height: calc(100vh - 170px )">
          <template #header>
            <div class="card-header">
              <span>选择我的能力（选择 3~6 项）</span>
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
  "公必绩点": "PubCoursePerf",
  "专选绩点": "EleCoursePerf",
  "专必绩点": "MandSpecCoursePerf",
  "党建思政获奖": "JdIdeaPolAwards",
  "学科竞赛获奖": "DiscCompAwards",
  "艺术比赛获奖": "ArtCompAwards",
  "体育比赛获奖": "SportCompAwards",
  "实践创业竞赛获奖": "EntrepCompAwards",
  "学术成果获奖": "AcademicAwards",
  "高水平论文发表": "HighLevelPubs",
  "志愿服务时长": "VolServHours",
  "专利发明": "Patents",
  "软件著作权发明": "SoftCopyInventions",
  "专著出版": "MonographsPub"
};
const checkList = ref([])

const changeChart = (checkItem, engCheckItem) => {
  // 最高水平
  option.value.radar.indicator.push({
    name: checkItem,
    max: state.dataList[3][engCheckItem]
  });
  // 我的水平
  option.value.series[0]['data'][0]['value'].push(
    state.dataList[0][engCheckItem]
  )
  // 班级平均水平
  option.value.series[0]['data'][1]['value'].push(
    state.dataList[1][engCheckItem]
  )
  // 年级平均水平
  option.value.series[0]['data'][2]['value'].push(
    state.dataList[2][engCheckItem]
  )
}

const refreshChart = () => {
  console.log(checkList.value)
  console.log(option.value.radar.indicator)
  console.log(state.dataList)

  if (checkList.value.length > 0) {
    // 清空indicator数组
    option.value.radar.indicator = [];
    option.value.series[0]['data'][0]['value'] = []
    option.value.series[0]['data'][1]['value'] = []
    option.value.series[0]['data'][2]['value'] = []

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
    ElMessage.error("请至少选择一个维度！")
  }
}

const state: IHooksOptions = reactive({
	dataListUrl: '/data-integrate/data-visualization/study-analysis/page',
	deleteUrl: '/data-integrate/data-visualization/study-analysis',
  queryForm: {
    NetID: 1,
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
    text: 'Basic Radar Chart'
  },
  legend: {
    data: ['我的水平', '班级平均水平', '年级平均水平']
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
        {
          value: [],
          name: '我的水平'
        },
        {
          value: [],
          name: '班级平均水平'
        },
        {
          value: [],
          name: '年级平均水平'
        }
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