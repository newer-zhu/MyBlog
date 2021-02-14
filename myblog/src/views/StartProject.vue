<template>
    <div>
        <el-container>
            <el-header>
                <Navibar/>
            </el-header>

            <el-main>
                <el-row>
                    <el-col :offset="4" :span="16">
                        <el-row>
                            <el-col :span="12">
                                项目名称：
                                <el-input></el-input>
                            </el-col>
                        </el-row>
                        <el-row>
                            <el-col>

                            </el-col>
                        </el-row>
                    </el-col>
                </el-row>
            </el-main>
        </el-container>
    </div>
</template>

<script>
    import {Navibar} from '../components/common/Navibar'
    export default {
        name: "StartProject",
        components: {Navibar}
    }
</script>

<style scoped>

</style>