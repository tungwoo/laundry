module.exports = {
  css: {
    loaderOptions: {
      less: {
        lessOptions: {
          javascriptEnabled: true,
          modifyVars: {
            'layout-trigger-background': '#002140',
            'primary-color': '#1890ff',
            'link-color': '#1890ff',
            'font-size-base': '14px',
            'line-height-base': '1.5'
          }
        }
      }
    }
  }
} 