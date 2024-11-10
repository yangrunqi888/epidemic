<template>
    <div v-loading="loading" style="width: 25vw;height: 60vh;text-align: center; ">
        <p>全国疫情地图</p>
        <p>截止于{{ time }}</p>
        <div ref="echarts1" style="width: 100%;height:55vh;margin: 0 auto"></div>
    </div>

</template>
<script>
import * as echarts from 'echarts'
import '../assets/china'
import axios from 'axios'
import Mock from 'mockjs'

export default {
    data() {
        return {
            dataList: [],
            time: "",
            loading: false,
        };
    },
    mounted() {
        this.fetchData(); // 获取数据
        const now = new Date();
        this.time = `${now.getFullYear()}-${(now.getMonth() + 1).toString().padStart(2, '0')}-${now.getDate().toString().padStart(2, '0')} ${now.getHours().toString().padStart(2, '0')}:${now.getMinutes().toString().padStart(2, '0')}:${now.getSeconds().toString().padStart(2, '0')}`;
    },
    methods: {
        fetchData() {
            // 模拟请求
            axios.get("http://www.888.com").then(response => {
                this.dataList = response.data.data; // 更新 dataList
                this.initChart(); // 初始化图表
            });
        },
        initChart() {
            var myChart = echarts.init(this.$refs.echarts1);
            var option = {
                tooltip: {
                    triggerOn: "click",
                    formatter: function (e) {
                        return e.value === 0 ? e.name + "：有疑似病例" : e.seriesName + "<br />" + e.name + "：" + e.value;
                    }
                },
                visualMap: {
                    min: 0,
                    max: 100000,
                    left: 26,
                    bottom: 0,
                    showLabel: true,
                    text: ["高", "低"],
                    pieces: [
                        { gt: 50000, label: "> 50000", color: "#7f1100" },
                        { gte: 10000, lte: 50000, label: "10000 - 50000人", color: "#ff5428" },
                        { gte: 1000, lt: 10000, label: "1000 - 10000人", color: "#ff8c71" },
                        { gt: 100, lt: 1000, label: "100 - 1000人", color: "#ffd768" },
                        { gt: 1, lt: 100, label: "1 - 100人", color: "#ffffff" }
                    ],
                    show: true
                },
                geo: {
                    map: "china",
                    roam: false,
                    scaleLimit: { min: 1, max: 2 },
                    zoom: 1.1,
                    top: 70,
                    label: {
                        normal: {
                            show: true,
                            fontSize: "9",
                            color: "rgba(0,0,0,0.7)"
                        }
                    },
                    itemStyle: {
                        normal: { borderColor: "rgba(0, 0, 0, 0.2)" },
                        emphasis: { areaColor: "#f2d5ad" }
                    }
                },
                series: [{
                    name: "确诊病例",
                    type: "map",
                    geoIndex: 0,
                    data: this.dataList
                }]
            };
            myChart.setOption(option);
            window.onresize = () => myChart.resize();
        }
    }
}
</script>
<style scoped>

</style>