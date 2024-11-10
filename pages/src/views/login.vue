<template>
    <div class="container" v-loading="loading" element-loading-text="登录中..." element-loading-spinner="el-icon-loading"  element-loading-background="rgba(0, 0, 0, 0.6)" >
        <div 
           class="login-container">
            <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form" auto-complete="on"
                label-position="left" label-width="auto">

                <div class="title-container">
                    <img class="logo" src='../assets/images/logo.jpg' />
                    <h3 class="title">羊尖社区疫情管理系统</h3>
                </div>

                <el-form-item prop="idNumber" label="用户名">

                    <el-input ref="idNumber" v-model="loginForm.idNumber" placeholder="Uername" name="idNumber"
                        type="text" tabindex="1" auto-complete="on" prefix-icon="el-icon-user" />
                </el-form-item>

                <el-form-item prop="password" label="密码">
                    <el-input v-model="loginForm.password" type="password" placeholder="Password" name="password"
                        auto-complete="on" @keyup.enter.native="handleLogin" prefix-icon="el-icon-lock" show-password />

                </el-form-item>
                <el-form-item label="角色" required>
                    <el-select v-model="role" placeholder="请选择登录角色">
                        <el-option v-for="item in roleOptions" :key="item.id" :label="item.label" :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <div class="login-button">
                    <el-button type="primary" @click.native.prevent="handleLogin">登录</el-button>
                </div>
            </el-form>
        </div>
    </div>
</template>
  
<script>
//引入api
import Cookie from 'js-cookie'
import { staffLogin,guardLogin,residentLogin } from "../api/login";
export default {
    name: "Login",
    data() {
        return {
            loginForm: {
                idNumber: "",
                password: "",
                socket: null,
                messageVisible:false,
            },
            // 登录规则
            loginRules: {
                idNumber: [
                    { required: true, trigger: "blur", message: "用户名不能为空" },
                ],
                password: [
                    { required: true, trigger: "blur", message: "密码不能为空" },
                    {
                        min: 6,
                        max: 13,
                        message: "密码长度在 6 到 13 个字符",
                        trigger: "blur",
                    },
                ]
            },
            loading: false,
            redirect: undefined,
            roleOptions: [{ "id": "0", "label": "居民" }, { "id": "1", "label": "保安" }, { "id": "2", "label": "管理员" },],
            role: "0",
        };
    },
    methods: {
        // 登录业务
        handleLogin() {
            this.$refs.loginForm.validate((valid) => {
                // 如果符合验证规则
                if (valid) {
                    //登录操作
                    this.loading=true;
                    if (this.role === "0") {
                        residentLogin(this.loginForm).then((res) => {
                            this.loading=false
                            if(res.data.code==200){
                                 var token=res.data.token;
                                 
                                 Cookie.set('token',token);
                                 Cookie.set('userInformation',JSON.stringify(res.data.data));
                                 Cookie.set('type',"6");
                                 
                                 
                                
                                 this.$store.commit('setMenu',res.data.menus);
                                 this.$store.commit('addMenu',this.$router)
                                 this.loginCorrect(res.data.data.name);
                                 this.$router.push({name:'home'})
                                 // 建立 WebSocket 连接
                                 this.connectWebSocket();
                                 
                            }
                            else{
                                this.loginError()
                            }
                            
                        }).catch((res)=>{
                            this.loading=false
                            this.$message.error('网络繁忙，请稍后重试!');
                        }
                        )
                    }//居民登录
                    else if (this.role === "1") {
                        guardLogin(this.loginForm).then((res) => {
                            this.loading=false
                            if(res.data.code==200){
                                 var token=res.data.token;
                                 
                                 Cookie.set('token',token);
                                 Cookie.set('userInformation',JSON.stringify(res.data.data));
                                 Cookie.set('type',res.data.data.position);
                                 
                                 
                                 this.$store.commit('setMenu',res.data.menus);
                                 this.$store.commit('addMenu',this.$router)
                                 this.loginCorrect(res.data.data.name);
                                 this.$router.push({name:'home'})
                            }
                            else{
                                this.loginError()
                            }
                            
                        }).catch((res)=>{
                            this.loading=false
                            this.$message.error('网络繁忙，请稍后重试!');
                        }
                        )
                    }//保安登录
                    else if (this.role === "2") {
                        staffLogin(this.loginForm).then((res) => {
                            this.loading=false
                            if(res.data.code==200){
                                 var token=res.data.token;
                                 
                                 Cookie.set('token',token);
                                 Cookie.set('userInformation',JSON.stringify(res.data.data));
                                 Cookie.set('type',res.data.data.position);
                                 
                                 
                                 this.$store.commit('setMenu',res.data.menus);
                                 this.$store.commit('addMenu',this.$router)
                                 this.loginCorrect(res.data.data.name);
                                 this.$router.push({name:'home'})
                            }
                            else{
                                this.loginError()
                            }
                            
                        }).catch((res)=>{
                            this.loading=false
                            this.$message.error('网络繁忙，请稍后重试!');
                        }
                        )
                    }//管理员（网格员）登录
                }
            });
        },
        loginCorrect(name){
            this.$message({
          showClose: true,
          message: '欢迎您，'+name+"!",
          type: 'success'
        });
    },
        loginError() {
        this.$message({
          showClose: true,
          message: '用户名或密码错误！',
          type: 'error'
        });
      },
      connectWebSocket() {
            this.socket = new WebSocket(`ws://localhost:8848/ws/messages`);
            this.socket.onopen = () => {
                console.log('已连接到 WebSocket 服务器.');
            };
            // this.socket.onclose = () => {
            //     console.log('从 WebSocket 服务器断开连接.');
            // };
            this.socket.onerror = (error) => {
                console.error('发生错误:', error);
            };
            this.socket.onmessage = (event) => {
                this.showMessage(event.data); // 调用 showMessage
            };
            this.$store.commit('setSocket', this.socket);
                
            },

            showMessage(message) {
    if (!this.messageVisible) {
        this.messageVisible = true; // 设置为可见
        this.$message({
            showClose: true,
            message: message,
            type: 'success',
            duration: 0, // 永久显示
            showClose: false, // 不显示关闭按钮
            onClose: () => {
                this.messageVisible = false; // 关闭时重置标志
            }
        });
    }
}

    },
    // beforeDestroy() {
    //     this.$store.commit('disconnectSocket');
    // },
};
</script>
<style lang="less" scoped>
.container {
    position: fixed;
    height: 100%;
    width: 100%;
    background-size: 100%;
    background-image: url("../assets/images/loginBackground.jpg");

    .login-container {
        width: 500px;
        margin: 70px 70px auto auto;
        border: 1px solid #eaeaea;
        border-radius: 15px;
        padding: 35px;
        box-shadow: 0 0 100px #a7e5f8;
        background-color: #ffffff;
        opacity: 0.9;

        .login-form {
            height: auto;

            .title-container {
                position: relative;
                vertical-align: middle;
                text-align: center;

                .logo {
                    line-height: 100px;
                    width: 100px;
                    border-radius: 50%;
                }

                .title {

                    font-size: 30px;
                    color: rgb(0, 0, 0);
                    margin: 0px auto 40px auto;
                    text-align: center;
                    font-weight: bold;
                }

            }

            .login-button {
                position: relative;
                vertical-align: middle;
                text-align: center;
            }
        }
    }
}
</style>