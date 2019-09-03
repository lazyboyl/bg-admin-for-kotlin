<template>
  <Modal v-model="show" :title="$t('role.editTitle')" @on-ok="ok" :loading="loading" :mask-closable="false">
    <Form ref="roleForm" :model="roleForm" :rules="roleFormRule">
      <Row>
        <Col span="11">
          <FormItem :label="$t('role.addRoleNameLabel')" prop="roleName">
            <Input type="text" :maxlength=50 v-model="roleForm.roleName" :placeholder="$t('role.addRoleNameHolder')"/>
          </FormItem>
          <FormItem :label="$t('role.addRoleCodeLabel')" prop="roleCode">
            <Input type="text" :maxlength=50 v-model="roleForm.roleCode" :placeholder="$t('role.addRoleCodeHolder')"/>
          </FormItem>
        </Col>
        <Col span="2">
        </Col>
        <Col span="11">
          <Tree :data="roleForm.roleTrees"  show-checkbox ref="roleTree"></Tree>
        </Col>
      </Row>

    </Form>
  </Modal>
</template>
<script>

  import {updateRole, checkRoleCodeAndName,getRoleByRoleId} from '../../../api/sys/role/role.api'

  export default {
    name: "editRole",
    props: {
      roleId: {
        type: String
      },
      value: {
        type: Boolean,
        default: false
      }
    },
    data() {
      return {
        show: this.value,
        loading: true,
        roleForm: {
          roleId:'',
          roleName: '',
          roleCode: '',
          roleTrees: []
        },
        roleFormRule: this.getRoleFormRule()
      }
    },
    methods: {
      checkRoleCode() {
        let _this = this;
        return function (rule, value, callback) {
          let roleCode = value;
          let roleId = _this.roleForm.roleId
          checkRoleCodeAndName({roleCode,roleId}).then(res => {
            if (res.obj.success == 'pass') {
              callback();
            } else {
              callback(new Error(_this.$t('role.checkRoleCodeFail')));
            }
          });
        }
      },
      checkRoleName() {
        let _this = this;
        return function (rule, value, callback) {
          let roleName = value;
          let roleId = _this.roleForm.roleId
          checkRoleCodeAndName({roleName,roleId}).then(res => {
            if (res.obj.success == 'pass') {
              callback();
            } else {
              callback(new Error(_this.$t('role.checkRoleNameFail')));
            }
          });
        }
      },
      ok() {
        this.$refs['roleForm'].validate((valid) => {
          if (valid) {
            updateRole({
              roleId:this.roleForm.roleId,
              roleName: this.roleForm.roleName,
              roleCode: this.roleForm.roleCode,
              roleTrees: JSON.stringify(this.roleForm.roleTrees)
            }).then(res => {
              if (res.code == 200) {
                this.$Message.success(this.$t('role.addSuccess'));
                // 提交表单数据成功则关闭当前的modal框
                this.closeModal(false);
                // 同时调用父页面的刷新页面的方法
                this.$emit('handleSearch');
              } else {
                this.$Message.error(this.$t('role.addRoleFail') + res.msg);
              }
            })
          } else {
            this.$Message.error(this.$t('role.addFormFail'));
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
      getRoleFormRule() {
        return {
          roleName: [
            {required: true, message: this.$t('role.addRoleNameRuleMessage'), trigger: 'blur'},
            {type: 'string', max: 50, message: this.$t('role.addRoleNameRuleMaxMessage'), trigger: 'blur'},
            {
              validator: this.checkRoleName({
                response: 'exist'
              }), trigger: 'blur'
            }
          ],
          roleCode: [
            {required: true, message: this.$t('role.addRoleCodeRuleMessage'), trigger: 'blur'},
            {type: 'string', max: 50, message: this.$t('role.addRoleCodeRuleMaxMessage'), trigger: 'blur'},
            {
              validator: this.checkRoleCode({
                response: 'exist'
              }), trigger: 'blur'
            }
          ]
        }
      },
      lazy() {
        let _self = this
        setTimeout(function () {
          _self.roleFormRule = _self.getRoleFormRule()
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
          this.$refs['roleForm'].resetFields();
          let roleId = this.roleId;
          getRoleByRoleId({roleId:roleId}).then(res=>{
            if(res.code==200){
              this.roleForm = res.obj;
            }else{
              this.$Message.success(res.msg);
              this.closeModal(false);
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
