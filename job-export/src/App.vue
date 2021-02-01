<template>
    <div id="app" ref="app">
        <router-view class="main-container page"/>
    </div>
</template>

<script>
    export default {
        name: "App"
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
