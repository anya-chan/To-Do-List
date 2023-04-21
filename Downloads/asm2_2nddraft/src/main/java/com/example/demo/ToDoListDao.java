/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.demo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author eeuser
 */
public class ToDoListDao {
    private JdbcTemplate jdbcTemplate;

    /**
     * As of Spring Framework 4.3, an @Autowired annotation on such a
     * constructor is no longer necessary if the target bean only defines one
     * constructor to begin with. However, if several constructors are
     * available, at least one must be annotated to teach the container which
     * one to use.
     */
    public ToDoListDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    public List<ToDoList> getAllToDoLists() {
        String sql = "SELECT * FROM todolists";
        return jdbcTemplate.query(sql, new RowMapper<ToDoList>() {
            @Override
            public ToDoList mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new ToDoList(
                        rs.getInt("id"),
                        rs.getString("item"),
                        rs.getString("status")
                );
            }
        });

        // alternately, using lambda
//        return jdbcTemplate.query(sql, (rs, rowNum) ->
//                new Course(
//                        rs.getInt("id"),
//                        rs.getString("name"),
//                        rs.getInt("teacher_id")
//                ));
    }


    public ToDoList getToDoListById(int id) {
        String sql = "SELECT * FROM todolists WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum)
                -> new ToDoList(
                        rs.getInt("id"),
                        rs.getString("item"),
                       rs.getString("status")
                ),
                id);
    }
    
    
    public List<ToDoList> getAllToDoListsByStatus(String status) {
        String sql = "SELECT * FROM todolists where status=?";
        return jdbcTemplate.query(sql, new RowMapper<ToDoList>() {
            @Override
            public ToDoList mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new ToDoList(
                        rs.getInt("id"),
                        rs.getString("item"),
                        rs.getString("status")
                );
            }
        },status);

        // alternately, using lambda
//        return jdbcTemplate.query(sql, (rs, rowNum) ->
//                new Course(
//                        rs.getInt("id"),
//                        rs.getString("name"),
//                        rs.getInt("teacher_id")
//                ));
    }

    public void addToDoList(ToDoList toDoList) {
        String sql = "INSERT INTO todolists (id, item, status) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql,toDoList.getId(), toDoList.getItem(), toDoList.getStatus());
    }

    public void updateToDoList(ToDoList toDoList) {
        String sql = "UPDATE todolists SET item = ?, status = ? WHERE id = ?";
        jdbcTemplate.update(sql,  toDoList.getItem(), toDoList.getStatus(), toDoList.getId());
    }

    public void deleteToDoList(int id) {
        String sql = "DELETE FROM todolists WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }


    // For update a large numbers of rows in a table
    public int[] batchUpdateUsingJdbcTemplate(List<ToDoList> toDoLists) {
        return jdbcTemplate.batchUpdate("INSERT INTO courses VALUES (?, ?, ?)",
                new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setInt(1, toDoLists.get(i).getId());
                ps.setString(2, toDoLists.get(i).getItem());
                ps.setString(3, toDoLists.get(i).getStatus());
            }

            @Override
            public int getBatchSize() {
                return 50;
            }
        });
    }
    
    // Establish connection every time, not efficient
    public void useManualConnection() throws SQLException {
        try (ResultSet rs = jdbcTemplate.getDataSource()
                .getConnection()
                .createStatement()
                .executeQuery("SELECT * FROM todolists")) {
/*
            System.out.println("The courses table:");
            while (rs.next()) {
                System.out.print(rs.getInt("id") + " ");
                System.out.print(rs.getString("name") + " ");
                System.out.println(rs.getInt("year"));
                System.out.println(rs.getFloat("rank"));
            }
*/
        }
    }
}
