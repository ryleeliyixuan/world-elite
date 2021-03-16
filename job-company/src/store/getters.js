const getters = {
    token: state => state.user.token,
    status: state => state.user.status,
    name: state => state.user.name,
    avatar: state => state.user.avatar,
    userId: state => state.user.userId,
    messageCount: state => state.setting.messageCount,
    jobDraft: state => state.setting.jobDraft
}
export default getters