<template>
  <Modal v-model="show" :title="$t('org.updateTitle')" @on-ok="ok" :loading="loading" :mask-closable="false">
    <Form ref="orgForm" :model="orgForm" :rules="orgFormRule">
      <FormItem :label="$t('org.addParentOrgNameLabel')">
        <Input type="text" v-model="orgForm.parentOrgName" disabled/>
      </FormItem>
      <FormItem :label="$t('org.addOrgNameLabel')" prop="orgName">
        <Input type="text" :maxlength=50 v-model="orgForm.orgName" :placeholder="$t('org.addOrgNameHolder')"/>
      </FormItem>
      <FormItem :label="$t('org.addOrgCodeLabel')" prop="orgCode">
        <Input type="text" :maxlength=50 v-model="orgForm.orgCode" :placeholder="$t('org.addOrgCodeHolder')"/>
      </FormItem>
    </Form>
  </Modal>
</template>
<script>

  import {checkOrgNameAndCode, getOrgByOrgId, updateOrg} from "../../../api/sys/org/org.api"

  export default {
    name: "updateOrg",
    props: {
      value: {
        type: Boolean,
        default: false
      },
      orgId: {
        type: Number
      }
    },
    data() {
      return {
        show: this.value,
        loading: true,
        orgForm: {
          orgId:0,
          orgName: '',
          orgCode: '',
          parentOrgId: 0,
          parentOrgName: '顶层组织架构'
        },
        orgFormRule: this.getOrgFormRule()
      }
    },
    methods: {
      ok() {
        this.$refs['orgForm'].validate((valid) => {
          if (valid) {
            updateOrg(this.orgForm).then(res => {
              if (res.code == 200) {
                this.$Message.success(res.msg);
                // 提交表单数据成功则关闭当前的modal框
                this.closeModal(false);
                // 同时调用父页面的刷新页面的方法
                this.$emit('operateOrgUpdate',res.obj);
              } else {
                this.$Message.error(res.msg);
              }
            })
          } else {
            this.$Message.error(this.$t('org.addOrgFail'));
          }
          setTimeout(() => {
            this.loading = false;
            this.$nextTick(() => {
              this.loading = true;
            });
          }, 1000);
        });
      },
      getOrgFormRule() {
        return {
          orgName: [
            {required: true, message: this.$t('org.addOrgNameRuleMessage'), trigger: 'blur'},
            {type: 'string', max: 50, message: this.$t('org.addOrgNameRuleMaxMessage'), trigger: 'blur'},
            {
              validator: this.checkOrgName({
                response: 'exist'
              }), trigger: 'blur'
            }
          ],
          orgCode: [
            {required: true, message: this.$t('org.addOrgCodeRuleMessage'), trigger: 'blur'},
            {type: 'string', max: 50, message: this.$t('org.addOrgCodeRuleMaxMessage'), trigger: 'blur'},
            {
              validator: this.checkOrgCode({
                response: 'exist'
              }), trigger: 'blur'
            }
          ]
        }
      },
      checkOrgCode() {
        let _this = this;
        return function (rule, value, callback) {
          let orgId = _this.orgId;
          let orgCode = value;
          checkOrgNameAndCode({orgCode: orgCode, orgId: orgId}).then(res => {
            if (res.obj.success == 'pass') {
              callback();
            } else {
              callback(new Error(_this.$t('org.checkOrgFail')));
            }
          });
        };
      },
      checkOrgName() {
        let _this = this;
        return function (rule, value, callback) {
          let orgId = _this.orgId
          let orgName = value;
          checkOrgNameAndCode({orgName: orgName, orgId: orgId}).then(res => {
            if (res.obj.success == 'pass') {
              callback();
            } else {
              callback(new Error(_this.$t('org.checkOrgFail')));
            }
          });
        };
      },
      lazy() {
        let _self = this
        setTimeout(function () {
          _self.orgFormRule = _self.getOrgFormRule()
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
          this.$refs['orgForm'].resetFields();
          getOrgByOrgId({orgId: this.orgId}).then(res => {
            if (res.code == 200) {
              this.orgForm = res.obj;
            } else {
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
