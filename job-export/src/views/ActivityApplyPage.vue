<template>
    <view-apply :applyId="applyId"></view-apply>
</template>

<script>
    import ViewApply from "@/components/activity/ViewApply"

    export default {
        name: "ActivityApplePage",
        components: {
            ViewApply
        },
        data() {
            return {
                applyId: 0
            }
        },
        created() {
            this.initData();
        },
        methods: {
            initData() {
                this.applyId = parseInt(this.$route.params.id);
            }
        }
    }
</script>
