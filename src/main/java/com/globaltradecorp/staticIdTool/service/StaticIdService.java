package com.globaltradecorp.staticIdTool.service;

import com.globaltradecorp.staticIdTool.dao.StaticIdDao;
import com.globaltradecorp.staticIdTool.model.AppUser;
import com.globaltradecorp.staticIdTool.model.ComponentType;
import com.globaltradecorp.staticIdTool.model.StaticId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.OffsetDateTime;
import java.util.List;

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

    public List<ComponentType> getComponentList() {
        return staticIdDao.getComponentList();
    }

    public List<StaticId> getStaticIdList(int componentId, String suffix, int rowsCount) {
        return staticIdDao.getStaticIdList(componentId, suffix, rowsCount);
    }

    public void addNewIdValue(List<String> newIdValues, Integer componentId) throws IdValueExistsException {

        Assert.notNull(newIdValues, "New ID values list could not be null");
        Assert.notNull(componentId, "Component ID could not be null");
        Assert.notEmpty(newIdValues, "New ID values list could not be empty");

        if (staticIdDao.idValueExists(newIdValues)) {
            throw new IdValueExistsException(String.format("ID value already exists: [%s]", newIdValues));
        }

        AppUser currentUser = getCurrentUser();
        ComponentType componentType = staticIdDao.getComponentTypeById(componentId);

        for (String newIdValue : newIdValues) {
            addNewId(currentUser, componentType, newIdValue);
        }
    }

    private void addNewId(AppUser currentUser, ComponentType componentType, String newIdValue) {
        StaticId newStaticId = StaticId.builder()
                .value(newIdValue)
                .createdBy(currentUser)
                .componentType(componentType)
                .build();

        staticIdDao.saveStaticId(newStaticId);
    }

    public void deleteIds(List<Integer> selectedIds, OffsetDateTime userDateTime) throws IdValueAccessException {
        if (selectedIds == null || selectedIds.isEmpty()) {
            return;
        }
        if (!checkAccessToIds(selectedIds)) {
            throw new IdValueAccessException("Current user has no access to provided IDs");
        }
        staticIdDao.deleteByIds(selectedIds, userDateTime);
    }

    /**
     * Check if current logged in user has access to selected IDs
     *
     * @param selectedIds - list of selected IDs
     * @return true - user has access, false - otherwise
     */
    private boolean checkAccessToIds(List<Integer> selectedIds) {
        int currentUserId = getCurrentUser().getId();
        for (Integer selectedId : selectedIds) {
            int found = staticIdDao.getUserIdById(selectedId);
            if (found != currentUserId) {
                return false;
            }
        }
        return true;
    }

    private AppUser getCurrentUser() {
        AppUser currentUser = appUserService.getCurrentUser();
        if (currentUser == null) {
            throw new RuntimeException("Could not identify current authorized user");
        }
        return currentUser;
    }

}
