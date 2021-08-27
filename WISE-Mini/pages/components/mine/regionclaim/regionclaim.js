const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    USERRECORD:wx.getStorageSync('USERRECORD')
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.setData({
      UPLOAD_IMAGE: app.globalData.UPLOAD_IMAGE
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
    var that = this
    var imageList = []
    wx.previewImage({
      urls: [that.data.UPLOAD_IMAGE + 'sys/wazgzt%E5%BE%AE%E4%BF%A1%E5%90%8D%E7%89%871000.png'], //需要预览的图片http链接列表，注意是数组
      current: 0, // 当前显示图片的http链接，默认是第一个
      success: function (res) {},
      fail: function (res) {},
      complete: function (res) {},
    })
  },
})