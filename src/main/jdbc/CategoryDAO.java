package main.jdbc;

import java.sql.*;
import java.util.*;

import main.jdbc.ItemDAO.Item;

public class CategoryDAO {
    
    public static class Category {
        public int id;
        public String name;
        public int maxCapacity;
        public int currentCapacity;
        public int availableSpace;
        public int utilizationPercentage;
        public List<Item> items;
        
        public Category() {
            this.items = new ArrayList<>();
        }
        
        @Override
        public String toString() {
            return String.format("Category[id=%d, name=%s, usage=%d/%d (%d%%)]", 
                    id, name, currentCapacity, maxCapacity, utilizationPercentage);
        }
    }
    
    public List<Category> getAllCategories() throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnectionManager.getInstance().getConnection();
            
            String categorySql = "SELECT category_id, name, max_capacity, current_capacity, " +
                    "(max_capacity - current_capacity) as available_space, " +
                    "CASE WHEN max_capacity > 0 THEN (current_capacity * 100 / max_capacity) ELSE 0 END as utilization_percentage " +
                    "FROM categories ORDER BY name";
            
            stmt = conn.prepareStatement(categorySql);
            rs = stmt.executeQuery();
            
            List<Category> categories = new ArrayList<>();
            
            while (rs.next()) {
                Category category = new Category();
                category.id = rs.getInt("category_id");
                category.name = rs.getString("name");
                category.maxCapacity = rs.getInt("max_capacity");
                category.currentCapacity = rs.getInt("current_capacity");
                category.availableSpace = rs.getInt("available_space");
                category.utilizationPercentage = rs.getInt("utilization_percentage");
                
                categories.add(category);
            }
            
            rs.close();
            stmt.close();
            
            for (Category category : categories) {
                String itemSql = "SELECT item_id, name, category_id, max_quantity, current_quantity, " +
                        "(max_quantity - current_quantity) as available_space " +
                        "FROM items WHERE category_id = ? ORDER BY name";
                
                stmt = conn.prepareStatement(itemSql);
                stmt.setInt(1, category.id);
                rs = stmt.executeQuery();
                
                while (rs.next()) {
                    Item item = new Item();
                    item.id = rs.getInt("item_id");
                    item.name = rs.getString("name");
                    item.categoryId = rs.getInt("category_id");
                    item.categoryName = category.name;
                    item.maxQuantity = rs.getInt("max_quantity");
                    item.currentQuantity = rs.getInt("current_quantity");
                    item.availableSpace = rs.getInt("available_space");
                    
                    category.items.add(item);
                }
                
                rs.close();
                stmt.close();
            }
            
            return categories;
        } finally {
            DBConnectionManager.getInstance().closeResources(conn, stmt, rs);
        }
    }

    public boolean checkSpaceForReceive(int categoryId, int quantity) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnectionManager.getInstance().getConnection();
            
            String sql = "SELECT (max_capacity - current_capacity >= ?) as has_space " +
                    "FROM categories WHERE category_id = ?";
            
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, quantity);
            stmt.setInt(2, categoryId);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getBoolean("has_space");
            }
            
            return false;
        } finally {
            DBConnectionManager.getInstance().closeResources(conn, stmt, rs);
        }
    }
} 