<template>
    <div class="tabs">
        <el-tag v-for="(item, index) in tags" :key="item.path"  
        :effect="$route.name===item.name?'dark':'plain'" 
        :closable="item.name !=='home'"
        @click="changeMenu(item)"
        @close="handleClose(item,index)"
        size="small"
        >
            {{ item.label }}
        </el-tag>
    </div>
</template>

<script>
import { mapState } from 'vuex';
  export default {
    name:'Tags',
    data() {
      return {
      
      }
    },
    computed: {
        ...mapState({
            tags: state => state.tab.tabList
        })
    },
    methods:{
        //跳转
        changeMenu(item){
            if(this.$route.path !== item.path && !(this.$route.path === '/home' && (item.path==='/'))){
            this.$router.push(item.path)
        }
        },
        //删除
        handleClose(item,index){
            const length=this.tags.length-1;
            this.$store.commit('handleClose',item)
            //删除非当前页面，不需要跳转
            if(item.name !== this.$route.name){
                return;
            }
            //删除最后一项，跳转前一项
            if(index === length){
                this.$router.push(this.tags[index-1].path)
            }
            //删除中间项，跳转后一项
            else{
                this.$router.push(this.tags[index].path)
            }
            
        }
    }
  }
</script>

<style lang="less" scoped>
    .tabs{
        padding:20px;
        .el-tag{
            cursor:pointer;
            margin-right:15px;
        }
    }
</style>