// // 主机开发本地环境
// const HOSTURL = 'http://本机IP:10002/wise-dev';
// // const HOSTURL = 'http://本机IP:10001/wise';

// // 线上正式环境
// // const HOSTURL = 'https://域名/wise';
// // 线上开发环境
// // const HOSTURL = 'https://域名/wise-dev';

const API = {
  "message_board_add": {
    'url': HOSTURL + '/bbs/bbsMessageBoard/wise/mini/add',
    'method': "post"
  },

  "user_star_clickStar": {
    'url': HOSTURL + '/bbs/bbsUserStar/wise/mini/clickStar',
    'method': "post"
  },

  "user_praise_clickPraise": {
    'url': HOSTURL + '/bbs/bbsUserPraise/wise/mini/clickPraise',
    'method': "post"
  },

  "waimai_list": {
    'url': HOSTURL + '/bbs/bbsWaimai/wise/mini/list',
    'method': "get"
  },
  "waimai_user_add": {
    'url': HOSTURL + '/bbs/bbsWaimaiUser/wise/mini/add',
    'method': "post"
  },

  "user_sig": {
    'url': HOSTURL + '/bbs/bbsChat/wise/mini/getUserSig',
    'method': "get"
  },

  "user_all_info": {
    'url': HOSTURL + '/bbs/bbsUserRecord/wise/mini/userAllInfo',
    'method': "get"
  },
};

//必须在这里暴露接口，以便被外界访问，不然就不能访问
module.exports = API