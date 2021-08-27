@[TOC](目录)
# 简介
一个信息发布类型的微信小程序，可以在线发布需求，例如寻人、寻物、打听。表白等，废话不说，扫码查看小程序演示：
![请添加图片描述](https://img-blog.csdnimg.cn/2c761508b0db4afa9d2616e3f4b43886.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L25iY3Nkbg==,size_16,color_FFFFFF,t_70 =200x200)

行星万象后台管理系统，第一次加载会比较慢，正式地址，不提供体验账号密码：

> [行星万象后台管理系统](https://www.vtep.cn)

很遗憾虽然行星万象的后台没有办法让你体验，但是JeecgBoot提供了一个体验地址，大体一样可以借鉴：

> [JeecgBoot演示](http://boot.jeecg.com/user/login)
> 账户/密码：jeecg/123456

在此说一下，截止2021年8月25日，行星万象项目陆陆续续开发了近1年时间，到现在为止基本功能已经开发完毕，有时间的话把即时聊天功能集成进去，本项目我会一直维护下去，只是一个人精力有限，进展可能会比较慢，但是放心不会烂尾，为了让新人快速上手，我会在开发文档这边花比较多的精力，做到傻瓜式步骤，保证拿到代码先能跑起来。

本项目所有的代码都可以拿去直接独立部署，免费使用，但是我希望能注明来源：

> [PowerBy 陕西行星环绕信息科技有限公司](https://gitee.com/zhaoqizhi/WISE-Fork)

如果只是需要使用线上系统可以联系我，我帮你开通区域，但是硬件和资源成本还是希望能帮我分担一下，我不凭借这个系统赚钱，但是也不想成为我的经济负担。

最后，如果你仅准备用一两天时间来摸透这个系统，建议还是立刻关闭这个标签页，如果你有了心理准备，那就可以往下看了。
## 需要具备的知识
1. 微信小程序基本常识，例如怎样新建一个空白小程序。
2. 对JeecgBoot等快速开发平台有一定了解，因为本平台后台基于JeecgBoot搭建，在项目使用过程中大部分问题都可以在[**JeecgBoot开发文档**](http://doc.jeecg.com/2043872)中找到答案，各位也可以去给点个star，吃水不忘挖井人。
## 基础开发环境
1. JDK: 1.8 Maven: 3.5+
2. MySql: 5.7+
3. Redis: 3.2 +
4. Node Js: 10.0 +
5. Npm: 5.6.0+
6. Yarn: 1.21.1+

# 超级起步
## 准备工作
1. 下载源码，别忘记Star支持一下。

> [https://gitee.com/zhaoqizhi/WISE-Fork](https://gitee.com/zhaoqizhi/WISE-Fork)

2. 打开项目

> 微信开发者工具打开：
> Idea打开：

3. 初始化数据库

初始化数据库，要求mysql5.7+，手工执行Sql脚本： jeecg-boot/db/jeecgboot-mysql-5.7.sql会自动建库和初始化数据。

4. 修改项目配置文件（数据库配置、redis配置）

>  配置文件： jeecg-boot-module-system/src/main/resources/application-dev.yml


- 数据库配置(连接和账号密码)
  ![请添加图片描述](https://img-blog.csdnimg.cn/b342324f9ae84fddb3814767def0ab95.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L25iY3Nkbg==,size_16,color_FFFFFF,t_70)

- Redis配置（配置redis的host和port）
  ![请添加图片描述](https://img-blog.csdnimg.cn/550900b35263493fac4acdf44552e569.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L25iY3Nkbg==,size_16,color_FFFFFF,t_70)
- 微信小程序配置文件修改

![在这里插入图片描述](https://img-blog.csdnimg.cn/369c19d71dd8423da1b19686cd29add1.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L25iY3Nkbg==,size_16,color_FFFFFF,t_70)

![在这里插入图片描述](https://img-blog.csdnimg.cn/55dcf5a1330642d3b7ed76b0bc117a3c.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L25iY3Nkbg==,size_16,color_FFFFFF,t_70)

## 启动项目
### 项目结构

> ├─jeecg-boot-parent（父POM： 项目依赖、modules组织）
> │  ├─jeecg-boot-base（共通模块： 工具类、config、权限、查询过滤器、注解、接口等）
> │  ├─jeecg-boot-module-demo    示例代码
> │  ├─jeecg-boot-module-system （系统管理权限等功能） -- 默认作为启动项目  
> │  ├─jeecg-boot-starter（微服务starter模块，不需要微服务可以删掉）  │
> │  ├─jeecg-cloud-module（微服务生态模块，不需要微服务可以删掉）

### 后端服务启动
确认好配置修改完成后可直接启动

> jeecg-boot-module-system/src/main/java/org/jeecg/JeecgSystemApplication.java

右键执行启动，通过 http://localhost:8080/jeecg-boot/doc.html 访问后台，默认进入swagger文档首页
### 管理系统启动
在ant-design-jeecg-vue目录下执行命令下载依赖，执行命令

> yarn install

找到项目目录下文件package.json文件，鼠标右键选择Show npm Scripts，点击命令 serve 启动项目，看到如下日志则启动成功。
![请添加图片描述](https://img-blog.csdnimg.cn/ea3e9854e55a4b49b8ecdd4a0bb86df2.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L25iY3Nkbg==,size_16,color_FFFFFF,t_70)
点击链接自动打开浏览器，默认账号密码： admin/123456。
# 你能得到什么
这里我不谈意义，只谈得失，熟悉此项目你会获得以下好处。

##  项目
如果你是一名在校生，你可以拿着这套代码去部署、去创业、去参加大创、挑战杯各类比赛申请经费都没有问题，甚至毕业面试也是一份不错的履历。
## 金钱
微信小程序集成外卖优惠券模块和微信小商店，开发者自行替换成自己的Url，用户使用即可实现盈利。
## 技术
从我的角度这是一个对学生非常好的练手项目，因为在开始做这个项目的时候我还是一名西安文理学院的在校生，从架构从逻辑都是我一步步走过来的，用这个项目练手一是有我做的基础在不会无聊，二是各方面技术都有涉猎，三是系统可上线。
# 常见问题
一般问题都可以在[**微信官网文档**](https://developers.weixin.qq.com/miniprogram/dev/framework/)和[**JeecgBoot开发文档**](http://doc.jeecg.com/2043872)中找到答案，如果无法解决或无从下手可以进QQ群：702655687尝试询问。
