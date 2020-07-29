<template>
    <div>
        <Row :gutter="8">
            <Col :lg="8" :md="8" :sm="8" :xs="8">
                <Card shadow>
                    <tree-table
                            :columns="columns"
                            :data="data"
                            :expand-type="false"
                            :is-fold="false"
                            :selectable="false"
                            :tree-type="true"
                            @radio-click="rowClick"
                            expand-key="menuName"
                            size="small"
                            :stripe="true"
                            :border="true"
                            style="max-height:700px;overflow: auto">
                        <template slot="status" slot-scope="scope">
                            <Badge status="success" v-if="scope.row.status===1"/>
                            <Badge status="error" v-else=""/>
                            <Icon :type="scope.row.icon" size="16"/>
                        </template>
                    </tree-table>
                </Card>
            </Col>
            <Col :lg="8" :md="16" :sm="16" :xs="16">
                <Card shadow>
                    <div class="search-con search-con-top">
                        <ButtonGroup>
                            <Button :disabled="hasAuthority('systemMenuEdit')?false:true" @click="setEnabled(true)"
                                    type="primary">添加
                            </Button>
                            <Button :disabled="formItem.menuId && hasAuthority('systemMenuEdit')?false:true"
                                    @click="confirmModal = true"
                                    type="primary">删除
                            </Button>
                        </ButtonGroup>
                        <Modal
                                @on-ok="handleRemove"
                                title="提示"
                                v-model="confirmModal">
                            确定删除,菜单资源【{{formItem.menuName}}】吗?{{formItem.children && formItem.children.length > 0 ?
                            '存在子菜单,将一起删除.是否继续?' : ''}}

                        </Modal>
                    </div>
                    <Form :label-width="80" :model="formItem" :rules="formItemRules" ref="menuForm">
                        <FormItem label="上级菜单" prop="parentId">
                            <treeselect
                                    :default-expand-level="1"
                                    :normalizer="treeSelectNormalizer"
                                    :options="selectTreeData"
                                    v-model="formItem.parentId"/>
                        </FormItem>
                        <FormItem label="菜单标识" prop="menuCode">
                            <Input placeholder="请输入内容" v-model="formItem.menuCode"></Input>
                        </FormItem>
                        <FormItem label="菜单名称" prop="menuName">
                            <Input placeholder="请输入内容" v-model="formItem.menuName"></Input>
                        </FormItem>
                        <FormItem label="页面地址" prop="path">
                            <Input placeholder="请输入内容" v-model="formItem.path">
                                <Select slot="prepend" style="width: 80px" v-model="formItem.scheme">
                                    <Option value="/">/</Option>
                                    <Option value="http://">http://</Option>
                                    <Option value="https://">https://</Option>
                                </Select>
                                <Select slot="append" style="width: 100px" v-model="formItem.target">
                                    <Option value="_self">窗口内打开</Option>
                                    <Option :disabled="formItem.scheme==='/'" value="_blank">新窗口打开</Option>
                                </Select>
                            </Input>
                            <span v-if="formItem.scheme === '/'">前端组件：/view/module/{{formItem.path}}.vue</span>
                            <span v-else="">跳转地址：{{formItem.scheme}}{{formItem.path}}</span>
                        </FormItem>
                        <FormItem label="图标">
                            <Input placeholder="请输入内容" v-model="formItem.icon">
                                <Icon :type="formItem.icon" size="22" slot="prepend"/>
                                <Poptip placement="bottom" slot="append" width="600">
                                    <Button icon="ios-search"></Button>
                                    <div slot="content">
                                        <ul class="icons">
                                            <li :title="item" @click="onIconClick(item)" class="icons-item"
                                                v-for="item in selectIcons">
                                                <Icon :type="item" size="28"/>
                                                <p>{{item}}</p>
                                            </li>
                                        </ul>
                                    </div>
                                </Poptip>
                            </Input>
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
                            <Input placeholder="请输入内容" type="textarea" v-model="formItem.menuDesc"></Input>
                        </FormItem>
                        <FormItem>
                            <Button :loading="saving" @click="handleSubmit" type="primary">保存</Button>
                            <Button @click="setEnabled(true)" style="margin-left: 8px">重置</Button>
                        </FormItem>
                    </Form>
                </Card>
            </Col>
            <Col :lg="8" :md="16" :sm="16" :xs="16">
                <Card shadow>
                    <menu-action :value="formItem"></menu-action>
                </Card>
            </Col>
        </Row>
    </div>
</template>

<script>
    import {listConvertTree} from '@/libs/util'
    import {addMenu, getMenus, removeMenu, updateMenu} from '@/api/menu'
    import MenuAction from './menu-action.vue'
    import icons from './icons'

    export default {
        name: 'SystemMenu',
        components: {
            MenuAction
        },
        data() {
            const validateEn = (rule, value, callback) => {
                let reg = /^[_a-zA-Z0-9]+$/;
                if (value === '') {
                    callback(new Error('菜单标识不能为空'))
                } else if (value !== '' && !reg.test(value)) {
                    callback(new Error('只允许字母、数字、下划线'))
                } else {
                    callback()
                }
            };
            return {
                confirmModal: false,
                saving: false,
                visible: false,
                selectIcons: icons,
                selectTreeData: [{
                    menuId: 0,
                    menuName: '无'
                }],
                formItemRules: {
                    parentId: [
                        {required: true, message: '上级菜单', trigger: 'blur'}
                    ],
                    menuCode: [
                        {required: true, validator: validateEn, trigger: 'blur'}
                    ],
                    menuName: [
                        {required: true, message: '菜单名称不能为空', trigger: 'blur'}
                    ]
                },
                formItem: {
                    menuId: '',
                    menuCode: '',
                    menuName: '',
                    icon: 'md-document',
                    path: '',
                    scheme: '/',
                    target: '_self',
                    status: 1,
                    parentId: '0',
                    priority: 0,
                    menuDesc: ''
                },
                columns: [
                    {title: '菜单名称', key: 'menuName', minWidth: '160px', width: 'auto'},
                    {title: '状态', key: 'status', type: 'template', template: 'status', width: '35'},
                ],
                data: []
            }
        },
        methods: {
            treeSelectNormalizer(node) {
                return {
                    id: node.menuId,
                    label: node.menuName,
                    children: node.children
                }
            },
            setSelectTree(data) {
                this.selectTreeData = data
            },
            setEnabled(enabled) {
                if (enabled) {
                    this.handleReset()
                }
            },
            rowClick(data) {
                this.handleReset()
                if (data) {
                    this.formItem = Object.assign({}, data.row)
                }
                this.formItem.status = this.formItem.status + ''
            },
            handleReset() {
                const newData = {
                    menuId: '',
                    menuCode: '',
                    menuName: '',
                    icon: 'md-document',
                    path: '',
                    scheme: '/',
                    target: '_self',
                    status: '1',
                    parentId: '0',
                    priority: 0,
                    menuDesc: ''
                }
                this.formItem = newData
                this.$refs['menuForm'].resetFields()
                this.saving = false
            },
            handleSubmit() {
                this.$refs['menuForm'].validate((valid) => {
                    if (valid) {
                        this.saving = true
                        if (this.formItem.menuId) {
                            updateMenu(this.formItem).then(res => {
                                if (res.rtnCode === '200') {
                                    this.$Message.success('保存成功')
                                }
                                this.handleSearch()
                            }).finally(() => {
                                this.saving = false
                            })
                        } else {
                            addMenu(this.formItem).then(res => {
                                if (res.rtnCode === '200') {
                                    this.$Message.success('保存成功')
                                }
                                this.handleSearch()
                            }).finally(() => {
                                this.saving = false
                            })
                        }
                    }
                })
            },
            handleRemove() {
                removeMenu(this.formItem.menuId).then(res => {
                    this.handleReset()
                    this.handleSearch()
                    if (res.rtnCode === '200') {
                        this.$Message.success('删除成功')
                    }
                })
            },
            onIconClick(item) {
                this.formItem.icon = item
            },
            handleSearch() {
                getMenus().then(res => {
                    let opt = {
                        primaryKey: 'menuId',
                        parentKey: 'parentId',
                        startPid: '0'
                    }
                    this.data = listConvertTree(res.data, opt)
                    this.setSelectTree(this.data)
                })
            }
        },
        mounted: function () {
            this.handleSearch()
        }
    }
</script>
<style>
    .icons {
        overflow: auto;
        zoom: 1;
        height: 300px;
    }

    .icons-item {
        float: left;
        margin: 6px;
        width: 60px;
        text-align: center;
        list-style: none;
        cursor: pointer;
        color: #5c6b77;
        transition: all .2s ease;
        position: relative;
    }

    .icons-item p {
        word-break: break-all;
        overflow: hidden;
    }
</style>
