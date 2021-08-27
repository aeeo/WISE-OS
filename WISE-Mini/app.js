

const miniShopPlugin = requirePlugin('mini-shop-plugin');
// 从v2.11.0起，SDK 支持了 WebSocket，推荐接入
const TIM = require('tim-wx-sdk-ws');
// import TIM from 'tim-js-sdk'; // HTTP 版本
const TIMUploadPlugin = require('tim-upload-plugin');
const API = require('./utils/API');

App({
  //全局配置项
  globalData: {
    //静态资源路径    userdata/用户上传图片路径   sys/系统图片  userinfo/用户信息路径
    UPLOAD_IMAGE: 'https://静态资源域名/',
    THUMBNAIL: '-topicthum', // 缩略图
    ARTWORK: '-topicimage', // 50%原图有水印
    ARTWORKWATER: '-topicimagewater100', //原图有水印
    ARTWORKNOWATER: '-topicimagenowater', //原图无水印

    // ********************************************************
    // 主机开发本地环境  配置本地静态ip方便调试
    // HOSTURL: 'http://本机IP:10002/wise-dev',
    // HOSTURL: 'http://本机IP:10001/wise',
    // HOSTURL: 'https://域名/wise',                  // 线上正式环境 
    // HOSTURL: 'https://域名/wise-dev',              // 线上开发环境
    // ********************************************************

    SwitchRegion: false,
    ISAUTHORIZATION: false,
    USERRECORD: {},
    SYSUSER: {},
    // 用户是否授权
    HASUSERINFO: false,
    needReloadTopicList: false, //是否需要重新加载页面
    needModUserIsStar: [false, -1], //是否需要修改收藏标签，-1代表列表下标
    firstLogin: true,         //第一次登陆标志，在Tim登陆后改为false
    conversationList: [],       //会话列表

    // 系统各类状态
    SYSSTATUS: {
      timLogin: false
    }
  },
  //自定义头
  onLaunch: function (option) {
    var that = this
    //自定义交易组件
    miniShopPlugin.initApp(this, wx);
    //自动版本更新
    this.autoUpdata()
    //获取分享参数
    wx.setStorageSync("OPTION", option)
    console.log("初始化接收参数：", option)
    wx.setStorageSync('CURRENTCLASSCODE', 'index')
    // https://developers.weixin.qq.com/miniprogram/dev/api/base/system/system-info/wx.getSystemInfo.html
    wx.getSystemInfo({
      success: e => {
        // 状态栏高度
        this.globalData.StatusBar = e.statusBarHeight;
        let custom = wx.getMenuButtonBoundingClientRect();
        this.globalData.Custom = custom;
        this.globalData.CustomBar = custom.bottom + custom.top - e.statusBarHeight;
        this.globalData.windowHeight = e.windowHeight     //可使用窗口高度，单位px
        this.globalData.screenHeight = e.screenHeight     //屏幕高度，单位px
      }
    })
  },
  //mark: 检测用户是否授权个人信息
  HASUSERINFO() {
    var that = this
    let SYSUSER = that.globalData.SYSUSER
    if (SYSUSER == {}) {
      wx.showToast({
        title: '正在同步数据，请稍后再试。',
        icon: 'none'
      })
      return 'wait'
    } else {
      if (undefined != SYSUSER.realname && "" != SYSUSER.realname && null != SYSUSER.realname) {
        that.globalData.HASUSERINFO = true
        if (that.globalData.firstLogin) {
          // 如果已经授权信息，且第一次登陆，需要加载消息列表
          that.initTim()
          that.globalData.firstLogin = false
        }
      } else {
        that.globalData.HASUSERINFO = false
      }
    }

    console.log("用户授权状态：", that.globalData.HASUSERINFO)
    return that.globalData.HASUSERINFO
  },
  /**
   * 本地没有token，获取用户openId，注册游客账户（没有发贴功能），获取token
   */
  // mark:获取Token
  getFirstLoginToken() {
    var that = this
    return getToken = new Promise(function (resolve, reject) {
      wx.login({
        success(res) {
          if (res.code) {
            wx.request({
              url: that.globalData.HOSTURL + '/bbs/bbsAuth/wise/mini/getToken?code=' + res.code + '&regionCode=' + wx.getStorageSync('OPTION').query.regionCode,
              success(res) {
                // console.log("请求结果：",res)
                var body = res.data.result.result
                wx.setStorageSync('TOKEN', body.token)
                wx.setStorageSync("LastLoginTime", new Date().getDate())
                // 每次获取token后更新用户记录，信息，限制(否则在 第一次使用小程序的用户打开分享的活动和贴子详情后能够点赞，需要校验)
                that.HASUSERINFO()
                that.getSysUser()
                that.userBehaviorLimit()
                resolve(res)
              },
              fail(err) {
                console.log("Token获取失败")
                // reject(err)
              }
            })
          } else {
            console.log('登录失败！' + res.errMsg)
            reject('登录失败！')
          }
        }
      })
    })
  },
  /**
   * 封装wx.request请求
   * method： 请求方式
   * url: 请求地址
   * data： 要传递的参数
   * callback： 请求成功回调函数
   * errFun： 请求失败回调函数
   **/
  // mark: Request
  wxRequest(method, url, data, timeout) {
    if (undefined == timeout || '' == timeout || null == timeout) {
      timeout = 20000
    }
    var that = this
    var request = new Promise(function (resolve, reject) {
      wx.request({
        url: url,
        method: method,
        data: data,
        timeout: timeout,
        header: {
          'content-type': method == 'GET' ? 'application/json' : 'application/json',
          'Accept': 'application/json',
          'X-Access-Token': wx.getStorageSync('TOKEN'),
          'regioncode': wx.getStorageSync('USERRECORD').regionCode,
          'classCode': wx.getStorageSync('CURRENTCLASSCODE') // 当前版块  后台根据情况筛选
        },
        dataType: 'json',
        success: function (res) {
          console.log(res)
          if (res.data.message == "Token失效，请重新登录") {
            console.log("本地Token失效,重新获取Token。")
            that.getFirstLoginToken()
          }
          const code = res.data.code
          switch (code) {
            case 500:
              wx.showToast({
                title: "啊哦，服务器开小差了",
                icon: "error",
                duration: 2000
              })
              break
            case 1000:
              wx.showToast({
                title: res.data.message,
                icon: "none"
              })
              break
          }
          resolve(res)
        },
        fail: function (err) {
          console.log(err)

          reject(err)
        },
        complete: function (params) {
          // 不管如何，关闭加载框
          // wx.hideLoading()
        }
      })
    })
    return request
  },
  /**
   * 通用Get请求，传url + 参数
   * @param url
   */
  wxGetRequest(url) {
    var that = this
    var request = new Promise(function (resolve, reject) {
      that.wxRequest("Get", that.globalData.HOSTURL + url, null, null).then(res => {
        resolve(res.data.result)
      }, err => {
        reject(err)
      })
    })
    return request;
  },

  /**
   * 推荐使用wx.getUserProfile获取用户信息，开发者每次通过该接口获取用户个人信息均需用户确认
   */
  // mark: 获取用户信息
  getUserProfile(e) {
    console.log("申请权限")
    var that = this
    var request = new Promise(function (resolve, reject) {
      wx.showLoading({
        title: '申请权限...',
      })
      // 开发者妥善保管用户快速填写的头像昵称，避免重复弹窗
      wx.getUserProfile({
        desc: '此操作需要获取您的授权', // 声明获取用户个人信息后的用途，后续会展示在弹窗中，请谨慎填写
        success: (resUserInfo) => {
          console.log("获取权限成功")
          that.getUserRecord()

          wx.setStorageSync('USERINFO', resUserInfo.userInfo)

          let userInfo = resUserInfo.userInfo
          that.wxRequest('post', that.globalData.HOSTURL + '/bbs/bbsAuth/wise/mini/perfectUser', userInfo, '').then(res => {
            wx.showToast({
              title: '授权成功。',
              icon: 'success'
            })
            let getUserProfile = {
              "userInfo": resUserInfo.userInfo,
              "status": true //更新状态
            }
            //拉取用户信息，否则HasUserInfo不更新，后面考虑合并到HasUserInfo里
            that.getSysUser().then(res => {
              // 获取用户限制
              that.HASUSERINFO()
            })
            that.userBehaviorLimit()
            resolve(getUserProfile)
          }, err => {
            wx.showToast({
              title: '授权失败。',
              icon: 'none'
            })
            let getUserProfile = {
              "userInfo": resUserInfo.userInfo,
              "status": false //更新状态
            }
            reject(getUserProfile)
          })
        },
        fail: (failRes) => {
          console.log(failRes)
          if (failRes.errMsg == "getUserProfile:fail auth deny") {
            wx.showToast({
              title: '取消授权。',
              icon: 'none'
            })
          } else {
            wx.showToast({
              title: '授权出错！',
              icon: 'none'
            })
          }
          let getUserProfile = {
            "status": false //更新状态
          }
          reject(getUserProfile)
        },
        complete: (res) => {
          console.log("complete")
          wx.hideLoading()
        }
      })
    })
    return request
  },

  /**
   * 生成UUID
   */
  guid2() {
    function S4() {
      return (((1 + Math.random()) * 0x10000) | 0).toString(16).substring(1);
    }
    return (S4() + S4() + "-" + S4() + "-" + S4() + "-" + S4() + "-" + S4() + S4() + S4());
  },

  //mark:获取用户Record
  getUserRecord() {
    var that = this
    let url = that.globalData.HOSTURL + '/bbs/bbsUserRecord/wise/mini/list'
    var getUserRecord = new Promise(function (resolve, reject) {
      that.wxRequest('get', url, '',).then((res) => {
        console.log("Auth----------获取用户Record", res)
        wx.setStorageSync("LastLoginTime", new Date().getDate())
        wx.setStorageSync('USERRECORD', res.data.result)
        that.globalData.USERRECORD = res.data.result
        // 检测用户是否授权个人信息
        that.HASUSERINFO()
        resolve(res.data.result)
      }, (err) => {
        reject(err)
      })
    })
    return getUserRecord
  },
  setTabbarBadge() {
    let USERRECORD = wx.getStorageSync('USERRECORD')
    let conversationListData = this.globalData.conversationList
    // console.log(this.globalData.conversationList)
    let converationListUnReadCount = 0
    if (undefined != conversationListData && null != conversationListData) {
      // console.log(conversationListData)
      conversationListData.forEach((item) => {
        converationListUnReadCount += item.unreadCount
      })
    }
    if (USERRECORD.userMessageCount != 0 || USERRECORD.userSysMessageCount != 0 || 0 != converationListUnReadCount) {
      console.log("setTabbar")
      wx.setTabBarBadge({
        index: 2,
        text: '' + (USERRECORD.userMessageCount + USERRECORD.userSysMessageCount + converationListUnReadCount)
      })
    } else {
      wx.removeTabBarBadge({
        index: 2,
        success(res) {
          // console.log("移除Badge成功", res)
        },
        fail(res) {
          // console.log("移除Badge失败", res)
        }
      })
    }
  },
  /**
   * 版本更新
   */
  autoUpdata() {
    const updateManager = wx.getUpdateManager()
    // 新的版本已经下载好，调用 applyUpdate 应用新版本并重启
    // updateManager.applyUpdate()
    updateManager.onCheckForUpdate(function (res) {
      if (res.hasUpdate) {
        // wx.clearStorage()
      }
    })
    updateManager.onUpdateReady(function () {
      wx.showModal({
        title: '更新提示',
        content: '新版本已经连夜发布成功，为保证体验，请立即重启。',
        success: function (res) {
          if (res.confirm) {
            // 新的版本已经下载好，调用 applyUpdate 应用新版本并重启
            // wx.clearStorage()
            updateManager.applyUpdate()
          }
        }
      })
    })
    updateManager.onUpdateFailed(function () {
      // 新版本下载失败
    })
  },
  // 用户行为限制，是否能够发布贴子，是否能够点赞，留言等
  userBehaviorLimit() {
    var that = this
    var request = new Promise(function (resolve, reject) {
      let url = that.globalData.HOSTURL + '/bbs/bbsAuth/wise/mini/userBehaviorLimit'
      that.wxRequest('get', url).then(res => {
        // console.log(res)
        wx.setStorage({
          data: res.data.result,
          key: 'userBehaviorLimit',
        })
        resolve(res)
      }, err => {
        // console.log(err)
        reject(err)
      })
    })
    return request
  },
  //获取用户信息
  getSysUser() {
    var that = this
    var request = new Promise(function (resolve, reject) {
      that.wxGetRequest("/bbs/bbsAuth/wise/mini/querySysUser").then(res => {
        console.log("Auth----------用户信息SysUser：", res)
        wx.setStorage({
          data: res,
          key: 'SYSUSER',
        })
        that.globalData.SYSUSER = res
        resolve(res)
      }, err => {
        reject(err)
      })
    })
    return request
  },
  createTim() {
    let options = {
      SDKAppID: 1400519380 // 接入时需要将0替换为您的即时通信 IM 应用的 SDKAppID
    };
    // 创建 SDK 实例，`TIM.create()`方法对于同一个 `SDKAppID` 只会返回同一份实例
    let tim = TIM.create(options); // SDK 实例通常用 tim 表示
    this.globalData.tim = tim
    return tim
  },
  //mark： 初始化Tim
  initTim(parameter) {
    let success = {}
    if (undefined != parameter && undefined != parameter.success) {
      success = parameter.success
    }

    var that = this
    let tim = that.createTim()
    // 设置 SDK 日志输出级别，详细分级请参见 <a href="https://web.sdk.qcloud.com/im/doc/zh-cn//SDK.html#setLogLevel">setLogLevel 接口的说明</a>
    // tim.setLogLevel(0); // 普通级别，日志量较多，接入时建议使用
    tim.setLogLevel(1); // release 级别，SDK 输出关键信息，生产环境时建议使用
    // 注册腾讯云即时通信 IM 上传插件
    tim.registerPlugin({
      'tim-upload-plugin': TIMUploadPlugin
    });

    // 收到推送的单聊、群聊、群提示、群系统通知的新消息，可通过遍历 event.data 获取消息列表数据并渲染到页面
    // event.name - TIM.EVENT.MESSAGE_RECEIVED
    // event.data - 存储 Message 对象的数组 - [Message]
    tim.on(TIM.EVENT.MESSAGE_RECEIVED, that.messageReceived);

    // tim.on(TIM.EVENT.MESSAGE_REVOKED, function (event) {
    // 收到消息被撤回的通知。使用前需要将SDK版本升级至v2.4.0或更高版本。
    // event.name - TIM.EVENT.MESSAGE_REVOKED
    // event.data - 存储 Message 对象的数组 - [Message] - 每个 Message 对象的 isRevoked 属性值为 true
    // });

    // tim.on(TIM.EVENT.MESSAGE_READ_BY_PEER, function (event) {
    // SDK 收到对端已读消息的通知，即已读回执。使用前需要将SDK版本升级至v2.7.0或更高版本。仅支持单聊会话。
    // event.name - TIM.EVENT.MESSAGE_READ_BY_PEER
    // event.data - event.data - 存储 Message 对象的数组 - [Message] - 每个 Message 对象的 isPeerRead 属性值为 true
    // });

    // 收到会话列表更新通知，可通过遍历 event.data 获取会话列表数据并渲染到页面
    // event.name - TIM.EVENT.CONVERSATION_LIST_UPDATED
    // event.data - 存储 Conversation 对象的数组 - [Conversation]
    tim.on(TIM.EVENT.CONVERSATION_LIST_UPDATED, that.conversationListUpdated);

    // tim.on(TIM.EVENT.GROUP_LIST_UPDATED, function (event) {
    // 收到群组列表更新通知，可通过遍历 event.data 获取群组列表数据并渲染到页面
    // event.name - TIM.EVENT.GROUP_LIST_UPDATED
    // event.data - 存储 Group 对象的数组 - [Group]
    // });

    // tim.on(TIM.EVENT.PROFILE_UPDATED, function (event) {
    // 收到自己或好友的资料变更通知
    // event.name - TIM.EVENT.PROFILE_UPDATED
    // event.data - 存储 Profile 对象的数组 - [Profile]
    // });

    // tim.on(TIM.EVENT.BLACKLIST_UPDATED, function (event) {
    // 收到黑名单列表更新通知
    // event.name - TIM.EVENT.BLACKLIST_UPDATED
    // event.data - 存储 userID 的数组 - [userID]
    // });

    // tim.on(TIM.EVENT.ERROR, function (event) {
    // 收到 SDK 发生错误通知，可以获取错误码和错误信息
    // event.name - TIM.EVENT.ERROR
    // event.data.code - 错误码
    // event.data.message - 错误信息
    // });

    // tim.on(TIM.EVENT.SDK_NOT_READY, function (event) {
    // 收到 SDK 进入 not ready 状态通知，此时 SDK 无法正常工作
    // event.name - TIM.EVENT.SDK_NOT_READY
    // });

    // tim.on(TIM.EVENT.KICKED_OUT, function (event) {
    // 收到被踢下线通知
    // event.name - TIM.EVENT.KICKED_OUT
    // event.data.type - 被踢下线的原因，例如 :
    //   - TIM.TYPES.KICKED_OUT_MULT_ACCOUNT 多实例登录被踢
    //   - TIM.TYPES.KICKED_OUT_MULT_DEVICE 多终端登录被踢
    //   - TIM.TYPES.KICKED_OUT_USERSIG_EXPIRED 签名过期被踢（v2.4.0起支持）。
    // });

    // tim.on(TIM.EVENT.NET_STATE_CHANGE, function (event) {
    // 网络状态发生改变（v2.5.0 起支持）。
    // event.name - TIM.EVENT.NET_STATE_CHANGE
    // event.data.state 当前网络状态，枚举值及说明如下：
    //   - TIM.TYPES.NET_STATE_CONNECTED - 已接入网络
    //   - TIM.TYPES.NET_STATE_CONNECTING - 连接中。很可能遇到网络抖动，SDK 在重试。接入侧可根据此状态提示“当前网络不稳定”或“连接中”
    //   - TIM.TYPES.NET_STATE_DISCONNECTED - 未接入网络。接入侧可根据此状态提示“当前网络不可用”。SDK 仍会继续重试，若用户网络恢复，SDK 会自动同步消息
    // });
    tim.on(TIM.EVENT.SDK_READY, function (event) {
      // 修改个人标配资料
      let sysUser = wx.getStorageSync('SYSUSER')
      let promise = tim.updateMyProfile({
        nick: sysUser.realname,
        avatar: sysUser.avatar,
        gender: sysUser.sex == 0 ? TIM.TYPES.GENDER_FEMALE : TIM.TYPES.GENDER_MALE,
        selfSignature: '个性签名',
        allowType: TIM.TYPES.ALLOW_TYPE_ALLOW_ANY
      });
      promise.then(function (imResponse) {
        if (typeof success === "function") {
          success(true)
        }
        console.log("更新资料成功", imResponse.data); // 更新资料成功
      }).catch(function (imError) {
        console.error('更新资料失败:', imError); // 更新资料失败的相关信息
      });
    });
    // 开始登录
    that.timLogin({ tim: tim, })
  },
  // mark:Tim登录
  timLogin(option) {
    console.log(option)
    let tim = option.tim
    let success = option.success
    let fail = option.fail
    var that = this
    // 获取userSig
    that.wxRequest(API.user_sig.method, API.user_sig.url, {
      "username": wx.getStorageSync('SYSUSER').username
    }).then(res => {
      if (res.statusCode == 200) {
        tim.login({
          userID: wx.getStorageSync('SYSUSER').username,
          userSig: res.data
        }).then(imResponse => {
          if (imResponse.data.repeatLogin === true) {
            // 标识账号已登录，本次登录操作为重复登录。v2.5.1 起支持
            console.warn("Tim登录失败.", imResponse.data.errorInfo);
            //确保callback是一个函数   
            if (typeof success === "function") {
              success(true) //重复按登录成功算
              // fail(false)
            }
          } else {
            that.globalData.SYSSTATUS.timLogin = true
            console.log("Tim登录成功."); // 登录成功
            //确保callback是一个函数   
            if (typeof success === "function") {
              success(true)
            }
          }
        });
      } else {
        console.log(res)
      }
    }, err => {
      console.log(res)
    })

  },
  messageReceived(event) {
    console.log("收到消息", event)
  },
  conversationListUpdated(event) {
    console.log("会话列表更新1", event)
    var objString = JSON.stringify(event);
    var conversationListTemp = JSON.parse(objString);
    this.globalData.conversationList = conversationListTemp.data
    this.setTabbarBadge()
  }
})