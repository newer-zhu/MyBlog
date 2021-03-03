<template>
    <div>
        <el-aside class="aside" width="400px">
            <el-row>
                <el-col :span="23" :offset="1">
                    <el-input placeholder="请输入搜索内容" v-model="querystr">
                        <el-button @click="search"  style="color: #409EFF" slot="append" icon="el-icon-search"></el-button>
                    </el-input>
                </el-col>
            </el-row>
            <el-row>

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
            }
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
            }
        }

    }
</script>

<style scoped>

</style>