// pages/components/usermessage/usermessage.js
const app = getApp();
const formatUtil = require('../../../../utils/formatutil');
Page({
  /** 
   * 页面的初始数据
   */
  data: {
    USERRECORD: wx.getStorageSync('ALLINFO').bbsUserRecord,
    //分页
    pageNo: 1,
    // 触底
    onReachBottomLoading: false,
    // 消息list
    userMessageList: [],
    showMessageDialog: false,
    messageContent: "",
    messageButton: [{
      text: '确定'
    }],
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.setData({
      UPLOAD_IMAGE: app.globalData.UPLOAD_IMAGE
    })
    this.getUserMessageList()
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

    this.data.userMessageList = []
    this.setData({
      isPullRefresh: true,
      onReachBottomLoading: false
    })
    this.getUserMessageList()
  },


  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {
    this.setData({
      pageNo: this.data.pageNo + 1,
      onReachBottomLoading: false
    })
    this.getUserMessageList()
  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  },
  //获取用户消息列表
  getUserMessageList() {

    var that = this
    that.setData({
      getTopicFlag: false
    })
    let url = app.globalData.HOSTURL + '/bbs/bbsUserMessage/wise/mini/fullList?pageNo=' + this.data.pageNo
    app.wxRequest('get', url, '').then(res => {
      if (res.data.code == 200) {
        if (res.data.result.records != null && res.data.result.records.length > 0) {
          //列表追加
          var tempList = that.data.userMessageList
          var resTopicList = res.data.result.records
          resTopicList.forEach(item => {
            if (item.topicImageUrl == null) {
              item.showUserMessageImage = false
            } else {
              item.showUserMessageImage = true,
                item.topicImageUrl = that.data.UPLOAD_IMAGE + item.topicImageUrl
            }
            if (item.messageType == 1) {
              item.center = item.replyContent
              item.createTime = "赞了你的留言  " + formatUtil.showDate(new Date(item.createTime))
            }
            if (item.messageType == 2) {
              item.center = "赞了你的发布"
              item.createTime = formatUtil.showDate(new Date(item.createTime))
              that.setData({
                showUserMessageImage: false
              })
            }
            if (item.messageType == 3) {
              item.center = item.replyContent
              item.createTime = "回复了你的发布  " + formatUtil.showDate(new Date(item.createTime))
            }
            if (item.messageType == 4) {
              item.center = "赞了你的评论"
              item.createTime = formatUtil.showDate(new Date(item.createTime))
              that.setData({
                showUserMessageImage: false
              })
            }
          })

          for (var item in resTopicList) {
            tempList.push(resTopicList[item])
          }
          // 将records中的userMessageCount=0
          let ALLINFO = wx.getStorageSync('ALLINFO')
          ALLINFO.bbsUserRecord.userMessageCount = 0
          wx.setStorageSync("ALLINFO", ALLINFO)
          console.log(tempList)
          that.setData({
            userMessageList: tempList,
            getTopicFlag: true,
            isFirstGetTopicFlag: false
          })
          if (that.data.isPullRefresh) {
            wx.showToast({
              title: '刷新成功',
              icon: 'success'
            })
            wx.stopPullDownRefresh()
            that.data.isPullRefresh = false
          }
          if (res.data) {

          }
        } else {
          that.setData({
            pageNo: this.data.pageNo - 1,
            getTopicFlag: true,
          })
        }
      } else {
        wx.showToast({
          title: '获取信息出错',
          icon: "none"
        })
        that.setData({
          pageNo: this.data.pageNo - 1,
          getTopicFlag: true,
        })
      }
      that.setData({
        onReachBottomLoading: true
      })
    }, err => {
      that.setData({
        userMessageList: res.data.result,
        getTopicFlag: false,
        onReachBottomLoading: true
      })
    })
  },
  // 跳转到贴子详情
  topicdetails: function (event) {
    console.log(event)
    const userMessageItem = event.currentTarget.dataset.item
    if (userMessageItem.messageType == "1") {
      this.setData({
        showMessageDialog: true,
        messageContent: userMessageItem.replyContent
      })
      return
    }
    // 构造传递参数
    let data = {}
    let topicitem = {}
    topicitem.id = userMessageItem.topicId
    data.topicitem = topicitem
    wx.navigateTo({
      url: '../../topic/topicdetails/topicdetails?topicId=' + userMessageItem.topicId,
    })
  },
  messageButtonTap(e) {
    // console.log(e)
    this.setData({
      showMessageDialog: false
    })
  },
  goUserInfo(e) {
    console.log(e)
    wx.navigateTo({
      url: '/pages/components/mine/userinfo/userinfo?username=' + e.currentTarget.dataset.username,
    })
  }
})