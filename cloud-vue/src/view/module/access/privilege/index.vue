<template>
  <div>
    <Card shadow>
      <Form :label-width="80" :model="pageInfo" inline ref="searchForm">
        <FormItem label="权限ID" prop="privilegeId">
          <Input placeholder="请输入关键字" type="text" v-model="pageInfo.privilegeId"/>
        </FormItem>
        <FormItem label="权限" prop="privilege">
          <Input placeholder="请输入关键字" type="text" v-model="pageInfo.privilege"/>
        </FormItem>
        <FormItem label="权限类别" prop="privilegeType">
          <label>
            <Select v-model="pageInfo.privilegeType">
              <Option value="">请选择</Option>
              <Option value="m">菜单</Option>
              <Option value="o">操作</Option>
              <Option value="e">元素</Option>
            </Select>
          </label>
        </FormItem>
        <FormItem>
          <Button @click="handleSearch(1)" type="primary">查询</Button>&nbsp;
          <Button @click="handleResetForm('searchForm')">重置</Button>
        </FormItem>
      </Form>

      <Table size="small" :columns="columns" :data="data" :loading="loading" border>
        <template slot="privilegeType" slot-scope="{ row }">
          <Tag color="magenta" v-if="row.privilegeType==='m'">菜单</Tag>
          <Tag color="cyan" v-else-if="row.privilegeType==='o'">操作</Tag>
          <Tag color="purple" v-else-if="row.privilegeType==='e'">元素</Tag>
        </template>
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
import {getPrivilegesPage} from '@/api/access/privilege';

export default {
  data() {
    return {
      currentRow: {},
      loading: false,
      pageInfo: {
        page: 1,
        limit: 10,
        privilegeId: '',
        privilege: '',
        privilegeType: ''
      },
      columns: [
        {title: '权限ID', key: 'privilegeId', width: 170},
        {title: '权限标识（菜单、操作、元素）', key: 'privilege'},
        {title: '类别', key: 'privilegeType', slot: 'privilegeType', width: 100},
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
      getPrivilegesPage(this.pageInfo)
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
