package org.jeecg.modules.bbs;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.api.vo.TopicResult;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.aspect.annotation.PermissionData;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.bbs.entity.*;
import org.jeecg.modules.bbs.service.*;
import org.jeecg.modules.bbs.service.impl.*;
import org.jeecg.modules.bbs.utils.BbsAuthUtils;
import org.jeecg.modules.bbs.utils.ContentCheck;
import org.jeecg.modules.bbs.vo.BbsTopicPage;
import org.jeecg.modules.cache.BbsRedisUtils;
import org.jeecg.modules.system.controller.SysRoleController;
import org.jeecg.modules.system.controller.SysUserController;
import org.jeecg.modules.system.entity.SysRole;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.ISysRoleService;
import org.jeecg.modules.system.service.ISysUserService;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 帖子
 * @Author: jeecg-boot
 * @Date: 2021-05-25
 * @Version: V1.0
 */
@Api(tags = "帖子")
@RestController
@RequestMapping("/bbs/bbsTopic")
@Slf4j
public class BbsTopicController {

    @Autowired
    private BbsTopicServiceImpl bbsTopicService;
    @Autowired
    private IBbsTopicService bbsTopicServiceService;
    @Autowired
    private IBbsTopicFullDtoService bbsTopicFullDtoService;
    @Autowired
    private IBbsTopicImageService bbsTopicImageService;
    @Autowired
    private IBbsTopicTagService bbsTopicTagService;
    @Autowired
    private IBbsTopicLinkService bbsTopicLinkService;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private SysUserController sysUserController;
    @Autowired
    private SysRoleController sysRoleController;
    @Autowired
    private IBbsUserRecordService bbsUserRecordService;
    @Autowired
    private BbsUserTopicClickController bbsUserTopicClickController;
    @Autowired
    private BbsUserPraiseServiceImpl bbsUserPraiseService;
    @Autowired
    private BbsUserStarServiceImpl bbsUserStarService;
    @Autowired
    private BbsReplyServiceImpl bbsReplyService;
    @Autowired
    private BbsClassServiceImpl bbsClassService;
    @Autowired
    private BbsRegionServiceImpl bbsRegionService;
    @Autowired
    private ISysRoleService sysRoleService;
    @Autowired
    private BbsRedisUtils bbsRedisUtils;
    @Autowired
    private BbsAuthController bbsAuthController;
    @Autowired
    private BbsAuthUtils bbsAuthUtils;


    /**
     * 分页列表查询
     *
     * @param bbsTopic
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "帖子-分页列表查询")
    @ApiOperation(value = "帖子-分页列表查询", notes = "帖子-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(BbsTopic bbsTopic,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<BbsTopic> queryWrapper = QueryGenerator.initQueryWrapper(bbsTopic, req.getParameterMap());
        Page<BbsTopic> page = new Page<BbsTopic>(pageNo, pageSize);
        IPage<BbsTopic> pageList = bbsTopicService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param bbsTopicPage
     * @return
     */
    @AutoLog(value = "帖子-添加")
    @ApiOperation(value = "帖子-添加", notes = "帖子-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody BbsTopicPage bbsTopicPage) {
        BbsTopic bbsTopic = new BbsTopic();
        BeanUtils.copyProperties(bbsTopicPage, bbsTopic);
        bbsTopicService.saveMain(bbsTopic, bbsTopicPage.getBbsTopicImageList(), bbsTopicPage.getBbsTopicTagList(), bbsTopicPage.getBbsTopicLinkList());
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param bbsTopicPage
     * @return
     */
    @AutoLog(value = "帖子-编辑")
    @ApiOperation(value = "帖子-编辑", notes = "帖子-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody BbsTopicPage bbsTopicPage) {
        BbsTopic bbsTopic = new BbsTopic();
        BeanUtils.copyProperties(bbsTopicPage, bbsTopic);
        BbsTopic bbsTopicEntity = bbsTopicService.getById(bbsTopic.getId());
        if (bbsTopicEntity == null) {
            return Result.error("未找到对应数据");
        }
        bbsTopicService.updateMain(bbsTopic, bbsTopicPage.getBbsTopicImageList(), bbsTopicPage.getBbsTopicTagList(), bbsTopicPage.getBbsTopicLinkList());
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "帖子-通过id删除")
    @ApiOperation(value = "帖子-通过id删除", notes = "帖子-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        bbsTopicService.delMain(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "帖子-批量删除")
    @ApiOperation(value = "帖子-批量删除", notes = "帖子-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.bbsTopicService.delBatchMain(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "帖子-通过id查询")
    @ApiOperation(value = "帖子-通过id查询", notes = "帖子-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        BbsTopic bbsTopic = bbsTopicService.getById(id);
        if (bbsTopic == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(bbsTopic);

    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "帖子图片通过主表ID查询")
    @ApiOperation(value = "帖子图片主表ID查询", notes = "帖子图片-通主表ID查询")
    @GetMapping(value = "/queryBbsTopicImageByMainId")
    public Result<?> queryBbsTopicImageListByMainId(@RequestParam(name = "id", required = true) String id) {
        List<BbsTopicImage> bbsTopicImageList = bbsTopicImageService.selectByMainId(id);
        return Result.OK(bbsTopicImageList);
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "帖子标签通过主表ID查询")
    @ApiOperation(value = "帖子标签主表ID查询", notes = "帖子标签-通主表ID查询")
    @GetMapping(value = "/queryBbsTopicTagByMainId")
    public Result<?> queryBbsTopicTagListByMainId(@RequestParam(name = "id", required = true) String id) {
        List<BbsTopicTag> bbsTopicTagList = bbsTopicTagService.selectByMainId(id);
        return Result.OK(bbsTopicTagList);
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "帖子跳转链接通过主表ID查询")
    @ApiOperation(value = "帖子跳转链接主表ID查询", notes = "帖子跳转链接-通主表ID查询")
    @GetMapping(value = "/queryBbsTopicLinkByMainId")
    public Result<?> queryBbsTopicLinkListByMainId(@RequestParam(name = "id", required = true) String id) {
        List<BbsTopicLink> bbsTopicLinkList = bbsTopicLinkService.selectByMainId(id);
        return Result.OK(bbsTopicLinkList);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param bbsTopic
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BbsTopic bbsTopic) {
        // Step.1 组装查询条件查询数据
        QueryWrapper<BbsTopic> queryWrapper = QueryGenerator.initQueryWrapper(bbsTopic, request.getParameterMap());
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        //Step.2 获取导出数据
        List<BbsTopic> queryList = bbsTopicService.list(queryWrapper);
        // 过滤选中数据
        String selections = request.getParameter("selections");
        List<BbsTopic> bbsTopicList = new ArrayList<BbsTopic>();
        if (oConvertUtils.isEmpty(selections)) {
            bbsTopicList = queryList;
        } else {
            List<String> selectionList = Arrays.asList(selections.split(","));
            bbsTopicList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
        }

        // Step.3 组装pageList
        List<BbsTopicPage> pageList = new ArrayList<BbsTopicPage>();
        for (BbsTopic main : bbsTopicList) {
            BbsTopicPage vo = new BbsTopicPage();
            BeanUtils.copyProperties(main, vo);
            List<BbsTopicImage> bbsTopicImageList = bbsTopicImageService.selectByMainId(main.getId());
            vo.setBbsTopicImageList(bbsTopicImageList);
            List<BbsTopicTag> bbsTopicTagList = bbsTopicTagService.selectByMainId(main.getId());
            vo.setBbsTopicTagList(bbsTopicTagList);
            List<BbsTopicLink> bbsTopicLinkList = bbsTopicLinkService.selectByMainId(main.getId());
            vo.setBbsTopicLinkList(bbsTopicLinkList);
            pageList.add(vo);
        }

        // Step.4 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "帖子列表");
        mv.addObject(NormalExcelConstants.CLASS, BbsTopicPage.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("帖子数据", "导出人:" + sysUser.getRealname(), "帖子"));
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
                List<BbsTopicPage> list = ExcelImportUtil.importExcel(file.getInputStream(), BbsTopicPage.class, params);
                for (BbsTopicPage page : list) {
                    BbsTopic po = new BbsTopic();
                    BeanUtils.copyProperties(page, po);
                    bbsTopicService.saveMain(po, page.getBbsTopicImageList(), page.getBbsTopicTagList(), page.getBbsTopicLinkList());
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
     * 分页查询完整帖子
     *
     * @param bbsTopic
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "帖子-分页查询完整帖子")
    @ApiOperation(value = "帖子-分页查询完整帖子", notes = "帖子-分页查询完整帖子")
    @GetMapping(value = "/wise/mini/fullList")
    public TopicResult<?> queryPageFullList(BbsTopic bbsTopic, int[] topicType, int classIndex, long timestampRequest,
                                            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                            @RequestParam(name = "pageSize", defaultValue = "15") Integer pageSize,
                                            HttpServletRequest req) {
        Page<BbsTopicFullDto> page = new Page<BbsTopicFullDto>(pageNo, pageSize);
        String regionCode = req.getHeader("regioncode");
        String classCode = req.getHeader("classCode");
        IPage<BbsTopicFullDto> bbsTopicFullDtos12 = bbsTopicFullDtoService.queryTopicFullDto(page, regionCode, classCode, topicType);

        //帖子浏览量+1
        ArrayList<BbsTopic> bbsTopics = new ArrayList<>();
        for (int i = 0; i < bbsTopicFullDtos12.getRecords().size(); i++) {
            bbsTopicFullDtos12.getRecords().get(i).setHitsCount(bbsTopicFullDtos12.getRecords().get(i).getHitsCount() + 1);
            BbsTopic bbsTopic1 = new BbsTopic();
            BeanUtils.copyProperties(bbsTopicFullDtos12.getRecords().get(i), bbsTopic1);
            bbsTopics.add(bbsTopic1);
        }
        try {
            boolean b = bbsTopicService.updateBatchById(bbsTopics);
        } catch (Exception e) {
            log.error(e.toString());
        }
        bbsRedisUtils.updateTopic(bbsTopicFullDtos12.getRecords());     //缓存更新

        return TopicResult.OK(bbsTopicFullDtos12, timestampRequest, classIndex);
    }

    /**
     * 根据关键词分页查询完整帖子
     *
     * @param bbsTopic
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "帖子-根据关键词分页查询完整帖子")
    @ApiOperation(value = "帖子-根据关键词分页查询完整帖子", notes = "帖子-根据关键词分页查询完整帖子")
    @GetMapping(value = "/wise/mini/fullListByKeyWord")
    public Result<?> fullListByKeyWord(BbsTopic bbsTopic, String keyWord,
                                       @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                       @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                       HttpServletRequest req) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        /**
         * 获取角色列表
         */
        Result<List<SysRole>> queryall = sysRoleController.queryall();

        QueryWrapper<BbsTopic> queryWrapper = QueryGenerator.initQueryWrapper(bbsTopic, req.getParameterMap()).orderByDesc("create_time");
        //添加区域筛选
        queryWrapper.eq("region_code", req.getHeader("regioncode"))
                .like("content", keyWord)
                .or()
                .like("title", keyWord);
        String classCode = req.getHeader("classCode");
        if (!"index".equals(classCode)) {
            queryWrapper.eq("class_code", req.getHeader("classCode"));
        }
        Page<BbsTopic> page = new Page<BbsTopic>(pageNo, pageSize);
        IPage<BbsTopic> pageList = bbsTopicService.page(page, queryWrapper);

        IPage<BbsTopicFullDto> bbsTopicFullDtos = new Page<>();
        List<BbsTopicFullDto> bbsTopicFullDtos1 = new ArrayList<>();
        for (BbsTopic record : pageList.getRecords()) {
            BbsTopicFullDto bbsTopicFullDto = new BbsTopicFullDto();
            BeanUtils.copyProperties(record, bbsTopicFullDto);
            bbsTopicFullDtos1.add(bbsTopicFullDto);
        }
        bbsTopicFullDtos.setRecords(bbsTopicFullDtos1);
        bbsTopicFullDtos.setTotal(pageList.getTotal());
        bbsTopicFullDtos.setSize(pageList.getSize());
        bbsTopicFullDtos.setCurrent(pageList.getCurrent());

        for (BbsTopicFullDto bbsTopicFullDto : bbsTopicFullDtos.getRecords()) {
            //查询条件mybatis中自带
            QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
            wrapper.eq("id", bbsTopicFullDto.getCreateBy());
            SysUser byId = sysUserService.getOne(wrapper);
            List<BbsTopicTag> bbsTags = bbsTopicTagService.selectByMainId(bbsTopicFullDto.getId());

            Map<String, Object> map = new HashMap<>();
            map.put("topic_id", bbsTopicFullDto.getId());
            List<BbsTopicImage> bbsTopicImageList = bbsTopicImageService.listByMap(map);

            /**
             * 获取用户角色 根据用户id获取Role
             */
            //查询用户角色id
            Result<List<String>> listResult = sysUserController.queryUserRole(bbsTopicFullDto.getCreateBy());
            //系统支持用户多角色，但默认每个用户有一个角色
            if (listResult.getResult() == null || listResult.getResult().size() <= 0) {
                log.error("帖子-未找到角色信息");
            } else if (queryall.getResult() == null || queryall.getResult().size() <= 0) {
                log.error("帖子-未找到角色信息");
            } else {
                for (SysRole sysRole : queryall.getResult()) {
                    if (sysRole.getId().equals(listResult.getResult().get(0))) {
                        bbsTopicFullDto.setUserRole(sysRole.getRoleName());
                    }
                }
            }
            /**
             * 根据帖子id和用户id获取用户点赞记录 判断用户对该帖子是否点赞
             */
            BbsUserPraise bbsUserIsPraise = bbsUserPraiseService.lambdaQuery().eq(BbsUserPraise::getTopicId, bbsTopicFullDto.getId()).eq(BbsUserPraise::getCreateBy, sysUser.getUsername()).one();
            if (null != bbsUserIsPraise) {
                bbsTopicFullDto.setUserIsPraise(true);
            } else {
                bbsTopicFullDto.setUserIsPraise(false);
            }

            /**
             * 根据帖子id和用户id获取用户收藏记录 判断用户对该帖子是否收藏
             */
            BbsUserStar bbsUserStar = bbsUserStarService.lambdaQuery().eq(BbsUserStar::getTopicId, bbsTopicFullDto.getId()).eq(BbsUserStar::getCreateBy, sysUser.getUsername()).one();
            if (null != bbsUserStar) {
                bbsTopicFullDto.setUserIsStar(true);
            } else {
                bbsTopicFullDto.setUserIsStar(false);
            }

            bbsTopicFullDto.setBbsCreateByName(byId.getRealname());
            bbsTopicFullDto.setAvatar(byId.getAvatar());
            bbsTopicFullDto.setBbsTopicTagList(bbsTags);
            bbsTopicFullDto.setBbsTopicImageList(bbsTopicImageList);
        }
        return Result.OK(bbsTopicFullDtos);
    }

    /**
     * 根据id查询完整帖子
     *
     * @param topicId
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "帖子-根据id查询完整帖子")
    @ApiOperation(value = "帖子-根据id查询完整帖子", notes = "帖子-根据id查询完整帖子")
    @GetMapping(value = "/wise/mini/fullListById")
    public Result<?> fullListById(@RequestParam(value = "topicId") String topicId,
                                  HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        BbsTopicFullDto bbsTopicFullDto = bbsTopicFullDtoService.queryTopicFullDtoById(topicId);
        if (null == bbsTopicFullDto) {
            return Result.error(1005, "id为" + topicId + "的贴子不存在。");
        }
        //只有1条，但还是封装list
        List<String> topicIdList = new ArrayList<>();
        topicIdList.add(bbsTopicFullDto.getId());
        //根据用户进行数据封装
        if (topicIdList.size() != 0) {
            List<BbsUserStar> bbsUserStars = bbsTopicFullDtoService.queryTopicFullDtoUserStar(topicIdList, loginUser.getUsername());
            List<BbsUserPraise> bbsUserPraises = bbsTopicFullDtoService.queryTopicFullDtoUserPraise(topicIdList, loginUser.getUsername());
            //封装用户点赞和收藏信息
            if (!bbsUserStars.isEmpty()) {
                bbsTopicFullDto.setUserIsStar(true);
            }
            if (!bbsUserPraises.isEmpty()) {
                bbsTopicFullDto.setUserIsPraise(true);
            }
        }
        bbsAuthUtils.getMiNiStorageFromSql(loginUser.getUsername());
        return Result.OK(bbsTopicFullDto);
    }

    /**
     * 根据id查询完整帖子
     *
     * @param topicId
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "帖子-根据id查询完整帖子")
    @ApiOperation(value = "帖子-根据id查询完整帖子", notes = "帖子-根据id查询完整帖子")
    @GetMapping(value = "/wise/mini/fullListById/anon")
    public Result<?> fullListByIdAnon(@RequestParam(value = "topicId") String topicId,
                                      HttpServletRequest req) {
        BbsTopicFullDto bbsTopicFullDto = bbsTopicFullDtoService.queryTopicFullDtoByIdAnon(topicId);
        if (null == bbsTopicFullDto) {
            return Result.error(1005, "id为" + topicId + "的贴子不存在。");
        }
        return Result.OK(bbsTopicFullDto);
    }

    /**
     * 用户发布帖子
     *
     * @param bbsTopic
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "用户发布帖子-分页查询完整帖子")
    @ApiOperation(value = "用户发布帖子-分页查询完整帖子", notes = "用户发布帖子-分页查询完整帖子")
    @GetMapping(value = "/wise/mini/queryPagePublishFullList")
    public Result<?> queryPagePublishFullList(BbsTopic bbsTopic, String username,
                                              @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                              @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                              HttpServletRequest req) {

        Page<BbsTopicFullDto> page = new Page<BbsTopicFullDto>(pageNo, pageSize);
        //加载贴子类型，普通加载只能加载到0,1,2 普通，置顶，精华。5代表为公共隐藏类型，只能通过单独加载
        int[] topicType = new int[]{0, 1, 2};
        IPage<BbsTopicFullDto> bbsTopicFullDtos12 = bbsTopicFullDtoService.queryUserPublishTopicFullDto(page, req, topicType, username);

        return Result.OK(bbsTopicFullDtos12);
    }

    /**
     * 添加
     *
     * @param bbsTopicPage
     * @return
     */
    @AutoLog(value = "帖子-添加")
    @ApiOperation(value = "帖子-添加", notes = "帖子-添加")
    @PostMapping(value = "/wise/mini/add")
    public Result<?> add(@RequestBody BbsTopicPage bbsTopicPage,
                         HttpServletRequest req) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        if (!bbsAuthUtils.judgeMiniUserAuth())
            return Result.error(1000, "未授权,无法发布。");

        BbsRegion regionOne = bbsRegionService.lambdaQuery().eq(BbsRegion::getRegionCode, req.getHeader("regioncode")).one();

        ContentCheck contentCheck = new ContentCheck();
        //标题审核
        if (null != bbsTopicPage.getTitle() && !bbsTopicPage.getTitle().isEmpty()) {
            Result<?> result1 = contentCheck.checkBySensitiveWord(bbsTopicPage.getTitle());
            if (result1.isSuccess()) {
                Result<?> result2 = contentCheck.checkByWeiXin(bbsTopicPage.getTitle());
                if (!result2.isSuccess()) {
                    result2.setMessage("标题" + result2.getMessage());
                    return result2;
                }
            } else {
                result1.setMessage("标题" + result1.getMessage());
                return result1;
            }
        }
        //内容审核
        Result<?> result3 = contentCheck.checkBySensitiveWord(bbsTopicPage.getContent());
        if (result3.isSuccess()) {
            Result<?> result4 = contentCheck.checkByWeiXin(bbsTopicPage.getContent());
            if (!result4.isSuccess()) {
                result4.setMessage("内容" + result4.getMessage());
                return result4;
            }
        } else {
            result3.setMessage("内容" + result3.getMessage());
            return result3;
        }
        // 用户记录 topic_count +1 maizi + 20 近期发布贴子 getTopicCount+1最近发布贴子时间更新
        BbsUserRecord bbsUserRecordOne = bbsUserRecordService.lambdaQuery().eq(BbsUserRecord::getCreateBy, sysUser.getUsername()).one();
        //判断发布贴子数量是否超出限制
        if (bbsUserRecordOne.getDayPublishTopic() >= regionOne.getDayPublishTopic()) {
            return Result.error("今天发布信息" + regionOne.getDayPublishTopic() + "条，超出限制，请等明天再次尝试！");
        } else if (bbsUserRecordOne.getWeekPublishTopic() >= regionOne.getWeekPublishTopic()) {
            return Result.error("本周发布信息" + regionOne.getWeekPublishTopic() + "条，超出限制，请等下周再次尝试！");
        }

        BbsTopic bbsTopic = new BbsTopic();
        //发布日期添加
        bbsTopicPage.setPublicTime(new Date());
        //编辑时间默认和发布时间相同
        bbsTopicPage.setEditTime(new Date());
        //添加区域、区域全名
        bbsTopicPage.setRegionCode(req.getHeader("regioncode"));
        bbsTopicPage.setRegionFullName(regionOne.getFullName());

        BeanUtils.copyProperties(bbsTopicPage, bbsTopic);
        bbsTopicService.saveMain(bbsTopic, bbsTopicPage.getBbsTopicImageList(), bbsTopicPage.getBbsTopicTagList(), null);

        boolean b1 = bbsUserRecordService.lambdaUpdate()
                .eq(BbsUserRecord::getCreateBy, sysUser.getUsername())
                .set(BbsUserRecord::getStoneCount, bbsUserRecordOne.getStoneCount() + 20)
                .set(BbsUserRecord::getTopicCount, bbsUserRecordOne.getTopicCount() + 1)
                .set(BbsUserRecord::getDayPublishTopic, bbsUserRecordOne.getDayPublishTopic() + 1)  //当天发布贴子数+1
                .set(BbsUserRecord::getWeekPublishTopic, bbsUserRecordOne.getWeekPublishTopic() + 1)  //本周发布贴子数+1
                .update();

        return Result.OK("添加成功！");
    }


    /**
     * 编辑
     *
     * @param bbsTopicPage
     * @return
     */
    @AutoLog(value = "帖子-修改")
    @ApiOperation(value = "帖子-修改", notes = "帖子-修改")
    @PostMapping(value = "/wise/mini/edit")
    public Result<?> edit(@RequestBody BbsTopicPage bbsTopicPage,
                          HttpServletRequest req) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        BbsRegion regionOne = bbsRegionService.lambdaQuery().eq(BbsRegion::getRegionCode, req.getHeader("regioncode")).one();

        ContentCheck contentCheck = new ContentCheck();
        //标题审核
        if (null != bbsTopicPage.getTitle() && !bbsTopicPage.getTitle().isEmpty()) {
            Result<?> result1 = contentCheck.checkBySensitiveWord(bbsTopicPage.getTitle());
            if (result1.isSuccess()) {
                Result<?> result2 = contentCheck.checkByWeiXin(bbsTopicPage.getTitle());
                if (!result2.isSuccess()) {
                    result2.setMessage("标题" + result2.getMessage());
                    return result2;
                }
            } else {
                result1.setMessage("标题" + result1.getMessage());
                return result1;
            }
        }
        //内容审核
        Result<?> result3 = contentCheck.checkBySensitiveWord(bbsTopicPage.getContent());
        if (result3.isSuccess()) {
            Result<?> result4 = contentCheck.checkByWeiXin(bbsTopicPage.getContent());
            if (!result4.isSuccess()) {
                result4.setMessage("内容" + result4.getMessage());
                return result4;
            }
        } else {
            result3.setMessage("内容" + result3.getMessage());
            return result3;
        }
        BbsTopic bbsTopic = new BbsTopic();

        //编辑时间
        bbsTopicPage.setEditTime(new Date());
        //添加区域、区域全名
        bbsTopicPage.setRegionCode(req.getHeader("regioncode"));
        bbsTopicPage.setRegionFullName(regionOne.getFullName());

        BeanUtils.copyProperties(bbsTopicPage, bbsTopic);
        bbsTopicService.updateMain(bbsTopic, bbsTopicPage.getBbsTopicImageList(), bbsTopicPage.getBbsTopicTagList(), null);
        return Result.OK("编辑成功！");
    }


    /**
     * 根据关键词搜索贴子
     *
     * @param keyword
     * @return
     */
    @AutoLog(value = "帖子-根据关键词搜索贴子")
    @ApiOperation(value = "帖子-根据关键词搜索贴子", notes = "帖子-根据关键词搜索贴子")
    @GetMapping(value = "/wise/mini/searchTopicByKeyword")
    public Result<?> searchTopicByKeyword(String keyword, HttpServletRequest req,
                                          @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        return bbsTopicService.searchTopicByKeyword(keyword, req, pageNo, pageSize);
    }

    /**
     * 通过id删除
     *
     * @param topicId
     * @return
     */
    @AutoLog(value = "帖子-通过id删除")
    @ApiOperation(value = "帖子-通过id删除", notes = "帖子-通过id删除")
    @DeleteMapping(value = "/wise/mini/deletePublishTopic")
    public Result<?> deletePublishTopic(@RequestParam(name = "topicId", required = true) String topicId) {

        return bbsTopicService.deletePublishTopic(topicId);

    }

    /**
     * 用户点击贴子
     *
     * @param topicId
     * @return
     */
    @AutoLog(value = "帖子-用户点击贴子")
    @ApiOperation(value = "帖子-用户点击贴子", notes = "帖子-用户点击贴子")
    @GetMapping(value = "/wise/mini/userClickTopic")
    public Result<?> userClickTopic(@RequestParam(value = "topicId") String topicId) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        // 贴子浏览量
        BbsTopic bbsTopicOne = bbsTopicService.lambdaQuery().eq(BbsTopic::getId, topicId).one();
        Date date = new Date();
        // 为撑场面暂时多加几个
        if (date.getHours() > 8 && date.getHours() < 23) {
            Random random = new Random();
            boolean update = bbsTopicService.lambdaUpdate()
                    .eq(BbsTopic::getId, topicId)
                    .set(BbsTopic::getHitsCount, bbsTopicOne.getHitsCount() + random.nextInt(5)).update();
        } else {
            boolean update = bbsTopicService.lambdaUpdate().eq(BbsTopic::getId, topicId).set(BbsTopic::getHitsCount, bbsTopicOne.getHitsCount() + 1).update();
        }

        // 用户浏览记录追加
        BbsUserTopicClick bbsUserTopicClick = new BbsUserTopicClick();
        bbsUserTopicClick.setTopicId(topicId);
        bbsUserTopicClickController.add(bbsUserTopicClick);
        // 用户记录 maizi+1 click_topic_count+1
        BbsUserRecord bbsUserRecordOne = bbsUserRecordService.lambdaQuery().eq(BbsUserRecord::getCreateBy, sysUser.getUsername()).one();
        boolean b = bbsUserRecordService.lambdaUpdate()
                .eq(BbsUserRecord::getCreateBy, sysUser.getUsername())
                .set(BbsUserRecord::getStoneCount, bbsUserRecordOne.getStoneCount() + 1)
                .set(BbsUserRecord::getClickTopicCount, bbsUserRecordOne.getClickTopicCount() + 1)
                .update();
        return Result.ok();
    }


    /**
     * 分页列表查询
     *
     * @param bbsTopic
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "帖子-分页列表查询")
    @ApiOperation(value = "帖子-分页列表查询", notes = "帖子-分页列表查询")
    @GetMapping(value = "/wise/back/list")
    @PermissionData(pageComponent = "bbs/content/topic/BbsTopicList")
    public Result<?> queryPageListWiseBack(BbsTopic bbsTopic,
                                           @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                           @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                           HttpServletRequest req) {
        Map<String, String[]> parameterMap = req.getParameterMap();
        QueryWrapper<BbsTopic> queryWrapper = QueryGenerator.initQueryWrapper(bbsTopic, req.getParameterMap());

        Page<BbsTopic> page = new Page<BbsTopic>(pageNo, pageSize);
        IPage<BbsTopic> pageList = bbsTopicService.page(page, queryWrapper);

        //补全用户昵称、头像、版块名
        for (int i = 0; i < pageList.getRecords().size(); i++) {
            SysUser userByName = sysUserService.getUserByName(pageList.getRecords().get(i).getCreateBy());

            pageList.getRecords().get(i).setCreateByName(userByName.getRealname());
            pageList.getRecords().get(i).setCreateByAvatar(userByName.getAvatar());

            //通用帖没有区域版块
            BbsClass bbsClass = bbsClassService.lambdaQuery()
                    .eq(BbsClass::getClassCode, pageList.getRecords().get(i).getClassCode())
                    .eq(BbsClass::getRegionCode, pageList.getRecords().get(i).getRegionCode())
                    .one();
            if (!pageList.getRecords().get(i).getTopicType().equals("5")) {
                pageList.getRecords().get(i).setClassName(bbsClass.getClassName());
            }
        }
        return Result.OK(pageList);
    }


    /**
     * 添加
     *
     * @param bbsTopicPage
     * @return
     */
    @AutoLog(value = "帖子-添加")
    @ApiOperation(value = "帖子-添加", notes = "帖子-添加")
    @PostMapping(value = "/wise/back/add")
    public Result<?> addWiseBack(@RequestBody BbsTopicPage bbsTopicPage) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        BbsTopic bbsTopic = new BbsTopic();
        BeanUtils.copyProperties(bbsTopicPage, bbsTopic);

        //创建贴子如果是置顶帖，发布时间改为2100.1.1，版块为index,区域为用户当前所在区域
        if ("1".equals(bbsTopic.getTopicType())) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date d = sdf.parse("2100-01-01 00:00:00");
                bbsTopic.setPublicTime(d);

                bbsTopic.setEditTime(new Date());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        //添加区域、区域全名
        BbsUserRecord bbsUserRecord = bbsUserRecordService.lambdaQuery()
                .eq(BbsUserRecord::getCreateBy, sysUser.getUsername())
                .one();
        BbsRegion bbsRegion = bbsRegionService.lambdaQuery().eq(BbsRegion::getRegionCode, bbsUserRecord.getRegionCode()).one();
        if (null == bbsTopic.getClassCode() || bbsTopic.getClassCode().isEmpty()) {
            bbsTopic.setClassCode("index");
        }
        bbsTopic.setRegionCode(bbsUserRecord.getRegionCode());
        bbsTopic.setRegionFullName(bbsRegion.getFullName());

        bbsTopicService.saveMain(bbsTopic, bbsTopicPage.getBbsTopicImageList(), bbsTopicPage.getBbsTopicTagList(), bbsTopicPage.getBbsTopicLinkList());
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param bbsTopicPage
     * @return
     */
    @AutoLog(value = "帖子-编辑")
    @ApiOperation(value = "帖子-编辑", notes = "帖子-编辑")
    @PutMapping(value = "/wise/back/edit")
    public Result<?> editWiseBack(@RequestBody BbsTopicPage bbsTopicPage) {
        BbsTopic bbsTopic = new BbsTopic();
        BeanUtils.copyProperties(bbsTopicPage, bbsTopic);
        BbsTopic bbsTopicEntity = bbsTopicService.getById(bbsTopic.getId());
        if (bbsTopicEntity == null) {
            return Result.error("未找到对应数据");
        }
        //编辑时间默认和发布时间相同
        bbsTopic.setEditTime(new Date());
        bbsTopicService.updateMain(bbsTopic, bbsTopicPage.getBbsTopicImageList(), bbsTopicPage.getBbsTopicTagList(), bbsTopicPage.getBbsTopicLinkList());
        return Result.OK("编辑成功!");
    }


    /**
     * 通过id删除
     *
     * @param topicId
     * @return
     */
    @AutoLog(value = "帖子-通过id删除")
    @ApiOperation(value = "帖子-通过id删除", notes = "帖子-通过id删除")
    @DeleteMapping(value = "/wise/back/delete")
    public Result<?> deleteBackPublishTopic(@RequestParam(name = "id", required = true) String ids) {
        bbsTopicService.deletePublishTopic(ids);
        return Result.OK("删除成功!");
    }

    /**
     * 通过id删除
     *
     * @param topicId
     * @return
     */
    @AutoLog(value = "帖子-通过id删除")
    @ApiOperation(value = "帖子-通过id删除", notes = "帖子-通过id删除")
    @DeleteMapping(value = "/wise/back/deleteBatch")
    public Result<?> deleteBackPublishTopicBatch(@RequestParam(name = "ids", required = true) String ids) {
        bbsTopicService.deletePublishTopicBatch(Arrays.asList(ids.split(",")));
        return Result.OK("删除成功!");
    }
}
