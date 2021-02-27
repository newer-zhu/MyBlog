<template>
    <div>
        <el-header>
            <Navibar/>
        </el-header>

            <el-form @submit.native.prevent :model="blogForm" :rules="this.rules" ref="blogForm" label-width="100px" class="blog-form">
                <el-row>
                    <el-col :span="16">
                        <el-row style="margin-left: 25px">
                            <el-col :span="20">
                                <el-form-item label="标题" prop="title">
                                    <el-input  v-model = "blogForm.title"></el-input>
                                </el-form-item>
                                <el-form-item label="摘要" prop="summary">
                                    <el-input type="textarea"
                                              maxlength="100" v-model="blogForm.summary"></el-input>
                                </el-form-item>
                            </el-col>
                        </el-row>
                    </el-col>
                    <el-col :span="8">
                        <el-switch
                                style="display: block;"
                                v-model="type"
                                active-color="yellow"
                                inactive-color="blue"
                                active-text="文件"
                                inactive-text="文章">
                        </el-switch>
                    </el-col>
                </el-row>

                <el-main>
                    <el-form-item v-if="type">
                        <el-upload
                                name="file"
                                class="upload-demo"
                                drag
                                action="http://localhost:8081/media/fileupload/"
                                :on-success="uploadSuccess"
                                :on-error="error"
                                multiple>
                            <i class="el-icon-upload"></i>
                            <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
                            <div class="el-upload__tip" slot="tip">文件大小不超过100mb，多文件上传请压缩</div>
                        </el-upload>
                    </el-form-item>
                    <el-form-item prop="content" v-else>
                        <mavon-editor
                                    :toolbars="markdownOption"
                                    v-model="blogForm.content"
                                    @imgAdd = "handleImgAdd"
                                    @imgDel = "imgDel"
                                    ref=md
                                    :ishljs = "true"
                            />
                    </el-form-item>
                </el-main>
                <div style="text-align: center;margin-bottom: 50px">
                    <el-button type="primary" @click="updateBlog('blogForm')">立即发布</el-button>
                    <el-button @click="resetForm('blogForm')">重置</el-button>
                </div>

                <el-dialog
                        title="提示"
                        :visible.sync="dialogVisible"
                        width="30%"
                >
                    <span style="color: #409EFF; padding-bottom: 10px" class="el-icon-question">标签可多选，分类仅能选一类</span>
                    <el-row>
                        <span>
                        标签(可自定义)
                         <el-select v-model="tags"
                                    filterable
                                    allow-create
                                    default-first-option
                                    multiple
                                    placeholder="输入自定义标签">
                            <el-option
                                    v-for="(item, index) in pushTags"
                                    :key="index"
                                    :label="item"
                                    :value="item">
                            </el-option>
                          </el-select>
                    </span>
                    </el-row>
                    <br>
                    <el-row>
                        <span>
                        请选择分类(可选)
                          <el-select v-model="selectColumn" placeholder="请选择">
                            <el-option
                                    v-for="item in columns"
                                    :key="item.id"
                                    :label="item.cname"
                                    :value="item.id">
                            </el-option>
                          </el-select>
                    </span>
                    </el-row>
                    <span slot="footer" class="dialog-footer">
                        <el-button @click="cancel">算了</el-button>
                        <el-button type="primary" @click="submitForm('blogForm')">确 定</el-button>
                    </span>
                </el-dialog>
            </el-form>
    </div>

</template>

<script>
    import Navibar from 'components/common/Navibar'
    import 'mavon-editor/dist/markdown/github-markdown.min.css'
    export default {
        name: "BlogEdit",
        components: {Navibar},
        data() {
            return {
                type: false,//类型，默认文章
                columns: [],//分栏
                tags: [],//文章的标签
                pushTags: [],//后台推送的标签
                blogForm: {
                    title: '',
                    summary: '',
                    content: '',
                    userId: '',
                    id: '',
                    isFile: 0,
                    visitors: 0,
                },
                projectId: '',
                markdownOption: {
                    bold: true, // 粗体
                    italic: true, // 斜体
                    header: true, // 标题
                    underline: true, // 下划线
                    strikethrough: true, // 中划线
                    mark: true, // 标记
                    superscript: true, // 上角标
                    subscript: true, // 下角标
                    quote: true, // 引用
                    ol: true, // 有序列表
                    ul: true, // 无序列表
                    link: true, // 链接
                    imagelink: true, // 图片链接
                    code: true, // code
                    table: true, // 表格
                    fullscreen: true, // 全屏编辑
                    readmodel: true, // 沉浸式阅读
                    htmlcode: true, // 展示html源码
                    help: true, // 帮助
                    /* 1.3.5 */
                    undo: true, // 上一步
                    redo: true, // 下一步
                    trash: true, // 清空
                    save: true, // 保存（触发events中的save事件）
                    /* 1.4.2 */
                    navigation: true, // 导航目录
                    /* 2.1.8 */
                    alignleft: true, // 左对齐
                    aligncenter: true, // 居中
                    alignright: true, // 右对齐
                    /* 2.2.1 */
                    subfield: true, // 单双栏模式
                    preview: true, // 预览
                },
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
                        { min: 0, max: 100, message: '长度在 0 到 100 个字符', trigger: 'blur'}
                    ]
                },
                dialogVisible: false,
                selectColumn: '',
            }
        },
        watch: {
            type(){
                return this.blogForm.isFile = this.type === false? 0 : 1;
            }
        },
        methods: {
            resetForm(formName) {
                this.$refs[formName].resetFields();
            },
            handleImgAdd(pos, file){
                console.log("开始上传图片");
                let formData = new FormData();
                formData.append('imgFile', file);
                let config = {};
                this.$axios.post('/media/saveimg',formData,config).then((res => {
                    console.log(JSON.stringify(res));
                    this.$refs.md.$img2Url(pos,res.data.data["resultImgUrl"]);
                }))
            },
            imgDel(pos){
                console.log(pos);
            },
            success(msg) {
                this.$message({
                    message: msg,
                    type: 'success'
                });
            },
            error(msg) {
                if (msg.length == 0){
                    msg = '出错啦'
                }
                this.$message.error(msg);
            },
            updateBlog(formName){
                let _this = this;
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        //修改博客
                        if (this.$route.params.blogId){
                            _this.$axios.post("/blog/modifyblog",this.blogForm).then((res)=>{
                                _this.blogForm = res.data.data.blog;
                                _this.columns = res.data.data.columns;
                                _this.pushTags = res.data.data.pushTags;
                                if (res.data.code === 200){
                                    this.success('发布成功！');
                                    _this.dialogVisible = true;
                                }
                            })
                        }else {//新建博客
                            _this.$axios.post("/blog/save?projectId="+_this.projectId,this.blogForm).then((res)=>{
                                _this.blogForm = res.data.data.blog;
                                _this.columns = res.data.data.columns;
                                _this.pushTags = res.data.data.pushTags;
                                if (res.data.code === 200){
                                    this.success('发布成功！');
                                    _this.dialogVisible = true;
                                }
                            })
                        }
                    } else {
                        this.error('发布失败，请稍后再试');
                        console.log('error submit!!');
                        return false;
                    }
                });

            },
            cancel(){
                this.dialogVisible = false;
                this.$router.replace({
                    name: "BlogDetail",
                    params: {
                        blogId: _this.blogForm.id
                    }
                });
            },
            submitForm() {
                let _this = this;
                //添加博客到分类
                if (!isNaN(_this.selectColumn)) {
                    _this.$axios.get("/column/blogtocolumn/" + _this.blogForm.id + "?columnId=" + _this.selectColumn);
                }
                //给博客打标签
                if (_this.tags.length != 0){
                    _this.$axios.post("/tag/blogtotags/"+_this.blogForm.id, _this.tags).then(res => {
                        console.log(res.data.data);})
                }
                this.$router.replace({
                    name: "BlogDetail",
                    params: {
                        blogId: _this.blogForm.id
                    }
                });
            },
            uploadSuccess(response, file, fileList){
                console.log(response);
                this.blogForm.content = response.data;
                this.success('上传成功！')
            }
        },
        created() {
            const userId = this.$store.getters.getUser.id;
            this.blogForm.userId = userId;
            this.projectId = this.$route.params.projectId;
            const blogId = this.$route.params.blogId;
            this.$axios("/column/getcolumnbyuserid/"+userId).then((res)=> {
                this.columns = res.data.data;
            });

            if (blogId){
                //博客回显
                this.$axios.get("/blog/" + blogId).then(res => {
                    const blog = res.data.data;
                    if (blog){
                        this.blogForm.id = blogId;
                        this.blogForm.title = blog.title;
                        this.blogForm.summary = blog.summary;
                        this.blogForm.content = blog.content;
                        this.blogForm.isFile = blog.isFile;
                        this.blogForm.visitors = blog.visitors;
                    }
                });
                //获取标签列表
                // this.$axios.get("/tag/getbyblogid/" + blogId).then(res => {
                //     this.pushTags = res.data.data;
                // });
                //获取分栏列表
                // this.$axios("/column/getcolumnbyuserid/"+userId).then((res)=> {
                //     this.columns = res.data.data;
                // })
            }
        }
    }
</script>

<style scoped>
    .blog-form{
        margin-top: 50px;
    }
</style>