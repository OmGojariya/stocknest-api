package com.stocknest.stocknest_api.service.impl;

import com.stocknest.stocknest_api.model.DownloadRequest;
import com.stocknest.stocknest_api.repository.DownloadRepository;
import com.stocknest.stocknest_api.service.DownloadService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DownloadServiceImpl implements DownloadService {

    @Autowired
    private final DownloadRepository downloadRequestRepository;

    @Override
    public DownloadRequest saveRequest(DownloadRequest request) {
        return downloadRequestRepository.save(request);
    }

    @Override
    public List<DownloadRequest> getRequestsByUserId(String userId) {
        return downloadRequestRepository.findByRequestedBy(userId);
    }
}
