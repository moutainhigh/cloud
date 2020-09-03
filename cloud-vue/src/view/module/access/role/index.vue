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
          <Button @click="handleResetForm('searchForm')">重置</Button>&nbsp;&nbsp;
          <Button @click="handleModal()" type="dashed">添加</Button>
        </FormItem>
      </Form>

      <Alert show-icon type="info"><code>权限</code>,<code>成员</code></Alert>
      <Table size="small" :columns="columns" :data="data" :loading="loading" border>
        <template slot="createdDate" slot-scope="{ row }">
          <span>{{ row.createdDate | dateFmt('YYYY-MM-DD HH:mm:ss') }}</span>
        </template>
        <template slot="action" slot-scope="{ row }">
          <a @click="handleModal(row)">编辑</a>&nbsp;&nbsp;
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
        <FormItem label="角色名称" prop="roleName">
          <label>
            <Input placeholder="请输入内容" v-model="modal.formItem.roleName"></Input>
          </label>
        </FormItem>
        <FormItem label="角色标识" prop="roleCode">
          <label>
            <Input placeholder="请以`ROLE_`为前缀命名" v-model="modal.formItem.roleCode"></Input>
          </label>
        </FormItem>
        <FormItem label="角色描述" prop="roleDesc">
          <label>
            <Input placeholder="请输入内容" v-model="modal.formItem.roleDesc"></Input>
          </label>
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
import {addRole, getRolesPage, removeRole, updateRole} from '@/api/access/role';

export default {
  data() {
    return {
      loading: false,
      modal: {
        title: '添加角色',
        visible: false,
        saving: false,
        formItem: {
          roleId: '',
          roleName: '',
          roleCode: '',
          roleDesc: ''
        },
        formItemRules: {
          roleName: [
            {required: true, message: '角色名称 必填', trigger: 'blur'}
          ],
          roleCode: [
            {required: true, message: '角色标识 必填', trigger: 'blur'}
          ]
        }
      },
      pageInfo: {
        page: 1,
        limit: 10,
        roleName: '',
        roleCode: ''
      },
      columns: [
        {title: '角色ID', key: 'roleId', width: 170},
        {title: '角色名', key: 'roleName', width: 120},
        {title: '角色编码', key: 'roleCode', width: 200},
        {title: '角色描述', key: 'roleDesc'},
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
      getRolesPage(this.pageInfo)
        .then(res => {
          this.data = res.data.records
          this.pageInfo.total = parseInt(res.data.total)
        })
        .finally(() => {
          this.loading = false
        })
    },
    /**
     * 弹框
     */
    handleModal(data) {
      if (data) {
        this.modal.title = '编辑角色';
        this.modal.formItem = Object.assign({}, this.modal.formItem, data);
      }
      this.modal.visible = true;
    },
    /**
     * 弹框保存事件
     */
    handleModalSubmit() {
      this.$refs['modalForm'].validate((valid) => {
        if (valid) {
          this.modal.saving = true;
          if (this.modal.formItem.roleId) {
            updateRole(this.modal.formItem)
              .then(() => {
                this.$Message.success('更新成功');
                this.handleReset();
                this.handleSearch();
              })
              .finally(() => {
                this.modal.saving = false;
              });
          } else {
            addRole(this.modal.formItem)
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
    handleRemove(data) {
      this.$Modal.confirm({
        title: '确定删除吗？',
        onOk: () => {
          removeRole(data.roleId)
            .then(() => {
              this.$Message.success('删除成功')
              this.handleSearch()
            })
        }
      });
    },
    handleReset() {
      this.modal.formItem = {
        roleId: '',
        roleName: '',
        roleCode: '',
        roleDesc: ''
      };
      // 重置验证
      this.$refs['modalForm'].resetFields();
      this.modal.visible = false;
      this.modal.saving = false;
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
