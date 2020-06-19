function resolve(dir) {
    return path.join(__dirname, dir)
  }
   
  const Timestamp = new Date().getTime();  //当前时间为了防止打包缓存不刷新，所以给每个js文件都加一个时间戳
  module.exports = {
    // 项目部署的基础路径
    // 我们默认假设你的应用将会部署在域名的根部，
    
    baseUrl: '/',
   
    // 将构建好的文件输出到哪里
    outputDir: 'dist',
   
    // 放置静态资源的地方 (js/css/img/font/...)
    // assetsDir: '',
   
    lintOnSave: false,
   
    // 使用带有浏览器内编译器的完整构建版本
   
    // babel-loader 默认会跳过 node_modules 依赖。
    // 通过这个选项可以显式转译一个依赖。
    transpileDependencies: [/* string or regex */],
   
    // 是否为生产环境构建生成 source map？
    productionSourceMap: false,
   
    // 调整内部的 webpack 配置。
    // 查阅 https://github.com/vuejs/vue-docs-zh-cn/blob/master/vue-cli/webpack.md
    chainWebpack: () => { },
   
   //configureWebpack 这部分打包文件添加时间戳，防止缓存不更新
   
    configureWebpack: {
      output: { // 输出重构  打包编译后的 文件名称  【模块名称.版本号.时间戳】
          filename: `[name].${process.env.VUE_APP_Version}.${Timestamp}.js`,
          chunkFilename: `[name].${process.env.VUE_APP_Version}.${Timestamp}.js`
        },
     },
   
    // CSS 相关选项
    css: {
        // 将组件内的 CSS 提取到一个单独的 CSS 文件 (只用在生产环境中)
        // 也可以是一个传递给 `extract-text-webpack-plugin` 的选项对象
        extract: true,
   
        // 是否开启 CSS source map？
        sourceMap: false,
   
        // 为预处理器的 loader 传递自定义选项。比如传递给
        // sass-loader 时，使用 `{ sass: { ... } }`。
        loaderOptions: {},
   
        // 为所有的 CSS 及其预处理文件开启 CSS Modules。
        // 这个选项不会影响 `*.vue` 文件。
        modules: false
    },
   
    // 在生产环境下为 Babel 和 TypeScript 使用 `thread-loader`
    // 在多核机器下会默认开启。
    parallel: require('os').cpus().length > 1,
   
    // PWA 插件的选项。
    // 查阅 https://github.com/vuejs/vue-docs-zh-cn/blob/master/vue-cli-plugin-pwa/README.md
    pwa: {},
   
    // 配置 webpack-dev-server 行为。
    // devServer: {
    //     open: process.platform === 'darwin',
    //     host: 'localhost',
    //     port: 8888,
    //     https: false,
    //     hotOnly: false,
    //     open:true,
    //     // 查阅 https://github.com/vuejs/vue-docs-zh-cn/blob/master/vue-cli/cli-service.md#配置代理
    //     proxy: 'http://localhost:3001', // string | Object
    //     before: app => { }
    // },
   
    configureWebpack: config => {
        if (process.env.NODE_ENV === 'production') {
            // 为生产环境修改配置...
            if(process.env.npm_lifecycle_event === 'analyze'){
                config.plugins.push(
                    new BundleAnalyzerPlugin()
                );
            }
            
        } else {
            // 为开发环境修改配置...
        }
        
    },
   
    // 第三方插件的选项
    pluginOptions: {
       
    }
  }