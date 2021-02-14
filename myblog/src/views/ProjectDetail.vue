<template>
    <div>
        <el-header>
            <Navibar/>
        </el-header>
        <el-main>

            <el-row>
                <el-col :span="8">
                    <h1>项目标题测试</h1>
                </el-col>
                <el-col :span="2" :offset="2">
                    <div class="block"><el-avatar :size="50" :src="circleUrl"></el-avatar></div>
                    <span style="left: 0px">username</span>
                </el-col>
                <el-col :span="3" :offset="5">
                    <span>项目评分</span>
                    <el-rate
                            v-model="rates"
                            show-text>
                    </el-rate>
                </el-col>
            </el-row>
            <el-divider/>
            <el-row>
                <el-col :span="16" :offset="4">
                    <el-carousel :interval="4000" type="card" height="200px">
                        <el-carousel-item v-for="item in 3" :key="item">
                            <el-image
                                    :src="url"
                                    :preview-src-list="srcList">
                            </el-image>
                        </el-carousel-item>
                    </el-carousel>
                </el-col>
            </el-row>
<!--            主体-->
            <el-row :gutter="20">
<!--                侧边-->
                <el-col :span="3">
                    <h3>项目进度</h3>
                    <div style="height: 500px">
                        <el-steps direction="vertical":active="1">
                            <el-step title="步骤 1" description="这是一段很长很长很长的描述性文字"></el-step>
                            <el-step title="步骤 2" description="这是一段很长很长很长的描述性文字"></el-step>
                            <el-step title="步骤 3" description="这段就没那么长了"></el-step>
                        </el-steps>
                    </div>
                </el-col>
                <el-divider direction="vertical"/>
                <el-col :span="12" >
                    <el-timeline>
                        <el-timeline-item
                                v-for="(activity, index) in activities"
                                :key="index"
                                :icon="activity.icon"
                                :type="activity.type"
                                :color="activity.color"
                                :size="activity.size"
                                placement="top"
                                :timestamp="activity.timestamp">
                            <el-card class="box-card">
                                <div slot="header" class="clearfix">
                                    <span>单项名称</span>
                                    <el-button style="float: right; padding: 3px 0" type="text">操作按钮</el-button>
                                </div>
                                <div v-for="o in 4" :key="o" class="text item">
                                    一大段话。。。。。。
                                </div>
                            </el-card>
                        </el-timeline-item>
                    </el-timeline>
                </el-col>
                <el-col :span="6" :offset="2">
                    <el-collapse v-model="activeNames" @change="handleChange">
                        <el-collapse-item title="一致性 Consistency" name="1">
                            <div>与现实生活一致：与现实生活的流程、逻辑保持一致，遵循用户习惯的语言和概念</div>
                        </el-collapse-item>
                        <el-collapse-item title="反馈 Feedback" name="2">
                            <div>控制反馈：通过界面样式和交互动效让用户可以清晰的感知自己的操作；</div>
                            <div>页面反馈：操作后，通过页面元素的变化清晰地展现当前状态。</div>
                        </el-collapse-item>
                        <el-collapse-item title="效率 Efficiency" name="3">
                            <div>简化流程：设计简洁直观的操作流程；</div>
                            <div>清晰明确：语言表达清晰且表意明确，让用户快速理解进而作出决策；</div>
                            <div>帮助用户识别：界面简单直白，让用户快速识别而非回忆，减少用户记忆负担。</div>
                        </el-collapse-item>
                        <el-collapse-item title="可控 Controllability" name="4">
                            <div>用户决策：根据场景可给予用户操作建议或安全提示，但不能代替用户进行决策；</div>
                            <div>结果可控：用户可以自由的进行操作，包括撤销、回退和终止当前操作等。</div>
                        </el-collapse-item>
                    </el-collapse>
                </el-col>
            </el-row>
        </el-main>
    </div>
</template>

<script>
    import Navibar from "components/common/Navibar";
    export default {
        name: "ProjectDetail",
        components: {Navibar},
        data(){
            return{
                rates: 5,
                circleUrl: 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png',
                activities: [{
                    content: '支持使用图标',
                    timestamp: '2018-04-12 20:46',
                    size: 'large',
                    type: 'primary',
                    icon: 'el-icon-more'
                }, {
                    content: '支持自定义颜色',
                    timestamp: '2018-04-03 20:46',
                    color: '#0bbd87'
                }, {
                    content: '支持自定义尺寸',
                    timestamp: '2018-04-03 20:46',
                    size: 'large'
                }, {
                    content: '默认样式的节点',
                    timestamp: '2018-04-03 20:46'
                }]
            }
        }
    }
</script>

<style scoped>
    .text {
        font-size: 14px;
    }

    .item {
        margin-bottom: 18px;
    }

    .clearfix:before,
    .clearfix:after {
        display: table;
        content: "";
    }
    .clearfix:after {
        clear: both
    }

    .box-card {
        width: 480px;
    }
    .el-carousel__item h3 {
        color: #475669;
        font-size: 18px;
        opacity: 0.75;
        line-height: 300px;
        margin: 0;
    }

    .el-carousel__item:nth-child(2n) {
        background-color: #99a9bf;
    }

    .el-carousel__item:nth-child(2n+1) {
        background-color: #d3dce6;
    }
</style>