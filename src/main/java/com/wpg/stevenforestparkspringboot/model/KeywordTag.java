package com.wpg.stevenforestparkspringboot.model;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.*;

import java.io.Serializable;

/**
 * @author Simon Tsai
 * @date 2021/11/30
 */
@Data
@Document(indexName = "operation")
public class KeywordTag implements Serializable {
	@Field(type = FieldType.Keyword, name = "sys")
	private String sys;

	@Field(type = FieldType.Keyword, name = "team")
	private String team;

	@MultiField(mainField = @Field(type = FieldType.Text, name="function"),
			otherFields = { @InnerField(suffix = "keyword", type = FieldType.Keyword, ignoreAbove = 256) })
	private String function;
}
