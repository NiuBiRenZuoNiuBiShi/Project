export function arrayToBase64(arr) {
  const binary = String.fromCharCode(...arr);
  return btoa(binary);
}

export function base64ToArray(base64Str) {
  const binaryStr = atob(base64Str); // Base64 解码成原始字符串
  const byteArray = Array.from(binaryStr, char => char.charCodeAt(0)); // 转成 byte 数组
  return byteArray;
}

export function base64ToBiArray(base64Str) {
  return new Uint8Array(base64ToArray(base64Str));
}