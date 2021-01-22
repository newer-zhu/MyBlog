<template>
    <div>
        <el-container>
            <el-header>
                <Navibar></Navibar>
            </el-header>

            <el-container class="main">
                <el-aside class="aside" width="400px">
                    <el-row :gutter="2">
                        <el-col :span="18"><el-input></el-input></el-col>
                        <el-col :span="6"><el-button type="primary" icon="el-icon-search"/></el-col>
                    </el-row>
<!--                    <div class="block">-->
<!--                        <el-timeline>-->
<!--                            <el-timeline-item-->
<!--                                    v-for="(activity, index) in activities"-->
<!--                                    :key="index"-->
<!--                                    :icon="activity.icon"-->
<!--                                    :type="activity.type"-->
<!--                                    :color="activity.color"-->
<!--                                    :size="activity.size"-->
<!--                                    :timestamp="activity.timestamp">-->
<!--                                {{activity.content}}-->
<!--                            </el-timeline-item>-->
<!--                        </el-timeline>-->
<!--                    </div>-->
                </el-aside>

                <el-main>
<!--                    走马灯-->
                   <el-carousel class="carousel" :interval="4000" type="card" height="250px">
                        <el-carousel-item v-for="item in imgSrc" :key="item">
                            <el-image :src="item"></el-image>
                        </el-carousel-item>

                    </el-carousel>
                    <el-row :gutter="20">
                        <el-col :span="16">
                            <div class="grid-content bg-purple">
                                <el-timeline>
                                    <el-timeline-item v-for="(card,index) in this.blogs" :timestamp="card.createdAt" placement="top">
                                        <router-link :to="{name: 'BlogDetail', params:{blogId:card.id}}">
                                            <el-card shadow="hover">
                                                <h3>{{card.title}}</h3>
                                                <p>{{card.summary}}</p>
                                            </el-card>
                                        </router-link>
                                    </el-timeline-item>
                                </el-timeline>
                            </div>
                        </el-col>

                        <el-col :span="8">
                            <div >
                                <el-card shadow="hover">
                                    <div slot="header" class="clearfix">
                                        <span><i class="el-icon-notebook-2"></i> 博客分类</span>
                                        <el-button style="float: right; padding: 3px 0" type="text">操作按钮</el-button>
                                    </div>
                                    <div v-for="(column, index) in this.columns" :key="column.c_name" class="text item">
                                        <el-row type="flex">
                                            <el-col :span="18"><h2>{{column.c_name+' ('+column.count+')'}}</h2></el-col>
                                            <el-col :span="6" ><el-link type="danger" style="text-underline: none" @click="delColumn(column.id)">删除</el-link></el-col>
                                        </el-row>
                                    </div>
                                </el-card>
                            </div>
                        </el-col>
                    </el-row>
                </el-main>
<!--                <el-backtop target=".page-component__scroll .el-scrollbar__wrap"></el-backtop>-->

            </el-container>

        </el-container>
<!--        <div>-->
<!--            <Footer/>-->
<!--        </div>-->
    </div>

</template>

<script>
    import Footer from "components/common/Footer"
    import Navibar from "components/common/Navibar";
    export default {
        name: "Index",
        components: {Footer,Navibar},
        data() {
            return{
                userId: 0,
                blogs: [],
                columns: [],
                activities: [{
                    content: '支持使用图标',
                    timestamp: '2018-04-12 20:46',
                    size: 'large',
                    type: 'primary',
                    icon: 'el-icon-more'
                }],
                imgSrc: [require('@/assets/img/view1.jpg'),require('@/assets/img/view2.jpg'),require('@/assets/img/view3.jpg')]
            };
        },
        methods: {
            createWebSocket(){
                let websocket = new WebSocket('ws://127.0.0.1:9000/timeline/'+this.userId)

            },
            success(msg) {
                this.$message({
                    message: msg,
                    type: 'success'
                });
            },
            error(msg){
                this.$message({
                    message: msg,
                    type: 'error'
                });
            },
            delColumn(column_id){
                this.$confirm('此操作将永久删除分栏, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.$axios.get("/column/delcolumnbyid/"+column_id).then((res) =>{
                        if (res.data.code === 200){
                            this.success("删除成功")
                        }else {
                            this.error(res.data.msg)
                        }
                    })
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });
            }
        },
        created() {
            const _this = this;
            this.userId = this.$store.getters.getUser.id;
            this.$axios("/blog/getbyuserid?userId="+_this.userId).then((res) => {
                _this.blogs = res.data.data;
                // console.log(_this.blogs);
            })
            this.$axios("/column/getcolumnbyuserid/"+_this.userId).then((res)=>{
                _this.columns = res.data.data;
            })
        }

    }
</script>

<style scoped>
    .main{
        padding: 10px;
    }
    .el-carousel__item h3 {
        color: #475669;
        font-size: 14px;
        opacity: 0.75;
        line-height: 150px;
        margin: 0;
    }
    .el-carousel__item h3 {
        color: #475669;
        font-size: 14px;
        opacity: 0.75;
        line-height: 200px;
        margin: 0;
    }
    .el-carousel__item:nth-child(2n) {
        background-color: #99a9bf;
    }

    .el-carousel__item:nth-child(2n+1) {
        background-color: #d3dce6;
    }
    .carousel{
        width: 86%;
        margin: 0 auto;
        text-align: center;
    }
    .aside{
        margin: 0 auto;
        padding-top: 10px;
    }
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

</style>