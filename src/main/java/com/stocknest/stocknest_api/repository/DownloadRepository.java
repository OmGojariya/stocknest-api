package com.stocknest.stocknest_api.repository;

import com.stocknest.stocknest_api.model.DownloadRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DownloadRepository extends MongoRepository<DownloadRequest,String> {
    List<DownloadRequest> findByRequestedBy(String requestedBy);
}
