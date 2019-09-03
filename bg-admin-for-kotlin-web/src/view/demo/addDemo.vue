<template>
  <Modal v-model="show" title="增加" @on-ok="ok" :loading="loading" :mask-closable="false">
    <Form ref="demoForm" :model="demoForm" :rules="demoFormRule">
      <Row>
        <Col span="24">
          <FormItem label="类型" prop="type">
            <Input type="text" :maxlength=10 v-model="demoForm.type" placeholder="请输入类型" />
          </FormItem>
        </Col>
      </Row>
      <Row>
        <Col span="10">
          <FormItem label="类型1" prop="type1">
            <Input type="text" :maxlength=50 v-model="demoForm.type1" placeholder="请输入类型1"/>
          </FormItem>
        </Col>
        <Col span="4" style="text-align: center">&nbsp;&nbsp;</Col>
        <Col span="10">
          <FormItem label="类型2" prop="type2">
            <Input type="text" :maxlength=50 v-model="demoForm.type2" placeholder="请输入类型2"/>
          </FormItem>
        </Col>
      </Row>
      <Row >
        <Col span="24">
          <FormItem label="类型2" prop="type3">
            <CheckboxGroup v-model="demoForm.type3">
              <Checkbox label="twitter">
                <Icon type="logo-twitter"></Icon>
                <span>Twitter</span>
              </Checkbox>
              <Checkbox label="facebook">
                <Icon type="logo-facebook"></Icon>
                <span>Facebook</span>
              </Checkbox>
              <Checkbox label="github">
                <Icon type="logo-github"></Icon>
                <span>Github</span>
              </Checkbox>
              <Checkbox label="snapchat">
                <Icon type="logo-snapchat"></Icon>
                <span>Snapchat</span>
              </Checkbox>
            </CheckboxGroup>
          </FormItem>
        </Col>
      </Row>
      <Row style="margin-top: 20px;">
        <Col span="24">
          <RadioGroup v-model="demoForm.type4" type="button">
            <Radio label="4" >
              <span>北京</span>
            </Radio>
            <Radio label="3">
              <span>上海</span>
            </Radio>
            <Radio label="2">
              <span>深圳</span>
            </Radio>
            <Radio label="1">
              <span>杭州</span>
            </Radio>
          </RadioGroup>
        </Col>
      </Row>
      <Row style="margin-top: 20px;">
        <Col span="24">
          <FormItem label="类型6" prop="type5">
            <Select v-model="demoForm.type5" style="width:200px">
              <Option v-for="item in cityList" :value="item.value" :key="item.value">{{ item.label }}</Option>
            </Select>
          </FormItem>
        </Col>
      </Row>
      <Row style="margin-top: 20px;">
        <Col span="24">
          <FormItem label="类型6" prop="type6">
            <DatePicker v-model="demoForm.type6" type="datetime" format="yyyy-MM-dd"  placeholder="请选择时间" style="width: 200px"></DatePicker>
          </FormItem>
        </Col>
      </Row>
      <Row style="margin-top: 20px;">
        <Col span="24">
          <FormItem label="类型6" prop="type7">
            <TimePicker v-model="demoForm.type7" type="time" placeholder="选择时间" style="width: 168px"></TimePicker>
          </FormItem>
        </Col>
      </Row>
    </Form>
  </Modal>
</template>
<script>
  export default {
    name: "addTest",
    props: {
      value: {
        type: Boolean,
        default: false
      }
    },
    data() {
      return {
        cityList: [
          {
            value: 'New York',
            label: 'New York'
          },
          {
            value: 'London',
            label: 'London'
          },
          {
            value: 'Sydney',
            label: 'Sydney'
          },
          {
            value: 'Ottawa',
            label: 'Ottawa'
          },
          {
            value: 'Paris',
            label: 'Paris'
          },
          {
            value: 'Canberra',
            label: 'Canberra'
          }
        ],
        demoForm: {
          type: '',
          type1:'',
          type2:'',
          type3:[],
          type4:'2',
          type5:'',
          type6:'',
          type7:''
        },
        demoFormRule: {
          type: [
            {required: true, message: '请输入类型', trigger: 'blur'},
            {type: 'string', max: 10, message: '类型允许输入最大长度为10个字符', trigger: 'blur'}
          ],
          type3: [
            {required: true, message: '请输入类型', trigger: 'change',type:"array"}
          ],
          type5: [
            {required: true, message: '请输入类型', trigger: 'change',type:"string"}
          ],
          type6 : [
            // 时间验证参考该网站：https://blog.csdn.net/weixin_41849462/article/details/86630305
            {required: true, message: '日期不能为空!', trigger: 'change', pattern: /.+/}
          ],
          type7 : [{required: true, message: '日期不能为空!', trigger: 'change', pattern: /.+/}]
        },
        show: this.value,
        loading: true
      }
    },
    watch: {
      value(val) {
        this.show = val;
      },
      show(val) {
        //当重新显示增加数据的时候重置整个form表单
        if(val){
          this.$refs['demoForm'].resetFields();
        }else{// 反之则关闭页面
          this.closeModal(val);
        }
      }
    },
    methods: {
      ok() {
        this.$refs['demoForm'].validate((valid) => {
          if (valid) {
            // 模拟增加数据成功
            this.$Message.success('增加数据成功');
            // 提交表单数据成功则关闭当前的modal框
            this.closeModal(false);
            // 同时调用父页面的刷新页面的方法
            this.$emit('handleSearch');
          } else {
            this.$Message.error('表单验证失败，请检查表单!');
          }
          setTimeout(() => {
            this.loading = false;
            this.$nextTick(() => {
              this.loading = true;
            });
          },1000);
        });
      },
      closeModal(val) {
        this.$emit('input', val);
      }
    }
  }
</script>
