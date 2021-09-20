const app = getApp();
Page({
  data: {
    siteModalName: -1,
    scrollLeft: 0,
    latitude: 34.813803,
    longitude: 106.259766,
    scale: 13,
    showscale: true, //显示比例尺
    markers: [],
    showMarkers: [],
    circle: [],
    showCircle: [],
    regionCount: 20, //每月切换region次数
    regionList: [],
    userRecord: '',
    currentRegion: [
      ['陕西省', '浙江省'],
      ['西安市', '铜川市', '宝鸡市', '咸阳市', '渭南市', '延安市', '汉中市', '榆林市', '安康市', '商洛市'],
    ],
    provinceCityList: [
      ['西安市', '铜川市', '宝鸡市', '咸阳市', '渭南市', '延安市', '汉中市', '榆林市', '安康市', '商洛市'],
      ['杭州市', '宁波市', '温州市']
    ],
    currentRegionIndex: [0, 0],

  },

  //mark: onLoad
  onLoad: function () {
    // this.mapCtx.moveToLocation()
    var that = this
    this.mapCtx = wx.createMapContext('myMap')

    // 获取当前屏幕对角线经纬度，初始化获取屏幕范围
    that.mapCtx.getRegion({
      success: function (res) {
        // console.log("屏幕范围：", res)
        that.data.northeast = res.northeast //东北  右上
        that.data.southwest = res.southwest //西南  左下
        that.getRegionList()
        that.getUserAllInfo()
      }
    })
    this.bindEvent() //  聚合簇
    this.initUserCenterLocation() //初始化用户位置



    // test 最大值
    // let pointArray = [
    //   [0, 0],
    //   [0, 1],
    //   [1, 1],
    //   [1, 0]
    // ]
    // this.maxDistance(pointArray)
  },
  //mark: 初始化用户位置
  initUserCenterLocation() {
    var that = this
    wx.getLocation({
      type: 'gcj02', //返回可以用于wx.openLocation的经纬度
      success(res) {
        const latitude = res.latitude
        const longitude = res.longitude
        that.setData({
          latitude,
          longitude,
        })
      }
    })
  },
  // markerClusterCreate(res) {
  //   console.log(res)
  // },
  //mark: onReady
  onReady: function (e) { },
  // 抽屉开始
  showModal(e) {
    var that = this
    if ("viewModal" == e.currentTarget.dataset.module) {
      that.setData({
        modalName: e.currentTarget.dataset.module
      })
    } else if ("switchSiteModal" == e.currentTarget.dataset.module) {
      wx.showModal({
        cancelColor: 'cancelColor',
        title: "切换区域",
        content: "本月剩余切换次数：" + (that.data.regionCount - wx.getStorageSync('ALLINFO').bbsUserRecord.regionSwitchCount) + "，请确认切换到：" + that.data.markers[e.detail.markerId - 1].name,
        success(res) {
          if (res.confirm) {
            /**
             * 用户点击确认，切换地区
             * 判断是否有剩余切换次数
             * 是：判断是否在当前区域
             *    用户记录regionSwitchCount+1，region_switch_date当前月，region_code当前区域编码，region_fullname当前区域名
             *    跳转到首页
             * 否：提示用户切换次数已达上限，无法切换
             */
            if (that.data.regionCount - wx.getStorageSync('ALLINFO').bbsUserRecord.regionSwitchCount > 0) {
              if (wx.getStorageSync('ALLINFO').bbsUserRecord.regionCode == that.data.regionList[e.detail.markerId - 1].regionCode) {
                wx.showToast({
                  title: "已经在" + that.data.markers[e.detail.markerId - 1].name + "，无需切换",
                  icon: "none"
                })
                return
              } else {
                that.switchRegion(that.data.regionList[e.detail.markerId - 1])
              }
            } else {
              wx.showToast({
                title: '次数已达上限，无法切换',
                icon: "none"
              })
            }
          } else if (res.cancel) {
            // console.log('用户点击取消')
          }
        }
      })
      // that.setData({
      //   switchSiteModal: e.currentTarget.dataset.module,
      //   switchSiteModalMarker: e.detail.markerId - 1,
      //   switchSiteModalMarkerName: this.data.markers[e.detail.markerId - 1].name,
      // })
    }
  },
  //mark: 隐藏弹框
  hideModal(e) {
    this.setData({
      modalName: null,
      switchSiteModal: null
    })
  },
  //mark: 列表选择区域
  selectSite(e) {
    this.hideModal()
    console.log(this.data.markers[e.currentTarget.dataset.bindex])
    this.setData({
      latitude: this.data.markers[e.currentTarget.dataset.bindex].latitude,
      longitude: this.data.markers[e.currentTarget.dataset.bindex].longitude,
      scale: this.data.markers[e.currentTarget.dataset.bindex].scale
    })
  },
  //抽屉结束

  callouttap(e) {
    // console.log('点击地图区域：', e.detail.markerId)
  },
  // 点击callouttap
  callouttap(e) {
    // console.log('@@@ callouttap', e)
  },

  //mark: 初始化获取region数据
  getRegionList(parameter) {
    var that = this
    wx.showLoading({
      title: '正在加载...',
    })
    let url = app.globalData.HOSTURL + '/bbs/bbsRegion/wise/mini/queryList'
    if (null == parameter || undefined == parameter) {
      parameter = ""
    }
    let bbsRegion = {
      province: parameter,
      regionStatus: 2
    }
    app.wxRequest('get', url, bbsRegion, (res) => {
      resolve(res.data.result.records)
    }, (err) => {
      reject(err)
    }).then(res => {
      var data = res.data.result
      var markerTem = []
      var showMarkerTem = []
      var circleTem = []
      var showCircleTem = []
      var currentRegion = []

      if (data.length > 0) {
        let id = 1
        data.forEach((item) => {
          // [{
          //   id: 1, //加上优化渲染速度
          //   latitude: 34.216061,
          //   longitude: 108.906913,
          //   name: '西安文理学院',
          //   scale: 13,
          //   callout: {
          //     content: '西安文理学院',
          //     anchorY: 0,
          //     anchorX: 0,
          //     display: 'ALWAYS',
          //     padding: 10,
          //     borderRadius: 5
          //   } //'BYCLICK':点击显示; 'ALWAYS':常显
          // },

          // circle: [{
          //   西安文理学院
          //   latitude: 34.216061,
          //   longitude: 108.906913,
          //   color: '#7cb5ec88', //描边的颜色
          //   fillColor: '#7cb5ec88', //填充颜色
          //   radius: 1000,
          //   level: "abovelabels"
          // }]
          var markerTemItem = {}
          var circleTemItem = {}
          var callout = {}

          markerTemItem.id = id++
          markerTemItem.latitude = item.latitude
          markerTemItem.longitude = item.longitude
          markerTemItem.name = item.fullName
          markerTemItem.scale = item.scale
          markerTemItem.height = 20
          markerTemItem.width = 20 //不生效
          markerTemItem.joinCluster = true
          markerTemItem.iconPath = '../../../static/icons/location32_32_2.png'

          callout.anchorY = item.anchorY
          callout.anchorX = item.anchorX
          callout.display = item.display
          // callout.display = "BYCLICK"
          callout.padding = item.padding
          callout.bgColor = "#FFFFFFCC" //背景色
          callout.borderRadius = item.borderRadius
          // 当前区域文字提示
          if (wx.getStorageSync('ALLINFO').bbsUserRecord.regionCode == item.regionCode) {
            callout.content = "当前区域：" + item.content
          } else {
            callout.content = "Go：" + item.content
          }
          markerTemItem.callout = callout

          // 列表追加
          markerTem.push(markerTemItem)
          // // 展示Marker范围
          // if (markerTemItem.latitude <= that.data.northeast.latitude &&
          //   markerTemItem.longitude <= that.data.northeast.longitude &&
          //   markerTemItem.latitude >= that.data.southwest.latitude &&
          //   markerTemItem.longitude >= that.data.southwest.longitude) {
          //   //列表追加
          //   showMarkerTem.push(markerTemItem)
          // }

          //圆型区域范围
          circleTemItem.latitude = item.latitude
          circleTemItem.longitude = item.longitude
          circleTemItem.radius = item.radius
          circleTemItem.level = "abovelabels"

          // 当前区域高亮
          if (wx.getStorageSync('ALLINFO').bbsUserRecord.regionCode == item.regionCode) {
            // currentRegion = [item.province.split('-')[0], item.province.split('-')[1]]
            circleTemItem.color = '#32CD3288' //描边的颜色
            circleTemItem.fillColor = '#32CD3288' //填充颜色
          } else {
            circleTemItem.color = '#7cb5ec44' //描边的颜色
            circleTemItem.fillColor = '#7cb5ec44' //填充颜色
          }
          // 列表追加
          circleTem.push(circleTemItem)
          // // 展示圆形范围
          // if (circleTemItem.latitude <= that.data.northeast.latitude &&
          //   circleTemItem.longitude <= that.data.northeast.longitude &&
          //   circleTemItem.latitude >= that.data.southwest.latitude &&
          //   circleTemItem.longitude >= that.data.southwest.longitude) {
          //   showCircleTem.push(circleTemItem)
          // }
        })
        that.setData({
          markers: markerTem,
          // showMarkers: showMarkerTem,
          circle: circleTem,
          // showCircle: showCircleTem,
          regionList: data,
        })
      } else {
        wx.showToast({
          title: '区域列表为空',
          icon: "none"
        })
      }
      wx.hideLoading()
    }, err => {
      wx.showToast({
        title: '获取地区列表失败',
        icon: "none"
      })
      wx.hideLoading()
    })
  },
  //获取用户信息
  getUserAllInfo() {
    var that = this
    // 获取token
    app.getUserAllInfo().then(res => {
      that.setData({
        USERRECORD: res.bbsUserRecord
      })
    })
  },
  //切换区域
  switchRegion(region) {
    var that = this
    let url = app.globalData.HOSTURL + '/bbs/bbsRegion/wise/mini/switchRegion';
    app.wxRequest('post', url, region, (res) => {
      resolve(res.data.result.records)
    }, (err) => {
      reject(err)
    }).then(res => {
      if (res.data.code != 200) {
        wx.showToast({
          title: '用户记录获取失败。',
          icon: 'none'
        })
      } else {
        var ALLINFO = wx.getStorageSync('ALLINFO')
        ALLINFO.bbsUserRecord.regionCode = region.regionCode
        ALLINFO.bbsUserRecord.regionFullName = region.fullName
        wx.setStorageSync('ALLINFO', ALLINFO)
        wx.showToast({
          title: '切换成功',
          icon: 'none'
        })
        app.globalData.SwitchRegion = true
        wx.navigateBack({
          delta: 1
        });
      }
    }, err => {
      wx.showToast({
        title: '用户记录获取失败。',
        icon: 'none'
      })
    })
  },
  // mark: 渲染完成提示
  bindupdated() {
    // wx.showToast({
    //   title: '加载完成',
    //   icon: 'none'
    // })
  },
  // 聚合簇
  bindEvent() {
    var that = this
    this.mapCtx.initMarkerCluster({
      enableDefaultStyle: false, // enableDefaultStyle	boolean	true	否	启用默认的聚合样式
      zoomOnClick: true, // zoomOnClick	boolean	true	否	点击已经聚合的标记点时是否实现聚合分离
      gridSize: 60, // gridSize	boolean	60	否	聚合算法的可聚合距离，即距离小于该值的点会聚合至一起，以像素为单位
      complete(res) {
        // console.log('initMarkerCluster', res)
      }
    })

    //聚合簇发生变化触发
    // enableDefaultStyle 为 true 时不会触发改事件
    this.mapCtx.on('markerClusterCreate', res => {
      console.log("聚合簇", res)
      const clusters = res.clusters
      const markers = clusters.map(cluster => {
        const {
          center,
          clusterId,
          markerIds
        } = cluster
        return {
          ...center,
          width: 23,
          height: 23,
          clusterId, // 必须
          label: {
            content: that.data.markers[markerIds[0]].name.substring(0, 6),
            fontSize: 13,
            color: '#DC143C',
            // width: 100,
            // height: 40,
            bgColor: '#FFFFFF00',
            borderRadius: 0,
            textAlign: 'left',
            anchorX: 11,
            anchorY: -22,
            padding: 0,
            // borderRadius:1000
          },
          // callout: {
          //   content: "hahahaha",
          //   fontSize: 12,
          //   padding: 10,
          // },
          iconPath: '../../../static/icons/location48_48.png'
        }
      })
      this.mapCtx.addMarkers({
        markers,
        clear: false,
        complete(res) {
          // console.log('clusterCreate addMarkers', res)
        }
      })
    })

  },
  // 使用渐进式渲染 机器会卡死无响应         bindregionchange="regionchange"
  regionchange(e) {
    var that = this
    let markers = that.data.markers
    let showMarkerTem = []
    let circle = that.data.circle
    let showCircleTem = []
    // console.log("地图变化", e)
    if (e.type == "end") {
      let region = e.detail.region
      that.data.northeast = region.northeast //东北  右上
      that.data.southwest = region.southwest //西南  左下
    }
    // 更新Marker信息 
    for (const key in markers) {
      let markerTemItem = markers[key]
      // 展示Marker范围
      if (markerTemItem.latitude <= that.data.northeast.latitude &&
        markerTemItem.longitude <= that.data.northeast.longitude &&
        markerTemItem.latitude >= that.data.southwest.latitude &&
        markerTemItem.longitude >= that.data.southwest.longitude) {
        //列表追加
        showMarkerTem.push(markerTemItem)
      }
    }

    // 更新圆圈区域信息
    for (const key in circle) {
      let circleTemItem = circle[key]
      // 展示圆形范围
      if (circleTemItem.latitude <= that.data.northeast.latitude &&
        circleTemItem.longitude <= that.data.northeast.longitude &&
        circleTemItem.latitude >= that.data.southwest.latitude &&
        circleTemItem.longitude >= that.data.southwest.longitude) {
        showCircleTem.push(circleTemItem)
      }
    }
    that.setData({
      showMarkers: showMarkerTem,
      showCircle: showCircleTem
    })
  },
  //评估多个范围，生成最接近的范围圆心和半径
  approachCircle(regions) {

  },
  //计算多个点之间的最大距离,传入点数组
  maxDistance(pointArray) {
    var that = this
    //p1 and p2 are the indices in the points array
    let p1 = 0,
      p2 = 1;
    let shortestDistance = that.distance(pointArray[p1][0], pointArray[p1][1], pointArray[p2][0], pointArray[p2][1]);
    //Compute distance for every two points
    for (let i = 0; i < pointArray.length; i++) {
      for (let j = i + 1; j < pointArray.length; j++) {
        var distance = that.distance(pointArray[i][0], pointArray[i][1], pointArray[j][0], pointArray[j][1]);
        if (shortestDistance < distance) {
          p1 = i;
          p2 = j;
          shortestDistance = distance;
        }
      }
    }
    // Display result
    // console.log("The closest two points are " + "(" + pointArray[p1][0] + "," +
    //   pointArray[p1][1] + ") and (" + pointArray[p2][0] + "," + pointArray[p2][1] + ")");
    return that.distance(pointArray[p1][0], pointArray[p1][1], pointArray[p2][0], pointArray[p2][1])
  },

  // sqrt
  distance(x1, y1, x2, y2) {
    return Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
  },
  RegionChange: function (e) {
    console.log(e)
    this.setData({
      region: e.detail.value
    })
  },
  //区域筛选
  MultiChange(e) {
    var that = this
    let provinceCityListIndex = e.detail.value
    //provinceCityListValue为名称：陕西省-榆林市
    let provinceCityListValue = that.data.currentRegion[0][provinceCityListIndex[0]] + '-' + that.data.provinceCityList[provinceCityListIndex[0]][provinceCityListIndex[1]]
    console.log(provinceCityListIndex)
    that.getRegionList(provinceCityListValue)
    that.setData({
      currentRegionIndex: provinceCityListIndex
    })
  },
  MultiColumnChange(e) {
    var that = this
    let data = {
      currentRegion: this.data.currentRegion,
      currentRegionIndex: this.data.currentRegionIndex
    };
    data.currentRegionIndex[e.detail.column] = e.detail.value;
    switch (e.detail.column) {
      case 0:
        switch (data.currentRegionIndex[0]) {
          case 0:
            data.currentRegion[1] = that.data.provinceCityList[0];
            break;
          case 1:
            data.currentRegion[1] = that.data.provinceCityList[1];
            break;
        }
        data.currentRegionIndex[1] = 0;
        data.currentRegionIndex[2] = 0;
        break;
    }
    this.setData(data);
  },
})