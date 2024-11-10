<template>
       <div v-loading="loading">
        <el-dialog title="访问详细记录" :visible.sync="visitDetailDialogVisible" width="50%">
            <el-descriptions class="margin-top" :column="2" :size="size" border>
                <el-descriptions-item v-for="item in dialogVisitDetailColumns" :key="item.prop">
                    <template slot="label">
                        {{ item.label }}
                    </template>
                    {{ visitDetailInformation[item.prop] }}
                </el-descriptions-item>
            </el-descriptions>
            <span slot="footer">
        <el-button v-if="visitDetailInformation.state === `未离开`" type="primary" @click="confirmLeaveDialogVisable = true">确认离开</el-button>
        <el-button  type="danger" @click="confirmDeleteDialogVisable = true" v-if="deletePrivilege">删除</el-button>
    </span>
        </el-dialog>
        <el-dialog title="提示" :visible.sync="confirmLeaveDialogVisable" width="30%">
        <span>是否确认访客{{ visitDetailInformation.visitName }}已离开</span>
        <span slot="footer" class="dialog-footer">
          <el-button @click="confirmLeaveDialogVisable = false">取 消</el-button>
          <el-button type="primary" @click="confirmLeave()">确 定</el-button>
        </span>
      </el-dialog>
      <el-dialog  :visible.sync="confirmDeleteDialogVisable" width="30%">
        <template slot="title">
                <div style="text-align: center; font-size:20px;">
                    <i class="el-icon-warning" style="color:red;"></i>警告
                </div>
            </template>
        <span>是否删除访客{{ visitDetailInformation.visitName }}此次访问记录</span>
        <span slot="footer" class="dialog-footer">
          <el-button @click="confirmDeleteDialogVisable = false">取 消</el-button>
          <el-button type="primary" @click="confirmDelete()">确 定</el-button>
        </span>
      </el-dialog>
        <el-dialog title="访客信息" :visible.sync="visitorDetailDialogVisible" width="50%">
            <el-descriptions class="margin-top" :column="2" :size="size" border>
                <el-descriptions-item v-for="item in dialogVisitorDetailColumns" :key="item.prop">
                    <template slot="label">
                        {{ item.label }}
                    </template>
                    {{ visitorDetailInformation[item.prop] }}
                </el-descriptions-item>
            </el-descriptions>
            <el-descriptions class="margin-top" :column="2" :size="size" border>
                <el-descriptions-item>
                    <template slot="label">
                       访客出入记录
                    </template>
                    <span v-html="records"></span>
                </el-descriptions-item>
            </el-descriptions>
        </el-dialog>
        <el-dialog title="新增记录" :visible.sync="insertDialogVisible" width="30%" :before-close="handleClose">
            <el-form ref="insertVisit" :model="insertData" :rules="rules" >
                <el-form-item label="访客身份证号" prop="visitNumber">
                            <el-input v-model="insertData.visitNumber" placeholder="请输入访客身份证号" style="width:auto"></el-input>
                        </el-form-item>
                        <el-form-item label="访客姓名" prop="visitName">
                            <el-input v-model="insertData.visitName" placeholder="请输入访客姓名" style="width:auto"></el-input>
                        </el-form-item>
                        <el-form-item label="访客性别" prop="gender">
                            <el-radio-group v-model="insertData.gender">
                                <el-radio label="0">男</el-radio>
                                <el-radio label="1">女</el-radio>
                            </el-radio-group>
                        </el-form-item>
                        <el-form-item label="访客联系电话" prop="phone">
                            <el-input v-model="insertData.phone" placeholder="请输入联系电话" style="width:auto"></el-input>
                        </el-form-item>
                <el-form-item label="居民身份证号" prop="idNumber">
                    <el-input v-model="insertData.idNumber" disabled style="width:auto"></el-input>
                </el-form-item>
                <el-form-item prop="idNumber" label="居民姓名">
                    <el-select v-model="insertData.idNumber" filterable placeholder="请选择居民" style="width:auto">
                        <el-option v-for="item in residents" :key="item.idNumber" :label="item.name" :value="item.idNumber">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item prop="reason" label="访问原因">
                    <el-input v-model="insertData.reason" style="width:75%"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="submitForm('insertVisit')">确定</el-button>
                    <el-button @click="resetForm('insertVisit')">重置</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>
        <el-dialog title="筛选记录" :visible.sync="filterDialogVisible" width="30%" :before-close="handleFilterClose">
            <el-form ref="filterForm" :model="searchInformation">
                <el-form-item prop="visitNumber" label="访客身份证号">
                    <el-input v-model="searchInformation.visitNumber" style="width: 50%;" placeholder="请输入访客身份证号"></el-input>
                </el-form-item>
                <el-form-item prop="visitName" label="访客姓名">
                    <el-input v-model="searchInformation.visitName" style="width: 50%;" placeholder="请输入访客姓名"></el-input>
                </el-form-item>
                <el-form-item prop="idNumber" label="居民身份证号">
                    <el-input v-model="searchInformation.idNumber" style="width: 50%;" placeholder="请输入居民身份证号"></el-input>
                </el-form-item>
                <el-form-item prop="residentName" label="居民姓名">
                    <el-input v-model="searchInformation.residentName" style="width: 50%;" placeholder="请输入居民姓名"></el-input>
                </el-form-item>
                <el-form-item label="进入小区时间范围" prop="entryTime">
                    <el-date-picker type="datetimerange" placeholder="选择日期范围" v-model="searchInformation.entryTime"
                        range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期"
                        value-format="yyyy-MM-dd HH:mm:ss">
                    </el-date-picker>
                </el-form-item>
                <el-form-item label="离开小区时间范围" prop="leaveTime">
                    <el-date-picker type="datetimerange" placeholder="选择日期范围" v-model="searchInformation.leaveTime"
                        range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期"
                        value-format="yyyy-MM-dd HH:mm:ss">
                    </el-date-picker>
                </el-form-item>
                <el-form-item prop="state" label="状态">
                    <el-select v-model="searchInformation.state" filterable placeholder="请选择当前状态">
                        <el-option v-for="item in stateOptions" :key="item.value" :label="item.label" :value="item.value">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item prop="guardNumber" label="批准保安工号" >
                    <el-input v-model="searchInformation.guardNumber" style="width: 50%;" placeholder="请输入批准保安工号"></el-input>
                </el-form-item>
                <el-form-item prop="guardName" label="批准保安姓名">
                    <el-input v-model="searchInformation.guardName" style="width: 50%;" placeholder="请输入批准保安姓名"></el-input>
                </el-form-item>
                <div style="text-align: center;">
                    <el-button @click="resetForm('filterForm')" type="primary">重置</el-button>
                </div>
            </el-form>
        </el-dialog>
        <div class="Data">
            <div class="insert" v-if="insertPrivilege">
                <el-button type="primary" @click="insertDialogVisible = true">新增访问</el-button>
            </div>
            <div class="filter">
                <el-button style="margin-right:20px" icon="el-icon-s-operation" size="mini"
                    @click="filterDialogVisible = true">筛选</el-button>
            </div>
            <el-table :data="tableData" @row-click="seeVisitorDetail" style="cursor: pointer;">
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
import { getData, insertData, updateData,deleteData,getRecords } from '@/api/visit';
import Cookie from 'js-cookie'
import { getList } from '@/api/resident';
export default{
    data(){
        return{
            filterDialogVisible: false,
            insertDialogVisible: false,
            visitDetailDialogVisible:false,
            visitorDetailDialogVisible:false,
            confirmLeaveDialogVisable:false,
            confirmDeleteDialogVisable:false,
            loading: false,
            tableData: [],
            residents: [],
            insertData: {
                visitNumber:"",
                visitName:"",
                gender:"",
                phone:"",
                idNumber:"",
                reason:"",
            },
            rules: {
                visitNumber: [
                    { required: true, message: '请输入身份证号', trigger: 'blur' },
                    { min: 18, max: 18, message: '长度为18位', trigger: 'blur' }
                ],
                visitName: [
                    { required: true, message: '请输入姓名', trigger: 'blur' },
                ],
                gender: [
                    { required: true, message: '请选择性别', trigger: 'change' },
                ],
                phone: [
                    { required: true, message: '请输入联系电话', trigger: 'blur' },
                ],
                idNumber: [
                    { required: true, message: '请选择居民', trigger: 'change' },
                ],
                reason: [
                    { required: true, message: '请输入访问原因', trigger: 'blur' },
                ],
            },
            pagination: {
                //分页相关属性
                currentPage: 1,
                pageSize: 15,
                total: null,
                queryString: "",
            },
            searchInformation: {
                pagination: null,
                visitNumber:"",
                visitName:"",
                idNumber:"",
                residentName:"",
                guardNumber:"",
                guardName:"",
                state:"",
                entryTime:[],
                leaveTime:[],
            },
            tableColmuns: [{ label: "访客身份证号", prop: "visitNumber" },{ label: "访客", prop: "visitName" },
            { label: "居民", prop: "residentName" },{ label: "居民地址", prop: "buildingRomNumber" },
            { label: "进入时间", prop: "entryTime" },{ label: "状态", prop: "state" }
        ],
        dialogVisitDetailColumns: [{ label: "访客身份证号", prop: "visitNumber" },{ label: "访客", prop: "visitName" },
        { label: "居民", prop: "idNumber" },{ label: "访问居民", prop: "residentName" },
            { label: "居民地址", prop: "buildingRomNumber" },{ label: "当前状态", prop: "state" },
        { label: "进入时间", prop: "entryTime" },{ label: "离开时间", prop: "leaveTime" },
        { label: "访问总时间（分钟）", prop: "visitTotalTime" },{ label: "访问原因", prop: "reason" },
        { label: "进小区批准保安工号", prop: "inGuardNumber" },{ label: "进小区批准保安", prop: "inGuardName" },
        { label: "离开小区批准保安工号", prop: "outGuardNumber" },{ label: "离开小区批准保安", prop: "outGuardName" },
        ],
        dialogVisitorDetailColumns:
         [{ label: "身份证号", prop: "visitNumber" },{ label: "姓名", prop: "visitName" },
         { label: "性别", prop: "gender" },{ label: "联系电话", prop: "phone" },
        ],
            stateOptions: [{ label: "未离开", value: "0" }, { label: "已离开", value: "1" }],
            visitDetailInformation:{},
            visitorDetailInformation:{},
            records:""
        }
    },
    mounted(){
        this.getPage();
        getList("guard").then((res) => {
            this.residents = res.data.data;
        })
    },
    methods:{
        getPage() {
            this.loading = true;
            this.pagination.queryString = JSON.parse(Cookie.get("userInformation")).employeeNumber;
            this.searchInformation.pagination = this.pagination;
            getData(this.searchInformation).then((res) => {
                this.loading = false;
                this.pagination.total = res.data.data.total;
                this.tableData = res.data.data.records;

            }).catch((res) => this.loading = false)
        },
        submitForm(formName) {
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    this.insertData.inGuardNumber = JSON.parse(Cookie.get("userInformation")).guardNumber;
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
        confirmDelete(){
            deleteData(this.visitDetailInformation).then((res) => {
                if (res.data.code == 200) {
                    this.successMessage("删除成功！")
                    this.getPage()
                    this.confirmDeleteDialogVisable=false;
                    this.visitDetailDialogVisible=false;
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
        confirmLeave() {
            this.visitDetailInformation.outGuardNumber = JSON.parse(Cookie.get("userInformation")).guardNumber;

            updateData(this.visitDetailInformation).then((res) => {
                if (res.data.code == 200) {
                    this.successMessage("处理成功！")
                    this.confirmLeaveDialogVisable=false;
                    this.visitDetailDialogVisible=false;
                    this.getPage()
                }
                else {
                    this.errorMessage(res.data.message)
                    this.confirmLeaveDialogVisable=false;
                }
            }).catch((res) => {
                this.errorMessage("处理失败！")
                this.confirmLeaveDialogVisable=false;
            })
        },
        handleDetail(row) {
            this.visitDetailDialogVisible = true;
            this.visitDetailInformation = { ...row };
        },
        seeVisitorDetail(row, column, event){
            if (column.label !== '操作'){
                this.visitorDetailInformation = { ...row }
                this.loading=true;
                getRecords(row.visitNumber).then((res)=>{
                    this.records=res.data.data;
                    this.loading=false;
                    this.visitorDetailDialogVisible = true;
                })
            }
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
        resetForm(formName) {
            this.$refs[formName].resetFields();
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