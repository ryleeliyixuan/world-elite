import Cookies from 'universal-cookie';

const cookies = new Cookies();

const TokenKey = 'company_user_token'
const UserIDKey = 'company_user_userId'

export function getToken() {
  return cookies.get(TokenKey)
}

export function getUserId() {
  return cookies.get(UserIDKey, {path: '/'})
}

export function setToken(token,  rememberFlag) {
  if(rememberFlag && rememberFlag == 1){
    cookies.set(TokenKey, token, {maxAge: 864000})
  }else{
    cookies.set(TokenKey, token)
  }
}

export function setUserId(userId, rememberFlag) {
  if (rememberFlag && rememberFlag == 1) {
    cookies.set(UserIDKey, userId, {maxAge: 864000, path: '/'})
  } else {
    cookies.set(UserIDKey, userId, {path: '/'})
  }
}

export function removeToken() {
  return cookies.remove(TokenKey)
}
