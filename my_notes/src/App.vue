<template>
  <div id="app">
    <router-view/>
  </div>
</template>

<script>
  export default {
    name: 'app',
    data(){
      return{
        isAlive: true
      }
    },
    mounted(){
      // this.localSocket();
    },
    methods:{

    }
  }
</script>

<style>
  @import "assets/css/base.css";
</style>
