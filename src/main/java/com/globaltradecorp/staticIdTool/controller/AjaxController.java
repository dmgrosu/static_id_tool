package com.globaltradecorp.staticIdTool.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Dmitri Grosu (dmitri.grosu@codefactorygroup.com), 1/8/21
 */
@RestController
public class AjaxController {

    @PostMapping("/newId")
    public ResponseEntity<String> addNewIdValue(@RequestParam("newId") String newIdValue,
                                                @RequestParam("componentId") Integer componentId) {
        return ResponseEntity.ok("");
    }

}
