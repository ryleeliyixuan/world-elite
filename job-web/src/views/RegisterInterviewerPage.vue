<template>

	<div class="align-center">
		<p style="font-size: 25px">面试官注册</p>
		<el-steps :active="active">
			<el-step title="个人信息"></el-step>
			<el-step title="面试信息"></el-step>
			<el-step title="信息认证"></el-step>
		</el-steps>

		<div v-if="active==1">
			<el-form ref="formOne" :model="formOne" :rules="rules">
				<div style="margin-top: 20px">
					<el-form-item label="昵称" label-width="100px" prop="nickName">
							<el-input style="max-width: 200px" v-model="formOne.nickName" placeholder="请输入您的昵称" maxlength="20" show-word-limit></el-input>
					</el-form-item>
					<el-form-item label="头像" label-width="100px">

						<el-upload action="https://jsonplaceholder.typicode.com/posts/" list-type="picture-card" :on-preview="handlePictureCardPreview"
								   :on-remove="handleRemove">
							<i class="el-icon-plus"></i>
						</el-upload>
						<el-dialog :visible.sync="dialogVisible">
							<img width="100%" :src="formOne.avatar" alt="">
						</el-dialog>
						<span >建议大小500*500</span>

					</el-form-item>
					<div>
						<el-form-item label="从事行业" label-width="100px">
							<el-select v-model="formOne.industryId"
									   clearable
									   placeholder="行业"
									   @change="handleFilter"
									   class="section1-select">
								<el-option v-for="item in industryOptions" :key="item.industryId" :label="item.name" :value="item.id"></el-option>
							</el-select>
						</el-form-item>
					</div>
					<div>
						<el-form-item label="所在公司" label-width="100px" prop="companyName">
							<el-input style="max-width: 200px" v-model="formOne.companyName" placeholder="请输入您的公司名称"></el-input>
						</el-form-item>
					</div>
					<div>
						<el-form-item label="从业时间" label-width="100px">
							<el-select v-model="formOne.experienceTimeId"   clearable placeholder="请选择您的工作经验年限"
							 @change="handleFilter" class="section1-select">
								<el-option v-for="item in experienceTimeOptions" :key="item.experienceTimeId" :label="item.name" :value="item.id"></el-option>
							</el-select>
						</el-form-item>
					</div>
					<div>
						<el-form-item label="从业经历" label-width="100px" prop="experience">
							<el-input style="max-width: 200px" type="textarea"  v-model="formOne.experience " placeholder="字数不超过150字" :autosize="{minRows: 3,maxRows: 10}"></el-input>
						</el-form-item>
					</div>
					<div>
						<el-form-item label="面试官自述" label-width="100px" prop="description">
							<el-input type="textarea" style="max-width: 200px" v-model="formOne.description" placeholder="字数不超过150字" :autosize="{minRows: 3,maxRows: 10}"></el-input>
						</el-form-item>
					</div>


				</div>
			</el-form>
		</div>
			<div v-if="active==2">
				<el-form ref="formTwo" :model="formTwo" :rules="rules">
				<el-form-item label="可提供面试内容：" label-width="150px">
					<el-checkbox v-model="checked1">HR面试（通用）</el-checkbox>
					<el-checkbox v-model="checked2">专业技术（测试）</el-checkbox>
					<el-checkbox v-model="checked3">行业经验</el-checkbox>

				</el-form-item>

				<div v-if="checked1===true">
					<el-form-item label="HR通用面试理想咨询价格：" label-width="300px" style="margin-left: -100px" prop="price">
						<el-input style="max-width: 300px" v-model="formTwo.price" placeholder="请输入您理想资询价位 (元/半小时)"></el-input>
					</el-form-item>
					<el-form-item label="面试内容简介：" label-width="300px" style="margin-left: -100px" prop="description">
						<el-input type="textarea" style="width: 200px" v-model="formTwo.description" placeholder="请对面试的内容进行具体描述，150字以内" :autosize="{minRows: 3,maxRows: 10}"></el-input>
					</el-form-item>
				</div>
				<div v-if="checked2===true">
					<el-form-item label="专业技术测试理想咨询价格：" label-width="300px" style="margin-left: -100px" prop="price">
						<el-input v-model="formTwo.price" style="max-width: 300px" placeholder="请输入您理想资询价位 (元/半小时)"></el-input>
					</el-form-item>
					<el-form-item label="面试内容简介：" label-width="300px" style="margin-left: -100px" prop="description">
						<el-input type="textarea" style="width: 200px" v-model="formTwo.description" placeholder="请对面试的内容进行具体描述，150字以内" :autosize="{minRows: 3,maxRows: 10}"></el-input>
					</el-form-item>
				</div>
				<div v-if="checked3===true">
					<el-form-item label="职业规划理想咨询价格：" label-width="300px" style="margin-left: -100px" prop="price">
						<el-input v-model="formTwo.price" style="max-width: 300px" placeholder="请输入您理想资询价位 (元/半小时)"></el-input>
					</el-form-item>
					<el-form-item label="面试内容简介：" label-width="300px" style="margin-left: -100px" prop="description">
						<el-input type="textarea" style="width: 200px" v-model="formTwo.description" placeholder="请对面试的内容进行具体描述，150字以内" :autosize="{minRows: 3,maxRows: 10}"></el-input>
					</el-form-item>
				</div>



				</el-form>
			</div>



			<div v-if="active==3">
				<el-form ref="formThree" :model="formThree" :rules="rules">
				<el-form-item label="您的姓名：" label-width="100px" prop="name">
					<el-input style="width: 200px" v-model="formThree.name" placeholder="请输入您的真实姓名"></el-input>
				</el-form-item>
				<el-form-item label="身份证号：" label-width="100px" prop="idNumber">
					<el-input style="width: 300px" v-model="formThree.idNumber" placeholder="请输入您的身份证号"></el-input>
				</el-form-item>

				<el-form-item label="身份证照片" label-width="100px">

					<el-upload action="https://jsonplaceholder.typicode.com/posts/" list-type="picture-card" :on-preview="handlePictureCardPreview"
					 :on-remove="handleRemove">
						<i class="el-icon-plus"></i>
					</el-upload>
					<el-dialog :visible.sync="dialogVisible">
						<img width="100%" :src="formThree.faceUrl" alt="">
					</el-dialog>
					<el-dialog :visible.sync="dialogVisible">
						<img width="100%" :src="formThree. emblemUrl" alt="">
					</el-dialog>
					<el-dialog :visible.sync="dialogVisible">
						<img width="100%" :src="formThree.holdUrl" alt="">
					</el-dialog>
					<span >*点击图片框上传身份证人面像+身份证国徽面+手持身份证正面照*</span>

				</el-form-item>

				<el-form-item label="注意事项：" label-width="100px">
					<div style="line-height: 20px">
						<p> 1、同一个身份证号只能认证一个账号；</p>
						<p> 2、身份证国徽面与正面信息应为同一身份证的信息且在有效期内；</p>
						<p> 3、所有上传照片需清晰且未遮挡，请勿进行美化和修改，以免认证失败；</p>
						<p> 4、 上传本人手持身份证信息面照片中应含有本人头部或上半身；</p>
						<p> 5、手持身份证照中本人形象需为免冠且并未化妆，五官清晰；</p>
						<p> 6、拍照手持身份证照时对焦点请置于身份证上，保证身份证信息清晰且未遮挡；</p>
						<p> 7、上传图片文件格式支持png，jpg和bmp；</p>
						<p> 8、单张图片大小不超过3MB，尺寸最小为500px * 500px；</p>
						<p> 9、所有上传信息均会被妥善保管，不会用于其他商业用途或传输给其他第三方；</p>
					</div>
				</el-form-item>
				<div class="Btn">
					<el-form-item>
						<el-row>
							<el-button type="primary" @click="onSubmit">提交认证信息</el-button>
							<el-button type="primary" @click="onSubmit4">其他认证方式</el-button>
						</el-row>
					</el-form-item>
				</div>
				</el-form>
			</div>
		<div v-if="active===1"></div>
		<div v-if="active===2"></div>
		<div v-if="active===3"></div>
		<div class="Btn">
			<el-row>
<!--				<el-button type="primary" @click="next1" v-if="active==1">下一步</el-button>-->
				<el-button type="primary"  @click="onSubmit2" :disabled="submit2Disabled" v-if="active==2">提交</el-button>
				<el-button type="primary"  @click="onSubmit1" :disabled="submit1Disabled" v-if="active==1">提交</el-button>
				<el-button type="primary" @click="next2" v-if="active==1||active==2">下一步</el-button>

				<el-button type="primary" @click="prev" v-if="active==2">上一步</el-button>


			</el-row>
		</div>

	</div>

</template>

<script>
	import {listByType} from "@/api/dict_api";
	import Pagination from "@/components/Pagination";
	import {mapGetters} from "vuex";
	import {getUserId,getToken} from '@/utils/auth'
	import {formatListQuery, parseListQuery} from "@/utils/common";
	import {addInterview,deleteInterview,addInterviewDirection,addInterviewAuth} from "@/api/interview_api";
	import Toast from "@/utils/toast";


	export default {
		ready(){
			this.submit1Disabled = false; //初始化为face表示是可以点击的
			this.submit2Disabled = false; //初始化为face表示是可以点击的


		},
		components: {
			Pagination
		},
		computed:{
			...mapGetters(["keyword"])
		},
		data() {

			const state = {
				token:getToken(),
				userId:getUserId(),
			}
			console.log(state.userId)
			console.log(state.token)



			var isIdNumber = (rule, value, callback) => {
				if (!value) {
					return new Error("请输入身份证号");
				} else {
					const reg = /^\d{6}(18|19|20)?\d{2}(0[1-9]|1[0-2])(([0-2][1-9])|10|20|30|31)\d{3}(\d|X|x)$/;
					const card = reg.test(value);
					if (!card) {
						//判断座机为12位
						callback(new Error("身份证格式如:423024xxxx0216xxxx"));
					} else {
						callback();
					}
				}
			};

			var isPrice = (rule, value, callback) => {
				if (!value) {
					callback();
				} else {
					var reg = /^-?\d{1,4}(?:\.\d{1,2})?$/;
					if (reg.test(value)) {
						callback();
					} else {
						callback(new Error("数字格式:0-9999或小数点后可加1到2位"));//如:1 或1.8 或1.85
					}
				}
			};


			return {
				listQuery: {
					keyword:"",
					page: 1,
					limit: 10,

				},
				submit1Disabled:false,
				submit2Disabled:false,

				dialogImageUrl: '',
				dialogVisible: false,
				checked1: false,
				checked2: false,
				checked3: false,
				active: 1,
				total:0,
				pageResult: {},
				industryOptions: [],
                experienceTimeOptions: [],
				showNoResult: false,


				formOne: {

					nickName: '',
					avatar: '',
					industryId: '',
					experienceTimeId:'',
					companyName: '',
					description: '',
					experience: '',
					delFlag:0,
				},
				formTwo:{
					description:'',
					direction:'',
					interviewerId:state.userId,
					price:''
				},
				formThree:{
					name:'',
					idNumber:'',
					faceUrl: '',
					emblemUrl:'',
					holdUrl: ''
				},
				rules: {
					nickName: [
						{ required: true, message: '请输入用户昵称', trigger: 'blur' },
						{ min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
					],
					name:[
							{ required: true, message: '请输入您的姓名', trigger: 'blur' },
						],
					idNumber: [{
						required: true,
							trigger: 'blur',
						validator:isIdNumber
						}
					],
					companyName: [
							{ required: true, message: '请输入您的公司名称', trigger: 'blur' },
					],
					description: [{required: true, message: '请输入面试官自述',trigger: 'blur'},
						{max: 150, message: '长度不超过150个字', trigger: 'blur'}
					],
					experience: [{
						required: true,
							message: '请输入从业经历',
							trigger: 'blur'
						},
						{
							max: 150,
							message: '长度不超过150个字',
							trigger: 'blur'
						}
					],
					price: [{
						required: true,
							message: '请输入您理想资询价位',
						trigger:'blur'

						},
						{validator:isPrice,
							trigger:'blur'
						}
					]


				},
				uploadPicOptions: {
					action: "",
					params: {},
					fileUrl: "",
					acceptFileType: ".jpg,.jpeg,.png,.JPG,.JPEG,.PNG",
				},

			};
		},
		created() {
			// 获取默认显示的图片
			this.baseImg = require('../assets/avatar.png')
			this.$emit("complete");
			this.initData();
			this.getList();
		},
		watch:{
			$route(){
				this.getList();
			}	,
			keyword(){
				this.library.keyword=this.keyword;
				this.handleRouteList();
			}
		},
		methods: {
		    initData(){
				listByType(6).then(
						response => (this.industryOptions = response.data.list)
				);
				listByType(10).then(
						response => (this.experienceTimeOptions = response.data.list)
				);
            },
			beforeAvatarUpload(file) {
				return new Promise((resolve, reject) => {
					if (checkPicSize(file)) {
						reject();
					} else {
						getUploadPicToken(file.name)
								.then((response) => {
									const { data } = response;
									this.uploadPicOptions.action = data.host;
									this.uploadPicOptions.params = data;
									this.uploadPicOptions.fileUrl = data.host + "/" + data.key;
									resolve(data);
								})
								.catch((error) => {
									reject(error);
								});
					}
				});
			},
			handleAvatarSuccess() {
				this.resumeForm.avatar = this.uploadPicOptions.fileUrl;
				this.resume.avatar = this.uploadPicOptions.fileUrl;

			},
			handleFilter() {
				this.listQuery.page = 1;
				this.handleRouteList();
			},
			getList() {
				this.showNoResult = false;
				parseListQuery(this.$route.query, this.listQuery);
			},
			handleRouteList() {
				this.$router.replace({
					path: this.$route.path,
					query: formatListQuery(this.listQuery)
				});
			},
			handleRouteList() {
				this.$router.replace({
					path: this.$route.path,
					query: formatListQuery(this.listQuery)
				});
			},
			handleRemove(file, fileList) {
				console.log(file, fileList);

			},
			handlePictureCardPreview(file) {
				this.dialogImageUrl = file.url;
				this.dialogVisible = true;
			},
			change(e) {
				console.log(e.target.files[0].name);
				// 判断是不是规定格式
				// let name  =  e.target.files[0].name

				// 获取到第一张图片
				let file = e.target.files[0]

				// 创建文件读取对象
				var reader = new FileReader()
				var that = this

				//  将文件读取为DataURL
				reader.readAsDataURL(file)

				// 读取成功调用方法
				reader.onload = e => {
					console.log('读取成功');

					// e.target.result 获取 读取成功后的  文件DataURL
					that.avatar = e.target.result

					// 如果要将图片上传服务器，就在这里调用后台方法
				}

			},
			handleAvatarSuccess(res, file) {
				this.avatar = URL.createObjectURL(file.raw);
			},
			prev() {
				--this.active;
				if (this.active < 0) this.active = 0;
			},
			onSubmit() {
				this.$refs["formThree"].validate(valid=> {
					if (valid) {
						addInterviewAuth(this.formThree).then(()=> {
							Toast.success("提交成功");
							let query = { ...this.$route.query
							};
							this.$router.push({
								path: this.redirect || "/RegisterInterviewerSuccess",
								query
							});
								}
						)
					} else {
						console.log('error submit!!');
						return false;
					}
				})
			} ,
			onSubmit1() {
				this.$refs["formOne"].validate(valid=> {
					if (valid) {

							addInterview(this.formOne).then(()=>{
								Toast.success("提交成功，请点击下一步");
							})
						this.submit1Disabled=true;


					} else {
						console.log('error submit!!');
						this.submit1Disabled=false;
					}
				})


			},
			onSubmit2() {
				this.$refs["formTwo"].validate(valid=> {
					if (valid) {
						addInterviewDirection(this.formTwo).then(()=> {

							Toast.success("提交成功，请点击下一步");
							// this.getCommentList();
								}
						)
						this.submit2Disabled=true;
					} else {
						console.log('error submit!!');
						this.submit2Disabled=false;
					}
				})
			},
			next2(){

						this.active ++;


			},
			onSubmit4(evt){
				evt.preventDefault();
					let query = { ...this.$route.query
					};
					delete query.redirect;
					this.$router.push({
						path: this.redirect || "/RegisterOther",
						query
					});
			},
			beforeAvatarUpload(file) {
				const isJPG = file.type === 'image/jpeg';
				const isLt2M = file.size / 1024 / 1024 < 10;

				if (!isJPG) {
					this.$message.error('上传头像图片只能是 JPG 格式!');
				}
				if (!isLt2M) {
					this.$message.error('上传头像图片大小不能超过 10MB!');
				}
				return isJPG && isLt2M;
			}
		},



		mounted() {

		},
	}
</script>

<style scoped lang="scss">
	.align-center {
		max-width: 800px;
		margin-left: 300px;
		margin-bottom: 100px;

		.avatar-uploader .el-upload {
			border: 1px dashed #d9d9d9;
			border-radius: 6px;
			cursor: pointer;
			position: relative;
			overflow: hidden;
		}

		.avatar-uploader .el-upload:hover {
			border-color: #409EFF;
		}

		.avatar-uploader-icon {
			font-size: 28px;
			color: #8c939d;
			width: 178px;
			height: 178px;
			line-height: 178px;
			text-align: center;
		}

		.avatar {
			width: 178px;
			height: 178px;
			display: block;
		}

		.section1-select {
			flex: 1;
			min-width: 130px;
			margin: 0 5px 5px;
			height: 100%;
		}

		.Btn {
			margin-left: 400px;
			margin-top: 12px;
		}

		.file {
			position: relative;
			width: 101px;
			height: 100px;
			background-color: #ccc;
		}

		.updata {
			margin-left: 10px;
			display: block;
			height: 100%;
			width: 100%;
			opacity: 0;
			position: absolute;
			top: 0;
			left: 0;
			z-index: 10;
		}

		.img {
			position: absolute;
			top: 0;
			left: 0;
			width: 101px;
			height: 100px;
		}
	}
</style>
