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
          <Button @click="handleResetForm('searchForm')">重置</Button>&nbsp;&nbsp;
          <Button @click="handleModal()" type="dashed">添加</Button>
        </FormItem>
      </Form>

      <Alert show-icon>谨慎添加或修改路由，如果修改不当，将影响正常访问！&nbsp;<a @click="handleRefreshGateway">手动刷新网关</a></Alert>
      <Table size="small" :columns="columns" :data="data" :loading="loading" border>
        <template slot="routeStripPrefix" slot-scope="{ row }">
          <Badge status="success" v-if="row.routeStripPrefix===true" text="是"/>
          <Badge status="orange" v-else-if="row.routeStripPrefix===false" text="否"/>
        </template>
        <template slot="routeRetryable" slot-scope="{ row }">
          <Badge status="success" v-if="row.routeRetryable===true" text="是"/>
          <Badge status="orange" v-else-if="row.routeRetryable===false" text="否"/>
        </template>
        <template slot="routeState" slot-scope="{ row }">
          <Badge status="green" v-if="row.routeState==='10'" text="启用"/>
          <Badge status="orange" v-else-if="row.routeState==='20'" text="禁用"/>
          <Badge status="red" v-else-if="row.routeState==='30'" text="锁定"/>
        </template>
        <template slot="createdDate" slot-scope="{ row }">
          <span>{{ row.createdDate | dateFmt('YYYY-MM-DD HH:mm:ss') }}</span>
        </template>
        <template slot="action" slot-scope="{ row }">
          <a @click="handleModal(row)">编辑</a>&nbsp;
          <a @click="handleRemove(row)">删除</a>
        </template>
      </Table>
      <Page :current="pageInfo.page" :page-size="pageInfo.limit" :total="pageInfo.total" @on-change="handlePage"
            @on-page-size-change='handlePageSize'
            show-elevator
            show-sizer
            show-total transfer></Page>
    </Card>
    <Modal :title="modal.title"
           v-model="modal.visible"
           @on-cancel="handleReset"
           width="40">
      <Form :label-width="80" :model="modal.formItem" :rules="modal.formItemRules" ref="modalForm">
        <FormItem label="路由名称" prop="routeDesc">
          <Input placeholder="请输入内容" v-model="modal.formItem.routeDesc"></Input>
        </FormItem>
        <FormItem label="路由标识" prop="routeName">
          <Input placeholder="默认使用服务名称{application.name}" v-model="modal.formItem.routeName"></Input>
        </FormItem>
        <FormItem label="路由前缀" prop="routePath">
          <Input placeholder="/{path}/**" v-model="modal.formItem.routePath"></Input>
        </FormItem>

        <FormItem label="路由方式">
          <Input placeholder="请输入负载均衡地址" v-model="modal.formItem.routeServiceId"></Input>
        </FormItem>
        <FormItem label="路由方式">
          <Input placeholder="请输入反向代理地址" v-model="modal.formItem.routeUrl"></Input>
        </FormItem>

        <FormItem label="忽略前缀" prop="routeStripPrefix">
          <RadioGroup type="button" v-model="modal.formItem.routeStripPrefix">
            <Radio :label="true">是</Radio>
            <Radio :label="false">否</Radio>
          </RadioGroup>
        </FormItem>
        <FormItem label="失败重试" prop="routeRetryable">
          <RadioGroup type="button" v-model="modal.formItem.routeRetryable">
            <Radio :label="true">是</Radio>
            <Radio :label="false">否</Radio>
          </RadioGroup>
        </FormItem>
        <FormItem label="路由状态" prop="routeState">
          <RadioGroup type="button" v-model="modal.formItem.routeState">
            <Radio label="10">启用</Radio>
            <Radio label="20">禁用</Radio>
          </RadioGroup>
        </FormItem>
        <div class="drawer-footer">
          <Button @click="handleReset" type="default">取消</Button>&nbsp;
          <Button :loading="modal.saving" @click="handleModalSubmit" type="primary">保存</Button>
        </div>
      </Form>
    </Modal>
  </div>
</template>

<script>
import {addRoute, getRoutesPage, refreshGateway, removeRoute, updateRoute} from '@/api/access/route';

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
        {title: '忽略前缀', key: 'routeStripPrefix', slot: 'routeStripPrefix', width: 90},
        {title: '错误重试', key: 'routeRetryable', slot: 'routeRetryable', width: 90},
        {title: '状态', key: 'routeState', slot: 'routeState', width: 80},
        {title: '路由描述', key: 'routeDesc', width: 120},
        {title: '创建时间', key: 'createdDate', slot: 'createdDate', width: 160},
        {title: '操作', slot: 'action', fixed: 'right', align: 'center', width: 120}
      ],
      data: [],
      modal: {
        title: '添加路由',
        visible: false,
        saving: false,
        formItem: {
          routeId: '',
          routeName: '',
          routeServiceId: '',
          routeUrl: '',
          routeStripPrefix: false,
          routeRetryable: false,
          routeState: '10'
        },
        formItemRules: {
          routeDesc: [
            {required: true, message: '路由名称 必填', trigger: 'blur'}
          ],
          routeName: [
            {required: true, message: '路由标识 必填', trigger: 'blur'}
          ],
          routePath: [
            {required: true, message: '路由前缀 必填', trigger: 'blur'}
          ],
          routeStripPrefix: [
            {required: true, message: '是否忽略前缀 必填', trigger: 'blur'}
          ],
          routeRetryable: [
            {required: true, message: '是否失败前缀 必填', trigger: 'blur'}
          ],
          routeState: [
            {required: true, message: '路由状态 必填', trigger: 'blur'}
          ]
        }
      }
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
    /**
     * 弹框
     */
    handleModal(data) {
      if (data) {
        this.modal.title = '编辑路由';
        this.modal.formItem = Object.assign({}, this.modal.formItem, data);
      }
      this.modal.visible = true;
    },
    handleReset() {
      this.modal.formItem = {
        routeId: '',
        routeName: '',
        routeServiceId: '',
        routeUrl: '',
        routeStripPrefix: false,
        routeRetryable: false,
        routeState: '10'
      };
      // 重置验证
      this.$refs['modalForm'].resetFields();
      this.modal.visible = false;
      this.modal.saving = false;
    },
    /**
     * 弹框保存事件
     */
    handleModalSubmit() {
      this.$refs['modalForm'].validate((valid) => {
        if (valid) {
          this.modal.saving = true;
          if (this.modal.formItem.routeId) {
            updateRoute(this.modal.formItem)
              .then(() => {
                this.$Message.success('更新成功');
                this.handleReset();
                this.handleSearch();
              })
              .finally(() => {
                this.modal.saving = false;
              });
          } else {
            addRoute(this.modal.formItem)
              .then(() => {
                this.$Message.success('添加成功');
                this.handleReset();
                this.handleSearch();
              })
              .finally(() => {
                this.modal.saving = false;
              });
          }
        }
      });
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
    handleRemove(data) {
      this.$Modal.confirm({
        title: '确定删除吗？',
        onOk: () => {
          removeRoute(data.routeId)
            .then(() => {
              this.$Message.success('删除成功')
              this.handleSearch()
            })
        }
      });
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
