<template>
    <div>
        <el-container>
            <el-header>
                <NaviBar/>
            </el-header>
            <el-main>
                <div class="my-main">
                    <el-row>
                        <el-col :span="16">
                            <div style="height: 1250px;width: 885px; padding: 10px 0 30px 0;float: right; margin-right: 50px; margin-left: 20px">
                                <div>
                                    <!--                            页头-->
                                    <el-card >
                                        <el-row>
                                            <el-col :span="8">
                                                <div style="">
                                                    <el-image class="image" fit="scale-down" src="https://cube.elemecdn.com/6/94/4d3ea53c084bad6931a56d5158a48jpeg.jpeg">
                                                        <div slot="placeholder" class="image-slot">
                                                            加载中<span class="dot">...</span>
                                                        </div>
                                                    </el-image>
                                                </div>
                                            </el-col>
                                            <el-col :span="14" :offset="1">
                                                <div style="">
                                                    <el-link :underline="false" target="_blank" :href="attach.Foundin[0].Name"style="font-size: 35px; color: #303133">{{'# '+tag.tagName}}</el-link>
                                                    <!--                                            <p style="display: inline; color: #606266; font-size: 20px; float: right">约 {{tag.number == null? 0:tag.number}} 篇</p>-->
                                                    <div style="display: inline; color: #606266; font-size: 20px; margin: 0px 0px 5px 15px">{{attach.英文名[0].Name}}</div>
                                                    <p style="display: inline; color: #606266; font-size: 16px; float: right">热度：{{attach.Popular[0].Name}}</p>
                                                    <div style="" v-show="attach != null">
                                                        <h5 style="color: #8f9298">相关标签</h5>
                                                        <el-tag @click="toTag(t.Name)" type="info" v-for="(t, i) in similar" style="margin: 0px 10px 5px 0px">
                                                            <el-link :underline="false" >
                                                                {{t}}
                                                            </el-link>
                                                        </el-tag>
                                                    </div>
                                                </div>
                                            </el-col>
                                        </el-row>
                                    </el-card>
                                    <!--                            文章列表-->
                                    <div style="background-color: #fdfdfe; border: 1px solid #faeaef; box-shadow: 1px 1px 10px #f8f8f8">
                                        <div v-show="page.total == 0" style="height: 400px">
                                            <p style="text-align: center; color: #8f9298 ">暂无相关文章</p>
                                        </div>
                                        <div v-for="b in page.list">
                                            <el-row>
                                                <div style="border-bottom: 1px solid #efefef; height: 100%">
                                                    <el-row>
                                                        <router-link :to="{name: 'BlogDetail', params:{blogId:b.id}}">
                                                            <el-col :span="18"><h2 style="margin-left: 8px;">{{b.title}}</h2></el-col>
                                                        </router-link>
                                                        <el-col :span="6"><p class="el-icon-view" style="margin-top: 20px; font-size: 20px; color: #9899a9">{{ b.visitors}}</p></el-col>
                                                    </el-row>
                                                    <el-row>
                                                        <p style="margin: 2px 15px;">{{b.summary}}</p>
                                                    </el-row>
                                                </div>
                                            </el-row>
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
                                </div>
                            </div>
                        </el-col>
                        <el-col :span="6">
                            <div style="margin-top: 10px; background-color: #fdfdfe; border-radius: 5px">
                                <div>
                                    <div v-for="(l, i) in attach.Foundin" style="display: inline;">
                                        <el-link class="el-icon-link" :underline="false" target="_blank" style="font-size: 20px;color: #333539;  margin: 5px 0px 5px 5px" :href="l.Name">百科链接 {{i+1}}</el-link>
                                    </div>
                                </div>
                                <div>
                                    <el-collapse style="margin: 10px 0px 0px 5px; background-color: #18caf6" accordion>
                                        <el-collapse-item v-for="(s, i) in chineseEle"  :title="s.title" :name="i+1">
                                            <div style="line-height: 15px" v-for="ss in s.content">
                                                {{ss.Name}}
                                            </div>
                                        </el-collapse-item>
                                    </el-collapse>
                                </div>
                            </div>
                        </el-col>
                    </el-row>
                </div>
            </el-main>
        </el-container>
    </div>
</template>

<script>
    import NaviBar from  "../components/common/Navibar"
    import Footer from "../components/common/Footer";
    export default {
        name: "TagDetail",
        components: {Footer, NaviBar},
        data(){
            return {
                tag: {},
                attach: {},
                chineseEle: [],
                page: {
                    list: [],
                    total: 1,
                    currentPage: 1,
                },
                similar: []
            }
        },
        methods: {
            handCurrentChange(current){
                this.loadPage(current);
            },
            loadPage(page){
                let _this = this;
                this.$axios.get("/tag/blogs/"+this.tag.id+"?page="+page).then(res => {
                    _this.page.list = res.data.data.list;
                    _this.page.total = res.data.data.total;
                })
            },
            toTag(name){
                this.$router.push({
                    name: 'TagDetail',
                    params: {
                        tagId: null,
                        tagName: name
                    }
                })
            },
            init(){
                this.tag.id = this.$route.params.tagId;
                this.tag.tagName = this.$route.params.tagName;
                this.$axios.post("/tag", this.tag).then(res => {
                    this.tag = res.data.data;
                    this.$axios.get("/tag/relative/"+this.tag.tagName).then(res => {
                        this.attach = JSON.parse(res.data.data);
                        this.$axios.get("/AI/similar/"+this.tag.tagName).then(res => {
                            this.similar = res.data.data;
                        });
                        console.log(this.attach);
                        for (let i in this.attach){
                            if(/^[\u4e00-\u9fa5]+$/i.test(i)){
                                let t = {title: i, content: this.attach[i]};
                                this.chineseEle.push(t);
                            }
                        }
                    });
                    this.loadPage(1);
                });

            }
        },
        watch :{
            '$route'(to, from){
                this.init();
            }
        },
        created(){
            this.init();
        }
    }
</script>

<style scoped>
    .bottom {
        margin-top: 13px;
        line-height: 12px;
    }

    .image {
        width: 100%;
        border-radius: 3px;
        /*display: block;*/
    }

    .clearfix:before,
    .clearfix:after {
        display: table;
        content: "";
    }
    .clearfix:after {
        clear: both
    }
    .el-main{
        padding: 1px 20px;
    }
    .my-main {
        background-image: url("../assets/img/tagBack.jpg");
        background-attachment: fixed;
        -moz-background-size:100% 100%;
        background-size:100% 100%;
        height: 100%;
        width: 100%;
    }
</style>