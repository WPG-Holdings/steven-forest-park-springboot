package com.wpg.stevenforestparkspringboot.service;

import com.wpg.stevenforestparkspringboot.model.KeywordTag;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
 * @author Simon Tsai
 * @date 2021/11/30
 */
@Service
public class ElasticCloudService {
	@Autowired
	ElasticsearchOperations elasticsearchOperations;

	@Autowired
	RestHighLevelClient client;
	public List<Object> getHotKeywordMap(int topNumber) throws IOException {
		SearchRequest searchRequest = new SearchRequest();
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.size(0);
		TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders.terms("function_counts").field("function.keyword").size(topNumber);
		searchSourceBuilder.aggregation(termsAggregationBuilder);
		searchRequest.source(searchSourceBuilder);

		SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
		Terms termsAggs = searchResponse.getAggregations().get("function_counts");
		List<? extends Terms.Bucket> buckets = termsAggs.getBuckets();
		List<Object> hotKeywordList = new ArrayList<>();
		for (Terms.Bucket bucket : buckets) {
			Long counts = bucket.getDocCount() / 2;
			String function = bucket.getKeyAsString();
			if ("others".equals(function)) {
				continue;
			}
			HashMap<String, Object> keywordMap = findTags(function);
			keywordMap.put("counts", counts);
			hotKeywordList.add(keywordMap);
		}
		return hotKeywordList;
	}
	public HashMap<String, Object> findTags(String function){
		TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("function.keyword", function);
		NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
		nativeSearchQueryBuilder.withQuery(termQueryBuilder);
		NativeSearchQuery nativeSearchQuery = nativeSearchQueryBuilder.build();

		SearchHits<KeywordTag> search = elasticsearchOperations.search(nativeSearchQuery, KeywordTag.class);
		Set<String> sysSet = new HashSet<>();
		Set<String> teamSet = new HashSet<>();
		for (SearchHit<KeywordTag> hit: search){
			sysSet.add(hit.getContent().getSys());
			teamSet.add(hit.getContent().getTeam());
		}
		HashMap<String, Object> tagMap = new HashMap<>();
		tagMap.put("keyword", function);
		tagMap.put("sysTag", new HashSet<>(sysSet));
		tagMap.put("teamTag", new HashSet<>(teamSet));
		return tagMap;
	}
}
