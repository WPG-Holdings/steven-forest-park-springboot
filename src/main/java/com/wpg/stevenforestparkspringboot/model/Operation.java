package com.wpg.stevenforestparkspringboot.model;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import javax.persistence.Id;
import java.io.Serializable;

/**
 * @author Samuel Luo
 * A operation.
 */
@Data
@Document(indexName = "operation")
public class Operation implements Serializable {

    @Id
    @Field()
    private String id;
}
