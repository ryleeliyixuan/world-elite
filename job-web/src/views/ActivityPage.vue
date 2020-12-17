<template>
    <div class="app-container container">
        <div class="activity-container" v-if="activity">
            <el-image :src="activity.thumbnail"
                      class="img-cover"
                      :alt="activity.title">
            </el-image>
            <b-media-body>
                <h5 class="mt-0 mb-2">{{activity.title}}</h5>
                <div class="text-label" v-if="activity.organizer && activity.organizer != ''">
                    <b>主办方：</b>
                    {{activity.organizer}}
                </div>
                <div class="text-label">
                    <b>活动城市：</b>
                    {{activity.city.name}}
                </div>
                <div class="text-label">
                    <b>活动地址：</b>
                    {{activity.address}}
                </div>
                <div class="text-label">
                    <b>活动时间：</b>
                    {{activity.startTime}} 到 {{activity.finishTime}}
                </div>
                <div class="mt-2">
                    <el-button type="primary" @click="onJoinClick">{{activity.joinFlag?'已报名':'报名'}}</el-button>
                </div>
            </b-media-body>
        </div>
        <div class="introdution mt-4 ql-editor" v-html="activity.description" v-if="activity"></div>
    </div>
</template>

<script>
    import {getActivityInfo} from '@/api/activity_api'
    import {doFavorite} from '@/api/favorite_api'

    export default {
        name: "ActivityPage",
        data() {
            return {
                activity: undefined,
                favoriteForm: {
                    objectId: undefined,
                    type: 3,
                    favorite: true
                }
            };
        },
        created() {
            this.initData();
        },
        methods: {
            initData() {
                getActivityInfo(this.$route.params.id).then(response => {
                    this.activity = response.data;
                    this.favoriteForm.objectId = this.activity.id;
                    this.$emit("complete");
                })
            },
            onJoinClick() {
                this.favoriteForm.favorite = this.activity.joinFlag ? (!this.activity.joinFlag) : true;
                doFavorite(this.favoriteForm).then(() => {
                    this.initData();
                })
            }
        }
    };
</script>

<style scoped lang="scss">

    .app-container {
        min-height: calc(100vh - 477px);

        .activity-container {
            display: flex;

            .img-cover {
                width: 150px;
                height: 150px;
                margin: 0 15px 0 0;
            }
        }
    }


    @media screen and (max-width: 850px) {
        .app-container {
            .activity-container {
                flex-direction: column;
                align-items: center;

                .img-cover {
                    margin: 0 0 15px 0;
                }
            }
        }
    }
</style>
