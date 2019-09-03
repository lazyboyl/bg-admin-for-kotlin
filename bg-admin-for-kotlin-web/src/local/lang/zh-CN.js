import zhCnDict from './sys/dict/zh-CN-dict'
import zhCnRole from './sys/role/zh-CN-role'
import zhCnTree from './sys/tree/zh-CN-tree'
import zhCnUser from "./sys/user/zh-CN-user";
import zhCnOrg from "./sys/org/zh-CN-org";

export default {
  dict: zhCnDict.dict,
  role: zhCnRole.role,
  tree: zhCnTree.tree,
  user: zhCnUser.user,
  org: zhCnOrg.org,
  login: {
    loginAccount: '请输入登录账号',
    loginPassword: '请输入登录的密码'
  },
  demo: {
    title: '我的应用',
    addButton: '创建应用'
  },
  modal: {
    title: '提示'
  },
  button: {
    modify: '修改',
    edit: '编辑',
    delete: '删除',
    show: '查看',
    save: '保存',
    cancel: '取消'
  },
  status: {
    available: '可用',
    disabled: '禁用'
  },
  actions: {
    freeze: '冻结',
    thaw: '解冻'
  }
}
