package com.letv.backend.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.letv.backend.model.ShardingItem;

/**
 * @author nieyanshun
 *
 */
@Repository
public class ShardingItemDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String _query =
            "select id from t_letv_sharding_test where state = 0 and id%10=? limit 2000;";

    private static final String _update =
            "update t_letv_sharding_test set state=? where id=? and state = 0;";


    public List<ShardingItem> getItems(int shardItem) {
        return jdbcTemplate.query(_query, new Object[] {shardItem}, new RowMapper<ShardingItem>() {

            @Override
            public ShardingItem mapRow(ResultSet rs, int rowNum) throws SQLException {
                ShardingItem item = new ShardingItem();
                item.setId(rs.getLong("id"));
                return item;
            }
        });
    }

    public void batchUpdate(List<ShardingItem> items) {
        jdbcTemplate.batchUpdate(_update, new BatchUpdateHelper(items));
    }

    public void update(ShardingItem item) {
        jdbcTemplate.update(_update, new Object[] {item.getState(), item.getId()});
    }

    class BatchUpdateHelper implements BatchPreparedStatementSetter {

        private List<ShardingItem> items;

        BatchUpdateHelper(List<ShardingItem> items) {
            this.items = items;
        }

        @Override
        public void setValues(PreparedStatement ps, int i) throws SQLException {
            ShardingItem item = items.get(i);
            ps.setInt(1, item.getState());
            ps.setLong(2, item.getId());
        }

        @Override
        public int getBatchSize() {
            return items.size();
        }
    }


}
