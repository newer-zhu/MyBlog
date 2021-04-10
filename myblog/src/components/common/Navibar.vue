<template>
    <div>
        <div>
            <el-menu :default-active="activeIndex" mode="horizontal" @select="handleSelect" background-color="#409dfe"
                     text-color="#fff" active-text-color="#ffd04b">
                <el-menu-item index="1" style="margin-left: 550px; width: 100px"><div class="el-icon-s-home">首页</div></el-menu-item>
                <el-submenu style="width: 100px" index="2">
                    <template slot="title"><div class="el-icon-thumb">发布</div></template>
                    <el-menu-item @click="writeBlog" index="2-1"><div class="el-icon-edit">写笔记</div></el-menu-item>
                    <router-link :to="{name: 'StartProject'}">
                        <el-menu-item index="2-2"><div class="el-icon-edit-outline" >发起项目</div></el-menu-item>
                    </router-link>
                </el-submenu>
                <el-menu-item @click="connect" index="3" style="margin-bottom: 0px; width: 100px">
                    <div v-if="requests.requestMessages.length != 0" class="el-icon-message" style="line-height: 30px">
                        <el-badge  :value="requests.requestMessages.length">
                            <div style="margin-bottom: 3px">消息</div>
                        </el-badge>
                    </div>
                    <div class="el-icon-message" v-else>
                        消息
                    </div>
                </el-menu-item>
                <el-menu-item index="4" style="width: 100px" @click="drawer = !drawer"><div class="el-icon-user">个人</div></el-menu-item>
                <el-menu-item index="6" style="float: right; right: 50px;width: 100px;" @click="logOut"><div class="el-icon-s-promotion">退出</div></el-menu-item>
            </el-menu>
            <el-drawer
                    :visible.sync="message"
                    size="25%"
                    direction="ltr"
                    :with-header="false"
            >
                <div class="message">
                    <div >
                        <div v-show="requests.requestMessages.length == 0">
                            <p style="color: #f7f5f6;font-size: 20px; text-align: center">暂时还没有消息哦</p>
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
            // websocket方案
            connect(){
                if (this.global.ws.readyState == 1){
                    console.log("已经建立连接");
                }else {
                    this.localSocket();
                }
                this.message = !this.message;
            },
            //传统方案
            loadMessage(){
                this.$axios.get("/message/"+this.userId).then(res => {
                    this.requests.requestMessages = [];
                    let reqs = res.data.data;
                    for (let r in reqs){
                        reqs[r].id = this.requests.nextId++;
                        this.requests.requestMessages.push(reqs[r]);
                    }
                });
                this.message = !this.message;
            },
            //websocket方案
            localSocket() {
                let that = this;
                if ("WebSocket" in window) {
                    let ws = new WebSocket("ws://localhost:8081/pro/"+that.$store.getters.getUser.id);
                    that.global.setWs(ws);
                    that.global.ws.onopen = function() {
                        console.log('websocket连接成功');
                    };
                    that.global.ws.onmessage = e => {
                        let reqs = JSON.parse(e.data);
                        console.log(reqs);
                        for (let r in reqs){
                            reqs[r].id = this.requests.nextId++;
                            this.requests.requestMessages.push(reqs[r]);
                        }
                    };
                    that.global.ws.onclose = function () {
                        // 关闭 websocket
                        console.log("连接已关闭...");
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
                if (this.global.ws.readyState == 1)
                    this.global.ws.close();
            },
            writeBlog(){
                this.$router.push("/blogedit")
            },
            deal(projectId,contributorId,res, index){
                this.$axios.get("/project/deal/"+contributorId+"/"+projectId+"?res="+res);
                this.requests.requestMessages.splice(index, 1);
                // console.log(this.requests.requestMessages);
            }
        },
        created(){
            if (this.user.username == ''){
                this.user = this.$store.getters.getUser;
            }
        },
        computed:{
            userId(){
                return this.$store.getters.getUser.id;
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
    .message{
        background-image: url("../../assets/img/messageBack.jpg");
        /*-moz-background-size:100% 100%; background-size:100% 100%;*/
        height: 100%;
    }
    .el-menu--horizontal>.el-submenu .el-submenu__icon-arrow{
        margin-left: 25px;
        position: static;
        vertical-align: middle;
        margin-top: -3px;
    }
</style>