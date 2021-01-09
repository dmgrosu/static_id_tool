package com.globaltradecorp.staticIdTool.model.dto;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * @author Dmitri Grosu (dmitri.grosu@codefactorygroup.com), 1/8/21
 */
@Value
@AllArgsConstructor
public class DeleteRequestDto {
    List<Integer> selectedIds;
    OffsetDateTime userTime;
}
