<template>
  <div class="app-container">
    <div
      class="left-container"
      :style="{ top: top }"
      v-if="industryList.length > 0 && showMenu"
    >
      <el-link
        class="left-item"
        :type="item.select ? 'primary' : 'default'"
        :underline="false"
        v-for="item in industryList"
        :key="item.id"
        @click="onLink(item)"
      >
        {{ item.name + " " + (item.total || "") }}
      </el-link>
    </div>

    <div class="main-container">
      <div
        class="company-list-container"
        v-for="item in industryList"
        :key="item.id"
      >
        <div
          class="company-name-container"
          v-if="companyList[item.id] && companyList[item.id].length > 0"
        >
          <div class="company-name" :ref="item.name">{{ item.name }}</div>
          <el-button
            :underline="false"
            type="text"
            class="company-name-text"
            v-if="companyList[item.id].length > companyCount"
            @click="onMore(item.id)"
            >更多
            <el-image
              :src="require('@/assets/more.png')"
              class="company-name-icon"
            ></el-image>
          </el-button>
        </div>
        <div
          class="company-item-container"
          v-if="companyList[item.id] && companyList[item.id].length > 0"
        >
          <el-card
            shadow="hover"
            :class="[
              'item-container',
              { 'item-container-4': companyCount === 4 },
              { 'item-container-3': companyCount === 3 },
              { 'item-container-2': companyCount === 2 },
              { 'item-container-1': companyCount === 1 },
            ]"
            v-for="(company, index) in companyList[item.id]"
            :key="company.id"
            v-if="index < companyCount"
            @click.native="openCompanyDetail(company)"
          >
            <el-image
              class="item-logo"
              :src="company.logo"
              fit="contain"
            ></el-image>
            <div>
              <div class="text-small text-gray" style="margin-bottom: 4px">
                {{ company.name }}
              </div>
              <div class="text-small text-gray" style="min-height: 42px">
                <span v-if="company.stage">{{ company.stage.name }}</span>
                <span v-if="company.property">
                  | {{ company.property.name }}</span
                >
                <span v-if="company.industry">
                  | {{ company.industry.name }}</span
                >
                <span v-if="company.scale"> | {{ company.scale.name }}</span>
              </div>
            </div>
          </el-card>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { listByType } from "@/api/dict_api";

export default {
  name: "WikiCardPage",
  data() {
    return {
      exporting: false,
      industryList: [], //行业列表
      companyList: {}, // 公司列表
      companyCount: 4, // 显示公司数
      showMenu: false, //
      top: "93px", // 目录距离顶部距离
    };
  },

  created() {
    this.initData();
    window.addEventListener("scroll", () => {
      let scrollTop =
        document.documentElement.scrollTop || document.body.scrollTop;
      let top = scrollTop < 88 ? 88 - scrollTop : 0;
      this.top = top + 5 + "px";
    });
  },
  mounted() {
    this.handleWidthChange(window.innerWidth);
    window.onresize = () => {
      this.handleWidthChange(window.innerWidth);
    };
  },
  methods: {
    initData() {
      listByType(6).then((response) => {
        this.industryList = response.data.list;
        if (this.industryList.length > 0) {
          this.$set(this.industryList[0], "select", true);
        }
        this.getCompanyList();
      });
    },

    onLink(item) {
      // 修改选中状态
      this.industryList.map((item) => {
        this.$set(item, "select", false);
        return item;
      });
      this.$set(item, "select", true);

      let card = this.$refs[item.name];
      if (card) {
        document.body.scrollTop = card[0].offsetTop + 88;
        document.documentElement.scrollTop = card[0].offsetTop + 88;
      }
    },

    getCompanyList() {
      this.industryList.forEach((item) => {
        this.getItem(item);
      });
    },

    getItem(item) {
      this.$axios
        .get("/company/list-wiki-by-industry", {
          params: { industryId: item.id, sort: '-priority'},
        })
        .then((data) => {
          this.$set(this.companyList, item.id, data.data.list);
          this.$set(item, "total", data.data.total);

          setTimeout(() => {
            this.showMenu = true;
          }, 500);
        });
    },

    openCompanyDetail(company) {
      this.$router.push(`/company/${company.id}`);
    },

    onMore(industryId) {
      this.$router.push({
        path: "/wiki-list",
        query: { industryId: industryId },
      });
    },

    handleWidthChange(width) {
      if (width > 1140) {
        this.companyCount = 4;
      } else if (width > 866) {
        this.companyCount = 3;
      } else if (width > 590) {
        this.companyCount = 2;
      } else {
        this.companyCount = 1;
      }
    },
  },
};
</script>


<style scoped lang="scss">
.app-container {
  max-width: 1140px;
  margin: 0 auto;
  padding-left: 80px;
  position: relative;

  .left-container {
    position: fixed;
    left: 60px;
    overflow-y: auto;

    .left-item {
      display: block;
      font-size: 14px;
      margin-bottom: 20px;
      line-height: 16px;
    }
  }

  .company-list-container {
    .company-name-container {
      display: flex;
      justify-content: space-between;
      padding-right: 16px;

      .company-name {
        font-size: 18px;
        font-weight: bold;
        color: #000;
      }

      .company-name-text {
        height: 40px;
        font-size: 14px;
        font-weight: 400;
        color: #999999;
        line-height: 20px;
      }

      .company-name-icon {
        width: 8px;
        height: 12px;
      }

      .company-count {
        margin-left: 8px;
        font-size: 12px;
        color: #cccccc;
      }
    }

    .company-item-container {
      display: flex;
      align-items: center;
      margin: 10px 0 20px;

      .item-container {
        margin-right: 10px;
        display: flex;
        flex-direction: column;
        align-items: center;

        .item-logo {
          height: 100px;
          max-width: 250px;
          width: 100%;
        }

        &:hover {
          cursor: pointer;
        }
      }

      .item-container:last-of-type {
        margin-right: 0;
      }

      .item-container-4 {
        width: 24%;
      }

      .item-container-3 {
        width: 32%;
      }

      .item-container-2 {
        width: 49%;
      }

      .item-container-1 {
        width: 100%;
      }
    }
  }
}

@media screen and (max-width: 1530px) {
  .app-container {
    .left-container {
      display: none;
    }
  }
}
</style>
