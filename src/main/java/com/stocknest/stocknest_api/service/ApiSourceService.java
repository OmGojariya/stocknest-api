package com.stocknest.stocknest_api.service;

import com.stocknest.stocknest_api.model.ApiSource;

import java.util.List;
import java.util.Optional;

public interface ApiSourceService {
    ApiSource save(ApiSource source);
    List<ApiSource> getAllSources();
    Optional<ApiSource> getSourceById(String id);
    ApiSource saveSource(ApiSource source);
    void deleteSource(String id);
}
