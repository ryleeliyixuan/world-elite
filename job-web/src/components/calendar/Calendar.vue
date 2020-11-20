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
                   class="dialog">
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
                      end: end
                 }">
                </el-time-select>
                <span class="dialog-text" style="padding: 0 11px;">至</span>
                <el-time-select
                        size="small"
                        class="select"
                        placeholder="结束时间"
                        v-model="endTime"
                        :picker-options="{
                      start: startTime || start,
                      step: '00:30',
                      end: end
                    }">
                </el-time-select>
                <div class="dialog-text" style="margin-top: 20px;">请选择您想预约的类型</div>
                <el-select v-model="type" placeholder="预约类型" class="select">
                    <el-option size="small"
                               v-for="item in typeList"
                               :key="item.value"
                               :label="item.label"
                               :value="item.value">
                    </el-option>
                </el-select>
                <div class="footer">
                    <el-button @click="dialogVisible = false" round style="width: 100px;" size="small">取消</el-button>
                    <el-button type="primary" @click="toStep2" round style="width: 100px; margin-left: 20px;" size="small">下一步</el-button>
                </div>
            </div>
            <div v-else-if="step===2">
                <div style="margin-bottom: 18px;"><span class="dialog-text">您预约的时间为：</span><span class="dialog-text2">{{startTime}}至{{endTime}}</span></div>
                <div style="margin-bottom: 18px;"><span class="dialog-text">您预约的类型为：</span><span class="dialog-text2">{{type}}</span></div>
                <div style="width:100%; height:1px; background:#C9DAFB;margin-bottom: 18px;"></div>
                <div style="font-size: 24px; color: #333333; line-height: 33px;margin-bottom: 18px;">共计：<span style="color:#3D6FF4;">￥{{'400.00'}}</span></div>
                <el-image class="qrcode" src="https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2973432407,1075552176&fm=26&gp=0.jpg"></el-image>
                <div class="footer">
                    <el-button @click="step=1" round style="width: 100px;" size="small">上一步</el-button>
                    <el-button type="primary" @click="onSave" round style="width: 100px; margin-left: 20px;" size="small">确定</el-button>
                </div>
            </div>
            <div v-else-if="step===3">
                <el-image class="image-success" :src="require('@/assets/pay-sucsses.png')"></el-image>
                <div style="font-size: 28px; color: #333333; line-height: 40px;margin: 13px 0; text-align: center;">恭喜您，预约成功！</div>
                <el-button type="primary" @click="onBack" round style="width: 200px; margin: 0 auto; display: block;" >返回</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
    import FullCalendar from '@fullcalendar/vue'
    import dayGridPlugin from '@fullcalendar/daygrid'
    import interactionPlugin from '@fullcalendar/interaction'

    export default {
        name: "Calendar",
        components: {
            FullCalendar // make the <FullCalendar> tag available
        },
        data() {
            return {
                dialogVisible: false, // 对话框是否可见
                date: 0, // 预约日期
                startTime: 0, // 预约起始时间
                endTime: 0, // 预约结束时间
                start: '00:00', // 预约最小时间
                end: '24:00', // 预约最大时间
                type: undefined, // 要预约的类型
                typeList: [{label: "HR面试（通用）", value: "HR面试（通用）"}], // 可选的预约类型
                step: 1, // 当前预约步骤

                calendarOptions: {
                    // 语言
                    locale: "zh-cn",

                    plugins: [dayGridPlugin, interactionPlugin],

                    // 日历配置
                    initialView: 'dayGridMonth',
                    initialDate: "2020-11-10",
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
        mounted() {
            this.calendarApi = this.$refs.fullCalendar.getApi()
            this.calendarApi.addEvent({
                start: new Date(2020, 10, 17, 10).getTime(),
                end: new Date(2020, 10, 17, 16).getTime(),
                eventBorderColor: '#D3F261', // 块边框颜色
                eventBackgroundColor: '#D3F261', // 块背景色
            })
        },
        methods: {
            onPrev() {
                this.calendarApi.prev()
            },

            onNext() {
                this.calendarApi.next()
            },

            // 检查参数，跳转下一步
            toStep2() {
                if (!this.startTime) {
                    this.$message.warning("请选择预约开始时间")
                } else if (!this.endTime) {
                    this.$message.warning("请选择预约结束时间")
                } else if (!this.type) {
                    this.$message.warning("请选择预约类型")
                } else {
                    this.step = 2;
                }
            },

            // 请求服务器获取二维码
            onSave() {
                let startTime = this.getDate(this.date, this.startTime);
                let endTime = this.getDate(this.date, this.endTime);
                let type = this.type;
                console.log(startTime, endTime, type);
                this.step = 3;
            },

            // 返回到我的模拟面试
            onBack() {
                this.dialogVisible = false;
                this.step = 1;
            },

            onDateClick(info) {
                console.log('Clicked on: ' + info.dateStr);
                console.log('Coordinates: ' + info.jsEvent.pageX + ',' + info.jsEvent.pageY);
                console.log('Current view: ' + info.view.type);
                // change the day's background color just for fun
                info.dayEl.style.backgroundColor = 'red'; // 改变背景颜色
            },

            onEventClick(info) {
                console.log("info : ", info);
                console.log('Event: ' + info.event.title);
                console.log('Coordinates: ' + info.jsEvent.pageX + ',' + info.jsEvent.pageY);
                console.log('View: ' + info.view.type);

                // change the border color just for fun
                // info.el.style.borderColor = 'red';
                console.log(info.event.start);
                this.date = new Date(info.event.start);
                this.start = this.getHourMinutes(info.event.start);
                this.end = this.getHourMinutes(info.event.end);
                this.dialogVisible = true;
            },

            getHourMinutes(time) {
                return time ? `${time.getHours()}:${time.getMinutes() < 10 ? '0' : ''}${time.getMinutes()}` : '00:00';
            },

            getDate(date, time) {
                let t = time.split(":");
                date.setHours(t[0], t[1])
                return date.getTime();
            }
        }
    }
</script>

<style scoped lang="scss">

    .app-container {
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
    }
</style>
