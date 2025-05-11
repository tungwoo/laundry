<template>
  <a-breadcrumb class="breadcrumb">
    <a-breadcrumb-item v-for="(item, index) in breadcrumbList" :key="index">
      <router-link v-if="item.path" :to="item.path">{{ item.title }}</router-link>
      <span v-else>{{ item.title }}</span>
    </a-breadcrumb-item>
  </a-breadcrumb>
</template>

<script>
export default {
  name: 'Breadcrumb',
  data() {
    return {
      breadcrumbList: []
    }
  },
  watch: {
    $route: {
      handler(route) {
        this.getBreadcrumb(route)
      },
      immediate: true
    }
  },
  methods: {
    getBreadcrumb(route) {
      // 根据路由匹配生成面包屑
      let matched = route.matched.filter(item => item.meta && item.meta.title)
      const first = matched[0]
      
      if (first && first.path !== '/dashboard') {
        matched = [{ path: '/dashboard', meta: { title: '首页' } }].concat(matched)
      }

      this.breadcrumbList = matched.map(item => {
        return {
          path: item.path,
          title: item.meta.title
        }
      })
    }
  }
}
</script>

<style scoped>
.breadcrumb {
  margin: 16px 0;
}
</style> 