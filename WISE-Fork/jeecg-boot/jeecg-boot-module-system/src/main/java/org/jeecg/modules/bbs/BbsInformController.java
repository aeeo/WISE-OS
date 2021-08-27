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
import org.jeecg.modules.bbs.entity.*;
import org.jeecg.modules.bbs.service.*;
import org.jeecg.modules.bbs.service.impl.BbsClassServiceImpl;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @Description: 举报列表
 * @Author: jeecg-boot
 * @Date: 2021-05-26
 * @Version: V1.0
 */
@Api(tags = "举报列表")
@RestController
@RequestMapping("/bbs/bbsInform")
@Slf4j
public class BbsInformController extends JeecgController<BbsInform, IBbsInformService> {
    @Autowired
    private IBbsInformService bbsInformService;
    @Autowired
    private IBbsTopicService bbsTopicService;
    @Autowired
    private IBbsReplyService bbsReplyService;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private IBbsMessageBoardService bbsMessageBoardService;
    @Autowired
    private IBbsRegionService bbsRegionService;
    @Autowired
    private BbsClassServiceImpl bbsClassService;

    /**
     * 分页列表查询
     *
     * @param bbsInform
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "举报列表-分页列表查询")
    @ApiOperation(value = "举报列表-分页列表查询", notes = "举报列表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(BbsInform bbsInform,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<BbsInform> queryWrapper = QueryGenerator.initQueryWrapper(bbsInform, req.getParameterMap());
        Page<BbsInform> page = new Page<BbsInform>(pageNo, pageSize);
        IPage<BbsInform> pageList = bbsInformService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param bbsInform
     * @return
     */
    @AutoLog(value = "举报列表-添加")
    @ApiOperation(value = "举报列表-添加", notes = "举报列表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody BbsInform bbsInform) {
        bbsInformService.save(bbsInform);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param bbsInform
     * @return
     */
    @AutoLog(value = "举报列表-编辑")
    @ApiOperation(value = "举报列表-编辑", notes = "举报列表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody BbsInform bbsInform) {
        bbsInformService.updateById(bbsInform);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "举报列表-通过id删除")
    @ApiOperation(value = "举报列表-通过id删除", notes = "举报列表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        bbsInformService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "举报列表-批量删除")
    @ApiOperation(value = "举报列表-批量删除", notes = "举报列表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.bbsInformService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "举报列表-通过id查询")
    @ApiOperation(value = "举报列表-通过id查询", notes = "举报列表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        BbsInform bbsInform = bbsInformService.getById(id);
        if (bbsInform == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(bbsInform);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param bbsInform
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BbsInform bbsInform) {
        return super.exportXls(request, bbsInform, BbsInform.class, "举报列表");
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
        return super.importExcel(request, response, BbsInform.class);
    }


    // ****行星万象修改位置戳****

    /**
     * 举报贴子
     *
     * @param bbsInform
     * @return
     */
    @AutoLog(value = "举报列表-举报贴子")
    @ApiOperation(value = "举报列表-举报贴子", notes = "举报列表-举报贴子")
    @PostMapping(value = "/wise/mini/informTopic")
    public Result<?> informTopic(@RequestBody BbsInform bbsInform) {

        return bbsInformService.informTopic(bbsInform);

    }

    /**
     * 举报评论
     *
     * @return
     */
    @AutoLog(value = "举报评论")
    @ApiOperation(value = "举报评论", notes = "举报评论")
    @PostMapping(value = "/wise/mini/informReply")
    public Result<?> informReply(@RequestBody BbsInform bbsInform) {
        return bbsInformService.informReply(bbsInform);

    }

    /**
     * 分页列表查询
     *
     * @param bbsInform
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "举报列表-分页列表查询")
    @ApiOperation(value = "举报列表-分页列表查询", notes = "举报列表-分页列表查询")
    @GetMapping(value = "/wise/back/list")
    public Result<?> queryPageListWiseBack(BbsInform bbsInform,
                                           @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                           @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                           HttpServletRequest req) {
        QueryWrapper<BbsInform> queryWrapper = QueryGenerator.initQueryWrapper(bbsInform, req.getParameterMap());
        Page<BbsInform> page = new Page<BbsInform>(pageNo, pageSize);
        IPage<BbsInform> pageList = bbsInformService.page(page, queryWrapper);

        //补全
        for (int i = 0; i < pageList.getRecords().size(); i++) {
            //1：举报贴子<br />2：举报评论<br />3：举报留言
            BbsInform bbsInform1 = pageList.getRecords().get(i);

            SysUser userByName = sysUserService.getUserByName(bbsInform1.getCreateBy());
            SysUser beUserByName = sysUserService.getUserByName((bbsInform1.getBeInformUsername()));
            bbsInform1.setCreateByName(userByName.getRealname());
            bbsInform1.setInformUsernameRealname((userByName.getRealname()));
            bbsInform1.setBeInformUsernameRealname(beUserByName.getRealname());

            if (bbsInform1.getType() == 1) {
                //帖子有区域
                BbsTopic bbsTopic = bbsTopicService.getById(bbsInform1.getTopicId());
                BbsClass bbsClass = bbsClassService.lambdaQuery()
                        .eq(BbsClass::getClassCode, bbsTopic.getClassCode())
                        .eq(BbsClass::getRegionCode, bbsTopic.getRegionCode())
                        .one();
                bbsInform1.setTopicTitle(bbsTopic.getTitle());
                bbsInform1.setTopicContent(bbsTopic.getContent());
                bbsInform1.setRegionCode(bbsClass.getRegionCode());
                bbsInform1.setRegionName(bbsTopic.getRegionFullName());
                bbsInform1.setClassCode(bbsClass.getClassCode());
                bbsInform1.setClassName(bbsClass.getClassName());
            } else if (bbsInform1.getType() == 2) {
                //评论也有区域
                BbsTopic bbsTopic = bbsTopicService.getById(bbsInform1.getTopicId());
                BbsClass bbsClass = bbsClassService.lambdaQuery()
                        .eq(BbsClass::getClassCode, bbsTopic.getClassCode())
                        .eq(BbsClass::getRegionCode, bbsTopic.getRegionCode())
                        .one();

                BbsReply bbsReply = bbsReplyService.getById(bbsInform1.getReplyId());
                bbsInform1.setReplyContent(bbsReply.getContent());

                bbsInform1.setRegionCode(bbsClass.getRegionCode());
                bbsInform1.setRegionName(bbsTopic.getRegionFullName());
                bbsInform1.setClassCode(bbsClass.getClassCode());
                bbsInform1.setClassName(bbsClass.getClassName());

            } else if (bbsInform1.getType() == 3) {
                BbsMessageBoard messageBoard = bbsMessageBoardService.getById(bbsInform1.getMessageId());
                BbsRegion bbsRegion = bbsRegionService.lambdaQuery().eq(BbsRegion::getRegionCode, messageBoard.getRegionCode()).one();
                bbsInform1.setMessageContent(messageBoard.getContent());
                bbsInform1.setRegionCode(bbsRegion.getRegionCode());
                bbsInform1.setRegionName(bbsRegion.getFullName());
            }
        }
        return Result.OK(pageList);
    }

}
