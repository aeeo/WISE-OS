const app = getApp();
const formatUtil = require('../../../../utils/formatutil');
Page({
    data: {
        refresherItems: [],
        count: 0,
        scrollTop: 0,
        isCard: 'no-card',
        topicLists: [],
        StatusBar: app.globalData.StatusBar,
        CustomBar: app.globalData.CustomBar,
        Custom: app.globalData.Custom,
        USERRECORD: wx.getStorageSync('ALLINFO').bbsUserRecord,
        REGIONCLASS: wx.getStorageSync('ALLINFO').bbsClassList,
        CURRENTCLASSCODE: wx.getStorageSync('CURRENTCLASSCODE'), //当前所在版块Code
        //动画
        list: [{
            name: 'scale-down',
            color: 'orange'
        }],
        //顶部加载条
        loadProgressShow: true,
        isLoad: false,
        getTopicFlag: false,
        isFirstGetTopicFlag: true,
        pageNo: 1,
        onReachBottomLoading: false,
        TabCur: 0,
        scrollLeft: 0,
        touchS: [0, 0],
        touchE: [0, 0],
        //悬浮按钮data 数据
        btnList: [{
            bgColor: "#16C2C2",
            //名称
            text: "关于",
            //字体大小
            fontSize: 28,
            //字体颜色
            color: "#000",
            imgUrl: "../../../static/images/fabu.png",
            imgHeight: '45',
            imgWidth: '45'
        }, {
            bgColor: "#64B532",
            //名称
            text: "分享",
            //字体大小
            fontSize: 28,
            //字体颜色
            color: "#000",
            imgUrl: "../../../static/images/fabu.png",
            imgHeight: '45',
            imgWidth: '45',
            ketWord: ''
        }]
    },
    onLoad(option) {
        console.log(option)
        var that = this
        this.setData({
            UPLOAD_IMAGE: app.globalData.UPLOAD_IMAGE,
            keyWord: option.keyWord
        })
        this.waitTopicList()
    },
    onReady() {
        var DevAskFlag = wx.getStorageSync('DevAskFlag');
        if (DevAskFlag != "yes") {
            //首屏公告
            this.setData({
                modalName: "DevlopAsk"
            })
        }
        wx.setStorageSync("DevAskFlag", "yes")
    },
    onShow() {
        var that = this
        this.setData({
            USERRECORD: wx.getStorageSync('ALLINFO').bbsUserRecord
        })
        if (app.globalData.SwitchRegion) {
            console.log("切换区域刷新页面")
            //刷新页面
            this.data.topicLists = []
            this.setData({
                onReachBottomLoading: false,
                pageNo: 1,
                topicLists: []
            })
            this.waitTopicList()
            app.globalData.SwitchRegion = false
        }
        if (app.globalData.needReloadTopicList) {
            console.log("重新加载刷新页面")
            //刷新页面
            this.data.topicLists = []
            this.setData({
                onReachBottomLoading: false,
                pageNo: 1
            })
            this.waitTopicList()
        }
        // 从贴子详情页面返回是否需要刷新收藏状态   
        if (app.globalData.needModUserIsStar[1] != -1) {
            console.log("收藏贴子刷新页面")
            var topicListTmp = this.data.topicLists
            topicListTmp[app.globalData.needModUserIsStar[1]].userIsStar = app.globalData.needModUserIsStar[0]
            this.setData({
                topicLists: topicListTmp
            })
        }
    },
    //关闭首屏公告
    hideModal(e) {
        this.setData({
            modalName: null
        })
    },
    //等待加载成功
    waitTopicList() {
        var that = this
        that.setData({
            loadProgressShow: true
        })
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
    //获取帖子列表
    getBbsTopicLists() {
        var that = this
        that.setData({
            getTopicFlag: false
        })
        let url = app.globalData.HOSTURL + '/bbs/bbsTopic/wise/mini/fullListByKeyWord?keyWord=' + this.data.keyWord + '&pageNo=' + this.data.pageNo
        app.wxRequest('get', url, '').then(res => {
            if (res.data.code == 200) {
                if (res.data.result.records.length > 0) {
                    res.data.result.records.forEach((item) => {
                        item.userRole = item.userRole.substring(4)
                        // 添加动画属性
                        item.exeCuteAnimation = item.userIsPraise
                        item.createTime = formatUtil.showDate(new Date(item.createTime))
                        item.updateTime = formatUtil.showDate(new Date(item.updateTime))
                        //正则去除html标签
                        item.content = item.content.replace(/<\/?.+?\/?>/g, '')
                        // 去除跳转标签
                        item.content = item.content.replace(/(?=!_).+(?:_!)/g, '')
                    })

                    //列表追加
                    var tempList = that.data.topicLists
                    var resTopicList = res.data.result.records
                    for (var item in resTopicList) {
                        let itemTopic = resTopicList[item]
                        tempList.push(itemTopic)
                    }
                    if (!that.data.isFirstGetTopicFlag && app.globalData.needReloadTopicList) {
                        app.globalData.needReloadTopicList = false
                    }
                    console.log(tempList)
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
        }, err => {
            that.setData({
                topicLists: res.data.result.records,
                getTopicFlag: false
            })
        })
    },

    onPageScroll(e) {
        this.setData({
            scrollTop: e.scrollTop
        })
    },
    // 卡片跳转
    topicdetails: function (event) {
        console.log(event)
        // 点击贴子直接本地缓存浏览量+1
        var topicListTem = this.data.topicLists
        event.currentTarget.dataset.topicitem.hitsCount = event.currentTarget.dataset.topicitem.hitsCount + 1
        topicListTem[event.currentTarget.dataset.bindex].hitsCount = topicListTem[event.currentTarget.dataset.bindex].hitsCount + 1
        this.setData({
            topicLists: topicListTem
        })

        var topicId = event.currentTarget.dataset.topicitem.id
        //点击贴子更新相关数据
        let url = app.globalData.HOSTURL + '/bbs/bbsTopic/wise/mini/userClickTopic?topicId=' + topicId
        app.wxRequest('get', url, '')
        wx.navigateTo({
            url: '../../topic/topicdetails/topicdetails',
            success: function (res) {
                // 通过eventChannel向被打开页面传送数据
                res.eventChannel.emit('acceptDataFromOpenerPage', {
                    data: event.currentTarget.dataset
                })
            }
        })
    },
    onReachBottom: function () {
        this.setData({
            pageNo: this.data.pageNo + 1,
            onReachBottomLoading: false
        })
        this.waitTopicList()
    },
    onPullDownRefresh() {
        //设置是否是刷新 提示相应文字
        app.globalData.needReloadTopicList = true
        //刷新页面
        this.data.topicLists = []
        this.setData({
            onReachBottomLoading: false,
            pageNo: 1
        })
        this.waitTopicList()
        // wx.stopPullDownRefresh()
    },
    getUserInfo: function (e) {
        console.log(e)
        app.globalData.userInfo = e.detail.userInfo
        this.setData({
            userInfo: e.detail.userInfo,
            hasUserInfo: true
        })
    },
    //浏览
    clickHitsCount(event) {
        console.log("浏览" + event)

    },
    //点赞
    clickPraiseCount(event) {
        console.log("点赞" + event)
    },
    //评论
    clickReplyCount(event) {
        console.log("评论" + event)
    },
    //点赞
    clickPraise(e) {
        //若果没有点赞  点赞并展示动画
        if (!this.data.topicLists[e.target.dataset.bindex].userIsPraise) {
            var topicListTem = this.data.topicLists
            topicListTem[e.target.dataset.bindex].userIsPraise = true
            topicListTem[e.target.dataset.bindex].exeCuteAnimation = true
            topicListTem[e.target.dataset.bindex].praiseCount = topicListTem[e.target.dataset.bindex].praiseCount + 1
            this.setData({
                topicLists: topicListTem,
            })
            let url = app.globalData.HOSTURL + '/bbs/bbsUserPraise/wise/mini/clickPraise?topicId=' + e.target.id + '&isPraise=' + true
            app.wxRequest('post', url, '', (res) => {
                resolve(res.data.result.records)
            }, (err) => {
                reject(err)
            }).then(res => {

            }, err => {

            })
        } else {
            var topicListTem = this.data.topicLists
            topicListTem[e.target.dataset.bindex].userIsPraise = false
            topicListTem[e.target.dataset.bindex].exeCuteAnimation = false
            topicListTem[e.target.dataset.bindex].praiseCount = topicListTem[e.target.dataset.bindex].praiseCount - 1
            this.setData({
                topicLists: topicListTem,
            })
            let url = app.globalData.HOSTURL + '/bbs/bbsUserPraise/wise/mini/clickPraise?topicId=' + e.target.id + '&isPraise=' + false
            app.wxRequest('post', url, '', (res) => {
                resolve(res.data.result.records)
            }, (err) => {
                reject(err)
            }).then(res => {

            }, err => {

            })
        }
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

    selectResult: function (e) {
        console.log('select result', e.detail)
    },
})