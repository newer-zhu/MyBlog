<template>
    <div>
        <vue-particles
                color="#475669"
                :particleOpacity="0.7"
                :particlesNumber="100"
                shapeType="circle"
                :particleSize="4"
                linesColor="#dedede"
                :linesWidth="1"
                :lineLinked="true"
                :lineOpacity="0.4"
                :linesDistance="150"
                :moveSpeed="3"
                :hoverEffect="true"
                hoverMode="grab"
                :clickEffect="true"
                clickMode="push"
                class="background-img">
        </vue-particles>
        <!--                粒子特效-->
        <div>
            <el-form :rules="rules" ref="form" label-position="left" :model="form" label-width="80px" class="login">
                <el-form-item label="用户名" class="LRItem" prop="username">
                    <el-input v-model="form.username"></el-input>
                </el-form-item>
                <el-form-item label="密码" class="LRItem" prop="password">
                    <el-input type="password" v-model="form.password"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="submitForm('form')">登录</el-button>
                    <el-button type="success" @click="toRegister" plain>注册</el-button>
                    <el-button @click="resetForm('form')">重置</el-button>
                </el-form-item>
            </el-form>
        </div>
    </div>
</template>

<script>
    export default {
        name: "Login",
        data() {
            return {
                form: {
                    username: 'testUser',
                    password: '111111',
                },
                rules: {
                    username: [
                        {required: true, message: '请输入用户名', trigger: 'blur'},
                        {min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur'}
                    ],
                    password: [
                        {required: true, message: '请输入密码', trigger: 'blur'},
                        {min: 6, max: 12, message: '长度在 6 到 12 个字符', trigger: 'blur'}
                    ]
                }
            }
        },
        methods: {
            toRegister(){
                this.$router.push("/register");
            },
            submitForm(formName) {
                const _this = this;
                this.$refs[formName].validate((valid) => {
                    if (valid) {//点击登录后的逻辑
                        this.$axios.post("/user/login",this.form).then((res) => {
                            // console.log(res)
                            const jwt = res.headers['authorization'];
                            const userInfo = res.data.data

                            _this.$store.commit('SET_TOKEN', jwt);
                            _this.$store.commit('SET_USERINFO', userInfo)
                            if (res.data.code === 200){
                                _this.$router.push("/home")
                                _this.$message.success("登录成功")
                            }else {
                                _this.$message.error(res.data.msg)
                            }

                        })
                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                });
            },
            resetForm(formName) {
                this.$refs[formName].resetFields();
            }
        }
    }
</script>

<style scoped>
    .login{
        border-radius: 5px;
        position: absolute;
        left: 45%;
        top: 50%;
        /*text-align: center;*/
        /*background-color: #99a9bf;*/
        transform: translate(-50%, -50%);
        /*width: 380px;*/
        /*height: 220px;*/
    }
    .background-img{
        background-image: url("../assets/img/earth.jpg");
        /*width: 100%;*/
        /*height: calc(100% - 100px);*/
        /*position: absolute;*/
        /*background-position: center;*/
        /*background-repeat: no-repeat;*/
        /*background-attachment: fixed;*/
    }
    #particles-js{
        width: 100%;
        height: 100%;
        position: absolute;
    }
</style>