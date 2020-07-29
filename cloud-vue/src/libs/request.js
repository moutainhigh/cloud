import axios from 'axios'
import $config from '@/config'
import {getToken} from '@/libs/util'
import {Message} from 'view-design'
import {sign} from '@/libs/sign'

let baseUrl = '';
switch (process.env.NODE_ENV) {
    case 'development':
        // 这里是本地的请求url
        baseUrl = $config.apiUrl.dev;
        break;
    case 'production':
        // 生产环境url
        baseUrl = $config.apiUrl.pro;
        break;
}

/**
 * 创建axios实例
 * @type {AxiosInstance}
 */
const service = axios.create({
    // api的base_url
    baseURL: baseUrl,
    // 设置请求超时时间30s
    timeout: 30000
});

service.apiUrl = baseUrl;

/**
 * 请求参数处理
 */
service.interceptors.request.use((config) => {
        // if (config.method === 'get') {
        //     config.params['v'] = new Date().getTime();
        // }
        // 参数签名处理
        config = sign(config, $config.appId, $config.appSecret, 'SHA256');
        // config.method === 'get' ? config.params = {...config.params} : config.data = qs.stringify({...config.data});
        if (config.method === 'get') {
            config.params = {...config.params};
        } else {
            config.data = {...config.data};
        }
        const token = getToken();
        if (token) {
            config.headers['Authorization'] = 'Bearer ' + token
        }
        return config
    }
);
/**
 * 响应结果处理
 */
service.interceptors.response.use(
    // 服务器正常响应
    (response) => {
        let res = response.data;
        let rtnCode = res.rtnCode;
        if (rtnCode !== '200') {
            console.log(res);
        }
        switch (rtnCode) {
            case '200':
                // 服务端定义的响应码`rtnCode=200`时请求成功
                // 使用`Promise.resolve`正常响应
                return Promise.resolve(res);
            case '400':
                Message.error({content: res.message});
                return Promise.reject(res);
            case '401':
                location.reload();
                return Promise.reject(res);// 此处应该是直接 return;
            case '403':
                Message.error({content: '禁止访问!'});
                return Promise.reject(res);
            case '429':
                Message.error({content: '访问太过频繁，请稍后再试!'});
                return Promise.reject(res);
            default:
                Message.error({content: res.message});
                return Promise.reject(res);
        }
    },
    // 服务器异常响应，例如：服务器连接超时
    (error) => {
        let message = '';
        if (error && error.response) {
            switch (error.response.status) {
                case 401:
                    location.reload();
                    return;
                case 403:
                    message = '禁止访问!';
                    break;
                case 429:
                    message = '访问太过频繁，请稍后再试!';
                    break;
                default:
                    message = error.response.data.message ? error.response.data.message : '服务器错误';
                    break;
            }
            Message.error({content: message});
            // 请求错误处理
            return Promise.reject(error)
        } else {
            message = '连接服务器失败';
            Message.error({content: message});
            return Promise.reject(error)
        }
    }
);

export default service
