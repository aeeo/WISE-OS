Component({
  externalClasses: ["tui-btn-class"],
  behaviors: ['wx://form-field-button'],
  properties: {
    //样式类型 primary, white, danger, warning, green,blue, gray，black,brown,gray-primary,gray-danger,gray-warning,gray-green
			type: {
				type: String,
				value: 'primary'
			},
			//是否加阴影
			shadow: {
				type: Boolean,
				value: false
			},
			// 宽度 rpx或 %
			width: {
				type: String,
				value: '100%'
			},
			//高度 rpx
			height: {
				type: String,
				value: '96rpx'
			},
			//字体大小 rpx
			size: {
				type: Number,
				value: 32
			},
			bold: {
				type: Boolean,
				value: false
			},
			margin: {
				type: String,
				value: '0'
			},
			//形状 circle(圆角), square(默认方形)，rightAngle(平角)
			shape: {
				type: String,
				value: 'square'
			},
			plain: {
				type: Boolean,
				value: false
			},
			link:{
				type: Boolean,
				value: false
			},
			disabled: {
				type: Boolean,
				value: false
			},
			//禁用后背景是否为灰色 （非空心button生效）
			disabledGray: {
				type: Boolean,
				value: false
			},
			loading: {
				type: Boolean,
				value: false
			},
			formType: {
				type: String,
				value: ''
			},
			openType: {
				type: String,
				value: ''
			},
			index: {
				type: [Number, String],
				value: 0
			},
			//是否需要阻止重复点击【默认200ms】
			preventClick: {
				type: Boolean,
				value: true
			}
  },
  data: {
		time: 0
  },
  methods: {
    handleClick() {
			if (this.data.disabled) return false;
			if (this.data.preventClick) {
				if(new Date().getTime() - this.data.time <= 200) return;
				this.data.time = new Date().getTime();
				setTimeout(() => {
					this.data.time = 0;
				}, 200);
			}
      this.triggerEvent('click', {
        index: Number(this.data.index)
      });
    },
    bindgetuserinfo({detail = {}} = {}) {
      this.triggerEvent('getuserinfo', detail);
    },
    bindcontact({
      detail = {}
    } = {}) {
      this.triggerEvent('contact', detail);
    },
    bindgetphonenumber({
      detail = {}
    } = {}) {
      this.triggerEvent('getphonenumber', detail);
    },
    binderror({
      detail = {}
    } = {}) {
      this.triggerEvent('error', detail);
    }
  }
})