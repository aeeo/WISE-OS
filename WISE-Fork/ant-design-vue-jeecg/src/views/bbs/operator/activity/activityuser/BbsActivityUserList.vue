<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('活动用户')">导出</a-button>
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

    <bbs-activity-user-modal ref="modalForm" @ok="modalFormOk"></bbs-activity-user-modal>
  </a-card>
</template>

<script>

import '@/assets/less/TableExpand.less'
import {mixinDevice} from '@/utils/mixin'
import {JeecgListMixin} from '@/mixins/JeecgListMixin'
import BbsActivityUserModal from './modules/BbsActivityUserModal'

export default {
  name: 'BbsActivityUserList',
  mixins: [JeecgListMixin, mixinDevice],
  components: {
    BbsActivityUserModal
  },
  data() {
    return {
      description: '活动用户管理页面',
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
        // {
        //   title: '活动ID',
        //   align: "center",
        //   sorter: true,
        //   dataIndex: 'activityId'
        // },
        {
          title: '活动',
          align: "center",
          sorter: true,
          dataIndex: 'activityTitle'
        },
        {
          title: '姓名',
          align: "center",
          sorter: true,
          dataIndex: 'chineseName'
        },
        {
          title: '手机',
          align: "center",
          sorter: true,
          dataIndex: 'phoneNumber'
        },
        {
          title: '微信',
          align: "center",
          sorter: true,
          dataIndex: 'wechat'
        },
        {
          title: '备注',
          align: "center",
          dataIndex: 'remark'
        },
        {
          title: '报名时间',
          align: "center",
          sorter: true,
          dataIndex: 'createTime'
        },
        {
          title: '报名状态',
          align: "center",
          sorter: true,
          dataIndex: 'applyStatus_dictText'
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
        // list: "/bbs/bbsActivityUser/list",
        list: "/bbs/bbsActivityUser/wise/back/list",
        delete: "/bbs/bbsActivityUser/delete",
        deleteBatch: "/bbs/bbsActivityUser/deleteBatch",
        exportXlsUrl: "/bbs/bbsActivityUser/exportXls",
        importExcelUrl: "bbs/bbsActivityUser/importExcel",

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
      fieldList.push({type: 'string', value: 'activityId', text: '活动ID', dictCode: ''})
      fieldList.push({type: 'string', value: 'activityTitle', text: '活动', dictCode: ''})
      fieldList.push({type: 'string', value: 'chineseName', text: '姓名', dictCode: ''})
      fieldList.push({type: 'string', value: 'phoneNumber', text: '手机', dictCode: ''})
      fieldList.push({type: 'string', value: 'wechat', text: '微信', dictCode: ''})
      fieldList.push({type: 'string', value: 'remark', text: '备注', dictCode: ''})
      fieldList.push({type: 'datetime', value: 'createTime', text: '报名时间'})
      fieldList.push({type: 'string', value: 'applyStatus', text: '报名状态', dictCode: 'bbs_activity_user_apply_status'})
      this.superFieldList = fieldList
    }
  }
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>