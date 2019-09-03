<template>
	<div>
		<Card title="语言维护">
			<div>
				<div style="display:inline-block;float:left;">
					<Button type="success" @click="addLanguage">+创建</Button>
				</div>
				<div style="display:inline-block;float:right;">
					<Input v-model="search" suffix="ios-search" placeholder="请输入需要查询的信息" style="width: auto" :search=true @on-search="handleSearch"/>
				</div>
			</div>
			<div style="clear: both;"></div>
			<div style="margin-top: 10px;">
				<Table ref="languageTable" @on-sort-change="onSortChange" :columns="columns" :data="languageData" :height="tableHeight">
					<template slot-scope="{ row, index }" slot="action">
						<Button type="primary" @click="handleEdit(row, index)" size="small">修改</Button>
						<Button type="error" @click="handleDelete(row, index)" size="small">删除</Button>
					</template>
				</Table>
			</div>
			<div style="margin-top: 10px;">
				<Page show-elevator show-sizer show-total :total="total" :current="current" :page-size="pageSize" @on-change="changePage" @on-page-size-change="changePageSize"/>
			</div>
		</Card>
		<addLanguage v-model="addShow" v-on:handleSearch="handleSearch"></addLanguage>
		<updateLanguage v-model="updateShow" :id="id" v-on:handleSearch="handleSearch"></updateLanguage>
	</div>
</template>
<script>
	import {deleteLanguage, queryLanguageList} from '../../../api/sys/language/language.api'
	import addLanguage from './addLanguage'
	import updateLanguage from './updateLanguage'
	export default {
		components: {
			addLanguage,
			updateLanguage
		},
		data() {
			return {
				search: '',
				languageData: [],
				columns: this.getLanguageColumns(),
				key: '',
				order: '',
				total: 0,
				current: 1,
				pageSize: 10,
				addShow: false,
				id: 0,
				updateShow: false,
				tableHeight: 200
			}
		},
		 methods: {
			addLanguage() {
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
			handleEdit(row, index) {
				this.id=row.id;
				this.updateShow = true;
			},
			handleDelete(row, index) {
				this.$Modal.confirm({					title: '提示',
					content: '<p>是否删除当前数据？</p>',
					onOk: () => {
						deleteLanguage({id: row.id}).then(res => {
							if (res.code == 200) {
								this.handleSearch();
							}
							this.$Message.success(res.msg);
						});
					},
					onCancel: () => {
						this.$Message.info('取消了当前的操作行为！');
					}
				});
			 },
			handleSearch() {
				let current = this.current
				let pageSize = this.pageSize
				let search = this.search
				let orderKey = this.key
				let orderByValue = this.order
				const _this = this;
				queryLanguageList({
					current,
					pageSize,
					search,
					orderKey,
					orderByValue
				}).then(res => {
					if (res.code == 200) {
						_this.total = res.obj.total
						_this.languageData = res.obj.rows
					}
					this.$Message.success(res.msg)
				});
			},
			getLanguageColumns() {
				return [
					{
						title: '语言类型名称',
						key: 'languageName',
						sortable: true
					},
					{
						title: '语言类型编码',
						key: 'languageCode',
						sortable: true
					},
					{
						title: '使用状态',
						key: 'state',
						sortable: true,
            render: (h, params) => {
						  if(params.row.state == '1'){
                return h('div',
                  '使用中'
                )
              }else{
                return h('div',
                  '已停用'
                )
              }
            }
					},
					{
						title:'操作',
						slot: 'action'
					}
				]
			},
		},
		mounted() {
			this.handleSearch();
			this.tableHeight = window.innerHeight - this.$refs.languageTable.$el.offsetTop - 270
		}
	}
</script>
