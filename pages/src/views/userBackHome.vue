<template>
    <div v-loading="loading">
        <el-dialog title="返乡申请" :visible.sync="uploadInformationDialogVisable" width="50%" :before-close="handleClose">
            <el-form ref="uploadInformation" :model="uploadInformation" label-position="top" :rules="rules"
                label-width="150px">
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item prop="backDate" label="返乡日期">
                            <el-date-picker type="date" placeholder="请选择返乡日期" v-model="uploadInformation.backDate"
                                value-format="yyyy-MM-dd"></el-date-picker>
                        </el-form-item>
                        <el-form-item label="健康码截图" required>
                            <el-upload class="avatar-uploader" drag ref="upload" :show-file-list="false" action=""
                                :before-upload="beforeUploadHealth">
                                <div v-show="previewHealthUrl !== ``"
                                    style="width: 100%;height: 100%; vertical-align:middle ;">
                                    <img :src="previewHealthUrl" :style="{ width: `60%`, height: `100%` }" />
                                </div>
                                <div v-show="previewHealthUrl === ``">
                                    <i class="el-icon-upload"></i>

                                    <div class="el-upload__text">将健康码截图拖到此处，或<em>点击上传</em></div>
                                </div>
                                <div class="el-upload__tip" slot="tip">只能上传jpg文件</div>
                            </el-upload>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="当前所在地" prop="currentLocation">
                            <el-select filterable placeholder="请选择省/直辖市" v-model="uploadInformation.currentLocation[0]"
                                ref="selectNativeProvince" style="width: 50%;">
                                <el-option v-for="item in  provinceOptions" :key="item" :label="item" :value="item"
                                    @click.native="updateNativeProvince(item)">
                                </el-option>
                            </el-select>
                            <el-select filterable placeholder="请选择市/区" v-model="uploadInformation.currentLocation[1]"
                                ref="selectNativeCity" style="width: 50%;">
                                <el-option v-for="item in cityOptions" :key="item" :label="item" :value="item">
                                </el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item label="行程码截图" required>
                            <el-upload class="avatar-uploader" drag ref="upload" :show-file-list="false" action=""
                                :before-upload="beforeUploadTravel">
                                <div v-show="previewTravelUrl !== ``"
                                    style="width: 100%;height: 100%; vertical-align:middle ;">
                                    <img :src="previewTravelUrl" :style="{ width: `60%`, height: `100%` }" />
                                </div>
                                <div v-show="previewTravelUrl === ``">
                                    <i class="el-icon-upload"></i>

                                    <div class="el-upload__text">将行程码截图拖到此处，或<em>点击上传</em></div>
                                </div>
                                <div class="el-upload__tip" slot="tip">只能上传jpg文件</div>
                            </el-upload>
                        </el-form-item>
                    </el-col>
                </el-row>
            </el-form>
            <template slot="footer">
                <el-button type="primary" @click="submitForm('uploadInformation')">上传</el-button>
            </template>
        </el-dialog>
        <el-dialog title="筛选记录" :visible.sync="filterDialogVisible" width="30%" :before-close="handleFilterClose">
            <el-form ref="filterForm" :model="searchBackHomeInformation">
                <el-form-item label="日期范围" prop="collectDateRange">
                    <el-date-picker type="daterange" placeholder="选择日期范围"
                        v-model="searchBackHomeInformation.collectDateRange" range-separator="-" start-placeholder="开始日期"
                        end-placeholder="结束日期" value-format="yyyy-MM-dd">
                    </el-date-picker>
                </el-form-item>
                <el-form-item prop="state" label="审核状态">
                    <el-select v-model="searchBackHomeInformation.state" filterable multiple placeholder="请选择核酸类型">
                        <el-option v-for="item in stateOptions" :key="item.value" :label="item.label" :value="item.value">
                        </el-option>
                    </el-select>
                </el-form-item>
                <div style="text-align: center;">
                    <el-button @click="resetForm('filterForm')" type="primary">重置</el-button>
                </div>
            </el-form>
        </el-dialog>
        <el-dialog title="截图" :visible.sync="ImageVisible" width="30%">
            <div style="text-align: center;">
                <img :src="picturePath" style="max-width: 100%; max-height: 100%;" />
            </div>
        </el-dialog>
        <div class="detail">
            <el-dialog title="查看详情" :visible.sync="detailDialogVisable" width="40%" v-loading="imageLoading"
                element-loading-text="拼命加载中" element-loading-spinner="el-icon-loading"
                element-loading-background="rgba(0, 0, 0, 0.8)">
                <el-descriptions class="margin-top" :column="2" :size="size" border>
                    <el-descriptions-item v-for="item in detailColumns" :key="item.prop">
                        <template slot="label">
                            {{ item.label }}
                        </template>
                        {{ detailInformation[item.prop] }}
                    </el-descriptions-item>
                    <el-descriptions-item>
                        <template slot="label">
                            健康码截图
                        </template>
                        <el-button @click="seePicutre(detailInformation, 0)">查看</el-button>
                    </el-descriptions-item>
                    <el-descriptions-item>
                        <template slot="label">
                            行程码截图
                        </template>
                        <el-button @click="seePicutre(detailInformation, 1)">查看</el-button>
                    </el-descriptions-item>
                    <el-descriptions-item v-if="detailInformation.state != `未审核`">
                        <template slot="label">
                            返乡要求
                        </template>
                        {{ detailInformation.demand }}
                    </el-descriptions-item>
                </el-descriptions>
                <template slot="footer">
                    <el-button type="danger" v-if="detailInformation.state == `未审核`"
                        @click="confirmDeleteDialogVisable = true">
                        撤销
                    </el-button>
                </template>
            </el-dialog>
        </div>
        <el-dialog :visible.sync="confirmDeleteDialogVisable" width="30%">
            <template slot="title">
                <div style="text-align: center; font-size:20px;">
                    <i class="el-icon-warning" style="color:red;"></i>警告
                </div>
            </template>
            <span>是否撤销此条返乡申请，撤销后无法恢复</span>
            <span slot="footer" class="dialog-footer">
                <el-button @click="confirmDeleteDialogVisable = false">取 消</el-button>
                <el-button type="primary" @click="deleteBackHome()">确 定</el-button>
            </span>
        </el-dialog>
        <div class="backHomeData">
            <div class="insert">
                <el-button type="primary" @click="uploadInformationDialogVisable = true">返乡申请</el-button>
            </div>
            <div class="filter">
                <el-button style="margin-right:20px" icon="el-icon-s-operation" size="mini"
                    @click="filterDialogVisible = true">筛选</el-button>
            </div>
            <el-table :data="tableData">
                <el-table-column v-for="item in tableColmuns" :key="item.prop" :prop="item.prop" :label="item.label"
                    :width="item.prop === `idNumber` ? `200px` : ``">
                </el-table-column>
                <el-table-column label="状态">
                    <template slot-scope="scope">
                        {{ scope.row.state }}

                    </template>
                </el-table-column>
                <el-table-column label="操作">
                    <template slot-scope="scope">
                        <el-button type="primary" @click="handleSeeDetail(scope.row)"> 查看详细</el-button>
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
import { getData, getHealthPicture, getTravelPicture, deleteData, insertData } from '@/api/backHome';
import Cookie from 'js-cookie'
import { getProvinceData } from '../api'
export default {
    data() {
        return {
            detailDialogVisable: false,
            filterDialogVisible: false,
            ImageVisible: false,
            confirmDeleteDialogVisable: false,
            imageLoading: false,
            uploadInformationDialogVisable: false,
            loading: false,
            uploadInformation: {
                idNumber: "",
                currentLocation: ["", ""],
                backDate: "",
            },
            previewHealthUrl: "",
            previewTravelUrl: "",
            healthPicture: "",
            travelPicture: "",
            tableData: [],
            residents: [],
            picturePath: "",
            stateOptions: [{ "value": "0", "label": "未通过" }, { "value": "1", "label": "已通过" }, { "value": "2", "label": "未审核" }],
            pagination: {
                //分页相关属性
                currentPage: 1,
                pageSize: 15,
                total: null,
                queryString: "",
            },
            searchBackHomeInformation: {
                pagination: null,
                idNumber: "",
                collectDateRange: "",
                startBackDate: "",
                endBackDate: "",
                state: [],
            },
            tableColmuns: [{ "label": "返乡时间", "prop": "backDate" },
            { "label": "当前所在地", "prop": "currentLocation" }, { "label": "审核人", "prop": "employerName" }, { "label": "返乡要求", "prop": "demand" },
            ],
            detailInformation: {},
            detailColumns: [{ "label": "身份证号：", "prop": "idNumber" }, { "label": "姓名：", "prop": "name" }, { "label": "返乡时间：", "prop": "backDate" },
            { "label": "当前所在地：", "prop": "currentLocation" }, { "label": "当前状态", "prop": "state" }, { "label": "审核人", "prop": "employerName" }],
            provinceOptions: [],
            cityOptions: [],
            rules: {
                backDate: [
                    { required: true, message: '请选择返乡时间', trigger: 'change' },
                ],
                currentLocation: [
                    { required: true, message: '请选择当前所在地', trigger: 'change' },
                ],

            },
        }
    },
    mounted() {
        getProvinceData("中国").then((res) => {
            for (var item of res.data.districts[0].districts) {
                this.provinceOptions.push(item.name)
            }
        }).catch((res) => {
            console.log(res)
        })
        this.getPage();
    },
    methods: {
        getPage() {
            this.loading = true;
            this.pagination.queryString = "admin";
            this.searchBackHomeInformation.idNumber = JSON.parse(Cookie.get("userInformation")).idNumber;
            this.searchBackHomeInformation.pagination = this.pagination;
            getData(this.searchBackHomeInformation).then((res) => {
                this.loading = false;
                this.pagination.total = res.data.data.total;
                this.tableData = res.data.data.records;

            }).catch((res) => this.loading = false)
        },
        submitForm(formName) {
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    if (this.healthPicture === "") {
                        this.errorMessage("请上传健康码截图");
                        return;
                    }
                    if (this.travelPicture === "") {
                        this.errorMessage("请上传行程码截图");
                        return;
                    }
                    const loading = this.$loading({
                        lock: true,
                        text: '上传中',
                        spinner: 'el-icon-loading',
                        background: 'rgba(0, 0, 0, 0.7)'
                    });
                    this.uploadInformation.idNumber = JSON.parse(Cookie.get("userInformation")).idNumber;
                    this.uploadInformation.currentLocation = this.uploadInformation.currentLocation[0] + this.uploadInformation.currentLocation[1]; const formData = new FormData()
                    formData.append('entity', JSON.stringify(this.uploadInformation))
                    formData.append('healthFile', this.healthPicture)
                    formData.append('travelFile', this.travelPicture)
                    insertData(formData).then((res) => {
                        loading.close();
                        if (res.data.code == 200) {
                            
                            this.successMessage("上传成功！")
                            this.uploadInformationDialogVisable = false;
                            this.healthPicture = '';
                            this.travelPicture = '';
                            this.previewHealthUrl = '';
                            this.previewTravelUrl = '';
                            this.getPage();
                            this.resetForm(formName);
                        }
                        else if (res.data.code == 414) {
                            this.errorMessage("您已申请过这一天的返乡申请")
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
        handleFilterClose(done) {
            this.searchBackHomeInformation.startBackDate = this.searchBackHomeInformation.collectDateRange[0];
            this.searchBackHomeInformation.endBackDate = this.searchBackHomeInformation.collectDateRange[1];
            this.getPage();
            done();
        },
        deleteBackHome() {
            deleteData(this.detailInformation).then((res) => {
                if (res.data.code == 200) {
                    this.detailDialogVisable = false;
                    this.confirmDeleteDialogVisable = false;
                    this.successMessage("撤销成功！")
                    this.getPage()
                }
                else {
                    this.errorMessage(res.data.message)
                    this.confirmDeleteDialogVisable = false;
                }
            }).catch((res) => {
                this.errorMessage("撤销失败")
                this.confirmDeleteDialogVisable = false;
            })
        },
        beforeUploadHealth(file) {
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
            this.previewHealthUrl = URL.createObjectURL(file);
            this.healthPicture = file
            return false;
        },
        beforeUploadTravel(file) {
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
            this.previewTravelUrl = URL.createObjectURL(file);
            this.travelPicture = file
            return false;
        },
        seePicutre(back, val) {
            this.imageLoading = true;
            if (val == 0) {
                getHealthPicture(back).then((res) => {
                    this.imageLoading = false;
                    this.picturePath = window.URL.createObjectURL(res.data);
                    this.ImageVisible = true
                }).catch((res) => {
                    this.imageLoading = false;
                    this.errorMessage("查看失败")
                })
            }
            else if (val == 1) {
                getTravelPicture(back).then((res) => {
                    this.imageLoading = false;
                    this.picturePath = window.URL.createObjectURL(res.data);
                    this.ImageVisible = true
                }).catch((res) => {
                    this.imageLoading = false;
                    this.errorMessage("查看失败")
                })
            }

        },
        updateNativeProvince(name) {
            getProvinceData(name).then((res) => {
                this.cityOptions = []
                if (res.data.districts[0].districts[0].name !== name) {
                    for (var item of res.data.districts[0].districts) {
                        this.cityOptions.push(item.name)
                    }
                }
                else {
                    for (var item of res.data.districts[0].districts[0].districts) {
                        this.cityOptions.push(item.name)
                    }
                }
            }).catch((res) => {
                console.log(res)
            })

            this.uploadInformation.currentLocation[1] = ''
        },
        handleClose(done) {
            this.$confirm('确认关闭？')
                .then(_ => {
                    done();
                    this.resetForm("uploadInformation");
                    this.healthPicture = '';
                    this.travelPicture = '';
                    this.previewHealthUrl = '';
                    this.previewTravelUrl = '';
                })
                .catch(_ => { });
        },
        resetForm(formName) {
            this.$refs[formName].resetFields();

        },
        handleSeeDetail(row) {
            this.detailInformation = { ...row };
            this.detailDialogVisable = true;
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