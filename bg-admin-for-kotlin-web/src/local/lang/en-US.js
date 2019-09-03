import enUsDict from "./sys/dict/en-US-dict";
import enUsRole from "./sys/role/en-US-role";
import enUsTree from "./sys/tree/en-US-tree";
import enUsUser from "./sys/user/en-US-user";
import enUsOrg from "./sys/org/en-US-org";

export default {
  dict: enUsDict.dict,
  role: enUsRole.role,
  tree: enUsTree.tree,
  user: enUsUser.user,
  org: enUsOrg.org,
  login: {
    loginAccount: 'please enter login',
    loginPassword: 'please enter password'
  },
  demo: {
    title: 'my application',
    addButton: 'create application'
  },
  modal: {
    title: 'tips'
  },
  button: {
    modify: 'modify',
    edit:'edit',
    delete: 'delete',
    show: 'show',
    save: 'save',
    cancel: 'cancel'
  },
  status: {
    available: 'available',
    disabled: 'disabled'
  },
  actions:{
    freeze:'freeze',
    thaw:'thaw'
  }
}
