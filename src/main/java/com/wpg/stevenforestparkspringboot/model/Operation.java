package com.wpg.stevenforestparkspringboot.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.io.Serializable;

/**
 * @author Samuel Luo
 * A operation.
 */
@Data
@Document(indexName = "operation")
public class Operation implements Serializable {
    @Id
	private String id;

	@Field(type = FieldType.Keyword, name = "lastUpdatedBy")
	private String lastUpdatedBy;

	@Field(type = FieldType.Keyword, name = "area")
	private String area;

	@Field(type = FieldType.Long, name = "azureID")
	private Long azureId;

	@Field(type = FieldType.Long, name = "count")
	private Long count;

	@Field(type = FieldType.Keyword, name = "creationTime")
	private String createTime;

	@Field(type = FieldType.Keyword, name = "creationDate")
	private String creationDate;

	@Field(type = FieldType.Keyword, name = "endFlag")
	private String endFlag;

	@Field(type = FieldType.Keyword, name = "endTime")
	private String endTime;

	@MultiField(mainField = @Field(type = FieldType.Text, name="function"),
			otherFields = { @InnerField(suffix = "keyword", type = FieldType.Keyword, ignoreAbove = 256) })
	private String function;

	@Field(type = FieldType.Keyword, name = "inform")
	private String inform;

	@Field(type = FieldType.Keyword, name = "it")
	private String it;

	@Field(type = FieldType.Keyword, name = "level")
	private String level;

	@Field(type = FieldType.Text, name = "memo")
	private String memo;

	@Field(type = FieldType.Keyword, name = "messageLink")
	private String messageLink;

	@Field(type = FieldType.Keyword, name = "prevEndTime")
	private String prevEndTime;

	@Field(type = FieldType.Text, name = "prevMemo")
	private String prevMemo;

	@Field(type = FieldType.Keyword, name = "prevStartTime")
	private String prevStartTime;

	@Field(type = FieldType.Keyword, name = "prevTeam")
	private String prevTeam;

	@Field(type = FieldType.Text, name = "processMethod")
	private String processMethod;

	@Field(type = FieldType.Keyword, name = "psName")
	private String psName;

	@Field(type = FieldType.Text, name = "question")
	private String question;

	@Field(type = FieldType.Keyword, name = "questionTime")
	private String questionTime;

	@Field(type = FieldType.Keyword, name = "send")
	private String send;

	@Field(type = FieldType.Keyword, name = "source")
	private String source;

	@Field(type = FieldType.Double, name = "spendTime")
	private Double spendTime;

	@Field(type = FieldType.Keyword, name = "ssc")
	private String ssc;

	@Field(type = FieldType.Keyword, name = "sscEndFlag")
	private String sscEndFlag;

	@Field(type = FieldType.Keyword, name = "sscEndTime")
	private String sscEndTime;

	@Field(type = FieldType.Date, name = "sscNotEndTime")
	private String sscNotEndTime;

	@Field(type = FieldType.Keyword, name = "startTime")
	private String startTime;

	@Field(type = FieldType.Keyword, name = "sys")
	private String sys;

	@Field(type = FieldType.Keyword, name = "team")
	private String team;

	@Field(type = FieldType.Date, name = "title")
	private String title;

	@Field(type = FieldType.Keyword, name = "transTime")
	private String transTime;

	@Field(type = FieldType.Keyword, name = "user")
	private String user;

	@MultiField(mainField = @Field(type = FieldType.Text, name="userFunction"),
			otherFields = { @InnerField(suffix = "keyword", type = FieldType.Keyword, ignoreAbove = 256) })
	private String userFunction;

	@MultiField(mainField = @Field(type = FieldType.Text, name="userMail"),
			otherFields = { @InnerField(suffix = "keyword", type = FieldType.Keyword, ignoreAbove = 256) })
	private String userMail;

}
