<template>
    <!--        抽屉-->
    <div>
        <el-drawer
                title="我是标题"
                :visible.sync="drawer"
                size="30%"
                :show-close="true"
                :before-close="close"
                :with-header="false">
            <!--                头像-->
            <div class="drawer" v-if="user != null">
                <div class="block avatar">
                    <el-avatar :size="75"  :src="user.avatar"></el-avatar>
                    <h2>{{user.username}}</h2>
                    <h4 style="text-align: center">{{user.description}}</h4>
                </div>
                <el-row>
                    <el-col :span="8" :offset="2">
                        <label style="color: indigo;font-weight: bold">大学</label><el-divider direction="vertical"></el-divider>
                        <p style="display: inline-block">{{user.college}}</p>
                    </el-col>
                    <el-col :span="8">
                        <label style="color: indigo; font-weight: bold">专业</label><el-divider direction="vertical"></el-divider>
                        <p style="display: inline-block">{{user.major}}</p>
                    </el-col>
                    <el-col :span="6">
                        <label style="color: indigo; font-weight: bold">年级</label><el-divider direction="vertical"></el-divider>
                        <p style="display: inline-block">{{user.grade}}</p>
                    </el-col>
                </el-row>
            </div>

            <span>我来啦!</span>
        </el-drawer>
    </div>
</template>

<script>
    export default {
        name: "UserInfo",
        data(){
            return{

            }
        },
        props: ['user', 'drawer'],
        watch: {

        },
        created() {
            // if (this.user.grade == null || this.user.grade == undefined){
            //     this.$axios.get("/user/"+this.user.id).then(res =>{
            //         this.user = res.data.data;
            //     })
            // }
        },
        methods: {
            close(done){
                console.log("close");
                this.$emit("update:drawer", false);
            }
        }
    }
</script>

<style scoped>
    .drawer{
        height: 100%;
        -moz-background-size:100% 100%; background-size:100% 100%;
        background-image: url("../../assets/img/green.jpg");
    }
    .avatar{
        margin: 0 auto;
        text-align: center;
        padding: 12px;
    }
</style>