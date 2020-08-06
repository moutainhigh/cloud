import Vue from 'vue'
import Vuex from 'vuex'

import user from './module/user'
import app from './module/app'

Vue.use(Vuex);
/**
 * TODO 此处需要优化，带上前缀（用户标识）。防止同浏览器不同账号登录数据错乱。
 */
export default new Vuex.Store({
  state: {
    //
  },
  mutations: {
    //
  },
  actions: {
    //
  },
  modules: {
    user,
    app
  }
});
