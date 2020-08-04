import request from '@/libs/request'

/**
 * 分页查询组织
 *
 * @param page
 * @param limit
 * @param groupParentId
 * @param groupType
 * @param groupState
 * @returns {*}
 */
export const getGroupsPage = ({page, limit, groupParentId, groupType, groupState}) => {
    const params = {
        page: page,
        limit: limit,
        groupParentId: groupParentId,
        groupType: groupType,
        groupState: groupState
    }
    return request({
        url: 'base/access/groups/page',
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
export const getGroups = ({groupId}) => {
    const params = {groupId: groupId}
    return request({
        url: 'base/access/groups',
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
