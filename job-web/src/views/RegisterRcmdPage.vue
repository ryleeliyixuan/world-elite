<template>
  <div class="app-container">
    <div class="login-box text-center">
      <div class="header1 text">WE精选推荐</div>
      <div v-if="recommendJobList && recommendJobList.length > 0">
        <div class="header2 text">你可能感兴趣的职位</div>
        <div class="card-container">
          <div
            class="card"
            v-for="item in recommendJobList"
            :key="item.id"
            @click="goToDetail(`job`, item.object.id)"
          >
            <div class="line1 text-center">
              <span class="line1-name" v-if="item.object.name">{{
                item.object.name
              }}</span>
              <!-- 现在返回的数据结构里没有salary了。但是有minsalary maxsalary -->
              <span
                class="line1-number"
                v-if="item.object.salary && item.object.salary.name"
                >{{ item.object.salary.name }}</span
              >
              <span class="line1-number" v-else
                >{{ item.object.minSalary }}k-{{ item.object.maxSalary }}k</span
              >
              <el-button
                circle
                class="flag"
                :style="
                  item.object.favoriteFlag === 1
                    ? `background: #ff3d00`
                    : `background: #FFFFFF;`
                "
                @click.stop="
                  handleFavorite(item.object.id, item.object.favoriteFlag, 1)
                "
                @mouseenter.native.prevent="MouseInFav(item)"
                @mouseleave.native.prevent="MouseOutFav(item)"
                ><svg-icon
                  :icon-class="
                    item.object.favoriteFlag === 1 ? 'jobflag' : 'jobunflag'
                  "
                  style="height: 10px; width: 11px"
              /></el-button>
            </div>
            <div class="line2 text-center"></div>
            <div class="line3 text-center">
              <el-avatar
                :size="40"
                :src="item.object.companyUser.avatar"
              ></el-avatar>
            </div>
            <div class="line4 text-center">根据您的求职意向推荐</div>
          </div>
        </div>
      </div>
      <div v-if="recommendCompanyList && recommendCompanyList.length > 0">
        <div class="header2 text">你可能感兴趣的雇主</div>
        <div class="card-container">
          <div
            class="card"
            v-for="item in recommendCompanyList"
            :key="item.id"
            @click="goToDetail(`company`, item.object.id)"
          >
            <div class="comp-line1 text-center">
              <el-avatar :size="40" :src="item.object.logo"></el-avatar>
              <el-button
                circle
                class="flag"
                :style="
                  item.object.favoriteFlag === 1
                    ? `background: #ff3d00`
                    : `background: #FFFFFF;`
                "
                @click.stop="
                  handleFavorite(item.object.id, item.object.favoriteFlag, 2)
                "
                @mouseenter.native.prevent="MouseInFav(item)"
                @mouseleave.native.prevent="MouseOutFav(item)"
                ><svg-icon
                  :icon-class="
                    item.object.favoriteFlag === 1 ? 'jobflag' : 'jobunflag'
                  "
                  style="height: 10px; width: 11px"
              /></el-button>
            </div>
            <div class="comp-line2 text-center" v-if="item.object.name">
              {{ item.object.name }}
            </div>
            <div class="line4 text-center">根据您求职意向的行业推荐</div>
            <div
              class="comp-line4 text-center"
              v-if="item.object.favoriteNumber"
            >
              {{ item.object.favoriteNumber }}人已收藏
            </div>
            <!-- 没有favoriteNumber这个字段的，就当作0人收藏 -->
            <div class="comp-line4 text-center" v-else>0人已收藏</div>
          </div>
        </div>
      </div>
      <div class="fav-all">
        <el-button style="background: #ffebee; color: #333333" @click="favAll()"
          ><svg-icon
            icon-class="register-rcmd-heart"
            style="height: 16px; width: 21px; margin-right: 6px"
          />收藏全部</el-button
        >
      </div>
      <div class="footer">
        <el-button style="background: #eceff1; color: #666666" @click="goPrev"
          >上一步</el-button
        >
        <el-button @click="goNext">下一步</el-button>
      </div>
    </div>
  </div>
</template>

<script>
import { getRecommendList } from "@/api/recommend_api";
import { doAllFavorite, doFavorite } from "@/api/favorite_api";

export default {
  name: "RegisterRcmdPage",

  data() {
    return {
      recommendJobList: [],
      recommendCompanyList: [],
    };
  },
  created() {
    this.initData();
    this.$emit("complete");
  },
  methods: {
    initData() {
      //获取推荐职位
      getRecommendList({
        objectType: 1, // 职位
        page: 1,
        limit: 4,
        sort: "+position",
      }).then((response) => {
        this.recommendJobList = response.data.list;
      });
      //获取推荐雇主
      getRecommendList({
        objectType: 2, // 公司
        page: 1,
        limit: 4,
        sort: "+position",
      }).then((response) => {
        this.recommendCompanyList = response.data.list;
        this.$emit("complete");
      });
    },
    //收藏某一个职位或公司
    handleFavorite(id, favorite, type) {
      let data = {
        favorite: Boolean(favorite),
        objectId: id,
        type: type, //1 职位 2 公司
      };
      doFavorite(data).then(() => {
        this.initData();
        this.$message("操作成功");
      });
    },
    //收藏键悬浮鼠标特效 - in
    MouseInFav(item) {
      item.object.favoriteFlag === 1
        ? (item.object.favoriteFlag = 0)
        : (item.object.favoriteFlag = 1);
    },
    //收藏键悬浮鼠标特效 - on
    MouseOutFav() {
      this.initData();
    },
    //收藏该页面全部 1.职位 2.公司
    favAll() {
      //收藏全部职位
      let jobIds = [];
      this.recommendJobList.forEach((item) => jobIds.push(item.object.id));
      //收藏全部公司
      let compIds = [];
      this.recommendCompanyList.forEach((item) => compIds.push(item.object.id));

      doAllFavorite({
        favorite: true,
        objectIdList: jobIds,
        type: 1,
      }).then(() => {
        this.initData();
        this.$message("收藏全部职位成功");
      });
      doAllFavorite({ favorite: true, objectIdList: compIds, type: 2 }).then(
        () => {
          this.initData();
          this.$message("收藏全部雇主成功");
        }
      );
    },
    //下一步
    goPrev() {
      this.$router.push("/register-job-orientation");
    },
    //上一步
    goNext() {
      this.$router.push("/register-success");
    },
    //跳转链接 1.职位 2.公司
    goToDetail(page, id) {
      this.$router.push(`${page}/${id}`);
    },
  },
};
</script>

<style scoped lang="scss">
/deep/ .el-button {
  border: 0px;
}

.app-container {
  max-width: 1200px;
  .bold {
    font-size: 36px;
    font-family: Roboto-Medium, Roboto;
    font-weight: 500;
    color: #333333;
    line-height: 39px;
  }

  .login-box {
    height: auto;
    box-shadow: 0px 4px 16px 3px rgba(191, 199, 215, 0.31);
    border-radius: 24px;
    background: #ffffff;
    margin-top: 35px;
    margin-bottom: 88px;
    padding-top: 21px;
    padding-bottom: 68px;
    .text {
      font-size: 24px;
      font-family: PingFangSC-Medium, PingFang SC;
      font-weight: 500;
      color: #333333;
      line-height: 33px;
    }
    .header1 {
      margin-bottom: 32px;
    }
    .header2 {
      font-size: 18px;
      font-family: PingFangSC-Medium, PingFang SC;
      font-weight: 500;
      color: #333333;
      line-height: 25px;
      margin-bottom: 16px;
    }

    .card-container {
      display: flex;
      justify-content: space-between;
      padding: 0 88px;
      margin-bottom: 31px;
      .card:hover {
        box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.2);
      }

      .card {
        width: 237px;
        height: 129px;
        background: #f3f7fe;
        border-radius: 13px;
        border: 0px;
        padding: 7px 12px;
        justify-content: space-between;
        cursor: pointer;

        .comp-line1 {
          display: flex;
          justify-content: center;
          align-items: flex-start;

          /deep/.el-avatar {
            margin-left: 40%;
          }
          /deep/.el-button {
            margin-left: 30%;
          }
        }

        .comp-line2 {
          font-size: 18px;
          font-family: PingFangSC-Medium, PingFang SC;
          font-weight: 500;
          color: #333333;
          line-height: 25px;
        }

        .comp-line4 {
          font-size: 12px;
          font-family: PingFangSC-Regular, PingFang SC;
          font-weight: 400;
          color: #4895ef;
          line-height: 17px;
        }

        .line1 {
          display: flex;
          justify-content: space-between;

          .line1-name {
            max-width: 100px;
            height: 22px;
            font-size: 16px;
            font-family: PingFangSC-Medium, PingFang SC;
            font-weight: 500;
            color: #333333;
            line-height: 22px;
            overflow: hidden;
            white-space: nowrap;
            text-overflow: ellipsis;
            -o-text-overflow: ellipsis;
          }

          .line1-number {
            font-size: 16px;
            font-family: PingFangSC-Medium, PingFang SC;
            font-weight: 500;
            color: #f44336;
            line-height: 22px;
          }
        }

        .line2 {
          font-size: 14px;
          font-family: PingFangSC-Regular, PingFang SC;
          font-weight: 400;
          color: #333333;
          line-height: 20px;
        }

        .line4 {
          font-size: 12px;
          font-family: PingFangSC-Regular, PingFang SC;
          font-weight: 400;
          color: #999999;
          line-height: 17px;
        }

        /deep/.el-button {
          height: 21px;
          width: 21px;
          display: flex;
          justify-content: center;
          align-items: center;
          margin: 0;
          box-shadow: 0px 5px 13px 0px #c0cde3;
          border: 0px;

          span {
            display: flex;
          }
        }

        /deep/.el-button:hover {
          background: #ff3d00;
        }
      }
    }

    .fav-all {
      margin-top: 31px;
      margin-bottom: 26px;
      /deep/.el-button {
        span {
          display: flex;
          justify-content: center;
          align-items: center;
        }
      }
    }

    .footer {
      display: flex;
      justify-content: center;

      /deep/.el-button {
        margin: 0;
      }

      .el-button + .el-button {
        margin-left: 10px;
      }
    }

    /deep/.el-button {
      width: 151px;
      height: 35px;
      background: #4895ef;
      border-radius: 18px;
      display: flex;
      align-items: center;
      justify-content: center;
      margin: auto;
      color: #ffffff;
    }
  }
}

@media screen and (max-width: 1120px) {
  .app-container {
    padding: 20px 20px 0;

    .login-box {
      height: auto;
      .card-container {
        padding: 0 44px;
        display: flex;
        flex-wrap: wrap;
        .card {
          margin: 5px;
        }
      }
    }
  }
}

@media screen and (max-width: 450px) {
  .app-container {
    .login-box {
      padding-right: 10px;
      padding-left: 10px;
      .card-container {
        padding: 0 12px;
        justify-content: center;
      }
    }
  }
}
</style>
