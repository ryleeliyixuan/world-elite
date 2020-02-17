const state = {
    keyword: undefined,
    hideSearch: true
}

const mutations = {
    SET_KEYWORD: (state, keyword) => {
        state.keyword = keyword
    },
    SET_HIDE_SEARCH: (state, hideSearch) => {
        state.hideSearch = hideSearch;
    }
}

export default {
    namespaced: true,
    state,
    mutations
}