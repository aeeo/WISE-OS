const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    activityId: "",

    UPLOAD_IMAGE: app.globalData.UPLOAD_IMAGE,
    ARTWORKNOWATER: app.globalData.ARTWORKNOWATER,
    SHAREHOSTURL: '',
    isAnon: '',
    HOSTURL: app.globalData.HOSTURL
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this
    this.data.activityId = options.activityId
    wx.showLoading({
      title: '加载中...',
    })

    var obj = wx.getLaunchOptionsSync()
    if (obj.scene == 1154) {
      that.data.SHAREHOSTURL = app.globalData.HOSTURL
      that.data.isAnon = '/anon'
      that.getMainData(that.data.SHAREHOSTURL)

    } else {
      that.data.SHAREHOSTURL = app.globalData.HOSTURL
      // 获取token
      app.getFirstLoginToken().then(res => {
        that.getMainData(that.data.SHAREHOSTURL)
      })
    }

    //部分版本在无referrerInfo的时候会返回 undefined,可以做一下判断
    // console.log('启动小程序的路径:', obj.path)
    // console.log('启动小程序的场景值:', obj.scene)
    // console.log('启动小程序的 query 参数:', obj.query)
    // console.log('来源信息:', obj.shareTicket)
    // console.log('来源信息参数appId:', obj.referrerInfo.appId)
    // console.log('来源信息传过来的数据:', obj.referrerInfo.extraData)
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
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  },
  // 加载活动信息
  getMainData(HOSTURL) {
    var that = this
    let url = HOSTURL + '/bbs/bbsActivity/wise/mini/queryById' + that.data.isAnon + '?id=' + this.data.activityId
    app.wxRequest('get', url, '').then(res => {
      console.log(res)
      if (res.data.code == 200) {
        that.setData({
          activity: res.data.result
        })
      } else {

      }
      wx.hideLoading()
    }, err => {
      wx.hideLoading()
    })
  },

  formSubmit(e) {
    var that = this

    let type = e.detail.target.dataset.type
    if (type == "1") {
      let activityUser = e.detail.value

      if (activityUser.chineseName == "") {
        wx.showToast({
          title: '姓名不能为空',
          icon: 'none'
        })
        return
      }
      if (activityUser.phoneNumber == "") {
        wx.showToast({
          title: '手机不能为空',
          icon: 'none'
        })
        return
      }else if(activityUser.phoneNumber.length > 11){
        wx.showToast({
          title: '请输入正确的手机号',
          icon: 'none'
        })
        return
      }
      activityUser.activityId = that.data.activityId

      let requestData = e.detail.value
      requestData.activityId = that.data.activityId
      let url = app.globalData.HOSTURL + '/bbs/bbsActivityUser/wise/mini/add'
      app.wxRequest('post', url, requestData).then(res => {
        console.log(res)
        if (res.data.code == 200) {
          wx.showToast({
            title: '报名成功',
            icon: "success"
          })
          that.getMainData(that.data.SHAREHOSTURL)
        } else {
          // wx.showToast({
          //   title: '报名失败',
          //   icon: "none"
          // })
        }
      })
    } else if (type == "2") {
      let requestData = { "activityId": that.data.activityId }
      let url = app.globalData.HOSTURL + '/bbs/bbsActivityUser/wise/mini/canclApply'
      app.wxRequest('post', url, requestData).then(res => {
        console.log(res)
        wx.showToast({
          title: '取消成功',
          icon: "success"
        })
        that.getMainData(that.data.SHAREHOSTURL)
      }, err => {
        console.log("取消失败", err)
      })
    }

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {
    var that = this
    let shareUrl = '/pages/index/index?activityId=' + this.data.activityId
    let shareTitle = ""
    let activity = that.data.activity
    if (undefined == activity.title || null == activity.title || "" == activity.title) {
      shareTitle = activity.content
    } else {
      shareTitle = activity.title
    }
    let imageUrl = ""
    if (null == activity.imageUrl || undefined == activity.imageUrl) {
      imageUrl = ''
    } else {
      imageUrl = that.data.UPLOAD_IMAGE + activity.imageUrl + that.data.ARTWORKNOWATER
    }
    return {
      title: shareTitle,
      path: shareUrl,
      imageUrl: imageUrl
    }
  },
  // 朋友圈
  onShareTimeline: function () {
    var that = this
    // var ctx = this.selectComponent('#activity')
    // ctx 为组件实例
    // var cover = ctx.imgList[0] // 首张图可以作为转发封面图
    // ctx.imgList.forEach((src, i, array) => {
    //   console.log(src)
    //   array[i] = src.replace('thumb', '') // 替换为高清图链接
    // })
    let shareTitle = ""
    let activity = that.data.activity
    if (undefined == activity.title || null == activity.title || "" == activity.title) {
      shareTitle = activity.content
    } else {
      shareTitle = activity.title
    }
    // console.log(shareTitle)
    let imageUrl = ""
    if (null == activity.imageUrl || undefined == activity.imageUrl) {
      imageUrl = ''
    } else {
      imageUrl = that.data.UPLOAD_IMAGE + activity.imageUrl + that.data.ARTWORKNOWATER
    }
    return {
      title: shareTitle,
      query: {
        "activityId": that.data.activityId
      },
      imageUrl: imageUrl
    }
  },
})