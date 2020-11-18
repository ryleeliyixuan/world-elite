<template>
    <div class="app-container">
        <calendar class="calendar"></calendar>
<!--        <el-date-picker size='small' v-model="selectDate" type="month"-->
<!--                        placeholder="选择月" value-format="yyyy-MM">-->
<!--        </el-date-picker>-->
<!--        <el-button size='small' @click='changeDate'>确定</el-button>-->
<!--        <fullcalendar-->
<!--                class="calendar"-->
<!--                :config="config"-->
<!--                :events="events"-->
<!--                ref="calendar"-->
<!--                @event-selected='eventClick'-->
<!--                @day-click="dayClick">-->
<!--        </fullcalendar>-->
<!--        <add-schedule v-if="isAdd" :isAdd='isAdd' :editItem='editItem' @add='addItem' @close='isAdd = false'></add-schedule>-->
    </div>
</template>
<script>
    import Calendar from '@/components/calendar/Calendar'
// <!--    import FullCalendar from 'vue-fullcalendar'-->
// <!--    // import 'fullcalendar/dist/fullcalendar.css'-->
// <!--    import addSchedule from '@/components/calendar/add.vue'-->

    export default {
        data() {
            return {
                isAdd: false,
                selectDate: '',//日期选择器选中的月份
                config: {
                    header: {//表头信息
                        left: 'title',
                        center: 'hide, custom',
                        right: 'prev,today,next'
                    },
                    firstDay: '0',//以周日为每周的第一天
                    // weekends: true,//是否在日历中显示周末
                    locale: 'zh-cn',//语言
                    defaultView: 'month',//默认按月显示
                    height: 'auto',//高度
                    fixedWeekCount: false,//是否固定显示六周
                    // weekMode:"liquid",//周数不定，每周的高度可变，整个日历高度不变
                    allDaySlot: false,
                    // allDay:true,
                    displayEventEnd: true,
                },
                events: [{
                    id: 1,
                    title: '出差',
                    start: new Date(1604797200000),
                    end: new Date(1604800800000),
                }, {
                    id: 2,
                    title: '春游',
                    start: new Date(1604797200000),
                    end: new Date(1604800800000),
                }, {
                    id: 3,
                    title: '春游',
                    start: new Date(1604797200000),
                    end: new Date(1604800800000),
                }, {
                    id: 4,
                    title: '春游',
                    start: new Date(1604797200000),
                    end: new Date(1604800800000),
                }, {
                    id: 5,
                    title: '春游',
                    start: new Date(1604797200000),
                    end: new Date(1604800800000),
                }],
                newItem: {},
                editItem: {}
            }
        },
        components: {Calendar},
        methods: {
            changeDate() {
                // this.$refs.calendar.fireMethod('gotoDate', this.selectDate)
                this.$refs.calendar.fireMethod('prev');
            },
            eventClick(event) { //events的点击事件
                this.editItem = event
                this.isAdd = true
                console.log(event);
            },
            dayClick(date, jsEvent, view) { //日期的点击事件
                this.editItem = {}
                this.isAdd = true
            },
            addItem(detail) {
                console.log(detail);
                this.newItem = JSON.parse(detail)
                console.log(this.newItem.period);
                if (this.editItem.id) {//如果是编辑，就删掉该条
                    this.events.forEach((el, ind) => {
                        if (el.id === this.editItem.id) {
                            this.events.splice(ind, 1)
                        }
                    })
                }
                this.events.push({
                    id: this.editItem.id ? this.editItem.id : Date.now(),
                    title: this.newItem.title,
                    start: this.newItem.period[0],
                    end: this.newItem.period[1],
                })
            },
        },
    }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="scss">
    .app-container {
        margin: 0 auto;
        max-width: 1200px;

        .calendar {
            width: 985px;
        }
    }
</style>
