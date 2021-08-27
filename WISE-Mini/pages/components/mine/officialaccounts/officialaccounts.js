const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    USERRECORD: wx.getStorageSync('USERRECORD'),
    weixingongzhonghao: ''
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this
    this.setData({
      UPLOAD_IMAGE: app.globalData.UPLOAD_IMAGE,
      THUMBNAIL: app.globalData.THUMBNAIL,
      ARTWORK: app.globalData.ARTWORK,
      USERRECORD: wx.getStorageSync('USERRECORD')
    })

    let url = app.globalData.HOSTURL + '/bbs/bbsSys/wise/mini/queryValueByKey?sysKey=' + "weixingongzhonghao"
    app.wxRequest('get', url, '', 5000).then(res => {
      that.setData({
        weixingongzhonghao: res.data.result.string
      })
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
  //点击topic图片放大预览
  clickTopicImage(event) {
    var imageList = [this.data.weixingongzhonghao]
    wx.previewImage({
      urls: [this.data.UPLOAD_IMAGE + this.data.weixingongzhonghao], //需要预览的图片http链接列表，注意是数组
      current: 0, // 当前显示图片的http链接，默认是第一个
      success: function (res) {},
      fail: function (res) {},
      complete: function (res) {},
    })
  },
})