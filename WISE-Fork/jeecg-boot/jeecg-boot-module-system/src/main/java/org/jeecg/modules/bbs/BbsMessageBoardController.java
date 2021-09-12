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
import org.jeecg.common.aspect.annotation.PermissionData;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.bbs.BbsAuthController;
import org.jeecg.modules.bbs.entity.*;
import org.jeecg.modules.bbs.service.IBbsMessageBoardService;
import org.jeecg.modules.bbs.service.IBbsUserRecordService;
import org.jeecg.modules.bbs.service.impl.BbsRegionServiceImpl;
import org.jeecg.modules.bbs.service.impl.BbsUserPraiseServiceImpl;
import org.jeecg.modules.bbs.utils.ContentCheck;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.ISysUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Description: 留言板
 * @Author: jeecg-boot
 * @Date: 2021-05-26
 * @Version: V1.0
 */
@Api(tags = "留言板")
@RestController
@RequestMapping("/bbs/bbsMessageBoard")
@Slf4j
public class BbsMessageBoardController extends JeecgController<BbsMessageBoard, IBbsMessageBoardService> {
    @Autowired
    private BbsTopicController bbsAuthController;
    @Autowired
    private IBbsMessageBoardService bbsMessageBoardService;
    @Autowired
    private BbsUserPraiseServiceImpl bbsUserPraiseService;
    @Autowired
    private IBbsUserRecordService bbsUserRecordService;
    @Autowired
    private BbsRegionServiceImpl bbsRegionService;
    @Autowired
    private ISysUserService sysUserService;

    /**
     * 分页列表查询
     *
     * @param bbsMessageBoard
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "留言板-分页列表查询")
    @ApiOperation(value = "留言板-分页列表查询", notes = "留言板-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(BbsMessageBoard bbsMessageBoard,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<BbsMessageBoard> queryWrapper = QueryGenerator.initQueryWrapper(bbsMessageBoard, req.getParameterMap());
        Page<BbsMessageBoard> page = new Page<BbsMessageBoard>(pageNo, pageSize);
        IPage<BbsMessageBoard> pageList = bbsMessageBoardService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param bbsMessageBoard
     * @return
     */
    @AutoLog(value = "留言板-添加")
    @ApiOperation(value = "留言板-添加", notes = "留言板-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody BbsMessageBoard bbsMessageBoard) {
        bbsMessageBoardService.save(bbsMessageBoard);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param bbsMessageBoard
     * @return
     */
    @AutoLog(value = "留言板-编辑")
    @ApiOperation(value = "留言板-编辑", notes = "留言板-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody BbsMessageBoard bbsMessageBoard) {
        bbsMessageBoardService.updateById(bbsMessageBoard);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "留言板-通过id删除")
    @ApiOperation(value = "留言板-通过id删除", notes = "留言板-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        bbsMessageBoardService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "留言板-批量删除")
    @ApiOperation(value = "留言板-批量删除", notes = "留言板-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.bbsMessageBoardService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "留言板-通过id查询")
    @ApiOperation(value = "留言板-通过id查询", notes = "留言板-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        BbsMessageBoard bbsMessageBoard = bbsMessageBoardService.getById(id);
        if (bbsMessageBoard == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(bbsMessageBoard);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param bbsMessageBoard
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BbsMessageBoard bbsMessageBoard) {
        return super.exportXls(request, bbsMessageBoard, BbsMessageBoard.class, "留言板");
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
        return super.importExcel(request, response, BbsMessageBoard.class);
    }


    // ****行星万象修改位置戳****

    /**
     * 分页列表完整查询-
     *
     * @param bbsMessageBoard
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "留言板-分页列表完整查询")
    @ApiOperation(value = "留言板-分页列表完整查询-", notes = "留言板-分页列表完整查询")
    @GetMapping(value = "/wise/mini/fullList")
    public Result<?> fullList(BbsMessageBoard bbsMessageBoard,
                              @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                              @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                              HttpServletRequest req) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        /**
         * 1、查出分页records数据
         * 2、根据records的id字段查询用户留言点赞记录
         */
        QueryWrapper<BbsMessageBoard> queryWrapper = QueryGenerator.initQueryWrapper(bbsMessageBoard, req.getParameterMap()).orderByDesc("create_time");
        Page<BbsMessageBoard> page = new Page<BbsMessageBoard>(pageNo, pageSize);
        //添加区域筛选
        queryWrapper.eq("region_code", req.getHeader("regioncode"));
        IPage<BbsMessageBoard> pageList = bbsMessageBoardService.page(page, queryWrapper);

        //转换为完整留言板
        IPage<BbsMessageBoardFullDto> bbsMessageBoardFullDtoIPage = new Page<>();
        List<BbsMessageBoardFullDto> bbsMessageBoardFullDtoList = new ArrayList<>();
        for (BbsMessageBoard record : pageList.getRecords()) {
            SysUser userByName = sysUserService.getUserByName(record.getCreateBy());

            BbsMessageBoardFullDto bbsMessageBoardFullDto = new BbsMessageBoardFullDto();
            BeanUtils.copyProperties(record, bbsMessageBoardFullDto);

            bbsMessageBoardFullDto.setCreateByName(userByName.getRealname());
            bbsMessageBoardFullDto.setAvatar(userByName.getAvatar());
            bbsMessageBoardFullDto.setSex(userByName.getSex());

            bbsMessageBoardFullDtoList.add(bbsMessageBoardFullDto);
        }
        bbsMessageBoardFullDtoIPage.setRecords(bbsMessageBoardFullDtoList);
        bbsMessageBoardFullDtoIPage.setTotal(pageList.getTotal());
        bbsMessageBoardFullDtoIPage.setSize(pageList.getSize());
        bbsMessageBoardFullDtoIPage.setCurrent(pageList.getCurrent());

        /**
         * 根据帖子id和用户id获取用户点赞记录 判断用户对该留言是否点赞
         */
        for (BbsMessageBoardFullDto bbsMessageBoardFullDto : bbsMessageBoardFullDtoIPage.getRecords()) {
            BbsUserPraise bbsUserIsPraise = bbsUserPraiseService.lambdaQuery().eq(BbsUserPraise::getMessageId, bbsMessageBoardFullDto.getId()).eq(BbsUserPraise::getCreateBy, sysUser.getUsername()).one();
            if (null != bbsUserIsPraise) {
                bbsMessageBoardFullDto.setUserIsPraise(true);
            } else {
                bbsMessageBoardFullDto.setUserIsPraise(false);
            }
        }
        return Result.OK(bbsMessageBoardFullDtoIPage);
    }

    /**
     * 添加
     *
     * @param bbsMessageBoard
     * @return
     */
    @AutoLog(value = "留言板-添加")
    @ApiOperation(value = "留言板-添加", notes = "留言板-添加")
    @PostMapping(value = "/wise/mini/add")
    public Result<?> add(@RequestBody BbsMessageBoard bbsMessageBoard,
                         HttpServletRequest req) {

        if(!bbsAuthController.judgeMiniUserAuth()) {
            return Result.error(1000, "未授权,无法发布。");
        }
        //内容审核
        ContentCheck contentCheck = new ContentCheck();
        Result<?> result3 = contentCheck.checkBySensitiveWord(bbsMessageBoard.getContent());
        if (result3.isSuccess()) {
            Result<?> result4 = contentCheck.checkByWeiXin(bbsMessageBoard.getContent());
            if (!result4.isSuccess()) {
                result4.setMessage("内容" + result4.getMessage());
                return result4;
            }
        } else {
            result3.setMessage("内容" + result3.getMessage());
            return result3;
        }
        //获取用户信息
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        // 用户记录 day_reply_count +1 week_reply_count+1 maizi+5 最近一次评论时间更新 最近发布贴子时间更新
        BbsUserRecord bbsUserRecordOne = bbsUserRecordService.lambdaQuery().eq(BbsUserRecord::getCreateBy, sysUser.getUsername()).one();
        // 查询用户所在区域
        BbsRegion regionOne = bbsRegionService.lambdaQuery().eq(BbsRegion::getRegionCode, bbsUserRecordOne.getRegionCode()).one();
        // 判断评论数量是否超出限制
        if (bbsUserRecordOne.getDayPublishMessage() >= regionOne.getDayPublishMessage()) {
            return Result.error("今天已经留言" + regionOne.getDayPublishMessage() + "次，超出限制，请等明天再次尝试！");
        } else if (bbsUserRecordOne.getWeekPublishMessage() >= regionOne.getWeekPublishMessage()) {
            return Result.error("本周已经留言" + regionOne.getWeekPublishMessage() + "次，超出限制，请等下周再次尝试！");
        }

        //添加区域筛选
        bbsMessageBoard.setRegionCode(req.getHeader("regioncode"));
        bbsMessageBoardService.save(bbsMessageBoard);

        bbsUserRecordService.lambdaUpdate()
                .eq(BbsUserRecord::getCreateBy, sysUser.getUsername())
                .set(BbsUserRecord::getStoneCount, bbsUserRecordOne.getStoneCount() + 5)
                .set(BbsUserRecord::getMessageCount, bbsUserRecordOne.getMessageCount() + 1)    //留言数量
                .set(BbsUserRecord::getDayPublishMessage, bbsUserRecordOne.getDayPublishMessage() + 1)  //当天留言数+1
                .set(BbsUserRecord::getWeekPublishMessage, bbsUserRecordOne.getWeekPublishMessage() + 1)  //本周留言数+1
                .update();
        return Result.OK("添加成功！");
    }

    /**
     * 分页列表查询
     *
     * @param bbsMessageBoard
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "留言板-分页列表查询")
    @ApiOperation(value = "留言板-分页列表查询", notes = "留言板-分页列表查询")
    @GetMapping(value = "/wise/back/list")
    @PermissionData(pageComponent = "bbs/content/messageboard/BbsMessageBoardList")
    public Result<?> queryPageListWiseBack(BbsMessageBoard bbsMessageBoard,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<BbsMessageBoard> queryWrapper = QueryGenerator.initQueryWrapper(bbsMessageBoard, req.getParameterMap());
        Page<BbsMessageBoard> page = new Page<BbsMessageBoard>(pageNo, pageSize);
        IPage<BbsMessageBoard> pageList = bbsMessageBoardService.page(page, queryWrapper);

        //补全
        for (int i = 0; i < pageList.getRecords().size(); i++) {
            BbsMessageBoard bbsMessageBoard1 = pageList.getRecords().get(i);

            BbsRegion bbsRegion = bbsRegionService.lambdaQuery().eq(BbsRegion::getRegionCode, bbsMessageBoard1.getRegionCode()).one();
            SysUser userByName = sysUserService.getUserByName(bbsMessageBoard1.getCreateBy());

            bbsMessageBoard1.setRegionName(bbsRegion.getFullName());
            bbsMessageBoard1.setCreateByName(userByName.getRealname());
        }

        return Result.OK(pageList);
    }
}
