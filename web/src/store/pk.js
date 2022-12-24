import ModuleUser from './user'


export default {
    state: {
        socket: null,
        opponent_username: "",
        opponent_photo: "",
        status: "matching", //两种，matching和playing
        btninfo: "开始匹配",
        gamemap: null
    },
    mutations: {
        updateSocket(state, socket) {
            state.socket = socket;
        },
        updateOpponent(state, opponent) {
            state.opponent_username = opponent.username;
            state.opponent_photo = opponent.photo;
        },
        updateStatus(state, status) {
            state.status = status;
        },
        updateGamemap(state, gamemap) {
            state.gamemap = gamemap;
        },
        updateBtnInfo(state, btninfo) {
            state.btninfo = btninfo;
        }

    },
    actions: {
    },
    modules: {
        user: ModuleUser,

    }
}
