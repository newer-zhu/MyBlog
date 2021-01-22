<template>
    <div>
        <el-header>
            <Navibar/>
        </el-header>
        <el-main>

        </el-main>
    </div>
</template>

<script>
    import Navibar from "../components/common/Navibar"

    export default {
        name: "ListDetail",
        components: {Navibar},
        data(){
            return {

            }
        },

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