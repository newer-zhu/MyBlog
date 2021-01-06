<template>
    <div>
        <el-container>
            <el-header>
                <Navibar/>
            </el-header>

            <el-main >
                    <el-upload style="text-align: center"
                            class="avatar-uploader"
                            name="avatarPic"
                            action="http://localhost:8081/web/avatarUpload"
                            accept="image/png,image/gif,image/jpg,image/jpeg"
                            :drag="true"
                            :show-file-list="false"
                            :on-success="handleAvatarSuccess"
                            :before-upload="beforeAvatarUpload">
                        <img v-if="imageUrl" :src="imageUrl" class="avatar">
                        <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                    </el-upload>

                <el-form :rules="rules" ref="form" label-position="left" :model="form" label-width="80px" class="login">
                    <el-form-item label="用户名" prop="username">
                        <el-input v-model="form.username"></el-input>
                    </el-form-item>
                    <el-form-item label="密码" prop="password">
                        <el-input type="password" v-model="form.password"></el-input>
                    </el-form-item>
                    <el-form-item label="邮箱" prop="email">
                        <el-input type="email" v-model="form.email"></el-input>
                    </el-form-item>
                    <el-form-item prop="birthday" label="生日">
                        <el-date-picker value-format="yyyy-MM-dd" type="date" placeholder="选择日期" v-model="form.birthday" style="width: 100%;"></el-date-picker>
                    </el-form-item>
                    <el-form-item label="自我介绍" prop="description">
                        <el-input type="textarea" v-model="form.description" placeholder="（可选）"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" @click="submitForm('form')">立即创建</el-button>
                        <el-button @click="resetForm('form')">重置</el-button>
                    </el-form-item>
                </el-form>

            </el-main>
        </el-container>
    </div>
</template>

<script>
    import Footer from "components/common/Footer"
    import Navibar from "components/common/Navibar";
    export default {
        name: "Register",
        components: {Navibar,Footer},
        data() {
            return {
                imageUrl: '',
                form: {
                    username: '',
                    password: '',
                    email: '',
                    birthday: '',
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
                        {  required: true, message: '请选择日期', trigger: 'change' }
                    ],
                    description: [
                        { required: false, message: '请填写个人简介', trigger: 'blur' }
                    ],
                    email: [
                        { required: false, message: '请输入邮箱', trigger: 'blur' },
                        { min: 6, max: 30, message: '长度在 6 到 30 个字符', trigger: 'blur' }
                    ]
                }
            };
            },
        methods: {
            submitForm(formName) {
                let _this = this;
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        _this.$axios.post("/user/register",this.form).then((res)=>{
                            let user = res.data.data;
                            console.log(user);
                            if (res.data.code === 200)
                                _this.$router.push("/login")

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
                console.log(res);
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
        font-size: 28px;
        color: #8c939d;
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
</style>