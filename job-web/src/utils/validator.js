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
            callback(new Error('密码需同时包含字母和数字，不支持特殊符号'))
        }
    }, 100)
}

export default {
    checkEmail: checkEmail,
    checkPassword: checkPassword
}