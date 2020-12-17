<template>
  <div class="app-container edit-activity">
    <el-form
      :model="activityForm"
      :rules="activityFormRules"
      ref="activityForm"
      label-width="100px"
    >
      <el-form-item label="活动图片" prop="thumbnail">
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
            v-if="thumbnail || activityForm.thumbnail"
            :src="thumbnail || activityForm.thumbnail"
            v-loading="uploadPicOptions.loading"
            class="thumbnail"
          />
          <i v-else class="el-icon-plus thumbnail-uploader-icon"></i>
        </el-upload>
      </el-form-item>
      <el-form-item label="活动名称" prop="title">
        <el-input v-model="activityForm.title" placeholder="填写活动名称" maxlength="80" show-word-limit></el-input>
      </el-form-item>
      <el-form-item label="主办方" prop="organizer">
        <el-input v-model="activityForm.organizer" placeholder="填写主办方" maxlength="100" show-word-limit></el-input>
      </el-form-item>
      <el-form-item label="活动城市" prop="cityId">
        <!--        -->
        <!--        <el-select v-model="activityForm.cityId" filterable clearable placeholder="所在城市">-->
        <!--          <el-option v-for="item in cityOptions" :key="item.id" :label="item.name" :value="item.id"></el-option>-->
        <!--        </el-select>-->
        <el-cascader placeholder="请选择工作城市"
                     :show-all-levels="true"
                     :options="cityOptions"
                     :props="cityIdProps"
                     filterable
                     clearable
                     v-model="activityForm.cityId">
        </el-cascader>

      </el-form-item>
      <el-form-item label="活动地点" prop="address">
        <el-input
          v-model="activityForm.address"
          placeholder="填写活动地点"
          maxlength="100"
          show-word-limit
        ></el-input>
      </el-form-item>
      <el-form-item label="活动时间" prop="timeRange">
        <el-date-picker
          v-model="activityForm.timeRange"
          value-format="yyyy-MM-dd HH:mm"
          format="yyyy-MM-dd HH:mm"
          type="datetimerange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="活动摘要" prop="summary">
        <el-input
          type="textarea"
          placeholder="输入活动摘要"
          v-model="activityForm.summary"
          maxlength="200"
          rows="3"
          show-word-limit
        ></el-input>
      </el-form-item>
      <el-form-item label="活动详情" prop="description" v-sticky="{stickyTop: 2}">
        <EditorView v-bind:content="activityForm.description" v-on:updateContent="activityForm.description = $event"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleSave" :loading="saveLoading">保存</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
  import { saveActivity, getActivityInfo } from '@/api/activity_api'
  import { listByType } from '@/api/dict_api'
  import { getUploadPicToken } from '@/api/upload_api'
  import VueSticky from 'vue-sticky'
  import EditorView from '@/components/EditorView'

  export default {
    name: 'ActivityList',
    components: {
      EditorView
    },
    directives: {
      sticky: VueSticky
    },
    data() {
      return {
        thumbnail: undefined,
        activityForm: {
          id: undefined,
          title: undefined,
          thumbnail: undefined,
          summary: undefined,
          cityId: undefined,
          description: undefined,
          startTime: undefined,
          finishTime: undefined,
          timeRange: [],
          address: undefined,
          organizer: undefined
        },
        activityFormRules: {
          title: [{ required: true, message: '请输入活动名称', trigger: 'blur' }],
          thumbnail: [
            { required: true, message: '请输入活动图片', trigger: 'change' }
          ],
          summary: [
            { required: true, message: '请输入活动文案', trigger: 'blur' }
          ],
          description: [
            { required: true, message: '请输入活动详情', trigger: 'blur' }
          ],
          cityId: [
            { required: true, message: '请选择活动城市', trigger: 'change' }
          ],
          timeRange: [
            { required: true, message: '请选择活动时间', trigger: 'change' }
          ],
          address: [
            { required: true, message: '请输入活动地址', trigger: 'blur' }
          ],
          organizer: [
            { required: true, message: '请输入主办方', trigger: 'blur' }
          ]
        },
        cityOptions: [{id: 1, name: "国内", children:[{id: 0, name: "加载中"}]}, {id: 2, name: "国外"}],
        uploadPicOptions: {
          action: '',
          params: {},
          fileUrl: '',
          acceptFileType: '.jpg,.jpeg,.png,.JPG,.JPEG,.PNG',
          loading: false
        },
        saveLoading: false,
        cityIdProps: {
          lazy: false,
          // lazyLoad: (node, resolve) => {
          //   if (node.level === 1) {
          //     this.$axios.request({
          //       url: '/city/list',
          //       method: 'get',
          //       params: { type: node.value }
          //     }).then(data => {
          //       console.log(data.data)
          //       let nodes = data.data.map(second => {
          //         let children = second.children && second.children.map(third => {
          //           return { id: third.id, name: third.name, leaf: true }
          //         })
          //         return { id: second.id, name: second.name, children }
          //       })
          //       resolve(nodes)
          //     })
          //   } else {
          //     resolve()
          //   }
          // },
          expandTrigger: 'hover',
          value: 'id',
          label: 'name',
          emitPath: false,
          children: 'children'
        }
      }
    },
    created() {
      this.initData()
    },
    watch: {
      'activityForm.timeRange': function() {
        if (
          this.activityForm.timeRange &&
          this.activityForm.timeRange.length == 2
        ) {
          this.activityForm.startTime = this.activityForm.timeRange[0]
          this.activityForm.finishTime = this.activityForm.timeRange[1]
        } else {
          this.activityForm.startTime = undefined
          this.activityForm.finishTime = undefined
        }
      }
    },
    methods: {
      initData() {
        // listByType(2).then(response => (this.cityOptions = response.data.list));
        this.getActivityInfo()

        this.$axios.request({
          url: "/city/list",
          method: "get",
          params: {type: 1}
        }).then(data => {
          this.cityOptions[0].children = data.data.map(second => {
            let children = second.children && second.children.map(third => {
              return {id: third.id, name: third.name, leaf: true}
            })
            return {id: second.id, name: second.name, children}
          });
        })

        this.$axios.request({
          url: "/city/list",
          method: "get",
          params: {type: 2}
        }).then(data => {
          this.cityOptions[1].children = data.data.map(second => {
            let children = second.children && second.children.map(third => {
              return {id: third.id, name: third.name, leaf: true}
            })
            return {id: second.id, name: second.name, children}
          });
        })
      },
      getActivityInfo() {
        const activityId = this.$route.query.id
        if (activityId) {
          getActivityInfo(activityId).then(response => {
            const { data } = response
            Object.keys(data).forEach(key => {
              this.activityForm[key] = data[key]
            })
            this.activityForm.cityId = data.city.id
            console.log(this.activityForm)
            this.activityForm.timeRange = [
              this.activityForm.startTime,
              this.activityForm.finishTime
            ]
          })
        }
      },
      handleSave() {
        this.$refs['activityForm'].validate(valid => {
          if (valid) {
            this.saveLoading = true
            saveActivity(this.activityForm)
              .then(() => {
                this.$message('保存成功')
                this.$router.go(-1)
              })
              .finally(() => {
                this.saveLoading = false
              })
          }
        })
      },
      beforeUpload(file) {
        return new Promise((resolve, reject) => {
          this.uploadPicOptions.loading = true
          getUploadPicToken(file.name)
            .then(response => {
              const { data } = response
              console.log(data)
              this.uploadPicOptions.action = data.host
              this.uploadPicOptions.params = data
              this.uploadPicOptions.fileUrl = data.host + '/' + data.key
              this.activityForm.thumbnail = data.host + '/' + data.key
              this.thumbnail = URL.createObjectURL(file)
              resolve(data)
            })
            .catch(error => {
              this.uploadPicOptions.loading = false
              reject(error)
            })
        })
      },
      handleUploadSuccess() {
        this.uploadPicOptions.loading = false
      }
    }
  }
</script>

<style lang="scss">
  .edit-activity {
    .thumbnail-uploader .el-upload {
      cursor: pointer;
      position: relative;
      overflow: hidden;
    }

    .thumbnail-uploader .el-upload:hover {
      border-color: #ccc;
    }

    $avatarSize: 200px;

    .thumbnail-uploader .thumbnail-uploader-icon {
      border: 1px dashed #d9d9d9;
      font-size: 28px;
      color: #8c939d;
      width: $avatarSize;
      height: $avatarSize;
      line-height: $avatarSize;
      text-align: center;
    }

    .thumbnail-uploader .thumbnail {
      width: $avatarSize;
      height: $avatarSize;
      display: block;
    }

    .ql-container .ql-editor {
      height: calc(100vh - 220px);
      font-size: 1rem;
    }

    .ql-toolbar {
      line-height: normal;
    }
  }
</style>
