<template>
  <div>
    <Card shadow>
      <Form :label-width="80" :model="pageInfo" inline ref="searchForm">
        <FormItem label="路径" prop="logPath">
          <Input placeholder="请输入关键字" type="text" v-model="pageInfo.logPath"/>
        </FormItem>
        <FormItem label="IP" prop="logIp">
          <Input placeholder="请输入关键字" type="text" v-model="pageInfo.logIp"/>
        </FormItem>
        <FormItem label="服务" prop="logServiceId">
          <Input placeholder="请输入关键字" type="text" v-model="pageInfo.logServiceId"/>
        </FormItem>
        <FormItem>
          <Button @click="handleSearch(1)" type="primary">查询</Button>&nbsp;
          <Button @click="handleResetForm('searchForm')">重置</Button>
        </FormItem>
      </Form>

      <Table size="small" :columns="columns" :data="data" :loading="loading" border>
        <template slot="logRequestTime" slot-scope="{ row }">
          <span>{{ row.logRequestTime | dateFmt('YYYY-MM-DD HH:mm:ss') }}</span>
        </template>
        <template slot="detail" slot-scope="{ row }">
          <a @click="openDrawer(row)">详情</a>
        </template>
      </Table>
      <Page size="small" :current="pageInfo.page" :page-size="pageInfo.limit" :total="pageInfo.total" @on-change="handlePage"
            @on-page-size-change='handlePageSize'
            show-elevator
            show-sizer
            show-total transfer></Page>
    </Card>
    <Drawer v-model="drawer" width="30">
      <div slot="header">
        <Badge status="success" v-if="row.logHttpStatus==='200'"/>
        <Badge status="error" v-else/>
        {{ row.logHttpStatus }} {{ row.logPath }} - {{ row.logServiceId }}
      </div>
      <div>
        <h3>请求头</h3>
        <pre>
             {{ row.logHeaders ? JSON.stringify(JSON.parse(row.logHeaders), null, 4) : '' }}
        </pre>
        <h3>请求参数</h3>
        <pre>
              {{ row.logParams ? JSON.stringify(JSON.parse(row.logParams), null, 4) : '' }}
        </pre>
        <h3>错误信息</h3>
        <pre>
          {{ row.logError }}
        </pre>
        <h3>认证信息</h3>
        <pre>
              {{ row.logAuthentication ? JSON.stringify(JSON.parse(row.logAuthentication), null, 4) : '' }}
        </pre>
      </div>
    </Drawer>
  </div>
</template>

<script>
import {getLogsPage} from '@/api/access/log'
import {readUserAgent} from "@/libs/util";

export default {
  data() {
    return {
      row: {},
      drawer: false,
      loading: false,
      pageInfo: {
        page: 1,
        limit: 10,
        logPath: '',
        logIp: '',
        logServiceId: ''
      },
      columns: [
        {title: '日志ID', key: 'logId', width: 170},
        {title: '地址', key: 'logPath', width: 200},
        {title: '方式', key: 'logMethod', width: 80},
        {title: 'IP', key: 'logIp', width: 120},
        {title: '区域', key: 'logRegion', width: 200},
        {
          title: '终端', key: 'operationMethod', width: 80, render: (h, params) => {
            return h('div', readUserAgent(params.row.logUserAgent).terminal)
          }
        },
        {
          title: '浏览器', key: 'operationAuth', width: 90, render: (h, params) => {
            return h('div', readUserAgent(params.row.logUserAgent).browser)
          }
        },
        {title: '服务名', key: 'logServiceId', width: 120},
        {title: '响应状态', key: 'logHttpStatus', width: 100},
        {title: '耗时(ms)', key: 'logUseMillis', width: 100},
        {title: '请求时间', key: 'logRequestTime', slot: 'logRequestTime', width: 160},
        {title: '详情', slot: 'detail', fixed: 'right', align: 'center', width: 100}
      ],
      data: []
    }
  },
  methods: {
    openDrawer(data) {
      this.row = data
      this.drawer = true
    },
    handleSearch(page) {
      if (page) {
        this.pageInfo.page = page;
      }
      this.loading = true
      getLogsPage(this.pageInfo)
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
