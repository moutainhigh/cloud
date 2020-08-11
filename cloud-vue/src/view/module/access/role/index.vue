<template>
  <div>
    <Card shadow>
      <Form :label-width="80" :model="pageInfo" inline ref="searchForm">
        <FormItem label="角色名" prop="roleName">
          <Input placeholder="请输入关键字" type="text" v-model="pageInfo.roleName"/>
        </FormItem>
        <FormItem label="角色编码" prop="roleCode">
          <Input placeholder="请输入关键字" type="text" v-model="pageInfo.roleCode"/>
        </FormItem>
        <FormItem>
          <Button @click="handleSearch(1)" type="primary">查询</Button>&nbsp;
          <Button @click="handleResetForm('searchForm')">重置</Button>
        </FormItem>
      </Form>

      <Alert show-icon type="info"><code>权限</code>,<code>成员</code></Alert>
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
import {getRolesPage} from '@/api/access/role';

export default {
  data() {
    return {
      currentRow: {},
      loading: false,
      pageInfo: {
        page: 1,
        limit: 10,
        roleName: '',
        roleCode: ''
      },
      columns: [
        {title: '角色ID', key: 'roleId', width: 170},
        {title: '角色名', key: 'roleName'},
        {title: '角色编码', key: 'roleCode'},
        {title: '创建时间', key: 'createdDate', slot: 'createdDate'}
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
      getRolesPage(this.pageInfo)
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
