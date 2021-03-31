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
                    <div @click="searchHeat(w.value)" style="padding-left: 50px" v-for="(w, i) in hotWords">
                        <el-row>
                            <el-col :span="16" >
                                <p style="font-size: 17px">{{w.value}}</p>
                            </el-col>
                            <el-col :span="4"><el-tag style="margin-top: 10px">{{Math.round(w.score * 100) / 100}}</el-tag></el-col>
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