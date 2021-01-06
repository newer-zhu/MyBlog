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
                <el-form-item label="内容" prop="content" style="margin-bottom: 20px; padding-bottom: 50px">
                    <quill-editor :options="editorOption"
                                  v-model="blogForm.content"
                                  @blur="onEditorBlur($event)"
                                  @change="onEditorChange($event)"
                                  @focus="onEditorFocus($event)"
                    ></quill-editor>
                </el-form-item>

                <el-form-item style="text-align: center;">
                    <el-button type="primary" @click="dialogVisible = true">立即发布</el-button>
                    <el-button @click="resetForm('blogForm')">重置</el-button>
                </el-form-item>
            </el-form>
            <el-dialog
                    title="提示"
                    :visible.sync="dialogVisible"
                    width="30%"
                    >
                <span>
                    请选择博客分类(可选)
                    <el-checkbox-group v-model="checkList">
                        <el-checkbox v-for="l in columns" :label="l.id">{{l.c_name}}</el-checkbox>
                    </el-checkbox-group>
                </span>
                <span slot="footer" class="dialog-footer">
                    <el-button @click="dialogVisible = false">取 消</el-button>
                    <el-button type="primary" @click="submitForm('blogForm')">确 定</el-button>
                </span>
            </el-dialog>
        </el-main>
    </div>

</template>

<script>
    import Navibar from 'components/common/Navibar'
    import {quillEditor} from 'vue-quill-editor'
    export default {
        name: "BlogEdit",
        components: {Navibar},
        data() {
            return {
                columns: [],//分类
                blogForm: {
                    title: '',
                    summary: '',
                    content: '',
                    userId: '',
                    id: ''
                },
                editorOption: {},
                rules: {
                    title: [
                        { required: true, message: '请输入标题', trigger: 'blur' },
                        { min: 1, max: 30, message: '长度在 1 到 30 个字符', trigger: 'blur' }
                    ],
                    content: [
                        { required: true, message: '请输入内容', trigger: 'blur' }
                    ],
                    summary: [
                        { required: false, message: '请填写摘要', trigger: 'blur' },
                        { min: 5, max: 50, message: '长度在 5 到 50 个字符', trigger: 'blur'}
                    ]
                },
                dialogVisible: false,
                checkList: []
            }
        },
        methods: {
            onEditorBlur(editor) {//失去焦点事件
            },
            onEditorFocus(editor) {//获得焦点事件
            },
            onEditorChange({editor, html, text}) {//编辑器文本发生变化
                //this.content可以实时获取到当前编辑器内的文本内容
                console.log(this.content);
            },
            resetForm(formName) {
                this.$refs[formName].resetFields();
            },
            success() {
                this.$message({
                    message: '发布成功！',
                    type: 'success'
                });
            },
            senderror() {
                this.$message.error('错了哦，这是一条错误消息');
            },
            submitForm(formName) {
                let _this = this;
                this.$refs[formName].validate((valid) => {
                    let entity;
                    if (valid) {
                        //修改博客
                        if (this.$route.params.blogId){
                            _this.$axios.post("/blog/modifyblog",this.blogForm).then((res)=>{
                                entity = res.data;
                                if (entity.code === 200){
                                    //添加博客到分类
                                    if (this.checkList.length !== 0){
                                        for (let cid in this.checkList){
                                            let url = "/blog/blogToColumn?blogId="+ entity.data.id + "&cid="+this.checkList[cid]
                                            this.$axios.get(url);
                                        }
                                    }
                                    console.log("submit successful！");
                                    this.success();
                                    _this.$router.push("/home");
                                }
                            })
                        }else {//新建博客
                            _this.$axios.post("/blog/saveblog",this.blogForm).then((res)=>{
                                entity = res.data;
                                if (entity.code === 200){
                                    //添加博客到分类
                                    if (this.checkList.length !== 0){
                                        for (let cid in this.checkList){
                                            let url = "/blog/blogToColumn?blogId="+ entity.data.id + "&cid="+this.checkList[cid]
                                            this.$axios.get(url);
                                        }
                                    }
                                    console.log("submit successful！");
                                    this.success();
                                    _this.$router.push("/blogedit/"+entity.data.id);
                                }
                            })
                        }
                    } else {
                        this.senderror();
                        console.log('error submit!!');
                        return false;
                    }
                });
            },
        },
        created() {
            const userId = this.$store.getters.getUser.id;
            this.blogForm.userId = userId
            const blogId = this.$route.params.blogId;
            this.$axios("/blog/getColumnByUserId/"+userId).then((res)=> {
                this.columns = res.data.data;
            })
            //博客回显
            if (blogId){
                this.$axios.get("/blog/getbyid/" + blogId).then(res => {
                    const blog = res.data.data
                    if (blog){
                        this.blogForm.id = blogId
                        this.blogForm.title = blog.title
                        this.blogForm.summary = blog.summary
                        this.blogForm.content = blog.content
                    }
                })
            }
        }
    }
</script>

<style scoped>
    .quill-editor{
        line-height: normal !important;
        height: 450px;
    }
    .blog-form{
        margin-top: 50px;
    }
</style>