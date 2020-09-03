<template>
  <div>
    <Card shadow>
      <Form :label-width="80" :model="pageInfo" inline ref="searchForm">
        <FormItem label="路由名称" prop="routeName">
          <Input placeholder="请输入关键字" type="text" v-model="pageInfo.routeName"/>
        </FormItem>
        <FormItem label="路由前缀" prop="routePath">
          <Input placeholder="请输入关键字" type="text" v-model="pageInfo.routePath"/>
        </FormItem>
        <FormItem>
          <Button @click="handleSearch(1)" type="primary">查询</Button>&nbsp;
          <Button @click="handleResetSearchForm('searchForm')">重置</Button>
        </FormItem>
      </Form>
      <div class="search-con search-con-top">
        <ButtonGroup>
          <Button :disabled="!hasAuthority('gatewayRouteEdit')" @click="handleModal()" class="search-btn"
                  type="primary">
            <span>添加</span>
          </Button>
        </ButtonGroup>
      </div>
      <Alert show-icon>谨慎添加或修改路由,如果修改不当,将影响正常访问！&nbsp;<a @click="handleRefreshGateway">手动刷新网关</a></Alert>
      <Table size="small" :columns="columns" :data="data" :loading="loading" border>
        <template slot="routeState" slot-scope="{ row }">
          <Badge status="success" text="启用" v-if="row.routeState==='10'"/>
          <Badge status="error" text="禁用" v-else/>
        </template>
        <template slot="routeType" slot-scope="{ row }">
          <span v-if="row.routeServiceId"><Tag color="green">负载均衡</Tag>{{ row.routeServiceId }}</span>
          <span v-else-if="row.routeUrl"><Tag color="blue">反向代理</Tag>{{ row.routeUrl }}</span>
        </template>

        <template slot="action" slot-scope="{ row }">
          <a :disabled="!hasAuthority('gatewayRouteEdit')" @click="handleModal(row)">编辑</a>&nbsp;
          <Dropdown @on-click="handleClick($event,row)" ref="dropdown" transfer
                    v-show="hasAuthority('gatewayRouteEdit')">
            <a href="javascript:void(0)">
              <span>更多</span>
              <Icon type="ios-arrow-down"></Icon>
            </a>
            <DropdownMenu slot="list">
              <DropdownItem name="remove">删除</DropdownItem>
            </DropdownMenu>
          </Dropdown>&nbsp;
        </template>
      </Table>
      <Page :current="pageInfo.page" :page-size="pageInfo.limit" :total="pageInfo.total" @on-change="handlePage"
            @on-page-size-change='handlePageSize'
            show-elevator
            show-sizer show-total></Page>
    </Card>

    <Modal :title="modalTitle"
           @on-cancel="handleReset"
           v-model="modalVisible"
           width="40">
      <div>
        <Form :label-width="100" :model="formItem" :rules="formItemRules" ref="routeForm">
          <FormItem label="路由名称" prop="routeDesc">
            <Input placeholder="请输入内容" v-model="formItem.routeDesc"></Input>
          </FormItem>
          <FormItem label="路由标识" prop="routeName">
            <Input placeholder="默认使用服务名称{application.name}" v-model="formItem.routeName"></Input>
          </FormItem>
          <FormItem label="路由前缀" prop="routePath">
            <Input placeholder="/{path}/**" v-model="formItem.routePath"></Input>
          </FormItem>
          <FormItem label="路由方式">
            <Select v-model="selectType">
              <Option label="负载均衡(routeServiceId)" value="routeServiceId"></Option>
              <Option label="反向代理(routeUrl)" value="routeUrl"></Option>
            </Select>
          </FormItem>
          <FormItem :rules="{required: true, message: '服务名称不能为空', trigger: 'blur'}" label="负载均衡" prop="routeServiceId"
                    v-if="selectType==='routeServiceId'">
            <Input placeholder="服务名称application.name" v-model="formItem.routeServiceId"></Input>
          </FormItem>
          <FormItem
            :rules="[{required: true, message: '服务地址不能为空', trigger: 'blur'},{type: 'routeUrl', message: '请输入有效网址', trigger: 'blur'}]"
            label="反向代理" prop="routeUrl"
            v-if="selectType==='routeUrl'">
            <Input placeholder="http://localhost:8080" v-model="formItem.routeUrl"></Input>
          </FormItem>
          <FormItem label="忽略前缀">
            <RadioGroup type="button" v-model="formItem.routeStripPrefix">
              <Radio label="0">否</Radio>
              <Radio label="1">是</Radio>
            </RadioGroup>
          </FormItem>
          <FormItem label="失败重试">
            <RadioGroup type="button" v-model="formItem.routeRetryable">
              <Radio label="0">否</Radio>
              <Radio label="1">是</Radio>
            </RadioGroup>
          </FormItem>
          <FormItem label="状态">
            <RadioGroup type="button" v-model="formItem.routeState">
              <Radio label="10">启用</Radio>
              <Radio label="20">禁用</Radio>
            </RadioGroup>
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
import {addRoute, removeRoute, updateRoute} from '@/api/route';
import {getRoutesPage} from '@/api/access/route';
import {refreshGateway} from '@/api/gateway';

export default {
  data() {
    return {
      loading: false,
      saving: false,
      modalVisible: false,
      modalTitle: '',
      pageInfo: {
        total: 0,
        page: 1,
        limit: 10
      },
      selectType: 'routeServiceId',
      selectServiceList: [],
      formItemRules: {
        routeDesc: [
          {required: true, message: '路由名称不能为空', trigger: 'blur'}
        ],
        routeName: [
          {required: true, message: '路由标识不能为空', trigger: 'blur'}
        ],
        routePath: [
          {required: true, message: '路由前缀不能为空', trigger: 'blur'}
        ]
      },
      formItem: {
        routeId: '',
        routePath: '',
        routeServiceId: '',
        routeUrl: '',
        routeStripPrefix: '0',
        routeRetryable: '0',
        routeState: '10',
        routeName: '',
        routeDesc: ''
      },
      columns: [
        {title: '路由名称', key: 'routeDesc', width: 150},
        {title: '路由标识', key: 'routeName', width: 120},
        {title: '路由前缀', key: 'routePath', width: 100},
        {title: '路由方式', key: 'routeType', slot: 'routeType', width: 200},
        {title: '忽略前缀', key: 'routeStripPrefix'},
        {title: '失败重试', key: 'routeRetryable'},
        {title: '状态', key: 'routeState', slot: 'routeState'},
        {title: '操作', slot: 'action', fixed: 'right', width: 120}
      ],
      data: []
    }
  },
  methods: {
    handleModal(data) {
      if (data) {
        this.modalTitle = '编辑路由';
        this.formItem = Object.assign({}, this.formItem, data)
      } else {
        this.modalTitle = '添加路由'
      }
      // this.formItem.routeState = this.formItem.routeState + '';
      this.formItem.stripPrefix = this.formItem.stripPrefix + '';
      this.formItem.retryable = this.formItem.retryable + '';
      // this.formItem.routeUrl = this.formItem.routeServiceId ? '' : this.formItem.routeUrl;
      // this.formItem.routeServiceId = this.formItem.routeUrl ? '' : this.formItem.routeServiceId;
      this.selectType = this.formItem.routeUrl ? 'routeUrl' : 'routeServiceId';
      this.modalVisible = true
    },
    handleReset() {
      this.formItem = {
        routeId: '',
        routePath: '',
        routeServiceId: '',
        routeUrl: '',
        routeStripPrefix: 0,
        routeRetryable: 0,
        routeState: '10',
        routeName: '',
        routeDesc: ''
      };
      //重置验证
      this.$refs['routeForm'].resetFields();
      this.modalVisible = false;
      this.saving = false;
    },
    handleSubmit() {
      this.$refs['routeForm'].validate((valid) => {
        if (valid) {
          this.saving = true;
          if (this.formItem.routeId) {
            updateRoute(this.formItem)
              .then(res => {
                this.$Message.success('保存成功');
                this.handleReset()
                this.handleSearch()
              })
              .finally(() => {
                this.saving = false
              })
          } else {
            addRoute(this.formItem)
              .then(res => {
                this.handleReset();
                this.handleSearch();
                this.$Message.success('保存成功')
              })
              .finally(() => {
                this.saving = false
              })
          }
        }
      })
    },
    handleSearch(page) {
      if (page) {
        this.pageInfo.page = page
      }
      this.loading = true;
      getRoutesPage(this.pageInfo)
        .then(res => {
          this.data = res.data.records;
          this.pageInfo.total = parseInt(res.data.total)
        })
        .finally(() => {
          this.loading = false
        })
    },
    handleResetSearchForm(form) {
      this.$refs[form].resetFields();
    },
    handlePage(current) {
      this.pageInfo.page = current;
      this.handleSearch()
    },
    handlePageSize(size) {
      this.pageInfo.limit = size;
      this.handleSearch()
    },
    handleRemove(data) {
      this.$Modal.confirm({
        title: '确定删除吗？',
        onOk: () => {
          removeRoute(data.routeId)
            .then(res => {
              this.pageInfo.page = 1;
              this.$Message.success('删除成功')
              this.handleSearch()
            })
        }
      })
    },
    handleClick(name, row) {
      if (name === 'remove') {
        this.handleRemove(row);
      }
    },
    handleRefreshGateway() {
      this.$Modal.confirm({
        title: '提示',
        content: '将重新加载所有网关实例包括（访问权限、流量限制、IP访问限制、路由缓存），是否继续？',
        onOk: () => {
          refreshGateway()
            .then(res => {
              this.$Message.success('刷新成功')
            })
        }
      })
    }
  },
  mounted: function () {
    this.handleSearch()
  }
}
</script>
