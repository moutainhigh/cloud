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
 * @param menuName
 * @param menuCode
 * @param menuIcon
 * @param menuPath
 * @param menuSchema
 * @param menuTarget
 * @param menuState
 * @param menuSorted
 * @returns {*}
 */
export const addMenu = ({menuParentId, menuName, menuCode, menuIcon, menuPath, menuSchema, menuTarget, menuState, menuSorted}) => {
    const data = {
        menuParentId: menuParentId,
        menuName: menuName,
        menuCode: menuCode,
        menuIcon: menuIcon,
        menuPath: menuPath,
        menuSchema: menuSchema,
        menuTarget: menuTarget,
        menuState: menuState,
        menuSorted: menuSorted
    };
    return request({
        url: 'base/access/menus',
        data,
        method: 'post'
    })
};
/**
 * 编辑菜单
 * @param menuParentId
 * @param menuId
 * @param menuName
 * @param menuCode
 * @param menuIcon
 * @param menuPath
 * @param menuSchema
 * @param menuTarget
 * @param menuState
 * @param menuSorted
 * @returns {*}
 */
export const updateMenu = ({menuParentId, menuId, menuName, menuCode, menuIcon, menuPath, menuSchema, menuTarget, menuState, menuSorted}) => {
    const data = {
        menuParentId: menuParentId,
        menuName: menuName,
        menuCode: menuCode,
        menuIcon: menuIcon,
        menuPath: menuPath,
        menuSchema: menuSchema,
        menuTarget: menuTarget,
        menuState: menuState,
        menuSorted: menuSorted
    };
    return request({
        url: 'base/access/menus/' + menuId,
        data,
        method: 'put'
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
