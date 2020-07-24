<template>
  <div>
    <Card shadow>
      <Form :label-width="80"
            :model="pageInfo"
            inline
            ref="searchForm">
        <FormItem label="请求路径" prop="path">
          <Input placeholder="请输入关键字" type="text" v-model="pageInfo.path"/>
        </FormItem>
        <FormItem label="IP" prop="ip">
          <Input placeholder="请输入关键字" type="text" v-model="pageInfo.ip"/>
        </FormItem>
        <FormItem label="服务名" prop="serviceId">
          <Input placeholder="请输入关键字" type="text" v-model="pageInfo.serviceId"/>
        </FormItem>
        <FormItem>
          <Button @click="handleSearch(1)" type="primary">查询</Button>&nbsp;
          <Button @click="handleResetForm('searchForm')">重置</Button>
        </FormItem>
      </Form>

      <Table size="small" :columns="columns" :data="data" :loading="loading" border>
        <template slot="httpStatus" slot-scope="{ row }">
          <Badge status="success" v-if="row.httpStatus==='200'"/>
          <Badge status="error" v-else=""/>
          <span>{{row.httpStatus}}</span>
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
    <Drawer v-model="drawer" width="30">
      <div slot="header">
        <Badge status="success" v-if="currentRow.httpStatus==='200'"/>
        <Badge status="error" v-else=""/>
        {{currentRow.httpStatus}}
        {{currentRow.path}} - {{currentRow.serviceId}}

      </div>
      <div>
        <h3>请求头</h3>
        <pre>
             {{ currentRow.headers ? JSON.stringify(JSON.parse(currentRow.headers), null, 4) : ''}}
        </pre>
        <h3>请求参数</h3>
        <pre>
              {{ currentRow.params ? JSON.stringify(JSON.parse(currentRow.params), null, 4) : ''}}
        </pre>
        <h3>错误信息</h3>
        <pre>
          {{currentRow.error}}
        </pre>
        <h3>认证信息</h3>
        <pre>
              {{ currentRow.authentication ? JSON.stringify(JSON.parse(currentRow.authentication), null, 4) : ''}}
        </pre>
      </div>
    </Drawer>
  </div>
</template>

<script>
    import {getAccessLogs} from '@/api/gateway'
    import {readUserAgent} from '@/libs/util'

    export default {
        name: 'GatewayAccessLog',
        data() {
            return {
                drawer: false,
                currentRow: {},
                loading: false,
                pageInfo: {
                    total: 0,
                    page: 1,
                    limit: 10,
                    path: '',
                    ip: '',
                    serviceId: ''
                },
                columns: [
                    {title: '请求地址', key: 'path', width: 220},
                    {
                        title: '请求方式',
                        key: 'method',
                        width: 120,
                        filters: [
                            {
                                label: 'POST',
                                value: 0
                            },
                            {
                                label: 'GET',
                                value: 1
                            },
                            {
                                label: 'DELETE',
                                value: 2
                            },
                            {
                                label: 'OPTIONS',
                                value: 3
                            },
                            {
                                label: 'PATCH',
                                value: 4
                            }
                        ],
                        filterMultiple: false,
                        filterMethod(value, row) {
                            if (value === 0) {
                                return row.method === 'POST'
                            } else if (value === 1) {
                                return row.method === 'GET'
                            } else if (value === 2) {
                                return row.method === 'DELETE'
                            } else if (value === 3) {
                                return row.method === 'OPTIONS'
                            } else if (value === 4) {
                                return row.method === 'PATCH'
                            }
                        }
                    },
                    {title: 'IP', key: 'ip', width: 150},
                    {title: '区域', key: 'region', width: 200},
                    {
                        title: '终端',
                        width: 100,
                        render: (h, params) => {
                            return h('div', readUserAgent(params.row.userAgent).terminal)
                        }
                    },
                    {
                        title: '浏览器',
                        width: 100,
                        render: (h, params) => {
                            return h('div', readUserAgent(params.row.userAgent).browser)
                        }
                    },
                    {title: '服务名', key: 'serviceId', width: 200},
                    {title: '响应状态', key: 'httpStatus', slot: 'httpStatus', width: 100},
                    {
                        title: '耗时',
                        key: 'useTime',
                        render: (h, params) => {
                            return h('div', (params.row.useTime ? params.row.useTime : 0) + ' ms')
                        },
                        width: 100
                    },
                    {title: '请求时间', key: 'requestTime', width: 200},
                    {title: '详情', slot: 'detail', fixed: 'right', width: 120}
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
                getAccessLogs(this.pageInfo).then(res => {
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
