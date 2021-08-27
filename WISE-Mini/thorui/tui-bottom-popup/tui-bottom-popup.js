Component({
  externalClasses: ["tui-popup-class"],
  properties: {
    //是否需要mask
    mask: {
      type: Boolean,
      value: true
    },
    //控制显示
    show: {
      type: Boolean,
      value: false
    },
    //背景颜色
    backgroundColor: {
      type: String,
      value: "#fff"
    },
    //高度 rpx
    height: {
      type: Number,
      value: 0
    },
    //设置圆角
    radius: {
      type: Boolean,
      value: true
    },
		zIndex: {
			type: [Number, String],
			value: 997
		},
		maskZIndex: {
			type: [Number, String],
			value: 996
		},
    //弹层显示时，垂直方向移动的距离
    translateY: {
      type: String,
      value: '0'
    }
  },
  methods: {
    handleClose() {
      if (!this.data.show) return;
      this.triggerEvent('close', {});
    },
    forbid(){}
  }
})