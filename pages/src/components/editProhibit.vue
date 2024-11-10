<template>
  <div class="upload-avatar">
    <el-upload class="avatar-uploader" drag ref="upload" :show-file-list="false" action="" :before-upload="beforeUpload">
      <div v-show="previewUrl !== ``" style="width: 100%;height: 100%; vertical-align:middle ;">
        <img :src="previewUrl" :style="{ width: `60%`, height: `100%` }" />
      </div>
      <div v-show="previewUrl === ``">
        <i class="el-icon-upload"></i>

        <div class="el-upload__text">将新头像拖到此处，或<em>点击上传</em></div>
      </div>
      <div class="el-upload__tip" slot="tip">只能上传jpg文件</div>
    </el-upload>
    <el-button class="upload-btn" type="primary" @click="confirmUpload">确定</el-button>
  </div>
</template>
<script>
import { uploadProhibit } from '@/api/staff'
import { uploadStaffProhibit } from '@/api/guard'
import { uploadResisdentProhibit } from '@/api/resident'
import Cookie from 'js-cookie';
export default {
  data() {
    return {
      previewUrl: '',
      prohibitPicture: '',
    }
  },
  mounted() {
  },
  methods: {
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
      this.prohibitPicture = file;
      return false;
    },
    confirmUpload() {
      if(this.prohibitPicture==''){
        this.$message.error('请选择新头像上传！');
        return;
      }
      const formData = new FormData()
      formData.append('file', this.prohibitPicture)
      this.$store.commit('setImageLoading', true);
      if ((JSON.parse(Cookie.get("userInformation")).hasOwnProperty('employeeNumber'))) {
        uploadProhibit(JSON.parse(Cookie.get("userInformation")).employeeNumber, formData).then((res) => {
          if (res.data.code == 200) {
            this.$message.success('头像上传成功!');
            this.$emit('submit-prohibit', true);
          } else {
            this.$message.error('上传失败，请重试!');
          }
        }).catch((error) => {
          this.$message.error('上传头像图片失败，请重试!');
        })
      }
      else if ((JSON.parse(Cookie.get("userInformation")).hasOwnProperty('guardNumber'))) {
        uploadStaffProhibit(JSON.parse(Cookie.get("userInformation")).guardNumber, formData).then((res) => {
          if (res.data.code == 200) {
            this.$message.success('头像上传成功!');
            this.$emit('submit-prohibit', true);
          } else {
            this.$message.error('上传失败，请重试!');
          }
        }).catch((error) => {
          this.$message.error('上传头像图片失败，请重试!');
        })
      }
      else {
        uploadResisdentProhibit(JSON.parse(Cookie.get("userInformation")).idNumber, formData).then((res) => {
          if (res.data.code == 200) {
            this.$message.success('头像上传成功!');
            this.$emit('submit-prohibit', true);
          } else {
            this.$message.error('上传失败，请重试!');
          }
        }).catch((error) => {
          this.$message.error('上传头像图片失败，请重试!');
        })
      }
      this.previewUrl='';
      this.prohibitPicture='';
    },
  }
}
</script>
<style  lang="less" scoped>
.upload-avatar {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.avatar-preview {
  width: 120px;
  height: 120px;
  margin-bottom: 10px;

  img {
    width: 100%;
    height: 100%;
    object-fit: contain;
  }
}

.avatar-uploader {
  margin-bottom: 10px;
  width: 100%;
  height: 100%;

  .el-upload {
    width: 100%;
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
  }

  .el-button {
    width: 100%;
    height: 100%;
  }

  input[type="file"] {
    display: none;
  }
}

.upload-btn {
  width: 80px;
}
</style>