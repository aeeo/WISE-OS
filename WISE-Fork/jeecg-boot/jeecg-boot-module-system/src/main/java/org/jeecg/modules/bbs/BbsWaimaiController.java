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
import org.jeecg.modules.bbs.entity.BbsWaimai;
import org.jeecg.modules.bbs.service.IBbsWaimaiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @Description: 外卖推广
 * @Author: jeecg-boot
 * @Date: 2021-05-27
 * @Version: V1.0
 */
@Api(tags = "外卖推广")
@RestController
@RequestMapping("/bbs/bbsWaimai")
@Slf4j
public class BbsWaimaiController extends JeecgController<BbsWaimai, IBbsWaimaiService> {
    @Autowired
    private IBbsWaimaiService bbsWaimaiService;

    /**
     * 分页列表查询
     *
     * @param bbsWaimai
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "外卖推广-分页列表查询")
    @ApiOperation(value = "外卖推广-分页列表查询", notes = "外卖推广-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(BbsWaimai bbsWaimai,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<BbsWaimai> queryWrapper = QueryGenerator.initQueryWrapper(bbsWaimai, req.getParameterMap());
        Page<BbsWaimai> page = new Page<BbsWaimai>(pageNo, pageSize);
        IPage<BbsWaimai> pageList = bbsWaimaiService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param bbsWaimai
     * @return
     */
    @AutoLog(value = "外卖推广-添加")
    @ApiOperation(value = "外卖推广-添加", notes = "外卖推广-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody BbsWaimai bbsWaimai) {
        bbsWaimaiService.save(bbsWaimai);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param bbsWaimai
     * @return
     */
    @AutoLog(value = "外卖推广-编辑")
    @ApiOperation(value = "外卖推广-编辑", notes = "外卖推广-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody BbsWaimai bbsWaimai) {
        bbsWaimaiService.updateById(bbsWaimai);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "外卖推广-通过id删除")
    @ApiOperation(value = "外卖推广-通过id删除", notes = "外卖推广-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        bbsWaimaiService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "外卖推广-批量删除")
    @ApiOperation(value = "外卖推广-批量删除", notes = "外卖推广-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.bbsWaimaiService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "外卖推广-通过id查询")
    @ApiOperation(value = "外卖推广-通过id查询", notes = "外卖推广-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        BbsWaimai bbsWaimai = bbsWaimaiService.getById(id);
        if (bbsWaimai == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(bbsWaimai);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param bbsWaimai
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BbsWaimai bbsWaimai) {
        return super.exportXls(request, bbsWaimai, BbsWaimai.class, "外卖推广");
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
        return super.importExcel(request, response, BbsWaimai.class);
    }


    // ****行星万象修改位置戳****

    /**
     * 分页列表查询
     *
     * @param bbsWaimai
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "外卖推广-分页列表查询")
    @ApiOperation(value = "外卖推广-分页列表查询", notes = "外卖推广-分页列表查询")
    @GetMapping(value = "/wise/mini/list")
    public Result<?> queryPageListWiseMini(BbsWaimai bbsWaimai,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<BbsWaimai> queryWrapper = QueryGenerator.initQueryWrapper(bbsWaimai, req.getParameterMap());
        queryWrapper.orderByAsc("sort");
        Page<BbsWaimai> page = new Page<BbsWaimai>(pageNo, pageSize);
        IPage<BbsWaimai> pageList = bbsWaimaiService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 分页列表查询
     *
     * @param bbsWaimai
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "行星外卖独立-分页列表查询")
    @ApiOperation(value = "外卖推广-分页列表查询", notes = "外卖推广-分页列表查询")
    @GetMapping(value = "/wise/mini/list/alone")
    public Result<?> queryPageListWiseMiniAlone(BbsWaimai bbsWaimai,
                                           @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                           @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                           HttpServletRequest req) {
        QueryWrapper<BbsWaimai> queryWrapper = QueryGenerator.initQueryWrapper(bbsWaimai, req.getParameterMap());
        queryWrapper.orderByAsc("sort");
        Page<BbsWaimai> page = new Page<BbsWaimai>(pageNo, pageSize);
        IPage<BbsWaimai> pageList = bbsWaimaiService.page(page, queryWrapper);
        return Result.OK(pageList);
    }
}
