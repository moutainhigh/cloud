<template>
  <div>
    <Alert show-icon type="info">
      <Breadcrumb separator="<b class='demo-breadcrumb-separator'>></b>">
        <BreadcrumbItem>系统菜单</BreadcrumbItem>
        <BreadcrumbItem v-for="item in menu.deeply">{{ item }}</BreadcrumbItem>
      </Breadcrumb>
    </Alert>
    <Row :gutter="8">
      <Col :lg="6" :md="8" :sm="8" :xs="8">
        <Card>
          <Tree ref="tree"
                :data="menu.treeData"
                :load-data="loadTreeData"
                :render="renderChildren"
                :show-checkbox="false"
                @on-select-change="selectChange"/>
        </Card>
      </Col>
      <Col :lg="18" :md="16" :sm="16" :xs="16">
        <Card>
          <Form :model="menu.viewData" inline ref="viewForm" :label-width="60">
            <FormItem label="菜单名称" prop="menuName" style="width: 300px;">
              <label>
                <Input v-model="menu.viewData.menuName" :disabled="menu.viewDisabled"/>
              </label>
            </FormItem>
            <FormItem label="菜单状态" prop="menuState" style="width: 140px;">
              <label>
                <Select v-model="menu.viewData.menuState" :disabled="menu.viewDisabled">
                  <Option value="10">启用</Option>
                  <Option value="20">锁定</Option>
                  <Option value="30">禁用</Option>
                </Select>
              </label>
            </FormItem>
            <FormItem label="创建时间" style="width: 200px;">
                <span disabled class="ivu-input ivu-input-default ivu-input-disabled">
                  {{ menu.viewData.createdDate | dateFmt('YYYY-MM-DD HH:mm:ss') }}
                </span>
            </FormItem>
            <FormItem label="菜单ID" prop="menuId" style="width: 200px;">
              <label>
                <Input v-model="menu.viewData.menuId" :disabled="menu.viewDisabled"/>
              </label>
            </FormItem>
            <FormItem label="父级ID" prop="menuParentId" style="width: 200px;">
              <label>
                <Input v-model="menu.viewData.menuParentId" :disabled="menu.viewDisabled"/>
              </label>
            </FormItem>
            <FormItem label="下级菜单" prop="existChild" style="width: 140px;">
              <span disabled
                    class="ivu-input ivu-input-default ivu-input-disabled">
                {{ menu.viewData.existChild ? '有' : '无' }}
              </span>
            </FormItem>
          </Form>
        </Card>
        <br/>
        <Card>
          <Form :label-width="80" :model="role.pageInfo" inline ref="handleRoleResetForm">
            <FormItem>
              <Button @click="handleRoleSearch(1)" type="primary">查询</Button>&nbsp;
              <Button @click="handleRoleResetForm('handleRoleResetForm')">重置</Button>&nbsp;&nbsp;
              <Button @click="handleModal()" class="search-btn" type="primary">添加</Button>
            </FormItem>
          </Form>
          <Table size="small" max-height="420" stripe :columns="role.columns" :data="role.data"
                 :loading="role.loading"
                 border>
            <template slot="createdDate" slot-scope="{ row }">
              <span>{{ row.createdDate | dateFmt('YYYY-MM-DD HH:mm:ss') }}</span>
            </template>
            <template slot="action" slot-scope="{ row }">
              <a>详情</a>
            </template>
          </Table>
        </Card>
        <br/>
        <Card>
          <Form :label-width="80" :model="user.pageInfo" inline ref="handleUserResetForm">
            <FormItem>
              <Button @click="handleUserSearch(1)" type="primary">查询</Button>&nbsp;
              <Button @click="handleUserResetForm('handleUserResetForm')">重置</Button>
            </FormItem>
          </Form>
          <Table size="small" max-height="420" stripe :columns="user.columns" :data="user.data"
                 :loading="user.loading" border>
            <template slot="createdDate" slot-scope="{ row }">
              <span>{{ row.createdDate | dateFmt('YYYY-MM-DD HH:mm:ss') }}</span>
            </template>
            <template slot="action" slot-scope="{ row }">
              <a>详情</a>
            </template>
          </Table>
        </Card>
      </Col>
    </Row>
    <!-- 添加/编辑菜单 -->
    <Modal :title="modal.title"
           v-model="modal.visible"
           @on-cancel="handleModalCancel"
           width="40">
      <Form :label-width="80" :model="modal.formItem" :rules="modal.formItemRules" ref="modalForm">
        <FormItem label="上级菜单" prop="menuParentId">
          <treeselect
              :default-expand-level="1"
              :normalizer="treeSelectNormalizer"
              :options="modal.selectTreeData"
              v-model="modal.formItem.menuParentId"/>
        </FormItem>
        <FormItem label="菜单名称" prop="menuName">
          <label>
            <Input placeholder="请输入内容" v-model="modal.formItem.menuName"></Input>
          </label>
        </FormItem>
        <FormItem label="页面地址" prop="menuPath">
          <Input placeholder="请输入内容" v-model="modal.formItem.menuPath">
            <Select slot="prepend" style="width: 80px" v-model="modal.formItem.menuSchema">
              <Option value="/">/</Option>
              <Option value="http://">http://</Option>
              <Option value="https://">https://</Option>
            </Select>
            <Select slot="append" style="width: 100px" v-model="modal.formItem.menuTarget">
              <Option value="_self">窗口内打开</Option>
              <Option :disabled="modal.formItem.menuSchema==='/'" value="_blank">新窗口打开</Option>
            </Select>
          </Input>
          <span v-if="modal.formItem.menuSchema === '/'">前端组件：/view/module/{{ modal.formItem.menuPath }}.vue</span>
          <span v-else>跳转地址：{{ modal.formItem.menuSchema }}{{ modal.formItem.menuPath }}</span>
        </FormItem>
        <FormItem label="图标">
          <Input placeholder="请输入内容" v-model="modal.formItem.menuIcon">
            <Icon :type="modal.formItem.menuIcon" size="22" slot="prepend"/>
            <Poptip placement="bottom" slot="append" width="600">
              <Button icon="ios-search"></Button>
              <div slot="content">
                <ul class="icons">
                  <li :title="item" @click="onIconClick(item)" class="icons-item"
                      v-for="item in modal.selectIcons">
                    <Icon :type="item" size="28"/>
                    <p>{{ item }}</p>
                  </li>
                </ul>
              </div>
            </Poptip>
          </Input>
        </FormItem>
        <div class="drawer-footer">
          <Button @click="handleModalCancel" type="default">取消</Button>&nbsp;
          <Button :loading="modal.saving" @click="handleModalSubmit" type="primary">保存</Button>
        </div>
      </Form>
    </Modal>
  </div>
</template>
<script>
import {addMenu, getMenuChildren, getMenuRoles, getMenus, getMenuUsers, viewMenu} from '@/api/access/menu';
import icons from "@/view/module/system/menus/icons";
import {listConvertTree} from "@/libs/util";

export default {
  data() {
    return {
      menu: {
        treeData: [
          {
            menuId: 0,
            title: '系统菜单',
            loading: false,
            children: [],
            existChild: true,
            render: (h, {root, node, data}) => {
              return h('span', {
                style: {
                  display: 'inline-block',
                  width: '100%'
                }
              }, [
                h('span', [
                  h('Icon', {
                    props: {
                      type: 'ios-apps'
                    },
                    style: {marginRight: '8px'}
                  }),
                  h('span', data.title)
                ]),
                h('span', {style: {display: 'inline-block', float: 'right', marginRight: '32px'}})
              ]);
            }
          }
        ],
        viewData: {},
        viewDisabled: true,
        deeply: []
      },
      modal: {
        title: '',
        formItem: {
          menuId: '',
          menuParentId: '0',
          menuName: '',
          menuIcon: 'md-document',
          menuPath: '',
          menuSchema: '/',
          menuTarget: '_self',
          menuState: '10'
        },
        formItemRules: {
          menuParentId: [
            {required: true, message: '上级菜单不能为空', trigger: 'blur'}
          ],
          menuName: [
            {required: true, message: '菜单名称不能为空', trigger: 'blur'}
          ]
        },
        visible: false,
        saving: false,
        selectTreeData: [{
          menuId: 0,
          menuName: '无'
        }],
        selectIcons: icons,
        data: []
      },
      role: {
        loading: false,
        pageInfo: {
          total: 0,
          page: 1,
          limit: 10,
          menuId: 0,
          menuState: ''
        },
        data: [],
        columns: [
          {title: '角色ID', key: 'roleId', width: 160},
          {title: '角色名', key: 'roleName', width: 200},
          {title: '创建时间', key: 'createdDate', slot: 'createdDate'},
          {title: '操作', slot: 'action', fixed: 'right', align: 'center', width: 160}
        ]
      },
      user: {
        loading: false,
        pageInfo: {
          total: 0,
          page: 1,
          limit: 10,
          menuId: 0,
          menuState: ''
        },
        data: [],
        columns: [
          {title: '用户ID', key: 'userId', width: 160},
          {title: '用户名', key: 'userName', width: 200},
          {title: '创建时间', key: 'createdDate', slot: 'createdDate'},
          {title: '操作', slot: 'action', fixed: 'right', align: 'center', width: 160}
        ]
      }
    }
  },
  methods: {
    handleUserResetForm(form) {
      this.$refs[form].resetFields()
    },
    handleRoleResetForm(form) {
      this.$refs[form].resetFields()
    },
    handleRoleSearch(page) {
      if (page) {
        this.role.pageInfo.page = page;
      }
      this.role.loading = true;
      getMenuRoles(this.role.pageInfo)
          .then(res => {
            this.role.data = res.data;
          })
          .finally(() => {
            this.role.loading = false;
          })
    },
    handleUserSearch(page) {
      if (page) {
        this.user.pageInfo.page = page;
      }
      this.user.loading = true;
      getMenuUsers(this.user.pageInfo)
          .then(res => {
            this.user.data = res.data;
          })
          .finally(() => {
            this.user.loading = false;
          })
    },
    treeSelectNormalizer(node) {
      return {
        id: node.menuId,
        label: node.menuName,
        children: node.children
      }
    },
    setSelectTree(data) {
      this.modal.selectTreeData = data;
    },
    onIconClick(item) {
      this.modal.formItem.menuIcon = item;
    },
    /**
     * 弹框
     */
    handleModal(data) {
      if (data) {
        this.modal.title = '编辑菜单';
        this.modal.formItem = Object.assign({}, this.modal.formItem, data);
      } else {
        this.modal.title = '添加菜单';
      }
      this.modal.visible = true;
    },
    /**
     * 弹框保持事件
     */
    handleModalSubmit() {
      console.log(this.modal.formItem);
      this.$refs.modalForm.validate((valid) => {
        if (valid) {
          this.modal.saving = true;
          if (this.modal.formItem.menuId) {

          } else {
            addMenu(this.modal.formItem)
                .then(res => {
                  // 此处刷新页面等操作
                  this.handleModalCancel();
                })
                .finally(() => {
                  this.modal.visible = false;
                  this.modal.saving = false;
                });
          }
        }
      });
    },
    /**
     * 弹框取消事件
     */
    handleModalCancel() {
      this.modal.formItem = {
        menuId: '',
        menuParentId: '0',
        menuName: '',
        menuIcon: 'md-document',
        menuPath: '',
        menuSchema: '/',
        menuTarget: '_self',
        menuState: '10'
      };
      this.$refs.modalForm.resetFields();
      this.modal.saving = false;
      this.modal.visible = false;
    },
    /**
     * 加载子节点
     * @param item
     * @param callback
     */
    loadTreeData(item, callback) {
      this._loadData(item, callback);
    },
    /**
     * 自定义子节点显示内容
     * @param h
     * @param root
     * @param node
     * @param data
     */
    renderChildren(h, {root, node, data}) {
      let menuIcon = data['menuIcon'] ? data['menuIcon'] : 'md-document';
      return h('span', {
        style: {
          display: 'inline-block',
          width: '100%'
        }
      }, [
        h('span', [
          h('Icon', {
            props: {
              type: menuIcon
            },
            style: {marginRight: '8px'}
          }),
          h('span', data.title)
        ]),
        h('span', {style: {display: 'inline-block', float: 'right', marginRight: '32px'}})
      ]);
    },
    /**
     * @param node
     * @param callback
     */
    _loadData(node, callback) {
      let target = node;
      let items = [];
      while (target.parent) {
        items.push(target.title);
        target = target.parent;
      }
      this.menu.deeply = items.reverse();

      getMenuChildren({menuId: node.menuId})
          .then(res => {
            let array = [];
            res.data.map(item => {
              item.title = item.menuName;
              item.loading = false;
              item.children = [];
              item.parent = node;
              array.push(item);
            });
            callback(array);
          })
          .finally(() => {
          });
    },
    /**
     * 点击选中节点文字
     * @param selectedNodes
     */
    selectChange(selectedNodes) {
      // 获取当前点击的节点
      const node = selectedNodes[selectedNodes.length - 1];
      if (node) {
        this._loadData(node, (response) => {
          // 没有子节点则返回
          if (!response) {
            return;
          }

          // 遍历子节点
          let array = [];
          response.forEach(item => {
            array.push(item);
            // this._loadData(item.menuId, () => {
            // });
          });
          // 挂载子节点
          node.children = array;
          // 展开子节点树
          node.expand = true;
        });

        // 详情
        viewMenu({menuId: node.menuId})
            .then(res => {
              this.menu.viewData = res.data;
            })
            .finally(() => {
            });
        // this.user.pageInfo.menuId = node.menuId;
        // this.role.pageInfo.menuId = node.menuId;
        // this.handleRoleSearch(1);
        // this.handleUserSearch(1);
      }
    }
  },
  mounted: function () {
    this.$refs.tree.handleSelect(0);

    getMenus()
        .then(res => {
          let opt = {
            primaryKey: 'menuId',
            parentKey: 'menuParentId',
            startPid: '0'
          }
          this.modal.data = listConvertTree(res.data, opt);
          this.setSelectTree(this.modal.data);
        });
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
