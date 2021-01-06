<template>
    <div>
        <div class="footer">
            <p class="words">{{claim}}</p>
            <br>
            <a class="words" :href="link">有事联系我 tel：17681295923</a>
        </div>
    </div>
</template>

<script>
    export default {
        name: "Footer",
        data(){
            return {
                claim: "Copyright@ZhuHodor",
                link: '#'
            }
        }
    }
</script>

<style scoped>
    .footer {
        height: 75px;
        width: 100%;
        background-color: rgba(18, 10, 27, 0.36);
        text-align: center;
        padding: 25px;
        position: fixed;
        bottom: 0;
    }
    .words{
        text-align: center;
        vertical-align: middle;
    }
</style>