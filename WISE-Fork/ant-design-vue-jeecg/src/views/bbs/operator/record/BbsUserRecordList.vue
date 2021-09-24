<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="用户账号">
              <a-input placeholder="请输入用户账号" v-model="queryParam.userAccount"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="当前区域编码">
              <a-input placeholder="请输入当前区域编码" v-model="queryParam.regionCode"></a-input>
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <a-col :xl="10" :lg="11" :md="12" :sm="24">
              <a-form-item label="本月区域切换次数">
                <a-input placeholder="请输入最小值" class="query-group-cust" v-model="queryParam.regionSwitchCount_begin"></a-input>
                <span class="query-group-split-cust"></span>
                <a-input placeholder="请输入最大值" class="query-group-cust" v-model="queryParam.regionSwitchCount_end"></a-input>
              </a-form-item>
            </a-col>
          </template>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <a @click="handleToggleSearch" style="margin-left: 8px">
                {{ toggleSearchStatus ? '收起' : '展开' }}
                <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>
              </a>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('用户信息记录')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
      <!-- 高级查询区域 -->
      <j-super-query :fieldList="superFieldList" ref="superQueryModal" @handleSuperQuery="handleSuperQuery"></j-super-query>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
      </a-dropdown>
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
        ref="table"
        size="middle"
        :scroll="{x:true}"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        class="j-table-force-nowrap"
        @change="handleTableChange">

        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <template slot="imgSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无图片</span>
          <img v-else :src="getImgView(text)" height="25px" alt="" style="max-width:80px;font-size: 12px;font-style: italic;"/>
        </template>
        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无文件</span>
          <a-button
            v-else
            :ghost="true"
            type="primary"
            icon="download"
            size="small"
            @click="downloadFile(text)">
            下载
          </a-button>
        </template>

        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
              <a-menu-item>
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <bbs-user-record-modal ref="modalForm" @ok="modalFormOk"></bbs-user-record-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import BbsUserRecordModal from './modules/BbsUserRecordModal'

  export default {
    name: 'BbsUserRecordList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      BbsUserRecordModal
    },
    data () {
      return {
        description: '用户信息记录管理页面',
        // 表头
        columns: [
          {
            title: '#',
            dataIndex: '',
            key:'rowIndex',
            width:60,
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
          },
          // {
          //   title:'用户标识',
          //   align:"center",
          //   sorter: true,
          //   dataIndex: 'createBy'
          // },
          {
            title:'头像',
            align:"center",
            sorter: true,
            dataIndex: 'avatar',
            scopedSlots: {customRender: 'imgSlot'}
          },
          {
            title:'用户名',
            align:"center",
            sorter: true,
            dataIndex: 'createByName'
          },
          {
            title:'用户账号',
            align:"center",
            sorter: true,
            dataIndex: 'userAccount'
          },
          {
            title:'陨石数',
            align:"center",
            sorter: true,
            dataIndex: 'stoneCount'
          },
          {
            title:'帖子数',
            align:"center",
            sorter: true,
            dataIndex: 'topicCount'
          },
          // {
          //   title:'留言数',
          //   align:"center",
          //   sorter: true,
          //   dataIndex: 'messageCount'
          // },
          // {
          //   title:'消息数',
          //   align:"center",
          //   sorter: true,
          //   dataIndex: 'informationCount'
          // },
          {
            title:'收藏数',
            align:"center",
            sorter: true,
            dataIndex: 'starCount'
          },
          {
            title:'被收藏数',
            align:"center",
            sorter: true,
            dataIndex: 'beStarCount'
          },
          {
            title:'浏览帖子数',
            align:"center",
            sorter: true,
            dataIndex: 'clickTopicCount'
          },
          // {
          //   title:'评论数',
          //   align:"center",
          //   sorter: true,
          //   dataIndex: 'commentCount'
          // },
          // {
          //   title:'被评论数',
          //   align:"center",
          //   sorter: true,
          //   dataIndex: 'beCommentCount'
          // },
          // {
          //   title:'点赞数',
          //   align:"center",
          //   sorter: true,
          //   dataIndex: 'praiseCount'
          // },
          // {
          //   title:'被赞数',
          //   align:"center",
          //   sorter: true,
          //   dataIndex: 'bePraiseCount'
          // },
          // {
          //   title:'当天发布贴子数',
          //   align:"center",
          //   sorter: true,
          //   dataIndex: 'dayPublishTopic'
          // },
          // {
          //   title:'本周发布贴子数',
          //   align:"center",
          //   sorter: true,
          //   dataIndex: 'weekPublishTopic'
          // },
          // {
          //   title:'当天发布留言数',
          //   align:"center",
          //   sorter: true,
          //   dataIndex: 'dayPublishMessage'
          // },
          // {
          //   title:'本周发布留言数',
          //   align:"center",
          //   sorter: true,
          //   dataIndex: 'weekPublishMessage'
          // },
          // {
          //   title:'当天点赞次数',
          //   align:"center",
          //   sorter: true,
          //   dataIndex: 'dayPublishPraise'
          // },
          // {
          //   title:'本周点赞次数',
          //   align:"center",
          //   sorter: true,
          //   dataIndex: 'weekPublishPraise'
          // },
          // {
          //   title:'当天评论次数',
          //   align:"center",
          //   sorter: true,
          //   dataIndex: 'dayPublishReply'
          // },
          // {
          //   title:'本周评论次数',
          //   align:"center",
          //   sorter: true,
          //   dataIndex: 'weekPublishReply'
          // },
          // {
          //   title:'当前区域编码',
          //   align:"center",
          //   sorter: true,
          //   dataIndex: 'regionCode'
          // },
          {
            title:'当前区域',
            align:"center",
            sorter: true,
            dataIndex: 'regionName'
          },
          {
            title:'本月区域切换次数',
            align:"center",
            sorter: true,
            dataIndex: 'regionSwitchCount'
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            fixed:"right",
            width:147,
            scopedSlots: { customRender: 'action' }
          }
        ],
        url: {
          // ****行星万象修改位置戳****
          // list: "/bbs/bbsUserRecord/list",
          list: "/bbs/bbsUserRecord/wise/back/list",
          delete: "/bbs/bbsUserRecord/delete",
          deleteBatch: "/bbs/bbsUserRecord/deleteBatch",
          exportXlsUrl: "/bbs/bbsUserRecord/exportXls",
          importExcelUrl: "bbs/bbsUserRecord/importExcel",
          
        },
        dictOptions:{},
        superFieldList:[],
      }
    },
    created() {
    this.getSuperFieldList();
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      },
    },
    methods: {
      initDictConfig(){
      },
      getSuperFieldList(){
        let fieldList=[];
        fieldList.push({type:'string',value:'createBy',text:'用户',dictCode:''})
        fieldList.push({type:'string',value:'avatar',text:'头像',dictCode:''})
        fieldList.push({type:'string',value:'createByName',text:'用户',dictCode:''})
        fieldList.push({type:'string',value:'userAccount',text:'用户账号',dictCode:''})
        fieldList.push({type:'int',value:'stoneCount',text:'陨石数',dictCode:''})
        fieldList.push({type:'int',value:'topicCount',text:'帖子数',dictCode:''})
        fieldList.push({type:'int',value:'messageCount',text:'留言数',dictCode:''})
        fieldList.push({type:'int',value:'informationCount',text:'消息数',dictCode:''})
        fieldList.push({type:'int',value:'starCount',text:'收藏数',dictCode:''})
        fieldList.push({type:'int',value:'beStarCount',text:'被收藏数',dictCode:''})
        fieldList.push({type:'int',value:'clickTopicCount',text:'浏览帖子数',dictCode:''})
        fieldList.push({type:'int',value:'commentCount',text:'评论数',dictCode:''})
        fieldList.push({type:'int',value:'beCommentCount',text:'被评论数',dictCode:''})
        fieldList.push({type:'int',value:'praiseCount',text:'点赞数',dictCode:''})
        fieldList.push({type:'int',value:'bePraiseCount',text:'被赞数',dictCode:''})
        fieldList.push({type:'int',value:'dayPublishTopic',text:'当天发布贴子数',dictCode:''})
        fieldList.push({type:'int',value:'weekPublishTopic',text:'本周发布贴子数',dictCode:''})
        fieldList.push({type:'int',value:'dayPublishMessage',text:'当天发布留言数',dictCode:''})
        fieldList.push({type:'int',value:'weekPublishMessage',text:'本周发布留言数',dictCode:''})
        fieldList.push({type:'int',value:'dayPublishPraise',text:'当天点赞次数',dictCode:''})
        fieldList.push({type:'int',value:'weekPublishPraise',text:'本周点赞次数',dictCode:''})
        fieldList.push({type:'int',value:'dayPublishReply',text:'当天评论次数',dictCode:''})
        fieldList.push({type:'int',value:'weekPublishReply',text:'本周评论次数',dictCode:''})
        fieldList.push({type:'string',value:'regionCode',text:'当前区域编码',dictCode:''})
        fieldList.push({type:'string',value:'regionName',text:'当前区域',dictCode:''})
        fieldList.push({type:'int',value:'regionSwitchCount',text:'本月区域切换次数',dictCode:''})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>