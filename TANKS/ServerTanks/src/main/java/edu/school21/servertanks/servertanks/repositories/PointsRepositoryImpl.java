package edu.school21.servertanks.servertanks.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Component;
import edu.school21.servertanks.servertanks.models.Points;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Component
public class PointsRepositoryImpl implements PointsRepository {
    private static final String CREATE_SCHEMA_QUERY = "CREATE SCHEMA IF NOT EXISTS serverTanks;";
    private static final String DROP_TABLE_QUERY = "DROP TABLE IF EXISTS serverTanks.points;";
    private static final String CREATE_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS serverTanks.points (\n" +
            "numberClient int,\n" +
            "shot int,\n" +
            "hit int );";
    private static String SAVE_QUERY = "INSERT INTO serverTanks.points (numberClient, hit, shot) VALUES (?, ?, ?)";
    private static String UPDATE_HIT_QUERY = "UPDATE serverTanks.points SET hit = hit + 1 WHERE numberClient = ?";
    private static String UPDATE_SHOT_QUERY = "UPDATE serverTanks.points SET shot = shot + 1 WHERE numberClient = ?";
    private static String INFO_QUERY = "SELECT * FROM serverTanks.points WHERE numberclient = ?";

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public PointsRepositoryImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        init();
    }

    private void init() {
        try {
            jdbcTemplate.execute(CREATE_SCHEMA_QUERY);
            jdbcTemplate.execute(DROP_TABLE_QUERY);
            jdbcTemplate.execute(CREATE_TABLE_QUERY);
        } catch (DataAccessException ex) {
            System.err.println("Can't create a db");
        }
    }

    @Override
    public void saveClient(Integer numberClient) {
        int rowsAffected = jdbcTemplate.update(SAVE_QUERY, numberClient, 0, 0);
        if (rowsAffected == 0) {
            throw new RuntimeException("Failed to save client: " + numberClient);
        }
    }

    @Override
    public void updateHit(Integer numberClient) {
        int rowsAffected = jdbcTemplate.update(UPDATE_HIT_QUERY, numberClient);
        if (rowsAffected == 0) {
            throw new RuntimeException("Failed to update hit for client: " + numberClient);
        }
    }

    @Override
    public void updateShot(Integer numberClient) {
        int rowsAffected = jdbcTemplate.update(UPDATE_SHOT_QUERY, numberClient);
        if (rowsAffected == 0) {
            throw new RuntimeException("Failed to update shot for client: " + numberClient);
        }
    }

    @Override
    public Points getInfo(int num) {
        return jdbcTemplate.query(INFO_QUERY, new Object[]{num},
                new BeanPropertyRowMapper<>(Points.class)).stream().findAny().orElse(null);
    }
}
