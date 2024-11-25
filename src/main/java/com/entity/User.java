package com.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serial;
import java.io.Serializable;

@Component
@Data
public class User implements Serializable{ //定义 'serialVersionUID' 字段的不可序列化类 'User'
    @TableId(value = "uid", type = IdType.AUTO)
    private Integer uId;

    private String username;

    private String password;

    @TableField("nick_name")
    private String nickname;

    @Version
    private Integer version;

    @TableLogic
    private Integer isDeleted;

    @Serial
    private static final long serialVersionUID = 1L;
}