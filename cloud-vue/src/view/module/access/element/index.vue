<template>
  <div>
    <Card shadow>
      <Form :label-width="80" :model="pageInfo" inline ref="searchForm">
        <FormItem label="元素名" prop="elementName">
          <Input placeholder="请输入关键字" type="text" v-model="pageInfo.elementName"/>
        </FormItem>
        <FormItem label="元素编码" prop="elementCode">
          <Input placeholder="请输入关键字" type="text" v-model="pageInfo.elementCode"/>
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
import {getElementsPage} from '@/api/access/element'

export default {
  data() {
    return {
      currentRow: {},
      loading: false,
      pageInfo: {
        page: 1,
        limit: 10,
        elementName: '',
        elementCode: ''
      },
      columns: [
        {title: '元素ID', key: 'elementId', width: 170},
        {title: '元素编码（按钮权限标识）', key: 'elementCode'},
        {title: '元素名', key: 'elementName'},
        {title: '创建时间', key: 'createdDate', slot: 'createdDate'}
      ],
      data: []
    }
  },
  methods: {
    handleSearch(page) {
      if (page) {
        this.pageInfo.page = page
      }
      this.loading = true
      getElementsPage(this.pageInfo)
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
