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
                            <el-button @click="search"  style="color: #409EFF" slot="append" icon="el-icon-search"></el-button>
                        </el-input>
                    </el-col>
                    <el-checkbox style="padding: 15px 190px 5px 20px" v-model="onlySelf">仅自己</el-checkbox>
                    时间范围
<!--                    <el-input  size="small" style="width: 75px;" v-model="searchRequest.range" placeholder="近( )天"></el-input>-->
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
                </el-row>
                <el-row>

                </el-row>
            </el-aside>

            <el-main>
                <el-row>
                    <el-col :span="16">
                        <div  style="border: 1px solid whitesmoke" v-for="(b, i) in page.blogs">
                            <UserInfo :user="b.user" :drawer.sync="drawer"></UserInfo>
                            <div style="margin-left: 5px">
                                <el-row>
                                    <el-col :span="17">
                                        <router-link :to="{name: 'BlogDetail', params:{blogId:b.id}}">
                                            <h3 style="color: #282c34" v-html="b.title"></h3>
                                            <i v-html="b.summary"></i>
                                        </router-link>
                                    </el-col>
                                    <el-col style="color: #8c939d" :span="7">
                                        <p>{{b.createdAt}}</p>
                                        <span style="font-size: 25px" :class="b.isFile ? 'el-icon-folder':'el-icon-document'"></span>
                                        <el-link @click="drawer = !drawer" v-if="b.user != null" style="display: inline; margin-left: 10px">{{b.user.username}}</el-link>
                                    </el-col>
                                </el-row>
                                <p style="color: #8c939d" v-html="b.content"></p>
                            </div>
                        </div>
                        <el-pagination
                                @current-change="handCurrentChange"
                                style="text-align: center"
                                layout="prev, pager, next"
                                :page-count="page.total"
                                :current-page="page.currentPage"
                        >
                        </el-pagination>
                    </el-col>
                </el-row>
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
                        title: '今日热点推荐',
                        summary: '近日，一位来自美国亚利桑那州监狱部门的负责人向新闻网站 KJZZ 举报，其所在监狱的囚犯管理软件存在 Bug ，导致数百名符合释放条件的囚犯被继续关押。实际上，该负责人于一年前就在监狱部门的内部指出该 Bug，但至今没有人采取行动来修复该软件的 Bug。',
                        content: '假期过半，大家过得如何，吃好喝好了吗？GitHub 很好！本周的 GitHub Trending 又上爆款项目——github1s 装完之后，一秒 GitHub 源码从浏览器变到你的 VS Code 里。此外，remotion 这个让你用 React 编程就能做出有意思视频的项目也是火到爆，一周获得近 5k star。除了项目火爆之外，本周 GitHub 也有个热点事件，便是昨日发生的 Ant Design 被删库事件，据某技术媒体所说为 GitHub 被黑导致“删库跑路”。嗝~ 这个瓜吃得真饱 😃\n' +
                            '\n',
                        userId: '',
                        createdAt: '2020-10-31 12:02:33',
                        id: '',
                        isFile: 0,
                        visitors: 0,
                    }],
                    total: 1,
                    currentPage: 1,
                },

            }
        },
        watch: {
            onlySelf(val){
                this.searchRequest.userId = this.onlySelf === true? this.userId : '';
            }
        },
        methods: {
            search(){
                if (this.time != null){
                    this.searchRequest.start = this.time[0];
                    this.searchRequest.end = this.time[1];
                }
                this.$axios.post("/search/query", this.searchRequest).then(res => {
                    this.page.blogs = res.data.data;
                });
            },
            handCurrentChange(current){
                this.page.currentPage = current;
                this.search();
            }
        },
        created() {
            this.userId  = this.$store.getters.getUser.id;
            this.searchRequest.querystr = this.$route.params.querystr;
            this.$axios.post("/search/query", this.searchRequest).then(res => {
                this.blogs = res.data.data;
            });
        }
    }
</script>

<style scoped>
    .main{
        padding: 10px;
    }
</style>