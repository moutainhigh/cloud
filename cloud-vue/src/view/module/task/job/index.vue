<template>
  <div>
    <Card shadow>
      <div class="search-con search-con-top">
        <ButtonGroup>
          <Button :disabled="hasAuthority('jobEdit')?false:true" @click="handleModal()" class="search-btn"
                  type="primary">
            <span>添加</span>
          </Button>
        </ButtonGroup>
      </div>
      <Table :columns="columns" :data="data" :loading="loading" border>
        <template slot="status" slot-scope="{ row }">
          <Badge status="success" text="正常" v-if="row.jobStatus==='NORMAL'"/>
          <Badge status="error" text="暂停" v-else=""/>
        </template>
        <template slot="type" slot-scope="{ row }">
          <p v-if="row.cronExpression">cron表达式:{{row.cronExpression}}</p>
          <p v-else="">调度时间:{{row.startDate}} ~ {{row.endDate}}</p>
        </template>
        <template slot="action" slot-scope="{ row }">
          <a :disabled="hasAuthority('jobEdit')?false:true" @click="handleModal(row)">编辑</a>&nbsp;
          <Dropdown @on-click="handleClick($event,row)" ref="dropdown" transfer v-show="hasAuthority('jobEdit')">
            <a href="javascript:void(0)">
              <span>更多</span>
              <Icon type="ios-arrow-down"></Icon>
            </a>
            <DropdownMenu slot="list">
              <DropdownItem name="pause" v-if="row.jobStatus ==='NORMAL'?true:false">暂停任务</DropdownItem>
              <DropdownItem name="resume" v-if="row.jobStatus ==='PAUSED'?true:false">恢复任务</DropdownItem>
              <DropdownItem name="remove">删除任务</DropdownItem>
            </DropdownMenu>
          </Dropdown>
        </template>
      </Table>
      <Page :current="pageInfo.page" :page-size="pageInfo.limit" :total="pageInfo.total" @on-change="handlePage" @on-page-size-change='handlePageSize'
            show-elevator
            show-sizer
            show-total transfer></Page>
    </Card>
    <Modal :title="modalTitle"
           @on-cancel="handleReset"
           v-model="modalVisible"
           width="40">
      <div>
        <Form :label-width="100" :model="formItem" :rules="formItemRules" ref="form1">
          <FormItem label="任务名称" prop="jobName">
            <Input :disabled="formItem.newData?false:true" placeholder="请输入内容" v-model="formItem.jobName"></Input>
          </FormItem>
          <FormItem label="任务类型" prop="jobType">
            <Select v-model="formItem.jobType">
              <Option value="cron">cron任务(CronTrigger)</Option>
              <Option value="simple">简单任务(SimpleTrigger)</Option>
            </Select>
          </FormItem>
          <FormItem label="开始时间" prop="startTime" v-if="formItem.jobType === 'simple'">
            <DatePicker placeholder="开始时间" style="width: 100%" type="datetime"
                        v-model="formItem.startTime"></DatePicker>
          </FormItem>
          <FormItem label="结束时间" prop="endTime" v-if="formItem.jobType === 'simple'">
            <DatePicker placeholder="结束时间" style="width: 100%" type="datetime" v-model="formItem.endTime"></DatePicker>
          </FormItem>
          <FormItem label="重复执行" prop="repeatCount" v-if="formItem.jobType === 'simple'">
            <InputNumber :min="-1" v-model="formItem.repeatCount"></InputNumber> &nbsp;&nbsp;次
            &nbsp;&nbsp;

            <RadioGroup @on-change="repeatCountTypeChange" type="button" v-model="formItem.repeatCountType">
              <Radio label="0">不重复执行</Radio>
              <Radio label="-1">不限制次数,一直重复执行(直到过期)</Radio>
            </RadioGroup>
          </FormItem>
          <FormItem label="重复执行间隔" prop="repeatInterval" v-if="formItem.jobType === 'simple'">
            <InputNumber :min="1000" v-model="formItem.repeatInterval"></InputNumber>
            <span>&nbsp;&nbsp;毫秒</span>
          </FormItem>
          <FormItem label="cron表达式" prop="cron" v-if="formItem.jobType === 'cron'">
            <Input placeholder="* * * * * ?" v-model="formItem.cron"></Input>
          </FormItem>
          <FormItem label="远程调度接口" prop="path">
            <Select @on-change="handleOnSelectChange" filterable v-model="formItem.path">
              <Option :value="item.path" v-for="item in selectApis">{{ item.path
                }} - {{ item.apiName}} - {{ item.serviceId}}

              </Option>
            </Select>
          </FormItem>
          <FormItem label="任务描述">
            <Input placeholder="请输入内容" type="textarea" v-model="formItem.jobDescription"></Input>
          </FormItem>
          <FormItem label="异常告警邮箱" prop="alarmMail">
            <Input placeholder="请输入内容" v-model="formItem.alarmMail"></Input>
          </FormItem>
        </Form>
        <div class="drawer-footer">
          <Button @click="handleReset" type="default">取消</Button>&nbsp;
          <Button :loading="saving" @click="handleSubmit" type="primary">保存</Button>
        </div>
      </div>
    </Modal>
  </div>
</template>

<script>
    import {addHttpJob, getJobs, pauseJob, removeJob, resumeJob, updateHttpJob} from '@/api/job'
    import {getAllApi} from '@/api/api'

    export default {
        name: 'TaskJob',
        data() {
            return {
                loading: false,
                modalVisible: false,
                modalTitle: '',
                saving: false,
                pageInfo: {
                    total: 0,
                    page: 1,
                    limit: 10
                },
                selectApis: [],
                formItemRules: {
                    jobName: [
                        {required: true, message: '任务名称不能为空', trigger: 'blur'}
                    ],
                    jobType: [
                        {required: true, message: '任务类型不能为空', trigger: 'blur'}
                    ],
                    cron: [
                        {required: true, message: 'cron表达式不能为空', trigger: 'blur'}
                    ],
                    path: [
                        {required: true, message: '调度接口不能为空', trigger: 'blur'}
                    ],
                    alarmMail: [
                        {required: false, type: 'email', message: '邮箱格式不正确', trigger: 'blur'}
                    ],
                    startTime: [
                        {required: true, message: '开始时间不能为空'}
                    ],
                    repeatInterval: [
                        {required: true, message: '间隔时间不能为空'}
                    ],
                    repeatCount: [
                        {required: true, message: '重试次数不能为空'}
                    ],
                },
                formItem: {
                    newData: true,
                    jobName: '',
                    jobDescription: '',
                    jobType: 'cron',
                    cron: '',
                    startTime: '',
                    endTime: '',
                    repeatInterval: 10000,
                    repeatCountType: '0',
                    repeatCount: 0,
                    serviceId: '',
                    path: '',
                    method: '',
                    contentType: '',
                    alarmMail: ''
                },
                columns: [
                    {
                        type: 'selection',
                        width: 60,
                        align: 'center'
                    },
                    {
                        title: '任务名称',
                        key: 'jobName',
                        width: 200,
                    },
                    {
                        title: '调度信息',
                        width: 350,
                        slot: 'type'
                    },
                    {
                        title: '状态',
                        key: 'jobStatus',
                        slot: 'status'
                    },
                    {
                        title: '任务描述',
                        key: 'jobDescription'
                    },
                    {
                        title: '操作',
                        key: '',
                        slot: 'action',
                        fixed: 'right',
                        width: 150
                    }
                ],
                data: []
            }
        },
        methods: {
            handleModal(data) {
                if (data) {
                    this.modalTitle = '编辑任务 - ' + data.jobName
                    this.formItem = Object.assign({}, this.formItem, data)
                    this.formItem.jobType = this.formItem.jobTrigger === 'org.quartz.impl.triggers.SimpleTriggerImpl' ? 'simple' : 'cron'
                    this.formItem.cron = data.cronExpression
                    this.formItem.startTime = data.startDate
                    this.formItem.endTime = data.endDate
                    this.formItem.repeatInterval = data.repeatInterval ? parseInt(data.repeatInterval) : 0
                    this.formItem.repeatCountType = data.repeatCount + ''
                    this.formItem.path = data.data.path
                    this.formItem.serviceId = data.data.serviceId
                    this.formItem.method = data.data.method
                    this.formItem.contentType = data.data.contentType
                    this.formItem.alarmMail = data.data.alarmMail
                    this.formItem.newData = false
                } else {
                    this.modalTitle = '添加任务'
                }
                this.modalVisible = true
            },
            handleTabClick(name) {
                this.current = name
                this.handleModal();
            },
            handleReset() {
                const newData = {
                    newData: true,
                    jobName: '',
                    jobDescription: '',
                    jobType: 'cron',
                    cron: '',
                    startTime: '',
                    endTime: '',
                    repeatInterval: 10000,
                    repeatCountType: '0',
                    repeatCount: 0,
                    serviceId: '',
                    path: '',
                    method: '',
                    contentType: '',
                    alarmMail: ''
                }
                this.formItem = newData
                //重置验证
                this.$refs['form1'].resetFields()
                this.modalVisible = false
                this.saving = false
            },
            handleSubmit() {
                this.$refs['form1'].validate((valid) => {
                    if (valid) {
                        if (this.formItem.jobType === 'simple') {
                            this.formItem.startTime = this.formItem.startTime ? this.formItem.startTime.pattern('yyyy-MM-dd HH:mm:ss') : ''
                            this.formItem.endTime = this.formItem.endTime ? this.formItem.endTime.pattern('yyyy-MM-dd HH:mm:ss') : ''
                        }
                        this.saving = true
                        if (!this.formItem.newData) {
                            updateHttpJob(this.formItem).then(res => {
                                if (res.code === 0) {
                                    this.$Message.success('保存成功')
                                }
                                this.handleReset()
                                this.handleSearch()
                            }).finally(() => {
                                this.saving = false
                            })
                        } else {
                            addHttpJob(this.formItem).then(res => {
                                if (res.code === 0) {
                                    this.$Message.success('保存成功')
                                }
                                this.handleReset()
                                this.handleSearch()
                            }).finally(() => {
                                this.saving = false
                            })
                        }
                    }
                })
            },
            handleRemove(data) {
                this.$Modal.confirm({
                    title: '确定删除吗？',
                    onOk: () => {
                        removeJob(data.jobName).then(res => {
                            this.handleSearch()
                            if (res.code === 0) {
                                this.pageInfo.page = 1
                                this.$Message.success('删除成功')
                            }
                        })
                    }
                })
            },
            handlePause(data) {
                this.$Modal.confirm({
                    title: '确定暂停任务吗？',
                    onOk: () => {
                        pauseJob(data.jobName).then(res => {
                            this.handleSearch()
                            if (res.code === 0) {
                                this.pageInfo.page = 1
                                this.$Message.success('暂停成功')
                            }
                        })
                    }
                })
            },
            handleResume(data) {
                this.$Modal.confirm({
                    title: '确定恢复任务吗？',
                    onOk: () => {
                        resumeJob(data.jobName).then(res => {
                            this.handleSearch()
                            if (res.code === 0) {
                                this.pageInfo.page = 1
                                this.$Message.success('恢复成功')
                            }
                        })
                    }
                })
            },
            handleSearch(page) {
                if (page) {
                    this.pageInfo.page = page
                }
                this.loading = true
                getJobs(this.pageInfo).then(res => {
                    this.data = res.data.records
                    this.pageInfo.total = parseInt(res.data.total)
                }).finally(() => {
                    this.loading = false
                })
            },
            handleLoadApiList() {
                this.loading = true
                getAllApi().then(res => {
                    this.selectApis = res.data
                })
            },
            handlePage(current) {
                this.pageInfo.page = current
                this.handleSearch()
            },
            handlePageSize(size) {
                this.pageInfo.limit = size
                this.handleSearch()
            },
            handleOnSelectChange(value) {
                let api = {}
                this.selectApis.some(item => {
                    if (item.path === value) {
                        api = item
                        return true
                    }
                })
                this.formItem.serviceId = api.serviceId
                this.formItem.path = api.path
                this.formItem.contentType = api.contentType
                this.formItem.method = api.requestMethod
            },
            handleClick(name, row) {
                switch (name) {
                    case 'pause':
                        this.handlePause(row)
                        break
                    case 'resume':
                        this.handleResume(row)
                        break
                    case 'remove':
                        this.handleRemove(row)
                        break
                }
            },
            repeatCountTypeChange(value) {
                this.formItem.repeatCount = parseInt(value)
            }
        },
        mounted: function () {
            this.handleSearch()
            this.handleLoadApiList()
        }
    }
</script>
