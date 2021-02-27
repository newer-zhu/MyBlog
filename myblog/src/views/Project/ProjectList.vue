<template>
    <div>
        <div v-if="this.page.projects.length === 0">
            <el-image :src="nothing">
            </el-image>
        </div>
        <div style="padding-bottom: 10px">
            <el-button type="primary" v-if="!isShowDelPro" @click="changeIsShowDelPro" class="el-icon-delete">管理</el-button>
            <el-button type="info" v-else @click="changeIsShowDelPro">取消</el-button>
        </div>
        <div v-for="(pro,index) in this.page.projects" style="padding-bottom: 5px;">
            <el-card shadow="hover" style="height: 125px;  border-color: #8c939d">
                <el-row>
                    <router-link :to="{name: 'ProjectDetail', params:{projectId:pro.id}}">
                        <el-col :span="18">
                            <h3>{{pro.projectName}}</h3>
                            <p style="font-size: 20px; color: #409eff; display: inline" class="el-icon-star-on">{{ pro.rates}}</p>
                        </el-col>
                    </router-link>
                    <el-col style="color: #8c939d" :span="6">
                        <p>{{pro.createAt}}</p>
                        <i style="font-size: 25px" class='el-icon-folder-opened'></i>
                        <el-button style="float: right" v-show="isShowDelPro" type="danger" size="small" @click="delPro(pro.id)">删除</el-button>
                    </el-col>
                </el-row>
            </el-card>
        </div>
        <el-pagination
                style="text-align: center"
                background
                :hide-on-single-page="true"
                @current-change="handCurrentChange"
                :current-page="page.currentPage"
                layout="prev, pager, next"
                :page-count="page.total">
        </el-pagination>
    </div>
</template>

<script>
    export default {
        name: "ProjectList",
        data(){
            return {
                nothing: require("../../assets/img/nothing.webp"),
                page: {
                    projects: [],
                    total: 1,
                    currentPage: 1,
                },
                userId: 0,
                isShowDelPro: false,//是否显示删除项目按钮
            }
        },
        methods: {
            handCurrentChange(current){
                if (this.$route.params.isFavorite === 'y'){
                    this.loadFavorite(current);
                }else {
                    this.loadProject(current);
                }
            },
            loadProject(current){
                this.$axios.get("/project/projects/"+this.userId+'?page='+current).then(res => {
                    this.page.projects = res.data.data.projectList;
                    this.page.total = res.data.data.total;
                })
            },
            loadFavorite(current){
                this.$axios.get("/project/getfavorite/"+this.userId).then(res => {
                    this.page.projects = res.data.data.favoriteList;
                    this.page.total = res.data.data.total;
                })
            },
            changeIsShowDelPro(){
               this.isShowDelPro = !this.isShowDelPro;
            },
            //删除项目的函数
            delPro(projectId){
                this.$confirm('此操作将永久删除此项目, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.$axios.get("/project/delete/"+projectId).then((res) =>{
                        if (res.data.code === 200){
                            this.success("删除成功")
                            this.loadProject();
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
        },
        watch: {
            '$route'(to, from){
                if (to.params.isFavorite === 'y'){
                    this.loadFavorite(1);
                }else {
                    this.loadProject(1);
                }
            }
        },
        created(){
            this.userId = this.$route.params.userId;
            if (this.$route.params.isFavorite === 'y'){
                this.loadFavorite(this.page.currentPage);
            }else {
                this.loadProject(this.page.currentPage);
            }
        }
    }
</script>

<style scoped>

</style>