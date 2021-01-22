<template>
    <div>
        <el-header>
            <Navibar/>
        </el-header>

            <el-form :model="blogForm" :rules="this.rules" ref="blogForm" label-width="100px" class="blog-form">
                <el-form-item label="标题" prop="title">
                <el-input v-model = "blogForm.title"></el-input>
                </el-form-item>
                <el-form-item label="介绍" prop="summary">
                    <el-input type="textarea" v-model="blogForm.summary"></el-input>
                </el-form-item>
                <el-main>
                <el-form-item label="内容" prop="content" style="">
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
            </el-form>
        <el-dialog
                title="提示"
                :visible.sync="dialogVisible"
                width="30%"
        >
                    <span>
                        请选择博客分类(可选)
                        <el-checkbox-group v-model="this.checkList">
                            <el-checkbox v-for="l in columns" :label="l.id">{{l.c_name}}</el-checkbox>
                        </el-checkbox-group>
                    </span>
            <span slot="footer" class="dialog-footer">
                        <el-button @click="dialogVisible = false">取 消</el-button>
                        <el-button type="primary" @click="submitForm('blogForm')">确 定</el-button>
                    </span>
        </el-dialog>
    </div>

</template>

<script>
    import Navibar from 'components/common/Navibar'
    export default {
        name: "BlogEdit",
        components: {Navibar},
        data() {
            return {
                columns: [],//分栏
                blogForm: {
                    title: '',
                    summary: '',
                    content: '',
                    userId: '',
                    id: ''
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
                        { min: 5, max: 50, message: '长度在 5 到 50 个字符', trigger: 'blur'}
                    ]
                },
                dialogVisible: false,
                checkList:[],//修改后的分栏
                selectList: [],//修改前的分栏

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
                this.$axios.post('/blogimg/saveimg',formData,config).then((res => {
                    console.log(JSON.stringify(res));
                    this.$refs.md.$img2Url(pos,res.data.data["resultImgUrl"]);
                }))
            },
            imgDel(){},
            success() {
                this.$message({
                    message: '发布成功！',
                    type: 'success'
                });
            },
            senderror() {
                this.$message.error('错了哦，这是一条错误消息');
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
                    let entity, b = _this.selectList, a = _this.checkList
                    if (valid) {
                        //修改博客
                        if (this.$route.params.blogId){
                            _this.$axios.post("/blog/modifyblog",this.blogForm).then((res)=>{
                                entity = res.data;
                                if (entity.code === 200){
                                    //添加博客到分类
                                    let  list = {blogId:entity.data.id, before: b, after: a}
                                    _this.$axios.post("/column/blogtocolumn",list);

                                    console.log("submit successful！");
                                    // _this.sendEvent(1);
                                    this.success();
                                    _this.$router.push("/home");
                                }
                            })
                        }else {//新建博客
                            _this.$axios.post("/blog/saveblog",this.blogForm).then((res)=>{
                                entity = res.data;
                                if (entity.code === 200){
                                    //添加博客到分类
                                    let  list = {blogId:entity.data.id, before: b, after: a}
                                    _this.$axios.post("/column/blogtocolumn",list);
                                    console.log("submit successful！");
                                    // _this.sendEvent(2);
                                    this.success();
                                    _this.$router.push("/blogdetail/"+entity.data.id);
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
            this.blogForm.userId = userId;
            const blogId = this.$route.params.blogId;
            this.$axios("/column/getcolumnbyuserid/"+userId).then((res)=> {
                this.columns = res.data.data;
            });
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
                });
                this.$axios("/column/getcolumnidbyblogid/"+blogId).then((res)=> {
                    this.selectList = res.data.data;
                    this.checkList = this.selectList;
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