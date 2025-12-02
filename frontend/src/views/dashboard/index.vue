<template>
  <div class="dashboard-container">
    <!-- 数据卡片 -->
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card shadow="hover" class="data-card">
          <template #header>
            <div class="card-header">
              <span>今日入园</span>
              <el-tag type="success">实时</el-tag>
            </div>
          </template>
          <div class="card-value">1,203 人</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="data-card">
          <template #header>
            <div class="card-header">
              <span>门票收入</span>
              <el-tag type="warning">CNY</el-tag>
            </div>
          </template>
          <div class="card-value">¥ 45,230</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="data-card">
          <template #header>
            <div class="card-header">
              <span>在园动物</span>
              <el-tag>Total</el-tag>
            </div>
          </template>
          <div class="card-value">342 头</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="data-card">
          <template #header>
            <div class="card-header">
              <span>待处理工单</span>
              <el-tag type="danger">紧急</el-tag>
            </div>
          </template>
          <div class="card-value">5 个</div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="20" class="chart-row">
      <el-col :span="16">
        <el-card shadow="hover">
          <div ref="chartRef" style="height: 400px;"></div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover" header="系统公告">
          <el-timeline>
            <el-timeline-item timestamp="2023/10/20" placement="top" type="primary">
              <el-card>
                <h4>新版系统上线</h4>
                <p>ZIMS v1.0 正式发布，包含动物与票务模块。</p>
              </el-card>
            </el-timeline-item>
            <el-timeline-item timestamp="2023/10/18" placement="top">
              <el-card>
                <h4>大熊猫馆闭馆维护通知</h4>
                <p>预计维护时间：10月20日 - 10月25日。</p>
              </el-card>
            </el-timeline-item>
          </el-timeline>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import * as echarts from 'echarts'

const chartRef = ref(null)

onMounted(() => {
  initChart()
})

const initChart = () => {
  const myChart = echarts.init(chartRef.value)
  const option = {
    title: {
      text: '近七日游客流量趋势'
    },
    tooltip: {
      trigger: 'axis'
    },
    xAxis: {
      type: 'category',
      data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        data: [820, 932, 901, 934, 1290, 1330, 1320],
        type: 'line',
        smooth: true,
        itemStyle: { color: '#409EFF' },
        areaStyle: { color: '#EBF5FF' }
      }
    ]
  }
  myChart.setOption(option)

  window.addEventListener('resize', () => {
    myChart.resize()
  })
}
</script>

<style scoped>
.dashboard-container {
  padding: 10px;
}
.data-card {
  margin-bottom: 20px;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.card-value {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
  margin-top: 10px;
}
.chart-row {
  margin-top: 20px;
}
</style>