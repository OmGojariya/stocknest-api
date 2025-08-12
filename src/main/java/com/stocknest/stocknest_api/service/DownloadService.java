package com.stocknest.stocknest_api.service;

import com.stocknest.stocknest_api.model.DownloadRequest;

import java.util.List;

public interface DownloadService {
    DownloadRequest saveRequest(DownloadRequest request);
    List<DownloadRequest> getRequestsByUserId(String userId);
}
