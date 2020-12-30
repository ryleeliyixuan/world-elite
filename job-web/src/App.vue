<template>
    <div id="app" ref="app">
        <MainNavBar class="page"/>
        <transition name="fade-transform" mode="out-in">
            <router-view @complete="onComplete" class="main-container page"/>
        </transition>
        <transition name="fade">
            <FooterView @complete="onComplete" v-if="show"/>
        </transition>
        <el-backtop></el-backtop>
    </div>
</template>

<script>
    import MainNavBar from "@/components/MainNavBar";
    import FooterView from "@/components/FooterView";

    export default {
        data() {
            return {
                noScroll: ["wiki-intro", "wiki-job", "wiki-activity", "wiki-community-post", "wiki-community-score", "wiki-community-post-detail"],
                show: true,
            };
        },
        components: {MainNavBar, FooterView},
        name: "App",
        watch: {
            $route(route) {
                this.show = true;
            },
        },
        updated() {
            if (!this.noScroll.includes(this.$route.name)) {
                setTimeout(() => {
                    this.$refs.app.scrollTop = 0;
                }, 400)
            }
        },
        methods: {
            onComplete() {
                this.show = true;
            },
        }
    };
</script>

<style lang="scss" scoped>
    #app {
        font-family: "Avenir", Helvetica, Arial, sans-serif;
        -webkit-font-smoothing: antialiased;
        -moz-osx-font-smoothing: grayscale;
        color: #2c3e50;
        height: 100vh;
        overflow: auto;
        display: flex;
        flex-direction: column;

        .page {
            width: 100%;
            max-width: 1200px;
            margin: 0 auto;
        }

        .main-container {
            flex: 1;
        }
    }
</style>
