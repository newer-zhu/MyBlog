<template>
    <div>
        <el-container>
            <el-header>
                <Navibar></Navibar>
            </el-header>

            <el-container class="main">
                <el-aside class="aside" width="400px">
                    <el-row>
                        <el-col>
                            <el-input placeholder="请输入搜索内容" v-model="input" class="input-with-select">
                                <el-button  style="color: #409EFF" slot="append" icon="el-icon-search"></el-button>
                            </el-input>
                        </el-col>
                    </el-row>
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
                            <router-view name="listDetail"/>
<!--                            <ListDetail/>-->
                        </el-col>

                        <el-col :span="8">
                            <div >
                                <el-card shadow="hover">
                                    <div slot="header" class="clearfix">
                                        <span><i class="el-icon-notebook-2"></i> 博客分类</span>
                                        <el-button @click="isShow" style="float: right; padding: 3px 0" type="text">删除</el-button>
                                    </div>
                                    <div v-for="(column, index) in this.columns" :key="column.c_name" class="text item">
                                        <el-row type="flex">
                                            <el-col :span="18">
                                                <router-link :to="{path: '/home/listDetail/'+column.id}">
                                                    <h2 :style="randColor">{{column.cname+' ('+column.count+')'}}</h2>
                                                </router-link>
                                            </el-col>
                                            <el-col :span="6">
                                                <el-checkbox v-show="isShowDel" v-model="checkList" style="margin-top: 20px;" :label="column.id">{{''}}</el-checkbox>
                                            </el-col>
                                        </el-row>
                                        <el-divider/>
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
    import ListDetail from "./ListDetail"
    export default {
        // inject:['reload'],
        name: "Index",
        components: {ListDetail, Footer,Navibar},
        data() {
            return{
                input: '',
                userId: 0,
                blogs: [],
                columns: [],
                imgSrc: [require('@/assets/img/view1.jpg'),require('@/assets/img/view2.jpg'),require('@/assets/img/view3.jpg')],
                isShowDel: true,//是否显示删除专栏复选框
                checkList: [],
                randColor: {//分栏字体显示
                    color: this.getColor(),
                    fontSize: '20px'
                }
            }
        },
        methods: {
            createWebSocket(){
                let websocket = new WebSocket('ws://127.0.0.1:9000/timeline/'+this.userId)

            },
            getColor(){
                let r = Math.floor(Math.random()*255);
                let g = Math.floor(Math.random()*255);
                let b = Math.floor(Math.random()*255);
                return 'rgba('+ r +','+ g +','+ b +',1)';
            },
            //删除分栏的函数
            isShow(){
                if (this.checkList.length != 0){
                    this.$confirm('此操作将永久删除分栏, 是否继续?', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(() => {
                        this.$axios.post("/column/delcolumnbyid", this.checkList).then((res) =>{
                            if (res.data.code === 200){
                                this.success("删除成功")
                                this.$router.go(0)
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
              this.isShowDel = !this.isShowDel;
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
        },
        created() {
            this.isShowDel = false;
            const _this = this;
            this.userId = this.$store.getters.getUser.id;
            this.$axios("/blog/getbyuserid?userId="+_this.userId).then((res) => {
                _this.blogs = res.data.data;
                // console.log(_this.blogs);
            })
            this.$axios("/column/getcolumnbyuserid/"+_this.userId).then((res)=>{
                _this.columns = res.data.data;
                console.log(_this.columns);
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