<template>
    <div v-loading="loading">
      <div>
            <el-dialog title="新增记录" :visible.sync="insertDialogVisible" width="40%" :before-close="handleClose">
                <el-form ref="insertQuarantine" :model="insertData" :rules="rules" label-width="120px">
                    <el-form-item  label="身份证号"  >
                        <el-input v-model="insertData.idNumber" disabled style="width:50%"></el-input>
                    </el-form-item>
                    <el-form-item prop="idNumber" label="姓名">
                        <el-select v-model="insertData.idNumber" filterable placeholder="请选择隔离人员">
                            <el-option v-for="item in residents" :key="item.idNumber" :label="item.name"
                                :value="item.idNumber">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item prop="type" label="隔离类型">
                        <el-select v-model="insertData.type" filterable placeholder="请选择隔离类型">
                            <el-option v-for="item in typeOptions" :key="item.value" :label="item.label"
                                :value="item.value">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="隔离期限" prop="collectDateRange">
                    <el-date-picker type="daterange" placeholder="选择日期范围" v-model="insertData.collectDateRange"
                        range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期" value-format="yyyy-MM-dd">
                    </el-date-picker>
                </el-form-item>
                    
                    <el-form-item>
                        <el-button type="primary" @click="submitForm('insertQuarantine')">确定</el-button>
                        <el-button @click="resetForm('insertQuarantine')">重置</el-button>
                    </el-form-item>
                </el-form>
            </el-dialog>
        </div>
        <!-- 筛选对话框 -->
        <el-dialog title="筛选记录" :visible.sync="filterDialogVisible" width="30%" :before-close="handleFilterClose">
            <el-form ref="filterForm" :model="searchQuarantineInformation" >
                <el-form-item prop="idNumber" label="身份证号"  >
                    <el-input placeholder="请输入身份证号" v-model="searchQuarantineInformation.idNumber" style="width: 50%;"></el-input>
                </el-form-item>
                <el-form-item prop="name" label="姓名">
                    <el-input placeholder="请输入姓名" v-model="searchQuarantineInformation.name" style="width: 50%;"></el-input>
                </el-form-item>
                <el-form-item prop="type" label="隔离类型">
                    <el-select v-model="searchQuarantineInformation.type" filterable placeholder="请选择隔离类型">
                        <el-option v-for="item in typeOptions" :key="item.value" :label="item.label" :value="item.value">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="开始隔离日期范围" prop="startTime">
                    <el-date-picker type="daterange" placeholder="选择日期范围" v-model="searchQuarantineInformation.startTime"
                        range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期" value-format="yyyy-MM-dd">
                    </el-date-picker>
                </el-form-item>
                <el-form-item label="结束隔离日期范围" prop="endTime">
                    <el-date-picker type="daterange" placeholder="选择日期范围" v-model="searchQuarantineInformation.endTime"
                        range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期" value-format="yyyy-MM-dd">
                    </el-date-picker>
                </el-form-item>
                <el-form-item prop="days">
                            <span>隔离天数范围</span>
                            <el-slider v-model="searchQuarantineInformation.days" range style="width:80%" :max="28">
                            </el-slider>
                        </el-form-item>
                        <el-form-item prop="state" label="隔离状态">
                    <el-select v-model="searchQuarantineInformation.state" filterable placeholder="请选择隔离状态">
                        <el-option v-for="item in stateOptions" :key="item.value" :label="item.label" :value="item.value">
                        </el-option>
                    </el-select>
                </el-form-item>
                <div style="text-align: center;">
                    <el-button @click="resetForm('filterForm')" type="primary">重置</el-button>
                </div>
            </el-form>
        </el-dialog>
        <div class="insert">
            <el-button type="primary" @click="insertDialogVisible = true">新增隔离</el-button>
        </div>
        <div class="filter">
            <el-button style="margin-right:20px" icon="el-icon-s-operation" size="mini"
                @click="filterDialogVisible = true">筛选</el-button>
        </div>
      <div class="QuarantineData">
            <el-table :data="tableData">
                <el-table-column v-for="item in tableColmuns" :key="item.prop" :prop="item.prop" :label="item.label"
                    :width="item.prop === `idNumber` ? `200px` : ``">
                </el-table-column>
                <el-table-column label="操作" fixed="right" width="300px">
                    <template slot-scope="scope">
                        <el-popconfirm confirm-button-text='是' cancel-button-text='否' icon="el-icon-info" icon-color="red"
                            :title="`是否要删除此条记录`" @confirm="deleteQuarantine(scope.row)">
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
import { getData,insertData,deleteData} from '@/api/quarantine';
import { getList } from '@/api/resident';
import Cookie from 'js-cookie'
export default{
  data(){
    return{
      insertDialogVisible: false,
      loading:false,
            insertData: {
                idNumber: "",
                type: "",
                collectDateRange:[],
                startTime: "",
                endTime:"",
            },
            filterDialogVisible: false,
            tableData: [],
            residents: [],
            typeOptions: [{ "value": "0", "label": "居家隔离" }, { "value": "1", "label": "集中隔离" }],
            stateOptions: [{ "value": "0", "label": "未开始隔离" }, { "value": "1", "label": "正在隔离" }, { "value": "2", "label": "结束隔离" }],
            pagination: {
                //分页相关属性
                currentPage: 1,
                pageSize: 15,
                total: null,
                queryString: "",
            },
            searchQuarantineInformation: {
                pagination: null,
                idNumber:"",
                name:"",
                type:"",
                startTime:[],
                endTime:[],
                days:[1,28],
                state:""
            }, 
            tableColmuns: [{ "label": "身份证号", "prop": "idNumber" }, { "label": "姓名", "prop": "name" }, { "label": "隔离类型", "prop": "type" }, { "label": "开始时间", "prop": "startTime" }, { "label": "结束时间", "prop": "endTime" }, { "label": "隔离天数", "prop": "days" }, { "label": "隔离状态", "prop": "state" }
            ],
            rules:{
                idNumber:[
                { required: true, message: '请选择核酸人员', trigger: 'change' },
                ],
                type:[
                { required: true, message: '请选择隔离类型', trigger: 'change' },
                ],
                collectDateRange:[
                { required: true, message: '请选择隔离期限', trigger: 'change' },
                ],
            }
    }
  },
  mounted(){
    this.getPage();
    getList(this.pagination.queryString).then((res) => {
            this.residents = res.data.data;
        })
    
  },
  methods:{
    getPage() {
            this.loading=true;
            this.pagination.queryString = JSON.parse(Cookie.get("userInformation")).employeeNumber;
            this.searchQuarantineInformation.pagination = this.pagination;
            getData(this.searchQuarantineInformation).then((res) => {
                this.loading=false;
                this.pagination.total = res.data.data.total;
                this.tableData = res.data.data.records;
            }).catch((res)=>this.loading=false)
        },
        deleteQuarantine(quarantine) {
            deleteData(quarantine).then((res) => {
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
                  this.insertData.startTime=this.insertData.collectDateRange[0];
                  this.insertData.endTime=this.insertData.collectDateRange[1];
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
        resetForm(formName) {
            this.$refs[formName].resetFields();

        },
        handleClose(done) {
            this.$confirm('确认关闭？')
                .then(_ => {
                    done();
                    this.resetForm("insertQuarantine");
                })
                .catch(_ => { });
        },
        handleFilterClose(done) {
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
<style lang="less" scoped>

</style>