package com.globaltradecorp.staticIdTool.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Dmitri Grosu (dmitri.grosu@codefactorygroup.com), 1/7/21
 */
@Data
@Builder
public class StaticId {

    /**
     * Database ID
     */
    private long id;
    /**
     * Static ID value
     */
    private String value;
    private String componentName;
    private String createdBy;
    private LocalDateTime createdAt;

}
