package com.zhuhodor.myblog.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Data
@Accessors(chain = true)//setter方法返回当前对象
@EqualsAndHashCode(callSuper = false)
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private int id;
    private String salt;
    @NotBlank(message = "昵称不能为空")
    private String username;
    private String avatar;

    @NotBlank(message = "密码不能为空")
    private String password;
    @Email(message = "邮箱格式不正确")
    private String email;
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8",shape = JsonFormat.Shape.STRING)
    @PastOrPresent
    private LocalDate birthday;
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8",shape = JsonFormat.Shape.STRING)
    @PastOrPresent
    private LocalDate createdAt;
    private String description;

    private List<Role> roles;

}
