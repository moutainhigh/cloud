<template>
  <Card shadow>
    <div>
      <div class="account-setting-con left-con">
        <Menu @on-select="handleSelect" active-name="profile">
          <MenuItem name="profile">
            <span>基本信息</span>
          </MenuItem>
          <MenuItem name="security">
            <span>安全设置</span>
          </MenuItem>
        </Menu>
      </div>
      <div class="account-setting-con view-con">
        <Form :model="profile" :rules="formItemRules" ref="userForm" v-if="currentSelect==='profile'">
          <h3>
            <span>基本信息</span>
          </h3>
          <Row>
            <Col span="8">
              <FormItem label="昵称" prop="nickName">
                <Input placeholder="请输入内容" v-model="profile.nickName"></Input>
              </FormItem>
              <FormItem label="描述">
                <Input placeholder="请输入内容" type="textarea" v-model="profile.userDesc"></Input>
              </FormItem>
            </Col>
            <Col span="8">
              <FormItem label="头像" prop="avatar">
                <Avatar style="height: 128px;width: 128px;border-radius: 50%;"/>
              </FormItem>
            </Col>
          </Row>
          <FormItem>
            <Button @click="handleSubmit('userForm')" type="primary">保存</Button>
          </FormItem>
        </Form>

        <div v-if="currentSelect==='security'">
          <h3>
            <span>安全设置</span>
          </h3>
          <Divider/>
          <p>登陆名</p>
          <p style="color: #9ea7b4">当前登录名:{{profile.userName}}</p>
          <Divider/>
          <p>绑定手机</p>
          <p style="color: #9ea7b4">
            <span v-if="profile.mobile">已绑定手机：{{profile.mobile}}&nbsp;&nbsp;<a>修改</a></span>
            <span v-else="">
               尚未绑定手机 <a>绑定</a>
            </span>
          </p>
          <Divider/>
          <p>绑定邮箱</p>
          <p style="color: #9ea7b4">
            <span v-if="profile.email">已绑定邮箱：{{profile.email}}&nbsp;&nbsp;<a>修改</a></span>
            <span v-else="">
               尚未绑定手机 <a>绑定</a>
            </span>
          </p>
          <Divider style="color: #9ea7b4"/>
        </div>
      </div>
    </div>
  </Card>
</template>

<script>
    import {updateCurrentUserInfo} from '@/api/user'

    export default {
        name: 'message_page',
        data() {
            return {
                currentSelect: 'profile',
                formItemRules: {
                    nickName: [
                        {required: true, message: '昵称不能为空', trigger: 'blur'}
                    ]
                },
                profile: this.$store.state.user,
            }
        },
        methods: {
            handleSubmit(form) {
                if (form === 'userForm') {
                    this.$refs['userForm'].validate((valid) => {
                        if (valid) {
                            updateCurrentUserInfo(this.profile).then(res => {
                                if (res.rtnCode === '200') {
                                    this.$Message.success('修改成功')
                                    this.$store.commit("setAvatar", this.profile.avatar)
                                    this.$store.commit("setNickName", this.profile.nickName)
                                }
                            }).finally(() => {
                                this.saving = false
                            })
                        }
                    })
                }
            },
            handleSelect(name) {
                this.currentSelect = name
            }
        },
        mounted() {
        }
    }
</script>

<style lang="less">
  .account-setting {
    &-con {
      height: ~"calc(100vh - 176px)";
      display: inline-block;
      vertical-align: top;
      position: relative;

      &.left-con {
        border-right: 1px solid #e6e6e6;
      }

      &.view-con {
        position: absolute;
        left: 264px;
        top: 16px;
        right: 16px;
        bottom: 16px;
        overflow: auto;
        padding: 12px 20px 0;
      }
    }
  }
</style>
