package com.wpg.stevenforestparkspringboot.repository;

import com.wpg.stevenforestparkspringboot.model.Operation;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author Samuel Luo
 */
public interface OperationRepository extends ElasticsearchRepository<Operation, String> {
}
