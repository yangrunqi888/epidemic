<template>

    <el-form :model="form" ref="form" :rules="rules" label-width="100px" style="width: 50%;">
        <el-form-item label="姓名">
            <el-input disabled v-model="form.name" ></el-input>
        </el-form-item>
        <el-form-item label="原密码" prop="oldPassword" >
            <el-input v-model="form.oldPassword" show-password></el-input>
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
            <el-input v-model="form.newPassword" show-password></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
            <el-input v-model="form.confirmPassword" show-password></el-input>
        </el-form-item>
        <el-form-item>
            <el-button type="primary" @click="submitForm('form')">修改密码</el-button>
        </el-form-item>
    </el-form>
</template>
<script>
import Cookie from 'js-cookie'
import {changePassword} from '@/api/password/'
export default {
    data() {
        return {
            form: {
            name:"",
            employeeNumber:"",
            idNumber:"",
          oldPassword: '',
          newPassword: '',
          confirmPassword: '',
          position:"6"
        },
        rules: {
          oldPassword: [
            { required: true, message: '请输入原密码', trigger: 'blur' }
          ],
          newPassword: [
            { required: true, message: '请输入新密码', trigger: 'blur' },
            { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' }
          ],
          confirmPassword: [
            { required: true, message: '请再次输入新密码', trigger: 'blur' },
            { validator: this.validateConfirmPassword, trigger: 'blur' }
          ]
         }
        }
    },
    mounted(){
        var person= JSON.parse(Cookie.get("userInformation"))
        this.form.name=person.name;
        switch(person.position){
            case "0":case "1":case "2":case "3":case "4":case "5":
                this.form.position=person.position;
                this.form.employeeNumber=person.guardNumber||person.employeeNumber
                 break;
            default:this.form.idNumber=person.idNumber;
            }
    },
    methods: {
      validateConfirmPassword(rule, value, callback) {
        if (value === '') {
          callback(new Error('请再次输入新密码'));
        } else if (value !== this.form.newPassword) {
          callback(new Error('两次输入密码不一致'));
        } else {
          callback();
        }
      },
      submitForm(formName) {
        this.$refs[formName].validate(valid => {
          if (valid) {
            changePassword(this.form).then((res)=>{
                if (res.data.code == 200) {
                            this.successMessage("修改成功！")
                            this.$refs[formName].resetFields();
                            this.$emit('submit-password', true);
                        }
                        else {
                            this.errorMessage(res.data.message)
                        }
            }).catch((res)=>{
                this.errorMessage("修改失败");
            })
          } else {
            return false;
          }
        });
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