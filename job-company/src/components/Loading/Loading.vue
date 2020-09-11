<template>
    <div>
        <div class="loading" v-if="loading">
            <i class="el-icon-loading" style="color: #3a8ee6;"></i>
        </div>
        <transition name="fade">
            <slot v-if="!loading"></slot>
        </transition>
    </div>
</template>

<script>
    export default {
        name: "Loading",
        props: {
            loading: {
                type: Boolean,
                default: true
            }
        },
        data() {
            return {
            };
        },

    }
</script>

<style scoped>
    .loading {
        width: 100vw;
        height: 100vh;
        z-index: 1000;
        line-height: calc(100vh - 130px);
        text-align: center;
        font-size: 48px;
    }


</style>
