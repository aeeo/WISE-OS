const app = getApp();
Component({
  /**
   * 组件的一些选项
   */
  options: {
    addGlobalClass: true,
    multipleSlots: true
  },
  /**
   * 组件的对外属性
   */
  properties: {
    bgColor: {
      type: String,
      default: ''
    },
    isCustom: {
      type: [Boolean, String],
      default: false
    },
    isBack: {
      type: [Boolean, String],
      default: false
    },
    showSite: {
      type: [Boolean, String],
      default: false
    },
    bgImage: {
      type: String,
      default: ''
    },
    regionCode: {
      type: String,
      default: ''
    },
    showHome: {
      type: [Boolean, String],
      default: false
    },
    isPrivateRegion: {  //是否私有区域
      type: Boolean,
      default: true
    }
  },
  /**
   * 组件的初始数据
   */
  data: {
    StatusBar: app.globalData.StatusBar,
    CustomBar: app.globalData.CustomBar,
    Custom: app.globalData.Custom
  },
  /**
   * 组件的方法列表
   */
  methods: {
    BackPage() {
      wx.navigateBack({
        delta: 1
      });
    },
    GoHome() {
      // wx.switchTab({
      //   url: '/pages/index/index'
      // })
      wx.reLaunch({
        url: '/pages/index/index?regionCode=' + this.data.regionCode,
      })
    },
    toHome() {
      wx.reLaunch({
        url: '/pages/index/index'
      })
    },
    //展示定位地址
    showSite(e) {
      if (this.data.isPrivateRegion) {
        wx.showToast({
          title: '本区域是私有区域哦。',
          icon: 'none'
        })
        return
      }
      wx.navigateTo({
        url: '/pages/components/site/site',
      })
      // var that = this
      // wx.hideTabBar()
      // setTimeout(function () {
      //   console.log("触发")
      //   that.triggerEvent('showSite', "viewModal")
      // }, 5000)
    },
  }
})