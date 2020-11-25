<template>
	<div class="aa">
		<div class="align-center">
			<el-form class="a">
				<ul v-html="agreement1" style="overflow:auto">
					<li v-for="i in agreement1" class="infinite-list-item">{{ i }}</li>
				</ul>
			</el-form>
			<el-form class="b">

				<ul v-html="agreement2" style="overflow:auto">
					<li v-for="j in agreement2" class="infinite-list-item">{{ j }}</li>
				</ul>
			</el-form>
		</div>
		<div class="ab">
			<el-checkbox v-model="checked">我已阅读并同意以上协议。</el-checkbox>
		</div>
		<div v-if="checked===true">
			<el-button class="Btn" @click="onSubmit">确定</el-button>
		</div>
	</div>
</template>
<script>
	import {agreement} from "@/utils/agreement";
	export default {
		data() {
			return {
				checked: false,
				agreement1: agreement.title,
				agreement2: agreement.content
			}
		},
		created() {
			this.$emit("complete");
		},
		methods: {
			onSubmit(evt) {
				evt.preventDefault();
				let query = { ...this.$route.query
				};
				delete query.redirect;
				this.$router.push({
					path: this.redirect || "/RegisterInterviewerPage",
					query
				});
			},
		}
	}
</script>
<style scoped lang="scss">
	.aa {
		margin-bottom: 200px;

	}

	.align-center {
		margin-left: 260px;
		width: 885px;
		/*height: 941px;*/
		background: #F5F5F5;
		border-radius: 17px;
	}

	.a {
		margin-top: 10px;
		/*margin-left: 100px;*/
		width: 885px;
		height: 41px;
		background: #3D6FF4;
		border-radius: 17px 17px 0px 0px;

		.a1 {
			padding-left: 15px;
			padding-top: 7px;
			width: 270px;
			height: 20px;
			font-size: 18px;
			font-family: PingFangSC-Medium, PingFang SC;
			font-weight: 300;
			color: #FFFFFF;
			line-height: 25px;
		}
	}

	.b {}

	.b1 {
		margin-left: 320px;
		width: 216px;
		height: 33px;
		font-size: 24px;
		font-family: PingFangSC-Medium, PingFang SC;
		font-weight: 500;
		color: #333333;
		line-height: 33px;
	}

	.ab {
		margin-left: 260px;

	}

	.Btn {
		margin-left: 500px;
		width: 332px;
		height: 47px;
		background: #EEEEEE;
		border-radius: 24px;
		font-size: 24px;
		font-family: PingFangSC-Medium, PingFang SC;
		font-weight: 500;
		color: #999999;
		line-height: 25px;
	}
</style>
