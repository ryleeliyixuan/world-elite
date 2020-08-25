<template>
  <div>
    <div class="app-footer">
      <el-row :gutter="20" type="flex" justify="space-between">
        <el-col :span="6">
          <h5 class="mb-3">友情链接</h5>
          <div>
            <el-link class="link-text" href="http://www.myworldelite.com/">{{$t('app_name')}}</el-link>
          </div>
          <div>
            <el-link class="link-text" href="http://company.myworldelite.com/">{{$t('app_name')}}企业端</el-link>
          </div>
        </el-col>
        <el-col :span="6">
          <h5 class="mb-3">用户帮助</h5>
          <div>
            <el-link class="link-text" href="http://www.myworldelite.com/user-protocol">用户协议以及隐私政策</el-link>
          </div>
        </el-col>
        <el-col :span="6">
          <h5 class="mb-3">联系我们</h5>
          <a class="link-text" href="mailto:career@myworldelite.com">邮箱：career@myworldelite.com</a>
        </el-col>
        <el-col :span="6">
          <div class="link-text text-center">
            <el-image class="img-qr-wechat" src="https://oss.myworldelite.com/static/worldelite-gongzhonghao.jpg"></el-image>
            <div class="text-center">微信公众号</div>
          </div>
        </el-col>
      </el-row>
    </div>
    <div class="footer-copyright text-small text-center">
        <span><a href="http://www.beian.miit.gov.cn/">蜀ICP备20009332</a></span>
        <span class="ml-4">©2020 成都寰球菁英信息科技有限公司</span>
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
  margin-top: 50px;
  padding: 40px 120px;
  background: #f7fbfd;
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
  border-top: 1px solid rgb(245, 238, 238);
  padding: 20px 120px;
  background: #f7fbfd;
}
</style>
