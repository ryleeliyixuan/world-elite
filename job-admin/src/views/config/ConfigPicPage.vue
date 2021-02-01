<template>
  <div class="app-container">
    <div class="title-1">1 轮播图设置</div>
    <div class="change-sty">
      <span>显示此模块：
      <el-switch
        v-model="showPicList"
        active-color="#13ce66"
        inactive-color="#ff4949">
      </el-switch>
        </span>

    </div>
    <div class="list-detail" v-if="showPicList==true">
    <el-button class="btn-upload" type="success" @click="openUploadDiag">+ 添加轮播图</el-button>
    <div v-if="showUploadDiag==true">
      <div class="EditUplaod" style="
    box-shadow: 0px 4px 16px 3px rgba(191, 199, 215, 0.31);
    margin-top: 30px;
    margin-bottom: 40px;
    padding-bottom: 40px;
    padding-top: 30px;
padding-left: 30px;">
      <el-form ref="urlForm" :model="urlForm"
               :rules="urlFormRule">
        <el-form-item label="轮播图图片:" prop="url">
          <el-upload
            class="thumbnail-uploader"
            :action="uploadPicOptions.action"
            :data="uploadPicOptions.params"
            :accept="uploadPicOptions.acceptFileType"
            :show-file-list="false"
            :on-success="handleUploadSuccess"
            :before-upload="beforeUpload"
          >
            <el-image
              v-if="url || urlForm.url"
              :src="url || urlForm.url"
              v-loading="uploadPicOptions.loading"
              class="thumbnail"
            />
            <i v-else class="el-icon-plus thumbnail-uploader-icon"></i>
          </el-upload>
        </el-form-item>
        <el-form-item label="轮播图名称:" prop="name" style="display: flex;">
            <el-input
              v-model="urlForm.name"
              placeholder="请输入轮播图名称"
            ></el-input>
        </el-form-item>
        <el-form-item label="轮播图跳转链接:"  style="display: flex;">
          <el-input
            v-model="urlForm.target"
            placeholder="请输入轮播图跳转链接"
          ></el-input>
        </el-form-item>
      </el-form>
        <div style="display: flex" class="btn-sava">
        <el-button  @click="closeSaveUpload">取消</el-button>
      <el-button  @click="handleUploadSuccess">保存</el-button>
        </div>
      </div>
    </div>
    <el-table
      :data="carouselList"
      class="tablist-style"
      border
    >
      <el-table-column
        type="index"
        label="顺序"
        width="80px"
        align="center"
      >
      </el-table-column>
      <el-table-column
        property="name"
        label="轮播图名称"
        align="center"

      >
      </el-table-column>
      <el-table-column
        property="updateTime"
        label="上传时间"
        align="center"
      >
      </el-table-column>
      <el-table-column
        fixed="right"
        label="操作"
        align="center"
        >
        <template slot-scope="scope">
          <el-button
            @click.native.prevent="handleDeleteCarousel(scope.$index,scope.row)"
            type="text"
            size="small">
            删除
          </el-button>
          <el-button
            @click.native.prevent="handMoveUpCarousel(scope.$index, scope.row)"
            type="text"
            size="small">
            上升
          </el-button>
          <el-button
            @click.native.prevent="handMoveDownCarousel(scope.$index, scope.row)"
            type="text"
            size="small">
            下降
          </el-button>
        </template>
      </el-table-column>

    </el-table>
    </div>


    <el-divider></el-divider>
    <div class="title-1">2 推荐板块设置</div>
    <div class="change-sty">
      <span>显示此模块：
      <el-switch
        v-model="showActivitiPic"
        active-color="#13ce66"
        inactive-color="#ff4949">
      </el-switch>
        </span>
    </div>
    <div class="list-detail" v-if="showActivitiPic==true" style="padding-left: 155px">
      <el-form :model="companyHomeForm" label-width="150px" class="demo-ruleForm">
        <el-form-item label="板块1（上方）：">
        <el-button type="success" @click="openPicEdit">上传图片</el-button>
          <span style="padding-left: 30px"
                v-if="this.companyHomeForm.topPicName && this.companyHomeForm.topPicName!=''">
            {{this.companyHomeForm.topPicName}}
            <i class="el-icon-success" style="color: #80ff00"></i>
          </span>
        </el-form-item>
      </el-form>
        <div v-if="showPicEdit==true"  style="
    box-shadow: 0px 4px 16px 3px rgba(191, 199, 215, 0.31);
    margin-top: 30px;
    margin-bottom: 40px;
    padding-bottom: 40px;
    padding-top: 30px;
padding-left: 40px;
    max-width: 560px;">
          <el-form ref="companyHomeForm" :model="companyHomeForm">
            <div style="display: block">
            <el-form-item label="模块上方图片:" prop="topPicUrl">
              <el-upload
                class="thumbnail-uploader"
                :action="uploadPicOptions.action"
                :data="uploadPicOptions.params"
                :accept="uploadPicOptions.acceptFileType"
                :show-file-list="false"
                :on-success="handleUploadSuccessHomeTop"
                :before-upload="beforeUpload"
              >
                <el-image
                  v-if="topPicUrl || companyHomeForm.topPicUrl"
                  :src="topPicUrl || companyHomeForm.topPicUrl"
                  v-loading="uploadPicOptions.loading"
                  class="thumbnail"
                />
                <i v-else class="el-icon-plus thumbnail-uploader-icon"></i>
              </el-upload>
            </el-form-item>
            </div>
            <el-form-item label="模块上方图片名称:" prop="topPicName" style="display:flex">
              <el-input
                v-model="companyHomeForm.topPicName"
                placeholder="请输入模块上方图片名称"
              ></el-input>
            </el-form-item>
            <el-form-item label="模块上方图片跳转链接:" prop="topPicTarget" style="display:flex">
              <el-input
                v-model="companyHomeForm.topPicTarget"
                placeholder="请输入模块上方图片跳转链接"
              ></el-input>
            </el-form-item>
          </el-form>
          <div style="display: flex">
          <el-button style="margin-left: 300px;" @click="closeTopPic">取消</el-button>
          <el-button style="margin-left: 10px;" @click="saveTopPic">保存</el-button>
          </div>
        </div>
      <el-form :model="companyHomeForm" label-width="150px" class="demo-ruleForm">
        <el-form-item label="板块2（下方）：">
          <el-button type="success" @click="openPicBottomEdit">上传图片</el-button>
          <span style="padding-left: 30px"
                v-if="this.companyHomeForm.bottomPicName && this.companyHomeForm.bottomPicName!=''">
            {{this.companyHomeForm.bottomPicName}}
            <i class="el-icon-success" style="color: #80ff00"></i>
          </span>
        </el-form-item>
      </el-form>
      <div v-if="showPicBottomEdit==true" style="
    box-shadow: 0px 4px 16px 3px rgba(191, 199, 215, 0.31);
    margin-top: 30px;
    margin-bottom: 40px;
    padding-bottom: 40px;
    padding-top: 30px;
padding-left: 40px;
    max-width: 560px;">
        <el-form ref="companyHomeForm" :model="companyHomeForm">
          <el-form-item label="模块下方图片:">
            <el-upload
              class="thumbnail-uploader"
              :action="uploadPicOptions.action"
              :data="uploadPicOptions.params"
              :accept="uploadPicOptions.acceptFileType"
              :show-file-list="false"
              :on-success="handleUploadSuccessHomeBottom"
              :before-upload="beforeUpload1"
            >
              <el-image
                v-if="bottomPicUrl || companyHomeForm.bottomPicUrl"
                :src="bottomPicUrl || companyHomeForm.bottomPicUrl"
                v-loading="uploadPicOptions.loading"
                class="thumbnail"
              />
              <i v-else class="el-icon-plus thumbnail-uploader-icon"></i>
            </el-upload>
          </el-form-item>
          <el-form-item label="模块下方图片名称:" prop="bottomPicName" style="display: flex">
            <el-input
              v-model="companyHomeForm.bottomPicName"
              placeholder="请输入模块下方图片名称"
            ></el-input>
          </el-form-item>
          <el-form-item label="模块下方图片跳转链接:" prop="bottomPicTarget" style="display: flex">
            <el-input
              v-model="companyHomeForm.bottomPicTarget"
              placeholder="请输入模块下方图片跳转链接"
            ></el-input>
          </el-form-item>
        </el-form>
        <el-button style="margin-left: 300px;" @click="closeBottomPic">取消</el-button>
        <el-button style="margin-left: 10px;"  @click="saveBottomPic">保存</el-button>
      </div>
      <el-button  style="margin-left: 570px" @click="savePic">保存</el-button>
    </div>

  </div>
</template>

<script>
import { saveWebHomeConfig,
           getHomeConfig,
          getCarouselList,
          deleteCarousel,
          moveUpCarousel,
          moveDownCarousel,
          addCarousel
        } from "@/api/config_api";
import Toast from "@/utils/toast";
import {getUploadPicToken} from "@/api/upload_api"

export default {
  name: "ConfigPicPage",
  data() {
    return {
      showActivitiPic:true,
      showPicList:true,
      url:undefined,
      showUploadDiag:false,
      urlForm:{
        name:undefined,
        url:undefined,
        target:undefined
      },
      urlFormRule:{
        url:[{required: true, message: "请选择要上传的轮播图", trigger: "blur"}],
        name:[{required: true, message: "请输入轮播图名称", trigger: "blur"}],
        target:[{required: true, message: "请输入轮播图跳转链接", trigger: "blur"}]
      },
      carouselList:[],
      showPicEdit:false,
      showPicBottomEdit:false,
      webHomeForm:undefined,
      companyHomeForm: {
        configType:11,
        topPicUrl:undefined,
        topPicName:undefined,
        topPicTarget:undefined,//上方图跳转
        bottomPicUrl:undefined,
        bottomPicName:undefined,
        bottomPicTarget:undefined//下方图跳转
      },
      topPicUrl:undefined,
      bottomPicUrl:undefined,
      uploadPicOptions: {
        action: "",
        params: {},
        fileUrl: "",
        acceptFileType: ".jpg,.jpeg,.png,.JPG,.JPEG,.PNG",
      },
    };
  },
  created() {
    this.initData();
  },
  methods: {
    initData() {
      this.getCarouselList()
      this.getHomeConfig()
    },
    getCarouselList(){
      getCarouselList().then((response) => {
        console.log(response.data.list)
        this.carouselList=response.data.list
      })
    },
    getHomeConfig(){
      getHomeConfig().then(response => (this.webHomeForm = response.data));
    },
    openUploadDiag(){
      this.showUploadDiag=true
    },
    openPicEdit(){
      this.showPicEdit=true
    },
    openPicBottomEdit(){
      this.showPicBottomEdit=true
    },
    handleDeleteCarousel(index,row){
      console.log(row.id)
      let id=row.id
      this.deleteCarouselById(deleteCarousel, id);
      this.getCarouselList();
    },
    deleteCarouselById(delById, id){
      this.$confirm("是否要删除轮播图？", {
        confirmButtonText: "删除",
      }).then(() => {
        delById(id).then(() => {
          Toast.success("删除成功");
          this.getCarouselList()
        });
      });
    },
    handMoveUpCarousel(index,row){
      let id=row.id
      this.moveUpCarouselById(moveUpCarousel, id);
      this.getCarouselList();
    },
    moveUpCarouselById(moveUpById,id){
        moveUpById(id).then(() => {
          Toast.success("上升成功");
          this.getCarouselList();
        });
    },
    handMoveDownCarousel(index,row){
      let id=row.id
      this.moveDownCarouselById(moveDownCarousel, id);
      this.getCarouselList();
    },
    moveDownCarouselById(moveDownById,id){
        moveDownById(id).then(() => {
          Toast.success("下降成功");
          this.getCarouselList();
        });
    },
    saveWebHomeConfig() {
      saveHomeConfig(this.webHomeForm).then(() => {
        Toast.success("保存成功");
      });
    },
    saveCompanyHomeConfig() {
      saveHomeConfig(this.companyHomeForm).then(() => {
        Toast.success("保存成功");
      });
    },
    beforeUpload(file) {
      return new Promise((resolve, reject) => {
          getUploadPicToken(file.name).then((response) => {
            const {data} = response;
            this.uploadPicOptions.action = data.host;
            this.uploadPicOptions.params = data;
            this.uploadPicOptions.fileUrl = data.host + "/" + data.key;
            this.urlForm.url = data.host + "/" + data.key;
            this.url = URL.createObjectURL(file)
            this.companyHomeForm.topPicUrl=data.host + "/" + data.key;
            this.topPicUrl=URL.createObjectURL(file);
            this.bottomPicUrl=URL.createObjectURL(file)
            resolve(data);
          }).catch((error) => {
            reject(error);
          });
      });
    },
    beforeUpload1(file) {
      return new Promise((resolve, reject) => {
        getUploadPicToken(file.name).then((response) => {
          const {data} = response;
          this.uploadPicOptions.action = data.host;
          this.uploadPicOptions.params = data;
          this.uploadPicOptions.fileUrl = data.host + "/" + data.key;
          this.companyHomeForm.bottomPicUrl=data.host + "/" + data.key;
          this.bottomPicUrl=URL.createObjectURL(file);
          resolve(data);
        }).catch((error) => {
          reject(error);
        });
      });
    },
    handleUploadSuccessHomeTop(){
      this.companyHomeForm.topPicUrl=this.uploadPicOptions.fileUrl
    },
    handleUploadSuccessHomeBottom(){
      this.companyHomeForm.BottomPicUrl=this.uploadPicOptions.fileUrl
    },
    onUploadSuccess(){
      this.urlForm.url=this.uploadPicOptions.fileUrl
      console.log(this.urlForm.url)
    },
    closeSaveUpload(){
      this.showUploadDiag = false;
    },
    handleUploadSuccess() {
      this.urlForm.url=this.uploadPicOptions.fileUrl
      this.$refs["urlForm"].validate((valid) => {
        if (valid) {
          addCarousel(this.urlForm).then(() => {
            this.getCarouselList();
            this.showUploadDiag = false;
          })
        }
      })
    },
    saveTopPic(){
      this.companyHomeForm.topPicUrl=this.uploadPicOptions.fileUrl;
      this.showPicEdit=false;
    },
    closeTopPic(){
      this.showPicEdit=false;
    },
    saveBottomPic(){
      this.showPicBottomEdit=false;
    },
    closeBottomPic(){
      this.showPicBottomEdit=false;
    },
    savePic(){
      saveWebHomeConfig(this.companyHomeForm).then(()=>{
        this.getHomeConfig();
      })
    }
  },
};
</script>
<style lang="scss" scoped>
  .app-container{
    .title-1{
      width: 140px;
      height: 50px;
      background: #aaa69d;
      color: #FFFFFF;
      line-height: 54px;
      padding-left: 16px;
    }
    .change-sty{
      padding-left: 20px;
      padding-top: 20px;
    }
    .list-detail{
      margin-left:100px;
      ::v-deep.el-input__inner {
        -webkit-appearance: none;
        background-color: #FFF;
        background-image: none;
        border-radius: 4px;
        border: 1px solid #DCDFE6;
        -webkit-box-sizing: border-box;
        box-sizing: border-box;
        color: #606266;
        display: inline-block;
        font-size: inherit;
        height: 40px;
        line-height: 40px;
        outline: 0;
        padding: 0 15px;
        -webkit-transition: border-color .2s cubic-bezier(.645,.045,.355,1);
        transition: border-color .2s cubic-bezier(.645,.045,.355,1);
        width: 300px;
      }
      .thumbnail-uploader{
        max-width: 300px;
        max-height: 200px;
        ::v-deep.el-upload {
          display: inline-block;
          text-align: center;
          cursor: pointer;
          outline: 0;
          border: 1px solid #aaa69d;
          max-width: 300px;
          min-width: 100px;
          max-height: 200px;
          min-height: 100px;
          line-height: 100px;
        }
        .thumbnail{
          max-width: 300px;
          max-height: 200px;

        }
      }
      .btn-upload{
        margin-left: 98px;
      }

    .tablist-style{
      margin-top: 20px;
      margin-left: 100px;
      width: 600px;
      border-color: #afc1ef;
    }
      .EditUplaod{
        margin-left: 99px;
        width: 545px;
        .btn-sava{
          margin-left: 270px;
        }
      }
    }
  }
</style>
