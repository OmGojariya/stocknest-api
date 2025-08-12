package com.stocknest.stocknest_api.repository;

import com.stocknest.stocknest_api.model.ApiSource;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiSourceRepository extends MongoRepository<ApiSource, String> {
    ApiSource findByProviderName(String providerName);
}

