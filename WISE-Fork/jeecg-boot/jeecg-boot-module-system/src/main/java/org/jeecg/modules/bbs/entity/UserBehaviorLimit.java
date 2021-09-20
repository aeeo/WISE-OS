package org.jeecg.modules.bbs.entity;

import lombok.Data;

@Data
public class UserBehaviorLimit {
    private Boolean canDayPunlishMessage;
    private Boolean canDayPunlishReply;
    private Boolean canDayPunlishTopic;
    private Boolean canDayPunlishparise;
    private Boolean canWeekPunlishMessage;
    private Boolean canWeekPunlishReply;
    private Boolean canWeekPunlishTopic;
    private Boolean canWeekPunlishparise;
}