<template>
    <div>
        <el-container>
            <el-header>
                <Navibar/>
            </el-header>
        </el-container>

        <el-container class="main">
            <el-aside class="aside" width="400px">
                <el-row>
                    <el-col :span="23" :offset="1">
                        <el-input placeholder="请输入搜索内容" v-model="searchRequest.querystr">
                            <el-button @click="search(searchRequest.current)"  style="color: #409EFF" slot="append" icon="el-icon-search"></el-button>
                        </el-input>
                    </el-col>
                    <el-checkbox style="padding: 15px 190px 5px 20px" v-model="onlySelf">仅自己</el-checkbox>
                    时间范围
                    <el-date-picker style="margin-left: 20px"
                            unlink-panels
                            v-model="time"
                            type="daterange"
                            align="right"
                            unlink-panels
                            value-format="yyyy-MM-dd"
                            range-separator="至"
                            start-placeholder="开始日期"
                            end-placeholder="结束日期"
                            :picker-options="pickerOptions">
                    </el-date-picker>
                    <div>
                        <h3 class="el-icon-search" style="padding-left: 10%; font-weight: bold; color: rgba(133,72,66,0.99)">热搜词榜</h3>
                        <div @click="searchHeat(w.value)" style="padding-left: 50px" v-for="(w, i) in hotWords">
                            <el-row>
                                <el-col :span="4" >
                                    <p style="font-size: 17px">{{w.value}}</p>
                                </el-col>
                                <el-col :span="4"><el-tag style="margin-top: 10px">{{Math.round(w.score * 100) / 100}}</el-tag></el-col>
                            </el-row>
                            <div class="link-top"></div>
                        </div>
                    </div>
                </el-row>
                <el-row>

                </el-row>
            </el-aside>

            <el-main>
                <el-row :gutter="10">
                    <el-alert style="height: 40px"
                            :title="'共找到文章'+page.total+'条,'+'有关项目'+page.projects.length+'条'"
                            type="success" effect="dark" :closable="false"
                            show-icon>
                    </el-alert>
<!--                    文章-->
                    <el-col :span="16">
                        <div v-show="page.blogs.length == 0">
                            <div style="margin-left: 5px">
                                <el-row>
                                    <el-col :span="17">
                                        <h3 style="color: #282c34">暂无相关文章结果</h3>
                                        <i>既然没人，赶快补充吧！</i>
                                    </el-col>
                                </el-row>
                            </div>
                        </div>
                        <div v-show="page.blogs.length != 0" style="border: 1px solid whitesmoke;border-bottom:2px solid #faeaef;" v-for="(b, i) in page.blogs">
                            <div style="margin-left: 5px">
                                <el-row>
                                    <el-col :span="17">
                                        <router-link :to="{name: 'BlogDetail', params:{blogId:b.id}}">
                                            <h3 style="color: #282c34" v-html="b.title"></h3>
                                            <i v-html="b.summary"></i>
                                        </router-link>
                                    </el-col>
                                    <el-col style="color: #8c939d" :span="7">
                                        <p>{{b.createdAt.slice(0, 11)}}</p>
                                        <span style="font-size: 25px" :class="b.isFile ? 'el-icon-folder':'el-icon-document'"></span>
                                        作者：<el-link @click="getUserInfo(b.user)" v-if="b.user != null" style="display: inline; margin-left: 10px">{{b.user.username}}</el-link>
                                    </el-col>
                                </el-row>

                                <p style="color: #8c939d" v-html="b.content"></p>
                            </div>
                        </div>
                    </el-col>
<!--                    项目-->
                    <el-col :span="8">
                        <el-card v-show="page.projects.length == 0" style="margin-left: 5px; padding-bottom: 10px; margin-top: 5px">
                            暂无相关项目
                        </el-card>
                        <div v-for="(p, i) in page.projects" style="box-shadow:  0 1px 8px 0 rgba(0, 0, 0, 0.1); margin-bottom: 1px; margin-top: 5px">
                            <div style="margin-left: 5px; padding-bottom: 10px">
                                <el-row>
                                    <el-col :span="16">
                                        <router-link :to="{name: 'ProjectDetail', params:{projectId:p.id}}">
                                            <h3 style="color: #303133" v-html="p.projectName"></h3>
                                        </router-link>
                                        作者：<el-link @click="getUserInfo(p.projectUser)" v-if="p.projectUser != null" style="display: inline; margin-left: 10px">{{p.projectUser.username}}</el-link>
                                    </el-col>
                                    <el-col style="color: #8c939d" :span="8">
                                        <p>{{p.createAt.slice(0, 11)}}</p>
                                        <i style="font-size: 25px" class='el-icon-folder-opened'></i>
                                        <p style="font-size: 18px; color: #409eff; display: inline; margin-left: 15px" class="el-icon-star-on">{{ p.rates }}</p>
                                    </el-col>
                                </el-row>
                            </div>
                        </div>
                    </el-col>
                    <el-pagination
                            hide-on-single-page
                            @current-change="handCurrentChange"
                            style="text-align: center"
                            layout="prev, pager, next"
                            :page-size="7"
                            :total="page.total"
                            :current-page="page.currentPage"
                    >
                    </el-pagination>
                </el-row>
                <UserInfo :user="subUser" :drawer.sync="drawer"></UserInfo>
            </el-main>
        </el-container>
    </div>
</template>

<script>
    import SearchList from '../components/common/SearchList'
    import Navibar from "../components/common/Navibar";
    import UserInfo from '../components/common/UserInfo'
    export default {
        name: "DisplayList",
        components: {Navibar, SearchList, UserInfo},
        data(){
            return{
                time: '',
                pickerOptions: {
                    shortcuts: [{
                        text: '最近一周',
                        onClick(picker) {
                            const end = new Date();
                            const start = new Date();
                            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
                            picker.$emit('pick', [start, end]);
                        }
                    }, {
                        text: '最近一个月',
                        onClick(picker) {
                            const end = new Date();
                            const start = new Date();
                            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
                            picker.$emit('pick', [start, end]);
                        }
                    }, {
                        text: '最近三个月',
                        onClick(picker) {
                            const end = new Date();
                            const start = new Date();
                            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
                            picker.$emit('pick', [start, end]);
                        }
                    }]
                },
                onlySelf: false,
                drawer: false,
                userId: '',
                searchRequest: {
                    querystr: '',
                    userId: '',
                    start: '',
                    end: '',
                    current: 1
                },
                page: {
                    blogs: [{
                        title: '暂无相关结果',
                        summary: '既然没人，赶快补充吧！',
                        content: '',
                        userId: '',
                        createdAt: '',
                        id: '',
                        isFile: 0,
                        visitors: 0,
                    }],
                    total: 1,
                    currentPage: 1,
                    projects: []
                },
                hotWords: [],
                subUser: {}
            }
        },
        watch: {
            onlySelf(val){
                this.searchRequest.userId = this.onlySelf === true? this.userId : '';
            },
            time(val){
                if (this.time != null && this.time != ''){
                    this.searchRequest.start = this.time[0];
                    this.searchRequest.end = this.time[1];
                }else {
                    this.searchRequest.start = '';
                    this.searchRequest.end = '';
                }
            }
        },
        methods: {
            search(current){
                this.searchRequest.current = current;
                this.$axios.post("/search/query", this.searchRequest).then(res => {
                    this.page.blogs = res.data.data.blogList;
                    this.page.total = res.data.data.total;
                    this.page.projects = res.data.data.projects;
                });
            },
            searchHeat(word){
                this.searchRequest.querystr = word;
                this.searchRequest.current = 1;
                this.search(1);
                this.page.currentPage = 1;
            },
            handCurrentChange(current){
                this.search(current);
            },
            getUserInfo(user){
                this.subUser = user;
                this.drawer = !this.drawer;
            }
        },
        created() {
            this.userId  = this.$store.getters.getUser.id;
            this.searchRequest.querystr = this.$route.params.querystr;
            this.search(1);
            this.$axios.get("/search/words").then(res => {
                this.hotWords = res.data.data;
            })
        }
    }
</script>

<style scoped>
    .main{
        padding: 10px;
    }
    /*中间的过度的横线*/
    .link-top {
        width: 90%;
        height: 1px;
        border-top: solid #ACC0D8 1px;
    }
</style>