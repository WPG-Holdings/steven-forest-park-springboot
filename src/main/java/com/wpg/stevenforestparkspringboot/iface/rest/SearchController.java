package com.wpg.stevenforestparkspringboot.iface.rest;

import com.wpg.stevenforestparkspringboot.model.Detail;
import com.wpg.stevenforestparkspringboot.model.Operation;
import com.wpg.stevenforestparkspringboot.model.QueryBody;
import com.wpg.stevenforestparkspringboot.service.ElasticCloudService;
import com.wpg.stevenforestparkspringboot.service.UploadService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
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
import org.springframework.data.elasticsearch.core.query.Query;
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
@CrossOrigin
@RequestMapping(path = "search")
public class SearchController {

    @Autowired
    UploadService uploadService;

    @Autowired
    ElasticsearchOperations elasticsearchOperations;

    @Autowired
    RestHighLevelClient client;

    @PostMapping("query")
    public ResponseEntity<?> query(@RequestBody QueryBody queryBody ) {
        try {
            BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
            // Must condition
            Map<String, String> mustQueryMap = queryBody.getMustQueryMap();
            for (Map.Entry<String, String> entry : mustQueryMap.entrySet()){
                boolQueryBuilder.must(QueryBuilders.termQuery(entry.getKey(), entry.getValue()));
            }
            if (queryBody.getShouldQueryMap() != null){
                Map<String, String> shouldQueryMap = queryBody.getShouldQueryMap();
                for (Map.Entry<String, String> entry : shouldQueryMap.entrySet()) {
                    boolQueryBuilder.should(QueryBuilders.termQuery(entry.getKey(), entry.getValue()));
                }
            }

            if (queryBody.getNotQueryMap() != null){
                Map<String, String> notQueryMap = queryBody.getShouldQueryMap();
                for (Map.Entry<String, String> entry : notQueryMap.entrySet()) {
                    boolQueryBuilder.mustNot(QueryBuilders.termQuery(entry.getKey(), entry.getValue()));
                }
            }
            NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
            nativeSearchQueryBuilder.withQuery(boolQueryBuilder).withPageable(PageRequest.of(queryBody.getPage(), queryBody.getSize()));
            NativeSearchQuery nativeSearchQuery = nativeSearchQueryBuilder.build();
            SearchHits<Operation> search = elasticsearchOperations.search(nativeSearchQuery, Operation.class);


            return new ResponseEntity<>(search, new HttpHeaders(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/detail/{title}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> detail(@PathVariable(name = "title") String title) {
        try {
            TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("title", title);
            NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
            nativeSearchQueryBuilder.withQuery(termQueryBuilder);
            NativeSearchQuery nativeSearchQuery = nativeSearchQueryBuilder.build();
            SearchHits<Detail> search = elasticsearchOperations.search(nativeSearchQuery, Detail.class);

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
