import zhTwDict from "./sys/dict/zh-TW-dict";
import zhTwRole from "./sys/role/zh-TW-role";
import zhTwTree from "./sys/tree/zh-TW-tree";
import zhTWUser from "./sys/user/zh-TW-user";
import zhTwOrg from "./sys/org/zh-TW-org";

export default {
  dict: zhTwDict.dict,
  role: zhTwRole.role,
  tree: zhTwTree.tree,
  user: zhTWUser.user,
  org: zhTwOrg.org,
  login: {
    loginAccount: '请输入登录账号（TW）',
    loginPassword: '请输入登录密码（TW）'
  },
  demo: {
    title: '我的例子(TW)',
    addButton: '创建应用(TW)'
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
