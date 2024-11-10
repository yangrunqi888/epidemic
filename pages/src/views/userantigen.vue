<template>
    <div v-loading="loading">
        <!-- 上传对话框 -->
        <el-dialog title="上传信息" :visible.sync="uploadInformationDialogVisable" width="30%" :before-close="handleClose">
            <el-form ref="uploadInformation" :model="uploadInformation" label-position="top" :rules="rules"
                label-width="150px">
                <el-form-item prop="collectTime" label="抗原日期">
                        <el-date-picker type="date" placeholder="请选择抗原日期" v-model="uploadInformation.collectTime"
                            value-format="yyyy-MM-dd"></el-date-picker>
                    </el-form-item>
                <el-form-item label="抗原照片" required>
                    <el-upload class="avatar-uploader" drag ref="upload" :show-file-list="false" action=""
                        :before-upload="beforeUpload">
                        <div v-show="previewUrl !== ``" style="width: 100%;height: 100%; vertical-align:middle ;">
                            <img :src="previewUrl" :style="{ width: `60%`, height: `100%` }" />
                        </div>
                        <div v-show="previewUrl === ``">
                            <i class="el-icon-upload"></i>

                            <div class="el-upload__text">将抗原照片拖到此处，或<em>点击上传</em></div>
                        </div>
                        <div class="el-upload__tip" slot="tip">只能上传jpg文件</div>
                    </el-upload>
                </el-form-item>
                <el-form-item prop="result" label="抗原结果">
                    <el-select v-model="uploadInformation.result" filterable placeholder="请选择核酸结果">
                        <el-option v-for="item in resultOptions" :key="item.value" :label="item.label" :value="item.value">
                        </el-option>
                    </el-select>
                </el-form-item>
            </el-form>
            <template slot="footer">
                <el-button type="primary" @click="submitForm('uploadInformation')">上传</el-button>
            </template>
        </el-dialog>
        <!-- 筛选对话框 -->
        <el-dialog title="筛选记录" :visible.sync="filterDialogVisible" width="30%" :before-close="handleFilterClose">
            <el-form ref="filterForm" :model="searchAntigenInformation">

                <el-form-item label="日期范围" prop="collectDateRange">
                    <el-date-picker type="daterange" placeholder="选择日期范围"
                        v-model="searchAntigenInformation.collectDateRange" range-separator="-" start-placeholder="开始日期"
                        end-placeholder="结束日期" value-format="yyyy-MM-dd">
                    </el-date-picker>
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
        <div class="insert">
            <el-button type="primary" @click="uploadInformationDialogVisable = true">抗原上报</el-button>
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
                        <el-button round @click="seePicutre(scope.row)">查看</el-button>
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
import { getData, deleteData, getAntigenPicture,insertData } from '@/api/antigen';
import Cookie from 'js-cookie'
export default {
    data() {
        return {
            filterDialogVisible: false,
            imageVisible: false,
            uploadInformationDialogVisable:false,
            loading: false,
            previewUrl:"",
            antigenPicture:"",
            tableData: [],
            antigenPath: "",
            resultOptions: [ { "value": "1", "label": "阴性" },{ "value": "0", "label": "阳性" }],
            uploadInformation:{
                idNumber:"",
                collectTime:"",
                result:""
            },
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
                collectStartTime: "",
                collectEndTime: "",
                collectDateRange: [],
                result: [],


            }, tableColmuns: [{ "label": "采样日期", "prop": "collectTime" }, { "label": "抗原结果", "prop": "result" }
            ],
            rules: {
                result: [
                    { required: true, message: '请选择抗原结果', trigger: 'change' },
                ],
                collectTime:[
                { required: true, message: '请选择测量日期', trigger: 'change' },
                ]
            }
        }
    },
    mounted() {
        this.getPage();
    },
    methods: {
        getPage() {
            this.loading = true;
            this.pagination.queryString = "admin";
            this.searchAntigenInformation.idNumber = JSON.parse(Cookie.get("userInformation")).idNumber;
            this.searchAntigenInformation.pagination = this.pagination;
            getData(this.searchAntigenInformation).then((res) => {
                this.loading = false
                this.pagination.total = res.data.data.total;
                this.tableData = res.data.data.records;
            }).catch((res) => this.loading = false)
        },
        submitForm(formName) {
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    if (this.antigenPicture === "") {
                        this.errorMessage("请上传抗原照片");
                        return;
                    }
                    const loading = this.$loading({
                        lock: true,
                        text: '上传中',
                        spinner: 'el-icon-loading',
                        background: 'rgba(0, 0, 0, 0.7)'
                    });
                    this.uploadInformation.idNumber=JSON.parse(Cookie.get("userInformation")).idNumber;
                    const formData = new FormData()
                    formData.append('entity', JSON.stringify(this.uploadInformation))
                    formData.append('file', this.antigenPicture)
                    insertData(formData).then((res) => {
                        loading.close();
                        if (res.data.code == 200) {
                            this.successMessage("上传成功！")
                            this.uploadInformationDialogVisable = false;
                            this.antigenPicture=""
                            this.previewUrl=""
                            this.getPage();
                            this.resetForm(formName);
                        }
                        else if(res.data.code==414){
                            this.errorMessage("您已上传过这一天的抗原信息")
                        }
                        else {
                            this.errorMessage(res.data.message)
                        }
                    }).catch((res) => {
                        loading.close();
                        this.errorMessage("输入信息有误，请仔细核对")
                    })


                } else {
                    console.log('error submit!!');
                    return false;
                }
            });
        },
        beforeUpload(file) {
            const isJPG = file.type === 'image/jpeg'
            const isLt10M = file.size / 1024 / 1024 < 10

            if (!isJPG) {
                this.$message.error('上传头像图片只能是 JPG 格式!');
                return false;
            }
            if (!isLt10M) {
                this.$message.error('上传头像图片大小不能超过 10MB!');
                return false;
            }
            // 保存预览图URL
            this.previewUrl = URL.createObjectURL(file);
            this.antigenPicture=file
            return false;
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
            this.loading = true;
            getAntigenPicture(antigen).then((res) => {
                this.antigenPath = window.URL.createObjectURL(res.data);
                this.loading = false;
                this.imageVisible = true
            }).catch((res) => {
                this.loading = false;
                this.errorMessage("查看失败")
            })

        },
        handleClose(done) {
            this.$confirm('确认关闭？')
                .then(_ => {
                    done();
                    this.resetForm("uploadInformation");
                    this.antigenPicture=""
                    this.previewUrl=""
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