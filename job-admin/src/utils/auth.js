import Cookies from 'universal-cookie';

const cookies = new Cookies();

const TokenKey = 'world_elite_admin_token'

export function getToken() {
  return cookies.get(TokenKey)
}

export function setToken(token,  rememberFlag) {
  if(rememberFlag && rememberFlag == 1){
    cookies.set(TokenKey, token, {maxAge: 864000})
  }else{
    cookies.set(TokenKey, token)
  }
}

export function removeToken() {
  return cookies.remove(TokenKey)
}
