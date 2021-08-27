<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <!-- 主表单区域 -->
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="topicType">
              <j-dict-select-tag type="list" v-model="model.topicType" dictCode="bbs_topic_type" placeholder="请选择类型"/>
            </a-form-model-item>
          </a-col>
          <!--          <a-col :span="24">-->
          <!--            <a-form-model-item label="版块专题ID" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="speciaiId">-->
          <!--              <a-input v-model="model.speciaiId" placeholder="请输入版块专题ID" disabled></a-input>-->
          <!--            </a-form-model-item>-->
          <!--          </a-col>-->
          <a-col :span="24">
            <a-form-model-item label="标题" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="title">
              <a-input v-model="model.title" placeholder="请输入标题"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="内容" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="content">
              <j-editor v-model="model.content"/>
            </a-form-model-item>
          </a-col>

          <!--          <a-col :span="24">-->
          <!--            <a-form-model-item label="是否启用" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="enabled">-->
          <!--              <j-dict-select-tag type="list" v-model="model.enabled" dictCode="" placeholder="请选择是否启用"/>-->
          <!--            </a-form-model-item>-->
          <!--          </a-col>-->
          <!--          <a-col :span="24">-->
          <!--            <a-form-model-item label="访问总数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="hitsCount">-->
          <!--              <a-input-number v-model="model.hitsCount" placeholder="请输入访问总数" style="width: 100%"/>-->
          <!--            </a-form-model-item>-->
          <!--          </a-col>-->
          <!--          <a-col :span="24">-->
          <!--            <a-form-model-item label="回复总数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="replyCount">-->
          <!--              <a-input-number v-model="model.replyCount" placeholder="请输入回复总数" style="width: 100%"/>-->
          <!--            </a-form-model-item>-->
          <!--          </a-col>-->

          <!--          <a-col :span="24">-->
          <!--            <a-form-model-item label="帖子图标ID" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="iconId">-->
          <!--              <a-input v-model="model.iconId" placeholder="请输入帖子图标ID"></a-input>-->
          <!--            </a-form-model-item>-->
          <!--          </a-col>-->
          <!--          <a-col :span="24">-->
          <!--            <a-form-model-item label="是否删除" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="delFlag">-->
          <!--              <j-dict-select-tag type="list" v-model="model.delFlag" dictCode="" placeholder="请选择是否删除"/>-->
          <!--            </a-form-model-item>-->
          <!--          </a-col>-->
          <a-col :span="24">
            <a-form-model-item label="帖子状态" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="status">
              <j-dict-select-tag type="list" v-model="model.status" dictCode="bbs_status" placeholder="请选择帖子状态"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="联系方式" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="contact">
              <a-input v-model="model.contact" placeholder="请输入联系方式"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="版块编码" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="classCode">
              <a-input v-model="model.classCode" placeholder="请输入版块编码"></a-input>
            </a-form-model-item>
          </a-col>
          <!--          <a-col :span="24">-->
          <!--            <a-form-model-item label="地址" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="site">-->
          <!--              <j-area-linkage type="cascader" v-model="model.site" placeholder="请输入省市区"/>-->
          <!--            </a-form-model-item>-->
          <!--          </a-col>-->
          <a-col :span="24">
            <a-form-model-item label="热门回复ID" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="hotReply">
              <a-input v-model="model.hotReply" placeholder="请输入热门回复ID" disabled></a-input>
            </a-form-model-item>
          </a-col>
          <!--          <a-col :span="24">-->
          <!--            <a-form-model-item label="最后回复用户ID" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="repliedBy">-->
          <!--              <a-input v-model="model.repliedBy" placeholder="请输入最后回复用户ID"></a-input>-->
          <!--            </a-form-model-item>-->
          <!--          </a-col>-->
          <!--          <a-col :span="24">-->
          <!--            <a-form-model-item label="最后回复时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="repliedTime">-->
          <!--              <j-date placeholder="请选择最后回复时间" v-model="model.repliedTime" :show-time="true"-->
          <!--                      date-format="YYYY-MM-DD HH:mm:ss" style="width: 100%"/>-->
          <!--            </a-form-model-item>-->
          <!--          </a-col>-->
          <!--          <a-col :span="24">-->
          <!--            <a-form-model-item label="点赞总数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="praiseCount">-->
          <!--              <a-input-number v-model="model.praiseCount" placeholder="请输入点赞总数" style="width: 100%"/>-->
          <!--            </a-form-model-item>-->
          <!--          </a-col>-->
          <!--          <a-col :span="24">-->
          <!--            <a-form-model-item label="收藏总数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="starCount">-->
          <!--              <a-input-number v-model="model.starCount" placeholder="请输入收藏总数" style="width: 100%"/>-->
          <!--            </a-form-model-item>-->
          <!--          </a-col>-->
          <!--          <a-col :span="24">-->
          <!--            <a-form-model-item label="区域编码" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="regionCode">-->
          <!--              <a-input v-model="model.regionCode" placeholder="请输入区域编码" disabled></a-input>-->
          <!--            </a-form-model-item>-->
          <!--          </a-col>-->
          <!--          <a-col :span="24">-->
          <!--            <a-form-model-item label="区域全名" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="regionFullName">-->
          <!--              <a-input v-model="model.regionFullName" placeholder="请输入区域全名" disabled></a-input>-->
          <!--            </a-form-model-item>-->
          <!--          </a-col>-->
          <!--          <a-col :span="24">-->
          <!--            <a-form-model-item label="举报次数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="informCount">-->
          <!--              <a-input-number v-model="model.informCount" placeholder="请输入举报次数" style="width: 100%"/>-->
          <!--            </a-form-model-item>-->
          <!--          </a-col>-->
          <!--          <a-col :span="24">-->
          <!--            <a-form-model-item label="所属部门" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sysOrgCode">-->
          <!--              <a-input v-model="model.sysOrgCode" placeholder="请输入所属部门"></a-input>-->
          <!--            </a-form-model-item>-->
          <!--          </a-col>-->
          <a-col :span="24">
            <a-form-model-item label="发布时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="publicTime">
              <j-date placeholder="请选择发布时间" v-model="model.publicTime" :show-time="true"
                      date-format="YYYY-MM-DD HH:mm:ss" style="width: 100%" disabled/>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="编辑时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="editTime">
              <j-date placeholder="请选择编辑时间" v-model="model.editTime" :show-time="true" date-format="YYYY-MM-DD HH:mm:ss"
                      style="width: 100%" disabled/>
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </j-form-container>
    <!-- 子表单区域 -->
    <a-tabs v-model="activeKey" @change="handleChangeTabs">
      <a-tab-pane tab="帖子图片" :key="refKeys[0]" :forceRender="true">
        <j-editable-table
          :ref="refKeys[0]"
          :loading="bbsTopicImageTable.loading"
          :columns="bbsTopicImageTable.columns"
          :dataSource="bbsTopicImageTable.dataSource"
          :maxHeight="300"
          :disabled="formDisabled"
          :rowNumber="true"
          :rowSelection="true"
          :actionButton="true"/>
      </a-tab-pane>
      <a-tab-pane tab="帖子标签" :key="refKeys[1]" :forceRender="true">
        <j-editable-table
          :ref="refKeys[1]"
          :loading="bbsTopicTagTable.loading"
          :columns="bbsTopicTagTable.columns"
          :dataSource="bbsTopicTagTable.dataSource"
          :maxHeight="300"
          :disabled="formDisabled"
          :rowNumber="true"
          :rowSelection="true"
          :actionButton="true"/>
      </a-tab-pane>
      <a-tab-pane tab="帖子跳转链接" :key="refKeys[2]" :forceRender="true">
        <j-editable-table
          :ref="refKeys[2]"
          :loading="bbsTopicLinkTable.loading"
          :columns="bbsTopicLinkTable.columns"
          :dataSource="bbsTopicLinkTable.dataSource"
          :maxHeight="300"
          :disabled="formDisabled"
          :rowNumber="true"
          :rowSelection="true"
          :actionButton="true"/>
      </a-tab-pane>
    </a-tabs>
  </a-spin>
</template>

<script>

import {FormTypes, getRefPromise, VALIDATE_NO_PASSED} from '@/utils/JEditableTableUtil'
import {JEditableTableModelMixin} from '@/mixins/JEditableTableModelMixin'

export default {
  name: 'BbsTopicForm',
  mixins: [JEditableTableModelMixin],
  components: {},
  data() {
    return {
      labelCol: {
        xs: {span: 24},
        sm: {span: 6},
      },
      wrapperCol: {
        xs: {span: 24},
        sm: {span: 16},
      },
      labelCol2: {
        xs: {span: 24},
        sm: {span: 3},
      },
      wrapperCol2: {
        xs: {span: 24},
        sm: {span: 20},
      },
      model: {},
      // 新增时子表默认添加几行空数据
      addDefaultRowNum: 1,
      validatorRules: {},
      refKeys: ['bbsTopicImage', 'bbsTopicTag', 'bbsTopicLink',],
      tableKeys: ['bbsTopicImage', 'bbsTopicTag', 'bbsTopicLink',],
      activeKey: 'bbsTopicImage',
      // 帖子图片
      bbsTopicImageTable: {
        loading: false,
        dataSource: [],
        columns: [
          {
            title: '图片地址',
            key: 'topicImage',
            type: FormTypes.image,
            token: true,
            responseName: "message",
            number: 1,
            width: "200px",
            placeholder: '请输入${title}',
            defaultValue: '',
          },
          {
            title: '排序',
            key: 'sort',
            type: FormTypes.inputNumber,
            width: "200px",
            placeholder: '请输入${title}',
            defaultValue: '',
            validateRules: [{pattern: "z", message: "${title}格式不正确"}],
          },
          {
            title: '所属部门',
            key: 'sysOrgCode',
            type: FormTypes.input,
            width: "200px",
            placeholder: '请输入${title}',
            defaultValue: '',
          },
        ]
      },
      // 帖子标签
      bbsTopicTagTable: {
        loading: false,
        dataSource: [],
        columns: [
          // {
          //   title: '帖子ID',
          //   key: 'topicId',
          //   type: FormTypes.input,
          //   width: "200px",
          //   placeholder: '请输入${title}',
          //   defaultValue: '',
          // },
          // {
          //   title: '类型',
          //   key: 'topicType',
          //   type: FormTypes.input,
          //   width: "200px",
          //   placeholder: '请输入${topicType}',
          //   defaultValue: '',
          // },
          {
            title: '标签',
            key: 'tag',
            type: FormTypes.input,
            width: "200px",
            placeholder: '请输入${title}',
            defaultValue: '',
            validateRules: [{required: true, message: '${title}不能为空'}],
          },
          {
            title: '排序',
            key: 'sort',
            type: FormTypes.inputNumber,
            width: "200px",
            placeholder: '请输入${title}',
            defaultValue: '',
            validateRules: [{pattern: "n", message: "${title}格式不正确"}],
          },
          // {
          //   title: '所属部门',
          //   key: 'sysOrgCode',
          //   type: FormTypes.input,
          //   width: "200px",
          //   placeholder: '请输入${title}',
          //   defaultValue: '',
          // },
        ]
      },
      // 帖子跳转链接
      bbsTopicLinkTable: {
        loading: false,
        dataSource: [],
        columns: [

          {
            title: '标题',
            key: 'title',
            type: FormTypes.input,
            width: "200px",
            placeholder: '请输入${title}',
            defaultValue: '',
            validateRules: [{required: true, message: '${title}不能为空'}],
          },
          {
            title: '链接类型',
            key: 'linkType',
            type: FormTypes.select,
            dictCode: "bbs_topic_link_type",
            width: "200px",
            placeholder: '请输入${title}',
            defaultValue: '',
            validateRules: [{required: true, message: '${title}不能为空'}],
          },
          // {
          //   title: '帖子ID',
          //   key: 'topicId',
          //   type: FormTypes.input,
          //   width: "200px",
          //   placeholder: '请输入${title}',
          //   defaultValue: '',
          // },
          {
            title: '链接路径',
            key: 'linkUrl',
            type: FormTypes.input,
            width: "200px",
            placeholder: '请输入${title}',
            defaultValue: '',
            // validateRules: [{required: true, message: '${title}不能为空'}],
          },
          {
            title: '参数',
            key: 'parameter',
            type: FormTypes.input,
            width: "200px",
            placeholder: '请输入${title}',
            defaultValue: '',
          },
          {
            title: '排序',
            key: 'sort',
            type: FormTypes.inputNumber,
            width: "200px",
            placeholder: '请输入${title}',
            defaultValue: '',
            validateRules: [{pattern: "n", message: "${title}格式不正确"}],
          },
        ]
      },
      url: {
        // ****行星万象修改位置戳****
        // add: "/bbs/bbsTopic/add",
        add: "/bbs/bbsTopic/wise/back/add",
        edit: "/bbs/bbsTopic/edit",
        queryById: "/bbs/bbsTopic/queryById",
        bbsTopicImage: {
          list: '/bbs/bbsTopic/queryBbsTopicImageByMainId'
        },
        bbsTopicTag: {
          list: '/bbs/bbsTopic/queryBbsTopicTagByMainId'
        },
        bbsTopicLink: {
          list: '/bbs/bbsTopic/queryBbsTopicLinkByMainId'
        },
      }
    }
  },
  props: {
    //表单禁用
    disabled: {
      type: Boolean,
      default: false,
      required: false
    }
  },
  computed: {
    formDisabled() {
      return this.disabled
    },
  },
  created() {
  },
  methods: {
    addBefore() {
      this.bbsTopicImageTable.dataSource = []
      this.bbsTopicTagTable.dataSource = []
      this.bbsTopicLinkTable.dataSource = []
    },
    getAllTable() {
      let values = this.tableKeys.map(key => getRefPromise(this, key))
      return Promise.all(values)
    },
    /** 调用完edit()方法之后会自动调用此方法 */
    editAfter() {
      this.$nextTick(() => {
      })
      // 加载子表数据
      if (this.model.id) {
        let params = {id: this.model.id}
        this.requestSubTableData(this.url.bbsTopicImage.list, params, this.bbsTopicImageTable)
        this.requestSubTableData(this.url.bbsTopicTag.list, params, this.bbsTopicTagTable)
        this.requestSubTableData(this.url.bbsTopicLink.list, params, this.bbsTopicLinkTable)
      }
    },
    //校验所有一对一子表表单
    validateSubForm(allValues) {
      return new Promise((resolve, reject) => {
        Promise.all([]).then(() => {
          resolve(allValues)
        }).catch(e => {
          if (e.error === VALIDATE_NO_PASSED) {
            // 如果有未通过表单验证的子表，就自动跳转到它所在的tab
            this.activeKey = e.index == null ? this.activeKey : this.refKeys[e.index]
          } else {
            console.error(e)
          }
        })
      })
    },
    /** 整理成formData */
    classifyIntoFormData(allValues) {
      let main = Object.assign(this.model, allValues.formValue)
      return {
        ...main, // 展开
        bbsTopicImageList: allValues.tablesValue[0].values,
        bbsTopicTagList: allValues.tablesValue[1].values,
        bbsTopicLinkList: allValues.tablesValue[2].values,
      }
    },
    validateError(msg) {
      this.$message.error(msg)
    },

  }
}
</script>

<style scoped>
</style>