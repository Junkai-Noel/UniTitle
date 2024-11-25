package com.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class Type implements Serializable {

    @TableId(value = "tid", type = IdType.AUTO)
    private Integer id;

    private String tname;

    @Version
    private Integer version;

    @TableLogic
    private Integer isDeleted;

    @Serial
    private static final long serialVersionUID = 1L;

}