package com.bettervns.teachersservice.dao;//package com.bettervns.teacherservice.dao;
//
//import com.bettervns.teacherservice.models.Teacher;
//import org.springframework.jdbc.core.RowMapper;
//
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
//
//public class TeacherMapper implements RowMapper<Teacher> {
//
//    @Override
//    public Teacher mapRow(ResultSet resultSet, int i) throws SQLException {
//        return new Teacher(resultSet.getInt("id"), resultSet.getString("teacher_name"),
//                resultSet.getString("surname"), resultSet.getString("father_name"),
//                resultSet.getString("email"),
//                resultSet.getInt("chair_id"));
//    }
//}
