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
import org.jeecg.modules.bbs.entity.BbsClass;
import org.jeecg.modules.bbs.service.IBbsClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @Description: 版块
 * @Author: jeecg-boot
 * @Date: 2021-01-15
 * @Version: V1.0
 */
@Api(tags = "版块")
@RestController
@RequestMapping("/bbs/bbsClass")
@Slf4j
public class BbsClassController extends JeecgController<BbsClass, IBbsClassService> {
    @Autowired
    private IBbsClassService bbsClassService;


    /**
     * 添加
     *
     * @param bbsClass
     * @return
     */
    @AutoLog(value = "版块-添加")
    @ApiOperation(value = "版块-添加", notes = "版块-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody BbsClass bbsClass) {
        bbsClassService.save(bbsClass);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param bbsClass
     * @return
     */
    @AutoLog(value = "版块-编辑")
    @ApiOperation(value = "版块-编辑", notes = "版块-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody BbsClass bbsClass) {
        bbsClassService.updateById(bbsClass);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "版块-通过id删除")
    @ApiOperation(value = "版块-通过id删除", notes = "版块-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        bbsClassService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "版块-批量删除")
    @ApiOperation(value = "版块-批量删除", notes = "版块-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.bbsClassService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "版块-通过id查询")
    @ApiOperation(value = "版块-通过id查询", notes = "版块-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        BbsClass bbsClass = bbsClassService.getById(id);
        if (bbsClass == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(bbsClass);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param bbsClass
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BbsClass bbsClass) {
        return super.exportXls(request, bbsClass, BbsClass.class, "版块");
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
        return super.importExcel(request, response, BbsClass.class);
    }


    // ****行星万象修改位置戳****

    /**
     * 分页列表查询
     *
     * @param bbsClass
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "版块-分页列表查询")
    @ApiOperation(value = "版块-分页列表查询", notes = "版块-分页列表查询")
    @GetMapping(value = "/wise/mini/list")
    public Result<?> queryPageList(BbsClass bbsClass,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "100") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<BbsClass> queryWrapper = QueryGenerator.initQueryWrapper(bbsClass, req.getParameterMap()).orderByAsc("class_sort");
        queryWrapper.eq("region_code", req.getHeader("regioncode"));
        Page<BbsClass> page = new Page<BbsClass>(pageNo, pageSize);
        IPage<BbsClass> pageList = bbsClassService.page(page, queryWrapper);
        return Result.OK(pageList);
    }
}
