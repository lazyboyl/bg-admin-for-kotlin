<template>
	<Modal v-model="show" title="修改" @on-ok="ok" :loading="loading" :mask-closable="false">
		<Form ref="languageForm" :model="languageForm" :rules="languageFormRule">
			<FormItem label="语言类型名称" prop="languageName">
				<Input type="text" :maxlength=50 v-model="languageForm.languageName" placeholder="请输入语言类型名称"/>
			</FormItem>
			<FormItem label="语言类型编码" prop="languageCode">
				<Input type="text" :maxlength=50 v-model="languageForm.languageCode" placeholder="请输入语言类型编码"/>
			</FormItem>
      <FormItem label="使用状态">
        <RadioGroup v-model="languageForm.state" vertical>
          <Radio label="1">
            <Icon type="md-checkbox-outline"></Icon>
            <span>启用</span>
          </Radio>
          <Radio label="2">
            <Icon type="md-close"></Icon>
            <span>禁用</span>
          </Radio>
        </RadioGroup>
      </FormItem>
		</Form>
	</Modal>
</template>
<script>
	import {updateLanguage,getLanguage} from '../../../api/sys/language/language.api'
	export default {
		name: "updateLanguage",
		props: {
			value: {
				type: Boolean,
				default: false
			},
			id: {
				type: Number
			}
		},
		data() {
			return {
				show: this.value,
				loading: true,
				languageForm: {
					id:1,
					languageName:'',
					languageCode:'',
					state:'',
				},
				languageFormRule: this.getLanguageFormRule()
			}
		},
		methods: {
			ok() {
				this.$refs['languageForm'].validate((valid) => {
					if (valid) {
						updateLanguage(this.languageForm).then(res => {
							if (res.code == 200) {
								this.closeModal(false);
								this.$emit('handleSearch');
								this.$Message.success(res.msg);
							}else{
								this.$Message.error(res.msg);
							}
						})
					} else {
						this.$Message.error('表单验证不通过！');
					}
					setTimeout(() => {
						this.loading = false;
						this.$nextTick(() => {
							this.loading = true;
						});
					}, 1000);
				});
			},
			closeModal(val) {
				this.$emit('input', val);
			},
			getLanguageFormRule() {
				return {
					languageName: [
						{required: true, message: '语言类型名称不能为空！', trigger: 'blur'},
						{type: 'string', max: 128, message: '数据的最大长度为128！', trigger: 'blur'}
					],
					languageCode: [
						{required: true, message: '语言类型编码不能为空！', trigger: 'blur'},
						{type: 'string', max: 128, message: '数据的最大长度为128！', trigger: 'blur'}
					]
				}
			}
		},
		watch: {
			value(val) {
				this.show = val;
			},
			show(val) {
				if (val) {
					this.$refs['languageForm'].resetFields();
					getLanguage({id: this.id}).then(res => {
						if (res.code == 200) {
							this.languageForm= res.obj;
						} else {
							this.$Message.error(res.msg);
						}
					});
				} else {
					this.closeModal(val);
				}
			}
		}
	}
</script>
