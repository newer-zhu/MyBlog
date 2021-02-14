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
                            </el-col>
                            <el-col :span="20">
                                <el-form-item label="介绍" prop="summary">
                                    <el-input type="textarea" show-word-limit
                                              maxlength="30" v-model="blogForm.summary"></el-input>
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
                    <el-button type="primary" @click="dialogVisible = true">立即发布</el-button>
                    <el-button @click="resetForm('blogForm')">重置</el-button>
                </div>

                <el-dialog
                        title="提示"
                        :visible.sync="dialogVisible"
                        width="30%"
                >
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
                                    v-for="item in pushTags"
                                    :key="item.id"
                                    :label="item.tagName"
                                    :value="item.tagName">
                            </el-option>
                          </el-select>
                    </span>
                    </el-row>
                    <br>
                    <el-row>
                        <span>
                        请选择分类(可选)
                            <!--                        <el-checkbox-group v-model="afterList">-->
                            <!--                            <el-checkbox v-for="l in columns" :label="l.id">{{l.c_name}}</el-checkbox>-->
                            <!--                        </el-checkbox-group>-->
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
                        <el-button @click="dialogVisible = false">取 消</el-button>
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
                event: {
                    content: '',
                    time: '',
                    level: '',
                    url: ''
                },//事件
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
                        { min: 0, max: 50, message: '长度在 0 到 50 个字符', trigger: 'blur'}
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
                console.log("开始上传");
                let formData = new FormData();
                formData.append('imgFile', file);
                let config = {};
                this.$axios.post('/media/saveimg',formData,config).then((res => {
                    console.log(JSON.stringify(res));
                    this.$refs.md.$img2Url(pos,res.data.data["resultImgUrl"]);
                }))
            },
            imgDel(){},
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
            // sendEvent(e) {
            //     //e = 1修改博客，2新建博客
            //     if (e === 1){
            //         this.event.content = '修改了博客 '+this.blogForm.title.slice(0, 7) + '...'
            //     }else if (e ==2){
            //         this.event.content = '新建了博客 '+this.blogForm.title.slice(0, 7) + '...'
            //     }
            //     console.log(this.$sendEvent(this.event));
            // },
            submitForm(formName) {
                let _this = this;
                this.$refs[formName].validate((valid) => {
                    // let entity, b = _this.beforeList, a = _this.afterList
                    if (valid) {
                        //新建的博客
                        let newBlog = {};
                        //修改博客
                        if (this.$route.params.blogId){
                            _this.$axios.post("/blog/modifyblog",this.blogForm).then((res)=>{
                                if (res.data.code === 200){
                                    //添加博客到分类
                                    if (!isNaN(_this.selectColumn)){
                                        _this.$axios.get("/column/blogtocolumn/"+_this.blogForm.id+"/"+_this.selectColumn);
                                    }
                                    // _this.sendEvent(1);
                                    this.success('发布成功！');
                                    _this.$router.push("/home");
                                }
                            })
                        }else {//新建博客
                            _this.$axios.post("/blog/saveblog",this.blogForm).then((res)=>{
                                newBlog = res.data.data;
                                _this.blogForm.id = newBlog.id;
                                if (res.data.code === 200){
                                    //这里说明没有更改分栏
                                    if (!isNaN(_this.selectColumn)){
                                        _this.$axios.get("/column/blogtocolumn/"+newBlog.id+"/"+_this.selectColumn);
                                    }
                                    // _this.sendEvent(2);
                                    this.success('发布成功');
                                    _this.$router.push("/blogdetail/"+newBlog.id);
                                }
                            })
                        }
                        if (_this.tags.length != 0){
                            //把自己创建的blog标签对象化
                            for (let tag in _this.tags){
                                let t =_this.tags[tag]
                                if (t.id == undefined){
                                    _this.tags[tag] = {id: 0, tagName: t, number: 0, image: ''}
                                    console.log(t);
                                }
                            }
                            console.log(_this.tags)
                            _this.$axios.post("/tag/blogtotags/"+_this.blogForm.id, _this.tags).then(res => {
                                console.log(res.data.data);})
                        }
                    } else {
                        this.error('发布失败，请稍后再试');
                        console.log('error submit!!');
                        return false;
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
            const blogId = this.$route.params.blogId;
            this.$axios("/column/getcolumnbyuserid/"+userId).then((res)=> {
                this.columns = res.data.data;
            });

            if (blogId){
                //博客回显
                this.$axios.get("/blog/getbyid/" + blogId).then(res => {
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
                this.$axios.get("/tag/getbyblogid/" + blogId).then(res => {
                    this.pushTags = res.data.data;
                });
                //获取分栏列表
                this.$axios("/column/getcolumnbyuserid/"+userId).then((res)=> {
                    this.columns = res.data.data;
                })
                //已选分栏
                this.$axios("/column/getcolumnbyblogid/"+blogId).then((res)=> {
                    let select = res.data.data['cname']
                    if (select != null){
                        this.selectColumn = select;
                    }
                })
            }
        }
    }
</script>

<style scoped>
    .blog-form{
        margin-top: 50px;
    }
</style>