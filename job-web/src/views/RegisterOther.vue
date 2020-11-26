<template>
	<div class="align-center">
		<img src="../assets/examineSuccess.png">
		<p class="a">认证资料提交成功</p>
		<p class="a1">我们将尽快完成资料审核</p>
		<div>
			<el-button class="Btn" @click="onSubmit">返回首页</el-button>
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
<style scoped>
	.align-center {
		margin-top: 50px;
		margin-left: 500px;
	}
	.a {
		margin-left: 50px;
		width: 288px;
		height: 50px;
		font-size: 36px;
		font-family: PingFangSC-Medium, PingFang SC;
		font-weight: 500;
		color: #333333;
		line-height: 50px;
	}
	.a1 {
		margin-left: 60px;
		width: 264px;
		height: 33px;
		font-size: 24px;
		font-family: PingFangSC-Regular, PingFang SC;
		font-weight: 400;
		color: #999999;
		line-height: 33px;
	}
	.Btn {
		margin-left: 80px;
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
</style>
