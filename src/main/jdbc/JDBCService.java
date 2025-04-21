package main.jdbc;

import java.util.*;
import java.sql.*;

import main.jdbc.CategoryDAO.Category;
import main.jdbc.ItemDAO.Item;
import main.jdbc.TransactionDAO.Transaction;

public class JDBCService {
    
    private CategoryDAO categoryDAO;
    private ItemDAO itemDAO;
    private TransactionDAO transactionDAO;

    public static class TransactionResponse {
        public boolean success;
        public List<Transaction> data;
        
        public TransactionResponse() {
            this.success = false;
            this.data = new ArrayList<>();
        }
    }

    public static class WarehouseResponse {
        public boolean success;
        public List<Category> data;
        
        public WarehouseResponse() {
            this.success = false;
            this.data = new ArrayList<>();
        }
    }
    
    public static class ItemsResponse {
        public boolean success;
        public List<Item> data;
        
        public ItemsResponse() {
            this.success = false;
            this.data = new ArrayList<>();
        }
    }

    public static class BasicResponse {
        public boolean success;
        public String message;
        
        public BasicResponse() {
            this.success = false;
            this.message = "";
        }
    }
    
    public static class SendReceivePayload {
        public int itemId;
        public int quantity;
    }
    
    public JDBCService() {
        this.categoryDAO = new CategoryDAO();
        this.itemDAO = new ItemDAO();
        this.transactionDAO = new TransactionDAO();
    }

    public TransactionResponse fetchAllTransactions() {
        TransactionResponse response = new TransactionResponse();
        
        try {
            List<Transaction> transactions = transactionDAO.getAllTransactions();
            response.data = transactions;
            response.success = true;
        } catch (SQLException e) {
            System.err.println("Error fetching transactions: " + e.getMessage());
            response.success = false;
        }
        
        return response;
    }

    public WarehouseResponse fetchAllCompartments() {
        WarehouseResponse response = new WarehouseResponse();
        
        try {
            List<Category> categories = categoryDAO.getAllCategories();
            response.data = categories;
            response.success = true;
        } catch (SQLException e) {
            System.err.println("Error fetching compartments: " + e.getMessage());
            response.success = false;
        }
        
        return response;
    }

    public ItemsResponse fetchAllItems() {
        ItemsResponse response = new ItemsResponse();
        
        try {
            List<Item> items = itemDAO.getAllItems();
            response.data = items;
            response.success = true;
        } catch (SQLException e) {
            System.err.println("Error fetching items: " + e.getMessage());
            response.success = false;
        }
        
        return response;
    }

    public BasicResponse sendTransaction(SendReceivePayload payload) {
        BasicResponse response = new BasicResponse();
        
        try {
            Item item = itemDAO.checkQuantityForSend(payload.itemId, payload.quantity);
            
            if (item == null) {
                response.success = false;
                response.message = "Not enough quantity available for this item";
                return response;
            }
            
            item = itemDAO.updateQuantity(payload.itemId, -payload.quantity);
            
            if (item == null) {
                response.success = false;
                response.message = "Failed to update item quantity";
                return response;
            }
            
            transactionDAO.createTransaction(payload.itemId, payload.quantity, "SEND");
            
            response.success = true;
            response.message = "Item sent successfully";
        } catch (SQLException e) {
            System.err.println("Error in send transaction: " + e.getMessage());
            response.success = false;
            response.message = "Database error: " + e.getMessage();
        }
        
        return response;
    }

    public BasicResponse receiveTransaction(SendReceivePayload payload) {
        BasicResponse response = new BasicResponse();
        
        try {
            List<Item> items = itemDAO.getAllItems();
            Item targetItem = null;
            
            for (Item item : items) {
                if (item.id == payload.itemId) {
                    targetItem = item;
                    break;
                }
            }
            
            if (targetItem == null) {
                response.success = false;
                response.message = "Item not found";
                return response;
            }
            
            boolean hasSpace = categoryDAO.checkSpaceForReceive(targetItem.categoryId, payload.quantity);
            
            if (!hasSpace) {
                response.success = false;
                response.message = "Not enough space in the category for this quantity";
                return response;
            }
            
            Item updatedItem = itemDAO.updateQuantity(payload.itemId, payload.quantity);
            
            if (updatedItem == null) {
                response.success = false;
                response.message = "Failed to update item quantity";
                return response;
            }
            
            transactionDAO.createTransaction(payload.itemId, payload.quantity, "RECEIVE");
            
            response.success = true;
            response.message = "Item received successfully";
        } catch (SQLException e) {
            System.err.println("Error in receive transaction: " + e.getMessage());
            response.success = false;
            response.message = "Database error: " + e.getMessage();
        }
        
        return response;
    }
} 