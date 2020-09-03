import request from '@/libs/request';


/**
 * 组织列表

 * @returns {*}
 */
export const getCurrentMenus = () => {
    return request({
        url: 'base/access/current/menus',
        method: 'get'
    })
};
