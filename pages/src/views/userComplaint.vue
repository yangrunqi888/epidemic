<template>
    <!-- 投诉详情对话框 -->
    <div v-loading="loading">
        <el-dialog title="我的诉求" :visible.sync="insertDialogVisable" width="30%" :before-close="handleClose">
            <el-form ref="uploadInformation" :model="uploadInformation"  :rules="rules"
                label-width="auto">
                <el-form-item prop="type" label="诉求类型">
                    <el-radio-group v-model="uploadInformation.type">
                                <el-radio label="0">建议</el-radio>
                                <el-radio label="1">投诉</el-radio>
                            </el-radio-group>
                    </el-form-item>
                    <el-form-item prop="anonymous" label="匿名">
                        <el-checkbox v-model="uploadInformation.anonymous"></el-checkbox>
                    </el-form-item>
                    <el-form-item prop="title" label="标题">
                        <el-input v-model="uploadInformation.title" placeholder="请输入标题"></el-input>
                    </el-form-item>
                    <el-form-item prop="text" label="内容">
                        <el-input type="textarea" :rows="5" v-model="uploadInformation.text" placeholder="请输入内容"></el-input>
                    </el-form-item>
            </el-form>
            <template slot="footer">
                <el-button @click="resetForm('uploadInformation')">重置</el-button>
                <el-button type="primary" @click="submitForm('uploadInformation')">提交诉求</el-button>
            </template>
        </el-dialog>
      <el-dialog title="筛选记录" :visible.sync="filterDialogVisible" width="30%" :before-close="handleFilterClose">
              <el-form ref="filterForm" :model="searchComplaintInformation" >
                
                <el-form-item prop="type" label="类别">
                      <el-select v-model="searchComplaintInformation.type" filterable placeholder="请选择类型">
                          <el-option v-for="item in typeOptions" :key="item.value" :label="item.label" :value="item.value">
                          </el-option>
                      </el-select>
                  </el-form-item>
                  <el-form-item prop="title" label="标题">
                      <el-input placeholder="请输入标题" v-model="searchComplaintInformation.title"></el-input>
                  </el-form-item>
                <el-form-item label="提交日期范围" prop="complaintTime">
                      <el-date-picker type="datetimerange" placeholder="选择日期范围" v-model="searchComplaintInformation.complaintTime"
                          range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期" value-format="yyyy-MM-dd HH:mm:ss">
                      </el-date-picker>
                  </el-form-item>
                  <el-form-item label="处理日期范围" prop="handleTime">
                      <el-date-picker type="datetimerange" placeholder="选择日期范围" v-model="searchComplaintInformation.handleTime"
                          range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期" value-format="yyyy-MM-dd HH:mm:ss">
                      </el-date-picker>
                  </el-form-item>
                  <el-form-item prop="state" label="处理状态">
                      <el-select v-model="searchComplaintInformation.state" filterable placeholder="请选择处理状态">
                          <el-option v-for="item in resultOptions" :key="item.value" :label="item.label" :value="item.value">
                          </el-option>
                      </el-select>
                  </el-form-item>
                  <div style="text-align: center;">
                      <el-button @click="resetForm('filterForm')" type="primary">重置</el-button>
                  </div>
              </el-form>
          </el-dialog>
      <el-dialog title="投诉详情" :visible.sync="complaintDetailDialogVisible" width="60%">
            <el-descriptions class="margin-top" :column="2" :size="size" border>
                  <el-descriptions-item v-for="item in dialogColmuns1" :key="item.prop">
                      <template slot="label">
                          {{ item.label }}
                      </template>
                      {{ complaintDetail[item.prop] }}
                  </el-descriptions-item>
                  <el-descriptions-item>
                      <template slot="label">
                         是否匿名
                      </template>
                      {{ complaintDetail.complaintName==="匿名"?"是":"否" }}
                  </el-descriptions-item>
              </el-descriptions>
              <el-descriptions class="margin-top" :column="1" :size="size" border>
                  <el-descriptions-item>
                      <template slot="label">
                         内容
                      </template>
                      {{ complaintDetail.text }}
                  </el-descriptions-item>
              </el-descriptions>
              <el-descriptions class="margin-top" :column="2" :size="size" border >
                  <el-descriptions-item v-for="item in dialogColmuns2" :key="item.prop" v-if="complaintDetail.state === `已处理`">
                      <template slot="label">
                          {{ item.label }}
                      </template>
                      {{ complaintDetail[item.prop] }}
                  </el-descriptions-item>
                  <el-descriptions-item v-if="complaintDetail.state === `已处理`">
                      <template slot="label" >
                        处理结果
                      </template>
                      {{ complaintDetail.result }}
                  </el-descriptions-item> 
              </el-descriptions>
              <el-descriptions v-if="complaintDetail.state === `已处理`" class="margin-top" :column="2" :size="size" border >
                  <el-descriptions-item>
                      <template slot="label">
                         评价
                      </template>
                      <el-rate :disabled="complaintDetail.comment!=0"  
                    v-model="complaintDetail.complaintComment" show-score text-color="#ff9900" score-template="{value}"></el-rate>

                  </el-descriptions-item>
              </el-descriptions>
                    
        <span slot="footer" class="dialog-footer">
          <el-button v-show="complaintDetail.state === `已处理`&&complaintDetail.comment==0" type="primary" @click="commentSubmit()">提交评价</el-button>
          <el-button v-show="complaintDetail.state === `未处理`" type="danger" @click="confirmDeleteDialogVisable = true">撤销诉求</el-button>
        </span>
      </el-dialog>
      <div>
        <el-dialog  :visible.sync="confirmDeleteDialogVisable" width="30%">
          <template slot="title">
                  <div style="text-align: center; font-size:20px;">
                      <i class="el-icon-warning" style="color:red;"></i>警告
                  </div>
              </template>
          <span>是否撤销此条投诉，撤销后无法恢复</span>
          <span slot="footer" class="dialog-footer">
            <el-button @click="confirmDeleteDialogVisable = false">取 消</el-button>
            <el-button type="primary" @click="confirmDelete()">确 定</el-button>
          </span>
        </el-dialog>
        <!-- 投诉列表 -->
        <div class="insert">
            <el-button type="primary" @click="insertDialogVisable = true">新建诉求</el-button>
        </div>
        <div class="filter">
              <el-button style="margin-right:20px" icon="el-icon-s-operation" size="mini"
                  @click="filterDialogVisible = true">筛选</el-button>
          </div>
        <el-table :data="tableData">
          <el-table-column v-for="item in tableColmuns" :key="item.prop" :prop="item.prop" :label="item.label"
            :width="item.prop === `idNumber` ? `200px` : ``">
          </el-table-column>
  
          <el-table-column label="操作" fixed="right" width="300px">
            <template slot-scope="scope">
  
              <el-button type="primary" @click="handleDetail(scope.row)">查看详情</el-button>
            </template>
          </el-table-column>
        </el-table>
  
  
        <!-- 分页 -->
        <div class="pagination" style="text-align: center;">
          <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
            :current-page="pagination.currentPage" :page-sizes="[1, 10, 15, 20, 25]" :page-size="pagination.pageSize"
            layout="total, sizes, prev, pager, next, jumper" :total="pagination.total">
          </el-pagination>
        </div>
      </div>
    </div>
  </template>
  <script>
  import { getData,commentComplaint,deleteData,insertData } from '@/api/complaint'
  import Cookie from 'js-cookie'
  export default {
    data() {
      return {
        filterDialogVisible: false,
        complaintDetailDialogVisible: false,
        confirmDeleteDialogVisable:false,
        insertDialogVisable:false,
        loading:false,
        uploadInformation:{
            type:"",
            title:"",
            text:"",
            anonymous:false,
        },

        tableData: [],
        complaintDetail: {},
        pagination: {
          //分页相关属性
          currentPage: 1,
          pageSize: 15,
          total: null,
          queryString: "",
        },
        searchComplaintInformation: {
          pagination: null,
          type: "",
          title: "",
          complaintTime: [],
          handleTime: [],
          state: "",
          idNumber:"",
        },
        typeOptions: [{ "value": "0", "label": "建议" }, { "value": "1", "label": "投诉" }],
        resultOptions: [{ "value": "0", "label": "未处理" }, { "value": "1", "label": "已处理" }],
        tableColmuns: [{ "label": "编号", "prop": "id" }, { "label": "类型", "prop": "type" },
        { "label": "标题", "prop": "title" }, { "label": "提交时间", "prop": "complaintTime" },
        { "label": "状态", "prop": "state" }],
          dialogColmuns1: [{ "label": "编号", "prop": "id" }, { "label": "类型", "prop": "type" },
        { "label": "标题", "prop": "title" }, { "label": "状态", "prop": "state" },
       { "label": "提交时间", "prop": "complaintTime" },],
         dialogColmuns2: [
         { "label": "处理人", "prop": "employeeName" }, { "label": "处理时间", "prop": "handleTime" }, 
        ],
        rules:{
            type: [
                    { required: true, message: '请选择诉求类型', trigger: 'change' },
                ],
            anonymous: [
                    { required: true, message: '请选择是否匿名', trigger: 'change' },
                ],
            title:[
            { required: true, message: '请输入诉求标题', trigger: 'blur' },
            ],
            text:[
            { required: true, message: '请输入诉求内容', trigger: 'blur' },
            ]
        }
        
      }
    },
    mounted() {
      this.getPage();
    },
    methods: {
      getPage() {
        this.loading=true;
        this.pagination.queryString = "admin"
        this.searchComplaintInformation.idNumber=JSON.parse(Cookie.get("userInformation")).idNumber;
        this.searchComplaintInformation.pagination = this.pagination;
        getData(this.searchComplaintInformation).then((res) => {
          this.loading=false;
          this.pagination.total = res.data.data.total;
          this.tableData = res.data.data.records;
        }).catch((res)=>this.loading=false)
      },
      submitForm(formName) {
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    this.uploadInformation.anonymous=this.uploadInformation.anonymous==false?"0":"1"
                    this.uploadInformation.complaintId=JSON.parse(Cookie.get("userInformation")).idNumber;
                    insertData(this.uploadInformation).then((res) => {
                        if (res.data.code == 200) {
                            this.successMessage("上传成功！")
                            this.insertDialogVisable = false;
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
      commentSubmit() {
        if(this.complaintDetail.complaintComment==0){
            this.errorMessage("请先评价再提交！")
            return;
        }
        this.complaintDetailDialogVisible=false;
        this.complaintDetail.comment = this.complaintDetail.complaintComment;
        this.complaintDetail.type=this.complaintDetail.type==="建议"?"0":"1"
        this.complaintDetail.state=this.complaintDetail.state==="未处理"?"0":"1"
        commentComplaint(this.complaintDetail).then((res) => {
                  if (res.data.code == 200) {
                      this.successMessage("评价成功！")
                      this.getPage()
                    
                      
                  }
                  else {
                      this.errorMessage(res.data.message)
                  }
              }).catch((res) => {
                  this.errorMessage("评价失败！")
              })
      },
      handleFilterClose(done) {
        this.getPage();
        done();
      },
      handleClose(done) {
            this.$confirm('确认关闭？')
                .then(_ => {
                    done();
                    this.resetForm("uploadInformation");
                })
                .catch(_ => { });
        },
      confirmDelete(){
        deleteData(this.complaintDetail.id).then((res) => {
                  if (res.data.code == 200) {
                      this.successMessage("撤销成功！")
                      this.complaintDetailDialogVisible=false;
                      this.confirmDeleteDialogVisable=false;
                      this.getPage()
                  }
                  else {
                      this.errorMessage(res.data.message)
                      this.confirmDeleteDialogVisable=false;
                  }
              }).catch((res) => {
                  this.errorMessage("撤销失败")
                  this.confirmDeleteDialogVisable=false;
              })
      },
      handleDetail(row) {
        this.complaintDetail = { ...row };
        var o={complaintComment:this.complaintDetail.comment}
        this.complaintDetail={...this.complaintDetail,...o}
        this.complaintDetailDialogVisible = true;
      },
      resetForm(formName) {
        this.$refs[formName].resetFields();
  
      }, handleSizeChange(val) {
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
   
  }
  </script>