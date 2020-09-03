import CryptoJS from 'crypto-js'

// SHA256
const sha256 = (data) => {
    return CryptoJS.SHA256(data).toString()
};
// MD5
const md5 = (data) => {
    return CryptoJS.MD5(data).toString()
};

const getDateTimeToString = () => {
    const date_ = new Date();
    let year = date_.getFullYear();
    let month = date_.getMonth() + 1;
    let day = date_.getDate();
    if (month < 10) month = '0' + month;
    if (day < 10) day = '0' + day;
    let hours = date_.getHours();
    let mins = date_.getMinutes();
    let secs = date_.getSeconds();
    let msecs = date_.getMilliseconds();
    if (hours < 10) hours = '0' + hours;
    if (mins < 10) mins = '0' + mins;
    if (secs < 10) secs = '0' + secs;
    if (msecs < 10) secs = '0' + msecs;
    return year + '' + month + '' + day + '' + hours + '' + mins + '' + secs
};

const getNonce = () => {
    return Math.random().toString(36).substring(2)
};

/**
 * 接口参数签名
 * @param {*} config 请求配置
 * @param appId 应用ID
 * @param appSecret 应用密钥
 * @param signType 签名类型
 */
export const sign = (config, appId, appSecret, signType) => {
    // 参与签名的公共字段
    let _app_id = "APP_ID";
    let _sign_type = "SIGN_TYPE";
    let _timestamp = "TIMESTAMP";
    let _nonce = "NONCE";
    let _secret_key = "SECRET_KEY";
    let _sign = "SIGN";
    // 参与签名的公共字段
    let signingColumns = {};
    signingColumns[_app_id] = appId;
    signingColumns[_sign_type] = signType;
    signingColumns[_timestamp] = getDateTimeToString();
    signingColumns[_nonce] = getNonce();

    // 参数格式化（url参数签名/request body参数的内容）
    if (config.method === 'get') {
        signingColumns = config.params = Object.assign(config.params ? config.params : {}, signingColumns);
    } else {
        signingColumns = config.data = Object.assign(config.data ? config.data : {}, signingColumns);
    }
    // 防止数据污染，删除签名字段（用户管理点击查询时此处会有签名字段在里面，还不清楚问题所在。先删除签名字段）
    delete signingColumns[_sign];

    // 获取所有字段Key并排序
    const sortedKeys = Object.keys(signingColumns).sort();

    let str = '';
    sortedKeys.map(k => {
        const v = signingColumns[k];
        if (v || v === 0) {
            // 参数值为空，则不参与签名
            str += k + '=' + v + '&'
        }
    });
    str += _secret_key + "=" + appSecret;

    let sign = '';
    switch (signType) {
        case 'MD5':
            sign = md5(str).toUpperCase();
            break
        case 'SHA256':
            sign = sha256(str).toUpperCase();
            break
    }

    // 添加签名字段和移除应用密钥
    if (config.method === 'get') {
        config.params[_sign] = sign;
    } else {
        config.data[_sign] = sign;
    }

    return config
};
