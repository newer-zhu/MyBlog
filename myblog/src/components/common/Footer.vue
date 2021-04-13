<template>
    <div>
        <div class="footer">
            <p class="words">笔记，且不仅是笔记</p>
            <p style="font-size: 15px;display: inline; position: absolute; right: 10px; bottom: 0px;">copyright@zhuhodor</p>
        </div>
    </div>
</template>

<script>
    export default {
        name: "Footer",
        data(){
            return {
                claim: "Copyright@ZhuHodor",
            }
        }
    }
</script>

<style scoped>
    .footer {
        height: 60px;
        background-color: rgba(1, 136, 254, 0.75);
        margin-top: 150px;
        bottom: 0;
        position: relative;
    }
    .words{
        font-size: 20px;
        color: #fdfdfe;
        text-align: center;
        padding-top: 20px;
    }
</style>