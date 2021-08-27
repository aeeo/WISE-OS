package org.jeecg.modules.bbs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.bbs.entity.BbsReply;
import org.jeecg.modules.bbs.mapper.BbsReplyMapper;
import org.jeecg.modules.bbs.service.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.bbs.entity.*;
import org.jeecg.modules.bbs.entity.BbsReply;
import org.jeecg.modules.bbs.mapper.BbsReplyMapper;
import org.jeecg.modules.bbs.service.IBbsReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Description: 帖子回复
 * @Author: jeecg-boot
 * @Date: 2021-05-26
 * @Version: V1.0
 */
@Service
public class BbsReplyServiceImpl extends ServiceImpl<BbsReplyMapper, BbsReply> implements IBbsReplyService {

    @Autowired
    private IBbsReplyService bbsReplyService;
    @Autowired
    private BbsTopicImageServiceImpl bbsTopicImageService;
    @Autowired
    private BbsUserMessageServiceImpl bbsUserMessageService;
    @Autowired
    private IBbsUserPraiseService bbsUserPraiseService;

    @Autowired
    private BbsTopicServiceImpl bbsTopicService;
    @Autowired
    private IBbsUserRecordService bbsUserRecordService;
    @Autowired
    private BbsRegionServiceImpl bbsRegionService;


    @Override
    public void addBbsReply(BbsReply bbsReply) {
        if (oConvertUtils.isEmpty(bbsReply.getPid())) {
            bbsReply.setPid(IBbsReplyService.ROOT_PID_VALUE);
        } else {
            //如果当前节点父ID不为空 则设置父节点的hasChildren 为1
            BbsReply parent = baseMapper.selectById(bbsReply.getPid());
            if (parent != null && !"1".equals(parent.getHasChild())) {
                parent.setHasChild("1");
                baseMapper.updateById(parent);
            }
        }
        baseMapper.insert(bbsReply);
    }

    @Override
    public void updateBbsReply(BbsReply bbsReply) {
        BbsReply entity = this.getById(bbsReply.getId());
        if (entity == null) {
            throw new JeecgBootException("未找到对应实体");
        }
        String old_pid = entity.getPid();
        String new_pid = bbsReply.getPid();
        if (!old_pid.equals(new_pid)) {
            updateOldParentNode(old_pid);
            if (oConvertUtils.isEmpty(new_pid)) {
                bbsReply.setPid(IBbsReplyService.ROOT_PID_VALUE);
            }
            if (!IBbsReplyService.ROOT_PID_VALUE.equals(bbsReply.getPid())) {
                baseMapper.updateTreeNodeStatus(bbsReply.getPid(), IBbsReplyService.HASCHILD);
            }
        }
        baseMapper.updateById(bbsReply);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBbsReply(String id) throws JeecgBootException {
        //查询选中节点下所有子节点一并删除
        id = this.queryTreeChildIds(id);
        if (id.indexOf(",") > 0) {
            StringBuffer sb = new StringBuffer();
            String[] idArr = id.split(",");
            for (String idVal : idArr) {
                if (idVal != null) {
                    BbsReply bbsReply = this.getById(idVal);
                    String pidVal = bbsReply.getPid();
                    //查询此节点上一级是否还有其他子节点
                    List<BbsReply> dataList = baseMapper.selectList(new QueryWrapper<BbsReply>().eq("pid", pidVal).notIn("id", Arrays.asList(idArr)));
                    if ((dataList == null || dataList.size() == 0) && !Arrays.asList(idArr).contains(pidVal)
                            && !sb.toString().contains(pidVal)) {
                        //如果当前节点原本有子节点 现在木有了，更新状态
                        sb.append(pidVal).append(",");
                    }
                }
            }
            //批量删除节点
            baseMapper.deleteBatchIds(Arrays.asList(idArr));
            //修改已无子节点的标识
            String[] pidArr = sb.toString().split(",");
            for (String pid : pidArr) {
                this.updateOldParentNode(pid);
            }
        } else {
            BbsReply bbsReply = this.getById(id);
            if (bbsReply == null) {
                throw new JeecgBootException("未找到对应实体");
            }
            updateOldParentNode(bbsReply.getPid());
            baseMapper.deleteById(id);
        }
    }

    @Override
    public List<BbsReply> queryTreeListNoPage(QueryWrapper<BbsReply> queryWrapper) {
        List<BbsReply> dataList = baseMapper.selectList(queryWrapper);
        List<BbsReply> mapList = new ArrayList<>();
        for (BbsReply data : dataList) {
            String pidVal = data.getPid();
            //递归查询子节点的根节点
            if (pidVal != null && !"0".equals(pidVal)) {
                BbsReply rootVal = this.getTreeRoot(pidVal);
                if (rootVal != null && !mapList.contains(rootVal)) {
                    mapList.add(rootVal);
                }
            } else {
                if (!mapList.contains(data)) {
                    mapList.add(data);
                }
            }
        }
        return mapList;
    }

    /**
     * 根据所传pid查询旧的父级节点的子节点并修改相应状态值
     *
     * @param pid
     */
    private void updateOldParentNode(String pid) {
        if (!IBbsReplyService.ROOT_PID_VALUE.equals(pid)) {
            Integer count = baseMapper.selectCount(new QueryWrapper<BbsReply>().eq("pid", pid));
            if (count == null || count <= 1) {
                baseMapper.updateTreeNodeStatus(pid, IBbsReplyService.NOCHILD);
            }
        }
    }

    /**
     * 递归查询节点的根节点
     *
     * @param pidVal
     * @return
     */
    private BbsReply getTreeRoot(String pidVal) {
        BbsReply data = baseMapper.selectById(pidVal);
        if (data != null && !"0".equals(data.getPid())) {
            return this.getTreeRoot(data.getPid());
        } else {
            return data;
        }
    }

    /**
     * 根据id查询所有子节点id
     *
     * @param ids
     * @return
     */
    private String queryTreeChildIds(String ids) {
        //获取id数组
        String[] idArr = ids.split(",");
        StringBuffer sb = new StringBuffer();
        for (String pidVal : idArr) {
            if (pidVal != null) {
                if (!sb.toString().contains(pidVal)) {
                    if (sb.toString().length() > 0) {
                        sb.append(",");
                    }
                    sb.append(pidVal);
                    this.getTreeChildIds(pidVal, sb);
                }
            }
        }
        return sb.toString();
    }

    /**
     * 递归查询所有子节点
     *
     * @param pidVal
     * @param sb
     * @return
     */
    private StringBuffer getTreeChildIds(String pidVal, StringBuffer sb) {
        List<BbsReply> dataList = baseMapper.selectList(new QueryWrapper<BbsReply>().eq("pid", pidVal));
        if (dataList != null && dataList.size() > 0) {
            for (BbsReply tree : dataList) {
                if (!sb.toString().contains(tree.getId())) {
                    sb.append(",").append(tree.getId());
                }
                this.getTreeChildIds(tree.getId(), sb);
            }
        }
        return sb;
    }




    // ****行星万象修改位置戳****

    /**
     * 添加回复
     *
     * @param bbsReply
     * @return
     */
    @Override
    @Transactional
    public Result<?> addReply(BbsReply bbsReply) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        // 用户记录 day_reply_count +1 week_reply_count+1 maizi+5 最近一次评论时间更新 最近发布贴子时间更新
        BbsUserRecord bbsUserRecordOne = bbsUserRecordService.lambdaQuery().eq(BbsUserRecord::getCreateBy, sysUser.getUsername()).one();
        // 查询用户所在区域
        BbsRegion regionOne = bbsRegionService.lambdaQuery().eq(BbsRegion::getRegionCode, bbsUserRecordOne.getRegionCode()).one();
        // 判断评论数量是否超出限制
        if (bbsUserRecordOne.getDayPublishReply() >= regionOne.getDayPublishReply()) {
            return Result.error("今天已经评论" + regionOne.getDayPublishReply() + "次，超出限制，请等明天再次尝试！");
        } else if (bbsUserRecordOne.getWeekPublishReply() >= regionOne.getWeekPublishReply()) {
            return Result.error("本周已经评论" + regionOne.getWeekPublishReply() + "次，超出限制，请等下周再次尝试！");
        }

        //保存评论信息
        bbsReply.setCreateBy(sysUser.getUsername());
        bbsReply.setCreateTime(new Date());
        bbsReplyService.addBbsReply(bbsReply);
        //用户记录表更新
        boolean b1 = bbsUserRecordService.lambdaUpdate()
                .eq(BbsUserRecord::getCreateBy, sysUser.getUsername())
                .set(BbsUserRecord::getStoneCount, bbsUserRecordOne.getStoneCount() + 5)
                .set(BbsUserRecord::getDayPublishReply, bbsUserRecordOne.getDayPublishReply() + 1)  //当天评论数+1
                .set(BbsUserRecord::getWeekPublishReply, bbsUserRecordOne.getWeekPublishReply() + 1)  //本周评论数+1
                .update();
        //被评论用户接受消息 用户收到的回复表 插入数据
        BbsUserMessage bbsUserMessage = new BbsUserMessage();
        bbsUserMessage.setCreateBy(sysUser.getUsername());
        bbsUserMessage.setMessageType("3");
        BbsTopic one = bbsTopicService.lambdaQuery().eq(BbsTopic::getId, bbsReply.getTopicId()).one();
        bbsUserMessage.setReceiveUsername(one.getCreateBy());
        bbsUserMessage.setTopicId(bbsReply.getTopicId());
        bbsUserMessage.setReplyContent(bbsReply.getContent());
        bbsUserMessage.setRegionCode(bbsUserRecordOne.getRegionCode());
        //封装一张贴子图片
        List<BbsTopicImage> bbsTopicImageList = bbsTopicImageService.lambdaQuery().eq(BbsTopicImage::getTopicId, bbsReply.getTopicId()).list();
        if (bbsTopicImageList.size() != 0) {
            bbsUserMessage.setTopicImageUrl(bbsTopicImageList.get(0).getTopicImage());
        }

        //贴子评论数+1
        bbsTopicService.lambdaUpdate().eq(BbsTopic::getId, bbsReply.getTopicId()).set(BbsTopic::getReplyCount, one.getReplyCount() + 1).update();
        bbsUserMessageService.saveOrUpdate(bbsUserMessage);
        return Result.OK("添加成功！");
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBbsReplyWiseMini(String id) throws JeecgBootException {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        //删除所有点赞记录
        QueryWrapper<BbsUserPraise> bbsUserPraiseQueryWrapper = new QueryWrapper<>();
        bbsUserPraiseQueryWrapper.eq("reply_id", id);
        bbsUserPraiseService.remove(bbsUserPraiseQueryWrapper);

        //删除所有评论下所有用户互动信息
        QueryWrapper<BbsUserMessage> bbsUserMessageQueryWrapper = new QueryWrapper<>();
        bbsUserMessageQueryWrapper.eq("message_type", "4").eq("reply_id", id);
        bbsUserMessageService.remove(bbsUserMessageQueryWrapper);

        //查询选中节点下所有子节点一并删除
        id = this.queryTreeChildIds(id);
        if (id.indexOf(",") > 0) {
            StringBuffer sb = new StringBuffer();
            String[] idArr = id.split(",");
            for (String idVal : idArr) {
                if (idVal != null) {
                    BbsReply bbsReply = this.getById(idVal);
                    String pidVal = bbsReply.getPid();
                    //查询此节点上一级是否还有其他子节点
                    List<BbsReply> dataList = baseMapper.selectList(new QueryWrapper<BbsReply>().eq("pid", pidVal).notIn("id", Arrays.asList(idArr)));
                    if ((dataList == null || dataList.size() == 0) && !Arrays.asList(idArr).contains(pidVal)
                            && !sb.toString().contains(pidVal)) {
                        //如果当前节点原本有子节点 现在木有了，更新状态
                        sb.append(pidVal).append(",");
                    }
                }
            }
            //批量删除节点
            baseMapper.deleteBatchIds(Arrays.asList(idArr));
            //修改已无子节点的标识
            String[] pidArr = sb.toString().split(",");
            for (String pid : pidArr) {
                this.updateOldParentNode(pid);
            }
        } else {
            BbsReply bbsReply = this.getById(id);
            if (bbsReply == null) {
                throw new JeecgBootException("未找到对应实体");
            }
            updateOldParentNode(bbsReply.getPid());
            baseMapper.deleteById(id);
        }



        //用户陨石数量-5
        BbsUserRecord sysUserRecord = bbsUserRecordService.lambdaQuery().eq(BbsUserRecord::getCreateBy, sysUser.getUsername()).one();
        sysUserRecord.setStoneCount(sysUserRecord.getStoneCount() - 5);
        bbsUserRecordService.updateById(sysUserRecord);
    }

    @Override
    public int queryReplyCount(String sysOrgCode) {
        return bbsReplyService.lambdaQuery().eq(BbsReply::getSysOrgCode, sysOrgCode).count();
    }
}
