<template>
  <!-- 投诉详情对话框 -->
  <div v-loading="loading">
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
              <el-form-item label="居民提交日期范围" prop="complaintTime">
                    <el-date-picker type="datetimerange" placeholder="选择日期范围" v-model="searchComplaintInformation.complaintTime"
                        range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期" value-format="yyyy-MM-dd HH:mm:ss">
                    </el-date-picker>
                </el-form-item>
                <el-form-item label="处理日期范围" prop="handleTime">
                    <el-date-picker type="datetimerange" placeholder="选择日期范围" v-model="searchComplaintInformation.handleTime"
                        range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期" value-format="yyyy-MM-dd HH:mm:ss">
                    </el-date-picker>
                </el-form-item>
                <el-form-item prop="employerNumber" label="处理人工号"  >
                    <el-input placeholder="请输入处理人工号" v-model="searchComplaintInformation.employeeId" style="width: 50%;"></el-input>
                </el-form-item>
                <el-form-item prop="employerName" label="处理人姓名">
                    <el-input placeholder="请输入处理人姓名" v-model="searchComplaintInformation.employeeName" style="width: 50%;"></el-input>
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
            </el-descriptions>
            <el-descriptions class="margin-top" :column="1" :size="size" border>
                <el-descriptions-item>
                    <template slot="label">
                       内容
                    </template>
                    {{ complaintDetail.text }}
                </el-descriptions-item>
            </el-descriptions>
            <el-descriptions class="margin-top" :column="3" :size="size" border >
                <el-descriptions-item v-for="item in dialogColmuns2" :key="item.prop" v-if="complaintDetail.state === `已处理`">
                    <template slot="label">
                        {{ item.label }}
                    </template>
                    {{ complaintDetail[item.prop] }}
                </el-descriptions-item>
                <el-descriptions-item v-if="complaintDetail.state === `已处理`">
                    <template slot="label">
                      居民评价
                    </template>
                    <el-rate v-if="complaintDetail.comment!=0" v-model="complaintDetail.comment" disabled show-score text-color="#ff9900" score-template="{value}">
              </el-rate>
              <span v-else>暂未评价</span>
                </el-descriptions-item>
                <el-descriptions-item>
                    <template slot="label">
                      处理
                    </template>
                    <el-input  v-if="complaintDetail.state === `未处理`" type="textarea" :rows="3" v-model="complaintDetail.result"></el-input>
                    <span v-else>{{ complaintDetail.result }}</span>
                </el-descriptions-item>
            </el-descriptions>
      <span slot="footer" class="dialog-footer">
        <el-button v-if="complaintDetail.state === `未处理`" type="primary" @click="complaintConfirmDialogVisable = true">提交处理结果</el-button>
        <el-button  type="danger" @click="confirmDeleteDialogVisable = true" v-if="deletePrivilege">删除</el-button>
      </span>
    </el-dialog>
    <div>
      <el-dialog title="提示" :visible.sync="complaintConfirmDialogVisable" width="30%">
        <span>是否要提交处理结果</span>
        <span slot="footer" class="dialog-footer">
          <el-button @click="complaintConfirmDialogVisable = false">取 消</el-button>
          <el-button type="primary" @click="complaintSubmit()">确 定</el-button>
        </span>
      </el-dialog>
      <el-dialog  :visible.sync="confirmDeleteDialogVisable" width="30%">
        <template slot="title">
                <div style="text-align: center; font-size:20px;">
                    <i class="el-icon-warning" style="color:red;"></i>警告
                </div>
            </template>
        <span>是否删除居民{{ complaintDetail.complaintName }}此条投诉记录</span>
        <span slot="footer" class="dialog-footer">
          <el-button @click="confirmDeleteDialogVisable = false">取 消</el-button>
          <el-button type="primary" @click="confirmDelete()">确 定</el-button>
        </span>
      </el-dialog>
      <!-- 投诉列表 -->
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
import { getData,copeComplaint,deleteData } from '@/api/complaint'
import Cookie from 'js-cookie'
export default {
  data() {
    return {
      filterDialogVisible: false,
      complaintDetailDialogVisible: false,
      complaintConfirmDialogVisable: false,
      confirmDeleteDialogVisable:false,
      loading:false,
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
        employeeId: "",
        employeeName: "",
        state: ""
      },
      typeOptions: [{ "value": "0", "label": "建议" }, { "value": "1", "label": "投诉" }],
      resultOptions: [{ "value": "0", "label": "未处理" }, { "value": "1", "label": "已处理" }],
      tableColmuns: [{ "label": "编号", "prop": "id" }, { "label": "类型", "prop": "type" },
      { "label": "标题", "prop": "title" }, { "label": "提交时间", "prop": "complaintTime" },
      { "label": "状态", "prop": "state" }],
        dialogColmuns1: [{ "label": "编号", "prop": "id" }, { "label": "类型", "prop": "type" },
      { "label": "标题", "prop": "title" }, { "label": "状态", "prop": "state" },
      { "label": "发起人", "prop": "complaintName" },{ "label": "提交时间", "prop": "complaintTime" },],
       dialogColmuns2: [
       { "label": "处理人", "prop": "employeeName" }, { "label": "处理时间", "prop": "handleTime" }, 
      ],
      
    }
  },
  mounted() {
    this.getPage();
  },
  methods: {
    getPage() {
      this.loading=true;
      this.pagination.queryString = JSON.parse(Cookie.get("userInformation")).employeeNumber;
      this.searchComplaintInformation.pagination = this.pagination;
      getData(this.searchComplaintInformation).then((res) => {
        this.loading=false;
        this.pagination.total = res.data.data.total;
        this.tableData = res.data.data.records;
      }).catch((res)=>this.loading=false)
    },
    complaintSubmit() {
      this.complaintDetail.employeeId = this.pagination.queryString;
      this.complaintDetail.type=this.complaintDetail.type==="建议"?"0":"1"
      copeComplaint(this.complaintDetail).then((res) => {
                if (res.data.code == 200) {
                    this.successMessage("处理成功！")
                    this.complaintDetailDialogVisible=false;
                    this.complaintConfirmDialogVisable=false;
                    this.getPage()
                }
                else {
                    this.errorMessage(res.data.message)
                }
            }).catch((res) => {
                this.errorMessage("处理失败！")
            })
    },
    handleFilterClose(done) {
      this.getPage();
      done();
    },
    confirmDelete(){
      deleteData(this.complaintDetail.id).then((res) => {
                if (res.data.code == 200) {
                    this.successMessage("删除成功！")
                    this.complaintDetailDialogVisible=false;
                    this.confirmDeleteDialogVisable=false;
                    this.getPage()
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
    handleDetail(row) {
      this.complaintDetail = { ...row };
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
  computed:{
        deletePrivilege(){
            switch (JSON.parse(Cookie.get("userInformation")).position) {
                case "0": return true; 
                default:false;
            }
        }
    }
}
</script>