
Component({
  externalClasses:['tui-drawer-class'],
  properties: {
    visible: {
      type: Boolean,
      value: false
    },
    mask: {
      type: Boolean,
      value: true
    },
    maskClosable: {
      type: Boolean,
      value: true
    },
    mode: {
      type: String,
      value: 'true' // left right
    },
		//drawer z-index
		zIndex: {
			type: [Number, String],
			value: 9998
		},
		//mask z-index
		maskZIndex: {
			type: [Number, String],
			value: 9996
		}
  },
  methods: {
    handleMaskClick() {
      if (!this.data.maskClosable) {
        return;
      }
      this.triggerEvent('close', {});
    },
    stop(){}
  }
})
