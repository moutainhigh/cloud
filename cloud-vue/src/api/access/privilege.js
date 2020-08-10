import request from "@/libs/request";

export const getPrivilegesPage = ({page, limit, privilegeId, privilege, privilegeType}) => {
    const params = {
        page: page,
        limit: limit,
        privilegeId: privilegeId,
        privilege: privilege,
        privilegeType: privilegeType
    }
    return request({
        url: 'base/access/privileges/page',
        params,
        method: 'get'
    })
};
