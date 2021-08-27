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
import org.jeecg.modules.bbs.controller.BbsTopicController;
import org.jeecg.modules.bbs.entity.*;
import org.jeecg.modules.bbs.service.IBbsRegionService;
import org.jeecg.modules.bbs.service.IBbsTopicFullDtoService;
import org.jeecg.modules.bbs.service.IBbsUserRecordService;
import org.jeecg.modules.bbs.service.IBbsUserSysMessageService;
import org.jeecg.modules.bbs.service.impl.BbsUserMessageServiceImpl;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @Description: 用户信息记录
 * @Author: jeecg-boot
 * @Date: 2021-05-27
 * @Version: V1.0
 */
@Api(tags = "用户信息记录")
@RestController
@RequestMapping("/bbs/bbsUserRecord")
@Slf4j
public class BbsUserRecordController extends JeecgController<BbsUserRecord, IBbsUserRecordService> {
    @Autowired
    private IBbsUserRecordService bbsUserRecordService;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private IBbsUserSysMessageService bbsUserSysMessageService;
    @Autowired
    private BbsUserMessageServiceImpl bbsUserMessageService;
    @Autowired
    private IBbsRegionService bbsRegionService;
    @Autowired
    private BbsTopicController bbsTopicController;
    @Autowired
    private IBbsTopicFullDtoService bbsTopicFullDtoService;


    /**
     * 分页列表查询
     *
     * @param bbsUserRecord
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "用户信息记录-分页列表查询")
    @ApiOperation(value = "用户信息记录-分页列表查询", notes = "用户信息记录-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(BbsUserRecord bbsUserRecord,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<BbsUserRecord> queryWrapper = QueryGenerator.initQueryWrapper(bbsUserRecord, req.getParameterMap());
        Page<BbsUserRecord> page = new Page<BbsUserRecord>(pageNo, pageSize);
        IPage<BbsUserRecord> pageList = bbsUserRecordService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param bbsUserRecord
     * @return
     */
    @AutoLog(value = "用户信息记录-添加")
    @ApiOperation(value = "用户信息记录-添加", notes = "用户信息记录-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody BbsUserRecord bbsUserRecord) {
        bbsUserRecordService.save(bbsUserRecord);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param bbsUserRecord
     * @return
     */
    @AutoLog(value = "用户信息记录-编辑")
    @ApiOperation(value = "用户信息记录-编辑", notes = "用户信息记录-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody BbsUserRecord bbsUserRecord) {
        bbsUserRecordService.updateById(bbsUserRecord);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "用户信息记录-通过id删除")
    @ApiOperation(value = "用户信息记录-通过id删除", notes = "用户信息记录-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        bbsUserRecordService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "用户信息记录-批量删除")
    @ApiOperation(value = "用户信息记录-批量删除", notes = "用户信息记录-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.bbsUserRecordService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "用户信息记录-通过id查询")
    @ApiOperation(value = "用户信息记录-通过id查询", notes = "用户信息记录-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        BbsUserRecord bbsUserRecord = bbsUserRecordService.getById(id);
        if (bbsUserRecord == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(bbsUserRecord);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param bbsUserRecord
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BbsUserRecord bbsUserRecord) {
        return super.exportXls(request, bbsUserRecord, BbsUserRecord.class, "用户信息记录");
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
        return super.importExcel(request, response, BbsUserRecord.class);
    }

    // ****行星万象修改位置戳****

    /**
     * 分页列表查询
     *
     * @param bbsUserRecord
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "用户信息记录-分页列表查询")
    @ApiOperation(value = "用户信息记录-分页列表查询", notes = "用户信息记录-分页列表查询")
    @GetMapping(value = "/wise/mini/list")
    public Result<?> queryPageListWiseMini(BbsUserRecord bbsUserRecordTemp,
                                           @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                           @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                           HttpServletRequest req) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        BbsUserRecord bbsUserRecord = bbsUserRecordService.lambdaQuery().eq(BbsUserRecord::getCreateBy, sysUser.getUsername()).one();

        if (null == bbsUserRecord) {
            return Result.error("用户信息记录不存在！");
        }


        /**未读收到的评论数量*/
        int count1 = bbsUserMessageService.lambdaQuery().eq(BbsUserMessage::getReceiveUsername, sysUser.getUsername())
                .eq(BbsUserMessage::getStatus, "1")
                .ne(BbsUserMessage::getCreateBy, sysUser.getUsername()).count();
        /**未读系统消息数量*/
        int count2 = bbsUserSysMessageService.lambdaQuery()
                .eq(BbsUserSysMessage::getReceiveUsername, sysUser.getUsername())
                .eq(BbsUserSysMessage::getStatus, "1")
                .in(BbsUserSysMessage::getRegionCode, bbsUserRecord.getRegionCode(), "all")
                .eq(BbsUserSysMessage::getStatus, "1")
                .count();

        bbsUserRecord.setUserMessageCount(count1);
        bbsUserRecord.setUserSysMessageCount(count2);

        //区域名
        BbsRegion bbsRegion = bbsRegionService.lambdaQuery().eq(BbsRegion::getRegionCode, bbsUserRecord.getRegionCode()).one();
        bbsUserRecord.setRegionFullName(bbsRegion.getFullName());

        bbsUserRecord.setCreateByName(sysUser.getRealname());
        bbsUserRecord.setAvatar(sysUser.getAvatar());
        bbsUserRecord.setSex(sysUser.getSex());

        return Result.OK(bbsUserRecord);
    }


    /**
     * 分页列表查询
     *
     * @param bbsUserRecord
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "用户个人及所有发布信息")
    @ApiOperation(value = "用户个人及所有发布信息", notes = "用户个人及所有发布信息")
    @GetMapping(value = "/wise/mini/userAllInfo")
    public Result<?> queryUserAllInfoWiseMini(@RequestParam(name = "username")String username,
                                              @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                              @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                              HttpServletRequest req) {

        BbsUserRecord bbsUserRecord = bbsUserRecordService.lambdaQuery().eq(BbsUserRecord::getCreateBy, username).one();

        if (null == bbsUserRecord) {
            return Result.error("用户信息记录不存在！");
        }

        /**未读收到的评论数量*/
        int count1 = bbsUserMessageService.lambdaQuery().eq(BbsUserMessage::getReceiveUsername, username)
                .eq(BbsUserMessage::getStatus, "1")
                .ne(BbsUserMessage::getCreateBy, username).count();
        /**未读系统消息数量*/
        int count2 = bbsUserSysMessageService.lambdaQuery()
                .eq(BbsUserSysMessage::getReceiveUsername, username)
                .eq(BbsUserSysMessage::getStatus, "1")
                .in(BbsUserSysMessage::getRegionCode, bbsUserRecord.getRegionCode(), "all")
                .eq(BbsUserSysMessage::getStatus, "1")
                .count();



        bbsUserRecord.setUserMessageCount(count1);
        bbsUserRecord.setUserSysMessageCount(count2);

        //区域名
        BbsRegion bbsRegion = bbsRegionService.lambdaQuery().eq(BbsRegion::getRegionCode, bbsUserRecord.getRegionCode()).one();
        bbsUserRecord.setRegionFullName(bbsRegion.getFullName());

        SysUser userByName = sysUserService.getUserByName(username);
        bbsUserRecord.setCreateByName(userByName.getRealname());
        bbsUserRecord.setAvatar(userByName.getAvatar());
        bbsUserRecord.setSex(userByName.getSex());


        //发布的贴子
        Page<BbsTopicFullDto> page = new Page<BbsTopicFullDto>(pageNo, pageSize);
        int[] topicType = new int[]{0, 1, 2};
        IPage<BbsTopicFullDto> bbsTopicFullDtos12 = bbsTopicFullDtoService.queryUserPublishTopicFullDto(page, req, topicType, username);

        bbsUserRecord.setBbsTopicFullDtoList(bbsTopicFullDtos12.getRecords());
        return Result.OK(bbsUserRecord);
    }

    /**
     * 分页列表查询
     *
     * @param bbsUserRecord
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "用户信息记录-分页列表查询")
    @ApiOperation(value = "用户信息记录-分页列表查询", notes = "用户信息记录-分页列表查询")
    @GetMapping(value = "/wise/back/list")
    public Result<?> queryPageListWiseBack(BbsUserRecord bbsUserRecord,
                                           @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                           @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                           HttpServletRequest req) {
        QueryWrapper<BbsUserRecord> queryWrapper = QueryGenerator.initQueryWrapper(bbsUserRecord, req.getParameterMap());
        Page<BbsUserRecord> page = new Page<BbsUserRecord>(pageNo, pageSize);
        IPage<BbsUserRecord> pageList = bbsUserRecordService.page(page, queryWrapper);

        //补全用户昵称、贴子内容
        for (int i = 0; i < pageList.getRecords().size(); i++) {
            BbsUserRecord userRecord = pageList.getRecords().get(i);

            SysUser userByName = sysUserService.getUserByName(userRecord.getCreateBy());
            BbsRegion bbsRegion = bbsRegionService.lambdaQuery().eq(BbsRegion::getRegionCode, userRecord.getRegionCode()).one();
            userRecord.setCreateByName(userByName.getRealname());
            userRecord.setRegionFullName(bbsRegion.getFullName());
        }

        return Result.OK(pageList);
    }


}
