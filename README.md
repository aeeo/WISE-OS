# 简介
一个信息发布类型的微信小程序，可以在线发布需求，例如寻人、寻物、打听，表白等。可改造表白墙、校园信息发布、帮打听、城市交流圈、大型朋友圈，废话不说，扫码查看小程序演示：
![请添加图片描述](https://img-blog.csdnimg.cn/2c761508b0db4afa9d2616e3f4b43886.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L25iY3Nkbg==,size_16,color_FFFFFF,t_70)
好项目应该被顶起来，可以的话给个Star鼓励下：

> Gitee：
> > [Gitee：https://gitee.com/vtep/WISE-OS.git](https://gitee.com/vtep/WISE-OS.git)
>
> Github：
>
> > [https://github.com/geneedyou/WISE-OS.git](https://github.com/geneedyou/WISE-OS.git)

行星万象后台管理系统，第一次加载会比较慢，正式地址，不提供体验账号密码：

> [行星万象后台管理系统](https://www.vtep.cn)

![在这里插入图片描述](https://img-blog.csdnimg.cn/b6074f3633774fa5b0505d187e032072.png?x-oss-process=image/watermark,type_ZHJvaWRzYW5zZmFsbGJhY2s,shadow_50,text_Q1NETiBA5LiJ5bKB5pe26LaF5biF5ZOm,size_20,color_FFFFFF,t_70,g_se,x_16)

很遗憾虽然行星万象的后台没有办法让你体验，但是JeecgBoot提供了一个体验地址，大体一样可以借鉴：

> [JeecgBoot演示](http://boot.jeecg.com/user/login)
> 账户/密码：jeecg/123456

在此说一下，截止2021年8月25日，行星万象项目陆陆续续开发了近1年时间，到现在为止基本功能已经开发完毕，有时间的话把即时聊天功能集成进去，本项目我会一直维护下去，只是一个人精力有限，进展可能会比较慢，但是放心不会烂尾，为了让新人快速上手，我会在开发文档这边花比较多的精力，做到傻瓜式步骤，保证拿到代码先能跑起来。

本项目所有的代码都可以拿去修改部署，免费使用，但是你必须要注明：

> [PowerBy Https://gitee.com/vtep/WISE-OS](https://gitee.com/vtep/WISE-OS.git)

如果只是需要使用线上系统可以联系我，我帮你开通区域，但是硬件和资源成本还是希望能分担一下，我不凭借这个系统赚钱，但是也不想成为我的经济负担，具体联系：18700871300（同微信）。

最后，如果你没有任何基础又或者仅准备用一两天时间来摸透这个系统，建议还是立刻关闭这个标签页，当你有了心理准备，那就可以往下看了。
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
## 环境搭建
环境搭建参考JeecgBoot官方文档：

> [http://doc.jeecg.com/2043873](http://doc.jeecg.com/2043873)

## 准备工作
请先确保浏览过[JeecgBoot](http://doc.jeecg.com/2043872)。

1. 下载源码，开源不易，务必Star。

> Gitee：
> > [Gitee：https://gitee.com/vtep/WISE-OS.git](https://gitee.com/vtep/WISE-OS.git)
>
> Github：
>
> > [https://github.com/geneedyou/WISE-OS.git](https://github.com/geneedyou/WISE-OS.git)

2. 初始化数据库

初始化数据库，要求mysql5.7+，新建数据库wised，手工执行Sql脚本初始化数据。
![在这里插入图片描述](https://img-blog.csdnimg.cn/0c33d1ce33e74e928262a4cce8fc4d9b.png?x-oss-process=image/watermark,type_ZHJvaWRzYW5zZmFsbGJhY2s,shadow_50,text_Q1NETiBA5LiJ5bKB5pe26LaF5biF5ZOm,size_14,color_FFFFFF,t_70,g_se,x_16)

 

> WISE-OS\数据库脚本（在mysql中执行即可）\wised.sql

3. 项目结构
> WISE-Mini（小程序）
> WISE-Fork（后台管理系统和后台服务）
>

> ant-design-vue-jeecg（后台管理系统）
> jeecg-boot-parent（父POM： 项目依赖、modules组织） 
> │  ├─jeecg-boot-base（共通模块： 工具类、config、权限、查询过滤器、注解、接口等） 
> │  ├─jeecg-boot-module-bbs    微信小程序代码包
> │  ├─jeecg-boot-module-demo    示例代码
> │  ├─jeecg-boot-module-system （系统管理权限等功能） -- 默认作为启动项目  
> │  ├─jeecg-boot-starter（微服务starter模块，不需要微服务可以删掉）
> │  ├─jeecg-cloud-module（微服务生态模块，不需要微服务可以删掉）

3. 打开项目

> 微信开发者工具打开：WISE-OS\WISE-Mini
> 后台服务：WISE-OS\WISE-Fork\jeecg-boot
> 后台管理系统：WISE-OS\WISE-Fork\ant-design-vue-jeecg

4. 修改项目配置文件（数据库配置、redis配置）

>  配置文件（本地）： jeecg-boot-module-system/src/main/resources/application-dev.yml
>  配置文件（线上）： jeecg-boot-module-system/src/main/resources/application-dev.yml


 - 数据库配置(连接和账号密码) 

![在这里插入图片描述](https://img-blog.csdnimg.cn/207ef38d834d482aaecd2d4855982e4e.png?x-oss-process=image/watermark,type_ZHJvaWRzYW5zZmFsbGJhY2s,shadow_50,text_Q1NETiBA5LiJ5bKB5pe26LaF5biF5ZOm,size_20,color_FFFFFF,t_70,g_se,x_16)
  - Redis配置（配置redis的host和port）

  ![在这里插入图片描述](https://img-blog.csdnimg.cn/b4ac8f3defd84d2190ac2c45e857ea56.png?x-oss-process=image/watermark,type_ZHJvaWRzYW5zZmFsbGJhY2s,shadow_50,text_Q1NETiBA5LiJ5bKB5pe26LaF5biF5ZOm,size_20,color_FFFFFF,t_70,g_se,x_16)


  - 七牛OSS配置

![在这里插入图片描述](https://img-blog.csdnimg.cn/f521c12d8f8a48c2aa15d9528c2c0d16.png?x-oss-process=image/watermark,type_ZHJvaWRzYW5zZmFsbGJhY2s,shadow_50,text_Q1NETiBA5LiJ5bKB5pe26LaF5biF5ZOm,size_20,color_FFFFFF,t_70,g_se,x_16)
- 小程序配置

![在这里插入图片描述](https://img-blog.csdnimg.cn/9604c293c8184764a760980fc613da97.png?x-oss-process=image/watermark,type_ZHJvaWRzYW5zZmFsbGJhY2s,shadow_50,text_Q1NETiBA5LiJ5bKB5pe26LaF5biF5ZOm,size_20,color_FFFFFF,t_70,g_se,x_16)
- 管理系统正式版本配置文件修改

![在这里插入图片描述](https://img-blog.csdnimg.cn/576a70eb0fb64b9384deb5ae3a4e3e49.png?x-oss-process=image/watermark,type_ZHJvaWRzYW5zZmFsbGJhY2s,shadow_50,text_Q1NETiBA5LiJ5bKB5pe26LaF5biF5ZOm,size_20,color_FFFFFF,t_70,g_se,x_16)

- 微信小程序配置文件修改

![在这里插入图片描述](https://img-blog.csdnimg.cn/38ca60af4a634e908ab40a16d5158851.png?x-oss-process=image/watermark,type_ZHJvaWRzYW5zZmFsbGJhY2s,shadow_50,text_Q1NETiBA5LiJ5bKB5pe26LaF5biF5ZOm,size_20,color_FFFFFF,t_70,g_se,x_16)

![在这里插入图片描述](https://img-blog.csdnimg.cn/7630b59e9ce8476e823dd54e784c037c.png?x-oss-process=image/watermark,type_ZHJvaWRzYW5zZmFsbGJhY2s,shadow_50,text_Q1NETiBA5LiJ5bKB5pe26LaF5biF5ZOm,size_20,color_FFFFFF,t_70,g_se,x_16)


## 启动项目
### 后端服务启动
确认好配置修改完成后可直接右键启动

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

1. 项目
如果你是一名在校生，你可以拿着这套代码去部署、去创业、去参加大创、挑战杯各类比赛申请经费都没有问题，甚至毕业面试也是一份不错的履历。
2. 金钱
微信小程序集成外卖优惠券模块和微信小商店，开发者自行替换成自己的Url，用户使用即可实现盈利。
3. 技术
从我的角度这是一个对学生以及毕业生非常好的练手项目，因为在开始做这个项目的时候我还是一名西安文理学院的在校生，从架构从逻辑都是我一步步走过来的，用这个项目练手一是有我做的基础在不会无聊，二是各方面技术都有涉猎，三是系统可上线。
# 常见问题
一般大部分问题都可以在[**微信官网文档**](https://developers.weixin.qq.com/miniprogram/dev/framework/)和[**JeecgBoot开发文档**](http://doc.jeecg.com/2043872)中找到答案，请确保吃透官方文档，如果仍旧无法解决或无从下手可以进QQ群：702655687尝试询问。
