<template>
  <div>
    <Card :title="$t('dict.title')">
      <div>
        <div style="display:inline-block;float:left;">
          <Button type="success" v-show="addButtonShow" @click="addDict">+{{$t('dict.addButton')}} </Button>
        </div>
        <div style="display:inline-block;float:right;">
          <Input v-model="search" suffix="ios-search" :placeholder="$t('dict.searchText')" style="width: auto"
                 :search=true @on-search="handleSearch"/>
          <Poptip
            placement="bottom-end"
            :transfer=true
            ref="queryDictPop"
          >
            <Icon type="md-more"></Icon>
            <div slot="content">
              <div style="display: block;">
                <Form ref="queryDictForm" :model="queryDictForm">
                  <FormItem prop="dictCode">
                    <Input v-model="queryDictForm.dictCode" placeholder="请输入需要查询的字典编码" style="width:180px;"/>
                  </FormItem>
                </Form>
                <div style="margin-top: 10px;text-align:center;">
                  <Button type="info" @click="handleSearch">查询</Button>
                  &nbsp;&nbsp;&nbsp;&nbsp;
                  <Button type="info" @click="clearQueryDictForm">清空</Button>
                </div>
              </div>
            </div>
          </Poptip>
        </div>
      </div>
      <div style="clear: both;"></div>
      <div style="margin-top: 10px;">
        <Table ref="dictTable" @on-sort-change="onSortChange" :columns="columns" :data="dictData" :height="tableHeight">
          <template slot-scope="{ row, index }" slot="dictCode">
            <Input type="text" v-model="editDictCode" v-if="editIndex === index"/>
            <span v-else>{{ row.dictCode }}</span>
          </template>

          <template slot-scope="{ row, index }" slot="dictText">
            <Input type="text" v-model="editDictText" v-if="editIndex === index"/>
            <span v-else>{{ row.dictText }}</span>
          </template>

          <template slot-scope="{ row, index }" slot="dictValue">
            <Input type="text" v-model="editDictValue" v-if="editIndex === index"/>
            <span v-else>{{ row.dictValue}}</span>
          </template>

          <template slot-scope="{ row, index }" slot="action">
            <div v-if="editIndex === index">
              <Button type="success" @click="handleUpdate(index)">{{$t('button.save')}}</Button>
              <Button type="error" @click="editIndex = -1">{{$t('button.cancel')}}</Button>
            </div>
            <div v-else>
              <Button v-show="editButtonShow" type="primary" @click="handleEdit(row, index)" size="small">{{$t('button.modify')}}</Button>
              <Button v-show="deleteButtonShow" type="error" @click="handleDelete(row, index)" size="small">{{$t('button.delete')}}</Button>
            </div>
          </template>
        </Table>
      </div>
      <div style="margin-top: 10px;">
        <Page show-elevator show-sizer show-total :total="total" :current="current"
              :page-size="pageSize" @on-change="changePage" @on-page-size-change="changePageSize"/>
      </div>
    </Card>
    <addDict v-model="addShow" v-on:handleSearch="handleSearch"></addDict>
  </div>
</template>
<script>
  import {deleteDict, updateDict, queryDictList} from '../../../api/sys/dict/dict.api'
  import addDict from './addDict'

  export default {
    components: {
      addDict
    },
    data() {
      return {
        addButtonShow: false,
        editButtonShow: false,
        deleteButtonShow: false,
        search: '',
        dictData: [],
        columns: this.getDictColumns(),
        key: 'dictType',
        order: 'desc',
        editIndex: -1,  // 当前聚焦的输入框的行数
        editId: '',
        editDictCode: '',
        editDictText: '',
        editDictValue: '',
        total: 0,
        current: 1,
        pageSize: 10,
        addShow: false,
        tableHeight: 200,
        queryDictForm: {
          dictCode: ''
        }
      }
    },
    methods: {
      clearQueryDictForm(){
        this.$refs['queryDictPop'].$slots.content[0].context.$refs['queryDictForm'].resetFields()
      },
      addDict() {
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
        console.log(sort.key + '-' + sort.order)
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
          content: '<p>' + this.$t('dict.deleteContent') + '</p>',
          onOk: () => {
            deleteDict({id: row.id}).then(res => {
              if (res.code == 200) {
                this.$Message.success(this.$t('dict.deleteDictSuccess'));
                // 删除数据成功同时刷新grid
                this.handleSearch();
              } else {
                this.$Message.warning(this.$t('dict.deleteDictFail') + '：' + res.msg);
              }
            });
          },
          onCancel: () => {
            this.$Message.info(this.$t('dict.deleteCancel'));
          }
        });
      },
      handleEdit(row, index) {
        this.editDictCode = row.dictCode
        this.editDictText = row.dictText
        this.editDictValue = row.dictValue
        this.editId = row.id
        this.editIndex = index
      },
      handleUpdate(index) {
        updateDict({
          id: this.editId,
          dictValue: this.editDictValue,
          dictText: this.editDictText,
          dictCode: this.editDictCode
        }).then(res => {
          if (res.code == 200) {
            this.$Message.success(this.$t('dict.updateDictSuccess'))
            this.editIndex = -1
            this.handleSearch()
          } else {
            this.$Message.error(this.$t('dict.updateDictFail') + ',' + res.msg)
          }
        });
      },
      handleSearch() {
        let current = this.current
        let pageSize = this.pageSize
        let search = this.search
        let orderKey = this.key
        let orderByValue = this.order
        let dictCode = this.queryDictForm.dictCode
        const _this = this;
        queryDictList({
          current,
          pageSize,
          search,
          orderKey,
          orderByValue,
          dictCode
        }).then(res => {
          if (res.code == 200) {
            this.$Message.success(this.$t('dict.searchSuccessText'))
            _this.total = res.obj.total
            _this.dictData = res.obj.rows
          } else {
            this.$Message.error(this.$t('dict.searchFailText') + ',' + res.msg)
          }
        });
      },
      getDictColumns() {
        return [
          {
            title: this.$t('dict.columns.dictTypeTitle'),
            key: 'dictType',
            sortable: true
          },
          {
            title: this.$t('dict.columns.dictCodeTitle'),
            slot: 'dictCode',
            key: 'dictCode',
            sortable: true
          },
          {
            title: this.$t('dict.columns.dictTextTitle'),
            slot: 'dictText',
            key: 'dictText',
            sortable: true
          },
          {
            title: this.$t('dict.columns.dictValueTitle'),
            slot: 'dictValue',
            key: 'dictValue',
            sortable: true
          },
          {
            title: this.$t('dict.columns.operationTitle'),
            slot: 'action'
          }
        ]
      },
      lazy() {
        let _self = this
        setTimeout(function () {
          _self.columns = _self.getDictColumns()
        }, 200)
      },
      loadButtonAuth() { // 加载菜单按钮权限
        this.addButtonShow = this.$checkButoonAuth('system-manage-dict-add');
        this.deleteButtonShow = this.$checkButoonAuth('system-manage-dict-delete');
        this.editButtonShow = this.$checkButoonAuth('system-manage-dict-update');
        console.log(this.$store.getters.access)
      }
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
    },
    mounted() {
      // 初始化菜单权限
      this.loadButtonAuth();
      // 初始化完成组件的时候执行以下的逻辑
      this.handleSearch();
      this.tableHeight = window.innerHeight - this.$refs.dictTable.$el.offsetTop - 270
    }
  }
</script>
