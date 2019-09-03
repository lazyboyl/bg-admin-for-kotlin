<template>
  <div>
    <Card title="国际化管理">
      <Row>
        <Col span="14">
          <Card style="min-height: calc(100vh - 274px);">
            <ButtonGroup>
              <Button type="success" v-show="addInternationalButtonShow" @click="handleAdd" size="small">
                新增
              </Button>
              <Button type="primary" v-show="updateInternationalButtonShow" @click="handleUpdate" size="small">
                修改
              </Button>
              <Button type="error" v-show="deleteInternationalButtonShow" @click="handleDelete" size="small">
                删除
              </Button>
            </ButtonGroup>
            <Tree ref="internationalizationTree" :data="internationalizationTreeDate"
                  @on-select-change="onSelectChange"></Tree>
          </Card>
        </Col>
        <Col span="9" offset="1">
          <Card style="min-height:300px;min-height: calc(100vh - 274px);" title="国际化信息">
            <Form>
              <FormItem label="节点路径">
                <Input type="text" :maxlength=200 v-model="nodePath"/>
              </FormItem>
              <FormItem label="父节点名称">
                <Input type="text" :maxlength=50 v-model="parentTreeName"/>
              </FormItem>
              <FormItem label="节点名称">
                <Input type="text" :maxlength=50 v-model="treeName"/>
              </FormItem>
              <FormItem label="节点编码">
                <Input type="text" :maxlength=50 v-model="treeCode"/>
              </FormItem>
              <FormItem v-for="(item,index) in languageInfo"
                        :key="index"
                        :label="item.languageName">
                <Input type="text" :maxlength=100 v-model="item.internationalizationLanguageValue"/>
              </FormItem>
            </Form>
          </Card>
        </Col>
      </Row>
    </Card>
    <addInternationalization v-model="addShow" :parentTreeName="treeName" :parentId="id"
                             v-on:operateTreeAdd="operateTreeAdd"></addInternationalization>
    <updateInternationalization v-model="updateShow" :id="id"
                                v-on:operateTreeUpdate="operateTreeUpdate"></updateInternationalization>
  </div>
</template>
<script>
  import {
    getInternationalizationTree,
    deleteInternationalization,
    getLanguage,
    getInternationalizationById
  } from '../../../api/sys/internationalization/internationalization.api';

  import addInternationalization from './addInternationalization';
  import updateInternationalization from './updateInternationalization';

  export default {
    components: {
      addInternationalization,
      updateInternationalization
    },
    data() {
      return {
        addShow: false,
        updateShow: false,
        addInternationalButtonShow: false,
        updateInternationalButtonShow: false,
        deleteInternationalButtonShow: false,
        internationalizationTreeDate: [],
        parentTreeName: '',
        parentId: 0,
        treeName: '',
        treeCode: '',
        id: 0,
        languageInfo: [],
        nodePath: ''
      }
    },
    methods: {
      operateTreeUpdate(obj, c) {
        if (c == undefined) {
          c = this.internationalizationTreeDate[0];
        }
        if (c.id == this.id) {
          c.title = obj.treeName;
          c.treeCode = obj.treeCode;
        }
        let childrenLength = c.children.length;
        for (let i = 0; i < childrenLength; i++) {
          c.children[i] = this.operateTreeUpdate(obj, c.children[i]);
        }
        return c;
      },
      operateTreeDelete(c) {
        let _this = this;
        let r = -1;
        if (c.id == this.parentId) {
          for (let j = 0; j < c.children.length; j++) {
            if (c.children[j].id == _this.id) {
              r = j;
            }
          }
          if (r != -1) {
            c.children.splice(r, 1);
          }
        }
        let childrenLength = c.children.length;
        for (let i = 0; i < childrenLength; i++) {
          if (c.children[i].children.length > 0) {
            c.children[i] = this.operateTreeDelete(c.children[i]);
          }
          if (c.children[i].id == this.parentId) {
            for (let j = 0; j < c.children[i].children.length; j++) {
              if (c.children[i].children[j].id == _this.id) {
                r = j;
              }
            }
            if (r != -1) {
              c.children[i].children.splice(r, 1);
            }
          }
        }
        return c;
      },
      operateTreeAdd(obj, c) {
        if (c == undefined) {
          c = this.internationalizationTreeDate[0];
          if (c.id == this.id) {
            let o = new Object();
            o.title = obj.treeName;
            o.parentTreeName = obj.parentTreeName;
            o.parentId = obj.parentId;
            o.id = obj.id;
            o.children = [];
            o.expand = true;
            c.children.push(o)
          }
        }
        let childrenLength = c.children.length;
        for (let i = 0; i < childrenLength; i++) {
          if (c.children[i].children.length > 0) {
            c.children[i] = this.operateTreeAdd(obj, c.children[i]);
          }
          if (c.children[i].id == this.id) {
            let o = new Object();
            o.title = obj.treeName;
            o.parentTreeName = obj.parentTreeName;
            o.parentId = obj.parentId;
            o.id = obj.id;
            o.children = [];
            o.expand = true;
            c.children[i].children.push(o)
          }
        }
        return c;
      },
      handleAdd() {
        if (this.id == 0) {
          this.$Message.warning('请选择需要增加国际化的节点');
          return;
        }
        this.addShow = true;
      },
      handleUpdate() {
        if (this.id == 0) {
          this.$Message.warning('请选择需要修改的国际化的节点');
          return;
        }
        this.updateShow = true;
      },
      handleDelete() {
        if (this.id == 0) {
          this.$Message.warning('请选择需要删除的国际化的节点');
          return;
        }
        this.$Modal.confirm({
          title: '提示',
          content: '<p>是否删除当前选中的节点？</p>',
          onOk: () => {
            deleteInternationalization({id: this.id}).then(res => {
              if (res.code == 200) {
                this.$Message.success(res.msg);
                this.operateTreeDelete(this.internationalizationTreeDate[0]);
              } else {
                this.$Message.warning(res.msg);
              }
            });
          }
        });
      },
      onSelectChange(data) {
        if (data.length == 0) {
          this.id = 0;
          this.treeName = '';
          this.treeCode = '';
          this.parentTreeName = '';
          this.parentId = 0;
          this.nodePath = '';
        } else {
          this.id = data[0].id;
          getInternationalizationById({id: this.id}).then(res => {
            if (res.code == 200) {
              this.id = res.obj.id;
              this.parentTreeName = res.obj.parentTreeName;
              this.treeName = res.obj.treeName;
              this.treeCode = res.obj.treeCode;
              this.parentId = res.obj.parentId;
              // 初始化国际化的相关信息数据
              this.languageInfo.forEach(k => {
                k.internationalizationLanguageValue = "";
                res.obj.internationalizationLanguageList.forEach(r => {
                  if (r.languageId == k.languageId) {
                    k.internationalizationLanguageValue = r.internationalizationLanguageValue;
                  }
                })
              })
              this.nodePath = '';
              res.obj.fullPath.split('.').forEach((r,index)=> {
                if(index>0){
                  if(this.nodePath==''){
                    this.nodePath = this.findCode(this.internationalizationTreeDate[0], r);
                  }else{
                    this.nodePath = this.nodePath + '.' +  this.findCode(this.internationalizationTreeDate[0], r);
                  }
                }
              });
            } else {
              this.$Message.error(res.msg);
            }
          })
        }
      },
      findCode(s, id) {
        if (s.id == id) {
          return s.treeCode;
        }
        if (s.children.length > 0) {
          for (let i = 0; i < s.children.length; i++) {
              let r = this.findCode(s.children[i], id)
              if(r != undefined ){
                return r;
              }
          }
        }
      },
      initInternationalization() {
        const _this = this;
        getInternationalizationTree({}).then(res => {
          if (res.code == 200) {
            _this.internationalizationTreeDate = res.obj;
          } else {
            this.$Message.error("加载菜单节点失败，请与管理员联系！")
          }
        });
      },
      loadButtonAuth() { // 加载菜单按钮权限
        this.addInternationalButtonShow = this.$checkButoonAuth('system-manage-internationalization-add');
        this.updateInternationalButtonShow = this.$checkButoonAuth('system-manage-internationalization-update');
        this.deleteInternationalButtonShow = this.$checkButoonAuth('system-manage-internationalization-delete');
      },
      initLanguage() {
        let _this = this;
        getLanguage({}).then(res => {
          if (res.code == 200) {
            let index = 0;
            res.obj.forEach(r => {
              _this.languageInfo.push({
                index: _this.index,
                internationalizationLanguageValue: '',
                languageId: r.id,
                languageName: r.languageName
              });
              _this.index = _this.index + 1;
            });
          } else {
            this.$Message.error(res.msg);
          }
        })
      }
    },
    mounted() {
      // 初始化菜单树
      this.initInternationalization();
      // 初始化按钮权限
      this.loadButtonAuth();
      // 初始化语言集合
      this.initLanguage();
    }
  }
</script>
