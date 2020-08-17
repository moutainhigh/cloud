<template>
  <div>
    <Card shadow>
      <Form :label-width="60" :model="pageInfo" inline ref="searchForm">
        <FormItem label="操作名" prop="operationName">
          <Input placeholder="请输入关键字" type="text" v-model="pageInfo.operationName"/>
        </FormItem>
        <FormItem label="操作描述" prop="operationDesc">
          <Input placeholder="请输入关键字" type="text" v-model="pageInfo.operationDesc"/>
        </FormItem>
        <FormItem label="操作URL" prop="operationPath">
          <Input placeholder="请输入关键字" type="text" v-model="pageInfo.operationPath"/>
        </FormItem>
        <FormItem label="所属服务" prop="operationServiceId">
          <Input placeholder="请输入关键字" type="text" v-model="pageInfo.operationServiceId"/>
        </FormItem>
        <FormItem>
          <Button @click="handleSearch(1)" type="primary">查询</Button>&nbsp;
          <Button @click="handleResetForm('searchForm')">重置</Button>
        </FormItem>
      </Form>
      <div class="search-con search-con-top">
        批量操作按钮>>>
        <Dropdown @on-click="handleBatchClick" v-if="tableSelection.length>0" style="margin-left: 20px">
          <Button>
            <span>批量操作</span>
            <Icon type="ios-arrow-down"></Icon>
          </Button>
          <DropdownMenu slot="list">
            <DropdownItem name="remove">删除</DropdownItem>
            <Dropdown placement="right-start">
              <DropdownItem>
                <span>状态</span>
                <Icon type="ios-arrow-forward"></Icon>
              </DropdownItem>
              <DropdownMenu slot="list">
                <DropdownItem name="state10">启用</DropdownItem>
                <DropdownItem name="state20">禁用</DropdownItem>
                <DropdownItem name="state30">维护中</DropdownItem>
              </DropdownMenu>
            </Dropdown>
            <Dropdown placement="right-start">
              <DropdownItem>
                <span>公开访问</span>
                <Icon type="ios-arrow-forward"></Icon>
              </DropdownItem>
              <DropdownMenu slot="list">
                <DropdownItem name="openTrue">允许公开访问</DropdownItem>
                <DropdownItem name="openFalse">拒绝公开访问</DropdownItem>
              </DropdownMenu>
            </Dropdown>
            <Dropdown placement="right-start">
              <DropdownItem>
                <span>身份认证</span>
                <Icon type="ios-arrow-forward"></Icon>
              </DropdownItem>
              <DropdownMenu slot="list">
                <DropdownItem name="authTrue">开启身份认证</DropdownItem>
                <DropdownItem name="authFalse">关闭身份认证</DropdownItem>
              </DropdownMenu>
            </Dropdown>
          </DropdownMenu>
        </Dropdown>
      </div>
      <Table size="small" :columns="columns" :data="data" :loading="loading"
             @on-selection-change="handleTableSelectChange" border>
        <template slot="createdDate" slot-scope="{ row }">
          <span>{{ row.createdDate | dateFmt('YYYY-MM-DD HH:mm:ss') }}</span>
        </template>
        <template slot="operationState" slot-scope="{ row }">
          <Badge status="green" v-if="row.operationState==='10'" text="启用"/>
          <Badge status="orange" v-else-if="row.operationState==='20'" text="禁用"/>
          <Badge status="red" v-else-if="row.operationState==='30'" text="锁定"/>
        </template>
        <template slot="operationAuth" slot-scope="{ row }">
          <Badge status="green" v-if="row.operationAuth===true" text="开启"/>
          <Badge status="orange" v-else-if="row.operationAuth===false" text="关闭"/>
        </template>
        <template slot="operationOpen" slot-scope="{ row }">
          <Badge status="green" v-if="row.operationOpen===true" text="允许"/>
          <Badge status="orange" v-else-if="row.operationOpen===false" text="拒绝"/>
        </template>
        <template slot="action" slot-scope="{ row }">
          <a @click="handleRemove(row)">删除</a>
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
import {
  getOperationsPage,
  removeOperation,
  updateOperationAuth,
  updateOperationOpen,
  updateOperationState
} from '@/api/access/operation'
import {removeElement} from "@/api/access/element";

export default {
  data() {
    return {
      loading: false,
      tableSelection: [],
      pageInfo: {
        page: 1,
        limit: 10,
        operationName: '',
        operationDesc: '',
        operationPath: '',
        operationServiceId: ''
      },
      columns: [
        {type: 'selection', width: 50, align: 'center'},
        {title: '操作URL', key: 'operationPath', tooltip: true, width: 180},
        {title: '操作名', key: 'operationName', width: 180},
        {title: '操作方法', key: 'operationMethod', width: 90},
        {title: '公开访问', key: 'operationOpen', slot: "operationOpen", width: 90},
        {title: '身份认证', key: 'operationAuth', slot: "operationAuth", width: 90},
        {title: '状态', key: 'operationState', slot: "operationState", width: 80},
        {title: '操作描述', key: 'operationDesc', tooltip: true, width: 180},
        {title: '所属服务', key: 'operationServiceId', width: 120},
        {title: '操作ID', key: 'operationId', width: 170},
        {title: '操作编码（操作权限标识）', key: 'operationCode', width: 260},
        {title: '创建时间', key: 'createdDate', slot: 'createdDate', width: 160},
        {title: '操作', slot: 'action', fixed: 'right', align: 'center', width: 120}
      ],
      data: []
    }
  },
  methods: {
    handleSearch(page) {
      this.tableSelection = [];
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
    handleTableSelectChange(selection) {
      this.tableSelection = selection
    },
    handleBatchClick(name) {
      if (name) {
        this.$Modal.confirm({
          title: `已勾选${this.tableSelection.length}项，是否继续执行操作？`,
          onOk: () => {
            let ids = []
            this.tableSelection
              .map(item => {
                if (!ids.includes(item.operationId)) {
                  ids.push(item.operationId)
                }
              })
            let operationIds = ids.join(',')

            switch (name) {
              case'remove':
                removeOperation(operationIds)
                  .then(res => {
                    this.$Message.success('批量移除成功')
                    this.handleSearch()
                  })
                break;
              case'state10':
                updateOperationState({operationIds: operationIds, operationState: "10"})
                  .then(res => {
                    this.$Message.success('批量启用成功')
                    this.handleSearch()
                  });
                break;
              case'state20':
                updateOperationState({operationIds: operationIds, operationState: "20"})
                  .then(res => {
                    this.$Message.success('批量禁用成功')
                    this.handleSearch()
                  });
                break;
              case'state30':
                updateOperationState({operationIds: operationIds, operationState: "30"})
                  .then(res => {
                    this.$Message.success('批量锁定成功')
                    this.handleSearch()
                  });
                break;
              case'authTrue':
                updateOperationAuth({operationIds: operationIds, operationAuth: true})
                  .then(res => {
                    this.$Message.success('批量开启身份认证成功')
                    this.handleSearch()
                  });
                break;
              case'authFalse':
                updateOperationAuth({operationIds: operationIds, operationAuth: false})
                  .then(res => {
                    this.$Message.success('批量关闭身份认证成功')
                    this.handleSearch()
                  });
                break;
              case'openTrue':
                updateOperationOpen({operationIds: operationIds, operationOpen: true})
                  .then(res => {
                    this.$Message.success('批量允许公开访问成功')
                    this.handleSearch()
                  });
                break;
              case'openFalse':
                updateOperationOpen({operationIds: operationIds, operationOpen: false})
                  .then(res => {
                    this.$Message.success('批量拒绝公开访问成功')
                    this.handleSearch()
                  });
                break;
            }
          }
        })
      }
    },
    handleRemove(data) {
      this.$Modal.confirm({
        title: '确定删除吗？',
        onOk: () => {
          removeOperation(data.operationId)
            .then(res => {
              this.$Message.success('移除成功')
              this.handleSearch()
            })
        }
      });
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
