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
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.bbs.entity.*;
import org.jeecg.modules.bbs.service.IBbsTopicImageService;
import org.jeecg.modules.bbs.service.IBbsTopicTagService;
import org.jeecg.modules.bbs.service.IBbsUserStarService;
import org.jeecg.modules.bbs.service.impl.BbsTopicServiceImpl;
import org.jeecg.modules.bbs.service.impl.BbsUserPraiseServiceImpl;
import org.jeecg.modules.bbs.service.impl.BbsUserRecordServiceImpl;
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
import java.beans.Transient;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 用户收藏
 * @Author: jeecg-boot
 * @Date: 2021-01-12
 * @Version: V1.0
 */
@Api(tags = "用户收藏")
@RestController
@RequestMapping("/bbs/bbsUserStar")
@Slf4j
public class BbsUserStarController extends JeecgController<BbsUserStar, IBbsUserStarService> {
    @Autowired
    private IBbsUserStarService bbsUserStarService;
    @Autowired
    private BbsTopicServiceImpl bbsTopicService;
    @Autowired
    private BbsUserRecordServiceImpl bbsUserRecordService;
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
    private BbsUserPraiseServiceImpl bbsUserPraiseService;
    @Autowired
    private ISysRoleService sysRoleService;


    /**
     * 添加
     *
     * @param bbsUserStar
     * @return
     */
    @AutoLog(value = "用户收藏-添加")
    @ApiOperation(value = "用户收藏-添加", notes = "用户收藏-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody BbsUserStar bbsUserStar) {
        bbsUserStarService.save(bbsUserStar);
        return Result.OK("添加成功！");
    }


    /**
     * 编辑
     *
     * @param bbsUserStar
     * @return
     */
    @AutoLog(value = "用户收藏-编辑")
    @ApiOperation(value = "用户收藏-编辑", notes = "用户收藏-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody BbsUserStar bbsUserStar) {
        bbsUserStarService.updateById(bbsUserStar);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "用户收藏-通过id删除")
    @ApiOperation(value = "用户收藏-通过id删除", notes = "用户收藏-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        bbsUserStarService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "用户收藏-批量删除")
    @ApiOperation(value = "用户收藏-批量删除", notes = "用户收藏-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.bbsUserStarService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "用户收藏-通过id查询")
    @ApiOperation(value = "用户收藏-通过id查询", notes = "用户收藏-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        BbsUserStar bbsUserStar = bbsUserStarService.getById(id);
        if (bbsUserStar == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(bbsUserStar);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param bbsUserStar
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BbsUserStar bbsUserStar) {
        return super.exportXls(request, bbsUserStar, BbsUserStar.class, "用户收藏");
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
        return super.importExcel(request, response, BbsUserStar.class);
    }


    // ****行星万象修改位置戳****

    /**
     * 分页列表查询
     *
     * @param bbsUserStar
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "用户收藏-分页列表查询")
    @ApiOperation(value = "用户收藏-分页列表查询", notes = "用户收藏-分页列表查询")
    @GetMapping(value = "/wise/mini/list")
    public Result<?> queryPageList(BbsUserStar bbsUserStar,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        /**
         * 获取角色列表
         */
        Result<List<SysRole>> queryall = sysRoleController.queryall();

        QueryWrapper<BbsUserStar> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("create_by", sysUser.getUsername()).orderByDesc("create_time");

        Page<BbsUserStar> page = new Page<BbsUserStar>(pageNo, pageSize);

        IPage<BbsUserStar> page1 = bbsUserStarService.page(page, queryWrapper);
        if (0 == page1.getRecords().size()) {
            return Result.OK();
        }
        List<BbsUserStar> records = page1.getRecords();
        List<String> stringObjectHashMap = new ArrayList<>();
        for (BbsUserStar userStar : page1.getRecords()) {
            stringObjectHashMap.add(userStar.getTopicId());
        }
        List<BbsTopic> bbsTopics = bbsTopicService.listByIds(stringObjectHashMap);
        List<BbsTopicFullDto> bbsTopicFullDtos1 = new ArrayList<>();
        for (int i = 0; i < bbsTopics.size(); i++) {
            BbsTopicFullDto bbsTopicFullDto = new BbsTopicFullDto();
            BeanUtils.copyProperties(bbsTopics.get(i), bbsTopicFullDto);
            //收藏时间
            bbsTopicFullDto.setCreateTime(page1.getRecords().get(i).getCreateTime());
            bbsTopicFullDtos1.add(bbsTopicFullDto);
        }

        for (BbsTopicFullDto bbsTopicFullDto : bbsTopicFullDtos1) {
            SysUser byId = sysUserService.getUserByName(bbsTopicFullDto.getCreateBy());
            List<BbsTopicTag> bbsTags = bbsTopicTagService.selectByMainId(bbsTopicFullDto.getId());

            Map<String, Object> map = new HashMap<>();
            map.put("topic_id", bbsTopicFullDto.getId());
            List<BbsTopicImage> bbsTopicImageList = bbsTopicImageService.listByMap(map);

            //查询用户角色
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
        List<BbsTopicFullDto> orderList = bbsTopicFullDtos1.stream().sorted(Comparator.comparing(BbsTopicFullDto::getCreateTime).reversed()).collect(Collectors.toList());

        return Result.OK(orderList);
    }

    /**
     * 收藏/取消收藏
     *
     * @param topicId
     * @return
     */
    @Transient
    @AutoLog(value = "用户收藏/取消收藏")
    @ApiOperation(value = "用户收藏/取消收藏", notes = "用户收藏/取消收藏")
    @PostMapping(value = "/wise/mini/clickStar")
    public Result<?> clickStar(String topicId, @RequestParam(value = "isStar") Boolean isStar) {
        if (null == topicId) {
            return Result.error("贴子Id不能为空");
        }
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        /***
         * 1、如果isStar==true，BbsUserStar插入
         * 2、帖子收藏量+1
         * 3、用户记录 不存在就创建 麦子+5 用户收藏量+1
         *
         * 1、如果isStar==false，否则，BbsUserStar删除
         * 2、帖子收藏量-1
         * 3、用户记录 不存在就创建 麦子-5 用户收藏量-1
         */
        BbsUserStar oneBbsUserStar = bbsUserStarService.lambdaQuery().eq(BbsUserStar::getCreateBy, sysUser.getUsername()).eq(BbsUserStar::getTopicId, topicId).one();
        BbsTopic oneBbsTopic = bbsTopicService.lambdaQuery().eq(BbsTopic::getId, topicId).one();
        BbsUserRecord oneBbsUserRecord = bbsUserRecordService.lambdaQuery().eq(BbsUserRecord::getCreateBy, sysUser.getUsername()).one();
        if (isStar) {
            //1、如果one1==null，BbsUserStar插入收藏记录，否则，提示
            if (null == oneBbsUserStar) {
                BbsUserStar bbsUserStar = new BbsUserStar();
                bbsUserStar.setCreateBy(sysUser.getUsername());
                bbsUserStar.setCreateTime(new Date());
                bbsUserStar.setTopicId(topicId);
                bbsUserStarService.save(bbsUserStar);
            } else {
                return Result.error("已收藏，无法再次收藏");
            }
            //2、帖子收藏量+1
            oneBbsTopic.setStarCount(oneBbsTopic.getStarCount() + 1);
            bbsTopicService.updateById(oneBbsTopic);
            //3、用户记录 麦子+5 用户收藏量+1
            bbsUserRecordService.lambdaUpdate().eq(BbsUserRecord::getCreateBy, sysUser.getUsername())
                    .set(BbsUserRecord::getStoneCount, oneBbsUserRecord.getStoneCount() + 5)
                    .set(BbsUserRecord::getStarCount, oneBbsUserRecord.getStarCount() + 1).update();
        } else {
            //1、如果isStar==false，BbsUserStar删除
            if (null == oneBbsUserStar) {
                return Result.error("未收藏，无法取消收藏");
            } else {
                Map<String, Object> hashMap = new HashMap<>();
                hashMap.put("topic_id", topicId);
                hashMap.put("create_by", sysUser.getUsername());
                boolean b = bbsUserStarService.removeByMap(hashMap);
                if (!b) {
                    return Result.error("无法取消收藏");
                }
            }
            //2、帖子收藏量-1
            oneBbsTopic.setStarCount(oneBbsTopic.getStarCount() - 1);
            bbsTopicService.updateById(oneBbsTopic);
            //3、用户记录 麦子-5 用户收藏量-1
            boolean b = bbsUserRecordService.lambdaUpdate().eq(BbsUserRecord::getCreateBy, sysUser.getUsername())
                    .set(BbsUserRecord::getStoneCount, oneBbsUserRecord.getStoneCount() - 5)
                    .set(BbsUserRecord::getStarCount, oneBbsUserRecord.getStarCount() - 1).update();
        }
        return Result.OK("成功！");
    }
}
