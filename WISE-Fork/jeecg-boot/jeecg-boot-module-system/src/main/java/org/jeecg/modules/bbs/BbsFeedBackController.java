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
import org.jeecg.modules.bbs.entity.BbsFeedBack;
import org.jeecg.modules.bbs.entity.BbsFeedBackFullDto;
import org.jeecg.modules.bbs.entity.BbsRegion;
import org.jeecg.modules.bbs.entity.BbsTopicImage;
import org.jeecg.modules.bbs.service.IBbsFeedBackService;
import org.jeecg.modules.bbs.service.impl.BbsTopicImageServiceImpl;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Description: 用户反馈
 * @Author: jeecg-boot
 * @Date: 2021-05-29
 * @Version: V1.0
 */
@Api(tags = "用户反馈")
@RestController
@RequestMapping("/bbs/bbsFeedBack")
@Slf4j
public class BbsFeedBackController extends JeecgController<BbsFeedBack, IBbsFeedBackService> {
    @Autowired
    private IBbsFeedBackService bbsFeedBackService;
    @Autowired
    private BbsTopicImageServiceImpl bbsTopicImageService;
    @Autowired
    private ISysUserService sysUserService;

    /**
     * 分页列表查询
     *
     * @param bbsFeedBack
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "用户反馈-分页列表查询")
    @ApiOperation(value = "用户反馈-分页列表查询", notes = "用户反馈-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(BbsFeedBack bbsFeedBack,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<BbsFeedBack> queryWrapper = QueryGenerator.initQueryWrapper(bbsFeedBack, req.getParameterMap());
        Page<BbsFeedBack> page = new Page<BbsFeedBack>(pageNo, pageSize);
        IPage<BbsFeedBack> pageList = bbsFeedBackService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param bbsFeedBack
     * @return
     */
    @AutoLog(value = "用户反馈-添加")
    @ApiOperation(value = "用户反馈-添加", notes = "用户反馈-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody BbsFeedBack bbsFeedBack) {
        bbsFeedBackService.save(bbsFeedBack);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param bbsFeedBack
     * @return
     */
    @AutoLog(value = "用户反馈-编辑")
    @ApiOperation(value = "用户反馈-编辑", notes = "用户反馈-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody BbsFeedBack bbsFeedBack) {
        bbsFeedBackService.updateById(bbsFeedBack);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "用户反馈-通过id删除")
    @ApiOperation(value = "用户反馈-通过id删除", notes = "用户反馈-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        bbsFeedBackService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "用户反馈-批量删除")
    @ApiOperation(value = "用户反馈-批量删除", notes = "用户反馈-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.bbsFeedBackService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "用户反馈-通过id查询")
    @ApiOperation(value = "用户反馈-通过id查询", notes = "用户反馈-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        BbsFeedBack bbsFeedBack = bbsFeedBackService.getById(id);
        if (bbsFeedBack == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(bbsFeedBack);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param bbsFeedBack
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BbsFeedBack bbsFeedBack) {
        return super.exportXls(request, bbsFeedBack, BbsFeedBack.class, "用户反馈");
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
        return super.importExcel(request, response, BbsFeedBack.class);
    }


    // ****行星万象修改位置戳****

    /**
     * 分页列表查询
     *
     * @param bbsFeedBack
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "用户反馈-分页列表查询")
    @ApiOperation(value = "用户反馈-分页列表查询", notes = "用户反馈-分页列表查询")
    @GetMapping(value = "/wise/mini/list")
    public Result<?> queryPageListWiseMini(BbsFeedBack bbsFeedBack,
                                           @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                           @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                           HttpServletRequest req) {
        QueryWrapper<BbsFeedBack> queryWrapper = QueryGenerator.initQueryWrapper(bbsFeedBack, req.getParameterMap());
        queryWrapper.orderByDesc("create_time");
        Page<BbsFeedBack> page = new Page<BbsFeedBack>(pageNo, pageSize);
        IPage<BbsFeedBack> pageList = bbsFeedBackService.page(page, queryWrapper);


        for (BbsFeedBack bbsFeedBack1 : pageList.getRecords()) {
            List<BbsTopicImage> list = bbsTopicImageService.lambdaQuery().eq(BbsTopicImage::getTopicId, bbsFeedBack1.getId()).list();
            bbsFeedBack1.setImageUrl(list);
        }
        //按时间排序
        pageList.getRecords().stream()
                .sorted(Comparator.comparing(BbsFeedBack::getCreateTime).reversed())
                .collect(Collectors.toList());


        //补全
        for (int i = 0; i < pageList.getRecords().size(); i++) {
            BbsFeedBack bbsFeedBack1 = pageList.getRecords().get(i);

            SysUser userByName = sysUserService.getUserByName(bbsFeedBack1.getCreateBy());
            SysUser disposeUser = sysUserService.getUserByName(bbsFeedBack1.getDisposeUsername());
            Set<String> userRolesSet = sysUserService.getUserRolesSet(disposeUser.getUsername());


            bbsFeedBack1.setCreateByName(userByName.getRealname());
            bbsFeedBack1.setUserAvatar(userByName.getAvatar());

            bbsFeedBack1.setDisposeRealName(disposeUser.getRealname());
            bbsFeedBack1.setDisposeUserAvatar(disposeUser.getAvatar());
            if (userRolesSet.iterator().hasNext()) {
                bbsFeedBack1.setDisposeUserRole(userRolesSet.iterator().next());
            }
        }

        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param bbsFeedBackFullDto
     * @return
     */
    @AutoLog(value = "用户反馈-添加")
    @ApiOperation(value = "用户反馈-添加", notes = "用户反馈-添加")
    @PostMapping(value = "/wise/mini/add")
    public Result<?> add(@RequestBody BbsFeedBackFullDto bbsFeedBackFullDto) {
        return bbsFeedBackService.addFeedBack(bbsFeedBackFullDto);

    }

    /**
     * 分页列表查询
     *
     * @param bbsFeedBack
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "用户反馈-分页列表查询")
    @ApiOperation(value = "用户反馈-分页列表查询", notes = "用户反馈-分页列表查询")
    @GetMapping(value = "/wise/back/list")
    public Result<?> queryPageListWiseBack(BbsFeedBack bbsFeedBack,
                                           @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                           @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                           HttpServletRequest req) {
        QueryWrapper<BbsFeedBack> queryWrapper = QueryGenerator.initQueryWrapper(bbsFeedBack, req.getParameterMap());
        queryWrapper.orderByDesc("create_time");
        Page<BbsFeedBack> page = new Page<BbsFeedBack>(pageNo, pageSize);
        IPage<BbsFeedBack> pageList = bbsFeedBackService.page(page, queryWrapper);


        for (BbsFeedBack bbsFeedBack1 : pageList.getRecords()) {
            List<BbsTopicImage> list = bbsTopicImageService.lambdaQuery().eq(BbsTopicImage::getTopicId, bbsFeedBack1.getId()).list();
            bbsFeedBack1.setImageUrl(list);
        }
        //按时间排序
        pageList.getRecords().stream()
                .sorted(Comparator.comparing(BbsFeedBack::getCreateTime).reversed())
                .collect(Collectors.toList());


        //补全
        for (int i = 0; i < pageList.getRecords().size(); i++) {
            BbsFeedBack bbsFeedBack1 = pageList.getRecords().get(i);

            SysUser userByName = sysUserService.getUserByName(bbsFeedBack1.getCreateBy());
            SysUser disposeUser = sysUserService.getUserByName(bbsFeedBack1.getDisposeUsername());
            Set<String> userRolesSet = sysUserService.getUserRolesSet(disposeUser.getUsername());


            bbsFeedBack1.setCreateByName(userByName.getRealname());
            bbsFeedBack1.setUserAvatar(userByName.getAvatar());

            bbsFeedBack1.setDisposeRealName(disposeUser.getRealname());
            bbsFeedBack1.setDisposeUserAvatar(disposeUser.getAvatar());
            if (userRolesSet.iterator().hasNext()) {
                bbsFeedBack1.setDisposeUserRole(userRolesSet.iterator().next());
            }
        }

        return Result.OK(pageList);
    }
}
