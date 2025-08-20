package com.stocknest.stocknest_api.repository;

import com.stocknest.stocknest_api.model.AngelOneToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AngelOneTokenRepository extends MongoRepository<AngelOneToken, String> {
    Optional<AngelOneToken> findByUserId(String userId);

    void deleteByUserId(String userId);
}
