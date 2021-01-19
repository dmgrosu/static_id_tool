package com.globaltradecorp.staticIdTool.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.time.OffsetDateTime;

/**
 * @author Dmitri Grosu (dmitri.grosu@codefactorygroup.com), 1/19/21
 */
@Value
@AllArgsConstructor
@Builder
public class AdminActionRequestDto {
    Integer userId;
    OffsetDateTime userTime;
    AdminAction action;
}
