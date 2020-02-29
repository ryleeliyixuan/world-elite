<script>
export default {
  name: 'MenuItem',
  functional: true,
  props: {
    icon: {
      type: String,
      default: ''
    },
    title: {
      type: String,
      default: ''
    },
    redDot:{
      type: Boolean,
      default: false,
    }
  },
  render(h, context) {
    const { icon, title, redDot } = context.props
    const vnodes = []

    if (icon) {
      vnodes.push(<svg-icon icon-class={icon}/>)
    }

    if (title) {
      vnodes.push(<span slot='title'>{(title)}</span>)
    }

    if(redDot) {
       vnodes.push(<el-badge is-dot></el-badge>)
    }

    return vnodes
  }
}
</script>
