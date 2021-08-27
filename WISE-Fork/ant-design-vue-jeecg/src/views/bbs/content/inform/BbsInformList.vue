<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="10" :lg="11" :md="12" :sm="24">
            <a-form-item label="创建日期">
              <j-date :show-time="true" date-format="YYYY-MM-DD HH:mm:ss" placeholder="请选择开始时间" class="query-group-cust" v-model="queryParam.createTime_begin"></j-date>
              <span class="query-group-split-cust"></span>
              <j-date :show-time="true" date-format="YYYY-MM-DD HH:mm:ss" placeholder="请选择结束时间" class="query-group-cust" v-model="queryParam.createTime_end"></j-date>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="举报类型">
              <j-dict-select-tag placeholder="请选择举报类型" v-model="queryParam.type" dictCode="bbs_inform_type"/>
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="处理结果">
                <j-dict-select-tag placeholder="请选择处理结果" v-model="queryParam.resultType" dictCode="bbs_inform_result_type"/>
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
      <a-button type="primary" icon="download" @click="handleExportXls('举报列表')">导出</a-button>
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

    <bbs-inform-modal ref="modalForm" @ok="modalFormOk"></bbs-inform-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import BbsInformModal from './modules/BbsInformModal'
  import {filterMultiDictText} from '@/components/dict/JDictSelectUtil'

  export default {
    name: 'BbsInformList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      BbsInformModal
    },
    data () {
      return {
        description: '举报列表管理页面',
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
          {
            title:'创建日期',
            align:"center",
            sorter: true,
            dataIndex: 'createTime'
          },
          {
            title:'举报类型',
            align:"center",
            sorter: true,
            dataIndex: 'type_dictText'
          },
          // {
          //   title:'举报用户名',
          //   align:"center",
          //   sorter: true,
          //   dataIndex: 'informUsername'
          // },
          // {
          //   title:'被举报用户名',
          //   align:"center",
          //   sorter: true,
          //   dataIndex: 'beInformUsername'
          // },
          {
            title:'举报用户昵称',
            align:"center",
            sorter: true,
            dataIndex: 'informUsernameRealname'
          },
          {
            title:'被举报用户昵称',
            align:"center",
            sorter: true,
            dataIndex: 'beInformUsernameRealname'
          },
          {
            title:'处理结果',
            align:"center",
            sorter: true,
            dataIndex: 'resultType_dictText'
          },
          {
            title:'处理意见',
            align:"center",
            sorter: true,
            dataIndex: 'resultSuggestion'
          },
          // {
          //   title:'贴子ID',
          //   align:"center",
          //   sorter: true,
          //   dataIndex: 'topicId'
          // },
          {
            title:'贴子内容',
            align:"center",
            sorter: true,
            dataIndex: 'topicContent'
          },
          // {
          //   title:'评论ID',
          //   align:"center",
          //   sorter: true,
          //   dataIndex: 'replyId'
          // },
          {
            title:'评论内容',
            align:"center",
            sorter: true,
            dataIndex: 'replyContent'
          },
          // {
          //   title:'留言ID',
          //   align:"center",
          //   sorter: true,
          //   dataIndex: 'messageId'
          // },
          {
            title:'留言内容',
            align:"center",
            sorter: true,
            dataIndex: 'messageContent'
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
          // list: "/bbs/bbsInform/list",
          list: "/bbs/bbsInform/wise/back/list",
          delete: "/bbs/bbsInform/delete",
          deleteBatch: "/bbs/bbsInform/deleteBatch",
          exportXlsUrl: "/bbs/bbsInform/exportXls",
          importExcelUrl: "bbs/bbsInform/importExcel",
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
        fieldList.push({type:'datetime',value:'createTime',text:'创建日期'})
        fieldList.push({type:'int',value:'type',text:'举报类型',dictCode:'bbs_inform_type'})
        // fieldList.push({type:'string',value:'informUsername',text:'举报用户名',dictCode:''})
        // fieldList.push({type:'string',value:'beInformUsername',text:'被举报用户名',dictCode:''})
        fieldList.push({type:'string',value:'informUsernameRealname',text:'举报用户昵称',dictCode:''})
        fieldList.push({type:'string',value:'beInformUsernameRealname',text:'被举报用户昵称',dictCode:''})
        fieldList.push({type:'int',value:'resultType',text:'处理结果',dictCode:'bbs_inform_result_type'})
        fieldList.push({type:'string',value:'resultSuggestion',text:'处理意见',dictCode:''})
        fieldList.push({type:'string',value:'topicId',text:'贴子ID',dictCode:''})
        fieldList.push({type:'string',value:'topicContent',text:'贴子内容',dictCode:''})
        fieldList.push({type:'string',value:'replyId',text:'评论ID',dictCode:''})
        fieldList.push({type:'string',value:'replyContent',text:'评论内容',dictCode:''})
        fieldList.push({type:'string',value:'messageId',text:'留言ID',dictCode:''})
        fieldList.push({type:'string',value:'messageContent',text:'留言内容',dictCode:''})

        fieldList.push({type:'string',value:'regionCode',text:'区域编码',dictCode:''})
        fieldList.push({type:'string',value:'regionName',text:'区域名称',dictCode:''})
        fieldList.push({type:'string',value:'classCode',text:'版块编码',dictCode:''})
        fieldList.push({type:'string',value:'className',text:'版块名称',dictCode:''})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>