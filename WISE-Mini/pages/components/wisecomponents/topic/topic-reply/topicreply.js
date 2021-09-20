const app = getApp();
const formatUtil = require('../../../../../utils/formatutil.js');
const emojiList = [
  ["😀", "😁", "😂", "😃", "😄", "😅", "😆", "😉", "😊", "😋", "😎", "😍", "😘", "😗", "😙", "😚", "😇", "😐", "😑", "😶", "😏", "😣", "😥", "😮", "😯", "😪", "😫", "😴", "😌", "😛", "😜", "😝", "😒", "😓", "😔", "😕", "😲", "😷", "😖", "😞", "😟", "😤", "😢", "😭", "😦", "😧", "😨", "😬", "😰", "😱", "😳", "😵", "😡", "😠", "👦", "👧", "👨", "👩", "👴", "👵", "👶", "👱", "👮", "👲", "👳", "👷", "👸", "💂", "🎅", "👰", "👼", "💆", "💇", "🙍", "🙎", "🙅", "🙆", "💁", "🙋", "🙇", "🙌", "🙏", "👤", "👥", "🚶", "🏃", "👯", "💃", "👫", "👬", "👭", "💏", "💑", "👪", "😈", "👿", "👹", "👺", "💀", "☠", "👻", "👽", "👾", "💣"],
  ["💪", "👈", "👉", "☝", "👆", "👇", "✌", "✋", "👌", "👍", "👎", "✊", "👊", "👋", "👏", "👐", "✍"],
  [
    "🙈", "🙉", "🙊", "🐵", "🐒", "🐶", "🐕", "🐩", "🐺", "🐱", "😺", "😸", "😹", "😻", "😼", "😽", "🙀", "😿", "😾", "🐈", "🐯", "🐅", "🐆", "🐴", "🐎", "🐮", "🐂", "🐃", "🐄", "🐷", "🐖", "🐗", "🐽", "🐏", "🐑", "🐐", "🐪", "🐫", "🐘", "🐭", "🐁", "🐀", "🐹", "🐰", "🐇", "🐻", "🐨", "🐼", "🐾", "🐔", "🐓", "🦆", "🦢", "🦜", "🦉", "🐣", "🐤", "🐥", "🐦", "🐧", "🐸", "🐊", "🐢", "🐍", "🐲", "🐉", "🐳", "🐋", "🐬", "🐟", "🐠", "🐡", "🐙", "🐚", "🐌", "🐛", "🐜", "🐝", "🐞", "🦋"
  ],
  ["💐", "🌸", "💮", "🌹", "🌺", "🌻", "🌼", "🌷", "🌱", "🌲", "🌳", "🌴", "🌵", "🌾", "🌿", "🍀", "🍁", "🍂", "🍃"],
  ["🌍", "🌎", "🌏", "🌐", "🌑", "🌒", "🌓", "🌔", "🌕", "🌖", "🌗", "🌘", "🌙", "🌚", "🌛", "🌜", "☀", "🌝", "🌞", "⭐", "🌟", "🌠", "☁", "⛅", "☔", "⚡", "❄", "🔥", "💧", "🌊"],
  ["🍇", "🍈", "🍉", "🍊", "🍋", "🍌", "🍍", "🍎", "🍏", "🍐", "🍑", "🍒", "🍓", "🍅", "🍆", "🌽", "🍄", "🌰", "🍞", "🍖", "🍗", "🍔", "🍟", "🍕", "🍳", "🍲", "🍱", "🍘", "🍙", "🍚", "🍛", "🍜", "🍝", "🍠", "🍢", "🍣", "🍤", "🍥", "🍡", "🍦", "🍧", "🍨", "🍩", "🍪", "🎂", "🍰", "🍫", "🍬", "🍭", "🍮", "🍯", "🍼", "☕", "🍵", "🍶", "🍷", "🍸", "🍹", "🍺", "🍻", "🍴"],
  ["🎪", "🎭", "🎨", "🎰", "🚣", "🛀", "🎫", "🏆", "⚽", "⚾", "🏀", "🏈", "🏉", "🎾", "🎱", "🎳", "⛳", "🎣", "🎽", "🎿", "🏂", "🏄", "🏇", "🏊", "🚴", "🚵", "🎯", "🎮", "🎲", "🎷", "🎸", "🎺", "🎻", "🎬"],
  ["🌋", "🗻", "🏠", "🏡", "🏢", "🏣", "🏤", "🏥", "🏦", "🏨", "🏩", "🏪", "🏫", "🏬", "🏭", "🏯", "🏰", "💒", "🗼", "🗽", "⛪", "⛲", "🌁", "🌃", "🌆", "🌇", "🌉", "🌌", "🎠", "🎡", "🎢", "🚂", "🚃", "🚄", "🚅", "🚆", "🚇", "🚈", "🚉", "🚊", "🚝", "🚞", "🚋", "🚌", "🚍", "🚎", "🚏", "🚐", "🚑", "🚒", "🚓", "🚔", "🚕", "🚖", "🚗", "🚘", "🚚", "🚛", "🚜", "🚲", "⛽", "🚨", "🚥", "🚦", "🚧", "⚓", "⛵", "🚤", "🚢", "✈", "💺", "🚁", "🚟", "🚠", "🚡", "🚀", "🎑", "🗿", "🛂", "🛃", "🛄", "🛅"],
  ["💌", "💎", "🔪", "💈", "🚪", "🚽", "🚿", "🛁", "⌛", "⏳", "⌚", "⏰", "🎈", "🎉", "🎊", "🎎", "🎏", "🎐", "🎀", "🎁", "📯", "📻", "📱", "📲", "☎", "📞", "📟", "📠", "🔋", "🔌", "💻", "💽", "💾", "💿", "📀", "🎥", "📺", "📷", "📹", "📼", "🔍", "🔎", "🔬", "🔭", "📡", "💡", "🔦", "🏮", "📔", "📕", "📖", "📗", "📘", "📙", "📚", "📓", "📃", "📜", "📄", "📰", "📑", "🔖", "💰", "💴", "💵", "💶", "💷", "💸", "💳", "✉", "📧", "📨", "📩", "📤", "📥", "📦", "📫", "📪", "📬", "📭", "📮", "✏", "✒", "📝", "📁", "📂", "📅", "📆", "📇", "📈", "📉", "📊", "📋", "📌", "📍", "📎", "📏", "📐", "✂", "🔒", "🔓", "🔏", "🔐", "🔑", "🔨", "🔫", "🔧", "🔩", "🔗", "💉", "💊", "🚬", "🔮", "🚩", "🎌", "💦", "💨"],
  ["♠", "♥", "♦", "♣", "🀄", "🎴", "🔇", "🔈", "🔉", "🔊", "📢", "📣", "💤", "💢", "💬", "💭", "♨", "🌀", "🔔", "🔕", "✡", "✝", "🔯", "📛", "🔰", "🔱", "⭕", "✅", "☑", "✔", "✖", "❌", "❎", "➕", "➖", "➗", "➰", "➿", "〽", "✳", "✴", "❇", "‼", "⁉", "❓", "❔", "❕", "❗", "©", "®", "™", "🎦", "🔅", "🔆", "💯", "🔠", "🔡", "🔢", "🔣", "🔤", "🅰", "🆎", "🅱", "🆑", "🆒", "🆓", "ℹ", "🆔", "Ⓜ", "🆕", "🆖", "🅾", "🆗", "🅿", "🆘", "🆙", "🆚", "🈁", "🈂", "🈷", "🈶", "🈯", "🉐", "🈹", "🈚", "🈲", "🉑", "🈸", "🈴", "🈳", "㊗", "㊙", "🈺", "🈵", "▪", "▫", "◻", "◼", "◽", "◾", "⬛", "⬜", "🔶", "🔷", "🔸", "🔹", "🔺", "🔻", "💠", "🔲", "🔳", "⚪", "⚫", "🔴", "🔵"],
  ["🐁", "🐂", "🐅", "🐇", "🐉", "🐍", "🐎", "🐐", "🐒", "🐓", "🐕", "🐖", "♈", "♉", "♊", "♋", "♌", "♍", "♎", "♏", "♐", "♑", "♒", "♓", "⛎"],
  ["💘", "❤", "💓", "💔", "💕", "💖", "💗", "💙", "💚", "💛", "💜", "💝", "💞", "💟", "❣"],
  ["🚂", "🚃", "🚄", "🚅", "🚆", "🚇", "🚈", "🚉", "🚊", "🚝", "🚞", "🚋", "🚌", "🚍", "🚎", "🚏", "🚐", "🚑", "🚒", "🚓", "🚔", "🚕", "🚖", "🚗", "🚘", "🚚", "🚛", "🚜", "🚲", "⛽", "🚨", "🚥", "🚦", "🚧", "⚓", "⛵", "🚣", "🚤", "🚢", "✈", "💺", "🚁", "🚟", "🚠", "🚡", "🚀"],
  ["📱", "📲", "☎", "📞", "📟", "📠", "🔋", "🔌", "💻", "💽", "💾", "💿", "📀", "🎥", "📺", "📷", "📹", "📼", "🔍", "🔎", "🔬", "🔭", "📡", "📔", "📕", "📖", "📗", "📘", "📙", "📚", "📓", "📃", "📜", "📄", "📰", "📑", "🔖", "💳", "✉", "📧", "📨", "📩", "📤", "📥", "📦", "📫", "📪", "📬", "📭", "📮", "✏", "✒", "📝", "📁", "📂", "📅", "📆", "📇", "📈", "📉", "📊", "📋", "📌", "📍", "📎", "📏", "📐", "✂", "🔒", "🔓", "🔏", "🔐", "🔑"],
  ["⬆", "↗", "➡", "↘", "⬇", "↙", "⬅", "↖", "↕", "↔", "↩", "↪", "⤴", "⤵", "🔃", "🔄", "🔙", "🔚", "🔛", "🔜", "🔝 "]
]
Component({
  // 隔离级别
  options: {
    styleIsolation: 'apply-shared'
  },
  /**
   * 组件的属性列表
   */
  properties: {
    replyTopicDetails: {
      type: Object,
      value: {}
    },
    showType: {
      type: String,
      value: 'scrollView'           //longList
    },
    //longList类型需要从外面传页码
    nextPage: {
      type: Boolean,
      value: false
    }
  },
  observers: {
    'replyTopicDetails'(val) {
      // console.log("replyTopicDetails属性值变化时执行", this.data.topicId, val)
      if (null != val && this.data.topicId != val.id) {
        this.data.topicId = val.id
        this.setData({          //如果查看不同贴子的列表，每次都需要重置带渲染，reload不带渲染
          fullReplys: []
        })
        this.reloadReply()
      }

    },
    'nextPage'(val) {
      // console.log("nextPage属性值变化时执行", this.data.replyPageNo, val)

      if (val) {
        this.getFullReplysNext()
      } else {
        this.reloadReply()
      }
    },
  },

  /**
   * 组件的初始数据
   */
  data: {
    InputBottom: 0,
    InputFocus: false,

    beReply: 0, //被点击或被长按的评论，评论点赞、评论长按都会填充

    emojiList,
    showEmojiPanel: false, // emoji面板
    replyContentInput: '',

    replyPlaceholder: "讲两句？",

    replyPageNo: 1,
    replyResult: {        //默认不相等，转圈
      current: 1,
      pages: 999
    },


    fullReplys: [],

    UPLOAD_IMAGE: app.globalData.UPLOAD_IMAGE,
    THUMBNAIL: app.globalData.THUMBNAIL,
    ARTWORK: app.globalData.ARTWORK,
    ARTWORKNOWATER: app.globalData.ARTWORKNOWATER,

    SHAREHOSTURL: app.globalData.HOSTURL, //朋友圈分享的页面使用这个主机地址
    isAnon: '', //是否调用匿名接口

    // 评论长按提示
    showCommentActionsheet: false
  },

  lifetimes: {
    attached: function () {
      var that = this
      that.data.SHAREHOSTURL = app.globalData.HOSTURL
      var obj = wx.getLaunchOptionsSync()
      console.log('启动小程序的路径:', obj.path)
      console.log('启动小程序的场景值:', obj.scene)
      console.log('启动小程序的 query 参数:', obj.query)
      console.log('来源信息:', obj.shareTicket)
      console.log('来源信息参数appId:', obj.referrerInfo.appId)
      console.log('来源信息传过来的数据:', obj.referrerInfo.extraData)
      this.initEnterType(obj)

      if (null != this.data.topicId && "" != this.data.topicId && undefined != this.data.topicId) {
        this.getFullReplys(this.data.topicId, this.data.replyPageNo)
      }
    },
    detached: function () {
      // 在组件实例被从页面节点树移除时执行
    },
  },
  /**
   * 组件的方法列表
   */
  methods: {
    initEnterType(obj) {
      var that = this
      // 在组件实例进入页面节点树时执行
      if (obj.scene == 1154) { //从朋友圈看到的单页面
        that.data.topicId = obj.topicId
        that.data.isAnon = '/anon'
      } else {
        if (null != that.data.replyTopicDetails) {
          that.data.topicId = that.data.replyTopicDetails.id
        }
      }
    },
    //mark:点击评论
    //回复评论 beReply input聚焦
    relpyComment(event) {
      var that = this
      that.setData({
        beReply: event.currentTarget.dataset.bereply,
        parentReplyId: event.currentTarget.dataset.parentreplyid,
        replyPlaceholder: '回复@' + event.currentTarget.dataset.bereply.realname + '：',
        InputFocus: true,
      })
      console.log(that.data.beReply)

      // const y = event.touches[0].pageY
      // wx.pageScrollTo({
      //   scrollTop: y - app.globalData.CustomBar - 100,
      //   duration: 300
      // })
    }, // 输入表情
    inputEmoji(e) {
      console.log(e)
      this.setData({
        replyContentInput: this.data.replyContentInput + e.currentTarget.dataset.emoji
      })
    },
    //mark: 评论框获得焦点
    InputFocus(e) {
      console.log("获得焦点")
      this.setData({
        InputBottom: e.detail.height,
      })
    },
    // mark:评论框失去焦点
    InputBlur(e) {
      console.log("失去焦点")
      var that = this
      if (this.data.showEmojiPanel) {
        that.setData({
          showEmojiPanel: false,
          replyPlaceholder: '',
          InputBottom: 0
        })
      } else {
        that.setData({
          InputBottom: 0
        })
      }
      // 如果当前评论框内容为空，则清空回复对象，重置默认对贴子评论
      if (that.data.replyContentInput == null || that.data.replyContentInput == "") {
        that.setData({
          parentReplyId: 0,
          replyPlaceholder: "讲两句？",
        })
      }
    }, // 打开/关闭emoji面板
    openEmojiPanel() {
      setTimeout(() => {
        if (!this.data.showEmojiPanel) {
          console.log("openEmojiPanel")
          this.setData({
            showEmojiPanel: true,
            InputBottom: 250,
          })
          wx.hideKeyboard({
            complete: res => {
              console.log('hideKeyboard res', res)
            }
          })
        } else {
          this.setData({
            showEmojiPanel: false,
            InputBottom: 0,
          })
        }
      }, 100)
    },
    // mark:发布评论
    comment() {
      wx.showLoading({
        title: '请稍等',
      })
      var that = this
      // 收起评论框
      that.setData({
        showEmojiPanel: false,
        InputBottom: 0
      })
      // 先查询一下用户是否授权
      // 授权才能评论
      if (app.globalData.HASUSERINFO) {
        console.log("已经授权")
        if (this.data.replyContentInput == '') {
          wx.showToast({
            title: '评论内容不能为空哦',
            icon: 'none'
          })
          return
        }
        var that = this
        var SYSUSER = wx.getStorageSync('ALLINFO').sysUser
        var realname = SYSUSER.realname
        var avatar = SYSUSER.avatar
        //beReply是用户当前被回复的信息，回复帖子0，回复评论1
        let url = that.data.SHAREHOSTURL + '/bbs/bbsReply/wise/mini/add?topicId=' + that.data.topicId
        console.log(that.data.beReply)
        var dataRequest = {
          content: that.data.replyContentInput,
          topicId: that.data.topicId,
          pid: that.data.parentReplyId,
          beReplyUsername: that.data.beReply.createBy,
          beReplyRealname: that.data.beReply.beReplyRealname,
          realname: realname,
          avatar: avatar,
          sysOrgCode: that.data.replyTopicDetails.sysOrgCode
        }
        console.log(dataRequest)
        app.wxRequest('post', url, dataRequest).then(res => {
          if (res.data.code == 200) {

            //清空评论框
            that.setData({
              replyContentInput: '',
              replyPlaceholder: '',
            })
            //提交完成 beReply = 0
            that.data.beReply = 0

          } else {
            wx.showToast({
              title: res.data.message,
              icon: 'none',
              duration: 2000
            })
          }
          //提交完成 beReply.id = 0
          // this.data.beReply.id = 0
          wx.hideLoading()
          that.reloadReply().then(res => {
            wx.showToast({
              title: '评论成功',
              icon: "none"
            })
          })
        }, err => {
          console.log(err)
          wx.showToast({
            title: '啊哦，服务器开小差了。',
            icon: true
          })
          wx.hideLoading()
          that.reloadReply()

          //提交完成 beReply.id = 0
          // this.data.beReply.id = 0
        })
      } else {
        // 获取授权
        app.getUserProfile()
      }
    },
    //获取评论内容
    replyContent(e) {
      this.setData({
        replyContentInput: e.detail.value
      })
    }, // 长按评论删除
    longpressComment(e) {
      console.log("长按！", e)
      var that = this

      let actionCommentGroupsTemp = [{
        text: '复制',
        value: 0
      }]
      let bereply = e.currentTarget.dataset.bereply
      let SYSUSER = wx.getStorageSync("SYSUSER")
      console.log(SYSUSER)
      // 自己不能举报自己发布的评论
      if (bereply.createBy != SYSUSER.username) {
        actionCommentGroupsTemp.push({
          text: '举报',
          value: 1
        })
      }

      // 评论发布者可以删除自己评论    帖子发布者可以删除任意评论   
      if (that.data.replyTopicDetails.createBy == SYSUSER.username || bereply.createBy == SYSUSER.username) {
        actionCommentGroupsTemp.push({
          text: '删除',
          value: 2
        })
      }

      that.setData({
        actionCommentGroups: actionCommentGroupsTemp,
        showCommentActionsheet: true,
        beReply: e.currentTarget.dataset.bereply,
      })
    },
    // mark: 点击评论的action，触发相应方法
    /**
     * 复制: 0
     * 举报: 1
     * 删除: 2
     */
    clickCommentAction(e) {
      console.log(e)
      var that = this

      if (e.detail.value == 0) {
        that.copyCommentAction(that.data.beReply)
      } else if (e.detail.value == 1) {
        that.warnCommentByIdAction(that.data.beReply)
      } else if (e.detail.value == 2) {
        that.deleteCommentByIdAction(that.data.beReply)
      }
      this.setData({
        showCommentActionsheet: false
      })
    },
    // mark: 复制评论
    copyCommentAction(beReply) {
      console.log(beReply)
      wx.setClipboardData({
        data: beReply.content,
        success(res) {

        }
      })
    },
    // mark: 删除评论
    deleteCommentByIdAction(beReply) {
      var that = this
      let url = that.data.SHAREHOSTURL + '/bbs/bbsReply/wise/mini/delete?id=' + beReply.id
      app.wxRequest('delete', url, '').then(res => {
        if (res.data.code == 200) {
          wx.showToast({
            title: '删除评论成功！',
            icon: 'none'
          })

          that.reloadReply()
        }
      }, err => {
        wx.showToast({
          title: '删除评论失败！',
          icon: 'none'
        })

        that.reloadReply()
      })
    },

    // mark: 举报评论
    warnCommentByIdAction(beReply) {
      var that = this
      wx.showModal({
        // cancelColor: '#39b54a',
        confirmColor: '#000000',
        title: '举报',
        content: "您确认要举报这条评论吗？",
        showCancel: true,
        success(res) {
          if (res.confirm) {
            var bbsInform = {}
            bbsInform.replyId = beReply.id
            bbsInform.type = 2 //举报评论
            bbsInform.informUsername = wx.getStorageSync('ALLINFO').sysUser.username
            bbsInform.beInformUsername = beReply.createBy

            let url = that.data.SHAREHOSTURL + '/bbs/bbsReply/wise/mini/informReply'
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
    // mark:获取评论列表
    getFullReplys(topicId, replyPageNo) {
      if (null == topicId || "" == topicId || undefined == topicId) {
        return
      }
      console.log("getFullReplys", topicId, replyPageNo)
      var that = this
      let bbsReply = {
        "topicId": topicId,
        "status": 1
      }
      const url = that.data.SHAREHOSTURL + '/bbs/bbsReply/wise/mini/rootFullList' + that.data.isAnon + '?pageNo=' + replyPageNo
      // console.log(url)
      return app.wxRequest('post', url, bbsReply).then(res => {
        console.log(res)
        that.setData({
          replyResult: res.data.result
        })
        res.data.result.records.forEach(item => {
          // 评论时间格式化
          item.createTime = formatUtil.showReplyDate(new Date(item.createTime))
          // 添加动画属性
          item.exeCuteAnimation = item.userIsPraise
          // 评论背景色
          item.backgroundColorIndex = Math.floor(Math.random() * 10);
          // 评论头像url拼接，这里不能在wxml里面拼接，因为在发布评论的时候拿的是微信的头像接口，微信返回的是全连接
          item.avatar = that.data.UPLOAD_IMAGE + item.avatar + that.data.THUMBNAIL
          // console.log(item.avatar)
          // 子评论时间格式化
          if (item.childFullReply.length != 0) {
            item.childFullReply.forEach(itemChild => {
              // 子评论时间格式化
              itemChild.createTime = formatUtil.showReplyDate(new Date(itemChild.createTime))
              // 添加动画属性
              itemChild.exeCuteAnimation = itemChild.userIsPraise
              // 评论头像url拼接，这里不能在wxml里面拼接，因为在发布评论的时候拿的是微信的头像接口，微信返回的是全连接
              itemChild.avatar = that.data.UPLOAD_IMAGE + itemChild.avatar + that.data.THUMBNAIL
            })
          }
        })
        let fullReplysTem = that.data.fullReplys
        for (var item in res.data.result.records) {
          fullReplysTem.push(res.data.result.records[item])
        }
        // console.log(that.data.fullReplys)
        // console.log(fullReplysTem)
        that.setData({
          fullReplys: fullReplysTem,
          isPull: false
        })
        // console.log(that.data.fullReplys)
        return true
      }, err => {
        return false
      })
    }, // 子评论点赞
    clickChildReplyPraise(e) {
      var that = this
      console.log(e)
      //若果没有点赞  点赞并展示动画
      if (!this.data.fullReplys[e.target.dataset.bindex].childFullReply[e.target.dataset.bindexchild].userIsPraise) {
        var topicListTem = this.data.fullReplys
        topicListTem[e.target.dataset.bindex].childFullReply[e.target.dataset.bindexchild].userIsPraise = true
        topicListTem[e.target.dataset.bindex].childFullReply[e.target.dataset.bindexchild].exeCuteAnimation = true
        topicListTem[e.target.dataset.bindex].childFullReply[e.target.dataset.bindexchild].praise = topicListTem[e.target.dataset.bindex].childFullReply[e.target.dataset.bindexchild].praise + 1
        this.setData({
          fullReplys: topicListTem,
        })
        let url = that.data.SHAREHOSTURL + '/bbs/bbsUserPraise/wise/mini/clickReplyPraise?replyId=' + e.target.id + '&isPraise=' + true
        app.wxRequest('post', url, '').then(res => {

        }, err => {

        })
      } else {
        var topicListTem = this.data.fullReplys
        topicListTem[e.target.dataset.bindex].childFullReply[e.target.dataset.bindexchild].userIsPraise = false
        topicListTem[e.target.dataset.bindex].childFullReply[e.target.dataset.bindexchild].exeCuteAnimation = false
        topicListTem[e.target.dataset.bindex].childFullReply[e.target.dataset.bindexchild].praise = topicListTem[e.target.dataset.bindex].childFullReply[e.target.dataset.bindexchild].praise - 1
        this.setData({
          fullReplys: topicListTem,
        })
        let url = that.data.SHAREHOSTURL + '/bbs/bbsUserPraise/wise/mini/clickReplyPraise?replyId=' + e.target.id + '&isPraise=' + false
        app.wxRequest('post', url, '').then(res => {

        }, err => {

        })
      }
    }, // 主评论点赞
    clickReplyPraise(e) {
      var that = this
      console.log(e)
      //若果没有点赞  点赞并展示动画
      if (!this.data.fullReplys[e.target.dataset.bindex].userIsPraise) {
        var topicListTem = this.data.fullReplys
        topicListTem[e.target.dataset.bindex].userIsPraise = true
        topicListTem[e.target.dataset.bindex].exeCuteAnimation = true
        topicListTem[e.target.dataset.bindex].praise = topicListTem[e.target.dataset.bindex].praise + 1
        this.setData({
          fullReplys: topicListTem,
        })
        let url = that.data.SHAREHOSTURL + '/bbs/bbsUserPraise/wise/mini/clickReplyPraise?replyId=' + e.target.id + '&isPraise=' + true
        app.wxRequest('post', url, '').then(res => {

        }, err => {

        })
      } else {
        var topicListTem = this.data.fullReplys
        topicListTem[e.target.dataset.bindex].userIsPraise = false
        topicListTem[e.target.dataset.bindex].exeCuteAnimation = false
        topicListTem[e.target.dataset.bindex].praise = topicListTem[e.target.dataset.bindex].praise - 1
        this.setData({
          fullReplys: topicListTem,
        })
        let url = that.data.SHAREHOSTURL + '/bbs/bbsUserPraise/wise/mini/clickReplyPraise?replyId=' + e.target.id + '&isPraise=' + false
        app.wxRequest('post', url, '').then(res => {

        }, err => {

        })
      }
    },
    getFullReplysNext() {
      // console.log(this.data.replyResult.pages, this.data.replyPageNo)
      if (this.data.replyResult.pages > this.data.replyPageNo) {
        // console.log(this.data.replyPageNo)
        this.data.replyPageNo = this.data.replyPageNo + 1
        this.getFullReplys(this.data.topicId, this.data.replyPageNo)
      }
    },
    socallRefresh() {
      this.reloadReply().then(res => {
        if (res) {
          wx.showToast({
            title: '刷新成功',
            icon: 'success'
          })
        }
      })
    },
    reloadReply() {
      this.data.fullReplys = []
      this.data.replyPageNo = 1
      this.data.replyResult = {
        current: 1,
        pages: 2
      }
      this.setData({
        replyResult: this.data.replyResult,
      })


      return this.getFullReplys(this.data.topicId, this.data.replyPageNo)
    }
  }
})