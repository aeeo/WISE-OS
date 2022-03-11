var app = getApp();
const formatUtil = require('../../../../utils/formatutil.js');
const API = require('../../../../utils/API');
Page({

  /**
   * 页面的初始数据
   */
  data: {
    USERRECORD: wx.getStorageSync('ALLINFO').bbsUserRecord,
    SYSUSER: wx.getStorageSync('ALLINFO').sysUser,
    UPLOAD_IMAGE: app.globalData.UPLOAD_IMAGE,
    USERALLINFO: {},
    topicLists: [],
    pageNo: 1,
    load: true
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    console.log(options)
    this.data.username = options.username
    this.getUserAllInfo(options.username)
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

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  },
  toChatDetail(e) {
    wx.showToast({
      title: '本区域私聊功能暂未开放',
      icon: "none"
    })
    return
    wx.navigateTo({
      url: '../../chat/chatdetail/chatdetail?username=' + this.data.username,
    })
  },
  // 获取用户所有信息
  getUserAllInfo(username) {
    var that = this
    let url = API.user_all_info.url + "?username=" + username + '&pageNo=' + that.data.pageNo
    app.wxRequest(API.user_all_info.method, url, '').then(res => {
      if (res.data.code == 200) {
        console.log(res)
        res.data.result.createTime = formatUtil.dateToDayCount(res.data.result.createTime)
        res.data.result.bbsTopicFullDtoList.forEach((item) => {
          item.userRole = item.userRole.substring(4)
          // 添加动画属性
          item.exeCuteAnimation = item.userIsPraise
          //正则去除html标签
          item.content = item.content.replace(/<\/?.+?\/?>/g, '')
          // 去除跳转标签
          item.content = item.content.replace(/(?=!_).+(?:_!)/g, '')
          item.createTime = formatUtil.showDate(item.createTime)
          // item.updateTime = formatUtil.showDate(item.updateTime)
          item.publicTime = formatUtil.showDate(item.publicTime)
          item.editTime = formatUtil.showDate(item.editTime)
        })
        //列表追加
        var tempList = that.data.topicLists
        for (var item in res.data.result.bbsTopicFullDtoList) {
          tempList.push(res.data.result.bbsTopicFullDtoList[item])
        }
        that.setData({
          topicLists: tempList,
          getTopicFlag: true,
          USERALLINFO: res.data.result,
          load: false
        })
      } else {

      }
    }, err => {
      wx.showToast({
        title: '获取用户信息失败',
        icon: "none"
      })
    })
  },
  onReachBottom: function () {
    this.setData({
      pageNo: this.data.pageNo + 1,
      load: true
    })
    this.getUserAllInfo(this.data.username)
  },
  onPullDownRefresh() {
    this.setData({
      topicLists: [],
      load: true,
      pageNo: 1
    })
    this.getUserAllInfo(this.data.username)
  },

  //点击头像放大预览      无水印   就一张
  clickAvatarImage(event) {
    var imageList = []
    imageList.push(this.data.UPLOAD_IMAGE + event.currentTarget.dataset.imagelist + app.globalData.ARTWORKNOWATER)
    let currentImage = this.data.UPLOAD_IMAGE + event.currentTarget.dataset.imagelist + app.globalData.ARTWORKNOWATER

    console.log(imageList, currentImage)
    wx.previewImage({
      urls: imageList, //需要预览的图片http链接列表，注意是数组
      current: currentImage, // 当前显示图片的http链接，默认是第一个
      success: function (res) {
        console.log("预览成功")
      },
      fail: function (res) {
        console.log("预览失败")
      },
      complete: function (res) { },
    })
  },
})