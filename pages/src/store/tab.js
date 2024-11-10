import Cookies from "js-cookie";

export default{
    state:{
        isCollapse:false,//菜单收起或展开
        tabList:[
            {
                path: "/",
                name: "home",
                label: "首页",
                icon: "s-home",
                url: "Home/Home"
            },
        ],//面包屑数据
        menu:[],
    },
    mutations:{
        //折叠菜单
        collapseMenu(state){
            state.isCollapse=!state.isCollapse;
        },
        //更新面包屑数据
        selectMenu(state,val){
            if(val.name!=='home'){
                const index=state.tabList.findIndex(item => item.name === val.name);
                if(index === -1){
                    state.tabList.push(val)
                }
            }
        },
        resetMenu(state){
            state.tabList=[
                {
                    path: "/",
                    name: "home",
                    label: "首页",
                    icon: "s-home",
                    url: "Home/Home"
                }]
        },
        //删除tab数据
        handleClose(state,item){
           const index = state.tabList.findIndex(val => val.name === item.name)
           state.tabList.splice(index,1)
        },
        setMenu(state,val){
            state.menu=val;
            localStorage.setItem("menu",JSON.stringify(val))
        },//设置menu数据
        addMenu(state,router){
            if(!localStorage.getItem('menu')) return
            const menu=JSON.parse(localStorage.getItem('menu'))
            state.menu=menu;
            //组装动态路由的数据
            const menuArray=[];
            menu.forEach(item => {
                if(item.children){
                    item.children=item.children.map(item => {
                        item.component= () => import(`../views/${item.url}`)
                        return item
                    })
                    menuArray.push(...item.children)
                }else{
                    item.component= () => import(`../views/${item.url}`)
                    menuArray.push(item);
                }
            });
          
            menuArray.forEach(item =>{
                router.addRoute('Main',item);
            })
        }//动态注册路由
    }
}