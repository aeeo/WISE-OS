package org.jeecg.modules.bbs;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.bbs.entity.BbsUserPraise;
import org.jeecg.modules.bbs.service.IBbsUserPraiseService;
import org.jeecg.modules.bbs.service.impl.BbsMessageBoardServiceImpl;
import org.jeecg.modules.bbs.service.impl.BbsTopicServiceImpl;
import org.jeecg.modules.bbs.service.impl.BbsUserRecordServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @Description: 用户点赞记录表
 * @Author: jeecg-boot
 * @Date: 2021-01-07
 * @Version: V1.0
 */
@Api(tags = "用户点赞记录表")
@RestController
@RequestMapping("/bbs/bbsUserPraise")
@Slf4j
public class BbsUserPraiseController extends JeecgController<BbsUserPraise, IBbsUserPraiseService> {
    @Autowired
    private IBbsUserPraiseService bbsUserPraiseService;
    @Autowired
    private BbsTopicServiceImpl bbsTopicService;
    @Autowired
    private BbsUserRecordServiceImpl bbsUserRecordService;
    @Autowired
    private BbsMessageBoardServiceImpl bbsMessageBoardService;


    /**
     * 分页列表查询
     *
     * @param bbsUserPraise
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "用户点赞记录表-分页列表查询")
    @ApiOperation(value = "用户点赞记录表-分页列表查询", notes = "用户点赞记录表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(BbsUserPraise bbsUserPraise,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<BbsUserPraise> queryWrapper = QueryGenerator.initQueryWrapper(bbsUserPraise, req.getParameterMap());
        Page<BbsUserPraise> page = new Page<BbsUserPraise>(pageNo, pageSize);
        IPage<BbsUserPraise> pageList = bbsUserPraiseService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param bbsUserPraise
     * @return
     */
    @AutoLog(value = "用户点赞记录表-添加")
    @ApiOperation(value = "用户点赞记录表-添加", notes = "用户点赞记录表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody BbsUserPraise bbsUserPraise) {
        bbsUserPraiseService.save(bbsUserPraise);
        return Result.OK("添加成功！");
    }


    /**
     * 编辑
     *
     * @param bbsUserPraise
     * @return
     */
    @AutoLog(value = "用户点赞记录表-编辑")
    @ApiOperation(value = "用户点赞记录表-编辑", notes = "用户点赞记录表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody BbsUserPraise bbsUserPraise) {
        bbsUserPraiseService.updateById(bbsUserPraise);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "用户点赞记录表-通过id删除")
    @ApiOperation(value = "用户点赞记录表-通过id删除", notes = "用户点赞记录表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        bbsUserPraiseService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "用户点赞记录表-批量删除")
    @ApiOperation(value = "用户点赞记录表-批量删除", notes = "用户点赞记录表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.bbsUserPraiseService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "用户点赞记录表-通过id查询")
    @ApiOperation(value = "用户点赞记录表-通过id查询", notes = "用户点赞记录表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        BbsUserPraise bbsUserPraise = bbsUserPraiseService.getById(id);
        if (bbsUserPraise == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(bbsUserPraise);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param bbsUserPraise
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BbsUserPraise bbsUserPraise) {
        return super.exportXls(request, bbsUserPraise, BbsUserPraise.class, "用户点赞记录表");
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
        return super.importExcel(request, response, BbsUserPraise.class);
    }


    // ****行星万象修改位置戳****

    /**
     * 点赞/取消赞
     *
     * @param topicId
     * @return
     */
    @AutoLog(value = "用户点赞/取消赞")
    @ApiOperation(value = "用户点赞/取消赞", notes = "用户点赞/取消赞")
    @PostMapping(value = "/wise/mini/clickPraise")
    public Result<?> clickPraise(String topicId, @RequestParam(value = "isPraise") Boolean isPraise, String messageId) {
        return bbsUserPraiseService.clickPraise(topicId, isPraise, messageId);
    }

    /**
     * 评论点赞/取消赞
     *
     * @param replyId
     * @return
     */
    @AutoLog(value = "评论点赞/取消赞")
    @ApiOperation(value = "评论点赞/取消赞", notes = "评论点赞/取消赞")
    @PostMapping(value = "/wise/mini/clickReplyPraise")
    public Result<?> clickReplyPraise(String replyId, @RequestParam(value = "isPraise") Boolean isPraise) {
        return bbsUserPraiseService.clickReplyPraise(replyId, isPraise);
    }
}
