<template>
    <div>
        <div>
            <el-menu :default-active="activeIndex" mode="horizontal" @select="handleSelect" background-color="#545c64"
                     text-color="#fff" active-text-color="#ffd04b">
                <el-menu-item index="1" @click="toIndex">首页</el-menu-item>
                <el-submenu index="2">
                    <template slot="title">发布</template>
                    <el-menu-item @click="writeBlog" index="2-1">写博客</el-menu-item>
                    <el-menu-item index="2-2">发起项目</el-menu-item>
                    <el-menu-item index="2-3">提问</el-menu-item>
                </el-submenu>
                <el-menu-item index="3">消息中心</el-menu-item>
                <el-menu-item index="4" @click="drawer = true">个人中心</el-menu-item>
                <el-menu-item index="5" >求职</el-menu-item>
                <el-menu-item index="6">探索</el-menu-item>
                <el-menu-item index="7">圈子</el-menu-item>
                <el-menu-item index="8" style="width: 400px">
                    <el-input
                            placeholder="请输入内容"
                            v-model="input">
                        <i slot="prefix" class="el-input__icon el-icon-search"></i>
                    </el-input>
                </el-menu-item>
                <!--                <el-menu-item index="6" v-if="this.$store.getters.getUser == null" @click="toLogin">登录/注册</el-menu-item>-->
                <el-menu-item index="9" @click="logOut">退出</el-menu-item>
            </el-menu>

        </div>
<!--        抽屉-->
        <div>
        <el-drawer
                title="我是标题"
                :visible.sync="drawer"
                size="30%"
                :with-header="false">
            <!--                头像-->
            <div class="drawer">
                <div class="block avatar">
                    <el-avatar :size="75" alt="avatar"  :src="circleUrl"></el-avatar>
                    <h2>{{user.username}}</h2>
                    <h4 style="text-align: center">{{user.description}}</h4>
                </div>
                <el-row>
                    <el-col :span="10" :offset="2">
                        <label style="color: indigo;font-weight: bold">大学</label><el-divider direction="vertical"></el-divider>
                        <p style="display: inline-block">{{user.college}}</p>
                    </el-col>
                    <el-col :span="12">
                        <label style="color: indigo; font-weight: bold">专业</label><el-divider direction="vertical"></el-divider>
                        <p style="display: inline-block">{{user.major}}</p>
                    </el-col>
                </el-row>
            </div>

            <span>我来啦!</span>
        </el-drawer>
        </div>

    </div>


</template>

<script>
    export default {
        name: "Navibar",
        data() {
            return {
                circleUrl: "https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png",
                fits: ['fill', 'contain', 'cover', 'none', 'scale-down'],
                activeIndex: '1',
                activeIndex2: '1',
                drawer: false,
                direction: 'rtl',
                user: {
                    username: '',
                    description: '',
                    avatar: '',
                    college: '',
                    major: ''
                },
                input: '',
            };
        },
        methods: {
            handleSelect(key, keyPath) {
                console.log(key, keyPath);
            },
            toLogin() {
                this.$router.push("/login");
            },
            toIndex() {
                this.$router.push("/home");
            },
            logOut(){
                console.log(this.$store.getters.getUser);
                this.$store.commit('REMOVE_INFO');
                this.$router.push("/login");
            },
            writeBlog(){
                this.$router.push("/blogedit")
            }
        },
        created(){
            if (this.user.username == ''){
                this.user = this.$store.getters.getUser;
            }
        }
    }
</script>

<style scoped>
    .avatar{
        margin: 0 auto;
        text-align: center;
        padding: 12px;
    }
    .drawer{
        height: 100%;
        -moz-background-size:100% 100%; background-size:100% 100%;
        background-image: url("../../assets/img/green.jpg");
    }
</style>