<template>
  <div class="app-container container">
    <!-- 百科头页 -->
    <div v-if="company" class="company-wiki-header-container">
      <div class="company-info-container">
        <div class="company-info">
          <img
            class="company-info-banner"
            v-if="company.banner"
            :src="company.banner"
            :alt="company.fullName"
          />
          <img
            class="company-info-banner"
            v-else
            src="../assets/defaultbanner.png"
            alt=""
          />
          <div class="company-info-description">
            <el-avatar
              class="company-info-logo"
              :src="company.logo"
              :alt="company.fullName"
            ></el-avatar>
            <h5 class="mt-0">{{ company.name }}</h5>
            <p style="margin-bottom: 8px">
              <span v-if="company.stage">{{ company.stage.name }} |</span>
              <span v-if="company.property">
                {{ company.property.name }} |</span
              >
              <span v-if="company.industry">
                {{ company.industry.name }} |</span
              >
              <span v-if="company.scale"> {{ company.scale.name }}</span>
            </p>
            <div
              style="margin-bottom: 16px; display: flex"
              class="company-info-rating"
            >
              <el-tooltip
                class="item"
                effect="dark"
                content="星级呈现该企业在本网站的综评分"
                placement="left"
              >
                <el-rate
                  v-model="companyRating"
                  disabled
                  show-score
                  text-color="#ff9900"
                  score-template="{value}"
                >
                </el-rate>
              </el-tooltip>
              <el-button
                type="text"
                @click="getMyScore(companyId), (dialogVisible = true)"
                size="mini"
                style="padding: 0; margin-left: 10px"
                >评分</el-button
              >
              <el-dialog :visible.sync="dialogVisible" width="80%" show-close>
                <div class="mb-4" style="color: grey">
                  星级呈现该企业在本网站的综评分
                </div>
                <el-form
                  ref="scoreForm"
                  :model="scoreForm"
                  :rules="scoreFormRules"
                  label-width="100px"
                >
                  <div class="mb-2" style="display: flex">
                    给个评分吧： <el-rate v-model="scoreForm.score"></el-rate>
                  </div>
                  <el-input
                    type="textarea"
                    :rows="6"
                    placeholder="文明社会，从理性讨论开始。"
                    v-model="scoreForm.content"
                    style="margin-bottom: 12px"
                    resize="none"
                  >
                  </el-input>
                  <el-checkbox v-model="scoreForm.anonymous"
                    >匿名发表</el-checkbox
                  >
                </el-form>
                <span slot="footer" class="dialog-footer">
                  <el-button
                    type="primary"
                    @click="saveScore(), (dialogVisible = false)"
                  >
                    {{ hasMyScore == true ? "修改我的评分" : "提交" }}
                  </el-button>
                  <el-button @click="dialogVisible = false">取 消</el-button>
                </span>
              </el-dialog>
            </div>
            <el-button
              type="primary"
              size="small"
              :loading="favoriteLoading"
              @click="handleFavorite"
            >
              {{ company.favoriteFlag === 1 ? "已订阅百科" : "订阅百科" }}
            </el-button>
          </div>
          <div class="company-info-stat">
            <el-row
              type="flex"
              class="row-bg"
              justify="space-between"
              size="mini"
            >
              <el-col>
                <el-tag type="info" effect="dark"
                  >已进驻： <span>{{ company.listed || "0" }}</span></el-tag
                >
              </el-col>
              <el-col :span="12">
                <el-tag type="info" effect="dark"
                  >已订阅： <span>{{ company.substribed || "0" }}</span></el-tag
                >
              </el-col>
            </el-row>
          </div>
        </div>
      </div>
    </div>
    <!-- 百科内容 -->
    <div v-if="company" class="company-wiki-content-container">
      <el-menu
        class="mb-4"
        :default-active="tabIndex"
        mode="horizontal"
        @select="handleSelectTab"
      >
        <el-menu-item index="intro">首页</el-menu-item>
        <el-menu-item index="job">岗位</el-menu-item>
        <el-menu-item index="activity">活动</el-menu-item>
        <el-menu-item index="community">圈子</el-menu-item>
      </el-menu>
      <!-- 首页 -->
      <div class="intro-box" v-if="tabIndex == 'intro'">
        <el-divider></el-divider>
        <div class="intro-summary">
          <div class="intro-summary-img">
            <el-image
              :src="summaryImgUrl"
              :alt="company.fullName"
              fit="cover"
            ></el-image>
          </div>
          <div class="intro-summary-text">
            {{ company.wikiSummary }}
          </div>
        </div>
        <el-divider></el-divider>
        <div class="intro-detail">
          <el-row :gutter="36">
            <el-col
              class="intro-address intro-detail-element"
              :xs="24"
              :sm="24"
              :md="12"
              :lg="12"
              :xl="12"
            >
              <h5 class="mt-4 mb-4">
                <i class="el-icon-location" style="color: #1e90ff"></i>
                公司地址
              </h5>
              <el-collapse
                v-model="activeAddress"
                accordion
                v-for="(addr, index) in addressList"
                :key="addr.id"
              >
                <el-collapse-item :title="addr.address" :name="index">
                  <div class="map-box">
                    <el-amap
                      :vid="'amap' + index"
                      :zoom="mapZoom"
                      :center="addr.mapWindow.position"
                    >
                      <el-amap-info-window
                        :position="addr.mapWindow.position"
                        :content="addr.mapWindow.content"
                      ></el-amap-info-window>
                    </el-amap>
                  </div>
                </el-collapse-item>
              </el-collapse>
            </el-col>
            <el-col
              class="intro-employee intro-detail-element"
              :xs="24"
              :sm="24"
              :md="12"
              :lg="12"
              :xl="12"
            >
              <h5 class="mt-4 mb-4">
                <i class="el-icon-s-cooperation" style="color: #1e90ff"></i>
                雇员数量
              </h5>
              <BarChart
                :items="employeeData"
                class="intro-employee-chart"
              ></BarChart>
            </el-col>
            <el-col
              class="intro-product intro-detail-element"
              :xs="24"
              :sm="24"
              :md="12"
              :lg="12"
              :xl="12"
            >
              <h5 class="mt-4 mb-4">
                <i class="el-icon-s-flag" style="color: #1e90ff"></i> 旗下产品
              </h5>
              <el-carousel height="380px" :interval="2500" arrow="always">
                <el-carousel-item
                  v-for="product in productList"
                  :key="product.img"
                >
                  <img
                    class="intro-product-image"
                    :src="product.img"
                    :alt="product.alt"
                    fit="cover"
                    v-on:click="select(product)"
                  />
                </el-carousel-item>
              </el-carousel>
            </el-col>
            <el-col
              class="intro-valuation intro-detail-element"
              :xs="24"
              :sm="24"
              :md="12"
              :lg="12"
              :xl="12"
            >
              <h5 class="mt-4 mb-4">
                <i class="el-icon-s-marketing" style="color: #1e90ff"></i>
                市值情况
              </h5>
              <el-card class="intro-valuation-stockstats" style="height: 380px">
                <div class="stockstats-basicinfo stockstats-section">
                  <el-row>
                    <el-col :span="12">
                      <h5>{{ ticker }}</h5>
                    </el-col>
                    <el-col :span="12">
                      <span>{{ timestamp }}</span>
                    </el-col>
                  </el-row>
                  <h6 style="color: grey">{{ market }}</h6>
                </div>
                <div class="stockstats-currentprice stockstats-section">
                  <div v-if="changeNumerical <= 0">
                    <h3 style="color: red">{{ currency + current }}</h3>
                    <i
                      class="el-icon-caret-bottom stockstats-change-down"
                      style="color: red"
                    ></i>
                    <span style="color: red">
                      {{ changeNumerical + " (" + changePercentage + "%)" }}
                    </span>
                  </div>
                  <div v-if="changeNumerical > 0">
                    <h3 style="color: green">{{ currency + current }}</h3>
                    <i
                      class="el-icon-caret-top stockstats-change-up"
                      style="color: green"
                    ></i>
                    <span v-if="changeNumerical > 0" style="color: green">
                      {{ changeNumerical + " (" + changePercentage + "%)" }}
                    </span>
                  </div>
                </div>
                <div class="stockstats-detail stockstats-section">
                  <el-row>
                    <el-col :span="12">
                      最高：<span style="font-weight: bold">{{ high }}</span>
                    </el-col>
                    <el-col :span="12">
                      最低：<span style="font-weight: bold">{{ low }}</span>
                    </el-col>
                  </el-row>
                  <el-row>
                    <el-col :span="12">
                      今开：<span style="font-weight: bold">{{ open }}</span>
                    </el-col>
                    <el-col :span="12">
                      <span></span>
                    </el-col>
                  </el-row>
                </div>
              </el-card>
            </el-col>
            <el-col
              class="intro-timeline intro-detail-element"
              :xs="24"
              :sm="24"
              :md="12"
              :lg="12"
              :xl="12"
            >
              <h5 class="mt-4 mb-4">
                <i class="el-icon-s-data" style="color: #1e90ff"></i> 发展路径
              </h5>
              <div class="block">
                <div class="radio">
                  <el-radio-group v-model="reverse">
                    <el-radio :label="true">时间倒序</el-radio>
                    <el-radio :label="false">时间正序</el-radio>
                  </el-radio-group>
                </div>
                <div class="infinite-list-wrapper" style="overflow: auto">
                  <el-timeline :reverse="reverse">
                    <el-timeline-item
                      v-for="(activity, index) in activities"
                      :key="index"
                      :timestamp="activity.timestamp"
                      placement="top"
                      v-infinite-scroll="load"
                      infinite-scroll-disabled="disabled"
                    >
                      {{ activity.content }}
                    </el-timeline-item>
                  </el-timeline>
                </div>
              </div>
            </el-col>
            <el-col
              class="intro-structure intro-detail-element"
              :xs="24"
              :sm="24"
              :md="12"
              :lg="12"
              :xl="12"
            >
              <h5 class="mt-4 mb-4">
                <i class="el-icon-s-custom" style="color: #1e90ff"></i>
                公司架构
              </h5>
              <el-button
                class="intro-structure-fullscreen"
                type="primary"
                icon="el-icon-full-screen"
                size="mini"
                @click="fullScreen = true"
                plain
                >全屏</el-button
              >
              <el-dialog
                title="公司结构"
                :visible.sync="fullScreen"
                width="90%"
              >
                <TreeChart
                  :items="structureList"
                  class="intro-structure-chart"
                ></TreeChart>
              </el-dialog>
              <TreeChart
                :items="structureList"
                class="intro-structure-chart"
              ></TreeChart>
            </el-col>
          </el-row>
        </div>
        <el-divider></el-divider>
        <el-row :gutter="36">
          <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
            <h5 class="mb-4">
              谁贡献过
              <el-button
                type="text"
                size="small"
                @click="moreContributor"
                class="edit-info-button"
              >
                更多</el-button
              >
            </h5>
            <div class="edit-info-container">
              <div
                class="edit-info"
                v-for="(contributedUser, index) in contributorDisplayed"
                :key="index"
              >
                <div v-if="contributedUser.rank <= 3">
                  <el-badge
                    :value="contributedUser.rank"
                    class="contributed-badge"
                    :type="contributedUser.type"
                  >
                    <el-avatar
                      size="medium"
                      :src="contributedUser.avatarUrl"
                    ></el-avatar>
                  </el-badge>
                </div>
                <div v-else>
                  <el-avatar
                    size="medium"
                    :src="contributedUser.avatarUrl"
                  ></el-avatar>
                </div>
                <span> {{ contributedUser.name }} </span>
                <span style="font-size: x-small; color: grey">
                  贡献过{{ contributedUser.frequency }}次
                </span>
              </div>
            </div>
          </el-col>
          <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
            <div class="edit-info-bar">
              <h5>
                相关推荐
                <el-button
                  type="text"
                  size="small"
                  @click="changeComps"
                  class="edit-info-button"
                >
                  换一批</el-button
                >
              </h5>
            </div>
            <div class="edit-info-container">
              <div
                class="edit-info"
                v-for="(company, index) in companyShowed"
                :key="index"
              >
                <!-- :src="company.avatarUrl" -->
                <el-avatar :size="70"> {{ company.name }} </el-avatar>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>
      <!-- 活动 -->
      <div
        class="activity-container"
        v-if="tabIndex == 'activity' && activityPage.list"
      >
        <div v-if="activityPage.list.length > 0">
          <el-tabs
            v-model="activeActivityName"
            type="card"
            @tab-click="handleActTabClick"
          >
            <el-tab-pane label="全部" name="-1"></el-tab-pane>
            <el-tab-pane label="报名中" name="0"></el-tab-pane>
            <el-tab-pane label="进行中" name="1"></el-tab-pane>
            <el-tab-pane label="已结束" name="2"></el-tab-pane>
          </el-tabs>
          <div class="activity-item" v-if="activityDisplayed.length > 0">
            <el-row>
              <el-col
                :xs="24"
                :sm="12"
                :md="8"
                :lg="6"
                :xl="6"
                v-for="(activity, index) in activityDisplayed"
                :key="activity.id"
              >
                <el-card class="m-2" :body-style="{ padding: '0px' }">
                  <div class="activity-item-thumbnail-wrapper">
                    <img
                      class="activity-item-thumbnail"
                      :src="activity.thumbnail"
                    />
                    <div class="activity-item-tag">
                      <el-tag
                        v-if="currentActivityStatus == -1"
                        effect="dark"
                        :type="activityStatusColor[activityStatus[index]]"
                        hit
                        >{{ activityStatusText[activityStatus[index]] }}</el-tag
                      >
                      <el-tag
                        v-else
                        :type="activityStatusColor[currentActivityStatus]"
                        effect="dark"
                        hit="true"
                      >
                        {{ activityStatusText[currentActivityStatus] }}
                      </el-tag>
                    </div>
                  </div>
                  <div class="activity-item-title p-2">
                    {{ activity.title }}
                  </div>
                  <div
                    class="activity-item-detail text-secondary p-2"
                    style="font-size: 13px"
                  >
                    <span
                      ><time class="time">{{ activity.startTime }}</time>
                      {{ activity.address }}
                    </span>
                    <a :href="activity.link" target="_blank">
                      <el-button type="text" size="mini">查看详情</el-button>
                    </a>
                  </div>
                </el-card>
              </el-col>
            </el-row>
          </div>
          <div v-else class="noInfoMsgBox">
            暂无"{{ activityStatusText[currentActivityStatus] }}"活动
          </div>
        </div>
        <div v-else class="noInfoMsgBox">暂 无 活 动</div>
      </div>
      <!-- 圈子 -->
      <div class="community-box" v-if="tabIndex == 'community'">
        <el-tabs v-model="activeCommName" type="card">
          <el-tab-pane label="闲聊区" name="1">
            <div v-if="postPage.list.length > 0" class="community-post-display">
              <div
                class="community-post-switch mb-4 d-flex justify-content-end"
              >
                <el-switch
                  v-model="postListQuery.sort"
                  active-color="#13ce66"
                  inactive-color="#ff4949"
                  active-text="最新"
                  inactive-text="最热"
                  active-value="-id"
                  inactive-value="-hots"
                  @change="getPostList"
                >
                </el-switch>
              </div>
              <el-row>
                <el-col :span="24" v-for="post in postPage.list" :key="post.id">
                  <el-card
                    class="mb-2"
                    :body-style="{ padding: '0px' }"
                    shadow="hover"
                  >
                    <div class="community-post-item">
                      <div class="community-post-item-title">
                        <h5 class="mb-2">{{ post.title }}</h5>
                        <el-button
                          type="text"
                          size="mini"
                          @click="handleCollapse(post.id)"
                        >
                          {{
                            curPostDetailId === post.id &&
                            curPostDetailOpen == true
                              ? "收起"
                              : "详情"
                          }}
                        </el-button>
                      </div>
                      <div
                        v-if="
                          curPostDetailOpen == true &&
                          curPostDetailId === post.id
                        "
                        class="community-post-detail"
                      >
                        <el-divider></el-divider>
                        <div class="mb-4 d-flex align-items-center">
                          <el-avatar
                            class="mr-2"
                            :src="postDetail.fromUser.avatar"
                            size="small"
                          ></el-avatar>
                          <small
                            >{{ postDetail.fromUser.name }} 发布于
                            {{ postDetail.createTime }}</small
                          >
                        </div>
                        <div class="mb-4">{{ postDetail.content }}</div>
                        <div
                          class="community-post-detail-action mb-4 d-flex justify-content-end"
                        >
                          <div v-if="postDetail.like">
                            <el-button
                              class="likeButton"
                              type="text"
                              size="mini"
                              @click="likePost(postDetail.id)"
                            >
                              <font-awesome-icon
                                style="color: #409eff"
                                :icon="['fa', 'thumbs-up']"
                                size="sm"
                              />
                              点赞
                              {{
                                postDetail.likes === 0 ? "" : postDetail.likes
                              }}
                            </el-button>
                          </div>
                          <div v-else>
                            <el-button
                              class="likeButton text-secondary"
                              type="text"
                              size="mini"
                              @click="likePost(postDetail.id)"
                            >
                              <font-awesome-icon
                                style="color: grey"
                                :icon="['fa', 'thumbs-up']"
                                size="sm"
                              />
                              点赞
                              {{
                                postDetail.likes === 0 ? "" : postDetail.likes
                              }}
                            </el-button>
                          </div>
                          <el-button
                            type="text"
                            size="mini"
                            icon="el-icon-s-comment"
                            >评论 {{ postDetail.comments }}
                          </el-button>
                          <el-button
                            type="text"
                            size="mini"
                            @click="reportPost(postDetail.id)"
                            icon="el-icon-s-flag"
                            >举报</el-button
                          >
                          <el-button
                            icon="el-icon-delete-solid"
                            type="text"
                            size="mini"
                            @click="deletePost(postDetail.id)"
                            v-if="postDetail.fromUser.userId === userId"
                            >删除我的发帖</el-button
                          >
                        </div>
                        <div class="community-post-detail-commentbox">
                          <el-form
                            ref="commentForm"
                            :rules="commentFormRules"
                            :model="commentForm"
                            label-width="80px"
                            label-position="top"
                          >
                            <el-form-item label="" prop="content">
                              <el-input
                                type="textarea"
                                :rows="3"
                                placeholder="发表评论"
                                v-model="commentForm.content"
                                style="margin-bottom: 12px"
                                resize="none"
                              >
                              </el-input>
                            </el-form-item>
                          </el-form>
                          <div class="d-flex justify-content-end">
                            <el-button
                              type="primary"
                              @click="commentPost(postDetail.id)"
                              >发布</el-button
                            >
                          </div>
                        </div>
                        <div
                          class="community-post-detail-comment"
                          v-if="commentPage && commentPage.list.length > 0"
                        >
                          <el-row>
                            <el-divider></el-divider>
                            <el-col
                              :span="24"
                              v-for="comment in commentPage.list"
                              :key="comment.id"
                            >
                              <el-card
                                class="mb-2"
                                :body-style="{ padding: '12px' }"
                                shadow="hover"
                              >
                                <div class="mb-3 d-flex align-items-center">
                                  <el-avatar
                                    style="margin-right: 12px"
                                    :src="comment.fromUser.avatar"
                                    size="small"
                                  ></el-avatar>
                                  <div class="mr-2 font-weight-bold">
                                    {{ comment.fromUser.name }}
                                  </div>
                                  <small class="text-secondary">{{
                                    comment.createTime
                                  }}</small>
                                </div>
                                <div>{{ comment.content }}</div>
                                <div
                                  class="community-post-item-comment-action d-flex justify-content-end"
                                >
                                  <div v-if="comment.like">
                                    <el-button
                                      type="text"
                                      size="mini"
                                      class="likeButton"
                                      @click="likeComment(comment.id)"
                                    >
                                      <font-awesome-icon
                                        style="color: #409eff"
                                        :icon="['fa', 'thumbs-up']"
                                        size="sm"
                                      />
                                      点赞
                                      {{
                                        comment.likes === 0 ? "" : comment.likes
                                      }}
                                    </el-button>
                                  </div>
                                  <div v-else>
                                    <el-button
                                      type="text"
                                      size="mini"
                                      class="likeButton text-secondary"
                                      @click="likeComment(comment.id)"
                                    >
                                      <font-awesome-icon
                                        style="color: grey"
                                        :icon="['fa', 'thumbs-up']"
                                        size="sm"
                                      />
                                      点赞
                                      {{
                                        comment.likes === 0 ? "" : comment.likes
                                      }}
                                    </el-button>
                                  </div>
                                  <el-button
                                    type="text"
                                    size="small"
                                    icon="el-icon-s-comment"
                                    @click="openReplybox(comment.id)"
                                    >回复 {{ comment.comments }}
                                  </el-button>
                                  <el-button
                                    type="text"
                                    size="small"
                                    @click="reportComment(comment.id)"
                                    icon="el-icon-s-flag"
                                    >举报</el-button
                                  >
                                  <el-button
                                    icon="el-icon-delete-solid"
                                    type="text"
                                    size="small"
                                    @click="deleteComment(comment.id, 1)"
                                    v-if="comment.fromUser.userId === userId"
                                    >删除</el-button
                                  >
                                </div>
                                <div
                                  class="community-post-detail-comment-reply"
                                  v-if="replyList && replyList.length > 0"
                                >
                                  <el-row>
                                    <el-col
                                      :span="24"
                                      v-for="reply in replyList"
                                      :key="reply.id"
                                    >
                                      <div v-if="reply.ownerId == comment.id">
                                        <el-card
                                          :body-style="{ padding: '12px' }"
                                          shadow="never"
                                        >
                                          <div
                                            class="mb-2 d-flex align-items-center"
                                            style="
                                              font-size: 10px;
                                              font-weight: bold;
                                            "
                                          >
                                            <el-avatar
                                              style="margin-right: 8px"
                                              :src="reply.fromUser.avatar"
                                              :size="20"
                                            ></el-avatar>
                                            {{ reply.fromUser.name }}
                                            <i class="el-icon-caret-right"></i>
                                            {{ reply.toUser.name }}
                                          </div>
                                          {{ reply.content }}
                                          <div
                                            class="community-post-item-reply-action d-flex justify-content-end"
                                          >
                                            <div v-if="reply.like">
                                              <el-button
                                                type="text"
                                                size="mini"
                                                class="likeButton"
                                                @click="likeComment(reply.id)"
                                              >
                                                <font-awesome-icon
                                                  style="color: #409eff"
                                                  :icon="['fa', 'thumbs-up']"
                                                  size="sm"
                                                />
                                                点赞
                                                {{
                                                  reply.likes === 0
                                                    ? ""
                                                    : reply.likes
                                                }}
                                              </el-button>
                                            </div>
                                            <div v-else>
                                              <el-button
                                                type="text"
                                                size="mini"
                                                class="likeButton text-secondary"
                                                @click="likeComment(reply.id)"
                                              >
                                                <font-awesome-icon
                                                  style="color: grey"
                                                  :icon="['fa', 'thumbs-up']"
                                                  size="sm"
                                                />
                                                点赞
                                                {{
                                                  reply.likes === 0
                                                    ? ""
                                                    : reply.likes
                                                }}
                                              </el-button>
                                            </div>
                                            <el-button
                                              type="text"
                                              size="small"
                                              icon="el-icon-s-comment"
                                              @click="
                                                openSecondaryReplybox(reply.id)
                                              "
                                              >回复
                                            </el-button>
                                            <el-button
                                              type="text"
                                              size="small"
                                              @click="reportComment(comment.id)"
                                              icon="el-icon-s-flag"
                                              >举报</el-button
                                            >
                                            <el-button
                                              icon="el-icon-delete-solid"
                                              type="text"
                                              size="mini"
                                              @click="
                                                deleteComment(reply.id, 1)
                                              "
                                              v-if="
                                                reply.fromUser.userId === userId
                                              "
                                              >删除</el-button
                                            >
                                          </div>
                                          <div
                                            v-if="
                                              secondaryRepyOpen &&
                                              reply.id == replyCommentId
                                            "
                                            class="reply-box mt-2"
                                          >
                                            <el-form
                                              ref="replyForm"
                                              :rules="replyFormRules"
                                              :model="replyForm"
                                              label-width="80px"
                                              label-position="top"
                                            >
                                              <el-form-item
                                                label=""
                                                prop="content"
                                              >
                                                <el-input
                                                  type="textarea"
                                                  :rows="3"
                                                  placeholder="发表回复"
                                                  resize="none"
                                                  v-model="replyForm.content"
                                                  style="margin-bottom: 12px"
                                                >
                                                </el-input>
                                              </el-form-item>
                                            </el-form>
                                            <div
                                              class="d-flex justify-content-end"
                                            >
                                              <el-button
                                                type="primary"
                                                size="mini"
                                                @click="
                                                  replyComment(comment.id)
                                                "
                                                >回复</el-button
                                              >
                                            </div>
                                          </div>
                                        </el-card>
                                      </div>
                                    </el-col>
                                  </el-row>
                                </div>
                                <div
                                  v-if="
                                    replyOpen && comment.id == replyCommentId
                                  "
                                  class="reply-box mt-2"
                                >
                                  <el-form
                                    ref="replyForm"
                                    :rules="replyFormRules"
                                    :model="replyForm"
                                    label-width="80px"
                                    label-position="top"
                                  >
                                    <el-form-item label="" prop="content">
                                      <el-input
                                        type="textarea"
                                        :rows="3"
                                        placeholder="发表回复"
                                        resize="none"
                                        v-model="replyForm.content"
                                        style="margin-bottom: 12px"
                                      >
                                      </el-input>
                                    </el-form-item>
                                  </el-form>
                                  <div class="d-flex justify-content-end">
                                    <el-button
                                      type="primary"
                                      size="mini"
                                      @click="replyComment(comment.id)"
                                      >回复</el-button
                                    >
                                  </div>
                                </div>
                              </el-card>
                            </el-col>
                          </el-row>
                        </div>
                      </div>
                    </div>
                  </el-card>
                </el-col>
              </el-row>
              <div
                class="community-load-more-post mt-2 d-flex justify-content-center"
                v-if="hasMorePost == true"
              >
                <el-button round @click="loadMorePost"> 加 载 更 多 </el-button>
              </div>
              <div v-else class="mt-2 d-flex justify-content-center">
                <el-button round disabled> 没 有 更 多 了 </el-button>
              </div>
            </div>
            <div v-else class="noInfoMsgBox">
              暂无帖子，快来发表你的帖子吧！
            </div>
            <el-divider></el-divider>
            <div class="community-post-comment">
              <div class="d-flex mb-2">
                <h5 class="mr-3">发布留言</h5>
                <div style="color: grey; font-size: 14px">
                  文明上网理性发言，请遵守用户服务协议
                </div>
              </div>
              <el-form
                ref="postForm"
                :model="postForm"
                :rules="postFormRules"
                label-width="100px"
                class="mt-4"
                label-position="left"
                hide-required-asterisk
              >
                <el-form-item label="标题" prop="title">
                  <el-input
                    type="textarea"
                    :rows="1"
                    placeholder="请输入标题（登录后可以发表留言）"
                    v-model="postForm.title"
                    style="margin-bottom: 12px"
                    maxlength="50"
                    show-word-limit
                  >
                  </el-input>
                </el-form-item>
                <el-form-item label="内容" prop="content">
                  <el-input
                    type="textarea"
                    :rows="5"
                    placeholder="请输入内容（登录后可以发表留言）"
                    v-model="postForm.content"
                    style="margin-bottom: 12px"
                  >
                  </el-input>
                </el-form-item>
              </el-form>
              <div class="d-flex justify-content-end">
                <el-button type="primary" @click="savePost">提交</el-button>
              </div>
            </div>
          </el-tab-pane>
          <el-tab-pane label="评分区" name="2">
            <div
              class="community-score-display"
              v-if="scorePage && scorePage.list.length > 0"
            >
              <div class="mb-4 d-flex justify-content-end">
                <el-switch
                  v-model="scoreListQuery.sort"
                  active-color="#13ce66"
                  inactive-color="#ff4949"
                  active-text="最新"
                  active-value="-id"
                  inactive-value="-hots"
                  inactive-text="最热"
                  @change="getScoreList"
                >
                </el-switch>
              </div>
              <el-row>
                <el-col
                  :span="24"
                  v-for="score in scorePage.list"
                  :key="score.id"
                >
                  <el-card
                    style="mb-2"
                    :body-style="{ padding: '16px' }"
                    shadow="hover"
                  >
                    <div class="community-score-item">
                      <div class="community-score-header">
                        <el-avatar
                          class="mr-2"
                          :src="score.fromUser.avatar"
                          size="small"
                        ></el-avatar>
                        <div style="font-weight: bold; font-size: 14px">
                          {{ score.fromUser.name }}
                        </div>
                        <div
                          style="
                            color: grey;
                            font-size: 10px;
                            margin-left: 12px;
                            margin-right: 12px;
                          "
                        >
                          {{ score.createTime }}
                        </div>
                        <el-rate
                          v-model="score.score"
                          disabled
                          show-score
                          text-color="#ff9900"
                          score-template="{value}"
                        >
                        </el-rate>
                      </div>
                      <div class="mb-4 p-3">
                        {{ score.content }}
                      </div>
                      <div
                        class="community-score-action mb-2 d-flex justify-content-end"
                      >
                        <div v-if="score.like">
                          <el-button
                            type="text"
                            size="mini"
                            class="likeButton"
                            @click="likeScore(score.id)"
                          >
                            <font-awesome-icon
                              style="color: #409eff"
                              :icon="['fa', 'thumbs-up']"
                              size="sm"
                            />
                            点赞
                            {{ score.likes === 0 ? "" : score.likes }}
                          </el-button>
                        </div>
                        <div v-else>
                          <el-button
                            type="text"
                            size="mini"
                            class="likeButton text-secondary"
                            @click="likeScore(score.id)"
                          >
                            <font-awesome-icon
                              style="color: grey"
                              :icon="['fa', 'thumbs-up']"
                              size="sm"
                            />
                            点赞
                            {{ score.likes === 0 ? "" : score.likes }}
                          </el-button>
                        </div>
                        <el-button
                          type="text"
                          size="mini"
                          icon="el-icon-s-comment"
                          @click="
                            openScoreCommentbox(
                              score.id,
                              score.likes,
                              score.comments
                            )
                          "
                          >评论 {{ score.comments }}
                        </el-button>
                        <el-button
                          type="text"
                          size="mini"
                          @click="reportScore(score.id)"
                          icon="el-icon-s-flag"
                          >举报</el-button
                        >
                        <el-button
                          icon="el-icon-delete-solid"
                          type="text"
                          size="mini"
                          @click="deleteScore(score.id)"
                          v-if="score.fromUser.userId === userId"
                          >删除我的评分</el-button
                        >
                      </div>
                      <div
                        class="community-score-comment-list"
                        v-if="
                          scoreCommentboxOpen &&
                          scoreItemOpened.id == score.id &&
                          scoreCommentList &&
                          scoreCommentList.length > 0
                        "
                      >
                        <el-row>
                          <el-divider></el-divider>
                          <el-col
                            :span="24"
                            v-for="(comment, index) in scoreCommentList"
                            :key="comment.id"
                          >
                            <div
                              v-if="
                                scoreCommentList[index].ownerId === score.id
                              "
                            >
                              <el-card
                                style="mb-2"
                                :body-style="{ padding: '12px' }"
                                shadow="hover"
                              >
                                <div class="mb-3 d-flex align-items-center">
                                  <el-avatar
                                    style="margin-right: 12px"
                                    :src="comment.fromUser.avatar"
                                    size="small"
                                  ></el-avatar>
                                  <span
                                    style="
                                      margin-right: 12px;
                                      font-weight: bold;
                                      font-size: 10px;
                                    "
                                    >{{ comment.fromUser.name }}</span
                                  >
                                  <span style="color: grey; font-size: 8px">{{
                                    comment.createTime
                                  }}</span>
                                </div>
                                <div>{{ comment.content }}</div>
                                <div
                                  class="community-post-item-comment-action d-flex justify-content-end"
                                >
                                  <div v-if="comment.like">
                                    <el-button
                                      type="text"
                                      size="mini"
                                      class="likeButton"
                                      @click="likeComment(comment.id)"
                                    >
                                      <font-awesome-icon
                                        style="color: #409eff"
                                        :icon="['fa', 'thumbs-up']"
                                        size="sm"
                                      />
                                      点赞
                                      {{
                                        comment.likes === 0 ? "" : comment.likes
                                      }}
                                    </el-button>
                                  </div>
                                  <div v-else>
                                    <el-button
                                      type="text"
                                      size="mini"
                                      class="likeButton text-secondary"
                                      @click="likeComment(comment.id)"
                                    >
                                      <font-awesome-icon
                                        style="color: grey"
                                        :icon="['fa', 'thumbs-up']"
                                        size="sm"
                                      />
                                      点赞
                                      {{
                                        comment.likes === 0 ? "" : comment.likes
                                      }}
                                    </el-button>
                                  </div>
                                  <el-button
                                    type="text"
                                    size="small"
                                    icon="el-icon-s-comment"
                                    @click="openReplybox(comment.id)"
                                    >回复 {{ comment.comments }}
                                  </el-button>
                                  <el-button
                                    type="text"
                                    size="small"
                                    @click="reportComment(comment.id)"
                                    icon="el-icon-s-flag"
                                    >举报</el-button
                                  >
                                  <el-button
                                    icon="el-icon-delete-solid"
                                    type="text"
                                    size="small"
                                    @click="deleteComment(comment.id, 2)"
                                    v-if="comment.fromUser.userId === userId"
                                    >删除</el-button
                                  >
                                </div>
                                <div
                                  class="community-post-detail-comment-reply"
                                  v-if="replyList && replyList.length > 0"
                                >
                                  <el-row>
                                    <el-col
                                      :span="24"
                                      v-for="reply in replyList"
                                      :key="reply.id"
                                    >
                                      <div v-if="reply.ownerId == comment.id">
                                        <el-card
                                          :body-style="{ padding: '12px' }"
                                          shadow="never"
                                        >
                                          <div
                                            class="mb-2"
                                            style="
                                              display: flex;
                                              align-items: center;
                                              font-size: 10px;
                                              font-weight: bold;
                                            "
                                          >
                                            <el-avatar
                                              style="margin-right: 8px"
                                              :src="reply.fromUser.avatar"
                                              :size="20"
                                            ></el-avatar>
                                            {{ reply.fromUser.name }}
                                            <i class="el-icon-caret-right"></i>
                                            {{ reply.toUser.name }}
                                          </div>
                                          {{ reply.content }}
                                          <div
                                            class="community-post-item-reply-action d-flex justify-content-end"
                                          >
                                            <div v-if="reply.like">
                                              <el-button
                                                type="text"
                                                size="mini"
                                                class="likeButton"
                                                @click="likeComment(reply.id)"
                                              >
                                                <font-awesome-icon
                                                  style="color: #409eff"
                                                  :icon="['fa', 'thumbs-up']"
                                                  size="sm"
                                                />
                                                点赞
                                                {{
                                                  reply.likes === 0
                                                    ? ""
                                                    : reply.likes
                                                }}
                                              </el-button>
                                            </div>
                                            <div v-else>
                                              <el-button
                                                type="text"
                                                size="mini"
                                                class="likeButton text-secondary"
                                                @click="likeComment(reply.id)"
                                              >
                                                <font-awesome-icon
                                                  style="color: grey"
                                                  :icon="['fa', 'thumbs-up']"
                                                  size="sm"
                                                />
                                                点赞
                                                {{
                                                  reply.likes === 0
                                                    ? ""
                                                    : reply.likes
                                                }}
                                              </el-button>
                                            </div>
                                            <el-button
                                              type="text"
                                              size="small"
                                              icon="el-icon-s-comment"
                                              @click="
                                                openSecondaryReplybox(reply.id)
                                              "
                                              >回复
                                            </el-button>
                                            <el-button
                                              type="text"
                                              size="small"
                                              @click="reportComment(comment.id)"
                                              icon="el-icon-s-flag"
                                              >举报</el-button
                                            >
                                            <el-button
                                              icon="el-icon-delete-solid"
                                              type="text"
                                              size="mini"
                                              @click="deleteComment(reply.id)"
                                              v-if="
                                                reply.fromUser.userId === userId
                                              "
                                              >删除</el-button
                                            >
                                          </div>
                                          <div
                                            v-if="
                                              secondaryRepyOpen &&
                                              reply.id == replyCommentId
                                            "
                                            class="reply-box mt-2"
                                          >
                                            <el-form
                                              ref="replyForm"
                                              :rules="replyFormRules"
                                              :model="replyForm"
                                              label-width="80px"
                                              label-position="top"
                                            >
                                              <el-form-item
                                                label=""
                                                prop="content"
                                              >
                                                <el-input
                                                  type="textarea"
                                                  :rows="3"
                                                  placeholder="发表回复"
                                                  resize="none"
                                                  v-model="replyForm.content"
                                                  style="margin-bottom: 12px"
                                                >
                                                </el-input>
                                              </el-form-item>
                                            </el-form>
                                            <div
                                              class="d-flex justify-content-end"
                                            >
                                              <el-button
                                                type="primary"
                                                size="mini"
                                                @click="
                                                  replyComment(comment.id)
                                                "
                                                >回复</el-button
                                              >
                                            </div>
                                          </div>
                                        </el-card>
                                      </div>
                                    </el-col>
                                  </el-row>
                                </div>
                                <div
                                  v-if="
                                    replyOpen && comment.id == replyCommentId
                                  "
                                  class="reply-box mt-2"
                                >
                                  <el-form
                                    ref="replyForm"
                                    :rules="replyFormRules"
                                    :model="replyForm"
                                    label-width="80px"
                                    label-position="top"
                                  >
                                    <el-form-item label="" prop="content">
                                      <el-input
                                        type="textarea"
                                        :rows="3"
                                        placeholder="发表回复"
                                        resize="none"
                                        v-model="replyForm.content"
                                        style="margin-bottom: 12px"
                                      >
                                      </el-input>
                                    </el-form-item>
                                  </el-form>
                                  <div class="d-flex justify-content-end">
                                    <el-button
                                      type="primary"
                                      size="mini"
                                      @click="replyComment(comment.id)"
                                      >回复</el-button
                                    >
                                  </div>
                                </div>
                              </el-card>
                            </div>
                          </el-col>
                        </el-row>
                      </div>
                      <div
                        class="community-score-commentbox"
                        v-if="
                          scoreCommentboxOpen && scoreItemOpened.id == score.id
                        "
                      >
                        <el-form
                          ref="scoreCommentForm"
                          :rules="scoreCommentFormRules"
                          :model="scoreCommentForm"
                          label-width="80px"
                          label-position="top"
                        >
                          <el-form-item label="" prop="content">
                            <el-input
                              class="mt-2"
                              type="textarea"
                              :rows="3"
                              placeholder="发表评论"
                              v-model="scoreCommentForm.content"
                              resize="none"
                            >
                            </el-input>
                          </el-form-item>
                        </el-form>
                        <div class="d-flex justify-content-end">
                          <el-button
                            type="primary"
                            @click="commentScore(score.id)"
                            >发布</el-button
                          >
                        </div>
                      </div>
                    </div>
                    <div
                      v-if="curPostDetailId === score.id"
                      class="community-post-dialog"
                    >
                      <el-divider></el-divider>
                      <div class="community-post-detail-commentbox">
                        <el-form
                          ref="commentForm"
                          :rules="commentFormRules"
                          :model="commentForm"
                          label-width="80px"
                          label-position="top"
                        >
                          <el-form-item label="" prop="content">
                            <el-input
                              type="textarea"
                              :rows="3"
                              placeholder="发表评论"
                              v-model="commentForm.content"
                              style="margin-bottom: 12px"
                              resize="none"
                            >
                            </el-input>
                          </el-form-item>
                        </el-form>
                        <div class="d-flex justify-content-end">
                          <el-button
                            type="primary"
                            @click="commentPost(postDetail.id)"
                            >发布</el-button
                          >
                        </div>
                      </div>
                    </div>
                  </el-card>
                </el-col>
              </el-row>
              <div
                class="community-load-more-post mt-2 d-flex justify-content-center"
                v-if="hasMoreScore == true"
              >
                <el-button round @click="loadMoreScore">
                  加 载 更 多
                </el-button>
              </div>
              <div v-else class="mt-2 d-flex justify-content-center">
                <el-button round disabled> 没 有 更 多 了 </el-button>
              </div>
            </div>
            <div
              v-if="scorePage && scorePage.list.length === 0"
              class="noInfoMsgBox"
            >
              暂无评分，快来发表你的评分吧！
            </div>
            <el-divider></el-divider>
            <div class="community-score-comment">
              <div class="mb-4 d-flex">
                <h5 class="mr-3">
                  {{ hasMyScore == true ? "修改我的" : "发布" }}评分
                </h5>
                <div style="color: grey; font-size: 14px">
                  文明上网理性发言，请遵守用户服务协议
                </div>
              </div>
              <el-form
                ref="scoreForm"
                :model="scoreForm"
                :rules="scoreFormRules"
                label-width="100px"
                class="mt-4"
                label-position="left"
                hide-required-asterisk
              >
                <el-form-item label="评分" prop="score">
                  <el-rate v-model="scoreForm.score"></el-rate>
                </el-form-item>
                <el-form-item label="评论" prop="content">
                  <el-input
                    type="textarea"
                    :rows="5"
                    resize="none"
                    placeholder="请输入评论（登录后可以发表评论）"
                    v-model="scoreForm.content"
                    style="margin-bottom: 12px"
                  >
                  </el-input>
                </el-form-item>
              </el-form>
              <div class="d-flex justify-content-end align-items-end">
                <el-checkbox class="mr-4" v-model="scoreForm.anonymous">
                  匿名发表
                </el-checkbox>
                <el-button type="primary" @click="saveScore">
                  {{ hasMyScore == true ? "修改我的评分" : "提交" }}
                </el-button>
              </div>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>
    </div>
  </div>
</template>


<script>
import Vue from "vue";
import VueAMap from "vue-amap";
import BarChart from "../components/BarChart";
import TreeChart from "../components/TreeChart";
import Pagination from "@/components/Pagination";
import { formatListQuery, parseListQuery } from "@/utils/common";

import { library } from "@fortawesome/fontawesome-svg-core";
import { faThumbsUp } from "@fortawesome/free-solid-svg-icons";
library.add(faThumbsUp);

//api
import {
  //post
  getPostList,
  deletePost,
  savePost,
  getPostDetail,
  likePost,
  reportPost,
  commentPost,
  //comment
  getCommentList,
  deleteComment,
  likeComment,
  reportComment,
  replyComment,
  //score
  saveScore,
  getScoreList,
  commentScore,
  deleteScore,
  reportScore,
  likeScore,
  getMyScore,
} from "@/api/community_api";

import { getCompanyInfo } from "@/api/company_api";
import { getActivityList } from "@/api/activity_api";
import { setPageTitle } from "@/utils/setting";
import { doFavorite } from "@/api/favorite_api";
import { mapGetters } from "vuex";
import Toast from "@/utils/toast";

Vue.use(VueAMap);

VueAMap.initAMapApiLoader({
  key: process.env.VUE_APP_AMAP_KEY,
  v: "1.4.4",
});

export default {
  name: "CompanyWikiMainPage",
  components: { Pagination, BarChart, TreeChart },

  data() {
    return {
      //FAKE COMPANY INFO LIST START - for test only
      //summary image url
      summaryImgUrl: "http://i4.hexun.com/2019-08-01/198052912.png",

      //recommended company
      recommendedComps: [
        {
          name: "company1",
          avatarUrl:
            "https://cube.elemecdn.com/e/fd/0fc7d20532fdaf769a25683617711png.png",
        },
        {
          name: "company2",
          avatarUrl:
            "https://cube.elemecdn.com/e/fd/0fc7d20532fdaf769a25683617711png.png",
        },
        {
          name: "company3",
          avatarUrl:
            "https://cube.elemecdn.com/e/fd/0fc7d20532fdaf769a25683617711png.png",
        },
        {
          name: "company4",
          avatarUrl:
            "https://cube.elemecdn.com/e/fd/0fc7d20532fdaf769a25683617711png.png",
        },
        {
          name: "company5",
          avatarUrl:
            "https://cube.elemecdn.com/e/fd/0fc7d20532fdaf769a25683617711png.png",
        },
        {
          name: "company6",
          avatarUrl:
            "https://cube.elemecdn.com/e/fd/0fc7d20532fdaf769a25683617711png.png",
        },
        {
          name: "company7",
          avatarUrl:
            "https://cube.elemecdn.com/e/fd/0fc7d20532fdaf769a25683617711png.png",
        },
        {
          name: "company8",
          avatarUrl:
            "https://cube.elemecdn.com/e/fd/0fc7d20532fdaf769a25683617711png.png",
        },
        {
          name: "company9",
          avatarUrl:
            "https://cube.elemecdn.com/e/fd/0fc7d20532fdaf769a25683617711png.png",
        },
        {
          name: "company10",
          avatarUrl:
            "https://cube.elemecdn.com/e/fd/0fc7d20532fdaf769a25683617711png.png",
        },
      ],

      //who has contributed
      contributedUsers: [
        {
          name: "user1",
          avatarUrl:
            "https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png",
          rank: 1,
          type: "danger",
          frequency: 10,
        },
        {
          name: "user2",
          avatarUrl:
            "https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png",
          rank: 2,
          type: "success",
          frequency: 9,
        },
        {
          name: "user3",
          avatarUrl:
            "https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png",
          rank: 3,
          type: "warning",
          frequency: 8,
        },
        {
          name: "user4",
          avatarUrl:
            "https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png",
          rank: 4,
          frequency: 7,
        },
        {
          name: "user5",
          avatarUrl:
            "https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png",
          rank: 5,
          frequency: 6,
        },
        {
          name: "user6",
          avatarUrl:
            "https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png",
          rank: 6,
          frequency: 5,
        },
        {
          name: "user7",
          avatarUrl:
            "https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png",
          rank: 7,
          frequency: 4,
        },
        {
          name: "user8",
          avatarUrl:
            "https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png",
          rank: 8,
          frequency: 3,
        },
        {
          name: "user9",
          avatarUrl:
            "https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png",
          rank: 9,
          frequency: 2,
        },
      ],

      //history
      activities: [
        {
          timestamp: "2009",
          content: "Bilibili正式成立",
        },
        {
          timestamp: "2012",
          content: "Bilibili移动端app正式上线",
        },
        {
          timestamp: "2013",
          content: "Bilibili首次举行bml",
        },
        {
          timestamp: "2016",
          content:
            "百度发布2016年搜索报告，bilibili成为十大新鲜关注（00后）第一位",
        },
        {
          timestamp: "2018",
          content: "Bilibili在美国纳斯达克上市，股票代码bili",
        },
        {
          timestamp: "2019",
          content: "阿里巴巴入股Bilibili",
        },
      ],

      //stock info
      ticker: "APPL",
      market: "NASDQ",
      changeNumerical: 1.53,
      changePercentage: 1.319,
      currency: "$",
      current: 117.51,
      timestamp: "10-20 7:59PM PSD",
      open: 116.2,
      low: 115.63,
      high: 118.93,

      //companyrating 公司评分
      companyRating: 3.8,

      //structurelist 公司结构
      structureList: [
        {
          name: "Bilibili Inc. Cayman Islands",
          label: {
            normal: {
              backgroundColor: "#1E90FF",
            },
          },
          children: [
            {
              name: "Bilibili HK Limited (HongKong)",
            },
            {
              name: "Hode Limited (HongKong)",
              children: [
                {
                  name: "Shanghai Bilibili Technology Co.,Ltd",
                },
                {
                  name: "Hode Shanghai Limited('Hode Technology')",
                  children: [
                    {
                      name:
                        "Shanghai Kuanyu Digital Technology Co., Ltd (Shanghai Kuanyu)",
                    },
                    {
                      name:
                        "Shanghai Hode Information Technology Co., Ltd (Shanghai Hode)",
                      children: [
                        {
                          name: "Sharejoy Network Technology Co., Ltd",
                        },
                      ],
                    },
                  ],
                },
              ],
            },
            {
              name: "Bilibili Co., Ltd (Japan)",
            },
          ],
        },
      ],
      employeeData: [
        //雇员数量
        { year: "2010", value: 1000 },
        { year: "2011", value: 2000 },
        { year: "2012", value: 4000 },
        { year: "2013", value: 3000 },
        { year: "2014", value: 1000 },
        { year: "2015", value: 1000 },
        { year: "2016", value: 1234 },
        { year: "2018", value: 2341 },
        { year: "2019", value: 8712 },
        { year: "2020", value: 12432 },
      ],
      addressList: [
        {
          //公司地址
          address: "上海市杨浦区政立路485号国正中心3号楼",
          cityId: 0,
          id: 20,
          latitude: 31.309352,
          longitude: 121.506414,
        },
      ],
      productList: [
        //产品列表
        {
          img:
            "https://x0.ifengimg.com/res/2019/F675FF42FC2459230839D9E9CDB1AF49FEDC5ABD_size414_w2560_h1707.jpeg",
          alt: "小红书产品1",
        },
        {
          img: "http://i.17173cdn.com/2fhnvk/YWxqaGBf/cms3/FVYpgtbnbfxhggm.jpg",
          alt: "小红书产品2",
        },
      ],
      //FAKE COMPANY INFO LIST END

      //PUBLIC ATTRIBUTE
      tabIndex: "intro",
      dialogVisible: false,
      favoriteForm: {
        objectId: undefined,
        type: 2,
        favorite: false,
      },
      favoriteLoading: false,

      //MAIN PAGE ATTRIBUTE
      companyId: undefined,
      company: undefined,
      //intro-address
      activeAddress: 0,
      mapZoom: 14,
      //intro-timeline
      count: 10,
      loading: false,
      reverse: true,
      //intro-structure
      fullScreen: false,
      //intro-relatives
      companyShowed: [], // 用来存放每次点击换一批出来的5个对象
      randomCompIndex: "", // 用来放5个随机数
      randomCompArr: [], // 用来放5个随机数的数组，用来循环
      //intro-contributor
      contributorDisplayed: [],
      contributorIndex: "",
      contributorIndex: [],

      //ACTIVITY ATTRIBUTES
      activityPage: {},
      activityStatusColor: ["", "danger", "info"],
      activityStatusText: ["报名中", "进行中", "已结束"],
      activityStatus: [],
      activeActivityName: "-1",
      activityDisplayed: [],
      currentActivityStatus: -1,

      //COMMUNITY ATTRIBUTES
      activeCommName: "1",
      //COMMUNITY POST ATTRIBUTES
      hasMorePost: false,
      postPage: {
        list: {},
      },
      postListQuery: {
        page: 1,
        limit: 5,
        sort: "-hots",
        id: undefined,
        companyId: undefined,
        cliqueId: undefined,
        title: "",
      },
      postForm: {
        id: undefined, //帖子ID
        companyId: undefined, //企业ID
        cliqueId: undefined, //圈子ID
        title: "", //帖子标题
        content: "", //帖子内容
        tags: [], //标签列表
      },
      postFormRules: {
        title: [
          { required: true, message: "请填写您的帖子标题", trigger: "blur" },
        ],
        content: [
          { required: true, message: "请填写您的帖子内容", trigger: "blur" },
        ],
      },
      curPostDetailId: undefined,
      curPostDetailOpen: false,
      //post detail
      postDetail: {},
      postContent: "",
      //post comment
      commentForm: {
        id: undefined,
        ownerId: undefined,
        content: "",
      },
      commentFormRules: {
        content: [
          { required: true, message: "请发表您的评论", trigger: "blur" },
        ],
      },

      //COMMUNITY COMMENT AND REPLY
      commentPage: {},
      replyList: [],
      replyOpen: false,
      secondaryRepyOpen: false,
      replyCommentId: undefined,
      replyForm: {
        id: undefined,
        ownerId: undefined,
        content: "",
      },
      replyFormRules: {
        content: [
          { required: true, message: "请发表您的回复", trigger: "blur" },
        ],
      },

      //COMMUNITY SCORE ATTRIBUTES
      scorePage: {},
      hasMoreScore: false,
      hasMyScore: false,
      myScoreId: undefined,
      scoreCommentboxOpen: false,
      scoreCommentList: [],
      scoreReplyList: [],
      scorePostId: [],
      scoreListQuery: {
        page: 1,
        limit: 5,
        sort: undefined,
        fromId: undefined,
        id: undefined,
        companyId: this.companyId,
      },
      scoreItemOpened: {
        id: undefined,
        likes: undefined,
        comments: undefined,
      },
      scoreForm: {
        id: undefined,
        companyId: undefined,
        score: undefined,
        content: "",
        anonymous: 0,
      },
      scoreFormRules: {
        score: [{ required: true, message: "请发表您的评分", trigger: "blur" }],
        content: [
          { required: true, message: "请发表您的评论", trigger: "blur" },
        ],
      },
      scoreCommentForm: {
        id: undefined,
        ownerId: undefined,
        content: "",
      },
      scoreCommentFormRules: {
        content: [
          { required: true, message: "请发表您的评论", trigger: "blur" },
        ],
      },
    };
  },
  created() {
    this.initData();
  },
  mounted() {
    //页面一加载的时候先出来5个推荐公司
    for (var i = 0; i < 5 && i < this.recommendedComps.length; i++) {
      this.companyShowed.push(this.recommendedComps[i]);
    }
    //页面一加载的时候先出来5个最高贡献者
    for (var i = 0; i < 5 && i < this.contributedUsers.length; i++) {
      this.contributorDisplayed.push(this.contributedUsers[i]);
    }
  },
  computed: {
    companyLink() {
      return this.company.homepage.startsWith("http")
        ? this.company.homepage
        : "http://" + this.company.homepage;
    },
    ...mapGetters(["token", "userId"]),
    noMore() {
      return this.count >= 20;
    },
    disabled() {
      return this.loading || this.noMore;
    },
  },
  watch: {
    $route() {
      this.initData();
    },
  },
  methods: {
    initData() {
      this.companyId = this.$route.params.id;
      this.postListQuery.companyId = this.companyId;
      this.favoriteForm.objectId = this.companyId;
      this.postForm.companyId = this.companyId;
      this.scoreForm.companyId = this.companyId;

      //COMPANY METHODS - MAIN PAGE - INIT
      getCompanyInfo(this.companyId).then((response) => {
        this.company = response.data;
        setPageTitle(this.company.name);

        //for test use
        if (this.addressList) {
          for (const addr of this.addressList) {
            addr.mapWindow = {
              position: [addr.longitude, addr.latitude],
              content: addr.address,
            };
          }
        }
        // if (this.company.addressList) {
        //     for (const addr of this.company.addressList) {
        //         addr.mapWindow = {
        //             position: [addr.longitude, addr.latitude],
        //             content: addr.address
        //         };
        //     }
        // }
      });
    },

    //MAIN PAGE METHODS
    getList() {
      parseListQuery(this.$route.query, this.listQuery);
      getCompanyWikiList(this.listQuery).then((response) => {
        this.list = response.data.list;
        this.total = response.data.total;
        this.$emit("complete");
      });
    },
    handleSelectTab(tabIndex) {
      this.tabIndex = tabIndex;
      if (tabIndex == "community") {
        this.getPostList();
        this.getScoreList();
      } else if (tabIndex == "activity") {
        this.getActivityList();
      }
    },
    handleFavorite() {
      this.favoriteLoading = true;
      this.favoriteForm.favorite = !this.favoriteForm.favorite;
      doFavorite(this.favoriteForm)
        .then(() => {
          this.company.favoriteFlag = this.favoriteForm.favorite ? 1 : 0;
          if (this.company.favoriteFlag == 1) {
            this.$message("收藏成功");
          } else {
            this.$message("取消收藏");
          }
        })
        .finally(() => {
          this.favoriteLoading = false;
        });
    },
    onLoginClick() {
      this.$router.push("/login");
    },
    load() {
      this.loading = true;
      setTimeout(() => {
        this.count += 2;
        this.loading = false;
      }, 2000);
    },
    //change another group of comps, clicking "换一批"
    changeComps() {
      //每次点击换一批触发这个方法
      let totalComp = this.recommendedComps.length;
      this.randomCompIndex = "";
      this.randomCompArr = [];
      this.companyShowed = []; // 每次点击换一批就让之前的数据全清空
      while (this.randomCompArr.length < 5) {
        // 用while来判断是否循环5次
        let num = parseInt(Math.floor(Math.random() * Math.floor(totalComp))); // 随机出5个0-totalcount之间的随机数
        if (this.randomCompArr.indexOf(num) == -1) {
          // 获得不重复的三个数
          this.randomCompArr.push(num);
          this.randomCompIndex = num;
          this.companyShowed.push(this.recommendedComps[this.randomCompIndex]); // 将随机数当作下标来遍历data里的内容
        }
      }
    },
    //展示更多贡献者
    moreContributor() {
      const totalContributor = this.contributedUsers.length;
      this.contributorDisplayed = [];
      for (var i = 0; i < totalContributor; i++) {
        this.contributorDisplayed.push(this.contributedUsers[i]);
      }
    },
    //MAIN PAGE ENDS

    //POST METHODS
    getPostList() {
      getPostList(this.postListQuery).then((response) => {
        this.postPage = response.data;
        this.hasMorePost = response.data.hasMore;
        this.$emit("complete");
      });
    },
    savePost() {
      this.$refs["postForm"].validate((valid) => {
        if (valid) {
          savePost(this.postForm).then(() => {
            Toast.success("成功发布帖子");
            this.getPostList();
          });
        }
      });
    },
    deletePost(id) {
      deletePost(id).then(() => {
        Toast.success("成功删除帖子");
        this.getPostList();
      });
    },
    //open and close comment box of post
    handleCollapse(id) {
      if (this.curPostDetailId == id) {
        this.curPostDetailOpen = !this.curPostDetailOpen;
      } else {
        this.curPostDetailOpen = true;
        this.curPostDetailId = id;
        this.getPostDetail(id);
      }
    },
    getPostDetail(id) {
      getPostDetail(id).then((response) => {
        this.postDetail = response.data;
        this.getCommentList();
        this.$emit("complete");
      });
    },
    likePost(id) {
      likePost(id).then((response) => {
        this.postDetail.like = response.data.like;
        this.postDetail.likes = response.data.likes;
      });
    },
    reportPost(id) {
      let data = {
        ownerId: id,
        optionId: undefined,
        content: "",
      };
      reportPost(data).then(() => {
        Toast.success("举报成功");
      });
      this.$emit("complete");
    },
    commentPost(id) {
      this.commentForm.ownerId = id;
      this.postDetail.comments++;
      this.$refs["commentForm"][0].validate((valid) => {
        if (valid) {
          commentPost(this.commentForm).then(() => {
            Toast.success("评论成功");
            this.getCommentList();
          });
        }
      });
    },
    loadMorePost() {
      this.postListQuery.limit += 5;
      this.getPostList();
    },
    //POST METHODS END

    //COMMENT METHOD
    likeComment(id) {
      likeComment(id).then((response) => {
        this.getCommentList();
      });
    },
    getCommentList() {
      let data = {
        page: 1,
        limit: 10,
        sort: "-id",
        fromId: undefined,
        toId: undefined,
        ownerId: this.ownerId,
      };
      getCommentList(data).then((response) => {
        let temp_list = response.data.list;
        let actual_list = [];
        let reply_list = [];
        let comment_id_list = [];
        let score_comment_list = [];
        let score_reply_list = [];

        for (let i = 0; i < temp_list.length; i++) {
          let comment = temp_list[i];
          comment_id_list.push(comment.id);
          //帖子的评论
          if (comment.type === 1 && comment.ownerId === this.postDetail.id) {
            actual_list.push(comment);
          }
          //评分的评论
          if (
            comment.type === 2 &&
            this.scorePostId.indexOf(comment.ownerId) != -1
          ) {
            score_comment_list.push(comment);
          }
        }

        //评论的评论 二级评论
        for (let i = 0; i < temp_list.length; i++) {
          let comment = temp_list[i];
          if (
            comment.type === 3 &&
            comment_id_list.indexOf(comment.ownerId) != -1
          ) {
            reply_list.push(comment);
          }
        }

        this.commentPage = response.data;
        this.commentPage.list = actual_list;
        this.replyList = reply_list;
        this.scoreCommentList = score_comment_list;
        this.scoreReplyList = score_reply_list;
      });
      this.$emit("complete");
    },
    deleteComment(id, type) {
      deleteComment(id).then(() => {
        Toast.success("成功删除评论");
        this.getCommentList();
        if (type === 2) {
          this.getScoreList();
        }
      });
    },
    reportComment(id) {
      let data = {
        ownerId: id,
        optionId: undefined,
        content: "",
      };
      reportComment(data).then(() => {
        Toast.success("举报成功");
      });
    },
    replyComment(id) {
      this.replyForm.ownerId = id;
      this.$refs["replyForm"][0].validate((valid) => {
        if (valid) {
          replyComment(this.replyForm).then(() => {
            Toast.success("回复成功");
            this.getCommentList();
          });
        }
      });
    },
    openReplybox(id) {
      if (this.replyCommentId === id) {
        //如果是上次点击
        this.replyOpen = !this.replyOpen;
      } else {
        this.replyOpen = true;
      }
      this.replyCommentId = id;
    },
    openSecondaryReplybox(id) {
      this.replyOpen = false;
      if (this.replyCommentId === id) {
        //如果是上次点击
        this.secondaryRepyOpen = !this.secondaryRepyOpen;
      } else {
        this.secondaryRepyOpen = true;
      }
      this.replyCommentId = id;
    },

    //SCORE METHODS
    loadMoreScore() {
      this.scoreListQuery.limit += 5;
      this.getScoreList();
    },
    getMyScore(id) {
      getMyScore(id).then((response) => {
        if (response.data != undefined) {
          this.hasMyScore = true;
          this.myScoreId = response.data.id;
          this.scoreForm.score = response.data.score;
          this.scoreForm.content = response.data.content;
          this.scoreForm.anonymous = Boolean(response.data.anonymous);
        } else {
          this.hasMyScore = false;
          this.scoreForm.id = undefined;
          this.scoreForm.score = undefined;
          this.scoreForm.content = "";
          this.scoreForm.anonymous = 0;
        }
      });
    },
    saveScore() {
      this.scoreForm.id = this.myScoreId;
      this.scoreForm.anonymous = +this.scoreForm.anonymous;
      this.$refs["scoreForm"].validate((valid) => {
        if (valid) {
          saveScore(this.scoreForm).then(() => {
            Toast.success("评分成功");
            this.getScoreList();
          });
        }
      });
    },
    deleteScore(id) {
      deleteScore(id).then(() => {
        Toast.success("成功删除评论");
        this.hasMyScore = false;
        this.myScoreId = undefined;
        this.getCommentList();
      });
    },
    likeScore(id) {
      likeScore(id).then((response) => {
        this.getScoreList();
      });
    },
    commentScore(id) {
      this.scoreCommentForm.ownerId = id;
      this.$refs["scoreCommentForm"][0].validate((valid) => {
        if (valid) {
          commentScore(this.scoreCommentForm).then((response) => {
            Toast.success("评论成功");
            this.getCommentList();
          });
        }
      });
    },
    reportScore(id) {
      let data = {
        ownerId: id,
        optionId: undefined,
        content: "",
      };
      reportScore(data).then(() => {
        Toast.success("举报成功");
      });
      this.$emit("complete");
    },
    getScoreList() {
      getScoreList(this.scoreListQuery).then((response) => {
        this.scorePage = response.data;
        for (let i = 0; i < this.scorePage.list.length; i++) {
          this.scorePostId.push(this.scorePage.list[i].id);
        }
        this.getMyScore(this.companyId);
      });
      this.$emit("complete");
    },
    openScoreCommentbox(id, likes, comments) {
      if (this.scoreItemOpened.id === id) {
        //如果是上次点击
        this.scoreCommentboxOpen = !this.scoreCommentboxOpen;
      } else {
        this.scoreCommentboxOpen = true;
      }
      this.scoreItemOpened.id = id;
      this.scoreItemOpened.likes = likes;
      this.scoreItemOpened.comments = comments;
      this.getCommentList();
    },

    //ACTIVITY METHODS
    getActivityList() {
      let data = {
        cityIds: [],
        keyword: undefined,
        status: "2",
        sort: "-id",
        page: 1,
        limit: 10,
      };
      getActivityList(data).then((response) => {
        let temp_list = response.data.list;
        let actual_list = [];
        let today = new Date();
        let today_year = today.getFullYear();
        let today_month = "" + today.getMonth() + 1;
        let today_date = "" + today.getDate();
        if (today_month.length < 2) {
          today_month = "0" + today_month;
        }
        if (today_date.length < 2) {
          today_date = "0" + today_date;
        }
        //filter activities based on organizer, getting the actual list
        for (let i = 0; i < temp_list.length; i++) {
          let activity = temp_list[i];
          if (activity.organizer == this.company.name) {
            let activity_year = activity.startTime.substring(0, 4);
            let activity_month = activity.startTime.substring(5, 7);
            let activity_date = activity.startTime.substring(8, 10);
            if (activity_year == today_year) {
              activity.startTime = activity.startTime.substring(0, 10);
              activity.link = "/activity/" + activity.id;
              actual_list.push(activity);
            }
          }
        }
        //sort activities by start time
        actual_list.sort(function (a, b) {
          let aa = a.startTime.split("-").join();
          let bb = b.startTime.split("-").join();
          return aa < bb ? -1 : aa > bb ? 1 : 0;
        });

        //inspect status of activities + categorize
        for (let i = 0; i < actual_list.length; i++) {
          let activity = actual_list[i];
          let timeline = [
            activity.curTime,
            activity.startTime,
            activity.finishTime,
          ];
          timeline.sort(function (a, b) {
            let aa = a.split("-").join();
            let bb = b.split("-").join();
            return aa < bb ? -1 : aa > bb ? 1 : 0;
          });

          let cur = activity.curTime;
          if (timeline[0] == cur) {
            this.activityStatus.push(0);
          } else if (timeline[1] == cur) {
            this.activityStatus.push(1);
          } else {
            this.activityStatus.push(2);
          }
        }

        this.activityPage = response.data;
        this.activityPage.list = actual_list;
        this.activityDisplayed = actual_list;
        this.$emit("complete");
      });
    },
    handleActTabClick(tab, event) {
      const status = parseInt(tab.name);
      this.currentActivityStatus = status;
      if (status == -1) {
        this.activityDisplayed = this.activityPage.list;
      } else {
        this.activityDisplayed = [];
        for (var i = 0; i < this.activityPage.list.length; i++) {
          if (this.activityStatus[i] == status) {
            this.activityDisplayed.push(this.activityPage.list[i]);
          }
        }
      }
    },
    //ACTIVITY METHODS
  },
};
</script>

<style scoped lang="scss">
.noInfoMsgBox {
  line-height: 80px;
  text-align: center;
  padding-bottom: 20px;
  width: 100%;
}

.likeButton {
  margin: 1px;
  width: 60px;
}

.app-container {
  margin: 0 auto;
  min-height: calc(100vh - 477px);

  .company-wiki-header-container {
    display: flex;
    align-items: center;
    justify-content: space-between;
  }

  .company-info-container {
    height: calc(91px + 100vw * 0.39);

    .company-info {
      position: relative;
      display: flex;
      flex-direction: column;
      align-items: center;

      .company-info-banner {
        width: 100%;
        z-index: -1;
      }

      .company-info-stat {
        width: 230px;
        height: 50px;
        position: absolute;
        right: 7%;
        top: 77%;
      }

      .company-info-logo {
        width: 100px;
        height: 100px;
        border: 2px solid white;
      }

      .company-info-description {
        position: absolute;
        top: calc(100% - 50px);
        display: flex;
        flex-direction: column;
        align-items: center;
      }

      .company-rating-dialog-content {
        margin-bottom: 12px;
      }
    }
  }

  .company-wiki-content-container {
    width: 100%;
    padding: 0 20px 20px 20px;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;

    .intro-summary {
      display: flex;
      flex-direction: row;

      .intro-summary-img {
        margin-right: 20px;
        max-width: 600px;
      }
    }

    .intro-detail-element {
      height: 450px;
      margin-bottom: 30px;
    }

    .map-box {
      height: 300px;
    }

    .intro-product-image {
      width: 100%;
      height: 100%;
      object-fit: cover;
    }

    .intro-valuation-stockstats {
      display: flex;
      flex-direction: column;
      align-content: stretch;

      .stockstats-section {
        margin-top: 24px;
        height: 80px;
      }
    }

    .infinite-list-wrapper {
      height: 350px;
    }

    .intro-structure-fullscreen {
      position: absolute;
      right: 50px;
    }

    .edit-info-container {
      display: flex;
      flex-flow: row wrap;
      justify-content: flex-start;
      margin-bottom: 24px;

      .edit-info {
        width: 20%;
        margin-bottom: 12px;

        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
      }
    }
  }

  .activity-container {
    width: 100%;
    .activity-item-thumbnail-wrapper {
      position: relative;
      width: 100%;
      height: 0;
      padding-top: 100%;
      .activity-item-thumbnail {
        position: absolute;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
      }
      .activity-item-tag {
        position: absolute;
        top: 10%;
        right: 0;
      }
    }
    .activity-item-title {
      display: flex;
      align-items: center;
      height: 50px;
      font-weight: bold;
      margin-bottom: 8px;
    }

    .activity-item-detail {
      display: flex;
      flex-direction: row;
      justify-content: space-between;
      align-items: center;
    }
  }
  .community-box {
    width: 100%;
    .community-post-item {
      padding: 14px;
      display: flex;
      flex-direction: column;
      justify-content: space-between;
    }
    .community-score-header {
      display: flex;
      align-items: center;
    }
  }
}

@media screen and (min-width: 1000px) {
  .app-container {
    .company-info-container {
      height: 525px;
    }
  }
}

@media screen and (max-width: 768px) {
  .app-container {
    .company-info-container {
      height: 350px;

      .company-info {
        display: flex;
        flex-direction: column;

        .company-info-stat {
          width: 230px;
          height: 25px;
          position: absolute;
          width: 100%;
          left: 7%;
          top: 50%;
        }
      }
    }

    .company-wiki-content-container {
      .intro-summary {
        display: flex;
        flex-direction: column;

        .intro-summary-img {
          margin-bottom: 12px;
        }
      }

      .edit-info-container {
        .edit-info {
          width: 33%;
        }
      }
      .community-score-header {
        display: flex;
        flex-direction: column;
      }
    }
  }
}

@media screen and (max-width: 415px) {
  .app-container {
    .company-info-container {
      height: 300px;
    }
  }
}
</style>