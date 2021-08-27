<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="Logo" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="logoUrl">
              <j-image-upload isMultiple :number=1 v-model="model.logoUrl" ></j-image-upload>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="标题" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="title">
              <a-input v-model="model.title" placeholder="请输入标题"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="标语" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="tagline">
              <a-textarea v-model="model.tagline" rows="4" placeholder="请输入标语" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="主图" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="masterUrl">
              <j-image-upload isMultiple  v-model="model.masterUrl" ></j-image-upload>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="APPID" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="appId">
              <a-input v-model="model.appId" placeholder="请输入APPID"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="PATH" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="path">
              <a-textarea v-model="model.path" rows="4" placeholder="请输入PATH" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="逻辑删除标志" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="deleteFlag">
              <j-dict-select-tag type="list" v-model="model.deleteFlag" dictCode="bbs_delete_flag" placeholder="请选择逻辑删除标志" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="状态" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="status">
              <j-dict-select-tag type="list" v-model="model.status" dictCode="bbs_status" placeholder="请选择状态" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="序号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sort">
              <a-input-number v-model="model.sort" placeholder="请输入序号" style="width: 100%" />
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </j-form-container>
  </a-spin>
</template>

<script>

  import { httpAction, getAction } from '@api/manage'
  import { validateDuplicateValue } from '@/utils/util'

  export default {
    name: 'BbsWaimaiForm',
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
           sort: [
              { required: true, message: '请输入序号!'},
              { pattern: /^-?\d+$/, message: '请输入整数!'},
           ],
        },
        url: {
          add: "/bbs/bbsWaimai/add",
          edit: "/bbs/bbsWaimai/edit",
          queryById: "/bbs/bbsWaimai/queryById"
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