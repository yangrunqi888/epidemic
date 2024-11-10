package com.yrq.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
@Data
@NoArgsConstructor
@Document(indexName = "announcements") // 保持索引名称与 Elasticsearch 中的一致
public class NoticeEntity2 {

    @Id
    private String id;
    private String title;
    private String text;
    @Field(name = "announce_time", type = FieldType.Text)
    private String announceTime;
    private String employee_id;
    String employeeName;
    // Getters and Setters
}

