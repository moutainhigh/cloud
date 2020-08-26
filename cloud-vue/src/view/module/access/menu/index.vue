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
            <Button @click="handleModal('edit')" :icon="menu.viewData.menuIcon" v-show="menu.viewShow"
                    class="search-btn" type="dashed">
              {{ menu.viewData.menuName }}
            </Button>&nbsp;&nbsp;
            <Button @click="handleModal('add')" class="search-btn" type="dashed">添加</Button>&nbsp;
            <Button @click="handleRemove(menu.viewData.menuId)" v-show="menu.viewShow" class="search-btn" type="dashed">
              移除
            </Button>
          </Form>
        </Card>
        <br/>
        <Card>
          <Form :label-width="80" :model="role.pageInfo" inline ref="handleRoleResetForm">
            <FormItem>
              <Button @click="handleRoleSearch(1)" type="primary">查询</Button>&nbsp;
              <Button @click="handleRoleResetForm('handleRoleResetForm')">重置</Button>&nbsp;
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
        <FormItem label="菜单标识" prop="menuCode">
          <label>
            <Input placeholder="请输入内容" v-model="modal.formItem.menuCode"></Input>
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
        <FormItem label="优先级">
          <InputNumber v-model="modal.formItem.menuSorted"></InputNumber>
        </FormItem>
        <FormItem label="状态">
          <RadioGroup type="button" v-model="modal.formItem.menuState">
            <Radio label="10">启用</Radio>
            <Radio label="20">禁用</Radio>
          </RadioGroup>
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
import {
  addMenu,
  removeMenu,
  updateMenu,
  getMenuChildren,
  getMenuRoles,
  getMenus,
  getMenuUsers,
  viewMenu
} from '@/api/access/menu';
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
                    style: {marginRight: '5px'}
                  }),
                  h('span', data.title)
                ]),
                h('span', {style: {display: 'inline-block', float: 'right', marginRight: '32px'}})
              ]);
            }
          }
        ],
        viewData: {},
        viewShow: false,
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
          menuState: '10',
          menuSorted: 0,
          menuCode: ''
        },
        formItemRules: {
          menuParentId: [
            {required: true, message: '上级菜单 必填', trigger: 'blur'}
          ],
          menuName: [
            {required: true, message: '菜单名称 必填', trigger: 'blur'}
          ],
          menuCode: [
            {required: true, message: '菜单标识 必填', trigger: 'blur'}
          ]
        },
        visible: false,
        saving: false,
        selectTreeData: [],
        selectIcons: icons
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
          {title: '角色ID', key: 'roleId', width: 170},
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
          {title: '用户ID', key: 'userId', width: 170},
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
     * @param type (edit/add)
     */
    handleModal(type) {
      if ('edit' === type) {
        this.modal.title = '编辑菜单';
        this.modal.formItem = Object.assign({}, this.modal.formItem, this.menu.viewData);
      } else {
        this.modal.title = '添加菜单';
      }
      this.modal.visible = true;
    },
    /**
     * 弹框保存事件
     */
    handleModalSubmit() {
      this.$refs.modalForm.validate((valid) => {
        if (valid) {
          this.modal.saving = true;
          if (this.modal.formItem.menuId) {
            updateMenu(this.modal.formItem)
              .then(res => {
                // 此处刷新页面等操作
                this.$refs.tree.handleSelect(0);
                this.handleModalCancel();
                this._refreshAllMenus();
              })
              .finally(() => {
                this.modal.visible = false;
                this.modal.saving = false;
              });
          } else {
            addMenu(this.modal.formItem)
              .then(res => {
                // 此处刷新页面等操作
                this.$refs.tree.handleSelect(0);
                this.handleModalCancel();
                this._refreshAllMenus();
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
        menuState: '10',
        menuSorted: 0,
        menuCode: ''
      };
      this.$refs.modalForm.resetFields();
      this.modal.saving = false;
      this.modal.visible = false;
    },
    /**
     * 移除
     * @param menuId 菜单ID
     */
    handleRemove(menuId) {
      this.$Modal.confirm({
        title: '确定删除吗？',
        onOk: () => {
          removeMenu(menuId)
            .then(res => {
              console.log(res);
              this.$Message.success('删除成功')
              // 此处刷新页面等操作
              this.$refs.tree.handleSelect(0);
            })
        }
      })
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
      // 若非启用状态则设置为禁用
      let badgeStatus;
      switch (data['menuState']) {
        case '10':
          badgeStatus = 'green';
          break;
        case '20':
          badgeStatus = 'orange';
          break;
        case '30':
          badgeStatus = 'red';
          break;
      }

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
            style: {marginRight: '5px'}
          }),
          h('span', data.title),
          h('Badge', {
            props: {
              status: badgeStatus
            },
            style: {marginLeft: '3px'}
          })
        ]),
        h('span', {style: {display: 'inline-block', float: 'right', marginRight: '32px'}})
      ]);
    },
    /**
     * 刷新下拉选择列表
     */
    _refreshAllMenus() {
      getMenus()
        .then(res => {
          let opt = {
            primaryKey: 'menuId',
            parentKey: 'menuParentId',
            startPid: '0'
          }
          let xx = res.data;
          xx.unshift({
            menuId: 0,
            menuName: '无',
            menuCode: "system",
            menuParentId: "0",
            menuPath: "",
            menuSchema: "/",
            menuTarget: "_self"
          });
          let result = listConvertTree(xx, opt);
          this.setSelectTree(result);
        });
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
            if (item['existChild']) {
              item.loading = false;
              item.children = [];
            }
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
          });
          // 挂载子节点
          node.children = array;
          // 展开子节点树
          node.expand = true;
        });
        // 展开箭头
        node.expand = true;

        // 详情
        viewMenu({menuId: node.menuId})
          .then(res => {
            this.menu.viewData = res.data;
            this.menu.viewShow = node.menuId !== 0;
          })
          .finally(() => {
          });
        // this.user.pageInfo.menuId = node.menuId;
        // this.role.pageInfo.menuId = node.menuId;
        // this.handleRoleSearch(1);
        // this.handleUserSearch(1);
      } else {
        this.menu.viewData = {};
        this.menu.viewShow = false;
      }
    }
  },
  mounted: function () {
    this.$refs.tree.handleSelect(0);
    this._refreshAllMenus();
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
