<template>
  <div>
    <Card :title="$t('role.title')">
      <div>
        <div style="display:inline-block;float:left;">
          <Button type="success" v-show="addRoleButtonShow" @click="addRole">+{{$t('role.addButton')}}</Button>
        </div>
        <div style="display:inline-block;float:right;">
          <Input v-model="search" suffix="ios-search" :placeholder="$t('role.searchText')" style="width: auto"
                 :search=true @on-search="handleSearch"/>
        </div>
      </div>
      <div style="clear: both;"></div>
      <div style="margin-top: 10px;">
        <Table ref="roleTable" @on-sort-change="onSortChange" :columns="columns" :data="roleData" :height="tableHeight">
          <template slot-scope="{ row, index }" slot="roleName">
            <Input type="text" v-model="editRoleName" v-if="editIndex === index"/>
            <span v-else>{{ row.roleName }}</span>
          </template>

          <template slot-scope="{ row, index }" slot="roleCode">
            <Input type="text" v-model="editRoleCode" v-if="editIndex === index"/>
            <span v-else>{{ row.roleCode }}</span>
          </template>

          <template slot-scope="{ row, index }" slot="action">
            <div v-if="editIndex === index">
              <Button size="small" type="success" @click="handleUpdate(index)">{{$t('button.save')}}</Button>
              <Button size="small" type="error" @click="editIndex = -1">{{$t('button.cancel')}}</Button>
            </div>
            <div v-else>
              <Button type="success" v-show="editRoleButtonShow" @click="handleEdit(row, index)" size="small">
                {{$t('button.edit')}}
              </Button>
              <Button type="primary" v-show="editRoleButtonShow" @click="handleModify(row)" size="small">
                {{$t('button.modify')}}
              </Button>
              <Button type="error" v-show="deleteRoleButtonShow" @click="handleDelete(row, index)" size="small">
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
    <addRole v-model="addShow" v-on:handleSearch="handleSearch"></addRole>
    <editRole v-model="editShow" :roleId="roleId" v-on:handleSearch="handleSearch"></editRole>
  </div>
</template>
<script>
  import {queryRoleList, updateRole, deleteRole} from '../../../api/sys/role/role.api'
  import addRole from './addRole'
  import editRole from './editRole'

  export default {
    components: {
      addRole,
      editRole
    },
    data() {
      return {
        addRoleButtonShow: false,
        editRoleButtonShow: false,
        deleteRoleButtonShow: false,
        addShow: false,
        editShow: false,
        roleId: '',
        search: '',
        roleData: [],
        columns: this.getRoleColumns(),
        total: 0,
        current: 1,
        pageSize: 10,
        key: 'crtDate',
        order: 'desc',
        editIndex: -1,  // 当前聚焦的输入框的行数
        editRoleId: '',
        editRoleName: '',
        editRoleCode: '',
        tableHeight: 200
      }
    },
    methods: {
      addRole() {
        this.addShow = true
      },
      changePage(current) {
        this.current = current;
        this.handleSearch();
      },
      changePageSize(pageSize) {// 改变每页记录的条数
        this.pageSize = pageSize;
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
      handleDelete(row, index) {
        this.$Modal.confirm({
          title: this.$t('modal.title'),
          content: '<p>' + this.$t('role.deleteContent') + '</p>',
          onOk: () => {
            deleteRole({roleId: row.roleId}).then(res => {
              if (res.code == 200) {
                this.$Message.success(this.$t('role.deleteRoleSuccess'));
                // 删除数据成功同时刷新grid
                this.handleSearch();
              } else {
                this.$Message.warning(this.$t('role.deleteRoleFail'));
              }
            });
          },
          onCancel: () => {
            this.$Message.info(this.$t('dict.deleteCancel'));
          }
        });
      },
      handleUpdate(index) {
        updateRole({roleId: this.editRoleId, roleName: this.editRoleName, roleCode: this.editRoleCode}).then(res => {
          if (res.code == 200) {
            this.$Message.success(this.$t('role.updateRoleSuccess'))
            this.editIndex = -1
            this.handleSearch()
          } else {
            this.$Message.error(this.$t('role.updateRoleFail') + ',' + res.msg)
          }
        });
      },
      handleModify(row) {
        this.roleId = row.roleId
        this.editShow = true
      },
      handleEdit(row, index) {
        this.editRoleName = row.roleName;
        this.editRoleCode = row.roleCode;
        this.editRoleId = row.roleId
        this.editIndex = index
      },
      handleSearch() {
        let current = this.current
        let pageSize = this.pageSize
        let search = this.search
        let orderKey = this.key
        let orderByValue = this.order
        const _this = this;
        queryRoleList({
          current,
          pageSize,
          search,
          orderKey,
          orderByValue
        }).then(res => {
          if (res.code == 200) {
            this.$Message.success(this.$t('role.searchSuccessText'))
            _this.total = res.obj.total
            _this.roleData = res.obj.rows
          } else {
            this.$Message.error(this.$t('role.searchFailText') + ',' + res.msg)
          }
        });
      },
      getRoleColumns() {
        return [
          {
            title: this.$t('role.columns.roleNameTitle'),
            slot: 'roleName',
            key: 'roleName',
            sortable: true
          },
          {
            title: this.$t('role.columns.roleCodeTitle'),
            slot: 'roleCode',
            key: 'roleCode',
            sortable: true
          },
          {
            title: this.$t('role.columns.roleCrtDateTitle'),
            key: 'crtDate',
            sortable: true,
            render: (h, params) => {
              return h('div',
                this.formatDate(new Date(params.row.crtDate), 'yyyy/MM/dd hh:mm:ss')
              )
            }
          },
          {
            title: this.$t('role.columns.operationTitle'),
            slot: 'action'
          }
        ]
      },
      lazy() {
        let _self = this
        setTimeout(function () {
          _self.columns = _self.getRoleColumns()
        }, 200)
      },
      loadButtonAuth() { // 加载菜单按钮权限
        this.addRoleButtonShow = this.$checkButoonAuth('system-manage-role-add');
        this.deleteRoleButtonShow = this.$checkButoonAuth('system-manage-role-delete');
        this.editRoleButtonShow = this.$checkButoonAuth('system-manage-role-update');
      }
    },
    mounted() {
      // 初始化菜单权限
      this.loadButtonAuth();
      // 初始化完成组件的时候执行以下的逻辑
      this.handleSearch();
      this.tableHeight = window.innerHeight - this.$refs.roleTable.$el.offsetTop - 270
    },
    computed: {
      userLang() {
        return this.$store.getters.userLang;
      }
    },
    watch: {
      userLang() {
        this.lazy()
      }
    }
  }
</script>
