package org.jeecg.modules.bbs;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.bbs.entity.BbsUserMessage;
import org.jeecg.modules.bbs.entity.BbsUserMessageFullDto;
import org.jeecg.modules.bbs.service.IBbsUserMessageFullDtoService;
import org.jeecg.modules.bbs.service.IBbsUserMessageService;
import org.jeecg.modules.bbs.utils.BbsAuthUtils;
import org.jeecg.modules.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @Description: 互动消息
 * @Author: jeecg-boot
 * @Date: 2021-01-22
 * @Version: V1.0
 */
@Api(tags = "互动消息")
@RestController
@RequestMapping("/bbs/bbsUserMessage")
@Slf4j
public class BbsUserMessageController extends JeecgController<BbsUserMessage, IBbsUserMessageService> {
    @Autowired
    private IBbsUserMessageService bbsUserMessageService;
    @Autowired
    private IBbsUserMessageFullDtoService bbsUserMessageFullDtoService;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private BbsAuthController bbsAuthController;
    @Autowired
    private BbsAuthUtils bbsAuthUtils;

    /**
     * 添加
     *
     * @param bbsUserMessage
     * @return
     */
    @AutoLog(value = "互动消息-添加")
    @ApiOperation(value = "互动消息-添加", notes = "互动消息-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody BbsUserMessage bbsUserMessage) {
        bbsUserMessageService.save(bbsUserMessage);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param bbsUserMessage
     * @return
     */
    @AutoLog(value = "互动消息-编辑")
    @ApiOperation(value = "互动消息-编辑", notes = "互动消息-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody BbsUserMessage bbsUserMessage) {
        bbsUserMessageService.updateById(bbsUserMessage);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "互动消息-通过id删除")
    @ApiOperation(value = "互动消息-通过id删除", notes = "互动消息-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        bbsUserMessageService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "互动消息-批量删除")
    @ApiOperation(value = "互动消息-批量删除", notes = "互动消息-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.bbsUserMessageService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "互动消息-通过id查询")
    @ApiOperation(value = "互动消息-通过id查询", notes = "互动消息-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        BbsUserMessage bbsUserMessage = bbsUserMessageService.getById(id);
        if (bbsUserMessage == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(bbsUserMessage);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param bbsUserMessage
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BbsUserMessage bbsUserMessage) {
        return super.exportXls(request, bbsUserMessage, BbsUserMessage.class, "互动消息");
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
        return super.importExcel(request, response, BbsUserMessage.class);
    }


    // ****行星万象修改位置戳****

    /**
     * 分页列表查询
     *
     * @param bbsUserMessage
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "互动消息-分页列表查询")
    @ApiOperation(value = "互动消息-分页列表查询", notes = "互动消息-分页列表查询")
    @GetMapping(value = "/wise/mini/fullList")
    public Result<?> queryPageList(BbsUserMessage bbsUserMessage,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        Page<BbsUserMessageFullDto> page = new Page<>(pageNo, pageSize);
        IPage<BbsUserMessageFullDto> bbsUserMessageIPage = bbsUserMessageFullDtoService.queryUserMessageFullList(page);

        //IPage<BbsUserMessage> pageList = bbsUserMessageService.page(page);

        //加载后将消息状态设置为已读
        UpdateWrapper<BbsUserMessage> userMessageUrapper = new UpdateWrapper<>();
        userMessageUrapper.eq("receive_username", sysUser.getUsername()).set("status", "0");
        bbsUserMessageService.update(userMessageUrapper);
        //查询后将状态置为已读
        bbsAuthUtils.getMiNiStorageFromSql(sysUser.getUsername());
        return Result.OK(bbsUserMessageIPage);
    }
}
