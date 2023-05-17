package com.goose.mpstest.repository;

import com.goose.mpstest.model.Airplane;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AirplaneRepository extends MongoRepository<Airplane, Long> {
}
