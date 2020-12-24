<template>
    <div class="app-container">
        <div class="title-container">
            <div class="title">我的模拟面试</div>
            <div class="identity-change-container">
                <div :class="['interviewee-container',{'select':identity===1}]" @click="onInterviewee">
                    面试者
                </div>
                <div :class="['interviewer-container',{'select':identity===2}]" @click="onInterviewer">
                    面试官
                </div>
            </div>
        </div>
        <div class="content-container">
            <div class="button-container">
                <el-avatar :src="myInfo.avatar" :size="117" cover class="avatar"></el-avatar>
                <div class="username">{{myInfo.name}}</div>
                <div :class="['button', {'select':menu===1}]" @click="onMineCalendar">
                    <el-image v-if="menu===1" style="width:21px; height:21px; margin-right: 8px;" :src="require('@/assets/mock/calendar.png')"></el-image>
                    <el-image v-else style="width:21px; height:21px; margin-right: 8px;" :src="require('@/assets/mock/calendar2.png')"></el-image>
                    我的日历
                </div>
                <div :class="['button', {'select':menu===2}]" @click="onMockRecord">
                    <el-image v-if="menu===2" style="width:21px; height:23px; margin-right: 8px;" :src="require('@/assets/mock/record.png')"></el-image>
                    <el-image v-else style="width:21px; height:23px; margin-right: 8px;" :src="require('@/assets/mock/record2.png')"></el-image>
                    面试记录
                </div>
                <div :class="['button', {'select':menu===3}]" @click="onSettleIn" v-if="userIdentity===1 && identity===1">
                    <el-image v-if="menu===3" style="width:19px; height:21px; margin-right: 8px;" :src="require('@/assets/mock/settle-in.png')"></el-image>
                    <el-image v-else style="width:19px; height:21px; margin-right: 8px;" :src="require('@/assets/mock/settle-in.png')"></el-image>
                    立即入驻
                </div>
                <div :class="['button', {'select':menu===4}]" @click="onIncome" v-if="userIdentity===2 && identity===2">
                    <el-image v-if="menu===4" style="width:19px; height:21px; margin-right: 8px;" :src="require('@/assets/mock/income.png')"></el-image>
                    <el-image v-else style="width:19px; height:21px; margin-right: 8px;" :src="require('@/assets/mock/income2.png')"></el-image>
                    我的收益
                </div>
            </div>
            <!--面试者-->
            <div class="calendar-container" v-if="identity===1 && menu===1">
                <FullCalendar ref="fullCalendar" :options="calendarOptions"/>
                <div class="additive-container">
                    <div class="tag"/>
                    <div class="tag-text">预约成功时间</div>
                    <div class="left-container" @click="onPrev">
                        <div class="left-arrow"/>
                    </div>
                    <div class="right-container" @click="onNext">
                        <div class="right-arrow"/>
                    </div>
                </div>
            </div>
            <!--面试官-->
            <div class="calendar-container" v-if="identity===2 && menu===1">
                <FullCalendar ref="fullCalendar" :options="calendarOptions"/>
                <div class="additive-container">
                    <div class="tag2"/>
                    <div class="tag-text">可预约时间</div>
                    <div class="tag"/>
                    <div class="tag-text">预约成功时间</div>
                    <div class="left-container" @click="onPrev">
                        <div class="left-arrow"/>
                    </div>
                    <div class="right-container" @click="onNext">
                        <div class="right-arrow"/>
                    </div>
                </div>
            </div>
            <!--           用户身份-->
            <div class="record-container" v-show="identity===1 && menu===2">
                <div class="title">面试记录</div>
                <el-table class="table" v-if="intervieweeRecordList" :data="intervieweeRecordList" :row-style="{height:'86px'}"
                          :header-row-style="{height:'86px'}">
                    <el-table-column prop="directionName" label="面试类别" width="180">
                        <template slot-scope="scope">
                            <div class="type">
                                <el-image class="type-icon" :src="require('@/assets/mock/settings.png')"></el-image>
                                {{scope.row.directionName}}
                            </div>
                        </template>
                    </el-table-column>
                    <el-table-column prop="interviewerName" label="面试官" width="120">
                        <template slot-scope="scope">
                            <div class="type">
                                {{scope.row.interviewerName}}
                            </div>
                        </template>
                    </el-table-column>
                    <el-table-column prop="time" label="面试时间" width="280">
                        <template slot-scope="scope">
                            <div class="type">
                                {{scope.row.beginTime | timestampToDateTime}}
                            </div>
                        </template>
                    </el-table-column>
                    <el-table-column prop="status" label="面试状态" width="120">
                        <template slot-scope="scope">
                            <div class="type">
                                {{statusList[scope.row.status]}}
                            </div>
                        </template>
                    </el-table-column>
                    <el-table-column prop="time" label="操作" width="130">
                        <template slot-scope="scope">
                            <div class="view" @click="onView(scope.row)">{{operationList[scope.row.status]}}</div>
                        </template>
                    </el-table-column>
                    <template slot="empty">
                        <div style="display: flex; flex-direction: column; align-items: center; margin: 0 auto;">
                            <el-image :src="require('@/assets/mock/empty2.png')" style="width:109px; height:156px;"></el-image>
                            <div style="font-size: 21px; font-weight: 600; color: #3D6FF4; line-height: 29px; margin-top: 20px;">当前暂无面试记录</div>
                        </div>
                    </template>
                </el-table>
                <el-pagination size="medium" class="pagination" layout="prev, pager, next, jumper" :total="intervieweeTotal" :page-size="5"
                               v-if="intervieweeTotal>0"
                               :current-page.sync="intervieweePage"
                               @current-change="getInterviewRecord"></el-pagination>
            </div>
            <!--            面试官身份-->
            <div class="record-container" v-show="identity===2 && menu===2">
                <div class="title">面试记录</div>
                <el-table class="table" v-if="interviewerRecordList" :data="interviewerRecordList" :row-style="{height:'86px'}"
                          :header-row-style="{height:'86px'}">
                    <el-table-column prop="directionName" label="面试类别" width="260">
                        <template slot-scope="scope">
                            <div class="type">
                                <el-image class="type-icon" :src="require('@/assets/mock/settings.png')"></el-image>
                                {{scope.row.directionName}}
                            </div>
                        </template>
                    </el-table-column>
                    <el-table-column prop="interviewerName" label="面试者" width="190">
                        <template slot-scope="scope">
                            <div class="type">
                                {{scope.row.userName}}
                            </div>
                        </template>
                    </el-table-column>
                    <el-table-column prop="time" label="面试时间" width="280">
                        <template slot-scope="scope">
                            <div class="type">
                                {{scope.row.beginTime | timestampToDateTime}}
                            </div>
                        </template>
                    </el-table-column>
                    <el-table-column label="操作" width="110">
                        <template slot-scope="scope">
                            <!--                            <div class="view" @click="onView(scope.row)">查看评价</div>-->
                        </template>
                    </el-table-column>
                    <template slot="empty">
                        <div style="display: flex; flex-direction: column; align-items: center; margin: 20px auto;">
                            <el-image :src="require('@/assets/mock/empty2.png')" style="width:109px; height:156px;"></el-image>
                            <div style="font-size: 21px; font-weight: 600; color: #3D6FF4; line-height: 29px; margin-top: 20px; padding-bottom: 50px">当前暂无面试记录
                            </div>
                        </div>
                    </template>
                </el-table>
                <el-pagination size="medium" class="pagination" layout="prev, pager, next, jumper" :total="interviewerTotal" :page-size="5"
                               v-if="interviewerTotal>0"
                               :current-page.sync="interviewerPage"
                               @current-change="getInterviewRecord"></el-pagination>
            </div>
            <div class="record-container" v-show="identity===2 && menu===4">
                <div class="title" style="margin-bottom: 20px;">我的收益</div>
                <el-table class="table" v-if="incomeList" :data="incomeList" :row-style="{height:'86px'}" :header-row-style="{height:'86px'}"
                          :show-header="false">
                    <el-table-column width="120">
                        <template slot-scope="scope">
                            <div class="type">
                                {{scope.row.month}}
                            </div>
                        </template>
                    </el-table-column>
                    <el-table-column width="200">
                        <template slot-scope="scope">
                            <div class="type">
                                总收入：{{scope.row.receivable}}元
                            </div>
                        </template>
                    </el-table-column>
                    <el-table-column width="200">
                        <template slot-scope="scope">
                            <div class="type">
                                实际收入：{{scope.row.receipts}}元
                            </div>
                        </template>
                    </el-table-column>
                    <el-table-column prop="time" width="130">
                        <template slot-scope="scope">
                            <div class="type">
                                {{scope.row.status===0?'月底结算':scope.row.status===1?'已到账':''}}
                            </div>
                        </template>
                    </el-table-column>
                    <el-table-column width="196">
                        <template slot-scope="scope">
                            <div class="detail" @click="onDetail(scope.row)">查看当月明细</div>
                        </template>
                    </el-table-column>
                    <template slot="empty">
                        <div style="display: flex; flex-direction: column; align-items: center; margin: 0 auto;">
                            <el-image :src="require('@/assets/mock/empty2.png')" style="width:109px; height:156px;"></el-image>
                            <div style="font-size: 21px; font-weight: 600; color: #3D6FF4; line-height: 29px; margin-top: 20px;">当前暂无收益记录</div>
                        </div>
                    </template>
                </el-table>
                <el-pagination size="medium" class="pagination" layout="prev, pager, next, jumper" :total="incomeTotal" :page-size="5"
                               v-if="incomeTotal>0"
                               :current-page.sync="incomePage"
                               @current-change="getIncomeRecord"></el-pagination>
            </div>
        </div>


        <el-dialog title="可供预约时间"
                   :visible.sync="dialogVisible"
                   class="dialog">
            <div v-if="step===1">
                <div class="dialog-text">请选择可供预约时间</div>
                <el-time-select
                    size="small"
                    class="select"
                    placeholder="起始时间"
                    v-model="beginTime"
                    :picker-options="{
                      start: startFirst,
                      step: '00:30',
                      end: endFirst
                 }">
                </el-time-select>
                <span class="dialog-text" style="padding: 0 11px;">至</span>
                <el-time-select
                    size="small"
                    class="select"
                    placeholder="结束时间"
                    v-model="endTime"
                    :picker-options="{
                      start: startSecond,
                      step: '00:30',
                      end: endSecond
                    }">
                </el-time-select>
                <div class="dialog-text" style="margin: 20px 20px 0 0; display: inline-block;">此时间段添加在</div>
                <el-select v-model="repeat" placeholder="预约类型" class="select">
                    <el-option size="small"
                               v-for="item in typeList"
                               :key="item.value"
                               :label="item.label"
                               :value="item.value">
                    </el-option>
                </el-select>
                <div class="footer">
                    <el-button @click="dialogVisible = false" round style="width: 100px;" size="small">取消</el-button>
                    <el-button type="primary" @click="onConfirm" round style="width: 100px; margin-left: 20px;" size="small">确定</el-button>
                </div>
            </div>
        </el-dialog>

        <el-dialog :visible.sync="eventDialogVisible" width="310px" :show-close="false" top="20%">
            <div style="display: flex; flex-direction: column; align-items: center;" v-if="status!=='1' && identity===2">
                <div style="font-weight: bold;font-size: 18px">抱歉，您目前无法提供面试预约：</div>
                <div style="font-weight: bold;font-size: 18px">详情请联系客服：</div>
                <div style="font-weight: bold;font-size: 18px">xiaokefu@we.com</div>
                <el-button type="primary" primary size="mini"
                           style="width:80px; font-size:12px; height:25px; margin: 30px 0 0 0; border-radius: 2px; line-height: 20px; padding: 0;"
                           @click="onRequire2">确认
                </el-button>
            </div>
            <div style="display: flex; flex-direction: column; align-items: center;" v-else-if="!showContactEmail">
                <el-button type="primary" size="medium" style="width: 150px; height: 50px;" @click="onEntryWebRTC">进入视频面试</el-button>
                <el-button type="info" plain size="mini"
                           style="width:80px; font-size:12px; height:25px; margin: 30px 0 0 0; border-radius: 2px; line-height: 20px; padding: 0;"
                           @click="onCancelInterview">取消面试预约
                </el-button>
            </div>

            <div style="display: flex; flex-direction: column; align-items: center;" v-else-if="showContactEmail">
                <div style="font-weight: bold;font-size: 18px">请联系人工客服：</div>
                <div style="font-weight: bold;font-size: 18px">xiaokefu@we.com</div>
                <el-button type="primary" primary size="mini"
                           style="width:80px; font-size:12px; height:25px; margin: 30px 0 0 0; border-radius: 2px; line-height: 20px; padding: 0;"
                           @click="onRequire">确认
                </el-button>
            </div>
        </el-dialog>

        <el-dialog title="提示" :visible.sync="payDialogVisible" width="600px">
            <div style="margin-bottom: 18px;"><span class="dialog-text">您预约的时间为：</span><span class="dialog-text2">{{orderInfo.beginTime|timestampToMonthDateHoursMinutes}}至{{orderInfo.endTime|timestampToMonthDateHoursMinutes}}</span>
            </div>
            <div style="margin-bottom: 18px;"><span class="dialog-text">您预约的类型为：</span><span class="dialog-text2">{{orderInfo.directionName}}</span>
            </div>
            <div style="width:100%; height:1px; background:#C9DAFB;margin-bottom: 18px;"></div>
            <div style="font-size: 24px; color: #333333; line-height: 33px;margin-bottom: 18px;">共计：
                <span style="color:#3D6FF4;">￥{{orderInfo.amount}}</span>
                <span style="color:#bdbdbd; font-size: 12px; padding-left: 8px;">请使用微信扫描下方二维码进行支付，10分钟内有效</span>
            </div>

            <!-- <div class="pay-type" :style="{flexDirection:payType==='WEIXIN_NATIVE'?'row-reverse':'row'}">-->
            <!--     <el-button v-if="payType!=='WEIXIN_NATIVE'" type="primary" round @click="onWeChat" style="width: 200px;">{{payType?'切换为微信支付':'使用微信支付'}}-->
            <!--     </el-button>-->
            <!--     <el-button v-if="payType!=='ALIPAY_NATIVE'" type="primary" round @click="onAliPay" style="width: 200px;">{{payType?'切换为支付宝支付':'使用支付宝支付'}}-->
            <!--     </el-button>-->
            <!--     <div v-if="payType" id="qrcode" v-loading="qrcodeLoading" style="width: 200px;"></div>-->
            <!-- </div>-->

            <div id="qrcode" v-loading="qrcodeLoading" style="width: 200px; height: 200px; margin: 0 auto;"></div>
            <div style="text-align: center; margin-top: 20px;">
                <el-button type="primary" @click="onPaymentCompleted" round style="width: 100px; margin-left: 20px;" size="small">我已支付</el-button>
            </div>
        </el-dialog>

        <el-dialog title="我的收益" :visible.sync="incomeDialogVisible" width="690px">
            <div class="title"></div>
            <el-table class="table" v-if="incomeDetailList" :data="incomeDetailList">
                <el-table-column width="120" label="面试者" prop="username">
                </el-table-column>
                <el-table-column width="200" label="日期">
                    <template slot-scope="scope">
                        <div class="type">
                            {{scope.row.beginTime | timestampToMonthDateHoursMinutes }}
                        </div>
                    </template>
                </el-table-column>
                <el-table-column width="200" label="面试类型" prop="direction">
                </el-table-column>
                <el-table-column width="130" label="金额" prop="amount">
                </el-table-column>
                <template slot="empty">
                    <div style="display: flex; flex-direction: column; align-items: center; margin: 0 auto;">
                        <el-image :src="require('@/assets/mock/empty2.png')" style="width:109px; height:156px;"></el-image>
                        <div style="font-size: 21px; font-weight: 600; color: #3D6FF4; line-height: 29px; margin-top: 20px;">当前暂无收益记录</div>
                    </div>
                </template>
            </el-table>
            <el-pagination size="medium" class="pagination" layout="prev, pager, next, jumper" :total="incomeDetailTotal" :page-size="5"
                           v-if="incomeDetailTotal>0"
                           :current-page.sync="incomeDetailPage"
                           @current-change="getIncomeDetail"></el-pagination>
        </el-dialog>
    </div>
</template>

<script>
    import FullCalendar from '@fullcalendar/vue'
    import dayGridPlugin from '@fullcalendar/daygrid'
    import momentPlugin from '@fullcalendar/moment'
    import interactionPlugin from '@fullcalendar/interaction'
    import QRCode from "qrcodejs2";

    export default {
        name: "MockMinePage",
        components: {
            FullCalendar // make the <FullCalendar> tag available
        },
        data() {
            return {
                dialogVisible: false, // 对话框是否可见
                showContactEmail: false,
                date: 0, // 预约日期
                startFirst: '00:00', // 预约最小开始时间
                beginTime: 0, // 预约起始时间
                endFirst: '23:30', // 预约最大开始时间
                endTime: 0, // 预约结束时间
                endSecond: '24:00', // 预约最大结束时间
                repeat: "1", // 要预约的类型
                typeList: [{label: "不重复", value: "1"},
                    {label: "按周重复", value: "2"},
                    {label: "按月重复", value: "3"}
                ], // 重复类型  1=不重复，2=按周重复，3=按月重复
                step: 1, // 当前预约步骤

                eventItem: undefined, // 选中的事件
                eventDialogVisible: false, // 事件对话框

                calendarOptions: {
                    // 语言
                    locale: "zh-cn",

                    plugins: [dayGridPlugin, interactionPlugin, momentPlugin],

                    // 日历配置
                    initialView: 'dayGridMonth',
                    initialDate: Date.now(),
                    headerToolbar: {
                        start: 'title', // will normally be on the left. if RTL, will be on the right
                        center: '',
                        end: '' // prev,today,next dayGridMonth,dayGridWeek,dayGridDay
                    },
                    titleFormat: {year: 'numeric', month: 'long'}, // 表头日期格式
                    aspectRatio: 1.8, // 宽高比

                    // 日期点击
                    dateClick: this.onDateClick,

                    // 事件定义
                    events: [],

                    // 事件点击
                    eventClick: this.onEventClick,

                    // 事件显示
                    eventDisplay: "block", // 展示方式，block：块    list-item：点
                    eventColor: "#D3F261", // 圆点颜色
                    eventTextColor: '#333333', // 文本颜色
                    eventBorderColor: '#D3F261', // 块边框颜色
                    eventBackgroundColor: '#D3F261', // 块背景色
                    displayEventEnd: true, // 显示事件结束时间

                    slotLabelFormat: 'HH:mm',
                    eventTimeFormat: 'HH:mm',

                    eventClassNames: "event-text"
                },

                calendarApi: undefined, // 日历api
                userIdentity: 1, // 用户身份   1：面试者    2：面试官
                identity: 1, // 当前选择的身份  1：面试者    2：面试官
                menu: 1, // 选中按钮  1：我的日历   2：面试记录
                intervieweeRecordList: undefined, // 面试者记录
                intervieweePage: 1, // 面试者记录页码
                intervieweeTotal: 0, // 面试者记录总数
                interviewerRecordList: undefined, // 面试官记录
                interviewerPage: 1, // 面试官记录页码
                interviewerTotal: 0, // 面试官记录总数

                incomeList: undefined, // 收益列表
                incomePage: 1, // 面试官记录页码
                incomeTotal: 0, // 面试官记录总数

                status: undefined, // 面试官身份状态
                statusList: ['', '待支付', '待面试', '面试中', '已面试', '支付失败', '已退款'],
                operationList: ['', '去支付', '进入面试间', '进入面试间', '', '', ''],

                payDialogVisible: false, // 支付对话框
                payType: undefined, // WEIXIN_NATIVE,  ALIPAY_NATIVE
                qrCode: undefined, // 二维码对象
                qrcodeLoading: false, // 二维码加载中
                orderInfo: {}, // 订单信息

                incomeDialogVisible: false, // 收益明细对话框
                incomeDetailId: undefined, // 查看收益的id
                incomeDetailList: undefined, // 收益明细记录
                incomeDetailPage: 1, // 收益明细记录页码
                incomeDetailTotal: 0, // 收益明细记录总数
                myInfo: {},//面试官信息
            }
        },
        watch: {
            beginTime() {
                if (this.beginTime && this.endTime) {
                    let time1 = this.getDateTime(this.date, this.beginTime);
                    let time2 = this.getDateTime(this.date, this.endTime);
                    if (time1 >= time2) {
                        this.endTime = this.getHourMinutes(new Date(time1 + 30 * 60 * 1000));
                    }
                }
            }
        },
        computed: {
            name() {
                return this.myInfo.nickName ? this.myInfo.nickName : this.$store.state.user.name;
            },

            startSecond() {
                return this.getHourMinutes(new Date(this.getDateTime(this.date, this.beginTime || this.startFirst) + 30 * 60 * 1000));
            }
        },
        mounted() {
            this.getInterviewerInfo();
            this.onInterviewee();
        },
        methods: {
            // 获取面试官信息，判断面试官身份
            getInterviewerInfo() {
                this.$axios.get("/mock/interviewer/my-info").then(data => {
                    this.userIdentity = data.data ? 2 : 1;
                    this.status = data.data && data.data.status;
                    this.myInfo.avatar = data.data && data.data.avatar || this.$store.state.user.avatar;
                    this.myInfo.name = data.data && data.data.nickName || this.$store.state.user.name;
                })
            },
            // 面试者
            onInterviewee() {
                // 切换身份为面试者
                this.identity = 1;
                if (this.menu === 1) {
                    this.getIntervieweeEvent();
                } else if (this.menu === 2) {
                    this.getInterviewRecord();
                } else if (this.menu === 4) {
                    this.menu = 1;
                    this.getIntervieweeEvent();
                }
            },


            // 面试官
            onInterviewer() {
                if (this.userIdentity === 2) {
                    // 切换身份为面视官
                    this.identity = 2;
                    if (this.menu === 1) {
                        this.getInterviewerEvent();
                    } else if (this.menu === 2) {
                        this.getInterviewRecord();
                    }
                } else {
                    this.$message.warning("您当前还未认证面试官，请先认证后操作!");
                }
            },

            // 上月
            onPrev() {
                this.calendarApi = this.$refs.fullCalendar.getApi();
                this.calendarApi.prev();
                if (this.identity === 1) {
                    this.getIntervieweeEvent();
                } else {
                    this.getInterviewerEvent();
                }

            },

            // 下月
            onNext() {
                this.calendarApi = this.$refs.fullCalendar.getApi();
                this.calendarApi.next();
                if (this.identity === 1) {
                    this.getIntervieweeEvent();
                } else {
                    this.getInterviewerEvent();
                }
            },

            // 点击我的日历
            onMineCalendar() {
                this.menu = 1;
                if (this.identity === 1) {
                    this.getIntervieweeEvent();
                } else {
                    this.getInterviewerEvent();
                }
            },

            // 点击面试记录
            onMockRecord() {
                this.menu = 2;
                this.getInterviewRecord();
            },

            // 点击立即入住
            onSettleIn() {
                this.menu = 3;
                this.$router.push("/interviewSecretPage");
            },

            // 我的收益
            onIncome() {
                this.menu = 4;
                this.getIncomeRecord();
            },

            // 操作面试记录
            onView(item) {
                console.log(item);
                if (item.status === 1) { // 去支付
                    this.orderInfo = item;
                    this.payDialogVisible = true;
                    this.qrcodeLoading = true;
                    this.$axios.get(`/pay/goods/order/${item.orderId}`).then(data => {
                        if (this.qrCode) {
                            this.qrCode.makeCode(data.data.qrCodeUrl);
                        } else {
                            this.qrCode = new QRCode('qrcode', {
                                width: 200,             // 宽度
                                height: 200,            // 高度
                                text: data.data.qrCodeUrl, // 二维码内容
                                render: 'canvas',       // 设置渲染方式（有两种方式 table和canvas，默认是canvas）
                                colorDark: "#000000",     //二维码颜色
                                colorLight: "#ffffff"  //二维码背景色
                            });
                        }
                        this.qrcodeLoading = false;
                    })
                } else if (item.status === 2 || item.status === 3) { // 进入面时间
                    if (item.endTime < Date.now()) {
                        this.$message.warning("面试已结束");
                    } else if (Date.now() < item.beginTime - 15 * 60 * 1000) {
                        this.$message.warning("开始前15分钟可以进入房间等待");
                    } else {
                        this.$router.push(`/webRTC/${item.reservationId}/${item.interviewerId}/${this.identity}`);
                    }
                }
            },

            // 查看收益明细
            onDetail(item) {
                this.incomeDetailId = item.id;
                this.getIncomeDetail();
            },

            // 添加可预约时间确认
            onConfirm() {
                if (!this.beginTime) {
                    this.$message.warning("请选择预约开始时间")
                } else if (!this.endTime) {
                    this.$message.warning("请选择预约结束时间")
                } else if (!this.repeat) {
                    this.$message.warning("请选择预约类型")
                } else {
                    let beginTime = this.getDate(this.date, this.beginTime);
                    let endTime = this.getDate(this.date, this.endTime) - (this.endTime === "24:00" ? 1 : 0);
                    let repeat = this.repeat;
                    if (endTime <= beginTime) {
                        this.$message.warning("结束时间必须大于开始时间")
                    } else {
                        this.$axios.post("/mock/interview/time", {beginTime, endTime, repeat}).then(data => {
                            let event = data.data;
                            this.calendarOptions.events.push({
                                id: event.id,
                                interviewerId: event.id,
                                start: parseInt(event.beginTime),
                                end: parseInt(event.endTime),
                                borderColor: '#D3F261', // 块边框颜色
                                backgroundColor: '#D3F261', // 块背景色
                            });
                            this.beginTime = undefined;
                            this.endTime = undefined;
                            this.dialogVisible = false;
                        })
                    }
                }
            },

            // 面试官点击日期，添加可预约时间
            onDateClick(info) {
                if (this.identity === 2) { // 选择了面试官
                    if (this.status === '1') {
                        if (info.date >= this.getNextDay()) {
                            this.step = 1;
                            this.startFirst = '00:00';
                            this.date = info.date;
                            this.dialogVisible = true;
                        } else if (info.date >= this.getZeroOfToday()) {
                            this.step = 1;
                            let date = new Date();
                            if (date.getMinutes() < 30) {
                                date.setMinutes(30, 0);
                            } else {
                                date.setHours(date.getHours() + 1, 0, 0);
                            }
                            this.startFirst = `${this.getDoubleValue(date.getHours())}:${this.getDoubleValue(date.getMinutes())}`;
                            this.date = info.date;
                            this.dialogVisible = true;
                        }
                    } else {
                        this.eventDialogVisible = true;
                    }
                }
            },

            // 点击事件
            onEventClick(info) {
                if (info.event.backgroundColor === '#FFE58F') {  // #FFE58F // 被预约  // #D3F261 // 可预约
                    this.eventItem = {
                        id: info.event.id,
                        start: info.event.start,
                        end: info.event.end,
                        interviewerId: info.event.extendedProps.interviewerId
                    };
                    this.showContactEmail = false;
                    this.eventDialogVisible = true;
                }
            },

            // 进入视频面试
            onEntryWebRTC() {
                console.log(this.eventItem.start);
                if (this.eventItem.end < Date.now()) {
                    this.$message.warning("面试已结束");
                } else if (Date.now() < this.eventItem.start - 15 * 60 * 1000) {
                    this.$message.warning("开始前15分钟可以进入房间等待");
                } else {
                    this.$router.push(`/webRTC/${this.eventItem.id}/${this.eventItem.interviewerId}/${this.identity}`);
                }
            },

            // 取消预约面试
            onCancelInterview() {
                this.showContactEmail = true;
            },

            // 联系客服确认
            onRequire() {
                this.eventDialogVisible = false;
            },
            //面试官下架弹窗
            onRequire2() {
                this.eventDialogVisible = false;
            },

            // 获取数据
            // 面试官获取我的可预约事件（当前月）
            getInterviewerEvent() {
                this.calendarOptions.events = [];
                let beginTime = this.getFirstDayOfMonth(this.calendarApi.getDate());
                let endTime = this.getLastDayOfMonth(this.calendarApi.getDate());
                this.$axios.get(`/mock/interview/time/my/${beginTime}/${endTime}`).then(data => {
                    let events = [];
                    data.data.forEach(item => {
                        let eventList = [ // 保留被人预约剩余的可预约事件，默认为我的总预约时间段
                            {
                                interviewerId: item.interviewerId,
                                start: item.beginTime,
                                end: item.endTime,
                                borderColor: '#D3F261', // 块边框颜色
                                backgroundColor: '#D3F261', // 块背景色
                            }
                        ];

                        // 遍历被预约事件
                        item.reservationList.forEach(reservation => { // 被预约事件
                            // 在可预约时间段内，去除被预约时间段，保留分散的可预约事件
                            let event = eventList.find(event => {
                                return reservation.beginTime >= event.start && reservation.endTime <= event.end;
                            })

                            if (event) {
                                if (event.start === reservation.beginTime && event.end !== reservation.endTime) { // 被预约时间段为可预约时间段的开头
                                    event.start = reservation.endTime; // 将可预约开始时间延迟至被预约时间结束
                                } else if (event.end === reservation.endTime && event.start !== reservation.beginTime) {  // 被预约时间段为可预约时间段的结尾
                                    event.end = reservation.beginTime; // 将可预约结束时间提前至被预约时间开始
                                } else if (event.start !== reservation.beginTime && event.end !== reservation.endTime) { // 被预约时间段为可预约时间段的中间部分
                                    event.end = reservation.beginTime; // 将可预约结束时间提前至被预约时间开始
                                    eventList.push({ // 追加可预约时间段，从被预约结束时间，至可预约时间的结束
                                        start: reservation.endTime,
                                        end: item.endTime,
                                        borderColor: '#D3F261', // 块边框颜色
                                        backgroundColor: '#D3F261', // 块背景色
                                    })
                                } else { // 被预约时间为可预约时间端的全部
                                    eventList = eventList.filter(it => it.start !== event.start && it.end !== event.end);
                                }
                            }

                            // 添加被预约事件
                            events.push({
                                id: reservation.id, // 被预约的id，可预约不需要
                                interviewerId: reservation.userId, // 预约人
                                start: parseInt(reservation.beginTime),
                                end: parseInt(reservation.endTime),
                                borderColor: '#FFE58F',
                                backgroundColor: '#FFE58F'
                            })
                        })

                        events = events.concat(eventList);
                    })
                    this.calendarOptions.events = events;
                })
            },

            // 面试者获取我的预约成功事件
            getIntervieweeEvent() {
                if (this.$refs.fullCalendar) {
                    this.calendarApi = this.$refs.fullCalendar.getApi();
                    let beginTime = this.getFirstDayOfMonth(this.calendarApi.getDate());
                    let endTime = this.getLastDayOfMonth(this.calendarApi.getDate());
                    this.$axios.get(`/mock/interview/reservation/my/${beginTime}/${endTime}`).then(data => {
                        this.calendarOptions.events = data.data.map(item => {
                            return {
                                id: item.id,
                                interviewerId: item.interviewerId,
                                reservationList: item.reservationList,
                                start: parseInt(item.beginTime),
                                end: parseInt(item.endTime),
                                borderColor: '#FFE58F', // 块边框颜色
                                backgroundColor: '#FFE58F', // 块背景色
                            }
                        });
                    })
                }
            },

            // 获取我的面试记录
            getInterviewRecord() {
                if (this.identity === 2) {
                    this.$axios.get('/mock/interview/records/interviewer', {params: {page: this.interviewerPage, limit: 5, sort: "-id"}}).then(data => {
                        this.interviewerRecordList = data.data.list;
                        this.interviewerTotal = data.data.total;
                    })
                } else {
                    this.$axios.get('/mock/interview/records/user', {params: {page: this.intervieweePage, limit: 5, sort: "-id"}}).then(data => {
                        this.intervieweeRecordList = data.data.list;
                        this.intervieweeTotal = data.data.total;
                    })
                }
            },

            // 获取我的面试记录
            getIncomeRecord() {
                if (this.identity === 2) {
                    this.$axios.get('/mock/interviewer/income/my', {params: {page: this.incomePage, limit: 5}}).then(data => {
                        this.incomeList = data.data.list;
                        this.incomeTotal = data.data.total;
                    })
                }
            },

            // 获取收益明细
            getIncomeDetail() {
                this.incomeDialogVisible = true;
                this.$axios.get(`/mock/interviewer/income/my-detail/${this.incomeDetailId}`, {params: {page: this.incomeDetailPage, limit: 5}}).then(data => {
                    this.incomeDetailTotal = data.data.total;
                    this.incomeDetailList = data.data.list;
                    console.log(data);
                })
            },

            // 我已支付
            onPaymentCompleted() {
                // 检查支付状态
                this.$axios(`/pay/order/status/${this.orderInfo.orderId}`).then(data => {
                    if (data.data === 1) { // 已付款
                        this.$message.success("已付款")
                        this.payDialogVisible = false;
                    } else if (data.data === 0) {
                        this.$message.warning("暂未支付")
                    } else if (data.data === -1) {
                        this.$message.warning("支付失败")
                    }
                })
            },

            // 工具方法
            // Date + "12:00"  => (Date 12:00).getTime();
            getDateTime(date, time) {
                if (date) {
                    let t = time.split(":");
                    const temp = new Date(date);
                    temp.setHours(t[0], t[1])
                    return temp.getTime();
                } else {
                    return Date.now();
                }
            },

            getHourMinutes(time, zero) {
                if (time) {
                    let minutes = time.getMinutes() < 10 ? "0" + time.getMinutes() : time.getMinutes();
                    if (!zero && time.getHours() === 0 && time.getMinutes() === 0) {
                        return "24:00";
                    } else if (time.getHours() < 10) {
                        return '0' + time.getHours() + ":" + minutes;
                    } else {
                        return time.getHours() + ":" + minutes;
                    }
                } else {
                    return "00:00";
                }
            },

            getDate(date, time) {
                let t = time.split(":");
                date.setHours(t[0], t[1], 0, 0)
                return date.getTime();
            },

            getZeroOfToday() {
                const temp = new Date();
                temp.setHours(0, 0, 0, 0);
                return temp.getTime();
            },

            getNextDay() {
                const temp = new Date();
                temp.setHours(0, 0, 0, 0);
                return temp.getTime() + 24 * 60 * 60 * 1000;
            },

            getFirstDayOfMonth(date) {
                const temp = new Date(date.getTime());
                temp.setDate(1)
                temp.setHours(0, 0, 0, 0);
                return temp.getTime();
            },

            getLastDayOfMonth(date) {
                const temp = new Date(date.getTime());
                temp.setMonth(temp.getMonth() + 1);
                temp.setDate(1)
                temp.setHours(0, 0, 0, 0);
                return temp.getTime() - 1;
            },

            getDoubleValue(num) {
                return num < 10 ? '0' + num : num
            }
        }
    }
</script>

<style scoped lang="scss">

    .app-container {
        width: 1175px;
        margin: 0 auto;

        .title-container {
            display: flex;
            align-items: center;
            justify-content: space-between;
            margin-bottom: 30px;

            .title {
                font-size: 24px;
                color: #333333;
                line-height: 50px;
            }

            .identity-change-container {
                display: flex;
                align-items: center;

                .interviewee-container {
                    width: 110px;
                    height: 42px;
                    border-radius: 21px 0 0 21px;
                    border: 1px solid #FA8C16;
                    background: rgba(250, 140, 22, 0.1);
                    display: flex;
                    align-items: center;
                    justify-content: center;
                    padding-left: 8px;
                    font-size: 21px;
                    color: #FA8C16;
                    line-height: 29px;

                    &:hover {
                        cursor: pointer;
                        background: #D4771356;
                    }
                }

                .interviewer-container {
                    width: 110px;
                    height: 42px;
                    border-radius: 0 21px 21px 0;
                    border: 1px solid #FA8C16;
                    background: rgba(250, 140, 22, 0.1);
                    display: flex;
                    align-items: center;
                    justify-content: center;
                    padding-right: 8px;
                    font-size: 21px;
                    color: #FA8C16;
                    line-height: 29px;

                    &:hover {
                        cursor: pointer;
                        background: #D4771356;
                    }
                }

                .select {
                    background: #FA8C16;
                    color: #FFFFFF;

                    &:hover {
                        cursor: pointer;
                        background: #d7750c;
                    }
                }
            }
        }

        .content-container {
            display: flex;
            justify-content: space-between;

            .button-container {
                width: 140px;
                display: flex;
                flex-direction: column;
                align-items: center;

                .avatar {
                    border: #5d9bfc 2px solid;
                }

                .username {
                    text-align: center;
                    margin-top: 11px;
                    font-size: 18px;
                    color: #1C2541;
                    line-height: 25px;
                    margin-bottom: 70px;
                }

                .button {
                    width: 140px;
                    height: 35px;
                    display: flex;
                    align-items: center;
                    justify-content: center;
                    margin-bottom: 25px;
                    color: #3D6FF4;

                    &:hover {
                        cursor: pointer;
                    }
                }

                .select {
                    background: #3D6FF4;
                    border-radius: 18px;
                    color: white;
                }
            }

            .calendar-container {
                width: 1000px;
                position: relative;
                box-shadow: 0 5px 13px 0 rgba(19, 67, 123, 0.2);
                padding: 16px 10px 16px;

                ::v-deep .fc-toolbar-title {
                    margin-left: 20px;
                }

                ::v-deep .event-text {
                    font-size: 12px;

                    &:hover {
                        cursor: pointer;
                    }
                }

                ::v-deep .fc .fc-daygrid-day-top {
                    flex-direction: row;
                }

                .additive-container {
                    position: absolute;
                    right: 44px;
                    top: 20px;
                    display: flex;
                    align-items: center;

                    .tag2 {
                        width: 18px;
                        height: 18px;
                        border-radius: 9px;
                        background: #D3F261;
                    }

                    .tag {
                        width: 18px;
                        height: 18px;
                        border-radius: 9px;
                        background: #FFE58F;
                    }

                    .tag-text {
                        font-size: 18px;
                        color: #333333;
                        line-height: 25px;
                        margin-left: 4px;
                        margin-right: 31px;
                    }

                    .left-container {
                        width: 32px;
                        height: 26px;
                        border-radius: 13px 0 0 13px;
                        background: #F4F5F8;
                        border: 1px solid #7195CA;
                        display: flex;
                        align-items: center;
                        justify-content: center;
                        padding-left: 8px;

                        &:hover {
                            cursor: pointer;
                            background: #d6d7d9;
                        }

                        .left-arrow {
                            width: 10px;
                            height: 10px;
                            border-left: 2px solid #7195CA;
                            border-bottom: 2px solid #7195CA;
                            transform: rotateZ(45deg);
                        }
                    }

                    .right-container {
                        width: 32px;
                        height: 26px;
                        border-radius: 0 13px 13px 0;
                        background: #7195CA;
                        border: 1px solid #7195CA;
                        display: flex;
                        align-items: center;
                        justify-content: center;
                        padding-right: 8px;

                        &:hover {
                            cursor: pointer;
                            background: #5677a1;
                        }

                        .right-arrow {
                            width: 10px;
                            height: 10px;
                            border-top: 2px solid #F4F5F8;
                            border-right: 2px solid #F4F5F8;
                            transform: rotateZ(45deg);
                        }
                    }
                }
            }

            .record-container {
                width: 925px;
                box-shadow: 0 5px 13px 0 rgba(19, 67, 123, 0.2);
                padding: 21px 35px;
                background: white;
                display: flex;
                flex-direction: column;

                .title {
                    font-size: 36px;
                    color: #333333;
                    line-height: 50px;
                }

                .table {
                    width: 100%;
                    height: 430px;

                    ::v-deep .is-leaf {
                        border: none;
                        vertical-align: middle;
                        font-size: 21px;
                        color: #999999;
                        line-height: 29px;
                        font-style: normal;
                        font-weight: 500;
                    }

                    .type {
                        display: flex;
                        align-items: center;
                        font-size: 21px;
                        color: #333333;
                        line-height: 29px;

                        .type-icon {
                            margin-right: 10px;
                            width: 34px;
                            height: 34px;
                            min-width: 34px;
                        }
                    }

                    .view {
                        font-size: 21px;
                        color: #3D6FF4;
                        line-height: 29px;

                        &:hover {
                            cursor: pointer;
                        }
                    }

                    .detail {
                        width: 185px;
                        height: 41px;
                        background: rgba(255, 189, 105, 0.3);
                        border-radius: 21px;
                        border: 1px solid #F78259;
                        font-size: 18px;
                        color: #F78259;
                        line-height: 41px;
                        text-align: center;

                        &:hover {
                            cursor: pointer;
                        }
                    }
                }

                .pagination {
                    align-self: center;
                    margin-top: 20px;
                    align-items: center;

                    ::v-deep .number, ::v-deep .more {
                        width: 37px;
                        height: 37px;
                        border-radius: 50%;
                        background: #EEEEEE;
                        box-shadow: 0 5px 11px 0 #CCCCCC;
                        line-height: 37px;
                        text-align: center;
                        margin: 0 6px;
                        color: #999999;

                        &.active {
                            color: white;
                            background: #4C90FC;
                            box-shadow: 0 5px 11px 0 rgba(30, 150, 252, 0.5);
                        }
                    }

                    ::v-deep .btn-prev, ::v-deep .btn-next {
                        width: 37px;
                        height: 37px;
                        border-radius: 50%;
                        background: #EEEEEE;
                        box-shadow: 0 5px 11px 0 #CCCCCC;
                        line-height: 37px;
                        text-align: center;
                        margin: 0 6px;
                        color: #999999;
                        padding: 0;

                        & .el-icon {
                            font-size: 18px;
                        }
                    }

                    ::v-deep .el-pagination__jump {
                        font-size: 18px;
                        color: #999999;
                        line-height: 25px;
                        height: 37px;

                        .el-input {
                            width: 66px;
                            height: 37px;
                            margin: 0 6px;

                            .el-input__inner {
                                width: 66px;
                                height: 37px;
                                font-size: 18px;
                            }
                        }
                    }
                }
            }
        }


        .dialog {
            ::v-deep .el-dialog {
                width: 500px;
                border-radius: 17px;
            }

            ::v-deep .el-dialog__header {
                padding: 20px 30px 10px;
            }

            ::v-deep .el-dialog__body {
                padding: 0 30px 20px;
            }

            .footer {
                text-align: center;
                margin-top: 20px;
            }


            ::v-deep .el-dialog__title {
                font-size: 24px;
                color: #333333;
                line-height: 33px;
            }

            .dialog-text {
                font-size: 17px;
                color: #999999;
                line-height: 25px;
                padding-bottom: 8px;
            }

            .dialog-text2 {
                font-size: 17px;
                color: #3D6FF4;
                line-height: 25px;
            }

            .select {
                width: 150px;

                ::v-deep .el-input__inner {
                    border-radius: 17px;
                    height: 32px;
                    min-height: 32px !important;
                }

                ::v-deep .el-input__icon {
                    line-height: 32px !important;
                }
            }

            .qrcode {
                width: 200px;
                height: 200px;
                margin: 0 auto;
                display: block;
            }

            .image-success {
                width: 200px;
                height: 200px;
                margin: 0 auto;
                display: block;
            }

        }

        .pagination {
            align-self: center;
            margin-top: 20px;
            align-items: center;
            justify-content: center;

            ::v-deep .number, ::v-deep .more {
                width: 37px;
                height: 37px;
                border-radius: 50%;
                background: #EEEEEE;
                box-shadow: 0 5px 11px 0 #CCCCCC;
                line-height: 37px;
                text-align: center;
                margin: 0 6px;
                color: #999999;

                &.active {
                    color: white;
                    background: #4C90FC;
                    box-shadow: 0 5px 11px 0 rgba(30, 150, 252, 0.5);
                }
            }

            ::v-deep .btn-prev, ::v-deep .btn-next {
                width: 37px;
                height: 37px;
                border-radius: 50%;
                background: #EEEEEE;
                box-shadow: 0 5px 11px 0 #CCCCCC;
                line-height: 37px;
                text-align: center;
                margin: 0 6px;
                color: #999999;
                padding: 0;

                & .el-icon {
                    font-size: 18px;
                }
            }

            ::v-deep .el-pagination__jump {
                font-size: 18px;
                color: #999999;
                line-height: 25px;
                height: 37px;

                .el-input {
                    width: 66px;
                    height: 37px;
                    margin: 0 6px;

                    .el-input__inner {
                        width: 66px;
                        height: 37px;
                        font-size: 18px;
                    }
                }
            }
        }
    }
</style>
