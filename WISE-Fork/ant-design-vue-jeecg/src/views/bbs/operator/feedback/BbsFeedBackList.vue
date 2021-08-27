<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="用户">
              <a-input placeholder="请输入用户" v-model="queryParam.createBy"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="处理历史">
              <a-input placeholder="请输入处理历史" v-model="queryParam.disposeHistory"></a-input>
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="处理进度">
                <j-dict-select-tag placeholder="请选择处理进度" v-model="queryParam.disposeStatus" dictCode="bbs_feedback_status"/>
              </a-form-item>
            </a-col>
            <a-col :xl="10" :lg="11" :md="12" :sm="24">
              <a-form-item label="处理时间">
                <j-date :show-time="true" date-format="YYYY-MM-DD HH:mm:ss" placeholder="请选择开始时间" class="query-group-cust" v-model="queryParam.disposeDate_begin"></j-date>
                <span class="query-group-split-cust"></span>
                <j-date :show-time="true" date-format="YYYY-MM-DD HH:mm:ss" placeholder="请选择结束时间" class="query-group-cust" v-model="queryParam.disposeDate_end"></j-date>
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
      <a-button type="primary" icon="download" @click="handleExportXls('用户反馈')">导出</a-button>
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

    <bbs-feed-back-modal ref="modalForm" @ok="modalFormOk"></bbs-feed-back-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import BbsFeedBackModal from './modules/BbsFeedBackModal'
  import {filterMultiDictText} from '@/components/dict/JDictSelectUtil'

  export default {
    name: 'BbsFeedBackList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      BbsFeedBackModal
    },
    data () {
      return {
        description: '用户反馈管理页面',
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
          //   title:'用户',
          //   align:"center",
          //   sorter: true,
          //   dataIndex: 'createBy'
          // },
          {
            title:'用户',
            align:"center",
            sorter: true,
            dataIndex: 'createByName'
          },
          {
            title:'用户头像',
            align:"center",
            sorter: true,
            dataIndex: 'userAvatar',
            scopedSlots: {customRender: 'imgSlot'}
          },
          {
            title:'反馈内容',
            align:"center",
            sorter: true,
            dataIndex: 'content'
          },
          {
            title:'处理结果',
            align:"center",
            sorter: true,
            dataIndex: 'disposeResult'
          },
          // {
          //   title:'处理人',
          //   align:"center",
          //   sorter: true,
          //   dataIndex: 'disposeUsername'
          // },
          {
            title:'处理人',
            align:"center",
            sorter: true,
            dataIndex: 'disposeRealName'
          },
          {
            title:'处理人角色',
            align:"center",
            sorter: true,
            dataIndex: 'disposeUserRole'
          },
          {
            title:'处理人头像',
            align:"center",
            sorter: true,
            dataIndex: 'disposeUserAvatar',
            scopedSlots: {customRender: 'imgSlot'}
          },
          {
            title:'处理历史',
            align:"center",
            sorter: true,
            dataIndex: 'disposeHistory'
          },
          {
            title:'联系方式',
            align:"center",
            sorter: true,
            dataIndex: 'userContact'
          },
          {
            title:'处理进度',
            align:"center",
            sorter: true,
            dataIndex: 'disposeStatus_dictText'
          },
          {
            title:'处理时间',
            align:"center",
            sorter: true,
            dataIndex: 'disposeDate'
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
          // list: "/bbs/bbsFeedBack/list",
          list: "/bbs/bbsFeedBack/wise/back/list",
          delete: "/bbs/bbsFeedBack/delete",
          deleteBatch: "/bbs/bbsFeedBack/deleteBatch",
          exportXlsUrl: "/bbs/bbsFeedBack/exportXls",
          importExcelUrl: "bbs/bbsFeedBack/importExcel",
          
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
        fieldList.push({type:'string',value:'createByName',text:'用户',dictCode:''})
        fieldList.push({type:'string',value:'userAvatar',text:'用户头像',dictCode:''})
        fieldList.push({type:'Text',value:'content',text:'反馈内容',dictCode:''})
        fieldList.push({type:'Text',value:'disposeResult',text:'处理结果',dictCode:''})
        fieldList.push({type:'string',value:'disposeUsername',text:'处理人',dictCode:''})
        fieldList.push({type:'string',value:'disposeRealName',text:'处理人',dictCode:''})
        fieldList.push({type:'string',value:'disposeUserRole',text:'处理人角色',dictCode:''})
        fieldList.push({type:'string',value:'disposeUserAvatar',text:'处理人头像',dictCode:''})
        fieldList.push({type:'string',value:'disposeHistory',text:'处理历史',dictCode:''})
        fieldList.push({type:'string',value:'userContact',text:'联系方式',dictCode:''})
        fieldList.push({type:'string',value:'disposeStatus',text:'处理进度',dictCode:'bbs_feedback_status'})
        fieldList.push({type:'datetime',value:'disposeDate',text:'处理时间'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>