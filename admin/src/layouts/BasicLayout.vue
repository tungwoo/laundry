<template>
  <a-layout class="layout">
    <a-layout-header class="header">
      <div class="logo">
        <img src="@/assets/logo.png" alt="logo" />
        <h1>洗衣店管理系统</h1>
      </div>
      <a-menu
        theme="dark"
        mode="horizontal"
        :default-selected-keys="['1']"
        :style="{ lineHeight: '64px' }"
      >
        <a-menu-item key="1">首页</a-menu-item>
      </a-menu>
    </a-layout-header>
    <a-layout>
      <a-layout-sider width="200" style="background: #fff">
        <a-menu
          mode="inline"
          :default-selected-keys="['1']"
          :default-open-keys="['sub1']"
          :style="{ height: '100%', borderRight: 0 }"
        >
          <a-menu-item key="1">
            <a-icon type="dashboard" />
            <span>首页</span>
          </a-menu-item>
        </a-menu>
      </a-layout-sider>
      <a-layout style="padding: 0 24px 24px">
        <Breadcrumb />
        <a-layout-content
          :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
        >
          <router-view />
        </a-layout-content>
      </a-layout>
    </a-layout>
  </a-layout>
</template>

<script>
import Breadcrumb from '@/components/Breadcrumb'

export default {
  name: 'BasicLayout',
  components: {
    Breadcrumb
  }
}
</script>

<style lang="less" scoped>
.layout {
  min-height: 100vh;
}
.header {
  display: flex;
  align-items: center;
}
.logo {
  display: flex;
  align-items: center;
  margin-right: 30px;
  
  img {
    width: 32px;
    height: 32px;
  }
  
  h1 {
    color: white;
    margin: 0 0 0 12px;
    font-size: 18px;
  }
}
</style> 