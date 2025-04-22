package main.jdbc;

import java.sql.*;

public class EmployeesDAO {
    
    private DBConnectionManager connectionManager;
    
    public EmployeesDAO() {
        this.connectionManager = DBConnectionManager.getInstance();
    }
    
    public static class Employee {
        public int id;
        public String username;
        public String name;
        
        public Employee(int id, String username, String name) {
            this.id = id;
            this.username = username;
            this.name = name;
        }
    }
    
    public Employee registerEmployee(String username, String password, String name) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = connectionManager.getConnection();
            
            stmt = conn.prepareStatement("SELECT id FROM employees WHERE username = ?");
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return null;
            }
            
            connectionManager.closeResources(null, stmt, rs);
            
            stmt = conn.prepareStatement(
                "INSERT INTO employees (username, password, name) VALUES (?, ?, ?) RETURNING id",
                Statement.RETURN_GENERATED_KEYS
            );
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, name);
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating employees failed, no rows affected.");
            }
            
            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                return new Employee(id, username, name);
            } else {
                throw new SQLException("Creating employees failed, no ID obtained.");
            }
            
        } finally {
            connectionManager.closeResources(conn, stmt, rs);
        }
    }
    
    public Employee loginEmployee(String username, String password) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = connectionManager.getConnection();
            
            stmt = conn.prepareStatement("SELECT id, name FROM employees WHERE username = ? AND password = ?");
            stmt.setString(1, username);
            stmt.setString(2, password);
            
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                return new Employee(id, username, name);
            } else {
                return null;
            }
            
        } finally {
            connectionManager.closeResources(conn, stmt, rs);
        }
    }
} 