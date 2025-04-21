package main.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemDAO {
    
    public static class Item {
        public int id;
        public String name;
        public int categoryId;
        public String categoryName;
        public int maxQuantity;
        public int currentQuantity;
        public int availableSpace;
    }
    
    public List<Item> getAllItems() throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Item> items = new ArrayList<>();
        
        try {
            conn = DBConnectionManager.getInstance().getConnection();
            
            String sql = "SELECT i.item_id, i.name, i.category_id, c.name as category_name, " +
                    "i.max_quantity, i.current_quantity, (i.max_quantity - i.current_quantity) as available_space " +
                    "FROM items i " +
                    "JOIN categories c ON i.category_id = c.category_id " +
                    "ORDER BY c.name, i.name";
            
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                Item item = new Item();
                item.id = rs.getInt("item_id");
                item.name = rs.getString("name");
                item.categoryId = rs.getInt("category_id");
                item.categoryName = rs.getString("category_name");
                item.maxQuantity = rs.getInt("max_quantity");
                item.currentQuantity = rs.getInt("current_quantity");
                item.availableSpace = rs.getInt("available_space");
                
                items.add(item);
            }
            
            return items;
        } finally {
            DBConnectionManager.getInstance().closeResources(conn, stmt, rs);
        }
    }
    
    public Item checkQuantityForSend(int itemId, int quantity) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnectionManager.getInstance().getConnection();
            
            String sql = "SELECT i.item_id, i.name, i.max_quantity, i.current_quantity, " +
                    "(i.current_quantity >= ?) as has_enough " +
                    "FROM items i WHERE i.item_id = ?";
            
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, quantity);
            stmt.setInt(2, itemId);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                boolean hasEnough = rs.getBoolean("has_enough");
                if (!hasEnough) {
                    return null;
                }
                
                Item item = new Item();
                item.id = rs.getInt("item_id");
                item.name = rs.getString("name");
                item.maxQuantity = rs.getInt("max_quantity");
                item.currentQuantity = rs.getInt("current_quantity");
                
                return item;
            }
            
            return null;
        } finally {
            DBConnectionManager.getInstance().closeResources(conn, stmt, rs);
        }
    }

    public Item updateQuantity(int itemId, int quantityChange) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnectionManager.getInstance().getConnection();
            
            conn.setAutoCommit(false);
            
            String sql = "UPDATE items SET current_quantity = current_quantity + ? " +
                    "WHERE item_id = ? " +
                    "RETURNING item_id, name, max_quantity, current_quantity";
            
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, quantityChange);
            stmt.setInt(2, itemId);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                Item item = new Item();
                item.id = rs.getInt("item_id");
                item.name = rs.getString("name");
                item.maxQuantity = rs.getInt("max_quantity");
                item.currentQuantity = rs.getInt("current_quantity");
                
                updateCategoryCapacity(conn, itemId, quantityChange);
                
                conn.commit();
                
                return item;
            }
            
            conn.rollback();
            return null;
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            DBConnectionManager.getInstance().closeResources(conn, stmt, rs);
        }
    }

    private void updateCategoryCapacity(Connection conn, int itemId, int quantityChange) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            String findSql = "SELECT category_id FROM items WHERE item_id = ?";
            stmt = conn.prepareStatement(findSql);
            stmt.setInt(1, itemId);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                int categoryId = rs.getInt("category_id");
                
                stmt.close();
                rs.close();
                
                String updateSql = "UPDATE categories SET current_capacity = current_capacity + ? " +
                        "WHERE category_id = ?";
                stmt = conn.prepareStatement(updateSql);
                stmt.setInt(1, quantityChange);
                stmt.setInt(2, categoryId);
                stmt.executeUpdate();
            }
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        }
    }
} 