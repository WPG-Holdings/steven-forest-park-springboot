package com.wpg.stevenforestparkspringboot.iface.rest;

import com.wpg.stevenforestparkspringboot.model.Detail;
import com.wpg.stevenforestparkspringboot.model.Operation;
import com.wpg.stevenforestparkspringboot.service.UploadService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Samuel Luo
 */
@Slf4j
@RestController
@RequestMapping(path = "search")
public class SearchController {

    @Autowired
    UploadService uploadService;

    @Autowired
    ElasticsearchOperations elasticsearchOperations;

    @PostMapping("search")
    public ResponseEntity<?> search() {
        try {
            TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("question", "aiimport");
            NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
            nativeSearchQueryBuilder.withQuery(termQueryBuilder);
            NativeSearchQuery nativeSearchQuery = nativeSearchQueryBuilder.build();
            SearchHits<Operation> search = elasticsearchOperations.search(nativeSearchQuery, Operation.class, IndexCoordinates.of("op_index"));
            for (SearchHit<Operation> hit: search){
                System.out.println("Question = " + hit.getContent());
            }

            return new ResponseEntity<>(search, new HttpHeaders(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/detail/{azureId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> detail(@PathVariable(name = "azureId") Integer azureId) {
        try {
            TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("azureID", azureId);
            NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
            nativeSearchQueryBuilder.withQuery(termQueryBuilder);
            NativeSearchQuery nativeSearchQuery = nativeSearchQueryBuilder.build();
            SearchHits<Operation> search = elasticsearchOperations.search(nativeSearchQuery, Operation.class, IndexCoordinates.of("op_index"));
            for (SearchHit<Operation> hit: search){
                System.out.println("content = " + hit.getContent());
            }

            return new ResponseEntity<>(search, new HttpHeaders(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
