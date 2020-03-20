const checkEmail = (rule, value, callback) => {
    
    const mailReg = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/
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

const checkIdCard = (rule, value, callback) => {
    if(!value){
        return callback(new Error('身份证不能为空'))
    }
    var idRegx = /(^[1-9]\d{5}(18|19|([23]\d))\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$)|(^[1-9]\d{5}\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{2}$)/;
    setTimeout(() => {
        if(idRegx.test(value)){
            callback();
        } else{
            callback(new Error('证件号码格式有误'))
        }
    }, 100)
}

export default {
    checkEmail: checkEmail,
    checkPassword: checkPassword,
    checkIdCard: checkIdCard
}