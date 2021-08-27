<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="用户" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="createBy">
              <a-input v-model="model.createBy" placeholder="请输入用户" disabled ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="反馈内容" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="content">
              <a-textarea v-model="model.content" rows="4" placeholder="请输入反馈内容" disabled/>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="处理进度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="disposeStatus">
              <j-dict-select-tag type="list" v-model="model.disposeStatus" dictCode="bbs_feedback_status" placeholder="请选择处理进度" />
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="处理结果" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="disposeResult">
              <a-input v-model="model.disposeResult" placeholder="请输入处理结果"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="处理人" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="disposeUsername">
              <a-input v-model="model.disposeUsername" placeholder="请输入处理人" disabled ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="处理历史" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="disposeHistory">
              <a-input v-model="model.disposeHistory" placeholder="请输入处理历史" disabled ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="联系方式" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="userContact">
              <a-input v-model="model.userContact" placeholder="请输入联系方式" disabled ></a-input>
            </a-form-model-item>
          </a-col>

          <a-col :span="12">
            <a-form-model-item label="处理时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="disposeDate">
              <j-date placeholder="请选择处理时间"  v-model="model.disposeDate" :show-time="true" date-format="YYYY-MM-DD HH:mm:ss" style="width: 100%" disabled/>
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
    name: 'BbsFeedBackForm',
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
        },
        url: {
          add: "/bbs/bbsFeedBack/add",
          edit: "/bbs/bbsFeedBack/edit",
          queryById: "/bbs/bbsFeedBack/queryById"
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