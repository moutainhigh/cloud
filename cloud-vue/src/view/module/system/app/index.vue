<template>
  <div>
    <Card shadow>
      <Form :label-width="80"
            :model="pageInfo"
            inline
            ref="searchForm">
        <FormItem label="AppId" prop="appId">
          <Input placeholder="请输入关键字" type="text" v-model="pageInfo.appId"/>
        </FormItem>
        <FormItem label="中文名称" prop="appName">
          <Input placeholder="请输入关键字" type="text" v-model="pageInfo.appName"/>
        </FormItem>
        <FormItem label="英文名称" prop="appName">
          <Input placeholder="请输入关键字" type="text" v-model="pageInfo.appNameEn"/>
        </FormItem>
        <FormItem>
          <Button @click="handleSearch(1)" type="primary">查询</Button>&nbsp;
          <Button @click="handleResetForm('searchForm')">重置</Button>
        </FormItem>
      </Form>
      <div class="search-con search-con-top">
        <ButtonGroup>
          <Button :disabled="hasAuthority('systemAppEdit')?false:true" @click="handleModal()" class="search-btn"
                  type="primary">
            <span>添加</span>
          </Button>
        </ButtonGroup>
      </div>
      <Alert show-icon type="info">客户端模式,请授权相关接口资源。否则请求网关服务器将提示<code>"权限不足,拒绝访问!"</code></Alert>
      <Table :columns="columns" :data="data" :loading="loading" border>
        <template slot="status" slot-scope="{ row }">
          <Badge status="success" text="上线" v-if="row.status===1"/>
          <Badge status="error" text="下线" v-else=""/>
        </template>
        <template slot="appType" slot-scope="{ row }">
          <Tag color="blue" v-if="row.appType==='server'">服务器应用</Tag>
          <Tag color="blue" v-else-if="row.appType==='app'">手机应用</Tag>
          <Tag color="blue" v-else-if="row.appType==='pc'">PC网页应用</Tag>
          <Tag color="blue" v-else="">手机网页应用</Tag>
        </template>
        <template slot="action" slot-scope="{ row }">
          <a :disabled="row.appId != 'gateway' && hasAuthority('systemAppEdit') ?false:true" @click="handleModal(row)">
            编辑</a>&nbsp;
          <Dropdown @on-click="handleClick($event,row)" ref="dropdown" transfer v-show="hasAuthority('systemAppEdit')">
            <a :disabled="row.appId === 'gateway' ?true:false" href="javascript:void(0)">
              <span>更多</span>
              <Icon type="ios-arrow-down"></Icon>
            </a>
            <DropdownMenu slot="list">
              <DropdownItem name="resetSecret">重置密钥</DropdownItem>
              <DropdownItem name="remove">删除应用</DropdownItem>
            </DropdownMenu>
          </Dropdown>
        </template>
      </Table>
      <Page :current="pageInfo.page" :page-size="pageInfo.limit" :total="pageInfo.total" @on-change="handlePage" @on-page-size-change='handlePageSize'
            show-elevator
            show-sizer
            show-total transfer></Page>
    </Card>

    <Modal :title="modalTitle"
           @on-cancel="handleReset"
           v-model="modalVisible"
           width="40">
      <div>
        <Tabs :value="current" @on-click="handleTabClick">
          <TabPane label="应用信息" name="form1">
            <Form :label-width="135" :model="formItem" :rules="formItemRules" ref="form1" v-show="current=='form1'">
              <FormItem label="应用图标">
                <div class="upload-list" v-for="item in uploadList">
                  <template v-if="item.status === 'finished'">
                    <img :src="item.url">
                    <div class="upload-list-cover">
                      <Icon @click.native="handleView(item.name)" type="ios-eye-outline"></Icon>
                      <Icon @click.native="handleRemoveImg(item)" type="ios-trash-outline"></Icon>
                    </div>
                  </template>
                  <template v-else>
                    <Progress :percent="item.percentage" hide-info v-if="item.showProgress"></Progress>
                  </template>
                </div>
                <Upload
                  :before-upload="handleBeforeUpload"
                  :default-file-list="defaultList"
                  :format="['jpg','jpeg','png']"
                  :max-size="2048"
                  :on-exceeded-size="handleMaxSize"
                  :on-format-error="handleFormatError"
                  :on-success="handleSuccess"
                  :show-upload-list="false"
                  action="//jsonplaceholder.typicode.com/posts/"
                  ref="upload"
                  style="display: inline-block;width:58px;"
                  type="drag">
                  <div style="width: 58px;height:58px;line-height: 58px;">
                    <Icon size="20" type="ios-camera"></Icon>
                  </div>
                </Upload>
              </FormItem>
              <FormItem label="AppId">
                <Input disabled placeholder="请输入内容" v-model="formItem.appId"></Input>
              </FormItem>
              <FormItem label="开发者">
                <Select clearable filterable v-model="formItem.developerId">
                  <Option :label="item.userName" :title="item.userName" :value="item.userId"
                          @click.native="handleOnSelectUser(item)" v-for="item in selectUsers">
                    <span>{{ item.userName }}</span>
                  </Option>
                </Select>
              </FormItem>
              <FormItem label="应用名称" prop="appName">
                <Input placeholder="请输入内容" v-model="formItem.appName"></Input>
              </FormItem>
              <FormItem label="英文名称" prop="appNameEn">
                <Input placeholder="请输入内容" v-model="formItem.appNameEn"></Input>
              </FormItem>
              <FormItem label="应用类型" prop="appType">
                <Select @on-change="handleOnAppTypeChange" v-model="formItem.appType">
                  <Option value="server">服务器应用</Option>
                  <Option value="app">手机应用</Option>
                  <Option value="pc">PC网页应用</Option>
                  <Option value="wap">手机网页应用</Option>
                </Select>
              </FormItem>
              <FormItem label="操作系统" prop="appOs" v-if="formItem.appType === 'app'">
                <RadioGroup v-model="formItem.appOs">
                  <Radio label="ios">
                    <Icon type="logo-apple"></Icon>
                    <span>苹果IOS</span>
                  </Radio>
                  <Radio label="android">
                    <Icon type="logo-android"></Icon>
                    <span>安卓Android</span>
                  </Radio>
                </RadioGroup>
              </FormItem>
              <FormItem label="应用官网" prop="website">
                <Input placeholder="请输入内容" v-model="formItem.website"></Input>
              </FormItem>
              <FormItem label="状态">
                <RadioGroup type="button" v-model="formItem.status">
                  <Radio label="0">下线</Radio>
                  <Radio label="1">上线</Radio>
                </RadioGroup>
              </FormItem>
              <FormItem label="描述">
                <Input placeholder="请输入内容" type="textarea" v-model="formItem.appDesc"></Input>
              </FormItem>
            </Form>
          </TabPane>
          <TabPane :disabled="!formItem.appId" label="开发信息" name="form2">
            <Form :label-width="135" :model="formItem" :rules="formItemRules" ref="form2" v-show="current=='form2'">
              <FormItem label="ApiKey">
                <Input disabled placeholder="请输入内容" v-model="formItem.apiKey"></Input>
              </FormItem>
              <FormItem label="SecretKey">
                <Input disabled placeholder="请输入内容" v-model="formItem.secretKey"></Input>
              </FormItem>
              <FormItem label="授权类型" prop="grantTypes">
                <CheckboxGroup v-model="formItem.grantTypes">
                  <Tooltip :content="item.desc" v-for="item in selectGrantTypes">
                    <Checkbox :label="item.label"><span>{{ item.title }}</span></Checkbox>
                  </Tooltip>
                </CheckboxGroup>
              </FormItem>
              <FormItem label="用户授权范围" prop="scopes">
            <span slot="label">用户授权范围
            <Tooltip content="提醒用户确认授权可访问的资源">
              <Icon size="16" type="ios-alert"/>
            </Tooltip>
            </span>
                <CheckboxGroup v-model="formItem.scopes">
                  <Checkbox :label="item.label" v-for="item in selectScopes"><span>{{ item.title }}</span>
                  </Checkbox>
                </CheckboxGroup>
              </FormItem>
              <FormItem label="自动授权范围">
            <span slot="label">自动授权范围
              <Tooltip content="不再提醒用户确认授权可访问的资源">
              <Icon size="16" type="ios-alert"/>
            </Tooltip>
            </span>
                <CheckboxGroup v-model="formItem.autoApproveScopes">
                  <Checkbox :label="item.label" v-for="item in selectScopes"><span>{{ item.title }}</span>
                  </Checkbox>
                </CheckboxGroup>
              </FormItem>
              <FormItem label="令牌有效期" prop="accessTokenValidity">
                <RadioGroup type="button" v-model="formItem.tokenValidity">
                  <Radio label="1">设置有效期</Radio>
                  <Radio label="0">不限制</Radio>
                </RadioGroup>
              </FormItem>
              <FormItem label="访问令牌有效期" prop="accessTokenValidity" v-show="formItem.tokenValidity === '1'">
                <InputNumber :min="900" v-model="formItem.accessTokenValidity"></InputNumber>
                <span>&nbsp;&nbsp;秒</span>
              </FormItem>
              <FormItem label="刷新令牌有效期" prop="refreshTokenValidity" v-show="formItem.tokenValidity === '1'">
                <InputNumber :min="900" v-model="formItem.refreshTokenValidity"></InputNumber>
                <span>&nbsp;&nbsp;秒</span>
              </FormItem>
              <FormItem label="第三方登陆回调地址" prop="redirectUrls">
                <Input placeholder="请输入内容" type="textarea" v-model="formItem.redirectUrls"></Input>
                <span>多个地址使用,逗号隔开</span>
              </FormItem>
            </Form>
          </TabPane>
          <TabPane :disabled="!formItem.appId" label="分配权限" name="form3">
            <Form :label-width="100" :model="formItem" :rules="formItemRules" ref="form3" v-show="current=='form3'">
              <FormItem label="过期时间" prop="expireTime">
                <Badge text="授权已过期" v-if="formItem.isExpired">
                  <DatePicker class="ivu-form-item-error" placeholder="授权有效期" type="datetime"
                              v-model="formItem.expireTime"></DatePicker>
                </Badge>
                <DatePicker placeholder="设置有效期" type="datetime" v-else="" v-model="formItem.expireTime"></DatePicker>
              </FormItem>
              <FormItem label="功能接口" prop="authorities">
                <Transfer
                  :data="selectApis"
                  :list-style="{width: '45%',height: '480px'}"
                  :render-format="transferRender"
                  :target-keys="formItem.authorities"
                  :titles="['选择接口', '已选择接口']"
                  @on-change="handleTransferChange"
                  filterable>
                </Transfer>
              </FormItem>
            </Form>
          </TabPane>
        </Tabs>
        <div class="drawer-footer">
          <Button @click="handleReset" type="default">取消</Button>&nbsp;
          <Button :loading="saving" @click="handleSubmit" type="primary">保存</Button>
        </div>
      </div>
    </Modal>
  </div>
</template>

<script>
    import {addApp, getAppClientInfo, getApps, removeApp, restApp, updateApp, updateAppClientInfo} from '@/api/app'
    import {getAllDevelopers} from '@/api/developer'
    import {getAuthorityApi, getAuthorityApp, grantAuthorityApp} from '@/api/authority'

    export default {
        name: 'SystemApp',
        data() {
            const validateEn = (rule, value, callback) => {
                let reg = /^[-_a-zA-Z0-9]+$/
                if (value === '') {
                    callback(new Error('英文不能为空'))
                } else if (value !== '' && !reg.test(value)) {
                    callback(new Error('只允许字母、数字、下划线、横线'))
                } else {
                    callback()
                }
            }
            return {
                loading: false,
                saving: false,
                current: 'form1',
                forms: [
                    'form1',
                    'form2',
                    'form3'
                ],
                selectApis: [],
                selectUsers: [],
                selectGrantTypes: [
                    {label: 'authorization_code', title: '授权码模式', desc: 'Web服务端应用与第三方移动App应用'},
                    {label: 'client_credentials', title: '客户端模式', desc: '没有用户参与的,内部服务端与第三方服务端'},
                    {label: 'password', title: '密码模式', desc: '内部Web网页应用与内部移动App应用'},
                    {label: 'implicit', title: '简化模式', desc: 'Web网页应用或第三方移动App应用'},
                    {label: 'refresh_token', title: '刷新令牌', desc: '刷新并延迟访问令牌时长'},
                ],
                selectScopes: [
                    {label: 'userProfile', title: '允许访问基本信息'},
                    // 这是测试数据,自定义对应接口权限标识
                    {label: 'api1', title: '允许访问....自定义信息'},
                ],
                pageInfo: {
                    total: 0,
                    page: 1,
                    limit: 10,
                    appId: '',
                    appName: '',
                    appNameEn: ''
                },
                defaultList: [
                    {
                        'name': '',
                        'url': ''
                    }
                ],
                modalVisible: false,
                modalTitle: '',
                imgName: '',
                visible: false,
                uploadList: [],
                formItemRules: {
                    website: [
                        {type: 'url', message: '请输入有效网址', trigger: 'blur'}
                    ],
                    appType: [
                        {required: true, message: '应用类型不能为空', trigger: 'blur'}
                    ],
                    appOs: [
                        {required: true, message: '操作系统不能为空', trigger: 'blur'}
                    ],
                    redirectUrls: [
                        {required: true, message: '授权重定向地址不能为空', trigger: 'blur'}
                    ],
                    appName: [
                        {required: true, message: '应用名称不能为空', trigger: 'blur'}
                    ],
                    appNameEn: [
                        {required: true, validator: validateEn, trigger: 'blur'}
                    ],
                    grantTypes: [
                        {required: true, type: 'array', min: 1, message: '授权类型不能为空', trigger: 'blur'}
                    ],
                    scopes: [
                        {required: true, type: 'array', min: 1, message: '用户授权范围不能为空', trigger: 'blur'}
                    ],
                    accessTokenValidity: [
                        {required: true, type: 'integer', min: 900, message: '访问令牌有效期不能少于900', trigger: 'blur'}
                    ],
                    refreshTokenValidity: [
                        {required: true, type: 'integer', min: 900, message: '刷新令牌有效期不能少于900', trigger: 'blur'}
                    ],
                },
                formItem: {
                    appId: '',
                    apiKey: '',
                    secretKey: '',
                    appName: '',
                    appNameEn: '',
                    appType: 'server',
                    appIcon: '',
                    appOs: '',
                    path: '',
                    website: '',
                    appDesc: '',
                    status: 1,
                    redirectUrls: '',
                    developerId: '',
                    scopes: ['userProfile'],
                    autoApproveScopes: [],
                    authorities: [],
                    grantTypes: [],
                    accessTokenValidity: 43200,
                    refreshTokenValidity: 2592000,
                    expireTime: '',
                    isExpired: false,
                    tokenValidity: '1'
                },
                columns: [
                    {
                        type: 'selection',
                        width: 60,
                        align: 'center'
                    },
                    {
                        title: '应用名称',
                        key: 'appName',
                        width: 200
                    },
                    {
                        title: 'AppId',
                        key: 'appId',
                        width: 200
                    },
                    {
                        title: 'ApiKey',
                        key: 'apiKey',
                        width: 250
                    },
                    {
                        title: 'SecretKey',
                        key: 'secretKey',
                        width: 300
                    },
                    {
                        title: '开发者',
                        key: 'userName',
                        width: 200
                    },
                    {
                        title: '应用类型',
                        slot: 'appType',
                        width: 180,
                        filters: [
                            {
                                label: '服务器应用',
                                value: 0
                            },
                            {
                                label: '手机应用',
                                value: 1
                            },
                            {
                                label: 'PC网页应用',
                                value: 2
                            },
                            {
                                label: '手机网页应用',
                                value: 3
                            }
                        ],
                        filterMultiple: false,
                        filterMethod(value, row) {
                            if (value === 0) {
                                return row.appType === 'server'
                            } else if (value === 1) {
                                return row.appType === 'app'
                            } else if (value === 2) {
                                return row.appType === 'pc'
                            } else if (value === 3) {
                                return row.appType === 'wap'
                            }
                        }
                    },
                    {
                        title: '状态',
                        slot: 'status',
                        key: 'status'
                    },
                    {
                        title: '操作',
                        slot: 'action',
                        fixed: 'right',
                        width: 120
                    }
                ],
                data: []
            }
        },
        methods: {
            handleModal(data) {
                if (data) {
                    this.formItem = Object.assign({}, this.formItem, data)
                    this.formItem.appId = data.appId
                }
                if (this.current === this.forms[0]) {
                    this.modalTitle = data ? '编辑应用 - ' + data.appName : '添加应用'
                    this.handleLoadUsers()
                }
                if (this.current === this.forms[1]) {
                    this.modalTitle = data ? '开发信息 - ' + data.appName : '开发信息'
                    this.handleLoadAppClientInfo(this.formItem.apiKey)
                }
                if (this.current === this.forms[2]) {
                    this.modalTitle = data ? '分配权限 - ' + data.appName : '分配权限'
                    this.handleLoadAppGranted(this.formItem.appId)
                }
                this.formItem.status = this.formItem.status + ''
            },
            handleResetForm(form) {
                this.$refs[form].resetFields()
            },
            handleReset() {
                //重置验证
                const newData = {
                    appId: '',
                    apiKey: '',
                    secretKey: '',
                    appName: '',
                    appNameEn: '',
                    appType: 'server',
                    appIcon: '',
                    appOs: '',
                    path: '',
                    website: '',
                    appDesc: '',
                    status: 1,
                    redirectUrls: '',
                    developerId: '',
                    scopes: ['userProfile'],
                    autoApproveScopes: [],
                    authorities: [],
                    grantTypes: [],
                    accessTokenValidity: 43200,
                    refreshTokenValidity: 2592000,
                    expireTime: '',
                    isExpired: false,
                    tokenValidity: '1'
                }
                this.formItem = newData
                this.forms.map(form => {
                    this.handleResetForm(form)
                })
                this.current = this.forms[0]
                this.saving = false
                this.modalVisible = false
            },
            handleSubmit() {
                if (this.current === this.forms[0]) {
                    this.$refs[this.current].validate((valid) => {
                        if (valid) {
                            this.saving = true
                            const data = Object.assign({}, this.formItem)
                            if (data.appId) {
                                updateApp(data).then(res => {
                                    if (res.rtnCode === '200') {
                                        this.$Message.success('保存成功')
                                    }
                                    this.handleReset()
                                    this.handleSearch()
                                }).finally(() => {
                                    this.saving = false
                                })
                            } else {
                                addApp(data).then(res => {
                                    if (res.rtnCode === '200') {
                                        this.$Message.success('保存成功')
                                    }
                                    this.handleReset()
                                    this.handleSearch()
                                }).finally(() => {
                                    this.saving = false
                                })
                            }
                        }
                    })
                }

                if (this.current === this.forms[1]) {
                    this.$refs[this.current].validate((valid) => {
                        if (valid) {
                            this.saving = true
                            if (this.formItem.tokenValidity === '0') {
                                this.formItem.accessTokenValidity = -1
                                this.formItem.refreshTokenValidity = -1
                            }
                            updateAppClientInfo(this.formItem).then(res => {
                                if (res.rtnCode === '200') {
                                    this.$Message.success('保存成功')
                                }
                                this.handleReset()
                                this.handleSearch()
                            }).finally(() => {
                                this.saving = false
                            })
                        }
                    })
                }

                if (this.current === this.forms[2]) {
                    this.$refs[this.current].validate((valid) => {
                        if (valid) {
                            this.saving = true
                            grantAuthorityApp({
                                appId: this.formItem.appId,
                                expireTime: this.formItem.expireTime ? this.formItem.expireTime.pattern('yyyy-MM-dd HH:mm:ss') : '',
                                authorityIds: this.formItem.authorities
                            }).then(res => {
                                if (res.rtnCode === '200') {
                                    this.$Message.success('授权成功')
                                    this.handleReset()
                                }
                                this.handleSearch()
                            }).finally(() => {
                                this.saving = false
                            })
                        }
                    })
                }
            },
            handleSearch(page) {
                if (page) {
                    this.pageInfo.page = page
                }
                this.loading = true
                getApps(this.pageInfo).then(res => {
                    this.data = res.data.records
                    this.pageInfo.total = parseInt(res.data.total)
                }).finally(() => {
                    this.loading = false
                })
            },
            handleRemove(data) {
                this.$Modal.confirm({
                    title: '删除后将无法恢复,确定删除吗？',
                    onOk: () => {
                        removeApp({appId: data.appId}).then(res => {
                            this.handleSearch()
                            if (res.rtnCode === '200') {
                                this.pageInfo.page = 1
                                this.$Message.success('删除成功')
                            }
                        })
                    }
                })
            },
            handleResetSecret(data) {
                this.$Modal.confirm({
                    title: '重置后将影响应用正常使用,确定重置吗？',
                    onOk: () => {
                        restApp({appId: data.appId}).then(res => {
                            if (res.rtnCode === '200') {
                                this.pageInfo.page = 1
                                this.formItem.secretKey = res.data
                                this.$Message.success('重置成功,请妥善保管.并及时更新到相关应用')
                            }
                            this.handleSearch()
                        })
                    }
                })
            },
            handleTabClick(name) {
                this.current = name
                this.handleModal();
            },
            handleClick(name, row) {
                switch (name) {
                    case 'remove':
                        this.handleRemove(row)
                        break
                    case 'resetSecret':
                        this.handleResetSecret(row)
                        break
                }
            },
            handleOnAppTypeChange(data) {
                if (data !== 'app') {
                    this.formItem.appOs = ''
                } else {
                    if (!this.formItem.appOs) {
                        this.formItem.appOs = 'ios'
                    }
                }
            },
            handleOnSelectUser(data) {
                this.formItem.developerId = data.userId
            },
            handlePage(current) {
                this.pageInfo.page = current
                this.handleSearch()
            }
            ,
            handlePageSize(size) {
                this.pageInfo.limit = size
                this.handleSearch()
            },
            handleLoadAppGranted(appId) {
                if (!appId) {
                    return
                }
                const that = this
                const p1 = getAuthorityApi('')
                const p2 = getAuthorityApp(appId)
                Promise.all([p1, p2]).then(function (values) {
                    let res1 = values[0]
                    let res2 = values[1]
                    if (res1.code === 0) {
                        res1.data.map(item => {
                            item.key = item.authorityId
                            item.label = `${item.prefix.replace('/**', '')}${item.path} - ${item.apiName}`
                            item.disabled = item.path === '/**'
                        })
                        that.selectApis = res1.data
                    }
                    if (res2.code === 0) {
                        let authorities = []
                        res2.data.map(item => {
                            if (item.authority.indexOf('APP_') === -1 && !authorities.includes(item.authorityId)) {
                                authorities.push(item.authorityId)
                            }
                        })
                        that.formItem.authorities = authorities;
                        // 时间
                        if (res2.data.length > 0) {
                            that.formItem.expireTime = res2.data[0].expireTime
                            that.formItem.isExpired = res2.data[0].isExpired
                        }
                    }
                    that.modalVisible = true
                })
            },
            handleLoadAppClientInfo(clientId) {
                if (!clientId) {
                    return
                }
                getAppClientInfo({clientId: clientId}).then(res => {
                    if (res.rtnCode === '200') {
                        this.formItem.scopes = res.data.scope ? res.data.scope : []
                        this.formItem.redirectUrls = res.data.redirect_uri ? res.data.redirect_uri.join(',') : ''
                        this.formItem.grantTypes = res.data.authorized_grant_types ? res.data.authorized_grant_types : []
                        this.formItem.accessTokenValidity = res.data.access_token_validity
                        this.formItem.refreshTokenValidity = res.data.refresh_token_validity
                        this.formItem.autoApproveScopes = res.data.autoapprove ? res.data.autoapprove : []
                        this.formItem.tokenValidity = this.formItem.accessTokenValidity === -1 ? '0' : '1';
                    }
                    this.modalVisible = true
                })
            },
            transferRender(item) {
                return `<span  title="${item.label}">${item.label}</span>`
            },
            handleTransferChange(newTargetKeys, direction, moveKeys) {
                if (newTargetKeys.indexOf('1') != -1) {
                    this.formItem.authorities = ['1']
                } else {
                    this.formItem.authorities = newTargetKeys
                }
            },
            handleLoadUsers() {
                getAllDevelopers().then(res => {
                    if (res.rtnCode === '200') {
                        this.selectUsers = res.data
                    }
                    this.modalVisible = true
                })
            },
            handleView(name) {
                this.imgName = name
                this.visible = true
            }
            ,
            handleRemoveImg(file) {
                const fileList = this.$refs.upload.fileList
                this.$refs.upload.fileList.splice(fileList.indexOf(file), 1)
            }
            ,
            handleSuccess(res, file) {
                file.url = ''
                file.name = ''
            }
            ,
            handleFormatError(file) {
                this.$Message.warning('图片支持png、jpg、jpeg')
            }
            ,
            handleMaxSize(file) {
                this.$Message.warning('图片大小不能超过2M.')
            }
            ,
            handleBeforeUpload() {
                const check = this.uploadList.length < 1
                if (!check) {
                    this.$Message.warning('只能上传一张.')
                }
                return check
            }
        },
        mounted: function () {
            this.handleSearch()
        }
    }
</script>
<style scoped>
  .upload-list {
    display: inline-block;
    width: 60px;
    height: 60px;
    text-align: center;
    line-height: 60px;
    border: 1px solid transparent;
    border-radius: 4px;
    overflow: hidden;
    background: #fff;
    position: relative;
    box-shadow: 0 1px 1px rgba(0, 0, 0, .2);
    margin-right: 4px;
  }

  .upload-list img {
    width: 100%;
    height: 100%;
  }

  .upload-list-cover {
    display: none;
    position: absolute;
    top: 0;
    bottom: 0;
    left: 0;
    right: 0;
    background: rgba(0, 0, 0, .6);
  }

  .upload-list:hover .upload-list-cover {
    display: block;
  }

  .upload-list-cover i {
    color: #fff;
    font-size: 20px;
    cursor: pointer;
    margin: 0 2px;
  }
</style>
