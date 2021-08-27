<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <!-- 主表单区域 -->
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24" >
            <a-form-model-item label="标题" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="title">
              <a-input v-model="model.title" placeholder="请输入标题" disabled></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="推送类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="type">
              <j-dict-select-tag type="list" v-model="model.type" dictCode="bbs_sys_message_type" placeholder="请选择推送类型" disabled/>
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="区域编码" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="regionCode">
              <a-input v-model="model.regionCode" placeholder="请输入区域编码" disabled></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="封面" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="imageUrl">
              <j-image-upload isMultiple  v-model="model.imageUrl" disabled></j-image-upload>
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="贴子ID" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="topicId">
              <a-input v-model="model.topicId" placeholder="请输入贴子ID" disabled></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="消息排序" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="messageSort">
              <a-input-number v-model="model.messageSort" placeholder="请输入消息排序" style="width: 100%" disabled/>
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="推送状态" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="status">
              <j-dict-select-tag type="list" v-model="model.status" dictCode="bbs_sys_message_status" placeholder="请选择推送状态" disabled/>
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </j-form-container>
      <!-- 子表单区域 -->
    <a-tabs v-model="activeKey" @change="handleChangeTabs">
      <a-tab-pane tab="用户系统消息" :key="refKeys[0]" :forceRender="true">
        <j-editable-table
          :ref="refKeys[0]"
          :loading="bbsUserSysMessageTable.loading"
          :columns="bbsUserSysMessageTable.columns"
          :dataSource="bbsUserSysMessageTable.dataSource"
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

  import { getAction } from '@/api/manage'
  import { FormTypes,getRefPromise,VALIDATE_NO_PASSED } from '@/utils/JEditableTableUtil'
  import { JEditableTableModelMixin } from '@/mixins/JEditableTableModelMixin'
  import { validateDuplicateValue } from '@/utils/util'

  export default {
    name: 'BbsSysMessageForm',
    mixins: [JEditableTableModelMixin],
    components: {
    },
    data() {
      return {
        labelCol: {
          xs: { span: 24 },
          sm: { span: 6 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        labelCol2: {
          xs: { span: 24 },
          sm: { span: 3 },
        },
        wrapperCol2: {
          xs: { span: 24 },
          sm: { span: 20 },
        },
        model:{
        },
        // 新增时子表默认添加几行空数据
        addDefaultRowNum: 1,
        validatorRules: {
           title: [
              { required: true, message: '请输入标题!'},
           ],
           regionCode: [
              { required: true, message: '请输入区域编码!'},
           ],
           topicId: [
              { required: true, message: '请输入贴子ID!'},
           ],
           messageSort: [
              { required: true, message: '请输入消息排序!'},
           ],
        },
        refKeys: ['bbsUserSysMessage', ],
        tableKeys:['bbsUserSysMessage', ],
        activeKey: 'bbsUserSysMessage',
        // 用户系统消息
        bbsUserSysMessageTable: {
          loading: false,
          dataSource: [],
          columns: [
            {
              title: '创建人',
              key: 'createBy',
              type: FormTypes.input,
              disabled:true,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '阅读状态',
              key: 'status',
              type: FormTypes.select,
              dictCode:"bbs_sys_message_read_status",
              disabled:true,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '区域编码',
              key: 'regionCode',
              type: FormTypes.input,
              disabled:true,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '系统消息ID',
              key: 'sysMessageId',
              type: FormTypes.input,
              disabled:true,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '消息类型',
              key: 'type',
              type: FormTypes.select,
              dictCode:"bbs_sys_message_type",
              disabled:true,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '接收用户名',
              key: 'receiveUsername',
              type: FormTypes.input,
              disabled:true,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
          ]
        },
        url: {
          add: "/bbs/bbsSysMessage/add",
          edit: "/bbs/bbsSysMessage/edit",
          queryById: "/bbs/bbsSysMessage/queryById",
          bbsUserSysMessage: {
            list: '/bbs/bbsSysMessage/queryBbsUserSysMessageByMainId'
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
      formDisabled(){
        return this.disabled
      },
    },
    created () {
    },
    methods: {
      addBefore(){
        this.bbsUserSysMessageTable.dataSource=[]
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
          let params = { id: this.model.id }
          this.requestSubTableData(this.url.bbsUserSysMessage.list, params, this.bbsUserSysMessageTable)
        }
      },
      //校验所有一对一子表表单
      validateSubForm(allValues){
          return new Promise((resolve,reject)=>{
            Promise.all([
            ]).then(() => {
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
          bbsUserSysMessageList: allValues.tablesValue[0].values,
        }
      },
      validateError(msg){
        this.$message.error(msg)
      },

    }
  }
</script>

<style scoped>
</style>