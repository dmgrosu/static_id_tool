package com.globaltradecorp.staticIdTool.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

/**
 * @author Dmitri Grosu (dmitri.grosu@codefactorygroup.com), 1/8/21
 */
@Value
@AllArgsConstructor
@Builder
public class StaticIdDto {
    Integer componentId;
    String idValue;
}
