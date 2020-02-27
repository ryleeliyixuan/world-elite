const getters = {
    token: state => state.user.token,
    name: state => state.user.name,
    avatar: state => state.user.avatar,
    keyword: state => state.setting.keyword,
    hideSearch: state => state.setting.hideSearch,
    messageCount: state => state.setting.messageCount
}
export default getters