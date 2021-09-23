var app = getApp();
Page({
    data: {
        userInfo: '',
        USERRECORD: wx.getStorageSync('ALLINFO').bbsUserRecord,
        SYSUSER: wx.getStorageSync('ALLINFO').susUser,
        UPLOAD_IMAGE: app.globalData.UPLOAD_IMAGE
    },
    onLoad() {
        var that = this
        // 更新成功刷新头像
        // 获取用户Record，刷新tabbar提示
        app.getUserAllInfo().then(res => {
            that.setData({
                SYSUSER: res.sysUser
            })
        })
    },
    onShow() {
        this.getYiYan()
        this.getUserAllInfo()
    },
    //获取用户信息  发布、点赞、商店、收藏数量
    getUserAllInfo() {
        var that = this
        // 获取用户Record，刷新tabbar提示
        app.getUserAllInfo().then(res => {
            that.setData({
                USERRECORD: res.bbsUserRecord
            })
            app.setTabbarBadge()
            wx.stopPullDownRefresh()
        })
    },
    onReady() {
        var YiYanFlag = wx.getStorageSync('YiYanFlag');
        if (YiYanFlag != "yes") {
            wx.showToast({
                title: '可以下拉试试哦\r\n（○｀ 3′○）',
                icon: 'none',
                duration: 3000
            })
        }
        wx.setStorageSync("YiYanFlag", "yes")
    },
    onTabItemTap(item) {
        // console.log("mine监听tabbar")
        // 获取用户Record，刷新tabbar提示
        var that = this
        // 获取用户Record，刷新tabbar提示
        app.getUserAllInfo().then(res => {
            that.setData({
                USERRECORD: res.bbsUserRecord
            })
            app.setTabbarBadge()
        })
    },
    onContact(e) {
        console.log('onContact', e)
    },
    onGotUserInfo(e) {
        console.log('onGotUserInfo', e)
    },
    onGotPhoneNumber(e) {
        console.log('onGotPhoneNumber', e)
    },
    onReachBottom: function () {
        console.log("页面上拉触底事件的处理函数")
    },
    onPullDownRefresh() {
        this.getUserAllInfo()
        this.getYiYan()
    },

    getYiYan() {
        var that = this
        wx.request({
            // https://v1.hitokoto.cn/ (opens new window)（从7种分类中随机抽取）
            // https://v1.hitokoto.cn/?c=b (opens new window)（请求获得一个分类是漫画的句子）
            // https://v1.hitokoto.cn/?c=f&encode=text (opens new window)（请求获得一个来自网络的句子，并以纯文本格式输出）
            // a	动画
            // b	漫画
            // c	游戏
            // d	文学
            // e	原创
            // f	来自网络
            // g	其他
            // h	影视
            // i	诗词
            // j	网易云
            // k	哲学
            // l	抖机灵
            // 其他	作为 动画 类型处理
            url: 'https://v1.hitokoto.cn/?c=a&c=b&c=c&c=d&c=e&c=f&c=g&c=h&c=i&c=j&c=k&encode=text',
            success(data) {
                var length = data.data.length
                if (data.data.charAt(length - 1) == '。' || data.data.charAt(length - 1) == '！' || data.data.charAt(length - 1) == '.') {
                    data.data = data.data.substring(0, length - 1)
                }
                that.setData({
                    YiYan: data.data
                })
            }
        })
    },
    // /**
    //  * 用户点击右上角分享
    //  */
    onShareAppMessage: function () {
        let imageUrl = this.data.UPLOAD_IMAGE + wx.getStorageSync('ALLINFO').bbsRegion.regionImage + this.data.THUMBNAIL
        return {
            title: "『" + this.data.USERRECORD.regionFullName + "』" + '都在用的本地小程序',
            path: '/pages/index/index?regionCode=' + this.data.USERRECORD.regionCode,
            // imageUrl: imageUrl
        }
    },
    // 朋友圈
    // onShareTimeline: function () {
    //     return {
    //         title: "『" + this.data.USERRECORD.regionFullName + "』" + '都在用的本地小程序',
    //         path: '/pages/index/index?regionCode=' + this.data.USERRECORD.regionCode,
    //         // 自定义图片路径，可以是本地文件或者网络图片。支持 PNG 及 JPG，显示图片长宽比是 1:1。	默认使用小程序 Logo	
    //         // imageUrl: 
    //     }
    // },
    //点击商店跳转
    toStore() {
        wx.navigateTo({
            url: '../components/mine/store/store',
        })
    },
    //点击发布跳转
    toPublishTopics() {
        wx.navigateTo({
            url: '../components/mine/publishtopics/publishtopics',
        })
    },
    //点击收藏跳转
    toStarTopics() {
        console.log("收藏")
        wx.navigateTo({
            url: '../components/mine/star/star',
        })
    },
    // 更新用户信息
    getUserProfile() {
        var that = this
        app.getUserProfile().then(res => {
            console.log(res)
            // 更新成功刷新头像
            app.getUserAllInfo().then(res => {
                that.setData({
                    SYSUSER: res.sysUser
                })
            })
        }, err => {
            console.log("用户取消授权")
        })
    },

})