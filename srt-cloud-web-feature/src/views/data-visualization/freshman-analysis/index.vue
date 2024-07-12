<template>
    <div>
        <el-card class="custom-card" body-style="height: calc(100vh - 170px )">
            <div slot="header" class="clearfix">
                <span class="spantext">入学新生基础属性分布</span>
            </div>
            <el-select class="thisSelect" v-model="yearValue" placeholder="请选择年份" @change="changeYear">
                <el-option
                v-for="item in selectOption"
                :key="item.value"
                :label="item.label"
                :value="item.value"
                >
                </el-option>
            </el-select>
            <div style="display: flex; height: 44%; width: 100%;">
                <div style="width: 25%;">
                    <div>
                        <span class="spantextsmall">学生类别分布</span>
                    </div>
                    <div id="studentType" style="height: 90%;"></div>
                </div>
                <div style="width: 25%;">
                    <div>
                        <span class="spantextsmall">性别分布</span>
                    </div>
                    <div id="gender" style="height: 90%;"></div>
                </div>
                <div style="width: 25%;">
                    <div>
                        <span class="spantextsmall">民族分布</span>
                    </div>
                    <div id="nationality" style="height: 90%;"></div>
                </div>
                <div style="width: 25%;">
                    <div>
                        <span class="spantextsmall">生日年月分布</span>
                    </div>
                    <div id="birthday" style="height: 90%;"></div>
                </div>
            </div>
            <div style="display: flex; height: 44%; width: 100%;">
                <div style="width: 25%;">
                    <div>
                        <span class="spantextsmall">培养层次分布</span>
                    </div>
                    <div id="trainingLevel" style="height: 90%;"></div>
                </div>
                <div style="width: 25%;">
                    <div>
                        <span class="spantextsmall">政治面貌分布</span>
                    </div>
                    <div id="politicalStatus" style="height: 90%;"></div>
                </div>
                <div style="width: 25%;">
                    <div>
                        <span class="spantextsmall">籍贯分布</span>
                    </div>
                    <div id="nativePlace" style="height: 90%;"></div>
                </div>
                <div style="width: 25%;">
                    <div>
                        <span class="spantextsmall">高考成绩分布</span>
                    </div>
                    <div id="examScore" style="height: 90%;"></div>
                </div>
            </div>
        </el-card>
    </div>
</template>

<script setup lang="ts">
import * as echarts from 'echarts';
import { ref, onMounted, nextTick } from 'vue';
import { getStudentTypeData } from '@/api/data-integrate/database';

const yearValue = ref(2020);
const selectOption = [
    {item: '2020年', value: 2020},
    {item: '2021年', value: 2021},
    {item: '2022年', value: 2022},
    {item: '2023年', value: 2023},
]

const changeYear = () => {
    console.log(yearValue.value)
    getChart('studentType', 0, true);
    getChart('gender', 1, true);
    getChart('nationality', 2, false);
    getChart('birthday', 3, false);
    getChart('trainingLevel', 4, true);
    getChart('politicalStatus', 5, true);
    getChart('nativePlace', 6, false);
    getChart('examScore', 7, false);
}

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
        trigger: 'item'
    },
    legend: { data:[] },
    dataset: {},
    series: []
};

// const data = [
//     [
//         {type: "港澳台生", value: 20, year: 2020},
//         {type: "国际生", value: 10, year: 2020},
//         {type: "境内生", value: 119, year: 2020},
//         {type: "港澳台生", value: 18, year: 2021},
//         {type: "国际生", value: 101, year: 2021},
//         {type: "境内生", value: 22, year: 2021},
//     ],
//     [
//         {type: "男", value: 107, year: 2020},
//         {type: "女", value: 46, year: 2020},
//         {type: "男", value: 41, year: 2021},
//         {type: "女", value: 117, year: 2021},
//     ]
// ]
const data = [[{}]]

const getData = (data: any, myChart: any, judge:boolean) => {

    const transformedData = data.reduce((acc, curr) => {
        const type = curr.type.slice(0, 7); // 只取前7个字符，得到"YYYY-MM"
        // console.log("type",type)
        const key = `${type}-${curr.year}`; // 使用type和year组合成一个唯一的键

        if (!acc[key]) {
            acc[key] = { type, value: 0, year: curr.year };
        }

        acc[key].value += curr.value;

        return acc;
    }, {});

    const result = Object.values(transformedData);

    // console.log("看看filteredData",result)
    
    const types = Array.from(new Set(data.map(item => item.type)));

    const dataset = [
        {
            dimensions: ['type', 'value', 'year'],
            source: result
        }
    ];
    const series = [];
    const legend = { data: [] as string[] };

    // console.log("看看画图的时候的年份数据")
    // console.log(yearValue.value)

    dataset.push({
        transform: [
            {
                type: 'filter',
                config: { dimension: 'year', value: yearValue.value}
            },
        ]
    })
    series.push({
      type: 'pie',
      radius: '50%',
      center: ['50%', '50%'],
      encode: {
        itemName: 'type',
        value: 'value'
      },
      datasetIndex:1,
      label: {
        show: judge,
      },
    },);
    types.forEach((type) => {
        legend.data.push(`${type}`)
    })

    if(judge) {
        myChart.setOption({
            legend: legend,
            dataset: dataset,
            series: series
        })
    }
    else{
        myChart.setOption({
            dataset: dataset,
            series: series
        })
    }

}


async function fetchData() {
    try {
        const res = await getStudentTypeData();
        // console.log("看看可视化接口收到的数据");
        // console.log(JSON.stringify(res.data.list));
        data[0] = res.data.list;
        console.log(data[0][3]);
        // console.log("结束");
    } catch (error) {
        console.error("获取数据时出错:", error);
    }
}


const getChart = async (chartId: string, index: number, judge:boolean) => {
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
    getChart('studentType', 0, true);
    getChart('gender', 1, true);
    getChart('nationality', 2, false);
    getChart('birthday', 3, false);
    getChart('trainingLevel', 4, true);
    getChart('politicalStatus', 5, true);
    getChart('nativePlace', 6, false);
    getChart('examScore', 7, false);
});

</script>

<style>

.thisSelect {
    /* margin-top: 10px; */
    margin-bottom: 25px;
    margin-left: 20px;
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
    margin-bottom: 15px;
    font-size: 22px;
    font-weight: bold;
    font-family: "微软雅黑";
}

.spantextsmall {
    width: 100%; 
    display: flex; 
    justify-content: center; 
    margin-bottom: 15px;
    font-size: 18px;
    font-weight: bold;
    font-family: "微软雅黑";
}

</style>