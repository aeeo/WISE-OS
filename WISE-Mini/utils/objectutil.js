// 从现有对象数组中取key返回新对象数组
function getObjectKeyList(objectArr, key) {
  let tempArr = []
  objectArr.forEach(item => {
    tempArr.push(item.key)    //.key写法不对，有问题
  })
}
//必须在这里暴露接口，以便被外界访问，不然就不能访问
module.exports = {
  getObjectKeyList: getObjectKeyList
}