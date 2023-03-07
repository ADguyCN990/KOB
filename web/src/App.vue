<template>
  <NavBar></NavBar>
  <RouterView></RouterView>
</template>

<script>
// import $ from 'jquery';
// import { useStore } from 'vuex';
// import { ref } from 'vue';
import NavBar from './components/NavBar.vue';
import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap/dist/js/bootstrap";



export default {
  components: {
    NavBar
  },
  setup() {

    

  }
}
</script>

<style>
body {
  background-image: url("https://adguycn990-typoraimage.oss-cn-hangzhou.aliyuncs.com/202211231912040.webp");
  background-size: cover;
}

</style>




