<template>
  <NavBar></NavBar>
  <RouterView></RouterView>
</template>

<script>
//import $ from 'jquery';
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
    // $.ajax({
    //   url: "http://127.0.0.1:3000/user/account/token/",
    //   type: "post",
    //   data: {
    //     username: "admin",
    //     password: "jinhui123",
    //   },
    //   success(resp) {
    //     console.log(resp);
    //   },
    //   error(resp) {
    //     console.log(resp);
    //   },
    // })

    // $.ajax({
    //   url: "http://127.0.0.1:3000/user/account/info/",
    //   type: "get",
    //   headers: {
    //     Authorization: "Bearer " + "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI2MGNlNTE0ODc3NGY0MDBkYTUwOTI2MDMzMGI0MWE4ZCIsInN1YiI6IjEiLCJpc3MiOiJzZyIsImlhdCI6MTY3MTY5MjQ4MiwiZXhwIjoxNjcyOTAyMDgyfQ.baOycXVb3iumuQt86np3uWWw0_WpfEtOLU7WNRA_DZA"
    //   },
    //   success(resp) {
    //     console.log(resp);
    //   },
    //   error(resp) {
    //     console.log(resp);
    //   }
    // })

    // $.ajax({
    //   url: "http://127.0.0.1:3000/user/account/register/",
    //   type: "post",
    //   data: {
    //     username: "脑残",
    //     password: "asdkjf;lasjdkf;asldfj",
    //     confirmedPassword: "asdkjf;lasjdkf;asldfj",
    //   },
    //   success(resp) {
    //     console.log(resp);
    //   },
    //   error(resp) {
    //     console.log(resp);
    //   }
    // })

    //const store = useStore();
    //console.log(store.state.user.token);
    // const jwt_token = localStorage.getItem("jwt_token");
    // $.ajax({
    //   url: "http://127.0.0.1:3000/user/bot/add/",
    //   type: "post",
    //   data: {
    //     title: "bot的名称",
    //     description: "bot的描述",
    //     content: "bot的内容",
    //   },
    //   headers: {
    //     Authorization: "Bearer " + jwt_token,
    //   },
    //   success(resp) {
    //     console.log(resp);
    //   },
    //   error(resp) {
    //     console.log(resp);
    //   }

    // })

    // const jwt_token = localStorage.getItem("jwt_token");
    // console.log(jwt_token);
    // $.ajax({
    //   url: "http://127.0.0.1:3000/user/bot/remove/",
    //   type: "post",
    //   data: {
    //     bot_id: 23,
    //   },
    //   headers: {
    //     Authorization: "Bearer " + jwt_token,
    //   },
    //   success(resp) {
    //     console.log(resp);
    //   },
    //   error(resp) {
    //     console.log(resp);
    //   }

    // })

    // const jwt_token = localStorage.getItem("jwt_token");
    // $.ajax({
    //   url: "http://127.0.0.1:3000/user/bot/update/",
    //   type: "post",
    //   data: {
    //     bot_id: 100,
    //     title: "不告诉你",
    //     description: "不告诉你",
    //     content: "不告诉你",
    //   },
    //   headers: {
    //     Authorization: "Bearer " + jwt_token,
    //   },
    //   success(resp) {
    //     console.log(resp);
    //   },
    //   error(resp) {
    //     console.log(resp);
    //   }

    // })

    // const jwt_token = localStorage.getItem("jwt_token");
    // $.ajax({
    //   url: "http://127.0.0.1:3000/user/bot/getlist/",
    //   type: "get",
    //   headers: {
    //     Authorization: "Bearer " + jwt_token,
    //   },
    //   success(resp) {
    //     console.log(resp);
    //   },
    //   error(resp) {
    //     console.log(resp);
    //   }

    // })


  }
}
</script>

<style>
body {
  background-image: url("https://adguycn990-typoraimage.oss-cn-hangzhou.aliyuncs.com/202211231912040.webp");
  background-size: cover;
}

</style>




