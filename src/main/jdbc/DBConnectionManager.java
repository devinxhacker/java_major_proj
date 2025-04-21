package main.jdbc;

import java.sql.*;
import java.util.Properties;

public class DBConnectionManager {
    
    private static final String JDBC_URL = "jdbc:postgresql://ep-divine-dust-a1keenu1-pooler.ap-southeast-1.aws.neon.tech:5432/neondb";
    private static final String DB_USER = "neondb_owner";
    private static final String DB_PASSWORD = "npg_jhqCe10prZRw";
    
    private static DBConnectionManager instance;
    
    private DBConnectionManager() {
    }
    
    public static synchronized DBConnectionManager getInstance() {
        if (instance == null) {
            instance = new DBConnectionManager();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
            
            Properties props = new Properties();
            props.setProperty("user", DB_USER);
            props.setProperty("password", DB_PASSWORD);
            props.setProperty("sslmode", "require");
            
            return DriverManager.getConnection(JDBC_URL, props);
        } catch (ClassNotFoundException e) {
            throw new SQLException("PostgreSQL JDBC driver not found", e);
        }
    }

    public void closeResources(Connection conn, PreparedStatement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
        } catch (SQLException e) {
            System.err.println("Error closing ResultSet: " + e.getMessage());
        }
        
        try {
            if (stmt != null) stmt.close();
        } catch (SQLException e) {
            System.err.println("Error closing PreparedStatement: " + e.getMessage());
        }
        
        try {
            if (conn != null && !conn.isClosed()) conn.close();
        } catch (SQLException e) {
            System.err.println("Error closing Connection: " + e.getMessage());
        }
    }

    public boolean testConnection() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            stmt = conn.prepareStatement("SELECT 1");
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                System.out.println("Database connection test successful");
                return true;
            }
            
            return false;
        } catch (SQLException e) {
            System.err.println("Database connection test failed: " + e.getMessage());
            return false;
        } finally {
            closeResources(conn, stmt, rs);
        }
    }
} 