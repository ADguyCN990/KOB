<template>
  <h1>我的最爱：{{ name }}</h1>
  <router-view/>
</template>

<script>
import $ from 'jquery';
import { ref } from 'vue';


export default {
  setup: () => {
    let name = ref("");

    $.ajax({
      url: "http://127.0.0.1:3000/pk/getbotinfo/",
      type: "get",
      success: resp => {
        name.value = resp;
      }
    })

    return {
      name
    }
  }
}
</script>

<style scoped>
</style>


