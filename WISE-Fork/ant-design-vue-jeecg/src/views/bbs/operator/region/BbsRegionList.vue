<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper" v-has="'region:query'">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="区域编码">
              <a-input placeholder="请输入区域编码" v-model="queryParam.regionCode"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="全称">
              <a-input placeholder="请输入全称" v-model="queryParam.fullName"></a-input>
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="简称">
                <a-input placeholder="请输入简称" v-model="queryParam.name"></a-input>
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="省份">
                <a-input placeholder="请输入省份" v-model="queryParam.province"></a-input>
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="区域类型">
                <j-dict-select-tag placeholder="请选择区域类型" v-model="queryParam.regionType" dictCode="bbs_region_type"/>
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="缩放等级">
                <a-input placeholder="请输入缩放等级" v-model="queryParam.scale"></a-input>
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="范围半径">
                <a-input placeholder="请输入范围半径" v-model="queryParam.radius"></a-input>
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="区域等级">
                <a-input placeholder="请输入区域等级" v-model="queryParam.regionGrade"></a-input>
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="状态">
                <j-dict-select-tag placeholder="请选择状态" v-model="queryParam.regionStatus" dictCode="bbs_region_region_status"/>
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
      <a-button @click="handleAdd" v-has="'region:add'" type="primary" icon="plus">新增</a-button>
      <a-button v-has="'region:export'" type="primary" icon="download" @click="handleExportXls('地区')">导出</a-button>
      <a-upload v-has="'region:import'" name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
      <!-- 高级查询区域 -->
      <j-super-query v-has="'region:expert'" :fieldList="superFieldList" ref="superQueryModal" @handleSuperQuery="handleSuperQuery"></j-super-query>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel" v-has="'region:delete'"><a-icon type="delete"/>删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px" v-has="'region:multiple'"> 批量操作 <a-icon type="down" /></a-button>
      </a-dropdown>
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;" v-has="'region:expert'">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
        ref="table"
        size="middle"
        bordered
        rowKey="id"
        class="j-table-force-nowrap"
        :scroll="{x:true}"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
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

    <bbs-region-modal ref="modalForm" @ok="modalFormOk"/>
  </a-card>
</template>

<script>

import {JeecgListMixin} from '@/mixins/JeecgListMixin'
import BbsRegionModal from './modules/BbsRegionModal'
import '@/assets/less/TableExpand.less'

export default {
    name: "BbsRegionList",
    mixins:[JeecgListMixin],
    components: {
      BbsRegionModal
    },
    data () {
      return {
        description: '地区管理页面',
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
            title:'区域编码',
            align:"center",
            sorter: true,
            dataIndex: 'regionCode'
          },
          {
            title:'全称',
            align:"center",
            sorter: true,
            dataIndex: 'fullName'
          },
          {
            title:'简称',
            align:"center",
            sorter: true,
            dataIndex: 'name'
          },
          {
            title:'经度',
            align:"center",
            sorter: true,
            dataIndex: 'longitude'
          },
          {
            title:'纬度',
            align:"center",
            sorter: true,
            dataIndex: 'latitude'
          },
          {
            title:'地图展示名',
            align:"center",
            dataIndex: 'content'
          },
          {
            title:'省份',
            align:"center",
            sorter: true,
            dataIndex: 'province'
          },
          {
            title:'区域类型',
            align:"center",
            sorter: true,
            dataIndex: 'regionType_dictText'
          },
          {
            title:'缩放等级',
            align:"center",
            sorter: true,
            dataIndex: 'scale'
          },
          {
            title:'范围半径',
            align:"center",
            sorter: true,
            dataIndex: 'radius'
          },
          {
            title:'区域等级',
            align:"center",
            sorter: true,
            dataIndex: 'regionGrade'
          },
          {
            title:'公众号二维码',
            align:"center",
            dataIndex: 'officeAccountImage',
            scopedSlots: {customRender: 'imgSlot'}
          },
          {
            title:'区域头像',
            align:"center",
            dataIndex: 'regionImage',
            scopedSlots: {customRender: 'imgSlot'}
          },
          {
            title:'收款码',
            align:"center",
            dataIndex: 'payeeImage',
            scopedSlots: {customRender: 'imgSlot'}
          },
          {
            title:'朋友圈二维码',
            align:"center",
            dataIndex: 'circleFriendsImage',
            scopedSlots: {customRender: 'imgSlot'}
          },
          {
            title:'状态',
            align:"center",
            sorter: true,
            dataIndex: 'regionStatus_dictText'
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            fixed:"right",
            width:147,
            scopedSlots: { customRender: 'action' },
          }
        ],
        url: {
          // ****行星万象修改位置戳****
          // list: "/bbs/bbsRegion/list",
          list: "/bbs/bbsRegion/wise/back/list",
          delete: "/bbs/bbsRegion/delete",
          deleteBatch: "/bbs/bbsRegion/deleteBatch",
          exportXlsUrl: "/bbs/bbsRegion/exportXls",
          importExcelUrl: "bbs/bbsRegion/importExcel",
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
      }
    },
    methods: {
      initDictConfig(){
      },
      getSuperFieldList(){
        let fieldList=[];
         fieldList.push({type:'string',value:'regionCode',text:'区域编码',dictCode:''})
         fieldList.push({type:'string',value:'fullName',text:'全称',dictCode:''})
         fieldList.push({type:'string',value:'name',text:'简称',dictCode:''})
         fieldList.push({type:'string',value:'longitude',text:'经度',dictCode:''})
         fieldList.push({type:'string',value:'latitude',text:'纬度',dictCode:''})
         fieldList.push({type:'string',value:'content',text:'地图展示名',dictCode:''})
         fieldList.push({type:'string',value:'province',text:'省份',dictCode:''})
         fieldList.push({type:'int',value:'dayPublishTopic',text:'每日发布贴子数',dictCode:''})
         fieldList.push({type:'int',value:'weekPublishTopic',text:'每周发布贴子数',dictCode:''})
         fieldList.push({type:'int',value:'dayPublishMessage',text:'每日发布留言数',dictCode:''})
         fieldList.push({type:'int',value:'weekPublishMessage',text:'每周发布留言数',dictCode:''})
         fieldList.push({type:'int',value:'dayPublishReply',text:'每日评论数',dictCode:''})
         fieldList.push({type:'int',value:'weekPublishReply',text:'每周评论数',dictCode:''})
         fieldList.push({type:'int',value:'dayPublishPraise',text:'每日点赞数',dictCode:''})
         fieldList.push({type:'int',value:'weekPublishPraise',text:'每周点赞数',dictCode:''})
         fieldList.push({type:'int',value:'regionType',text:'区域类型',dictCode:'bbs_region_type'})
         fieldList.push({type:'int',value:'scale',text:'缩放等级',dictCode:''})
         fieldList.push({type:'int',value:'radius',text:'范围半径',dictCode:''})
         fieldList.push({type:'int',value:'regionGrade',text:'区域等级',dictCode:''})
         fieldList.push({type:'string',value:'officeAccountImage',text:'公众号二维码',dictCode:''})
         fieldList.push({type:'string',value:'regionImage',text:'区域头像',dictCode:''})
         fieldList.push({type:'string',value:'payeeImage',text:'收款码',dictCode:''})
         fieldList.push({type:'string',value:'circleFriendsImage',text:'朋友圈二维码',dictCode:''})
         fieldList.push({type:'int',value:'regionStatus',text:'状态',dictCode:'bbs_region_region_status'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>