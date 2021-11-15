const app = getApp();
const formatUtil = require('../../utils/formatutil.js');
const API = require('../../utils/API');
const DevAsk = `<p class="MsoNormal" align="left" style="text-align:left;text-indent:21.0pt;">
行星万象是行星环绕工作室开发的本地生活小程序，用户可以在当下区域浏览发布各类信息，如话题、吐槽、找对象、失物招领等等。我们希望建立一个良性的交流环境，任何恶意发布反动、色情、租赁、诱导等信息的用户都将被永久封禁！ <span></span>
</p>
<p class="MsoNormal" align="left" style="text-align:left;text-indent:21.0pt;">
每个人经历不同，自然会想法不同，看到三观不合的人或事，尽量理解，选择性接受，禁止互喷，否则管理员容易误伤。<span></span>
</p>`

Page({
    data: {
        // 骨架屏
        showSkelton: true,
        refresherItems: [],
        count: 0,
        scrollTop: 0,
        isCard: true,
        topicLists: [],
        StatusBar: app.globalData.StatusBar,
        CustomBar: app.globalData.CustomBar,
        Custom: app.globalData.Custom,
        windowHeight: app.globalData.windowHeight,
        screenHeight: app.globalData.screenHeight,

        BBSREGION:  wx.getStorageSync('ALLINFO').bbsRegion,
        USERRECORD: wx.getStorageSync('ALLINFO').bbsUserRecord,
        REGIONCLASS: wx.getStorageSync('ALLINFO').bbsClassList,
        CURRENTCLASSCODE: wx.getStorageSync('CURRENTCLASSCODE'), //当前所在版块Code
        getTopicFlag: false,
        isFirstGetTopicFlag: true,
        DevAsk,
        startFooterLoading: true,
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
            imgWidth: '45'
        }],
        // tab切换 当前版块索引
        currentClass: 0,
        // topicListHeight: [],
        // 数据校验
        dataVerify: {
            // 设置时间戳，判断数据是否过期
            timestampRequest: [],
            // 设置时间戳，判断数据是否加载成功，成功才能加载下一页
            isGetTopicFinsh: [true],
            // 版块索引
        },
        showActionsheet: false,
        actionGroups: [],
        showReply: false, //展示评论弹框
        fullReplys: [],
        forbidSkip:true             //分享跳转，第一次为true，之后设置为false，否则每次都认为是分享
    },
    onLoad(options) {
        this.setData({
            UPLOAD_IMAGE: app.globalData.UPLOAD_IMAGE,
            THUMBNAIL: app.globalData.THUMBNAIL,
            ARTWORK: app.globalData.ARTWORK,
            ARTWORKNOWATER: app.globalData.ARTWORKNOWATER, //原图无水印
            options: options
        })
        var that = this

        // 获取token
        app.getFirstLoginToken().then(res => {
            that.setData({
                USERRECORD: res.bbsUserRecord,
            })
            that.getRegionClass(res.bbsClassList)
            that.waitTopicList().then(res => {
                console.log("==========================================================")
                // 通过其他分享渠道跳转至别的页面,需要准备好后再跳转
                that.shareNavigateTo(that.data.options)
            }, err => {

            })
        })
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
        /**
         * 切换区域后返回需要重新加载当前区域贴子列表
         */
        if (app.globalData.SwitchRegion) {
            console.log("切换区域刷新页面")
            //刷新页面
            this.setData({
                startFooterLoading: true,
                topicLists: []
            })
            app.getUserAllInfo().then(res => {
                that.setData({
                    USERRECORD: res.bbsUserRecord,
                })
                let currentClass = that.data.currentClass
                that.getRegionClass(res.bbsClassList)
                if (currentClass == 0) {
                    that.waitTopicList() //currentClass改变会触发swipper加载,等于0不会触发，因此为零时此处补全
                }
            })
            app.globalData.SwitchRegion = false
        }
        /**
         * 是否需要重新加载当前版块贴子列表
         */
        if (app.globalData.needReloadTopicList) {
            console.log("重新加载刷新页面")
            //刷新页面
            this.data.topicLists[this.data.currentClass].topicList = []
            this.setData({
                startFooterLoading: true,
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
    onTabItemTap(item) {
        // console.log("index监听tabbar")
        // 获取用户Record，刷新tabbar提示
        var that = this
        app.getUserAllInfo().then(res => {
            that.setData({
                USERRECORD: res.bbsUserRecord
            })
            app.setTabbarBadge()
        })
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
            startFooterLoading: true,
        })
        return new Promise(function (resolve, reject) {
            that.getBbsTopicLists().then(res => {
                wx.stopPullDownRefresh()
                resolve(true)
            }, err => {
                reject(false)
            })
        })
    },
    // mark: 获取帖子列表
    getBbsTopicLists() {
        var that = this
        const thisCurrentClass = this.data.currentClass

        that.data.dataVerify.isGetTopicFinsh[thisCurrentClass] = false
        that.data.getTopicFlag = false
        // 默初始化pageNo为1
        let pageNo = 0
        if (this.data.topicLists.length != 0) {
            pageNo = this.data.topicLists[this.data.currentClass].pageNo
        } else {
            pageNo = 1
        }
        this.data.dataVerify.timestampRequest[thisCurrentClass] = (new Date()).getTime()
        let url = app.globalData.HOSTURL + '/bbs/bbsTopic/wise/mini/fullList?pageNo=' + pageNo + '&timestampRequest=' + this.data.dataVerify.timestampRequest[thisCurrentClass] + '&classIndex=' + this.data.currentClass
        //加载贴子类型，普通加载只能加载到0,1,2 普通，置顶，精华。5代表为公共隐藏类型，只能通过单独加载
        let topicType = [0, 1, 2]
        url += "&topicType=" + topicType
        return result = new Promise(function (resolve, reject) {
            app.wxRequest('get', url, '', 5000).then(res => {
                if (res.data.code == 200) {
                    if (res.data.result.records.length > 0) {
                        res.data.result.records.forEach((item) => {
                            item.userRole = item.userRole.substring(4)
                            // 添加动画属性
                            item.exeCuteAnimation = item.userIsPraise
                            item.createTime = formatUtil.showDate(new Date(item.createTime.replace(/-/g, '/')))
                            // item.updateTime = formatUtil.showDate(new Date(item.updateTime.replace(/-/g, '/')))
                            item.publicTime = formatUtil.showDate(new Date(item.publicTime.replace(/-/g, '/')))
                            item.editTime = formatUtil.showDate(new Date(item.editTime.replace(/-/g, '/')))
                            // 正则去除html标签
                            let contentString = item.content.replace(/<\/?.+?\/?>/g, '')
                            // 富文本不换行
                            if (item.content == contentString) {
                                // 普通文本换行
                                item.content = item.content.replace(/\n/g, "<br>")
                            }

                            // 去除跳转标签
                            // item.content = item.content.replace(/(?=!_).+(?:_!)/g, '')
                        })
                        //列表追加
                        let tempList = that.data.topicLists
                        var recordList = res.data.result.records
                        for (var item in recordList) {
                            let itemTopic = recordList[item]
                            tempList[thisCurrentClass].topicList.push(itemTopic)
                        }
                        // 设置页面属性
                        tempList[thisCurrentClass].needLoad = false //设置不需要再次加载,隐藏骨架屏
                        that.data.getTopicFlag = true
                        that.data.isFirstGetTopicFlag = false

                        that.setData({
                            topicLists: tempList,
                            showSkelton: false, //隐藏骨架屏
                            isPull: false,
                        })

                        // 没有更多数据停止加载提示
                        let count = res.data.result.records.length
                        let resultSize = res.data.result.size
                        if (count < resultSize) {
                            that.setData({
                                startFooterLoading: false
                            })
                        }
                    } else {
                        // 没有更多数据
                        let topicListsTmp = that.data.topicLists
                        topicListsTmp[thisCurrentClass].pageNo = topicListsTmp[thisCurrentClass].pageNo - 1
                        topicListsTmp[thisCurrentClass].needLoad = false //设置不需要再次加载，隐藏骨架屏

                        that.data.getTopicFlag = true
                        that.setData({
                            topicLists: topicListsTmp,
                            showSkelton: false, //隐藏骨架屏

                            startFooterLoading: false,
                            isPull: false
                        })
                    }
                    that.data.dataVerify.isGetTopicFinsh[thisCurrentClass] = true
                    // 获取用户Record，刷新tabbar提示
                    app.getUserAllInfo().then(res => {
                        that.setData({
                            USERRECORD: res.bbsUserRecord
                        })
                        app.setTabbarBadge()
                    })
                    //数据加载成功
                    if (!that.data.isFirstGetTopicFlag && app.globalData.needReloadTopicList) {
                        wx.showToast({
                            title: '刷新成功',
                            icon: 'success'
                        })
                        app.globalData.needReloadTopicList = false
                    } else {
                        wx.hideLoading() //如果不是下拉刷新，则可能是第一次加载，显示loding
                    }

                } else {
                    // 首次打开需要判断token是否过期
                    if (res.data.code == 500 && res.data.message == "Token失效，请重新登录") {
                        console.log("本地token失效,请求token")
                        that.getBbsTopicLists()
                    }
                    wx.showToast({
                        title: '获取信息出错',
                        icon: "none"
                    })
                }
                that.data.dataVerify.isGetTopicFinsh[thisCurrentClass] = true
                resolve(true)
            }, err => {
                that.data.getTopicFlag = false
                that.setData({
                    topicLists: res.data.result.records,
                    isPull: false
                })
                that.data.dataVerify.isGetTopicFinsh[thisCurrentClass] = true
                reject(false)
            })
        })
    },

    onPageScroll(e) {
        this.setData({
            scrollTop: e.scrollTop
        })
    },
    // 下拉刷新
    scrollOnReachBottom: function () {
        var that = this
        let topicListsTmp = that.data.topicLists
        topicListsTmp[that.data.currentClass].pageNo = topicListsTmp[that.data.currentClass].pageNo + 1
        that.setData({
            topicLists: topicListsTmp,
            startFooterLoading: true
        })
        this.waitTopicList()
    },
    // 触底
    onReachBottom: function () {
        var that = this
        // let pageNo = that.data.topicLists[that.data.currentClass].pageNo
        if (that.data.dataVerify.isGetTopicFinsh[that.data.currentClass]) {
            let topicListsTmp = that.data.topicLists
            topicListsTmp[that.data.currentClass].pageNo = topicListsTmp[that.data.currentClass].pageNo + 1
            that.setData({
                topicLists: topicListsTmp,
                startFooterLoading: true
            })
            this.waitTopicList()
        } else {
            console.log("等待上一次请求完成！")
        }
    },
    onPullDownRefresh() {
        var that = this
        //设置是否是刷新 提示相应文字
        app.globalData.needReloadTopicList = true
        //刷新页面
        this.data.topicLists[this.data.currentClass].topicList = []
        let topicListsTmp = that.data.topicLists
        topicListsTmp[that.data.currentClass].pageNo = 1
        that.setData({
            // topicLists: topicListsTmp,
            startFooterLoading: true,
        })
        this.waitTopicList()
        // wx.stopPullDownRefresh()
    },
    scrollOnPull() {
        var that = this
        //设置是否是刷新 提示相应文字
        app.globalData.needReloadTopicList = true
        //刷新页面

        this.data.topicLists[this.data.currentClass].topicList = []
        let topicListsTmp = that.data.topicLists
        topicListsTmp[that.data.currentClass].pageNo = 1
        that.setData({
            startFooterLoading: true,
        })
        this.waitTopicList()
    },

    selectResult: function (e) {
        console.log('select result', e.detail)
    },
    //顶部tab
    tabSelect(e) {
        // console.log("切换tab")
        this.data.CURRENTCLASSCODE = this.data.REGIONCLASS[e.currentTarget.dataset.id].classCode

        this.setData({
            currentClass: e.currentTarget.dataset.id,
            scrollLeft: (e.currentTarget.dataset.id - 1) * 60,
        })
        wx.setStorageSync('CURRENTCLASSCODE', this.data.CURRENTCLASSCODE)
    },
    //mark:渲染区域下的版块
    getRegionClass(bbsClassList) {
        var that = this
        // 初始化topicList数组 size
        bbsClassList.forEach(item => {
            const arrItem = {}
            const topicList = []
            arrItem.topicList = topicList
            arrItem.needLoad = true
            arrItem.pageNo = 1
            that.data.topicLists.push(arrItem)
        })
        that.setData({
            topicLists: that.data.topicLists,
            REGIONCLASS: bbsClassList,
            currentClass: 0
        })
        wx.setStorageSync('CURRENTCLASSCODE', "index")
    },
    toSearch() {
        wx.navigateTo({
            url: '/pages/components/topic/topicsearch/topicsearch',
        })
    },
    // 滑动窗口
    swiperChange: function (e) {
        var that = this
        // console.log('滑动窗口', e)
        that.data.CURRENTCLASSCODE = that.data.REGIONCLASS[e.detail.current].classCode
        that.setData({
            currentClass: e.detail.current,
            scrollLeft: e.detail.current * 60,
        })
        wx.setStorageSync('CURRENTCLASSCODE', that.data.CURRENTCLASSCODE)

        //从区域选择页面返回，不需要加载，在OnShow方法中已经处理
        if (that.data.topicLists[that.data.currentClass].needLoad && !app.globalData.SwitchRegion) {
            console.log("show++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++")
            that.waitTopicList()
        }
    },

    /**
     * 用户点击右上角分享
     */
    onShareAppMessage: function () {
        let imageUrl = this.data.UPLOAD_IMAGE + wx.getStorageSync('ALLINFO').bbsRegion.regionImage + this.data.THUMBNAIL
        return {
            title: "『" + this.data.USERRECORD.regionFullName + "』" + '都在用的本地小程序',
            path: '/pages/index/index?regionCode=' + this.data.USERRECORD.regionCode,
            // imageUrl: imageUrl
        }
    },
    // 朋友圈       需要搞个匿名访问，或者进入后直接重新打开
    // onShareTimeline: function () {
    //     let imageUrl = this.data.UPLOAD_IMAGE + wx.getStorageSync('ALLINFO').bbsRegion.regionImage + this.data.THUMBNAIL
    //     return {
    //         title: "『" + this.data.USERRECORD.regionFullName + "』" + '都在用的本地小程序',
    //         path: '/pages/index/index?regionCode=' + this.data.USERRECORD.regionCode,
    //         imageUrl: imageUrl
    //     }
    // },
    // 组件监听
    myEventListener: function (e) {
        // console.log(e)
        let detail = e.detail
        var that = this
        if (detail.type == "more") {
            let actionTopicIndex = e.detail.topicIndex
            let currentClassIndex = this.data.currentClass
            let actionTopic = this.data.topicLists[currentClassIndex].topicList[actionTopicIndex]
            let actionGroups = []

            let SYSUSER = wx.getStorageSync('ALLINFO').sysUser
            // let bbsUserRecord = wx.getStorageSync('ALLINFO').bbsUserRecord
            // if (bbsUserRecord.roleCodeList && (bbsUserRecord.roleCodeList.includes("admin") ||
            //     bbsUserRecord.roleCodeList.includes("bbs_operator_admin") ||
            //     bbsUserRecord.roleCodeList.includes("bbs_admin"))) {
            //     let actionGroupItem1 = {
            //         text: '冻结贴子（管理员）',
            //         value: -1
            //     }
            //     let actionGroupItem2 = {
            //         text: '禁言用户3天（管理员）',
            //         value: -1
            //     }
            //     let actionGroupItem3 = {
            //         text: '永久禁言（管理员）',
            //         value: -1
            //     }
            //     let actionGroupItem4 = {
            //         text: '禁止用户登录（管理员）',
            //         value: -1
            //     }
            //     actionGroups.push(actionGroupItem1)
            //     actionGroups.push(actionGroupItem2)
            //     actionGroups.push(actionGroupItem3)
            //     actionGroups.push(actionGroupItem4)
            // }

            if (actionTopic.userIsStar) {
                let actionGroupItem = {
                    text: '取消收藏',
                    value: 0
                }
                actionGroups.push(actionGroupItem)
            } else {
                let actionGroupItem = {
                    text: '收藏',
                    value: 0
                }
                actionGroups.push(actionGroupItem)
            }
            // 举报按钮展示
            if (actionTopic.topicType == "0") {
                if (SYSUSER.username != actionTopic.createBy) {
                    let actionGroupItem = {
                        text: '举报',
                        value: 1
                    }
                    actionGroups.push(actionGroupItem)
                }
            }
            this.setData({
                actionTopic: actionTopic,
                showActionsheet: true,
                actionGroups: actionGroups
            })
        } else if (detail.type == "reply") {
            let replyTopicDetails = e.detail.topicDetails
            that.setData({
                showReply: !that.data.showReply,
                replyTopicDetails: replyTopicDetails
            })
            if (that.data.showReply) {
                that.data.fullReplys = []
                wx.hideTabBar({
                    animation: false,
                })
            } else {
                wx.showTabBar({
                    animation: false,
                })
            }
        }
    },
    // mark: 点击action，触发相应方法
    clickAction(e) {
        // 判断用户点击的是收藏还是举报
        if (e.detail.index == 0) {
            this.clickStar()
        } else if (e.detail.index == 1) {
            this.clickWarn()
        }
        this.setData({
            showActionsheet: false
        })
    },
    // mark: 举报
    clickWarn(e) {
        var that = this
        wx.showModal({
            // cancelColor: '#39b54a',
            confirmColor: '#000000',
            title: '举报',
            content: "您确认要举报这条信息吗？",
            showCancel: true,
            success(res) {
                if (res.confirm) {
                    var bbsInform = {}
                    bbsInform.topicId = that.data.actionTopic.id
                    bbsInform.type = 1 //默认
                    bbsInform.informUsername = wx.getStorageSync('ALLINFO').sysUser.username
                    bbsInform.beInformUsername = that.data.actionTopic.createBy
                    console.log(bbsInform)
                    let url = app.globalData.HOSTURL + '/bbs/bbsInform/wise/mini/informTopic'
                    app.wxRequest('post', url, bbsInform).then(res => {
                        if (res.data.code == 200) {
                            wx.showToast({
                                title: "举报成功",
                                icon: "none"
                            })
                        } else {
                            wx.showToast({
                                title: res.data.message,
                                icon: "none"
                            })
                        }
                    }, err => {

                    })
                } else if (res.cancel) {
                    wx.showToast({
                        title: "取消",
                        icon: "none"
                    })
                }
            }
        })
    },
    //mark:收藏
    clickStar() {

        var that = this
        let actionTopic = that.data.actionTopic
        //若果没有收藏  收藏并展示动画
        console.log(that.data.actionTopic)
        console.log(actionTopic.userIsStar)
        if (!actionTopic.userIsStar) {
            actionTopic.userIsStar = true
            actionTopic.executeStarAnimation = true
            actionTopic.starCount = actionTopic.starCount + 1
            this.setData({
                actionTopic: actionTopic,
            })
            let url = API.user_star_clickStar.url + '?topicId=' + actionTopic.id + '&isStar=' + true
            app.wxRequest(API.user_star_clickStar.method, url, '').then(res => {
                if (res.data.code == 200) {
                    if (that.data.topicIndex != -1) {
                        app.globalData.needModUserIsStar = [true, that.data.topicIndex]
                    }
                    wx.showToast({
                        title: '收藏成功',
                        icon: "none"
                    })
                } else {
                    wx.showToast({
                        title: res.data.message,
                        icon: "none"
                    })
                }
            }, err => {
                wx.showToast({
                    title: '收藏失败',
                    icon: "none"
                })
            })
        } else {
            actionTopic.userIsStar = false
            actionTopic.executeStarAnimation = false
            actionTopic.starCount = actionTopic.starCount - 1
            this.setData({
                actionTopic: actionTopic,
            })

            let url = API.user_star_clickStar.url + '?topicId=' + actionTopic.id + '&isStar=' + false
            app.wxRequest(API.user_star_clickStar.method, url, '').then(res => {
                if (res.data.code == 200) {
                    if (that.data.topicIndex != -1) {
                        app.globalData.needModUserIsStar = [false, that.data.topicIndex]
                    }
                    wx.showToast({
                        title: '取消收藏成功',
                        icon: "none"
                    })
                } else {
                    wx.showToast({
                        title: res.data.message,
                        icon: "none"
                    })
                }
            }, err => {
                wx.showToast({
                    title: '取消收藏失败',
                    icon: "none"
                })
            })
        }
    },
    hideReply(e) {
        var that = this
        that.setData({
            showReply: false
        })
        wx.showTabBar({
            animation: true,
        })
        console.log("hideReply")
    },

    // 通过其他分享渠道跳转至别的页面
    shareNavigateTo(options) {
        // 从贴子分享跳过来需要跳转
        if (undefined != options.topicId && '' != options.topicId) {
            wx.navigateTo({
                url: '/pages/components/topic/topicdetails/topicdetails?topicId=' + options.topicId + '&forbidSkip=true',
                success: function (res) {
                    console.log("跳转贴子：", res)
                },
                fail: function (res) {
                    console.log(res)
                },
            })
        }
        if (undefined != options.activityId && '' != options.activityId) {
            wx.navigateTo({
                url: '/packageA/activitydetail/activitydetail?activityId=' + options.activityId,
                success: function (res) {
                    // console.log("跳转贴子：", res)
                },
                fail: function (res) {
                    // console.log(res)
                },
            })
        }
    }
})