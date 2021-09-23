const app = getApp();
const formatUtil = require('../../utils/formatutil');
const API = require('../../utils/API');
// 从v2.11.0起，SDK 支持了 WebSocket，推荐接入
const TIM = require('tim-wx-sdk-ws');
// import TIM from 'tim-js-sdk'; // HTTP 版本
const TIMUploadPlugin = require('tim-upload-plugin');


Page({

  /**
   * 页面的初始数据
   */
  data: {
    USERRECORD: wx.getStorageSync('ALLINFO').bbsUserRecord,
    StatusBar: app.globalData.StatusBar,
    CustomBar: app.globalData.CustomBar,
    windowHeight: app.globalData.windowHeight,
    screenHeight: app.globalData.screenHeight,

    UPLOAD_IMAGE: app.globalData.UPLOAD_IMAGE,
    THUMBNAIL: app.globalData.THUMBNAIL,
    ARTWORK: app.globalData.ARTWORK,

    iconList: [{
      icon: 'noticefill',
      color: 'olive',
      badge: '',
      name: '系统消息',
      url: "/pages/components/msg/sysmessage/sysmessage"
    }, {
      icon: 'commentfill',
      color: 'orange',
      // badge: wx.getStorageSync('ALLINFO').bbsUserRecord.userMessageCount,
      badge: 0,
      name: '互动消息',
      url: "/pages/components/msg/usermessage/usermessage"
    },],

    isPullRefresh: false,        //是否下拉刷新
    conversationList: app.globalData.conversationList       //会话列表
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options1) {
    var that = this
    if (!app.globalData.SYSSTATUS.timLogin) {
      app.initTim({
        success(res) {
          console.log(res)
          that.initListenTim()
          that.getConversationList()
        }
      })
    } else {
      that.initListenTim()
      that.getConversationList()
    }
  },
  testCallback(res) {
    return true
  },
  initListenTim() {
    var that = this
    let tim = app.globalData.tim
    tim.on(TIM.EVENT.CONVERSATION_LIST_UPDATED, that.updataConversationList);
  },
  getConversationList() {
    var that = this
    let tim = app.globalData.tim
    // 拉取会话列表
    tim.getConversationList().then(that.setConversationList).catch(function (imError) {
      console.error("获取会话列表失败", imError) // 获取会话列表失败的相关信息
    });
  },
  setConversationList(event) {
    var that = this

    wx.stopPullDownRefresh()
    if (that.data.isPullRefresh) {
      wx.showToast({
        title: '刷新成功。',
        icon: "success"
      })
      that.data.isPullRefresh = false
    }

    // 收到会话列表更新通知，可通过遍历 event.data 获取会话列表数据并渲染到页面
    // event.name - TIM.EVENT.CONVERSATION_LIST_UPDATED
    // event.data - 存储 Conversation 对象的数组 - [Conversation]
    console.log("会话列表更新2", event)
    var objString = JSON.stringify(event);
    var conversationListTemp = JSON.parse(objString);
    // let conversationListTemp = Object.assign({}, event);
    conversationListTemp.data.conversationList.forEach((item) => {
      item.lastMessage.lastTime = formatUtil.timestampToDate(item.lastMessage.lastTime * 1000)
    })
    console.log(conversationListTemp.data.conversationList)
    that.setData({
      conversationList: conversationListTemp.data.conversationList
    })

    // 展示tabbar
    app.globalData.conversationList = conversationListTemp.data.conversationList
    console.log(conversationListTemp.data.conversationList)
    app.setTabbarBadge()
  },

  updataConversationList(event) {
    var that = this
    let tim = app.globalData.tim
    // 收到会话列表更新通知，可通过遍历 event.data 获取会话列表数据并渲染到页面
    // event.name - TIM.EVENT.CONVERSATION_LIST_UPDATED
    // event.data - 存储 Conversation 对象的数组 - [Conversation]
    console.log("会话列表更新2", event)
    app.globalData.conversationList = event
    var objString = JSON.stringify(event);
    var conversationListTemp = JSON.parse(objString);
    // let conversationListTemp = Object.assign({}, event);
    conversationListTemp.data.forEach((item) => {
      item.lastMessage.lastTime = formatUtil.timestampToDate(item.lastMessage.lastTime * 1000)
    })
    console.log(conversationListTemp.data)
    that.setData({
      conversationList: conversationListTemp.data
    })
  },
  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    var that = this
    that.getConversationList()


    // 红点提示
    let userMessageCount = wx.getStorageSync('ALLINFO').bbsUserRecord.userMessageCount
    let userSysMessageCount = wx.getStorageSync('ALLINFO').bbsUserRecord.userSysMessageCount
    let iconListTmp = that.data.iconList
    iconListTmp[0].badge = userSysMessageCount
    iconListTmp[1].badge = userMessageCount
    that.setData({
      iconList: iconListTmp,
      USERRECORD: wx.getStorageSync('ALLINFO').bbsUserRecord,
    })
  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {
    var that = this
    that.data.isPullRefresh = true
    that.getConversationList()
  },

  // /**
  //    * 用户点击右上角分享
  //    */
  onShareAppMessage: function () {
    return {
      title: "『" + this.data.USERRECORD.regionFullName + "』" + '都在用的本地小程序',
      path: '/pages/index/index?regionCode=' + this.data.USERRECORD.regionCode,
      // imageUrl:
    }
  },
  // 朋友圈
  // onShareTimeline: function () {
  //   return {
  //     title: "『" + this.data.USERRECORD.regionFullName + "』" + '都在用的本地小程序',
  //     path: '/pages/index/index?regionCode=' + this.data.USERRECORD.regionCode,
  //     // 自定义图片路径，可以是本地文件或者网络图片。支持 PNG 及 JPG，显示图片长宽比是 1:1。	默认使用小程序 Logo	
  //     // imageUrl: 
  //   }
  // },

  // ListTouch触摸开始
  ListTouchStart(e) {
    this.setData({
      ListTouchStart: e.touches[0].pageX
    })
  },
  // ListTouch计算方向
  ListTouchMove(e) {
    this.setData({
      ListTouchDirection: e.touches[0].pageX - this.data.ListTouchStart > 0 ? 'right' : 'left'
    })
  },
  // ListTouch计算滚动
  ListTouchEnd(e) {
    if (this.data.ListTouchDirection == 'left') {
      this.setData({
        modalName: e.currentTarget.dataset.target
      })
    } else {
      this.setData({
        modalName: null
      })
    }
    this.setData({
      ListTouchDirection: null
    })
  },
  toChatDetail(e) {
    // console.log(e)
    wx.navigateTo({
      url: '../components/chat/chatdetail/chatdetail',
      success: function (res) {
        // 通过eventChannel向被打开页面传送数据
        res.eventChannel.emit('acceptDataFromOpenerPage', {
          data: e.currentTarget.dataset.conversationitem
        })
      }
    })
  },

  toUserInfo(e) {
    wx.navigateTo({
      url: '../components/mine/userinfo/userinfo',
    })
  },

  open: function () {
    this.setData({
      show: true
    })
  },
  buttontap(e) {
    console.log(e.detail)
  },
  // 点击grid跳转
  gridClick(e) {
    //  || e.currentTarget.dataset.index == 2
    // if (e.currentTarget.dataset.index == 0) {
    //   wx.showToast({
    //     title: '正在开发中...',
    //     icon: 'none'
    //   })
    //   return
    // }
    var that = this
    console.log(e)
    // 清空grid badge
    if (e.currentTarget.dataset.index == 0) {
      let ALLINFO = wx.getStorageSync('ALLINFO')
      ALLINFO.bbsUserRecord.userSysMessageCount = 0
      wx.setStorageSync('ALLINFO', ALLINFO)
      let userMessageCount = wx.getStorageSync('ALLINFO').bbsUserRecord.userMessageCount
      let userSysMessageCount = wx.getStorageSync('ALLINFO').bbsUserRecord.userSysMessageCount
      let iconListTmp = this.data.iconList
      iconListTmp[0].badge = userSysMessageCount
      this.setData({
        iconList: iconListTmp,
      })
      if (userMessageCount != 0) {
        wx.setTabBarBadge({
          index: 2,
          text: '' + userMessageCount
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
    }
    if (e.currentTarget.dataset.index == 1) {
      let ALLINFO = wx.getStorageSync('ALLINFO')
      ALLINFO.bbsUserRecord.userSysMessageCount = 0
      wx.setStorageSync('ALLINFO', ALLINFO)
      let userMessageCount = wx.getStorageSync('ALLINFO').bbsUserRecord.userMessageCount
      let userSysMessageCount = wx.getStorageSync('ALLINFO').bbsUserRecord.userSysMessageCount
      let iconListTmp = this.data.iconList
      iconListTmp[1].badge = userMessageCount
      this.setData({
        iconList: iconListTmp,
      })
      if (userSysMessageCount != 0) {
        wx.setTabBarBadge({
          index: 2,
          text: '' + userMessageCount
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
    }
    wx.navigateTo({
      url: that.data.iconList[e.currentTarget.dataset.index].url,
    })
  },
})