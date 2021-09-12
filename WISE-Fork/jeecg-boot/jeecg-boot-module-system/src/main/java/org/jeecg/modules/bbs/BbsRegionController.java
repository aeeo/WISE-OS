package org.jeecg.modules.bbs.controller;

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
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.bbs.entity.BbsClass;
import org.jeecg.modules.bbs.entity.BbsRegion;
import org.jeecg.modules.bbs.entity.BbsUserRecord;
import org.jeecg.modules.bbs.service.IBbsClassService;
import org.jeecg.modules.bbs.service.IBbsRegionService;
import org.jeecg.modules.bbs.service.impl.BbsUserRecordServiceImpl;
import org.jeecg.modules.bbs.vo.BbsRegionPage;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.ISysDepartService;
import org.jeecg.modules.system.service.ISysUserService;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: 地区
 * @Author: jeecg-boot
 * @Date: 2021-05-27
 * @Version: V1.0
 */
@Api(tags = "地区")
@RestController
@RequestMapping("/bbs/bbsRegion")
@Slf4j
public class BbsRegionController {
    @Autowired
    private IBbsRegionService bbsRegionService;
    @Autowired
    private IBbsClassService bbsClassService;
    @Autowired
    private BbsUserRecordServiceImpl bbsUserRecordService;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private ISysDepartService sysDepartService;

    /**
     * 分页列表查询
     *
     * @param bbsRegion
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "地区-分页列表查询")
    @ApiOperation(value = "地区-分页列表查询", notes = "地区-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(BbsRegion bbsRegion,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<BbsRegion> queryWrapper = QueryGenerator.initQueryWrapper(bbsRegion, req.getParameterMap());
        Page<BbsRegion> page = new Page<BbsRegion>(pageNo, pageSize);
        IPage<BbsRegion> pageList = bbsRegionService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param bbsRegionPage
     * @return
     */
    @AutoLog(value = "地区-添加")
    @ApiOperation(value = "地区-添加", notes = "地区-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody BbsRegionPage bbsRegionPage) {
        BbsRegion bbsRegion = new BbsRegion();
        BeanUtils.copyProperties(bbsRegionPage, bbsRegion);
        bbsRegionService.saveMain(bbsRegion, bbsRegionPage.getBbsClassList());
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param bbsRegionPage
     * @return
     */
    @AutoLog(value = "地区-编辑")
    @ApiOperation(value = "地区-编辑", notes = "地区-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody BbsRegionPage bbsRegionPage) {
        BbsRegion bbsRegion = new BbsRegion();
        BeanUtils.copyProperties(bbsRegionPage, bbsRegion);
        BbsRegion bbsRegionEntity = bbsRegionService.getById(bbsRegion.getId());
        if (bbsRegionEntity == null) {
            return Result.error("未找到对应数据");
        }
        bbsRegionService.updateMain(bbsRegion, bbsRegionPage.getBbsClassList());
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "地区-通过id删除")
    @ApiOperation(value = "地区-通过id删除", notes = "地区-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        bbsRegionService.delMain(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "地区-批量删除")
    @ApiOperation(value = "地区-批量删除", notes = "地区-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.bbsRegionService.delBatchMain(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "地区-通过id查询")
    @ApiOperation(value = "地区-通过id查询", notes = "地区-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        BbsRegion bbsRegion = bbsRegionService.getById(id);
        if (bbsRegion == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(bbsRegion);

    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "版块通过主表ID查询")
    @ApiOperation(value = "版块主表ID查询", notes = "版块-通主表ID查询")
    @GetMapping(value = "/queryBbsClassByMainId")
    public Result<?> queryBbsClassListByMainId(@RequestParam(name = "id", required = true) String id) {
        List<BbsClass> bbsClassList = bbsClassService.selectByMainId(id);
        return Result.OK(bbsClassList);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param bbsRegion
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BbsRegion bbsRegion) {
        // Step.1 组装查询条件查询数据
        QueryWrapper<BbsRegion> queryWrapper = QueryGenerator.initQueryWrapper(bbsRegion, request.getParameterMap());
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        //Step.2 获取导出数据
        List<BbsRegion> queryList = bbsRegionService.list(queryWrapper);
        // 过滤选中数据
        String selections = request.getParameter("selections");
        List<BbsRegion> bbsRegionList = new ArrayList<BbsRegion>();
        if (oConvertUtils.isEmpty(selections)) {
            bbsRegionList = queryList;
        } else {
            List<String> selectionList = Arrays.asList(selections.split(","));
            bbsRegionList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
        }

        // Step.3 组装pageList
        List<BbsRegionPage> pageList = new ArrayList<BbsRegionPage>();
        for (BbsRegion main : bbsRegionList) {
            BbsRegionPage vo = new BbsRegionPage();
            BeanUtils.copyProperties(main, vo);
            List<BbsClass> bbsClassList = bbsClassService.selectByMainId(main.getId());
            vo.setBbsClassList(bbsClassList);
            pageList.add(vo);
        }

        // Step.4 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "地区列表");
        mv.addObject(NormalExcelConstants.CLASS, BbsRegionPage.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("地区数据", "导出人:" + sysUser.getRealname(), "地区"));
        mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
        return mv;
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
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile file = entity.getValue();// 获取上传文件对象
            ImportParams params = new ImportParams();
            params.setTitleRows(2);
            params.setHeadRows(1);
            params.setNeedSave(true);
            try {
                List<BbsRegionPage> list = ExcelImportUtil.importExcel(file.getInputStream(), BbsRegionPage.class, params);
                for (BbsRegionPage page : list) {
                    BbsRegion po = new BbsRegion();
                    BeanUtils.copyProperties(page, po);
                    bbsRegionService.saveMain(po, page.getBbsClassList());
                }
                return Result.OK("文件导入成功！数据行数:" + list.size());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return Result.error("文件导入失败:" + e.getMessage());
            } finally {
                try {
                    file.getInputStream().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return Result.OK("文件导入失败！");
    }


    // ****行星万象修改位置戳****

    /**
     * 列表查询
     *
     * @param bbsRegion
     * @param req
     * @return
     */
    @AutoLog(value = "地区-列表查询")
    @ApiOperation(value = "地区-列表查询", notes = "地区-列表查询")
    @GetMapping(value = "/wise/mini/queryList")
    public Result<?> queryList(BbsRegion bbsRegion, HttpServletRequest req) {
        QueryWrapper<BbsRegion> queryWrapper = QueryGenerator.initQueryWrapper(bbsRegion, req.getParameterMap());
        //根据fullName首字符排序
        queryWrapper.orderBy(true, true, "convert(full_name USING gbk)");
        List<BbsRegion> list = bbsRegionService.list(queryWrapper);
        return Result.OK(list);
    }

    /**
     * 列表查询
     *
     * @return
     */
    @AutoLog(value = "地区-根据regionCode查询详细信息")
    @ApiOperation(value = "地区-根据regionCode查询详细信息", notes = "地区-根据regionCode查询详细信息")
    @GetMapping(value = "/wise/mini/queryByRegionCode")
    public Result<?> queryByRegionCode(@RequestParam(value = "regionCode") String regionCode) {
        BbsRegion one = bbsRegionService.lambdaQuery().eq(BbsRegion::getRegionCode, regionCode).one();
        return Result.OK(one);
    }


    /**
     * 地区-切换
     *
     * @param bbsRegion
     * @param username  用户在获取token的时候还可能会切换地区，但是此时shiro还取不到用户的信息，所以只能传username
     * @return
     */
    @AutoLog(value = "地区-切换")
    @ApiOperation(value = "地区-切换", notes = "地区-切换")
    @PostMapping(value = "/wise/mini/switchRegion")
    public Result<?> switchRegion(@RequestBody BbsRegion bbsRegion, @RequestParam(name = "username", defaultValue = "") String username) {
        BbsUserRecord bbsUserRecord = new BbsUserRecord();

        String currentUsername = "";
        if (!"".equals(username)) {
            bbsUserRecord = bbsUserRecordService.lambdaQuery().eq(BbsUserRecord::getCreateBy, username).one();
            currentUsername = username;
        } else {
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            bbsUserRecord = bbsUserRecordService.lambdaQuery().eq(BbsUserRecord::getCreateBy, sysUser.getUsername()).one();
            currentUsername = sysUser.getUsername();
        }

        //用户记录regionSwitchCount+1，region_switch_date当前月，region_code当前区域编码，region_fullname当前区域名
        if (bbsUserRecord.getRegionCode().equals(bbsRegion.getRegionCode())) {
            return Result.OK("已经在此区域，无需切换！");
        }
        bbsUserRecordService.lambdaUpdate().eq(BbsUserRecord::getCreateBy, currentUsername)
                .set(BbsUserRecord::getRegionCode, bbsRegion.getRegionCode())
                .set(BbsUserRecord::getSysOrgCode, bbsRegion.getSysOrgCode())
                .set(BbsUserRecord::getRegionSwitchCount, bbsUserRecord.getRegionSwitchCount() + 1)
                .update();

        SysUser userByName = sysUserService.getUserByName(currentUsername);

        sysUserService.addUserWithDepart(userByName, bbsRegion.getRegionDepartId());    //修改用户在区域对应的部门,追加
        SysDepart sysDepartServiceById = sysDepartService.getById(bbsRegion.getRegionDepartId());
        sysUserService.updateUserDepart(userByName.getUsername(), sysDepartServiceById.getOrgCode());   //设置用户当前部门
        return Result.OK("切换区域成功");
    }

    /**
     * BBS添加地区
     *
     * @param bbsRegionPage
     * @return
     */
    @AutoLog(value = "地区-添加")
    @ApiOperation(value = "地区-添加", notes = "地区-添加")
    @PostMapping(value = "/wise/back/addRegion")
    public Result<?> addRegion(@RequestBody BbsRegionPage bbsRegionPage) {
        BbsRegion bbsRegion = new BbsRegion();
        BeanUtils.copyProperties(bbsRegionPage, bbsRegion);
        bbsRegionService.addRegion(bbsRegion, bbsRegionPage.getBbsClassList());
        return Result.OK("添加成功！");
    }
    /**
     * 初始化所有区域版块
     *  在配置文件jdbc链接后面添加&allowMultiQueries=true
     *  druid filters 删除wall, 才可以调用
     * @return
     */
    //@AutoLog(value = "地区-初始化所有区域版块")
    //@ApiOperation(value = "地区-初始化所有区域版块", notes = "地区-初始化所有区域版块")
    //@PostMapping(value = "/wise//initAllRegionClass")
    //public Result<?> initAllRegionClass() {
    //    bbsRegionService.initAllRegionClass();
    //    return Result.OK("添加成功！");
    //}


    /**
     * 添加
     * 分页列表查询
     *
     * @param bbsRegionPage
     * @param bbsRegion
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "地区-添加")
    @ApiOperation(value = "地区-添加", notes = "地区-添加")
    @PostMapping(value = "/wise/back/add")
    public Result<?> addWiseBack(@RequestBody BbsRegionPage bbsRegionPage) {
        if (null == bbsRegionPage) {
            return Result.error("区域不能为空！");
        }
        if (null == bbsRegionPage.getRegionCode() || bbsRegionPage.getRegionCode().isEmpty()) {
            return Result.error("区域编码不能为空！");
        }
        BbsRegion one = bbsRegionService.lambdaQuery().eq(BbsRegion::getRegionCode, bbsRegionPage.getRegionCode()).one();
        if (null != one) {
            return Result.error("区域编码已存在！");
        }
        BbsRegion bbsRegion = new BbsRegion();
        BeanUtils.copyProperties(bbsRegionPage, bbsRegion);
        bbsRegionService.addRegion(bbsRegion, bbsRegionPage.getBbsClassList());
        //bbsRegionService.saveMain(bbsRegion, bbsRegionPage.getBbsClassList());
        return Result.OK("添加成功！");
    }


    /**
     * 分页列表查询
     *
     * @param bbsRegion
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "地区-分页列表查询")
    @ApiOperation(value = "地区-分页列表查询", notes = "地区-分页列表查询")
    @GetMapping(value = "/wise/back/list")
    @PermissionData(pageComponent = "bbs/operator/region/BbsRegionList")
    public Result<?> queryPageListWiseBack(BbsRegion bbsRegion,
                                           @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                           @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                           HttpServletRequest req) {
        QueryWrapper<BbsRegion> queryWrapper = QueryGenerator.initQueryWrapper(bbsRegion, req.getParameterMap());
        Page<BbsRegion> page = new Page<BbsRegion>(pageNo, pageSize);
        IPage<BbsRegion> pageList = bbsRegionService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

}