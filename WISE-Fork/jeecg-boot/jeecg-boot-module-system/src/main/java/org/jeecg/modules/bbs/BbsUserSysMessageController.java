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
import org.jeecg.modules.bbs.entity.BbsUserSysMessage;
import org.jeecg.modules.bbs.service.IBbsUserSysMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @Description: 用户系统消息
 * @Author: jeecg-boot
 * @Date: 2021-02-28
 * @Version: V1.0
 */
@Api(tags = "用户系统消息")
@RestController
@RequestMapping("/bbs/bbsUserSysMessage")
@Slf4j
public class BbsUserSysMessageController extends JeecgController<BbsUserSysMessage, IBbsUserSysMessageService> {
    @Autowired
    private IBbsUserSysMessageService bbsUserSysMessageService;

    /**
     * 分页列表查询
     *
     * @param bbsUserSysMessage
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "用户系统消息-分页列表查询")
    @ApiOperation(value = "用户系统消息-分页列表查询", notes = "用户系统消息-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(BbsUserSysMessage bbsUserSysMessage,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<BbsUserSysMessage> queryWrapper = QueryGenerator.initQueryWrapper(bbsUserSysMessage, req.getParameterMap());
        Page<BbsUserSysMessage> page = new Page<BbsUserSysMessage>(pageNo, pageSize);
        IPage<BbsUserSysMessage> pageList = bbsUserSysMessageService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param bbsUserSysMessage
     * @return
     */
    @AutoLog(value = "用户系统消息-添加")
    @ApiOperation(value = "用户系统消息-添加", notes = "用户系统消息-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody BbsUserSysMessage bbsUserSysMessage) {
        bbsUserSysMessageService.save(bbsUserSysMessage);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param bbsUserSysMessage
     * @return
     */
    @AutoLog(value = "用户系统消息-编辑")
    @ApiOperation(value = "用户系统消息-编辑", notes = "用户系统消息-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody BbsUserSysMessage bbsUserSysMessage) {
        bbsUserSysMessageService.updateById(bbsUserSysMessage);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "用户系统消息-通过id删除")
    @ApiOperation(value = "用户系统消息-通过id删除", notes = "用户系统消息-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        bbsUserSysMessageService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "用户系统消息-批量删除")
    @ApiOperation(value = "用户系统消息-批量删除", notes = "用户系统消息-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.bbsUserSysMessageService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "用户系统消息-通过id查询")
    @ApiOperation(value = "用户系统消息-通过id查询", notes = "用户系统消息-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        BbsUserSysMessage bbsUserSysMessage = bbsUserSysMessageService.getById(id);
        if (bbsUserSysMessage == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(bbsUserSysMessage);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param bbsUserSysMessage
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BbsUserSysMessage bbsUserSysMessage) {
        return super.exportXls(request, bbsUserSysMessage, BbsUserSysMessage.class, "用户系统消息");
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
        return super.importExcel(request, response, BbsUserSysMessage.class);
    }

}
