const app = getApp();
const formatUtil = require('../../../../utils/formatutil');
const API = require('../../../../utils/API');
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
        //动画
        toggleDelay: false,
        list: [{
            name: 'scale-down',
            color: 'orange'
        }],
        //顶部加载条
        loadProgressShow: true,
        loadProgress: 0,
        isLoad: false,
        getTopicFlag: false,
        pageNo: 1,
        onReachBottomLoading: false,
        modalName: ''
    },
    onLaunch() {

    },
    onLoad() {
        this.setData({
            UPLOAD_IMAGE: app.globalData.UPLOAD_IMAGE
        })
        //获取帖子列表
        this.waitStarList()
    },
    //等待加载成功
    waitStarList() {
        var that = this
        that.setData({
            loadProgressShow: true
        })
        that.loadProgress()
        var index = 1
        that.getStarTopicLists()
        var timer = setInterval(function () {
            if (index % 5 == 0) {
                that.getStarTopicLists()
            }
            if (index == 10) {
                that.loadProgress()
                that.setData({
                    isLoad: true,
                    onReachBottomLoading: false
                })
                wx.stopPullDownRefresh()
                clearInterval(timer)
            }
            if (that.data.getTopicFlag == true) {

                that.loadProgress()
                that.setData({
                    isLoad: true,
                    onReachBottomLoading: true
                })
                wx.stopPullDownRefresh()
                clearInterval(timer)
            }
            console.log(index)
            index = index + 1;
        }, 1000);
    },
    //获取收藏帖子列表
    getStarTopicLists() {
        var that = this
        that.setData({
            getTopicFlag: false
        })
        let url = app.globalData.HOSTURL + '/bbs/bbsUserStar/wise/mini/list?pageNo=' + this.data.pageNo
        var topicLists = app.wxRequest('get', url, '').then(res => {
            if (res.data.result != null) {
                console.log(res)
                res.data.result.forEach((item) => {
                    item.createTime = formatUtil.showDate(item.createTime)
                    // item.updateTime = formatUtil.showDate(item.updateTime)
                    item.publicTime = formatUtil.showDate(item.publicTime)
                    item.editTime = formatUtil.showDate(item.editTime)
                    item.userRole = item.userRole.substring(4)
                    //正则去除html标签
                    item.content = item.content.replace(/<\/?.+?\/?>/g, '')
                    // 去除跳转标签
                    item.content = item.content.replace(/(?=!_).+(?:_!)/g, '')
                })
                //列表追加
                var tempList = that.data.topicLists
                for (var item in res.data.result) {
                    tempList.push(res.data.result[item])
                }
                that.setData({
                    topicLists: tempList,
                    getTopicFlag: true
                })
            } else {
                that.setData({
                    getTopicFlag: true,
                    pageNo: 1
                })
            }
        }, err => {
            that.setData({
                topicLists: res.data.result,
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
        console.log(event.currentTarget.dataset)
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
        this.waitStarList()
    },
    onPullDownRefresh() {
        this.setData({
            loadProgress: 0,
            onReachBottomLoading: false,
            topicLists: [],
            pageNo: 0
        })
        this.waitStarList()
    },
    //顶部进度条加载动画
    loadProgress() {
        var that = this
        if (!that.data.getTopicFlag) {
            that.setData({
                loadProgress: that.data.loadProgress + 3
            })
            if (that.data.loadProgress < 100) {
                setTimeout(() => {
                    that.loadProgress();
                }, 100)
            } else {
                //关闭自动取消
                that.setData({
                    loadProgress: 0
                })
            }
        } else {
            var timer = setInterval(function () {
                that.setData({
                    loadProgress: that.data.loadProgress + 2
                })
                if (that.data.loadProgress >= 100) {
                    that.setData({
                        loadProgressShow: false
                    })
                    clearInterval(timer)
                }
            }, 50)
        }
    },
    hideModal(e) {
        this.setData({
            modalName: null
        })
    },
    // 组件监听
    myEventListener: function (e) {
        console.log(e)
        let actionTopic = this.data.topicLists[e.detail.topicIndex]
        let actionGroups = []

        console.log(actionTopic)
        let SYSUSER = wx.getStorageSync('ALLINFO').sysUser
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
        this.setData({
            actionTopic: actionTopic,
            actionGroups: actionGroups,
            showActionsheet: true
        })
    },
    // mark: 点击action，触发相应方法
    clickAction(e) {
        // 判断用户点击的是收藏还是举报
        if (e.detail.index == 0) {
            this.clickStar()
        }
        this.setData({
            showActionsheet: false
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
            console.log(API)
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
})