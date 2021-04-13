package com.zhuhodor.myblog.Entity.BlogModule;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhuhodor.myblog.Entity.ProjectModule.Project;
import com.zhuhodor.myblog.Entity.Tag;
import com.zhuhodor.myblog.Entity.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Blog implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    @Id
    public String id;

    private String userId;

    @NotBlank(message = "标题不能为空")

    private String title;

    private String summary;

    @NotBlank(message = "内容不能为空")
    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createdAt;

    private int isFile;

    private Integer visitors;

    @TableField(exist = false)
    private Project project;

    @TableField(exist = false)
    private List<Tag> tags;

    @TableField(exist = false)
    private User user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Blog blog = (Blog) o;
        return id.equals(blog.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
