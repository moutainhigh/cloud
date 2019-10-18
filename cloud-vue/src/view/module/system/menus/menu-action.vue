<template>
  <div>
    <div class="search-con search-con-top">
      <ButtonGroup>
        <Button
          :disabled="value.menuId && value.menuId!=='0' && !value.hasChild && hasAuthority('systemMenuEdit')?false:true"
          @click="handleModal()" class="search-btn" type="primary">
          <span>添加功能按钮</span>
        </Button>
      </ButtonGroup>
    </div>
    <Alert show-icon type="info">请绑定相关接口资源。否则请求网关服务器将提示<code>"权限不足,拒绝访问!"</code></Alert>
    <Table :columns="columns" :data="data" :loading="loading" border>
      <template slot="status" slot-scope="{ row }">
        <Badge status="success" v-if="row.status===1"/>
        <Badge status="error" v-else=""/>
        <span>{{row.actionName}}</span>
      </template>
      <template slot="action" slot-scope="{ row }">
        <a :disabled="hasAuthority('systemMenuEdit')?false:true" @click="handleModal(row)">编辑</a> &nbsp;
        <a :disabled="hasAuthority('systemMenuEdit')?false:true" @click="handleModal(row,forms[1])">接口权限</a> &nbsp;
        <a :disabled="hasAuthority('systemMenuEdit')?false:true" @click="handleRemove(row)">删除</a>
      </template>
    </Table>
    <Modal :title="modalTitle"
           @on-cancel="handleReset"
           v-model="modalVisible"
           width="40">
      <div>
        <Form :label-width="100" :model="formItem" :rules="formItemRules" ref="form1" v-show="current=='form1'">
          <FormItem label="上级菜单">
            <Input disabled v-model="value.menuName"></Input>
          </FormItem>
          <FormItem label="功能标识" prop="actionCode">
            <Input placeholder="请输入内容" v-model="formItem.actionCode"></Input>
            <span>菜单标识+自定义标识.默认后缀：View、Edit</span>
          </FormItem>
          <FormItem label="功能名称" prop="actionName">
            <Input placeholder="请输入内容" v-model="formItem.actionName"></Input>
          </FormItem>
          <FormItem label="优先级">
            <InputNumber v-model="formItem.priority"></InputNumber>
          </FormItem>
          <FormItem label="状态">
            <RadioGroup type="button" v-model="formItem.status">
              <Radio label="0">禁用</Radio>
              <Radio label="1">启用</Radio>
            </RadioGroup>
          </FormItem>
          <FormItem label="描述">
            <Input placeholder="请输入内容" type="textarea" v-model="formItem.actionDesc"></Input>
          </FormItem>
        </Form>
        <Form :model="formItem" :rules="formItemRules" ref="form2" v-show="current=='form2'">
          <FormItem prop="authorities">
            <Transfer
              :data="selectApis"
              :list-style="{width: '45%',height: '480px'}"
              :render-format="transferRender"
              :target-keys="formItem.authorityIds"
              :titles="['选择接口', '已选择接口']"
              @on-change="handleTransferChange"
              filterable>
            </Transfer>
          </FormItem>
        </Form>
        <div class="drawer-footer">
          <Button @click="handleReset" type="default">取消</Button>&nbsp;
          <Button :loading="saving" @click="handleSubmit" type="primary">保存</Button>
        </div>
      </div>
    </Modal>
  </div>
</template>

<script>
    import {addAction, getActionsByMenu, removeAction, updateAction,} from '@/api/action'
    import {getAuthorityAction, getAuthorityApi, grantAuthorityAction} from '@/api/authority'

    export default {
        name: 'MenuAction',
        props: {
            value: Object
        },
        data() {
            const validateEn = (rule, value, callback) => {
                let reg = /^[_a-zA-Z0-9]+$/
                if (value === '') {
                    callback(new Error('功能标识不能为空'))
                } else if (value !== '' && !reg.test(value)) {
                    callback(new Error('只允许字母、数字、下划线'))
                } else {
                    callback()
                }
            }
            return {
                modalVisible: false,
                saving: false,
                loading: false,
                current: 'form1',
                forms: [
                    'form1',
                    'form2'
                ],
                modalTitle: '',
                confirmModal: false,
                selectApis: [],
                formItemRules: {
                    actionCode: [
                        {required: true, validator: validateEn, message: '功能编码不能为空', trigger: 'blur'}
                    ],
                    actionName: [
                        {required: true, message: '功能名称不能为空', trigger: 'blur'}
                    ]
                },
                formItem: {
                    actionId: '',
                    actionCode: '',
                    actionName: '',
                    authorityIds: [],
                    status: 1,
                    menuId: '',
                    priority: 0,
                    actionDesc: ''
                },
                columns: [
                    {
                        title: '功能名称',
                        slot: 'status',
                        width: 150
                    },
                    {
                        title: '功能编码',
                        key: 'actionCode'
                    },
                    {
                        title: '操作',
                        slot: 'action',
                        fixed: 'right',
                        width: 160
                    }
                ],
                data: []
            }
        },
        methods: {
            handleModal(data, step) {
                if (data) {
                    this.formItem = Object.assign({}, this.formItem, data)
                }
                if (!step) {
                    step = this.forms[0]
                }
                if (step === this.forms[0]) {
                    this.modalTitle = data ? '编辑功能 - ' + this.value.menuName + ' > ' + data.actionName : '添加功能 - ' + this.value.menuName
                    this.modalVisible = true
                    this.formItem.actionCode = this.formItem.actionId ? this.formItem.actionCode : this.value.menuCode
                }
                if (step === this.forms[1]) {
                    this.modalTitle = data ? '接口授权 - ' + this.value.menuName + ' > ' + data.actionName : '接口授权'
                    this.handleLoadActionApi(this.formItem.actionId)
                }
                this.current = step
                this.formItem.status = this.formItem.status + ''
            },
            handleReset() {
                const newData = {
                    actionId: '',
                    actionCode: '',
                    actionName: '',
                    authorityIds: [],
                    status: 1,
                    priority: 0,
                    actionDesc: ''
                }
                this.formItem = newData
                //重置验证
                this.forms.map(form => {
                    this.$refs[form].resetFields()
                })
                this.current = this.forms[0]
                this.modalVisible = false
                this.saving = false
            },
            handleSubmit() {
                if (this.current === this.forms[0]) {
                    this.$refs[this.current].validate((valid) => {
                        if (valid) {
                            this.saving = true
                            if (this.formItem.actionId) {
                                updateAction(this.formItem).then(res => {
                                    this.handleReset()
                                    this.handleSearch()
                                    if (res.code === 0) {
                                        this.$Message.success('保存成功')
                                    }
                                }).finally(() => {
                                    this.saving = false
                                })
                            } else {
                                addAction(this.formItem).then(res => {
                                    this.handleReset()
                                    this.handleSearch()
                                    if (res.code === 0) {
                                        this.$Message.success('保存成功')
                                    }
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
                            grantAuthorityAction({
                                actionId: this.formItem.actionId,
                                authorityIds: this.formItem.authorityIds
                            }).then(res => {
                                this.handleReset()
                                this.handleSearch()
                                if (res.code === 0) {
                                    this.$Message.success('绑定成功')
                                }
                            }).finally(() => {
                                this.saving = false
                            })
                        }
                    })
                }

            },
            handleSearch() {
                if (!this.value || !this.value.menuId) {
                    return
                }
                this.formItem.menuId = this.value.menuId
                this.loading = true
                getActionsByMenu(this.formItem.menuId).then(res => {
                    this.data = res.data
                }).finally(() => {
                    this.loading = false
                })
            },
            handleRemove(data) {
                this.$Modal.confirm({
                    title: '确定删除吗？',
                    onOk: () => {
                        removeAction(data.actionId).then(res => {
                            this.handleSearch()
                            if (res.code === 0) {
                                this.pageInfo.page = 1
                                this.$Message.success('删除成功')
                            }
                        })
                    }
                })
            },
            handleLoadActionApi(actionId) {
                if (!actionId) {
                    return
                }
                const that = this
                const p1 = getAuthorityApi('')
                const p2 = getAuthorityAction(actionId)
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
                        const result = []
                        res2.data.map(item => {
                            if (!result.includes(item.authorityId)) {
                                result.push(item.authorityId)
                            }
                        })
                        that.formItem.authorityIds = result
                    }
                    that.modalVisible = true
                })
            },
            transferRender(item) {
                return `<span  title="${item.label}">${item.label}</span>`
            },
            handleTransferChange(newTargetKeys, direction, moveKeys) {
                if (newTargetKeys.indexOf('1') != -1) {
                    this.formItem.authorityIds = ['1']
                } else {
                    this.formItem.authorityIds = newTargetKeys
                }
            },
        },
        watch: {
            value(val) {
                this.handleSearch()
            }
        },
        mounted: function () {
        }
    }
</script>
