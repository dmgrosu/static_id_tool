package com.globaltradecorp.staticIdTool.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * @author Dmitri Grosu (dmitri.grosu@codefactorygroup.com), 1/8/21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteRequestDto {
    List<Integer> selectedIds;
    OffsetDateTime userTime;
}
