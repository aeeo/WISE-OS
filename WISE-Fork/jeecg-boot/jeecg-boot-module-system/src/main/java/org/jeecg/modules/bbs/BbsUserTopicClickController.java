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
import org.jeecg.modules.bbs.entity.*;
import org.jeecg.modules.bbs.service.*;
import org.jeecg.modules.bbs.service.impl.*;
import org.jeecg.modules.system.controller.SysRoleController;
import org.jeecg.modules.system.controller.SysUserController;
import org.jeecg.modules.system.entity.SysRole;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.ISysRoleService;
import org.jeecg.modules.system.service.ISysUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 用户浏览记录表
 * @Author: jeecg-boot
 * @Date: 2021-01-07
 * @Version: V1.0
 */
@Api(tags = "用户浏览记录表")
@RestController
@RequestMapping("/bbs/bbsUserTopicClick")
@Slf4j
public class BbsUserTopicClickController extends JeecgController<BbsUserTopicClick, IBbsUserTopicClickService> {
    @Autowired
    private IBbsUserTopicClickService bbsUserTopicClickService;
    @Autowired
    private IBbsTopicService bbsTopicService;
    @Autowired
    private IBbsTopicImageService bbsTopicImageService;
    @Autowired
    private IBbsTopicTagService bbsTopicTagService;
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


    /**
     * 添加
     *
     * @param bbsUserTopicClick
     * @return
     */
    @AutoLog(value = "用户浏览记录表-添加")
    @ApiOperation(value = "用户浏览记录表-添加", notes = "用户浏览记录表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody BbsUserTopicClick bbsUserTopicClick) {
        bbsUserTopicClickService.save(bbsUserTopicClick);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param bbsUserTopicClick
     * @return
     */
    @AutoLog(value = "用户浏览记录表-编辑")
    @ApiOperation(value = "用户浏览记录表-编辑", notes = "用户浏览记录表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody BbsUserTopicClick bbsUserTopicClick) {
        bbsUserTopicClickService.updateById(bbsUserTopicClick);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "用户浏览记录表-通过id删除")
    @ApiOperation(value = "用户浏览记录表-通过id删除", notes = "用户浏览记录表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        bbsUserTopicClickService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "用户浏览记录表-批量删除")
    @ApiOperation(value = "用户浏览记录表-批量删除", notes = "用户浏览记录表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.bbsUserTopicClickService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "用户浏览记录表-通过id查询")
    @ApiOperation(value = "用户浏览记录表-通过id查询", notes = "用户浏览记录表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        BbsUserTopicClick bbsUserTopicClick = bbsUserTopicClickService.getById(id);
        if (bbsUserTopicClick == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(bbsUserTopicClick);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param bbsUserTopicClick
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BbsUserTopicClick bbsUserTopicClick) {
        return super.exportXls(request, bbsUserTopicClick, BbsUserTopicClick.class, "用户浏览记录表");
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
        return super.importExcel(request, response, BbsUserTopicClick.class);
    }


    // ****行星万象修改位置戳****

    /**
     * 分页列表查询
     *
     * @param bbsUserTopicClick
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "用户浏览记录表-分页列表查询")
    @ApiOperation(value = "用户浏览记录表-分页列表查询", notes = "用户浏览记录表-分页列表查询")
    @GetMapping(value = "/wise/mini/list")
    public Result<?> queryPageList(BbsUserTopicClick bbsUserTopicClick,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        QueryWrapper<BbsUserTopicClick> queryWrapper1 = QueryGenerator.initQueryWrapper(bbsUserTopicClick, req.getParameterMap()).orderByDesc("create_time");
        queryWrapper1.eq("user_id", sysUser.getId());
        Page<BbsUserTopicClick> page2 = new Page<>(pageNo, pageSize);


        /**
         * 获取角色列表
         */
        Result<List<SysRole>> queryall = sysRoleController.queryall();

        QueryWrapper<BbsUserStar> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("create_by", sysUser.getUsername()).orderByDesc("create_time");

        //用户浏览列表
        //方案一
        //	SELECT topic_id,max(create_time) from bbs_user_topic_click GROUP BY topic_id
        IPage<BbsUserTopicClick> pageList = bbsUserTopicClickService.queryRecently(page2, sysUser.getUsername());


        //方案二
        //IPage<BbsUserTopicClick> pageList = bbsUserTopicClickService.page(page2, queryWrapper1);


        if (0 == pageList.getRecords().size()) {
            return Result.OK();
        }
        List<BbsUserTopicClick> records = pageList.getRecords();
        List<String> stringObjectHashMap = new ArrayList<>();
        for (BbsUserTopicClick userTopicClick : pageList.getRecords()) {
            stringObjectHashMap.add(userTopicClick.getTopicId());
        }
        List<BbsTopic> bbsTopics = bbsTopicService.listByIds(stringObjectHashMap);
        List<BbsTopicFullDto> bbsTopicFullDtos1 = new ArrayList<>();
        for (BbsTopic item : bbsTopics) {
            BbsTopicFullDto bbsTopicFullDto = new BbsTopicFullDto();
            BeanUtils.copyProperties(item, bbsTopicFullDto);
            bbsTopicFullDtos1.add(bbsTopicFullDto);
        }

        for (BbsTopicFullDto bbsTopicFullDto : bbsTopicFullDtos1) {

            SysUser byId = sysUserService.getUserByName(bbsTopicFullDto.getCreateBy());
            List<BbsTopicTag> bbsTags = bbsTopicTagService.selectByMainId(bbsTopicFullDto.getId());

            Map<String, Object> map = new HashMap<>();
            map.put("topic_id", bbsTopicFullDto.getId());
            List<BbsTopicImage> bbsTopicImageList = bbsTopicImageService.listByMap(map);

            //查询用户角色id
            List<String> roles = sysUserService.getRole(bbsTopicFullDto.getCreateBy());
            SysRole sysRole = sysRoleService.lambdaQuery().eq(SysRole::getRoleCode, roles.get(0)).one();

            //系统支持用户多角色，但默认每个用户有一个角色
            if (roles == null || roles.size() <= 0) {
                log.error("帖子-未找到角色信息");
            } else {
                bbsTopicFullDto.setUserRole(sysRole.getRoleName());
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
            BbsUserStar bbsTopicUserStar = bbsUserStarService.lambdaQuery().eq(BbsUserStar::getTopicId, bbsTopicFullDto.getId()).eq(BbsUserStar::getCreateBy, sysUser.getUsername()).one();
            if (null != bbsTopicUserStar) {
                bbsTopicFullDto.setUserIsStar(true);
            } else {
                bbsTopicFullDto.setUserIsStar(false);
            }

            bbsTopicFullDto.setBbsCreateByName(byId.getRealname());
            bbsTopicFullDto.setAvatar(byId.getAvatar());
            bbsTopicFullDto.setBbsTopicTagList(bbsTags);
            bbsTopicFullDto.setBbsTopicImageList(bbsTopicImageList);
        }

        //替换用户浏览时间
        for (BbsUserTopicClick record : pageList.getRecords()) {
            for (BbsTopicFullDto bbsTopicFullDto : bbsTopicFullDtos1) {
                if (record.getTopicId().equals(bbsTopicFullDto.getId())) {
                    bbsTopicFullDto.setCreateTime(record.getCreateTime());
                }
            }
        }
        List<BbsTopicFullDto> orderList = bbsTopicFullDtos1.stream().sorted(Comparator.comparing(BbsTopicFullDto::getCreateTime).reversed()).collect(Collectors.toList());

        return Result.OK(orderList);
    }

}
