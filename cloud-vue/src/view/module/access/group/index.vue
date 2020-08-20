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

            <Button class="search-btn" type="dashed">编辑</Button>&nbsp;&nbsp;
            <Button class="search-btn" type="dashed">添加</Button>&nbsp;
            <Button class="search-btn" type="dashed">移除</Button>
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
  </div>
</template>
<script>
import {getGroupChildren, getGroupRoles, getGroupUsers, viewGroup} from '@/api/access/group';

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
      this.group.deeply = items.reverse();

      getGroupChildren({groupId: node.groupId})
        .then(res => {
          let array = [];
          res.data.map(item => {
            item.title = item.groupName;
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
            // this._loadData(item.groupId, () => {
            // });
          });
          // 挂载子节点
          node.children = array;
          // 展开子节点树
          node.expand = true;
        });

        // viewData 组织详情
        viewGroup({groupId: node.groupId})
          .then(res => {
            this.group.viewData = res.data;
          })
          .finally(() => {
          });
        this.user.pageInfo.groupId = node.groupId;
        this.role.pageInfo.groupId = node.groupId;
        this.handleRoleSearch(1);
        this.handleUserSearch(1);
      }
    }
  },
  mounted: function () {
    this.$refs.tree.handleSelect(0);
  }
}
</script>
