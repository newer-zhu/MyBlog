<template>
    <div>
        <div style="padding-bottom: 10px">
            <el-row gutter="90">
                <el-col :span="2">
                    <el-button type="primary" v-if="!isShowDelCol" @click="changeShowDelCol" class="el-icon-delete">管理</el-button>
                    <el-button type="info" class="el-icon-circle-close" v-else @click="changeShowDelCol">取消</el-button>
                </el-col>
                <el-col :span="20">
                    <el-image :src="require('../../assets/img/longback.png')" style="height: 40px; width: 640px; border-radius: 2px">
                    </el-image>
                </el-col>
            </el-row>
        </div>
        <div v-show="page.blogs.length == 0">
            <el-image :src="require('../../assets/img/empty.png')" class="empty">
            </el-image>
        </div>
        <div  v-for="(card,index) in this.page.blogs" style="padding-bottom: 1px;">
            <el-card shadow="hover" style="height: 125px;  border-color: #e3effc">
                <el-row>
                    <router-link :to="{name: 'BlogDetail', params:{blogId:card.id}}">
                        <el-col :span="17">
                            <h3 style="color: #282c34; margin-top: 2px">{{card.title}}</h3>
                            <p style="color: #525252">{{card.summary}}</p>
                        </el-col>
                    </router-link>
                    <el-col style="color: #8c939d" :span="7">
                        <el-link type="danger" v-show="isShowDelCol" @click="delCol(card.id)" style="float: right">删除</el-link>
                        <p>{{card.createdAt}}</p>
                        <i style="font-size: 25px" :class="card.isFile ? 'el-icon-folder':'el-icon-document'">
                            <router-link v-if="card.project != null" :to="{name: 'ProjectDetail', params:{projectId:card.project.id}}">
                                <el-link type="success" style="display: inline;font-size: 15px">{{'《'+card.project.projectName+'》'}}</el-link>
                            </router-link>
                        </i>
                    </el-col>
                </el-row>
            </el-card>
        </div>
        <el-pagination
                style="text-align: center; padding-top: 25px"
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
    import Navibar from "../../components/common/Navibar";
    export default {
        name: "ListDetail",
        components: {Navibar},
        data(){
            return {
                page: {
                    blogs: [],
                    total: 1,
                    currentPage: 1,
                },
                userId: 0,
                columnId: 0,
                isShowDelCol: false
            }
        },
        methods: {
            handCurrentChange(current){
                //全部文章
                if (this.$route.params.columnId === -1){
                    this.$axios("/blog/getpagesbyuserid/"+this.userId+"?page="+current).then((res) => {
                        this.page.blogs = res.data.data.blogList;
                        this.page.total = res.data.data.total;
                    });
                }else {
                    //分栏文章
                    this.$axios.get("/blog/getpagesbycolumnid/"+this.columnId+"?page="+current).then(res => {
                        this.page.blogs = res.data.data.blogList;
                        this.page.total = res.data.data.total;
                    })
                }
            },
            changeShowDelCol(){
                this.isShowDelCol = !this.isShowDelCol;
            },
            loadList(){
                if (this.columnId == -1){
                    this.$axios("/blog/getpagesbyuserid/"+this.userId).then((res) => {
                        this.page.blogs = res.data.data.blogList;
                        console.log(this.page.blogs);
                        this.page.total = res.data.data.total;
                    });
                }else {
                    this.$axios.get("/blog/getpagesbycolumnid/"+ this.columnId).then(res => {
                        this.page.blogs = res.data.data.blogList;
                        this.page.total = res.data.data.total;
                    });
                }
            },
            delCol(blogId){
                if (this.columnId == -1){
                    this.$axios.get("/blog/delblogbyid?blogId="+blogId).then(res => {
                        this.$message({
                            message: '删除成功'
                        });
                    })
                }else {
                    this.$axios.get("/column/dismiss/"+blogId+"/"+this.columnId);
                }
                this.loadList();
            },

        },
        watch: {
            '$route'(to, from){
                this.columnId = to.params.columnId;
                this.loadList();
            },
        },
        created() {
            this.columnId = this.$route.params.columnId;
            this.userId = this.$store.getters.getUser.id;
            this.loadList();
        }
    }
</script>

<style scoped>
    .text {
        font-size: 14px;
    }

    .item {
        padding: 18px 0;
    }

    .box-card {
        width: 480px;
        margin: 0 auto;
    }
</style>