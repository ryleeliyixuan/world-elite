const getters = {
  sidebar: state => state.app.sidebar,
  token: state => state.user.token,
  device: state => state.app.device,
  name: state => state.user.name,
  avatar: state => state.user.avatar
}
export default getters
