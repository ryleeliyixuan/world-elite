
/**
 * @param {Number} num 数值
 * @returns {String} 处理后的字符串
 * @description 如果传入的数值小于10，即位数只有1位，则在前面补充0
 */
export const getHandledValue = num => {
    return num < 10 ? '0' + num : num
};

/**
 * 获取当前时间年月日，如：2000-10-20
 */
export const getNowDate = () => {
    const d = new Date();
    const year = d.getFullYear();
    const month = getHandledValue(d.getMonth() + 1);
    const date = getHandledValue(d.getDate());
    return year + '-' + month + '-' + date;
};


export const getDate = (time) => {
    const d = new Date(time);
    const year = d.getFullYear();
    const month = getHandledValue(d.getMonth() + 1);
    const date = getHandledValue(d.getDate());
    return year + '-' + month + '-' + date;
};
