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
                                :disabled="isRate"
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
                                            <el-card v-show="page.total == 0" shadow="hover" class="box-card">
                                                <div slot="header" class="clearfix">
                                                    <span>{{card.title}}</span>
                                                </div>
                                                <el-row>
                                                    <el-col :span="18">
                                                        <p>{{card.summary}}</p>
                                                    </el-col>
                                                    <el-col :span="6">
                                                        <p :class="card.isFile ? 'el-icon-folder':'el-icon-reading'"></p>
                                                    </el-col>
                                                </el-row>
                                            </el-card>
                                            <router-link :to="{name: 'BlogDetail', params: {blogId: card.id, projectId: project.id}}">
                                                <el-card v-if="page.total != 0" shadow="hover" class="box-card">
                                                    <div slot="header" class="clearfix">
                                                        <span>{{card.title}}</span>
                                                    </div>
                                                    <el-row>
                                                        <el-col :span="18">
                                                            <p>{{card.summary}}</p>
                                                        </el-col>
                                                        <el-col style="color: #8c939d;" :span="6">
                                                            <div style="margin-left: 10px;">
                                                                <i  style="font-size: 20px" :class="card.isFile ? 'el-icon-folder':'el-icon-reading'"></i>
                                                                <br>
                                                                <p  style="font-size: 15px" class="el-icon-view">{{card.visitors}}</p>
                                                            </div>
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
                                    <el-tooltip v-show="project.startUser != userId" class="item" effect="dark" content="申请" placement="right-start">
                                        <el-button  type="warning" @click="request" icon="el-icon-s-promotion" circle></el-button>
                                    </el-tooltip>
                                    <br>
                                    <el-popover
                                            placement="top"
                                            width="160"
                                            v-model="delVisible">
                                        <p>确定删除吗？</p>
                                        <div style="text-align: right; margin: 0">
                                            <el-button size="mini" type="text" @click="delVisible = false">取消</el-button>
                                            <el-button type="primary" size="mini" @click="delProject">确定</el-button>
                                        </div>
                                    </el-popover>
                                    <el-tooltip v-show="userId == project.startUser" class="item" effect="dark" content="删除" placement="right-start">
                                        <el-button type="danger"  @click="delVisible = true" icon="el-icon-delete" circle></el-button>
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
                    <el-tabs style="padding-top: 50px" >
                        <el-tab-pane @click="getBlogTime" label="时间动态" name="first">
                            <div>
                                <el-calendar v-model="calendar.date">
                                    <template slot="dateCell" slot-scope="{date, data}">
                                        <div :class="data.isSelected ? 'is-selected' : ''">
                                            {{data.day.split('-').slice(1).join('-')}}
                                        </div>
                                        <div v-show="calendar.blogTime.indexOf(data.day.toString()) != -1">
                                            <el-tag style="font-weight: bold; font-size: 18px" type="danger">{{calendar.timeMap[data.day.toString()]}}</el-tag>
                                        </div>
                                    </template>
                                </el-calendar>
                            </div>
                        </el-tab-pane>
                        <el-tab-pane label="标签一览" name="second">
                            <el-progress v-for="i in 5" :percentage="20" ></el-progress>
                        </el-tab-pane>
                        <el-tab-pane label="贡献人员" name="third">
                            <el-table
                                    :data="contributors"
                                    height="250"
                                    border
                                    style="width: 100%">
                                <el-table-column
                                        prop="date"
                                        label="日期"
                                        width="150">
                                </el-table-column>
                                <el-table-column
                                        prop="username"
                                        label="昵称"
                                        width="180">
                                </el-table-column>
                                <el-table-column
                                        prop="number"
                                        label="次数">
                                </el-table-column>
                            </el-table>
                        </el-tab-pane>
                        <el-tab-pane label="资料编辑" name="fourth">
                            <el-row>
                                <el-col>
                                    <el-form :model="project" ref="dynamicValidateForm" label-width="100px" >
                                        <el-form-item
                                                prop="projectName"
                                                label="项目名"
                                                :rules="[
                        { required: true, message: '请输入项目名', trigger: 'blur' },
                        { min: 3, max: 30, message: '长度在 3 到 30 个字符', trigger: 'blur' }
                        ]"
                                        >
                                            <el-input placeholder="3 到 30 个字符" v-model="project.projectName"></el-input>
                                        </el-form-item>
                                        <el-divider/>
                                        <el-form-item
                                                v-for="(domain, index) in project.overview"
                                                :label="domain.title"
                                                :key="domain.key"
                                                :rules="{required: true, message: '简介不能为空', trigger: 'blur'}">
<!--                                            <i class="el-icon-info" style="color: forestgreen">简介以“标题|内容的形式”，如“项目重点|掌握网络tcp协议的原理”</i>-->
                                            <el-input maxlength="50"
                                                      show-word-limit style="width: 90%; padding-right: 5px"
                                                      :rows="2" placeholder="简介内容"
                                                      type="textarea" v-model="domain.content"></el-input>
<!--                                            <el-button @click.prevent="removeDomain(domain)">删除</el-button>-->
                                        </el-form-item>
                                        <el-form-item>
                                            <el-button type="primary" @click="submitForm('dynamicValidateForm')">修改</el-button>
<!--                                            <el-button @click="addDomain">新增简介</el-button>-->
                                            <el-button @click="resetForm('dynamicValidateForm')">重置</el-button>
                                        </el-form-item>
                                    </el-form>
                                </el-col>
                            </el-row>
                        </el-tab-pane>
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
                    id: -1,
                    startUser: 0,
                    projectUser: {},
                    projectName: '项目标题',
                    rates: 3.7,
                    favorite: 0,
                    overview: [{title: '', content: ''}],
                },
                contributors: [],
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
                    total: 0,
                    currentPage: 1,
                },
                rates: 0,
                isRate: false,
                colors: ['#2d9abf', '#F7BA2A', '#FF9900'],
                drawer: false,
                //日历数据
                calendar: {
                    date: new Date(),
                    blogTime: [],
                    timeMap: {}
                },
                delVisible: false
            }
        },
        computed: {
            favoriteInfo(){
                return this.isFavorite === "info" ? "收藏" : "取消收藏"
            }
        },
        methods: {
            loadList(current){
                this.$axios.get("/project/blogs/"+this.project.id+"?page="+current).then(res => {
                    if (res.data.data.total != 0){
                        this.page.blogs = res.data.data.blogList;
                        this.page.total = res.data.data.total;
                    }
                });
            },
            getBlogTime(){
                this.$axios.get("/project/blogtime/"+this.project.id).then(res => {
                    this.calendar.timeMap = res.data.data;
                    this.calendar.blogTime = Object.keys(res.data.data);
                })
            },
            edit(){
                this.$router.push({
                    name: 'BlogEdit',
                    params: {
                        projectId: this.project.id
                    }
                })
            },
            rate(r){
                this.$axios.get("/project/rates/"+this.userId+"/"+this.project.id+"/"+r).then(res => {
                    this.isRate = true;
                    this.success(res.data.data);
                })
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
                    this.$axios.get("project/favorite/"+this.$store.getters.getUser.id+"/"+this.project.id).then(res => {
                        if (res.data.code === 200){
                            this.success(res.data.data);
                            this.isFavorite = "warning"
                        }else {
                            this.error(res.data.msg);
                        }
                    })
                }else {
                    this.$axios.get("project/cancel/"+this.$store.getters.getUser.id+"/"+this.project.id).then(res => {
                        if (res.data.code === 200){
                            this.success(res.data.data);
                            this.isFavorite = "info"
                        }else {
                            this.error(res.data.msg);
                        }
                    })
                }
            },
            //修改简介
            submitForm(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        this.$axios.post("/project/overview", this.project).then(res => {
                            this.success(res.data.data);
                        })
                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                })
            },
            resetForm(formName) {
                this.$refs[formName].resetFields();
            },
            removeDomain(item) {
                let index = this.project.overview.indexOf(item)
                if (index !== -1) {
                    this.project.overview.splice(index, 1)
                }
            },
            delProject(){
                this.$axios.get("/project/delete/"+this.project.id).then(res => {
                    this.$message.success(res.data.data);
                });
                this.delVisible = false;
                this.$router.replace({
                    path: '/home'
                })
            },
            request(){
                let _this = this;
                this.$axios.get("/project/request/"+this.userId+"/"+this.project.id).then(res => {
                    _this.$message.success(res.data.msg);
                    if (res.data.code == 200 && this.global.ws.readyState == 1){
                        _this.global.ws.send(this.project.id);
                    }
                });
            },
        },
        created(){
            this.userId = this.$store.getters.getUser.id;
            this.$axios.get("/project/"+this.$route.params.projectId+"?userId="+this.userId).then(res => {
                this.project = res.data.data;
                if (res.data.data.userRate != null){
                    this.isRate = true
                    this.rates = parseInt(res.data.data.userRate)
                }
                this.loadList(1);
                this.getBlogTime();
                this.$axios.get("/project/isfavorite/"+this.userId+"/"+this.project.id).then(res => {
                    if (res.data.data === true)
                        this.isFavorite = 'warning';
                    else
                        this.isFavorite = 'info';
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
    .colorBlock{
        color: red;
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