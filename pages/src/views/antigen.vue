<template>
    <div  v-loading="loading">
         <!-- 筛选对话框 -->
         <el-dialog title="筛选记录" :visible.sync="filterDialogVisible" width="30%" :before-close="handleFilterClose">
            <el-form ref="filterForm" :model="searchAntigenInformation" >
                <el-form-item prop="idNumber" label="身份证号"  >
                    <el-input v-model="searchAntigenInformation.idNumber" style="width: 50%;"></el-input>
                </el-form-item>
                <el-form-item prop="name" label="姓名">
                    <el-input v-model="searchAntigenInformation.name" style="width: 50%;"></el-input>
                </el-form-item>

                <el-form-item label="日期范围" prop="collectDateRange">
                    <el-date-picker type="daterange" placeholder="选择日期范围" v-model="searchAntigenInformation.collectDateRange"
                        range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期" value-format="yyyy-MM-dd">
                    </el-date-picker>
                </el-form-item>
                <el-form-item prop="result" label="抗原结果">
                    <el-select v-model="searchAntigenInformation.result" filterable multiple placeholder="请选择核酸类型">
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
            <el-dialog :visible.sync="imageVisible" title="抗原截图">
                <div style="text-align: center;">
                    <img :src="antigenPath" />
                </div>
            </el-dialog>
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
                <el-table-column label="抗原截图">
                    <template slot-scope="scope">
                        <el-button round :disabled="scope.row.picturePath == null"
                            @click="seePicutre(scope.row)">查看</el-button>
                    </template>
                </el-table-column>
                <el-table-column label="操作" fixed="right" width="300px">
                    <template slot-scope="scope">
                        <el-popconfirm confirm-button-text='是' cancel-button-text='否' icon="el-icon-info" icon-color="red"
                            :title="`是否要删除此条记录`" @confirm="deleteAntigen(scope.row)">
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
import { getData, deleteData, getAntigenPicture } from '@/api/antigen';
import { Loading } from 'element-ui';
import Cookie from 'js-cookie'
export default{
    data(){
        return{
            filterDialogVisible: false,
            imageVisible: false,
            loading:false,
            tableData: [],
            antigenPath: "",
            resultOptions: [{ "value": "0", "label": "阳性" }, { "value": "1", "label": "阴性" }],
            pagination: {
                //分页相关属性
                currentPage: 1,
                pageSize: 15,
                total: null,
                queryString: "",
            },
            searchAntigenInformation: {
                pagination: null,
                idNumber: "",
                name: "",
                collectStartTime: "",
                collectEndTime: "",
                collectDateRange: [],
                result: [],


            }, tableColmuns: [{ "label": "身份证号", "prop": "idNumber" }, { "label": "姓名", "prop": "name" }, { "label": "采样日期", "prop": "collectTime" },  { "label": "抗原结果", "prop": "result" }
            ],
        }
    },
    mounted(){
        this.getPage();
    },
    methods:{
        getPage() {
            this.loading=true;
            this.pagination.queryString = JSON.parse(Cookie.get("userInformation")).employeeNumber;
            this.searchAntigenInformation.pagination = this.pagination;
            getData(this.searchAntigenInformation).then((res) => {
                this.loading=false
                this.pagination.total = res.data.data.total;
                this.tableData = res.data.data.records;
            }).catch((res)=>this.loading=false)
        },
        deleteAntigen(acid) {
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
       resetForm(formName) {
            this.$refs[formName].resetFields();

        },
        seePicutre(antigen) {
            this.loading=true;
            getAntigenPicture(antigen).then((res) => {
                this.antigenPath = window.URL.createObjectURL(res.data);
                this.loading=false;
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
                    this.resetForm(formName);
                })
                .catch(_ => { });
        },
        handleFilterClose(done) {
            this.searchAntigenInformation.collectStartTime = this.searchAntigenInformation.collectDateRange[0];
            this.searchAntigenInformation.collectEndTime = this.searchAntigenInformation.collectDateRange[1];
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