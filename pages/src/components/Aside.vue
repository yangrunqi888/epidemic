<template>
    <div class="aside">
      <div class="aside-header">
        <img
          src='../assets/images/logo.jpg'
          style="width: 30px; height: 30px"
        />
        <span
        v-show="!isCollapse"
          style="
            font-size: 20px;
            font-weight: 600;
            color: white;
            margin-left: 10px;
          "
          >羊尖社区疫情管理</span
        >
      </div>
      <div class="aside-menu">
        <perfect-scrollbar ref="scrollbar">
          <el-menu
            default-active="2"
            class="el-menu-vertical-demo"
            background-color="#191A23"
            text-color="#ffffff" active-text-color="#008c5b"
            :collapse="isCollapse"
          >
           <!-- 一级菜单 -->
           <el-menu-item @click="clickMenu(item)" v-for="item in noChild" :key="item.name" :index="item.path">
                <i :class="`el-icon-${item.icon}`"></i>
                <span slot="title">{{ item.label }}</span>
            </el-menu-item>
            <!-- 二级菜单 -->
            <el-submenu v-for="item in hasChild" :key="item.label" :index="item.label">
                <template slot="title">
                    <i :class="`el-icon-${item.icon}`"></i>
                    <span>{{ item.label }}</span>
                </template>
                <el-menu-item-group class="el-sub">
                    <!-- <template slot="title">分组一</template> -->
                    <el-menu-item @click="clickMenu(item2)" v-for="item2 in item.children" :key="item2.name" :index="item2.path">
                        <i :class="`el-icon-${item2.icon}`"></i>
                        {{ item2.label }}
                    </el-menu-item>
                </el-menu-item-group>
            </el-submenu></el-menu>
        </perfect-scrollbar>
      </div>
    </div>
  </template>
  <script>
  export default {
    data() {
      return {};
    },
    methods: {
      clickMenu(item){
        if(this.$route.path !== item.path && !(this.$route.path === '/home' && (item.path==='/'))){
            this.$router.push(item.path)
        }
        this.$store.commit('selectMenu',item)
      },
    },
    watch: {
      //路由改变时，滚动条回到顶部
      $route() {
        this.$refs.scrollbar.$el.scrollTop = 0;
      },
    },
    computed: {
        noChild() {
            return this.menuData.filter(item => !item.children)
        },
        hasChild() {
            return this.menuData.filter(item => item.children)
        },
        isCollapse(){
            return this.$store.state.tab.isCollapse;
        },
        menuData(){
            return JSON.parse(localStorage.getItem('menu'))
        }
    },
  };
  </script>
   
   
  <style lang='less' scoped>
  .aside {
    height: 100vh;
    .aside-header {
      height: 60px;
      background-color: #191A23;
      line-height: 60px;
      display: flex;
      align-items: center;
      justify-content: center;
    }
    .aside-menu {
      height: calc(100vh - 60px);
      //展开时宽度
      .el-menu-vertical-demo:not(.el-menu--collapse) {
        width: 256px;
      }
      .el-menu {
        height: 100%;
        border: 0 !important; //垂直时，去除右侧白边
      }
      //perfect-scrollbar默认的类名。自定义滚动条内容区域高度
      .ps {
        height: 100%;
      }
    }
  }
  </style>