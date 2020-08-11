<template>
  <div :class="{fullscreen:fullscreen}" class="tinymce-container" :style="{width:containerWidth}">
    <textarea :id="tinymceId" class="tinymce-textarea"/>
    <div class="editor-custom-btn-container">
      <editorImage color="#1890ff" class="editor-upload-btn" @successCBK="imageSuccessCBK"/>
    </div>
  </div>
</template>

<script>
  /**
   * docs:
   * https://panjiachen.github.io/vue-element-admin-site/feature/component/rich-editor.html#tinymce
   */
  import editorImage from './components/EditorImage'
  import plugins from './plugins'
  import toolbar from './toolbar'
  import load from './dynamicLoadScript'

  // why use this cdn, detail see https://github.com/PanJiaChen/tinymce-all-in-one
  const tinymceCDN = 'https://cdn.jsdelivr.net/npm/tinymce-all-in-one@4.9.3/tinymce.min.js'

  export default {
    name: 'Tinymce',
    components: { editorImage },
    props: {
      id: {
        type: String,
        default: function() {
          return 'vue-tinymce-' + +new Date() + ((Math.random() * 1000).toFixed(0) + '')
        }
      },
      value: {
        type: String,
        default: ''
      },
      toolbar: {
        type: Array,
        required: false,
        default() {
          return []
        }
      },
      menubar: {
        type: String,
        default: 'file edit insert view format table' // tools help
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
        hasInit: false,
        tinymceId: this.id,
        fullscreen: false,
        languageTypeList: {
          'en': 'en',
          'zh': 'zh_CN',
          'es': 'es_MX',
          'ja': 'ja'
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
        if (!this.hasChange && this.hasInit) {
          this.$nextTick(() =>
            window.tinymce.get(this.tinymceId).setContent(val || ''))
        }
      }
    },
    mounted() {
      this.init()
    },
    activated() {
      if (window.tinymce) {
        this.initTinymce()
      }
    },
    deactivated() {
      this.destroyTinymce()
    },
    destroyed() {
      this.destroyTinymce()
    },
    methods: {
      init() {
        // dynamic load tinymce from cdn
        load(tinymceCDN, (err) => {
          if (err) {
            this.$message.error(err.message)
            return
          }
          this.initTinymce()
        })
      },
      initTinymce() {
        const _this = this
        window.tinymce.init({
          selector: `#${this.tinymceId}`,
          language: this.languageTypeList['zh'],

          // UI界面配置
          statusbar: false, // 状态栏指的是编辑器最底下、左侧显示dom信息、右侧显示Tiny版权链接和调整大小的那一条。
          branding: false, // 显示右下角技术支持
          elementpath: false, // 显示底栏的元素路径
          height: this.height, // 高度
          min_height: 400, // 最小高度400px
          resize: true, // true 调整高度  false 完全不动, 'both' 宽高都能改变
          menubar: this.menubar, // 菜单栏
          toolbar: this.toolbar.length > 0 ? this.toolbar : toolbar, // 工具栏
          // skin: 'oxide-dark', 5.0主题

          // 内容外观配置
          body_class: 'panel-body',
          color_cols: 4,

          // 格式化配置
          fontsize_formats: '8pt 10pt 12pt 14pt 16pt 18pt 24pt 36pt 48pt 56pt 72pt',
          font_formats: '微软雅黑=Microsoft YaHei,Helvetica Neue,PingFang SC,sans-serif;苹果苹方=PingFang SC,Microsoft YaHei,sans-serif;宋体=simsun,serif;仿宋体=FangSong,serif;黑体=SimHei,sans-serif;Arial=arial,helvetica,sans-serif;Arial Black=arial black,avant garde;Book Antiqua=book antiqua,palatino;',

          // 其他配置
          custom_undo_redo_levels: 10, // 撤销次数
          end_container_on_empty_block: true, // 空元素回车将其拆分
          nowrap: false, // 文本水平不换行
          object_resizing: false, // 调整大小控件开关
          typeahead_urls: true, // 键入网址判断

          // 插件 - advlist 高级列表插件
          advlist_bullet_styles: 'default,circle,disc,square',
          advlist_number_styles: 'default,lower-alpha,lower-greek,lower-roman,upper-alpha,upper-roman',

          // 指定编辑器body初始化时底边距，也就是加一个margin-bottom。
          autoresize_bottom_margin: 50,

          powerpaste_word_import: 'clean',
          code_dialog_height: 450,
          code_dialog_width: 1000,
          imagetools_cors_hosts: ['www.tinymce.com', 'codepen.io'],
          default_link_target: '_blank',
          link_title: false,
          nonbreaking_force_tab: true, // inserting nonbreaking space &nbsp; need Nonbreaking Space Plugin
          autosave_ask_before_unload: true,

          // 集成配置
          auto_focus: true, // 自动获得焦点
          cache_suffix: '?v=5.0.0', // 缓存请求后缀
          plugins: plugins, // 指定需加载的插件
          setup(editor) { // 初始化前执行
            editor.on('FullscreenStateChanged', (e) => {
              _this.fullscreen = e.state
            })
          },
          init_instance_callback: editor => { // 初始化结束后执行
            if (_this.value) {
              editor.setContent(_this.value)
            }
            _this.hasInit = true
            editor.on('NodeChange Change KeyUp SetContent', () => {
              this.hasChange = true
              this.$emit('input', editor.getContent())
            })
          }
        })
      },
      destroyTinymce() {
        const tinymce = window.tinymce.get(this.tinymceId)
        if (this.fullscreen) {
          tinymce.execCommand('mceFullScreen')
        }

        if (tinymce) {
          tinymce.destroy()
        }
      },
      setContent(value) {
        window.tinymce.get(this.tinymceId).setContent(value)
      },
      getContent() {
        window.tinymce.get(this.tinymceId).getContent()
      },
      imageSuccessCBK(file) {
        window.tinymce.get(this.tinymceId).insertContent(`<img class="wscnph" src="${file.url}" >`)
      }
    }
  }
</script>

<style scoped>
  .tinymce-container {
    position: relative;
    line-height: normal;
  }

  .tinymce-container >>> .mce-fullscreen {
    z-index: 10000;
  }

  .tinymce-textarea {
    visibility: hidden;
    z-index: -1;
  }

  .editor-custom-btn-container {
    position: absolute;
    right: 4pt;
    top: 4pt;
    /*z-index: 2005;*/
  }

  .fullscreen .editor-custom-btn-container {
    z-index: 10000;
    position: fixed;
  }

  .editor-upload-btn {
    display: inline-block;
  }

  .panel-body {
    overflow-y: scroll;
  }
</style>
