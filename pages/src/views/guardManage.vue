<template>
    <div v-loading="loading">
        <!-- 新增表单 -->
        <el-dialog title="新增" :visible.sync="insertDialogVisible" width="50%" :before-close="handleClose">
            <el-form ref="insertGuard" :model="insertForm" :rules="rules" label-width="150px">
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="工号" prop="guardNumber">
                            <el-input v-model="insertForm.guardNumber" placeholder="请输入工号"></el-input>
                        </el-form-item>
                        <el-form-item label="身份证号" prop="idNumber">
                            <el-input v-model="insertForm.idNumber" placeholder="请输入身份证号"></el-input>
                        </el-form-item>
                        <el-form-item label="性别" prop="gender">
                            <el-radio-group v-model="insertForm.gender">
                                <el-radio label="0">男</el-radio>
                                <el-radio label="1">女</el-radio>
                            </el-radio-group>
                        </el-form-item><el-form-item prop="birthday" label="出生日期">
                            <el-date-picker type="date" placeholder="请选择出生日期" v-model="insertForm.birthday"
                                style="width: 100%;" format="yyyy-MM-dd" value-format="yyyy-MM-dd"></el-date-picker>
                        </el-form-item><el-form-item prop="politicsStatus" label="政治面貌">
                            <el-select v-model="insertForm.politicsStatus" filterable placeholder="请选择政治面貌">
                                <el-option v-for="item in  politicsStatusOptions" :key="item" :label="item" :value="item">
                                </el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item label="联系电话" prop="phone">
                            <el-input v-model="insertForm.phone" placeholder="请输入联系电话"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="姓名" prop="name">
                            <el-input v-model="insertForm.name" placeholder="请输入姓名"></el-input>
                        </el-form-item>
                        <el-form-item prop="education" label="学历">
                            <el-select v-model="insertForm.education" filterable placeholder="请选择学历">
                                <el-option v-for="item in  educationOptions" :key="item" :label="item" :value="item">
                                </el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item prop="position" label="职位">
                            <el-select v-model="insertForm.position" filterable placeholder="请选择职位">
                                <el-option v-for="item in  positionOptions" :key="item.value" :label="item.label"
                                    :value="item.value">
                                </el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item prop="guardStart" label="入职时间">
                            <el-date-picker type="date" placeholder="请选择入职时间" v-model="insertForm.guardStart"
                                style="width: 100%;" format="yyyy-MM-dd" value-format="yyyy-MM-dd"></el-date-picker>
                        </el-form-item>
                        <el-form-item label="电子邮箱" prop="email">
                            <el-input v-model="insertForm.email" placeholder="请输入电子邮箱"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
            </el-form>
            <span slot="footer">
                <el-button type="primary" @click="submitForm('insertGuard')">确定</el-button>
                <el-button @click="resetForm('insertGuard')">重置</el-button>
            </span>
        </el-dialog>
        <!-- 编辑对话框 -->
        <el-dialog title="修改" :visible.sync="editDialogVisible" width="50%" :before-close="handleClose">
            <el-form ref="editGuard" :model="editForm" :rules="rules" label-width="150px">
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="工号" prop="guardNumber">
                            <el-input disabled v-model="editForm.guardNumber" placeholder="请输入工号"></el-input>
                        </el-form-item>
                        <el-form-item label="身份证号" prop="idNumber">
                            <el-input disabled v-model="editForm.idNumber" placeholder="请输入身份证号"></el-input>
                        </el-form-item>
                        <el-form-item label="性别" prop="gender">
                            <el-radio-group v-model="editForm.gender">
                                <el-radio label="0">男</el-radio>
                                <el-radio label="1">女</el-radio>
                            </el-radio-group>
                        </el-form-item><el-form-item prop="birthday" label="出生日期">
                            <el-date-picker type="date" placeholder="请选择出生日期" v-model="editForm.birthday"
                                style="width: 100%;" format="yyyy-MM-dd" value-format="yyyy-MM-dd"></el-date-picker>
                        </el-form-item><el-form-item prop="politicsStatus" label="政治面貌">
                            <el-select v-model="editForm.politicsStatus" filterable placeholder="请选择政治面貌">
                                <el-option v-for="item in  politicsStatusOptions" :key="item" :label="item" :value="item">
                                </el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item label="联系电话" prop="phone">
                            <el-input v-model="editForm.phone" placeholder="请输入联系电话"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="姓名" prop="name">
                            <el-input v-model="editForm.name" placeholder="请输入姓名"></el-input>
                        </el-form-item>
                        <el-form-item prop="education" label="学历">
                            <el-select v-model="editForm.education" filterable placeholder="请选择学历">
                                <el-option v-for="item in  educationOptions" :key="item" :label="item" :value="item">
                                </el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item prop="position" label="职位">
                            <el-select v-model="editForm.position" filterable placeholder="请选择职位">
                                <el-option v-for="item in  positionOptions" :key="item.value" :label="item.label"
                                    :value="item.value">
                                </el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item prop="guardStart" label="入职时间">
                            <el-date-picker type="date" placeholder="请选择入职时间" v-model="editForm.guardStart"
                                style="width: 100%;" format="yyyy-MM-dd" value-format="yyyy-MM-dd"></el-date-picker>
                        </el-form-item>
                        <el-form-item label="电子邮箱" prop="email">
                            <el-input v-model="editForm.email" placeholder="请输入电子邮箱"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
            </el-form>
            <span slot="footer">
                <el-button type="primary" @click="editGuard('editGuard')">确定</el-button>
            </span>
        </el-dialog>
        <!-- 筛选对话框 -->
        <el-dialog title="筛选" :visible.sync="filterDialogVisible" width="50%" :before-close="handleFilterClose">
            <el-form ref="filterForm" :model="searchInformation">
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="工号" prop="guardNumber">
                            <el-input v-model="searchInformation.guardNumber" placeholder="请输入工号"
                                style="width:80%"></el-input>
                        </el-form-item>
                        <el-form-item label="身份证号" prop="idNumber">
                            <el-input v-model="searchInformation.idNumber" placeholder="请输入身份证号"
                                style="width:80%"></el-input>
                        </el-form-item><el-form-item label="性别" prop="gender">
                            <el-radio-group v-model="searchInformation.gender">
                                <el-radio label="0">男</el-radio>
                                <el-radio label="1">女</el-radio>
                            </el-radio-group>
                        </el-form-item>
                        <el-form-item prop="age">
                            <span class="demonstration">年龄范围</span>
                            <el-slider v-model="searchInformation.age" range style="width:80%">
                            </el-slider>
                        </el-form-item>

                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="姓名" prop="name">
                            <el-input v-model="searchInformation.name" placeholder="请输入姓名" style="width:80%"></el-input>
                        </el-form-item>
                        <el-form-item prop="politicsStatus" label="政治面貌">
                            <el-select v-model="searchInformation.politicsStatus" filterable multiple placeholder="请选择政治面貌">
                                <el-option v-for="item in  politicsStatusOptions" :key="item" :label="item" :value="item">
                                </el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item prop="position" label="职位">
                            <el-select v-model="searchInformation.position" filterable multiple placeholder="请选择职位">
                                <el-option v-for="item in  positionOptions" :key="item.value" :label="item.label"
                                    :value="item.value">
                                </el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item label="入职日期范围" prop="guardStart">
                            <el-date-picker type="daterange" placeholder="选择日期范围" v-model="searchInformation.guardStart"
                                range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期"
                                value-format="yyyy-MM-dd">
                            </el-date-picker>
                        </el-form-item>
                        <div style="text-align: center;">
                            <el-button @click="resetForm('filterForm')" type="primary">重置</el-button>
                        </div>
                    </el-col>
                </el-row>
            </el-form>
        </el-dialog>
        <!-- 新增居民按钮 -->
        <div class="insert">
            <el-button type="primary" @click="insertDialogVisible = true">新增保安</el-button>
        </div>
        <div class="filter">
            <el-button style="margin-right:20px" icon="el-icon-s-operation" size="mini"
                @click="filterDialogVisible = true">筛选</el-button>
        </div>
        <!-- 详细信息对话框 -->
        <el-dialog title="查看详情" :visible.sync="detailDialogVisable" width="50%">
            <el-descriptions class="margin-top" :column="2" :size="size" border>
                <el-descriptions-item v-for="item in dialogColumns" :key="item.prop">
                    <template slot="label">
                        {{ item.label }}
                    </template>
                    {{ detailInformation[item.prop] }}
                </el-descriptions-item>
            </el-descriptions>
            <template slot="footer">
                <el-button type="primary" @click="handleEditForm()">编辑</el-button>

                <el-popconfirm confirm-button-text='是' cancel-button-text='否' icon="el-icon-info" icon-color="orange"
                    :title="`是否要重置工号${this.detailInformation.guardNumber}的员工密码`" @confirm="resetGuard()">
                    <el-button type="warning" slot="reference">重置密码</el-button>
                </el-popconfirm>
                <el-popconfirm confirm-button-text='是' cancel-button-text='否' icon="el-icon-info" icon-color="red"
                    :title="`是否要删除工号${this.detailInformation.guardNumber}的员工信息`" @confirm="deleteGuard()">
                    <el-button type="danger" slot="reference">删除</el-button>
                </el-popconfirm>
            </template>
        </el-dialog>
        <!-- 管理员信息表格 -->
        <div class="Data">
            <el-table :data="tableData">
                <el-table-column v-for="item in tableColmuns" :key="item.prop" :prop="item.prop" :label="item.label"
                :width="item.prop === `idNumber` ? `200px` : ``">
                </el-table-column>
                <el-table-column label="操作">
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
import { getData, insertData, deleteData, resetPassword, editData } from "@/api/guard"
export default {
    data() {
        return {
            insertDialogVisible: false,
            editDialogVisible: false,
            filterDialogVisible: false,
            loading: false,
            detailDialogVisable: false,
            insertForm: {
                guardNumber: '',
                idNumer: '',
                gender: '',
                name: '',
                birthday: '',
                politicsStatus: '',
                education: '',
                position: '',
                guardStart: '',
                phone: '',
                email: '',
            },
            editForm: {},
            rules: {
                guardNumber: [
                    { required: true, message: '请输入工号', trigger: 'blur' }
                ],
                name: [
                    { required: true, message: '请输入姓名', trigger: 'blur' }
                ],
                idNumber: [
                    { required: true, message: '请输入身份证号', trigger: 'blur' },
                    { min: 18, max: 18, message: '长度为18位', trigger: 'blur' }
                ],
                position: [
                    { required: true, message: '请选择职位', trigger: 'change' },
                ],
                gender: [
                    { required: true, message: '请选择性别', trigger: 'change' },
                ],
                guardStart: [
                    { required: true, message: '请选择入职日期', trigger: 'change' },
                ],
                birthday: [
                    { required: true, message: '请选择出生日期', trigger: 'change' },
                ],
            },
            politicsStatusOptions: ["群众", "中共党员", "中共预备党员", "共青团员", "民革党员", "民盟盟员", "民建会员", "民进会员", "农工党党员", "致公党党员", "九三学社社员", "台盟盟员", "无党派人士"],
            educationOptions: ["小学", "初中", "高中（中职、技校）", "大专（高职）", "本科", "硕士研究生", "博士研究生"],
            tableColmuns: [{ "label": "工号", "prop": "guardNumber" }, { "label": "身份证号", "prop": "idNumber" }, { "label": "姓名", "prop": "name" }, { "label": "性别", "prop": "gender" }, { "label": "年龄", "prop": "age" }, { "label": "出生日期", "prop": "birthday" }
                , { "label": "入职时间", "prop": "employmentStart" }],
            dialogColumns: [{ "label": "工号", "prop": "guardNumber" }, { "label": "身份证号", "prop": "idNumber" }, { "label": "姓名", "prop": "name" }, { "label": "性别", "prop": "gender" }, { "label": "年龄", "prop": "age" }, { "label": "出生日期", "prop": "birthday" }
                , { "label": "政治面貌", "prop": "politicsStatus" }, { "label": "学历", "prop": "education" }, { "label": "职位", "prop": "position" }, { "label": "入职时间", "prop": "employmentStart" }
                , { "label": "联系电话", "prop": "phone" }, { "label": "电子邮箱", "prop": "email" }],

            positionOptions: [{ label: "保安队长", value: "4" }, { label: "保安", value: "5" }],
            tableData: [],
            pagination: {
                //分页相关属性
                currentPage: 1,
                pageSize: 15,
                total: null,
            },
            searchInformation: {
                pagination: null,
                guardNumber: "",
                idNumber: "",
                name: "",
                gender: "",
                age: [0, 100],
                politicsStatus: [],
                position: [],
                guardStart: [],
            },
            detailInformation: {},
        }
    },
    mounted() {
        this.getPage();
    },
    methods: {
        handleClose(done) {
            this.$confirm('确认关闭？')
                .then(_ => {
                    done();
                    this.resetForm(formName);
                })
                .catch(_ => { });
        },
        handleFilterClose(done) {
            this.getPage();
            done();
        },
        submitForm(formName) {
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    insertData(this.insertForm).then((res) => {
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
        deleteGuard() {
            deleteData(this.detailInformation.guardNumber).then((res) => {
                if (res.data.code == 200) {
                    this.successMessage("删除成功！")
                    this.getPage()
                    this.detailDialogVisable = false;
                }
                else {
                    this.errorMessage(res.data.message)
                }
            }).catch((res) => {
                this.errorMessage("删除失败")
            })
        },
        resetGuard() {
            resetPassword(this.detailInformation.guardNumber).then((res) => {
                if (res.data.code == 200) {
                    this.detailDialogVisable = false;
                    this.successMessage("重置成功！")
                    this.getPage()
                }
                else {
                    this.errorMessage(res.data.message)
                }
            }).catch((res) => {
                this.errorMessage("重置失败")
            })
        },
        handleEditForm() {
            this.editForm = { ...this.detailInformation };
            this.editForm.gender = this.editForm.gender === "男" ? "0" : "1";
            this.editForm.position = this.positionOptions.find(option => option.label === this.editForm.position).value;
            this.editDialogVisible = true;
            this.detailDialogVisable = false;
        },
        editGuard(formName) {
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    editData(this.editForm).then((res) => {
                        if (res.data.code == 200) {
                            this.successMessage("修改成功！")
                            this.editDialogVisible = false;
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
        getPage() {
            this.loading = true;
            this.searchInformation.pagination = this.pagination;
            getData(this.searchInformation).then((res) => {
                this.loading = false
                this.pagination.total = res.data.data.total;
                this.tableData = res.data.data.records;
            }).catch((res) => this.loading = false)
        },
        handleDetail(row){
            this.detailDialogVisable=true;
            this.detailInformation={...row}
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