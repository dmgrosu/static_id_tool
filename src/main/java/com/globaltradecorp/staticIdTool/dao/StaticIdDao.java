package com.globaltradecorp.staticIdTool.dao;

import com.globaltradecorp.staticIdTool.model.StaticId;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author Dmitri Grosu (dmitri.grosu@codefactorygroup.com), 1/8/21
 */
@Component
public class StaticIdDao {

    public Map<Integer, String> getComponentList() {
        // TODO replace stub
        return null;
    }

    public List<StaticId> getStaticIdList(int rowsCount) {
        // TODO replace stub
        return null;
    }

    public boolean idValueExists(String newIdValue) {
        // TODO replace stub
        return false;
    }

    public void saveStaticId(StaticId newStaticId, int componentId) {
        // TODO replace stub
    }
}
