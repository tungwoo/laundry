import Vue from 'vue'
import moment from 'moment'

// 日期格式化
Vue.filter('dateFormat', function(value, format = 'YYYY-MM-DD HH:mm:ss') {
  if (!value) return ''
  return moment(value).format(format)
})

// 金额格式化
Vue.filter('moneyFormat', function(value) {
  if (!value && value !== 0) return '0.00'
  return parseFloat(value).toFixed(2)
}) 