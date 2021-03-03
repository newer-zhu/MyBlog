package com.zhuhodor.myblog.elasticsearch.Entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhuhodor.myblog.Entity.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Document(indexName = "project")
@NoArgsConstructor
public class EsProject {
    @Id
    private Integer id;

    @Field(type = FieldType.Keyword)
    private Integer startUser;

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String projectName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private String createAt;

    @Field(type = FieldType.Integer)
    private Integer rates;

}
