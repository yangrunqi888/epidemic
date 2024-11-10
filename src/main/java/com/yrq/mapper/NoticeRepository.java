package com.yrq.mapper;

import com.yrq.entity.NoticeEntity;
import com.yrq.entity.NoticeEntity2;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeRepository extends ElasticsearchRepository<NoticeEntity2, String> {
}