<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="外卖ID">
              <a-input placeholder="请输入外卖ID" v-model="queryParam.waimaiId"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="区域编码">
              <a-input placeholder="请输入区域编码" v-model="queryParam.regionCode"></a-input>
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="用户">
                <a-input placeholder="请输入用户" v-model="queryParam.createBy"></a-input>
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="进入方式">
                <j-dict-select-tag placeholder="请选择进入方式" v-model="queryParam.type" dictCode="bbs_user_waimai_type"/>
              </a-form-item>
            </a-col>
            <a-col :xl="10" :lg="11" :md="12" :sm="24">
              <a-form-item label="创建时间">
                <j-date :show-time="true" date-format="YYYY-MM-DD HH:mm:ss" placeholder="请选择开始时间" class="query-group-cust" v-model="queryParam.createTime_begin"></j-date>
                <span class="query-group-split-cust"></span>
                <j-date :show-time="true" date-format="YYYY-MM-DD HH:mm:ss" placeholder="请选择结束时间" class="query-group-cust" v-model="queryParam.createTime_end"></j-date>
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
      <a-button type="primary" icon="download" @click="handleExportXls('用户点击外卖记录')">导出</a-button>
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

    <bbs-waimai-user-modal ref="modalForm" @ok="modalFormOk"></bbs-waimai-user-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import BbsWaimaiUserModal from './modules/BbsWaimaiUserModal'
  import {filterMultiDictText} from '@/components/dict/JDictSelectUtil'

  export default {
    name: 'BbsWaimaiUserList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      BbsWaimaiUserModal
    },
    data () {
      return {
        description: '用户点击外卖记录管理页面',
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
          //   title:'外卖ID',
          //   align:"center",
          //   sorter: true,
          //   dataIndex: 'waimaiId'
          // },
          {
            title:'外卖',
            align:"center",
            sorter: true,
            dataIndex: 'waimaiTitle'
          },
          // {
          //   title:'区域编码',
          //   align:"center",
          //   sorter: true,
          //   dataIndex: 'regionCode'
          // },
          {
            title:'区域名',
            align:"center",
            sorter: true,
            dataIndex: 'regionName'
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
            title:'进入方式',
            align:"center",
            sorter: true,
            dataIndex: 'type_dictText'
          },
          {
            title:'创建时间',
            align:"center",
            sorter: true,
            dataIndex: 'createTime'
          },
          {
            title:'使用状态',
            align:"center",
            dataIndex: 'useStatus_dictText'
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
          // list: "/bbs/bbsWaimaiUser/list",
          list: "/bbs/bbsWaimaiUser/wise/back/list",

          delete: "/bbs/bbsWaimaiUser/delete",
          deleteBatch: "/bbs/bbsWaimaiUser/deleteBatch",
          exportXlsUrl: "/bbs/bbsWaimaiUser/exportXls",
          importExcelUrl: "bbs/bbsWaimaiUser/importExcel",
          
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
        // fieldList.push({type:'string',value:'waimaiId',text:'外卖ID',dictCode:''})
        fieldList.push({type:'string',value:'waimaiTitle',text:'外卖',dictCode:''})
        // fieldList.push({type:'string',value:'regionCode',text:'区域编码',dictCode:''})
        fieldList.push({type:'string',value:'regionName',text:'区域编码',dictCode:''})
        // fieldList.push({type:'string',value:'createBy',text:'用户',dictCode:''})
        fieldList.push({type:'string',value:'createByName',text:'用户',dictCode:''})
        fieldList.push({type:'int',value:'type',text:'进入方式',dictCode:'bbs_user_waimai_type'})
        fieldList.push({type:'datetime',value:'createTime',text:'创建时间'})
        fieldList.push({type:'int',value:'useStatus',text:'使用状态',dictCode:'bbs_user_waimai_use_status'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>