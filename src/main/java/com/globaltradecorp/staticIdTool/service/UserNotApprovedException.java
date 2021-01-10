package com.globaltradecorp.staticIdTool.service;

/**
 * @author Dmitri Grosu (dmitri.grosu@codefactorygroup.com), 1/10/21
 */
public class UserNotApprovedException extends Exception {
    public UserNotApprovedException(String message) {
        super(message);
    }
}
