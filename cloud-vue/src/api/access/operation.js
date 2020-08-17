import request from "@/libs/request";

export const getOperationsPage = ({page, limit, operationName, operationDesc, operationPath, operationServiceId}) => {
  const params = {
    page: page,
    limit: limit,
    operationName: operationName,
    operationDesc: operationDesc,
    operationPath: operationPath,
    operationServiceId: operationServiceId
  }
  return request({
    url: 'base/access/operations/page',
    params,
    method: 'get'
  })
};
/**
 * 编辑操作状态
 */
export const updateOperationState = ({operationIds, operationState}) => {
  const data = {
    operationState: operationState
  };
  return request({
    url: 'base/access/operations/' + operationIds + "/state",
    data,
    method: 'put'
  })
};
/**
 * 编辑操作公开访问状态
 */
export const updateOperationOpen = ({operationIds, operationOpen}) => {
  const data = {
    operationOpen: operationOpen
  };
  return request({
    url: 'base/access/operations/' + operationIds + "/open",
    data,
    method: 'put'
  })
};
/**
 * 编辑操作身份认证状态
 */
export const updateOperationAuth = ({operationIds, operationAuth}) => {
  const data = {
    operationAuth: operationAuth
  };
  return request({
    url: 'base/access/operations/' + operationIds + "/auth",
    data,
    method: 'put'
  })
};
/**
 * 移除操作
 * @param operationIds
 * @returns {*}
 */
export const removeOperation = (operationIds) => {
  return request({
    url: 'base/access/operations/' + operationIds,
    method: 'delete'
  })
};
