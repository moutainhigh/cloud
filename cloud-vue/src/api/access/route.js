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
    url: 'actuator/gateways/refresh',
    data,
    method: 'post'
  })
}
