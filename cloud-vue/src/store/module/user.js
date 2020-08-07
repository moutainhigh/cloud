import {getUserInfo, login, logout} from '@/api/user'
import {getCurrentMenus} from '@/api/access/current'
import {getToken, setToken} from '@/libs/util'

export default {

  state: {
    userName: '',
    userId: '',
    nickName: '',
    avatarImgPath: '',
    token: getToken(),
    hasGetInfo: false,
    userDesc: '',
    mobile: '',
    email: '',
    access: [], // 用户权限（获取用户信息接口res.data.authorities.authority值），例如：['ROLE_admin', 'MENU_system', 'ACTION_systemMenuView',... ]
    menus: []   // 用户菜单
  },
  mutations: {
    setAvatar(state, avatarPath) {
      state.avatarImgPath = avatarPath
    },
    setNickName(state, nickName) {
      state.nickName = nickName
    },
    setUserId(state, id) {
      state.userId = id
    },
    setUserName(state, name) {
      state.userName = name
    },
    setAccess(state, access) {
      state.access = access
    },
    setToken(state, {token, auto}) {
      state.token = token;
      // 设置到Cookie
      setToken(token, auto)
    },
    setHasGetInfo(state, status) {
      state.hasGetInfo = status
    },
    setUserMenus(state, menus) {
      state.menus = menus
    },
    setMobile(state, mobile) {
      state.mobile = mobile
    },
    setEmail(state, email) {
      state.email = email
    },
    setUserDesc(state, userDesc) {
      state.userDesc = userDesc
    }
  },
  actions: {
    /**
     * 登录
     * @param commit
     * @param username
     * @param password
     * @param auto
     * @returns {Promise<unknown>}
     */
    handleLogin({commit}, {username, password, auto}) {
      username = username.trim()
      return new Promise((resolve, reject) => {
        login({username, password})
          .then(res => {
            let token = res.data.access_token;
            console.info('-------> 登录成功，设置Token：%o', res.data);
            commit('setToken', {token, auto});
            resolve(res);
          })
          .catch(err => {
            reject(err);
          });
      })
    },
    /**
     * 退出登录
     * @param state
     * @param commit
     * @returns {Promise<unknown>}
     */
    handleLogout({state, commit}) {
      return new Promise((resolve, reject) => {
        logout()
          .then(res => {
            console.info('-------> 退出成功，清除信息');
            commit('setToken', '');
            commit('setAccess', []);
            commit('setHasGetInfo', false);
            resolve(res);
          })
          .catch(err => {
            reject(err);
          });
      })
    },
    /**
     * 获取用户相关信息
     * @param state
     * @param commit
     * @returns {Promise<unknown>}
     */
    getUserInfo({state, commit}) {
      return new Promise((resolve, reject) => {
        getUserInfo()
          .then(res => {
            console.info('-------> 设置用户信息：%o', res.data);
            // 转换权限
            const access = []
            if (res.data.authorities) {
              res.data.authorities.map(item => {
                if (item.authority) {
                  access.push(item.authority)
                }
              });
            }
            commit('setAccess', access)
            commit('setAvatar', res.data.avatar)
            commit('setUserName', res.data.username)
            commit('setNickName', res.data.nickName)
            commit('setUserId', res.data.userId)
            commit('setEmail', res.data.email)
            commit('setMobile', res.data.mobile)
            commit('setUserDesc', res.data.userDesc)
            commit('setHasGetInfo', true)

            // 获取用户菜单
            // getCurrentUserMenu()
            //     .then(res => {
            //         commit('setUserMenus', res.data);
            //         resolve(state);
            //     })
            //     .catch(err => {
            //         reject(err)
            //     });

            getCurrentMenus()
              .then(res => {
                let arr = [];
                res.data.map(menu => {
                  arr.push({
                    menuId: menu.menuId,
                    menuParentId: menu.menuParentId,
                    menuName: menu.menuName,
                    menuCode: menu.menuCode,
                    menuIcon: menu.menuIcon,
                    menuSchema: menu.menuSchema,
                    menuPath: menu.menuPath,
                    menuTarget: menu.menuTarget
                  });
                });
                console.info('-------> 设置用户菜单：%o', arr);
                commit('setUserMenus', arr);

                resolve(state);
              })
              .catch(err => {
                reject(err);
              });
          })
          .catch(err => {
            reject(err);
          });
      })
    }
  }
}
