<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="创建日期" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="createTime">
              <j-date placeholder="请选择创建日期"  v-model="model.createTime" :show-time="true" date-format="YYYY-MM-DD HH:mm:ss" style="width: 100%" disabled/>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="举报类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="type">
              <j-dict-select-tag type="list" v-model="model.type" dictCode="bbs_inform_type" placeholder="请选择举报类型" disabled/>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="举报用户名" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="informUsername">
              <a-input v-model="model.informUsername" placeholder="请输入举报用户名" disabled ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="被举报用户名" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="beInformUsername">
              <a-input v-model="model.beInformUsername" placeholder="请输入被举报用户名" disabled ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="处理结果" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="resultType">
              <j-dict-select-tag type="list" v-model="model.resultType" dictCode="bbs_inform_result_type" placeholder="请选择处理结果" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="处理意见" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="resultSuggestion">
              <a-input v-model="model.resultSuggestion" placeholder="请输入处理意见"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="贴子ID" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="topicId">
              <a-input v-model="model.topicId" placeholder="请输入贴子ID" disabled ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="评论ID" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="replyId">
              <a-input v-model="model.replyId" placeholder="请输入评论ID" disabled ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="留言ID" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="messageId">
              <a-input v-model="model.messageId" placeholder="请输入留言ID" disabled ></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </j-form-container>
  </a-spin>
</template>

<script>

  import { httpAction, getAction } from '@/api/manage'
  import { validateDuplicateValue } from '@/utils/util'

  export default {
    name: 'BbsInformForm',
    components: {
    },
    props: {
      //表单禁用
      disabled: {
        type: Boolean,
        default: false,
        required: false
      }
    },
    data () {
      return {
        model:{
         },
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        confirmLoading: false,
        validatorRules: {
           type: [
              { required: true, message: '请输入举报类型!'},
           ],
           informUsername: [
              { required: true, message: '请输入举报用户名!'},
           ],
           beInformUsername: [
              { required: true, message: '请输入被举报用户名!'},
           ],
        },
        url: {
          add: "/bbs/bbsInform/add",
          edit: "/bbs/bbsInform/edit",
          queryById: "/bbs/bbsInform/queryById"
        }
      }
    },
    computed: {
      formDisabled(){
        return this.disabled
      },
    },
    created () {
       //备份model原始值
      this.modelDefault = JSON.parse(JSON.stringify(this.model));
    },
    methods: {
      add () {
        this.edit(this.modelDefault);
      },
      edit (record) {
        this.model = Object.assign({}, record);
        this.visible = true;
      },
      submitForm () {
        const that = this;
        // 触发表单验证
        this.$refs.form.validate(valid => {
          if (valid) {
            that.confirmLoading = true;
            let httpurl = '';
            let method = '';
            if(!this.model.id){
              httpurl+=this.url.add;
              method = 'post';
            }else{
              httpurl+=this.url.edit;
               method = 'put';
            }
            httpAction(httpurl,this.model,method).then((res)=>{
              if(res.success){
                that.$message.success(res.message);
                that.$emit('ok');
              }else{
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
            })
          }
         
        })
      },
    }
  }
</script>