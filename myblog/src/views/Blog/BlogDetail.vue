<template>
    <div>
        <el-header><Navibar/></el-header>

        <el-main>
            <el-row>
                <el-col :span="18">
                    <div class="blogDetail">
                        <el-row>
                            <el-col :span="20">
                                <h1>{{blog.title}}</h1>
                            </el-col>
                            <el-col :span="2">
                                <p style="font-size: 20px;color: teal" class="el-icon-view">{{' '+blog.visitors}}</p>
                            </el-col>
                        </el-row>
                        <el-row>
                            <el-col :offset="20" :span="2">
                                <el-link icon="el-icon-delete" @click="delBlog" style="font-size: 18px">删除</el-link>
                            </el-col>
                            <el-col :span="2">
                                <router-link :to="{name: 'BlogEdit', params:{blogId:blog.id}}">
                                    <el-link v-if="ownBlog" icon="el-icon-edit" style="font-size: 18px">编辑</el-link>
                                </router-link>
                            </el-col>
                        </el-row>
                        <h3 v-show="blog.isFile == 0">{{blog.summary}}</h3>
                        <el-divider/>
                        <el-row>
<!--                            文件显示-->
                            <div v-if="blog.isFile == 1">
                                <el-row :gutter="10">
                                    <el-col :span="10" >
                                        <el-card shadow="never" :body-style="{ padding: '0px' }">
                                            <img src="../../assets/img/filepreview.jpg" class="image">
                                            <div style="padding: 14px;">
                                                <div class="bottom clearfix">
                                                    <!--                                        <time class="time">{{ currentDate }}</time>-->
                                                    <el-link type="success" :href="link" class="button el-icon-download">下载</el-link>
                                                </div>
                                            </div>
                                        </el-card>
                                    </el-col>
                                    <el-col :span="10" style="background-color: antiquewhite">
                                        <h2>{{blog.summary}}</h2>
                                    </el-col>
                                </el-row>
                            </div>

<!--                            正文显示-->
                            <div class="markdown-body" v-highlight v-else>
                                <el-row style="margin-left: 5px">
                                    <VueMarkdown :source="blog.content"></VueMarkdown>
                                </el-row>
                            </div>
                        </el-row>

<!--                        评论组件-->
                        <el-row>
                            <comment ref="comment"
                                     :authorId="this.checkUserId"
                                     :commentNum="this.commentNum"
                                     :commentList="this.commentList"
                                     @doSend="this.commentSend"
                                     @doChidSend="this.childSend"
                                     :avatar="require('assets/img/view1.jpg')"
                            ></comment>
                        </el-row>
                    </div>
                </el-col>
            </el-row>
        </el-main>
    </div>
</template>

<script>
    import Navibar from 'components/common/Navibar'
    import 'github-markdown-css/github-markdown.css'
    import VueMarkdown from 'vue-markdown'
    import comment from 'bright-comment'
    export default {
        name: "BlogDetail",
        components: {Navibar,VueMarkdown,comment},
        data(){
            return {
                checkUserId: 1,//当前登录用户Id
                blog: {
                    title: '',
                    summary: '',
                    content: '',
                    userId: '',
                    id: '',
                    isFile: 0,
                    visitors: 1,
                },
                commentList: [],
                commentNum: 0,
                ownBlog: true
            }
        },
        computed: {
            link(){
                return "http://localhost:8081/media/filedownload/" + this.blog.id
            }
        },
        methods: {
            delBlog(){
                this.$confirm('确定删除这篇博客吗？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.$axios.get("/blog/delblogbyid?blogId="+this.blog.id).then(res => {
                        if (res.data.code === 200){
                            this.$message({
                                message: '删除成功！',
                                type: 'success'
                            });
                            this.$router.push("/home")
                        }else {
                            this.$message.error("删除错误，请稍后再试")
                        }
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });
                });
            },
            commentSend(content){
                this.$axios.post("/comment/parentcomment/"+this.blog.id,
                    {commentUser: this.checkUserId, targetUser: this.blog.userId,
                    content: content, createDate: ''}).then(res =>{
                        if (res.data.code === 200){
                            this.loadComments();
                            this.success('评论成功');
                        }
                })
            },

            //参数（内容，被评论用户， 父评论）
            childSend(content, t_userId, targetId){
                this.$axios.post("/comment/childcomment/"+targetId,
                    {commentUser: this.checkUserId, targetUser: t_userId,
                        content: content, createDate: ''
                    }).then(res => {
                        if (res.data.code === 200){
                            this.loadComments();
                            this.success("评论成功");
                        }
                })
            },
            success(msg) {
                this.$message({
                    message: msg,
                    type: 'success'
                });
            },
            loadComments(){
                this.$axios.get("/comment/getlistbyblogid/" + this.blog.id).then(res => {
                    this.commentList = res.data.data;
                    for(let i in this.commentList){
                        this.commentNum += this.commentList[i]['childrenList'].length + 1;
                    }
                })
            }
        },
        created(){
            this.checkUserId = this.$store.getters.getUser.id;
            const blogId = this.$route.params.blogId;//请求路径上的blogId参数值
            this.$axios.get("/blog/getbyid/" + blogId).then(res => {
                const blog = res.data.data;
                if (blog){
                    this.blog.id = blogId
                    this.blog.title = blog.title
                    this.blog.summary = blog.summary
                    this.blog.content = blog.content
                    this.blog.userId = blog.userId;
                    this.blog.isFile = blog.isFile;
                    this.blog.visitors = blog.visitors;
                    //把md格式的数据渲染成css
                    let  MarkDown = require("markdown-loader")
                    let md = new MarkDown()
                    let  res = md.render(this.blog.content)
                    this.blog.content = res
                }
            });
            this.$axios.get("/comment/getlistbyblogid/" + blogId).then(res => {
                this.commentList = res.data.data;
                for(let i in this.commentList){
                    this.commentNum += this.commentList[i]['childrenList'].length + 1;
                }
            })
        }
    }
</script>

<style scoped>
    .blogDetail{
        box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
        width: 100%;
        min-height: 700px;
        padding: 20px;
    }
    .bottom {
        margin-top: 13px;
        line-height: 12px;
    }
    .button {
        padding: 0;
        float: right;
        font-size: 20px;
    }
    .clearfix:before,
    .clearfix:after {
        display: table;
        content: "";
    }
    .clearfix:after {
        clear: both
    }
    .markdown-body{
        font-size: 20px;
    }
</style>