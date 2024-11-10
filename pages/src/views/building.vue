<template>
    <div v-loading="loading">
        <div>
            <el-dialog title="新增楼房" :visible.sync="insertDialogVisible" width="50%" :before-close="handleClose">
                <el-form ref="insertBuilding" :model="insertBuildingData" :rules="rules" label-width="120px">
                    <el-form-item label="楼栋号" prop="buildingNumber">
                        <el-input v-model="insertBuildingData.buildingNumber" placeholder="请输入楼栋号"></el-input>
                    </el-form-item>
                    <el-form-item prop="perFloorRoomNumber" label="每层楼房间数">
                        <el-slider v-model="insertBuildingData.perFloorRoomNumber" :step="1" :max="4" show-stops show-input>
                        </el-slider>
                    </el-form-item>
                    <el-form-item prop="floorNumber" label="总层数">
                        <el-slider v-model="insertBuildingData.floorNumber" :step="1" :max="30" show-stops show-input>
                        </el-slider>
                    </el-form-item>
                    <el-form-item prop="gridNumber" label="所属网格">
                        <el-select v-model="insertBuildingData.gridNumber" filterable placeholder="请选择所属网格">
                            <el-option v-for="item in grids" :key="item" :label="item" :value="item">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" @click="submitForm('insertBuilding')">确定</el-button>
                        <el-button @click="resetForm('insertBuilding')">重置</el-button>
                    </el-form-item>
                </el-form>
            </el-dialog>
        </div>
        <div>
            <el-dialog title="编辑楼房" :visible.sync="editDialogVisible" width="50%" :before-close="handleClose">
                <el-form ref="editBuilding" :rules="rules" :model="editForm" label-width="120px">
                    <el-form-item label="楼栋号" prop="buildingNumber">
                        <el-input v-model="editForm.buildingNumber" :disabled="true"></el-input>
                    </el-form-item>
                    <el-form-item prop="perFloorRoomNumber" label="每层楼房间数">
                        <el-slider v-model="editForm.perFloorRoomNumber" :step="1" :max="4" show-stops show-input>
                        </el-slider>
                    </el-form-item>
                    <el-form-item prop="floorNumber" label="总层数">
                        <el-slider v-model="editForm.floorNumber" :step="1" :max="30" show-stops show-input>
                        </el-slider>
                    </el-form-item>
                    <el-form-item prop="gridNumber" label="所属网格">
                        <el-select v-model="editForm.gridNumber" filterable placeholder="请选择所属网格">
                            <el-option v-for="item in grids" :key="item" :label="item" :value="item">
                            </el-option>

                        </el-select>
                    </el-form-item>
                    <el-form-item prop="headerId" label="楼栋长">
                        <el-select v-model="editForm.headerId" filterable placeholder="请选择楼栋长">
                            <el-option v-for="item in buildingLiving" :key="item.idNumber" :label="item.name"
                                :value="item.idNumber">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" @click="edit('editBuilding')">确定</el-button>

                    </el-form-item>
                </el-form>
            </el-dialog>

            <!-- 筛选对话框 -->
            <el-dialog title="筛选居民" :visible.sync="filterDialogVisible" width="50%" :before-close="handleFilterClose">
                <el-form ref="filterForm" :model="searchBuildingInformation">
                    <el-form-item label="楼栋号" prop="buildingNumber">
                        <el-input v-model="searchBuildingInformation.buildingNumber" placeholder="请输入楼栋号"></el-input>
                    </el-form-item>
                    <el-form-item prop="grid" label="所属网格">
                        <el-select v-model="searchBuildingInformation.grid" multiple filterable placeholder="请选择所属网格">
                            <el-option v-for="item in grids" :key="item" :label="item" :value="item">
                            </el-option>

                        </el-select>
                    </el-form-item>
                    <div style="text-align: center;">
                        <el-button @click="resetForm('filterForm')" type="primary">重置</el-button>
                    </div>
                </el-form>
            </el-dialog>
        </div>
        <div class="insert">
            <el-button type="primary" @click="insertDialogVisible = true">新增楼房</el-button>
        </div>
        <div class="filter">
            <el-button style="margin-right:20px" icon="el-icon-s-operation" size="mini"
                @click="filterDialogVisible = true">筛选</el-button>
        </div>
        <div class="buildingData">
            <el-table :data="tableData">
                <el-table-column v-for="item in tableColmuns" :key="item.prop" :prop="item.prop" :label="item.label">
                </el-table-column>
                <el-table-column label="操作" fixed="right" width="300px">
                    <template slot-scope="scope">
                        <el-button type="primary" @click="handleEditForm(scope.row)">编辑</el-button>
                        <el-popconfirm confirm-button-text='是' cancel-button-text='否' icon="el-icon-info" icon-color="red"
                            :title="`是否要删除楼栋号为${scope.row.buildingNumber}的楼栋信息`"
                            @confirm="deleteBuilding(scope.row.buildingNumber)">
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
import { getData, deleteData, insertData, editBuilding } from '@/api/building';
import { getList } from '@/api/grid'
import { getlivingList } from '@/api/live'
import Cookie from 'js-cookie'
export default {
    data() {
        return {
            insertDialogVisible: false,
            editDialogVisible: false,
            filterDialogVisible: false,
            loading:false,
            insertBuildingData: {
                buildingNumber: "",
                perFloorRoomNumber: 0,
                floorNumber: 0,
                gridNumber: ""
            },
            editForm: {},
            tableData: [],
            tableColmuns: [{ "label": "楼栋号", "prop": "buildingNumber" }, { "label": "每层楼房间数", "prop": "perFloorRoomNumber" }, { "label": "总层数", "prop": "floorNumber" }, { "label": "总居住人数", "prop": "totalPerson" }, { "label": "楼栋长", "prop": "headerName" }
                , { "label": "楼栋长房间号", "prop": "headerRoom" }, { "label": "所属网格", "prop": "gridNumber" },],
            pagination: {
                //分页相关属性
                currentPage: 1,
                pageSize: 15,
                total: null,
                queryString: "",
            },
            searchBuildingInformation: {
                pagination: null,
                buildingNumber: "",
                grid: []
            },
            grids: [],
            buildingLiving: [],
            rules: {
                buildingNumber: [
                    { required: true, message: '请输入楼栋号', trigger: 'blur' }
                ],
                perFloorRoomNumber: [
                    {
                        required: true,
                        message: '请选择每层楼房间数',
                        trigger: 'blur'
                    },
                    {
                        validator: (rule, value, callback) => {
                            if (value <= 0) {
                                callback(new Error('每层楼房间数必须大于0'))
                            } else {
                                callback()
                            }
                        },
                        trigger: 'blur'
                    }
                ],
                floorNumber: [
                    {
                        required: true,
                        message: '请选择总楼层数',
                        trigger: 'blur'
                    },
                    {
                        validator: (rule, value, callback) => {
                            if (value <= 0) {
                                callback(new Error('楼层数必须大于0'))
                            } else {
                                callback()
                            }
                        },
                        trigger: 'blur'
                    }
                ],
                gridNumber: [
                    { required: true, message: '请选择所属网格', trigger: 'change' },
                ],
               
            },
        }
    },
    mounted() {
        this.getPage();
        getList(this.pagination.queryString).then((res) => {
            this.grids=res.data.data

        })//获取管理区域
    },
    methods: {
        handleFilterClose(done) {
            this.getPage();
            done();
        },
        deleteBuilding(id) {
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
        edit(formName) {
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    editBuilding(this.editForm).then((res) => {
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
            this.loading=true;
            this.pagination.queryString = JSON.parse(Cookie.get("userInformation")).employeeNumber;
            this.searchBuildingInformation.pagination = this.pagination;
            getData(this.searchBuildingInformation).then((res) => {
                this.loading=false;
                this.pagination.total = res.data.data.total;
                this.tableData = res.data.data.records;
            }).catch((res)=>this.loading=false)
        },
        handleEditForm(building) {
            this.editDialogVisible = true;
            this.editForm = { ...building };
            this.editForm.perFloorRoomNumber = parseInt(this.editForm.perFloorRoomNumber);
            this.editForm.floorNumber = parseInt(this.editForm.floorNumber);
            getlivingList(this.editForm.buildingNumber).then((res) => {
                this.buildingLiving = res.data.data;
            })
        },
        submitForm(formName) {
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    insertData(this.insertBuildingData).then((res) => {
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

        }, handleClose(done) {
            this.$confirm('确认关闭？')
                .then(_ => {
                    done();
                    this.resetForm("insertBuilding");
                })
                .catch(_ => { });
        }, handleSizeChange(val) {
            this.pagination.pageSize = val;
            this.getPage()
        },
        handleCurrentChange(val) {
            this.pagination.currentPage = val;
            this.getPage()
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