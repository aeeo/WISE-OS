package org.jeecg.modules.bbs.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.bbs.entity.BbsStatist;
import org.jeecg.modules.bbs.mapper.BbsStatistMapper;
import org.jeecg.modules.bbs.service.IBbsStatistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @Description: 统计
 * @Author: jeecg-boot
 * @Date: 2021-05-31
 * @Version: V1.0
 */
@Service
public class BbsStatistServiceImpl extends ServiceImpl<BbsStatistMapper, BbsStatist> implements IBbsStatistService {

    @Autowired
    private IBbsStatistService bbsStatistService;

    @Override
    public JSONObject yesterdayCountInfo(String sysOrgCode) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, -24);
        String todayDate = dateFormat.format(new Date());
        String yesterdayDate = dateFormat.format(calendar.getTime());

        BbsStatist yesterdayCountInfo = bbsStatistService.lambdaQuery()
                .and(l -> l.ge(BbsStatist::getCreateTime, yesterdayDate)
                        .le(BbsStatist::getCreateTime, todayDate))
                .eq(BbsStatist::getName, "dayStatistData-" + sysOrgCode)
                .one();

        //如果第一次
        if (null == yesterdayCountInfo) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userOnlineCount", 0);
            jsonObject.put("userCount", 0);
            jsonObject.put("newUserCount", 0);
            jsonObject.put("topicCount", 0);
            jsonObject.put("newTopicCount", 0);
            jsonObject.put("topicPraiseCount", 0);
            jsonObject.put("newTopicPraiseCount", 0);
            jsonObject.put("replyCount", 0);
            jsonObject.put("newReplyCount", 0);
            jsonObject.put("messageBoardCount", 0);
            jsonObject.put("newMessageBoardCount", 0);
            jsonObject.put("messageBoardPraiseCount", 0);
            jsonObject.put("newMessageBoardPraiseCount", 0);
            jsonObject.put("waimaiUserCount", 0);
            jsonObject.put("newWaimaiUserCount", 0);
            return jsonObject;
        } else {
            JSONObject jsonObject = JSONObject.parseObject(yesterdayCountInfo.getStatistData());
            return jsonObject;
        }
    }

    @Override
    public int yesterdayCount(String sysOrgCode, String name) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, -24);
        String todayDate = dateFormat.format(new Date());
        String yesterdayDate = dateFormat.format(calendar.getTime());

        BbsStatist yesterdayCountInfo = bbsStatistService.lambdaQuery()
                .and(l -> l.ge(BbsStatist::getCreateTime, yesterdayDate)
                        .le(BbsStatist::getCreateTime, todayDate))
                .eq(BbsStatist::getName, "dayStatistData-" + sysOrgCode)
                .one();

        JSONObject yesterdayCountInfoJson = JSONObject.parseObject(yesterdayCountInfo.getStatistData());

        int count = Integer.valueOf(yesterdayCountInfoJson.get(name).toString());
        return count;
    }

    @Override
    @Transactional
    public boolean insertDayStatistData(JSONObject jsonObject, String sysOrgCode) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        String todayDate = dateFormat.format(new Date());

        BbsStatist bbsStatist1 = bbsStatistService.lambdaQuery()
                .ge(BbsStatist::getCreateTime, todayDate)
                .eq(BbsStatist::getName, "dayStatistData-" + sysOrgCode)
                .one();
        BbsStatist bbsStatist = new BbsStatist();
        if (null != bbsStatist1) {
            bbsStatist.setId(bbsStatist1.getId());
        }
        bbsStatist.setName("dayStatistData-" + sysOrgCode);
        bbsStatist.setStatistData(jsonObject.toJSONString());
        boolean b = bbsStatistService.saveOrUpdate(bbsStatist);
        return b;
    }

}
