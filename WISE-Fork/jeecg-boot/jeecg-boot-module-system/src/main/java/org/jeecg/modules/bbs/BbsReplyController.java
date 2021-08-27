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
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.bbs.entity.BbsReply;
import org.jeecg.modules.bbs.entity.BbsTopic;
import org.jeecg.modules.bbs.entity.BbsUserPraise;
import org.jeecg.modules.bbs.service.IBbsReplyService;
import org.jeecg.modules.bbs.service.IBbsTopicService;
import org.jeecg.modules.bbs.service.impl.BbsInformServiceImpl;
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
import java.util.Comparator;
import java.util.List;

/**
 * @Description: 帖子回复
 * @Author: jeecg-boot
 * @Date: 2021-05-26
 * @Version: V1.0
 */
@Api(tags = "帖子回复")
@RestController
@RequestMapping("/bbs/bbsReply")
@Slf4j
public class BbsReplyController extends JeecgController<BbsReply, IBbsReplyService> {
    @Autowired
    private IBbsReplyService bbsReplyService;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private BbsUserPraiseServiceImpl bbsUserPraiseService;
    @Autowired
    private BbsInformServiceImpl bbsInformService;
    @Autowired
    private IBbsTopicService bbsTopicService;


    /**
     * 分页列表查询
     *
     * @param bbsReply
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "帖子回复-分页列表查询")
    @ApiOperation(value = "帖子回复-分页列表查询", notes = "帖子回复-分页列表查询")
    @GetMapping(value = "/rootList")
    public Result<?> queryPageList(BbsReply bbsReply,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        String hasQuery = req.getParameter("hasQuery");
        if (hasQuery != null && "true".equals(hasQuery)) {
            QueryWrapper<BbsReply> queryWrapper = QueryGenerator.initQueryWrapper(bbsReply, req.getParameterMap());
            List<BbsReply> list = bbsReplyService.queryTreeListNoPage(queryWrapper);
            IPage<BbsReply> pageList = new Page<>(1, 10, list.size());
            pageList.setRecords(list);
            return Result.OK(pageList);
        } else {
            String parentId = bbsReply.getPid();
            if (oConvertUtils.isEmpty(parentId)) {
                parentId = "0";
            }
            bbsReply.setPid(null);
            QueryWrapper<BbsReply> queryWrapper = QueryGenerator.initQueryWrapper(bbsReply, req.getParameterMap());
            // 使用 eq 防止模糊查询
            queryWrapper.eq("pid", parentId);
            Page<BbsReply> page = new Page<BbsReply>(pageNo, pageSize);
            IPage<BbsReply> pageList = bbsReplyService.page(page, queryWrapper);
            return Result.OK(pageList);
        }
    }

    /**
     * 获取子数据
     *
     * @param bbsReply
     * @param req
     * @return
     */
    @AutoLog(value = "帖子回复-获取子数据")
    @ApiOperation(value = "帖子回复-获取子数据", notes = "帖子回复-获取子数据")
    @GetMapping(value = "/childList")
    public Result<?> queryPageList(BbsReply bbsReply, HttpServletRequest req) {
        QueryWrapper<BbsReply> queryWrapper = QueryGenerator.initQueryWrapper(bbsReply, req.getParameterMap());
        List<BbsReply> list = bbsReplyService.list(queryWrapper);
        IPage<BbsReply> pageList = new Page<>(1, 10, list.size());
        pageList.setRecords(list);
        return Result.OK(pageList);
    }

    /**
     * 批量查询子节点
     *
     * @param parentIds 父ID（多个采用半角逗号分割）
     * @param parentIds
     * @return
     */
    @AutoLog(value = "帖子回复-批量获取子数据")
    @ApiOperation(value = "帖子回复-批量获取子数据", notes = "帖子回复-批量获取子数据")
    @GetMapping("/getChildListBatch")
    public Result getChildListBatch(@RequestParam("parentIds") String parentIds) {
        try {
            QueryWrapper<BbsReply> queryWrapper = new QueryWrapper<>();
            List<String> parentIdList = Arrays.asList(parentIds.split(","));
            queryWrapper.in("pid", parentIdList);
            List<BbsReply> list = bbsReplyService.list(queryWrapper);
            IPage<BbsReply> pageList = new Page<>(1, 10, list.size());
            pageList.setRecords(list);
            return Result.OK(pageList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error("批量查询子节点失败：" + e.getMessage());
        }
    }

    /**
     * 添加
     *
     * @param bbsReply
     * @return
     */
    @AutoLog(value = "帖子回复-添加")
    @ApiOperation(value = "帖子回复-添加", notes = "帖子回复-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody BbsReply bbsReply) {
        bbsReplyService.addBbsReply(bbsReply);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param bbsReply
     * @return
     */
    @AutoLog(value = "帖子回复-编辑")
    @ApiOperation(value = "帖子回复-编辑", notes = "帖子回复-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody BbsReply bbsReply) {
        bbsReplyService.updateBbsReply(bbsReply);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "帖子回复-通过id删除")
    @ApiOperation(value = "帖子回复-通过id删除", notes = "帖子回复-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        bbsReplyService.deleteBbsReply(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "帖子回复-批量删除")
    @ApiOperation(value = "帖子回复-批量删除", notes = "帖子回复-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.bbsReplyService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "帖子回复-通过id查询")
    @ApiOperation(value = "帖子回复-通过id查询", notes = "帖子回复-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        BbsReply bbsReply = bbsReplyService.getById(id);
        if (bbsReply == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(bbsReply);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param bbsReply
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BbsReply bbsReply) {
        return super.exportXls(request, bbsReply, BbsReply.class, "帖子回复");
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
        return super.importExcel(request, response, BbsReply.class);
    }


    // ****行星万象修改位置戳****

    /**
     * 分页列表查询
     *
     * @param bbsReply
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "帖子回复-完整分页列表查询")
    @ApiOperation(value = "帖子回复-完整分页列表查询", notes = "帖子回复-完整分页列表查询")
    @PostMapping(value = "/wise/mini/rootFullList")
    public Result<?> queryFullPageList(@RequestBody BbsReply bbsReply,
                                       @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                       @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                       HttpServletRequest req) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        QueryWrapper<BbsReply> queryWrapper = QueryGenerator.initQueryWrapper(bbsReply, req.getParameterMap()).orderByDesc("create_time");
        // 使用 eq 防止模糊查询
        queryWrapper.eq("pid", 0);
        Page<BbsReply> page = new Page<BbsReply>(pageNo, pageSize);
        IPage<BbsReply> pageList = bbsReplyService.page(page, queryWrapper);

        ArrayList<BbsReply> bbsReplyFullDtoArrayList = new ArrayList<>();
        for (BbsReply record : pageList.getRecords()) {
            BbsReply bbsReplyFullDto = new BbsReply();
            BeanUtils.copyProperties(record, bbsReplyFullDto);

            //完善用户名 头像
            SysUser byId = sysUserService.getUserByName(bbsReplyFullDto.getCreateBy());
            bbsReplyFullDto.setAvatar(byId.getAvatar());
            bbsReplyFullDto.setRealname(byId.getRealname());

            SysUser byId3 = sysUserService.getUserByName(bbsReplyFullDto.getBeReplyUsername());
            bbsReplyFullDto.setBeReplyRealname(bbsReplyFullDto.getRealname());
            bbsReplyFullDto.setBeReplyAvatar(bbsReplyFullDto.getAvatar());
            //完善用户是否点赞
            //根据帖子id和用户id获取用户点赞记录 判断用户对该评论是否点赞

            if(null != sysUser){
                BbsUserPraise bbsUserIsPraise = bbsUserPraiseService.lambdaQuery()
                        .eq(BbsUserPraise::getReplyId, record.getId())
                        .eq(BbsUserPraise::getCreateBy, sysUser.getUsername()).one();
                if (null != bbsUserIsPraise) {
                    bbsReplyFullDto.setUserIsPraise(true);
                } else {
                    bbsReplyFullDto.setUserIsPraise(false);
                }
            }


            //完善子回复列表
            QueryWrapper<BbsReply> queryWrapperPid = new QueryWrapper<>();
            queryWrapperPid.eq("pid", record.getId()).orderByDesc("create_time");
            List<BbsReply> childReplyList = bbsReplyService.list(queryWrapperPid);

            ArrayList<BbsReply> bbsChildReplyFullDtoArrayList = new ArrayList<>();
            for (BbsReply reply : childReplyList) {
                BbsReply childReplyFullDto = new BbsReply();
                BeanUtils.copyProperties(reply, childReplyFullDto);
                SysUser byId1 = sysUserService.getUserByName(reply.getCreateBy());
                childReplyFullDto.setRealname(byId1.getRealname());
                childReplyFullDto.setAvatar(byId1.getAvatar());

                SysUser byId2 = sysUserService.getUserByName(reply.getBeReplyUsername());
                childReplyFullDto.setBeReplyRealname(byId2.getRealname());
                childReplyFullDto.setBeReplyAvatar(byId2.getAvatar());

                //完善用户是否点赞
                //根据帖子id和用户id获取用户点赞记录 判断用户对该评论是否点赞
                if(null != sysUser){
                    BbsUserPraise bbsUserIsPraiseChild = bbsUserPraiseService.lambdaQuery()
                            .eq(BbsUserPraise::getReplyId, reply.getId())
                            .eq(BbsUserPraise::getCreateBy, sysUser.getUsername()).one();
                    if (null != bbsUserIsPraiseChild) {
                        childReplyFullDto.setUserIsPraise(true);
                    } else {
                        childReplyFullDto.setUserIsPraise(false);
                    }
                }

                bbsChildReplyFullDtoArrayList.add(childReplyFullDto);
            }
            //子评论需要根据时间正倒序
            bbsChildReplyFullDtoArrayList.sort(Comparator.comparing(BbsReply::getCreateTime).reversed());
            bbsReplyFullDto.setChildFullReply(bbsChildReplyFullDtoArrayList);
            bbsReplyFullDtoArrayList.add(bbsReplyFullDto);
        }
        pageList.setRecords(bbsReplyFullDtoArrayList);
        return Result.OK(pageList);
    }
    /**
     * 分页列表查询
     *
     * @param bbsReply
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "帖子回复-完整分页列表查询")
    @ApiOperation(value = "帖子回复-完整分页列表查询", notes = "帖子回复-完整分页列表查询")
    @PostMapping(value = "/wise/mini/rootFullList/anon")
    public Result<?> queryFullPageListAnon(@RequestBody BbsReply bbsReply,
                                       @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                       @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                       HttpServletRequest req) {
        QueryWrapper<BbsReply> queryWrapper = QueryGenerator.initQueryWrapper(bbsReply, req.getParameterMap()).orderByDesc("create_time");
        // 使用 eq 防止模糊查询
        queryWrapper.eq("pid", 0);
        Page<BbsReply> page = new Page<BbsReply>(pageNo, pageSize);
        IPage<BbsReply> pageList = bbsReplyService.page(page, queryWrapper);

        ArrayList<BbsReply> bbsReplyFullDtoArrayList = new ArrayList<>();
        for (BbsReply record : pageList.getRecords()) {
            BbsReply bbsReplyFullDto = new BbsReply();
            BeanUtils.copyProperties(record, bbsReplyFullDto);

            //完善用户名 头像
            SysUser byId = sysUserService.getUserByName(bbsReplyFullDto.getCreateBy());
            bbsReplyFullDto.setAvatar(byId.getAvatar());
            bbsReplyFullDto.setRealname(byId.getRealname());

            SysUser byId3 = sysUserService.getUserByName(bbsReplyFullDto.getBeReplyUsername());
            bbsReplyFullDto.setBeReplyRealname(bbsReplyFullDto.getRealname());
            bbsReplyFullDto.setBeReplyAvatar(bbsReplyFullDto.getAvatar());
            //完善子回复列表
            QueryWrapper<BbsReply> queryWrapperPid = new QueryWrapper<>();
            queryWrapperPid.eq("pid", record.getId()).orderByDesc("create_time");
            List<BbsReply> childReplyList = bbsReplyService.list(queryWrapperPid);

            ArrayList<BbsReply> bbsChildReplyFullDtoArrayList = new ArrayList<>();
            for (BbsReply reply : childReplyList) {
                BbsReply childReplyFullDto = new BbsReply();
                BeanUtils.copyProperties(reply, childReplyFullDto);
                SysUser byId1 = sysUserService.getUserByName(reply.getCreateBy());
                childReplyFullDto.setRealname(byId1.getRealname());
                childReplyFullDto.setAvatar(byId1.getAvatar());

                SysUser byId2 = sysUserService.getUserByName(reply.getBeReplyUsername());
                childReplyFullDto.setBeReplyRealname(byId2.getRealname());
                childReplyFullDto.setBeReplyAvatar(byId2.getAvatar());

                bbsChildReplyFullDtoArrayList.add(childReplyFullDto);
            }
            //子评论需要根据时间正倒序
            bbsChildReplyFullDtoArrayList.sort(Comparator.comparing(BbsReply::getCreateTime).reversed());
            bbsReplyFullDto.setChildFullReply(bbsChildReplyFullDtoArrayList);
            bbsReplyFullDtoArrayList.add(bbsReplyFullDto);
        }
        pageList.setRecords(bbsReplyFullDtoArrayList);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param bbsReply
     * @return
     */
    @AutoLog(value = "帖子回复-添加")
    @ApiOperation(value = "帖子回复-添加", notes = "帖子回复-添加")
    @PostMapping(value = "/wise/mini/add")
    public Result<?> addWiseMini(@RequestBody BbsReply bbsReply) {
        //内容审核
        ContentCheck contentCheck = new ContentCheck();
        Result<?> result3 = contentCheck.checkBySensitiveWord(bbsReply.getContent());
        if (result3.isSuccess()) {
            Result<?> result4 = contentCheck.checkByWeiXin(bbsReply.getContent());
            if (!result4.isSuccess()) {
                result4.setMessage("评论" + result4.getMessage());
                return result4;
            }
        } else {
            result3.setMessage("评论" + result3.getMessage());
            return result3;
        }
        return bbsReplyService.addReply(bbsReply);
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "帖子回复-通过id删除")
    @ApiOperation(value = "帖子回复-通过id删除", notes = "帖子回复-通过id删除")
    @DeleteMapping(value = "/wise/mini/delete")
    public Result<?> deleteWiseMini(@RequestParam(name = "id", required = true) String id) {
        bbsReplyService.deleteBbsReplyWiseMini(id);

        return Result.OK("删除成功!");
    }


    /**
     * 批量查询子节点
     *
     * @param parentIds 父ID（多个采用半角逗号分割）
     * @param parentIds
     * @return
     */
    @AutoLog(value = "帖子回复-批量获取子数据")
    @ApiOperation(value = "帖子回复-批量获取子数据", notes = "帖子回复-批量获取子数据")
    @GetMapping("/wise/mini/getChildListBatch")
    public Result getChildListBatchWiseMini(@RequestParam("parentIds") String parentIds) {
        try {
            QueryWrapper<BbsReply> queryWrapper = new QueryWrapper<>();
            List<String> parentIdList = Arrays.asList(parentIds.split(","));
            queryWrapper.in("pid", parentIdList);
            List<BbsReply> list = bbsReplyService.list(queryWrapper);
            IPage<BbsReply> pageList = new Page<>(1, 10, list.size());
            pageList.setRecords(list);
            return Result.OK(pageList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error("批量查询子节点失败：" + e.getMessage());
        }
    }




    /**
     * 分页列表查询
     *
     * @param bbsReply
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "帖子回复-分页列表查询")
    @ApiOperation(value = "帖子回复-分页列表查询", notes = "帖子回复-分页列表查询")
    @GetMapping(value = "/wise/back/rootList")
    @PermissionData(pageComponent = "bbs/content/reply/BbsReplyList")
    public Result<?> queryPageListWiseBack(BbsReply bbsReply,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        String hasQuery = req.getParameter("hasQuery");
        if (hasQuery != null && "true".equals(hasQuery)) {
            QueryWrapper<BbsReply> queryWrapper = QueryGenerator.initQueryWrapper(bbsReply, req.getParameterMap());
            List<BbsReply> list = bbsReplyService.queryTreeListNoPage(queryWrapper);
            IPage<BbsReply> pageList = new Page<>(1, 10, list.size());
            pageList.setRecords(list);

            //补全用户昵称、贴子内容
            for (int i = 0; i < pageList.getRecords().size(); i++) {
                BbsReply bbsReply1 = pageList.getRecords().get(i);

                SysUser userByName = sysUserService.getUserByName(bbsReply1.getCreateBy());
                BbsTopic bbsTopic = bbsTopicService.getById(bbsReply1.getTopicId());

                pageList.getRecords().get(i).setCreateByName(userByName.getRealname());
                pageList.getRecords().get(i).setTopicContent(bbsTopic.getContent());
            }

            return Result.OK(pageList);
        } else {
            String parentId = bbsReply.getPid();
            if (oConvertUtils.isEmpty(parentId)) {
                parentId = "0";
            }
            bbsReply.setPid(null);
            QueryWrapper<BbsReply> queryWrapper = QueryGenerator.initQueryWrapper(bbsReply, req.getParameterMap());
            // 使用 eq 防止模糊查询
            queryWrapper.eq("pid", parentId);
            Page<BbsReply> page = new Page<BbsReply>(pageNo, pageSize);
            IPage<BbsReply> pageList = bbsReplyService.page(page, queryWrapper);


            //补全用户昵称、贴子内容
            for (int i = 0; i < pageList.getRecords().size(); i++) {
                BbsReply bbsReply1 = pageList.getRecords().get(i);

                SysUser userByName = sysUserService.getUserByName(bbsReply1.getCreateBy());
                BbsTopic bbsTopic = bbsTopicService.getById(bbsReply1.getTopicId());

                pageList.getRecords().get(i).setCreateByName(userByName.getRealname());
                pageList.getRecords().get(i).setTopicContent(bbsTopic.getContent());
            }

            return Result.OK(pageList);
        }
    }

    /**
     * 获取子数据
     *
     * @param bbsReply
     * @param req
     * @return
     */
    @AutoLog(value = "帖子回复-获取子数据")
    @ApiOperation(value = "帖子回复-获取子数据", notes = "帖子回复-获取子数据")
    @GetMapping(value = "/wise/back/childList")
    public Result<?> queryPageListWiseBack(BbsReply bbsReply, HttpServletRequest req) {
        QueryWrapper<BbsReply> queryWrapper = QueryGenerator.initQueryWrapper(bbsReply, req.getParameterMap());
        List<BbsReply> list = bbsReplyService.list(queryWrapper);
        IPage<BbsReply> pageList = new Page<>(1, 10, list.size());
        pageList.setRecords(list);

        //补全用户昵称、贴子内容
        for (int i = 0; i < pageList.getRecords().size(); i++) {
            BbsReply bbsReply1 = pageList.getRecords().get(i);

            SysUser userByName = sysUserService.getUserByName(bbsReply1.getCreateBy());
            BbsTopic bbsTopic = bbsTopicService.getById(bbsReply1.getTopicId());

            pageList.getRecords().get(i).setCreateByName(userByName.getRealname());
            pageList.getRecords().get(i).setTopicContent(bbsTopic.getContent());
        }

        return Result.OK(pageList);
    }

    /**
     * 批量查询子节点
     *
     * @param parentIds 父ID（多个采用半角逗号分割）
     * @param parentIds
     * @return
     */
    @AutoLog(value = "帖子回复-批量获取子数据")
    @ApiOperation(value = "帖子回复-批量获取子数据", notes = "帖子回复-批量获取子数据")
    @GetMapping("/wise/back/getChildListBatch")
    public Result getChildListBatchWiseBack(@RequestParam("parentIds") String parentIds) {
        try {
            QueryWrapper<BbsReply> queryWrapper = new QueryWrapper<>();
            List<String> parentIdList = Arrays.asList(parentIds.split(","));
            queryWrapper.in("pid", parentIdList);
            List<BbsReply> list = bbsReplyService.list(queryWrapper);
            IPage<BbsReply> pageList = new Page<>(1, 10, list.size());
            pageList.setRecords(list);

            //补全用户昵称、贴子内容
            for (int i = 0; i < pageList.getRecords().size(); i++) {
                BbsReply bbsReply1 = pageList.getRecords().get(i);

                SysUser userByName = sysUserService.getUserByName(bbsReply1.getCreateBy());
                BbsTopic bbsTopic = bbsTopicService.getById(bbsReply1.getTopicId());

                pageList.getRecords().get(i).setCreateByName(userByName.getRealname());
                pageList.getRecords().get(i).setTopicContent(bbsTopic.getContent());
            }

            return Result.OK(pageList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error("批量查询子节点失败：" + e.getMessage());
        }
    }
}
