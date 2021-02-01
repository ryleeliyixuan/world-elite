/**
 * 根据属性值获取在数组中的index
 */
Array.prototype.getIndexByProprety = function (val, proprety) {
    var arr = this;
    var index = -1;
    arr.forEach((v, i, a) => {
        if (v[proprety] == val) {
            index = i;
        }
    });
    return index;
}

/**
 * 根据属性值获取数组中的某项
 */
Array.prototype.getObjByProprety = function (val, proprety) {
    var arr = this;
    var obj = {};
    arr.forEach((v, i, a) => {
        if (v[proprety] === val) {
            obj = v;
        }
    });
    return obj;
}
