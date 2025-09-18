const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    port: 80,
    open: true,
    client: {
      overlay: false
    }
  }
  
})
