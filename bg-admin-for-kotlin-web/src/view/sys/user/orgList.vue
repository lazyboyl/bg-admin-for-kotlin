<template>
  <div>
    <Card :title="$t('user.title')">
      <Row>
        <Col span="6">
          <Card style="min-height:300px;height: calc(100vh - 274px);">
            <ButtonGroup>
              <Button v-show="addOrgButtonShow" type="success" @click="handleAdd" size="small">
                {{$t('user.orgButton.addOrgButton')}}
              </Button>
              <Button v-show="updateOrgButtonShow" type="primary" @click="handleUpdate" size="small">
                {{$t('user.orgButton.modifyOrgButton')}}
              </Button>
              <Button type="error" v-show="deleteOrgButtonShow" @click="handleDelete" size="small">
                {{$t('user.orgButton.deleteOrgButton')}}
              </Button>
            </ButtonGroup>
            <Tree :data="orgDate" @on-select-change="onSelectChange"></Tree>
          </Card>
        </Col>
        <Col span="17" offset="1">
          <Card :title="parentOrgName">
            <div>
              <div style="display:inline-block;float:left;">
                <Button type="success" v-show="addUserButtonShow" @click="handleUserAdd">+{{$t('user.createUser')}}
                </Button>
              </div>
              <div style="display:inline-block;float:right;">
                <Input v-model="search" suffix="ios-search" :placeholder="$t('user.searchText')" style="width: auto"
                       :search=true @on-search="handleSearch"/>
              </div>
            </div>
            <div style="clear: both;"></div>
            <div style="margin-top: 10px;">
              <Table ref="userTable" @on-sort-change="onSortChange" :columns="columns" :data="userData"
                     :height="tableHeight">
                <template slot-scope="{ row, index }" slot="action">
                  <div>
                    <Button type="success" v-show="updateUserButtonShow" @click="handleEditUser(row, index)"
                            size="small">{{$t('button.edit')}}
                    </Button>
                    <Button type="error" v-show="deleteUserButtonShow" @click="handleDeleteUser(row)" size="small">
                      {{$t('button.delete')}}
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
    <addOrg v-model="addOrgShow" :parentOrgId="orgId" :parentOrgName="parentOrgName"
            v-on:operateOrgAdd="operateOrgAdd"></addOrg>
    <updateOrg v-model="updateOrgShow" :orgId="orgId" v-on:operateOrgUpdate="operateOrgUpdate">>
    </updateOrg>
    <addUser v-model="addUserShow" v-on:handleSearch="handleSearch"></addUser>
    <updateUser v-model="updateUserShow" :userId="userId" v-on:handleSearch="handleSearch"></updateUser>
  </div>
</template>
<script>

  import {
    getOrgTree,
    deleteOrg
  } from "../../../api/sys/org/org.api"

  import {
    queryUserList,
    deleteUser
  } from "../../../api/sys/user/user.api"

  import addOrg from './addOrg'
  import updateOrg from './updateOrg'
  import addUser from './addUser'
  import updateUser from './updateUser'

  export default {
    components: {
      addOrg,
      updateOrg,
      addUser,
      updateUser
    },
    data() {
      return {
        addOrgButtonShow: false,
        updateOrgButtonShow: false,
        deleteOrgButtonShow: false,
        addUserButtonShow: false,
        updateUserButtonShow: false,
        deleteUserButtonShow: false,
        search: '',
        columns: this.getUserColumns(),
        userData: [],
        key: '',
        order: '',
        total: 0,
        current: 1,
        pageSize: 10,
        orgDate: [],
        hasChildren: false,
        parentOrgId: 1,
        orgId: 0,
        parentOrgName: this.getParentOrgName(),
        fullPath: '',
        editIndex: -1,
        addOrgShow: false,
        updateOrgShow: false,
        addUserShow: false,
        updateUserShow: false,
        userId: '',
        tableHeight: 200
      }
    },
    methods: {
      operateOrgUpdate(obj, c) {
        if (c == undefined) {
          c = this.orgDate[0];
        }
        if (c.orgId == this.orgId) {
          c.title = obj.orgName;
          c.orgCode = obj.orgCode;
        }
        let childrenLength = c.children.length;
        for (let i = 0; i < childrenLength; i++) {
          c.children[i] = this.operateOrgUpdate(obj, c.children[i]);
        }
        return c;
      },
      operateOrgDelete(c) {
        let _this = this;
        let r = -1;
        if (c.orgId == this.parentOrgId) {
          for (let j = 0; j < c.children.length; j++) {
            if (c.children[j].orgId == _this.orgId) {
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
            c.children[i] = this.operateOrgDelete(c.children[i]);
          }
          if (c.children[i].orgId == this.parentOrgId) {
            for (let j = 0; j < c.children[i].children.length; j++) {
              if (c.children[i].children[j].orgId == _this.orgId) {
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
      operateOrgAdd(obj, c) {
        if (c == undefined) {
          c = this.orgDate[0];
          if (c.orgId == obj.parentOrgId) {
            let o = new Object();
            o.title = obj.orgName;
            o.orgCode = obj.orgCode;
            o.orgId = obj.orgId;
            o.parentOrgId = obj.parentOrgId;
            o.parentOrgName = obj.parentOrgName;
            o.fullPath = obj.fullPath;
            o.children = [];
            o.expand = true;
            c.children.push(o)
          }
        }
        let childrenLength = c.children.length;
        for (let i = 0; i < childrenLength; i++) {
          if (c.children[i].children.length > 0) {
            c.children[i] = this.operateOrgAdd(obj, c.children[i]);
          }
          if (c.children[i].orgId == obj.parentOrgId) {
            let o = new Object();
            o.title = obj.orgName;
            o.orgCode = obj.orgCode;
            o.orgId = obj.orgId;
            o.parentOrgId = obj.parentOrgId;
            o.parentOrgName = obj.parentOrgName;
            o.fullPath = obj.fullPath;
            o.children = [];
            o.expand = true;
            c.children[i].children.push(o)
          }
        }
        return c;
      },
      handleDeleteUser(row) {
        this.$Modal.confirm({
          title: this.$t('modal.title'),
          content: '<p>' + this.$t('user.deleteUserContent') + '</p>',
          onOk: () => {
            deleteUser({userId: row.userId}).then(res => {
              if (res.code == 200) {
                this.$Message.success(res.msg);
                this.handleSearch();
              } else {
                this.$Message.warning(res.msg);
              }
            });
          },
          onCancel: () => {
            this.$Message.info(this.$t('user.deleteCancel'));
          }
        });
      },
      handleEditUser(row, index) {
        this.userId = row.userId;
        this.updateUserShow = true;
      },
      handleSearch() {
        let current = this.current
        let pageSize = this.pageSize
        let search = this.search
        let orderKey = this.key
        let orderByValue = this.order
        let fullPath = this.fullPath
        const _this = this;
        queryUserList({
          current,
          pageSize,
          search,
          orderKey,
          orderByValue,
          fullPath
        }).then(res => {
          if (res.code == 200) {
            this.$Message.success(this.$t('user.searchSuccessText'))
            _this.total = res.obj.total
            _this.userData = res.obj.rows
          } else {
            this.$Message.error(this.$t('user.searchFailText') + ',' + res.msg)
          }
        })
      },
      handleUserAdd() {
        this.addUserShow = true
      },
      handleAdd() {
        this.addOrgShow = true
      },
      handleUpdate() {
        if (this.parentOrgId == 0) {
          this.$Message.warning('请选择需要修改的组织架构节点');
          return;
        }
        this.updateOrgShow = true
      },
      handleDelete() {
        if (this.parentOrgId == 0) {
          this.$Message.warning('请选择需要删除的组织架构节点');
          return;
        }
        this.$Modal.confirm({
          title: this.$t('modal.title'),
          content: '<p>' + this.$t('org.deleteOrgContent') + '</p>',
          onOk: () => {
            deleteOrg({orgId: this.orgId}).then(res => {
              if (res.code == 200) {
                this.$Message.success(res.msg);
                // 删除数据成功动态才做菜单树
                this.operateOrgDelete(this.orgDate[0]);
                this.parentOrgId = 0;
                this.parentOrgName = '虚拟顶级组织';
              } else {
                this.$Message.warning(res.msg);
              }
            });
          },
          onCancel: () => {
            this.$Message.info(this.$t('org.deleteCancel'));
          }
        });
      },
      initTree() {
        const _this = this;
        getOrgTree({}).then(res => {
          if (res.code == 200) {
            _this.orgDate = res.obj;
          } else {
            this.$Message.error("加载组织架构菜单节点失败，请与管理员联系！")
          }
        });
      },
      onSelectChange(data) {
        // 如果长度为0说明当前没有任何节点被选中
        if (data.length == 0) {
          this.parentOrgId = 1;
          this.orgId = 0;
          this.parentOrgName = '虚拟顶级组织';
          this.hasChildren = false;
          this.fullPath = '';
        } else {
          this.parentOrgId = data[0].parentOrgId;
          this.orgId = data[0].orgId;
          this.parentOrgName = data[0].title;
          this.fullPath = data[0].fullPath;
          if (data[0].children.length == 0) {
            this.hasChildren = false;
          } else {
            this.hasChildren = true;
          }
        }
        this.handleSearch();
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
      changePage(current) {
        this.current = current;
        this.handleSearch();
      },
      changePageSize(pageSize) {// 改变每页记录的条数
        this.pageSize = pageSize;
        this.handleSearch();
      },
      getUserColumns() {
        return [
          {
            title: this.$t('user.columns.loginAccountTitle'),
            key: 'loginAccount',
            sortable: true
          },
          {
            title: this.$t('user.columns.nickNameTitle'),
            key: 'nickName',
            sortable: true
          },
          {
            title: this.$t('user.columns.crtDateTitle'),
            key: 'crtDate',
            sortable: true,
            render: (h, params) => {
              return h('div',
                this.formatDate(new Date(params.row.crtDate), 'yyyy/MM/dd hh:mm:ss')
              )
            }
          },
          {
            title: this.$t('user.columns.lastLoginDateTitle'),
            key: 'lastLoginDate',
            sortable: true,
            render: (h, params) => {
              if (params.row.lastLoginDate != null) {
                return h('div',
                  this.formatDate(new Date(params.row.lastLoginDate), 'yyyy/MM/dd hh:mm:ss')
                )
              } else {
                return h('div', '')
              }

            }
          },
          {
            title: this.$t('user.columns.operationTitle'),
            slot: 'action'
          }
        ]
      },
      getParentOrgName() {
        return this.$t('user.parentOrgName')
      },
      lazy() {
        let _self = this
        setTimeout(function () {
          _self.columns = _self.getUserColumns()
          _self.parentOrgName = _self.getParentOrgName()
        }, 200)
      },
      loadButtonAuth() { // 加载菜单按钮权限
        this.addOrgButtonShow = this.$checkButoonAuth('system-manage-org-add');
        this.updateOrgButtonShow = this.$checkButoonAuth('system-manage-org-update');
        this.deleteOrgButtonShow = this.$checkButoonAuth('system-manage-org-delete');
        this.addUserButtonShow = this.$checkButoonAuth('system-manage-user-add');
        this.updateUserButtonShow = this.$checkButoonAuth('system-manage-user-update');
        this.deleteUserButtonShow = this.$checkButoonAuth('system-manage-user-delete');
      }
    },
    mounted() {
      // 初始化菜单权限
      this.loadButtonAuth();
      // 初始化完成以后加载菜单数据
      this.initTree();
      this.handleSearch();
      this.tableHeight = window.innerHeight - this.$refs.userTable.$el.offsetTop - 335
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
      parentOrgId() {
        this.handleSearch();
      }
    }
  }

</script>
