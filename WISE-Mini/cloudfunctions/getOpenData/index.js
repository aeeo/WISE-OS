// 云函数入口文件
const cloud = require('wx-server-sdk')

cloud.init()

// 云函数入口函数
exports.main = async (event, context) => {
  let action = event.action     //action功能类似路由
  switch (action) {
    case "getOpenData":
      return getOpenData()
    case "getPhoneNumber":
      return getPhoneNumber(event.weRunData)
    default:
      return "error"
  }
}


function getOpenData() {
  const wxContext = cloud.getWXContext()
  return {
    event,
    openid: wxContext.OPENID,
    appid: wxContext.APPID,
    unionid: wxContext.UNIONID,
  }
}

function getPhoneNumber(weRunData) {
  return weRunData.data.phoneNumber;
}