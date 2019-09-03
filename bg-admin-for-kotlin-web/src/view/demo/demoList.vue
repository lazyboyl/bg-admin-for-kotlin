<template>
  <div>
    <Card :title="$t('demo.title')">
      <div>
        <div style="display:inline-block;float:left;">
          <Button type="success" @click="addTest">+{{$t('demo.addButton')}}</Button>
        </div>
        <div style="display:inline-block;float:right;">
          <!-- 若需要点击查询的图片触发查询事件需要增加 :search=true 具体查看API文档 -->
          <Input  v-model="queryString" suffix="ios-search" placeholder="Enter text" style="width: auto" :search=true @on-search="handleSearch" @on-enter="handleSearch" />
          <DatePicker v-model="startTime" type="datetime" format="yyyy-MM-dd"  placeholder="请选择时间" style="width: 200px"></DatePicker>
          <Select v-model="selectType" style="width:200px">
            <Option value="1" key="11">111</Option>
            <Option value="2" key="22">222</Option>
            <Option value="3" key="33">333</Option>
          </Select>
        </div>
      </div>
      <div style="clear: both;"></div>
      <div style="margin-top: 10px;">
        <Table :columns="columns" :data="data">
          <template slot-scope="{ row, index }" slot="name">
            <Input type="text" v-model="editName" v-if="editIndex === index" />
            <span v-else>{{ row.name }}</span>
          </template>

          <template slot-scope="{ row, index }" slot="age">
            <Input type="text" v-model="editAge" v-if="editIndex === index" />
            <span v-else>{{ row.age }}</span>
          </template>

          <template slot-scope="{ row, index }" slot="birthday">
            <Input type="text" v-model="editBirthday" v-if="editIndex === index" />
            <span v-else>{{ getBirthday(row.birthday) }}</span>
          </template>

          <template slot-scope="{ row, index }" slot="address">
            <Input type="text" v-model="editAddress" v-if="editIndex === index" />
            <span v-else>{{ row.address }}</span>
          </template>

          <template slot-scope="{ row, index }" slot="action">
            <div v-if="editIndex === index">
              <Button @click="handleSave(index)">保存</Button>
              <Button @click="editIndex = -1">取消</Button>
            </div>
            <div v-else>
              <Button @click="handleEdit(row, index)" size="small">修改</Button>
              <Button @click="handleDelete(row, index)" size="small">删除</Button>
              <Button @click="handleShow(row, index)" size="small">查看</Button>
            </div>
          </template>
        </Table>
      </div>
      <div style="margin-top: 10px;">
        <Page :total=50 :current=3 show-sizer />
      </div>
    </Card>
    <addDemo v-model="addShow" v-on:handleSearch="handleSearch"></addDemo>
  </div>
</template>
<script>
  import addDemo from './addDemo'

  export default {
    components:{
      addDemo
    },
    data () {
      return {
        addShow:false,
        queryString:'',
        startTime:'',
        selectType:'',
        columns: [
          {
            title: '姓名',
            slot: 'name'
          },
          {
            title: '年龄',
            slot: 'age'
          },
          {
            title: '出生日期',
            slot: 'birthday'
          },
          {
            title: '地址',
            slot: 'address'
          },
          {
            title: '操作',
            slot: 'action'
          }
        ],
        data: [
          {
            name: '王小明',
            age: 18,
            birthday: '919526400000',
            address: '北京市朝阳区芍药居'
          },
          {
            name: '张小刚',
            age: 25,
            birthday: '696096000000',
            address: '北京市海淀区西二旗'
          },
          {
            name: '李小红',
            age: 30,
            birthday: '563472000000',
            address: '上海市浦东新区世纪大道'
          },
          {
            name: '周小伟',
            age: 26,
            birthday: '687024000000',
            address: '深圳市南山区深南大道'
          }
        ],
        editIndex: -1,  // 当前聚焦的输入框的行数
        editName: '',  // 第一列输入框，当然聚焦的输入框的输入内容，与 data 分离避免重构的闪烁
        editAge: '',  // 第二列输入框
        editBirthday: '',  // 第三列输入框
        editAddress: '',  // 第四列输入框
      }
    },
    methods: {
      handleShow(row, inde){
        console.log('获取的查看的数据是：'+row.name)
      },
      handleDelete(row, index){
        console.log('获取的删除的数据是：'+row.name)
      },
      handleSearch(){
        console.log('调用成功，刷新列表页'+this.queryString+'--'+this.startTime+'---'+this.selectType)
      },
      addTest(){
        this.addShow = true
      },
      handleEdit (row, index) {
        this.editName = row.name;
        this.editAge = row.age;
        this.editAddress = row.address;
        this.editBirthday = row.birthday;
        this.editIndex = index;
      },
      handleSave (index) {
        this.data[index].name = this.editName;
        this.data[index].age = this.editAge;
        this.data[index].birthday = this.editBirthday;
        this.data[index].address = this.editAddress;
        this.editIndex = -1;
      },
      getBirthday (birthday) {
        const date = new Date(parseInt(birthday));
        const year = date.getFullYear();
        const month = date.getMonth() + 1;
        const day = date.getDate();
        return `${year}-${month}-${day}`;
      }
    },
    watch:{
      startTime(val){
        console.log("查询时间的变化值是："+val)
        this.handleSearch();
      },
      selectType(val){
        console.log("查询selectType的变化值是："+val)
        this.handleSearch();
      }
    }
  }
</script>
