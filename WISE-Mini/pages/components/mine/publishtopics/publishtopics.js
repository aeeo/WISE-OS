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
        //动画
        toggleDelay: false,
        list: [{
            name: 'scale-down',
            color: 'orange'
        }],
        //顶部加载条
        isLoad: false,
        getTopicFlag: false,
        pageNo: 1,
        onReachBottomLoading: false,
        modalName: '',
        showActionsheet: false,
        actionGroups: [{
            text: '编辑',
            value: 0
        }, {
            text: '删除',
            type: 'warn',
            value: 1
        }]
    },
    onLaunch() {

    },
    onLoad() {
        this.setData({
            UPLOAD_IMAGE: app.globalData.UPLOAD_IMAGE
        })
        //获取帖子列表
        this.waitPublishTopicList()
    },
    //等待加载成功
    waitPublishTopicList() {
        var that = this
        that.setData({
            loadProgressShow: true
        })
        that.getPublisTopicLists()
    },
    //mark:获取帖子列表
    getPublisTopicLists(isRefresh) {
        var that = this
        if (isRefresh) {
            that.data.topicLists = []
        }
        that.setData({
            getTopicFlag: false
        })
        let url = app.globalData.HOSTURL + '/bbs/bbsTopic/wise/mini/queryPagePublishFullList?pageNo=' + this.data.pageNo + '&username=' + wx.getStorageSync('ALLINFO').sysUser.username
        app.wxRequest('get', url, '').then(res => {
            res.data.result.records.forEach((item) => {
                item.userRole = item.userRole.substring(4)
                // 添加动画属性
                item.exeCuteAnimation = item.userIsPraise
                //正则去除html标签
                item.content = item.content.replace(/<\/?.+?\/?>/g, '')
                // 去除跳转标签
                item.content = item.content.replace(/(?=!_).+(?:_!)/g, '')
                item.createTime = formatUtil.showDate(new Date(item.createTime.replace(/-/g, '/')))
                // item.updateTime = formatUtil.showDate(new Date(item.updateTime.replace(/-/g, '/')))
                item.publicTime = formatUtil.showDate(new Date(item.publicTime.replace(/-/g, '/')))
                item.editTime = formatUtil.showDate(new Date(item.editTime.replace(/-/g, '/')))
            })
            //列表追加
            let tempList = that.data.topicLists
            for (var item in res.data.result.records) {
                tempList.push(res.data.result.records[item])
            }
            that.setData({
                topicLists: tempList,
                getTopicFlag: true
            })
            that.setData({
                isLoad: true,
                onReachBottomLoading: true
            })
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
    //卡片跳转
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
        this.waitPublishTopicList()
    },
    onPullDownRefresh() {
        this.setData({
            onReachBottomLoading: false,
            topicLists: [],
            pageNo: 0
        })
        this.waitPublishTopicList()
    },
    //删除帖子确认
    deleteTopicMode(topicId) {
        var that = this
        wx.showModal({
            cancelColor: '#39b54a',
            confirmColor: '#000000',
            title: '删除',
            content: "删除后不可恢复，确认要删除这条信息吗？",
            showCancel: true,
            success(res) {
                if (res.confirm) {
                    wx.showToast({
                        title: '已删除',
                        icon: "none"
                    })
                    that.deleteTopic(topicId)
                } else if (res.cancel) {
                    console.log('用户点击取消')
                }
            }
        })
    },
    // 删除帖子
    deleteTopic(topicId) {
        wx.showLoading({
            title: '正在删除...',
        })
        var that = this
        let url = app.globalData.HOSTURL + '/bbs/bbsTopic/wise/mini/deletePublishTopic?topicId=' + topicId
        app.wxRequest('delete', url, '').then(res => {
            if (res.data.code == 200) {
                // 移除贴子
                var topicListTem = []
                for (var item in that.data.topicLists) {
                    if (that.data.topicLists[item].id != topicId) {
                        topicListTem.push(that.data.topicLists[item])
                    }
                }
                that.setData({
                    topicLists: topicListTem
                })
                wx.showToast({
                    title: '删除成功',
                    icon: 'none'
                })
                //获取帖子列表
                that.getPublisTopicLists(true)
            } else {
                wx.showToast({
                    title: '删除失败',
                    icon: 'none'
                })
            }
        }, err => {
            that.setData({
                topicLists: res.data.result.records,
                getTopicFlag: false
            })
        })
    },
    hideModal(e) {
        this.setData({
            modalName: null
        })
    },
    //点击topic图片放大预览
    clickTopicImage(event) {
        var imageList = []
        for (var itemImage in event.currentTarget.dataset.imagelist) {
            imageList.push(this.data.UPLOAD_IMAGE + event.currentTarget.dataset.imagelist[itemImage].topicImage + app.globalData.ARTWORK)
        }
        let x = this.data.UPLOAD_IMAGE + event.currentTarget.id + app.globalData.ARTWORK
        console.log(imageList, x)
        wx.previewImage({
            urls: imageList, //需要预览的图片http链接列表，注意是数组
            current: this.data.UPLOAD_IMAGE + event.currentTarget.id + app.globalData.ARTWORK, // 当前显示图片的http链接，默认是第一个
            success: function (res) { },
            fail: function (res) { },
            complete: function (res) { },
        })
    },
    // 组件监听
    myEventListener: function (e) {

        let actionTopicIndex = e.detail.topicIndex
        let actionTopic = this.data.topicLists[actionTopicIndex]

        this.setData({
            actionTopic: actionTopic,
            showActionsheet: true
        })
    },
    // mark: 点击action，触发相应方法
    clickAction(e) {
        var that = this
        if (e.detail.index == 0) { //编辑
            wx.navigateTo({
                url: '/pages/components/issue/issue?isEdit=true',
                success: function (res) {
                    // 通过eventChannel向被打开页面传送数据
                    res.eventChannel.emit('editEventChannel', {
                        data: that.data.actionTopic
                    })
                }
            })
        } else if (e.detail.index == 1) { //删除
            var topicId = that.data.actionTopic.id
            this.deleteTopicMode(topicId) //删除帖子确认
        }
        this.setData({
            showActionsheet: false
        })
    },
})