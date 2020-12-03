<template>
    <div class="app-container">
        <div class="title">面试预约</div>
        <div class="calendar-container">
            <FullCalendar ref="fullCalendar" :options="calendarOptions"/>
            <div class="additive-container">
                <div class="tag"/>
                <div class="tag-text">可预约时间</div>
                <div class="left-container" @click="onPrev">
                    <div class="left-arrow"/>
                </div>
                <div class="right-container" @click="onNext">
                    <div class="right-arrow"/>
                </div>
            </div>
        </div>

        <el-dialog title="预约面试时间"
                   :visible.sync="dialogVisible"
                   class="dialog"
                   :before-close="onDialogClose">
            <div v-if="step===1">
                <div class="dialog-text">请选择您想预约的时间段</div>
                <el-time-select
                        size="small"
                        class="select"
                        placeholder="起始时间"
                        v-model="startTime"
                        :picker-options="{
                      start: start,
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
                      start: secondTime,
                      step: '00:30',
                      end: endSecond
                    }">
                </el-time-select>
                <div class="dialog-text" style="margin-top: 20px;">请选择您想预约的类型</div>
                <el-select v-model="directionId" placeholder="预约类型" class="select">
                    <el-option size="small"
                               v-for="item in directionList"
                               :key="item.id"
                               :label="item.direction"
                               :value="item.id">
                    </el-option>
                </el-select>
                <div class="footer">
                    <el-button @click="dialogVisible = false" round style="width: 100px;" size="small">取消</el-button>
                    <el-button type="primary" @click="toStep2" round style="width: 100px; margin-left: 20px;" size="small">下一步</el-button>
                </div>
            </div>
            <div v-else-if="step===2">
                <div style="margin-bottom: 18px;"><span class="dialog-text">您预约的时间为：</span><span class="dialog-text2">{{startTime}}至{{endTime}}</span></div>
                <div style="margin-bottom: 18px;"><span class="dialog-text">您预约的类型为：</span><span class="dialog-text2">{{findDirection()}}</span>
                </div>
                <div style="width:100%; height:1px; background:#C9DAFB;margin-bottom: 18px;"></div>
                <div style="font-size: 24px; color: #333333; line-height: 33px;margin-bottom: 18px;">共计：
                    <span style="color:#3D6FF4;">￥{{amount}}</span>
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

                <div class="footer">
                    <el-button @click="step=1" round style="width: 100px;" size="small">上一步</el-button>
                    <!--                    <el-button type="primary" @click="onPaymentCompleted" round style="width: 100px; margin-left: 20px;" size="small" :disabled="!payType">-->
                    <!--                        {{payType?'我已支付':'选择支付方式'}}-->
                    <!--                    </el-button>-->
                    <el-button type="primary" @click="onPaymentCompleted" round style="width: 100px; margin-left: 20px;" size="small">{{orderId?'我已支付':'关闭'}}
                    </el-button>
                </div>
            </div>
            <div v-else-if="step===3">
                <el-image class="image-success" :src="require('@/assets/pay-sucsses.png')"></el-image>
                <div style="font-size: 28px; color: #333333; line-height: 40px;margin: 13px 0; text-align: center;">恭喜您，预约成功！</div>
                <el-button type="primary" @click="onBack" round style="width: 200px; margin: 0 auto; display: block;">返回</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
    import FullCalendar from '@fullcalendar/vue'
    import dayGridPlugin from '@fullcalendar/daygrid'
    import interactionPlugin from '@fullcalendar/interaction'
    import QRCode from 'qrcodejs2'

    export default {
        name: "MockPromisePage",
        components: {
            FullCalendar // make the <FullCalendar> tag available
        },
        data() {
            return {
                dialogVisible: false, // 对话框是否可见

                date: 0, // 预约日期
                startTime: '00:00', // 预约起始时间
                endTime: '00:00', // 预约结束时间
                start: '00:00', // 预约最小时间
                endFirst: '23:59', // 预约最大时间
                endSecond: '23:59', // 预约最大时间
                interviewerTimeId: undefined, // 预约时间id
                directionId: undefined, // 要预约的方向Id
                directionList: [], // 可选的预约类型
                step: 1, // 当前预约步骤
                attachParam: undefined, // 预约id
                amount: undefined, // 预约id

                payType: undefined, // WEIXIN_NATIVE,  ALIPAY_NATIVE
                qrCode: undefined, // 二维码对象
                qrcodeLoading: false, // 二维码加载中
                orderId: undefined, // 订单id

                query: {
                    beginTime: undefined,
                    endTime: undefined,
                    id: undefined,
                    interviewerId: undefined
                },

                calendarOptions: {
                    // 语言
                    locale: "zh-cn",

                    plugins: [dayGridPlugin, interactionPlugin],

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
                    eventTimeFormat: { // am / pm
                        hour: 'numeric',
                        minute: '2-digit',
                        meridiem: 'short'
                    },

                    eventClassNames: "event-text"
                },

                calendarApi: undefined, // 日历api
            }
        },


        computed: {
            secondTime() {
                return this.getHourMinutes(new Date(this.getDateTime(this.date, this.startTime || this.start) + 30 * 60 * 1000));
            }
        },
        mounted() {
            this.calendarApi = this.$refs.fullCalendar.getApi()
            this.query.interviewerId = this.$route.params.id;
            this.getEvent();
            this.getDirection();
        },
        methods: {
            // 上一月
            onPrev() {
                this.calendarApi.prev()
                this.getEvent();
            },

            // 下一月
            onNext() {
                this.calendarApi.next()
                this.getEvent();
            },

            // 检查参数，跳转下一步
            toStep2() {
                if (!this.startTime) {
                    this.$message.warning("请选择预约开始时间")
                } else if (!this.endTime) {
                    this.$message.warning("请选择预约结束时间")
                } else if (!this.directionId) {
                    this.$message.warning("请选择预约类型")
                } else {
                    // 添加预约
                    this.$axios.post("/mock/interview/reservation", {
                        beginTime: this.getDateTime(this.date, this.startTime),
                        directionId: this.directionId,
                        endTime: this.getDateTime(this.date, this.endTime),
                        interviewTimeId: this.interviewerTimeId
                    }).then(data => {
                        this.attachParam = data.data.id;
                        this.amount = data.data.amount;
                        this.step = 2;
                        this.$nextTick(() => {
                            this.onWeChat();
                        })
                    })
                }
            },

            // 关闭对话框，清楚参数
            onDialogClose(done) {
                this.startTime = undefined;
                this.endTime = undefined;
                this.directionId = undefined;
                done();
            },

            // 点击微信支付
            onWeChat() {
                this.payType = 'WEIXIN_NATIVE';
                this.getQrCode();
            },

            // 点击支付宝支付
            onAliPay() {
                this.payType = 'ALIPAY_NATIVE';
                this.getQrCode();
            },

            // 获取支付链接
            getQrCode() {
                this.qrcodeLoading = true;
                let direction = this.directionList.find(item => item.id === this.directionId);
                this.$axios.get("/pay/goods/buy", {
                    params: {
                        amount: this.amount,
                        goodsId: direction.id,
                        goodsName: direction.direction,
                        userId: this.query.interviewerId,
                        type: this.payType,
                        attachParam: this.attachParam
                    }
                }).then(data => {
                    this.orderId = data.data.payOrderId;
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
                }).catch(() => {
                    this.qrcodeLoading = false;
                })
            },

            // 我已支付
            onPaymentCompleted() {
                if (this.orderId) {
                    // 检查支付状态
                    this.$axios(`/pay/order/status/${this.orderId}`).then(data => {
                        if (data.data === 1) { // 已付款
                            this.step = 3;
                        } else if (data.data === 0) {
                            this.$message.warning("暂未支付")
                        } else if (data.data === -1) {
                            this.$message.warning("支付失败")
                        }
                    })
                } else {
                    this.dialogVisible = false;
                }
            },

            // 返回到我的模拟面试
            onBack() {
                this.dialogVisible = false;
                this.step = 1;
                this.$router.push("/mock-mine");
            },

            // 日期时间
            onDateClick(info) {
            },

            // 点击事件，显示对话框，进行预约
            onEventClick(info) {
                this.interviewerTimeId = info.event.extendedProps.interviewerTimeId;
                this.date = new Date(info.event.start);
                this.start = this.getHourMinutes(info.event.start);
                this.startTime = this.getHourMinutes(info.event.start);
                this.endFirst = this.getHourMinutes(new Date(info.event.end.getTime() - 30 * 60 * 1000));
                this.endSecond = this.getHourMinutes(info.event.end);
                this.endTime = this.getHourMinutes(new Date(info.event.start.getTime() + 30 * 60 * 1000));
                this.step = 1;
                this.dialogVisible = true;
            },

            // 获取可预约事件
            getEvent() {
                this.query.beginTime = this.getFirstDayOfMonth(this.calendarApi.getDate());
                this.query.endTime = this.getLastDayOfMonth(this.calendarApi.getDate());
                this.$axios.post("/mock/interview/time/query", this.query).then(data => {
                    let events = [];
                    data.data.forEach(item => {
                        if (item.reservationList.length === 0) { // 没有被任何人预约，即可预约事件
                            events.push({
                                interviewerId: item.interviewerId,
                                start: parseInt(item.beginTime),
                                end: parseInt(item.endTime),
                                borderColor: '#D3F261', // 块边框颜色
                                backgroundColor: '#D3F261', // 块背景色
                            })
                        } else {

                            let eventList = [ // 保留被人预约剩余的可预约事件，默认为我的总预约时间段
                                {
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
                                // events.push({
                                //     id: reservation.id, // 被预约的id，可预约不需要
                                //     interviewerId: reservation.userId, // 预约人
                                //     start: parseInt(reservation.beginTime),
                                //     end: parseInt(reservation.endTime),
                                //     borderColor: '#FFE58F',
                                //     backgroundColor: '#FFE58F'
                                // })
                            })

                            events = events.concat(eventList);
                        }
                    })
                    this.calendarOptions.events = events;
                })
            },

            getDirection() {
                this.$axios.get(`/mock/interviewer/direction/${this.query.interviewerId}`).then(data => {
                    this.directionList = data.data;
                })
            },

            findDirection() {
                let item = this.directionList.find(item => item.id === this.directionId);
                return item && item.direction;
            },

            // 以下工具函数
            getHourMinutes(time) {
                return time ? `${time.getHours() < 10 ? '0' : ''}${time.getHours()}:${time.getMinutes() < 10 ? '0' : ''}${time.getMinutes()}` : '00:00';
            },

            // Date + "12:00"  => (Date 12:00).getTime();
            getDateTime(date, time) {
                if (date) {
                    let t = time.split(":");
                    date.setHours(t[0], t[1])
                    return date.getTime();
                } else {
                    return Date.now();
                }
            },

            getFirstDayOfMonth(date) {
                const temp = new Date(date.getTime());
                temp.setDate(1)
                return temp.getTime();
            },

            getLastDayOfMonth(date) {
                const temp = new Date(date.getTime());
                temp.setMonth(temp.getMonth() + 1);
                temp.setDate(1)
                return temp.getTime() - 1;
            }
        }
    }
</script>

<style scoped lang="scss">

    .app-container {
        width: 1100px;
        margin: 0 auto;

        .title {
            font-size: 36px;
            color: #333333;
            line-height: 50px;
            margin-bottom: 30px;
        }

        .calendar-container {
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

                .tag {
                    width: 18px;
                    height: 18px;
                    border-radius: 9px;
                    background: #D3F261;
                }

                .tag-text {
                    font-size: 18px;
                    color: #333333;
                    line-height: 25px;
                    margin-left: 4px;
                    margin-right: 21px;
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

            .pay-type {
                display: flex;
                align-items: center;
                justify-content: space-around;
                height: 200px;
            }

            .image-success {
                width: 200px;
                height: 200px;
                margin: 0 auto;
                display: block;
            }
        }
    }
</style>
