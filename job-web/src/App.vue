<template>
    <div id="app" ref="app">
        <div style="background: white;">
            <MainNavBar class="nav-bar"/>
        </div>
        <transition name="fade-transform" mode="out-in">
            <keep-alive include='JobListPage'>
                <router-view class="main-container page"/>
            </keep-alive>
        </transition>
        <transition name="fade">
            <div style="background: white;">
                <FooterView/>
            </div>
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
            };
        },
        components: {MainNavBar, FooterView},
        name: "App",
        updated() {
            if (!this.noScroll.includes(this.$route.name)) {
                setTimeout(() => {
                    this.$refs.app.scrollTop = 0;
                }, 400)
            }
        },
    };
</script>

<style lang="scss" scoped>
    #app {
        font-family: "Avenir", Helvetica, Arial, sans-serif;
        -webkit-font-smoothing: antialiased;
        -moz-osx-font-smoothing: grayscale;
        background: #f6f9fc;
        min-height: 100vh;
        width: 100%;
        overflow: auto;
        display: flex;
        flex-direction: column;

        .nav-bar {
            width: 100%;
            max-width: 1333px;
            margin: 0 auto;
        }

        .page {
            width: 100%;
            max-width: 1200px;
            margin: 17px auto;
        }

        .main-container {
            flex: 1;
        }
    }
</style>
