const app = getApp();
const API = require('../../../../utils/API');

Page({

  /**
   * 页面的初始数据
   */
  data: {
    UPLOAD_IMAGE: app.globalData.UPLOAD_IMAGE,
    THUMBNAIL: app.globalData.THUMBNAIL,
    ARTWORKNOWATER: app.globalData.ARTWORKNOWATER,
    waimaiList: [],
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    wx.showLoading({
      title: '加载中...',
    })
    this.getWaimaiList()
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
  // mark: 获取外卖列表
  getWaimaiList() {
    var that = this
    app.wxRequest(API.waimai_list.method, API.waimai_list.url, {
      'status': 1,
      'deleteFlag': 0
    }).then(res => {
      let waimaiList = res.data.result.records
      that.setData({
        waimaiList: waimaiList
      })
      wx.hideLoading()
    }, err => {
      wx.hideLoading()
    })
  },
  // mark:打开其他小程序
  openMiniProgram(e) {
    var that = this
    let waimaiitem = e.currentTarget.dataset.waimaiitem
    console.log(e)
    wx.navigateToMiniProgram({
      appId: waimaiitem.appId,
      path: waimaiitem.path,
      extraData: {
        foo: 'bar'
      },
      envVersion: 'release',
      success(res) {
        console.log("打开成功")
        // 插入记录
        that.addUserWaimai({
          'waimaiId': waimaiitem.id,
          'regionCode': wx.getStorageSync("USERRECORD").regionCode,
          'useStatus': 1,
        })
      },
      fail(err) {
        // 插入记录
        that.addUserWaimai({
          'waimaiId': waimaiitem.id,
          'regionCode': wx.getStorageSync("USERRECORD").regionCode,
          'useStatus': 0,
        })
        // console.log("打开失败")
      },
      complete(e) {
        // console.log("complete")
      }
    })
  },
  // 插入记录
  addUserWaimai(bbsWaimaiUser) {
    var that = this
    app.wxRequest(API.waimai_user_add.method, API.waimai_user_add.url, bbsWaimaiUser).then(res => {

    })
  }
  /**
   * 美团外卖6
   * /index/pages/h5/h5?weburl=https%3A%2F%2Frunion.meituan.com%2Furl%3Fkey%3D3a1904270f7f9bfb0b89d2fb731064f3%26url%3Dhttps%253A%252F%252Fi.meituan.com%252Fawp%252Fhfe%252Fblock%252Fa13b87919a9ace9cfab4%252F89400%252Findex.html%253Fappkey%253D3a1904270f7f9bfb0b89d2fb731064f3%253Ameituanwaimai6%26sid%3Dmeituanwaimai6&lch=cps:waimai:5:3a1904270f7f9bfb0b89d2fb731064f3:meituanwaimai6&f_token=1&f_userId=1
   * 美团4
   * /index/pages/h5/h5?weburl=https%3A%2F%2Frunion.meituan.com%2Furl%3Fkey%3D3a1904270f7f9bfb0b89d2fb731064f3%26url%3Dhttps%253A%252F%252Fi.meituan.com%252Fawp%252Fhfe%252Fblock%252Fc9ff59b58f6f5bf385b6%252F94455%252Findex.html%253Fappkey%253D3a1904270f7f9bfb0b89d2fb731064f3%253Ameituan%26sid%3Dmeituan&lch=cps:waimai:5:3a1904270f7f9bfb0b89d2fb731064f3:meituan&f_token=1&f_userId=1
   */
  // openMeiTuan6(){
  //   wx.navigateToMiniProgram({
  //     appId: 'wxde8ac0a21135c07d',
  //     path: '/index/pages/h5/h5?lch=cps:waimai:5:3a1904270f7f9bfb0b89d2fb731064f3:meituanwaimai6:2:99175&f_userId=1&weburl=https%3A%2F%2Fclick.meituan.com%2Ft%3Ft%3D1%26c%3D1%26p%3DOWMpZ-uzIFOVe6JyOONs3dXuqV0qcAf-r-KCvHdXiNfR7IOX1mpLp1OrN5s_8ex8ydPsbp1sBNVQCcnhhRDl1kZuprhKrUiLtdp_7xrgrCYYMHTrdzpQnzY-FaSiQhmwxc5TDwF02K5k-unNPMFwMr2G052eurr9EDRC9a2_pTCOGwciKXC-xnmeflIwFrOr9Fs3_H0ygIfHqDVzNKlzRjaRwYj8mHchsmZcWhyW7_YqCoLvuXUjavZqhU2I5yEHDRDkYaKStJycA9KoiBZ-DlhwWKZE7JPh38wSsjULTeSRn7EHtwCc_Vo1accn5PrlvBKI2zaoDEryvWJecIz6aZ92zEaeSi29AZgZP9f27trOP45li_fd2l8XgFHoAW7YRspnc0dO_8m-lsabFG444AuEIIu-tQxwZS1j7xBwSJpW7zzh77VJsCcyl8i5EedKs0fg4_9vOc4GPUSD2e-cs-cbAhdnusUsEY5jrTo9QD-HZr6pz4Z_Qg4AI5Uo5vIF1h0HFARi-h5Q1tBOQ1-oUSNx8rU66KLodZJkdo7eJCAMap-KVbaxFDQXX8qE1e-0SChPFf27lWBnBsXZct2i5Bl-dOsZyD-IQ4WEGU222pJpPWT20017UUdhJ955b6bTss1uagU1riwbiBcmTEaU3VGxIqmKiSf3Bv--7u15sXOI5Enyi7YLpUywCfoLSCbkUznm4LAlcU-YHiSJN7kIzg&f_token=1',
  //     extraData: {
  //       foo: 'bar'
  //     },
  //     envVersion: 'release',
  //     success(res) {
  //       console.log(res)
  //       // 打开成功
  //     }
  //   })
  // },
  // openMeiTuan4(){
  //   wx.navigateToMiniProgram({
  //     appId: 'wxde8ac0a21135c07d',
  //     path: '/index/pages/h5/h5?weburl=https%3A%2F%2Frunion.meituan.com%2Furl%3Fkey%3D3a1904270f7f9bfb0b89d2fb731064f3%26url%3Dhttps%253A%252F%252Fi.meituan.com%252Fawp%252Fhfe%252Fblock%252Fc9ff59b58f6f5bf385b6%252F94455%252Findex.html%253Fappkey%253D3a1904270f7f9bfb0b89d2fb731064f3%253Ameituan%26sid%3Dmeituan&lch=cps:waimai:5:3a1904270f7f9bfb0b89d2fb731064f3:meituan&f_token=1&f_userId=1',
  //     extraData: {
  //       foo: 'bar'
  //     },
  //     envVersion: 'release',
  //     success(res) {
  //       console.log(res)
  //       // 打开成功
  //     }
  //   })
  // },

  // /**
  //  * 饿了么百亿补贴
  //  */
  // openELeMoBaiYi(){
  //   wx.navigateToMiniProgram({
  //     appId: 'wxece3a9a4c82f58c9',
  //     // path: 'taoke/pages/shopping-guide/index?scene=Fl3b1ru',    //爱分享
  //     path: 'pages/sharePid/web/index?scene=https://s.click.ele.me/TJ612qu',//  每日领饿了么红包蓝
  //     extraData: {
  //       foo: 'bar'
  //     },
  //     envVersion: 'release',
  //     success(res) {
  //       // 打开成功
  //     }
  //   })
  // },
  // openELeMoChaoShi(){
  //   wx.navigateToMiniProgram({
  //     appId: 'wxece3a9a4c82f58c9',
  //     // path: 'taoke/pages/shopping-guide/index?scene=Fl3b1ru',    //爱分享
  //     path: 'pages/sharePid/web/index?scene=https://s.click.ele.me/Ln512qu',//  在家逛超市红
  //     extraData: {
  //       foo: 'bar'
  //     },
  //     envVersion: 'release',
  //     success(res) {
  //       // 打开成功
  //     }
  //   })
  // }
})