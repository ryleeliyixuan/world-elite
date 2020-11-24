export const getHandledValue = num => {
    return num < 10 ? '0' + num : num
};
/**
 * 获取当前时间年月日时分秒，如：2000-10-20 20:20:20
 * @param {Number} timeStamp 传入的时间戳
 **/
export const timestampToDateTime = (timeStamp) => {
    if (timeStamp) {
        const d = new Date(timeStamp);
        const year = d.getFullYear();
        const month = getHandledValue(d.getMonth() + 1);
        const date = getHandledValue(d.getDate());
        const hours = getHandledValue(d.getHours());
        const minutes = getHandledValue(d.getMinutes());
        const second = getHandledValue(d.getSeconds());
        return year + '-' + month + '-' + date + ' ' + hours + ':' + minutes + ':' + second;
    } else {
        return '';
    }
};

/**
 * 获取当前时间年月日，如：2000-10-20
 * @param {Number} timeStamp 传入的时间戳
 **/
export const timestampToDate = (timeStamp) => {
    if (timeStamp) {
        const d = new Date(timeStamp);
        const year = d.getFullYear();
        const month = getHandledValue(d.getMonth() + 1);
        const date = getHandledValue(d.getDate());
        return year + '/' + month + '/' + date;
    } else {
        return '';
    }
};

/**
 * 获取当前时间年月日，如：10/20 00:00
 * @param {Number} timeStamp 传入的时间戳
 **/
export const timestampToMonthDateHoursMinutes = (timeStamp) => {
    if (timeStamp) {
        const d = new Date(timeStamp);
        const month = getHandledValue(d.getMonth() + 1);
        const date = getHandledValue(d.getDate());
        const hours = getHandledValue(d.getHours());
        const minutes = getHandledValue(d.getMinutes());
        return month + '/' + date + ' ' + hours + ":" + minutes;
    } else {
        return '';
    }
};

// 获取00:00格式的时间
export const timestampToMinute = (timestamp) => {
    if (timestamp) {
        const d = new Date(timestamp);
        const hours = getHandledValue(d.getHours());
        const minutes = getHandledValue(d.getMinutes());
        return hours + ":" + minutes;
    } else {
        return '';
    }
}

// 保留一位小数
export const toFixed1 = (num) => {
    if (num) {
        return parseFloat(num.toFixed(1))
    } else {
        return 0.0;
    }
}


// 保留两位小数
export const toFixed2 = (num) => {
    if (num) {
        return parseFloat(parseFloat(num).toFixed(2))
    } else {
        return "0.00";
    }
}
