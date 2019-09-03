<template>
  <Modal v-model="show" :title="$t('user.addTitle')" @on-ok="ok" :loading="loading" :mask-closable="false">
    <Form ref="userForm" :model="userForm" :rules="userFormRule">
      <FormItem :label="$t('user.addLoginAccountLabel')" prop="loginAccount">
        <Input type="text" :maxlength=50 v-model="userForm.loginAccount"
               :placeholder="$t('user.addLoginAccountLabelHolder')"/>
      </FormItem>
      <FormItem :label="$t('user.addNickNameLabel')" prop="nickName">
        <Input type="text" :maxlength=50 v-model="userForm.nickName" :placeholder="$t('user.addNickNameHolder')"/>
      </FormItem>
      <FormItem :label="$t('user.addRoleLabel')" prop="roles">
        <Select v-model="userForm.roles" multiple :placeholder="$t('user.addRoleLabelHolder')">
          <Option v-for="item in roleDate" :value="item.roleId" :key="item.roleId">{{ item.roleName }}</Option>
        </Select>
      </FormItem>
      <FormItem :label="$t('org.addParentOrgNameLabel')" prop="orgIds"
                :placeholder="$t('org.addParentOrgNameLabelHolder')">
        <Cascader v-model="userForm.orgIds" :data="orgData" change-on-select filterable></Cascader>
      </FormItem>
      <FormItem :label="$t('user.addPcaLabel')" prop="pca">
        <al-cascader v-model="userForm.pca" level="2" @on-change="pcaChange"></al-cascader>
      </FormItem>
      <FormItem :label="$t('user.addAddressLabel')" prop="address">
        <Input type="textarea" :rows="4" :maxlength=100 v-model="userForm.address" :placeholder="$t('user.addAddressRuleMessage')"/>
      </FormItem>
      <FormItem label="头像地址" prop="headImg">
        <Row>
          <Col span="24">
            <baseImgUpload ref="headImg" :maxLength="1" idImageType="user"
                           @getUploadList="getHeadImgUploadList"></baseImgUpload>
          </Col>
        </Row>
      </FormItem>
    </Form>
  </Modal>
</template>
<script>
  import {getOrgCascader} from "../../../api/sys/org/org.api"
  import {
    loadAllRole,
    createUser,
    checkLoginAccount
  } from "../../../api/sys/user/user.api"
  import baseImgUpload from '../../../components/upload';

  export default {
    name: "addOrg",
    components: {
      baseImgUpload
    },
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
        userForm: {
          loginAccount: '',
          nickName: '',
          orgIds: [],
          roles: [],
          headImg: '',
          province: '',
          provinceName: '',
          city: '',
          cityName: '',
          area: '',
          areaName: '',
          address: '',
          pca: []
        },
        userFormRule: this.getUserFormRule(),
        orgData: [],
        roleDate: []
      }
    },
    methods: {
      getHeadImgUploadList(val) {
        if (val.length > 0) {
          this.userForm.headImg = val[0].name;
        } else {
          this.userForm.headImg = '';
        }
      },
      ok() {
        this.$refs['userForm'].validate((valid) => {
          if (valid) {
            createUser({
              loginAccount: this.userForm.loginAccount,
              nickName: this.userForm.nickName,
              headImg: this.userForm.headImg,
              orgIds: this.userForm.orgIds.join(','),
              roles: this.userForm.roles.join(','),
              province: this.userForm.province,
              provinceName: this.userForm.provinceName,
              city: this.userForm.city,
              cityName: this.userForm.cityName,
              area: this.userForm.area,
              areaName: this.userForm.areaName,
              address: this.userForm.address
            }).then(res => {
              if (res.code == 200) {
                this.$Message.success(res.msg);
                // 提交表单数据成功则关闭当前的modal框
                this.closeModal(false);
                // 同时调用父页面的刷新页面的方法
                this.$emit('handleSearch');
              } else {
                this.$Message.error(res.msg);
              }
            })
          } else {
            this.$Message.error(this.$t('user.addUserFail'));
          }
          setTimeout(() => {
            this.loading = false;
            this.$nextTick(() => {
              this.loading = true;
            });
          }, 1000);
        });
      },
      getUserFormRule() {
        return {
          loginAccount: [
            {required: true, message: this.$t('user.addLoginAccountRuleMessage'), trigger: 'blur'},
            {type: 'string', max: 50, message: this.$t('user.addLoginAccountRuleMaxMessage'), trigger: 'blur'},
            {
              validator: this.checkLoginAccount({
                response: 'exist'
              }), trigger: 'blur'
            }
          ],
          nickName: [
            {required: true, message: this.$t('user.addNickNameRuleMessage'), trigger: 'blur'},
            {type: 'string', max: 50, message: this.$t('user.addNickNameRuleMaxMessage'), trigger: 'blur'}
          ],
          pca: [
            {required: true, message: this.$t('user.addPcaRuleMessage'), trigger: 'change', type: "array"}
          ],
          address:[
            {required: true, message: this.$t('user.addAddressRuleMessage'), trigger: 'blur'}
          ],
          roles: [
            {required: true, message: this.$t('user.addRolesRuleMessage'), trigger: 'change', type: "array"}
          ],
          orgIds: [
            {required: true, message: this.$t('user.addOrgIdsRuleMessage'), trigger: 'change', type: "array"}
          ],
          headImg: [
            {required: true, message: '请选择头像地址', trigger: 'blur'}
          ]
        }
      },
      checkLoginAccount() {
        let _this = this;
        return function (rule, value, callback) {
          let loginAccount = value;
          checkLoginAccount({loginAccount: loginAccount}).then(res => {
            if (res.obj.success == 'pass') {
              callback();
            } else {
              callback(new Error(_this.$t('user.checkUserFail')));
            }
          });
        };
      },
      closeModal(val) {
        this.$emit('input', val);
      },
      lazy() {
        let _self = this
        setTimeout(function () {
          _self.userFormRule = _self.getUserFormRule()
        }, 200)
      },
      pcaChange(data) {
        if(data!=''){
          this.userForm.province = data[0].code
          this.userForm.provinceName = data[0].name
          this.userForm.city = data[1].code
          this.userForm.cityName = data[1].name
          this.userForm.area = data[2].code
          this.userForm.areaName = data[2].name
        }
      }
    },
    watch: {
      value(val) {
        this.show = val;
      },
      show(val) {
        //当重新显示增加数据的时候重置整个form表单
        if (val) {
          this.$refs['userForm'].resetFields();

          this.$refs['headImg'].cleanAll();
          getOrgCascader({}).then(res => {
            if (res.code == 200) {
              this.orgData = res.obj;
            } else {
              this.$Message.error(res.msg);
            }
          });
          loadAllRole({}).then(res => {
            if (res.code == 200) {
              this.roleDate = res.obj;
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
