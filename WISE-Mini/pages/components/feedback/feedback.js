const app = getApp();
const formatUtil = require('../../../utils/formatutil');
Page({
  data: {
    StatusBar: app.globalData.StatusBar,
    CustomBar: app.globalData.CustomBar,
    isCard: 'no-card',
    ColorList: app.globalData.ColorList,
    fullReplys: '',
    topicdetails: '',
    topicIndex: -1,
    InputBottom: 0,
    InputFocus: false,
    pid: 0,
    replyContentInput: '',
    isLoad: true,
    list: [{
      name: 'scale-down',
      color: 'orange'
    }],
    pageNo: 1,
    topicLists: [],
    // 是否需要刷新页面
    needReload: false,
    // 步骤
    // 1 审核中 评估 实现 完成
    // 2 已审核 审核不通过 实现 完成
    // 3 已审核 评估中 实现 完成
    // 4 已审核 评估完成 已驳回 完成
    // 5 已审核 评估完成 实现中 完成
    // 6 已审核 评估完成 已实现 已完成
    asd: 1,
    numList: [
      [{
        name: '审核中',
        style: 'text-pink',
        icon: 'roundclosefill',
        status: -1
      }, {
        name: '评估',
        status: -1
      }, {
        name: '实现',
        status: -1
      }, {
        name: '完成',
        status: -1
      },],
      [{
        name: '已审核',
        style: 'text-pink',
        status: -1
      }, {
        name: '审核不通过',
        style: 'text-pink',
        status: 1
      }, {
        name: '实现',
        status: -1
      }, {
        name: '完成',
        status: -1
      },],
      [{
        name: '已审核',
        style: 'text-pink',
        status: -1
      }, {
        name: '评估中',
        style: 'text-pink',
        status: -1
      }, {
        name: '实现',
        style: 'text-pink',
        status: -1
      }, {
        name: '完成',
        style: 'text-pink',
        status: -1
      },],
      [{
        name: '已审核',
        style: 'text-pink',
        status: -1
      }, {
        name: '评估完成',
        style: 'text-pink',
        status: -1
      }, {
        name: '已驳回',
        style: 'text-pink',
        status: 2
      }, {
        name: '完成',
        status: -1
      },],
      [{
        name: '已审核',
        style: 'text-pink',
        status: -1
      }, {
        name: '评估完成',
        style: 'text-pink',
        status: -1
      }, {
        name: '实现中',
        style: 'text-pink',
        status: -1
      }, {
        name: '完成',
        status: -1
      },],
      [{
        name: '已审核',
        style: 'text-pink',
        status: -1
      }, {
        name: '评估完成',
        style: 'text-pink',
        status: -1
      }, {
        name: '已实现',
        style: 'text-pink',
        status: -1
      }, {
        name: '已完成',
        style: 'text-pink',
        status: -1
      },],

    ],
    num: 0,
  },
  onLoad() {
    this.setData({
      UPLOAD_IMAGE: app.globalData.UPLOAD_IMAGE
    })
    var that = this
    that.waitTopicList()
  },
  onShow() {
    var that = this
    /**
     * 反馈成功后需要刷新当前页面
     */
    if (this.data.needReload) {
      this.data.topicLists = []
      that.setData({
        needReload: false
      })
      wx.showLoading({
        title: '正在刷新',
      })
      that.waitTopicList()
    }
  },
  isCard(e) {
    this.setData({
      isCard: e.detail.value
    })
  },
  onReachBottom() {
    this.setData({
      pageNo: this.data.pageNo + 1,
      onReachBottomLoading: false
    })
    this.waitTopicList()
  },
  onPullDownRefresh() {
    wx.stopPullDownRefresh()
  },

  //等待加载成功
  waitTopicList() {
    var that = this
    var index = 1
    that.getBbsTopicLists()
    var timer = setInterval(function () {
      if (index % 5 == 0) {
        that.getBbsTopicLists()
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
  //获取反馈列表
  getBbsTopicLists() {
    var that = this
    that.setData({
      getTopicFlag: false
    })
    let url = app.globalData.HOSTURL + '/bbs/bbsFeedBack/wise/mini/list?pageNo=' + this.data.pageNo
    app.wxRequest('get', url, '').then(res => {
      if (res.data.code == 200) {
        if (res.data.result.records.length > 0) {
          res.data.result.records.forEach((item) => {
            // item.userRole = item.userRole.substring(4)
            // 添加动画属性
            item.exeCuteAnimation = item.userIsPraise
            item.createTime = formatUtil.showDate(new Date(item.createTime))
            item.userAvatar = that.data.UPLOAD_IMAGE + item.userAvatar
            item.disposeUserAvatar = that.data.UPLOAD_IMAGE + item.disposeUserAvatar
            item.imageUrl.forEach((itemImage) => {
              itemImage.topicImage = that.data.UPLOAD_IMAGE + itemImage.topicImage
            })
          })
          //列表追加
          var tempList = that.data.topicLists
          var resTopicList = res.data.result.records
          for (const key in resTopicList) {
            tempList.push(resTopicList[key])
          }
          that.setData({
            topicLists: tempList,
            getTopicFlag: true,
            isFirstGetTopicFlag: false
          })
          if (res.data) {

          }
        } else {
          that.setData({
            pageNo: this.data.pageNo - 1,
            getTopicFlag: true
          })
        }
      } else {
        wx.showToast({
          title: '获取信息出错',
          icon: "none"
        })
      }
      // 隐藏加载提示
      wx.hideLoading()
    }, err => {
      that.setData({
        topicLists: res.data.result.records,
        getTopicFlag: false
      })
      wx.hideLoading()
    })
  },
  //点击topic图片放大预览
  clickTopicImage(event) {
    var imageList = []
    for (var itemImage in event.currentTarget.dataset.imagelist) {
      imageList.push(event.currentTarget.dataset.imagelist[itemImage].topicImage)
    }
    wx.previewImage({
      urls: imageList, //需要预览的图片http链接列表，注意是数组
      current: event.currentTarget.id, // 当前显示图片的http链接，默认是第一个
      success: function (res) { },
      fail: function (res) { },
      complete: function (res) { },
    })
  },
  toFeedBack() {
    wx.navigateTo({
      url: '/pages/components/feedbackform/feedbackform',
    })
  }
});