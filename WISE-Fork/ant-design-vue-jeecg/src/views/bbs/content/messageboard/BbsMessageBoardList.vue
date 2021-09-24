<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="内容">
              <a-input placeholder="请输入内容" v-model="queryParam.content"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="10" :lg="11" :md="12" :sm="24">
            <a-form-item label="点赞总数">
              <a-input placeholder="请输入最小值" class="query-group-cust" v-model="queryParam.praiseCount_begin"></a-input>
              <span class="query-group-split-cust"></span>
              <a-input placeholder="请输入最大值" class="query-group-cust" v-model="queryParam.praiseCount_end"></a-input>
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="区域编码">
                <a-input placeholder="请输入区域编码" v-model="queryParam.regionCode"></a-input>
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="创建人">
                <a-input placeholder="请输入创建人" v-model="queryParam.createBy"></a-input>
              </a-form-item>
            </a-col>
            <a-col :xl="10" :lg="11" :md="12" :sm="24">
              <a-form-item label="举报次数">
                <a-input placeholder="请输入最小值" class="query-group-cust" v-model="queryParam.informCount_begin"></a-input>
                <span class="query-group-split-cust"></span>
                <a-input placeholder="请输入最大值" class="query-group-cust" v-model="queryParam.informCount_end"></a-input>
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
      <a-button type="primary" icon="download" @click="handleExportXls('留言板')">导出</a-button>
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
          <div v-html="text" :style="{
              maxWidth: '400px',
              whiteSpace: 'nowrap',
              textOverflow: 'ellipsis',
              overflow: 'hidden',
              wordWrap: 'break-word',
              wordBreak: 'break-all',
            }"></div>
        </template>
<!--        <template slot="htmlSlot" slot-scope="text">-->
<!--          <div v-html="text"></div>-->
<!--        </template>-->
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

    <bbs-message-board-modal ref="modalForm" @ok="modalFormOk"></bbs-message-board-modal>
  </a-card>
</template>

<script>
  // ****行星万象修改位置戳****
  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import BbsMessageBoardModal from './modules/BbsMessageBoardModal'

  export default {
    name: 'BbsMessageBoardList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      BbsMessageBoardModal
    },
    data () {
      return {
        description: '留言板管理页面',
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
            title:'头像',
            align:"center",
            sorter: true,
            dataIndex: 'avatar',
            scopedSlots: {customRender: 'imgSlot'}
          },
          {
            title:'用户',
            align:"center",
            sorter: true,
            dataIndex: 'createByName'
          },
          {
            title:'内容',
            align:"center",
            sorter: true,
            width: 200,
            // ellipsis:true,
            dataIndex: 'content',
            scopedSlots: {customRender: 'htmlSlot'}
          },
          {
            title:'点赞总数',
            align:"center",
            sorter: true,
            dataIndex: 'praiseCount'
          },
          // {
          //   title:'区域编码',
          //   align:"center",
          //   sorter: true,
          //   dataIndex: 'regionCode'
          // },
          {
            title:'区域',
            align:"center",
            sorter: true,
            dataIndex: 'regionName'
          },
          // {
          //   title:'创建人',
          //   align:"center",
          //   sorter: true,
          //   dataIndex: 'createBy'
          // },
          {
            title:'举报次数',
            align:"center",
            sorter: true,
            dataIndex: 'informCount'
          },
          {
            title:'创建时间',
            align:"center",
            sorter: true,
            dataIndex: 'createTime'
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
          // list: "/bbs/bbsMessageBoard/list",
          list: "/bbs/bbsMessageBoard/wise/back/list",
          delete: "/bbs/bbsMessageBoard/delete",
          deleteBatch: "/bbs/bbsMessageBoard/deleteBatch",
          exportXlsUrl: "/bbs/bbsMessageBoard/exportXls",
          importExcelUrl: "bbs/bbsMessageBoard/importExcel",
          
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
        fieldList.push({type:'string',value:'content',text:'内容',dictCode:''})
        fieldList.push({type:'int',value:'praiseCount',text:'点赞总数',dictCode:''})
        fieldList.push({type:'string',value:'regionCode',text:'区域编码',dictCode:''})
        fieldList.push({type:'string',value:'regionName',text:'区域',dictCode:''})
        fieldList.push({type:'string',value:'createBy',text:'创建人',dictCode:''})
        fieldList.push({type:'string',value:'avatar',text:'头像',dictCode:''})
        fieldList.push({type:'string',value:'createByName',text:'用户',dictCode:''})
        fieldList.push({type:'int',value:'informCount',text:'举报次数',dictCode:''})
        fieldList.push({type:'datetime',value:'createTime',text:'创建时间'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>