package org.jeecg.modules.bbs;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.bbs.entity.BbsRegion;
import org.jeecg.modules.bbs.entity.BbsStatist;
import org.jeecg.modules.bbs.entity.BbsTopic;
import org.jeecg.modules.bbs.service.*;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.ISysDepartService;
import org.jeecg.modules.system.service.ISysLogService;
import org.jeecg.modules.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @Description: BBS_统计
 * @Author: jeecg-boot
 * @Date: 2020-12-10
 * @Version: V1.0
 */
@Api(tags = "BBS_统计")
@RestController
@RequestMapping("/bbs/BbsStatist")
@Slf4j
public class BbsStatistController extends JeecgController<BbsStatist, IBbsStatistService> {

    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private IBbsUserRecordService bbsUserRecordService;
    @Autowired
    private IBbsRegionService bbsRegionService;
    @Autowired
    private ISysDepartService sysDepartService;
    @Autowired
    private ISysLogService sysLogService;
    @Autowired
    private IBbsTopicService bbsTopicService;
    @Autowired
    private IBbsStatistService bbsStatistService;
    @Autowired
    private IBbsUserPraiseService bbsUserPraiseService;
    @Autowired
    private IBbsReplyService bbsReplyService;
    @Autowired
    private IBbsMessageBoardService bbsMessageBoardService;
    @Autowired
    private IBbsWaimaiUserService bbsWaimaiUserService;

    /**
     * 分页列表查询
     *
     * @param bbsStatist
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "统计-分页列表查询")
    @ApiOperation(value = "统计-分页列表查询", notes = "统计-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(BbsStatist bbsStatist,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<BbsStatist> queryWrapper = QueryGenerator.initQueryWrapper(bbsStatist, req.getParameterMap());
        Page<BbsStatist> page = new Page<BbsStatist>(pageNo, pageSize);
        IPage<BbsStatist> pageList = bbsStatistService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param bbsStatist
     * @return
     */
    @AutoLog(value = "统计-添加")
    @ApiOperation(value = "统计-添加", notes = "统计-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody BbsStatist bbsStatist) {
        bbsStatistService.save(bbsStatist);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param bbsStatist
     * @return
     */
    @AutoLog(value = "统计-编辑")
    @ApiOperation(value = "统计-编辑", notes = "统计-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody BbsStatist bbsStatist) {
        bbsStatistService.updateById(bbsStatist);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "统计-通过id删除")
    @ApiOperation(value = "统计-通过id删除", notes = "统计-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        bbsStatistService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "统计-批量删除")
    @ApiOperation(value = "统计-批量删除", notes = "统计-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.bbsStatistService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "统计-通过id查询")
    @ApiOperation(value = "统计-通过id查询", notes = "统计-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        BbsStatist bbsStatist = bbsStatistService.getById(id);
        if (bbsStatist == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(bbsStatist);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param bbsStatist
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BbsStatist bbsStatist) {
        return super.exportXls(request, bbsStatist, BbsStatist.class, "统计");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, BbsStatist.class);
    }


    // ****行星万象修改位置戳****

    /**
     * 统计当天数据
     *
     * @param sysOrgCode
     * @return
     */
    public Boolean todayCountStatist(String sysOrgCode) {
        //判断区域是否上线
        BbsRegion one = bbsRegionService.lambdaQuery()
                .eq(BbsRegion::getRegionStatus, 2)
                .eq(BbsRegion::getSysOrgCode, sysOrgCode)
                .one();
        if (null == one) {
            return false;
        }


        JSONObject statistData = bbsStatistService.yesterdayCountInfo(sysOrgCode);

        //上线用户数
        int userOnlineCount = sysLogService.queryOnlineUserDayCountInfo(sysOrgCode);
        //用户数
        int userCount = sysUserService.lambdaQuery().eq(SysUser::getOrgCode, sysOrgCode).count();
        //新增用户数 = 用户数 - 昨日用户数
        int newUserCount = userCount - (int) statistData.get("userCount");

        //贴子数
        int topicCount = bbsTopicService.lambdaQuery()
                .eq(BbsTopic::getSysOrgCode, sysOrgCode)
                .count();
        //新增帖子数 = 贴子数 - 昨天帖子数
        int newTopicCount = topicCount - (int) statistData.get("topicCount");

        //帖子点赞数
        int topicPraiseCount = bbsUserPraiseService.queryTopicPraiseCount(sysOrgCode);
        //新增帖子点赞数
        int newTopicPraiseCount = topicPraiseCount - (int) statistData.get("topicPraiseCount");

        //贴子评论数
        int replyCount = bbsReplyService.queryReplyCount(sysOrgCode);
        //新增评论数 = 贴子评论数 - 昨日评论数
        int newReplyCount = replyCount - (int) statistData.get("replyCount");

        //留言数
        int messageBoardCount = bbsMessageBoardService.queryMessageBoardCount(sysOrgCode);
        //新增留言数 = 留言数 - 昨日留言数
        int newMessageBoardCount = messageBoardCount - (int) statistData.get("messageBoardCount");

        //留言点赞数
        int messageBoardPraiseCount = bbsUserPraiseService.queryMessageBoardPraiseCount(sysOrgCode);
        //新增留言点赞数 = 留言点赞数 - 昨日留言点赞数
        int newMessageBoardPraiseCount = messageBoardPraiseCount - (int) statistData.get("messageBoardPraiseCount");

        //外卖点击数
        int waimaiUserCount = bbsWaimaiUserService.queryWaimaiUserCount(sysOrgCode);
        //新增外卖点击数 = 外卖点击数 - 昨日外卖点击数
        int newWaimaiUserCount = waimaiUserCount - (int) statistData.get("waimaiUserCount");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userOnlineCount", userOnlineCount);
        jsonObject.put("userCount", userCount);
        jsonObject.put("newUserCount", newUserCount);
        jsonObject.put("topicCount", topicCount);
        jsonObject.put("newTopicCount", newTopicCount);
        jsonObject.put("topicPraiseCount", topicPraiseCount);
        jsonObject.put("newTopicPraiseCount", newTopicPraiseCount);
        jsonObject.put("replyCount", replyCount);
        jsonObject.put("newReplyCount", newReplyCount);
        jsonObject.put("messageBoardCount", messageBoardCount);
        jsonObject.put("newMessageBoardCount", newMessageBoardCount);
        jsonObject.put("messageBoardPraiseCount", messageBoardPraiseCount);
        jsonObject.put("newMessageBoardPraiseCount", newMessageBoardPraiseCount);
        jsonObject.put("waimaiUserCount", waimaiUserCount);
        jsonObject.put("newWaimaiUserCount", newWaimaiUserCount);

        boolean i = bbsStatistService.insertDayStatistData(jsonObject, sysOrgCode);
        return i;
    }

    @GetMapping("/wise/back/getDayCountInfo")
    @AutoLog(value = "按天统计")
    @ApiOperation(value = "按天统计", notes = "按天统计，定时任务每天跑一次，用户请求一次调一次")
    public Result<?> getDayCountInfo(@RequestParam(value = "sysOrgCode") String sysOrgCode,
                                     @RequestParam(value = "startTime") String startTime,
                                     @RequestParam(value = "endTime") String endTime) throws Exception {
        if (null == sysOrgCode) {
            return Result.OK("部门编码不能为空！");
        }
        Boolean statistSuccess = this.todayCountStatist(sysOrgCode);
        if (statistSuccess) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date parseStartTime = sdf.parse(startTime);
            Date parseEndTime = sdf.parse(endTime);

            List<BbsStatist> list = bbsStatistService.lambdaQuery()
                    .and(l -> l.gt(BbsStatist::getCreateTime, parseStartTime)
                            .lt(BbsStatist::getCreateTime, parseEndTime))
                    .eq(BbsStatist::getName, "dayStatistData-" + sysOrgCode)
                    .list();
            return Result.OK(list);
        } else {
            return Result.error("统计出错！");
        }
    }

    @GetMapping("/wise/back/getAllCountInfo")
    @AutoLog(value = "总览统计")
    @ApiOperation(value = "总览统计", notes = "总览统计，定时任务每天跑一次，用户请求一次调一次")
    public Result<?> getAllCountInfo(@RequestParam(value = "sysOrgCode") String sysOrgCode) throws Exception {
        if (null == sysOrgCode) {
            return Result.OK("部门编码不能为空！");
        }
        Boolean statistSuccess = this.todayCountStatist(sysOrgCode);
        if (statistSuccess) {
            List<BbsStatist> list = bbsStatistService.lambdaQuery()
                    .eq(BbsStatist::getName, "dayStatistData-" + sysOrgCode)
                    .list();
            return Result.OK(list);
        } else {
            return Result.error("统计出错！");
        }
    }

}
