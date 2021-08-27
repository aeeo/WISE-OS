// pages/components/topicsearch/topicsearch.js
const app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {

  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    // 双向绑定？
    this.setData({
      search: this.search.bind(this)
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
  //搜索
  search: function (value) {
    var that = this
    return new Promise((resolve, reject) => {
      setTimeout(() => {
        wx.showLoading({
          title: '加载中...',
        })
        // resolve([{
        //   text: '搜索结果',
        //   value: 1
        // }, {
        //   text: '搜索结果2',
        //   value: 2
        // }])
        // 
        console.log(value)
        if (null != value && undefined != value && '' != value) {
          let url = app.globalData.HOSTURL + '/bbs/bbsTopic/wise/mini/searchTopicByKeyword?keyword=' + value
          app.wxRequest('get', url, ).then(res => {
            if (res.data.code == 200) {
              let result = []
              const records = res.data.result.records
              if (records != null && records != undefined) {
                for (var item in records) {
                  const arrItem = {}
                  if (records[item].title != '' && records[item].title != null) {
                    arrItem.text = records[item].title.substring(0,20)
                    arrItem.value = records[item].id
                  } else {
                    arrItem.text = records[item].content.substring(0,20)
                    arrItem.value = records[item].id
                  }
                  result.push(arrItem)
                }
              }
              resolve(result)
            } else {
              wx.showToast({
                title: '啊哦，服务器开小差了。',
                icon: "none"
              })
            }
          }, err => {
            wx.showToast({
              title: '啊哦，服务器开小差了。',
              icon: 'none'
            })
          })
        } else {
          // 输入列表为空，将查询列表置空
          resolve([])
          wx.hideLoading()
        }
      }, 200)
    })
  },
  //选择搜索结果，跳转贴子详情
  selectResult(e) {
    console.log(e)
    wx.navigateTo({
      url: '../topicdetails/topicdetails?topicId=' + e.detail.item.value,
    })
  },
  // 回车，跳转搜索列表
  searchList() {
    console.log("123")
    wx.showToast({
      title: 'title',
      icon: 'none'
    })
  }
})