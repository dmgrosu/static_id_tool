package com.globaltradecorp.staticIdTool.model;

import lombok.Builder;
import lombok.Value;

/**
 * @author Dmitri Grosu (dmitri.grosu@codefactorygroup.com), 1/8/21
 */
@Value
@Builder
public class ComponentType {
    int id;
    String name;
}
