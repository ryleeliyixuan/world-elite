<template>
	<div class="app-container">
		<div class="main-container">

		<img src="../assets/examine1.png" style="padding-left: 450px">
		<p class="a">认证资料提交成功</p>
		<p class="a1">我们将尽快完成资料审核</p>
		<div style="padding-left: 434px">
			<el-button class="Btn" @click="onSubmit">返回首页</el-button>
		</div>
		</div>
	</div>
</template>
<script>
	export default {
		data() {
			return {
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
					path: this.redirect || "/",
					query
				});
			},
		}

	}
</script>
<style scoped lang="scss">
	.app-container {
		max-width: 1140px;
		margin: 0 auto;
		min-height: calc(100vh - 477px);
		padding-bottom: 100px;
		


		.main-container {
			padding-top: 100px;
			overflow-y: auto;
			width: 1000px;

			.a {
				padding-left: 460px;
				min-width: 288px;
				height: 50px;
				font-size: 21px;
				font-family: PingFangSC-Medium, PingFang SC;
				font-weight: 500;
				color: #333333;
				line-height: 50px;
			}

			.a1 {
				padding-left: 430px;
				min-width: 288px;
				height: 33px;
				font-size: 21px;
				font-family: PingFangSC-Regular, PingFang SC;
				font-weight: 400;
				color: #999999;
				line-height: 33px;
			}

			.Btn {
				width: 227px;
				height: 41px;
				background: #EDF3FF;
				border-radius: 21px;
				font-size: 18px;
				font-family: PingFangSC-Medium, PingFang SC;
				font-weight: 500;
				color: #3D6FF4;
				line-height: 15px;
				margin-bottom: 200px;
			}
		}
	}
	@media screen and (max-width: 850px) {
		.app-container {
			flex-direction: column;
			align-items: center;

		}
	}
</style>
