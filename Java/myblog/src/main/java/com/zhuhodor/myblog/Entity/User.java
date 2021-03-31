package com.zhuhodor.myblog.Entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
@Accessors(chain = true)//setter方法返回当前对象
@EqualsAndHashCode(callSuper = false)
@TableName("user")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private Integer id;
    private String salt;
    @NotBlank(message = "昵称不能为空")
    private String username;
    private String avatar;

    @NotBlank(message = "密码不能为空")
    private String password;
    @Email(message = "邮箱格式不正确")
    private String email;
    private String major;
    private String college;
    private String description;
    private String grade;

    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8",shape = JsonFormat.Shape.STRING)
    @PastOrPresent
    private Date createAt;

}
