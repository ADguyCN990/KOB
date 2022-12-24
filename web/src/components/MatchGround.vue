<template>
    <div class="matchGround">
        <div class="row">
            <div class="col-6">
                <div class="user-photo">
                    <img :src="$store.state.user.photo" alt="">
                </div>
                <div class="user-username">
                    {{ $store.state.user.username }}
                </div>
            </div>

            <div class="col-6">

                <div class="user-photo">
                    <img :src="$store.state.pk.opponent_photo" alt="">
                </div>
                <div class="user-username">
                    {{ $store.state.pk.opponent_username }}
                </div>
            </div>

            <div class="col-12" style="padding-top: 15vh;text-align: center">
                <button @click="click_match_btn" type="button" class="btn btn-success btn-lg"> {{ $store.state.pk.btninfo }} </button>
            </div>
        </div>
    </div>
</template>

<script>
import { ref } from 'vue';
import store from '../store';

export default {
    setup() {
        let match_btn_info = ref("开始匹配");
        store.state.pk.btninfo = "开始匹配";
        const click_match_btn = () => {
            if (store.state.pk.btninfo === "开始匹配") {
                store.state.pk.btninfo = "取消匹配";
                store.state.pk.socket.send(JSON.stringify({
                    event: "start-matching",
                }));
            }
            else if (store.state.pk.btninfo === "取消匹配"){
                store.state.pk.btninfo = "开始匹配";
                store.state.pk.socket.send(JSON.stringify({
                    event: "stop-matching",
                }));
            }
        };
        return {
            match_btn_info,
            click_match_btn,
        }
    },

    
}

</script>


<style scoped>
div.matchGround {
    width: 60vw;
    height: 70vh;
    margin: 40px auto;
    background-color: rgba(50, 50, 50, 0.5);
}

div.user-photo {
    text-align: center;
    padding-top: 10vh;
    
}

div.user-photo > img {
    border-radius: 50%;
    width: 20vh;
    height: 20vh
}
div.user-username {
    text-align: center;
    font-size: 24px;
    font-weight: 600;
    color: white;
    padding-top: 2vh;
}


</style>