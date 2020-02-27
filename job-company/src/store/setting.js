const state = {
    messageCount: 0
}

const mutations = {
    MESSAGE_COUNT: (state, messageCount) => {
        state.messageCount = messageCount
    }
}

export default {
    namespaced: true,
    state,
    mutations
}