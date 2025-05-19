<template>
  <div class="login-container">
    <div class="login-form">
      <div class="login-logo">
        <img src="@/assets/logo.png" alt="Logo">
        <h1>洗衣店管理系统</h1>
      </div>
      <a-form :form="form" @submit="handleSubmit">
        <a-form-item>
          <a-input
            v-decorator="[
              'username',
              { rules: [{ required: true, message: '请输入用户名!' }] }
            ]"
            placeholder="用户名"
            size="large"
          >
            <a-icon slot="prefix" type="user" />
          </a-input>
        </a-form-item>
        <a-form-item>
          <a-input-password
            v-decorator="[
              'password',
              { rules: [{ required: true, message: '请输入密码!' }] }
            ]"
            placeholder="密码"
            size="large"
          >
            <a-icon slot="prefix" type="lock" />
          </a-input-password>
        </a-form-item>
        <a-form-item>
          <a-button type="primary" html-type="submit" size="large" block :loading="loading">
            登录
          </a-button>
        </a-form-item>
      </a-form>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Login',
  data() {
    return {
      loading: false,
      form: this.$form.createForm(this)
    }
  },
  methods: {
    handleSubmit(e) {
      e.preventDefault()
      this.form.validateFields((err, values) => {
        if (!err) {
          this.loading = true
          this.$store.dispatch('user/login', values)
            .then(() => {
              this.$router.push({ path: '/' })
              this.$message.success('登录成功')
            })
            .catch(error => {
              this.$message.error('登录失败: ' + (error.message || '未知错误'))
              console.error(error)
            })
            .finally(() => {
              this.loading = false
            })
        }
      })
    }
  }
}
</script>

<style lang="less" scoped>
.login-container {
  height: 100vh;
  background: url("@/assets/login-bg.svg") no-repeat center center;
  background-size: cover;
  display: flex;
  justify-content: center;
  align-items: center;
}

.login-form {
  width: 360px;
  padding: 32px;
  background: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.login-logo {
  text-align: center;
  margin-bottom: 32px;
  
  img {
    width: 64px;
    height: 64px;
  }
  
  h1 {
    margin-top: 16px;
    font-size: 24px;
    color: rgba(0, 0, 0, 0.85);
  }
}
</style> 