<template>
    <div>
        <el-container>
            <el-header>
                <Navibar/>
            </el-header>

            <el-main>
                <el-row>
                    <el-col :span="16" :offset="4">
                        <el-form :model="project" ref="dynamicValidateForm" label-width="100px" >
                            <el-form-item
                                    prop="projectName"
                                    label="项目名"
                                    :rules="[
                        { required: true, message: '请输入项目名', trigger: 'blur' },
                        { min: 3, max: 30, message: '长度在 3 到 30 个字符', trigger: 'blur' }
                        ]"
                            >
                                <el-input placeholder="3 到 30 个字符" v-model="project.projectName"></el-input>
                            </el-form-item>
                            <el-form-item>
                            </el-form-item>
                            <el-form-item
                                    v-for="(domain, index) in project.overview"
                                    :label="'简介' + (index+1)"
                                    :key="domain.key"
                                    :prop="'overview.' + index + '.content'"
                                    :rules="{required: true, message: '简介不能为空', trigger: 'blur'}">
                                <i class="el-icon-info" style="color: forestgreen">简介以“标题|内容的形式”，如“重点内容|掌握学习的正确姿势.......”</i>
                                <el-input maxlength="60"
                                          show-word-limit style="width: 90%; padding-right: 5px"
                                          :rows="2" placeholder="简介标题|简介内容"
                                          type="textarea" v-model="domain.content"></el-input>
                                <el-button @click.prevent="removeDomain(domain)">删除</el-button>
                            </el-form-item>
                            <el-form-item>
                                <el-button type="primary" @click="submitForm('dynamicValidateForm')">创建</el-button>
                                <el-button @click="addDomain">新增简介</el-button>
                                <el-button @click="resetForm('dynamicValidateForm')">重置</el-button>
                            </el-form-item>
                        </el-form>
                    </el-col>
                </el-row>
            </el-main>
        </el-container>
    </div>
</template>

<script>
    import Navibar from 'components/common/Navibar'
    export default {
        name: "StartProject",
        components: {Navibar},
        data() {
            return {
                project: {
                    startUser: 0,
                    projectName: '',
                    overview: [{content: ''}],
                }
            }
        },
        created() {
            this.project.startUser = this.$store.getters.getUser.id;
        },
        methods: {
            submitForm(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        this.$axios.post("/project/create", this.project).then(res => {
                            this.$router.replace("/projectdetail/"+res.data.data.id);
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
            removeDomain(item) {
                let index = this.project.overview.indexOf(item)
                if (index !== -1) {
                    this.project.overview.splice(index, 1)
                }
            },
            addDomain() {
                this.project.overview.push({
                    content: '',
                    key: Date.now()
                });
            }
        }
    }
</script>

<style scoped>

</style>