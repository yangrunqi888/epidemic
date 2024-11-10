// store/websocket.js
const state = {
    socket: null,
};

const mutations = {
    setSocket(state, socket) {
        state.socket = socket;
        console.log("socket on:",state.socket)
    },
    disconnectSocket(state) {
        if (state.socket) {
            state.socket.close();
            state.socket = null; // 清空 WebSocket 实例
            console.log("socket close:",state.socket)
        }
    },
};

export default {
    state,
    mutations,
};
