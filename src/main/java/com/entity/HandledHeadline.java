package com.entity;

import lombok.Data;

@Data
public class HandledHeadline {
    private long hId;
    private String title;
    private String article;
    private long type;
    private String typeName;
    private String publisher;
    private long pageViews;
    private String author;
    private Integer pastHours;
}
