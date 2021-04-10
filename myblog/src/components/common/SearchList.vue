<template>
    <div>
        <el-aside class="aside" width="350px">
            <el-row>
                <el-col :span="23" :offset="1">
                    <el-input placeholder="请输入搜索内容" v-model="querystr">
                        <el-button @click="search"  style="color: #409EFF" slot="append" icon="el-icon-search"></el-button>
                    </el-input>
                </el-col>
            </el-row>
            <el-row>
                <div>
                    <h3 class="el-icon-search" style="padding-left: 10%; font-weight: bold; color: rgba(133,72,66,0.99)">热搜词榜</h3>
                    <div @click="searchHeat(w.value)" style="padding-left: 50px; cursor: pointer" v-for="(w, i) in hotWords">
                        <el-row>
                            <el-col :span="15" >
                                <p class="hot_search_list">{{w.value}}</p>
                            </el-col>
                            <el-col :span="5">
                                <div style="margin-top: 30px">
                                    <img style="height: 15px; width: 15px;" :src="require('../../assets/icon/hot.png')">
                                    <div style="color:#ff3c41; display: inline; margin-left: 4px;">{{Math.round(w.score * 100) / 100}}</div>
                                </div>
                            </el-col>
                        </el-row>
                        <div class="link-top"></div>
                    </div>
                </div>
            </el-row>
        </el-aside>
    </div>
</template>

<script>
    export default {
        name: "SearchList",
        data(){
            return{
                querystr: '',
                hotWords: []
            }
        },
        created(){
            this.$axios.get("/search/words").then(res => {
                this.hotWords = res.data.data;
            })
        },
        methods: {
            search(){
                if (this.querystr != ""){
                    this.$router.push({
                        name: 'DisplayList',
                        params: {
                            querystr: this.querystr
                        }
                    });
                }
            },
            searchHeat(word){
                this.querystr = word;
                this.search();
            }
        }

    }
</script>

<style scoped>
    .link-top {
        width: 90%;
        height: 1px;
        border-top: solid #ACC0D8 1px;
    }
</style>

<style>
    .hot_search_list{
        font-size: 17px;
        margin: 20px 0px 5px 15px
    }
</style>