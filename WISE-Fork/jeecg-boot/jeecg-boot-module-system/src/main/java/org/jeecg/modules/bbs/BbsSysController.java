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
import org.jeecg.modules.bbs.entity.BbsSys;
import org.jeecg.modules.bbs.service.IBbsSysService;
import org.jeecg.modules.bbs.utils.SessionCounter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 系统配置
 * @Author: jeecg-boot
 * @Date: 2021-02-23
 * @Version: V1.0
 */
@Api(tags = "系统配置")
@RestController
@RequestMapping("/bbs/bbsSys")
@Slf4j
public class BbsSysController extends JeecgController<BbsSys, IBbsSysService> {
    @Autowired
    private IBbsSysService bbsSysService;


    /**
     * 分页列表查询
     *
     * @param bbsSys
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "系统配置-分页列表查询")
    @ApiOperation(value = "系统配置-分页列表查询", notes = "系统配置-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(BbsSys bbsSys,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<BbsSys> queryWrapper = QueryGenerator.initQueryWrapper(bbsSys, req.getParameterMap());
        Page<BbsSys> page = new Page<BbsSys>(pageNo, pageSize);
        IPage<BbsSys> pageList = bbsSysService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param bbsSys
     * @return
     */
    @AutoLog(value = "系统配置-添加")
    @ApiOperation(value = "系统配置-添加", notes = "系统配置-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody BbsSys bbsSys) {
        bbsSysService.save(bbsSys);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param bbsSys
     * @return
     */
    @AutoLog(value = "系统配置-编辑")
    @ApiOperation(value = "系统配置-编辑", notes = "系统配置-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody BbsSys bbsSys) {
        bbsSysService.updateById(bbsSys);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "系统配置-通过id删除")
    @ApiOperation(value = "系统配置-通过id删除", notes = "系统配置-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        bbsSysService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "系统配置-批量删除")
    @ApiOperation(value = "系统配置-批量删除", notes = "系统配置-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.bbsSysService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "系统配置-通过id查询")
    @ApiOperation(value = "系统配置-通过id查询", notes = "系统配置-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        BbsSys bbsSys = bbsSysService.getById(id);
        if (bbsSys == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(bbsSys);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param bbsSys
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BbsSys bbsSys) {
        return super.exportXls(request, bbsSys, BbsSys.class, "系统配置");
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
        return super.importExcel(request, response, BbsSys.class);
    }


    // ****行星万象修改位置戳****

    /**
     * 查询系统在线人数
     *
     * @return
     */
    @AutoLog(value = "查询系统在线人数")
    @ApiOperation(value = "查询系统在线人数", notes = "查询系统在线人数")
    @GetMapping(value = "/wise/mini/onlinePeopleNumber")
    public Result<?> onlinePeopleNumber(HttpServletRequest httpServletRequest) {
        //HttpSession session = httpServletRequest.getSession();
        return Result.OK(SessionCounter.getActiveSessions());
    }

    /**
     * 分页列表查询
     *
     * @return
     */
    @AutoLog(value = "系统配置-根据Key查询值")
    @ApiOperation(value = "系统配置-根据Key查询值", notes = "系统配置-根据Key查询值")
    @GetMapping(value = "/wise/mini/queryValueByKey")
    public Result<?> queryValueByKey(@RequestParam(name = "sysKey", defaultValue = "") String sysKey) {
        BbsSys bbsSys = bbsSysService.lambdaQuery().eq(BbsSys::getSysKey, sysKey).one();

        Map<String, String> stringHashMap = new HashMap<>();
        stringHashMap.put("string", bbsSys.getSysValueString());
        stringHashMap.put("rich", bbsSys.getSysValueRich());
        return Result.OK(stringHashMap);
    }
}
