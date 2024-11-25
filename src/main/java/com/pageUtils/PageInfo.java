package com.pageUtils;

import lombok.Data;

import java.util.List;

@Data
public class PageInfo {
    private Long totalSize;
    private Long totalPage;
    private Long pageSize;
    private Long pageNum;
    private List<?> pageData;
}
