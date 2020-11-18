<template>
    <div>
        <FullCalendar ref="fullCalendar" :options="calendarOptions"/>
        <el-dialog
                title="预约面试时间"
                :visible.sync="dialogVisible"
                width="500px">
            <span>请选择您想预约得时间段</span>
            <el-time-select
                    placeholder="起始时间"
                    v-model="startTime"
                    :picker-options="{
                      start: start,
                      step: '00:30',
                      end: end
                 }">
            </el-time-select>
            <el-time-select
                    placeholder="结束时间"
                    v-model="endTime"
                    :picker-options="{
                      start: startTime || start,
                      step: '00:30',
                      end: end
                    }">
            </el-time-select>
            <el-button type="primary" @click="getDate(date, startTime)">buttonCont</el-button>
            <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="dialogVisible = false">确 定</el-button>
      </span>
        </el-dialog>
    </div>
</template>

<script>
    import FullCalendar from '@fullcalendar/vue'
    import dayGridPlugin from '@fullcalendar/daygrid'
    import interactionPlugin from '@fullcalendar/interaction'
    import {formatDate} from '@fullcalendar/vue';

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

                calendarOptions: {
                    plugins: [dayGridPlugin, interactionPlugin],

                    // 日历配置
                    initialView: 'dayGridMonth',
                    initialDate: "2020-11-10",
                    headerToolbar: {
                        start: 'title', // will normally be on the left. if RTL, will be on the right
                        center: '',
                        end: 'prev,today,next dayGridMonth,dayGridWeek,dayGridDay' // will normally be on the right. if RTL, will be on the left
                    },
                    titleFormat: {year: 'numeric', month: 'long'}, // 表头日期格式

                    // 日期点击
                    dateClick: this.onDateClick,

                    // 事件定义
                    events: [],

                    // 事件点击
                    eventClick: this.onEventClick,

                    // 事件显示
                    eventColor: "blue", // 全部事件圆点颜色
                    displayEventEnd: true, // 显示事件结束时间
                    eventTimeFormat: { // am / pm
                        hour: 'numeric',
                        minute: '2-digit',
                        meridiem: 'short'
                    },

                    // 事件渲染
                    eventClassNames: (info) => {
                    },
                    eventContent: (event) => {
                        // event.timeText = "aaa";
                    },
                }
            }
        },
        mounted() {
            // this.calendarOptions.events.push({
            //     start: new Date(2020, 10, 17, 10).getTime(),
            //     end: new Date(2020, 10, 17, 16).getTime(),
            //     // color:"red" // 该事件圆点颜色
            // })
        },
        methods: {
            a() {
                let calendarApi = this.$refs.fullCalendar.getApi()
                calendarApi.next()
            },

            onDateClick(info) {
                console.log('Clicked on: ' + info.dateStr);
                console.log('Coordinates: ' + info.jsEvent.pageX + ',' + info.jsEvent.pageY);
                console.log('Current view: ' + info.view.type);
                // change the day's background color just for fun
                info.dayEl.style.backgroundColor = 'red'; // 改变背景颜色
                this.calendarOptions.events.push({
                    start: new Date(2020, 10, 17, 10).getTime(),
                    end: new Date(2020, 10, 17, 16).getTime(),
                    // color:"red" // 该事件圆点颜色
                })
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
                console.log(date);
            }
        }
    }
</script>

<style scoped>
    .aaa {
        background: red;
    }
</style>
