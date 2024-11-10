const { defineConfig } = require('@vue/cli-service')
const port = process.env.port || 8080; //配置端口号
module.exports = defineConfig({
  transpileDependencies: true,
  lintOnSave:false,
  pages: {
    index: {
      entry: 'src/main.js', // 入口文件
      title: '羊尖社区疫情管理系统'
    }
  },
  devServer:{
    port, //端口号就是自己设置的值
    proxy:{
    '/province':
    {
      target:'https://api.map.baidu.com',
      changeOrigin:true,
      pathRewrite:{
        '^/province':''
      }
    },
    '/local':
    {
      target:'http://localhost:8848',
      changeOrigin:true,
        pathRewrite:{
          '^/local':''
        }
    }
    }
  }

})
