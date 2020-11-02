const UserInfoKey = 'userInfo';

let info = null;


export const storage = {
    setUserInfo: (userInfo) => {
        info = userInfo;
        return localStorage.setItem(UserInfoKey, JSON.stringify(userInfo));
    },

    getUserInfo: () => {
        return info || JSON.parse(localStorage.getItem(UserInfoKey)) || {};
    },

    removeUserInfo: () => {
        info = null;
        return localStorage.removeItem(UserInfoKey);
    },

    getToken() {
        return this.getUserInfo().token;
    },

    getUsername() {
        return this.getUserInfo().name;
    },

    getAvatar() {
        return this.getUserInfo().avatar;
    },

    getUserId() {
        return this.getUserInfo().id;
    },

    setLoginInfo: (userInfo) => {
        return localStorage.setItem("loginInfo", JSON.stringify(userInfo));
    },

    getLoginInfo: () => {
        return JSON.parse(localStorage.getItem("loginInfo")) || {};
    },

    removeLoginInfo: () => {
        return localStorage.removeItem("loginInfo");
    },

    setData: (key, value) => {
        console.log("setData -  key:" + key + "  value:" + JSON.stringify(value));
        sessionStorage.setItem(key, JSON.stringify(value));
    },

    getData: (key) => {
        console.log("getData -  key:" + key);
        return JSON.parse(sessionStorage.getItem(key)) || {};
    },

    removeData: (key) => {
        console.log("remove -  key:" + key);
        sessionStorage.removeItem(key);
    }
}

export default {
    install: function (Vue) {
        Vue.prototype.$storage = storage;
    }
};
