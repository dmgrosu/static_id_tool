package com.globaltradecorp.staticIdTool.service;

/**
 * @author Dmitri Grosu (dmitri.grosu@codefactorygroup.com), 1/8/21
 */
public class IdValueExistsException extends Exception {
    public IdValueExistsException(String message) {
        super(message);
    }
}
