import Vue from 'vue'
import VueRouter from 'vue-router'
import Main from '../views/main.vue'
import Home from '../views/home.vue'
import Resident from '../views/resident.vue'
import Building from '../views/building.vue'
import Quarantine from '../views/quarantine.vue'
import NucleicAcid from "../views/nucleicAcid.vue"
import Antigen from "../views/antigen.vue"
import Temperature from "../views/temperature.vue"
import BackHome from "../views/backHome.vue"
import GoOut from "../views/goOut.vue"
import Visit from "../views/visit.vue"
import Complaint from "../views/complaint.vue"
import Login from "../views/login.vue"
import changePassword from"@/components/changePassword.vue"
//1. 创建路由组件
Vue.use(VueRouter)

//2. 映射组件
const routes=[
    {
        path:'/',
        component:Main,
        name:'Main',
        redirect:'/home',//重定向
        children:[
            // {path:'/home',component:Home,name:'home'},
            //  {path:'/resident',component:Resident,name:'resident'},
            //  {path:'/building',component:Building,name:'building'},
            //  {path:'/quarantine',component:Quarantine,name:'quarantine'},
            //  {path:'/nucleicAcid',component:NucleicAcid,name:'nucleicAcid'},
            //  {path:'/antigen',component:Antigen,name:'antigen'},
            //  {path:'/temperature',component:Temperature,name:'temperature'},
            //  {path:'/backHome',component:BackHome,name:'backHome'},
            //  {path:'/goOut',component:GoOut,name:'goOut'},
            //  {path:'/visit',component:Visit,name:'visit'},
            //  {path:'/complaint',component:Complaint,name:'complaint'},
        ]
    },
    {
        path:'/login',
        name:'login',
        component:Login
    }
]

//3. 创建router实例
const router=new VueRouter({
    routes
})

export default router