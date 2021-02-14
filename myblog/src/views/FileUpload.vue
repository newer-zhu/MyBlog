<template>
    <div>
        <el-header>
            <Navibar/>
        </el-header>

        <el-main>
            <el-form :model="blogForm" :rules="this.rules" ref="blogForm" label-width="100px" class="blog-form">

                <el-form-item label="标题" prop="title">
                    <el-input v-model = "blogForm.title"></el-input>
                </el-form-item>
                <el-form-item label="介绍" prop="summary">
                    <el-input type="textarea" v-model="blogForm.summary"></el-input>
                </el-form-item>
                <div style="text-align: center;margin-bottom: 50px">
                    <el-button type="primary" @click="dialogVisible = true">立即发布</el-button>
                    <el-button @click="resetForm('blogForm')">重置</el-button>
                </div>
                <el-dialog
                        title="提示"
                        :visible.sync="dialogVisible"
                        width="30%"
                >
                    <span>
                        请选择分类(可选)
                        <el-checkbox-group v-model="afterList">
                            <el-checkbox v-for="l in columns" :label="l.id">{{l.c_name}}</el-checkbox>
                        </el-checkbox-group>
                    </span>
                    <span slot="footer" class="dialog-footer">
                        <el-button @click="dialogVisible = false">取 消</el-button>
                        <el-button type="primary" @click="submitForm('blogForm')">确 定</el-button>
                    </span>
                </el-dialog>
            </el-form>

        </el-main>
    </div>
</template>

<script>
    import {Navibar} from 'components/common/Navibar'
    export default {
        name: "FileUpload",
        data(){
            return{

            }
        }
    }
</script>

<style scoped>

</style>