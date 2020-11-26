const checkEmail = (rule, value, callback) => {
    
    const mailReg = /^\w+((.\w+)|(-\w+))@[A-Za-z0-9]+((.|-)[A-Za-z0-9]+).[A-Za-z0-9]+$/;
    if (!value) {
        return callback(new Error('邮箱不能为空'))
    }
    setTimeout(() => {
        if (mailReg.test(value)) {
            callback()
        } else {
            callback(new Error('请输入正确的邮箱格式'))
        }
    }, 100)
}

const checkPassword = (rule, value, callback) => {
    if(!value){
        return callback(new Error('密码不能为空'))
    }
    var pwdRegx = /^(?!([a-zA-Z]+|\d+)$)[a-zA-Z\d]{8,20}$/;
    setTimeout(() => {
        if(value.length < 8){
            callback(new Error('密码不能少于8位'))
        } else if(pwdRegx.test(value)){
            callback();
        } else{
            callback(new Error('密码需要同时包含数字和字母'))
        }
    }, 100)
}

export function isIdNumber(idNumber) {
    const reg = /^\d{6}(18|19|20)?\d{2}(0[1-9]|1[0-2])(([0-2][1-9])|10|20|30|31)\d{3}(\d|X|x)$/;
    //18位从1900到2099年中间的范围验证
    return reg.test(idNumber)
}
export function isPrice(price) {
    var reg = /^-?\d{1,4}(?:\.\d{1,2})?$/;
    return reg.test(price)
}

export default {
    checkEmail: checkEmail,
    checkPassword: checkPassword
}