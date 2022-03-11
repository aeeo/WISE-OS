function dateFormat(fmt, dateStr) {
  let date
  if (typeof (dateStr) == "string") {
    date = stringToDate(dateStr)
  } else {
    date = dateStr
  }

  let ret
  const opt = {
    "Y+": date.getFullYear().toString(), // 年
    "m+": (date.getMonth() + 1).toString(), // 月
    "d+": date.getDate().toString(), // 日
    "H+": date.getHours().toString(), // 时
    "M+": date.getMinutes().toString(), // 分
    "S+": date.getSeconds().toString() // 秒
    // 有其他格式化字符需求可以继续添加，必须转化成字符串
  }
  for (let k in opt) {
    ret = new RegExp("(" + k + ")").exec(fmt);
    if (ret) {
      fmt = fmt.replace(ret[1], (ret[1].length == 1) ? (opt[k]) : (opt[k].padStart(ret[1].length, "0")))
    };
  }
  return fmt;
}
// 贴子 
function showDate(dateStr) {
  let date
  if (typeof (dateStr) == "string") {
    date = stringToDate(dateStr)
  } else {
    date = dateStr
  }
  const opt = {
    "Y+": date.getFullYear().toString(), // 年
    "m+": (date.getMonth() + 1).toString(), // 月
    "d+": date.getDate().toString(), // 日
    "H+": date.getHours().toString(), // 时
    "M+": date.getMinutes().toString(), // 分
    "S+": date.getSeconds().toString() // 秒
    // 有其他格式化字符需求可以继续添加，必须转化成字符串
  }
  const year = date.getFullYear()
  const month = date.getMonth() + 1
  const day = date.getDate()
  const hour = date.getHours()
  const minute = date.getMinutes()
  const second = date.getSeconds()

  var currentDate = new Date()
  const yearCurrent = currentDate.getFullYear()
  const monthCurrent = currentDate.getMonth() + 1
  const dayCurrent = currentDate.getDate()
  const hourCurrent = currentDate.getHours()
  const minuteCurrent = currentDate.getMinutes()
  const secondCurrent = currentDate.getSeconds()

  var currentDateStamp = (new Date()).getTime()
  var dateStamp = date.getTime()
  const oneHour = 3600000
  const oneDay = oneHour * 24
  const oneMonth = oneDay * 30

  if ((currentDateStamp - dateStamp) < (oneHour / 20)) {
    // 3分钟内
    return this.dateFormat("刚刚", date);
  } else
    if ((currentDateStamp - dateStamp) < oneHour) {
      // 向上取整（有小数，整数部分就+1）
      // Math.ceil(5.1234);   6
      return this.dateFormat(Math.ceil((currentDateStamp - dateStamp) / 60000) + "分钟前", date);
    } else if (year == yearCurrent && month == monthCurrent && day == dayCurrent) {
      return this.dateFormat("HH:MM", date);
    } else if (year == yearCurrent && month == monthCurrent && day == (dayCurrent - 1)) {
      return this.dateFormat("昨天HH:MM", date);
    }
    else if (year == yearCurrent) {
      return this.dateFormat("mm月dd日", date);
    }
  return this.dateFormat("YYYY年mm月dd日", date);
}
// mark: 评论时间格式化
/**
 * 刚刚
 * 3分钟前
 * 59分钟前
 * 20:41
 * 昨天20:41
 * 05月20日
 */
function showReplyDate(dateStr) {
  let date
  if (typeof (dateStr) == "string") {
    date = stringToDate(dateStr)
  } else {
    date = dateStr
  }

  const opt = {
    "Y+": date.getFullYear().toString(), // 年
    "m+": (date.getMonth() + 1).toString(), // 月
    "d+": date.getDate().toString(), // 日
    "H+": date.getHours().toString(), // 时
    "M+": date.getMinutes().toString(), // 分
    "S+": date.getSeconds().toString() // 秒
    // 有其他格式化字符需求可以继续添加，必须转化成字符串
  }
  const year = date.getFullYear()
  const month = date.getMonth() + 1
  const day = date.getDate()
  const hour = date.getHours()
  const minute = date.getMinutes()
  const second = date.getSeconds()

  var currentDate = new Date()
  const yearCurrent = currentDate.getFullYear()
  const monthCurrent = currentDate.getMonth() + 1
  const dayCurrent = currentDate.getDate()
  const hourCurrent = currentDate.getHours()
  const minuteCurrent = currentDate.getMinutes()
  const secondCurrent = currentDate.getSeconds()

  var currentDateStamp = (new Date()).getTime()
  var dateStamp = date.getTime()
  const oneHour = 3600000
  const oneDay = oneHour * 24
  const oneMonth = oneDay * 30

  var maohao = ":"
  if (minute < 10) {
    maohao = ":0"
  }
  if ((currentDateStamp - dateStamp) < (oneHour / 20)) {
    // 3分钟内
    return this.dateFormat("刚刚", date);
  } else
    if ((currentDateStamp - dateStamp) < oneHour) {
      // 向上取整（有小数，整数部分就+1）
      // Math.ceil(5.1234);   6
      return this.dateFormat(Math.ceil((currentDateStamp - dateStamp) / 60000) + "分钟前", date);
    } else if (year == yearCurrent && month == monthCurrent && day == dayCurrent) {
      return this.dateFormat("HH:MM", date);
    } else if (year == yearCurrent && month == monthCurrent && day == (dayCurrent - 1)) {
      return this.dateFormat("昨天HH:MM", date);
    }
    // else if ((currentDateStamp - dateStamp) < oneHour * 3) {
    //   return this.dateFormat("2小时前", date);
    // } else if ((currentDateStamp - dateStamp) < oneHour * 4) {
    //   return this.dateFormat("3小时前", date);
    // } else if ((currentDateStamp - dateStamp) < oneHour * 5) {
    //   return this.dateFormat("4小时前", date);
    // } else if ((currentDateStamp - dateStamp) < oneHour * 6) {
    //   return this.dateFormat("5小时前", date);
    // } else if ((currentDateStamp - dateStamp) < oneHour * 7) {
    //   return this.dateFormat("6小时前", date);
    // } else if ((currentDateStamp - dateStamp) < oneHour * 8) {
    //   return this.dateFormat("7小时前", date);
    // } else if ((currentDateStamp - dateStamp) < oneHour * 9) {
    //   return this.dateFormat("8小时前", date);
    // } else if (year == yearCurrent && month == monthCurrent && day == dayCurrent) {
    //   return this.dateFormat("今天", date);
    // } 
    // else if (year == yearCurrent && month == monthCurrent && day == dayCurrent - 1) {
    //   return this.dateFormat("昨天", date);
    // } else if (year == yearCurrent && month == monthCurrent && day == dayCurrent - 2) {
    //   return this.dateFormat("前天", date);
    // } else {
    //   // 向上取整（有小数，整数部分就+1）
    //   // Math.ceil(5.1234);   6
    //   return this.dateFormat(Math.ceil((currentDateStamp - dateStamp) / oneDay) + "天前", date);
    // }
    else if (year == yearCurrent) {
      return this.dateFormat("mm月dd日", date);
    }
  // else if (year == yearCurrent && month == monthCurrent - 1) {
  //     return this.dateFormat("上个月dd日 HH:MM", date);
  //   } else if (year == yearCurrent - 1) {
  //     return this.dateFormat("去年mm月dd日 HH:MM", date);
  //   } else if (year == yearCurrent - 2) {
  //     return this.dateFormat("前年mm月dd日 HH:MM", date);
  //   }
  return this.dateFormat("YYYY年mm月dd日", date);
}


// mark: 时间戳转日期
/**
 * timestamp:毫秒
 * 刚刚
 * 3分钟前
 * 59分钟前
 * 20:41
 * 昨天20:41
 * 05月20日
 */
function timestampToDate(timestamp) {
  // console.log(timestamp)
  let date = new Date(timestamp)
  // console.log(date)
  const opt = {
    "Y+": date.getFullYear().toString(), // 年
    "m+": (date.getMonth() + 1).toString(), // 月
    "d+": date.getDate().toString(), // 日
    "H+": date.getHours().toString(), // 时
    "M+": date.getMinutes().toString(), // 分
    "S+": date.getSeconds().toString() // 秒
    // 有其他格式化字符需求可以继续添加，必须转化成字符串
  }
  const year = date.getFullYear()
  const month = date.getMonth() + 1
  const day = date.getDate()
  const hour = date.getHours()
  const minute = date.getMinutes()
  const second = date.getSeconds()

  var currentDate = new Date()
  const yearCurrent = currentDate.getFullYear()
  const monthCurrent = currentDate.getMonth() + 1
  const dayCurrent = currentDate.getDate()
  const hourCurrent = currentDate.getHours()
  const minuteCurrent = currentDate.getMinutes()
  const secondCurrent = currentDate.getSeconds()

  var currentDateStamp = (new Date()).getTime()
  var dateStamp = date.getTime()
  const oneHour = 3600000
  const oneDay = oneHour * 24
  const oneMonth = oneDay * 30

  var maohao = ":"
  if (minute < 10) {
    maohao = ":0"
  }
  if ((currentDateStamp - dateStamp) < (oneHour / 20)) {
    // 3分钟内
    return this.dateFormat("刚刚", date);
  } else
    if ((currentDateStamp - dateStamp) < oneHour) {
      // 向上取整（有小数，整数部分就+1）
      // Math.ceil(5.1234);   6
      return this.dateFormat(Math.ceil((currentDateStamp - dateStamp) / 60000) + "分钟前", date);
    } else if (year == yearCurrent && month == monthCurrent && day == dayCurrent) {
      return this.dateFormat("HH:MM", date);
    } else if (year == yearCurrent && month == monthCurrent && day == (dayCurrent - 1)) {
      return this.dateFormat("昨天HH:MM", date);
    }
    // else if ((currentDateStamp - dateStamp) < oneHour * 3) {
    //   return this.dateFormat("2小时前", date);
    // } else if ((currentDateStamp - dateStamp) < oneHour * 4) {
    //   return this.dateFormat("3小时前", date);
    // } else if ((currentDateStamp - dateStamp) < oneHour * 5) {
    //   return this.dateFormat("4小时前", date);
    // } else if ((currentDateStamp - dateStamp) < oneHour * 6) {
    //   return this.dateFormat("5小时前", date);
    // } else if ((currentDateStamp - dateStamp) < oneHour * 7) {
    //   return this.dateFormat("6小时前", date);
    // } else if ((currentDateStamp - dateStamp) < oneHour * 8) {
    //   return this.dateFormat("7小时前", date);
    // } else if ((currentDateStamp - dateStamp) < oneHour * 9) {
    //   return this.dateFormat("8小时前", date);
    // } else if (year == yearCurrent && month == monthCurrent && day == dayCurrent) {
    //   return this.dateFormat("今天", date);
    // } 
    // else if (year == yearCurrent && month == monthCurrent && day == dayCurrent - 1) {
    //   return this.dateFormat("昨天", date);
    // } else if (year == yearCurrent && month == monthCurrent && day == dayCurrent - 2) {
    //   return this.dateFormat("前天", date);
    // } else {
    //   // 向上取整（有小数，整数部分就+1）
    //   // Math.ceil(5.1234);   6
    //   return this.dateFormat(Math.ceil((currentDateStamp - dateStamp) / oneDay) + "天前", date);
    // }
    else if (year == yearCurrent) {
      return this.dateFormat("mm月dd日", date);
    }
  // else if (year == yearCurrent && month == monthCurrent - 1) {
  //     return this.dateFormat("上个月dd日 HH:MM", date);
  //   } else if (year == yearCurrent - 1) {
  //     return this.dateFormat("去年mm月dd日 HH:MM", date);
  //   } else if (year == yearCurrent - 2) {
  //     return this.dateFormat("前年mm月dd日 HH:MM", date);
  //   }
  return this.dateFormat("YYYY年mm月dd日", date);
}

// mark: 转日期天数
/**
 * 256天
 */
function dateToDayCount(dateStr) {
  let date
  if (typeof (dateStr) == "string" || !dateStr) {
    date = stringToDate(dateStr)
  } else {
    date = dateStr
  }
  var currentDateStamp = (new Date()).getTime()
  var dateStamp = date.getTime()
  const oneHour = 3600000
  const oneDay = oneHour * 24
  // 向上取整（有小数，整数部分就+1）
  // Math.ceil(5.1234);   6
  return this.dateFormat(Math.ceil((currentDateStamp - dateStamp) / oneDay), date);
}


// 格式化数字
function showCount(count) {

}

function stringToDate(dateStr) {
  if(dateStr){
    return new Date(dateStr.replace(/-/g, '/'))//兼容ios
  }else{
    return new Date()
  }
}
//必须在这里暴露接口，以便被外界访问，不然就不能访问
module.exports = {
  dateFormat: dateFormat,
  showDate: showDate,
  showReplyDate: showReplyDate,
  showCount: showCount,
  timestampToDate: timestampToDate,
  dateToDayCount: dateToDayCount
}