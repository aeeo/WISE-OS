<template>
  <a-card :bordered="false">
    <a-tabs defaultActiveKey="1" @change="callback">
      <a-tab-pane tab="时间分布" key="1">
        <a-row>
          <a-col :span="4">
            <a-radio-group :value="ytdType" @change="statisticst">
              <a-radio-button value="day">今天</a-radio-button>
              <a-radio-button value="week">本周</a-radio-button>
              <a-radio-button value="month">本月</a-radio-button>
              <a-radio-button value="year">今年</a-radio-button>
            </a-radio-group>
          </a-col>
          <a-col :span="14">
            <a-range-picker
              :show-time="{ format: 'HH:mm' }"
              format="YYYY-MM-DD HH:mm"
              :placeholder="['开始时间', '结束时间']"
              @change="onChange"
              @ok="onOk"/>
          </a-col>
        </a-row>
        <div style="padding: 30px">
          <a-row :gutter="16">
            <a-col :span="6" v-for="cardListItem in cardListData" :key="cardListItem.title">
              <a-card>
                <a-statistic
                  :title="cardListItem.title"
                  :value="cardListItem.count"
                  :precision="0"
                  suffix=""
                  :value-style="{ color: cardListItem.count >= 0 ? '#3f8600' : '#cf1322' }"
                  style="margin-right: 50px">
                  <template #prefix>
                    <a-icon :type="cardListItem.count >= 0 ? 'arrow-up' : 'arrow-down'"/>
                  </template>
                </a-statistic>
              </a-card>
            </a-col>
          </a-row>
        </div>
      </a-tab-pane>
      <a-tab-pane tab="总览" key="3">
        <a-row>
          <a-col :span="10">
            <a-radio-group :value="allType" @change="statisticst">
              <a-radio-button value="all">总览</a-radio-button>
              <a-radio-button value="onlineUser">在线人数</a-radio-button>
              <a-radio-button value="month">新增用户</a-radio-button>
              <a-radio-button value="year">贴子数量</a-radio-button>
            </a-radio-group>
          </a-col>
        </a-row>
        <!-- 多行折线图 -->
        <a-row>
          <line-chart-multid v-if="lineDataSource.length != 0" title="在现用户数" :dataSource="lineDataSource" :fields="lineDataSourceFields" :height="200"/>
        </a-row>
        <div style="padding: 30px">
          <a-row :gutter="16">
            <a-col :span="6" v-for="cardListItem in cardListData" :key="cardListItem.title">
              <a-card>
                <a-statistic
                  :title="cardListItem.title"
                  :value="cardListItem.count"
                  :precision="0"
                  suffix=""
                  :value-style="{ color: cardListItem.count >= 0 ? '#3f8600' : '#cf1322' }"
                  style="margin-right: 50px">
                  <template #prefix>
                    <a-icon :type="cardListItem.count >= 0 ? 'arrow-up' : 'arrow-down'"/>
                  </template>
                </a-statistic>
              </a-card>
            </a-col>
          </a-row>
        </div>
      </a-tab-pane>
    </a-tabs>
  </a-card>
</template>

<script>
import Bar from '@/components/chart/Bar'
import Pie from '@/components/chart/Pie'
import ACol from 'ant-design-vue/es/grid/Col'
import LineChartMultid from '@/components/chart/LineChartMultid'
import {getAction} from '@/api/manage'
import Vue from 'vue'
import moment from 'moment';

export default {
  name: 'YTDStatist',
  components: {
    ACol,
    LineChartMultid,
    Bar,
    Pie
  },
  data() {
    return {
      description: '档案统计页面',
      // 查询条件
      queryParam: {},
      // 数据集
      countSource: [],
      // 年月日
      ytdType: 'day',
      // 总览
      allType: 'All',
      // 统计图类型
      tabStatus: "ytd",
      //折线图数据
      lineDataSource: [],
      lineDataSourceFields: [],

      //卡片查询条件
      cardQueryParam: {
        //http://momentjs.cn/docs/#/get-set/weekday/
        sysOrgCode: Vue.ls.get("Login_Userinfo").orgCode,
        startTime: moment().weekday(-7).format("yyyy-MM-DD 00:00:00"), // 上个星期一
        endTime: moment().weekday(0).format("yyyy-MM-DD 00:00:00"), // 上个星期一
      },
      //卡片数据
      cardListData: [],
      url: {
        getDayCountInfo: "/bbs/BbsStatist/wise/back/getDayCountInfo",
        getAllCountInfo: "/bbs/BbsStatist/wise/back/getAllCountInfo",
      },
    }
  },
  created() {
    let url = this.url.getDayCountInfo;
    console.log("created")
    this.cardQueryParam = {
      sysOrgCode: Vue.ls.get("Login_Userinfo").orgCode,
      startTime: moment(new Date()).format("yyyy-MM-DD 00:00:00"),
      endTime: moment(new Date()).format("yyyy-MM-DD HH:mm:ss"),
    }
    this.loadDate(url, 'day', this.cardQueryParam);
  },
  methods: {
    //时间选择框
    onChange(value, dateString) {
      console.log('Selected Time: ', value);
      console.log('Formatted Selected Time: ', dateString);
      this.cardQueryParam = {
        sysOrgCode: Vue.ls.get("Login_Userinfo").orgCode,
        startTime: dateString[0] + ":00",
        endTime: dateString[1] + ":00",
      }
    },
    onOk(value) {
      console.log('onOk: ', value);
      let url = this.url.getDayCountInfo;
      this.loadDate(url, "day", this.cardQueryParam);
    },

    loadDate(url, type, param) {
      console.log(type)
      this.cardListData = []
      this.lineDataSource = []
      getAction(url, param, 'get').then((res) => {
        if (res.success) {
          this.countSource = [];
          if (["day", "week", "month", "year"].includes(type)) {
            this.getCardNewDataCountSource(res.result);
          } else if ("onlineUser" === type) {
            this.getLineDataSource(res.result, "userOnlineCount");
          } else {
            this.getCardDataCountSource(res.result);
          }
        } else {
          var that = this;
          that.$message.warning(res.message);
        }
      })
    },
    //新增
    getCardNewDataCountSource(data) {
      this.cardListData = []
      let cardData = JSON.parse(data[0].statistData);
      for (let i = 1; i < data.length; i++) {
        let cardDataItemJson = JSON.parse(data[i].statistData)
        cardData.newUserCount += cardDataItemJson.newUserCount;
        cardData.userOnlineCount += cardDataItemJson.userOnlineCount;
        cardData.newTopicCount += cardDataItemJson.newTopicCount;
        cardData.newTopicPraiseCount += cardDataItemJson.newTopicPraiseCount;
        cardData.newReplyCount += cardDataItemJson.newReplyCount;
        cardData.newMessageBoardCount += cardDataItemJson.newMessageBoardCount;
        cardData.newMessageBoardPraiseCount += cardDataItemJson.newMessageBoardPraiseCount;
        cardData.newWaimaiUserCount += cardDataItemJson.newWaimaiUserCount;
      }

      this.cardListData.push({
        title: "用户",
        count: cardData.newUserCount,
        type: cardData.newUserCount > 0 ? "arrow-up" : "arrow-down"
      })
      this.cardListData.push({
        title: "上线用户",
        count: cardData.userOnlineCount,
        type: cardData.userOnlineCount > 0 ? "arrow-up" : "arrow-down"
      })
      this.cardListData.push({
        title: "帖子",
        count: cardData.newTopicCount,
        type: cardData.newTopicCount > 0 ? "arrow-up" : "arrow-down"
      })
      this.cardListData.push({
        title: "帖子点赞",
        count: cardData.newTopicPraiseCount,
        type: cardData.newTopicPraiseCount > 0 ? "arrow-up" : "arrow-down"
      })
      this.cardListData.push({
        title: "评论",
        count: cardData.newReplyCount,
        type: cardData.newReplyCount > 0 ? "arrow-up" : "arrow-down"
      })
      this.cardListData.push({
        title: "留言",
        count: cardData.newMessageBoardCount,
        type: cardData.newMessageBoardCount > 0 ? "arrow-up" : "arrow-down"
      })
      this.cardListData.push({
        title: "留言点赞",
        count: cardData.newMessageBoardPraiseCount,
        type: cardData.newMessageBoardPraiseCount > 0 ? "arrow-up" : "arrow-down"
      })
      this.cardListData.push({
        title: "外卖点击",
        count: cardData.newWaimaiUserCount,
        type: cardData.newWaimaiUserCount > 0 ? "arrow-up" : "arrow-down"
      })
    },
    //总览
    getCardDataCountSource(data) {
      this.cardListData = []
      let cardData = JSON.parse(data[0].statistData);

      // for (let i = 1; i < data.length; i++) {
      //   let cardDataItemJson = JSON.parse(data[i].statistData)
      //   cardData.userCount += cardDataItemJson.userCount;
      //   cardData.userOnlineCount += cardDataItemJson.userOnlineCount;
      //   cardData.topicCount += cardDataItemJson.topicCount;
      //   cardData.messageBoardCount += cardDataItemJson.messageBoardCount;
      //   cardData.messageBoardPraiseCount += cardDataItemJson.messageBoardPraiseCount;
      //   cardData.topicPraiseCount += cardDataItemJson.topicPraiseCount;
      //   cardData.replyCount += cardDataItemJson.replyCount;
      //   cardData.waimaiUserCount += cardDataItemJson.waimaiUserCount;
      // }

      this.cardListData.push({
        title: "用户",
        count: cardData.newUserCount,
        type: cardData.newUserCount > 0 ? "arrow-up" : "arrow-down"
      })
      this.cardListData.push({
        title: "上线用户",
        count: cardData.userOnlineCount,
        type: cardData.userOnlineCount > 0 ? "arrow-up" : "arrow-down"
      })
      this.cardListData.push({
        title: "帖子",
        count: cardData.topicCount,
        type: cardData.topicCount > 0 ? "arrow-up" : "arrow-down"
      })
      this.cardListData.push({
        title: "帖子点赞",
        count: cardData.topicPraiseCount,
        type: cardData.topicPraiseCount > 0 ? "arrow-up" : "arrow-down"
      })
      this.cardListData.push({
        title: "评论",
        count: cardData.replyCount,
        type: cardData.replyCount > 0 ? "arrow-up" : "arrow-down"
      })
      this.cardListData.push({
        title: "留言",
        count: cardData.messageBoardCount,
        type: cardData.messageBoardCount > 0 ? "arrow-up" : "arrow-down"
      })
      this.cardListData.push({
        title: "留言点赞",
        count: cardData.messageBoardPraiseCount,
        type: cardData.messageBoardPraiseCount > 0 ? "arrow-up" : "arrow-down"
      })
      this.cardListData.push({
        title: "外卖点击",
        count: cardData.waimaiUserCount,
        type: cardData.waimaiUserCount > 0 ? "arrow-up" : "arrow-down"
      })
    },
    //折线图
    getLineDataSource(data, key) {
      // [{type: 'Jan111', jeecg: 7777.0,}]


      let lineData = []
      for (let i = 0; i < data.length; i++) {
        let cardDataItemJson = JSON.parse(data[i].statistData)

        for (var keyItem in cardDataItemJson) {
          console.log(key,keyItem)
          if (key === keyItem) {
            let lineItem = {}
            lineItem.type = data[i].createTime
            lineItem.在线用户 = cardDataItemJson.userOnlineCount;
            lineData.push(lineItem)
          }
        }
      }
      console.log(lineData)
      this.lineDataSource = lineData
      this.lineDataSourceFields = ["在线用户"]
    },
    // 选择统计图类别
    callback(key) {
      if (key === "1") {
        this.tabStatus = "ytd";
        this.queryYTDDate("day");
      } else if (key === "3") {
        this.tabStatus = "all";
        this.queryAllDate("all");
      }
    },
    // 选择统计类别
    statisticst(e) {
      console.log(this.tabStatus, e.target.value)
      if (this.tabStatus === "all") {
        this.allType = e.target.value;
        this.queryAllDate(this.allType);
      } else if (this.tabStatus === "ytd") {
        this.ytdType = e.target.value;
        console.log(this.ytdType);
        this.queryYTDDate(this.ytdType);
      }
    },
    queryYTDDate(type) {
      console.log("切换tab--queryYTDDate")
      this.getUrl(type, {});
    },
    queryAllDate(type) {
      console.log("切换tab--queryAllDate")
      this.getUrl(type, {});
    },
    // 选择请求url
    getUrl(type, param) {
      console.log(type)
      let url = "";
      if (type === 'day') {
        url = this.url.getDayCountInfo;
        this.cardQueryParam = {
          sysOrgCode: Vue.ls.get("Login_Userinfo").orgCode,
          startTime: moment(new Date()).format("yyyy-MM-DD 00:00:00"),
          endTime: moment(new Date()).format("yyyy-MM-DD HH:mm:ss"),
        }
      }
      if (type === 'week') {
        url = this.url.getDayCountInfo;
        this.cardQueryParam = {
          sysOrgCode: Vue.ls.get("Login_Userinfo").orgCode,
          startTime: moment().weekday(0).format("yyyy-MM-DD 00:00:00"), // 这个星期一
          endTime: moment(new Date()).format("yyyy-MM-DD HH:mm:ss"),
        }
      }
      if (type === 'month') {
        url = this.url.getDayCountInfo;
        this.cardQueryParam = {
          sysOrgCode: Vue.ls.get("Login_Userinfo").orgCode,
          startTime: moment(new Date()).format("yyyy-MM-00 00:00:00"),
          endTime: moment(new Date()).format("yyyy-MM-DD HH:mm:ss"),
        }
      }
      if (type === 'year') {
        url = this.url.getDayCountInfo;
        this.cardQueryParam = {
          sysOrgCode: Vue.ls.get("Login_Userinfo").orgCode,
          startTime: moment(new Date()).format("yyyy-00-00 00:00:00"),
          endTime: moment(new Date()).format("yyyy-MM-DD HH:mm:ss"),
        }
      }

      if (["all", "onlineUser"].includes(type)) {
        url = this.url.getAllCountInfo;
        this.cardQueryParam = {
          sysOrgCode: Vue.ls.get("Login_Userinfo").orgCode,
        }
      }

      // console.log("geturl", type)
      this.loadDate(url, type, this.cardQueryParam);
    },
  }
}
</script>
<style scoped>
.ant-card-body .table-operator {
  margin-bottom: 18px;
}

.ant-table-tbody .ant-table-row td {
  padding-top: 15px;
  padding-bottom: 15px;
}

.anty-row-operator button {
  margin: 0 5px
}

.ant-btn-danger {
  background-color: #ffffff
}

.ant-modal-cust-warp {
  height: 100%
}

.ant-modal-cust-warp .ant-modal-body {
  height: calc(100% - 110px) !important;
  overflow-y: auto
}

.ant-modal-cust-warp .ant-modal-content {
  height: 90% !important;
  overflow-y: hidden
}

.statistic {
  padding: 0px !important;
  margin-top: 50px;
}

.statistic h4 {
  margin-bottom: 20px;
  text-align: center !important;
  font-size: 24px !important;;
}

.statistic #canvas_1 {
  width: 100% !important;
}
</style>