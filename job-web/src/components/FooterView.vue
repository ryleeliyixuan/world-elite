<template>
  <div>
    <div class="app-footer">
      <el-row :gutter="20" type="flex" justify="space-between">
        <el-col :span="6">
          <h5 class="mb-3">友情链接</h5>
          <div>
            <el-link class="link-text">帮助中心</el-link>
          </div>
          <div>
            <el-link class="link-text">帮助中心</el-link>
          </div>
          <div>
            <el-link class="link-text">帮助中心</el-link>
          </div>
        </el-col>
        <el-col :span="6">
          <h5 class="mb-3">用户帮助</h5>
          <div>
            <el-link class="link-text">帮助中心</el-link>
          </div>
          <div>
            <el-link class="link-text">用户服务协议</el-link>
          </div>
          <div>
            <el-link class="link-text">隐私政策</el-link>
          </div>
        </el-col>
        <el-col :span="6">
          <h5 class="mb-3">联系我们</h5>
          <div class="link-text">商务服务：cc@ddd.com</div>
          <div class="link-text">反馈建议：cc@dd.com</div>
        </el-col>
        <el-col :span="6">
          <div class="link-text text-center">
            <el-image class="img-qr-wechat"></el-image>
            <div class="text-center">微信公众号</div>
          </div>
        </el-col>
      </el-row>
    </div>
    <div class="footer-copyright text-small text-center">
        <span>京ICP备14023790号-2</span>
        <span class="ml-4">©2020 成都寰球菁英信息科技有限公司</span>
        <span span class="ml-4">Agile Studio工作室提供技术支持</span>
    </div>
  </div>
</template>

<script>
import { mapGetters, mapMutations } from "vuex";
import { getUnReadMessageCount, getMessageList } from "@/api/message_api";

export default {
  name: "FooterView",
  data() {
    return {};
  },
  methods: {}
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.app-footer {
  border-top: 1px solid #eee;
  margin-top: 50px;
  padding: 40px 120px;
  background: #fefefe;
}
.link-text {
  font-size: 14px;
  font-weight: 400;
  margin-bottom: 5px;
}
.img-qr-wechat {
  width: 100px;
  height: 100px;
}
.footer-copyright {
  padding: 20px 120px;
  background: #efefef;
}
</style>
