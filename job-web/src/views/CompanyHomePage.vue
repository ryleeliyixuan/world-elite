<template>
    <div class="app-container container">
        <div v-if="company" class="company-top-container">
            <div class="company-info-container">
                <el-image :src="company.logo" class="company-logo" :alt="company.fullName">
                    <div slot="error" style="text-align: center; line-height: 100px; font-size: 40px;">
                        <i class="el-icon-picture-outline"></i>
                    </div>
                </el-image>
                <div class="company-info">
                    <h5 class="mt-0">{{company.name}}</h5>
                    <p class="mb-0">
                        <span v-if="company.stage">{{company.stage.name}} .</span>
                        <span v-if="company.property">{{company.property.name}} .</span>
                        <span v-if="company.industry">{{company.industry.name}} .</span>
                        <span v-if="company.scale">{{company.scale.name}}</span>
                    </p>
                    <div class="mt-2 mb-0" v-if="company.homepage">
                        <el-link :href="companyLink" target="_blank" :underline="false">
                            公司官网
                            <i class="el-icon-position el-icon--right"></i>
                        </el-link>
                    </div>
                </div>
            </div>
            <el-button type="primary"
                       :icon="company.favoriteFlag === 1? 'el-icon-star-on' : 'el-icon-star-off'"
                       plain
                       :loading="favoriteLoading"
                       @click="handleFavorite">
                {{company.favoriteFlag === 1? '已收藏': '收藏企业'}}
            </el-button>
        </div>
        <el-menu :default-active="tabIndex" mode="horizontal" @select="handleSelectTab">
            <el-menu-item index="intro">公司介绍</el-menu-item>
            <el-menu-item index="job">招聘岗位</el-menu-item>
            <el-menu-item index="activity">相关活动</el-menu-item>
        </el-menu>
        <div class="mt-4" v-if="company">
            <div class="intro-box" v-if="tabIndex == 'intro'">
                <div v-if="company.jobList && company.jobList.length !== 0">
                    <h5 class="mt-4 mb-4">
                        热招职位
                        <el-link :underline="false" class="float-right" @click="handleSelectTab('job')">
                            查看全部
                            <i class="el-icon-arrow-right el-icon--right"></i>
                        </el-link>
                    </h5>
                    <div class="recommend-jobs mb-4">
                        <el-row :gutter="12">
                            <el-col :span="8" v-for="job in company.jobList" :key="job.id">
                                <el-card shadow="hover" @click.native="onJobClick(job)" class="link-pointer">
                                    <h6>
                                        {{job.name}}
                                        <span
                                                class="text-danger float-right"
                                        >{{job.minSalary}}K - {{job.maxSalary}}K</span>
                                    </h6>
                                    <div
                                            class="text-small text-gray"
                                    >{{job.minDegree? job.minDegree.name: ''}} . {{job.city? job.city.name: ''}}
                                    </div>
                                </el-card>
                            </el-col>
                        </el-row>
                    </div>
                </div>
                <h5 class="mt-4 mb-4" v-if="company.introduction">公司简介</h5>
                <div class="introdution" v-html="company.introduction"></div>
                <h5 class="mt-4 mb-4" v-if="company.addressList && company.addressList.length != 0">公司地址</h5>
                <el-collapse v-model="activeAddress"
                             accordion
                             v-for="(addr, index) in company.addressList"
                             :key="addr.id">
                    <el-collapse-item :title="addr.address" :name="index">
                        <div class="map-box">
                            <el-amap :vid="'amap' + index" :zoom="mapZoom" :center="addr.mapWindow.position">
                                <el-amap-info-window
                                        :position="addr.mapWindow.position"
                                        :content="addr.mapWindow.content"
                                ></el-amap-info-window>
                            </el-amap>
                        </div>
                    </el-collapse-item>
                </el-collapse>
                <div v-if="company.companyWiki && company.companyWiki != ''">
                    <h5 class="mt-4 mb-4">公司百科</h5>
                    <div class="introdution ql-editor">
                        <div class="wiki_sidebar" v-html="company.wikiSidebar"></div>
                        <div class="wiki_content" v-html="company.companyWiki"></div>
                        <div style="clear: both;"></div>
                    </div>
                    <div style="display: none;">
                        <p>登录后查看百科</p>
                        <el-button type="primary" @click="onLoginClick">马上登录</el-button>
                    </div>
                </div>
            </div>
            <div class="row job-box" v-else-if="tabIndex == 'job'">
                <div class="col-lg-8 col-md-10 col-sm-12" v-if="jobPage.list.length>0">
                    <el-card shadow="hover"
                             v-for="job in jobPage.list"
                             :key="job.id"
                             class="mb-2 link-pointer"
                             @click.native="onJobClick(job)">
                        <h6> {{job.name}}
                            <span class="text-danger float-right">
                                {{job.minSalary}}K - {{job.maxSalary}}K</span>
                        </h6>
                        <div class="text-small text-gray">
                            {{job.minDegree? job.minDegree.name: ''}} . {{job.city? job.city.name: ''}}
                        </div>
                    </el-card>
                    <pagination v-show="total"
                                :total="total"
                                :page.sync="listQuery.page"
                                :limit.sync="listQuery.limit"
                                @pagination="getCompanyJobList"/>
                </div>
                <div style="line-height: 80px; text-align: center; padding-bottom: 20px; width: 100%;" v-else>
                    暂无岗位
                </div>
            </div>
            <!-- Activity START-->
            <div class="activity-box" v-else>
                <ul class="timeline" v-if="activityPage.list.length>0">
                    <li class="event"
                        v-for="activity in activityPage.list"
                        :key="activity.id"
                        :data-date="activity.startTime">
                        <h3>
                            <a :href="activity.link" target="_blank"> {{activity.title}} </a>
                        </h3>
                        <p>{{activity.summary}}</p>
                    </li>
                </ul>
                <div style="line-height: 80px; text-align: center; padding-bottom: 20px; width: 100%;" v-else>
                    暂无活动
                </div>
            </div>
            <!-- Activity END -->
        </div>
    </div>
</template>


<script>
    import Vue from "vue";
    import VueAMap from "vue-amap";
    import Pagination from "@/components/Pagination";

    import {getCompanyInfo} from "@/api/company_api";
    import {getCompanyJobList} from "@/api/job_api";
    // Activity START
    import {getActivityList} from "@/api/activity_api";
    // Activity END
    import {setPageTitle} from "@/utils/setting";
    import {doFavorite} from "@/api/favorite_api";
    import {mapGetters} from "vuex";

    Vue.use(VueAMap);

    VueAMap.initAMapApiLoader({
        key: process.env.VUE_APP_AMAP_KEY,
        v: "1.4.4"
    });

    export default {
        name: "CompanyHomePage",
        components: {Pagination},
        data() {
            return {
                companyId: undefined,
                company: undefined,
                total: 0,
                jobPage: {},
                activityPage: {},
                tabIndex: "intro",
                activeAddress: 0,
                mapZoom: 14,
                listQuery: {
                    companyId: undefined,
                    page: 1,
                    limit: 10,
                    sort: "-pub_time"
                },
                favoriteForm: {
                    objectId: undefined,
                    type: 2,
                    favorite: false
                },
                favoriteLoading: false
            };
        },
        created() {
            this.initData();
            window.addEventListener('scroll', this.handleScroll);
        },
        computed: {
            companyLink() {
                return this.company.homepage.startsWith("http")
                    ? this.company.homepage
                    : "http://" + this.company.homepage;
            },
            ...mapGetters(["token"])
        },
        watch: {
            $route() {
                this.initData();
            }
        },
        methods: {
            initData() {
                this.companyId = this.$route.params.id;
                this.listQuery.companyId = this.companyId;
                this.favoriteForm.objectId = this.companyId;
                getCompanyInfo(this.companyId).then(response => {
                    this.company = response.data;
                    setPageTitle(this.company.name);
                    if (this.company.addressList) {
                        for (const addr of this.company.addressList) {
                            addr.mapWindow = {
                                position: [addr.longitude, addr.latitude],
                                content: addr.address
                            };
                        }
                    }
                }).then(response => {
                    // Activity START
                    this.getActivityList();
                    // Activity END
                    if (this.company != null && this.company.companyWiki != null) {
                        let ulist = document.createElement("div");
                        let content = document.createElement("div");
                        content.innerHTML = this.company.companyWiki;

                        // const url = "/company/" + this.$route.params.id + "/";

                        // crete table of contents
                        let idIndex = 0;
                        let h_list = content.querySelectorAll("h3, h4, h5, h6");
                        for (let i = 0; i < h_list.length; i++) {
                            let h = h_list[i];
                            // get plain text
                            let text = h.innerText;
                            // assign id
                            let idAtt = document.createAttribute("id");
                            idAtt.value = "s" + idIndex++;
                            h.setAttributeNode(idAtt);

                            // create list item and link
                            let item = document.createElement("li");
                            let link = document.createElement("a");
                            let hrefAtt = document.createAttribute("href");
                            // hrefAtt.value = url + "#" + h.id;
                            hrefAtt.value = "#" + h.id;

                            link.setAttributeNode(hrefAtt);
                            let num = parseInt(h.tagName.substring(1, 2), 10) - 2;
                            for (let j = 1; j < num; j++) {
                                link.innerHTML += "&emsp;";
                            }
                            link.innerHTML += text;
                            // append
                            item.appendChild(link);
                            ulist.appendChild(item);
                        }
                        this.company.companyWiki = content.innerHTML;
                        this.company.wikiSidebar = ulist.innerHTML;
                    }
                });
                this.getCompanyJobList();
            },
            handleScroll(event) {
                if (this.tabIndex == "intro") {
                    let h_list = document.getElementsByClassName("wiki_content").length>0 ? document.getElementsByClassName("wiki_content")[0].querySelectorAll("h3, h4, h5, h6") : [];
                    let table = document.getElementsByClassName("wiki_sidebar").length>0 ? document.getElementsByClassName("wiki_sidebar")[0].getElementsByTagName("a") : [];

                    for (let i = 0; i < h_list.length; i++) {
                        let element = h_list[i];
                        let position = element.getBoundingClientRect();

                        let next_element = null;
                        let next_position = null;
                        if (i != h_list.length - 1) {
                            next_element = h_list[i + 1];
                            next_position = next_element.getBoundingClientRect();
                        }

                        if (next_element == null) {
                            if (position.top <= 10) {
                                for (let j = 0; j < table.length; j++) {
                                    table[j].style.color = "#707070";
                                }
                                table[i].style.color = "#551A8B";
                                break;
                            } else if (position.bottom < 0) {
                                for (let j = 0; j < table.length; j++) {
                                    table[j].style.color = "#707070";
                                }
                                table[i].style.color = "#551A8B";
                                break;
                            }
                        } else {
                            if (position.top >= 0 && position.top <= 10) {
                                for (let j = 0; j < table.length; j++) {
                                    table[j].style.color = "#707070";
                                }
                                table[i].style.color = "#551A8B";
                                break;
                            } else if (position.top < 10 && next_position.top > 10) {
                                for (let j = 0; j < table.length; j++) {
                                    table[j].style.color = "#707070";
                                }
                                table[i].style.color = "#551A8B";
                                break;
                            } else if (position.bottom < 10 && next_position.top > 10) {
                                for (let j = 0; j < table.length; j++) {
                                    table[j].style.color = "#707070";
                                }
                                table[i].style.color = "#551A8B";
                                break;
                            }
                        }
                    }
                }
            },
            getCompanyJobList() {
                getCompanyJobList(this.listQuery).then(response => {
                    this.jobPage = response.data;
                    this.total = response.data.total;
                });
            },
            // Activity START
            getActivityList() {
                let data = {
                    cityIds: [],
                    keyword: undefined,
                    status: "2",
                    sort: "-id",
                    page: 1,
                    limit: 10
                };
                getActivityList(data).then(response => {
                    let temp_list = response.data.list;
                    let actual_list = [];
                    let today = new Date();
                    let today_year = today.getFullYear();
                    let today_month = '' + today.getMonth() + 1;
                    let today_date = '' + today.getDate();
                    if (today_month.length < 2) {
                        today_month = '0' + today_month;
                    }
                    if (today_date.length < 2) {
                        today_date = '0' + today_date;
                    }
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
                    actual_list.sort(function (a, b) {
                        let aa = a.startTime.split('-').join();
                        let bb = b.startTime.split('-').join();
                        return aa < bb ? -1 : (aa > bb ? 1 : 0);
                    });
                    this.activityPage = response.data;
                    this.activityPage.list = actual_list;
                    this.$emit("complete");
                });
            },
            // Activity END
            handleSelectTab(tabIndex) {
                this.tabIndex = tabIndex;
                if (tabIndex == "intro") {
                    this.listQuery.page = 1;
                    this.getCompanyJobList();
                }
            },
            handleFavorite() {
                this.favoriteLoading = true;
                this.favoriteForm.favorite = !this.favoriteForm.favorite;
                doFavorite(this.favoriteForm)
                    .then(() => {
                        this.company.favoriteFlag = this.favoriteForm.favorite ? 1 : 0;
                        if(this.company.favoriteFlag==1){
                            this.$message("收藏成功");
                        }
                        else {
                            this.$message("取消收藏");
                        }

                    })
                    .finally(() => {
                        this.favoriteLoading = false;
                    });
            },
            onJobClick(job) {
                this.$router.push({name: "job", params: {id: job.id}});
            },
            onLoginClick() {
                this.$router.push('/login');
            }
        }
    };

</script>

<style scoped>
    .company-logo {
        width: 100px;
        height: 100px;
    }

    .company-logo-error {
        width: 100px;
        height: 100px;
        text-align: center;
        line-height: 100px;
        font-size: 40px;
    }

    .map-box {
        height: 200px;
    }

    .app-container {
        margin: 0 auto;
        min-height: calc(100vh - 477px);
    }

    .wiki_sidebar {
        position: sticky;
        font-size: 17px;
        top: 50px;
        width: 20%;
        float: left;
        overflow-y: scroll;
        max-height: 85vh;
        padding-left: 20px;
        list-style-type: none;
    }

    .wiki_content {
        width: 75%;
        float: right;
    }

    .timeline {
        border-left: 4px solid gray;
        border-bottom-right-radius: 4px;
        border-top-right-radius: 4px;
        margin: 50px 50px 50px 200px;
        letter-spacing: 0.5px;
        position: relative;
        font-size: 18px;
        padding: 50px;
        list-style: none;
        text-align: left;
        font-weight: 100;
        max-width: 70%;
    }

    .timeline h3 {
        letter-spacing: 1.5px;
        font-weight: 500;
        font-size: 25px;
    }

    .timeline .event {
        padding-bottom: 25px;
        margin-bottom: 50px;
        position: relative;
    }

    .timeline .event:last-of-type {
        padding-bottom: 0;
        margin-bottom: 0;
        border: none;
    }

    .timeline .event:before, .timeline .event:after {
        position: absolute;
        display: block;
        top: 0;
    }

    .timeline .event:before {
        left: -218px;
        color: gray;
        content: attr(data-date);
        text-align: right;
        font-weight: 100;
        font-size: 16px;
        min-width: 120px;
    }

    .timeline .event:after {
        box-shadow: 0 0 0 4px gray;
        left: -58px;
        background: white;
        border-radius: 50%;
        height: 11px;
        width: 11px;
        content: "";
        top: 5px;
    }

    .company-top-container {
        display: flex;
        align-items: center;
        justify-content: space-between;
    }

    .company-info-container {
        display: flex;
        align-items: center;
    }

    .company-info {
        display: flex;
        flex-direction: column;
        margin: 0 15px;
    }

    @media screen and (max-width: 850px) {
        .wiki_sidebar {
            display: none;
        }

        .wiki_content {
            width: 100%;
            float: none;
        }


        .company-info-container {
            flex-direction: column;
        }

        .company-info {
            margin: 0;
        }
    }

    @media screen and (max-width: 410px) {
        .company-top-container {
            flex-direction: column;
        }

        .company-info-container {
            flex-direction: column;
            margin-bottom: 20px;
        }
    }
</style>

<style>
    .wiki_sidebar a {
        color: #707070;
    }
</style>
