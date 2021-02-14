package com.zhuhodor.myblog.elasticsearch.Entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.elasticsearch.search.DocValueFormat;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Document(indexName = "blog")
public class EsBlog {
    @Id
    public String id;

    @Field(type = FieldType.Keyword)
    private String userId;

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String title;

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String summary;

    @Field(type = FieldType.Text, analyzer = "ik_smart")
    private String content;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern ="yyyy-MM-dd HH:mm:ss", timezone ="GMT+8")
    @Field(type = FieldType.Date, pattern ="yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    @Field(type = FieldType.Integer)
    private int isFile;

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private  String tags;

}
