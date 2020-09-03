<template>
  <div>
    <Alert show-icon type="info">
      <Breadcrumb separator="<b class='demo-breadcrumb-separator'>></b>">
        <BreadcrumbItem>组织机构</BreadcrumbItem>
        <BreadcrumbItem v-for="item in group.deeply">{{ item }}</BreadcrumbItem>
      </Breadcrumb>
    </Alert>
    <Row :gutter="8">
      <Col :lg="6" :md="8" :sm="8" :xs="8">
        <Card>
          <Tree ref="tree"
                :data="group.treeData"
                :load-data="loadTreeData"
                :render="renderChildren"
                :show-checkbox="false"
                @on-select-change="selectChange"/>
        </Card>
      </Col>
      <Col :lg="18" :md="16" :sm="16" :xs="16">
        <Card>
          <Form :model="group.viewData" inline ref="viewForm" :label-width="60">
            <FormItem label="组织名称" prop="groupName" style="width: 300px;">
              <Input v-model="group.viewData.groupName" :disabled="group.viewDisabled"/>
            </FormItem>
            <FormItem label="组织类型" prop="groupType" style="width: 140px;">
              <label>
                <Select v-model="group.viewData.groupType" :disabled="group.viewDisabled">
                  <Option value="g">集团</Option>
                  <Option value="c">公司</Option>
                  <Option value="d">部门</Option>
                  <Option value="t">小组</Option>
                  <Option value="p">岗位</Option>
                  <Option value="r">角色线</Option>
                </Select>
              </label>
            </FormItem>
            <FormItem label="组织状态" prop="groupState" style="width: 140px;">
              <label>
                <Select v-model="group.viewData.groupState" :disabled="group.viewDisabled">
                  <Option value="10">启用</Option>
                  <Option value="20">锁定</Option>
                  <Option value="30">禁用</Option>
                </Select>
              </label>
            </FormItem>
            <FormItem label="创建时间" style="width: 200px;">
                <span disabled class="ivu-input ivu-input-default ivu-input-disabled">
                  {{ group.viewData.createdDate | dateFmt('YYYY-MM-DD HH:mm:ss') }}
                </span>
            </FormItem>
            <FormItem label="组织ID" prop="groupId" style="width: 200px;">
              <Input v-model="group.viewData.groupId" :disabled="group.viewDisabled"/>
            </FormItem>
            <FormItem label="父级ID" prop="groupParentId" style="width: 200px;">
              <Input v-model="group.viewData.groupParentId" :disabled="group.viewDisabled"/>
            </FormItem>
            <FormItem label="下级组织" prop="existChild" style="width: 140px;">
              <span disabled
                    class="ivu-input ivu-input-default ivu-input-disabled">
                {{ group.viewData.existChild ? '有' : '无' }}
              </span>
            </FormItem>

            <Button @click="handleModal('add')" class="search-btn" type="dashed">添加</Button>&nbsp;
            <Button class="search-btn" type="dashed" v-show="group.viewShow">编辑</Button>&nbsp;
            <Button @click="handleRemove(group.viewData.groupId)" class="search-btn" type="dashed"
                    v-show="group.viewShow">移除
            </Button>
          </Form>
        </Card>
        <br/>
        <Card>
          <Form :label-width="80" :model="role.pageInfo" inline ref="handleRoleResetForm">
            <FormItem>
              <Button @click="handleRoleSearch(1)" type="primary">查询</Button>&nbsp;
              <Button @click="handleRoleResetForm('handleRoleResetForm')">重置</Button>
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
    <!-- 添加/编辑组织 -->
    <Modal :title="modal.title"
           v-model="modal.visible"
           @on-cancel="handleModalCancel"
           width="40">
      <Form :label-width="80" :model="modal.formItem" :rules="modal.formItemRules" ref="modalForm">
        <FormItem label="组织类型" prop="groupType">
          <label>
            <Select v-model="modal.formItem.groupType">
              <Option value="g">集团</Option>
              <Option value="c">公司</Option>
              <Option value="d">部门</Option>
              <Option value="t">小组</Option>
              <Option value="p">岗位</Option>
              <Option value="u">人员</Option>
              <Option value="r">角色线</Option>
            </Select>
          </label>
        </FormItem>
        <FormItem label="上级组织" prop="groupParentId">
          <treeselect
            :default-expand-level="1"
            :normalizer="treeSelectNormalizer"
            :options="modal.selectTreeData"
            v-model="modal.formItem.groupParentId"/>
        </FormItem>
        <FormItem label="组织名称" prop="groupName">
          <label>
            <Input placeholder="请输入内容" v-model="modal.formItem.groupName"></Input>
          </label>
        </FormItem>
        <FormItem label="状态" prop="groupState">
          <RadioGroup type="button" v-model="modal.formItem.groupState">
            <Radio label="10">启用</Radio>
            <Radio label="20">禁用</Radio>
            <Radio label="30">锁定</Radio>
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
  addGroup,
  getGroupChildren,
  getGroupRoles,
  getGroups,
  getGroupUsers, removeGroup,
  updateGroup,
  viewGroup
} from '@/api/access/group';
import icons from "@/view/module/system/menus/icons";
import {listConvertTree} from "@/libs/util";

export default {
  data() {
    return {
      group: {
        treeData: [
          {
            groupId: 0,
            title: '组织机构',
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
          groupId: '',
          groupParentId: '0',
          groupName: '',
          groupState: '10',
          groupType: ''
        },
        formItemRules: {
          groupParentId: [
            {required: true, message: '上级组织 必填', trigger: 'blur'}
          ],
          groupName: [
            {required: true, message: '组织名称 必填', trigger: 'blur'}
          ],
          groupType: [
            {required: true, message: '组织类型 必填', trigger: 'blur'}
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
          groupId: 0,
          groupType: '',
          groupState: ''
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
          groupId: 0,
          groupType: '',
          groupState: ''
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
      getGroupRoles(this.role.pageInfo)
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
      getGroupUsers(this.user.pageInfo)
        .then(res => {
          this.user.data = res.data;
        })
        .finally(() => {
          this.user.loading = false;
        })
    },
    treeSelectNormalizer(node) {
      return {
        id: node.groupId,
        label: node.groupName,
        children: node.children
      }
    },
    setSelectTree(data) {
      this.modal.selectTreeData = data;
    },
    /**
     * 弹框
     * @param type (edit/add)
     */
    handleModal(type) {
      if ('edit' === type) {
        this.modal.title = '编辑组织';
        this.modal.formItem = Object.assign({}, this.modal.formItem, this.group.viewData);
      } else {
        this.modal.title = '添加组织';
      }
      this.modal.visible = true;
    },
    /**
     * 弹框取消事件
     */
    handleModalCancel() {
      // this.modal.formItem = {
      //   groupId: '',
      //   groupParentId: '0',
      //   groupName: '',
      //   groupState: '10',
      //   groupType: ''
      // };
      // this.$refs.modalForm.resetFields();
      this.modal.saving = false;
      this.modal.visible = false;
    },
    /**
     * 弹框保存事件
     */
    handleModalSubmit() {
      this.$refs.modalForm.validate((valid) => {
        if (valid) {
          this.modal.saving = true;
          if (this.modal.formItem.groupId) {
            updateGroup(this.modal.formItem)
              .then(res => {
                // 此处刷新页面等操作
                this.$refs.tree.handleSelect(0);
                this.handleModalCancel();
                this._refreshAllGroups();
              })
              .finally(() => {
                this.modal.visible = false;
                this.modal.saving = false;
              });
          } else {
            addGroup(this.modal.formItem)
              .then(res => {
                // 此处刷新页面等操作
                this.$refs.tree.handleSelect(0);
                this.handleModalCancel();
                this._refreshAllGroups();
              })
              .finally(() => {
                this.modal.visible = false;
                this.modal.saving = false;
              });
          }
        }
      });
    },
    handleRemove(groupId) {
      this.$Modal.confirm({
        title: '确定删除吗？',
        onOk: () => {
          removeGroup(groupId)
            .then(() => {
              this.$Message.success('删除成功')
              this.handleSearch()
            })
        }
      });
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
      let iconType = '';
      switch (data['groupType']) {
        case 'g':
          iconType = 'md-cloud';
          break;
        case 'c':
          iconType = 'md-podium';
          break;
        case 'd':
          iconType = 'md-home';
          break;
        case 't':
          iconType = 'md-people';
          break;
        case 'p':
          iconType = 'md-person';
          break;
        case 'r':
          iconType = 'md-person';
          break;
      }
      // 若非启用状态则设置为禁用
      let badgeStatus;
      switch (data['groupState']) {
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
              type: iconType
            },
            style: {marginRight: '8px'}
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
    _refreshAllGroups() {
      getGroups({groupTypes: 'g,c,d,t'})
        .then(res => {
          let opt = {
            primaryKey: 'groupId',
            parentKey: 'groupParentId',
            startPid: '0'
          }
          let xx = res.data;
          xx.unshift({
            groupId: 0,
            groupName: '无',
            groupParentId: "0",
            groupState: "10",
            groupType: ""
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
      this.group.deeply = items.reverse();

      getGroupChildren({groupId: node.groupId})
        .then(res => {
          let array = [];
          res.data.map(item => {
            item.title = item.groupName;
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

        // 组织详情
        viewGroup({groupId: node.groupId})
          .then(res => {
            this.group.viewData = res.data;
            this.group.viewShow = node.groupId !== 0;
          })
          .finally(() => {
          });
        // this.user.pageInfo.groupId = node.groupId;
        // this.role.pageInfo.groupId = node.groupId;
        // this.handleRoleSearch(1);
        // this.handleUserSearch(1);
      } else {
        this.group.viewData = {};
        this.group.viewShow = false;
      }
    }
  },
  mounted: function () {
    this.$refs.tree.handleSelect(0);
    this._refreshAllGroups();
  }
}
</script>
