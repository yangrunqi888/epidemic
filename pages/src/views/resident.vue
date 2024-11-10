<template>
    <div class="resident" v-loading="loading">
        <!-- 新增居民表单 -->
        <el-dialog title="新增居民" :visible.sync="insertDialogVisible" width="50%" :before-close="handleClose">
            <el-form ref="insertResident" :model="ruleForm" :rules="rules" label-width="150px">
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="身份证号" prop="idNumber">
                            <el-input v-model="ruleForm.idNumber" placeholder="请输入身份证号"></el-input>
                        </el-form-item><el-form-item label="性别" prop="gender">
                            <el-radio-group v-model="ruleForm.gender">
                                <el-radio label="0">男</el-radio>
                                <el-radio label="1">女</el-radio>
                            </el-radio-group>
                        </el-form-item><el-form-item prop="birthDay" label="出生日期">
                            <el-date-picker type="date" placeholder="请选择出生日期" v-model="ruleForm.birthDay"
                                style="width: 100%;" value-format="yyyy-MM-dd"></el-date-picker>
                        </el-form-item><el-form-item prop="politicsStatus" label="政治面貌">
                            <el-select v-model="ruleForm.politicsStatus" filterable placeholder="请选择政治面貌">
                                <el-option v-for="item in  politicsStatusOptions" :key="item" :label="item" :value="item">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="姓名" prop="name">
                            <el-input v-model="ruleForm.name" placeholder="请输入居民姓名"></el-input>
                        </el-form-item>
                        <el-form-item prop="nation" label="民族">
                            <el-select v-model="ruleForm.nation" filterable placeholder="请选择民族">
                                <el-option v-for="item in nationOptions" :key="item" :label="item" :value="item">
                                </el-option>
                            </el-select>
                        </el-form-item> <el-form-item prop="job" label="职业">
                            <el-input v-model="ruleForm.job" placeholder="请输入职业"></el-input>
                        </el-form-item> <el-form-item label="籍贯">
                            <el-select filterable placeholder="请选择省/直辖市" v-model="ruleForm.nativeProvince"
                                ref="selectNativeProvince" style="width: 50%;">
                                <el-option v-for="item in  provinceOptions" :key="item" :label="item" :value="item"
                                    @click.native="updateNativeProvince(item)">
                                </el-option>
                            </el-select>
                            <el-select filterable placeholder="请选择市/区" v-model="ruleForm.nativeCity" ref="selectNativeCity"
                                style="width: 50%;">
                                <el-option v-for="item in cityOptions" :key="item" :label="item" :value="item">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-form-item label="户籍地址" prop="permanentResidentAddress">
                    <el-input v-model="ruleForm.permanentResidentAddress" placeholder="请输入详细户籍地址"></el-input>
                </el-form-item>
                <el-row :gutter="20">
                    <el-col :span="12"><el-form-item label="联系电话" prop="phone">
                            <el-input v-model="ruleForm.phone" placeholder="请输入联系电话"></el-input>
                        </el-form-item>
                        <el-form-item label="居住楼栋号" prop="buildingNumber">
                            <el-select filterable placeholder="请选择楼栋号" v-model="ruleForm.buildingNumber">
                                <el-option v-for="item in buildings" :key="item" :value="item"></el-option>
                            </el-select></el-form-item>
                        <el-form-item label="居住房间号" prop="roomNumber">
                            <el-input v-model="ruleForm.roomNumber" placeholder="请输入房间号"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="电子邮箱" prop="email">
                            <el-input v-model="ruleForm.email" placeholder="请输入电子邮箱"></el-input>
                        </el-form-item>
                        <el-form-item label="居住状态">
                            <el-select filterable placeholder="请选择居住状态" v-model="ruleForm.housingState">
                                <el-option v-for="item in  housingStateOptions" :key="item.id" :label="item.name"
                                    :value="item.id">
                                </el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item>
                            <el-button type="primary" @click="submitForm('insertResident')">确定</el-button>
                            <el-button @click="resetForm('insertResident')">重置</el-button>
                        </el-form-item>
                    </el-col>
                </el-row>
            </el-form>
        </el-dialog>
        <!-- 编辑对话框 -->
        <el-dialog title="修改居民" :visible.sync="editDialogVisible" width="50%" :before-close="handleClose">
            <el-form ref="editForm" :model="editForm" :rules="rules" label-width="120px">
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="身份证号" prop="idNumber">
                            <el-input v-model="editForm.idNumber" :disabled="true"></el-input>
                        </el-form-item><el-form-item label="性别" prop="gender">
                            <el-radio-group v-model="editForm.gender">
                                <el-radio label="0">男</el-radio>
                                <el-radio label="1">女</el-radio>
                            </el-radio-group>
                        </el-form-item><el-form-item prop="birthday" label="出生日期">
                            <el-date-picker type="date" placeholder="请选择出生日期" v-model="editForm.birthDay"
                                style="width: 100%;" value-format="yyyy-MM-dd"></el-date-picker>
                        </el-form-item><el-form-item prop="politicsStatus" label="政治面貌">
                            <el-select v-model="editForm.politicsStatus" filterable placeholder="请选择政治面貌">
                                <el-option v-for="item in  politicsStatusOptions" :key="item" :label="item" :value="item">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="姓名" prop="name">
                            <el-input v-model="editForm.name" placeholder="请输入居民姓名"></el-input>
                        </el-form-item>
                        <el-form-item prop="nation" label="民族">
                            <el-select v-model="editForm.nation" filterable placeholder="请选择民族">
                                <el-option v-for="item in nationOptions" :key="item" :label="item" :value="item">
                                </el-option>
                            </el-select>
                        </el-form-item> <el-form-item label="职业">
                            <el-input v-model="editForm.job" placeholder="请输入职业"></el-input>
                        </el-form-item>
                        <el-form-item label="籍贯">
                            <el-select filterable placeholder="请选择省/直辖市" v-model="editForm.nativeProvince"
                                ref="selectNativeProvince2" style="width: 50%;">
                                <el-option v-for="item in  provinceOptions" :key="item" :label="item"
                                    :value="item" @click.native="updateNativeProvince(item)">
                                </el-option>
                            </el-select>
                            <el-select filterable placeholder="请选择市/区" v-model="editForm.nativeCity" ref="selectNativeCity2"
                                style="width: 50%;">
                                <el-option v-for="item in cityOptions" :key="item" :label="item" :value="item">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-form-item label="户籍地址">
                    <el-input v-model="editForm.permanentResidentAddress" placeholder="请输入详细户籍地址"></el-input>
                </el-form-item>
                <el-row :gutter="20">
                    <el-col :span="12"><el-form-item label="联系电话">
                            <el-input v-model="editForm.phone" placeholder="请输入联系电话"></el-input>
                        </el-form-item>
                        <el-form-item label="居住楼栋号" prop="buildingNumber">
                            <el-select filterable placeholder="请选择楼栋号" v-model="editForm.buildingNumber">
                                <el-option v-for="item in  buildings" :key="item" :value="item">
                                </el-option>
                            </el-select> </el-form-item>
                        <el-form-item label="居住房间号" prop="roomNumber">
                            <el-input v-model="editForm.roomNumber" placeholder="请输入房间号" style="width: 50%;"></el-input>
                        </el-form-item>

                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="电子邮箱">
                            <el-input v-model="editForm.email" placeholder="请输入电子邮箱"></el-input>
                        </el-form-item>
                        <el-form-item label="居住状态">
                            <el-select filterable placeholder="请选择居住状态" v-model="editForm.housingState">
                                <el-option v-for="item in  housingStateOptions" :key="item.id" :label="item.name"
                                    :value="item.id">
                                </el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item prop="state" label="人员状态">
                            <el-select v-model="editForm.state" filterable placeholder="请选择人员状态">
                                <el-option v-for="item in stateOptions" :key="item.id" :label="item.name" :value="item.id">
                                </el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item>
                            <el-button type="primary" @click="editResident('editForm')">确定</el-button>
                        </el-form-item>
                    </el-col>
                </el-row>
            </el-form>
        </el-dialog>
        
        <!-- 筛选对话框 -->
        <el-dialog title="筛选居民" :visible.sync="filterDialogVisible" width="50%" :before-close="handleFilterClose">
            <el-form ref="filterForm" :model="searchResidentInformation">
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="身份证号" prop="idNumber">
                            <el-input v-model="searchResidentInformation.idNumber" placeholder="请输入居民身份证号"
                                style="width:80%"></el-input>
                        </el-form-item><el-form-item label="性别" prop="gender">
                            <el-radio-group v-model="searchResidentInformation.gender">
                                <el-radio label="0">男</el-radio>
                                <el-radio label="1">女</el-radio>
                            </el-radio-group>
                        </el-form-item><el-form-item prop="age">
                            <span class="demonstration">年龄范围</span>
                            <el-slider v-model="searchResidentInformation.age" range style="width:80%">
                            </el-slider>
                        </el-form-item><el-form-item prop="politicsStatus" label="政治面貌">
                            <el-select v-model="searchResidentInformation.politicsStatus" filterable multiple
                                placeholder="请选择政治面貌">
                                <el-option v-for="item in  politicsStatusOptions" :key="item" :label="item" :value="item">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="姓名" prop="name">
                            <el-input v-model="searchResidentInformation.name" placeholder="请输入居民姓名"
                                style="width:80%"></el-input>
                        </el-form-item>
                        <el-form-item prop="nation" label="民族">
                            <el-select v-model="searchResidentInformation.nation" filterable multiple placeholder="请选择民族">
                                <el-option v-for="item in nationOptions" :key="item" :label="item" :value="item">
                                </el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item label="人员状态" prop="state">
                            <el-select v-model="searchResidentInformation.state" filterable clearable placeholder="请选择人员状态">
                                <el-option v-for="item in stateOptions" :key="item.id" :label="item.name" :value="item.id">
                                </el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item label="居住楼栋">
                            <el-select filterable multiple placeholder="请选择楼栋号"
                                v-model="searchResidentInformation.buildingNumber" style="width: 50%;">
                                <el-option v-for="item in  buildings" :key="item" :value="item">
                                </el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item label="居住状态" prop="housingState">
                            <el-select v-model="searchResidentInformation.housingState" filterable clearable
                                placeholder="请选择居住状态">
                                <el-option v-for="item in housingStateOptions" :key="item.id" :label="item.name"
                                    :value="item.id">
                                </el-option>
                            </el-select>
                        </el-form-item>
                        <div style="text-align: center;">
                            <el-button @click="resetForm('filterForm')" type="primary">重置</el-button>
                        </div>
                    </el-col>
                </el-row>
            </el-form>
        </el-dialog>
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
            <template slot="footer" >
                <el-button type="primary" @click="handleEditForm()">编辑</el-button>
                <el-popconfirm confirm-button-text='是' cancel-button-text='否' icon="el-icon-info"
                            icon-color="orange" :title="`是否要重置身份证号${detailInformation.idNumber}的居民密码`"
                            @confirm="resetResident()">
                            <el-button type="warning" slot="reference">重置密码</el-button>
                    </el-popconfirm>
                    <el-popconfirm confirm-button-text='是' cancel-button-text='否' icon="el-icon-info" icon-color="red"
                            :title="`是否要删除身份证号${detailInformation.idNumber}的居民信息`" @confirm="deleteResident()">
                            <el-button type="danger" slot="reference">删除</el-button>
                        </el-popconfirm> 
            </template>
        </el-dialog>
        <!-- 新增居民按钮 -->
        <div class="insert">
            <el-button type="primary" @click="insertDialogVisible = true">新增居民</el-button>
        </div>
        <div class="filter">
            <el-button style="margin-right:20px" icon="el-icon-s-operation" size="mini"
                @click="filterDialogVisible = true">筛选</el-button>
        </div>
        <!-- 居民信息表格 -->
        <div class="residentData">
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
import { getProvinceData } from '../api'
import { getList } from '@/api/building'
import { getData, insertData, deleteData, resetPassword, editResident } from '@/api/resident';
import Cookie from "js-cookie"
export default {
    data() {
        return {
            insertDialogVisible: false,
            editDialogVisible: false,
            filterDialogVisible: false,
            detailDialogVisable:false,
            loading:false,
            ruleForm: {
                idNumber: '',
                name: '',
                gender: '',
                birthDay: '',
                nation: '',
                politicsStatus: '',
                job: '',
                nativeProvince: '',
                nativeCity: '',
                permanentResidentAddress: '',
                phone: '',
                email: '',
                buildingNumber: '',
                roomNumber: '',
                housingState: '',
            },
            editForm: {
                idNumber: '',
                name: '',
                gender: '',
                birthDay: '',
                nation: '',
                politicsStatus: '',
                job: '',
                nativePlace: '',
                permanentResidentAddress: '',
                phone: '',
                email: '',
                buildingNumber: '',
                roomNumber: '',
                housingState: '',
            },
            nativePlaceProvince: '',
            nativePlaceCity: '',
            rules: {
                idNumber: [
                    { required: true, message: '请输入身份证号', trigger: 'blur' },
                    { min: 18, max: 18, message: '长度为18位', trigger: 'blur' }
                ],
                name: [
                    { required: true, message: '请输入姓名', trigger: 'blur' },
                ],
                gender: [
                    { required: true, message: '请选择性别', trigger: 'change' },
                ],
                birthDay: [
                    {  required: true, message: '请选择出生日期', trigger: 'change' },
                ],
                buildingNumber: [
                    { required: true, message: '请输入居住楼栋号', trigger: 'change' },
                ],
                roomNumber: [
                    { required: true, message: '请输入居住房间', trigger: 'blur' },
                ],
                housingState: [
                    { required: true, message: '请选择居住类型', trigger: 'blur' },
                ],
            },
            nationOptions: [
                "汉族", "壮族", "满族", "回族", "苗族", "维吾尔族", "土家族", "彝族", "蒙古族", "藏族", "布依族", "侗族", "瑶族", "朝鲜族", "白族", "哈尼族",
                "哈萨克族", "黎族", "傣族", "畲族", "傈僳族", "仡佬族", "东乡族", "高山族", "拉祜族", "水族", "佤族", "纳西族", "羌族", "土族", "仫佬族", "锡伯族",
                "柯尔克孜族", "达斡尔族", "景颇族", "毛南族", "撒拉族", "布朗族", "塔吉克族", "阿昌族", "普米族", "鄂温克族", "怒族", "京族", "基诺族", "德昂族", "保安族",
                "俄罗斯族", "裕固族", "乌孜别克族", "门巴族", "鄂伦春族", "独龙族", "塔塔尔族", "赫哲族", "珞巴族"
            ],
            politicsStatusOptions: ["群众", "中共党员", "中共预备党员", "共青团员", "民革党员", "民盟盟员", "民建会员", "民进会员", "农工党党员", "致公党党员", "九三学社社员", "台盟盟员", "无党派人士"],
            provinceOptions: [],
            cityOptions: [],
            buildings: [],
            rooms: [],
            housingStateOptions: [{ id: "0", name: "户主" }, { id: "1", name: "租客" }],
            stateOptions: [{ id: "0", name: "正常" }, { id: "1", name: "通勤人员" }, { id: "2", name: "黄码" }, { id: "3", name: "红码" },],
            tableColmuns: [{ "label": "身份证号", "prop": "idNumber" }, { "label": "姓名", "prop": "name" }, { "label": "性别", "prop": "gender" }, { "label": "年龄", "prop": "age" }, { "label": "出生日期", "prop": "birthDay" }
                
                , { "label": "人员状态", "prop": "state" }, { "label": "本小区居住地址", "prop": "buildingRoomNumber" }],
            dialogColumns: [{ "label": "身份证号", "prop": "idNumber" }, { "label": "姓名", "prop": "name" }, { "label": "性别", "prop": "gender" }, { "label": "年龄", "prop": "age" }, { "label": "出生日期", "prop": "birthDay" }
                , { "label": "民族", "prop": "nation" }, { "label": "政治面貌", "prop": "politicsStatus" }, { "label": "职业", "prop": "job" }, { "label": "籍贯", "prop": "nativePlace" }
                , { "label": "联系电话", "prop": "phone" }, { "label": "电子邮箱", "prop": "email" }, { "label": "人员状态", "prop": "state" }, { "label": "本小区居住地址", "prop": "buildingRoomNumber" }, 
                { "label": "居住状态", "prop": "housingState" }, { "label": "户籍地址", "prop": "permanentResidentAddress" }],
            tableData: [],
            pagination: {
                //分页相关属性
                currentPage: 1,
                pageSize: 15,
                total: null,
                queryString: "",
            },
            searchResidentInformation: {
                pagination: null,
                idNumber: "",
                name: "",
                gender: "",
                maxAge: "",
                minAge: "",
                age: [0, 100],
                nation: [],
                politicsStatus: [],
                state: "",
                buildingNumber: [],
                housingState: ""
            },
            detailInformation:{},
        }
    },
    mounted() {
        this.getPage();
        getProvinceData("中国").then((res) => {
            for (var item of res.data.districts[0].districts) {
                this.provinceOptions.push(item.name)
            }
        }).catch((res) => {
            console.log(res)
        })
        getList(this.pagination.queryString).then((res) => {
            for (var building of res.data.data) {
                this.buildings.push(building.buildingNumber)
            }

        })//获取管理区域的楼栋号

    },
    methods: {
        handleClose(done) {
            this.$confirm('确认关闭？')
                .then(_ => {
                    done();
                    this.resetForm("insertResident");

                })
                .catch(_ => { });
        },
        handleFilterClose(done) {
            this.searchResidentInformation.maxAge = this.searchResidentInformation.age[1];
            this.searchResidentInformation.minAge = this.searchResidentInformation.age[0];
            this.getPage();
            done();
        },
        submitForm(formName) {
            this.$refs[formName].validate((valid) => {
                if (valid) {
                  
                    insertData(this.ruleForm).then((res) => {
                        if (res.data.code == 200) {
                            this.successMessage("新增成功！")
                            this.insertDialogVisible = false;
                            this.getPage();
                            this.resetForm(formName);
                            this.cityOptions = []
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
            if (formName === "insertResident") {
                this.ruleForm.nativeCity = "";
                this.ruleForm.nativeProvince = "";
                this.ruleForm.buildingNumber = "";
                this.ruleForm.roomNumber = "";
                this.ruleForm.housingState = "";
            }
            else {
                this.editForm.nativeCity = "";
                this.editForm.nativeProvince = "";
                this.editForm.buildingNumber = "";
                this.editForm.roomNumber = "";
                this.editForm.housingState = "";
            }

        },
        updateNativeProvince(name) {
            getProvinceData(name).then((res) => {
                this.cityOptions = []
                if(res.data.districts[0].districts[0].name!==name){
                    for (var item of res.data.districts[0].districts) {
                    this.cityOptions.push(item.name)
                    }
                }
                else{
                    for (var item of res.data.districts[0].districts[0].districts) {
                    this.cityOptions.push(item.name)
                    }
                }
            }).catch((res) => {
                console.log(res)
            })
            
            this.ruleForm.nativeCity = ''
        },
        deleteResident() {
            deleteData(this.detailInformation.idNumber).then((res) => {
                if (res.data.code == 200) {
                    this.successMessage("删除成功！")
                    this.getPage()
                    this.detailDialogVisable=false;
                }
                else {
                    this.errorMessage(res.data.message)
                }
            }).catch((res) => {
                this.errorMessage("删除失败")
            })
        },
        resetResident() {
            resetPassword(this.detailInformation.idNumber).then((res) => {
                if (res.data.code == 200) {
                    this.detailDialogVisable=false;
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
        handleDetail(row){
            this.detailDialogVisable=true;
            this.detailInformation={...row}
        },
        handleEditForm() {
            this.editForm = { ...this.detailInformation };
            this.editForm.gender = this.editForm.gender === "男" ? "0" : "1";
            this.editForm.housingState = this.editForm.housingState === "户主" ? "0" : "1";
            if (this.editForm.state === "正常") this.editForm.state = "0";
            else if (this.editForm.state === "通勤人员") this.editForm.state = "1";
            else if (this.editForm.state === "黄码") this.editForm.state = "2";
            else if (this.editForm.state === "红码") this.editForm.state = "3";
            this.updateNativeProvince(this.editForm.nativeProvince)
            this.editDialogVisible = true;
            this.detailDialogVisable=false;

        },
        editResident(formName) {
            this.editForm.nativeProvince = this.$refs.selectNativeProvince2.selectedLabel;
            this.editForm.nativeCity = this.$refs.selectNativeCity2.selectedLabel;


            this.$refs[formName].validate((valid) => {
                if (valid) {
                    editResident(this.editForm).then((res) => {
                        if (res.data.code == 200) {
                            this.successMessage("修改成功！")
                            this.editDialogVisible = false;
                            this.getPage();
                            this.resetForm(formName);
                            this.cityOptions = [];
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
            this.loading=true
            this.pagination.queryString = JSON.parse(Cookie.get("userInformation")).employeeNumber;
            this.searchResidentInformation.pagination = this.pagination;
            getData(this.searchResidentInformation).then((res) => {
                this.loading=false;
                this.pagination.total = res.data.data.total;
                this.tableData = res.data.data.records;
            }).catch((res)=>this.loading=false)
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