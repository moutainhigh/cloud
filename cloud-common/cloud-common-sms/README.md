# guerlab-sms-server-starter

## 安装教程

#### 1.引入jar包

```
<dependency>
    <groupId>com.smart4y.cloud.sms</groupId>
    <artifactId>guerlab-sms-server-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

#### 2.`bootstrap.yml` 增加配置项 sms.*

```yaml
sms:
  #通用配置
  ##手机号码正则表达式，为空则不做验证
  reg: 
  web:
    ##启用web端点
    enable: true 
    ##访问路径前缀
    base-path: /commons/sms 
  verification-code:
    ##验证码长度
    code-length: 6 
    ##为true则验证失败后删除验证码
    delete-by-verify-fail: false 
    ##为true则验证成功后删除验证码
    delete-by-verify-succeed: true
    ##重试间隔时间，单位秒 
    retry-interval-time: 60
    ##验证码有效期，单位秒 
    expiration-time: 180 
    ##识别码长度
    identification-code-length: 3 
    ##是否启用识别码
    use-identification-code: false 
  redis:
    ##验证码业务在保存到redis时的key的前缀
    key-prefix: VerificationCode 
  ##阿里云
  aliyun:
    ##启用客户端
    enable: true
    ##AccessKeyId
    access-key-id: 7gBZcbsC7kLIWCdELIl8nxcs
    ##AccessKeySecret
    access-key-secret: 0osTIhce7uPvDKHz6aa67bhCukaKoYl4
    ##endpoint，默认为cn-hangzhou
    endpoint: cn-hangzhou 
    ##短信签名
    sign-name: 
    ##短信模板配置（key为业务层的NoticeData中type的值，value为阿里云中生成的短信模板ID）
    templates:
      ##验证码业务所使用的短信模板ID
      VerificationCode: SMS_00000000 
      ##自定义业务所使用的短信模板ID
      test: SMS_00000001 
  ##百度云
  baiducloud:
    ##启用客户端
    enable: true
    ##AccessKeyId
    access-key-id: 7gBZcbsC7kLIWCdELIl8nxcs
    ##AccessKeySecret
    secret-access-key: 0osTIhce7uPvDKHz6aa67bhCukaKoYl4
    endpoint: ##endpoint
    ##短信模板配置
    templates:
      ##验证码业务所使用的短信模板ID
      VerificationCode: SMS_00000000 
      ##自定义业务所使用的短信模板ID
      test: SMS_00000001 
  ##华为云  
  huawei:
    ##启用客户端
    enable: true
    uri: ##请求地址
    app-key: ##APP_Key
    app-secret: ##APP_Secret
    sender: ##国内短信签名通道号或国际/港澳台短信通道号
    signature: ##签名名称
    endpoint: ##endpoint
    ##短信模板配置
    templates:
      ##验证码业务所使用的短信模板ID
      VerificationCode: SMS_00000000 
      ##自定义业务所使用的短信模板ID
      test: SMS_00000001 
    params-orders:
      VerificationCode: ##规定验证码业务的参数顺序
        - code
        - identificationCode
  ##京东云
  jdcloud:
    ##启用客户端
    enable: true
    ##AccessKeyId
    access-key-id: 7gBZcbsC7kLIWCdELIl8nxcs
    ##AccessKeySecret
    access-key-secret: 0osTIhce7uPvDKHz6aa67bhCukaKoYl4
    region: cn-north-1 ##地域, 默认cn-north-1
    sign-id: ##签名ID
    ##短信模板配置
    templates:
      ##验证码业务所使用的短信模板ID
      VerificationCode: SMS_00000000 
      ##自定义业务所使用的短信模板ID
      test: SMS_00000001 
    params-orders:
      VerificationCode: ##规定验证码业务的参数顺序
        - code
        - identificationCode
  ##网易云信
  netease:
    ##启用客户端
    enable: true
    app-key: ##appkey
    app-secret: ##appSecret
    ##短信模板配置
    templates:
      ##验证码业务所使用的短信模板ID
      VerificationCode: SMS_00000000 
      ##自定义业务所使用的短信模板ID
      test: SMS_00000001 
    params-orders:
      VerificationCode: ##规定验证码业务的参数顺序
        - code
        - identificationCode
  ##腾讯云
  qcloud:
    ##启用客户端
    enable: true
    app-id: ##短信应用SDK AppID
    appkey: ##短信应用SDK AppKey
    sms-sign: ##短信签名
    ##短信模板配置
    templates:
      ##验证码业务所使用的短信模板ID
      VerificationCode: SMS_00000000 
      ##自定义业务所使用的短信模板ID
      test: SMS_00000001 
    params-orders:
      VerificationCode: ##规定验证码业务的参数顺序（短信模板参数将按照下列顺序转换为腾讯云短信应用sdk所需要的数组参数）
        - code
        - identificationCode
  ##七牛云
  qiniu:
    ##启用客户端
    enable: true
    access-key: ##accessKey
    secret-key: ##secretKey
    ##短信模板配置
    templates:
      ##验证码业务所使用的短信模板ID
      VerificationCode: SMS_00000000 
      ##自定义业务所使用的短信模板ID
      test: SMS_00000001 
  ##又拍云
  upyun:
    ##启用客户端
    enable: true
    token: ##token
    ##短信模板配置
    templates:
      ##验证码业务所使用的短信模板ID
      VerificationCode: SMS_00000000 
      ##自定义业务所使用的短信模板ID
      test: SMS_00000001 
    params-orders:
      VerificationCode: ##规定验证码业务的参数顺序
        - code
        - identificationCode
  ##云片网
  yunpian:
    ##启用客户端
    enable: true
    apikey: ##apikey
    ##短信模板配置
    templates:
      ##验证码业务所使用的短信模板ID
      VerificationCode: SMS_00000000 
      ##自定义业务所使用的短信模板ID
      test: SMS_00000001 
```

### 3.启动方法增加注解项@EnableSmsServer
```java

@EnableSmsServer
@SpringBootApplication
public class Starter {
    public static void main(String[] args) {
        SpringApplication.run(Starter.class, args);
    }
}
```

### 4.定制验证码的储存位置（可选）
* 实现`IVerificationCodeRepository`接口即可

### 5.发送验证码
* 5.1 注入`VerificationCodeService` (com.smart4y.cloud.sms.service.VerificationCodeService)
* 5.2 发送验证码，调用verificationCodeService.send(phone)方法进行验证码发送
* 5.3 验证码验证，调用verificationCodeService.verify(phone, code, identificationCode)进行验证，其中code为验证码，identificationCode为识别码，识别码非必填

### 6.发送通知
* 6.1 注入`NoticeService` (com.smart4y.cloud.sms.service.NoticeService)
* 7.2 发送通知，调用noticeService.send(noticeData, phones)进行通知发送，noticeData为net.guerlab.sms.core.domain.NoticeData实例，phones为手机号码列表

### 7.请求地址
* /commons/sms/verificationCode/{phone}	POST	给指定号码发送验证码
* /commons/sms/verificationCode/{phone}	GET	获取指定号码的验证码信息
* /commons/sms/verificationCode	POST	验证验证码是否有效
* /commons/sms/notice	POST	发送短信通知