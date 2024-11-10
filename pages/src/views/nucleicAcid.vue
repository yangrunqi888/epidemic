<template>
    <div v-loading="loading">
        <div>
            <el-dialog title="新增记录" :visible.sync="insertDialogVisible" width="30%" :before-close="handleClose">
                <el-form ref="insertAcid" :model="insertData" :rules="rules" label-width="120px">
                    <el-form-item  label="身份证号"  >
                        <el-input v-model="insertData.idNumber" disabled style="width:75%"></el-input>
                    </el-form-item>
                    <el-form-item prop="idNumber" label="姓名">
                        <el-select v-model="insertData.idNumber" filterable placeholder="请选择核酸人员">
                            <el-option v-for="item in residents" :key="item.idNumber" :label="item.name"
                                :value="item.idNumber">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item prop="collectTime" label="采集日期">
                        <el-date-picker type="date" placeholder="请选择采集日期" v-model="insertData.collectTime"
                            value-format="yyyy-MM-dd"></el-date-picker>
                    </el-form-item>
                    <el-form-item prop="type" label="核酸类型">
                        <el-select v-model="insertData.type" filterable placeholder="请选择核酸类型">
                            <el-option v-for="item in typeOptions" :key="item.value" :label="item.label"
                                :value="item.value">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" @click="submitForm('insertAcid')">确定</el-button>
                        <el-button @click="resetForm('insertAcid')">重置</el-button>
                    </el-form-item>
                </el-form>
            </el-dialog>
        </div>
        <!-- 筛选对话框 -->
        <el-dialog title="筛选记录" :visible.sync="filterDialogVisible" width="30%" :before-close="handleFilterClose">
            <el-form ref="filterForm" :model="searchAcidInformation" >
                <el-form-item prop="idNumber" label="身份证号"  >
                    <el-input v-model="searchAcidInformation.idNumber" style="width: 50%;"></el-input>
                </el-form-item>
                <el-form-item prop="name" label="姓名">
                    <el-input v-model="searchAcidInformation.name" style="width: 50%;"></el-input>
                </el-form-item>

                <el-form-item label="日期范围" prop="collectDateRange">
                    <el-date-picker type="daterange" placeholder="选择日期范围" v-model="searchAcidInformation.collectDateRange"
                        range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期" value-format="yyyy-MM-dd">
                    </el-date-picker>
                </el-form-item>


                <el-form-item prop="type" label="核酸类型">
                    <el-select v-model="searchAcidInformation.type" filterable multiple placeholder="请选择核酸类型">
                        <el-option v-for="item in typeOptions" :key="item.value" :label="item.label" :value="item.value">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item prop="result" label="核酸结果">
                    <el-select v-model="searchAcidInformation.result" filterable multiple placeholder="请选择核酸结果">
                        <el-option v-for="item in resultOptions" :key="item.value" :label="item.label" :value="item.value">
                        </el-option>
                    </el-select>
                </el-form-item>
                <div style="text-align: center;">
                    <el-button @click="resetForm('filterForm')" type="primary">重置</el-button>
                </div>
            </el-form>
        </el-dialog>
        <div>
            <el-dialog :visible.sync="imageVisible" title="核酸截图">
                <div style="text-align: center; ">
                    <img :src="acidPath" />
                </div>
            </el-dialog>
        </div>
        <div class="insert">
            <el-button type="primary" @click="insertDialogVisible = true">新增核酸任务</el-button>
        </div>
        <div class="filter">
            <el-button style="margin-right:20px" icon="el-icon-s-operation" size="mini"
                @click="filterDialogVisible = true">筛选</el-button>
        </div>
        <div class="NucidData">
            <el-table :data="tableData">
                <el-table-column v-for="item in tableColmuns" :key="item.prop" :prop="item.prop" :label="item.label"
                    :width="item.prop === `idNumber` ? `200px` : ``">
                </el-table-column>
                <el-table-column label="核酸截图">
                    <template slot-scope="scope">
                        <el-button round :disabled="scope.row.picturePath == null"
                            @click="seePicutre(scope.row)">{{ scope.row.picturePath == null?`待上传`:`查看` }}</el-button>
                    </template>
                </el-table-column>
                <el-table-column label="操作" fixed="right" width="300px">
                    <template slot-scope="scope">
                        <el-popconfirm confirm-button-text='是' cancel-button-text='否' icon="el-icon-info" icon-color="red"
                            :title="`是否要删除此条记录`" @confirm="deleteAcid(scope.row)">
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
import { getData, deleteData, insertData, getAcidPicture } from '@/api/acid';
import { getList } from '@/api/resident';
import Cookie from 'js-cookie'
export default {
    data() {
        return {
            insertDialogVisible: false,
            insertData: {
                idNumber: "",
                collectTime: "",
                type: "",
                
            },loading:false,
            filterDialogVisible: false,
            imageVisible: false,
            tableData: [],
            residents: [],
            acidPath: "",
            resultOptions: [{ "value": "0", "label": "阳性" }, { "value": "1", "label": "阴性" }, { "value": "2", "label": "未完成" }],
            typeOptions: [{ "value": "0", "label": "返乡核酸" }, { "value": "1", "label": "隔离核酸" }, { "value": "2", "label": "通勤人员" }, { "value": "3", "label": "其他" },],
            pagination: {
                //分页相关属性
                currentPage: 1,
                pageSize: 15,
                total: null,
                queryString: "",
            },
            searchAcidInformation: {
                pagination: null,
                idNumber: "",
                name: "",
                collectStartTime: "",
                collectEndTime: "",
                collectDateRange: [],
                type: [],
                result: [],


            }, tableColmuns: [{ "label": "身份证号", "prop": "idNumber" }, { "label": "姓名", "prop": "name" }, { "label": "采样日期", "prop": "collectTime" }, { "label": "核酸类型", "prop": "type" }, { "label": "核酸结果", "prop": "result" }
            ],
            rules:{
                idNumber:[
                { required: true, message: '请选择核酸人员', trigger: 'change' },
                ],
                type:[
                { required: true, message: '请选择核酸类型', trigger: 'change' },
                ],
                collectTime:[
                { required: true, message: '请选择核酸日期', trigger: 'change' },
                ],
            }
        }
    },
    mounted() {
        this.getPage();
        getList(this.pagination.queryString).then((res) => {
            this.residents = res.data.data;
        })
    },
    methods: {
        getPage() {
            this.loading=true;
            this.pagination.queryString = JSON.parse(Cookie.get("userInformation")).employeeNumber;
            this.searchAcidInformation.pagination = this.pagination;
            getData(this.searchAcidInformation).then((res) => {
                this.loading=false;
                this.pagination.total = res.data.data.total;
                this.tableData = res.data.data.records;
            }).catch((res)=>this.loading=false)
        },
        deleteAcid(acid) {
            deleteData(acid).then((res) => {
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
        submitForm(formName) {
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    insertData(this.insertData).then((res) => {
                        if (res.data.code == 200) {
                            this.successMessage("新增成功！")
                            this.insertDialogVisible = false;
                            this.getPage();
                            this.resetForm(formName);
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
        }, resetForm(formName) {
            this.$refs[formName].resetFields();

        },
        seePicutre(acid) {
            this.loading=true;
            getAcidPicture(acid).then((res) => {
                this.loading=false;
                this.acidPath = window.URL.createObjectURL(res.data);
                this.imageVisible = true
            }).catch((res) => {
                this.loading=false;
                this.errorMessage("查看失败")
            })

        },
        handleClose(done) {
            this.$confirm('确认关闭？')
                .then(_ => {
                    done();
                    this.resetForm("insertAcid");
                })
                .catch(_ => { });
        },
        handleFilterClose(done) {
            this.searchAcidInformation.collectStartTime = this.searchAcidInformation.collectDateRange[0];
            this.searchAcidInformation.collectEndTime = this.searchAcidInformation.collectDateRange[1];
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

