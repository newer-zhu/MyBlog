<template>
    <div>
        <el-header>
            <Navibar/>
        </el-header>
        <el-container>
            <el-main>
                <!--页头-->
                <el-row>
                    <UserInfo :user="project.projectUser" :drawer.sync="drawer"></UserInfo>
                    <el-col :span="9">
                        <h1>{{project.projectName}}</h1>
                    </el-col>
                    <el-col :span="2" :offset="1">
                        <div>
                            <div style="padding-left: 35px">
                                <div @click="drawer = !drawer">
                                    <el-avatar :size="50" fit="contain" :src="project.projectUser.avatar"></el-avatar>
                                </div>
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
                <el-row :gutter="30">
                    <el-col :span="16">
                        <el-row>
                            <div v-if="project.pictures.length == 0 && userId == project.startUser">
                                <p class="el-icon-info" style="color: #606266"> 可在资料编辑中上传头图 </p>
                            </div>
                            <!--                        展示图-->
                            <el-carousel v-show="project.pictures.length != 0" :interval="4000" type="card" height="200px">
                                <el-carousel-item v-for="item in project.pictures" :key="item.name">
                                    <el-image
                                      style="height: 100%;"
                                      :src="item.url"
                                      fit="contain"
                                    >
                                    </el-image>
                                </el-carousel-item>
                            </el-carousel>
                            <!--评论侧边-->
                            <el-col :span="6">
                                <h3 class="el-icon-s-comment" style="font-size: 25px; color: #56595f; font-weight: bold">项目评论</h3>
                                <div v-for="(co, i) in comments">
                                    <el-link style="color: #606266; font-size: 18px">{{co.user.username}}</el-link> <div style="font-size: 12px; color: #8f9298">{{co.time}}</div>
                                    <p style="color: #303133">{{co.comment}}</p>
                                </div>
<!--                                <div style="height: 500px;width: 200px; overflow: hidden">-->

<!--                                </div>-->
                            </el-col>
                            <el-col :span="18" >
                                <el-row>
                                    <el-col :span="23">
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
                                                        <span >{{card.title}}</span>
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
                                    <!--                                按钮-->
                                    <el-col :span="1">
                                        <el-tooltip class="item" effect="dark" content="新建" placement="right-start">
                                            <el-button @click="edit" type="primary" icon="el-icon-edit" circle></el-button>
                                        </el-tooltip>
                                        <br>
                                        <el-tooltip class="item" effect="dark" :content="favoriteInfo" placement="right-start">
                                            <el-button  :type="isFavorite" @click="favorite" icon="el-icon-star-off" circle></el-button>
                                        </el-tooltip>
                                        <br>
                                        <el-tooltip  class="item" effect="dark" content="申请" placement="right-start">
                                            <el-button  type="warning" :disabled="project.startUser == userId" @click="request" icon="el-icon-s-promotion" circle></el-button>
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
                                        <el-tooltip   class="item" effect="dark" content="删除" placement="right-start">
                                            <el-button type="danger" :disabled="userId != project.startUser"  @click="delVisible = true" icon="el-icon-delete" circle></el-button>
                                        </el-tooltip>
                                        <br>
                                    </el-col>
                                </el-row>
                            </el-col>
                        </el-row>
                    </el-col>
                    <el-col :span="7" >
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
                                                <p style="color: rgba(156,44,37,0.85); font-weight: bold">{{calendar.timeMap[data.day.toString()]}} 篇</p>
                                            </div>
                                        </template>
                                    </el-calendar>
                                </div>
                            </el-tab-pane>
                            <el-tab-pane label="标签一览" name="second">
                                <div style="padding-top: 10px" v-for="(n, i) in tags">
                                    <el-row>
                                        <el-tag>{{n}}</el-tag>
                                        <el-progress :percentage="parseInt(per[i])"></el-progress>
                                    </el-row>
                                </div>
                            </el-tab-pane>
                            <el-tab-pane label="贡献人员" name="third">
                                <el-table
                                  :data="project.contributors"
                                  height="250"
                                  border
                                  style="">
                                    <el-table-column
                                      prop="date"
                                      label="加入日期"
                                      width="150">
                                    </el-table-column>
                                    <el-table-column
                                      prop="username"
                                      label="昵称"
                                      width="180">
                                        <template slot-scope="scope">
                                            <el-link style="display: inline">{{scope.row.username}}</el-link>
                                            <el-tag style="margin-left: 15px" v-show="project.startUser == scope.row.userId" size="small" type="danger">发起人</el-tag>
<!--                                            <button type="danger" style="float:right;" class="el-icon-error"-->
<!--                                                    @click="deal(scope.row.userId, 0)"-->
<!--                                                    v-show="project.startUser != scope.row.userId && userId == project.startUser">-->
<!--                                            </button>-->
                                        </template>
                                    </el-table-column>
                                    <el-table-column
                                      prop="number"
                                      width="77"
                                      label="次数">
                                    </el-table-column>
                                </el-table>
                            </el-tab-pane>
                            <el-tab-pane label="资料编辑" v-if="userId == project.projectUser.id" name="fourth">
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
                                                <el-input maxlength="50"
                                                          show-word-limit style="width: 90%; padding-right: 5px"
                                                          :rows="2" placeholder="简介内容"
                                                          type="textarea" v-model="domain.content"></el-input>
                                            </el-form-item>
                                            <el-form-item label="图片上传">
                                                <div>
                                                    <el-upload
                                                      class="upload-demo"
                                                      name="pic"
                                                      :action="'http://localhost:8081/media/uploadProjectPic?projectId='+project.id"
                                                      :on-remove="removePic"
                                                      :on-success="uploadSuccess"
                                                      :file-list="project.pictures">
                                                        <el-button size="small" type="primary">点击上传</el-button>
                                                        <div slot="tip" class="el-upload__tip">只能上传jpg图片</div>
                                                    </el-upload>
                                                </div>
                                            </el-form-item>
                                            <el-form-item>
                                                <el-button type="success" style="margin-left: 100px" @click="submitForm('dynamicValidateForm')">修改</el-button>
<!--                                                <el-button @click="resetForm('dynamicValidateForm')">重置</el-button>-->
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
<!--            评论面板-->
            <el-dialog style="width: 1000px; text-align: center; margin: 0 auto" title="个人评论" :visible.sync="isComment">
                <el-form >
                    <el-form-item>
                        <el-input
                          maxlength="200"
                          show-word-limit
                          type="textarea" v-model="comment" autocomplete="off"></el-input>
                    </el-form-item>
                </el-form>
                <div slot="footer" class="dialog-footer">
                    <el-button @click="isComment = false">算 了</el-button>
                    <el-button type="primary" @click="sendComment">评 论</el-button>
                </div>
            </el-dialog>
        </el-container>
    </div>
</template>

<script>
    import Navibar from "components/common/Navibar";
    import vueInfiniteAutoScroll from 'vue-infinite-auto-scroll'
    const UserInfo = () => import('../../components/common/UserInfo');
    export default {
        name: "ProjectDetail",
        components: {Navibar, UserInfo, vueInfiniteAutoScroll},
        data(){
            return{
                userId: 0,
                project: {
                    id: -1,
                    startUser: 0,
                    projectUser: {},
                    projectName: '项目标题',
                    rates: 5,
                    favorite: 0,
                    overview: [{title: '', content: ''}],
                    contributors: [],
                    pictures: [{projectId: '',name: '', url: ''}]
                },
                comments: [],
                tags: [],
                tagNum: 0,//标签总数
                comment: '',
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
                isRate: false,//是否已经评分过了
                isComment: false,//是否显示评论
                colors: ['#2d9abf', '#F7BA2A', '#FF9900'],
                drawer: false,
                //日历数据
                calendar: {
                    date: new Date(),
                    blogTime: [],
                    timeMap: {}
                },
                delVisible: false,
                per: [20, 10]
            }
        },
        computed: {
            favoriteInfo(){
                return this.isFavorite === "info" ? "收藏" : "取消收藏"
            },
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
            loadComments(){
                this.$axios.get("/comment/project/"+this.project.id).then(res => {
                    this.comments = res.data.data;
                    console.log(this.comments);
                })
            },
            getBlogTime(){
                this.$axios.get("/project/blogtime/"+this.project.id).then(res => {
                    this.calendar.timeMap = res.data.data;
                    this.calendar.blogTime = Object.keys(res.data.data);
                })
            },
            edit(){
                if (this.userId != this.project.startUser){
                    this.$axios.get("/project/editable/"+this.project.id+"/"+this.userId).then(res => {
                        if (res.data.data){
                            this.$router.push({
                                name: 'BlogEdit',
                                params: {
                                    projectId: this.project.id
                                }
                            })
                        }else {
                            this.$message.warning("暂无权限！");
                        }
                    })
                }else {
                    this.$router.push({
                        name: 'BlogEdit',
                        params: {
                            projectId: this.project.id
                        }
                    })
                }
            },
            //评分
            rate(r){
                this.$axios.get("/project/rates/"+this.userId+"/"+this.project.id+"/"+r).then(res => {
                    this.isRate = true;
                    this.success(res.data.data);
                });
                this.isComment = true;
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
            //修改基本信息
            submitForm(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        this.$axios.post("/project/overview", this.project).then(res => {
                            this.insertPics();
                            this.success(res.data.data);
                        })
                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                })
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
            sendComment(){
                let c = {projectId: this.project.id, rates: this.rates, userId: this.userId, comment: this.comment, time: ''};
                this.$axios.post("/comment/projectComment", c).then(res => {
                    this.$message.success(res.data.data);
                    this.isComment = false;
                    this.loadComments();
                });
            },
            request(){
                let _this = this;
                this.$axios.get("/project/request/"+this.userId+"/"+this.project.id).then(res => {
                    _this.$message.success(res.data.msg);
                    if (res.data.code == 200 && this.global.ws.readyState == 1){
                        _this.global.ws.send(this.project.id);
                    }else {
                        console.log("未连接websocket");
                    }
                });
            },
            loadTags(){
                this.$axios.get("tag/"+this.project.id).then(res => {
                    this.tags = res.data.data;
                    for (let l in this.tags){
                        let s = this.tags[l].split(':');
                        this.tagNum += parseInt(s[1]);
                    }
                    for (let i in this.tags){
                        this.per[i] = parseInt(this.tags[i].split(':')[1]) / this.tagNum * 100;
                    }
                });
            },
            removePic(file, fileList){
                for (let i in this.project.pictures){
                    if (this.project.pictures[i].uid == file.uid)
                        this.project.pictures.splice(i, 1);
                }
            },
            uploadSuccess(res, file, fileList){
                this.project.pictures.push(res.data);
                console.log(this.project.pictures);
            },
            insertPics(){
                if (this.project.pictures.length != 0){
                    this.$axios.post("/project/insertPic", this.project.pictures);
                }
            },
            deal(contributorId,res){
                this.$axios.get("/project/deal/"+contributorId+"/"+this.project.id+"?res="+res);
            }
        },
        created(){
            this.userId = this.$store.getters.getUser.id;
            this.$axios.get("/project/"+this.$route.params.projectId+"?userId="+this.userId).then(res => {
                this.project = res.data.data;
                if (res.data.data.userRate != null){
                    this.isRate = true;
                    this.rates = parseFloat(res.data.data.userRate);
                }
                this.loadList(1);
                this.getBlogTime();
                this.loadTags();
                this.$axios.get("/project/isfavorite/"+this.userId+"/"+this.project.id).then(res => {
                    if (res.data.data === true)
                        this.isFavorite = 'warning';
                    else
                        this.isFavorite = 'info';
                });
                this.loadComments();
            });
        }
    }
</script>

<style scoped>
    .el-carousel__item:nth-child(2n) {
        background-color: #99a9bf;
    }
    .el-carousel__item:nth-child(n) {

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