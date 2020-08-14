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

/**
 * 添加元素
 */
export const addElement = ({elementName, elementCode}) => {
    const data = {
        elementName: elementName,
        elementCode: elementCode
    };
    return request({
        url: 'base/access/elements',
        data,
        method: 'post'
    })
};
/**
 * 编辑元素
 */
export const updateElement = ({elementId, elementName, elementCode}) => {
    const data = {
        elementName: elementName,
        elementCode: elementCode
    };
    return request({
        url: 'base/access/elements/' + elementId,
        data,
        method: 'put'
    })
};
/**
 * 移除元素
 * @param elementId
 * @returns {*}
 */
export const removeElement = (elementId) => {
    return request({
        url: 'base/access/elements/' + elementId,
        method: 'delete'
    })
};
