<template>
    <div v-loading="loading">
        <el-dialog title="详细记录" :visible.sync="detailDialogVisible" width="50%">
            <el-descriptions class="margin-top" :column="2" :size="size" border>
                <el-descriptions-item v-for="item in dialogColmuns" :key="item.prop">
                    <template slot="label">
                        {{ item.label }}
                    </template>
                    {{ detailInformation[item.prop] }}
                </el-descriptions-item>
            </el-descriptions>
            <span slot="footer">
        <el-button v-if="detailInformation.state === `外出中`" type="primary" @click="confirmBackDialogVisable = true">确认返回</el-button>
        <el-button  type="danger" @click="confirmDeleteDialogVisable = true" v-if="deletePrivilege">删除</el-button>
    </span>
        </el-dialog>
        <el-dialog title="提示" :visible.sync="confirmBackDialogVisable" width="30%">
        <span>是否确认居民{{ detailInformation.name }}已返回</span>
        <span slot="footer" class="dialog-footer">
          <el-button @click="confirmBackDialogVisable = false">取 消</el-button>
          <el-button type="primary" @click="confirmBack()">确 定</el-button>
        </span>
      </el-dialog>
      <el-dialog  :visible.sync="confirmDeleteDialogVisable" width="30%">
        <template slot="title">
                <div style="text-align: center; font-size:20px;">
                    <i class="el-icon-warning" style="color:red;"></i>警告
                </div>
            </template>
        <span>是否删除居民{{ detailInformation.name }}此次出行记录</span>
        <span slot="footer" class="dialog-footer">
          <el-button @click="confirmDeleteDialogVisable = false">取 消</el-button>
          <el-button type="primary" @click="confirmDelete()">确 定</el-button>
        </span>
      </el-dialog>
        <el-dialog title="新增记录" :visible.sync="insertDialogVisible" width="30%" :before-close="handleClose">
            <el-form ref="insertOut" :model="insertData" :rules="rules" label-width="120px">
                <el-form-item label="身份证号">
                    <el-input v-model="insertData.idNumber" disabled style="width:75%"></el-input>
                </el-form-item>
                <el-form-item prop="idNumber" label="姓名">
                    <el-select v-model="insertData.idNumber" filterable placeholder="请选择外出人员">
                        <el-option v-for="item in residents" :key="item.idNumber" :label="item.name" :value="item.idNumber">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item prop="reason" label="外出原因">
                    <el-input v-model="insertData.reason" style="width:75%"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="submitForm('insertOut')">确定</el-button>
                    <el-button @click="resetForm('insertOut')">重置</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>
        <el-dialog title="筛选记录" :visible.sync="filterDialogVisible" width="30%" :before-close="handleFilterClose">
            <el-form ref="filterForm" :model="searchOutInformation">
                <el-form-item prop="idNumber" label="身份证号">
                    <el-input v-model="searchOutInformation.idNumber" style="width: 50%;" placeholder="请输入身份证号"></el-input>
                </el-form-item>
                <el-form-item prop="name" label="姓名">
                    <el-input v-model="searchOutInformation.name" style="width: 50%;" placeholder="请输入姓名"></el-input>
                </el-form-item>
                <el-form-item label="外出日期范围" prop="outTime">
                    <el-date-picker type="datetimerange" placeholder="选择日期范围" v-model="searchOutInformation.outTime"
                        range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期"
                        value-format="yyyy-MM-dd HH:mm:ss">
                    </el-date-picker>
                </el-form-item>
                <el-form-item label="返回日期范围" prop="backTime">
                    <el-date-picker type="datetimerange" placeholder="选择日期范围" v-model="searchOutInformation.backTime"
                        range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期"
                        value-format="yyyy-MM-dd HH:mm:ss">
                    </el-date-picker>
                </el-form-item>
                <el-form-item prop="state" label="状态">
                    <el-select v-model="searchOutInformation.state" filterable placeholder="请选择返回状态">
                        <el-option v-for="item in stateOptions" :key="item.value" :label="item.label" :value="item.value">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item prop="guardNumber" label="批准保安工号">
                    <el-input v-model="searchOutInformation.guardNumber" style="width: 50%;" placeholder="请输入批准保安工号"></el-input>
                </el-form-item>
                <el-form-item prop="guardName" label="批准保安姓名">
                    <el-input v-model="searchOutInformation.guardName" style="width: 50%;" placeholder="请输入批准保安姓名"></el-input>
                </el-form-item>
                <el-form-item prop="outTotalTime" label="外出总时间范围(分钟)"><br>
                    <el-slider v-model="searchOutInformation.outTotalTime" :step="30" range style="width:80%" :max="999">
                    </el-slider>
                </el-form-item>
                <div style="text-align: center;">
                    <el-button @click="resetForm('filterForm')" type="primary">重置</el-button>
                </div>
            </el-form>
        </el-dialog>
        <div class="Data">
            <div class="insert" v-if="insertPrivilege">
                <el-button type="primary" @click="insertDialogVisible = true">新增外出</el-button>
            </div>
            <div class="filter">
                <el-button style="margin-right:20px" icon="el-icon-s-operation" size="mini"
                    @click="filterDialogVisible = true">筛选</el-button>
            </div>
            <el-table :data="tableData">
                <el-table-column v-for="item in tableColmuns" :key="item.prop" :prop="item.prop" :label="item.label"
                    :width="flexWidth(item.prop, tableData, item.label)">
                </el-table-column>
                <el-table-column label="操作" fixed="right" width="300px">
                    <template slot-scope="scope">

                        <el-button type="primary" @click="handleDetail(scope.row)">查看详细</el-button>
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
import { getData, insertData, updateData,deleteData } from '@/api/out';
import Cookie from 'js-cookie'
import { getList } from '@/api/resident';
export default {
    data() {
        return {
            filterDialogVisible: false,
            insertDialogVisible: false,
            detailDialogVisible: false,
            confirmBackDialogVisable:false,
            confirmDeleteDialogVisable:false,
            loading: false,
            tableData: [],
            residents: [],
            insertData: {
                idNumber: "",
                outGuardNumber: "",
                reason: "",
            },
            pagination: {
                //分页相关属性
                currentPage: 1,
                pageSize: 15,
                total: null,
                queryString: "",
            },
            searchOutInformation: {
                pagination: null,
                idNumber: "",
                name: "",
                outTime: [],
                backTime: [],
                guardNumber: "",
                guardName: "",
                outTotalTime: [0, 999],
                state: ""
            },
            tableColmuns: [{ "label": "身份证号", "prop": "idNumber" }, { "label": "姓名", "prop": "name" }, { "label": "外出时间", "prop": "outTime" },
            { "label": "批准外出保安", "prop": "outGuardName" }, { "label": "状态", "prop": "state" },
            { "label": "外出时长（分钟）", "prop": "outTotalTime" }
            ],
            dialogColmuns: [{ "label": "身份证号", "prop": "idNumber" }, { "label": "姓名", "prop": "name" }, 
            { "label": "外出时间", "prop": "outTime" },{ "label": "返回时间", "prop": "backTime" }, 
             { "label": "批准外出保安", "prop": "outGuardName" }, { "label": "批准返回保安", "prop": "backGuardName" }, 
             { "label": "状态", "prop": "state" },{ "label": "外出时长（分钟）", "prop": "outTotalTime" },{ "label": "外出原因", "prop": "reason" },
            ],
            detailInformation: {},
            stateOptions: [{ label: "外出中", value: "0" }, { label: "已返回", value: "1" }],
            rules: {
                idNumber: [
                    { required: true, message: '请选择外出人员', trigger: 'change' },
                ],
                reason: [
                    { required: true, message: '请输入外出理由', trigger: 'blur' },
                ],
            }
        }
    },
    mounted() {
        this.getPage();
        getList("guard").then((res) => {
            this.residents = res.data.data;
        })
    },
    methods: {
        getPage() {
            this.loading = true;
            this.pagination.queryString = JSON.parse(Cookie.get("userInformation")).employeeNumber;
            this.searchOutInformation.pagination = this.pagination;
            getData(this.searchOutInformation).then((res) => {
                this.loading = false;
                this.pagination.total = res.data.data.total;
                this.tableData = res.data.data.records;

            }).catch((res) => this.loading = false)
        },
        submitForm(formName) {
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    this.insertData.outGuardNumber = JSON.parse(Cookie.get("userInformation")).guardNumber;
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
        },
        handleDetail(row) {
            this.detailDialogVisible = true;
            this.detailInformation = { ...row };
        },
        confirmBack() {
            this.detailInformation.backGuardNumber = JSON.parse(Cookie.get("userInformation")).guardNumber;

            updateData(this.detailInformation).then((res) => {
                if (res.data.code == 200) {
                    this.successMessage("处理成功！")
                    this.confirmBackDialogVisable=false;
                    this.detailDialogVisible=false;
                    this.getPage()
                }
                else {
                    this.errorMessage(res.data.message)
                    this.confirmBackDialogVisable=false;
                }
            }).catch((res) => {
                this.errorMessage("处理失败！")
                this.confirmBackDialogVisable=false;
            })
        },
        confirmDelete(){
            deleteData(this.detailInformation).then((res) => {
                if (res.data.code == 200) {
                    this.successMessage("删除成功！")
                    this.getPage()
                    this.confirmDeleteDialogVisable=false;
                    this.detailDialogVisible=false;
                }
                else {
                    this.errorMessage(res.data.message)
                    this.confirmDeleteDialogVisable=false;
                }
            }).catch((res) => {
                this.errorMessage("删除失败")
                this.confirmDeleteDialogVisable=false;
            })
        },
        handleClose(done) {
            this.$confirm('确认关闭？')
                .then(_ => {
                    done();
                    this.resetForm("insertOut");
                })
                .catch(_ => { });
        },
        handleFilterClose(done) {
            this.getPage();
            done();
        },
        resetForm(formName) {
            this.$refs[formName].resetFields();
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
        flexWidth(prop, tableData, title, num = 0) {


            if (tableData.length === 0) {//表格没数据不做处理
                return;
            }

            let flexWidth = 0;//初始化表格列宽
            let columnContent = '';//占位最宽的内容
            let canvas = document.createElement("canvas");
            let context = canvas.getContext("2d");
            context.font = "14px Microsoft YaHei";

            if ((prop === '') && title) {//标题长内容少的，取标题的值,
                columnContent = title

            } else {// 获取该列中占位最宽的内容
                let index = 0;

                for (let i = 0; i < tableData.length; i++) {

                    const now_temp = tableData[i][prop] + '';
                    const max_temp = tableData[index][prop] + '';
                    const now_temp_w = context.measureText(now_temp).width
                    const max_temp_w = context.measureText(max_temp).width
                    if (now_temp_w > max_temp_w) {
                        index = i;
                    }
                }
                columnContent = tableData[index][prop]
                //比较占位最宽的值跟标题、标题为空的留出四个位置
                const column_w = context.measureText(columnContent).width
                const title_w = context.measureText(title).width
                if (column_w < title_w) {
                    columnContent = title || '留四个字'
                }
            }
            // 计算最宽内容的列宽
            let width = context.measureText(columnContent);
            flexWidth = width.width + 40 + num
            return flexWidth + 5 + 'px';
        },
    },
    computed:{
        insertPrivilege(){
            switch (JSON.parse(Cookie.get("userInformation")).position) {
                case "0":case"4":case"5": return true; 
                default:false;
            }
        },
        deletePrivilege(){
            switch (JSON.parse(Cookie.get("userInformation")).position) {
                case "0":case"5": return true; 
                default:false;
            }
        }
    }
}
</script>