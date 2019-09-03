<template>
  <Modal v-model="show" :title="$t('tree.addButtonTitle')" @on-ok="ok" :loading="loading" :mask-closable="false">
    <Form ref="buttonForm" :model="buttonForm" :rules="buttonFormRule">
      <FormItem :label="$t('tree.addParentTreeNameLabel')">
        <Input type="text" v-model="buttonForm.parentTreeName" disabled/>
      </FormItem>
      <FormItem :label="$t('tree.addButtonNameLabel')" prop="treeName">
        <Input type="text" :maxlength=50 v-model="buttonForm.treeName" :placeholder="$t('tree.addButtonNameHolder')"/>
      </FormItem>
      <FormItem :label="$t('tree.addButtonCodeLabel')" prop="treeCode">
        <Input type="text" :maxlength=50 v-model="buttonForm.treeCode" :placeholder="$t('tree.addButtonCodeHolder')"/>
      </FormItem>
      <FormItem :label="$t('tree.addButtonPowerPathLabel')">
        <Input type="textarea" :rows="4" :maxlength=1000 v-model="buttonForm.powerPath"
               :placeholder="$t('tree.addPowerPathHolder')"/>
      </FormItem>
    </Form>
  </Modal>
</template>
<script>

  import {checkTreeCode, updateButton,getTreeByTreeId} from "../../../api/sys/tree/tree.api"

  export default {
    name: "addButton",
    props: {
      value: {
        type: Boolean,
        default: false
      },
      treeId: {
        type: Number
      }
    },
    data() {
      return {
        show: this.value,
        loading: true,
        buttonForm: {
          treeName: '',
          treeCode: '',
          parentTreeId: '',
          parentTreeName: '',
          powerPath: ''
        },
        buttonFormRule: this.getButtonFormRule()
      }
    },
    methods: {
      ok() {
        this.$refs['buttonForm'].validate((valid) => {
          if (valid) {
            updateButton(this.buttonForm).then(res => {
              if (res.code == 200) {
                this.$Message.success(this.$t('tree.updateButtonSuccess'));
                // 提交表单数据成功则关闭当前的modal框
                this.closeModal(false);
                // 同时调用父页面的刷新页面的方法
                this.$emit('handleSearch');
              } else {
                this.$Message.error(this.$t('tree.updateButtonFail') + res.msg);
              }
            })
          } else {
            this.$Message.error(this.$t('tree.updateButtonFail'));
          }
          setTimeout(() => {
            this.loading = false;
            this.$nextTick(() => {
              this.loading = true;
            });
          }, 1000);
        });
      },
      checkButtonCode() {
        let _this = this;
        return function (rule, value, callback) {
          let buttonCode = value;
          checkTreeCode({treeCode: buttonCode,treeId:_this.treeId}).then(res => {
            if (res.obj.success == 'pass') {
              callback();
            } else {
              callback(new Error(_this.$t('tree.checkFail')));
            }
          });
        };
      },
      getButtonFormRule() {
        return {
          treeName: [
            {required: true, message: this.$t('tree.addButtonNameRuleMessage'), trigger: 'blur'},
            {type: 'string', max: 50, message: this.$t('tree.addButtonNameRuleMaxMessage'), trigger: 'blur'}
          ],
          treeCode: [
            {required: true, message: this.$t('tree.addButtonCodeRuleMessage'), trigger: 'blur'},
            {type: 'string', max: 50, message: this.$t('tree.addButtonCodeRuleMaxMessage'), trigger: 'blur'},
            {
              validator: this.checkButtonCode({
                response: 'exist'
              }), trigger: 'blur'
            }
          ]
        }
      },
      lazy() {
        let _self = this
        setTimeout(function () {
          _self.buttonFormRule = _self.getButtonFormRule()
        }, 200)
      },
      closeModal(val) {
        this.$emit('input', val);
      }
    },
    watch: {
      value(val) {
        this.show = val;
      },
      show(val) {
        //当重新显示增加数据的时候重置整个form表单
        if (val) {
          this.$refs['buttonForm'].resetFields();
          getTreeByTreeId({treeId:this.treeId}).then(res=>{
            if(res.code==200){
              this.buttonForm = res.obj;
            }else{
              this.$Message.error(res.msg);
            }
          });
        } else {// 反之则关闭页面
          this.closeModal(val);
        }
      },
      userLang() {
        this.lazy()
      }
    },
    computed: {
      userLang() {
        return this.$store.getters.userLang;
      }
    }
  }
</script>
