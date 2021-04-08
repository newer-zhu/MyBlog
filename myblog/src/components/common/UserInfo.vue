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
                        <label class="first-text">大学</label><el-divider direction="vertical"></el-divider>
                        <p class="subText">{{user.college}}</p>
                    </el-col>
                    <el-col :span="8">
                        <label class="first-text">专业</label><el-divider direction="vertical"></el-divider>
                        <p class="subText">{{user.major}}</p>
                    </el-col>
                    <el-col :span="6">
                        <label class="first-text">年级</label><el-divider direction="vertical"></el-divider>
                        <p class="subText">{{user.grade}}</p>
                    </el-col>
                </el-row>
                <div style="text-align: center">
                    <p class="el-icon-message" style="font-size: 20px; color: #020209">{{'邮箱： ' + user.email}}</p>
                </div>
            </div>
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
        background-image: url("../../assets/img/userInfo.jpg");
    }
    .avatar{
        margin: 0 auto;
        text-align: center;
        padding: 12px;
    }
    .subText{
        display: inline-block;
        font-weight: 600;
        font-size: 16px;
        color: rgba(0, 0, 27, 0.93);
    }
    .first-text{
        color: #1717a1;
        font-weight: bold
    }
</style>