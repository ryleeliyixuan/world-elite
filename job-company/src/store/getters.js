const getters = {
    token: state => state.user.token,
    name: state => state.user.name,
    avatar: state => state.user.avatar,
    messageCount: state => state.setting.messageCount
}
export default getters