<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="标题">
              <a-input placeholder="请输入标题" v-model="queryParam.title"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="标语">
              <a-input placeholder="请输入标语" v-model="queryParam.tagline"></a-input>
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="APPID">
                <a-input placeholder="请输入APPID" v-model="queryParam.appId"></a-input>
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="PATH">
                <a-input placeholder="请输入PATH" v-model="queryParam.path"></a-input>
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="逻辑删除标志">
                <j-dict-select-tag placeholder="请选择逻辑删除标志" v-model="queryParam.deleteFlag" dictCode="bbs_delete_flag"/>
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="状态">
                <j-dict-select-tag placeholder="请选择状态" v-model="queryParam.status" dictCode="bbs_status"/>
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="序号">
                <a-input placeholder="请输入序号" v-model="queryParam.sort"></a-input>
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
      <a-button type="primary" icon="download" @click="handleExportXls('外卖推广')">导出</a-button>
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
        :scroll="{x:1800}"
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

    <bbs-waimai-modal ref="modalForm" @ok="modalFormOk"></bbs-waimai-modal>
  </a-card>
</template>

<script>

import '@assets/less/TableExpand.less'
import {mixinDevice} from '@/utils/mixin'
import {JeecgListMixin} from '@/mixins/JeecgListMixin'
import BbsWaimaiModal from './modules/BbsWaimaiModal'

export default {
  name: 'BbsWaimaiList',
  mixins: [JeecgListMixin, mixinDevice],
  components: {
    BbsWaimaiModal
  },
  data() {
    return {
      description: '外卖推广管理页面',
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
          title: 'Logo',
          align: "center",
          sorter: true,
          dataIndex: 'logoUrl',
          scopedSlots: {customRender: 'imgSlot'}
        },
        {
          title: '标题',
          align: "center",
          sorter: true,
          dataIndex: 'title'
        },
        {
          title: '标语',
          align: "center",
          sorter: true,
          dataIndex: 'tagline'
        },
        {
          title: '主图',
          align: "center",
          sorter: true,
          dataIndex: 'masterUrl',
          scopedSlots: {customRender: 'imgSlot'}
        },
        {
          title: 'APPID',
          align: "center",
          sorter: true,
          dataIndex: 'appId'
        },
        {
          title: 'PATH',
          align: "center",
          sorter: true,
          width: 200,
          ellipsis: true,
          dataIndex: 'path'
        },
        {
          title: '逻辑删除标志',
          align: "center",
          sorter: true,
          dataIndex: 'deleteFlag_dictText'
        },
        {
          title: '状态',
          align: "center",
          sorter: true,
          dataIndex: 'status_dictText'
        },
        {
          title: '序号',
          align: "center",
          dataIndex: 'sort'
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
        // ****行星万象修改位置戳****
        list: "/bbs/bbsWaimai/list",
        delete: "/bbs/bbsWaimai/delete",
        deleteBatch: "/bbs/bbsWaimai/deleteBatch",
        exportXlsUrl: "/bbs/bbsWaimai/exportXls",
        importExcelUrl: "bbs/bbsWaimai/importExcel",

      },
      dictOptions: {},
      superFieldList: [],
    }
  },
  created() {
    this.getSuperFieldList();
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
    },
  },
  methods: {
    initDictConfig() {
    },
    getSuperFieldList() {
      let fieldList = [];
      fieldList.push({type: 'string', value: 'logoUrl', text: 'Logo', dictCode: ''})
      fieldList.push({type: 'string', value: 'title', text: '标题', dictCode: ''})
      fieldList.push({type: 'string', value: 'tagline', text: '标语', dictCode: ''})
      fieldList.push({type: 'string', value: 'masterUrl', text: '主图', dictCode: ''})
      fieldList.push({type: 'string', value: 'appId', text: 'APPID', dictCode: ''})
      fieldList.push({type: 'string', value: 'path', text: 'PATH', dictCode: ''})
      fieldList.push({type: 'int', value: 'deleteFlag', text: '逻辑删除标志', dictCode: 'bbs_delete_flag'})
      fieldList.push({type: 'int', value: 'status', text: '状态', dictCode: 'bbs_status'})
      fieldList.push({type: 'int', value: 'sort', text: '序号', dictCode: ''})
      this.superFieldList = fieldList
    }
  }
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>