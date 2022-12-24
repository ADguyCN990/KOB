<template>
    <div>
        <PlayGround v-if="$store.state.pk.status === 'playing'">
            
        </PlayGround>

        <MatchGround v-if="$store.state.pk.status === 'matching'">
            
        </MatchGround>
    </div>
</template>

<script>
import PlayGround from '../../components/PlayGround.vue'
import MatchGround from '../../components/MatchGround.vue'
import { onMounted } from 'vue';
import { onUnmounted } from 'vue';
import { useStore } from 'vuex'
export default {
    components: {
        PlayGround,
        MatchGround,
    },
    setup() {
        const store = useStore();
        //const jwt_token = localStorage.getItem("jwt_token");
        const socket_url = `ws://127.0.0.1:3000/websocket/${store.state.user.token}`;
        let socket = null;
        onMounted(() => {

            store.commit("updateOpponent", {
                username: "我的对手",
                photo: "https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png",

            })

            socket = new WebSocket(socket_url);
            socket.onopen = () => { //连接成功时调用的函数
                console.log("connected!");
                store.commit("updateSocket", socket);
            }

            socket.onmessage = msg => { //前端接收到信息时调用的函数
                const data = JSON.parse(msg.data); //将JAVA传来的JSON字符串转化为JSON对象
                if (data.event === "start-matching") {
                    store.commit("updateOpponent", {
                        username: data.opponent_username,
                        photo: data.opponent_photo,
    
                    });
                    store.state.pk.btninfo = "匹配成功",
                    setTimeout(() => {
                        store.commit("updateStatus", "playing");
                    }, 3000) //匹配成功，进入对战界面
                }
                store.commit("updateGamemap", data.gamemap);
                console.log(data);
            }

            socket.onclose = () => { //关闭时调用的函数
                console.log("disconnected!");
            }
        });

        onUnmounted(() => { //当当前页面关闭时调用
            store.commit("updateStatus", "matching");
            socket.close(); //卸载的时候断开连接
        });
    }
}
</script>

<style scoped>

</style>