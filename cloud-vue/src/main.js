// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import store from './store'
import iView from 'view-design'
import i18n from '@/locale'
import config from '@/config'
import importDirective from '@/directive'
import installPlugin from '@/plugin'
import 'view-design/dist/styles/iview.css'
import './index.less'
import '@/assets/icons/iconfont.css'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
import TreeTable from 'tree-table-vue'
import Treeselect from '@riophae/vue-treeselect'
import 'highlight.js/styles/googlecode.css' //样式文件
import moment from 'moment';

Vue.use(iView, {
    i18n: (key, value) => i18n.t(key, value)
})

Vue.component(TreeTable.name, TreeTable)
// 注册组件
Vue.component('treeselect', Treeselect)

/**
 * @description 注册admin内置插件
 */
installPlugin(Vue)
/**
 * @description 生产环境关掉提示
 */
Vue.config.productionTip = false;
/**
 * @description 全局注册应用配置
 */
Vue.prototype.$config = config;
/**
 * 注册指令
 */
importDirective(Vue)

/**
 * 按钮权限控制
 *
 * 使用方法：<br/>
 * v-show="hasAuthority('systemUserCreate')"
 * <br/>
 * v-show="hasAuthority('systemUserCreate,systemUserUpdate,')"
 * <br/>
 * @param authorities 多个用,号隔开
 * @returns {boolean}
 */
Vue.prototype.hasAuthority = function (authorities) {
    return authorities && authorities.split(',')
        .some(item => {
            return store.state.user.access.includes(item);
        })
}
/**
 * 全局过滤器 - 时间格式化
 *
 * 使用方法：<br/>
 * <p>
 *     {{ row.createdDate | dateFmt('YYYY-MM-DD HH:mm:ss') }}
 * </p>
 * @param dateObj   日期时间对象
 * @param formatter 显示格式
 */
Vue.filter("dateFmt", (dateObj, formatter = "YYYY-MM-DD") => {
    if (!dateObj || !dateObj.year) {
        return '';
    }
    let vv = dateObj.year
        + "-" + dateObj['monthValue']
        + "-" + dateObj['dayOfMonth']
        + " " + (dateObj.hour ? dateObj.hour : '00')
        + ":" + (dateObj.minute ? dateObj.minute : '00')
        + ":" + (dateObj.second ? dateObj.second : '00')
    ;
    return moment(vv, moment.HTML5_FMT.DATETIME_LOCAL_SECONDS).format(formatter);
})

/* eslint-disable no-new */
new Vue({
    el: '#app',
    router,
    i18n,
    store,
    render: h => h(App)
})
