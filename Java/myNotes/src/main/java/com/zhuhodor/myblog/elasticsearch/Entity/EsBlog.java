package com.zhuhodor.myblog.elasticsearch.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.elasticsearch.search.DocValueFormat;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;


@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Document(indexName = "blog")
@NoArgsConstructor
public class EsBlog {
    @Id
    public String id;

    @Field(type = FieldType.Keyword)
    private String userId;

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String title;

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String summary;

    @Field(type = FieldType.Text, analyzer = "ik_smart_word")
    private String content;

    @Field(pattern = "yyyy-MM-dd HH:mm:ss", type = FieldType.Date, format = DateFormat.year_month_day)
    @JsonFormat(pattern ="yyyy-MM-dd HH:mm:ss", timezone ="GMT+8")
    private Date createdAt;

    @Field(type = FieldType.Integer)
    private int isFile;

    public EsBlog(String id){
        this.id = id;
    }

}
