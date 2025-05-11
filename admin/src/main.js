import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'

// 引入Ant Design Vue
import Antd from 'ant-design-vue'
import 'ant-design-vue/dist/antd.css'

// 引入全局样式
import './assets/styles/global.less'

// 引入自定义组件
import './components'

Vue.use(Antd)

Vue.config.productionTip = false

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app') 