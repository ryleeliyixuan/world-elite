import { CalendarApi, Duration, PluginDef } from '@fullcalendar/common';
import moment from 'moment';

declare function toMoment(date: Date, calendar: CalendarApi): moment.Moment;
declare function toMomentDuration(fcDuration: Duration): moment.Duration;
declare const _default: PluginDef;

export default _default;
export { toMoment, toMomentDuration };
