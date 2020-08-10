<template>
  <div>
    <Card shadow>
      <Form :label-width="80" :model="pageInfo" inline ref="searchForm">
        <FormItem label="操作名" prop="operationName">
          <Input placeholder="请输入关键字" type="text" v-model="pageInfo.operationName"/>
        </FormItem>
        <FormItem label="操作描述" prop="operationDesc">
          <Input placeholder="请输入关键字" type="text" v-model="pageInfo.operationDesc"/>
        </FormItem>
        <FormItem label="操作URL" prop="operationPath">
          <Input placeholder="请输入关键字" type="text" v-model="pageInfo.operationPath"/>
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
import {getOperationsPage} from '@/api/access/operation'

export default {
  data() {
    return {
      currentRow: {},
      loading: false,
      pageInfo: {
        page: 1,
        limit: 10,
        operationName: '',
        operationDesc: '',
        operationPath: ''
      },
      columns: [
        // {title: '操作ID', key: 'operationId', width: 160},
        // {title: '操作编码', key: 'operationCode', width: 260},
        {title: '操作URL', key: 'operationPath', width: 200},
        {title: '操作名', key: 'operationName', width: 180},
        {title: '操作描述', key: 'operationDesc', width: 200},
        {title: '操作方法', key: 'operationMethod', width: 90},

        {title: '需要认证', key: 'operationAuth', width: 90},
        {title: '是否开放', key: 'operationOpen', width: 90},
        {title: '状态', key: 'operationState', width: 80},
        {title: '所属服务', key: 'operationServiceId', width: 120},

        {title: '创建时间', key: 'createdDate', slot: 'createdDate', width: 160}
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
      getOperationsPage(this.pageInfo)
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
