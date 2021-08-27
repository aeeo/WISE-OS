<template>
  <j-modal
    :title="title"
    :width="width"
    :visible="visible"
    :confirmLoading="confirmLoading"
    switchFullscreen
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭">
    <a-spin :spinning="confirmLoading">
      <a-form-model ref="form" :model="model" :rules="validatorRules">
        <a-form-model-item label="帖子ID" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="topicId">
          <a-input v-model="model.topicId" placeholder="请输入帖子ID" disabled></a-input>
        </a-form-model-item>
        <a-form-model-item label="评论内容" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="content">
          <a-input v-model="model.content" placeholder="请输入评论内容" disabled></a-input>
        </a-form-model-item>
        <a-form-model-item label="用户" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="createBy">
          <a-input v-model="model.createBy" placeholder="请输入用户" disabled></a-input>
        </a-form-model-item>
        <a-form-model-item label="点赞数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="praise">
          <a-input-number v-model="model.praise" placeholder="请输入点赞数" style="width: 100%" disabled/>
        </a-form-model-item>
        <a-form-model-item label="回复数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="replyCount">
          <a-input-number v-model="model.replyCount" placeholder="请输入回复数" style="width: 100%" disabled/>
        </a-form-model-item>
        <a-form-model-item label="创建日期" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="createTime">
          <j-date placeholder="请选择创建日期" v-model="model.createTime" :show-time="true" date-format="YYYY-MM-DD HH:mm:ss" style="width: 100%" disabled/>
        </a-form-model-item>
        <a-form-model-item label="举报次数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="informCount">
          <a-input-number v-model="model.informCount" placeholder="请输入举报次数" style="width: 100%" disabled/>
        </a-form-model-item>
        <a-form-model-item label="状态" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="status">
          <j-dict-select-tag type="list" v-model="model.status"  dictCode="bbs_status" placeholder="请选择状态" />
        </a-form-model-item>
        <a-form-model-item label="逻辑删除" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="deleteFlag">
          <j-dict-select-tag type="list" v-model="model.deleteFlag"  dictCode="bbs_delete_flag" placeholder="请选择逻辑删除" />
        </a-form-model-item>
        
      </a-form-model>
    </a-spin>
  </j-modal>
</template>

<script>

  import { httpAction } from '@/api/manage'
  import { validateDuplicateValue } from '@/utils/util'
  export default {
    name: "BbsReplyModal",
    components: { 
    },
    data () {
      return {
        title:"操作",
        width:800,
        visible: false,
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
          add: "/bbs/bbsReply/add",
          edit: "/bbs/bbsReply/edit",
        },
        expandedRowKeys:[],
        pidField:""
     
      }
    },
    created () {
       //备份model原始值
       this.modelDefault = JSON.parse(JSON.stringify(this.model));
    },
    methods: {
      add (obj) {
        this.modelDefault=''
        this.edit(Object.assign(this.modelDefault , obj));
      },
      edit (record) {
        this.model = Object.assign({}, record);
        this.visible = true;
      },
      close () {
        this.$emit('close');
        this.visible = false;
        this.$refs.form.clearValidate()
      },
      handleOk () {
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
             if(this.model.id && this.model.id === this.model[this.pidField]){
              that.$message.warning("父级节点不能选择自己");
              that.confirmLoading = false;
              return;
            }
            httpAction(httpurl,this.model,method).then((res)=>{
              if(res.success){
                that.$message.success(res.message);
                this.$emit('ok');
              }else{
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
              that.close();
            })
          }else{
             return false
          }
        })
      },
      handleCancel () {
        this.close()
      },
      submitSuccess(formData,flag){
        if(!formData.id){
          let treeData = this.$refs.treeSelect.getCurrTreeData()
          this.expandedRowKeys=[]
          this.getExpandKeysByPid(formData[this.pidField],treeData,treeData)
          this.$emit('ok',formData,this.expandedRowKeys.reverse());
        }else{
          this.$emit('ok',formData,flag);
        }
      },
      getExpandKeysByPid(pid,arr,all){
        if(pid && arr && arr.length>0){
          for(let i=0;i<arr.length;i++){
            if(arr[i].key==pid){
              this.expandedRowKeys.push(arr[i].key)
              this.getExpandKeysByPid(arr[i]['parentId'],all,all)
            }else{
              this.getExpandKeysByPid(pid,arr[i].children,all)
            }
          }
        }
      }
      
      
    }
  }
</script>