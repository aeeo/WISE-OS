const app = getApp();
const formatUtil = require('../../../../utils/formatutil');
const API = require('../../../../utils/API');
// 从v2.11.0起，SDK 支持了 WebSocket，推荐接入
const TIM = require('tim-wx-sdk-ws');
// import TIM from 'tim-js-sdk'; // HTTP 版本
const TIMUploadPlugin = require('tim-upload-plugin');

Page({
  data: {
    InputBottom: 0,
    conversationUser: {},               //被回复的会话总信息
    conversationUserProfile: {},        //被回复的用户信息
    messageList: [],                    //本轮会话消息列表
    nextReqMessageID: 0,                // 用于续拉，分页续拉时需传入该字段。
    isCompleted: false,                 // 表示是否已经拉完所有消息。
    SYSUSER: wx.getStorageSync('ALLINFO').sysUser,
    USERRECORD: wx.getStorageSync('ALLINFO').bbsUserRecord,

    StatusBar: app.globalData.StatusBar,
    CustomBar: app.globalData.CustomBar,
    windowHeight: app.globalData.windowHeight,
    screenHeight: app.globalData.screenHeight,

    UPLOAD_IMAGE: app.globalData.UPLOAD_IMAGE,
    THUMBNAIL: app.globalData.THUMBNAIL,
    ARTWORK: app.globalData.ARTWORK,
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this

    // 判断是聊天会话进入还是用户主页进入（后期需要改成查询是否有聊天过）
    if (null != options.username && undefined != options.username) {
      //主页进入
      let conversationUserProfile = { "userID": options.username }
      that.data.conversationUserProfile = conversationUserProfile
    } else {
      // 监听acceptDataFromOpenerPage事件，获取上一页面通过eventChannel传送到当前页面的数据
      const eventChannel = this.getOpenerEventChannel()
      eventChannel.on('acceptDataFromOpenerPage', function (data) {
        that.setData({
          conversationUser: data.data,
          conversationUserProfile: data.data.userProfile,
        })
        // console.log(data.data)
        that.getMessageList()
      })
    }

    this.setData({
      SYSUSER: wx.getStorageSync('ALLINFO').sysUser,
      USERRECORD: wx.getStorageSync('ALLINFO').bbsUserRecord,
    })
  },
  getMessageList(e) {
    var that = this
    // 打开某个会话时，第一次拉取消息列表
    let tim = app.globalData.tim
    console.log(that.data.conversationUser.conversationID)
    let promise = tim.getMessageList({ conversationID: that.data.conversationUser.conversationID, count: 15 });
    promise.then(function (imResponse) {
      const messageList = imResponse.data.messageList; // 消息列表。
      const nextReqMessageID = imResponse.data.nextReqMessageID; // 用于续拉，分页续拉时需传入该字段。
      const isCompleted = imResponse.data.isCompleted; // 表示是否已经拉完所有消息。
      console.log("消息列表：", imResponse)

      messageList.forEach((item) => {
        item.formatTime = formatUtil.timestampToDate(item.time * 1000)
      })

      that.setData({
        messageList: messageList,
        nextReqMessageID: nextReqMessageID,
        isCompleted: isCompleted
      }, function () {
        // this is setData callback
        that.setData({
          toView: "messageList-" + (messageList.length - 1),
        })
      })
      // 将某会话下所有未读消息已读上报
      that.setMessageRead()
    });
  },
  setMessageRead() {
    var that = this
    let tim = app.globalData.tim
    // 将某会话下所有未读消息已读上报
    let promise = tim.setMessageRead({ conversationID: that.data.conversationUser.conversationID });
    promise.then(function (imResponse) {
      // 已读上报成功，指定 ID 的会话的 unreadCount 属性值被置为0
      console.log('已读成功:', imResponse);
    }).catch(function (imError) {
      // 已读上报失败
      console.warn('setMessageRead error:', imError);
    });
  },
  scrolltoupper(e) {
    console.log(e)
    var that = this
    let tim = app.globalData.tim
    let nextReqMessageID = that.data.nextReqMessageID
    // 查看更多消息
    let promise = tim.getMessageList({ conversationID: that.data.conversationUser.conversationID, nextReqMessageID, count: 15 });
    promise.then(function (imResponse) {
      console.log(imResponse)
      const messageList = imResponse.data.messageList; // 消息列表。
      const nextReqMessageID = imResponse.data.nextReqMessageID; // 用于续拉，分页续拉时需传入该字段。
      const isCompleted = imResponse.data.isCompleted; // 表示是否已经拉完所有消息。

      let messageListLast = that.data.messageList

      messageList.forEach((item) => {
        item.formatTime = formatUtil.timestampToDate(item.time * 1000)
      })

      messageListLast.forEach((item) => {
        messageList.push(item)
      })

      that.setData({
        messageList: messageList,
        nextReqMessageID: nextReqMessageID,
        isCompleted: isCompleted
      }, function () {
        // this is setData callback
        that.setData({
          toView: "messageList-14",
        })
      })
    });
  },
  // mark:发送消息
  sendTextMessage(e) {
    var that = this
    if (null == that.data.currentChatContent || undefined == that.data.currentChatContent) {
      wx.showToast({
        title: '请输入内容。',
        icon: 'none'
      })
      return
    }
    let tim = app.globalData.tim
    let message = tim.createTextMessage({
      to: that.data.conversationUserProfile.userID,
      conversationType: TIM.TYPES.CONV_C2C,
      // 消息优先级，用于群聊（v2.4.2起支持）。如果某个群的消息超过了频率限制，后台会优先下发高优先级的消息，详细请参考：https://cloud.tencent.com/document/product/269/3663#.E6.B6.88.E6.81.AF.E4.BC.98.E5.85.88.E7.BA.A7.E4.B8.8E.E9.A2.91.E7.8E.87.E6.8E.A7.E5.88.B6)
      // 支持的枚举值：TIM.TYPES.MSG_PRIORITY_HIGH, TIM.TYPES.MSG_PRIORITY_NORMAL（默认）, TIM.TYPES.MSG_PRIORITY_LOW, TIM.TYPES.MSG_PRIORITY_LOWEST
      // priority: TIM.TYPES.MSG_PRIORITY_NORMAL,
      payload: {
        text: that.data.currentChatContent
      },
      // 消息自定义数据（云端保存，会发送到对端，程序卸载重装后还能拉取到，v2.10.2起支持）
      // cloudCustomData: 'your cloud custom data'
    });
    // 2. 发送消息
    let promise = tim.sendMessage(message);
    promise.then(function (imResponse) {
      // 发送成功
      console.log(imResponse);
      that.setData({
        currentChatContent: ""
      })

      // 上屏
      let messageListLast = that.data.messageList
      imResponse.data.message.formatTime = formatUtil.timestampToDate(imResponse.data.message.time * 1000)

      // let messageList = []
      messageListLast.push(imResponse.data.message)
      // messageListLast.forEach((item) => {
      //   messageList.push(item)
      // })

      that.setData({
        messageList: messageListLast,
      }, function () {
        // this is setData callback
        that.setData({
          toView: "messageList-" + (that.data.messageList.length - 1),
        })
      })
    }).catch(function (imError) {
      // 发送失败
      console.warn('sendMessage error:', imError);
    });
  },



  InputFocus(e) {
    this.setData({
      InputBottom: e.detail.height
    })
  },
  InputBlur(e) {
    this.setData({
      InputBottom: 0
    })
  },
  chatInput(e) {
    console.log(e)
    this.data.currentChatContent = e.detail.value
  }
})