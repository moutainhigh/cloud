<template>
  <div>
    <Card shadow>
      <Form :label-width="80"
            :model="pageInfo"
            inline
            ref="searchForm">
        <FormItem label="通知地址" prop="url">
          <Input placeholder="请输入关键字" type="text" v-model="pageInfo.url"/>
        </FormItem>
        <FormItem label="业务类型" prop="type">
          <Input placeholder="请输入关键字" type="text" v-model="pageInfo.type"/>
        </FormItem>
        <FormItem label="通知结果" prop="result">
          <Select v-model="pageInfo.result">
            <Option value="">全部</Option>
            <Option value="1">成功</Option>
            <Option value="0">失败</Option>
          </Select>
        </FormItem>
        <FormItem>
          <Button @click="handleSearch(1)" type="primary">查询</Button>&nbsp;
          <Button @click="handleResetForm('searchForm')">重置</Button>
        </FormItem>
      </Form>

      <Table :columns="columns" :data="data" :loading="loading" border>
        <template slot="status" slot-scope="{ row }">
          <Badge status="success" text="成功" v-if="row.result===1"/>
          <Badge status="error" text="失败" v-else=""/>
        </template>
        <template slot="detail" slot-scope="{ row }">
          <a @click="openDrawer(row)">详情</a>
        </template>
      </Table>
      <Page :current="pageInfo.page" :page-size="pageInfo.limit" :total="pageInfo.total" @on-change="handlePage" @on-page-size-change='handlePageSize'
            show-elevator
            show-sizer
            show-total transfer></Page>
    </Card>
    <Drawer :closable="false" v-model="drawer" width="50">
      <div slot="header">
        <Badge status="success" v-if="currentRow.result===1"/>
        <Badge status="error" v-else=""/>
        {{currentRow.url}}

      </div>
      <div>
        <h3>执行参数</h3>
        <pre>
              {{ currentRow.data ? JSON.stringify(JSON.parse(currentRow.data), null, 2) : ''}}
        </pre>
      </div>
    </Drawer>
  </div>
</template>

<script>
    import {getNotifyHttpLogs} from '@/api/msg'

    export default {
        name: 'MsgHttpLogs',
        data() {
            return {
                drawer: false,
                currentRow: {},
                loading: false,
                pageInfo: {
                    total: 0,
                    page: 1,
                    limit: 10,
                    url: '',
                    type: '',
                    result: ''
                },
                columns: [
                    {
                        type: 'selection',
                        width: 60,
                        align: 'center'
                    },
                    {
                        title: '通知标识',
                        key: 'msgId',
                        width: 200
                    },
                    {
                        title: '通知地址',
                        key: 'url',
                        width: 350
                    },
                    {
                        title: '业务类型',
                        key: 'type',
                        width: 150
                    },
                    {
                        title: '通知结果',
                        key: 'result',
                        slot: 'status',
                        width: 100
                    },
                    {
                        title: '重试次数',
                        key: 'retryNums',
                        width: 100
                    },
                    {
                        title: '通知次数',
                        key: 'totalNums',
                        width: 100
                    },
                    {
                        title: '当前重试时间',
                        key: 'delay',
                        render: (h, params) => {
                            return h('div', (params.row.delay ? params.row.delay / 1000 : 0) + ' s')
                        },
                        width: 200
                    },
                    {
                        title: '最后修改时间',
                        key: 'lastModifiedDate'
                    },
                    {
                        title: '详情',
                        slot: 'detail',
                        fixed: 'right',
                        width: 150
                    }
                ],
                data: []
            }
        },
        methods: {
            openDrawer(data) {
                this.currentRow = data
                this.drawer = true
            },
            handleSearch(page) {
                if (page) {
                    this.pageInfo.page = page
                }
                this.loading = true
                getNotifyHttpLogs(this.pageInfo).then(res => {
                    this.data = res.data.records
                    this.pageInfo.total = parseInt(res.data.total)
                }).finally(() => {
                    this.loading = false
                })
            },
            handleResetForm(form) {
                this.$refs[form].resetFields()
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
