import Vue from 'vue'
import App from './App'
import {router} from './router/index'
import iView from 'iview'
import i18n from './local'
import 'iview/dist/styles/iview.css'
import store from './store';
import runConfig from './config/run.config';
import JsEncrypt from 'jsencrypt';
import iviewArea from 'iview-area';
import {initInternationalization} from './api/sys/internationalization/internationalization.api';


/**
 * 生成唯一的uuid
 * @returns {string}
 */
Vue.prototype.$uuid = function () {
  return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
    let r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
    return v.toString(16);
  });
};

/**
 * 全局的 鉴定按钮权限的方法
 * @param code 按钮编码
 * @returns {boolean} true表示鉴权通过，false表示鉴权不通过
 */
Vue.prototype.$checkButoonAuth = function (code) {
  let access = this.$store.getters.access;
  for (let i = 0; i < access.length; i++) {
      if(access[i] == code){
        return true;
      }
  }
};

/**
 * 配置全局的加密方法
 * @param obj 需要加密的字符串
 */
Vue.prototype.$encruption = function (obj) {
  let encrypt = new JsEncrypt();
  encrypt.setPublicKey(
    `-----BEGIN PUBLIC KEY-----MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDyKmfJhgAHEK0Yo6qOtFB8sSfYIZBJUyyXi1X7EgkZo3kmMOu6/uT7SwC9EnqbiMySJfAlvB200ZMIk6e1NQrPLZpc5VSuzSQ3NqdXOdbNnhXAUeME8IcsbscK9XP8BxldMhUhIK+3zovI1VCY2kLO6TMwEVvTdH+F1jW/WFkplwIDAQAB-----END PUBLIC KEY-----`
  )
  return encrypt.encrypt(obj);
};

Vue.config.productionTip = false

Vue.use(iviewArea);

Vue.use(iView, {
  i18n: (key, value) => i18n.t(key, value)
})

/**
 * @description 全局注册应用配置
 */
Vue.prototype.$runConfig = runConfig;


/**
 * 表示当前的应用启动的时候是以mock的方式启动
 */
if (runConfig.runConfig.mock) {
  require('./config/mock/mock.js')
}

// 格式化时间全局通用方法
Vue.prototype.formatDate = function (date, fmt) {
  let o = {
    'M+': date.getMonth() + 1, // 月份
    'd+': date.getDate(), // 日
    'h+': date.getHours(), // 小时
    'm+': date.getMinutes(), // 分
    's+': date.getSeconds(), // 秒
    'S': date.getMilliseconds() // 毫秒
  }
  if (/(y+)/.test(fmt)) {
    fmt = fmt.replace(RegExp.$1, (date.getFullYear() + '').substr(4 - RegExp.$1.length))
  }
  for (var k in o) {
    if (new RegExp('(' + k + ')').test(fmt)) {
      fmt = fmt.replace(RegExp.$1, (RegExp.$1.length === 1) ? (o[k]) : (('00' + o[k]).substr(('' + o[k]).length)))
    }
  }
  return fmt
}

/* eslint-disable no-new */
const app = new Vue({
  el: '#app',
  router,
  i18n,
  store: store,
  components: {App},
  template: '<App/>'
});

initInternationalization({}).then(res=>{
  if(res.code==200){
    for(let a in res.obj){
      let v =require('iview/src/locale/lang/'+a);
      app.$i18n.setLocaleMessage(a,Object.assign(v.default,res.obj[a]));
    }
  }
});


