<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <!-- 主表单区域 -->
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="区域编码" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="regionCode">
              <a-input v-model="model.regionCode" placeholder="请输入区域编码"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="全称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="fullName">
              <a-input v-model="model.fullName" placeholder="请输入全称"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="简称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="name">
              <a-input v-model="model.name" placeholder="请输入简称"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="经度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="longitude">
              <a-input v-model="model.longitude" placeholder="请输入经度"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="纬度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="latitude">
              <a-input v-model="model.latitude" placeholder="请输入纬度"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="地图展示名" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="content">
              <a-input v-model="model.content" placeholder="请输入地图展示名"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="省份" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="province">
              <a-input v-model="model.province" placeholder="请输入省份"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="每日发布贴子数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="dayPublishTopic">
              <a-input-number v-model="model.dayPublishTopic" placeholder="请输入每日发布贴子数" style="width: 100%" disabled/>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="每周发布贴子数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="weekPublishTopic">
              <a-input-number v-model="model.weekPublishTopic" placeholder="请输入每周发布贴子数" style="width: 100%" disabled/>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="每日发布留言数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="dayPublishMessage">
              <a-input-number v-model="model.dayPublishMessage" placeholder="请输入每日发布留言数" style="width: 100%" disabled/>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="每周发布留言数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="weekPublishMessage">
              <a-input-number v-model="model.weekPublishMessage" placeholder="请输入每周发布留言数" style="width: 100%" disabled/>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="每日评论数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="dayPublishReply">
              <a-input-number v-model="model.dayPublishReply" placeholder="请输入每日评论数" style="width: 100%" disabled/>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="每周评论数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="weekPublishReply">
              <a-input-number v-model="model.weekPublishReply" placeholder="请输入每周评论数" style="width: 100%" disabled/>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="每日点赞数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="dayPublishPraise">
              <a-input-number v-model="model.dayPublishPraise" placeholder="请输入每日点赞数" style="width: 100%" disabled/>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="每周点赞数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="weekPublishPraise">
              <a-input-number v-model="model.weekPublishPraise" placeholder="请输入每周点赞数" style="width: 100%" disabled/>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="区域类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="regionType">
              <j-dict-select-tag type="list" v-model="model.regionType" dictCode="bbs_region_type"
                                 placeholder="请选择区域类型"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="缩放等级" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="scale">
              <a-input-number v-model="model.scale" placeholder="请输入缩放等级" style="width: 100%"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="范围半径" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="radius">
              <a-input-number v-model="model.radius" placeholder="请输入范围半径" style="width: 100%"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="区域等级" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="regionGrade">
              <a-input-number v-model="model.regionGrade" placeholder="请输入区域等级" style="width: 100%"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="公众号二维码" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="officeAccountImage">
              <j-image-upload isMultiple :number=1 v-model="model.officeAccountImage"></j-image-upload>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="区域头像" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="regionImage">
              <j-image-upload isMultiple :number=1 v-model="model.regionImage"></j-image-upload>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="收款码" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="payeeImage">
              <j-image-upload isMultiple :number=1 v-model="model.payeeImage"></j-image-upload>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="朋友圈二维码" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="circleFriendsImage">
              <j-image-upload isMultiple :number=1 v-model="model.circleFriendsImage"></j-image-upload>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="状态" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="regionStatus">
              <j-dict-select-tag type="list" v-model="model.regionStatus" dictCode="bbs_region_region_status"
                                 placeholder="请选择状态"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="区域关联部门ID" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="regionDepartId">
              <a-input v-model="model.regionDepartId" placeholder="请输入区域关联部门ID"></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </j-form-container>
    <!-- 子表单区域 -->
    <a-tabs v-model="activeKey" @change="handleChangeTabs">
      <a-tab-pane tab="版块" :key="refKeys[0]" :forceRender="true">
        <j-editable-table
          :ref="refKeys[0]"
          :loading="bbsClassTable.loading"
          :columns="bbsClassTable.columns"
          :dataSource="bbsClassTable.dataSource"
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
import {validateDuplicateValue} from '@/utils/util'

export default {
  name: 'BbsRegionForm',
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
      model: {
        province: "陕西省-",
        dayPublishTopic: 2,
        weekPublishTopic: 5,
        dayPublishMessage: 5,
        weekPublishMessage: 20,
        dayPublishReply: 20,
        weekPublishReply: 50,
        dayPublishPraise: 100,
        weekPublishPraise: 500,
        scale: 13,
        radius: 1000,
        regionGrade: 5,
      },
      // 新增时子表默认添加几行空数据
      addDefaultRowNum: 1,
      validatorRules: {
        regionCode: [
          {required: true, message: '请输入区域编码!'},
          {validator: (rule, value, callback) => validateDuplicateValue('bbs_region', 'region_code', value, this.model.id, callback)},
        ],
        fullName: [
          {required: true, message: '请输入全称!'},
        ],
        name: [
          {required: true, message: '请输入简称!'},
        ],
        longitude: [
          {required: true, message: '请输入经度!'},
        ],
        latitude: [
          {required: true, message: '请输入纬度!'},
        ],
        content: [
          {required: true, message: '请输入地图展示名!'},
        ],
        regionType: [
          {required: true, message: '请输入区域类型!'},
        ],
        scale: [
          {required: true, message: '请输入缩放等级!'},
        ],
        radius: [
          {required: true, message: '请输入范围半径!'},
        ],
      },
      refKeys: ['bbsClass',],
      tableKeys: ['bbsClass',],
      activeKey: 'bbsClass',
      // 版块
      bbsClassTable: {
        loading: false,
        dataSource: [],
        columns: [
          {
            title: '版块编码(不允许修改)',
            key: 'classCode',
            type: FormTypes.input,
            width: "200px",
            placeholder: '请输入${title}',
            defaultValue: '',
            validateRules: [{required: true, message: '${title}不能为空'}],
          },
          {
            title: '版块名称',
            key: 'className',
            type: FormTypes.input,
            width: "200px",
            placeholder: '请输入${title}',
            defaultValue: '',
            validateRules: [{required: true, message: '${title}不能为空'}],
          },
          {
            title: '版块排序',
            key: 'classSort',
            type: FormTypes.inputNumber,
            width: "200px",
            placeholder: '请输入${title}',
            defaultValue: '',
            validateRules: [{required: true, message: '${title}不能为空'}],
          },
          {
            title: '版块贴子数量',
            key: 'classTopicCount',
            type: FormTypes.inputNumber,
            disabled: true,
            width: "200px",
            placeholder: '请输入${title}',
            defaultValue: '',
          },
        ]
      },
      url: {
        // ****行星万象修改位置戳****
        // add: "/bbs/bbsRegion/add",
        add: "/bbs/bbsRegion/wise/back/add",
        edit: "/bbs/bbsRegion/edit",
        queryById: "/bbs/bbsRegion/queryById",
        bbsClass: {
          list: '/bbs/bbsRegion/queryBbsClassByMainId'
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
      this.bbsClassTable.dataSource = []
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
        this.requestSubTableData(this.url.bbsClass.list, params, this.bbsClassTable)
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
        bbsClassList: allValues.tablesValue[0].values,
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