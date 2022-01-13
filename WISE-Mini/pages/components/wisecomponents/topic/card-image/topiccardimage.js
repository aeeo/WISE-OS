// pages/components/wisecomponents/topic/card-image/topiccardimage.js
const app = getApp();
Component({
  // 隔离级别
  options: {
    styleIsolation: 'apply-shared'
  },
  /**
   * 组件的属性列表
   */
  properties: {
    // 图片列表
    imageList: {
      type: Array,
      // value: [],
    },
  },
  observers: {
    'imageList'(val) {
      // console.log("属性值变化时执行", val)
    },
  },

  /**
   * 组件的初始数据
   */
  data: {
    UPLOAD_IMAGE: app.globalData.UPLOAD_IMAGE,
    THUMBNAIL: app.globalData.THUMBNAIL,
    ARTWORK: app.globalData.ARTWORK,
    ARTWORKNOWATER: app.globalData.ARTWORKNOWATER, //原图无水印
  },

  /**
   * 组件的方法列表
   */
  methods: {
    //点击图片放大预览    有水印
    clickImage(event) {
      var imageList = []
      for (var itemImage in event.currentTarget.dataset.imagelist) {
        imageList.push(this.data.UPLOAD_IMAGE + event.currentTarget.dataset.imagelist[itemImage].topicImage + app.globalData.ARTWORKNOWATER)
      }
      let currentImageUrl = this.data.UPLOAD_IMAGE + event.currentTarget.dataset.imagelistitem.topicImage + app.globalData.ARTWORKNOWATER
      // console.log(imageList, currentImageUrl)
      wx.previewImage({
        urls: imageList, //需要预览的图片http链接列表，注意是数组
        current: currentImageUrl, // 当前显示图片的http链接，默认是第一个
        success: function (res) {
          console.log("预览成功")
        },
        fail: function (res) {
          wx.showToast({
            title: '图片加载失败',
            icon: 'none'
          })
          console.log("预览失败")
        },
        complete: function (res) { },
      })
    },
    imageError: function(e) {
      // console.error(e)
      // console.error('image3发生error事件，携带值为', e.detail.errMsg)
      let imageListTemp = this.data.imageList
      // 找不到资源的图片不显示
      imageListTemp[e.target.dataset.index].hidden = true
      this.setData({
        imageList: imageListTemp
      })
    }
  }
})