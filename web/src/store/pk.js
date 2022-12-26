import ModuleUser from './user'


export default {
    state: {
        socket: null,
        opponent_username: "",
        opponent_photo: "",
        status: "matching", //两种，matching和playing
        btninfo: "开始匹配",
        gamemap: null,
        loser: "none", //none, A, B, all
        a_id: 0,
        a_sx: 0,
        a_sy: 0,
        b_id: 0,
        b_sx: 0,
        b_sy: 0,
        gameObject: null,
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
        updateGame(state, game) {
            state.gamemap = game.map;
            state.a_id = game.a_id;
            state.a_sx = game.a_sx;
            state.a_sy = game.a_sy;
            state.b_id = game.b_id;
            state.b_sx = game.b_sx;
            state.b_sy = game.b_sy;
        },
        updateBtnInfo(state, btninfo) {
            state.btninfo = btninfo;
        },
        updateGameObject(state, gameObject) {
            state.gameObject = gameObject;
        },
        updateLoser(state, loser) {
            state.loser = loser;
        }

    },
    actions: {
    },
    modules: {
        user: ModuleUser,

    }
}
