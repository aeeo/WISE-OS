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
            <a-form-item label="内容">
              <a-input placeholder="请输入内容" v-model="queryParam.content"></a-input>
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="类型">
                <j-dict-select-tag placeholder="请选择类型" v-model="queryParam.topicType" dictCode="bbs_topic_type"/>
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="是否删除">
                <a-input placeholder="请输入是否删除" v-model="queryParam.delFlag"></a-input>
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="帖子状态">
                <j-dict-select-tag placeholder="请选择帖子状态" v-model="queryParam.status" dictCode="bbs_status"/>
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="联系方式">
                <a-input placeholder="请输入联系方式" v-model="queryParam.contact"></a-input>
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="地址">
                <j-area-linkage type="cascader" v-model="queryParam.site" placeholder="请选择省市区"/>
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="区域编码">
                <a-input placeholder="请输入区域编码" v-model="queryParam.regionCode"></a-input>
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="区域全名">
                <a-input placeholder="请输入区域全名" v-model="queryParam.regionFullName"></a-input>
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="举报次数">
                <a-input placeholder="请输入举报次数" v-model="queryParam.informCount"></a-input>
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="版块编码">
                <a-input placeholder="请输入版块编码" v-model="queryParam.classCode"></a-input>
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="发布时间">
                <j-date :show-time="true" date-format="YYYY-MM-DD HH:mm:ss" placeholder="请选择发布时间"
                        v-model="queryParam.publicTime"></j-date>
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
      <a-button type="primary" icon="download" @click="handleExportXls('帖子')">导出</a-button>
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
        bordered
        rowKey="id"
        class="j-table-force-nowrap"
        :scroll="{x:2500}"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :isorter="isorter"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        @change="handleTableChange">

        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text" :style="{
              maxWidth: '700px',
              whiteSpace: 'nowrap',
              textOverflow: 'ellipsis',
              overflow: 'hidden',
              wordWrap: 'break-word',
              wordBreak: 'break-all',
            }"></div>
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
        <template slot="pcaSlot" slot-scope="text">
          <div>{{ getPcaText(text) }}</div>
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

    <bbs-topic-modal ref="modalForm" @ok="modalFormOk"/>
  </a-card>
</template>

<script>

import {JeecgListMixin} from '@/mixins/JeecgListMixin'
import BbsTopicModal from './modules/BbsTopicModal'
import Area from '@/components/_util/Area'
import '@/assets/less/TableExpand.less'

export default {
  name: "BbsTopicList",
  mixins: [JeecgListMixin],
  components: {
    BbsTopicModal
  },
  data() {
    return {
      description: '帖子管理页面',
      isorter: {
        column: 'publicTime',
        order: 'desc',
      },
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
        //   title: '创建人',
        //   align: "center",
        //   sorter: true,
        //   dataIndex: 'createBy'
        // },
        {
          title: '头像',
          align: "center",
          sorter: true,
          width: 80,
          dataIndex: 'createByAvatar',
          scopedSlots: {customRender: 'imgSlot'}
        },
        {
          title: '用户',
          align: "center",
          sorter: true,
          width: 150,
          dataIndex: 'createByName'
        },
        {
          title: '标题',
          align: "center",
          sorter: true,
          dataIndex: 'title'
        },
        {
          title: '内容',
          align: "center",
          sorter: true,
          dataIndex: 'content',
          width: 500,
          ellipsis: true,
          // scopedSlots: {customRender: 'htmlSlot'}
        },
        {
          title: '类型',
          align: "center",
          sorter: true,
          width: 100,
          dataIndex: 'topicType_dictText'
        },
        {
          title: '访问总数',
          align: "center",
          sorter: true,
          width: 100,
          dataIndex: 'hitsCount'
        },
        {
          title: '回复总数',
          align: "center",
          sorter: true,
          width: 100,
          dataIndex: 'replyCount'
        },
        {
          title: '点赞总数',
          align: "center",
          sorter: true,
          width: 100,
          dataIndex: 'praiseCount'
        },
        {
          title: '收藏总数',
          align: "center",
          sorter: true,
          width: 100,
          dataIndex: 'starCount'
        },
        {
          title: '举报次数',
          align: "center",
          sorter: true,
          width: 100,
          dataIndex: 'informCount'
        },
        {
          title: '区域全名',
          align: "center",
          sorter: true,
          dataIndex: 'regionFullName'
        },
        {
          title: '版块编码',
          align: "center",
          sorter: true,
          width: 100,
          dataIndex: 'classCode'
        },
        // {
        //   title: '所属部门',
        //   align: "center",
        //   dataIndex: 'sysOrgCode'
        // },
        {
          title: '发布时间',
          align: "center",
          sorter: true,
          dataIndex: 'publicTime'
        },
        {
          title: '编辑时间',
          align: "center",
          sorter: true,
          dataIndex: 'editTime'
        },
        {
          title: '操作',
          dataIndex: 'action',
          align: "center",
          fixed: "right",
          width: 147,
          scopedSlots: {customRender: 'action'},
        }
      ],
      url: {
        // list: "/bbs/bbsTopic/list",
        list: "/bbs/bbsTopic/wise/back/list",
        delete: "/bbs/bbsTopic/delete",
        deleteBatch: "/bbs/bbsTopic/deleteBatch",
        exportXlsUrl: "/bbs/bbsTopic/exportXls",
        importExcelUrl: "bbs/bbsTopic/importExcel",

      },
      dictOptions: {},
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
    }
  },
  methods: {
    getPcaText(code) {
      return this.pcaData.getText(code);
    },
    initDictConfig() {
    },
    getSuperFieldList() {
      let fieldList = [];
      fieldList.push({type: 'string', value: 'createBy', text: '创建人', dictCode: ''})
      fieldList.push({type: 'string', value: 'createByName', text: '用户', dictCode: ''})
      fieldList.push({type: 'string', value: 'createByAvatar', text: '头像', dictCode: ''})
      fieldList.push({type: 'datetime', value: 'createTime', text: '创建日期'})
      fieldList.push({type: 'string', value: 'speciaiId', text: '版块专题ID', dictCode: ''})
      fieldList.push({type: 'string', value: 'title', text: '标题', dictCode: ''})
      fieldList.push({type: 'Text', value: 'content', text: '内容', dictCode: ''})
      fieldList.push({type: 'string', value: 'topicType', text: '类型', dictCode: 'bbs_topic_type'})
      fieldList.push({type: 'string', value: 'enabled', text: '是否启用', dictCode: ''})
      fieldList.push({type: 'int', value: 'hitsCount', text: '访问总数', dictCode: ''})
      fieldList.push({type: 'int', value: 'replyCount', text: '回复总数', dictCode: ''})
      fieldList.push({type: 'string', value: 'repliedBy', text: '最后回复用户ID', dictCode: ''})
      fieldList.push({type: 'datetime', value: 'repliedTime', text: '最后回复时间'})
      fieldList.push({type: 'string', value: 'iconId', text: '帖子图标ID', dictCode: ''})
      fieldList.push({type: 'string', value: 'delFlag', text: '是否删除', dictCode: ''})
      fieldList.push({type: 'string', value: 'status', text: '帖子状态', dictCode: 'bbs_status'})
      fieldList.push({type: 'string', value: 'contact', text: '联系方式', dictCode: ''})
      fieldList.push({type: 'pca', value: 'site', text: '地址'})
      fieldList.push({type: 'string', value: 'hotReply', text: '热门回复ID', dictCode: ''})
      fieldList.push({type: 'int', value: 'praiseCount', text: '点赞总数', dictCode: ''})
      fieldList.push({type: 'int', value: 'starCount', text: '收藏总数', dictCode: ''})
      fieldList.push({type: 'string', value: 'regionCode', text: '区域编码', dictCode: ''})
      fieldList.push({type: 'string', value: 'regionFullName', text: '区域全名', dictCode: ''})
      fieldList.push({type: 'int', value: 'informCount', text: '举报次数', dictCode: ''})
      fieldList.push({type: 'string', value: 'classCode', text: '版块编码', dictCode: ''})
      fieldList.push({type: 'string', value: 'sysOrgCode', text: '所属部门', dictCode: ''})
      fieldList.push({type: 'datetime', value: 'publicTime', text: '发布时间'})
      fieldList.push({type: 'datetime', value: 'editTime', text: '编辑时间'})
      this.superFieldList = fieldList
    }
  }
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>