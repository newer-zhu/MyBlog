<template>
    <div>
        <el-header><Navibar/></el-header>

        <el-main>
            <el-row :gutter="30">
                <UserInfo :user="subUser" :drawer.sync="drawer"></UserInfo>
                <el-col :span="18" >
                    <div class="blogDetail">
                        <el-row>
                            <el-col :span="20">
                                <h1 style="">{{blog.title}}</h1>
                            </el-col>

                            <el-col :span="4">
                                <p style="font-size: 20px;color: teal" class="el-icon-view">{{' '+blog.visitors}}</p>
                                <p class="el-icon-c-scale-to-original" style="font-size: 18px;color: teal">{{' '+ blog.createdAt}}</p>
                            </el-col>
                            <el-row v-show="ownBlog">
                                <el-col :offset="20" :span="2">
                                    <el-link icon="el-icon-delete" @click="delBlog" style="font-size: 18px">删除</el-link>
                                </el-col>
                                <el-col :span="2">
                                    <router-link :to="{name: 'BlogEdit', params:{blogId:blog.id}}">
                                        <el-link icon="el-icon-edit" style="font-size: 18px">编辑</el-link>
                                    </router-link>
                                </el-col>
                            </el-row>
                        </el-row>
                        <h3 style="color: #606266" v-show="blog.isFile == 0">{{blog.summary}}</h3>
                        <el-divider/>
                        <el-row>
<!--                            文件显示-->
                            <div v-if="blog.isFile == 1">
                                <el-row :gutter="10">
                                    <el-col :span="10" >
                                        <el-card shadow="never">
                                            <div style="padding: 10px;">
                                                <p class="el-icon-folder-checked">{{blog.content}}</p>
                                                <div class="bottom">
                                                    <el-link type="success" :href="link" class="button el-icon-download">下载</el-link>
                                                </div>
                                            </div>
                                        </el-card>
                                    </el-col>
                                    <el-col :span="10" style="">
                                        <h3 style="color: #606266">{{blog.summary}}</h3>
                                    </el-col>
                                </el-row>
                            </div>

<!--                            正文显示-->
                            <div class="markdown-body" v-highlight v-else >
                                <el-row style="margin-left: 5px">
                                    <VueMarkdown :source="blog.content"></VueMarkdown>
                                </el-row>
                            </div>
                            <el-dialog
                                    title="收藏"
                                    :visible.sync="columnList.columnVisible"
                                    width="30%">
                                <span>请选择收藏列表</span>
                                <el-select v-model="columnList.selectColumn" placeholder="请选择">
                                    <el-option
                                            v-for="item in columnList.columns"
                                            :key="item.id"
                                            :label="item.cname"
                                            :value="item.id">
                                    </el-option>
                                </el-select>
                                <span slot="footer" class="dialog-footer">
                                    <el-button @click="columnList.columnVisible = false">取 消</el-button>
                                    <el-button type="primary" @click="get2Column(columnList.selectColumn)">确 定</el-button>
                                </span>
                            </el-dialog>
                            <el-button @click="get2Column(-1)" type="danger" class="el-icon-star-off" style="margin-left: 50%">收藏</el-button>
                        </el-row>
<!--                        评论组件-->
                        <el-row>
                            <comment ref="comment"
                                     :authorId="this.checkUserId"
                                     :commentNum="this.commentNum"
                                     :commentList="this.commentList"
                                     @doSend="this.commentSend"
                                     @doChidSend="this.childSend"
                                     :avatar="blog.user.avatar"
                            ></comment>
                        </el-row>
                    </div>
                </el-col>
                <el-col :span="6">
                    <div  style="margin-left: 20px">
                        <div @click="openInfo(blog.user, false)" style="text-align: center">
                            <el-avatar :src="blog.user.avatar" :size="60" />
                            <p class="el-icon-user-solid" style="font-size: 20px; color: #303133; display: block">{{blog.user.username}}</p>
                        </div>
                        <div style="background-color: #faeaef; width: 100%;border-radius: 5px" >
                            <el-link v-for="(t, i) in blog.tags" :underline="false">
                                <router-link :to="{name: 'TagDetail', params: {tagId: t.id}}">
                                    <el-tag
                                      style="margin: 5px 0 5px 5px;font-size: 15px;"
                                      :key="i"
                                      :type="tagColor[(i+1)%5]"
                                      :disable-transitions="true"
                                      :hit="true"
                                      effect="dark">
                                        {{ t.tagName }}
                                    </el-tag>
                                </router-link>
                            </el-link>
                        </div>
                        <router-link v-if="blog.project != null" replace :to="{name: 'ProjectDetail', params:{projectId:blog.project.id}}">
                            <el-card shadow="hover" style="border-radius: 4px; margin-top: 5px;">
                                <el-row>
                                    <el-col :span="16">
                                        <h3>{{blog.project.projectName}}</h3>
                                        <p style="font-size: 20px; color: #409eff; display: inline" class="el-icon-star-on">{{blog.project.rates}}</p>
                                    </el-col>
                                    <el-col style="color: #8c939d" :span="8">
                                        <p>{{blog.project.createAt}}</p>
                                    </el-col>
                                </el-row>
                            </el-card>
                        </router-link>
                    </div>
                    <div style="margin-left: 20px; margin-top: 5px">
                        <h3 style="color: #909399">相关笔记</h3>
                        <div v-for="s in suggestions">
                            <div class="suggestion">
                                <router-link :to="{name: 'BlogDetail', params:{blogId: s.id}}">
                                    <div v-html="s.title" v-if="blog.tags.length == 0" style="font-size: 20px; font-weight: 600;color: #565655;">
                                        <i style="font-size: 18px; margin-left: 10px" :class="s.isFile ? 'el-icon-folder':'el-icon-document'"></i>
                                    </div>
                                    <div v-else style="font-size: 20px; font-weight: 600;color: #565655;">
                                        {{s.title}} <i style="font-size: 18px; margin-left: 10px" :class="s.isFile ? 'el-icon-folder':'el-icon-document'"></i>
                                    </div>
                                </router-link>
                                <p style="color: #83878f">{{s.summary.slice(0, 35) + '...'}}</p>
                                <div>
                                    <el-link @click="openInfo(s.user, true)" :underline="false" style="font-size: 18px;">{{s.user.username}}</el-link>
                                    <div style="color: #909399; display: inline; margin-left: 50px">{{s.createdAt.slice(0, 11)}}</div>
                                    <div class="el-icon-view" style="font-size: 18px; color: #8c939d;margin-left: 10px">{{s.visitors}}</div>
                                </div>
                            </div>
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
    import comment from 'bright-comment'
    import UserInfo from "../../components/common/UserInfo";
    export default {
        name: "BlogDetail",
        components: {UserInfo, Navibar,VueMarkdown,comment},
        data(){
            return {
                checkUserId: 1,//当前登录用户Id
                blog: {
                    id: '',
                    title: '',
                    summary: '',
                    content: '',
                    userId: '',
                    isFile: 0,
                    visitors: 1,
                    tags: [],
                    project: {},
                    user: {}
                },
                columnList: {
                    columnVisible: false,
                    selectColumn: '',
                    columns: []
                },
                commentList: [],
                commentNum: 0,
                ownBlog: true,
                tagColor: ['', 'info', 'success', 'warning', 'danger'],
                drawer: false,
                suggestions: [],
                subUser: {}
            }
        },
        computed: {
            link(){
                return "http://localhost:8081/media/filedownload/" + this.blog.id
            },
        },
        watch: {
            '$route'(to, from){
                this.init();
            }
        },
        methods: {
            openInfo(user, load){
                if (load == true){
                    this.$axios.get("/user/"+user.id).then(res => {
                        this.subUser = res.data.data;
                    })
                }else {
                    this.subUser = user;
                }
                this.drawer = ! this.drawer;
            },
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
                            if (this.blog.project != null)
                                this.$router.push("/projectdetail/"+this.blog.project.id);
                            else
                                this.$router.push("/home");
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
            get2Column(id){
                if (id === -1){
                    this.$axios("/column/getcolumnbyuserid/"+this.checkUserId).then((res)=> {
                        this.columnList.columns = res.data.data;
                    });
                    this.columnList.columnVisible = true;
                }else {
                    if (id){
                        this.$axios.get("/column/blogtocolumn/"+this.blog.id+"?columnId="+id);
                    }
                    this.columnList.columnVisible = false;
                }
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
            },
            loadRelative(arr){
                this.$axios.post("/blog/relative/"+this.blog.id, arr).then(res => {
                    this.suggestions = res.data.data;
                    console.log(res.data.data);
                })
            },
            init(){
                let _this = this;
                this.checkUserId = this.$store.getters.getUser.id;
                const blogId = this.$route.params.blogId;//请求路径上的blogId参数值
                if (blogId != undefined){
                    this.$axios.get('/blog/' + blogId).then(res => {
                        this.blog = res.data.data;
                        //加载推荐
                        let arr = [];
                        for (let i in this.blog.tags){
                            arr.push(this.blog.tags[i].id);
                        }
                        this.loadRelative(arr);
                        //加载评论
                        this.$axios.get("/comment/getlistbyblogid/" + this.blog.id).then(res => {
                            this.commentList = res.data.data;
                            for(let i in this.commentList){
                                this.commentNum += this.commentList[i]['childrenList'].length + 1;
                            }
                        });
                    });
                    // 把md格式的数据渲染成css
                    let  MarkDown = require("markdown-loader");
                    let md = new MarkDown();
                    let  re = md.render(_this.blog.content);
                    this.blog.content = re;
                }
            }
        },
        created(){
            this.init();
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
    .markdown-body{
        font-size: 20px;
    }
    .suggestion{
        background-color: #fff;
        font-style: normal;
        margin-left: 15px;
        padding: 10px 10px 12px 5px;
        border-top: 1px solid #DCDFE6;
        border-radius: 2px
    }
</style>