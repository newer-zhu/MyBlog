<template>
    <div>
        <div class="grid-content bg-purple">
            <el-timeline>
                <el-timeline-item v-for="(card,index) in this.blogs" :timestamp="card.createdAt" placement="top">
                    <router-link :to="{name: 'BlogDetail', params:{blogId:card.id}}">
                        <el-card shadow="hover">
                            <h3>{{card.title}}</h3>
                            <p>{{card.summary}}</p>
                        </el-card>
                    </router-link>
                </el-timeline-item>
            </el-timeline>
        </div>
<!--        <el-pagination-->
<!--                background-->
<!--                layout="prev, pager, next"-->
<!--                :total="10">-->
<!--        </el-pagination>-->
    </div>
</template>

<script>
    export default {
        name: "ListDetail",
        data(){
            return {
                userId: 0,
                blogs: [],
            }
        },
        watch: {
            '$route'(to, from){
                this.$axios.get("/blog/getpagesbycolumnid/"+ to.params.columnId).then(res => {
                    this.blogs = res.data.data;
                });
            }
        },
        created() {
            const _this = this;
            let columnId = this.$route.params.columnId;
            this.userId = this.$store.getters.getUser.id;
            this.$axios.get("/blog/getpagesbycolumnid/"+ columnId).then(res => {
                _this.blogs = res.data.data;
            });
        }

    }
</script>

<style scoped>
    .text {
        font-size: 14px;
    }

    .item {
        padding: 18px 0;
    }

    .box-card {
        width: 480px;
        margin: 0 auto;
    }
</style>