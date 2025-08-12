package com.stocknest.stocknest_api.service.impl;

import com.stocknest.stocknest_api.model.ApiSource;
import com.stocknest.stocknest_api.repository.ApiSourceRepository;
import com.stocknest.stocknest_api.service.ApiSourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ApiSourceServiceImpl implements ApiSourceService {

    @Autowired
    private final ApiSourceRepository apiSourceRepository;

    @Override
    public ApiSource save(ApiSource source) {
        return apiSourceRepository.save(source);
    }

    @Override
    public List<ApiSource> getAllSources() {
        return apiSourceRepository.findAll();
    }

    @Override
    public Optional<ApiSource> getSourceById(String id) {
        return apiSourceRepository.findById(id);
    }

    @Override
    public ApiSource saveSource(ApiSource source) {
        return apiSourceRepository.save(source);
    }

    @Override
    public void deleteSource(String id) {
        apiSourceRepository.deleteById(id);
    }
}
