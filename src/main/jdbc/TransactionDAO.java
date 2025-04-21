package main.jdbc;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TransactionDAO {
    
    public static class Transaction {
        public int id;
        public int itemId;
        public String itemName;
        public String categoryName;
        public int quantity;
        public String type; // SEND or RECEIVE
        public Date date;
        
        @Override
        public String toString() {
            return String.format("Transaction[id=%d, item=%s, quantity=%d, type=%s]", 
                    id, itemName, quantity, type);
        }
    }

    public List<Transaction> getAllTransactions() throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Transaction> transactions = new ArrayList<>();
        
        try {
            conn = DBConnectionManager.getInstance().getConnection();
            
            String sql = "SELECT t.transaction_id, t.item_id, i.name as item_name, " +
                    "c.name as category_name, t.quantity, t.transaction_type, t.transaction_date " +
                    "FROM transactions t " +
                    "JOIN items i ON t.item_id = i.item_id " +
                    "JOIN categories c ON i.category_id = c.category_id " +
                    "ORDER BY t.transaction_date DESC";
            
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                Transaction t = new Transaction();
                t.id = rs.getInt("transaction_id");
                t.itemId = rs.getInt("item_id");
                t.itemName = rs.getString("item_name");
                t.categoryName = rs.getString("category_name");
                t.quantity = rs.getInt("quantity");
                t.type = rs.getString("transaction_type");
                t.date = rs.getTimestamp("transaction_date");
                
                transactions.add(t);
            }
            
            return transactions;
        } finally {
            DBConnectionManager.getInstance().closeResources(conn, stmt, rs);
        }
    }

    public int createTransaction(int itemId, int quantity, String type) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnectionManager.getInstance().getConnection();
            
            String sql = "INSERT INTO transactions (item_id, quantity, transaction_type) " +
                    "VALUES (?, ?, ?) RETURNING transaction_id";
            
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, itemId);
            stmt.setInt(2, quantity);
            stmt.setString(3, type);
            
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
            throw new SQLException("Failed to create transaction, no ID returned");
        } finally {
            DBConnectionManager.getInstance().closeResources(conn, stmt, rs);
        }
    }
} 