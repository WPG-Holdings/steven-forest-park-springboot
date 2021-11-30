package com.wpg.stevenforestparkspringboot.iface.rest;

import com.wpg.stevenforestparkspringboot.model.Detail;
import com.wpg.stevenforestparkspringboot.model.Operation;
import com.wpg.stevenforestparkspringboot.service.ElasticCloudService;
import com.wpg.stevenforestparkspringboot.service.UploadService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
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

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
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

    @Autowired
    RestHighLevelClient client;

    @GetMapping("query")
    public ResponseEntity<?> query(@RequestParam(value = "fieldName") String fieldName,
                                   @RequestParam(value = "value") String value,
                                   @RequestParam(required = false, value = "page", defaultValue = "0") Integer page,
                                   @RequestParam(required = false, value = "size", defaultValue = "10") Integer size) {
        try {
            TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery(fieldName, value);
            NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
            nativeSearchQueryBuilder.withQuery(termQueryBuilder).withPageable(PageRequest.of(page, size));
            NativeSearchQuery nativeSearchQuery = nativeSearchQueryBuilder.build();
            SearchHits<Operation> search = elasticsearchOperations.search(nativeSearchQuery, Operation.class);

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

    @Autowired
    ElasticCloudService elasticCloudService;
    @GetMapping("hotKeywordCounts")
    public ResponseEntity<List<Object>> getFunctionCount(@RequestParam("topNumber") int topNumber) throws IOException {
        return ResponseEntity.ok().body(elasticCloudService.getHotKeywordMap(topNumber));
    }
}
