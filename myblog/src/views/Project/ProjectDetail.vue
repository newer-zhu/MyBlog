<template>
    <div>
        <el-header>
            <Navibar/>
        </el-header>
        <el-main>

            <el-row>
                <UserInfo :user="project.projectUser" :drawer.sync="drawer"></UserInfo>
                <el-col :span="9">
                    <h1>{{project.projectName}}</h1>
                </el-col>
                <el-col :span="2" :offset="1">
                    <div @click="drawer = !drawer">
                        <div style="padding-left: 35px">
                            <el-avatar :size="50" :src="project.projectUser.avatar"></el-avatar>
                        </div>
                        <div  style="text-align: center">
                            <span style="font-size: 25px">{{project.projectUser.username}}</span>
                        </div>
                    </div>
                </el-col>
                <el-col :span="3" :offset="5">
                    <div>
                        <span>项目评分</span>
                        <el-rate
                                v-model="project.rates"
                                disabled
                                show-score
                                text-color="#ff9900"
                                score-template="{value}">
                        </el-rate>
                    </div>
                    <div style="padding-top: 10px">
                        <span>个人打分</span>
                        <el-rate
                                @change="rate"
                                text-color="#ff9900"
                                show-score
                                :allow-half="true"
                                v-model="rates"
                                :colors="colors">
                        </el-rate>
                    </div>
                </el-col>
            </el-row>
            <el-divider/>

<!--            主体-->
            <el-row>
                <el-col :span="16">
                    <el-row>
<!--                        展示图-->
                        <el-carousel :interval="4000" type="card" height="200px">
                            <el-carousel-item v-for="item in imgSrc" :key="item">
                                <el-image
                                        :src="item"
                                        >
                                </el-image>
                            </el-carousel-item>
                        </el-carousel>
                        <!--                侧边-->
                        <el-col :span="4">
                            <h3>项目进度</h3>
                            <div style="height: 500px">
                                <el-steps direction="vertical":active="1">
                                    <el-step title="步骤 1" description="这是一段很长很长很长的描述性文字"></el-step>
                                    <el-step title="步骤 2" description="这是一段很长很长很长的描述性文字"></el-step>
                                    <el-step title="步骤 3" description="这段就没那么长了"></el-step>
                                </el-steps>
                            </div>
                        </el-col>
                        <el-col :span="20" >
                            <el-row>
                                <el-col :span="21">
                                    <el-timeline>
                                        <el-timeline-item
                                                v-for="(card, index) in page.blogs"
                                                :key="index"
                                                icon="el-icon-check"
                                                type="primary"
                                                color="#FF66E9"
                                                size="large"
                                                placement="top"
                                                :timestamp="card.createdAt">
                                            <router-link :to="{name: 'BlogDetail', params: {blogId: card.id, projectId: project.projectId}}">
                                                <el-card shadow="hover" class="box-card">
                                                    <div slot="header" class="clearfix">
                                                        <span>{{card.title}}</span>
                                                    </div>
                                                    <el-row>
                                                        <el-col :span="18">
                                                            <p>{{card.summary}}</p>
                                                        </el-col>
                                                        <el-col style="color: #8c939d" :span="6">
                                                            <i :class="card.isFile ? 'el-icon-folder':'el-icon-reading'"></i>
                                                        </el-col>
                                                    </el-row>
                                                </el-card>
                                            </router-link>
                                        </el-timeline-item>
                                    </el-timeline>
                                    <el-pagination
                                            style="text-align: center"
                                            background
                                            :hide-on-single-page="true"
                                            @current-change="loadList"
                                            :current-page="page.currentPage"
                                            layout="prev, pager, next"
                                            :page-count="page.total">
                                    </el-pagination>
                                </el-col>
                                <el-col :span="3">
                                    <el-tooltip class="item" effect="dark" content="新建" placement="right-start">
                                        <el-button @click="edit" type="primary" icon="el-icon-edit" circle></el-button>
                                    </el-tooltip>
                                    <br>
                                    <el-tooltip class="item" effect="dark" :content="favoriteInfo" placement="right-start">
                                        <el-button  :type="isFavorite" @click="favorite" icon="el-icon-star-off" circle></el-button>
                                    </el-tooltip>
                                    <br>
                                    <el-tooltip class="item" effect="dark" content="删除" placement="right-start">
                                        <el-button type="danger" icon="el-icon-delete" circle></el-button>
                                    </el-tooltip>
                                    <br>
                                </el-col>
                            </el-row>
                        </el-col>
                    </el-row>
                </el-col>
                <el-col :span="7" :offset="1">
                    <el-collapse v-model="activeItem">
                        <el-collapse-item v-for="(item, index) in project.overview" :title="item.title" :name="index">
                            <div style="font-size: 14px">{{item.content}}</div>
                        </el-collapse-item>
                    </el-collapse>
                    <el-tabs style="padding-top: 50px"  type="card" >
                        <el-tab-pane label="时间动态" name="first">
                            <div>
                                <el-calendar v-model="date">
                                </el-calendar>
                            </div>
                        </el-tab-pane>
                        <el-tab-pane label="配置管理" name="second">配置管理</el-tab-pane>
                        <el-tab-pane label="角色管理" name="third">角色管理</el-tab-pane>
                        <el-tab-pane label="定时任务" name="fourth">定时任务补偿</el-tab-pane>
                    </el-tabs>
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
                </el-col>
            </el-row>
        </el-main>
    </div>
</template>

<script>
    import Navibar from "components/common/Navibar";
    import UserInfo from '../../components/common/UserInfo'
    export default {
        name: "ProjectDetail",
        components: {Navibar, UserInfo},
        data(){
            return{
                userId: 0,
                project: {
                    projectId: -1,
                    startUser: 0,
                    projectUser: {},
                    projectName: '项目标题',
                    rates: 3.7,
                    favorite: 0,
                    overview: [{title: '', content: ''}],
                },
                imgSrc: [require('@/assets/img/view1.jpg'),require('@/assets/img/view2.jpg'),require('@/assets/img/view3.jpg')],
                activeItem: [],
                isFavorite: "info",
                page: {
                    blogs: [{
                        title: '万事开头难',
                        summary: '开始你的第一步帖子吧！',
                        createdAt: '2000-10-30 00:00:00',
                        content: '',
                        userId: '',
                        id: '',
                        isFile: 0,
                        visitors: 0,
                    }],
                    total: 1,
                    currentPage: 1,
                },
                date: new Date(),
                rates: 0,
                colors: ['#2d9abf', '#F7BA2A', '#FF9900'],
                drawer: false
            }
        },
        computed: {
            favoriteInfo(){
                return this.isFavorite === "info" ? "收藏" : "取消收藏"
            }
        },
        methods: {
            openInfo(){this.drawer = true;},
            loadList(current){
                this.$axios.get("/project/blogs/"+this.project.projectId+"?page="+current).then(res => {
                    if (res.data.data.length != 0){
                        this.page.blogs = res.data.data.blogList;
                        this.page.total = res.data.data.total;
                    }
                });
            },
            edit(){
                this.$router.push({
                    name: 'BlogEdit',
                    params: {
                        projectId: this.project.projectId
                    }
                })
            },
            rate(r){
                console.log(r);
            },
            success(msg) {
                this.$message({
                    message: msg,
                    type: 'success'
                });
            },
            error(msg) {
                if (msg.length == 0){
                    msg = '出错啦'
                }
                this.$message.error(msg);
            },
            favorite(){
                if (this.isFavorite === "info"){
                    this.$axios.get("project/favorite/"+this.$store.getters.getUser.id+"/"+this.project.projectId).then(res => {
                        if (res.data.code === 200){
                            this.success(res.data.data);
                            this.isFavorite = "warning"
                        }else {
                            this.error(res.data.msg);
                        }
                    })
                }else {
                    this.$axios.get("project/cancel/"+this.$store.getters.getUser.id+"/"+this.project.projectId).then(res => {
                        if (res.data.code === 200){
                            this.success(res.data.data);
                            this.isFavorite = "info"
                        }else {
                            this.error(res.data.msg);
                        }
                    })
                }
            }
        },
        created(){
            this.userId = this.$store.getters.getUser.id;
            this.$axios.get("/project/"+this.$route.params.projectId).then(res => {
                this.project = res.data.data;
                this.loadList(1);
                this.$axios.get("/project/isfavorite/"+this.userId+"/"+this.project.projectId).then(res => {
                    if (res.data.data === true)
                        this.isFavorite = 'warning';
                    else
                        this.isFavorite = 'info';
                    console.log(res.data.data);
                });
            });
        },
    }
</script>

<style scoped>
    .el-carousel__item:nth-child(2n) {
        background-color: #99a9bf;
    }

    .el-carousel__item:nth-child(2n+1) {
        background-color: #d3dce6;
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

    .box-card {
        width: 620px;
    }
    .el-carousel__item h3 {
        color: #475669;
        font-size: 18px;
        opacity: 0.75;
        line-height: 300px;
        margin: 0;
    }
</style>