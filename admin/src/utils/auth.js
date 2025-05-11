import JSCookie from 'js-cookie';

const TokenKey = 'Admin-Token';

export function getToken() {
  return JSCookie.get(TokenKey);
}

export function setToken(token) {
  return JSCookie.set(TokenKey, token);
}

export function removeToken() {
  return JSCookie.remove(TokenKey);
} 