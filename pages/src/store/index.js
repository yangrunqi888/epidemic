import Vue from 'vue'
import Vuex from 'vuex'
import tab from './tab.js'
import user from './user.js'
import websocket from './websocket.js';
Vue.use(Vuex)

//创建vuex实例
export default new Vuex.Store({
    modules:{
        tab,user, websocket,
    }
})