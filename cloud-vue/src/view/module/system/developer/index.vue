<template>
  <div>
    <Card>
      <Form :label-width="80"
            :model="pageInfo"
            inline
            ref="searchForm">
        <FormItem label="登录名" prop="userName">
          <Input placeholder="请输入关键字" type="text" v-model="pageInfo.userName"/>
        </FormItem>
        <FormItem label="手机号" prop="mobile">
          <Input placeholder="请输入关键字" type="text" v-model="pageInfo.mobile"/>
        </FormItem>
        <FormItem label="邮箱" prop="email">
          <Input placeholder="请输入关键字" type="text" v-model="pageInfo.email"/>
        </FormItem>
        <FormItem>
          <Button @click="handleSearch(1)" type="primary">查询</Button>&nbsp;
          <Button @click="handleResetForm('searchForm')">重置</Button>
        </FormItem>
      </Form>

      <div class="search-con search-con-top">
        <ButtonGroup>
          <Button :disabled="hasAuthority('developerEdit')?false:true" @click="handleModal()"
                  type="primary">
            <span>添加</span>
          </Button>
        </ButtonGroup>
      </div>

      <Table :columns="columns" :data="data" :loading="loading" border>
        <template slot="status" slot-scope="{ row }">
          <Badge status="success" text="正常" v-if="row.status===1"/>
          <Badge status="warning" text="锁定" v-else-if="row.status===2"/>
          <Badge status="error" text="禁用" v-else=""/>
        </template>
        <template slot="action" slot-scope="{ row }">
          <a :disabled="hasAuthority('developerEdit')?false:true" @click="handleModal(row)">编辑</a>&nbsp;
          <Dropdown @on-click="handleClick($event,row)" ref="dropdown" transfer v-show="hasAuthority('developerEdit')">
            <a href="javascript:void(0)">
              <span>更多</span>
              <Icon type="ios-arrow-down"></Icon>
            </a>
            <DropdownMenu slot="list">
              <DropdownItem name="sendToEmail">发送到密保邮箱</DropdownItem>
            </DropdownMenu>
          </Dropdown>&nbsp;
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
          <TabPane label="开发者信息" name="form1">
            <Form :label-width="100" :model="formItem" :rules="formItemRules" ref="form1" v-show="current == 'form1'">
              <FormItem label="开发者类型" prop="userType">
                <RadioGroup v-model="formItem.userType">
                  <Radio label="isp">服务提供商</Radio>
                  <Radio label="normal">自研开发者</Radio>
                </RadioGroup>
              </FormItem>
              <FormItem label="昵称" prop="nickName">
                <Input placeholder="请输入内容" v-model="formItem.nickName"></Input>
              </FormItem>
              <FormItem label="登录名" prop="userName">
                <Input :disabled="formItem.userId?true:false" placeholder="请输入内容" v-model="formItem.userName"></Input>
              </FormItem>
              <FormItem label="登录密码" prop="password" v-if="formItem.userId?false:true">
                <Input placeholder="请输入内容" type="password" v-model="formItem.password"></Input>
              </FormItem>
              <FormItem label="再次确认密码" prop="passwordConfirm" v-if="formItem.userId?false:true">
                <Input placeholder="请输入内容" type="password" v-model="formItem.passwordConfirm"></Input>
              </FormItem>
              <FormItem label="邮箱" prop="email">
                <Input placeholder="请输入内容" v-model="formItem.email"></Input>
              </FormItem>
              <FormItem label="手机号" prop="mobile">
                <Input placeholder="请输入内容" v-model="formItem.mobile"></Input>
              </FormItem>
              <FormItem label="状态">
                <RadioGroup type="button" v-model="formItem.status">
                  <Radio label="0">禁用</Radio>
                  <Radio label="1">正常</Radio>
                  <Radio label="2">锁定</Radio>
                </RadioGroup>
              </FormItem>
              <FormItem label="描述">
                <Input placeholder="请输入内容" type="textarea" v-model="formItem.userDesc"></Input>
              </FormItem>
            </Form>
          </TabPane>
          <TabPane :disabled="!formItem.userId" label="修改密码" name="form2">
            <Form :label-width="100" :model="formItem" :rules="formItemRules" ref="form2" v-show="current == 'form2'">
              <FormItem label="登录名" prop="userName">
                <Input :disabled="formItem.userId?true:false" placeholder="请输入内容" v-model="formItem.userName"></Input>
              </FormItem>
              <FormItem label="登录密码" prop="password">
                <Input placeholder="请输入内容" type="password" v-model="formItem.password"></Input>
              </FormItem>
              <FormItem label="再次确认密码" prop="passwordConfirm">
                <Input placeholder="请输入内容" type="password" v-model="formItem.passwordConfirm"></Input>
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
    import {addDeveloper, getDevelopers, updateDeveloper, updatePassword} from '@/api/developer'

    export default {
        name: 'SystemDeveloper',
        data() {

            const validateEn = (rule, value, callback) => {
                let reg = /^[_a-zA-Z0-9]+$/
                let reg2 = /^.{4,18}$/;
                // 长度为6到18个字符
                if (value === '') {
                    callback(new Error('登录名不能为空'))
                } else if (value !== '' && !reg.test(value)) {
                    callback(new Error('只允许字母、数字、下划线'))
                } else if (value !== '' && !reg2.test(value)) {
                    callback(new Error('长度6到18个字符'))
                } else {
                    callback()
                }
            }
            const validatePass = (rule, value, callback) => {
                let reg2 = /^.{6,18}$/;
                if (value === '') {
                    callback(new Error('请输入密码'))
                } else if (value !== this.formItem.password) {
                    callback(new Error('两次输入密码不一致'))
                } else if (value !== '' && !reg2.test(value)) {
                    callback(new Error('长度6到18个字符'))
                } else {
                    callback()
                }
            }

            const validatePassConfirm = (rule, value, callback) => {
                if (value === '') {
                    callback(new Error('请再次输入密码'))
                } else if (value !== this.formItem.password) {
                    callback(new Error('两次输入密码不一致'))
                } else {
                    callback()
                }
            }
            const validateMobile = (rule, value, callback) => {
                let reg = /^1\d{10}$/
                if (value !== '' && !reg.test(value)) {
                    callback(new Error('手机号码格式不正确'))
                } else {
                    callback()
                }
            }
            return {
                loading: false,
                saving: false,
                modalVisible: false,
                modalTitle: '',
                current: 'form1',
                forms: [
                    'form1',
                    'form2'
                ],
                selectMenus: [],
                selectRoles: [],
                pageInfo: {
                    page: 1,
                    pageSize: 10,
                    sort: "createdDate",
                    order: "desc"
                },
                formItemRules: {
                    userType: [
                        {required: true, message: '开发者类型不能为空', trigger: 'blur'}
                    ],
                    userName: [
                        {required: true, message: '开发者名不能为空', trigger: 'blur'},
                        {required: true, validator: validateEn, trigger: 'blur'}
                    ],
                    password: [
                        {required: true, validator: validatePass, trigger: 'blur'}
                    ],
                    passwordConfirm: [
                        {required: true, validator: validatePassConfirm, trigger: 'blur'}
                    ],
                    nickName: [
                        {required: true, message: '昵称不能为空', trigger: 'blur'}
                    ],
                    email: [
                        {required: false, type: 'email', message: '邮箱格式不正确', trigger: 'blur'}
                    ]
                    ,
                    mobile: [
                        {validator: validateMobile, trigger: 'blur'}
                    ]
                },
                formItem: {
                    userId: '',
                    userName: '',
                    nickName: '',
                    password: '',
                    passwordConfirm: '',
                    status: 1,
                    companyId: '',
                    email: '',
                    mobile: '',
                    userType: 'isp',
                    userDesc: '',
                    avatar: '',
                    grantRoles: [],
                    grantActions: [],
                    grantMenus: [],
                    expireTime: '',
                    isExpired: false
                },
                columns: [
                    {
                        type: 'selection',
                        width: 60,
                    },
                    {
                        title: '登录名',
                        key: 'userName',
                        width: 200
                    },
                    {
                        title: '昵称',
                        key: 'nickName',
                        width: 150
                    },
                    {
                        title: '邮箱',
                        key: 'email',
                        width: 200
                    },
                    {
                        title: '手机号',
                        key: 'mobile',
                        width: 200
                    },
                    {
                        title: '状态',
                        slot: 'status',
                        key: 'status',
                        width: 100
                    },
                    {
                        title: '开发者类型',
                        key: 'userType',
                        width: 150
                    },
                    {
                        title: '注册时间',
                        key: 'createdDate',
                        width: 180
                    },
                    {
                        title: '描述',
                        key: 'userDesc'
                    },
                    {
                        title: '操作',
                        slot: 'action',
                        fixed: 'right',
                        width: 150
                    }
                ],
                data: []
            }
        },
        methods: {
            handleModal(data) {
                if (data) {
                    this.formItem = Object.assign({}, this.formItem, data)
                }
                if (this.current === this.forms[0]) {
                    this.modalTitle = data ? '编辑开发者 - ' + data.userName : '添加开发者'
                    this.modalVisible = true
                }
                if (this.current === this.forms[1]) {
                    this.modalTitle = data ? '修改密码 - ' + data.userName : '修改密码'
                    this.modalVisible = true
                }
                this.formItem.status = this.formItem.status + ''
            },
            handleResetForm(form) {
                this.$refs[form].resetFields()
            },
            handleReset() {
                const newData = {
                    userId: '',
                    userName: '',
                    nickName: '',
                    password: '',
                    passwordConfirm: '',
                    status: 1,
                    companyId: '',
                    email: '',
                    mobile: '',
                    userType: 'isp',
                    userDesc: '',
                    avatar: '',
                    grantRoles: [],
                    grantMenus: [],
                    grantActions: [],
                    expireTime: '',
                    isExpired: false
                }
                this.formItem = newData
                //重置验证
                this.forms.map(form => {
                    this.handleResetForm(form)
                })
                this.current = this.forms[0]
                this.formItem.grantMenus = []
                this.formItem.grantActions = []
                this.modalVisible = false
                this.saving = false
            },
            handleSubmit() {
                if (this.current === this.forms[0]) {
                    this.$refs[this.current].validate((valid) => {
                        if (valid) {
                            this.saving = true
                            if (this.formItem.userId) {
                                updateDeveloper(this.formItem).then(res => {
                                    if (res.code === 0) {
                                        this.$Message.success('保存成功')
                                        this.handleReset()
                                    }
                                    this.handleSearch()
                                }).finally(() => {
                                    this.saving = false
                                })
                            } else {
                                addDeveloper(this.formItem).then(res => {
                                    if (res.code === 0) {
                                        this.$Message.success('保存成功')
                                        this.handleReset()
                                    }
                                    this.handleSearch()
                                }).finally(() => {
                                    this.saving = false
                                })
                            }
                        }
                    })
                }

                if (this.current === this.forms[1] && this.formItem.userId) {
                    this.$refs[this.current].validate((valid) => {
                        if (valid) {
                            this.saving = true
                            updatePassword({
                                userId: this.formItem.userId,
                                password: this.formItem.password
                            }).then(res => {
                                if (res.code === 0) {
                                    this.$Message.success('修改成功')
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
                getDevelopers(this.pageInfo).then(res => {
                    this.data = res.data.records
                    this.pageInfo.total = parseInt(res.data.total)
                }).finally(() => {
                    this.loading = false
                })
            },
            handlePage(current) {
                this.pageInfo.page = current
                this.handleSearch()
            },
            handlePageSize(size) {
                this.pageInfo.limit = size
                this.handleSearch()
            },
            handleClick(name, row) {
                switch (name) {
                    case'sendToEmail':
                        this.$Message.warning("发送至密保邮箱,开发中...")
                        break
                }
            },
            handleTabClick(name) {
                this.current = name
                this.handleModal();
            }
        },
        mounted: function () {
            this.handleSearch()
        }
    }
</script>
