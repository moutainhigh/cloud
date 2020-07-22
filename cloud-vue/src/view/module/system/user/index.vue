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
          <Button :disabled="hasAuthority('systemUserEdit')?false:true" @click="handleModal()"
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
          <a :disabled="hasAuthority('systemUserEdit')?false:true" @click="handleModal(row)">编辑</a>&nbsp;
          <Dropdown @on-click="handleClick($event,row)" ref="dropdown" transfer v-show="hasAuthority('systemUserEdit')">
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
          <TabPane label="用户信息" name="form1">
            <Form :label-width="100" :model="formItem" :rules="formItemRules" ref="form1" v-show="current == 'form1'">
              <FormItem label="用户类型" prop="userType">
                <RadioGroup v-model="formItem.userType">
                  <Radio label="super">超级管理员</Radio>
                  <Radio label="normal">普通管理员</Radio>
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
          <TabPane :disabled="!formItem.userId" label="分配角色" name="form2">
            <Form :label-width="100" :model="formItem" :rules="formItemRules" ref="form2" v-show="current == 'form2'">
              <FormItem label="分配角色" prop="grantRoles">
                <CheckboxGroup v-model="formItem.grantRoles">
                  <Checkbox :label="item.roleId" v-for="item in selectRoles"><span>{{ item.roleName }}</span></Checkbox>
                </CheckboxGroup>
              </FormItem>
            </Form>
          </TabPane>
          <TabPane :disabled="!formItem.userId" label="分配权限" name="form3">
            <Alert show-icon type="info">
              支持用户单独分配功能权限<code>(除角色已经分配菜单功能,禁止勾选!)</code></Alert>
            <Form :label-width="100" :model="formItem" :rules="formItemRules" ref="form3" v-show="current == 'form3'">
              <FormItem label="过期时间" prop="expireTime">
                <Badge text="授权已过期" v-if="formItem.isExpired">
                  <DatePicker class="ivu-form-item-error" placeholder="设置有效期" type="datetime"
                              v-model="formItem.expireTime"></DatePicker>
                </Badge>
                <DatePicker placeholder="设置有效期" type="datetime" v-else="" v-model="formItem.expireTime"></DatePicker>
              </FormItem>
              <FormItem label="功能菜单" prop="grantMenus">
                <tree-table
                  :columns="columns2"
                  :data="selectMenus"
                  :expand-type="false"
                  :is-fold="false"
                  :selectable="true"
                  :tree-type="true"
                  expand-key="menuName"
                  ref="tree"
                  style="max-height:450px;overflow: auto">
                  <template slot="operation" slot-scope="scope">
                    <CheckboxGroup v-model="formItem.grantActions">
                      <Checkbox :disabled="item.disabled" :label="item.authorityId"
                                v-for="item in scope.row.actionList">
                        <span :title="item.actionDesc">{{item.actionName}}</span>
                      </Checkbox>
                    </CheckboxGroup>
                  </template>
                </tree-table>
              </FormItem>
            </Form>
          </TabPane>
          <TabPane :disabled="!formItem.userId" label="修改密码" name="form4">
            <Form :label-width="100" :model="formItem" :rules="formItemRules" ref="form4" v-show="current == 'form4'">
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
    import {addUser, addUserRoles, getUserRoles, getUsers, updatePassword, updateUser} from '@/api/user'
    import {getAllRoles} from '@/api/role'
    import {listConvertTree} from '@/libs/util'
    import {getAuthorityMenu, getAuthorityUser, grantAuthorityUser} from '@/api/authority'

    export default {
        name: 'SystemUser',
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
            };
            return {
                loading: false,
                saving: false,
                modalVisible: false,
                modalTitle: '',
                current: 'form1',
                forms: [
                    'form1',
                    'form2',
                    'form3',
                    'form4'
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
                        {required: true, message: '用户类型不能为空', trigger: 'blur'}
                    ],
                    userName: [
                        {required: true, message: '用户名不能为空', trigger: 'blur'},
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
                    ],
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
                    userType: 'normal',
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
                        title: '用户类型',
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
                columns2: [
                    {
                        title: '菜单',
                        key: 'menuName',
                        minWidth: '250px',
                    },
                    {
                        title: '操作',
                        type: 'template',
                        template: 'operation',
                        minWidth: '200px'
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
                    this.modalTitle = data ? '编辑用户 - ' + data.userName : '添加用户'
                    this.modalVisible = true
                }
                if (this.current === this.forms[1]) {
                    this.modalTitle = data ? '分配角色 - ' + data.userName : '分配角色'
                    this.handleLoadRoles(this.formItem.userId)
                }
                if (this.current === this.forms[2]) {
                    this.modalTitle = data ? '分配权限 - ' + data.userName : '分配权限'
                    this.handleLoadUserGranted(this.formItem.userId)
                }
                if (this.current === this.forms[3]) {
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
                    userType: 'normal',
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
                                updateUser(this.formItem).then(res => {
                                    if (res.rtnCode === '200') {
                                        this.$Message.success('保存成功')
                                        this.handleReset()
                                    }
                                    this.handleSearch()
                                }).finally(() => {
                                    this.saving = false
                                })
                            } else {
                                addUser(this.formItem).then(res => {
                                    if (res.rtnCode === '200') {
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
                            addUserRoles(this.formItem).then(res => {
                                if (res.rtnCode === '200') {
                                    this.$Message.success('分配角色成功')
                                    this.handleReset()
                                }
                                this.handleSearch()
                            }).finally(() => {
                                this.saving = false
                            })
                        }
                    })
                }

                if (this.current === this.forms[2] && this.formItem.userId) {
                    this.$refs[this.current].validate((valid) => {
                        if (valid) {
                            const authorityIds = this.getCheckedAuthorities()
                            this.saving = true
                            grantAuthorityUser({
                                userId: this.formItem.userId,
                                expireTime: this.formItem.expireTime ? this.formItem.expireTime.pattern('yyyy-MM-dd HH:mm:ss') : '',
                                authorityIds: authorityIds
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

                if (this.current === this.forms[3] && this.formItem.userId) {
                    this.$refs[this.current].validate((valid) => {
                        if (valid) {
                            this.saving = true
                            updatePassword({
                                userId: this.formItem.userId,
                                password: this.formItem.password
                            }).then(res => {
                                if (res.rtnCode === '200') {
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
                getUsers(this.pageInfo).then(res => {
                    this.data = res.data.records
                    this.pageInfo.total = parseInt(res.data.total)
                }).finally(() => {
                    this.loading = false
                })
            },
            getCheckedAuthorities() {
                const menus = this.$refs['tree'].getCheckedProp('authorityId')
                return menus.concat(this.formItem.grantActions)
            },
            handleLoadUserGranted(userId) {
                const that = this
                const p1 = getAuthorityMenu()
                const p2 = getAuthorityUser(userId)
                const roleAuthorites = []
                Promise.all([p1, p2]).then(function (values) {
                    let res1 = values[0]
                    let res2 = values[1]
                    if (res1.code === 0 && res1.data) {
                        let opt = {
                            primaryKey: 'menuId',
                            parentKey: 'parentId',
                            startPid: '0'
                        }
                        if (res2.code === 0 && res2.data && res2.data.length > 0) {
                            let menus = []
                            let actions = []
                            res2.data.map(item => {
                                if (item.owner === 'role') {
                                    roleAuthorites.push(item.authorityId);
                                }
                                // 菜单权限
                                if (item.authority.indexOf('MENU_') != -1 && !menus.includes(item.authorityId)) {
                                    menus.push(item.authorityId)
                                }
                                // 操作权限
                                if (item.authority.indexOf('ACTION_') != -1 && !actions.includes(item.authorityId)) {
                                    actions.push(item.authorityId)
                                }
                            })
                            that.formItem.grantMenus = menus;
                            that.formItem.grantActions = actions;
                            // 时间
                            if (res2.data.length > 0) {
                                that.formItem.expireTime = res2.data[0].expireTime
                                that.formItem.isExpired = res2.data[0].isExpired
                            }
                        }
                        res1.data.map(item => {
                            // 菜单选中
                            if (that.formItem.grantMenus.includes(item.authorityId)) {
                                item._isChecked = true
                                // 归属角色权限,禁止授权
                                if (roleAuthorites.includes(item.authorityId)) {
                                    // 插件不支持,禁用
                                    item.disabled = true
                                    item.menuName += ' (禁止勾选)'
                                }
                            }

                            // 功能权限
                            item.actionList.map(action => {
                                if (roleAuthorites.includes(action.authorityId)) {
                                    action.disabled = true
                                }
                            })
                        })
                        that.selectMenus = listConvertTree(res1.data, opt)
                    }
                    that.modalVisible = true
                })
            },
            handleLoadRoles(userId) {
                if (!userId) {
                    return
                }
                const that = this
                const p1 = getAllRoles()
                const p2 = getUserRoles(userId)
                Promise.all([p1, p2]).then(function (values) {
                    let res1 = values[0]
                    let res2 = values[1]
                    if (res1.code === 0) {
                        that.selectRoles = res1.data
                    }
                    if (res2.code === 0) {
                        let result = []
                        res2.data.map(item => {
                            result.push(item.roleId)
                        })
                        that.formItem.grantRoles = result
                    }
                    that.modalVisible = true
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
