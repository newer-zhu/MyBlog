package com.zhuhodor.myblog.Entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Objects;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@TableName("tag")
public class Tag {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String tagName;
    private String image;
    private Integer number;
    private Integer heat;

    public Tag(String tagName){
        this.tagName = tagName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return id.equals(tag.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
