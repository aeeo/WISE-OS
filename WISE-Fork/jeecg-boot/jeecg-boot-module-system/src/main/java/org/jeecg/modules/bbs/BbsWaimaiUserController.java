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
import org.jeecg.modules.bbs.entity.BbsRegion;
import org.jeecg.modules.bbs.entity.BbsWaimai;
import org.jeecg.modules.bbs.entity.BbsWaimaiUser;
import org.jeecg.modules.bbs.service.IBbsWaimaiService;
import org.jeecg.modules.bbs.service.IBbsWaimaiUserService;
import org.jeecg.modules.bbs.service.impl.BbsRegionServiceImpl;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @Description: 用户点击外卖记录
 * @Author: jeecg-boot
 * @Date: 2021-05-27
 * @Version: V1.0
 */
@Api(tags = "用户点击外卖记录")
@RestController
@RequestMapping("/bbs/bbsWaimaiUser")
@Slf4j
public class BbsWaimaiUserController extends JeecgController<BbsWaimaiUser, IBbsWaimaiUserService> {
    @Autowired
    private IBbsWaimaiService bbsWaimaiService;
    @Autowired
    private IBbsWaimaiUserService bbsWaimaiUserService;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private BbsRegionServiceImpl bbsRegionService;
    /**
     * 分页列表查询
     *
     * @param bbsWaimaiUser
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "用户点击外卖记录-分页列表查询")
    @ApiOperation(value = "用户点击外卖记录-分页列表查询", notes = "用户点击外卖记录-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(BbsWaimaiUser bbsWaimaiUser,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<BbsWaimaiUser> queryWrapper = QueryGenerator.initQueryWrapper(bbsWaimaiUser, req.getParameterMap());
        Page<BbsWaimaiUser> page = new Page<BbsWaimaiUser>(pageNo, pageSize);
        IPage<BbsWaimaiUser> pageList = bbsWaimaiUserService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param bbsWaimaiUser
     * @return
     */
    @AutoLog(value = "用户点击外卖记录-添加")
    @ApiOperation(value = "用户点击外卖记录-添加", notes = "用户点击外卖记录-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody BbsWaimaiUser bbsWaimaiUser) {
        bbsWaimaiUserService.save(bbsWaimaiUser);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param bbsWaimaiUser
     * @return
     */
    @AutoLog(value = "用户点击外卖记录-编辑")
    @ApiOperation(value = "用户点击外卖记录-编辑", notes = "用户点击外卖记录-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody BbsWaimaiUser bbsWaimaiUser) {
        bbsWaimaiUserService.updateById(bbsWaimaiUser);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "用户点击外卖记录-通过id删除")
    @ApiOperation(value = "用户点击外卖记录-通过id删除", notes = "用户点击外卖记录-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        bbsWaimaiUserService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "用户点击外卖记录-批量删除")
    @ApiOperation(value = "用户点击外卖记录-批量删除", notes = "用户点击外卖记录-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.bbsWaimaiUserService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "用户点击外卖记录-通过id查询")
    @ApiOperation(value = "用户点击外卖记录-通过id查询", notes = "用户点击外卖记录-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        BbsWaimaiUser bbsWaimaiUser = bbsWaimaiUserService.getById(id);
        if (bbsWaimaiUser == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(bbsWaimaiUser);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param bbsWaimaiUser
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BbsWaimaiUser bbsWaimaiUser) {
        return super.exportXls(request, bbsWaimaiUser, BbsWaimaiUser.class, "用户点击外卖记录");
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
        return super.importExcel(request, response, BbsWaimaiUser.class);
    }

    // ****行星万象修改位置戳****

    /**
     * 添加
     *
     * @param bbsWaimaiUser
     * @return
     */
    @AutoLog(value = "用户点击外卖记录-添加")
    @ApiOperation(value = "用户点击外卖记录-添加", notes = "用户点击外卖记录-添加")
    @PostMapping(value = "/wise/mini/add")
    public Result<?> addWiseMini(@RequestBody BbsWaimaiUser bbsWaimaiUser) {
        bbsWaimaiUserService.save(bbsWaimaiUser);
        return Result.OK("添加成功！");
    }


    /**
     * 分页列表查询
     *
     * @param bbsWaimaiUser
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "用户点击外卖记录-分页列表查询")
    @ApiOperation(value = "用户点击外卖记录-分页列表查询", notes = "用户点击外卖记录-分页列表查询")
    @GetMapping(value = "/wise/back/list")
    public Result<?> queryPageListWiseBack(BbsWaimaiUser bbsWaimaiUser,
                                           @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                           @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                           HttpServletRequest req) {
        QueryWrapper<BbsWaimaiUser> queryWrapper = QueryGenerator.initQueryWrapper(bbsWaimaiUser, req.getParameterMap());
        Page<BbsWaimaiUser> page = new Page<BbsWaimaiUser>(pageNo, pageSize);
        IPage<BbsWaimaiUser> pageList = bbsWaimaiUserService.page(page, queryWrapper);


        //补全    外卖标题、用户名
        for (int i = 0; i < pageList.getRecords().size(); i++) {
            BbsWaimaiUser bbsWaimaiUser1 = pageList.getRecords().get(i);
            SysUser userByName = sysUserService.getUserByName(pageList.getRecords().get(i).getCreateBy());
            BbsRegion bbsRegion = bbsRegionService.lambdaQuery().eq(BbsRegion::getRegionCode, bbsWaimaiUser1.getRegionCode()).one();
            BbsWaimai waimai = bbsWaimaiService.getById(bbsWaimaiUser1.getWaimaiId());

            bbsWaimaiUser1.setCreateByName(userByName.getRealname());
            bbsWaimaiUser1.setWaimaiTitle(waimai.getTitle());
            bbsWaimaiUser1.setRegionName(bbsRegion.getFullName());
        }

        return Result.OK(pageList);
    }


}

