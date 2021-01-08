package com.globaltradecorp.staticIdTool.service;

import com.globaltradecorp.staticIdTool.dao.StaticIdDao;
import com.globaltradecorp.staticIdTool.model.AppUser;
import com.globaltradecorp.staticIdTool.model.StaticId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;

/**
 * @author Dmitri Grosu (dmitri.grosu@codefactorygroup.com), 1/8/21
 */
@Service
public class StaticIdService {

    private final StaticIdDao staticIdDao;
    private final AppUserService appUserService;

    @Autowired
    public StaticIdService(StaticIdDao staticIdDao, AppUserService appUserService) {
        this.staticIdDao = staticIdDao;
        this.appUserService = appUserService;
    }

    public Map<Integer, String> getComponentList() {
        return staticIdDao.getComponentList();
    }

    public List<StaticId> getStaticIdList(int rowsCount) {
        return staticIdDao.getStaticIdList(rowsCount);
    }

    public void addNewIdValue(String newIdValue, Integer componentId) throws IdValueExistsException {

        Assert.notNull(newIdValue, "New ID value could not be null");
        Assert.notNull(componentId, "Component ID could not be null");

        if (staticIdDao.idValueExists(newIdValue)) {
            throw new IdValueExistsException(String.format("ID value already exists: [%s]", newIdValue));
        }

        AppUser currentUser = getCurrentUser();

        StaticId newStaticId = StaticId.builder()
                .value(newIdValue)
                .createdBy(currentUser)
                .build();

        staticIdDao.saveStaticId(newStaticId, componentId);
    }

    private AppUser getCurrentUser() {
        AppUser currentUser = appUserService.getCurrentUser();
        if (currentUser == null) {
            throw new RuntimeException("Could not identify current authorized user");
        }
        return currentUser;
    }

}
