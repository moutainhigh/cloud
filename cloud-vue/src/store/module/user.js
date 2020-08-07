import {getCurrentUserMenu, getUserInfo, login, logout} from '@/api/user'
import {getToken, setToken} from '@/libs/util'

export default {
    /**
     * TODO 此处代码定义关键为前后端接口数据字段映射，后台字段需要适配此处的定义
     */
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
        /**
         * com.smart4y.cloud.core.dto.AuthorityMenuDTO
         * ////////////////////用户菜单格式////////////////////
         * // actionList: Array(0)
         * // authority: "MENU_system"
         * // authorityId: "1"
         * // children: (4) [{…}, {…}, {…}, {…}]
         * // createdDate: "2018-07-29 21:20:10"
         * // hasChild: true
         * // icon: "md-folder"
         * // isPersist: 1
         * // lastModifiedDate: "2019-05-25 01:49:23"
         * // menuCode: "system"
         * // menuDesc: "系统管理"
         * // menuId: "1"
         * // menuName: "系统管理"
         * // parentId: "0"
         * // path: ""
         * // priority: 0
         * // scheme: "/"
         * // serviceId: "open-cloud-base-server"
         * // status: 1
         * // target: "_self"
         * //////////////////////////////////////////////
         */
        menus: []   // 用户菜单
    },
    mutations: {
        setAvatar(state, avatarPath) {
            state.avatarImgPath = avatarPath
        },
        getAvatar(state) {
            console.info('-------> 获取用户头像：%s', state.avatarImgPath);
            return state.avatarImgPath;
        },
        setNickName(state, nickName) {
            state.nickName = nickName
        },
        getNickName(state) {
            console.info('-------> 获取用户昵称：%s', state.nickName);
            return state.nickName;
        },
        setUserId(state, id) {
            state.userId = id
        },
        getUserId(state) {
            console.info('-------> 获取用户ID：%s', state.nickName);
            return state.userId;
        },
        setUserName(state, name) {
            state.userName = name
        },
        getUserName(state) {
            console.info('-------> 获取用户名：%s', state.userName);
            return state.userName;
        },
        setAccess(state, access) {
            state.access = access
        },
        getAccess(state) {
            console.info('-------> 获取用户权限：%s', state.access);
            return state.access;
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
                            })
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
                        getCurrentUserMenu()
                            .then(res => {
                                console.info('-------> 设置用户菜单：%o', res.data);
                                commit('setUserMenus', res.data)
                                resolve(state)
                            })
                            .catch(err => {
                                reject(err)
                            });
                    })
                    .catch(err => {
                        reject(err)
                    });
            })
        }
    }
}
