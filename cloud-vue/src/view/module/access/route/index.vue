<template>
  <div>
    <Card shadow>
      <Form :label-width="80" :model="pageInfo" inline ref="searchForm">
        <FormItem label="路由名" prop="routeName">
          <Input placeholder="请输入关键字" type="text" v-model="pageInfo.routeName"/>
        </FormItem>
        <FormItem label="路由URL" prop="routePath">
          <Input placeholder="请输入关键字" type="text" v-model="pageInfo.routePath"/>
        </FormItem>
        <FormItem>
          <Button @click="handleSearch(1)" type="primary">查询</Button>&nbsp;
          <Button @click="handleResetForm('searchForm')">重置</Button>
        </FormItem>
      </Form>

      <Alert show-icon>谨慎添加或修改路由,如果修改不当,将影响正常访问！&nbsp;<a @click="handleRefreshGateway">手动刷新网关</a></Alert>
      <Table size="small" :columns="columns" :data="data" :loading="loading" border>
        <template slot="createdDate" slot-scope="{ row }">
          <span>{{ row.createdDate | dateFmt('YYYY-MM-DD HH:mm:ss') }}</span>
        </template>
        <template slot="action" slot-scope="{ row }">
          <a>编辑</a>&nbsp;
        </template>
      </Table>
      <Page :current="pageInfo.page" :page-size="pageInfo.limit" :total="pageInfo.total" @on-change="handlePage"
            @on-page-size-change='handlePageSize'
            show-elevator
            show-sizer
            show-total transfer></Page>
    </Card>
  </div>
</template>

<script>
import {getRoutesPage} from '@/api/access/route';
import {refreshGateway} from "@/api/gateway";

export default {
  data() {
    return {
      currentRow: {},
      loading: false,
      pageInfo: {
        page: 1,
        limit: 10,
        routeName: '',
        routePath: ''
      },
      columns: [
        {title: '路由ID', key: 'routeId', width: 170},
        {title: '路由名', key: 'routeName', width: 110},
        {title: '路由前缀', key: 'routePath', width: 90},
        {title: '路由服务', key: 'routeServiceId', width: 110},
        {title: '路由URL', key: 'routeUrl', width: 150},
        {title: '忽略前缀', key: 'routeStripPrefix', width: 90},
        {title: '错误重试', key: 'routeRetryable', width: 90},
        {title: '状态', key: 'routeState', width: 70},
        {title: '路由描述', key: 'routeDesc', width: 120},
        {title: '创建时间', key: 'createdDate', slot: 'createdDate', width: 160},
        {title: '操作', slot: 'action', fixed: 'right', align: 'center', width: 120}
      ],
      data: []
    }
  },
  methods: {
    handleSearch(page) {
      if (page) {
        this.pageInfo.page = page;
      }
      this.loading = true
      getRoutesPage(this.pageInfo)
        .then(res => {
          this.data = res.data.records
          this.pageInfo.total = parseInt(res.data.total)
        })
        .finally(() => {
          this.loading = false
        })
    },
    handleResetForm(form) {
      this.$refs[form].resetFields();
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
    },
    handlePage(current) {
      this.pageInfo.page = current
      this.handleSearch()
    },
    handlePageSize(size) {
      this.pageInfo.limit = size
      this.handleSearch()
    }
  },
  mounted: function () {
    this.handleSearch()
  }
}
</script>
