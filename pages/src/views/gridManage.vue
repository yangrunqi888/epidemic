<template>
    <div v-loading="loading">
        <el-dialog title="新增网格" :visible.sync="insertDialogVisible" width="50%" :before-close="handleClose">
            <el-form ref="insertForm" :model="insertData" :rules="rules" label-width="120px">
                <el-form-item label="网格号" prop="gridNumber">
                    <el-input v-model="insertData.gridNumber" placeholder="请输入网格号"></el-input>
                </el-form-item>
                <el-form-item label="网格面积" prop="area">
                    <el-input type="number" v-model="insertData.area" placeholder="请输入网格面积"></el-input>
                </el-form-item>
                <el-form-item label="网格区域" prop="region">
                    <el-input v-model="insertData.region" placeholder="请输入网格区域"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="submitForm('insertForm')">确定</el-button>
                    <el-button @click="resetForm('insertForm')">重置</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>
        <el-dialog title="编辑网格" :visible.sync="editDialogVisible" width="50%" :before-close="handleClose">
            <el-form ref="insertForm" :model="editForm" :rules="rules" label-width="120px">
                <el-form-item label="网格号" prop="gridNumber">
                    <el-input disabled v-model="editForm.gridNumber" placeholder="请输入网格号"></el-input>
                </el-form-item>
                <el-form-item label="网格面积" prop="area">
                    <el-input type="number" v-model="editForm.area" placeholder="请输入网格面积"></el-input>
                </el-form-item>
                <el-form-item label="网格区域" prop="region">
                    <el-input v-model="editForm.region" placeholder="请输入网格区域"></el-input>
                </el-form-item>
                <el-form-item label="网格成员" prop="region">
                    <el-transfer v-model="editForm.staffNumbers" :data="staffs" :filterable="true"
                        filter-placeholder="请输入搜索内容" :props="{ label: 'name', key: 'employeeNumber' }"
                        :titles="['可选择成员', '已有成员']">
                    </el-transfer>
                </el-form-item>
                <el-form-item prop="staffs" label="网格长">
                    <el-select v-model="editForm.leadership" filterable placeholder="请选择网格长">
                        <el-option v-for="item in editForm.staffs" :key="item.employeeNumber" :label="item.name"
                            :value="item.employeeNumber">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="editData('insertForm')">确定</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>
        <!-- 筛选对话框 -->
        <el-dialog title="筛选" :visible.sync="filterDialogVisible" width="30%" :before-close="handleFilterClose">
            <el-form ref="filterForm" :model="searchInformation">
                <el-form-item label="网格号" prop="gridNumber">
                    <el-input v-model="searchInformation.gridNumber" placeholder="请输入网格号"></el-input>
                </el-form-item>
                <el-form-item label="网格长工号" prop="leadership">
                    <el-input v-model="searchInformation.leadership" placeholder="请输入网格长工号"></el-input>
                </el-form-item>
                <el-form-item label="网格长姓名" prop="leadershipName">
                    <el-input v-model="searchInformation.leadershipName" placeholder="请输入网格长姓名"></el-input>
                </el-form-item>
                <el-form-item prop="staffs" label="网格成员">
                    <el-select v-model="searchInformation.staffs" multiple filterable placeholder="请选择网格成员">
                        <el-option v-for="item in staffs" :key="item.employeeNumber" :label="item.name"
                            :value="item.employeeNumber">
                        </el-option>
                    </el-select>
                </el-form-item>
                <div style="text-align: center;">
                    <el-button @click="resetForm('filterForm')" type="primary">重置</el-button>
                </div>
            </el-form>
        </el-dialog>
        <div class="insert">
            <el-button type="primary" @click="insertDialogVisible = true">新增网格</el-button>
        </div>
        <div class="filter">
            <el-button style="margin-right:20px" icon="el-icon-s-operation" size="mini"
                @click="filterDialogVisible = true">筛选</el-button>
        </div>
        <!-- 信息表格 -->
        <div class="Data">
            <el-table :data="tableData">
                <el-table-column v-for="item in tableColmuns" :key="item.prop" :prop="item.prop" :label="item.label">
                </el-table-column>
                <el-table-column label="网格成员">
                    <template slot-scope="scope">
                        <el-popover placement="right" width="200" trigger="hover">
                            <el-table :data="scope.row.staffs">
                                <el-table-column property="employeeNumber" label="工号"></el-table-column>
                                <el-table-column property="name" label="姓名"></el-table-column>
                            </el-table>
                            <el-button slot="reference">查看</el-button>
                        </el-popover>
                    </template>
                </el-table-column>
                <el-table-column label="操作" fixed="right" width="300px">
                    <template slot-scope="scope">
                        <el-button type="primary" @click="handleEditForm(scope.row)">编辑</el-button>

                        <el-popconfirm confirm-button-text='是' cancel-button-text='否' icon="el-icon-info" icon-color="red"
                            :title="`是否要删除网格${scope.row.idNumber}的信息`" @confirm="deleteGrid(scope.row.gridNumber)">
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
        </div>
    </div>
</template>
<script>
import { getData, insertData, updatetData,deleteData } from '@/api/grid'
import { selectList } from '@/api/staff'
export default {
    data() {
        return {
            insertDialogVisible: false,
            editDialogVisible: false,
            filterDialogVisible: false,
            loading:false,
            insertData: {
                gridNumber: "",
                area: "",
                region: "",
            },
            staffs: [],
            tableData: [],
            editForm: {},
            tableColmuns: [{ "label": "网格号", "prop": "gridNumber" }, { "label": "网格面积", "prop": "area" }, { "label": "网格区域", "prop": "region" }, { "label": "网格长", "prop": "leadershipName" }],
            pagination: {
                //分页相关属性
                currentPage: 1,
                pageSize: 15,
                total: null,
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
                gridNumber: "",
                leadership: "",
                leadershipName: "",
                staffs: []
            },
            rules: {
                gridNumber: [
                    { required: true, message: '请输入网格号', trigger: 'blur' }
                ],
            }
        }
    },
    mounted() {
        this.getPage()
        selectList().then((res) => {
            this.staffs = res.data.data.filter(item => item.position == 1 || item.position == 2);
        })
    },
    methods: {
        getPage() {
            this.loading=true;
            this.searchInformation.pagination = this.pagination;
            getData(this.searchInformation).then((res) => {
                this.loading=false;
                this.pagination.total = res.data.data.total;
                this.tableData = res.data.data.records;
                for (let i = 0; i < this.tableData.length; i++) {
                    this.tableData[i].staffNumbers = []
                    for (let j = 0; j < this.tableData[i].staffs.length; j++) {
                        this.tableData[i].staffNumbers.push(this.tableData[i].staffs[j].employeeNumber);
                    }
                }
            }).catch((res)=>this.loading=false)
        },
        deleteGrid(id) {
            deleteData(id).then((res) => {
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
        },
        resetForm(formName) {
            this.$refs[formName].resetFields();
        },
        editData(formName) {
            updatetData(this.editForm).then((res) => {
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

        },
        handleEditForm(grid) {
            this.editDialogVisible = true;
            this.editForm = { ...grid };
            // getlivingList(this.editForm.buildingNumber).then((res) => {
            //     this.buildingLiving = res.data.data;
            // })
        },
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
    watch: {
  'editForm.staffNumbers': {
    deep: true,
    handler: function(newVal, oldVal) {
      if (newVal.length !== oldVal.length) {
        this.editForm.staffs=[];
        for(var i=0;i<newVal.length;i++){
        this.editForm.staffs.push(this.staffs.filter(item => item.employeeNumber ===newVal[i] )[0])
    }
      }
    }
  }
}
}
</script>