package com.pageUtils;

import lombok.Data;

@Data
public class PageParam {
    private String keyWords;
    private Integer type;
    private Integer pageNumber;
    private Integer pageSize;
}
