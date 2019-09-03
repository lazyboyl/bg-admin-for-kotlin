<template>
  <Modal v-model="show" :title="$t('tree.addTreeTitle')" @on-ok="ok" :loading="loading" :mask-closable="false">
    <Form ref="treeForm" :model="treeForm" :rules="treeFormRule">
      <FormItem :label="$t('tree.addParentTreeNameLabel')">
        <Input type="text" v-model="treeForm.parentTreeName" disabled/>
      </FormItem>
      <FormItem :label="$t('tree.addTreeNameLabel')" prop="treeName">
        <Input type="text" :maxlength=50 v-model="treeForm.treeName" :placeholder="$t('tree.addTreeNameHolder')"/>
      </FormItem>
      <FormItem :label="$t('tree.addTreeCodeLabel')" prop="treeCode">
        <Input type="text" :maxlength=50 v-model="treeForm.treeCode" :placeholder="$t('tree.addTreeCodeHolder')"/>
      </FormItem>
      <FormItem :label="$t('tree.addPowerPathLabel')" >
        <Input type="textarea" :rows="4" :maxlength=500 v-model="treeForm.powerPath" :placeholder="$t('tree.addPowerPathHolder')"/>
      </FormItem>
    </Form>
  </Modal>
</template>
<script>
  import {checkTreeCode,getTreeByTreeId,updateTree} from "../../../api/sys/tree/tree.api"

  export default {
    name: "updateTree",
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
        treeForm: {
          treeName: '',
          treeCode: '',
          treeId: '',
          parentTreeName: '',
          powerPath : ''
        },
        treeFormRule: this.getTreeFormRule()
      }
    },
    methods: {
      ok() {
        this.$refs['treeForm'].validate((valid) => {
          if (valid) {
            updateTree(this.treeForm).then(res => {
              if (res.code == 200) {
                this.$Message.success(this.$t('tree.addTreeSuccess'));
                // 提交表单数据成功则关闭当前的modal框
                this.closeModal(false);
                // 同时调用父页面的刷新页面的方法
                this.$emit('reloadTree');
              } else {
                this.$Message.error(this.$t('tree.addTreeFail')+res.msg);
              }
            })
          } else {
            this.$Message.error(this.$t('tree.addTreeFail'));
          }
          setTimeout(() => {
            this.loading = false;
            this.$nextTick(() => {
              this.loading = true;
            });
          }, 1000);
        });
      },
      checkTreeCode(){
        let _this = this;
        return function (rule, value, callback) {
          let treeCode = value;
          checkTreeCode({treeCode:treeCode,treeId:_this.treeId}).then(res => {
            if (res.obj.success == 'pass') {
              callback();
            } else {
              callback(new Error(_this.$t('tree.checkTreeFail')));
            }
          });
        };
      },
      getTreeFormRule() {
        return {
          treeName: [
            {required: true, message: this.$t('tree.addTreeNameRuleMessage'), trigger: 'blur'},
            {type: 'string', max: 50, message: this.$t('tree.addTreeNameRuleMaxMessage'), trigger: 'blur'}
          ],
          treeCode: [
            {required: true, message: this.$t('tree.addTreeCodeRuleMessage'), trigger: 'blur'},
            {type: 'string', max: 50, message: this.$t('tree.addTreeCodeRuleMaxMessage'), trigger: 'blur'},
            {
              validator: this.checkTreeCode({
                response: 'exist'
              }), trigger: 'blur'
            }
          ]
        }
      },
      lazy() {
        let _self = this
        setTimeout(function () {
          _self.treeFormRule = _self.getTreeFormRule()
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
        if(val){
          this.$refs['treeForm'].resetFields();
          getTreeByTreeId({treeId:this.treeId}).then(res=>{
            if(res.code==200){
              this.treeForm = res.obj;
            }else{
              this.$Message.error(res.msg);
            }
          });
        }else{// 反之则关闭页面
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
