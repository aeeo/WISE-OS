package org.jeecg.modules.bbs.utils;

import org.jeecg.modules.bbs.entity.BbsSys;
import org.jeecg.modules.bbs.service.IBbsSysService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
@Component
@WebListener
public class SessionCounter implements HttpSessionListener {
    public static SessionCounter sessionCounter;
    @PostConstruct
    public void init() {
        sessionCounter = this;
    }
    @Autowired
    private IBbsSysService bbsSysService;

    private static int activeSessions = 0;
    //session创建时执行
    @Override
    public void sessionCreated(HttpSessionEvent se) {
        activeSessions++;
        //websocket在线人数保存
        //sessionCounter.bbsSysService.lambdaUpdate().eq(BbsSys::getSysKey, "online_people").set(BbsSys::getSysValueString, activeSessions).update();
    }
    //session销毁时执行
    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        if (activeSessions > 0) {
            activeSessions--;
            //websocket在线人数保存
            //sessionCounter.bbsSysService.lambdaUpdate().eq(BbsSys::getSysKey, "online_people").set(BbsSys::getSysValueString, activeSessions).update();
        }
    }
    //获取活动的session个数(在线人数)
    public static int getActiveSessions() {
        return activeSessions;
    }

}