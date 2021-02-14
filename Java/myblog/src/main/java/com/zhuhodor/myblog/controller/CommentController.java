package com.zhuhodor.myblog.controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhuhodor.myblog.Entity.Comment;
import com.zhuhodor.myblog.Entity.User;
import com.zhuhodor.myblog.common.Result;
import com.zhuhodor.myblog.dao.CommentDao;
import com.zhuhodor.myblog.dao.CommentUser;
import com.zhuhodor.myblog.service.CommentService;
import com.zhuhodor.myblog.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/comment")
@Slf4j
public class CommentController {
    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    //只支持两级评论
    @GetMapping("/getlistbyblogid/{blogId}")
    public Result<Comment> getListById(@PathVariable("blogId") int blogId){
        log.info("获取blogId={}的评论",blogId);
        //顶层评论
        List<Comment> comments = commentService.selectCommentByBlogId(blogId);
        //返回结果
        ArrayList<CommentDao> list = new ArrayList<>();
        for (Comment c : comments){
            //将查询的评论实体转换成需要的实体
            CommentDao cD = new CommentDao();
            User commentUser = userService.findUserById(c.getCommentUser());
            cD.setCommentUser(new CommentUser(commentUser.getId(),commentUser.getAvatar(), commentUser.getUsername()));
            User targetUser = userService.findUserById(c.getTargetUser());
            cD.setTargetUser(new CommentUser(targetUser.getId(),targetUser.getAvatar(), targetUser.getUsername()));
            cD.setContent(c.getContent());
            cD.setId(c.getId());
            cD.setCreateDate(c.getCreateDate());
//            第二层评论
            List<Comment> children = commentService.selectChildrenByCommentId(c.getId());
            List<CommentDao> daos = new ArrayList<>();
            for (Comment co : children){
                //将第二层评论的实体转换成需要的实体类型
                CommentDao dao = new CommentDao();
                User cUser = userService.findUserById(co.getCommentUser());
                dao.setCommentUser(new CommentUser(cUser.getId(), cUser.getAvatar(), cUser.getUsername()));
                User tUser = userService.findUserById(co.getTargetUser());
                dao.setTargetUser(new CommentUser(tUser.getId(), tUser.getAvatar(), tUser.getUsername()));
                dao.setContent(co.getContent());
                dao.setId(co.getId());
                dao.setCreateDate(co.getCreateDate());
                daos.add(dao);
            }
            cD.setChildrenList(daos);
            list.add(cD);
        }
        return Result.success(list);
    }

    @PostMapping("/parentcomment/{blogId}")
    public Result parentComment(@RequestBody Comment comment, @PathVariable("blogId") int blogId){
        comment.setCreateDate(LocalDateTime.now().toString());
        try {
            commentService.save(comment);
            commentService.commentToBlog(comment.getId(), blogId);
        }catch (Exception e){
            e.printStackTrace();
            return Result.fail("评论失败");
        }
        return Result.success("评论成功");
    }

    @PostMapping("/childcomment/{targetComment}")
    public Result childComment(@RequestBody Comment comment, @PathVariable("targetComment") int targetComment){
        comment.setCreateDate(LocalDateTime.now().toString());
        try {
            commentService.save(comment);
            commentService.childCommentToParent(comment.getId(), targetComment);
        }catch (Exception e){
            e.printStackTrace();
            return Result.fail("评论失败");
        }
        return Result.success("评论成功");
    }

}