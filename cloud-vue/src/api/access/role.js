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
