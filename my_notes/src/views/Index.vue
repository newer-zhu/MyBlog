<template>
  <div>
    <el-container>
      <el-header style="height: 5%; ">
        <Navibar></Navibar>
      </el-header>
      <el-container class="main">
        <el-main>
          <el-row>
            <!--                      侧边搜索-->
            <el-col :span="5">
              <SearchList/>
            </el-col>
            <!--                      主体-->
            <el-col :span="18" :offset="1">
              <el-row :gutter="20">
                <!--                              博客列表-->
                <el-col :span="16">
                  <router-view name="listDetail"/>
                  <router-view name="projectList"/>
                </el-col>
                <!--                                右侧列表-->
                <transition name="el-fade-in-linear">
                  <el-col :span="8">
                    <div style="padding-bottom: 3px">
                      <el-card shadow="never" style="">
                        <div slot="header" class="clearfix">
                          <span><i style="font-size: 20px" class="el-icon-notebook-2"></i> 文章分类</span>
                          <el-button @click="createCol" style="padding: 3px; margin-left: 100px; font-size: 16px"
                                     class="el-icon-document-add" type="text">新建
                          </el-button>
                          <el-button v-if="!isShowDelCol" @click="changeDelCol" style="float: right; padding: 3px 0; font-size: 16px"
                                     class="el-icon-delete" type="text">删除
                          </el-button>
                          <el-button v-else @click="changeDelCol" style="float: right; padding: 3px 0" type="text">取消
                          </el-button>
                        </div>
                        <div style="">
                          <el-row type="flex" style="line-height: 1px; height: 15px">
                            <el-col :span="18">
                              <router-link :to="{name: 'ListDetail', params: {columnId: -1}}">
                                <h2 style="color: rgba(35,133,124,0.99)">{{"我的文章"}}</h2>
                              </router-link>
                            </el-col>
                          </el-row>
                          <el-divider/>
                        </div>
                        <div v-for="(column, index) in this.columns" :key="column.c_name" class="text item">
                          <el-row type="flex" style="line-height: 1px; height: 15px">
                            <el-col :span="18">
                              <router-link :to="{name: 'ListDetail', params: {columnId: column.id}}">
                                <h2 style="color: #3449bf">{{column.cname+' ('+column.count+')'}}</h2>
                              </router-link>
                            </el-col>
                            <el-col :span="6">
                              <el-button type="danger" size="small" @click="delCol(column.id)" v-show="isShowDelCol">删除
                              </el-button>
                            </el-col>
                          </el-row>
                          <el-divider style="margin-top: 2px;"/>
                        </div>
                      </el-card>
                    </div>

                    <div style="padding-bottom: 3px">
                      <router-link :to="{path: '/home/projectList/'+this.userId+'/n'}">
                        <el-card shadow="hover">
                          <div>
                            <span><i style="font-size: 20px" class="el-icon-folder-opened"></i> 项目集</span>
                          </div>
                        </el-card>
                      </router-link>
                    </div>

                    <div>
                      <router-link :to="{path: '/home/projectList/'+this.userId+'/y'}">
                        <el-card shadow="hover">
                          <div>
                            <span><i style="font-size: 20px" class="el-icon-collection"></i> 收藏项目</span>
                          </div>
                        </el-card>
                      </router-link>
                    </div>
                  </el-col>
                </transition>
                <el-col :span="16" v-if="$route.path == '/home'">
                  <el-tabs v-model="active" type="border-card" @tab-click="handleClick">
                    <el-tab-pane name="first">
                      <span slot="label"><h3 class="el-icon-more">本专业</h3></span>
                      <div style="padding: 5px" class="source">
                        <div>
                          <ul class="infinite-list" style="overflow:auto">
                            <li class="infinite-list-item" v-for="su in suggest.suggestions">
                              <div>
                                <div>
                                  <router-link :to="{name: 'BlogDetail', params: {blogId: su.id}}">
                                    <div style="font-size: 25px; color: #303133; display: inline">
                                      {{su.title}}
                                    </div>
                                  </router-link>
                                  <div style="display: inline; color: #8c939d; float:right;">{{su.createdAt}}</div>
                                  <p style="color: #8c939d">{{su.summary.slice(0, 40)+'...'}}</p>
                                </div>
                              </div>
                            </li>
                          </ul>
                        </div>
                      </div>
                      <el-pagination
                        style="text-align: center; padding-top: 25px"
                        background
                        @current-change="handCurrentChange"
                        :current-page="suggest.currentPage"
                        layout="prev, pager, next"
                        :page-count="suggest.total">
                      </el-pagination>
                    </el-tab-pane>
                    <el-tab-pane name="second">
                      <span slot="label"><h3 class="el-icon-s-order">排行榜</h3></span>
                      <div style="height: 450px; width: 100%" v-if="rank">
                        <dv-scroll-ranking-board :config="config" class="range-flow"/>
                      </div>
                    </el-tab-pane>
                  </el-tabs>
                </el-col>
                <el-backtop :right="320" :bottom="50">
                  <div
                    style="{
                              height: 100%;
                              width: 100%;
                              background-color: #f2f5f6;
                              box-shadow: 0 0 6px rgba(0,0,0, .12);
                              text-align: center;
                              line-height: 40px;
                              color: #1989fa;
                                  }"
                  >
                    UP
                  </div>
                </el-backtop>
                <el-dialog
                  title="提示"
                  :visible.sync="isVisible"
                  width="30%"
                  center>
                  <span>分栏名称</span>
                  <el-input style="padding-top: 5px" v-model="newCol"></el-input>
                  <span slot="footer" class="dialog-footer">
    <el-button @click="isVisible = false">取 消</el-button>
    <el-button type="primary" @click="addColumn">确 定</el-button>
  </span>
                </el-dialog>
              </el-row>
            </el-col>
          </el-row>
        </el-main>

        <el-footer>
          <Footer/>
        </el-footer>
      </el-container>
    </el-container>
  </div>

</template>

<script>
  import Footer from "components/common/Footer"
  import Navibar from "components/common/Navibar";
  import ListDetail from "./Blog/ListDetail";
  import SearchList from '../components/common/SearchList'
  export default {
    name: "Index",
    components: {ListDetail, Footer, Navibar, SearchList},
    data() {
      return {
        headImg: require('../assets/img/head.png'),
        //轮流榜
        config: {
          data: [],
          unit: '热度'
        },
        rank: false,
        userId: 0,
        columns: [],
        projects: [],
        favorite: [],
        isShowDelCol: false,//是否显示删除专栏按钮
        isVisible: false,
        newCol: '',
        isShowList: true,
        checkList: [],
        randColor: {//分栏字体显示
          color: this.getColor(),
          fontSize: '20px'
        },
        active: 'first',
        suggest:{
            suggestions: [],
            total: 0
        },
      }
    },
    methods: {
      handCurrentChange(current) {
        this.loadSuggestion(current);
      },
      getColor() {
        let r = Math.floor(Math.random() * 255);
        let g = Math.floor(Math.random() * 255);
        let b = Math.floor(Math.random() * 255);
        return 'rgba(' + r + ',' + g + ',' + b + ',1)';
      },
      changeDelCol() {
        this.isShowDelCol = !this.isShowDelCol;
      },
      createCol() {
        this.isVisible = !this.isVisible;
      },
      //新建分栏函数
      addColumn() {
        this.$axios.post("/column/create", {userId: this.userId, cname: this.newCol, count: 0}).then(res => {
          if (res.data.code === 200) {
            this.success("新建分栏成功");
            this.loadColumn();
          } else {
            this.error(res.data.msg)
          }
          this.newCol = '';
          this.isVisible = false;
        })
      },
      //删除分栏的函数
      delCol(columnId) {
        this.$confirm('此操作将永久删除分栏, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$axios.get("/column/delete/" + columnId).then((res) => {
            if (res.data.code === 200) {
              this.success("删除成功")
              this.loadColumn();
            } else {
              this.error(res.data.msg)
            }
          })
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });
        });
      },
      isShow() {
        if (this.checkList.length != 0) {
          this.$confirm('此操作将永久删除分栏, 是否继续?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            this.$axios.post("/column/delcolumnbyid", this.checkList).then((res) => {
              if (res.data.code === 200) {
                this.success("删除成功");
                this.$axios("/column/getcolumnbyuserid/" + _this.userId).then((res) => {
                  _this.columns = res.data.data;
                });
                // this.$router.go(0)
              } else {
                this.error(res.data.msg)
              }
            })
          }).catch(() => {
            this.$message({
              type: 'info',
              message: '已取消删除'
            });
          });
        }
        this.isShowDel = !this.isShowDel;
      },
      success(msg) {
        this.$message({
          message: msg,
          type: 'success'
        });
      },
      error(msg) {
        this.$message({
          message: msg,
          type: 'error'
        });
      },
      loadColumn() {
        this.$axios("/column/getcolumnbyuserid/" + this.userId).then((res) => {
          this.columns = res.data.data;
        });
      },
      loadMajors(){
        const { config } = this;
        this.$axios.get("/major/top").then(res => {
          let majors = res.data.data;
          for (let i in majors){
            let m = {name: majors[i].label, value: majors[i].popular};
            this.config.data.push(m);
            this.config= {...this.config}
          }
        })
      },
      handleClick(tab, event) {
        if (tab.index == 0){
          // this.loadSuggestion(1);
        }else if (tab.index == 1){
          this.rank = true;
        }
      },
      loadSuggestion(page){
        this.$axios.get("/major/name/"+this.$store.getters.getUser.major+'?page='+page).then(res => {
          this.suggest.suggestions = res.data.data.blogList;
          this.suggest.total = res.data.data.total;
        })
      },
    },
    created() {
      const _this = this;
      this.loadMajors();
      this.userId = this.$store.getters.getUser.id;
      this.isShowList = this.$route.path === '/home';
      console.log(this.$route.path);
      this.loadColumn();
      this.loadSuggestion(1);
    },
    watch: {
      '$route'(path) {
        this.isShowList = path.path === '/home';
      }
    }
  }
</script>

<style scoped>
  .main {
    /*padding-top: 5%;*/
  }

  .range-flow{
    margin-left:50px;
    width: 500px;
    height: 400px;
    color: #282c34;
  }
  .el-carousel__item h3 {
    color: #475669;
    font-size: 14px;
    opacity: 0.75;
    line-height: 150px;
    margin: 0;
  }
  .el-divider--horizontal{
    margin-top: 24px;
    margin-bottom: 12px;
    height: 1px;
    display: block;
    width: 100%;
  }
  .el-carousel__item h3 {
    color: #475669;
    font-size: 14px;
    opacity: 0.75;
    line-height: 200px;
    margin: 0;
  }
  .el-carousel__item:nth-child(2n) {
    background-color: #99a9bf;
  }

  .el-carousel__item:nth-child(2n+1) {
    background-color: #d3dce6;
  }

  .carousel {
    width: 86%;
    margin: 0 auto;
    text-align: center;
  }

  .aside {
    margin: 0 auto;
    padding-top: 10px;
  }

  .text {
    font-size: 14px;
  }

  .item {
    margin-bottom: 18px;
  }

  .clearfix:before,
  .clearfix:after {
    display: table;
    content: "";
  }
  .clearfix:after {
    clear: both
  }
  .line {
    width: 100%;
    height: 1px;
    border-top: solid #345aa4 2px;
  }
</style>