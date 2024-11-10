import Vue from 'vue'
import App from './App.vue'

import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';

import router from'./router'

import store from './store'

import "./api/mock"
import Cookie from 'js-cookie'

import PerfectScrollbar from 'vue2-perfect-scrollbar'
import 'vue2-perfect-scrollbar/dist/vue2-perfect-scrollbar.css'
Vue.use(PerfectScrollbar)

Vue.config.productionTip = false
Vue.use(ElementUI);

router.beforeEach((to,from,next)=>{
  //判断token是否存在
  const token=Cookie.get("token")
  if(!token && to.name!=='login'){
    //token不存在，用户未登录，跳转登录界面
    next({name:'login'})
  }
  else if(token && to.name === 'login'){
    next({name:'home'})
  }
  else{
    next()
  }
})


new Vue({
  router,
  store,
  render: h => h(App),
  created(){
    store.commit('addMenu',router);
  }
}).$mount('#app')
