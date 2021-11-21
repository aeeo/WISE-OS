package org.jeecg.modules.bbs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.bbs.entity.BbsRegion;
import org.jeecg.modules.bbs.entity.BbsClass;
import org.jeecg.modules.bbs.entity.BbsTopic;
import org.jeecg.modules.bbs.entity.BbsTopicImage;
import org.jeecg.modules.bbs.mapper.BbsClassMapper;
import org.jeecg.modules.bbs.mapper.BbsRegionMapper;
import org.jeecg.modules.bbs.service.IBbsClassService;
import org.jeecg.modules.bbs.service.IBbsRegionService;
import org.jeecg.modules.bbs.service.IBbsTopicImageService;
import org.jeecg.modules.bbs.service.IBbsTopicService;
import org.jeecg.modules.cache.BbsRedisUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Collection;
import java.util.UUID;

/**
 * @Description: 地区
 * @Author: jeecg-boot
 * @Date: 2021-02-28
 * @Version: V1.0
 */
@Service
public class BbsRegionServiceImpl extends ServiceImpl<BbsRegionMapper, BbsRegion> implements IBbsRegionService {

    @Autowired
    private BbsRegionMapper bbsRegionMapper;
    @Autowired
    private BbsClassMapper bbsClassMapper;
    @Autowired
    private IBbsRegionService bbsRegionService;
    @Autowired
    private IBbsClassService bbsClassService;
    @Autowired
    private IBbsTopicService bbsTopicService;
    @Autowired
    private IBbsTopicImageService bbsTopicImageService;
    @Autowired
    private BbsRedisUtils bbsRedisUtils;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addRegion(BbsRegion bbsRegion, List<BbsClass> bbsClassList) {
        //获取表中最大的region_id
        int maxId = bbsRegionMapper.maxRegionId();
        bbsRegion.setRegionId(++maxId);
        //版块设置
        //全局
        //    1. 首页 index
        //    5. 区域名缩写（西安文理学院 - 文理学院 - wenlixueyuan）
        //    10. 话题 huati
        //    15. 吐槽 tucao
        //    20. 表白 biaobai
        //	  1000. 其他 qita
        //区域自定义
        //    25. 活动 huodong
        //    30. 安利 anli
        //    40. 宠物 chongwu
        //    45. 转让 zhuanrang
        //    50. 农产品 nongchanpin	(下沉城市专属)

        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        //拼音小写
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        //不带声调
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        //要转换的中文，格式，转换之后的拼音的分隔符，遇到不能转换的是否保留   wo,shi,zhong,guo,ren,，hello
        String class2 = null;
        try {
            class2 = PinyinHelper.toHanYuPinyinString(bbsRegion.getName(), format, "", false);
        } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
            badHanyuPinyinOutputFormatCombination.printStackTrace();
        }

        BbsClass bbsClass1 = new BbsClass();
        bbsClass1.setClassName("首页");
        bbsClass1.setClassCode("index");
        bbsClass1.setClassSort(1);

        //如果是学校类型，添加
        BbsClass bbsClass2 = new BbsClass();
        if (bbsRegion.getRegionType() == 2) {
            bbsClass2.setClassName(bbsRegion.getName());
            bbsClass2.setClassCode(class2);
            bbsClass2.setClassSort(5);
        }

        BbsClass bbsClass3 = new BbsClass();
        bbsClass3.setClassName("话题");
        bbsClass3.setClassCode("huati");
        bbsClass3.setClassSort(10);

        BbsClass bbsClass4 = new BbsClass();
        bbsClass4.setClassName("吐槽");
        bbsClass4.setClassCode("tucao");
        bbsClass4.setClassSort(15);

        BbsClass bbsClass5 = new BbsClass();
        bbsClass5.setClassName("表白");
        bbsClass5.setClassCode("biaobai");
        bbsClass5.setClassSort(20);

        BbsClass bbsClass6 = new BbsClass();
        bbsClass6.setClassName("其他");
        bbsClass6.setClassCode("qita");
        bbsClass6.setClassSort(1000);

        BbsClass bbsClass7 = new BbsClass();
        bbsClass7.setClassName("活动");
        bbsClass7.setClassCode("huodong");
        bbsClass7.setClassSort(25);

        BbsClass bbsClass8 = new BbsClass();
        bbsClass8.setClassName("安利");
        bbsClass8.setClassCode("anli");
        bbsClass8.setClassSort(30);

        BbsClass bbsClass10 = new BbsClass();
        bbsClass10.setClassName("宠物");
        bbsClass10.setClassCode("chongwu");
        bbsClass10.setClassSort(40);

        BbsClass bbsClass11 = new BbsClass();
        bbsClass11.setClassName("转让");
        bbsClass11.setClassCode("zhuanrang");
        bbsClass11.setClassSort(45);

        bbsClassList.add(bbsClass1);
        bbsClassList.add(bbsClass2);
        bbsClassList.add(bbsClass3);
        bbsClassList.add(bbsClass4);
        bbsClassList.add(bbsClass5);
        bbsClassList.add(bbsClass6);
        bbsClassList.add(bbsClass7);
        bbsClassList.add(bbsClass8);
        bbsClassList.add(bbsClass10);
        bbsClassList.add(bbsClass11);

        //插入
        bbsRegionMapper.insert(bbsRegion);
        if (bbsClassList != null && bbsClassList.size() > 0) {
            for (BbsClass entity : bbsClassList) {
                //跳过空版块
                if (null == entity.getClassCode() || entity.getClassCode().isEmpty()) {
                    continue;
                }
                //外键设置
                entity.setRegionCode(bbsRegion.getRegionCode());
                bbsClassMapper.insert(entity);
            }
        }
    }

    @Override
    public void initAllRegionClass() {
        List<BbsRegion> list = bbsRegionService.lambdaQuery().list();
        for (int i = 0; i < list.size(); i++) {
            List<BbsTopic> list1 = bbsTopicService.lambdaQuery().eq(BbsTopic::getRegionCode, list.get(i).getRegionCode()).list();
            if (list1.size() == 0) {
                //插入贴子主表
                bbsClassMapper.addClassByRegionFullName(list.get(i).getRegionCode(), list.get(i).getFullName());

                //插入图片
                List<BbsTopic> topicRegionList = bbsTopicService.lambdaQuery().eq(BbsTopic::getRegionCode, list.get(i).getRegionCode()).list();

                //SELECT * FROM bbs_topic_image AS a LEFT JOIN bbs_topic AS b ON a.topic_id = b.id
                ArrayList<BbsTopicImage> bbsTopicImages = new ArrayList<>();
                bbsTopicImages.add(new BbsTopicImage(UUID.randomUUID().toString().replaceAll("-",""),"o_XHg4pJvz9U7d_RoU-oDWPiqBVE", null, null, null, "userdata/1615693236309_1365696104236396546", "你知道吗？在王者峡谷里，鲁班七号平均每天被杀1800万次，也就是平均每小时被杀75万次，假设你读到这里所需时间为5秒，那么鲁班已经在峡谷里死了1040次。不，你不知道，你只关心你自己。"));
                bbsTopicImages.add(new BbsTopicImage(UUID.randomUUID().toString().replaceAll("-",""),"o_XHg4pJvz9U7d_RoU-oDWPiqBVE", null, null, null, "userdata/1615695401374_1365696104236396546", "这里是厕所深处，我吃饭的地方，欢迎来到我的食堂，娇贵的干饭人。"));
                bbsTopicImages.add(new BbsTopicImage(UUID.randomUUID().toString().replaceAll("-",""),"o_XHg4pJvz9U7d_RoU-oDWPiqBVE", null, null, null, "userdata/1615695401354_1365696104236396546", "这里是厕所深处，我吃饭的地方，欢迎来到我的食堂，娇贵的干饭人。"));
                bbsTopicImages.add(new BbsTopicImage(UUID.randomUUID().toString().replaceAll("-",""),"o_XHg4pJvz9U7d_RoU-oDWPiqBVE", null, null, null, "userdata/1615695651854_1365696104236396546", "你知道吗？人的头发主要由蛋白质构成，如果将其分解，能够得到20中氨基酸。这些氨基酸完全可以用来做化学调味或汤的底料，而且味道很好。你有没有想过？没有，你只关心你自己。"));
                bbsTopicImages.add(new BbsTopicImage(UUID.randomUUID().toString().replaceAll("-",""),"o_XHg4pJvz9U7d_RoU-oDWPiqBVE", null, null, null, "userdata/1615695651873_1365696104236396546", "你知道吗？人的头发主要由蛋白质构成，如果将其分解，能够得到20中氨基酸。这些氨基酸完全可以用来做化学调味或汤的底料，而且味道很好。你有没有想过？没有，你只关心你自己。"));
                bbsTopicImages.add(new BbsTopicImage(UUID.randomUUID().toString().replaceAll("-",""),"o_XHg4pJvz9U7d_RoU-oDWPiqBVE", null, null, null, "userdata/1615698796084_1365696104236396546", "你知道吗？你每天所喝的水中，几乎每个水分子都被恐龙喝过。你没有想过？没有，你只关心你自己。"));
                bbsTopicImages.add(new BbsTopicImage(UUID.randomUUID().toString().replaceAll("-",""),"o_XHg4pJvz9U7d_RoU-oDWPiqBVE", null, null, null, "userdata/1615698925786_1365696104236396546", "你知道吗？南极有1.2亿只企鹅，同时，北极目前只剩下2万多只北极熊。如果企鹅决定入侵北极，那么一只北极熊要吃6000只企鹅。你有没有想过？没有，你只关心你自己！"));
                bbsTopicImages.add(new BbsTopicImage(UUID.randomUUID().toString().replaceAll("-",""),"o_XHg4pJvz9U7d_RoU-oDWPiqBVE", null, null, null, "userdata/1615698925779_1365696104236396546", "你知道吗？南极有1.2亿只企鹅，同时，北极目前只剩下2万多只北极熊。如果企鹅决定入侵北极，那么一只北极熊要吃6000只企鹅。你有没有想过？没有，你只关心你自己！"));
                bbsTopicImages.add(new BbsTopicImage(UUID.randomUUID().toString().replaceAll("-",""),"o_XHg4pJvz9U7d_RoU-oDWPiqBVE", null, null, null, "userdata/1615698925764_1365696104236396546", "你知道吗？南极有1.2亿只企鹅，同时，北极目前只剩下2万多只北极熊。如果企鹅决定入侵北极，那么一只北极熊要吃6000只企鹅。你有没有想过？没有，你只关心你自己！"));
                bbsTopicImages.add(new BbsTopicImage(UUID.randomUUID().toString().replaceAll("-",""),"o_XHg4pJvz9U7d_RoU-oDWPiqBVE", null, null, null, "userdata/1615699226224_1365696104236396546", "你知道吗，长颈鹿喝咖啡的时候，在嘴边是热的，咖啡还没到肚子里就凉了。你不知道 你只在乎你的核弹！"));
                bbsTopicImages.add(new BbsTopicImage(UUID.randomUUID().toString().replaceAll("-",""),"o_XHg4pJvz9U7d_RoU-oDWPiqBVE", null, null, null, "userdata/1615699226241_1365696104236396546", "你知道吗，长颈鹿喝咖啡的时候，在嘴边是热的，咖啡还没到肚子里就凉了。你不知道 你只在乎你的核弹！"));
                bbsTopicImages.add(new BbsTopicImage(UUID.randomUUID().toString().replaceAll("-",""),"o_XHg4pJvz9U7d_RoU-oDWPiqBVE", null, null, null, "userdata/1615699306378_1365696104236396546", "我不是十分认可你的观点，因为我在火星的海底吃泡面，而你在美国的飞机上准备去火星打袋鼠！可是这篇文章所描述的，并不是外星人能否扎辫子。所以你所说的微博其实是对的，但是我只想说：乌拉圭的石油在医院里面真的很吃香。我作为一个地地道道的非洲白人对于理智的疯子来说，我的秘密是不能告诉新西兰的猩猩的，所以你会觉得我很菜吗？你绝对会，因为昨天的猪头剃了个光头。但其实真正导致这个问题的起源是gta5很贵，我认为这个价钱可能跟你的哥哥没有关系，因为老鼠把我的鼻屎吃了，你能理解吗？我最开始为什么不认可你。但是有一说一酸奶烘焙健康早餐不好吃，因为微积分的小数点变成了句号，而我旁边的猴子是金猴因此它的手里拿了唢呐。你的观点大体上是没错的，但是你没考虑到刚果的总统是黑人。总而言之这个和你的作息有关，你可以试一试把新鲜的带鱼放在头上。我的手机要没电了，这并不影响加拿大的冰。我喝的是美味的芜湖江水，因此美国的总统是萨科而且蒙古的海军十分强大，我认为我的逻辑没有问题因为罗技是个大牌子。严肃来说根据刑法第114514条的规定，我的窗帘已经拉开了。总而言之：言而总之：爱迪生真的不会说英语。你还违背了猎豹的拇指定律因为猩猩的体毛是电脑形状的，你不在乎，你只在乎自己能否咬打火机。因此我可以确定，艾德曼金属的主要成分是teacher ma的嘴! 我呼出的每一口气都有利于蜜蜂与老鼠交配繁衍，温室里的鸡经过六个月的孵化就会生根发芽。有一个全球性的问题，麻辣烫里有几颗鹌鹑蛋，这是你自己要考虑的事情，进了社会没人会帮你解决全球变暖的问题。其实我只想说铅笔并不能造出飞机，每一次降雨都会带来数以万计的蚂蚁，所以只能提高生产力，这样才能缩短轮胎破土而出的时间。您好，我毕业于清华大学，不过去年才拿到小学毕业证，希望贵司可以将我种在撒哈拉沙漠里。"));
                bbsTopicImages.add(new BbsTopicImage(UUID.randomUUID().toString().replaceAll("-",""),"o_XHg4pJvz9U7d_RoU-oDWPiqBVE", null, null, null, "userdata/1615699390693_1365696104236396546", "大家玩儿梗玩儿的这么开心你搁这儿泼冷水扫兴，你关心过我们吗？你只关心你自己！"));
                bbsTopicImages.add(new BbsTopicImage(UUID.randomUUID().toString().replaceAll("-",""),"o_XHg4pJvz9U7d_RoU-oDWPiqBVE", null, null, null, "userdata/1615699390684_1365696104236396546", "大家玩儿梗玩儿的这么开心你搁这儿泼冷水扫兴，你关心过我们吗？你只关心你自己！"));
                bbsTopicImages.add(new BbsTopicImage(UUID.randomUUID().toString().replaceAll("-",""),"o_XHg4pJvz9U7d_RoU-oDWPiqBVE", null, null, null, "userdata/1615699390666_1365696104236396546", "大家玩儿梗玩儿的这么开心你搁这儿泼冷水扫兴，你关心过我们吗？你只关心你自己！"));
                bbsTopicImages.add(new BbsTopicImage(UUID.randomUUID().toString().replaceAll("-",""),"o_XHg4pJvz9U7d_RoU-oDWPiqBVE", null, null, null, "userdata/1615699480471_1365696104236396546", "光之国奥特警备队里就有30多亿个奥特曼，而地球的总人口只有70亿，如果光之国派奥特警备队攻击地球，那么地球上每两个人就要对战一个平均身高四十米，平均体重四万吨，能大能小，平均飞行速度9马赫，能近战能放光线还会使用各种武器且战斗经验丰富的强大奥特曼，你不知道，你不在乎，你只在乎你自己。"));
                bbsTopicImages.add(new BbsTopicImage(UUID.randomUUID().toString().replaceAll("-",""),"o_XHg4pJvz9U7d_RoU-oDWPiqBVE", null, null, null, "userdata/1615699480451_1365696104236396546", "光之国奥特警备队里就有30多亿个奥特曼，而地球的总人口只有70亿，如果光之国派奥特警备队攻击地球，那么地球上每两个人就要对战一个平均身高四十米，平均体重四万吨，能大能小，平均飞行速度9马赫，能近战能放光线还会使用各种武器且战斗经验丰富的强大奥特曼，你不知道，你不在乎，你只在乎你自己。"));
                bbsTopicImages.add(new BbsTopicImage(UUID.randomUUID().toString().replaceAll("-",""),"o_XHg4pJvz9U7d_RoU-oDWPiqBVE", null, null, null, "userdata/1615699480433_1365696104236396546", "光之国奥特警备队里就有30多亿个奥特曼，而地球的总人口只有70亿，如果光之国派奥特警备队攻击地球，那么地球上每两个人就要对战一个平均身高四十米，平均体重四万吨，能大能小，平均飞行速度9马赫，能近战能放光线还会使用各种武器且战斗经验丰富的强大奥特曼，你不知道，你不在乎，你只在乎你自己。"));
                bbsTopicImages.add(new BbsTopicImage(UUID.randomUUID().toString().replaceAll("-",""),"o_XHg4pJvz9U7d_RoU-oDWPiqBVE", null, null, null, "userdata/1615699663567_1365696104236396546", "你说得对，但是中国的人口有14亿，同时，澳大利亚只有4700万只袋鼠。 如果袋鼠决定入侵中国，那么30个中国人才能分到一只袋鼠。你不知道，你不在乎，你只关心你的经验"));
                bbsTopicImages.add(new BbsTopicImage(UUID.randomUUID().toString().replaceAll("-",""),"o_XHg4pJvz9U7d_RoU-oDWPiqBVE", null, null, null, "userdata/1615699663550_1365696104236396546", "你说得对，但是中国的人口有14亿，同时，澳大利亚只有4700万只袋鼠。 如果袋鼠决定入侵中国，那么30个中国人才能分到一只袋鼠。你不知道，你不在乎，你只关心你的经验"));
                bbsTopicImages.add(new BbsTopicImage(UUID.randomUUID().toString().replaceAll("-",""),"o_XHg4pJvz9U7d_RoU-oDWPiqBVE", null, null, null, "userdata/1615699663526_1365696104236396546", "你说得对，但是中国的人口有14亿，同时，澳大利亚只有4700万只袋鼠。 如果袋鼠决定入侵中国，那么30个中国人才能分到一只袋鼠。你不知道，你不在乎，你只关心你的经验"));
                bbsTopicImages.add(new BbsTopicImage(UUID.randomUUID().toString().replaceAll("-",""),"o_XHg4pJvz9U7d_RoU-oDWPiqBVE", null, null, null, "userdata/1615699757226_1365696104236396546", "吸血鬼吸人血是为了补充维生素D，因为他们无法去晒太阳，你有了解过吗？你没有，你只关心你自己"));
                bbsTopicImages.add(new BbsTopicImage(UUID.randomUUID().toString().replaceAll("-",""),"o_XHg4pJvz9U7d_RoU-oDWPiqBVE", null, null, null, "userdata/1615699757235_1365696104236396546", "吸血鬼吸人血是为了补充维生素D，因为他们无法去晒太阳，你有了解过吗？你没有，你只关心你自己"));
                bbsTopicImages.add(new BbsTopicImage(UUID.randomUUID().toString().replaceAll("-",""),"o_XHg4pJvz9U7d_RoU-oDWPiqBVE", null, null, null, "userdata/1615699757202_1365696104236396546", "吸血鬼吸人血是为了补充维生素D，因为他们无法去晒太阳，你有了解过吗？你没有，你只关心你自己"));
                bbsTopicImages.add(new BbsTopicImage(UUID.randomUUID().toString().replaceAll("-",""),"o_XHg4pJvz9U7d_RoU-oDWPiqBVE", null, null, null, "userdata/1615699918524_1365696104236396546", "你说的对，可是老干妈的酸辣并不能为广东肠粉提鲜，反而黄焖鸡米饭更让我感觉吃得放心；而实际上俄罗斯的优势在于地广人稀，假设元神的成功可以带领中国单机游戏旭日东升，那么我觉得穿西装的话，还是打领带比较得体一些。 我觉得这个观点有点偏激了，首先外星人是紫色的，而且派大星和海绵宝宝一起去抓水母了。当然，太阳从东边升起的时候，也代表她会从西边落下。依古比古的毯子好像是红色的，就算小头爸爸不是大耳朵图图的亲生父亲，我还是认为肖战不应该偷猪猪侠的超级棒棒糖。 可是这对在夏威夷跟着恶魔人学习超级赛亚奥特曼的蒙奇鸣一来说有什么作用呢？去非洲打企鹅才能更加接近掀翻敬老院的天下第一网球武道会三大将吧。 我不是十分认可你的观点，因为我在美利坚前往火星的飞机上而但你却在海底打乌拉圭的袋鼠，因为外星人戴着紫色的帽子，所以你会觉得我很菜么？你绝对会。不过带鱼用手机将皮卡丘变成了酸奶，是的，我也有一个巧克力棒。 你说的是一个方面，但从另一个角度讲，恐龙灭绝于6500万年前的白垩纪大灭绝事件，但月球上有5万个陨石坑，所以许文强和丁力可以称霸上海滩，然而很可惜日本老龄化严重，全球变暖毫无疑问会加剧这一进程，企鹅会在其中扮演什么样的角色，山顶洞人的命运将何去何从？没人知道。"));
                bbsTopicImages.add(new BbsTopicImage(UUID.randomUUID().toString().replaceAll("-",""),"o_XHg4pJvz9U7d_RoU-oDWPiqBVE", null, null, null, "userdata/1615699918541_1365696104236396546", "你说的对，可是老干妈的酸辣并不能为广东肠粉提鲜，反而黄焖鸡米饭更让我感觉吃得放心；而实际上俄罗斯的优势在于地广人稀，假设元神的成功可以带领中国单机游戏旭日东升，那么我觉得穿西装的话，还是打领带比较得体一些。 我觉得这个观点有点偏激了，首先外星人是紫色的，而且派大星和海绵宝宝一起去抓水母了。当然，太阳从东边升起的时候，也代表她会从西边落下。依古比古的毯子好像是红色的，就算小头爸爸不是大耳朵图图的亲生父亲，我还是认为肖战不应该偷猪猪侠的超级棒棒糖。 可是这对在夏威夷跟着恶魔人学习超级赛亚奥特曼的蒙奇鸣一来说有什么作用呢？去非洲打企鹅才能更加接近掀翻敬老院的天下第一网球武道会三大将吧。 我不是十分认可你的观点，因为我在美利坚前往火星的飞机上而但你却在海底打乌拉圭的袋鼠，因为外星人戴着紫色的帽子，所以你会觉得我很菜么？你绝对会。不过带鱼用手机将皮卡丘变成了酸奶，是的，我也有一个巧克力棒。 你说的是一个方面，但从另一个角度讲，恐龙灭绝于6500万年前的白垩纪大灭绝事件，但月球上有5万个陨石坑，所以许文强和丁力可以称霸上海滩，然而很可惜日本老龄化严重，全球变暖毫无疑问会加剧这一进程，企鹅会在其中扮演什么样的角色，山顶洞人的命运将何去何从？没人知道。"));
                bbsTopicImages.add(new BbsTopicImage(UUID.randomUUID().toString().replaceAll("-",""),"o_XHg4pJvz9U7d_RoU-oDWPiqBVE", null, null, null, "userdata/1615699918558_1365696104236396546", "你说的对，可是老干妈的酸辣并不能为广东肠粉提鲜，反而黄焖鸡米饭更让我感觉吃得放心；而实际上俄罗斯的优势在于地广人稀，假设元神的成功可以带领中国单机游戏旭日东升，那么我觉得穿西装的话，还是打领带比较得体一些。 我觉得这个观点有点偏激了，首先外星人是紫色的，而且派大星和海绵宝宝一起去抓水母了。当然，太阳从东边升起的时候，也代表她会从西边落下。依古比古的毯子好像是红色的，就算小头爸爸不是大耳朵图图的亲生父亲，我还是认为肖战不应该偷猪猪侠的超级棒棒糖。 可是这对在夏威夷跟着恶魔人学习超级赛亚奥特曼的蒙奇鸣一来说有什么作用呢？去非洲打企鹅才能更加接近掀翻敬老院的天下第一网球武道会三大将吧。 我不是十分认可你的观点，因为我在美利坚前往火星的飞机上而但你却在海底打乌拉圭的袋鼠，因为外星人戴着紫色的帽子，所以你会觉得我很菜么？你绝对会。不过带鱼用手机将皮卡丘变成了酸奶，是的，我也有一个巧克力棒。 你说的是一个方面，但从另一个角度讲，恐龙灭绝于6500万年前的白垩纪大灭绝事件，但月球上有5万个陨石坑，所以许文强和丁力可以称霸上海滩，然而很可惜日本老龄化严重，全球变暖毫无疑问会加剧这一进程，企鹅会在其中扮演什么样的角色，山顶洞人的命运将何去何从？没人知道。"));
                bbsTopicImages.add(new BbsTopicImage(UUID.randomUUID().toString().replaceAll("-",""),"o_XHg4pJvz9U7d_RoU-oDWPiqBVE", null, null, null, "userdata/1615700441755_1365696104236396546", "特朗普今年已经74岁了，他在四年间整整蒸发了20亿美元的财产却不厌其烦的建设我国，假设4700万袋鼠游完全程大概需要441.8亿公斤食物，那么特朗普拥有24亿美元的资产就要分给每个袋鼠平均0.005美元购买食物，除非特朗普愿意和他的家人一起不吃不喝，否则袋鼠将永远不可能入侵乌拉圭。他可是个74岁的老人啊，你关心过特朗普吗？没有！你只关心袋鼠能不能入侵乌拉圭！"));
                bbsTopicImages.add(new BbsTopicImage(UUID.randomUUID().toString().replaceAll("-",""),"o_XHg4pJvz9U7d_RoU-oDWPiqBVE", null, null, null, "userdata/1615700441735_1365696104236396546", "特朗普今年已经74岁了，他在四年间整整蒸发了20亿美元的财产却不厌其烦的建设我国，假设4700万袋鼠游完全程大概需要441.8亿公斤食物，那么特朗普拥有24亿美元的资产就要分给每个袋鼠平均0.005美元购买食物，除非特朗普愿意和他的家人一起不吃不喝，否则袋鼠将永远不可能入侵乌拉圭。他可是个74岁的老人啊，你关心过特朗普吗？没有！你只关心袋鼠能不能入侵乌拉圭！"));
                bbsTopicImages.add(new BbsTopicImage(UUID.randomUUID().toString().replaceAll("-",""),"o_XHg4pJvz9U7d_RoU-oDWPiqBVE", null, null, null, "userdata/1615700441714_1365696104236396546", "特朗普今年已经74岁了，他在四年间整整蒸发了20亿美元的财产却不厌其烦的建设我国，假设4700万袋鼠游完全程大概需要441.8亿公斤食物，那么特朗普拥有24亿美元的资产就要分给每个袋鼠平均0.005美元购买食物，除非特朗普愿意和他的家人一起不吃不喝，否则袋鼠将永远不可能入侵乌拉圭。他可是个74岁的老人啊，你关心过特朗普吗？没有！你只关心袋鼠能不能入侵乌拉圭！"));
                bbsTopicImages.add(new BbsTopicImage(UUID.randomUUID().toString().replaceAll("-",""),"o_XHg4pJvz9U7d_RoU-oDWPiqBVE", null, null, null, "userdata/1615700534670_1365696104236396546", "你知不知道每当鲸鱼拉屎的时候，它的屁屁要张开很大，拉完之后久久不能闭合。非常痛苦你不知道，因为你只在乎你自己。"));
                bbsTopicImages.add(new BbsTopicImage(UUID.randomUUID().toString().replaceAll("-",""),"o_XHg4pJvz9U7d_RoU-oDWPiqBVE", null, null, null, "userdata/1615700578434_1365696104236396546", "乌拉圭距离澳大利亚有13509公里，假设每只袋鼠的平均游泳速度为3km/h，24小时不停歇赶往乌拉圭，登陆海岸大概需要188天，又假设每只袋鼠每天需要进食5公斤食物，那么4700万袋鼠游完全程大概需要441.8亿公斤食物。综上所述，除非特朗普愿意提供物资给4700万只袋鼠，则袋鼠将永远不可能入侵乌拉圭。你关心过特朗普么？没有！你只关心你自己。"));
                bbsTopicImages.add(new BbsTopicImage(UUID.randomUUID().toString().replaceAll("-",""),"o_XHg4pJvz9U7d_RoU-oDWPiqBVE", null, null, null, "userdata/1615700648731_1365696104236396546", "约五公斤的乳猪，宰杀后在腹部剖开,取出肋骨,放入特制烧烤叉撑开，放入烤炉烤成。如果烧烤时用慢火，烧出的乳猪猪皮光滑，称之为光皮。可以用猛火烧烤，其间在猪皮涂上油，令猪皮成充满气泡的金黄色，即为“麻皮乳猪”。乳猪的特点包括皮薄脆、肉松嫩、骨香酥。吃时把乳猪斩成小件，因肉少皮薄，称为片皮乳猪;有时点上少许“乳猪酱\"以增加风味。全国人民在2019年全年-共吃掉了大约54419万头猪，但全国人民2019年只吃掉了345万头烤乳猪，你不知道，你不关心，你只知道你的母猪需要产后护理。"));
                bbsTopicImages.add(new BbsTopicImage(UUID.randomUUID().toString().replaceAll("-",""),"o_XHg4pJvz9U7d_RoU-oDWPiqBVE", null, null, null, "userdata/1615700648750_1365696104236396546", "约五公斤的乳猪，宰杀后在腹部剖开,取出肋骨,放入特制烧烤叉撑开，放入烤炉烤成。如果烧烤时用慢火，烧出的乳猪猪皮光滑，称之为光皮。可以用猛火烧烤，其间在猪皮涂上油，令猪皮成充满气泡的金黄色，即为“麻皮乳猪”。乳猪的特点包括皮薄脆、肉松嫩、骨香酥。吃时把乳猪斩成小件，因肉少皮薄，称为片皮乳猪;有时点上少许“乳猪酱\"以增加风味。全国人民在2019年全年-共吃掉了大约54419万头猪，但全国人民2019年只吃掉了345万头烤乳猪，你不知道，你不关心，你只知道你的母猪需要产后护理。"));
                bbsTopicImages.add(new BbsTopicImage(UUID.randomUUID().toString().replaceAll("-",""),"o_XHg4pJvz9U7d_RoU-oDWPiqBVE", null, null, null, "userdata/1615700723968_1365696104236396546", "你忽略了一点，袋鼠的尾气排放很猛而且不污染环境，再加上袋鼠可以像大雁学习，利用年气流轮流排气，达到事半功倍倍倍的效果，那平均速度可以提升到48km/h，只需要不到12天！你关心过袋鼠吗?没有，你只关心特朗普"));
                bbsTopicImages.add(new BbsTopicImage(UUID.randomUUID().toString().replaceAll("-",""),"o_XHg4pJvz9U7d_RoU-oDWPiqBVE", null, null, null, "userdata/1615700792939_1365696104236396546", "是的，你说得对，但乌拉圭距离澳大利亚有13509公里，假设每只袋鼠的平均游泳速度为3km/h, 24小时前进，登陆大概需要188天，你不知道，你不在乎，你只关心乌拉圭人!"));
                bbsTopicImages.add(new BbsTopicImage(UUID.randomUUID().toString().replaceAll("-",""),"o_XHg4pJvz9U7d_RoU-oDWPiqBVE", null, null, null, "userdata/1615700860285_1365696104236396546", "说得对，我也这么觉得。但我认为母猪产后护理，首先要从产前做起，母猪产前四五天要逐渐减少饲喂量，其目的是减少腹部压力产前吃得少产后才能吃得多。若产前吃得多,不仅会使产程过长，还会造成产后胃积食，你不知道，你不在乎，你只知道你的袋鼠!"));
                bbsTopicImages.add(new BbsTopicImage(UUID.randomUUID().toString().replaceAll("-",""),"o_XHg4pJvz9U7d_RoU-oDWPiqBVE", null, null, null, "userdata/1615700929343_1365696104236396546", "我第一次看到这个是疫情期间有一个高速公路出现野生动物目击的照片，然后有个人发微博说这个生物怎么怎么少，什么什么保护环境保护野生动物什么的。然后有个人反驳他，用的就是这类似的文体，主要大概讲得就是这种野生动物问题什么什么一直在发生，你只是一时良心发现，至于野生动物具体怎么样，你不关心，你只在乎你自己。"));
                bbsTopicImages.add(new BbsTopicImage(UUID.randomUUID().toString().replaceAll("-",""),"o_XHg4pJvz9U7d_RoU-oDWPiqBVE", null, null, null, "userdata/1615700975728_1365696104236396546", "你说得对，但是梵蒂冈的人口只有801人，同时，仅拥核国家就有1.9万枚核弹。如果拥核国家决定核击梵蒂冈，那么每一个梵蒂冈人要扛23枚核弹，你不知道你不在乎，你只关心乌拉圭。"));
                bbsTopicImages.add(new BbsTopicImage(UUID.randomUUID().toString().replaceAll("-",""),"o_XHg4pJvz9U7d_RoU-oDWPiqBVE", null, null, null, "userdata/1615701051982_1365696104236396546", "你知道吗，乌拉圭的人口有345.7万，同时仅澳大利亚就有4700万袋鼠，如果袋鼠决定入侵乌拉圭，那么每一个乌拉圭人都要打14只袋鼠。你不知道，你不在乎，你只关心你自己。"));
                bbsTopicImages.add(new BbsTopicImage(UUID.randomUUID().toString().replaceAll("-",""),"o_XHg4pJvz9U7d_RoU-oDWPiqBVE", null, null, null, "userdata/1615701051963_1365696104236396546", "你知道吗，乌拉圭的人口有345.7万，同时仅澳大利亚就有4700万袋鼠，如果袋鼠决定入侵乌拉圭，那么每一个乌拉圭人都要打14只袋鼠。你不知道，你不在乎，你只关心你自己。"));
                bbsTopicImages.add(new BbsTopicImage(UUID.randomUUID().toString().replaceAll("-",""),"o_XHg4pJvz9U7d_RoU-oDWPiqBVE", null, null, null, "userdata/1615701051994_1365696104236396546", "你知道吗，乌拉圭的人口有345.7万，同时仅澳大利亚就有4700万袋鼠，如果袋鼠决定入侵乌拉圭，那么每一个乌拉圭人都要打14只袋鼠。你不知道，你不在乎，你只关心你自己。"));
                bbsTopicImages.add(new BbsTopicImage(UUID.randomUUID().toString().replaceAll("-",""),"o_XHg4pJvz9U7d_RoU-oDWPiqBVE", null, null, null, "userdata/1615701089390_1365696104236396546", "你知道吗？这个平台是我们辛辛苦苦一行一行敲出来的，因为我们想让你们有更好的体验，手指头都僵硬了。你不知道，你不在乎，你只关心你自己。"));
                for (int j = 0; j < topicRegionList.size(); j++) {
                    //id已经丢失，根据内容匹配对应
                    for (int k = 0; k < bbsTopicImages.size(); k++) {
                        if(topicRegionList.get(j).getContent().equals(bbsTopicImages.get(k).getTopicId())){
                            bbsTopicImages.get(k).setTopicId(topicRegionList.get(j).getId());
                            bbsTopicImageService.save(bbsTopicImages.get(k));
                        }
                    }
                }
            }
        }

    }


    @Override
    @Transactional
    public void saveMain(BbsRegion bbsRegion, List<BbsClass> bbsClassList) {
        bbsRegionMapper.insert(bbsRegion);
        if (bbsClassList != null && bbsClassList.size() > 0) {
            for (BbsClass entity : bbsClassList) {
                //外键设置
                entity.setRegionCode(bbsRegion.getRegionCode());
                bbsClassMapper.insert(entity);
            }
        }
    }

    @Override
    @Transactional
    public void updateMain(BbsRegion bbsRegion, List<BbsClass> bbsClassList) {
        bbsRegionMapper.updateById(bbsRegion);
        ArrayList<BbsRegion> bbsRegions = new ArrayList<>();
        bbsRegions.add(bbsRegion);
        bbsRedisUtils.updateRegion(bbsRegions);

        //1.先删除子表数据(版块无法删除，只能新增)
        //bbsClassMapper.deleteByMainId(bbsRegion.getId());
        for (BbsClass item : bbsClassList) {
            BbsClass bbsClass = bbsClassService.lambdaQuery().eq(BbsClass::getClassCode, item.getClassCode()).eq(BbsClass::getRegionCode, bbsRegion.getRegionCode()).one();
            if (null != bbsClass) {
                //(版块无法删除，只能新增)
                continue;
            }else{
                //外键设置
                item.setRegionCode(bbsRegion.getRegionCode());
                bbsClassMapper.insert(item);
            }
        }

        //2.子表数据重新插入
        //if (bbsClassList != null && bbsClassList.size() > 0) {
        //    for (BbsClass entity : bbsClassList) {
        //        //外键设置
        //        entity.setRegionCode(bbsRegion.getRegionCode());
        //        bbsClassMapper.insert(entity);
        //    }
        //}
    }

    @Override
    @Transactional
    public void delMain(String id) {
        bbsClassMapper.deleteByMainId(id);
        bbsRegionMapper.deleteById(id);
    }

    @Override
    @Transactional
    public void delBatchMain(Collection<? extends Serializable> idList) {
        for (Serializable id : idList) {
            bbsClassMapper.deleteByMainId(id.toString());
            bbsRegionMapper.deleteById(id);
        }
    }


}
