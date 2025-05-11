<template>
  <div class="not-found">
    <div class="not-found-content">
      <h1>404</h1>
      <h2>抱歉，您访问的页面不存在</h2>
      <router-link to="/dashboard">
        <a-button type="primary">返回首页</a-button>
      </router-link>
    </div>
  </div>
</template>

<script>
export default {
  name: 'NotFound'
}
</script>

<style lang="less" scoped>
.not-found {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  
  &-content {
    text-align: center;
    
    h1 {
      font-size: 120px;
      color: #1890ff;
      margin-bottom: 16px;
    }
    
    h2 {
      margin-bottom: 24px;
    }
  }
}
</style> 