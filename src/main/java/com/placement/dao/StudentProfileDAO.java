
package com.placement.dao;

import com.placement.config.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentProfileDAO {

    public int getStudentIdByUserId(int userId) throws SQLException {

        String query = "SELECT student_id FROM student_profiles WHERE user_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return rs.getInt("student_id");
                }
            }
        }

        return -1;
    }
}
