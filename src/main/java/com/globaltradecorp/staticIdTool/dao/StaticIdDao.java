package com.globaltradecorp.staticIdTool.dao;

import com.globaltradecorp.staticIdTool.model.ComponentType;
import com.globaltradecorp.staticIdTool.model.StaticId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * @author Dmitri Grosu (dmitri.grosu@codefactorygroup.com), 1/8/21
 */
@Component
public class StaticIdDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public StaticIdDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<ComponentType> getComponentList() {
        String sql = "select * from staticid.component_type where deleted_at is null";
        return jdbcTemplate.query(sql, new ComponentTypeRowMapper());
    }

    public List<StaticId> getStaticIdList(int componentId, String suffix, int rowsCount) {
        String sql = "select c_id.id, " +
                "c_id.component_id as component_id, " +
                "c_id.id_value, " +
                "c_id.create_user_id as user_id, " +
                "c_id.created_at, " +
                "au.username as username, " +
                "au.first_name as user_first_name, " +
                "au.last_name as user_last_name, " +
                "au.email as user_email, " +
                "ct.name as component_name " +
                "from staticid.created_id as c_id " +
                "join staticid.app_user au on c_id.create_user_id = au.id " +
                "join staticid.component_type ct on c_id.component_id = ct.id " +
                "where ct.id = ? and c_id.id_value like ? and c_id.deleted_at is null " +
                "order by c_id.id_value desc " +
                "limit ?";
        return jdbcTemplate.query(sql, new StaticIdRowMapper(), componentId, "%" + suffix, rowsCount);
    }

    public boolean idValueExists(String newIdValue) {
        try {
            String sql = "select count(*) from staticid.created_id where id_value = ? and deleted_at is null";
            Long found = jdbcTemplate.queryForObject(sql, Long.class, newIdValue);
            return found != null && found > 0;
        } catch (EmptyResultDataAccessException ex) {
            return false;
        }
    }

    public void saveStaticId(StaticId staticId) {
        if (staticId.isNew()) {
            String sql = "insert into staticid.created_id(component_id, id_value, create_user_id) " +
                    "values (?, ?, ?)";
            jdbcTemplate.update(sql,
                    staticId.getComponentType().getId(),
                    staticId.getValue(),
                    staticId.getCreatedBy().getId()
            );
        } else {
            String sql = "update staticid.created_id set component_id=?, id_value=?, create_user_id=? where id=?";
            jdbcTemplate.update(sql,
                    staticId.getComponentType().getId(),
                    staticId.getValue(),
                    staticId.getCreatedBy().getId(),
                    staticId.getId()
            );
        }
    }

    public ComponentType getComponentTypeById(Integer componentId) {
        String sql = "select * from staticid.component_type where id = ?";
        return jdbcTemplate.queryForObject(sql, new ComponentTypeRowMapper(), componentId);
    }

    public int getUserIdById(int id) {
        String sql = "select create_user_id from staticid.created_id where id=? and deleted_at is null";
        Integer queryResult = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return queryResult == null ? 0 : queryResult;
    }

    public void deleteByIds(List<Integer> selectedIds, OffsetDateTime userDateTime) {
        String[] ids = new String[selectedIds.size()];
        for (int i = 0; i < selectedIds.size(); i++) {
            Integer selectedId = selectedIds.get(i);
            ids[i] = selectedId.toString();
        }
        String sql = "update staticid.created_id set deleted_at = ? where id in (" + String.join(",", ids) + ")";
        jdbcTemplate.update(sql, userDateTime);
    }

}
