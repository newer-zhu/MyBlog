<template>
    <div>
        <el-header><Navibar/></el-header>

        <el-main>
            <el-row>
                <el-col :span="20">
                    <div class="blogDetail">
                        <h2>{{blog.title}}</h2>
                        <el-link icon="el-icon-delete" @click="delBlog">删除</el-link>
                        <br/>
                        <router-link :to="{name: 'BlogEdit', params:{blogId:blog.id}}">
                            <el-link v-if="ownBlog" icon="el-icon-edit">编辑</el-link>
                        </router-link>
                        <h3>{{blog.summary}}</h3>
                        <el-divider/>
                        <div class="markdown-body">
                            <VueMarkdown :source="blog.content"></VueMarkdown>
                        </div>
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
    export default {
        name: "BlogDetail",
        components: {Navibar,VueMarkdown},
        data(){
            return {
                blog: {
                    title: '',
                    summary: '',
                    content: '',
                    userId: '',
                    id: ''
                },
                ownBlog: true
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
            }
        },
        created(){
            const userId = this.$store.getters.getUser.id;
            const blogId = this.$route.params.blogId;
            this.blog.userId = userId;
            this.$axios.get("/blog/getbyid/" + blogId).then(res => {
                const blog = res.data.data
                if (blog){
                    this.blog.id = blogId
                    this.blog.title = blog.title
                    this.blog.summary = blog.summary
                    this.blog.content = blog.content
                    //把md格式的数据渲染成css
                    let  MarkDown = require("markdown-loader")
                    let md = new MarkDown()
                    let  res = md.render(this.blog.content)
                    this.blog.content = res
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
</style>