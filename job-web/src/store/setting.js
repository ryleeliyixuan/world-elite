const state = {
    keyword: undefined,
    hideSearch: true,
    messageCount: 0,
    avatar: "",
}

const mutations = {
    SET_KEYWORD: (state, keyword) => {
        state.keyword = keyword
    },
    SET_HIDE_SEARCH: (state, hideSearch) => {
        state.hideSearch = hideSearch;
    },
    MESSAGE_COUNT: (state, messageCount) => {
        state.messageCount = messageCount
    },
}

export default {
    namespaced: true,
    state,
    mutations
}