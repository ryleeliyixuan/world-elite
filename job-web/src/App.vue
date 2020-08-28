<template>
    <div id="app" v-show="show">
        <MainNavBar/>
        <router-view @complete="onComplete"/>
        <FooterView/>
        <el-backtop></el-backtop>
    </div>
</template>

<script>
    import MainNavBar from "@/components/MainNavBar";
    import FooterView from '@/components/FooterView'

    export default {
        data() {
            return {
                show: false
            }
        },
        components: {MainNavBar, FooterView},
        name: "App",
        watch: {
            $route(route) {
                this.show = false;
            }
        },
        methods: {
            onComplete() {
                this.show = true;
            }
        }
    };
</script>

<style>
    #app {
        font-family: "Avenir", Helvetica, Arial, sans-serif;
        -webkit-font-smoothing: antialiased;
        -moz-osx-font-smoothing: grayscale;
        color: #2c3e50;
    }
</style>
