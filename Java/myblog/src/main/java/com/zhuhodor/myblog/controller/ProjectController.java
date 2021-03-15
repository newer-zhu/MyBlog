package com.zhuhodor.myblog.controller;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhuhodor.myblog.Entity.BlogModule.Blog;
import com.zhuhodor.myblog.Entity.Project;
import com.zhuhodor.myblog.common.Result;
import com.zhuhodor.myblog.elasticsearch.Entity.EsProject;
import com.zhuhodor.myblog.vo.OverviewVo;
import com.zhuhodor.myblog.vo.ProjectVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/project")
@Slf4j
public class ProjectController extends BaseController{

    /**
     * 根据Id获取项目信息
     * @param projectId
     * @return
     */
    @GetMapping("/{projectId}")
    public Result getProjectById(@PathVariable("projectId") String projectId, @RequestParam("userId") String userId){
        log.info("获取id={}的项目信息", projectId);
        Project project = projectService.getById(projectId);
        ArrayList<OverviewVo> vos = new ArrayList<>();
        String[] split = project.getOverview().split("7&%#");
        for (int i = 0; i < split.length; i++){
            String[] ov = split[i].split("\\|");
            if (ov.length != 2){
                vos.add(new OverviewVo(ov[0], ""));
            }else {
                vos.add(new OverviewVo(ov[0], ov[1]));
            }
        }
        String userRate = redisUtils.hget("userRates:"+projectId, userId);
        return Result.success(MapUtil.builder()
                .put("id", project.getId())
                .put("startUser", project.getStartUser())
                .put("projectUser", userService.findUserById(project.getStartUser()))
                .put("overview", vos)
                .put("rates",project.getRates())
                .put("projectName", project.getProjectName())
                .put("favorite", projectService.favoriteCount(projectId))
                .put("userRate", userRate).map());
    }

    /**
     * 根据用户Id分页获取项目集
     * @param userId
     * @return
     */
    @GetMapping("/projects/{userId}")
    public Result getProjectByUserId(@PathVariable("userId") String userId, @RequestParam(value = "page", defaultValue = "1", required = false) Integer page){
        log.info("分页获取userId={}的项目集", userId);
        PageHelper.startPage(page, 6);
        List<Project> projects = projectService.list(new QueryWrapper<Project>().eq("start_user", userId));
        PageInfo<Project> pageInfo = new PageInfo<>(projects);
        return Result.success(MapUtil.builder().put("projectList", projects)
        .put("total", pageInfo.getPages()).map());
    }

    /**
     * 创建一个项目
     * @param projectVo
     * @return
     */
    @PostMapping("/create")
    public Result createProject(@RequestBody ProjectVo projectVo){
        log.info("创建项目");
        Project project = new Project();
        BeanUtils.copyProperties(projectVo, project, "overview");
        StringBuilder sb = new StringBuilder();
//        多条介绍合并成一条String
        for (OverviewVo s : projectVo.getOverview()){
            sb.append(s.getContent());
            sb.append("7&%#");
        }
        project.setOverview(sb.toString());
        Timestamp now = new Timestamp(System.currentTimeMillis());
        project.setCreateAt(now);
        projectService.save(project);
        rabbitTemplate.convertAndSend("project", "project.save", project);
        return Result.success(project);
    }

    /**
     * 获取项目中的所有文章
     * @param projectId
     * @return
     */
    @GetMapping("/blogs/{projectId}")
    public Result getBlogsFromProject(@PathVariable("projectId") String projectId,
                                      @RequestParam(value = "page", required = false, defaultValue = "1") Integer page){
        log.info("获取projectId={}的第{}页博客", projectId, page);
        PageHelper.startPage(page, 6);
        List<Blog> projectBlogs = projectService.findBlogsByProjectId(projectId);
        PageInfo<Blog> pageInfo = new PageInfo<>(projectBlogs);
        return Result.success(MapUtil.builder().put("blogList", projectBlogs)
        .put("total", pageInfo.getPages()).map());
    }

    /**
     * 根据Id删除项目
     * @param projectId
     * @return
     */
    @GetMapping("/delete/{projectId}")
    public Result deleteById(@PathVariable("projectId") String projectId){
        boolean b = projectService.removeById(projectId);
        rabbitTemplate.convertAndSend("project", "project.del", projectId);
        if (b)
            return Result.success("删除成功");
        else
            return Result.fail("删除失败");
    }

    @GetMapping("/getfavorite/{userId}")
    public Result getFavorite(@PathVariable("userId") String userId,
                              @RequestParam(value = "page", defaultValue = "1", required = false) Integer page,
                              @RequestParam(value = "limit", required = false, defaultValue = "6") Integer limit){
        PageHelper.startPage(page, limit);
        List<Project> favorite = projectService.getFavoriteByUserId(userId);
        PageInfo<Project> pageInfo = new PageInfo<>(favorite);
        return Result.success(MapUtil.builder().put("favoriteList", favorite)
                .put("total", pageInfo.getPages()).map());
    }

    /**
     * 判断用户是否收藏此项目
     * @param userId
     * @param projectId
     * @return
     */
    @GetMapping("/isfavorite/{userId}/{projectId}")
    public Result isFavorite(@PathVariable("userId") String userId, @PathVariable("projectId") String projectId){
        int count = projectService.isFavorite(userId, projectId);
        if (count > 0)
            return Result.success(true);
        else
            return Result.success(false);
    }

    /**
     * 收藏项目
     */
    @GetMapping("/favorite/{userId}/{projectId}")
    public Result favoriteProject(@PathVariable("userId") String userId, @PathVariable("projectId") String projectId){
        boolean b = projectService.favorite(userId, projectId);
        if (b)
            return Result.success("已收藏");
        else
            return Result.fail("请稍后再试");

    }

    /**
     * 取消收藏
     * @param userId
     * @param projectId
     * @return
     */
    @GetMapping("/cancel/{userId}/{projectId}")
    public Result cancelFavorite(@PathVariable("userId") String userId, @PathVariable("projectId") String projectId){
        boolean b = projectService.cancel(userId, projectId);
        if (b)
            return Result.success("取消收藏");
        else
            return Result.fail("请稍后再试");
    }

    /**
     * 时间轴
     * @param projectId
     * @return
     */
    @GetMapping("/blogtime/{projectId}")
    public Result getBlogTime(@PathVariable("projectId") String projectId){
        log.info("获取projectId={}的时间线", projectId);
        List<Date> time = projectService.getBlogTime(projectId);
        HashMap<String, Integer> map = new HashMap<>();
        time.forEach(t -> {
            String format = new SimpleDateFormat("yyyy-MM-dd").format(t);
            map.put(format, map.getOrDefault(format, 0) + 1);
        });
        return Result.success(map);
    }

    /**
     * 更新简介
     * @param projectVo
     * @return
     */
    @PostMapping("/overview")
    public Result updateOverview(@RequestBody ProjectVo projectVo){
        log.info("更新简介 ==== id = {}", projectVo.getId());
        Project project = new Project();
        BeanUtils.copyProperties(projectVo, project, "overview");
        StringBuilder sb = new StringBuilder();
//        多条介绍合并成一条String
        for (OverviewVo s : projectVo.getOverview()){
            sb.append(s.getTitle()+'|');
            sb.append(s.getContent());
            sb.append("7&%#");
        }
        project.setOverview(sb.toString());
        Timestamp now = new Timestamp(System.currentTimeMillis());
        project.setCreateAt(now);
        projectService.updateById(project);
        rabbitTemplate.convertAndSend("project", "project.upgrade", project);
        return Result.success("更新成功");
    }

    /**
     * 评分
     * @param userId
     * @param rate
     * @param projectId
     * @return
     */
    @GetMapping("/rates/{userId}/{projectId}/{rate}")
    public Result rates(@PathVariable String userId ,@PathVariable Float rate,@PathVariable String projectId){
        log.info("用户{}给项目{}评分",userId, projectId);
        if (!redisUtils.isSetMem("rates:"+projectId, userId)){
            redisUtils.sadd("rates:"+projectId, userId);
            redisUtils.hset("userRates:"+projectId, userId, String.valueOf(rate));
            QueryWrapper<Project> wrapper = new QueryWrapper<>();
            wrapper.eq("id", projectId);
            wrapper.select("id", "rates");
            Project one = projectService.getOne(wrapper);
            one.setRates((Float) ((one.getRates()+rate) / redisUtils.snum("rates:"+projectId)));
            projectService.updateById(one);
            return Result.success("评价成功");
        }
        return Result.success("只能评价一次哦");
    }

    /**
     * 申请成为贡献者
     * @param userId
     * @param projectId
     * @return
     */
    @GetMapping("/request/{userId}/{projectId}")
    public Result request(@PathVariable String userId, @PathVariable String projectId){
        if (projectService.request(userId, projectId)){
            return Result.success(202,"您已经申请过啦！", null);
        }else {
            return Result.success(200, "请求成功", null);
        }
    }

    /**
     * 处理申请
     * @param userId
     * @param projectId
     * @param res
     * @return
     */
    @GetMapping("/deal/{userId}/{projectId}")
    public Result dealRequest(@PathVariable String userId, @PathVariable String projectId, @RequestParam("res") String res){
        log.info("用户[{}]的请求被处理, 结果为[{}]", userId, res);
        projectService.dealRequest(projectId, userId, res);
        return Result.success("处理成功");
    }
}
