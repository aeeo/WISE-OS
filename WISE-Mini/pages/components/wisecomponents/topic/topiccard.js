const app = getApp();
Component({
  // 隔离级别
  options: {
    styleIsolation: 'apply-shared'
  },
  lifetimes: {
    attached: function () {
      // 在组件实例进入页面节点树时执行
      // console.log(this.data.topicLists)
    },
    detached: function () {
      // 在组件实例被从页面节点树移除时执行
    },
  },
  /**
   * 组件的属性列表
   * 属性的类型可以为 String Number Boolean Object Array 其一，也可以为null 表示不限制类型。
   */
  properties: {
    topicLists: {
      type: Array,
      value: [],
    },
    topicType: {
      type: String,
      value: "1"
    },
    showRole: {
      type: Boolean,
      value: false
    },
    // 展示底部浏览次数、点赞次数条
    showBottomBar: {
      type: Boolean,
      value: false
    },
    /**
     * 是否显示区域标签
     * true 是
     * false 否
     */
    showRegion: {
      type: Boolean,
      value: false
    },
    showMore: {
      type: Boolean,
      value: true
    }
  },
  observers: {
    'topicLists'(val) {
      // console.log("属性值变化时执行", val)
    },
    'showRegion'(val) {
      // console.log("属性值变化时执行", val)
    }
  },


  /**
   * 组件的初始数据
   */
  data: {
    UPLOAD_IMAGE: app.globalData.UPLOAD_IMAGE,
    THUMBNAIL: app.globalData.THUMBNAIL,
    ARTWORK: app.globalData.ARTWORK,
    ARTWORKNOWATER: app.globalData.ARTWORKNOWATER, //原图无水印

    showActionsheet: false,
    groups: [{
      text: '示例菜单',
      value: 1
    },
    {
      text: '示例菜单',
      value: 2
    },
    {
      text: '负向菜单',
      type: 'warn',
      value: 3
    }],
    ColorList: [{
      title: '嫣红',
      name: 'red',
      color: '#e54d42'
    },
    {
      title: '桔橙',
      name: 'orange',
      color: '#f37b1d'
    },
    {
      title: '明黄',
      name: 'yellow',
      color: '#fbbd08'
    },
    {
      title: '橄榄',
      name: 'olive',
      color: '#8dc63f'
    },
    {
      title: '森绿',
      name: 'green',
      color: '#39b54a'
    },
    {
      title: '天青',
      name: 'cyan',
      color: '#1cbbb4'
    },
    {
      title: '海蓝',
      name: 'blue',
      color: '#0081ff'
    },
    {
      title: '姹紫',
      name: 'purple',
      color: '#6739b6'
    },
    {
      title: '木槿',
      name: 'mauve',
      color: '#9c26b0'
    },
    {
      title: '桃粉',
      name: 'pink',
      color: '#e03997'
    },
    {
      title: '棕褐',
      name: 'brown',
      color: '#a5673f'
    },
    {
      title: '玄灰',
      name: 'grey',
      color: '#8799a3'
    },
    {
      title: '草灰',
      name: 'gray',
      color: '#aaaaaa'
    },
    {
      title: '墨黑',
      name: 'black',
      color: '#333333'
    },]
  },

  /**
   * 组件的方法列表
   */
  methods: {
    //点赞
    clickPraise(e) {
      var that = this
      // 查询用户是否授权
      if (app.globalData.HASUSERINFO) {
        // console.log(that.data.currentClass)
        // console.log(that.data.topicLists)
        //若果没有点赞  点赞并展示动画
        if (!that.data.topicLists[e.target.dataset.bindex].userIsPraise) {
          var topicListTem = that.data.topicLists
          topicListTem[e.target.dataset.bindex].userIsPraise = true
          topicListTem[e.target.dataset.bindex].exeCuteAnimation = true
          topicListTem[e.target.dataset.bindex].praiseCount = topicListTem[e.target.dataset.bindex].praiseCount + 1
          that.setData({
            topicLists: topicListTem,
          })
          let url = app.globalData.HOSTURL + '/bbs/bbsUserPraise/wise/mini/clickPraise?topicId=' + e.target.id + '&isPraise=' + true
          app.wxRequest('post', url, '').then(res => {

          }, err => {

          })
        } else {
          var topicListTem = that.data.topicLists
          topicListTem[e.target.dataset.bindex].userIsPraise = false
          topicListTem[e.target.dataset.bindex].exeCuteAnimation = false
          topicListTem[e.target.dataset.bindex].praiseCount = topicListTem[e.target.dataset.bindex].praiseCount - 1
          that.setData({
            topicLists: topicListTem,
          })
          let url = app.globalData.HOSTURL + '/bbs/bbsUserPraise/wise/mini/clickPraise?topicId=' + e.target.id + '&isPraise=' + false
          app.wxRequest('post', url, '').then(res => {

          }, err => {
            wx.showToast({
              title: '失败，请稍后再试。',
              icon: 'none'
            })
          })
        }
      } else {
        app.getUserProfile()
      }
    },

    // 卡片跳转
    topicdetails: function (event) {
      console.log(event)
      // 点击贴子直接本地缓存浏览量+1
      var topicListTem = this.data.topicLists
      event.currentTarget.dataset.topicitem.hitsCount = event.currentTarget.dataset.topicitem.hitsCount + 1
      topicListTem.hitsCount = topicListTem.hitsCount + 1

      this.data.topicLists = topicListTem
      for (const item in event.currentTarget.dataset.topicitem.bbsTopicImageList) {
        event.currentTarget.dataset.topicitem.bbsTopicImageList[item] = this.data.UPLOAD_IMAGE + event.currentTarget.dataset.topicitem.bbsTopicImageList[item]
      }
      var topicId = event.currentTarget.dataset.topicitem.id
      //点击贴子更新相关数据
      let url = app.globalData.HOSTURL + '/bbs/bbsTopic/wise/mini/userClickTopic?topicId=' + topicId
      app.wxRequest('get', url)
      console.log(event.currentTarget.dataset)
      wx.navigateTo({
        url: '/pages/components/topic/topicdetails/topicdetails',
        // url: '../components/topic/topicdetails/topicdetails',
        success: function (res) {
          // 通过eventChannel向被打开页面传送数据
          res.eventChannel.emit('acceptDataFromOpenerPage', {
            data: event.currentTarget.dataset
          })
        }
      })
    },
    clickMore(e) {
      var myEventDetail = {} // detail对象，提供给事件监听函数
      myEventDetail.topicIndex = e.currentTarget.dataset.topicindex
      myEventDetail.type = "more"
      var myEventOption = {} // 触发事件的选项
      this.triggerEvent('myevent', myEventDetail, myEventOption)
    },
    clickReply(e) {
      var myEventDetail = {} // detail对象，提供给事件监听函数
      myEventDetail.topicIndex = e.currentTarget.dataset.topicindex
      myEventDetail.topicDetails = e.currentTarget.dataset.topicdetails
      myEventDetail.type = "reply"
      var myEventOption = {} // 触发事件的选项
      this.triggerEvent('myevent', myEventDetail, myEventOption)
    },
    goUserInfo(e) {
      console.log(e)
      wx.navigateTo({
        url: '/pages/components/mine/userinfo/userinfo?username=' + e.currentTarget.dataset.username,
      })
    },
    goTopicLink(e){
      console.log(e)
      wx.navigateTo({
        url: '/pages/components/mine/userinfo/userinfo?username=' + e.currentTarget.dataset.username,
      })
    }
  },
})