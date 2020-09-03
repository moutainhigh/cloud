import request from "@/libs/request";

export const getUsersPage = ({page, limit, userId, userName}) => {
    const params = {
        page: page,
        limit: limit,
        userId: userId,
        userName: userName
    }
    return request({
        url: 'base/access/users/page',
        params,
        method: 'get'
    })
};
