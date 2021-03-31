<template>
    <div>
        <el-container>
            <el-header>
                <NaviBar/>
            </el-header>
            <el-container class="mycontainer">
                <el-main>
                    <div style="width: 60%; margin: 0 auto">
                        <div style="">
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
                                            <h1 style="">
                                                # {{tag.tagName}}
                                                <p style="display: inline; color: #606266; font-size: 20px; float: right">{{'约 '+tag.number+' 篇'}}</p>
                                            </h1>

                                        </div>
                                    </el-col>
                                </el-row>
                            </el-card>
                            <div style="background-color: #fdfdfe; border: 1px solid #faeaef; box-shadow: 1px 1px 10px #f8f8f8">
                                <div v-for="b in page.list">
                                    <el-row>
                                        <div v-for="i in 5" style="border-bottom: 1px solid #efefef; height: 120px">
                                            <el-row>
                                                <router-link :to="{name: 'BlogDetail', params:{blogId:b.id}}">
                                                    <el-col :span="18"><h2 style="margin-left: 8px;">{{b.title}}</h2></el-col>
                                                </router-link>
                                                <el-col :span="6"><p class="el-icon-view" style="margin-top: 20px; font-size: 20px; color: #9899a9">{{ b.visitors}}</p></el-col>
                                            </el-row>
                                            <el-row>
                                                <el-col :span="18">
                                                    <p style="margin: 2px 15px;">{{b.summary}}</p>
                                                </el-col>
<!--                                                <el-col :span="6">{{b.user.username}}</el-col>-->
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
                </el-main>
            </el-container>
        </el-container>
    </div>
</template>

<script>
    import NaviBar from  "../components/common/Navibar"
    export default {
        name: "TagDetail",
        components: {NaviBar},
        data(){
            return {
                tag: {

                },
                page: {
                    list: [],
                    total: 1,
                    currentPage: 1,
                }
            }
        },
        methods: {
            handCurrentChange(current){
                this.loadPage(current)
            },
            loadPage(page){
                this.$axios.get("/tag/blogs/"+this.tag.id+"?page="+page).then(res => {
                    this.page.list = res.data.data.list;
                    this.page.total = res.data.data.total;
                })
            }
        },
        created(){
            this.tag.id = this.$route.params.tagId;
            this.$axios.get("/tag/id/"+this.tag.id).then(res => {
                this.tag = res.data.data;
            });
            this.loadPage(1);
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
    .mycontainer {
        height: 100%;
        width: 100%;
        background-image: url("../assets/img/tagBack.jpg");
    }
</style>