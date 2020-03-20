const state = {
    messageCount: 0,
    jobDraft: undefined,
}

const mutations = {
    MESSAGE_COUNT: (state, messageCount) => {
        state.messageCount = messageCount
    },
    JOB_DRAFT: (state, jobDraft) => {
        state.jobDraft = jobDraft
    },
}

export default {
    namespaced: true,
    state,
    mutations
}