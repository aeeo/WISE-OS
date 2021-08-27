<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="活动标题" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="title">
              <a-input v-model="model.title" placeholder="请输入活动标题"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="封面" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="imageUrl">
              <j-image-upload isMultiple v-model="model.imageUrl"></j-image-upload>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="活动内容" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="content">
              <j-editor v-model="model.content"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="开始时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="startTime">
              <j-date placeholder="请选择开始时间" v-model="model.startTime" :show-time="true"
                      date-format="YYYY-MM-DD HH:mm:ss" style="width: 100%"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="截止时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="endTime">
              <j-date placeholder="请选择截止时间" v-model="model.endTime" :show-time="true" date-format="YYYY-MM-DD HH:mm:ss"
                      style="width: 100%"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="需要人数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="needPeopleCount">
              <a-input-number v-model="model.needPeopleCount" placeholder="请输入需要人数" style="width: 100%"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="活动现状" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="activitySituation">
              <a-input v-model="model.activitySituation" placeholder="请输入活动现状"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="负责人" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="princiaplPeopleName">
              <a-input v-model="model.princiaplPeopleName" placeholder="请输入负责人"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="手机" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="phoneNumber">
              <a-input v-model="model.phoneNumber" placeholder="请输入手机"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="微信" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="wechat">
              <a-input v-model="model.wechat" placeholder="请输入微信"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="访问总数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="hitsCount">
              <a-input-number v-model="model.hitsCount" placeholder="请输入访问总数" style="width: 100%" disabled/>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="省市区" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="provinces">
              <j-area-linkage type="cascader" v-model="model.provinces" placeholder="请输入省市区"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="地点" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="site">
              <a-input v-model="model.site" placeholder="请输入地点"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="活动类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="activityType">
              <j-dict-select-tag type="list" v-model="model.activityType" dictCode="bbs_activity_type"
                                 placeholder="请选择活动类型"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="推送类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pushType">
              <j-dict-select-tag type="list" v-model="model.pushType" dictCode="bbs_activity_push_type"
                                 placeholder="请选择推送类型"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="区域编码" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="regionCode">
              <a-input v-model="model.regionCode" placeholder="请输入区域编码"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="跳转链接" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="skipUrl">
              <a-input v-model="model.skipUrl" placeholder="请输入跳转链接"></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </j-form-container>
  </a-spin>
</template>

<script>

import {httpAction} from '@/api/manage'

export default {
  name: 'BbsActivityForm',
  components: {},
  props: {
    //表单禁用
    disabled: {
      type: Boolean,
      default: false,
      required: false
    }
  },
  data() {
    return {
      model: {},
      labelCol: {
        xs: {span: 24},
        sm: {span: 5},
      },
      wrapperCol: {
        xs: {span: 24},
        sm: {span: 16},
      },
      confirmLoading: false,
      validatorRules: {
        phoneNumber: [
          {required: false},
          {pattern: /^1[3456789]\d{9}$/, message: '请输入正确的手机号码!'},
        ],
      },
      url: {
        add: "/bbs/bbsActivity/add",
        edit: "/bbs/bbsActivity/edit",
        queryById: "/bbs/bbsActivity/queryById"
      }
    }
  },
  computed: {
    formDisabled() {
      return this.disabled
    },
  },
  created() {
    //备份model原始值
    this.modelDefault = JSON.parse(JSON.stringify(this.model));
  },
  methods: {
    add() {
      this.edit(this.modelDefault);
    },
    edit(record) {
      this.model = Object.assign({}, record);
      this.visible = true;
    },
    submitForm() {
      const that = this;
      // 触发表单验证
      this.$refs.form.validate(valid => {
        if (valid) {
          that.confirmLoading = true;
          let httpurl = '';
          let method = '';
          if (!this.model.id) {
            httpurl += this.url.add;
            method = 'post';
          } else {
            httpurl += this.url.edit;
            method = 'put';
          }
          httpAction(httpurl, this.model, method).then((res) => {
            if (res.success) {
              that.$message.success(res.message);
              that.$emit('ok');
            } else {
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