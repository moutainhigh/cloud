<template>
  <div>
    <Alert show-icon type="info">
      <Breadcrumb separator="<b class='demo-breadcrumb-separator'>></b>">
        <BreadcrumbItem>杭州冒险元素网络技术有限公司</BreadcrumbItem>
        <BreadcrumbItem>金融科技事业部</BreadcrumbItem>
      </Breadcrumb>
    </Alert>
    <Row :gutter="8">
      <Col :lg="6" :md="8" :sm="8" :xs="8">
        <Card shadow>
          <Tree :data="treeData"
                :load-data="loadTreeData"
                :render="renderChildren"
                :show-checkbox="false"
                @on-select-change="selectChange"/>
        </Card>
      </Col>
      <Col :lg="18" :md="16" :sm="16" :xs="16">
        <Col :lg="24" :md="16" :sm="16" :xs="16">
          <Card shadow>
            <Form :model="viewData" inline ref="viewForm">

            </Form>
          </Card>
        </Col>
        <Col :lg="24" :md="16" :sm="16" :xs="16">
          <br/>
          <Card shadow>
            <Form :label-width="80" :model="pageInfo" inline ref="searchForm">
              <FormItem label="组织类型" prop="groupType" style="width: 140px;">
                <label>
                  <Select v-model="pageInfo.groupType">
                    <Option value="g">集团</Option>
                    <Option value="c">公司</Option>
                    <Option value="d">部门</Option>
                    <Option value="t">小组</Option>
                  </Select>
                </label>
              </FormItem>
              <FormItem label="组织状态" prop="groupState" style="width: 140px;">
                <label>
                  <Select v-model="pageInfo.groupState">
                    <Option value="10">启用</Option>
                    <Option value="20">锁定</Option>
                    <Option value="30">禁用</Option>
                  </Select>
                </label>
              </FormItem>
              <FormItem>
                <Button @click="handleSearch(1)" type="primary">查询</Button>&nbsp;
                <Button @click="handleResetForm('searchForm')">重置</Button>
              </FormItem>
            </Form>
            <Table size="small" :columns="columns" :data="data" :loading="loading" border>
              <template slot="createdDate" slot-scope="{ row }">
                <span>{{ row.createdDate | dateFmt('YYYY-MM-DD HH:mm:ss') }}</span>
              </template>
              <template slot="groupType" slot-scope="{ row }">
                <Tag v-if="row.groupType==='g'" color="magenta">集团</Tag>
                <Tag v-if="row.groupType==='c'" color="gold">公司</Tag>
                <Tag v-if="row.groupType==='d'" color="lime">部门</Tag>
                <Tag v-if="row.groupType==='t'" color="cyan">小组</Tag>
              </template>
              <template slot="groupState" slot-scope="{ row }">
                <Badge status="success" text="启用" v-if="row.groupState==='10'"/>
                <Badge status="warning" text="锁定" v-else-if="row.groupState==='20'"/>
                <Badge status="error" text="禁用" v-else/>
              </template>
              <template slot="action" slot-scope="{ row }">
                <a @click="viewDrawer(row)">详情</a>
              </template>
            </Table>
            <Page :current="pageInfo.page" :page-size="pageInfo.limit" :total="pageInfo.total"
                  show-elevator
                  show-sizer
                  show-total transfer
                  @on-change="handlePage"
                  @on-page-size-change="handlePageSize"/>
          </Card>
        </Col>
      </Col>
    </Row>
  </div>
</template>
<script>
import {getGroups, getGroupsPage} from '@/api/access/group';

export default {
  data() {
    return {
      drawer: false,
      loading: false,
      pageInfo: {
        total: 0,
        page: 1,
        limit: 10,
        groupParentId: 0,
        groupType: '',
        groupState: ''
      },
      data: [],
      viewData: {},
      columns: [
        {title: '组织ID', key: 'groupId', width: 120},
        {title: '组织名', key: 'groupName', width: 210},
        {title: '组织类型', key: 'groupType', slot: 'groupType', width: 100},
        {title: '组织状态', key: 'groupState', slot: 'groupState', width: 100},
        {title: '创建时间', key: 'createdDate', slot: 'createdDate', width: 160},
        {title: '操作', slot: 'action', fixed: 'right'}
      ],
      treeData: [
        {
          groupId: 0,
          title: '组织机构',
          loading: false,
          children: [],
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
      ]
    }
  },
  methods: {
    handleResetForm(form) {
      this.$refs[form].resetFields()
    },
    handleSearch(page) {
      if (page) {
        this.pageInfo.page = page;
      }
      this.loading = true
      getGroupsPage(this.pageInfo)
          .then(res => {
            this.data = res.data.records;
            this.pageInfo.total = parseInt(res.data.total);
          })
          .finally(() => {
            this.loading = false;
          })
    },
    viewDrawer(data) {
      this.drawer = true;
    },
    /**
     * 加载子节点
     * @param item
     * @param callback
     */
    loadTreeData(item, callback) {
      getGroups({groupId: parseInt(item.groupId)})
          .then(res => {
            let data = [];
            res.data.map(d => {
              d.title = d.groupName;
              if (d['existChild']) {
                d.loading = false;
                d.children = [];
              } else {
                d.expand = true;
              }
              data.push(d);
            });
            callback(data);
          })
          .finally(() => {
          });
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
     * 点击选中节点文字
     * @param selectedList
     */
    selectChange(selectedList) {
      // 获取当前点击的节点
      const node = selectedList[selectedList.length - 1];
      this.pageInfo.groupParentId = node ? node.groupId : 0;
      this.handleSearch(1);

      // viewData 组织详情
    },
    handlePage(current) {
      this.pageInfo.page = current
      this.handleSearch()
    },
    handlePageSize(size) {
      this.pageInfo.limit = size
      this.handleSearch()
    },
  },
  mounted: function () {
  }
}
</script>
