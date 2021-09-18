const app = getApp();
const qiniuUploader = require("../../../utils/qiniuUploader");
// 初始化七牛云相关配置
function initQiniu() {
  const url = app.globalData.HOSTURL + "/bbs/qiniuoss/getUptoken"
  const bucket = app.globalData.UPLOAD_IMAGE.substring(0, app.globalData.UPLOAD_IMAGE.length - 1)
  var options = {
    // bucket所在区域，这里是华北区。ECN, SCN, NCN, NA, ASG，分别对应七牛云的：华东，华南，华北，北美，新加坡 5 个区域
    region: 'SCN',
    // 获取uptoken方法三选一即可，执行优先级为：uptoken > uptokenURL > uptokenFunc。三选一，剩下两个置空。推荐使用uptokenURL，详情请见 README.md
    // 由其他程序生成七牛云uptoken，然后直接写入uptoken
    // uptoken: 'x2gNTA54jqKvDKNnqn_AzhISdnumt184yDsGZwXw:YxLYfCXM1lbSNTEsQKm7FqI0DE0=:eyJzY29wZSI6Indpc2VoZWFyIiwiZGVhZGxpbmUiOjE2MTUzMDQ0Mjd9',
    // 从指定 url 通过 HTTP GET 获取 uptoken，返回的格式必须是 json 且包含 uptoken 字段，例如： {"uptoken": "0MLvWPnyy..."}
    uptokenURL: url,
    // uptokenFunc 这个属性的值可以是一个用来生成uptoken的函数，详情请见 README.md
    // uptokenFunc: function () {
    //   // do something
    //   return qiniuUploadToken;
    // },
    // bucket 外链域名，下载资源时用到。如果设置，会在 success callback 的 res 参数加上可以直接使用的 fileURL 字段。否则需要自己拼接
    domain: bucket,
    // qiniuShouldUseQiniuFileName 如果是 true，则文件的 key 由 qiniu 服务器分配（全局去重）。如果是 false，则文件的 key 使用微信自动生成的 filename。出于初代sdk用户升级后兼容问题的考虑，默认是 false。
    // 微信自动生成的 filename较长，导致fileURL较长。推荐使用{qiniuShouldUseQiniuFileName: true} + "通过fileURL下载文件时，自定义下载名" 的组合方式。
    // 自定义上传key 需要两个条件：1. 此处shouldUseQiniuFileName值为false。 2. 通过修改qiniuUploader.upload方法传入的options参数，可以进行自定义key。（请不要直接在sdk中修改options参数，修改方法请见demo的index.js）
    // 通过fileURL下载文件时，文件自定义下载名，请参考：七牛云“对象存储 > 产品手册 > 下载资源 > 下载设置 > 自定义资源下载名”（https://developer.qiniu.com/kodo/manual/1659/download-setting）。本sdk在README.md的"常见问题"板块中，有"通过fileURL下载文件时，自定义下载名"使用样例。
    shouldUseQiniuFileName: false,
  };
  // 将七牛云相关配置初始化进本sdk

  qiniuUploader.init(options);
}
Page({
  data: {
    StatusBar: app.globalData.StatusBar,
    CustomBar: app.globalData.CustomBar,
    REGIONCLASS: wx.getStorageSync('REGIONCLASS'),
    topicClassList: [],
    userSeleceTopicClassIndex: null,
    userSeleceTopicClassCode: '', //用户提交表单版块编号
    multiIndex: [0, 0, 0],
    time: '12:01',
    date: '2018-12-25',
    region: ['陕西省', '西安市', '雁塔区'],
    formData: {}, //表单数据
    imgList: [],
    modalName: null,
    textareaAValue: '',
    textareaBValue: '',
    isLoad: true,
    UPLOAD_IMAGE: app.globalData.UPLOAD_IMAGE,
    THUMBNAIL: app.globalData.THUMBNAIL,
    isAnon:false,       //是否匿名

    imageReturnList: [],
    // 图片上传（从相册）返回对象。上传完成后，此属性被赋值
    imageObject: {},
    // 文件上传（从客户端会话）返回对象。上传完成后，此属性被赋值
    messageFileObject: {},
    // 图片上传（从相册）进度对象。开始上传后，此属性被赋值
    imageProgress: {},
    // 文件上传（从客户端会话）进度对象。开始上传后，此属性被赋值
    messageFileProgress: {},
    // 文件在线查看来源fileUrl
    viewFileOnlineFileUrl: '',
    // 文件下载进度对象。用于文件在线查看前的预下载
    downloadFileProgress: {},
    // 此属性在qiniuUploader.upload()中被赋值，用于中断上传
    cancelTask: function () { },
  },
  onLoad(option) {
    var that = this
    console.log(option)
    // 草稿&编辑处理
    that.saveOrEditDeal(option)

    // 版块处理
    this.setData({
      REGIONCLASS: wx.getStorageSync('REGIONCLASS')
    })
    var topicCodeListsTmp = []
    for (var item in this.data.REGIONCLASS) {
      topicCodeListsTmp.push(this.data.REGIONCLASS[item].className)
    }
    this.setData({
      topicClassList: topicCodeListsTmp
    })
  },
  // 草稿&编辑处理
  saveOrEditDeal(option) {
    var that = this
    if (option.hasOwnProperty("isEdit")) {
      console.log("编辑")
      // 监听acceptDataFromOpenerPage事件，获取上一页面通过eventChannel传送到当前页面的数据
      const eventChannel = that.getOpenerEventChannel()
      eventChannel.on('editEventChannel', function (res) {
        console.log(res.data)
        that.setData({
          isEdit: true
        })
        // 编辑加载
        that.editFormLoad(res.data)
      })
    } else {
      that.data.isEdit = false
      // 草稿加载
      that.saveFormLoad()
    }
  },
  // 草稿加载，如果不是编辑，就是加载草稿，哪怕没有草稿，也要给老子造一个草稿
  saveFormLoad() {
    let topicFormData = wx.getStorageSync('topicFormData')
    if (undefined == topicFormData || "" == topicFormData) {
      topicFormData = {
        "id": "",
        "imageType": 1, //0：临时路径，1：OSS路径
        "title": "",
        "topicClass": null,
        "contact": "",
        "content": "",
        "imgList": []
      }
    }
    this.setData({
      formData: topicFormData,
      imgList: topicFormData.imgList
    })
  },
  // 编辑加载
  editFormLoad(formData) {
    var that = this
    console.log(formData)
    let formDataTemp = {}
    formDataTemp.id = formData.id
    formDataTemp.title = formData.title
    let REGIONCLASS = wx.getStorageSync('REGIONCLASS')
    console.log(REGIONCLASS)
    for (const key in REGIONCLASS) {
      if (REGIONCLASS[key].classCode == formData.classCode) {
        formDataTemp.topicClass = key
      }
    }
    formDataTemp.contact = formData.contact
    formDataTemp.content = formData.content
    let imgList = []
    formData.bbsTopicImageList.forEach(item => {
      var imageListTem = {}
      imageListTem.imageTempUrl = "" //本地临时路径
      imageListTem.imageUrl = item.topicImage //OSS路径
      imageListTem.percent = 100
      imageListTem.imageType = 1 //0：临时路径，1：OSS路径
      imgList.push(imageListTem)
    })
    formDataTemp.imgList = imgList
    console.log(formDataTemp)
    that.setData({
      formData: formDataTemp,
      userSeleceTopicClassIndex: formDataTemp.topicClass,    //给版块id赋值，如果直接用formdata显示有问题
      userSeleceTopicClassCode: that.data.topicClassList[formDataTemp.topicClass],    //给版块id赋值，如果直接用formdata显示有问题
      imgList: imgList
    })
  },
  // 图片上传（从相册）方法
  didPressChooesImage: function () {
    var that = this;
    didPressChooesImage(that);
  },
  // 文件上传（从客户端会话）方法，支持图片、视频、其余文件 (PDF(.pdf), Word(.doc/.docx), Excel(.xls/.xlsx), PowerPoint(.ppt/.pptx)等文件格式)
  didPressChooesMessageFile: function () {
    var that = this;
    didPressChooesMessageFile(that);
  },
  // 在线查看文件，支持的文件格式：pdf, doc, docx, xls, xlsx, ppt, pptx。关于wx.openDocument方法，详情请参考微信官方文档：https://developers.weixin.qq.com/miniprogram/dev/api/file/wx.openDocument.html
  didPressViewFileOnline: function () {
    var that = this;
    didPressViewFileOnline(that);
  },
  // 中断上传方法
  didCancelTask: function () {
    this.data.cancelTask();
  },
  MultiChange(e) {
    this.setData({
      multiIndex: e.detail.value
    })
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
  ViewImage(e) {
    console.log(e)
    var that = this
    let imageList = []
    that.data.imgList.forEach(item => {
      if (item.imageType == 0) {
        imageList.push(item.imageTempUrl)
      } else if (item.imageType == 1) {
        imageList.push(that.data.UPLOAD_IMAGE + item.imageUrl + that.data.THUMBNAIL)
      }
    })
    wx.previewImage({
      urls: imageList,
      current: that.data.UPLOAD_IMAGE + e.currentTarget.dataset.url + that.data.THUMBNAIL
    });
  },
  DelImg(e) {
    var that = this
    wx.showModal({
      title: '',
      content: '确定要删除这图片吗？',
      cancelText: '取消',
      confirmText: '确认',
      success: res => {
        if (res.confirm) {
          // 删除七牛云图片
          // 存在问题，微信小程序无法监听页面返回按钮事件，因此无论怎样设置逻辑总有部分图片是无用的，只能去定时清理，后台数据库不保存没有引用的图片
          // 除非是做成点击发布再上传图片，但是体验不够好
          // that.deleteQiNiuOSS(that.data.imgList[e.currentTarget.dataset.index].imageUrl)

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
  // mark: 是否匿名
  switchAnon(e){
    this.data.isAnon = !this.data.isAnon
  },
  /**
   * 表单上传
   * @param  formData 
   */
  formSubmit: async function (formData) {
    var data = formData.detail.value
    console.log(data)

    var that = this
    let actionType = formData.detail.target.dataset.type
    // mark:新增发布
    if (actionType == 1) {
      // 保存表单
      let imageListTem = that.data.imgList
      imageListTem.forEach(item => {
        item.imageType = 1 //0：临时路径，1：OSS路径
      });
      data.imgList = imageListTem
      wx.setStorage({
        data: data,
        key: 'topicFormData',
        success: function () {
          wx.showToast({
            title: '保存成功。',
            icon: 'success'
          })
        },
        fail: function () {
          wx.showToast({
            title: '保存失败。',
            icon: 'none'
          })
        }
      })
    } else if (actionType == 0) {
      var uploadFinished = true
      that.data.imgList.forEach((item) => {
        if (item.percent != 100) {
          uploadFinished = false
        }
      })
      if (!uploadFinished) {
        wx.showToast({
          title: '请等待图片上传完成...',
          icon: 'none'
        })
        return
      }
      var bbsTopicImageListForm = []
      that.data.imgList.forEach(item => {
        let imageTemp = {
          "topicImage": item.imageUrl
        }
        bbsTopicImageListForm.push(imageTemp)
      })
      var site
      for (var image in data.site) {
        site += image
        site += ","
      }
      if (data.content == '') {
        wx.showToast({
          title: '内容不能为空',
          icon: 'none',
          duration: 2000
        })
      } else if (data.content.length == 0) {
        wx.showToast({
          title: '内容不能为空',
          icon: 'none',
          duration: 2000
        })
      } else if (data.content.length > 2048) {
        console.log(data.content.length)
        wx.showToast({
          title: '内容过长',
          duration: 2000
        })
      } else {
        var bbsTopicFullDto = {
          title: data.title,
          content: data.content,
          contact: data.contact,
          site: site,
          bbsTopicImageList: bbsTopicImageListForm,
          praiseCount: 0,
          replyCount: 0,
          hitsCount: 0,
          classCode: that.data.userSeleceTopicClassCode == "" ? "index" : that.data.userSeleceTopicClassCode,
          regionCode: wx.getStorageSync('USERRECORD').regionCode,
          anon: that.data.isAnon ? 1 : 0      //1:匿名
        }
        this.setData({
          isLoad: false
        })
        wx.showLoading({
          title: '发布中',
          mask: true
        })
        app.wxRequest('post', app.globalData.HOSTURL + '/bbs/bbsTopic/wise/mini/add', bbsTopicFullDto).then(res => {
          if (res.data.code != 200) {
            wx.showToast({
              title: res.data.message,
              icon: 'none',
              duration: 2000
            })
            this.setData({
              isLoad: true
            })
          } else {
            // 清空草稿
            wx.removeStorage({
              key: 'topicFormData'
            })
            this.setData({
              isLoad: true
            })
            app.globalData.needReloadTopicList = true
            // 刷新用户限制数据
            app.userBehaviorLimit()
            wx.showToast({
              title: '发布成功',
              icon: 'success',
              duration: 1000,
              success() {
                setTimeout(() => {
                  app.globalData.needReloadTopicLis = true
                  wx.switchTab({
                    url: '/pages/index/index',
                  })
                }, 1000)
              }
            })
          }
        }, err => {

          this.setData({
            isLoad: true
          })
        })
      }
    } else if (actionType == 2) {
      // mark:编辑发布
      // 编辑，包括可能新增或修改信息
      var uploadFinished = true
      that.data.imgList.forEach((item) => {
        if (item.percent != 100) {
          uploadFinished = false
        }
      })
      if (!uploadFinished) {
        wx.showToast({
          title: '请等待图片上传完成...',
          icon: 'none'
        })
        return
      }
      var bbsTopicImageListForm = []
      that.data.imgList.forEach(item => {
        let imageTemp = {
          "topicImage": item.imageUrl
        }
        bbsTopicImageListForm.push(imageTemp)
      })
      var site
      for (var image in data.site) {
        site += image
        site += ","
      }
      if (data.content == '') {
        wx.showToast({
          title: '内容不能为空',
          icon: 'none',
          duration: 2000
        })
      } else if (data.content.length == 0) {
        wx.showToast({
          title: '内容不能为空',
          icon: 'none',
          duration: 2000
        })
      } else if (data.content.length > 2048) {
        console.log(data.content.length)
        wx.showToast({
          title: '内容过长',
          duration: 2000
        })
      } else {
        // 编辑要有id
        console.log(that.data.formData)
        var bbsTopicFullDto = {
          id: that.data.formData.id,
          title: data.title,
          content: data.content,
          contact: data.contact,
          site: site,
          bbsTopicImageList: bbsTopicImageListForm,
          classCode: that.data.userSeleceTopicClassCode,
          regionCode: wx.getStorageSync('USERRECORD').regionCode
        }
        this.setData({
          isLoad: false
        })
        wx.showLoading({
          title: '更新中',
          mask: true
        })
        app.wxRequest('post', app.globalData.HOSTURL + '/bbs/bbsTopic/wise/mini/edit', bbsTopicFullDto).then(res => {
          if (res.data.code != 200) {
            wx.showToast({
              title: res.data.message,
              icon: 'none',
              duration: 2000
            })
            this.setData({
              isLoad: true
            })
          } else {
            // 清空草稿
            wx.removeStorage({
              key: 'topicFormData'
            })
            this.setData({
              isLoad: true
            })
            app.globalData.needReloadTopicList = true
            // 刷新用户限制数据
            app.userBehaviorLimit()
            wx.showToast({
              title: '更新成功',
              icon: 'success',
              duration: 1000,
              success() {
                setTimeout(() => {
                  app.globalData.needReloadTopicLis = true
                  wx.switchTab({
                    url: '/pages/index/index',
                  })
                }, 1000)
              }
            })
          }
        }, err => {
          wx.showToast({
            title: '更新失败' + err,
            icon: 'error',
            duration: 2000
          })
          this.setData({
            isLoad: true
          })
        })
      }
    }
  },
  /**
   * 图片上传
   */
  // async uploadImage(imgList) {
  //   var that = this
  //   var bbsTopicImageListForm = [];
  //   for (var tempFilePath in imgList) {
  //     await that.myUploadFile(imgList[tempFilePath], 'file').then(res => {
  //       bbsTopicImageListForm.push(res)
  //     })
  //   }
  //   return bbsTopicImageListForm
  // },

  /**
   * 文件上传
   */
  // myUploadFile(filePath, name) {
  //   return new Promise(function (resolve, reject) {
  //     //压缩图片
  //     wx.compressImage({
  //       src: filePath, // 图片路径
  //       quality: 30, // 压缩质量
  //       success(res) {
  //         wx.uploadFile({
  //           header: {
  //             'X-Access-Token': wx.getStorageSync('TOKEN')
  //           },
  //           url: app.globalData.HOSTURL + '/bbs/qiniuoss/upload',
  //           filePath: res.tempFilePath,
  //           name: name,
  //           formData: {
  //             'fileType': 'jfif'
  //           },
  //           success(res) {
  //             let topicImageId1 = JSON.parse(res.data).message
  //             var json = {}
  //             json.topicImage = topicImageId1
  //             resolve(json)
  //           },
  //           fail(err) {
  //             reject(err)
  //           }
  //         })
  //       }
  //     })
  //   })
  // },

  PickerChange(e) {
    var that = this
    console.log(that.data.REGIONCLASS[e.detail.value].className)
    let formData = that.data.formData
    formData.topicClass = e.detail.value
    that.setData({
      formdata: formData,
      userSeleceTopicClassIndex: e.detail.value,
      userSeleceTopicClassCode: that.data.REGIONCLASS[e.detail.value].classCode
    })
  },
})

// mark: didPressChooesImage图片上传（从相册）方法
// 图片上传（从相册）方法
function didPressChooesImage(that) {
  // 初始化七牛云配置
  initQiniu();
  // 置空messageFileObject，否则在第二次上传过程中，wxml界面会存留上次上传的信息
  that.setData({
    'imageObject': {},
    'imageProgress': {}
  });
  // 微信 API 选择图片（从相册）
  wx.chooseImage({
    sizeType: ['compressed'], //可以指定是原图还是压缩图，默认二者都有
    // 最多可以选择的图片张数。
    count: 9,
    success: function (res) {
      console.log(res)
      let imageListTem = that.data.imgList
      res.tempFilePaths.forEach(item => {
        var imageListTemTemp = {}
        imageListTemTemp.imageTempUrl = item //本地临时路径
        imageListTemTemp.imageUrl = "" //OSS路径
        imageListTemTemp.percent = 0
        imageListTemTemp.imageType = 0 //0：临时路径，1：OSS路径

        imageListTem.push(imageListTemTemp)
        that.setData({
          imgList: imageListTem
        })
        // 开始上传   this对象，上传url
        wiseUpload(that, item)
      });
    }
  })
}
// mark:选择后上传
function wiseUpload(that, filePath) {
  console.log(filePath)
  // 生成文件名
  let fileName = new Date().getTime() + "_" + wx.getStorageSync('USERRECORD').createBy
  // wx.chooseImage 目前微信官方尚未开放获取原图片名功能(2020.4.22)
  // 向七牛云上传
  qiniuUploader.upload(filePath, (res) => {
    that.setData({
      'imageObject': res
    });
    // console.log(res)
    // var jsonO = {}
    // jsonO.topicImage = res.key
    // that.data.imageReturnList.push(jsonO)
    // console.log('提示: wx.chooseImage 目前微信官方尚未开放获取原图片名功能(2020.4.22)');
    // console.log('file url is: ' + res.fileURL);
  }, (error) => {
    console.error('error: ' + JSON.stringify(error));
  },
    // 此项为qiniuUploader.upload的第四个参数options。若想在单个方法中变更七牛云相关配置，可以使用上述参数。如果不需要在单个方法中变更七牛云相关配置，则可使用 null 作为参数占位符。推荐填写initQiniu()中的七牛云相关参数，然后此处使用null做占位符。
    // 若想自定义上传key，请把自定义key写入此处options的key值。如果在使用自定义key后，其它七牛云配置参数想维持全局配置，请把此处options除key以外的属性值置空。
    // 启用options参数请记得删除null占位符
    // {
    //   region: 'NCN', // 华北区
    //   uptokenURL: 'https://[yourserver.com]/api/uptoken',
    //   domain: 'http://[yourBucketId].bkt.clouddn.com',
    //   shouldUseQiniuFileName: false,
    //   key: 'testKeyNameLSAKDKASJDHKAS',
    //   uptokenURL: 'myServer.com/api/uptoken'
    // },
    {
      region: 'SCN',
      key: "userdata/" + fileName,
    },
    (progress) => {
      // that.setData({
      //   'imageProgress': progress
      // });
      var imgListTem = that.data.imgList
      imgListTem.forEach(item => {
        if (item.imageTempUrl == filePath) {
          item.percent = progress.progress // 实时显示上传进度
          item.imageUrl = "userdata/" + fileName
        }
      })
      that.setData({
        imgList: imgListTem
      })
      // console.log('上传进度', progress.progress);
      // console.log('已经上传的数据长度', progress.totalBytesSent);
      // console.log('预期需要上传的数据总长度', progress.totalBytesExpectedToSend);
    }, cancelTask => that.setData({
      cancelTask
    })
  );
 
}

// 文件上传（从客户端会话）方法，支持图片、视频、其余文件 (PDF(.pdf), Word(.doc/.docx), Excel(.xls/.xlsx), PowerPoint(.ppt/.pptx)等文件格式)
function didPressChooesMessageFile(that) {
  // 初始化七牛云相关参数
  initQiniu();
  // 置空messageFileObject和messageFileProgress，否则在第二次上传过程中，wxml界面会存留上次上传的信息
  that.setData({
    'messageFileObject': {},
    'messageFileProgress': {}
  });
  // 微信 API 选择文件（从客户端会话）
  wx.chooseMessageFile({
    // 最多可以选择的文件个数。目前本sdk只支持单文件上传，若选择多文件，只会上传第一个文件
    count: 1,
    // type: 所选的文件的类型
    // type的值: {all: 从所有文件选择}, {video: 只能选择视频文件}, {image: 只能选择图片文件}, {file: 只能选择除了图片和视频之外的其它的文件}
    type: 'all',
    success: function (res) {
      var filePath = res.tempFiles[0].path;
      var fileName = res.tempFiles[0].name;
      // 向七牛云上传
      qiniuUploader.upload(filePath, (res) => {
        res.fileName = fileName;
        that.setData({
          'messageFileObject': res
        });
        console.log('file name is: ' + fileName);
        console.log('file url is: ' + res.fileURL);
      }, (error) => {
        console.error('error: ' + JSON.stringify(error));
      },
        // 此项为qiniuUploader.upload的第四个参数options。若想在单个方法中变更七牛云相关配置，可以使用上述参数。如果不需要在单个方法中变更七牛云相关配置，则可使用 null 作为参数占位符。推荐填写initQiniu()中的七牛云相关参数，然后此处使用null做占位符。
        // 若想自定义上传key，请把自定义key写入此处options的key值。如果在使用自定义key后，其它七牛云配置参数想维持全局配置，请把此处options除key以外的属性值置空。
        // 启用options参数请记得删除null占位符
        // {
        //   region: 'NCN', // 华北区
        //   uptokenURL: 'https://[yourserver.com]/api/uptoken',
        //   domain: 'http://[yourBucketId].bkt.clouddn.com',
        //   shouldUseQiniuFileName: false,
        //   key: 'testKeyNameLSAKDKASJDHKAS',
        //   uptokenURL: 'myServer.com/api/uptoken'
        // },
        null,
        (progress) => {
          that.setData({
            'messageFileProgress': progress
          });
          console.log('上传进度', progress.progress);
          console.log('已经上传的数据长度', progress.totalBytesSent);
          console.log('预期需要上传的数据总长度', progress.totalBytesExpectedToSend);
        }, cancelTask => that.setData({
          cancelTask
        })
      );
    }
  })
}

// 在线查看文件的fileUrl输入框，输入完毕后点击确认
function didPressViewFileOnlineInputConfirm(that, event) {
  console.log(event.detail.value);
  that.setData({
    'viewFileOnlineFileUrl': event.detail.value
  });
  console.log(that.data.viewFileOnlineFileUrl);
}

// 在线查看文件，支持的文件格式：pdf, doc, docx, xls, xlsx, ppt, pptx。关于wx.openDocument方法，详情请参考微信官方文档：https://developers.weixin.qq.com/miniprogram/dev/api/file/wx.openDocument.html
function didPressViewFileOnline(that) {
  const downloadTask = wx.downloadFile({
    // url: 'http://[yourBucketId].bkt.clouddn.com/FumUUOIIj...',
    url: that.data.viewFileOnlineFileUrl,
    success: function (res) {
      console.log(res);
      var filePath = res.tempFilePath;
      wx.openDocument({
        filePath: filePath,
        success: function (res) {
          console.log('打开文档成功');
        },
        fail: function (res) {
          console.log(res);
        }
      });
    },
    fail: function (res) {
      console.log(res);
    }
  });
  downloadTask.onProgressUpdate((res) => {
    that.setData({
      'downloadFileProgress': res
    });
    console.log('下载进度', res.progress);
    console.log('已经下载的数据长度', res.totalBytesWritten);
    console.log('预期需要下载的数据总长度', res.totalBytesExpectedToWrite);
  });
}