<template>
  <div>
    <Card shadow>
      <Form :label-width="80"
            :model="pageInfo"
            inline
            ref="searchForm">
        <FormItem label="角色名称" prop="roleName">
          <Input placeholder="请输入关键字" type="text" v-model="pageInfo.roleName"/>
        </FormItem>
        <FormItem label="角色编码" prop="roleCode">
          <Input placeholder="请输入关键字" type="text" v-model="pageInfo.roleCode"/>
        </FormItem>
        <FormItem>
          <Button @click="handleSearch(1)" type="primary">查询</Button>&nbsp;
          <Button @click="handleResetForm('searchForm')">重置</Button>
        </FormItem>
      </Form>
      <div class="search-con search-con-top">
        <ButtonGroup>
          <Button :disabled="hasAuthority('systemRoleEdit')?false:true" @click="handleModal()" class="search-btn"
                  type="primary">
            <span>添加</span>
          </Button>
        </ButtonGroup>
      </div>
      <Table size="small" :columns="columns" :data="data" :loading="loading" border>
        <template slot="status" slot-scope="{ row }">
          <Badge status="success" text="启用" v-if="row.status===1"/>
          <Badge status="error" text="禁用" v-else=""/>
        </template>
        <template slot="action" slot-scope="{ row }">
          <a :disabled="row.roleCode != 'all' && hasAuthority('systemRoleEdit')?false:true"
             @click="handleModal(row)">编辑</a>&nbsp;
          <Dropdown @on-click="handleClick($event,row)" ref="dropdown" transfer v-show="hasAuthority('systemRoleEdit')">
            <a :disabled="row.roleCode === 'all' ?true:false" href="javascript:void(0)">
              <span>更多</span>
              <Icon type="ios-arrow-down"></Icon>
            </a>
            <DropdownMenu slot="list">
              <DropdownItem name="remove">删除角色</DropdownItem>
            </DropdownMenu>
          </Dropdown>&nbsp;
        </template>
      </Table>
      <Page :current="pageInfo.page" :page-size="pageInfo.limit" :total="pageInfo.total" @on-change="handlePage"
            @on-page-size-change='handlePageSize'
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
          <TabPane label="角色信息" name="form1">
            <Form :label-width="100" :model="formItem" :rules="formItemRules" ref="form1" v-show="current == 'form1'">
              <FormItem label="角色标识" prop="roleCode">
                <Input placeholder="请输入内容" v-model="formItem.roleCode"></Input>
              </FormItem>
              <FormItem label="角色名称" prop="roleName">
                <Input placeholder="请输入内容" v-model="formItem.roleName"></Input>
              </FormItem>
              <FormItem label="状态">
                <RadioGroup type="button" v-model="formItem.status">
                  <Radio label="0">禁用</Radio>
                  <Radio label="1">启用</Radio>
                </RadioGroup>
              </FormItem>
              <FormItem label="描述">
                <Input placeholder="请输入内容" type="textarea" v-model="formItem.roleDesc"></Input>
              </FormItem>
            </Form>
          </TabPane>
          <TabPane :disabled="!formItem.roleId" label="分配权限" name="form2">
            <Form :label-width="100" :model="formItem" :rules="formItemRules" ref="form2" v-show="current == 'form2'">
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
                    style="max-height:480px;overflow: auto">
                  <template slot="operation" slot-scope="scope">
                    <CheckboxGroup v-model="formItem.grantActions">
                      <Checkbox :label="item.authorityId" v-for="item in scope.row.actionList">
                        <span :title="item.actionDesc">{{ item.actionName }}</span>
                      </Checkbox>
                    </CheckboxGroup>
                  </template>
                </tree-table>
              </FormItem>
            </Form>
          </TabPane>
          <TabPane :disabled="!formItem.roleId" label="角色成员" name="form3">
            <Form :model="formItem" :rules="formItemRules" ref="form3" v-show="current == 'form3'">
              <FormItem prop="authorities">
                <Transfer
                    :data="selectUsers"
                    :list-style="{width: '45%',height: '480px'}"
                    :render-format="transferRender"
                    :target-keys="formItem.userIds"
                    :titles="['选择用户', '已选择用户']"
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
import {addRole, addRoleUsers, getRoles, getRoleUsers, removeRole, updateRole} from '@/api/role'
import {getAllUsers} from '@/api/user'
import {getAuthorityMenu, getAuthorityRole, grantAuthorityRole} from '@/api/authority'
import {listConvertTree} from '@/libs/util'

export default {
  name: 'SystemRole',
  data() {
    const validateEn = (rule, value, callback) => {
      let reg = /^[_a-zA-Z0-9]+$/
      if (value === '') {
        callback(new Error('角色标识不能为空'))
      } else if (value !== '' && !reg.test(value)) {
        callback(new Error('只允许字母、数字、下划线'))
      } else {
        callback()
      }
    };
    return {
      titles: ['选择接口', '已选择接口'],
      listStyle: {
        width: '240px',
        height: '500px'
      },
      loading: false,
      modalVisible: false,
      modalTitle: '',
      saving: false,
      current: 'form1',
      forms: [
        'form1',
        'form2',
        'form3'
      ],
      selectApis: [],
      selectMenus: [],
      selectUsers: [],
      pageInfo: {
        total: 0,
        page: 1,
        limit: 10,
        roleCode: '',
        roleName: ''
      },
      formItemRules: {
        roleCode: [
          {required: true, validator: validateEn, trigger: 'blur'}
        ],
        roleName: [
          {required: true, message: '角色名称不能为空', trigger: 'blur'}
        ]
      },
      formItem: {
        roleId: '',
        roleCode: '',
        roleName: '',
        path: '',
        status: 1,
        menuId: '',
        priority: 0,
        roleDesc: '',
        grantMenus: [],
        grantActions: [],
        expireTime: '',
        isExpired: false,
        userIds: []
      },
      columns: [
        {
          title: '角色名称',
          key: 'roleName',
          width: 200,
        },
        {
          title: '角色标识',
          key: 'roleCode',
          width: 100,
        },
        {
          title: '状态',
          slot: 'status',
          key: 'status',
          width: 100,
          filters: [
            {
              label: '禁用',
              value: 0
            },
            {
              label: '启用',
              value: 1
            }
          ],
          filterMultiple: false,
          filterMethod(value, row) {
            if (value === 0) {
              return row.status === 0
            } else if (value === 1) {
              return row.status === 1
            }
          }
        },
        {
          title: '描述',
          key: 'roleDesc'
        },
        {
          title: '最后修改时间',
          key: 'lastModifiedDate',
          width: 200,
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
        this.modalTitle = data ? '编辑角色 - ' + data.roleName : '添加角色'
        this.modalVisible = true
      }
      if (this.current === this.forms[1]) {
        this.modalTitle = data ? '分配权限 - ' + data.roleName : '分配权限'
        this.handleLoadRoleGranted(this.formItem.roleId)
      }
      if (this.current === this.forms[2]) {
        this.modalTitle = data ? '角色成员 - ' + data.roleName : '角色成员'
        this.handleLoadRoleUsers(this.formItem.roleId)
      }
      this.formItem.status = this.formItem.status + ''
    },
    handleResetForm(form) {
      this.$refs[form].resetFields()
    },
    handleTabClick(name) {
      this.current = name
      this.handleModal();
    },
    handleReset() {
      const newData = {
        roleId: '',
        roleCode: '',
        roleName: '',
        path: '',
        status: 1,
        menuId: '',
        priority: 0,
        roleDesc: '',
        grantMenus: [],
        grantActions: [],
        expireTime: '',
        isExpired: false,
        userIds: []
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
            if (this.formItem.roleId) {
              updateRole(this.formItem).then(res => {
                if (res.rtnCode === '200') {
                  this.$Message.success('保存成功')
                  this.handleReset()
                }
                this.handleSearch()
              }).finally(() => {
                this.saving = false
              })
            } else {
              addRole(this.formItem).then(res => {
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

      if (this.current === this.forms[1]) {
        this.$refs[this.current].validate((valid) => {
          if (valid) {
            const authorityIds = this.getCheckedAuthorities()
            this.saving = true
            grantAuthorityRole({
              roleId: this.formItem.roleId,
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

      if (this.current === this.forms[2]) {
        this.$refs[this.current].validate((valid) => {
          if (valid) {
            this.saving = true
            addRoleUsers({
              roleId: this.formItem.roleId,
              userIds: this.formItem.userIds
            }).then(res => {
              if (res.rtnCode === '200') {
                this.$Message.success('保存成功')
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
      getRoles(this.pageInfo).then(res => {
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
    handleRemove(data) {
      this.$Modal.confirm({
        title: '确定删除吗？',
        onOk: () => {
          removeRole(data.roleId).then(res => {
            if (res.rtnCode === '200') {
              this.pageInfo.page = 1
              this.$Message.success('删除成功')
            }
            this.handleSearch()
          })
        }
      })
    },
    getCheckedAuthorities() {
      const menus = this.$refs['tree'].getCheckedProp('authorityId')
      return menus.concat(this.formItem.grantActions)
    },
    handleLoadRoleGranted(roleId) {
      if (!roleId) {
        return
      }
      const that = this
      const p1 = getAuthorityMenu()
      const p2 = getAuthorityRole(roleId)
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
            }
          })
          that.selectMenus = listConvertTree(res1.data, opt)
        }
        that.modalVisible = true
      })
    },
    handleLoadRoleUsers(roleId) {
      if (!roleId) {
        return
      }
      const that = this
      const p1 = getAllUsers()
      const p2 = getRoleUsers(roleId)
      Promise.all([p1, p2]).then(function (values) {
        let res1 = values[0]
        let res2 = values[1]
        if (res1.code === 0) {
          res1.data.map(item => {
            item.key = item.userId
            item.label = `${item.userName}(${item.nickName})`
          })
          that.selectUsers = res1.data
        }
        if (res2.code === 0) {
          let userIds = []
          res2.data.map(item => {
            if (!userIds.includes(item.userId)) {
              userIds.push(item.userId)
            }
          })
          that.formItem.userIds = userIds;
        }
        that.modalVisible = true
      })
    },
    transferRender(item) {
      return `<span  title="${item.label}">${item.label}</span>`
    },
    handleTransferChange(newTargetKeys, direction, moveKeys) {
      this.formItem.userIds = newTargetKeys
    },
    handleClick(name, row) {
      switch (name) {
        case 'remove':
          this.handleRemove(row)
          break
      }
    }
  },
  mounted: function () {
    this.handleSearch()
  }
}
</script>
