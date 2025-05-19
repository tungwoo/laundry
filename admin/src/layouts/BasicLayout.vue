<template>
  <a-layout class="layout">
    <a-layout-header class="header">
      <div class="logo">
        <img src="@/assets/logo.png" alt="logo" />
        <h1>洗衣店管理系统</h1>
      </div>
      <div class="header-right">
        <a-dropdown v-if="userInfo.name">
          <a class="user-dropdown-link" @click="e => e.preventDefault()">
            <a-avatar icon="user" />
            <span class="username">{{ userInfo.name }}</span>
            <a-icon type="down" />
          </a>
          <a-menu slot="overlay">
            <a-menu-item key="0">
              <a-icon type="user" />
              <span>个人中心</span>
            </a-menu-item>
            <a-menu-item key="1">
              <a-icon type="setting" />
              <span>账户设置</span>
            </a-menu-item>
            <a-menu-divider />
            <a-menu-item key="3" @click="handleLogout">
              <a-icon type="logout" />
              <span>退出登录</span>
            </a-menu-item>
          </a-menu>
        </a-dropdown>
        <a-button v-else type="link" @click="$router.push('/login')">
          <a-icon type="login" />
          <span>登录</span>
        </a-button>
      </div>
    </a-layout-header>
    <a-layout>
      <a-layout-sider width="200" style="background: #fff">
        <a-menu
          mode="inline"
          :default-selected-keys="[activeMenu]"
          :default-open-keys="openKeys"
          :style="{ height: '100%', borderRight: 0 }"
        >
          <!-- 渲染一级菜单 -->
          <a-menu-item 
            v-for="route in firstLevelRoutes" 
            :key="route.path" 
            @click="() => $router.push(route.path)"
          >
            <a-icon v-if="route.meta && route.meta.icon" :type="route.meta.icon" />
            <span>{{ route.meta ? route.meta.title : route.name }}</span>
          </a-menu-item>
          
          <!-- 渲染多级菜单 -->
          <a-sub-menu 
            v-for="route in multiLevelRoutes" 
            :key="route.path"
          >
            <span slot="title">
              <a-icon v-if="route.meta && route.meta.icon" :type="route.meta.icon" />
              <span>{{ route.meta ? route.meta.title : route.name }}</span>
            </span>
            <a-menu-item 
              v-for="child in route.children" 
              :key="route.path + '/' + child.path"
              @click="() => $router.push(route.path + '/' + child.path)"
            >
              <a-icon v-if="child.meta && child.meta.icon" :type="child.meta.icon" />
              <span>{{ child.meta ? child.meta.title : child.name }}</span>
            </a-menu-item>
          </a-sub-menu>
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
import { mapGetters } from 'vuex'

export default {
  name: 'BasicLayout',
  components: {
    Breadcrumb
  },
  data() {
    return {
      firstLevelRoutes: [],
      multiLevelRoutes: []
    }
  },
  computed: {
    ...mapGetters(['userInfo']),
    activeMenu() {
      return this.$route.path
    },
    openKeys() {
      const matched = this.$route.matched
      return matched.map(item => item.path)
    }
  },
  methods: {
    handleLogout() {
      this.$confirm({
        title: '确定要退出登录吗？',
        content: '退出后需要重新登录',
        onOk: () => {
          this.$store.dispatch('user/logout').then(() => {
            this.$router.push('/login')
            this.$message.success('已成功退出登录')
          })
        }
      })
    }
  },
  created() {
    const mainRoutes = this.$router.options.routes.find(route => route.path === '/')
    if (mainRoutes && mainRoutes.children) {
      const visibleRoutes = mainRoutes.children.filter(route => !route.meta || !route.meta.hidden)
      
      // 分离一级菜单和多级菜单
      this.firstLevelRoutes = visibleRoutes.filter(route => !route.children || route.children.length === 0)
      this.multiLevelRoutes = visibleRoutes.filter(route => route.children && route.children.length > 0)
    }
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
  justify-content: space-between;
}
.logo {
  display: flex;
  align-items: center;
  
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
.header-right {
  color: white;
}
.user-dropdown-link {
  color: white;
  display: flex;
  align-items: center;
  
  .username {
    margin: 0 8px;
  }
}
</style> 