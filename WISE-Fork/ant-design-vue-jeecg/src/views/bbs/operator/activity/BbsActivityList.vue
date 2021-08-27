<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="活动标题">
              <a-input placeholder="请输入活动标题" v-model="queryParam.title"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="10" :lg="11" :md="12" :sm="24">
            <a-form-item label="截止时间">
              <j-date :show-time="true" date-format="YYYY-MM-DD HH:mm:ss" placeholder="请选择开始时间" class="query-group-cust"
                      v-model="queryParam.endTime_begin"></j-date>
              <span class="query-group-split-cust"></span>
              <j-date :show-time="true" date-format="YYYY-MM-DD HH:mm:ss" placeholder="请选择结束时间" class="query-group-cust"
                      v-model="queryParam.endTime_end"></j-date>
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="活动类型">
                <j-dict-select-tag placeholder="请选择活动类型" v-model="queryParam.activityType"
                                   dictCode="bbs_activity_type"/>
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="推送类型">
                <j-dict-select-tag placeholder="请选择推送类型" v-model="queryParam.pushType"
                                   dictCode="bbs_activity_push_type"/>
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="区域编码">
                <a-input placeholder="请输入区域编码" v-model="queryParam.regionCode"></a-input>
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
      <a-button type="primary" icon="download" @click="handleExportXls('活动')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl"
                @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
      <!-- 高级查询区域 -->
      <j-super-query :fieldList="superFieldList" ref="superQueryModal"
                     @handleSuperQuery="handleSuperQuery"></j-super-query>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel">
            <a-icon type="delete"/>
            删除
          </a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作
          <a-icon type="down"/>
        </a-button>
      </a-dropdown>
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a
        style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
        ref="table"
        size="middle"
        :scroll="{x:3500}"
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
          <img v-else :src="getImgView(text)" height="25px" alt=""
               style="max-width:80px;font-size: 12px;font-style: italic;"/>
        </template>
        <template slot="pcaSlot" slot-scope="text">
          <div>{{ getPcaText(text) }}</div>
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

          <a-divider type="vertical"/>
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down"/></a>
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

    <bbs-activity-modal ref="modalForm" @ok="modalFormOk"></bbs-activity-modal>
  </a-card>
</template>

<script>

import '@/assets/less/TableExpand.less'
import {mixinDevice} from '@/utils/mixin'
import {JeecgListMixin} from '@/mixins/JeecgListMixin'
import BbsActivityModal from './modules/BbsActivityModal'
import Area from '@/components/_util/Area'

export default {
  name: 'BbsActivityList',
  mixins: [JeecgListMixin, mixinDevice],
  components: {
    BbsActivityModal
  },
  data() {
    return {
      description: '活动管理页面',
      // 表头
      columns: [
        {
          title: '#',
          dataIndex: '',
          key: 'rowIndex',
          width: 60,
          align: "center",
          customRender: function (t, r, index) {
            return parseInt(index) + 1;
          }
        },
        {
          title: '活动标题',
          align: "center",
          dataIndex: 'title',
          width: 300,
        },
        {
          title: '封面',
          align: "center",
          dataIndex: 'imageUrl',
          scopedSlots: {customRender: 'imgSlot'}
        },
        {
          title: '活动内容',
          align: "center",
          dataIndex: 'content',
          // scopedSlots: {customRender: 'htmlSlot'}
          width: 500,
          ellipsis: true,
        },
        {
          title: '开始时间',
          align: "center",
          sorter: true,
          dataIndex: 'startTime'
        },
        {
          title: '截止时间',
          align: "center",
          sorter: true,
          dataIndex: 'endTime'
        },
        {
          title: '需要人数',
          align: "center",
          sorter: true,
          dataIndex: 'needPeopleCount'
        },
        {
          title: '活动现状',
          align: "center",
          dataIndex: 'activitySituation'
        },
        {
          title: '负责人',
          align: "center",
          dataIndex: 'princiaplPeopleName'
        },
        {
          title: '手机',
          align: "center",
          dataIndex: 'phoneNumber'
        },
        {
          title: '微信',
          align: "center",
          dataIndex: 'wechat'
        },
        {
          title: '访问总数',
          align: "center",
          sorter: true,
          dataIndex: 'hitsCount'
        },
        {
          title: '省市区',
          align: "center",
          dataIndex: 'provinces',
          scopedSlots: {customRender: 'pcaSlot'}
        },
        {
          title: '地点',
          align: "center",
          dataIndex: 'site'
        },
        {
          title: '活动类型',
          align: "center",
          dataIndex: 'activityType_dictText'
        },
        {
          title: '推送类型',
          align: "center",
          dataIndex: 'pushType_dictText'
        },
        {
          title: '区域编码',
          align: "center",
          dataIndex: 'regionCode'
        },
        {
          title: '跳转链接',
          align: "center",
          dataIndex: 'skipUrl'
        },
        {
          title: '操作',
          dataIndex: 'action',
          align: "center",
          fixed: "right",
          width: 147,
          scopedSlots: {customRender: 'action'}
        }
      ],
      url: {
        list: "/bbs/bbsActivity/list",
        delete: "/bbs/bbsActivity/delete",
        deleteBatch: "/bbs/bbsActivity/deleteBatch",
        exportXlsUrl: "/bbs/bbsActivity/exportXls",
        importExcelUrl: "bbs/bbsActivity/importExcel",

      },
      dictOptions: {},
      pcaData: '',
      superFieldList: [],
    }
  },
  created() {
    this.pcaData = new Area()
    this.getSuperFieldList();
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
    },
  },
  methods: {
    getPcaText(code) {
      return this.pcaData.getText(code);
    },
    initDictConfig() {
    },
    getSuperFieldList() {
      let fieldList = [];
      fieldList.push({type: 'string', value: 'title', text: '活动标题', dictCode: ''})
      fieldList.push({type: 'string', value: 'imageUrl', text: '封面', dictCode: ''})
      fieldList.push({type: 'Text', value: 'content', text: '活动内容', dictCode: ''})
      fieldList.push({type: 'datetime', value: 'startTime', text: '开始时间'})
      fieldList.push({type: 'datetime', value: 'endTime', text: '截止时间'})
      fieldList.push({type: 'int', value: 'needPeopleCount', text: '需要人数', dictCode: ''})
      fieldList.push({type: 'string', value: 'activitySituation', text: '活动现状', dictCode: ''})
      fieldList.push({type: 'string', value: 'princiaplPeopleName', text: '负责人', dictCode: ''})
      fieldList.push({type: 'string', value: 'phoneNumber', text: '手机', dictCode: ''})
      fieldList.push({type: 'string', value: 'wechat', text: '微信', dictCode: ''})
      fieldList.push({type: 'int', value: 'hitsCount', text: '访问总数', dictCode: ''})
      fieldList.push({type: 'pca', value: 'provinces', text: '省市区'})
      fieldList.push({type: 'string', value: 'site', text: '地点', dictCode: ''})
      fieldList.push({type: 'string', value: 'activityType', text: '活动类型', dictCode: 'bbs_activity_type'})
      fieldList.push({type: 'string', value: 'pushType', text: '推送类型', dictCode: 'bbs_activity_push_type'})
      fieldList.push({type: 'string', value: 'regionCode', text: '区域编码', dictCode: ''})
      fieldList.push({type: 'string', value: 'skipUrl', text: '跳转链接', dictCode: ''})
      this.superFieldList = fieldList
    }
  }
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>