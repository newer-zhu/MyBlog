<template>
    <div>
        <div>
            <el-menu :default-active="activeIndex" class="el-menu-demo" mode="horizontal" @select="handleSelect">
                <el-menu-item index="1" @click="toIndex">首页</el-menu-item>
                <el-submenu index="2">
                    <template slot="title">我的工作台</template>
                    <el-menu-item @click="writeBlog" index="2-1">写博客</el-menu-item>
                    <el-menu-item index="2-2">选项2</el-menu-item>
                    <el-menu-item index="2-3">选项3</el-menu-item>
                </el-submenu>
                <el-menu-item index="3">消息中心</el-menu-item>
                <el-menu-item index="4" @click="drawer = true">个人中心</el-menu-item>
                <el-menu-item index="5" >个人简历</el-menu-item>
                <el-menu-item index="6" v-if="this.$store.getters.getUser == null" @click="toLogin">登录/注册</el-menu-item>
                <el-menu-item index="7" v-else @click="logOut">退出</el-menu-item>
            </el-menu>

        </div>
<!--        抽屉-->
        <div>
        <el-drawer
                title="我是标题"
                :visible.sync="drawer"
                :with-header="false">
            <!--                头像-->
            <div class="drawer">
                <div class="block avatar">
                    <el-avatar :size="75" alt="avatar"  :src="circleUrl"></el-avatar>
                    <h2>{{user.username}}</h2>
                </div>
                <div>
                    <h4>{{user.description}}</h4>
                </div>
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
                    avatar: ''
                }
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