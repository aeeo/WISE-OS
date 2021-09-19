const app = getApp();
const formatUtil = require('../../../../utils/formatutil');

const DevAsk = `<p class="MsoNormal" align="left" style="text-align:left;text-indent:24.0pt;">
<p class="MsoNormal" align="left" style="text-align:left;text-indent:24.0pt;">
    <p class="MsoNormal" align="left" style="text-align:left;text-indent:21.0pt;">
        九州万事通是行星工作室（目前仅我一人）开发的本地小程序，用户可以在浏览发布附近的信息，找对象、失物招领、娱乐、教育、医疗、政策、农业、商业等等，优质帖子会被系统设为置顶。<span></span>
    </p>
    <p class="MsoNormal" align="left" style="text-align:left;text-indent:21.0pt;">
        小程序是一片净土，不会穿插广告，但是服务器的费用还是很昂贵，如果有能力可以点击【我的-支持】为服务器续命。<span></span>
    </p>
    <p class="MsoNormal" align="left" style="text-align:left;text-indent:21.0pt;">
        我们希望建立一个良性的交流环境，一旦发现销售中介等大量散播信息、以及任何扰乱秩序，发布反动言论、留言、色情的用户都将被永久封禁！万一被封，可以去公益平台捐<span>100</span>元，拿着凭证找管理员解禁。<span></span>
    </p>
    <p class="MsoNormal" align="left" style="text-align:left;text-indent:21.0pt;">
        目前测试阶段，欢迎反馈【小程序底部<span>-</span>我的<span>-</span>意见反馈】<span></span>
    </p>
    <p class="MsoNormal" align="left" style="text-align:left;text-indent:21.0pt;">
        对了，角落有许多小彩蛋，能不能找到看你们的了。<span></span>
    </p>
</p>
<p class="MsoNormal" align="left" style="text-align:left;text-indent:24.0pt;">
    <span></span>
</p>
</p>
<p class="MsoNormal" align="left" style="text-align:left;text-indent:24.0pt;">
<span></span> 
</p>
`
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
        USERRECORD: wx.getStorageSync('USERRECORD'),
        REGIONCLASS: wx.getStorageSync('REGIONCLASS'),
        CURRENTCLASSCODE: wx.getStorageSync('CURRENTCLASSCODE'), //当前所在版块Code
        
        UPLOAD_IMAGE: app.globalData.UPLOAD_IMAGE,
        THUMBNAIL: app.globalData.THUMBNAIL,
        ARTWORK: app.globalData.ARTWORK,
        ARTWORKNOWATER: app.globalData.ARTWORKNOWATER,
        //动画
        list: [{
            name: 'scale-down',
            color: 'orange'
        }],
        //顶部加载条
        loadProgressShow: true,
        loadProgress: 0,
        isLoad: false,
        getTopicFlag: false,
        isFirstGetTopicFlag: true,
        pageNo: 1,
        //骨架屏
        // skeletonLoading: true,
        // hideCategory: false,
        // hideGoods: false,
        // hideFooter: false,
        // hideBanner: false,
        DevAsk,
        onReachBottomLoading: false,
        // tabSelectIndex: "0",
        TabCur: 0,
        scrollLeft: 0,
        touchS: [0, 0],
        touchE: [0, 0]
    },
    onLoad() {
        var that = this
        that.setData({
            UPLOAD_IMAGE: app.globalData.UPLOAD_IMAGE
        })
        /**
         * 查看上一次登录时间，如果不是今天，重新登录并获取token、用户信息、贴子列表
         */
        if (wx.getStorageSync('LastLoginTime') != new Date().getDate()) {
            console.log("获取token")
            wx.setStorageSync("LastLoginTime", new Date().getDate())
            // 获取token
            app.getFirstLoginToken().then(res => {
                app.getUserRecord().then(res => {
                    that.setData({
                        USERRECORD: res
                    })
                    that.waitTopicList()
                })
            })
        } else {
            app.getUserRecord().then(res => {
                that.setData({
                    USERRECORD: res
                })
                that.waitTopicList()
            })
        }
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
            topicLists: that.data.topicLists,
            USERRECORD: wx.getStorageSync('USERRECORD')
        })
        if (app.globalData.SwitchRegion) {
            //刷新页面
            this.data.topicLists = []
            this.setData({
                loadProgress: 0,
                onReachBottomLoading: false,
                pageNo: 1,
                topicLists: []
            })
            this.waitTopicList()
            app.globalData.SwitchRegion = false
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
        that.loadProgress()
        var index = 1
        that.getBbsTopicLists()
        var timer = setInterval(function () {
            if (index % 5 == 0) {
                that.getBbsTopicLists()
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
            index = index + 1;
        }, 1000);
    },
    //获取帖子列表
    getBbsTopicLists() {
        var that = this
        that.setData({
            getTopicFlag: false
        })
        let url = app.globalData.HOSTURL + '/bbs/bbsUserTopicClick/wise/mini/list?pageNo=' + this.data.pageNo
        app.wxRequest('get', url, '').then(res => {
            if (res.data.code == 200) {
                if (res.data.result != null && res.data.result.length > 0) {
                    res.data.result.forEach((item) => {
                        item.userRole = item.userRole.substring(4)
                        // 添加动画属性
                        item.exeCuteAnimation = item.userIsPraise
                        item.createTime = formatUtil.showDate(new Date(item.createTime.replace(/-/g, '/')))
                        item.updateTime = formatUtil.showDate(new Date(item.updateTime.replace(/-/g, '/')))
                        //正则去除html标签
                        item.content = item.content.replace(/<\/?.+?\/?>/g, '')
                        // 去除跳转标签
                        item.content = item.content.replace(/(?=!_).+(?:_!)/g, '')
                    })
                    //列表追加
                    var tempList = that.data.topicLists
                    var resTopicList = res.data.result
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
                that.setData({
                    getTopicFlag: true,
                    isFirstGetTopicFlag: false
                })
                wx.showToast({
                    title: '获取浏览记录出错',
                    icon: "none"
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
        // 点击贴子直接本地缓存浏览量+1
        var topicListTem = this.data.topicLists
        event.currentTarget.dataset.topicitem.hitsCount = event.currentTarget.dataset.topicitem.hitsCount + 1
        topicListTem[event.currentTarget.dataset.bindex].hitsCount = topicListTem[event.currentTarget.dataset.bindex].hitsCount + 1
        this.data.topicLists = topicListTem

        var topicId = event.currentTarget.dataset.topicitem.id
        //点击贴子更新相关数据
        let url = app.globalData.HOSTURL + '/bbs/bbsTopic/wise/mini/userClickTopic?topicId=' + topicId
        app.wxRequest('get', url, '', (res) => {
            resolve(res.data.result)
        }, (err) => {
            reject(err)
        })

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
        //刷新页面
        this.data.topicLists = []
        this.setData({
            loadProgress: 0,
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
                resolve(res.data.result)
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
                resolve(res.data.result)
            }, (err) => {
                reject(err)
            }).then(res => {

            }, err => {

            })
        }
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
                // that.setData({
                //     loadProgress: 0
                // })
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
    //点击topic图片放大预览
    clickTopicImage(event) {
        var imageList = []
        for (var itemImage in event.currentTarget.dataset.imagelist) {
            imageList.push(event.currentTarget.dataset.imagelist[itemImage].topicImage)
        }
        wx.previewImage({
            urls: imageList, //需要预览的图片http链接列表，注意是数组
            current: event.currentTarget.id, // 当前显示图片的http链接，默认是第一个
            success: function (res) {},
            fail: function (res) {},
            complete: function (res) {},
        })
    },
})