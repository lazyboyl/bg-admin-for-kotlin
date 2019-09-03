<template>
  <Modal v-model="show" title="修改国际化信息" @on-ok="ok" :loading="loading" :mask-closable="false">
    <Form ref="internationalizationForm" :model="internationalizationForm">
      <FormItem label="父节点名称">
        <Input type="text" :maxlength=50 v-model="internationalizationForm.parentTreeName" disabled/>
      </FormItem>
      <FormItem label="节点名称" prop="treeName" :rules="{required: true, message: '节点名称不能为空！', trigger: 'blur'}">
        <Input type="text" :maxlength=50 v-model="internationalizationForm.treeName"
               placeholder="请输入国际化的节点名称"/>
      </FormItem>
      <FormItem label="节点编码" prop="treeCode" :rules="{required: true, message: '节点编码不能为空！', trigger: 'blur'}">
        <Input type="text" :maxlength=50 v-model="internationalizationForm.treeCode"
               placeholder="请输入国际化的节点编码"/>
      </FormItem>
      <FormItem v-for="(item,index) in internationalizationForm.languageInfo"
                :key="index"
                :label="item.languageName"
                :prop="'languageInfo.'+index+'.internationalizationLanguageValue'"
                :rules="{required: true, message: '国际化对应的值不能为空！', trigger: 'blur'}">
        <Input type="text" :maxlength=100 v-model="item.internationalizationLanguageValue"
               placeholder="请输入国际化对应的值"/>
      </FormItem>
    </Form>
  </Modal>
</template>
<script>

  import {
    getLanguage,
    updateInternationalize,
    getInternationalizationById
  } from '../../../api/sys/internationalization/internationalization.api';


  export default {
    name: "updateInternationalization",
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
        internationalizationForm: {
          id: this.id,
          parentTreeName: '',
          parentId: 0,
          treeName: '',
          treeCode: '',
          languageInfo: []
        },
        languages: []
      }
    },
    methods: {
      closeModal(val) {
        this.$emit('input', val);
      },
      ok() {
        console.log(JSON.stringify(this.internationalizationForm))
        this.$refs['internationalizationForm'].validate((valid) => {
          if (valid) {
            updateInternationalize({
              id: this.id,
              treeName: this.internationalizationForm.treeName,
              treeCode: this.internationalizationForm.treeCode,
              languageInfo: JSON.stringify(this.internationalizationForm.languageInfo)
            }).then(res => {
              if (res.code == 200) {
                this.$Message.success(res.msg);
                // 提交表单数据成功则关闭当前的modal框
                this.closeModal(false);
                // 同时调用父页面的刷新页面的方法
                this.$emit('operateTreeUpdate', res.obj);
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
      initLanguage() {
        let _this = this;
        getLanguage({}).then(res => {
          if (res.code == 200) {
            let index = 0;
            res.obj.forEach(r => {
              _this.internationalizationForm.languageInfo.push({
                index: _this.index,
                internationalizationLanguageValue: '',
                languageId: r.id,
                languageName: r.languageName
              });
              _this.index = _this.index + 1;
            });
            _this.languages = res.obj;
          } else {
            this.$Message.error(res.msg);
          }
        })
      }
    },
    watch: {
      value(val) {
        this.show = val;
      },
      show(val) {
        //当重新显示增加数据的时候重置整个form表单
        if (val) {
          this.$refs['internationalizationForm'].resetFields();
          getInternationalizationById({id: this.id}).then(res => {
            if (res.code == 200) {
              this.internationalizationForm.id = res.obj.id;
              this.internationalizationForm.parentTreeName = res.obj.parentTreeName;
              this.internationalizationForm.treeName = res.obj.treeName;
              this.internationalizationForm.treeCode = res.obj.treeCode;
              this.internationalizationForm.parentId = res.obj.parentId;
              // 初始化国际化的相关信息数据
              res.obj.internationalizationLanguageList.forEach(r => {
                this.internationalizationForm.languageInfo.forEach(k => {
                  if (r.languageId == k.languageId) {
                     k.internationalizationLanguageValue = r.internationalizationLanguageValue;
                  }
                })
              })
            } else {
              this.$Message.error(res.msg);
            }
          })
        } else {// 反之则关闭页面
          this.closeModal(val);
        }
      }
    },
    mounted() {
      this.initLanguage();
    }
  }
</script>
