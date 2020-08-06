import request from '@/libs/request'


/**
 * 组织列表

 * @returns {*}
 */
export const getMenus = () => {
    return request({
        url: 'base/access/menus',
        method: 'get'
    })
};
/**
 * 添加菜单
 * <br/>
 * @param menuParentId
 * @param menuId
 * @param menuName
 * @param menuIcon
 * @param menuPath
 * @param menuSchema
 * @param menuTarget
 * @param menuState
 * @returns {*}
 */
export const addMenu = ({menuParentId, menuId, menuName, menuIcon, menuPath, menuSchema, menuTarget, menuState}) => {
    const data = {
        menuParentId: menuParentId,
        menuId: menuId,
        menuName: menuName,
        menuIcon: menuIcon,
        menuPath: menuPath,
        menuSchema: menuSchema,
        menuTarget: menuTarget,
        menuState: menuState
    };
    return request({
        url: 'base/access/menus',
        data,
        method: 'post'
    })
};
/**
 * 根据{menuId}查询组织列表
 *
 * @param menuId
 * @returns {*}
 */
export const getMenuChildren = ({menuId}) => {
    const params = {menuId: menuId}
    return request({
        url: 'base/access/menus/children',
        params,
        method: 'get'
    })
};
/**
 * 查询组织详情
 *
 * @param menuId
 * @returns {*}
 */
export const viewMenu = ({menuId}) => {
    return request({
        url: 'base/access/menus/' + menuId,
        method: 'get'
    })
};
/**
 * 查询组织{menuId}下的直接用户（不查询下下级数据）
 *
 * @param menuId
 * @returns {*}
 */
export const getMenuUsers = ({menuId}) => {
    return request({
        url: 'base/access/menus/' + menuId + "/users",
        method: 'get'
    })
};

/**
 * 查询组织{menuId}下的直接角色（不查询下下级数据）
 *
 * @param menuId
 * @returns {*}
 */
export const getMenuRoles = ({menuId}) => {
    return request({
        url: 'base/access/menus/' + menuId + "/roles",
        method: 'get'
    })
};
