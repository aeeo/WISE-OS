<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="用户" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="createBy">
              <a-input v-model="model.createBy" placeholder="请输入用户" disabled ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="用户账号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="userAccount">
              <a-input v-model="model.userAccount" placeholder="请输入用户账号" disabled ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="陨石数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="stoneCount">
              <a-input-number v-model="model.stoneCount" placeholder="请输入陨石数" style="width: 100%" disabled/>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="帖子数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="topicCount">
              <a-input-number v-model="model.topicCount" placeholder="请输入帖子数" style="width: 100%" disabled/>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="留言数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="messageCount">
              <a-input-number v-model="model.messageCount" placeholder="请输入留言数" style="width: 100%" disabled/>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="消息数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="informationCount">
              <a-input-number v-model="model.informationCount" placeholder="请输入消息数" style="width: 100%" disabled/>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="收藏数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="starCount">
              <a-input-number v-model="model.starCount" placeholder="请输入收藏数" style="width: 100%" disabled/>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="被收藏数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="beStarCount">
              <a-input-number v-model="model.beStarCount" placeholder="请输入被收藏数" style="width: 100%" disabled/>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="浏览帖子数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="clickTopicCount">
              <a-input-number v-model="model.clickTopicCount" placeholder="请输入浏览帖子数" style="width: 100%" disabled/>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="评论数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="commentCount">
              <a-input-number v-model="model.commentCount" placeholder="请输入评论数" style="width: 100%" disabled/>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="被评论数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="beCommentCount">
              <a-input-number v-model="model.beCommentCount" placeholder="请输入被评论数" style="width: 100%" disabled/>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="点赞数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="praiseCount">
              <a-input-number v-model="model.praiseCount" placeholder="请输入点赞数" style="width: 100%" disabled/>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="被赞数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="bePraiseCount">
              <a-input-number v-model="model.bePraiseCount" placeholder="请输入被赞数" style="width: 100%" disabled/>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="当天发布贴子数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="dayPublishTopic">
              <a-input-number v-model="model.dayPublishTopic" placeholder="请输入当天发布贴子数" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="本周发布贴子数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="weekPublishTopic">
              <a-input-number v-model="model.weekPublishTopic" placeholder="请输入本周发布贴子数" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="当天发布留言数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="dayPublishMessage">
              <a-input-number v-model="model.dayPublishMessage" placeholder="请输入当天发布留言数" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="本周发布留言数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="weekPublishMessage">
              <a-input-number v-model="model.weekPublishMessage" placeholder="请输入本周发布留言数" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="当天点赞次数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="dayPublishPraise">
              <a-input-number v-model="model.dayPublishPraise" placeholder="请输入当天点赞次数" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="本周点赞次数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="weekPublishPraise">
              <a-input-number v-model="model.weekPublishPraise" placeholder="请输入本周点赞次数" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="当天评论次数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="dayPublishReply">
              <a-input-number v-model="model.dayPublishReply" placeholder="请输入当天评论次数" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="本周评论次数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="weekPublishReply">
              <a-input-number v-model="model.weekPublishReply" placeholder="请输入本周评论次数" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="当前区域编码" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="regionCode">
              <a-input v-model="model.regionCode" placeholder="请输入当前区域编码" disabled ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="本月区域切换次数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="regionSwitchCount">
              <a-input-number v-model="model.regionSwitchCount" placeholder="请输入本月区域切换次数" style="width: 100%" />
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
    name: 'BbsUserRecordForm',
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
           stoneCount: [
              { required: false},
              { pattern: /^-?\d+$/, message: '请输入整数!'},
           ],
           topicCount: [
              { required: false},
              { pattern: /^-?\d+$/, message: '请输入整数!'},
           ],
           messageCount: [
              { required: false},
              { pattern: /^-?\d+$/, message: '请输入整数!'},
           ],
           informationCount: [
              { required: false},
              { pattern: /^-?\d+$/, message: '请输入整数!'},
           ],
           starCount: [
              { required: false},
              { pattern: /^-?\d+$/, message: '请输入整数!'},
           ],
           beStarCount: [
              { required: false},
              { pattern: /^-?\d+$/, message: '请输入整数!'},
           ],
           clickTopicCount: [
              { required: false},
              { pattern: /^-?\d+$/, message: '请输入整数!'},
           ],
           commentCount: [
              { required: false},
              { pattern: /^-?\d+$/, message: '请输入整数!'},
           ],
           beCommentCount: [
              { required: false},
              { pattern: /^-?\d+$/, message: '请输入整数!'},
           ],
           praiseCount: [
              { required: false},
              { pattern: /^-?\d+$/, message: '请输入整数!'},
           ],
           bePraiseCount: [
              { required: false},
              { pattern: /^-?\d+$/, message: '请输入整数!'},
           ],
           dayPublishTopic: [
              { required: false},
              { pattern: /^-?\d+$/, message: '请输入整数!'},
           ],
           weekPublishTopic: [
              { required: false},
              { pattern: /^-?\d+$/, message: '请输入整数!'},
           ],
           dayPublishMessage: [
              { required: false},
              { pattern: /^-?\d+$/, message: '请输入整数!'},
           ],
           weekPublishMessage: [
              { required: false},
              { pattern: /^-?\d+$/, message: '请输入整数!'},
           ],
           dayPublishPraise: [
              { required: false},
              { pattern: /^-?\d+$/, message: '请输入整数!'},
           ],
           weekPublishPraise: [
              { required: false},
              { pattern: /^-?\d+$/, message: '请输入整数!'},
           ],
           dayPublishReply: [
              { required: false},
              { pattern: /^-?\d+$/, message: '请输入整数!'},
           ],
           weekPublishReply: [
              { required: false},
              { pattern: /^-?\d+$/, message: '请输入整数!'},
           ],
           regionSwitchCount: [
              { required: false},
              { pattern: /^-?\d+$/, message: '请输入整数!'},
           ],
        },
        url: {
          add: "/bbs/bbsUserRecord/add",
          edit: "/bbs/bbsUserRecord/edit",
          queryById: "/bbs/bbsUserRecord/queryById"
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