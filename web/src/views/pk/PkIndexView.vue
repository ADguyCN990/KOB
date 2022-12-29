<template>
    <div>
        <PlayGround v-if="$store.state.pk.status === 'playing'">
            
        </PlayGround>

        <MatchGround v-if="$store.state.pk.status === 'matching'">
            
        </MatchGround>

        <ResultBoard v-if="$store.state.pk.loser !== 'none'">

        </ResultBoard>
    </div>
</template>

<script>
import PlayGround from '../../components/PlayGround.vue'
import MatchGround from '../../components/MatchGround.vue'
import ResultBoard from '../../components/ResultBoard.vue'
import { onMounted } from 'vue';
import { onUnmounted } from 'vue';
import { useStore } from 'vuex'
//import { setTimeout } from 'timers/promises';
export default {
    components: {
        PlayGround,
        MatchGround,
        ResultBoard
    },
    setup() {
        const store = useStore();
        //const jwt_token = localStorage.getItem("jwt_token");
        const socket_url = `ws://127.0.0.1:3000/websocket/${store.state.user.token}`;
        store.commit("updateLoser", "none");
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
                    }, 500); //匹配成功，进入对战界面
                    store.commit("updateGame", data.game);
                    console.log(data.game);
                }
                else if (data.event === "move") { //后端返回蛇的移动方向
                    console.log(data);
                    const game = store.state.pk.gameObject; //取出游戏界面
                    const [snake0, snake1] = game.snakes;
                    snake0.set_direction(data.a_direction); 
                    snake1.set_direction(data.b_direction);
                }
                else if (data.event === "result") {
                    const game = store.state.pk.gameObject; //取出游戏界面
                    const [snake0, snake1] = game.snakes;
                    if (data.loser === "all" || data.loser === "A") { //A死
                        snake0.status = "die";
                    }
                    if (data.loser === "all" || data.loser === "B") { //B死
                        snake1.status = "die";
                    }
                    store.commit("updateLoser", data.loser);
                }

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