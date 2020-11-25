import Cookies from 'universal-cookie';

const cookies = new Cookies();

const TokenKey = 'web_user_token'
const UserIDKey = 'world_user_userId'

export function getToken() {
  return cookies.get(TokenKey)
  // return "1254006090146533376AjCAbEaaaKxitlSkmpqf"
}
export function getUserId() {
  return cookies.get(UserIDKey)
}

export function setToken(token,  rememberFlag) {
  if(rememberFlag && rememberFlag === 1){
    cookies.set(TokenKey, token, {maxAge: 864000})
  }else{
    cookies.set(TokenKey, token)
  }
}
export function setUserId(userId,  rememberFlag) {
  if(rememberFlag && rememberFlag == 1){
    cookies.set(UserIDKey, userId, {maxAge: 864000})
  }else{
    cookies.set(UserIDKey, userId)
  }
}
export function removeToken() {
  return cookies.remove(TokenKey)
}
