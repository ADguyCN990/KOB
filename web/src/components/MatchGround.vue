<template>
    <div class="matchGround">
        <div class="row">
            <div class="col-4">
                <div class="user-photo">
                    <img :src="$store.state.user.photo" alt="">
                </div>
                <div class="user-username">
                    {{ $store.state.user.username }}
                </div>
            </div>

            <div class="col-4">
                <div class="user-select-bot">
                    <select class="form-select" aria-label="Default select example" v-model="select_bot">
                        <option value="-1" selected>亲自出马</option>
                        <option v-for="bot in bots" :key="bot.id" :value="bot.id">{{ bot.title }}</option>
                    </select>
                </div>
            </div>
            

            <div class="col-4">

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
import { useStore } from 'vuex';
import $ from 'jquery'


export default {
    setup() {
        const store = useStore();
        let match_btn_info = ref("开始匹配");
        let select_bot = ref("-1");
        let bots = ref([]);
        store.state.pk.btninfo = "开始匹配";
        const click_match_btn = () => {
            if (store.state.pk.btninfo === "开始匹配") {
                store.state.pk.btninfo = "取消匹配";
                store.state.pk.socket.send(JSON.stringify({
                    event: "start-matching",
                    bot_id: select_bot.value,
                }));
            }
            else if (store.state.pk.btninfo === "取消匹配"){
                store.state.pk.btninfo = "开始匹配";
                store.state.pk.socket.send(JSON.stringify({
                    event: "stop-matching",
                }));
            }
        };
        const jwt_token = localStorage.getItem("jwt_token");
        
        
        const refresh_bots = () => {
            $.ajax({
                url: "https://app2938.acapp.acwing.com.cn/api/user/bot/getlist/",
                type: "get",
                headers: {
                    Authorization: "Bearer " + jwt_token,
                },
                success(resp) {
                    bots.value = resp;
                },

            })
        }
        refresh_bots();
        return {
            match_btn_info,
            click_match_btn,
            bots,
            select_bot,
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
div.user-select-bot {
    padding-top: 20vh;
}

div.user-select-bot>select {
    width: 60%;
    margin: 0 auto;
}
</style>