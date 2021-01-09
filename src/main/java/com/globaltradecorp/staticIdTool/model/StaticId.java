package com.globaltradecorp.staticIdTool.model;

import lombok.Builder;
import lombok.Value;
import org.springframework.lang.Nullable;

import java.time.OffsetDateTime;

/**
 * @author Dmitri Grosu (dmitri.grosu@codefactorygroup.com), 1/7/21
 */
@Value
@Builder
public class StaticId {

    /**
     * Database ID, null for new objects
     */
    @Nullable
    Long id;
    /**
     * Static ID value
     */
    String value;
    ComponentType componentType;
    AppUser createdBy;
    OffsetDateTime createdAt;

    public boolean isNew() {
        return id == null;
    }

}
