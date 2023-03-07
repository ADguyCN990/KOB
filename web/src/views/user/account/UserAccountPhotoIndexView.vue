<template>

    <div class="container">
        <div class="row">
            <div class="col-3">
                <div class="card" style="margin-top: 20px;">
                    <div class="card-body">
                        <img class="img-fluid" :src="$store.state.user.photo" alt="" style="width: 19.2vw; height: 19.2vw;">
                    </div>
                </div>
            </div>
            <div class="col-9">
                <div class="card" style="margin-top: 20px;">
                    <div class="card-header">
                        <span style="font-size: 130%">更改头像</span>                      
                    </div>

                    <div class="card-body">          
                        <span  v-for="photo in photos" :key="photo.id">
                            <span >
                                <img :src="photo.url" alt="" class="img-thumbnail ten" @click="changephoto(photo)">
                            </span>
                        </span>   
                    </div>

                </div>
            </div>
        </div>
    </div>

</template>


<script >
import $ from 'jquery'
import { ref } from 'vue'
import store from '../../../store';

export default {
    setup() {
        const jwt_token = localStorage.getItem("jwt_token");
        let photos = ref([]);
        $.ajax({
            url: "https://app2938.acapp.acwing.com.cn/api/user/account/showphoto/",
            type: "get",
            headers: {
                Authorization: "Bearer " + jwt_token,
            },
            success(resp) {
                photos.value = resp;
            },
        })
        const changephoto = (photo) => {
            $.ajax({
                url: "https://app2938.acapp.acwing.com.cn/api/user/account/getphoto/",
                type: "post",
                headers: {
                    Authorization: "Bearer " + jwt_token,
                },
                data: {
                    url: photo.url,
                },
                success(resp) {
                    if (resp.error_message === "success") {
                        alert("头像更改成功！");
                        store.state.user.photo = photo.url;
                    }
                },
            })
        } 

        return {
            photos,
            changephoto,
        }
    }
}

</script>

<style scoped>
.img-thumbnail {
    height: 19.2vw;
    width: 19.2vw;
    margin: 0.5vw;
}

.ten:hover {
    box-shadow: 2px 2px 10px lightgray;
    transition: 300ms;
}

</style>