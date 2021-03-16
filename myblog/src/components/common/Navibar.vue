<template>
    <div>
        <div>
            <el-menu :default-active="activeIndex" mode="horizontal" @select="handleSelect" background-color="#545c64"
                     text-color="#fff" active-text-color="#ffd04b">
                <el-menu-item index="1" >首页</el-menu-item>
                <el-submenu index="2">
                    <template slot="title">发布</template>
                    <el-menu-item @click="writeBlog" index="2-1">写笔记</el-menu-item>
                    <router-link :to="{name: 'StartProject'}">
                        <el-menu-item index="2-2">发起项目</el-menu-item>
                    </router-link>
                </el-submenu>
                <el-menu-item @click="connect" index="3">消息中心</el-menu-item>
                <el-menu-item index="4" @click="drawer = !drawer">个人中心</el-menu-item>
                <el-menu-item index="5">圈子</el-menu-item>
                <el-menu-item index="6" style="float: right; right: 50px" @click="logOut">退出</el-menu-item>
            </el-menu>
            <el-drawer
                    :visible.sync="message"
                    size="30%"
                    direction="ltr"
                    :with-header="false"
                    :before-close="handleClose">
                <div>
                    <div v-show="requests.requestMessages.length == 0">
                        <p>暂时还没有消息哦</p>
                    </div>
                    <div v-for="(r, i) in requests.requestMessages" :key="r.id">
                        <el-card>
                            <p>{{'"'+r.username+'"' + "申请成为项目:《"+r.projectName+"》的贡献者"}}</p>
                            <div style="float: right; margin-bottom: 3px">
                                <el-button size="small" @click="deal(r.projectId, r.contributorId, 1, i)" icon="el-icon-check" type="primary">同意</el-button>
                                <el-button size="small" @click="deal(r.projectId, r.contributorId, 0, i)" icon="el-icon-close" type="danger">拒绝</el-button>
                            </div>
                        </el-card>
                    </div>
                </div>
            </el-drawer>
        </div>
<!--        抽屉-->
        <UserInfo :user="this.$store.getters.getUser" :drawer.sync="drawer"></UserInfo>
    </div>


</template>

<script>
    import UserInfo from "./UserInfo";
    export default {
        name: "Navibar",
        components: {UserInfo},
        data() {
            return {
                circleUrl: "https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png",
                fits: ['fill', 'contain', 'cover', 'none', 'scale-down'],
                activeIndex: '1',
                drawer: false,
                message: false,
                user: {
                    username: '',
                    description: '',
                    avatar: '',
                    college: '',
                    major: '',
                    grade: '',
                    email: ''
                },
                input: '',
                requests:{
                    nextId: 0,
                    requestMessages: []
                }
            };
        },
        methods: {
            connect(){
                if (this.global.ws.readyState == 1){
                    console.log("已经建立连接");
                }else {
                    this.localSocket();
                }
                this.message = !this.message;
            },
            localSocket() {
                let that = this;
                if ("WebSocket" in window) {
                    // if (this.socket!= null || this.socket.readyState == 1){
                    //     console.log("关闭连接");
                    //     this.socket.close();
                    // }
                    let ws = new WebSocket("ws://localhost:8081/pro/"+that.$store.getters.getUser.id);
                    that.global.setWs(ws);
                    // this.socket = ws;
                    // this.$store.commit("SET_WS", that.ws);
                    that.global.ws.onopen = function() {
                        console.log('websocket连接成功');
                    };
                    that.global.ws.onmessage = e => {
                        let reqs = JSON.parse(e.data);
                        for (let r in reqs){
                            reqs[r].id = this.requests.nextId++;
                            this.requests.requestMessages.push(reqs[r]);
                        }
                    };
                    that.global.ws.onclose = function () {
                        // 关闭 websocket
                        console.log("连接已关闭...");
                        //断线重新连接
                        // setTimeout(() => {
                        //     that.localSocket();
                        // }, 2000);
                    };
                } else {
                    // 浏览器不支持 WebSocket
                    console.log("您的浏览器不支持 WebSocket!");
                    this.openNotificationWithIcon('error', '浏览器', '您的浏览器不支持显示消息请更换', 1,1)
                }
            },
            handleSelect(key, keyPath) {
                if (key === '1'){
                    this.$router.push("/home")
                }
            },
            logOut(){
                this.$store.commit('REMOVE_INFO');
                this.$router.push("/login");
                this.global.ws.close();
            },
            writeBlog(){
                this.$router.push("/blogedit")
            },
            deal(projectId,contributorId,res, index){
                this.$axios.get("/project/deal/"+contributorId+"/"+projectId+"?res="+res);
                this.requests.requestMessages.splice(index, 1);
                console.log(this.requests.requestMessages);
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