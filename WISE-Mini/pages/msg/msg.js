const app = getApp();
const formatUtil = require('../../utils/formatutil');
const API = require('../../utils/API');

Page({
  data: {
    USERRECORD: wx.getStorageSync('USERRECORD'),
    StatusBar: app.globalData.StatusBar,
    CustomBar: app.globalData.CustomBar,


    iconList: [{
      icon: 'noticefill',
      color: 'olive',
      badge: '',
      name: '活动',
      // url: "/pages/components/msg/sysmessage/sysmessage"
      url: "/packageA/activity/activity"

    }, {
      icon: 'commentfill',
      color: 'orange',
      badge: wx.getStorageSync('USERRECORD').userMessageCount,
      name: '行星小商店',
      url: "/pages/components/msg/usermessage/usermessage"
    }, {
      icon: 'presentfill',
      color: 'red',
      badge: '',
      name: '好吃与好物',
      url: "/pages/components/msg/savemoney/savemoney"
    },],
    skin: true,
    gridBorder: 'no-border',
    UPLOAD_IMAGE: '',
    bbsMessageList: [],

    messageContent: '', //留言内容
    //分页
    pageNo: 1,
    // 触底
    onReachBottomLoading: false,
    //点赞动画
    list: [{
      name: 'scale-down',
      color: 'orange'
    }],
    // 在线人数
    onlinePeopleNumber: 0,
    msgFocus: false,
    showMsgSkeleton: true //显示骨架屏
  },
  onLoad: function () {
    // this.getOnlinePeopleNumber()
    // this.towerSwiper('swiperList')
    this.setData({
      UPLOAD_IMAGE: app.globalData.UPLOAD_IMAGE,
      THUMBNAIL: app.globalData.THUMBNAIL,
      USERRECORD: wx.getStorageSync('USERRECORD')
    })
    this.waitMessageList()
    console.log(wx.getStorageSync('USERRECORD'))
  },
  onShow() {
    // 获取用户Record，刷新tabbar提示
    var that = this
    app.getUserRecord().then(res => {
      that.setData({
        USERRECORD: res
      })
      app.setTabbarBadge()
    })
    let userMessageCount = wx.getStorageSync('USERRECORD').userMessageCount
    let userSysMessageCount = wx.getStorageSync('USERRECORD').userSysMessageCount
    let iconListTmp = this.data.iconList
    iconListTmp[0].badge = userSysMessageCount
    iconListTmp[1].badge = userMessageCount
    this.setData({
      iconList: iconListTmp,
      USERRECORD: wx.getStorageSync('USERRECORD')
    })
  },
  onTabItemTap(item) {
    // console.log("msg监听tabbar")
    // 获取用户Record，刷新tabbar提示
    var that = this
    app.getUserRecord().then(res => {
      that.setData({
        USERRECORD: res
      })
      app.setTabbarBadge()
    })
  },

  // 九宫格start
  showModal(e) {
    console.log(app.globalData.HASUSERINFO)
    var that = this
    // 查询用户是否授权
    if (app.globalData.HASUSERINFO) {
      that.setData({
        modalName: e.currentTarget.dataset.target,
        msgFocus: true
      })

    } else {
      app.getUserProfile()
    }
  },
  hideModal(e) {
    this.setData({
      modalName: null
    })
  },

  gridchange: function (e) {
    this.setData({
      gridCol: e.detail.value
    });
  },
  gridswitch: function (e) {
    this.setData({
      gridBorder: e.detail.value
    });
  },
  menuBorder: function (e) {
    this.setData({
      menuBorder: e.detail.value
    });
  },
  menuArrow: function (e) {
    this.setData({
      menuArrow: e.detail.value
    });
  },
  menuCard: function (e) {
    this.setData({
      menuCard: e.detail.value
    });
  },
  switchSex: function (e) {
    this.setData({
      skin: e.detail.value
    });
  },

  //mark:获取留言板列表
  getMessages() {
    var that = this
    let url = app.globalData.HOSTURL + '/bbs/bbsMessageBoard/wise/mini/fullList?pageNo=' + this.data.pageNo
    var bbsMessageList = app.wxRequest('get', url, '').then(res => {
      if (res.data.code == 200) {
        if (that.data.userPullRefresh) {
          wx.showToast({
            title: '刷新成功',
            icon: 'success'
          })
          that.data.userPullRefresh = false
        }
        if (res.data.result.records.length > 0) {
          res.data.result.records.forEach((item) => {
            // 添加动画属性
            item.exeCuteAnimation = item.userIsPraise
            item.createTime = formatUtil.showDate(new Date(item.createTime.replace(/-/g, '/')))
            // item.updateTime = formatUtil.showDate(new Date(item.updateTime.replace(/-/g, '/')))
          })
          //列表追加
          var tempList = that.data.bbsMessageList
          for (var item in res.data.result.records) {
            tempList.push(res.data.result.records[item])
          }
          that.setData({
            bbsMessageList: tempList,
            getTopicFlag: true
          })
          // 获取用户Record，刷新tabbar提示
          app.getUserRecord().then(res => {
            // 设置grid红点
            let userMessageCount = res.userMessageCount
            let userSysMessageCount = res.userSysMessageCount
            let iconListTmp = that.data.iconList
            iconListTmp[0].badge = userSysMessageCount
            iconListTmp[1].badge = userMessageCount

            that.setData({
              iconList: iconListTmp,
              USERRECORD: res
            })
            app.setTabbarBadge()
          })



        } else {
          that.setData({
            pageNo: that.data.pageNo - 1,
            getTopicFlag: true
          })
        }
      } else {
        that.setData({
          pageNo: 1
        })
      }
      that.setData({
        showMsgSkeleton: false, //隐藏骨架屏
      })
    }, err => {
      that.setData({
        showMsgSkeleton: false, //隐藏骨架屏
      })
    })
    return bbsMessageList
  },
  //获取留言板列表-带加载动画
  waitMessageList() {
    var that = this
    var index = 1
    that.getMessages()
    var timer = setInterval(function () {
      if (index % 5 == 0) {
        that.getMessages()
      }
      if (index == 10) {
        that.setData({
          isLoad: true,
          onReachBottomLoading: false
        })
        wx.stopPullDownRefresh()
        clearInterval(timer)
      }
      if (that.data.getTopicFlag == true) {
        that.setData({
          isLoad: true,
          onReachBottomLoading: true
        })
        wx.stopPullDownRefresh()
        clearInterval(timer)
      }
      index = index + 1;
    }, 1000);
  },
  /**
   * 留言获取
   */
  messageInputContent(e) {
    this.setData({
      messageContent: e.detail.value
    })
  },
  /**
   * 发布留言确认 上传留言
   *
   */
  uploadMessage(e) {
    wx.showLoading({
      title: '正在发布...',
      mask: true
    })
    if (this.data.messageContent.length > 100) {
      wx.showToast({
        title: '留言内容过长，需在100字以内。',
        icon: 'none',
        duration: 2000
      })
    } else if (this.data.messageContent.length < 2) {
      wx.showToast({
        title: '留言内容过短，需大于2个字。',
        icon: 'none',
        duration: 2000
      })
    } else {
      var that = this
      var uploadContent = {
        content: this.data.messageContent
      }
      var bbsMessageList = app.wxRequest(API.message_board_add.method, API.message_board_add.url, uploadContent, (res) => {
        resolve(res.data.result.records)
      }, (err) => {
        reject(err)
      })
      bbsMessageList.then(res => {
        if (res.data.code == 200) {
          that.hideModal(e)
          wx.showToast({
            title: '留言成功',
            icon: 'success',
            duration: 2000
          })

          that.setData({
            messageContent: ''
          })
          that.data.bbsMessageList = []
          that.getMessages()
        } else {
          wx.showToast({
            title: res.data.message,
            icon: 'none',
            duration: 2000
          })
        }
      })
    }

  },
  onReachBottom: function () {
    this.setData({
      pageNo: this.data.pageNo + 1,
      onReachBottomLoading: false
    })
    this.waitMessageList()
  },
  onPullDownRefresh() {
    // 每次刷新pageNo重置为1
    this.data.bbsMessageList = []
    this.setData({
      onReachBottomLoading: false,
      pageNo: 1,
      userPullRefresh: true
    })
    this.waitMessageList()
    // wx.stopPullDownRefresh()
  },
  //点赞
  clickPraise(e) {
    console.log(e)
    console.log(this.data.bbsMessageList[e.target.dataset.bindex].userIsPraise)

    //若果没有点赞  点赞并展示动画
    if (!this.data.bbsMessageList[e.target.dataset.bindex].userIsPraise) {
      var bbsMessageListTem = this.data.bbsMessageList
      bbsMessageListTem[e.target.dataset.bindex].userIsPraise = true
      bbsMessageListTem[e.target.dataset.bindex].exeCuteAnimation = true
      bbsMessageListTem[e.target.dataset.bindex].praiseCount = bbsMessageListTem[e.target.dataset.bindex].praiseCount + 1
      this.setData({
        bbsMessageList: bbsMessageListTem,
      })
      let url = API.user_praise_clickPraise.url + '?messageId=' + e.target.id + '&isPraise=' + true
      app.wxRequest(API.user_praise_clickPraise.method, url, '', (res) => {
        resolve(res.data.result.records)
      }, (err) => {
        reject(err)
      }).then(res => {

      }, err => {

      })
      // 动画
      // var anmiaton = e.currentTarget.dataset.class;
      // var that = this;
      // that.setData({
      //     animation: anmiaton
      // })
      // setTimeout(function () {
      //     that.setData({
      //         animation: ''
      //     })
      // }, 500)
    } else {
      var bbsMessageListTem = this.data.bbsMessageList
      bbsMessageListTem[e.target.dataset.bindex].userIsPraise = false
      bbsMessageListTem[e.target.dataset.bindex].exeCuteAnimation = false
      bbsMessageListTem[e.target.dataset.bindex].praiseCount = bbsMessageListTem[e.target.dataset.bindex].praiseCount - 1
      this.setData({
        bbsMessageList: bbsMessageListTem,
      })

      let url = API.user_praise_clickPraise.url + '?messageId=' + e.target.id + '&isPraise=' + false
      app.wxRequest(API.user_praise_clickPraise.method, url, '', (res) => {
        resolve(res.data.result.records)
      }, (err) => {
        reject(err)
      }).then(res => {

      }, err => {

      })
    }
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
      // let USERRECORD = wx.getStorageSync('USERRECORD')
      // USERRECORD.userSysMessageCount = 0
      // wx.setStorageSync('USERRECORD', USERRECORD)
      // let userMessageCount = wx.getStorageSync('USERRECORD').userMessageCount
      // let userSysMessageCount = wx.getStorageSync('USERRECORD').userSysMessageCount
      // let iconListTmp = this.data.iconList
      // iconListTmp[0].badge = userSysMessageCount
      // this.setData({
      //   iconList: iconListTmp,
      // })
      // if (userMessageCount != 0) {
      //   wx.setTabBarBadge({
      //     index: 1,
      //     text: '' + userMessageCount
      //   })
      // } else {
      //   wx.removeTabBarBadge({
      //     index: 1,
      //     success(res) {
      //       // console.log("移除Badge成功", res)
      //     },
      //     fail(res) {
      //       // console.log("移除Badge失败", res)
      //     }
      //   })
      // }
      // wx.showToast({
      //   title: '正在开发中...',
      //   icon: 'none'
      // })
      // return
    }
    if (e.currentTarget.dataset.index == 1) {
      // let USERRECORD = wx.getStorageSync('USERRECORD')
      // USERRECORD.userSysMessageCount = 0
      // wx.setStorageSync('USERRECORD', USERRECORD)
      // let userMessageCount = wx.getStorageSync('USERRECORD').userMessageCount
      // let userSysMessageCount = wx.getStorageSync('USERRECORD').userSysMessageCount
      // let iconListTmp = this.data.iconList
      // iconListTmp[1].badge = userMessageCount
      // this.setData({
      //   iconList: iconListTmp,
      // })
      // if (userSysMessageCount != 0) {
      //   wx.setTabBarBadge({
      //     index: 1,
      //     text: '' + userMessageCount
      //   })
      // } else {
      //   wx.removeTabBarBadge({
      //     index: 1,
      //     success(res) {
      //       // console.log("移除Badge成功", res)
      //     },
      //     fail(res) {
      //       // console.log("移除Badge失败", res)
      //     }
      //   })
      // }
      let appId = 'wx65d148a2004f8ff0'

      if (!appId) {
        appId = 'wx65d148a2004f8ff0'
      }
      wx.navigateToMiniProgram({
        appId: appId,
        path: 'pages/index/index',
        extraData: {
          foo: 'bar'
        },
        // envVersion: 'develop',
        success(res) {
          // 打开成功
        }, fail(e) {
          if (e.errMsg != "navigateToMiniProgram:fail cancel") {
            wx.showToast({
              title: '商店打开出错。',
              icon: 'none'
            })
          }
        }
      })
      return
    }
    wx.navigateTo({
      url: that.data.iconList[e.currentTarget.dataset.index].url,
    })
  },
  getOnlinePeopleNumber() {
    var that = this
    let url = app.globalData.HOSTURL + '/bbs/bbsSys/onlinePeopleNumber'
    app.wxRequest('get', url, '').then(res => {
      if (res.data.code == 200) {
        console.log(res.data.result)
        that.setData({
          onlinePeopleNumber: res.data.result
        })
      } else {

      }
    }, err => {

    })
  },
  test() {
    wx.requestSubscribeMessage({
      tmplIds: ['CmA7jHsLU6N5lDzGbpK2vT6vNNhCdV8R4ysqdsFu38w'],
      success(res) {
        console.log(res)
      },
      fail(res) {
        console.log(res)
      }
    })
  },
  // /**
  //  * 用户点击右上角分享
  //  */
  onShareAppMessage: function () {
    return {
      title: "『" + this.data.USERRECORD.regionFullName + "』" + '都在用的本地小程序',
      path: '/pages/index/index?regionCode=' + this.data.USERRECORD.regionCode,
      // imageUrl:
    }
  },
  // 朋友圈
  onShareTimeline: function () {
    return {
      title: "『" + this.data.USERRECORD.regionFullName + "』" + '都在用的本地小程序',
      path: '/pages/index/index?regionCode=' + this.data.USERRECORD.regionCode,
      // 自定义图片路径，可以是本地文件或者网络图片。支持 PNG 及 JPG，显示图片长宽比是 1:1。	默认使用小程序 Logo
      // imageUrl:
    }
  },
  goUserInfo(e) {
    console.log(e)
    wx.navigateTo({
      url: '/pages/components/mine/userinfo/userinfo?username=' + e.currentTarget.dataset.username,
    })
  }
})