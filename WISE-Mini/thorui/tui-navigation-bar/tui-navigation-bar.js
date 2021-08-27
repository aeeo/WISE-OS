Component({
  properties: {
    //NavigationBar标题
    title: {
      type: String,
      value: ''
    },
    //NavigationBar标题颜色
    color: {
      type: String,
      value: '#fff'
    },
    //NavigationBar背景颜色 rgb
    backgroundColor: {
      type: String,
      value: '86,119,252'
    },
    //是否需要分割线
    splitLine: {
      type: Boolean,
      value: false
    },
    //是否设置不透明度
    isOpcity: {
      type: Boolean,
      value: true
    },
    //滚动条滚动距离
    scrollTop: {
      type: [Number, String],
      value: 0,
      observer(val) {
        if (this.data.isOpcity) {
          this.opcityChange();
        }
      }
    },
    /*
     isOpcity 为true时生效
     opcity=scrollTop /windowWidth * scrollRatio
    */
    scrollRatio: {
      type: [Number, String],
      value: 0.3
    },
    //是否自定义header内容
    isCustom: {
      type: Boolean,
      value: false
    },
    //是否沉浸式
    isImmersive: {
      type: Boolean,
      value: true
    },
    isFixed: {
      type: Boolean,
      value: true
    },
		//是否开启高斯模糊效果[仅在支持的浏览器有效果]
		backdropFilter: {
			type: Boolean,
			value: false
		}
  },
  data: {
    width: 375, //header宽度
    left: 375, //小程序端 左侧距胶囊按钮距离
    height: 44, //header高度
    top: 0,
    scrollH: 1, //滚动总高度,计算opcity
    opcity: 0, //0-1
    statusBarHeight: 0 //状态栏高度
  },
  lifetimes: {
    attached: function () {
      this.setData({
        opcity: this.data.isOpcity ? this.data.opcity : 1
      })
      let obj = wx.getMenuButtonBoundingClientRect();
      wx.getSystemInfo({
        success: res => {
          let height = this.data.height;
          if (this.data.isImmersive) {
            height = obj.top ? obj.top + obj.height + 8 : res.statusBarHeight + 44;
          }
          this.setData({
            statusBarHeight: res.statusBarHeight,
            width: res.windowWidth,
            left: obj.left || res.windowWidth,
            height: height,
            scrollH: res.windowWidth * this.data.scrollRatio,
            top: obj.top ? obj.top + (obj.height - 32) / 2 : res.statusBarHeight + 6
          }, () => {
            this.triggerEvent('init', {
              width: this.data.width,
              height: this.data.height,
              left: obj.left,
              top: this.data.top,
              statusBarHeight: this.data.statusBarHeight,
              opcity: this.data.opcity
            });
          })
        }
      });
    }
  },
  methods: {
    opcityChange() {
			let scroll = this.data.scrollTop <= 1 ? 0 : this.data.scrollTop;
			let opcity = scroll / this.data.scrollH;
			if ((this.data.opcity >= 1 && opcity >= 1) || (this.data.opcity == 0 && opcity == 0)) {
				return;
      }
      this.setData({
        opcity:opcity
      })
      console.log(scroll)
			this.triggerEvent('change', {
				opcity: opcity
			});
		}
  }
})