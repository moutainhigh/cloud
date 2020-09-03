import request from "@/libs/request";

export const getRolesPage = ({page, limit, roleName, roleCode}) => {
  const params = {
    page: page,
    limit: limit,
    roleName: roleName,
    roleCode: roleCode
  }
  return request({
    url: 'base/access/roles/page',
    params,
    method: 'get'
  })
};

/**
 * 添加角色
 */
export const addRole = ({roleName, roleCode, roleDesc}) => {
  const data = {
    roleName: roleName,
    roleCode: roleCode,
    roleDesc: roleDesc
  };
  return request({
    url: 'base/access/roles',
    data,
    method: 'post'
  })
};
/**
 * 编辑角色
 */
export const updateRole = ({roleId, roleName, roleCode, roleDesc}) => {
  const data = {
    roleName: roleName,
    roleCode: roleCode,
    roleDesc: roleDesc
  };
  return request({
    url: 'base/access/roles/' + roleId,
    data,
    method: 'put'
  })
};
/**
 * 移除角色
 * @param roleId
 * @returns {*}
 */
export const removeRole = (roleId) => {
  return request({
    url: 'base/access/roles/' + roleId,
    method: 'delete'
  })
};

