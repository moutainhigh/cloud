import request from "@/libs/request";

export const getRoutesPage = ({page, limit, routeName, routePath}) => {
  const params = {
    page: page,
    limit: limit,
    routeName: routeName,
    routePath: routePath
  }
  return request({
    url: 'base/gateways/routes/page',
    params,
    method: 'get'
  })
};
/**
 * 刷新网关
 * @returns {*}
 */
export const refreshGateway = () => {
  const data = {}
  return request({
    url: 'actuator/gateways/routes/refresh',
    data,
    method: 'post'
  })
}

/**
 * 添加路由
 */
export const addRoute = ({routeDesc, routeName, routeServiceId, routeUrl, routePath, routeStripPrefix, routeRetryable, routeState}) => {
  const data = {
    routeDesc: routeDesc,
    routeName: routeName,
    routeServiceId: routeServiceId,
    routeUrl: routeUrl,
    routePath: routePath,
    routeStripPrefix: routeStripPrefix,
    routeRetryable: routeRetryable,
    routeState: routeState
  };
  return request({
    url: 'base/gateways/routes',
    data,
    method: 'post'
  })
};
/**
 * 编辑路由
 */
export const updateRoute = ({routeId, routeDesc, routeName, routeServiceId, routeUrl, routePath, routeStripPrefix, routeRetryable, routeState}) => {
  const data = {
    routeDesc: routeDesc,
    routeName: routeName,
    routeServiceId: routeServiceId,
    routeUrl: routeUrl,
    routePath: routePath,
    routeStripPrefix: routeStripPrefix,
    routeRetryable: routeRetryable,
    routeState: routeState
  };
  return request({
    url: 'base/gateways/routes/' + routeId,
    data,
    method: 'put'
  })
};
/**
 * 移除路由
 * @param routeId
 * @returns {*}
 */
export const removeRoute = (routeId) => {
  return request({
    url: 'base/gateways/routes/' + routeId,
    method: 'delete'
  })
};
