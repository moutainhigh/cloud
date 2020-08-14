<template>
  <div>
    <Card shadow>
      <Form :label-width="80" :model="pageInfo" inline ref="searchForm">
        <FormItem label="元素名" prop="elementName">
          <label>
            <Input placeholder="请输入关键字" type="text" v-model="pageInfo.elementName"/>
          </label>
        </FormItem>
        <FormItem label="元素编码" prop="elementCode">
          <label>
            <Input placeholder="请输入关键字" type="text" v-model="pageInfo.elementCode"/>
          </label>
        </FormItem>
        <FormItem>
          <Button @click="handleSearch(1)" type="primary">查询</Button>&nbsp;
          <Button @click="handleResetSearchForm('searchForm')">重置</Button>&nbsp;&nbsp;
          <Button @click="handleModal()" type="dashed">添加</Button>
        </FormItem>
      </Form>

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
        <FormItem label="元素名称" prop="elementName">
          <label>
            <Input placeholder="请输入内容" v-model="modal.formItem.elementName"></Input>
          </label>
        </FormItem>
        <FormItem label="元素标识" prop="elementCode">
          <label>
            <Input placeholder="请输入内容" v-model="modal.formItem.elementCode"></Input>
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
import {addElement, getElementsPage, removeElement, updateElement} from '@/api/access/element';

export default {
  data() {
    return {
      modal: {
        title: '添加元素',
        visible: false,
        saving: false,
        formItem: {
          elementId: '',
          elementName: '',
          elementCode: ''
        },
        formItemRules: {
          elementName: [
            {required: true, message: '元素名称 必填', trigger: 'blur'}
          ],
          elementCode: [
            {required: true, message: '元素标识 必填', trigger: 'blur'}
          ]
        }
      },
      loading: false,
      pageInfo: {
        page: 1,
        limit: 10,
        elementName: '',
        elementCode: ''
      },
      columns: [
        {title: '元素ID', key: 'elementId'},
        {title: '元素编码（按钮权限标识）', key: 'elementCode'},
        {title: '元素名', key: 'elementName'},
        {title: '创建时间', key: 'createdDate', slot: 'createdDate'},
        {title: '操作', slot: 'action', fixed: 'right', align: 'center', width: 120}
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
    /**
     * 弹框
     */
    handleModal(data) {
      if (data) {
        this.modal.title = '编辑元素';
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
          if (this.modal.formItem.elementId) {
            updateElement(this.modal.formItem)
                .then(() => {
                  this.$Message.success('更新成功');
                  this.handleReset();
                  this.handleSearch();
                })
                .finally(() => {
                  this.modal.saving = false;
                });
          } else {
            addElement(this.modal.formItem)
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
          removeElement(data.elementId)
              .then(() => {
                this.$Message.success('删除成功')
                this.handleSearch()
              })
        }
      });
    },
    handleReset() {
      this.modal.formItem = {
        elementId: '',
        elementName: '',
        elementCode: ''
      };
      // 重置验证
      this.$refs['modalForm'].resetFields();
      this.modal.visible = false;
      this.modal.saving = false;
    },
    handleResetSearchForm(form) {
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
