var app = getApp();
Component({
  options: {
    addGlobalClass: true
  },
  properties: {
    // 这里定义了innerText属性，属性值可以在组件使用时指定
  },
  data: {
    userInfo: '',
    // 这里是一些组件内部数据
    types: ['topLeft', 'topRight', 'bottomLeft', 'bottomRight', 'center'],
    typeIndex: 3,
    colors: [
      'light',
      'stable',
      'positive',
      'calm',
      'balanced',
      'energized',
      'assertive',
      'royal',
      'dark',
    ],
    colorIndex: 7,
    dirs: ['horizontal', 'vertical', 'circle'],
    dirIndex: 1,
    sAngle: 0,
    eAngle: 360,
    spaceBetween: 10,
    buttons: [{
      // openType: 'getUserInfo',
      label: '发布信息',
      icon: "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACIAAAAgCAYAAAB3j6rJAAADiUlEQVRYR8WXW8hlYxjHf/8bFIUk5zI1F4a4kEOjHMbFFKEMUw5zQTnUSDnmlCiH0YQpDcKFSaSYUYxDoczIYZgkExdGbsZZGRIuXPjrvz3ra1nfPnyzv715bnbrXe96399+zo8osX0AcBOwFDiqWZ/w7yZgs6Q7u+cqC7aPBD6b8KXDjvtW0iHtDbK9O/BRSwsBeh/4ZsJgC4GLW2eukXRd8xyQC4Bna2GTpCUTBpg5ro/mD5L0fTYE5GZgFfAHsFDSd9MCKTc4C9hYdyyWtKUBiePcAXws6dhpQhTIPsDPdc8SSXHgnkYakKmapf0HbXuXQGxfD9w/ppa2Aiskbe9+Pw7I1cBDY4Lks8MkfT1vkLLpaWOC7JS0rd+3u6yRMQFGfmZ75s/9L85qew/gcOC3rsn+k6ixvQi4HLgISE2LrJJ0azuzDg3fthpH6HybpJ19HPNA4BWgX47K+kpJO4ZqxPZjwBUjjf7Pho2Szunki9SxV4HTa/0q4HngPODRWntD0tJRIJcAT84RZLWktBEzYnt9XZq1ZyStaF7aXg48V89nT81HbD8BXFYXvQecBNwm6d4WzFtAIuiGqYDYXg3cWBfGIT8AXgD2Bu6TdEvlqE+r/bhw4iC2c/E93ciwfXzBHAo8AiTjNtpZMFEQ23HGtQWxVlJKRNtnjgA2AOkIG1kvaflIkKrOXX9Npe6V75a90309Xc/rJF3az8ltXws8WO9ejjNL+nNU1KwBrhkQNftK+qVs3W52Nkg6fwBEwjaRFHmzIH7NwyiQdPQ9x+rIdklXFsTJwNv1/nVgmaTfux/YzlkvAknz7xTEj82+kaYZoI3ecmXdhGAkDXcgej1oW2wvLoj9q1HPvh3tPfMFSRf+AJAwzOFf9IE4uiAW1MiSfbOapfmCbAZOAe6WdHsfiFTamOMY4MuC7dujtEG2SjphmCk66t4z5bzWTpXU+Eljtv2AlyqjZkaKJj4cdH5AkgGTCeNgiyR9NReYSlC9gyX1JsZGbO9WI0Mc9KeC+Bdo946ALKskk3ezktAgKNtNQXxN0pktiFTalVXsMiudKynRNFQCshfwOXBw7VxXETDLoZqTksxs57KHq6w/BRwHJH80A/xfBRHzjJRmCE9lfHfk7toQU9g+EehNaX0kk9zjkpI55yQztrWdyngXcEZGz2FfNz5Rc3MSWmrHD8AnyRNzMUX3/L8BUeDAW+8P2HYAAAAASUVORK5CYII=",
    },
    //  {
    //   openType: 'getUserInfo',
    //   label: '发布留言',
    //   icon: 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAADqElEQVRYR8WXa8jecxjHP99XIkmOkRfOQm1EzpnZspxiyqkctrA5M6LwAlEUoa1sxsPIYQ4v1mKY8KQsZMWiZoQ3E6llEXv31ff2+z397//uZ3t238+dq56eu9/hur7X6Xv9/uJ/Fu2Ifdt7AUcARwP7t+6uAb6W9NuO6JwQANtnAlcDV01A+TrgXWC5pK+2d36bAGwfA9wPXLg9RePsvwUslvTRePfHBWD79mJ898blpcDnJdRrm0ptnwwcC5xeAO/U2H9E0r29QPQEYPsl4MqW4aWSuoyO55Xtg4EbgTsbZ1ZLmtW+sxUA2x8DZ5SDyeGDklb0kwLbM4AHgNPK/Ucl3dPU1QXA9s3AonLgTWCepD/6MV7v2E4q3ms4FYcCqiNjAGxfBrxWjUu6ZBDD7bu2vyntm60ZtTCbAL4EjgPWSjp+Mo1Hl+0DgbcLiFWSzh2LgO15wDPF6HxJqfZJl5adayWNdCJg+zPgxGF532rXGulPJE1TCc1P5dDdkh6bdNcbCm2nAENukZMCYA7wQlk4SNLPQwaQSCfikUUBsBi4HhiVNL1H9e4G3AecAIxIermk7VBgAfA7sKwCtx3yOQ9IG2cebOqh8zvgcOCLAPgUOAV4XVJasUtsh73Sx5E/gSMlbbS9Eji/rAfAXNt7A98C+R+5QtIrPXS+ClwO/BgAUborsFDSbT0ON3OW7emSRm27eVZSdIVBw6RVukinLtp+uER1cy4lhJnzW9FkCfXFwBsNpVMlrbNdqzlbnfTZPgT4oXH2VkmVWceWbT8N3ABsCYDw/dSSr4SlnYIDgFxIuJ+UdEcBdjbwUElLPB0t6+GT8MpIcaoJqKPb9nLgUmB9ALwDnJPKlJSROnSx/QEwM7UVAGG964BfJe03dOv/ReBvYGdgSQA0i2ympA+HCcJ2Ureq2LgpAOL1eiD9/ryka4YMYCFwS7GxT50FNQpbgCmSvh8GCNuHAeGd8MRKSRdUAInCL8VoV+/afgLYIGnJoKBsR8f8omeOpBeb74HngIR/jJIbj5RNkvYcBIDts4D3i44Vkmbnd43AUYVCs9bpddt7FFabUrh+br8AbGcAZYZkfuSJFzbtfDNUAJXX/wJOBaYBqdb8bQBm9TslS9XHeByKLJD0VHWmAthcuiCfVfu2PO1wfz/e95gNd0l6vKmrAsh74KICou6HoCb8LdALYItj8sJ+tn2uWYS7lA/Pf4CNkjIlB5YCYo2k1b2UTejjdGAU21DwLzU/iAnRvdFVAAAAAElFTkSuQmCC',
    // },
    // {
    //   // openType: 'getUserInfo',
    //   label: '问题反馈',
    //   icon: 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAB0klEQVRYR+2XsUtWURjGfw+52NDaf2CDEdIgokuCc2Hi4BCkIAhuDio0mINDFhmRgTTU1hApzg46BS0hog666RiEi4sFj7xwvrp+2Nfn54nr8L3LhcM5z/s7z/uee88VJYdKzk8T4Oo5YPs90A20Ze6PPeCLpOGi7hkHbG8D7ZkTV8vtSLpdGfwNYPspMAN8AlYkfcwJYnsI6AcGgVlJke/PKbD9GXgIDEv6kDN5Rcv2YyBKvCxpoBpgHbgHLAHTko5yQti+ATwDxoANSb1/A4jxX8CUpJc5IGyPA/PA9aRXEyBqH/WK2AGeSFptBMR2HzAHdKb174DRfzkQ1vxIpehKC1fCPklf6wGxHUd4ChhJ89eAt0CUNUpd04FeSRux0PaDBHIzCS0AryQdnAdiuxWYAKLDW4BDIMq4KOmn7eix+gEK3TuZGiiGvgMvEshJYc6jlLwjjb0BXkvaL8xpDCC5cQsIkIqt34DN1LBheYhHhN3PJcXzTDTsQFHF9v0E0lOlH3bPS4qdnxtZAAp23gHuAteALWBX0nGtJs0KUM9p+C8laCRxlia8TOImQL0OlP45LvdCkt525V3JCnUq71Ka46hdVOPq/RdcdAeXnd904BT3aQYwzO0sPQAAAABJRU5ErkJggg==',
    // },
    {
      // openType: 'getUserInfo',
      label: '外卖领券',
      icon: 'data:image/png;base64,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',
    }, {
      openType: 'contact',
      label: '在线客服',
      icon: 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADAAAAAwCAYAAABXAvmHAAAEg0lEQVRoQ+2ZWagcRRSGvx/EBxF3DS4PakTElbggmohbMBE1mhhXFEMkRjEaH0RcHjQiRFQI7hrUEEUQownGNUowRCW4RI24gYpiUNwiLg9uyC9Hay59+3bP9PT0DPeCB+phZqpO/V9XVZ9zasQYNzWp3/auwEHA9sAOwBbAD6ltkrSmyfnCV88Atg8DTgKmAod3EPgj8HxqyyX91itQbQDbZwPRTi0RsRGINi61LXP9PgMeiybp/bogXQPY3g54KCf8G+Al4L3UNkj6NivK9h7A8cBxwDk5wbMlLakD0RWA7fHAemDrNNmvwN3APZLiaVcy20cB84HTMwNulnRNJQeZTpUBbE8Hlqexf4foJPzjbidt9bcdAAESQGFrJB3bjb9KALZnAw8mx18C0yW93c1E7fravg64qQ5ERwDbk9P+Dv+/A1MkrW1KfGY15gCL0+fVkmLejtYWwPa2SfwhydNZkh7v6LVmB9tXAIvS8PskXdLJVSeA+4GLkpP5ku7o5LDX322vAE5Lfi6Q9HA7n6UAtqcAL6TBSyXN6lVclfG2DwbWAZsDayUdXRfgUeDcNHiapKerCGiij+1bgSuTrzhzL5b5LVwB24cCb6ZBb0jqlCI0oXvIh+3dgA+ArYAlkuItWGhlAJcDt6cR10pa2KjCCs5sx/aNbfw9MF5SBM0RVgbwCHAeEMnXREm1g1UFrYVdbM8D7kw/zpAUh7sywEfAPukwLZK0rK6QOuNs7w3EG/CYND5SlUsrAdi+DMi+LmPpdpIUQWwgZvtqILttf4lEUNJbeQHDtpDtE4HnClROlrR6IOoB268Ak3LzrZIUNccwGwKwHfn6O8BeBUJL92A/oGxvAA4s8L1A0g3Z77MARwKvlQjqGBGbBLH9ObB7gc+VkoYVUFmAi4F7S4TMkxR5/0DM9iYgCqe8/SQp8rMhywJEznF+icKBxgLbfwGblWiZIOnd1m9ZgKeAaaMEwG2W+oBsDZ0FiAqrLH0d9BZqBzBO0ndFKxCn+/oS8lmSlg7kAPz3Gi0DWC8p8rTCM7AzEIFilwKhMyU9OUCAr0p0zJXUqtr+lZMPZGWrMFXSqgECrAROyc33rKST8xpGJHO2HwAuzHT8GoiDE4ndQMx2/kHGfdMJ+bumESvQUmc7YsLEVMSvkFSUXvQNxnbcMcVDjOAa1dk6SZEPjbCOtxJ9U1ni2PYM4EZJ+1eZezQCxK1H1MJ3jTkA23sCUYtEtP1wLAJEMT9J0hFVxJce4qqDm+xnO6qvl+P1KemZqr5HzRmw/Qdwm6S4J61sowLAdmQAGyXFDXhX1jcA21HZbRN/eEj6s0hVuvWOJHKhpAVdKU+dGwWwvS8QUfSMjJgQH3+KXCXp1fje9oSUukf2O6eXW7/GAGzvmA7hfm2eZDzlVkkYV5VPSIo0obY1CdAuHW8J/AKIQxqJ2c+1VWcGNgkQe/6TDqIWS5rbhPCWj8YA0t6OvX9LyY3CMklnNim+b4EsBaVYkbhZ+BR4XVIUKY1boyvQuLoKDv8HqPCQ+tplzK/AP6WUhUBLum13AAAAAElFTkSuQmCC',
    }
    ],
  },
  onload() {

  },
  methods: {
    onContact(e) {
      console.log('onContact', e)
    },
    // 点击悬浮按钮发布信息需要授权个人信息
    fabbuttonAction(e) {
      console.log(e.detail.value.label)
      let HASUSERINFO = app.HASUSERINFO()
      if ('wait' == HASUSERINFO) {
        return
      }
      console.log(HASUSERINFO)
      // 如果没有授权过，需要用户授权
      if (!HASUSERINFO) {
        app.getUserProfile().then(res => {
          if (res.status) {
            // 判断是否能够发布
            let userBehaviorLimit = wx.getStorageSync('userBehaviorLimit')
            if (userBehaviorLimit.canWeekPunlishTopic) {
              if (userBehaviorLimit.canDayPunlishTopic) {
                wx.navigateTo({
                  url: '/pages/components/issue/issue',
                })
              } else {
                wx.showToast({
                  title: '今天发布次数已达上限！',
                  icon: 'none'
                })
              }
            } else {
              wx.showToast({
                title: '本周发布次数已达上限！',
                icon: 'none'
              })
            }
          }
        })
      } else {
        // 如果已经授权，则直接跳转相应表单
        if (e.detail.value.label == "发布信息") {
          // 判断是否能够发布
          let userBehaviorLimit = wx.getStorageSync('userBehaviorLimit')
          if (userBehaviorLimit.canWeekPunlishTopic) {
            if (userBehaviorLimit.canDayPunlishTopic) {
              wx.navigateTo({
                url: '/pages/components/issue/issue',
              })
            } else {
              wx.showToast({
                title: '今天发布次数已达上限！',
                icon: 'none'
              })
            }
          } else {
            wx.showToast({
              title: '本周发布次数已达上限！',
              icon: 'none'
            })
          }
        }
        //  else if (e.detail.value.label == "发布留言") {
        //   wx.switchTab({
        //     url: '/pages/msg/msg'
        //   })
        // } 
        else if (e.detail.value.label == "问题反馈") {
          wx.navigateTo({
            url: '/pages/components/feedback/feedback',
          })
        }else if(e.detail.value.label == "外卖领券"){
          wx.navigateTo({
            url: '/pages/components/msg/savemoney/savemoney',
          })
        }
      }
    },

    onGotPhoneNumber(e) {
      console.log('onGotPhoneNumber', e)
    },
    onChange(e) {
      // console.log('onChange', e)
    },
    pickerChange(e) {
      const {
        value
      } = e.detail
      const {
        model
      } = e.currentTarget.dataset

      this.setData({
        [model]: value,
      })
    },
    onSwitch(e) {
      this.setData({
        reverse: e.detail.value,
      })
    },
    onAngle(e) {
      const {
        value
      } = e.detail
      const sAngle = value ? -90 : 0
      const eAngle = value ? -210 : 360
      const spaceBetween = value ? 30 : 10

      this.setData({
        sAngle,
        eAngle,
        spaceBetween,
      })
    }
  },
})