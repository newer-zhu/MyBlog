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
        <el-container>

            <el-main class="main">
                <el-upload
                  v-if="form.avatar == ''"
                  class="avatar-uploader"
                  name="avatar"
                  action="http://localhost:8081/media/uploadAvatar"
                  accept="image/png,image/jpg,image/jpeg"
                  :drag="false"
                  :show-file-list="false"
                  :on-success="handleAvatarSuccess"
                  :before-upload="beforeAvatarUpload">
                    <img v-if="imageUrl" :src="form.avatar" class="avatar">
                    <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                </el-upload>
                <el-image v-else :src="form.avatar"
                          style="width: 100px; height: 100px; text-align: center; border-radius: 3px"
                          fit="cover">
                </el-image>
                <div>
                    <el-form :rules="rules" ref="form" label-position="right" :model="form" label-width="80px" class="login">
                        <el-form-item label="用户名" class="LRItem" prop="username">
                            <el-input v-model="form.username"></el-input>
                        </el-form-item>
                        <el-form-item label="密码" class="LRItem" prop="password">
                            <el-input type="password" v-model="form.password"></el-input>
                        </el-form-item>
                        <el-form-item prop="college" class="LRItem" label="学校">
                            <el-input v-model="form.college"></el-input>
                        </el-form-item>
                        <el-form-item prop="major" class="LRItem" label="专业">
                            <el-cascader
                              style="width: 460px"
                              @change="handleChange"
                              :options="options"
                            ></el-cascader>
                        </el-form-item>
                        <el-form-item prop="grade" class="LRItem" label="年级">
                            <el-input placeholder=" (选填) " v-model="form.grade"></el-input>
                        </el-form-item>
                        <el-form-item label="邮箱" class="LRItem" prop="email">
                            <el-input type="email" placeholder=" (选填) " v-model="form.email"></el-input>
                        </el-form-item>
                        <el-form-item label="自我介绍" class="LRItem" prop="description">
                            <el-input type="textarea" v-model="form.description" placeholder="（选填）"></el-input>
                        </el-form-item>
                        <el-form-item>
                            <el-button type="primary" @click="submitForm('form')">创建</el-button>
                            <el-button @click="resetForm('form')">重置</el-button>
                        </el-form-item>
                    </el-form>
                </div>
            </el-main>
        </el-container>
    </div>
</template>

<script>
    export default {
        name: "Register",
        data() {
            return {
                imageUrl: '',
                form: {
                    avatar: '',
                    username: '',
                    password: '',
                    email: '',
                    college: '',
                    major: '',
                    grade: '',
                    description: ''
                },
                rules: {
                    username: [
                        { required: true, message: '请输入用户名', trigger: 'blur' },
                        { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
                    ],
                    password: [
                        { required: true, message: '请输入密码', trigger: 'blur' },
                        { min: 6, max: 12, message: '长度在 6 到 12 个字符', trigger: 'blur' }
                    ],
                    birthday: [
                        {  required: false, message: '请选择日期', trigger: 'change' }
                    ],
                    description: [
                        { required: false, message: '请填写个人简介', trigger: 'blur' }
                    ],
                    college: [
                        { required: true, message: '请填写学校', trigger: 'blur' }
                    ],
                    major: [
                        { required: true, message: '请填写专业', trigger: 'blur' }
                    ],
                    grade: [
                        { required: false, message: '请填写年级', trigger: 'blur' }
                    ],
                    email: [
                        { required: false, message: '请输入邮箱', trigger: 'blur' },
                        { type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change'] }
                    ]
                },
                options: [],
            };
        },
        methods: {
            submitForm(formName) {
                let _this = this;
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        _this.$axios.post("/user/register",this.form).then((res)=>{
                            let user = res.data.data;
                            if (res.data.code === 200)
                                _this.$router.replace("/login")
                        })
                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                });
            },
            resetForm(formName) {
                this.$refs[formName].resetFields();
            },
            handleAvatarSuccess(res) {
                this.form.avatar = res.data;
            },
            beforeAvatarUpload(file) {
                const isJPG = file.type === 'image/jpeg';
                const isLt10M = file.size / 1024 / 1024 < 10;

                if (!isJPG) {
                    this.$message.error('上传头像图片只能是 JPG 格式!');
                }
                if (!isLt10M) {
                    this.$message.error('上传头像图片大小不能超过 10MB!');
                }
                return isJPG && isLt10M;
            },
            handleChange(value){
                this.$axios.get("/major/getNameById/"+value[1]).then(res => {
                    this.form.major = res.data.data;
                });
            }
        },
        created(){
            this.$axios.get("/major").then(res => {
                this.options = res.data.data;
            });
        }
    }
</script>

<style scoped>
    .login{
        margin: 0 auto;
        width: 540px;
        padding-top: 50px;
    }
    .main{
        margin: 0 auto;
        text-align: center;
        border-radius: 5px;
        position: absolute;
        left: 45%;
        top: 50%;
        /*text-align: center;*/
        /*background-color: #99a9bf;*/
        transform: translate(-50%, -50%);
    }

    .avatar-uploader .el-upload {
        border: 1px dashed #d9d9d9;
        border-radius: 6px;
        cursor: pointer;
        position: relative;
        overflow: hidden;
    }
    .avatar-uploader .el-upload:hover {
        border-color: #409EFF;
    }
    .avatar-uploader-icon {
        font-size: 50px;
        color: #1a1b1b;
        width: 178px;
        height: 178px;
        line-height: 178px;
        text-align: center;
    }
    .avatar {
        width: 178px;
        height: 178px;
        display: block;
    }
    .background-img{
        background-image: url("../assets/img/earth.jpg");
        max-width: 100%;
        max-height: 100%;
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

<style>
    .LRItem .el-form-item__label{
        color: #fdfdfe;
    }
</style>