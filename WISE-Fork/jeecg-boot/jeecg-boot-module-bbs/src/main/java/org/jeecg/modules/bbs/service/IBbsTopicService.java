package org.jeecg.modules.bbs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.bbs.entity.BbsTopic;
import org.jeecg.modules.bbs.entity.BbsTopicImage;
import org.jeecg.modules.bbs.entity.BbsTopicLink;
import org.jeecg.modules.bbs.entity.BbsTopicTag;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 帖子
 * @Author: jeecg-boot
 * @Date: 2021-05-25
 * @Version: V1.0
 */
public interface IBbsTopicService extends IService<BbsTopic> {

    /**
     * 添加一对多
     */
    public void saveMain(BbsTopic bbsTopic, List<BbsTopicImage> bbsTopicImageList, List<BbsTopicTag> bbsTopicTagList, List<BbsTopicLink> bbsTopicLinkList);

    /**
     * 修改一对多
     */
    public void updateMain(BbsTopic bbsTopic, List<BbsTopicImage> bbsTopicImageList, List<BbsTopicTag> bbsTopicTagList, List<BbsTopicLink> bbsTopicLinkList);

    /**
     * 删除一对多
     */
    public void delMain(String id);

    /**
     * 批量删除一对多
     */
    public void delBatchMain(Collection<? extends Serializable> idList);


    Result<?> searchTopicByKeyword(String keyword, HttpServletRequest req, int pageNo, int pageSize);

    Result<?> deletePublishTopic(String topicId);

    void deletePublishTopicBatch(List<String> list);
}
