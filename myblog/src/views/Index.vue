<template>
    <div>
        <el-container>
            <el-header>
                <Navibar></Navibar>
            </el-header>

            <el-container class="main">
                <el-aside class="aside" width="400px">
                    <el-row>
                        <el-col :span="23" :offset="1">
                            <el-input placeholder="请输入搜索内容" v-model="input">
                                <el-button  style="color: #409EFF" slot="append" icon="el-icon-search"></el-button>
                            </el-input>
                        </el-col>
                    </el-row>
                    <el-row>

                    </el-row>
                </el-aside>

                <el-main>
                    <el-row :gutter="20">
                        <el-col :span="16">

<!--                            <div  v-show="isShowList">-->
<!--                                <div v-for="(card,index) in this.page.blogs" style="padding-bottom: 5px;">-->
<!--                                    <router-link :to="{name: 'BlogDetail', params:{blogId:card.id}}">-->
<!--                                        <el-card shadow="hover" style="height: 125px;  border-color: #8c939d">-->
<!--                                            <el-row>-->
<!--                                                <el-col :span="18">-->
<!--                                                    <h3>{{card.title}}</h3>-->
<!--                                                    <p>{{card.summary}}</p>-->
<!--                                                </el-col>-->
<!--                                                <el-col style="color: #8c939d" :span="6">-->
<!--                                                    <p>{{card.createdAt}}</p>-->
<!--                                                    <i :class="card.isFile ? 'el-icon-folder':'el-icon-document'"></i>-->
<!--                                                </el-col>-->
<!--                                            </el-row>-->
<!--                                        </el-card>-->
<!--                                    </router-link>-->
<!--                                </div>-->
<!--                                <el-pagination-->
<!--                                        style="text-align: center"-->
<!--                                        background-->
<!--                                        :hide-on-single-page="true"-->
<!--                                        @current-change="handCurrentChange"-->
<!--                                        :current-page="page.currentPage"-->
<!--                                        layout="prev, pager, next"-->
<!--                                        :page-count="page.total">-->
<!--                                </el-pagination>-->
<!--                            </div>-->

                            <router-view name="listDetail"/>
                            <router-view name="projectList"/>
                        </el-col>

                        <el-col :span="8">
                            <div style="padding-bottom: 3px">
                                <el-card shadow="never">
                                    <div slot="header" class="clearfix">
                                        <span><i style="font-size: 20px" class="el-icon-notebook-2"></i> 博客分类</span>
                                        <el-button @click="createCol" style="padding: 3px; padding-left: 100px" class="el-icon-document-add" type="text">新建</el-button>
                                        <el-button v-if="!isShowDelCol" @click="changeDelCol" style="float: right; padding: 3px 0" class="el-icon-delete" type="text">删除</el-button>
                                        <el-button v-else @click="changeDelCol" style="float: right; padding: 3px 0" type="text">取消</el-button>
                                    </div>
                                    <div>
                                        <el-row type="flex"  style="line-height: 1px; height: 15px">
                                            <el-col :span="18">
                                                <router-link :to="{name: 'ListDetail', params: {columnId: -1}}">
                                                    <h2 style="color: rgba(35,133,124,0.99)">{{"我的文章"}}</h2>
                                                </router-link>
                                            </el-col>
                                        </el-row>
                                        <el-divider/>
                                    </div>
                                    <div v-for="(column, index) in this.columns" :key="column.c_name" class="text item">
                                        <el-row type="flex"  style="line-height: 1px; height: 15px">
                                            <el-col :span="18">
                                                <router-link :to="{name: 'ListDetail', params: {columnId: column.id}}">
                                                    <h2 style="color: #3449bf">{{column.cname+' ('+column.count+')'}}</h2>
                                                </router-link>
                                            </el-col>
                                            <el-col :span="6">
                                                <el-button type="danger" size="small" @click="delCol(column.id)" v-show="isShowDelCol">删除</el-button>
<!--                                                <el-checkbox  v-model="checkList" style="margin-top: 20px; border-color: red" :label="column.id">{{''}}</el-checkbox>-->
                                            </el-col>
                                        </el-row>
                                        <el-divider/>
                                    </div>
                                </el-card>
                            </div>

                            <div style="padding-bottom: 3px">
                                <router-link :to="{path: '/home/projectList/'+this.userId+'/n'}">
                                    <el-card shadow="hover">
                                        <div>
                                            <span><i style="font-size: 20px" class="el-icon-folder-opened"></i> 项目集</span>
                                        </div>
                                    </el-card>
                                </router-link>
                            </div>

                            <div>
                                <router-link :to="{path: '/home/projectList/'+this.userId+'/y'}">
                                    <el-card shadow="hover">
                                        <div>
                                            <span><i style="font-size: 20px" class="el-icon-collection"></i> 收藏项目</span>
                                        </div>
                                    </el-card>
                                </router-link>
                            </div>
                        </el-col>
                    </el-row>
                    <el-backtop :right="320" :bottom="50">
                        <div
                                style="{
                                    height: 100%;
                                    width: 100%;
                                    background-color: #18caf6;
                                    box-shadow: 0 0 5px rgba(0,0,0, .12);
                                    text-align: center;
                                    line-height: 40px;
                                    color: #fa0e17;
                                  }"
                        >
                            UP
                        </div>
                    </el-backtop>
                    <el-dialog
                            title="提示"
                            :visible.sync="isVisible"
                            width="30%"
                            center>
                        <span>分栏名称</span>
                        <el-input style="padding-top: 5px" v-model="newCol"></el-input>
                        <span slot="footer" class="dialog-footer">
    <el-button @click="isVisible = false">取 消</el-button>
    <el-button type="primary" @click="addColumn">确 定</el-button>
  </span>
                    </el-dialog>
                </el-main>
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
    import ListDetail from "./Blog/ListDetail"
    export default {
        name: "Index",
        components: {ListDetail, Footer,Navibar},
        data() {
            return{
                input: '',
                userId: 0,
                columns: [],
                projects: [],
                favorite: [],
                isShowDelCol: false,//是否显示删除专栏按钮
                isVisible: false,
                newCol: '',
                isShowList: true,
                checkList: [],
                randColor: {//分栏字体显示
                    color: this.getColor(),
                    fontSize: '20px'
                },
                page: {
                    blogs: [],
                    total: 1,
                    currentPage: 1,
                }
            }
        },
        methods: {
            handCurrentChange(current){
                this.$axios.get("/blog/getpagesbyuserid/"+this.userId+"?page="+current).then(res => {
                    this.page.blogs = res.data.data.blogList;
                })
            },
            createWebSocket(){
                let websocket = new WebSocket('ws://127.0.0.1:9000/timeline/'+this.userId)
            },
            getColor(){
                let r = Math.floor(Math.random()*255);
                let g = Math.floor(Math.random()*255);
                let b = Math.floor(Math.random()*255);
                return 'rgba('+ r +','+ g +','+ b +',1)';
            },
            changeDelCol(){
                this.isShowDelCol = !this.isShowDelCol;
            },
            createCol(){
                this.isVisible = !this.isVisible;
            },
            //新建分栏函数
            addColumn(){
                this.$axios.post("/column/create", {userId: this.userId, cname: this.newCol, count: 0}).then(res => {
                    if (res.data.code === 200){
                        this.success("新建分栏成功");
                        this.loadColumn();
                    }else {
                        this.error(res.data.msg)
                    }
                    this.newCol = '';
                    this.isVisible = false;
                })
            },
            //删除分栏的函数
            delCol(columnId){
                this.$confirm('此操作将永久删除分栏, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.$axios.get("/column/delete/"+columnId).then((res) =>{
                        if (res.data.code === 200){
                            this.success("删除成功")
                            this.loadColumn();
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
            },
            isShow(){
                if (this.checkList.length != 0){
                    this.$confirm('此操作将永久删除分栏, 是否继续?', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(() => {
                        this.$axios.post("/column/delcolumnbyid", this.checkList).then((res) =>{
                            if (res.data.code === 200){
                                this.success("删除成功");
                                this.$axios("/column/getcolumnbyuserid/"+_this.userId).then((res)=>{
                                    _this.columns = res.data.data;
                                });
                                // this.$router.go(0)
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
            loadColumn(){
                this.$axios("/column/getcolumnbyuserid/"+this.userId).then((res)=>{
                    this.columns = res.data.data;
                });
            },

        },
        created() {
            const _this = this;
            this.userId = this.$store.getters.getUser.id;
            this.isShowList = this.$route.path === '/home';
            console.log(this.$route.path);
            this.loadColumn();
        },
        watch: {
            '$route'(path){
                this.isShowList = path.path === '/home';
            }
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