<template>
  <Modal v-model="show" :title="$t('dict.addTitle')" @on-ok="ok" :loading="loading" :mask-closable="false">
    <Form ref="dictForm" :model="dictForm" :rules="dictFormRule">
      <FormItem :label="$t('dict.addDictTypeLabel')" prop="dictType">
        <Input type="text" :maxlength=50 v-model="dictForm.dictType" :placeholder="$t('dict.addDictTypeHolder')"/>
      </FormItem>
      <FormItem :label="$t('dict.addDictCodeLabel')" prop="dictCode">
        <Input type="text" :maxlength=50 v-model="dictForm.dictCode" :placeholder="$t('dict.addDictCodeHolder')"/>
      </FormItem>
      <FormItem :label="$t('dict.addDictTextLabel')" prop="dictText">
        <Input type="text" :maxlength=50 v-model="dictForm.dictText" :placeholder="$t('dict.addDictTextHolder')"/>
      </FormItem>
      <FormItem :label="$t('dict.addDictValueLabel')" prop="dictValue">
        <Input type="text" :maxlength=50 v-model="dictForm.dictValue" :placeholder="$t('dict.addDictValueHolder')"/>
      </FormItem>
    </Form>
  </Modal>
</template>
<script>
  import {addDict, checkTypeAndCode} from '../../../api/sys/dict/dict.api'

  export default {
    name: "addDict",
    props: {
      value: {
        type: Boolean,
        default: false
      }
    },
    data() {
      return {
        show: this.value,
        loading: true,
        dictForm: {
          dictType: '',
          dictCode: '',
          dictText: '',
          dictValue: ''
        },
        dictFormRule: this.getDictFormRule()
      }
    },
    methods: {
      check() {
        let _this = this;
        return function (rule, value, callback) {
          let dictType = _this.dictForm.dictType;
          let dictCode = value;
          checkTypeAndCode({dictType, dictCode}).then(res => {
            if (res.obj.success == 'pass') {
              callback();
            } else {
              callback(new Error(_this.$t('dict.checkFail')));
            }
          });
        };
      },
      ok() {
        this.$refs['dictForm'].validate((valid) => {
          if (valid) {
            addDict(this.dictForm).then(res => {
              if (res.code == 200) {
                this.$Message.success(this.$t('dict.addSuccess'));
                // 提交表单数据成功则关闭当前的modal框
                this.closeModal(false);
                // 同时调用父页面的刷新页面的方法
                this.$emit('handleSearch');
              } else {
                this.$Message.error(this.$t('dict.addDictFail')+res.msg);
              }
            })
          } else {
            this.$Message.error(this.$t('dict.addFormFail'));
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
      getDictFormRule() {
        return {
          dictType: [
            {required: true, message: this.$t('dict.addDictTypeRuleMessage'), trigger: 'blur'},
            {type: 'string', max: 50, message: this.$t('dict.addDictTypeRuleMaxMessage'), trigger: 'blur'}
          ],
          dictCode: [
            {required: true, message: this.$t('dict.addDictCodeRuleMessage'), trigger: 'blur'},
            {type: 'string', max: 50, message: this.$t('dict.addDictCodeRuleMaxMessage'), trigger: 'blur'},
            {
              validator: this.check({
                response: 'exist'
              }), trigger: 'blur'
            }
          ],
          dictText: [
            {required: true, message: this.$t('dict.addDictTextRuleMessage'), trigger: 'blur'},
            {type: 'string', max: 50, message: this.$t('dict.addDictTextRuleMaxMessage'), trigger: 'blur'}
          ],
          dictValue: [
            {required: true, message: this.$t('dict.addDictValueRuleMessage'), trigger: 'blur'},
            {type: 'string', max: 50, message: this.$t('dict.addDictValueRuleMaxMessage'), trigger: 'blur'}
          ]
        }
      },
      lazy() {
        let _self = this
        setTimeout(function () {
          _self.dictFormRule = _self.getDictFormRule()
        }, 200)
      }
    },
    watch: {
      value(val) {
        this.show = val;
      },
      show(val) {
        //当重新显示增加数据的时候重置整个form表单
        if (val) {
          this.$refs['dictForm'].resetFields();
        } else {// 反之则关闭页面
          this.closeModal(val);
        }
      },
      userLang(){
        this.lazy()
      }
    },
    computed:{
      userLang() {
        return this.$store.getters.userLang;
      }
    }
  }
</script>
