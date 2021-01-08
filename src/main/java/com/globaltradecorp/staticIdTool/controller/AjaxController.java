package com.globaltradecorp.staticIdTool.controller;

import com.globaltradecorp.staticIdTool.model.StaticId;
import com.globaltradecorp.staticIdTool.model.dto.StaticIdDto;
import com.globaltradecorp.staticIdTool.service.IdValueExistsException;
import com.globaltradecorp.staticIdTool.service.StaticIdService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Dmitri Grosu (dmitri.grosu@codefactorygroup.com), 1/8/21
 */
@RestController
public class AjaxController {

    private StaticIdService staticIdService;
    public static final Logger logger = LoggerFactory.getLogger(AjaxController.class);

    @Autowired
    public AjaxController(StaticIdService staticIdService) {
        this.staticIdService = staticIdService;
    }

    @GetMapping("/getExisting")
    public ResponseEntity<List<StaticId>> showExisting(@RequestParam("componentId") Integer componentId,
                                                       @RequestParam(value = "prefix", required = false, defaultValue = "") String prefix,
                                                       @RequestParam(value = "rows", required = false, defaultValue = "10") int rowsCount) {
        try {
            List<StaticId> staticIds = staticIdService.getStaticIdList(componentId, prefix, rowsCount);
            return ResponseEntity.ok(staticIds);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/addNew")
    public ResponseEntity<String> addNewIdValue(@RequestBody StaticIdDto staticIdDto) {
        try {
            staticIdService.addNewIdValue(staticIdDto.getIdValue(), staticIdDto.getComponentId());
            return ResponseEntity.ok().build();
        } catch (IdValueExistsException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

}
