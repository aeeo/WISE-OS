var app = getApp();
const htmlSnip =
	`
	<p>
	<p class="MsoNormal" style="text-indent:21.0pt;">
		没有套路，没有运费，陨石数量够了直接兑换，只需要填写地址即可，不需要出一分钱。<span></span>
	</p>
	<p class="MsoNormal" style="text-indent:21.0pt;">
		使用平台会获得陨石，可以兑换演唱会们票、定制礼品、零食大礼包等，放心不需要刻意去积攒，规则算很友好，可以攒够，不会像一些平台一样越攒越慢、望梅止渴。<span></span>
	</p>
</p>
<p>
	<br />
</p>
`
const accessRole =
	`
<div align="center">
	<table class="MsoNormalTable" border="1" cellspacing="0" cellpadding="0" width="265" style="width:198.9pt;border-collapse:collapse;border:none;">
		<tbody>
			<tr>
				<td width="86" style="border:inset black 1.0pt;">
					<p class="MsoNormal" align="center" style="text-align:center;">
						<span style="font-size:9.0pt;font-family:宋体;">事项</span><span style="font-size:9.0pt;font-family:&quot;"></span>
					</p>
				</td>
				<td width="85" style="border:inset black 1.0pt;">
					<p class="MsoNormal" align="center" style="text-align:center;">
						<span style="font-size:9.0pt;font-family:宋体;">次数</span><span style="font-size:9.0pt;font-family:&quot;"></span>
					</p>
				</td>
				<td width="94" style="border:inset black 1.0pt;">
					<p class="MsoNormal" align="center" style="text-align:center;">
						<span style="font-size:9.0pt;font-family:宋体;">陨石</span><span style="font-size:9.0pt;font-family:&quot;"></span>
					</p>
				</td>
			</tr>
			<tr>
				<td width="86" style="border:inset black 1.0pt;">
					<p class="MsoNormal" align="center" style="text-align:center;">
						<span style="font-size:9.0pt;font-family:宋体;">点赞</span><span style="font-size:9.0pt;font-family:&quot;"></span>
					</p>
				</td>
				<td width="85" style="border:inset black 1.0pt;">
					<p class="MsoNormal" align="center" style="text-align:center;">
						<span style="font-size:9.0pt;font-family:&quot;">100</span><span style="font-size:9.0pt;font-family:宋体;">次</span><span style="font-size:9.0pt;font-family:&quot;">/</span><span style="font-size:9.0pt;font-family:宋体;">天</span><span style="font-size:9.0pt;font-family:&quot;"></span>
					</p>
				</td>
				<td width="94" style="border:inset black 1.0pt;">
					<p class="MsoNormal" align="center" style="text-align:center;">
						<span style="font-size:9.0pt;font-family:&quot;">1</span><span style="font-size:9.0pt;font-family:宋体;">块</span><span style="font-size:9.0pt;font-family:&quot;">/</span><span style="font-size:9.0pt;font-family:宋体;">次</span><span style="font-size:9.0pt;font-family:&quot;"></span>
					</p>
				</td>
			</tr>
			<tr>
				<td width="86" style="border:inset black 1.0pt;">
					<p class="MsoNormal" align="center" style="text-align:center;">
						<span style="font-size:9.0pt;font-family:宋体;">被赞</span><span style="font-size:9.0pt;font-family:&quot;"></span>
					</p>
				</td>
				<td width="85" style="border:inset black 1.0pt;">
					<p class="MsoNormal" align="center" style="text-align:center;">
						<span style="font-size:9.0pt;font-family:宋体;">不限制</span><span style="font-size:9.0pt;font-family:&quot;"></span>
					</p>
				</td>
				<td width="94" style="border:inset black 1.0pt;">
					<p class="MsoNormal" align="center" style="text-align:center;">
						<span style="font-size:9.0pt;font-family:&quot;">2</span><span style="font-size:9.0pt;font-family:宋体;">块</span><span style="font-size:9.0pt;font-family:&quot;">/</span><span style="font-size:9.0pt;font-family:宋体;">次</span><span style="font-size:9.0pt;font-family:&quot;"></span>
					</p>
				</td>
			</tr>
			<tr>
				<td width="86" style="border:inset black 1.0pt;">
					<p class="MsoNormal" align="center" style="text-align:center;">
						<span style="font-size:9.0pt;font-family:宋体;">收藏</span><span style="font-size:9.0pt;font-family:&quot;"></span>
					</p>
				</td>
				<td width="85" style="border:inset black 1.0pt;">
					<p class="MsoNormal" align="center" style="text-align:center;">
						<span style="font-size:9.0pt;font-family:&quot;">100</span><span style="font-size:9.0pt;font-family:宋体;">次</span><span style="font-size:9.0pt;font-family:&quot;">/</span><span style="font-size:9.0pt;font-family:宋体;">天</span><span style="font-size:9.0pt;font-family:&quot;"></span>
					</p>
				</td>
				<td width="94" style="border:inset black 1.0pt;">
					<p class="MsoNormal" align="center" style="text-align:center;">
						<span style="font-size:9.0pt;font-family:&quot;">5</span><span style="font-size:9.0pt;font-family:宋体;">块</span><span style="font-size:9.0pt;font-family:&quot;">/</span><span style="font-size:9.0pt;font-family:宋体;">次</span><span style="font-size:9.0pt;font-family:&quot;"></span>
					</p>
				</td>
			</tr>
			<tr>
				<td width="86" style="border:inset black 1.0pt;">
					<p class="MsoNormal" align="center" style="text-align:center;">
						<span style="font-size:9.0pt;font-family:宋体;">被收藏</span><span style="font-size:9.0pt;font-family:&quot;"></span>
					</p>
				</td>
				<td width="85" style="border:inset black 1.0pt;">
					<p class="MsoNormal" align="center" style="text-align:center;">
						<span style="font-size:9.0pt;font-family:宋体;">不限制</span><span style="font-size:9.0pt;font-family:&quot;">/</span><span style="font-size:9.0pt;font-family:宋体;">天</span><span style="font-size:9.0pt;font-family:&quot;"></span>
					</p>
				</td>
				<td width="94" style="border:inset black 1.0pt;">
					<p class="MsoNormal" align="center" style="text-align:center;">
						<span style="font-size:9.0pt;font-family:&quot;">20</span><span style="font-size:9.0pt;font-family:宋体;">块</span><span style="font-size:9.0pt;font-family:&quot;">/</span><span style="font-size:9.0pt;font-family:宋体;">次</span><span style="font-size:9.0pt;font-family:&quot;"></span>
					</p>
				</td>
			</tr>
			<tr>
				<td width="86" style="border:inset black 1.0pt;">
					<p class="MsoNormal" align="center" style="text-align:center;">
						<span style="font-size:9.0pt;font-family:宋体;">评论</span><span style="font-size:9.0pt;font-family:&quot;"></span>
					</p>
				</td>
				<td width="85" style="border:inset black 1.0pt;">
					<p class="MsoNormal" align="center" style="text-align:center;">
						<span style="font-size:9.0pt;font-family:&quot;">20</span><span style="font-size:9.0pt;font-family:宋体;">条</span><span style="font-size:9.0pt;font-family:&quot;">/</span><span style="font-size:9.0pt;font-family:宋体;">天</span><span style="font-size:9.0pt;font-family:&quot;"></span>
					</p>
				</td>
				<td width="94" style="border:inset black 1.0pt;">
					<p class="MsoNormal" align="center" style="text-align:center;">
						<span style="font-size:9.0pt;font-family:&quot;">5</span><span style="font-size:9.0pt;font-family:宋体;">块</span><span style="font-size:9.0pt;font-family:&quot;">/</span><span style="font-size:9.0pt;font-family:宋体;">条</span><span style="font-size:9.0pt;font-family:&quot;"></span>
					</p>
				</td>
			</tr>
			<tr>
				<td width="86" style="border:inset black 1.0pt;">
					<p class="MsoNormal" align="center" style="text-align:center;">
						<span style="font-size:9.0pt;font-family:宋体;">被评论</span><span style="font-size:9.0pt;font-family:&quot;"></span>
					</p>
				</td>
				<td width="85" style="border:inset black 1.0pt;">
					<p class="MsoNormal" align="center" style="text-align:center;">
						<span style="font-size:9.0pt;font-family:宋体;">不限制</span><span style="font-size:9.0pt;font-family:&quot;"></span>
					</p>
				</td>
				<td width="94" style="border:inset black 1.0pt;">
					<p class="MsoNormal" align="center" style="text-align:center;">
						<span style="font-size:9.0pt;font-family:&quot;">10</span><span style="font-size:9.0pt;font-family:宋体;">块</span><span style="font-size:9.0pt;font-family:&quot;">/</span><span style="font-size:9.0pt;font-family:宋体;">条</span><span style="font-size:9.0pt;font-family:&quot;"></span>
					</p>
				</td>
			</tr>
			<tr>
				<td width="86" style="border:inset black 1.0pt;">
					<p class="MsoNormal" align="center" style="text-align:center;">
						<span style="font-size:9.0pt;font-family:宋体;">打开小程序</span><span style="font-size:9.0pt;font-family:&quot;"></span>
					</p>
				</td>
				<td width="85" style="border:inset black 1.0pt;">
					<p class="MsoNormal" align="center" style="text-align:center;">
						<span style="font-size:9.0pt;font-family:宋体;">不限制</span><span style="font-size:9.0pt;font-family:&quot;"></span>
					</p>
				</td>
				<td width="94" style="border:inset black 1.0pt;">
					<p class="MsoNormal" align="center" style="text-align:center;">
						<span style="font-size:9.0pt;font-family:&quot;">2</span><span style="font-size:9.0pt;font-family:宋体;">块</span><span style="font-size:9.0pt;font-family:&quot;">/</span><span style="font-size:9.0pt;font-family:宋体;">次</span><span style="font-size:9.0pt;font-family:&quot;"></span>
					</p>
				</td>
			</tr>
			<tr>
				<td width="86" style="border:inset black 1.0pt;">
					<p class="MsoNormal" align="center" style="text-align:center;">
						<span style="font-size:9.0pt;font-family:宋体;">发布留言</span><span style="font-size:9.0pt;font-family:&quot;"></span>
					</p>
				</td>
				<td width="85" style="border:inset black 1.0pt;">
					<p class="MsoNormal" align="center" style="text-align:center;">
						<span style="font-size:9.0pt;font-family:&quot;">5</span><span style="font-size:9.0pt;font-family:宋体;">条</span><span style="font-size:9.0pt;font-family:&quot;">/</span><span style="font-size:9.0pt;font-family:宋体;">天</span><span style="font-size:9.0pt;font-family:&quot;"></span>
					</p>
				</td>
				<td width="94" style="border:inset black 1.0pt;">
					<p class="MsoNormal" align="center" style="text-align:center;">
						<span style="font-size:9.0pt;font-family:&quot;">5</span><span style="font-size:9.0pt;font-family:宋体;">块</span><span style="font-size:9.0pt;font-family:&quot;">/</span><span style="font-size:9.0pt;font-family:宋体;">条</span><span style="font-size:9.0pt;font-family:&quot;"></span>
					</p>
				</td>
			</tr>
			<tr>
				<td width="86" style="border:inset black 1.0pt;">
					<p class="MsoNormal" align="center" style="text-align:center;">
						<span style="font-size:9.0pt;font-family:宋体;">发布帖子</span><span style="font-size:9.0pt;font-family:&quot;"></span>
					</p>
				</td>
				<td width="85" style="border:inset black 1.0pt;">
					<p class="MsoNormal" align="center" style="text-align:center;">
						<span style="font-size:9.0pt;font-family:&quot;">2</span><span style="font-size:9.0pt;font-family:宋体;">条</span><span style="font-size:9.0pt;font-family:&quot;">/</span><span style="font-size:9.0pt;font-family:宋体;">天</span><span style="font-size:9.0pt;font-family:&quot;"></span>
					</p>
				</td>
				<td width="94" style="border:inset black 1.0pt;">
					<p class="MsoNormal" align="center" style="text-align:center;">
						<span style="font-size:9.0pt;font-family:&quot;">20</span><span style="font-size:9.0pt;font-family:宋体;">块</span><span style="font-size:9.0pt;font-family:&quot;">/</span><span style="font-size:9.0pt;font-family:宋体;">次</span><span style="font-size:9.0pt;font-family:&quot;"></span>
					</p>
				</td>
			</tr>
			<tr>
				<td width="86" style="border:inset black 1.0pt;">
					<p class="MsoNormal" align="center" style="text-align:center;">
						<span style="font-size:9.0pt;font-family:宋体;">反馈</span><span style="font-size:9.0pt;font-family:&quot;">BUG</span>
					</p>
				</td>
				<td width="85" style="border:inset black 1.0pt;">
					<p class="MsoNormal" align="center" style="text-align:center;">
						<span style="font-size:9.0pt;font-family:宋体;">不限制</span><span style="font-size:9.0pt;font-family:&quot;"></span>
					</p>
				</td>
				<td width="94" style="border:inset black 1.0pt;">
					<p class="MsoNormal" align="center" style="text-align:center;">
						<span style="font-size:9.0pt;font-family:&quot;">100</span><span style="font-size:9.0pt;font-family:宋体;">块</span><span style="font-size:9.0pt;font-family:&quot;">/</span><span style="font-size:9.0pt;font-family:宋体;">条</span><span style="font-size:9.0pt;font-family:&quot;"></span>
					</p>
				</td>
			</tr>
			<tr>
				<td width="86" style="border:inset black 1.0pt;">
					<p class="MsoNormal" align="center" style="text-align:center;">
						<span style="font-size:9.0pt;font-family:宋体;">精华帖</span><span style="font-size:9.0pt;font-family:&quot;"></span>
					</p>
				</td>
				<td width="85" style="border:inset black 1.0pt;">
					<p class="MsoNormal" align="center" style="text-align:center;">
						<span style="font-size:9.0pt;font-family:宋体;">不限制</span><span style="font-size:9.0pt;font-family:&quot;"></span>
					</p>
				</td>
				<td width="94" style="border:inset black 1.0pt;">
					<p class="MsoNormal" align="center" style="text-align:center;">
						<span style="font-size:9.0pt;font-family:&quot;">500</span><span style="font-size:9.0pt;font-family:宋体;">块</span><span style="font-size:9.0pt;font-family:&quot;">/</span><span style="font-size:9.0pt;font-family:宋体;">条</span><span style="font-size:9.0pt;font-family:&quot;"></span>
					</p>
				</td>
			</tr>
			<tr>
				<td width="86" style="border:inset black 1.0pt;">
					<p class="MsoNormal" align="center" style="text-align:center;">
						<span style="font-size:9.0pt;font-family:宋体;">关注公众号</span><span style="font-size:9.0pt;font-family:&quot;"></span>
					</p>
				</td>
				<td width="85" style="border:inset black 1.0pt;">
					<p class="MsoNormal" align="center" style="text-align:center;">
						<span style="font-size:9.0pt;font-family:宋体;">次</span><span style="font-size:9.0pt;font-family:&quot;">/</span><span style="font-size:9.0pt;font-family:宋体;">人生</span><span style="font-size:9.0pt;font-family:&quot;"></span>
					</p>
				</td>
				<td width="94" style="border:inset black 1.0pt;">
					<p class="MsoNormal" align="center" style="text-align:center;">
						<span style="font-size:9.0pt;font-family:&quot;">500</span><span style="font-size:9.0pt;font-family:宋体;">块</span><span style="font-size:9.0pt;font-family:&quot;"></span>
					</p>
				</td>
			</tr>
		</tbody>
	</table>
</div>
`
Page({

	/**
	 * 页面的初始数据
	 */
	data: {
		htmlSnip,
		USERRECORD: wx.getStorageSync('ALLINFO').bbsUserRecord,
		showAccessRole: false,
		accessRole
	},

	/**
	 * 生命周期函数--监听页面加载
	 */
	onLoad: function (options) {
		var that = this
		app.getUserAllInfo().then(res => {
			that.setData({
				USERRECORD: res.bbsUserRecord
			})
		})
		this.setData({
			UPLOAD_IMAGE: app.globalData.UPLOAD_IMAGE
		})
	},

	/**
	 * 生命周期函数--监听页面初次渲染完成
	 */
	onReady: function () {

	},

	/**
	 * 生命周期函数--监听页面显示
	 */
	onShow: function () {

	},

	/**
	 * 生命周期函数--监听页面隐藏
	 */
	onHide: function () {

	},

	/**
	 * 生命周期函数--监听页面卸载
	 */
	onUnload: function () {

	},

	/**
	 * 页面相关事件处理函数--监听用户下拉动作
	 */
	onPullDownRefresh: function () {

	},

	/**
	 * 页面上拉触底事件的处理函数
	 */
	onReachBottom: function () {

	},

	/**
	 * 用户点击右上角分享
	 */
	onShareAppMessage: function () {

	},

	/**
	 * 陨石兑换规则
	 */
	openConverRule() {
		this.setData({
			converRule: true
		})
	},
	hideConverRule() {
		this.setData({
			converRule: false
		})
	},
	/**
	 * 陨石获取规则
	 */
	openGetRule() {
		this.setData({
			getRule: true
		})
	},
	hideGetRule() {
		this.setData({
			getRule: false
		})
	},

	/**
	 * 商品兑换
	 */
	goodConver(e) {
		console.log(this.USERRECORD)
		if (this.data.USERRECORD.stoneCount >= 100000) {
			wx.showModal({
				cancelColor: 'cancelColor',
				title: "商品兑换",
				content: "确认花费100000陨石兑换此商品吗？",
				success(res) {
					if (res.confirm) {
						wx.showToast({
							title: '兑换成功，已为您发放1000元无门槛优惠券。',
							icon: "none"
						})
					} else if (res.cancel) {
						console.log('用户点击取消')
					}
				}
			})
		} else {
			wx.showToast({
				title: '陨石不足，无法兑换！',
				icon: "none"
			})
		}
		// console.log(e)
		// let url = "plugin-private://wx34345ae5855f892d/pages/productDetail/productDetail?productId=" + e.currentTarget.dataset.id
		// console.log(url)

		// wx.navigateTo({
		// 	url: url
		// })
	},
	// mark:获取规则
	showAccessRole() {
		var that = this
		that.setData({
			showAccessRole: true
		})
	},
	hideAccessRole() {
		var that = this
		that.setData({
			showAccessRole: false
		})
	}
})