package com.wpg.stevenforestparkspringboot.model;

import lombok.Data;

import java.util.Map;

/**
 * @author Simon Tsai
 * @date 2021/11/30
 */
@Data
public class QueryBody {
	Integer page;
	Integer size;
	Map<String, String> mustQueryMap;
	Map<String, String> shouldQueryMap;
	Map<String, String> notQueryMap;
}
