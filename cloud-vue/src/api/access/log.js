import request from "@/libs/request";

export const getLogsPage = ({page, limit, logPath, logIp, logServiceId}) => {
    const params = {
        page: page,
        limit: limit,
        logPath: logPath,
        logServiceId: logServiceId,
        logIp: logIp
    }
    return request({
        url: 'base/gateways/logs/page',
        params,
        method: 'get'
    })
};
