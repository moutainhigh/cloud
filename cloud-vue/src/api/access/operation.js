import request from "@/libs/request";

export const getOperationsPage = ({page, limit, operationName, operationDesc, operationPath}) => {
    const params = {
        page: page,
        limit: limit,
        operationName: operationName,
        operationDesc: operationDesc,
        operationPath: operationPath
    }
    return request({
        url: 'base/access/operations/page',
        params,
        method: 'get'
    })
};
