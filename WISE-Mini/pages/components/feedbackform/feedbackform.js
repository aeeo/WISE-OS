const app = getApp();
Page({
  data: {
    StatusBar: app.globalData.StatusBar,
    CustomBar: app.globalData.CustomBar,
    REGIONCLASS: wx.getStorageSync('REGIONCLASS'),
    userSeleceTopicRegionCode: '', //用户提交表单版块编号
    index: null,
    multiIndex: [0, 0, 0],
    time: '12:01',
    date: '2018-12-25',
    region: ['广东省', '广州市', '海珠区'],
    imgList: [],
    modalName: null,
    textareaAValue: '',
    textareaBValue: '',
    isLoad: true,
  },
  onLoad() {
  },
  PickerChange(e) {
    console.log(e);
    this.setData({
      index: e.detail.value
    })
  },
  MultiChange(e) {
    this.setData({
      multiIndex: e.detail.value
    })
  },
  MultiColumnChange(e) {
    let data = {
      multiArray: this.data.multiArray,
      multiIndex: this.data.multiIndex
    };
    data.multiIndex[e.detail.column] = e.detail.value;
    switch (e.detail.column) {
      case 0:
        switch (data.multiIndex[0]) {
          case 0:
            data.multiArray[1] = ['扁性动物', '线形动物', '环节动物', '软体动物', '节肢动物'];
            data.multiArray[2] = ['猪肉绦虫', '吸血虫'];
            break;
          case 1:
            data.multiArray[1] = ['鱼', '两栖动物', '爬行动物'];
            data.multiArray[2] = ['鲫鱼', '带鱼'];
            break;
        }
        data.multiIndex[1] = 0;
        data.multiIndex[2] = 0;
        break;
      case 1:
        switch (data.multiIndex[0]) {
          case 0:
            switch (data.multiIndex[1]) {
              case 0:
                data.multiArray[2] = ['猪肉绦虫', '吸血虫'];
                break;
              case 1:
                data.multiArray[2] = ['蛔虫'];
                break;
              case 2:
                data.multiArray[2] = ['蚂蚁', '蚂蟥'];
                break;
              case 3:
                data.multiArray[2] = ['河蚌', '蜗牛', '蛞蝓'];
                break;
              case 4:
                data.multiArray[2] = ['昆虫', '甲壳动物', '蛛形动物', '多足动物'];
                break;
            }
            break;
          case 1:
            switch (data.multiIndex[1]) {
              case 0:
                data.multiArray[2] = ['鲫鱼', '带鱼'];
                break;
              case 1:
                data.multiArray[2] = ['青蛙', '娃娃鱼'];
                break;
              case 2:
                data.multiArray[2] = ['蜥蜴', '龟', '壁虎'];
                break;
            }
            break;
        }
        data.multiIndex[2] = 0;
        break;
    }
    this.setData(data);
  },
  TimeChange(e) {
    this.setData({
      time: e.detail.value
    })
  },
  DateChange(e) {
    this.setData({
      date: e.detail.value
    })
  },
  RegionChange: function (e) {
    this.setData({
      region: e.detail.value
    })
  },
  /**
   * 选择图片同时上传
   */
  ChooseImage() {

    wx.chooseImage({
      count: 3, //默认9
      sizeType: ['original', 'compressed'], //可以指定是原图还是压缩图，默认二者都有
      sourceType: ['album'], //从相册选择
      success: (res) => {
        if (this.data.imgList.length != 0) {
          this.setData({
            imgList: this.data.imgList.concat(res.tempFilePaths)
          })
        } else {
          this.setData({
            imgList: res.tempFilePaths
          })
        }
      }
    });
  },
  ViewImage(e) {
    wx.previewImage({
      urls: this.data.imgList,
      current: e.currentTarget.dataset.url
    });
  },
  DelImg(e) {
    wx.showModal({
      title: '',
      content: '确定要删除这张照片吗？',
      cancelText: '取消',
      confirmText: '确认',
      success: res => {
        if (res.confirm) {
          this.data.imgList.splice(e.currentTarget.dataset.index, 1);
          this.setData({
            imgList: this.data.imgList
          })
        }
      }
    })
  },
  textareaAInput(e) {
    this.setData({
      textareaAValue: e.detail.value
    })
  },
  textareaBInput(e) {
    this.setData({
      textareaBValue: e.detail.value
    })
  },
  /**
   * 表单上传
   * @param  formData 
   */
  formSubmit: async function (formData) {
    var that = this
    var data = formData.detail.value
    console.log(formData.detail.value)
    var bbsTopicImageListForm = await this.uploadImage(this.data.imgList)
    var site
    for (var image in data.site) {
      site += image
      site += ","
    }
    if (data.content == '') {
      wx.showToast({
        title: '内容不能为空',
        icon:"none",
        duration: 2000
      })
    } else if (data.content.length < 4) {
      wx.showToast({
        title: '反馈内容过短',
        icon:"none",
        duration: 2000
      })
    } else if (data.content.length > 2048) {
      console.log(data.content.length)
      wx.showToast({
        title: '内容过长',
        icon:"none",
        duration: 2000
      })
    } else {
      var bbsTopicFullDto = {
        content: data.content,
        contact: data.contact,
        imageUrl: bbsTopicImageListForm,
      }
      this.setData({
        isLoad: false
      })
      wx.showLoading({
        title: '提交中',
      })
      app.wxRequest('post', app.globalData.HOSTURL + '/bbs/bbsFeedBack/wise/mini/add', bbsTopicFullDto).then(res => {
        if (res.data.code != 200) {
          wx.showToast({
            title: '提交失败' + err,
            icon: 'error',
            duration: 2000
          })
          this.setData({
            isLoad: true
          })
        } else {
          this.setData({
            isLoad: true
          })
          wx.showToast({
            title: '提交成功',
            icon: 'success',
            duration: 1000,
            success() {
              setTimeout(() => {
                let pages = getCurrentPages(); // 当前页，
                let prevPage = pages[pages.length - 2]; // 上一页
                prevPage.setData({
                  needReload: true,
                })
                wx.navigateBack({
                  delta: 1
                });
              }, 1000)
            }
          })
        }
      }, err => {
        wx.showToast({
          title: '提交失败' + err,
          icon: 'error',
          duration: 2000
        })
        this.setData({
          isLoad: true
        })
      })
    }
  },

  /**
   * 图片上传
   */
  async uploadImage(imgList) {
    var that = this
    var bbsTopicImageListForm = [];
    for (var tempFilePath in imgList) {
      await that.myUploadFile(imgList[tempFilePath], 'file').then(res => {
        bbsTopicImageListForm.push(res)
      })
    }
    return bbsTopicImageListForm
  },

  /**
   * 文件上传
   */
  myUploadFile(filePath, name) {
    return new Promise(function (resolve, reject) {
      //压缩图片
      wx.compressImage({
        src: filePath, // 图片路径
        quality: 30, // 压缩质量
        success(res) {
          wx.uploadFile({
            header: {
              'X-Access-Token': wx.getStorageSync('TOKEN')
            },
            url: app.globalData.HOSTURL + '/bbs/qiniuoss/upload',
            filePath: res.tempFilePath,
            name: name,
            formData: {
              'fileType': 'jfif'
            },
            success(res) {
              let topicImageId1 = JSON.parse(res.data).message
              var json = {}
              json.topicImage = topicImageId1
              resolve(json)
            },
            fail(err) {
              reject(err)
            }
          })
        }
      })
    })
  },

  PickerChange(e) {
    var that = this
    console.log(this.data.REGIONCLASS[e.detail.value].className)
    this.setData({
      index: e.detail.value,
      userSeleceTopicRegionCode: that.data.REGIONCLASS[e.detail.value].classCode
    })
  },
})