<template>
    <div  v-loading="loading"> 
        <el-dialog title="筛选记录" :visible.sync="filterDialogVisible" width="30%" :before-close="handleFilterClose">
            <el-form ref="filterForm" :model="searchBackHomeInformation" >
                <el-form-item prop="idNumber" label="身份证号"  >
                    <el-input placeholder="请输入身份证号" v-model="searchBackHomeInformation.idNumber" style="width: 50%;"></el-input>
                </el-form-item>
                <el-form-item prop="name" label="姓名">
                    <el-input placeholder="请输入姓名" v-model="searchBackHomeInformation.name" style="width: 50%;"></el-input>
                </el-form-item>
                <el-form-item label="日期范围" prop="collectDateRange">
                    <el-date-picker type="daterange" placeholder="选择日期范围" v-model="searchBackHomeInformation.collectDateRange"
                        range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期" value-format="yyyy-MM-dd">
                    </el-date-picker>
                </el-form-item>
                <el-form-item prop="state" label="审核状态">
                    <el-select v-model="searchBackHomeInformation.state" filterable multiple placeholder="请选择核酸类型">
                        <el-option v-for="item in stateOptions" :key="item.value" :label="item.label" :value="item.value">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item prop="employerNumber" label="审核人工号"  >
                    <el-input placeholder="请输入审核人工号" v-model="searchBackHomeInformation.employerNumber" style="width: 50%;"></el-input>
                </el-form-item>
                <el-form-item prop="employerName" label="审核人姓名">
                    <el-input placeholder="请输入审核人姓名" v-model="searchBackHomeInformation.employerName" style="width: 50%;"></el-input>
                </el-form-item>
                
                <div style="text-align: center;">
                    <el-button @click="resetForm('filterForm')" type="primary">重置</el-button>
                </div>
            </el-form>
        </el-dialog>
        <el-dialog title="提示" :visible.sync="verifyConfirmDialogVisable" width="30%">
            <span>是否{{verifyType==false?"拒绝":"同意"}}{{ verifyInformation.name }}的返乡请求</span>
            <span slot="footer" class="dialog-footer">
                <el-button @click="verifyConfirmDialogVisable = false">取 消</el-button>
                <el-button type="primary" @click="verifySubmit()">确 定</el-button>
            </span>
        </el-dialog>
        <el-dialog title="截图" :visible.sync="ImageVisible" width="30%">
            <div style="text-align: center;">
                <img :src="picturePath" style="max-width: 100%; max-height: 100%;" />
            </div>
        </el-dialog>
        <div class="verify">
            <el-dialog title="返乡审核" :visible.sync="verifyDialogVisible" width="40%" v-loading="imageLoading"  element-loading-text="拼命加载中"
    element-loading-spinner="el-icon-loading"
    element-loading-background="rgba(0, 0, 0, 0.8)">
                <el-row class="row">
                    <!-- <div style="text-align: center;">
                        <el-col :span="12">{{ item.label }}</el-col>
                        <el-col :span="12">{{ verifyInformation[item.prop] }}</el-col>
                    </div> -->
                    <el-descriptions class="margin-top" :column="2" :size="size" border>
                <el-descriptions-item v-for="item in verifyColumns" :key="item.prop">
                    <template slot="label">
                        {{ item.label }}
                    </template>
                    {{ verifyInformation[item.prop] }}
                </el-descriptions-item>
                <el-descriptions-item>
                    <template slot="label">
                        健康码截图
                    </template>
                    <el-button @click="seePicutre(verifyInformation, 0)">查看</el-button>
                </el-descriptions-item>
                <el-descriptions-item>
                    <template slot="label">
                        行程码截图
                    </template>
                    <el-button @click="seePicutre(verifyInformation, 1)">查看</el-button>
                </el-descriptions-item>
                <el-descriptions-item>
                    <template slot="label">
                        留言
                    </template>
                    <el-input v-model="verifyInformation.demand" placeholder="请输入留言" type="textarea"
                    autosize></el-input>
                </el-descriptions-item>
            </el-descriptions>
                </el-row>
                <el-row class="row">
                    <el-col :span="12">
                        <div style="text-align: center;">
                            <div>
                                <el-button type="success" @click="verifyType=true;verifyConfirmDialogVisable=true;">通过</el-button>
                            </div>
                        </div>
                    </el-col>
                    <el-col :span="12">
                        <div style="text-align: center;">
                            <div><el-button type="danger" @click="verifyType=false;verifyConfirmDialogVisable=true;">不通过</el-button></div>
                        </div>
                    </el-col>
                </el-row>
            </el-dialog>
        </div>
        
        <div class="backHomeData">
            <div class="filter">
            <el-button style="margin-right:20px" icon="el-icon-s-operation" size="mini"
                @click="filterDialogVisible = true">筛选</el-button>
        </div>
            <el-table :data="tableData">
                <el-table-column v-for="item in tableColmuns" :key="item.prop" :prop="item.prop" :label="item.label"
                    :width="item.prop === `idNumber` ? `200px` : ``">
                </el-table-column>
                <el-table-column label="状态"  >
                    <template slot-scope="scope">
                        <el-button v-if="scope.row.state === `未审核`" type="primary"
                            @click="handleVerify(scope.row)">审核</el-button>
                        <span v-else>{{ scope.row.state }}</span>
                        
                    </template>
                </el-table-column>
                <el-table-column label="操作" v-if="deletePrivilege">
                    <template slot-scope="scope">
                        <el-popconfirm confirm-button-text='是' cancel-button-text='否' icon="el-icon-info" icon-color="red"
                            :title="`是否要删除此条信息`" @confirm="deleteBackHome(scope.row)">
                            <el-button type="danger" slot="reference">删除</el-button>
                        </el-popconfirm>
                        
                    </template>
                </el-table-column>
            </el-table>
        </div>
        <!-- 分页 -->
        <div class="pagination" style="text-align: center;">
            <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
                :current-page="pagination.currentPage" :page-sizes="[1, 10, 15, 20, 25]" :page-size="pagination.pageSize"
                layout="total, sizes, prev, pager, next, jumper" :total="pagination.total">
            </el-pagination>
        </div>
    </div>
</template>
<script>
import { getData, getHealthPicture, getTravelPicture,verify,deleteData } from '@/api/backHome';
import Cookie from 'js-cookie'
export default {
    data() {
        return {
            verifyDialogVisible: false,
            verifyConfirmDialogVisable:false,
            filterDialogVisible: false,
            ImageVisible: false,
            verifyType:false,
            imageLoading:false,
            loading:false,
            tableData: [],
            residents: [],
            picturePath: "",
            stateOptions: [{ "value": "0", "label": "未通过" }, { "value": "1", "label": "已通过" }, { "value": "2", "label": "未审核" }],
            pagination: {
                //分页相关属性
                currentPage: 1,
                pageSize: 15,
                total: null,
                queryString: "",
            },
            searchBackHomeInformation: {
                pagination: null,
                idNumber: "",
                name: "",
                collectDateRange:"",
                startBackDate: "",
                endBackDate: "",
                state: [],
                employerNumber: "",
                employerName: "",
            

            },
            tableColmuns: [{ "label": "身份证号", "prop": "idNumber" }, { "label": "姓名", "prop": "name" }, { "label": "返乡时间", "prop": "backDate" },
            { "label": "当前所在地", "prop": "currentLocation" }, { "label": "审核人", "prop": "employerName" }, { "label": "返乡要求", "prop": "demand" },
            ],
            verifyInformation: {},
            verifyColumns: [{ "label": "身份证号：", "prop": "idNumber" }, { "label": "姓名：", "prop": "name" }, { "label": "返乡时间：", "prop": "backDate" },
            { "label": "当前所在地：", "prop": "currentLocation" },],
        }
    },
    mounted() {
        this.getPage();
    },
    methods: {
        getPage() {
            this.loading=true;
            this.pagination.queryString = JSON.parse(Cookie.get("userInformation")).employeeNumber;
            this.searchBackHomeInformation.pagination = this.pagination;
            getData(this.searchBackHomeInformation).then((res) => {
                this.loading=false;
                this.pagination.total = res.data.data.total;
                this.tableData = res.data.data.records;

            }).catch((res)=>this.loading=false)
        },
        handleFilterClose(done) {
            this.searchBackHomeInformation.startBackDate = this.searchBackHomeInformation.collectDateRange[0];
            this.searchBackHomeInformation.endBackDate = this.searchBackHomeInformation.collectDateRange[1];
            this.getPage();
            done();
        },
        deleteBackHome(row){
            deleteData(row).then((res) => {
                if (res.data.code == 200) {
                    this.successMessage("删除成功！")
                    this.getPage()
                }
                else {
                    this.errorMessage(res.data.message)
                }
            }).catch((res) => {
                this.errorMessage("删除失败")
            })
        },
        seePicutre(back, val) {
            this.imageLoading=true;
            if (val == 0) {
                getHealthPicture(back).then((res) => {
                    this.imageLoading=false;
                    this.picturePath = window.URL.createObjectURL(res.data);
                    this.ImageVisible = true
                }).catch((res) => {
                    this.imageLoading=false;
                    this.errorMessage("查看失败")
                })
            }
            else if (val == 1) {
                getTravelPicture(back).then((res) => {
                    this.imageLoading=false;
                    this.picturePath = window.URL.createObjectURL(res.data);
                    this.ImageVisible = true
                }).catch((res) => {
                    this.imageLoading=false;
                    this.errorMessage("查看失败")
                })
            }

        },
        verifySubmit() {
            this.verifyInformation.employerNumber = this.pagination.queryString;
            this.verifyInformation.state=this.verifyType==false?"0":"1";
            verify(this.verifyInformation).then((res) => {
                if (res.data.code == 200) {
                    this.successMessage("审核完成！")
                    this.verifyConfirmDialogVisable=false;
                    this.verifyDialogVisible=false;
                    this.getPage()
                }
                else {
                    this.errorMessage(res.data.message)
                }
            }).catch((res) => {
                this.errorMessage("审核失败")
            })

        },
        resetForm(formName) {
            this.$refs[formName].resetFields();

        },
        handleVerify(row) {
            this.verifyInformation = {...row};
            this.verifyDialogVisible = true;
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
        }
    },
    computed:{
        deletePrivilege(){
            switch (JSON.parse(Cookie.get("userInformation")).position) {
                case "0": return true; 
                default:false;
            }
        }
    }
}
</script>
<style lang="less" scoped>
.verify {
    .row {
        margin-top: 20px;
        margin-bottom: 20px;
    }
}
</style>