import request from '@/libs/request'

/**
 * 根据{groupId}查询组织列表
 *
 * @param groupId
 * @returns {*}
 */
export const getGroupChildren = ({groupId}) => {
    const params = {groupId: groupId}
    return request({
        url: 'base/access/groups/children',
        params,
        method: 'get'
    })
};
/**
 * 查询组织详情
 *
 * @param groupId
 * @returns {*}
 */
export const viewGroup = ({groupId}) => {
    return request({
        url: 'base/access/groups/' + groupId,
        method: 'get'
    })
};
/**
 * 查询组织{groupId}下的直接用户（不查询下下级数据）
 *
 * @param groupId
 * @returns {*}
 */
export const getGroupUsers = ({groupId}) => {
    return request({
        url: 'base/access/groups/' + groupId + "/users",
        method: 'get'
    })
};

/**
 * 查询组织{groupId}下的直接角色（不查询下下级数据）
 *
 * @param groupId
 * @returns {*}
 */
export const getGroupRoles = ({groupId}) => {
    return request({
        url: 'base/access/groups/' + groupId + "/roles",
        method: 'get'
    })
};
