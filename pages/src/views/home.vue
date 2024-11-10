<template>
    <el-row v-loading="noticeLoading">
        <el-dialog title="发布公告" :visible.sync="newsDiaglogVisable" width="50%" :before-close="handleClose">
            <el-form :model="noticeForm" ref="noticeForm" label-width="100px">
                <el-form-item label="标题" prop="title">
                    <el-input v-model="noticeForm.title" placeholder="请输入标题"></el-input>
                </el-form-item>
                <el-form-item label="内容" prop="text">
                    <div style="border: 1px solid #ccc;">
                        <Toolbar style="border-bottom: 1px solid #ccc" :editor="editor" />
                        <Editor style="height: 500px; overflow-y: hidden;" v-model="noticeForm.text"
                            @onCreated="onCreated" />
                    </div>
                </el-form-item>
            </el-form>
            <span slot="footer">
                <el-button type="primary" @click="announceNotice()">发布</el-button>
            </span>
        </el-dialog>
        <el-dialog title="公告详情" :visible.sync="noticeDetailVisable" width="50%">
            <el-form>
                <el-form-item>
                    <h3 style="text-align: center;">{{ notice.title }}</h3>
                </el-form-item>
                <el-form-item>
                    <div style="text-align: center;">
                    <span>{{ notice.announceTime }}</span>&nbsp;&nbsp;<span>{{ notice.employeeName }}</span>
                </div>
                </el-form-item>

                <el-form-item>
                    <div v-html="notice.text"></div>
                </el-form-item>
            </el-form>
        </el-dialog>
        <el-col :span="8" style="padding-right: 10px;">

            <el-card class="box-card">
                <div  class="user">
                    <div v-loading="this.$store.state.user.imageLoading">
                    <img  :src="imagUrl" alt="" /></div>
                    <div class="user-info">
                        <p class="name">{{ userInformation.name }}</p>
                    </div>
                </div>

                <div class="login-info">

                    <el-row>
                        <el-col :span="12" class="label">
                            <p>身份：</p>
                            <p v-if="!isResident">入职时间：</p>
                            <p v-else>住址：</p>
                        </el-col>
                        <el-col :span="12" class="value">
                            <p>{{ userInformation.type }}</p>
                            <p v-if="!isResident">{{ userInformation.employmentStart }}</p>
                            <p v-else>{{ userInformation.buildingRoomNumber }}</p>
                        </el-col>
                    </el-row>
                </div>
            </el-card>

            <div class="graph">
                <el-card>
                    <China style="margin: auto;"></China>
                </el-card>
            </div>
        </el-col>
        <el-col :span="16">
            <div class="data" style="padding-left: 10px; ">
                <el-card v-for="item in countData" :key="item.name" :body-style="{  display: 'flex', padding: 0 }">
                    <i class="icon" :class="`el-icon-${item.icon}`" :style="{ background: item.color }">
                    </i>
                    <div class="detail">
                        <p class="num">{{ item.value }}</p>
                        <p class="desc">{{ item.name }}</p>
                    </div>
                </el-card>
                <!-- <el-card style="margin-top:20px;">
    <div style="display: flex; flex-direction: column;">
        <span class="title">发泄吐槽栏</span>
        <el-input
            v-model="complaintMessage"
            placeholder="请输入您的吐槽信息"
            type="textarea"
            rows="4"
            style="margin-bottom: 10px;"
        ></el-input>
        <el-button type="primary" @click="sendComplaint">发送吐槽</el-button>
    </div>
</el-card> -->
    <!-- 悬浮按钮，默认显示 -->
    <el-button
        type="primary"
        icon="el-icon-message"
        circle
        style="position: fixed; bottom: 20px; right: 20px; z-index: 1000;"
        @click="showComplaintBox = true"
    ></el-button>

    <!-- 吐槽输入框，默认隐藏 -->
    <el-dialog
        title="发泄吐槽栏"
        :visible.sync="showComplaintBox"
        width="30%"
        @close="complaintMessage = ''"
    >
        <el-input
            v-model="complaintMessage"
            placeholder="请输入您的吐槽信息"
            type="textarea"
            rows="4"
            style="margin-bottom: 10px;"
        ></el-input>
        <span slot="footer" class="dialog-footer">
            <el-button @click="showComplaintBox = false">取消</el-button>
            <el-button type="primary" @click="sendComplaint">发送吐槽</el-button>
        </span>
    </el-dialog>
            </div>

            <el-card  style="margin-top:20px;height:600px">
                <div style="display:flex; flex-direction: column;"><span class="title">社区公告</span></div>
                <el-button type="primary" @click="newsDiaglogVisable = true" v-if="noticePrivilege">发布新公告</el-button>
                <el-table @row-click="seeNotice" class="jump-trl-table" :data="tableData" style="width: 100%">

                    <el-table-column align="center" label="序号" type="index" width="50" />
                    <el-table-column style="cursor:pointer" v-for="item in newsColumns" :key="item.prop" :label="item.label"
                        :prop="item.prop">
                    </el-table-column>
                    <el-table-column label="操作" fixed="right" v-if="noticePrivilege">
                    <template slot-scope="scope">
                        <el-popconfirm confirm-button-text='是' cancel-button-text='否' icon="el-icon-info" icon-color="red"
                            :title="`是否要删除这条公告`" @confirm="deleteNotice(scope.row.id)">
                            <el-button type="danger" slot="reference">删除</el-button>
                        </el-popconfirm>
                    </template>
                </el-table-column>
                </el-table>
                <!-- 分页 -->
                <div class="pagination" style="text-align: center;">
                    <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
                        :current-page="pagination.currentPage" :page-sizes="[1, 10, 15, 20, 25]"
                        :page-size="pagination.pageSize" layout="total, sizes, prev, pager, next, jumper"
                        :total="pagination.total">
                    </el-pagination>
                </div>
            </el-card>
        </el-col>
    </el-row>
</template>
<script>
import { getData } from '@/api/home'
import China from '@/components/china.vue'
import Cookie from 'js-cookie'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
import { insertData, getNotice,deleteData } from '@/api/notice'
import { insertComplaint } from '@/api/rubbish'
export default {
    data() {
        return {
            notice: {},
            newsDiaglogVisable: false,
            noticeDetailVisable: false,
            noticeLoading:false,
            noticeForm: {
                title: '',
                text: '',
                employeeId: '',
            },
            editor: null,
            userInformation: { "name": null, "type": null, "employmentStart": null },
            tableData: [],
            newsColumns: [{ label: "标题", prop: "title" }, { label: "发布时间", prop: "announceTime" }, { label: "发布人", prop: "employeeName" },],
            countData: [],
            pagination: {
                //分页相关属性
                currentPage: 1,
                pageSize: 15,
                total: null,
            },
            complaintMessage: '',  // 新增，用于存储吐槽信息
            showComplaintBox: false    // 控制弹框的显示
        }
    },
    mounted() {
        this.getpage()

       if(JSON.parse(Cookie.get("userInformation")).hasOwnProperty("position")){
        var id=JSON.parse(Cookie.get("userInformation")).guardNumber||JSON.parse(Cookie.get("userInformation")).employeeNumber
        var position=JSON.parse(Cookie.get("userInformation")).position
        var cardSearch={"id":id,"position":position}
        getData(cardSearch).then((res) => {
            this.countData = res.data.data;
        })
       }
        this.noticeForm.employeeId = JSON.parse(Cookie.get("userInformation")).employeeNumber;
    },
    components: {
        China, Editor, Toolbar
    },
    methods: {
        onCreated(editor) {
            this.editor = Object.seal(editor) // 一定要用 Object.seal() ，否则会报错
        },
        handleClose(done) {
            this.$confirm('确认关闭？')
                .then(_ => {
                    done();
                    this.resetForm("noticeForm");
                })
                .catch(_ => { });
        },
        resetForm(formName) {
            this.$refs[formName].resetFields();
        },
        seeNotice(row, column, event) {
            if (column.label !== '操作'){
            this.notice = { ...row }
            this.noticeDetailVisable = true;
            }
        },
        getpage() {

            const information = JSON.parse(Cookie.get("userInformation"))
            const type = Cookie.get("type")
            this.userInformation.name = information.name;
            this.userInformation.employmentStart = information.guardStart||information.employmentStart;
            this.userInformation.buildingRoomNumber=information.buildingRoomNumber;
            switch(type){
                case "0":this.userInformation.type = "管理员"; break;
                case "1":this.userInformation.type = "网格员"; break;
                case "2":this.userInformation.type = "网格长"; break;
                case "3":this.userInformation.type = "片长"; break;
                case "4":this.userInformation.type = "保安队长"; break;
                case "5":this.userInformation.type = "保安"; break;
                case "6":this.userInformation.type = "居民"; break;
            }
            this.noticeLoading=true
            getNotice(this.pagination).then((res) => {
                this.noticeLoading=false
                this.pagination.total = res.data.data.total;
                this.tableData = res.data.data.records;
            }).catch((res)=>this.noticeLoading=false)

        },
        announceNotice() {
            this.$refs["noticeForm"].validate((valid) => {
                if (valid) {
                    insertData(this.noticeForm).then((res) => {
                        if (res.data.code == 200) {
                            this.successMessage("发布成功！")
                            this.newsDiaglogVisable = false;
                            this.getpage();
                            this.resetForm("noticeForm");
                        }
                        else {
                            this.errorMessage(res.data.message)
                        }
                    }).catch((res) => {
                        this.errorMessage(res)
                    })


                } else {
                    console.log('error submit!!');
                    return false;
                }
            });
        },
        deleteNotice(id){
            deleteData(id).then((res) => {
                if (res.data.code == 200) {
                    this.successMessage("删除成功！")
                    this.getpage()
                }
                else {
                    this.errorMessage(res.data.message)
                }
            }).catch((res) => {
                this.errorMessage("删除失败")
            })
        },
        handleSizeChange(val) {
            this.pagination.pageSize = val;
            this.getPage()
        },
        handleCurrentChange(val) {
            this.pagination.currentPage = val;
            this.getPage()
        },
        errorMessage(message) {
            this.$message({
                showClose: true,
                message,
                type: 'error'
            })
        },
        successMessage(message) {
            this.$message({
                showClose: true,
                message,
                type: 'success'
            })
        },
        sendComplaint() {
        if (!this.complaintMessage.trim()) {
            this.errorMessage("请输入有效的吐槽内容");
            return;
        }

        // 调用后端 API 将吐槽信息发送到 RabbitMQ
        insertComplaint({ message: this.complaintMessage })
            .then((res) => {
                if (res.data.code === 200) {
                    this.successMessage("吐槽发送成功！");
                    this.complaintMessage = '';  // 清空输入框
                } else {
                    this.errorMessage(res.data.message);
                }
            })
            .catch(() => {
                this.errorMessage("发送失败，请稍后重试！");
            });
    },

    },
    computed: {
        imagUrl() {
            return this.$store.state.user.imgUrl;
        },
        myWidth(){
            switch (JSON.parse(Cookie.get("userInformation")).position) {
                case "0":case"1":case"2":case"3": return "32%"; 
                case "4":case"5":return "48%"; 
            }
        },
        noticePrivilege(){
            switch (JSON.parse(Cookie.get("userInformation")).position) {
                case "0":case"1":case"2":case"3": return true; 
                default:false;
            }
        },
        isResident(){
            if(!(JSON.parse(Cookie.get("userInformation")).hasOwnProperty('position'))){
                return true
            }
            else{
                return false
            }
        }
    }
}
</script>
<style lang="less" scope>
@my-width: v-bind(myWidth);
.user {
    display: flex;
    align-items: center;
    margin-bottom: 20px;
    border-bottom: 1px solid #ccc;

    img {
        width: 150px;
        height: 150px;
        border-radius: 50%;
        margin-right: 40px;
    }

    .name {
        font-size: 32px;
        margin-bottom: 10px;
    }

    .access {
        color: #999999
    }

}

.login-info {
    line-height: 28px;
    font-size: 14px;

    .label {

        color: #999999;


    }

    .value {
        color: #666666;

    }
}

.title {
    color: #202020;
    align-self: center;
}

.data {
    display: flex;
    flex-wrap: wrap;
    justify-content: space-between;

    .el-card {
        width: @my-width;
        margin-bottom: 20px;

        .icon {
            width: 80px;
            height: 80px;
            font-size: 30px;
            text-align: center;
            color: #fff;
            line-height: 80px;
        }

        .detail {
            display: flex;
            flex-direction: column;
            justify-content: center;
            margin-left: 15px;

            .num {
                font-size: 30px;
                margin-bottom: 10px;
                line-height: 30px;
            }

            .desc {
                font-size: 14px;
                color: #999;
                text-align: center;
            }
        }
    }
}

.jump-trl-table .el-table__row {
    cursor: pointer;
}

.graph {
    // display: flex;
    // justify-content: space-between;
    // margin-top: 20px;
    display: flex;
    justify-content: space-between;
    margin-top: 20px;
}
</style>
<style src="@wangeditor/editor/dist/css/style.css"></style>