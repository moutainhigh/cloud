import request from '@/libs/request'

/**
 * 组织列表
 * @param groupTypes
 * @returns {*}
 */
export const getGroups = ({groupTypes}) => {
    const params = {
        groupTypes: groupTypes
    };
    return request({
        url: 'base/access/groups',
        params,
        method: 'get'
    })
};

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
 * 添加组织
 * @returns {*}
 */
export const addGroup = ({groupParentId, groupName, groupState, groupType}) => {
    const data = {
        groupParentId: groupParentId,
        groupName: groupName,
        groupState: groupState,
        groupType: groupType
    };
    return request({
        url: 'base/access/groups',
        data,
        method: 'post'
    })
};
/**
 * 编辑组织
 * @returns {*}
 */
export const updateGroup = ({groupId, groupParentId, groupName, groupState, groupType}) => {
    const data = {
        groupParentId: groupParentId,
        groupName: groupName,
        groupState: groupState,
        groupType: groupType
    };
    return request({
        url: 'base/access/groups/' + groupId,
        data,
        method: 'put'
    })
};
/**
 * 移除组织
 * @param groupId
 * @returns {*}
 */
export const removeGroup = (groupId) => {
    return request({
        url: 'base/access/groups/' + groupId,
        method: 'delete'
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
