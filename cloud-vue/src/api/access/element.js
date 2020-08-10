import request from "@/libs/request";

export const getElementsPage = ({page, limit, elementName, elementCode}) => {
    const params = {
        page: page,
        limit: limit,
        elementName: elementName,
        elementCode: elementCode
    }
    return request({
        url: 'base/access/elements/page',
        params,
        method: 'get'
    })
};
