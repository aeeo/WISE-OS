package org.jeecg.modules.bbs;

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
import org.jeecg.modules.bbs.entity.BbsSysMessage;
import org.jeecg.modules.bbs.entity.BbsUserRecord;
import org.jeecg.modules.bbs.service.IBbsSysMessageService;
import org.jeecg.modules.bbs.service.IBbsUserRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @Description: 系统消息
 * @Author: jeecg-boot
 * @Date: 2021-02-28
 * @Version: V1.0
 */
@Api(tags = "系统消息")
@RestController
@RequestMapping("/bbs/bbsSysMessage")
@Slf4j
public class BbsSysMessageController extends JeecgController<BbsSysMessage, IBbsSysMessageService> {
    @Autowired
    private IBbsSysMessageService bbsSysMessageService;
    @Autowired
    private IBbsUserRecordService bbsUserRecordService;

    /**
     * 分页列表查询
     *
     * @param bbsSysMessage
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "系统消息-分页列表查询")
    @ApiOperation(value = "系统消息-分页列表查询", notes = "系统消息-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(BbsSysMessage bbsSysMessage,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<BbsSysMessage> queryWrapper = QueryGenerator.initQueryWrapper(bbsSysMessage, req.getParameterMap());
        Page<BbsSysMessage> page = new Page<BbsSysMessage>(pageNo, pageSize);
        IPage<BbsSysMessage> pageList = bbsSysMessageService.page(page, queryWrapper);
        return Result.OK(pageList);
    }


    ///**
    // *   添加
    // *
    // * @param bbsSysMessage
    // * @return
    // */
    //@AutoLog(value = "系统消息-添加")
    //@ApiOperation(value="系统消息-添加", notes="系统消息-添加")
    //@PostMapping(value = "/add")
    //public Result<?> add(@RequestBody BbsSysMessage bbsSysMessage) {
    //    bbsSysMessageService.save(bbsSysMessage);
    //    return Result.OK("添加成功！");
    //}

    /**
     * 编辑
     *
     * @param bbsSysMessage
     * @return
     */
    @AutoLog(value = "系统消息-编辑")
    @ApiOperation(value = "系统消息-编辑", notes = "系统消息-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody BbsSysMessage bbsSysMessage) {
        bbsSysMessageService.updateById(bbsSysMessage);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "系统消息-通过id删除")
    @ApiOperation(value = "系统消息-通过id删除", notes = "系统消息-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        bbsSysMessageService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "系统消息-批量删除")
    @ApiOperation(value = "系统消息-批量删除", notes = "系统消息-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.bbsSysMessageService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "系统消息-通过id查询")
    @ApiOperation(value = "系统消息-通过id查询", notes = "系统消息-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        BbsSysMessage bbsSysMessage = bbsSysMessageService.getById(id);
        if (bbsSysMessage == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(bbsSysMessage);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param bbsSysMessage
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BbsSysMessage bbsSysMessage) {
        return super.exportXls(request, bbsSysMessage, BbsSysMessage.class, "系统消息");
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
        return super.importExcel(request, response, BbsSysMessage.class);
    }


// ****行星万象修改位置戳****


    /**
     * 用户系统消息
     *
     * @param bbsSysMessage
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "用户系统消息-分页列表查询")
    @ApiOperation(value = "系统消息-分页列表查询", notes = "系统消息-分页列表查询")
    @GetMapping(value = "/wise/mini/queryUserMessageList")
    public Result<?> queryUserMessageList(BbsSysMessage bbsSysMessage,
                                          @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                          HttpServletRequest req) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        BbsUserRecord bbsUserRecord = bbsUserRecordService.lambdaQuery().eq(BbsUserRecord::getCreateBy, sysUser.getUsername()).one();
        Page<BbsSysMessage> page = new Page<>(pageNo, pageSize);
        Result<?> resultPage = bbsSysMessageService.queryUserMessageList(page, sysUser.getUsername(), bbsUserRecord.getRegionCode());

        return resultPage;
    }

    /**
     * 添加
     *
     * @param bbsSysMessage
     * @return
     */
    @AutoLog(value = "系统消息-添加")
    @ApiOperation(value = "系统消息-添加", notes = "系统消息-添加")
    @PostMapping(value = "/wise/back/add")
    public Result<?> add(@RequestBody BbsSysMessage bbsSysMessage) {
        bbsSysMessageService.addSysMessage(bbsSysMessage);
        return Result.OK("添加成功！");
    }
}
