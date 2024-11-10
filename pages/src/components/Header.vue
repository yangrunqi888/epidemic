<style lang="less" scoped>
.header {
    background-color: #191A23;
    height: 60px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 0 20px;

    .text {
        color: white;
        font-size: 14px;
        margin-left: 10px;
    }

    ;


    .right {
        padding-top: 20px;

        .user {
            width: 40px;
            height: 40px;
            border-radius: 50%;
            vertical-align: middle;
        }
    }

    .left {
        display: flex;
        align-items: center;

        /deep/.el-breadcrumb__item {
            .el-breadcrumb__inner {
                font-weight: normal;

                &.is-link {
                    color: #666
                }
            }

            &:last-child {
                .el-breadcrumb__inner {
                    color: #fff
                }
            }
        }
    }
}
</style>
<template>
    <div class="header">
        <el-dialog title="修改密码" :visible.sync="passwordDialogVisable" width="50%" style="min-width: 400px;">
            <div style="display: flex; flex-direction: column; align-items: center;">
                <ChangePassword @submit-password="handlePasswordSubmitted"></ChangePassword>
            </div>
        </el-dialog>
        <el-dialog title="修改头像" :visible.sync="prohibitDialogVisable" width="50%" style="min-width: 400px;">
            <div style="display: flex; flex-direction: column; align-items: center;">
                <EditProhibit @submit-prohibit="handleProhibitSubmitted"></EditProhibit>
            </div>
        </el-dialog>
        <div class="left">
            <el-button style="margin-right:20px" icon="el-icon-menu" size="mini" @click="handleMenu()"></el-button>
            <!-- 面包屑 -->
            <!-- <el-breadcrumb separator="/">
                    <el-breadcrumb-item v-for="item in tags" :key="item.path" :to="{ path: item.path }">{{ item.label }}
                    </el-breadcrumb-item>
                </el-breadcrumb> -->
        </div>
        <div class="right">
            <el-dropdown>
                <span class="el-dropdown-link">
                    <div v-loading="this.$store.state.user.imageLoading" element-loading-background="#454545">
                        <img class="user" :src='imagUrl' />
                    </div><i class="el-icon-arrow-down el-icon--right"></i>
                </span>
                <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item @click.native="ChangePassword">修改密码</el-dropdown-item>
                    <el-dropdown-item @click.native="ChangeProhibit">修改头像</el-dropdown-item>
                    <el-dropdown-item @click.native="exit">退出</el-dropdown-item>
                </el-dropdown-menu>
            </el-dropdown>
        </div>

    </div>
</template>


<script>
import Cookie from 'js-cookie';
import { mapState } from 'vuex';
import { getProhibit } from '@/api/staff'
import { getGuardProhibit } from '@/api/guard'
import { getResisdentProhibit } from '@/api/resident'
import ChangePassword from '@/components/changePassword.vue';
import EditProhibit from '@/components/editProhibit.vue';
export default {
    data() {
        return {
            passwordDialogVisable: false,
            prohibitDialogVisable: false,

        }
    },
    components: {
        ChangePassword, EditProhibit
    },
    mounted() {
        this.passwordDialogVisable = false;
    },
    methods: {
        handleMenu() {
            this.$store.commit('collapseMenu')
        },
        exit() {
            this.$store.commit('updateImageURL', "");
            this.$store.commit('resetMenu')
            Cookie.remove('userInformation')
            Cookie.remove('type')
            Cookie.remove('token')
            Cookie.remove('menu')
            this.$store.commit('disconnectSocket');
            this.$message.closeAll(); // 关闭所有弹窗
            this.$router.push({ name: 'login' })

        },
        ChangePassword() {
            this.passwordDialogVisable = true;
        },
        handlePasswordSubmitted(isSubmitted) {
            if (isSubmitted === true)
                this.passwordDialogVisable = false;
        },
        ChangeProhibit() {
            this.prohibitDialogVisable = true;
        },
        handleProhibitSubmitted(isSubmitted) {
            if (isSubmitted === true) {
                this.prohibitDialogVisable = false;
                this.getData()
            }
        },
        getData() {
            const information = JSON.parse(Cookie.get("userInformation"))
            this.$store.commit('setImageLoading', true);
            if (information.hasOwnProperty('employeeNumber')) {
                getProhibit(information.employeeNumber).then((res) => {

                    const url = window.URL.createObjectURL(new Blob([res.data],{type:"application/zip"}))


                    this.$store.commit('updateImageURL', url);
                }).catch((res => {
                    console.log("fail", res);
                    this.$store.commit('setImageLoading', false);
                }))
            }
            else if(information.hasOwnProperty('guardNumber')){
                getGuardProhibit(information.guardNumber).then((res) => {
                    this.$store.commit('updateImageURL', window.URL.createObjectURL(res.data));
                }).catch((res => {
                    this.$store.commit('setImageLoading', false);
                }))
            }
            else{
                getResisdentProhibit(information.idNumber).then((res) => {
                    this.$store.commit('updateImageURL', window.URL.createObjectURL(res.data));
                }).catch((res => {
                    this.$store.commit('setImageLoading', false);
                }))
            }

        }
    },
    computed: {
        ...mapState({
            tags: state => state.tab.tabList
        }),
        imagUrl() {
            return this.$store.state.user.imgUrl;
        }
    },
    mounted() {
        this.getData()
    }

}

</script>