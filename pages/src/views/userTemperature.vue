<template>
    <div v-loading="loading">
        <!-- 上传对话框 -->
        <el-dialog title="上传信息" :visible.sync="uploadInformationDialogVisable" width="30%" :before-close="handleClose">
            <el-form ref="uploadInformation" :model="uploadInformation" label-position="top" :rules="rules"
                label-width="150px">
                <el-form-item prop="collectTime" label="测量时间">
                    <el-date-picker type="datetime" placeholder="请选择测量时间" v-model="uploadInformation.collectTime"
                        value-format="yyyy-MM-dd HH:mm:ss"></el-date-picker>
                </el-form-item>
                <el-form-item prop="result" label="体温">
                    <el-input-number v-model="uploadInformation.result" :precision="1" :step="0.1" :min="35"
                        :max="42"></el-input-number>
                </el-form-item>
            </el-form>
            <template slot="footer">
                <el-button type="primary" @click="submitForm('uploadInformation')">上传</el-button>
            </template>
        </el-dialog>
        <div class="insert">
            <el-button type="primary" @click="uploadInformationDialogVisable = true">体温上报</el-button>
        </div>
        <!-- 筛选对话框 -->
        <el-dialog title="筛选记录" :visible.sync="filterDialogVisible" width="30%" :before-close="handleFilterClose">
            <el-form ref="filterForm" :model="searchTemperatureInformation">
                <el-form-item label="日期范围" prop="collectDateRange">
                    <el-date-picker type="datetimerange" placeholder="选择日期范围"
                        v-model="searchTemperatureInformation.collectDateRange" range-separator="-" start-placeholder="开始日期"
                        end-placeholder="结束日期" value-format="yyyy-MM-dd HH:mm:ss">
                    </el-date-picker>
                </el-form-item>
                <div style="text-align: center;">
                    <el-button @click="resetForm('filterForm')" type="primary">重置</el-button>
                </div>
            </el-form>
        </el-dialog>
        <div class="filter">
            <el-button style="margin-right:20px" icon="el-icon-s-operation" size="mini"
                @click="filterDialogVisible = true">筛选</el-button>
        </div>
        <div class="NucidData">
            <el-table :data="tableData">
                <el-table-column v-for="item in tableColmuns" :key="item.prop" :prop="item.prop" :label="item.label"
                    :width="item.prop === `idNumber` ? `200px` : ``">
                </el-table-column>

                <el-table-column label="状态">
                    <template slot-scope="scope">
                        {{ scope.row.result <= 37 ? `体温正常` : `体温异常` }} </template> </el-table-column>
                <el-table-column label="操作" fixed="right" width="300px">
                    <template slot-scope="scope">
                        <el-popconfirm confirm-button-text='是' cancel-button-text='否' icon="el-icon-info" icon-color="red"
                            :title="`是否要删除此条记录`" @confirm="deleteTemperature(scope.row)">
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
import { getData, deleteData, insertData } from '@/api/temperature';
import Cookie from 'js-cookie'
export default {
    data() {
        return {
            loading: false,
            filterDialogVisible: false,
            uploadInformationDialogVisable: false,
            tableData: [],
            uploadInformation: {
                collectTime: "",
                result: 37.0,
                idNumber: "",
            },
            pagination: {
                //分页相关属性
                currentPage: 1,
                pageSize: 15,
                total: null,
                queryString: "",
            },
            searchTemperatureInformation: {
                pagination: null,
                idNumber: "",
                name: "",
                collectStartTime: "",
                collectEndTime: "",
                collectDateRange: [],
                result: [],
            }, tableColmuns: [{ "label": "测量时间", "prop": "collectTime" }, { "label": "温度", "prop": "result" }
            ],
            rules: {
                result: [
                    { required: true, message: '请输入体温', trigger: 'change' },
                ],
                collectTime:[
                { required: true, message: '请选择测量日期', trigger: 'change' },
                ]
            }
        }
    }, mounted() {
        this.getPage();
    },
    methods: {
        getPage() {
            this.loading = true;
            this.pagination.queryString = "admin"
            this.searchTemperatureInformation.idNumber = JSON.parse(Cookie.get("userInformation")).idNumber;
            this.searchTemperatureInformation.pagination = this.pagination;
            getData(this.searchTemperatureInformation).then((res) => {
                this.loading = false
                this.pagination.total = res.data.data.total;
               
                this.tableData = res.data.data.records.map(obj => ({
                    ...obj,
                    result: obj.result.toFixed(1)
                }));
            }).catch((res) => this.loading = false)
        },
        submitForm(formName) {
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    this.uploadInformation.idNumber = JSON.parse(Cookie.get("userInformation")).idNumber;

                    insertData(this.uploadInformation).then((res) => {
                        if (res.data.code == 200) {
                            this.successMessage("上传成功！")
                            this.uploadInformationDialogVisable = false;
                            this.antigenPicture = ""
                            this.previewUrl = ""
                            this.getPage();
                            this.resetForm(formName);
                        }
                        else if (res.data.code == 414) {
                            this.errorMessage("您今日已上传过抗原信息")
                        }
                        else {
                            this.errorMessage(res.data.message)
                        }
                    }).catch((res) => {
                        this.errorMessage("输入信息有误，请仔细核对")
                    })


                } else {
                    console.log('error submit!!');
                    return false;
                }
            });
        },
        deleteTemperature(tem) {
            deleteData(tem).then((res) => {
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
        resetForm(formName) {
            this.$refs[formName].resetFields();

        },
        handleClose(done) {
            this.$confirm('确认关闭？')
                .then(_ => {
                    done();
                    this.resetForm("uploadInformation");
                })
                .catch(_ => { });
        },
        handleFilterClose(done) {
            this.searchTemperatureInformation.collectStartTime = this.searchTemperatureInformation.collectDateRange[0];
            this.searchTemperatureInformation.collectEndTime = this.searchTemperatureInformation.collectDateRange[1];
            this.getPage();
            done();
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
    }
}
</script>