<template>
    <div class="common-layout">
        <el-container>
            <el-header>
                <el-card class="custom-card">
                    <div slot="header" class="clearfix">
                        <span class="spantext">各学年课业绩点趋势分析</span>
                    </div>
                    <div id="mainCoursePerformance" style="height: 400px;"></div> 
                </el-card>
                <el-card class="custom-card">
                    <div slot="header" class="clearfix">
                        <span class="spantext">各学年综合竞赛趋势分析</span>
                    </div>
                    <div id="mainComprehensiveCompetition" style="height: 400px;"></div> 
                </el-card>
                <el-card class="custom-card">
                    <div slot="header" class="clearfix">
                        <span class="spantext">各学年专业竞赛趋势分析</span>
                    </div>
                    <div id="mainprofessionalCompetition" style="height: 400px;"></div> 
                </el-card>
                <el-card class="custom-card">
                    <div slot="header" class="clearfix">
                        <span class="spantext">各学年科研成果趋势分析</span>
                    </div>
                    <div id="mainacademicAchievements" style="height: 400px;"></div> 
                </el-card>
                <el-card class="custom-card">
                    <div slot="header" class="clearfix">
                        <span class="spantext">各学年社会服务趋势分析</span>
                    </div>
                    <div id="mainsocialService" style="height: 400px;"></div> 
                </el-card>
            </el-header>
        </el-container>
    </div>
</template>

<script setup lang="ts">
import * as echarts from 'echarts';
import { ref, onMounted, nextTick } from 'vue';
import { getVisualPeriodData } from '@/api/data-integrate/database';

const option = {
    color: [
        '#dbb8b2',
        '#496c88',
        '#a5b6c5',
        '#bebab9',
        '#a5ccc7',
        '#1c3c63',
        '#d3d5d4',
        '#97a9b7',
        '#bda29a',
        '#6e7074',
        '#546570',
    ],
    tooltip: {
        trigger: 'axis',
        axisPointer: {
            type: 'cross',
            crossStyle: {
                color: '#999'
            }
        },
    },
    toolbox: {
        feature: {
            dataView: { show: true, readOnly: false },
            magicType: { show: true, type: ['line', 'bar'] },
            // restore: { show: true },
            saveAsImage: { show: true }
        }
    },
    legend: { data:[] },
    dataset: {},
    grid: [
        { right: '55%' },
        { left: '55%' },
    ],
    xAxis: [
        {
            type: 'category',
            gridIndex: 0,
            axisPointer: {
                type: 'shadow'
            },
        },
        {
            type: 'category',
            gridIndex: 1,
            axisPointer: {
                type: 'shadow'
            },
        },
    ],
    yAxis: [
        { gridIndex: 0 }, 
        { gridIndex: 1 }
    ],
    series: []
};

const getData = (data: any, myChart: any, judge: number) => {

    // 获取所有教育阶段和类别
    const educationLevels = Array.from(new Set(data.map(item => item.education)));
    const classes = Array.from(new Set(data.map(item => item.classs)));

    const dataset = [
        {
            dimensions: ['education', 'classs', 'year', 'value'],
            source: data
        }
    ];
    const series = [];
    const legend = { data: [] as string[] };

    educationLevels.forEach((education, index) => {
        classes.forEach((classs, index2) => {
            dataset.push({
                transform: [
                    {
                        type: 'filter',
                        config: { dimension: 'education', value: education }
                    },
                    {
                        type: 'filter',
                        config: { dimension: 'classs', value: classs }
                    },
                    {
                        type: 'sort',
                        config: { dimension: 'year', order: 'asc' } // 对年份进行升序排序
                    },
                ]
            })

            if (judge > 0) {
                // 画柱状图
                series.push({
                    type: 'bar',
                    name: education + '-' + classs,
                    xAxisIndex: index,
                    yAxisIndex: index,
                    datasetIndex: dataset.length - 1,
                    encode: {
                        x: 'year',
                        y: 'value'
                    }
                });
            } 
            else if (judge <= 0) {
                // 画折线图
                series.push({
                    type: 'line',
                    name: education + '-' + classs,
                    xAxisIndex: index,
                    yAxisIndex: index,
                    datasetIndex: dataset.length - 1,
                    encode: {
                        x: 'year',
                        y: 'value'
                    }
                });
            }

            legend.data.push(`${education}-${classs}`)

        });
    });

    myChart.setOption({
        legend: legend,
        dataset: dataset,
        series: series
    })
}

const data = [[{}]]

async function fetchData() {
    try {
        const res = await getVisualPeriodData();
        // console.log("看看可视化接口收到的数据");
        // console.log(JSON.stringify(res.data.list[0]));
        data[0] = res.data.list;
        console.log(data[0])
        // console.log(data[0][0]);
        // console.log("结束");
    } catch (error) {
        console.error("获取数据时出错:", error);
    }
}

const getChart = async (chartId: string, index: number, judge: number) => {
    var chartDom = document.getElementById(chartId);
    const existingInstance = echarts.getInstanceByDom(chartDom);
    if (existingInstance) {
        existingInstance.dispose();
    }
    var myChart = echarts.init(chartDom);
    myChart.setOption(option)

    await fetchData()

    myChart.showLoading();
    getData(data[0][index], myChart, judge)
    myChart.hideLoading();
}

onMounted(() => {
    getChart('mainCoursePerformance', 0, 1);
    getChart('mainComprehensiveCompetition', 1, 0);
    getChart('mainprofessionalCompetition', 2, 1)
    getChart('mainacademicAchievements', 3, 0)
    getChart('mainsocialService', 4, 1)
});

</script>


<style scoped>
.custom-card {
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1) !important;
    padding-top: 20px;
    padding-right: 30px;
    margin-top: 30px;
    margin-bottom: 30px;
}

.clearfix:before,
.clearfix:after {
    display: table;
    content: "";
}
.clearfix:after {
    clear: both
}
.clearfix {
    width: 100%;
}
.spantext {
    width: 100%; 
    display: flex; 
    justify-content: center; 
    lign-items: center;
    margin-bottom: 25px;
    font-size: 22px;
    font-weight: bold;
    font-family: "微软雅黑";
}
</style>