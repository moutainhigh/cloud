import Vue from 'vue'
import Router from 'vue-router'
import routes from './routers'
import store from '@/store'
import iView from 'view-design'
import {canTurnTo, formatRouters, getToken, setToken} from '@/libs/util'
import config from '@/config'

const {homeName} = config
Vue.use(Router)
// 存放加载的动态路由
let dyncRouters = []

let BASE_URL = ''
switch (process.env.NODE_ENV) {
  case 'development':
    BASE_URL = config.publicPath.dev // 这里是本地的请求url
    break
  case 'production':
    BASE_URL = config.publicPath.pro // 生产环境url
    break
}

const router = new Router({
  base: BASE_URL,
  routes: routes,
  mode: 'hash' // history-请求形式   hash-添加锚点形式
})
const LOGIN_PAGE_NAME = 'login'

const permitList = [LOGIN_PAGE_NAME, 'loginSuccess']

const turnTo = (to, access, next) => {
  if (!to.name) {
    // 防止地址栏刷新动态路由跳转到401或404,先跳转到homeName
    router.replace(to)
  } else if (canTurnTo(to.name, access, routes)) {
    next()
  } else {
    // 无权限，重定向到401页面
    next({replace: true, name: 'error_401'})
  }
}

router.beforeEach((to, from, next) => {
  iView.LoadingBar.start()

  // 是否登录了（true登录了）
  const token = getToken();
  // 目标页面
  let toName = to.name;
  // 是否为路由白名单（'login', 'loginSuccess'）
  let permit = permitList.includes(toName);

  if (!token) {
    if (!permit) {
      console.info('未登录，并且[%s]不是白名单页面，跳转到登录页[%s]', toName, LOGIN_PAGE_NAME);
      next({
        name: LOGIN_PAGE_NAME
      })
    } else {
      console.info('未登录，并且[%s]是白名单页面，跳转页面[%s]', toName, toName);
      next() // 跳转
    }
  } else {
    if (toName === LOGIN_PAGE_NAME) {
      console.info('已登录，并且上个页面是登录页[%s]', toName);
      next({
        name: homeName
      })
    } else {
      let hasGetInfo = store.state.user.hasGetInfo;
      if (hasGetInfo) {
        console.info('已登录，跳转到其他需要认证的页面[%s]，已有用户信息直接跳转', toName);
        turnTo(to, store.state.user.access, next)
      } else {
        console.info('已登录，跳转到其他需要认证的页面[%s]，无用户信息，获取用户菜单信息后跳转', toName);
        store.dispatch('getUserInfo')
          .then(res => {
            console.info('已登录，跳转到其他需要认证的页面[%s]，已查询到用户信息[%s]', toName);
            if (!dyncRouters || dyncRouters.length === 0) {
              dyncRouters = dyncRouters.concat(...formatRouters(store.state.user.menus, store.state.user.access));
              // 防止重复添加路由报错
              router.addRoutes(dyncRouters);
              routes.push(...dyncRouters)
            }

            turnTo(to, store.state.user.access, next);
          })
          .catch(err => {
            setToken('');
            next({
              name: 'login'
            })
          })
      }
    }
  }
})

router.afterEach(to => {
  iView.LoadingBar.finish()
  window.scrollTo(0, 0)
})

export default router;
