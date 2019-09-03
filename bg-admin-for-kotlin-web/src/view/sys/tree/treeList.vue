<template>
  <div>
    <Card :title="$t('tree.title')">
      <Row>
        <Col span="6">
          <Card style="min-height: calc(100vh - 274px);">
            <ButtonGroup>
              <Button type="success" v-show="addTreeButtonShow" @click="handleAdd" size="small">
                {{$t('tree.treeButton.addTreeButton')}}
              </Button>
              <Button type="primary" v-show="updateTreeButtonShow" @click="handleUpdate" size="small">
                {{$t('tree.treeButton.modifyTreeButton')}}
              </Button>
              <Button type="error" v-show="deleteTreeButtonShow" @click="handleDelete" size="small">
                {{$t('tree.treeButton.deleteTreeButton')}}
              </Button>
            </ButtonGroup>
            <Tree :data="roleTreeDate" @on-select-change="onSelectChange"></Tree>
          </Card>
        </Col>
        <Col span="17" offset="1">
          <Card :title="parentTreeName">
            <div>
              <div style="display:inline-block;float:left;">
                <Button type="success" v-show="addButtonButtonShow" @click="handleTreeButton">+{{$t('tree.createButton')}}</Button>
              </div>
              <div style="display:inline-block;float:right;">
                <Input v-model="search" suffix="ios-search" :placeholder="$t('tree.searchText')" style="width: auto"
                       :search=true @on-search="handleSearch"/>
              </div>
            </div>
            <div style="clear: both;"></div>
            <div style="margin-top: 10px;">
              <Table ref="treeButtonTable" @on-sort-change="onSortChange" :columns="columns" :data="treeButtonData"
                     :height="tableHeight">
                <template slot-scope="{ row, index }" slot="treeName">
                  <Input type="text" v-model="editTreeName" v-if="editIndex === index"/>
                  <span v-else>{{ row.treeName }}</span>
                </template>

                <template slot-scope="{ row, index }" slot="treeCode">
                  <Input type="text" v-model="editTreeCode" v-if="editIndex === index"/>
                  <span v-else>{{ row.treeCode }}</span>
                </template>

                <template slot-scope="{ row, index }" slot="treeState">
                  <span v-if="row.treeState=='1'">{{ $t('status.available') }}</span>
                  <span v-else>{{ $t('status.disabled') }}</span>
                </template>

                <template slot-scope="{ row, index }" slot="action">
                  <div v-if="editIndex === index">
                    <Button size="small" type="success" @click="handleUpdateButton(index)">{{$t('button.save')}}
                    </Button>
                    <Button size="small" type="error" @click="editIndex = -1">{{$t('button.cancel')}}</Button>
                  </div>
                  <div v-else>
                    <Button type="success" v-show="updateButtonButtonShow"  @click="handleEditButton(row, index)" size="small">{{$t('button.edit')}}
                    </Button>
                    <Button type="error" v-show="deleteButtonButtonShow" @click="handleDeleteButton(row)" size="small">{{$t('button.delete')}}</Button>
                    <Button type="primary" v-show="freezeAndThawButtonShow" v-if="row.treeState=='1'" @click="handleOperate(row,'2')" size="small">
                      {{$t('actions.freeze')}}
                    </Button>
                    <Button type="info" v-show="freezeAndThawButtonShow" v-else @click="handleOperate(row,'1')" size="small">{{$t('actions.thaw')}}
                    </Button>
                  </div>
                </template>
              </Table>
            </div>
            <div style="margin-top: 10px;">
              <Page show-elevator show-sizer show-total :total="total" :current="current"
                    :page-size="pageSize" @on-change="changePage" @on-page-size-change="changePageSize"/>
            </div>
          </Card>
        </Col>
      </Row>
    </Card>
    <addButton v-model="addButtonShow" :parentTreeId="parentTreeId" :parentTreeName="parentTreeName"
               v-on:handleSearch="handleSearch"></addButton>
    <addTree v-model="addTreeShow" :parentTreeId="parentTreeId" :parentTreeName="parentTreeName"
             v-on:reloadTree="initTree"></addTree>
    <updateTree v-model="updateTreeShow" :treeId="parentTreeId" v-on:reloadTree="initTree"></updateTree>
    <updateButton v-model="updateButtonShow" :treeId="editTreeId" v-on:handleSearch="handleSearch"></updateButton>
  </div>
</template>
<script>
  import {
    getTreeList,
    queryTreeButtonList,
    deleteButton,
    operateButton,
    deleteTree
  } from "../../../api/sys/tree/tree.api"
  import addButton from './addButton'
  import updateButton from './updateButton'
  import addTree from './addTree'
  import updateTree from './updateTree'

  export default {
    components: {
      addButton,
      updateButton,
      addTree,
      updateTree
    },
    data() {
      return {
        addTreeButtonShow: false,
        updateTreeButtonShow: false,
        deleteTreeButtonShow: false,
        addButtonButtonShow: false,
        updateButtonButtonShow: false,
        deleteButtonButtonShow: false,
        freezeAndThawButtonShow: false,
        addButtonShow: false,
        updateButtonShow: false,
        addTreeShow: false,
        updateTreeShow: false,
        roleTreeDate: [],
        parentTreeName: this.getParentTreeName(),
        parentTreeId: 0,
        hasChildren: false,
        search: '',
        key: '',
        order: '',
        total: 0,
        current: 1,
        pageSize: 10,
        columns: this.getTreeButtonColumns(),
        treeButtonData: [],
        editTreeName: '',
        editTreeCode: '',
        editTreeId: 0,
        editIndex: -1,
        tableHeight: 150
      }
    },
    methods: {
      handleOperate(row, treeState) {
        let content = this.$t('tree.freezeContent')
        if (treeState == '1') {
          content = this.$t('tree.thawContent')
        }
        this.$Modal.confirm({
          title: this.$t('modal.title'),
          content: '<p>' + content + '</p>',
          onOk: () => {
            operateButton({treeId: row.treeId, treeState: treeState}).then(res => {
              if (res.code == 200) {
                this.$Message.success(this.$t('tree.operateButtonSuccess'));
                // 冻结用户成功同时刷新grid
                this.handleSearch();
              } else {
                this.$Message.warning(this.$t('tree.operateButtonFail'));
              }
            });
          },
          onCancel: () => {
            this.$Message.info(this.$t('dict.deleteCancel'));
          }
        });
      },
      handleDeleteButton(row) {
        this.$Modal.confirm({
          title: this.$t('modal.title'),
          content: '<p>' + this.$t('tree.deleteContent') + '</p>',
          onOk: () => {
            deleteButton({treeId: row.treeId}).then(res => {
              if (res.code == 200) {
                this.$Message.success(this.$t('tree.deleteButtonSuccess'));
                // 删除数据成功同时刷新grid
                this.handleSearch();
              } else {
                this.$Message.warning(this.$t('tree.deleteButtonFail'));
              }
            });
          },
          onCancel: () => {
            this.$Message.info(this.$t('dict.deleteCancel'));
          }
        });
      },
      handleEditButton(row, index) {
        this.editTreeId = row.treeId
        this.updateButtonShow = true
      },
      handleUpdateButton(index) {
        updateButton({
          treeId: this.editTreeId,
          treeCode: this.editTreeCode,
          treeName: this.editTreeName
        }).then(res => {
          if (res.code == 200) {
            this.$Message.success(this.$t('tree.updateButtonSuccess'))
            this.editIndex = -1
            this.handleSearch()
          } else {
            this.$Message.error(this.$t('tree.updateButtonFail') + ',' + res.msg)
          }
        });
      },
      handleTreeButton() {
        this.addButtonShow = true
      },
      handleAdd() {
        this.addTreeShow = true
      },
      handleUpdate() {
        if (this.parentTreeId == 0) {
          this.$Message.warning('请选择需要修改的菜单节点');
          return;
        }
        this.updateTreeShow = true
      },
      handleDelete() {
        if (this.parentTreeId == 0) {
          this.$Message.warning('请选择需要删除的菜单节点');
          return;
        }
        this.$Modal.confirm({
          title: this.$t('modal.title'),
          content: '<p>' + this.$t('tree.deleteTreeContent') + '</p>',
          onOk: () => {
            deleteTree({treeId: this.parentTreeId}).then(res => {
              if (res.code == 200) {
                this.$Message.success(res.msg);
                this.parentTreeId = 0;
                this.parentTreeName = '顶层节点';
                // 删除数据成功同时刷新grid
                this.initTree();
              } else {
                this.$Message.warning(res.msg);
              }
            });
          },
          onCancel: () => {
            this.$Message.info(this.$t('dict.deleteCancel'));
          }
        });
      },
      changePage(current) {
        this.current = current;
        this.handleSearch();
      },
      changePageSize(pageSize) {// 改变每页记录的条数
        this.pageSize = pageSize;
        this.handleSearch();
      },
      handleSearch() {
        let current = this.current
        let pageSize = this.pageSize
        let search = this.search
        let orderKey = this.key
        let orderByValue = this.order
        let parentTreeId = this.parentTreeId
        const _this = this;
        queryTreeButtonList({
          current,
          pageSize,
          search,
          parentTreeId,
          orderKey,
          orderByValue
        }).then(res => {
          if (res.code == 200) {
            this.$Message.success(this.$t('tree.searchSuccessText'))
            _this.total = res.obj.total
            _this.treeButtonData = res.obj.rows
          } else {
            this.$Message.error(this.$t('tree.searchFailText') + ',' + res.msg)
          }
        })
      },
      onSelectChange(data) {
        // 如果长度为0说明当前没有任何节点被选中
        if (data.length == 0) {
          this.parentTreeId = 0;
          this.parentTreeName = '顶层节点';
          this.hasChildren = false;
        } else {
          this.parentTreeId = data[0].treeId;
          this.parentTreeName = data[0].title;
          if (data[0].children.length == 0) {
            this.hasChildren = false;
          } else {
            this.hasChildren = true;
          }
        }
      },
      onSortChange(sort) {
        if (sort.order == 'normal') {
          this.order = '';
        } else {
          this.key = sort.key;
          this.order = sort.order;
        }
        this.handleSearch();
      },
      initTree() {
        const _this = this;
        getTreeList({}).then(res => {
          if (res.code == 200) {
            _this.roleTreeDate = res.obj;
          } else {
            this.$Message.error("加载菜单节点失败，请与管理员联系！")
          }

        });
      },
      getParentTreeName() {
        return this.$t('tree.parentTreeName')
      },
      getTreeButtonColumns() {
        return [
          {
            title: this.$t('tree.columns.treeNameTitle'),
            slot: 'treeName',
            key: 'treeName',
            sortable: true
          }, {

            title: this.$t('tree.columns.treeCodeTitle'),
            slot: 'treeCode',
            key: 'treeCode',
            sortable: true
          },
          {
            title: this.$t('tree.columns.treeStateTitle'),
            key: 'treeState',
            slot: 'treeState',
            sortable: true
          },
          {
            title: this.$t('tree.columns.operationTitle'),
            slot: 'action'
          }
        ]
      },
      lazy() {
        let _self = this
        setTimeout(function () {
          _self.columns = _self.getTreeButtonColumns()
          _self.parentTreeName = _self.getParentTreeName();
        }, 200)
      },
      loadButtonAuth() { // 加载菜单按钮权限
        this.addTreeButtonShow = this.$checkButoonAuth('system-manage-tree-add');
        this.updateTreeButtonShow = this.$checkButoonAuth('system-manage-tree-update');
        this.deleteTreeButtonShow = this.$checkButoonAuth('system-manage-tree-delete');
        this.addButtonButtonShow = this.$checkButoonAuth('system-manage-button-add');
        this.updateButtonButtonShow = this.$checkButoonAuth('system-manage-button-update');
        this.deleteButtonButtonShow = this.$checkButoonAuth('system-manage-button-delete');
        this.freezeAndThawButtonShow = this.$checkButoonAuth('system-manage-button-operate');
      }
    },
    mounted() {
      // 初始化菜单权限
      this.loadButtonAuth();
      // 初始化完成以后加载菜单数据
      this.initTree();
      this.tableHeight = window.innerHeight - this.$refs.treeButtonTable.$el.offsetTop - 335
    },
    computed: {
      userLang() {
        return this.$store.getters.userLang;
      }
    },
    watch: {
      userLang() {
        this.lazy()
      },
      parentTreeId() {
        this.handleSearch();
      }
    }
  }
</script>
