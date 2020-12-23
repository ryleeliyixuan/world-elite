<template>
  <div :class="['tinymce-container',{fullscreen:fullscreen}]" :style="{width:containerWidth}">
    <editor id="tinymce" v-model="content" :init="initTinymce"></editor>
    <div class="editor-custom-btn-container">
      <editorImage color="#1890ff" class="editor-upload-btn" @successCBK="imageSuccessCBK"/>
    </div>
  </div>
</template>
<script>

  import editorImage from './components/EditorImage'
  import tinymce from 'tinymce/tinymce'
  import Editor from '@tinymce/tinymce-vue'
  import 'tinymce/themes/silver'
  import 'tinymce/icons/default'
  import plugins from './plugins'
  import toolbar from './toolbar'

  import 'tinymce/plugins/anchor'// 插入瞄点
  import 'tinymce/plugins/advlist'// 高级列表插件
  import 'tinymce/plugins/autolink'// 自动链接
  import 'tinymce/plugins/autoresize'// 自动适应大小
 // import 'tinymce/plugins/autosave'// 自动保存
  import 'tinymce/plugins/charmap'// 插入特殊字符
  import 'tinymce/plugins/code'// 插入html源码
  import 'tinymce/plugins/emoticons'// 插入unicode字符表情
  import emojis from 'tinymce/plugins/emoticons/js/emojis' // emoji字符文件
  import 'tinymce/plugins/fullscreen'// 全屏
  import 'tinymce/plugins/help'// 帮助
  import 'tinymce/plugins/hr'// 水平分割线
  // import 'tinymce/plugins/image'// 插入上传图片插件
  import 'tinymce/plugins/insertdatetime'// 插入日期时间
  import 'tinymce/plugins/link'// 插入超链接
  import 'tinymce/plugins/lists'// 列表插件
  // import 'tinymce/plugins/media'// 插入视频插件
  import 'tinymce/plugins/pagebreak'// 插入分页符
  import 'tinymce/plugins/preview'// 预览
  import 'tinymce/plugins/print'// 打印
  import 'tinymce/plugins/quickbars'// 快捷操作
  // import 'tinymce/plugins/save'// 保存
  import 'tinymce/plugins/searchreplace'// 查找和替换
  import 'tinymce/plugins/table'// 插入表格插件
  import 'tinymce/plugins/toc'// 目录生成
  import 'tinymce/plugins/wordcount'// 字数统计插件
  // import bdmap from  './tinymce/bdmap/plugin.js'// 百度地图

  export default {
    name: 'Tinymce',
    components: { editorImage, Editor },
    props: {
      value: {
        type: String,
        default: ''
      },
      height: {
        type: [Number, String],
        required: false,
        default: 360
      },
      width: {
        type: [Number, String],
        required: false,
        default: 'auto'
      }
    },
    data() {
      return {
        hasChange: false,
        fullscreen: false,
        content: this.value,
        initTinymce: {
          // 基本设置
          language_url: '/tinymce/lang/zh_CN.js',// 语言包的路径
          language: 'zh_CN',
          skin_url: '/tinymce/skins/ui/oxide',// skin路径 oxide-dark：深色  oxide：浅色
          menubar: 'file edit insert view format table tools help', // 菜单栏
          toolbar: toolbar, // 工具栏，false：隐藏工具栏
          plugins: plugins, // 指定需加载的插件

          // UI界面配置
          statusbar: false, // 状态栏指的是编辑器最底下、左侧显示dom信息、右侧显示Tiny版权链接和调整大小的那一条。
          branding: false, // 显示右下角技术支持
          elementpath: false, // 显示底栏的元素路径
          width: this.width, // 宽度，默认100%
          height: this.height, // 高度
          min_width: '400px', // 最小宽度400px
          // min_height: '400px', // 最小高度400px
          // max_height: '800px', // 最大高度800px
          resize: true, // true 调整高度  false 完全不动, 'both' 宽高都能改变
          draggable_modal: true, // 模态窗口允许拖动
          // fixed_toolbar_container: '#mytoolbar', // 指定工具栏在某一容器顶部固定。
          toolbar_mode: 'sliding',
          toolbar_sticky: true, // 粘性工具栏（或停靠工具栏），在向下滚动网页直到不再可见编辑器时，将工具栏和菜单停靠在屏幕顶部。

          // 集成配置
          auto_focus: true, // 自动获得焦点
          cache_suffix: '?v=5.4.1', // 缓存请求后缀
          setup: this.setup, // 初始化前执行
          init_instance_callback: this.init_instance_callback, // 初始化结束后执行

          // 内容外观配置
          body_class: 'panel-body', // 为编辑区body指定类或id
          // content_css : ['mycontent.css', 'mycontent2.css'], // 允许自定义TinyMCE编辑区域内的样式
          placeholder: '在这里输入文字',
          color_cols: 8, // 颜色选择列表的列数
          // color_map:[], // 自定义颜色选择列表的颜色
          custom_colors: true, // 调色盘开关

          // 格式化配置
          fontsize_formats: '8pt 10pt 12pt 14pt 16pt 18pt 24pt 36pt 48pt 56pt 72pt',
          font_formats: '微软雅黑=Microsoft YaHei,Helvetica Neue,PingFang SC,sans-serif;苹果苹方=PingFang SC,Microsoft YaHei,sans-serif;宋体=simsun,serif;仿宋体=FangSong,serif;黑体=SimHei,sans-serif;Arial=arial,helvetica,sans-serif;Arial Black=arial black,avant garde;Book Antiqua=book antiqua,palatino;',
          // block_formats: 'Paragraph=p;Heading 1=h1;Heading 2=h2;Heading 3=h3;Heading 4=h4;Heading 5=h5;Heading 6=h6;',

          // 其他配置
          custom_undo_redo_levels: 10, // 撤销次数
          end_container_on_empty_block: false, // 空元素回车将其拆分
          nowrap: false, // 文本水平不换行
          object_resizing: false, // 调整大小控件开关
          typeahead_urls: true, // 键入网址判断

          // image - 图片上传
          // image_caption: true, // 在弹出框中增加一个“标题”选项，
          // image_list: [ // 预定义图片列表。也可以使用一个返回json数据的URL。如 image_list: "/mylist.php"。它还支持自定义异步函数
          //   {title: '狗', value: 'mydog.jpg'},
          //   {title: '猫', value: 'mycat.gif'}
          // ],
          image_advtab: true, // 可以自定义图片的css样式（内置style）、边距（margin）和边框（border）。
          images_upload_url: '/demo/upimg.php', // 指定一个接受上传文件的后端处理程序地址
          images_upload_base_path: '/demo', // 如果返回的地址是相对路径，可以给相对路径指定它所相对的基本路径
          images_upload_credentials: false, // 对images_upload_url中指定的地址调用时是否传递cookie等跨域的凭据。值为布尔值，默认false。
          images_upload_handler: this.imageHandler, // 此选项允许你使用自定义函数代替TinyMCE来处理上传操作。该自定义函数需提供三个参数：blobInfo、成功回调和失败回调。

          // link - 超链接
          link_default_protocol: 'http', // 默认协议
          default_link_target: '_blank', // 默认链接打开方式
          file_picker_callback: this.fileHandler, // 文件上传处理函数
          // file_picker_types: 'file image media', // 指定文件上传类型

          // advlist - 高级列表插件
          advlist_bullet_styles: 'default,circle,disc,square',
          advlist_number_styles: 'default,lower-alpha,lower-greek,lower-roman,upper-alpha,upper-roman',

          // autoresize - 自适应插件
          autoresize_bottom_margin: 50, // 指定编辑器body初始化时底边距，也就是加一个margin-bottom。

          // autosave - 自动保存
          autosave_ask_before_unload: true, // 当关闭或跳转URL时，弹出提示框提醒用户仍未保存变更内容。默认开启提示。
          autosave_interval: '30s', // 自动存稿的世界间隔。注意该值为字符串，以秒为单位
          // autosave_prefix: "tinymce-autosave-{path}{query}-{id}-", // 自动存稿在本地存储（local storage）中的字段（key）前缀。
          autosave_restore_when_empty: true, // 当编辑器初始化时内容区为空时，Tinymce是否应自动还原存储在本地存储中的草稿。
          autosave_retention: '20m', // 设置自动草稿的有效期。当草稿超过有效期则忽略。值是字符串，单位是分。

          // emoticons - emoji加载
          emoticons_database_url: emojis

          // imagetools_cors_hosts: ['www.tinymce.com', 'codepen.io'],
        }
      }
    },
    computed: {
      containerWidth() {
        const width = this.width
        if (/^[\d]+(\.[\d]+)?$/.test(width)) { // matches `100`, `'100'`
          return `${width}pt`
        }
        return width
      }
    },
    watch: {
      value(val) {
        this.content = val
      },
      content(val) {
        this.$emit('input', val)
      }
    },
    mounted() {
      tinymce.init({ theme: 'silver' })
    },
    activated() {
      // if (window.tinymce) {
      //   this.initTinymce()
      // }
    },
    deactivated() {
      // this.destroyTinymce()
    },
    destroyed() {
      // this.destroyTinymce()
    },
    methods: {
      // tinymce.editors['编辑器id'].setMode('design');//开启编辑模式
      // tinymce.editors['编辑器id'].setMode('readonly');//开启只读模式
      // tinymce.editors['编辑器id'].notificationManager.open({
      //   text: '这是一条提示信息。',
      //   type: 'info' // success 成功  info 普通信息  warning 警告信息  error 错误信息
      //   timeout: 5000,
      //   closeButton: false,
      // });

      imageHandler(blobInfo, successCallback, failCallback) {
        console.log(blobInfo)
        let file = blobInfo.blob()//转化为易于理解的file对象
      },
      fileHandler(callback, value, meta) {
        // Provide file and text for the link dialog
        if (meta.filetype === 'file') {
          callback('mypage.html', { text: 'My text' })
        }
        // Provide image and alt text for the image dialog
        // if (meta.filetype === 'image') {
        //   callback('myimage.jpg', { alt: 'My alt text' })
        // }
        // Provide alternative source and posted for the media dialog
        // if (meta.filetype === 'media') {
        //   callback('movie.mp4', { source2: 'alt.ogg', poster: 'image.jpg' })
        // }
      },

      setup(editor) { // 初始化前执行
        console.log('ID为: ' + editor.id + ' 的编辑器即将初始化.')
        editor.on('FullscreenStateChanged', (e) => {
          this.fullscreen = e.state
        })
      },
      init_instance_callback: editor => { // 初始化结束后执行
        console.log('ID为: ' + editor.id + ' 的编辑器已初始化完成.')
        // if (_this.value) {
        //   editor.setContent(_this.value)
        // }
        // _this.hasInit = true
        // editor.on('NodeChange Change KeyUp SetContent', () => {
        //   this.hasChange = true
        //   this.$emit('input', editor.getContent())
        // })
      },

      // destroyTinymce() {
      //   const tinymce = window.tinymce.get(this.tinymceId)
      //   if (this.fullscreen) {
      //     tinymce.execCommand('mceFullScreen')
      //   }
      //
      //   if (tinymce) {
      //     tinymce.destroy()
      //   }
      // },
      // setContent(value) {
      //   window.tinymce.get(this.tinymceId).setContent(value)
      // },
      getContent() {
        tinymce.editors['tinymce'].getContent()
      },
      imageSuccessCBK(file) {
        tinymce.editors['tinymce'].insertContent(`<img style="max-width: 500px;max-height: 500px" src="${file.url}" >`)
      }
    }
  }
</script>

<style scoped>
  .tinymce-container {
    position: relative;
    line-height: normal;
  }

  .editor-custom-btn-container {
    position: absolute;
    right: 4pt;
    top: 4pt;
    z-index: 1300;
  }
  .fullscreen .editor-custom-btn-container {
    z-index: 10000;
    position: fixed;
    right: 4px;
    top: 4px;
  }

  .editor-upload-btn {
    display: inline-block;
  }


</style>
