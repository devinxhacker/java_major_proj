package main.api;

import java.util.Date;

public class ApiSchema {

	// transactions
    public static class TransactionApiResponse {
        
    	public boolean success;
        public TransactionData[] data;
    }

    public static class TransactionData {
        
    	public int id, itemId, quantity;
        public String itemName, categoryName, type;
        public Date date;
    }

    // warehouse
    public static class WarehouseApiResponse {
        
    	public boolean success;
        public CategoryData[] data;
    }

    public static class CategoryData {
    	
        public int id, maxCapacity, currentCapacity, availableSpace, utilizationPercentage;
        public String name;
        public ItemData[] items;
    }

    public static class ItemData {
        
    	public int id, maxQuantity, currentQuantity, availableSpace;
        public String name;
    }
    
    // send/receive
    public static class ItemsApiResponse {
    
    	public boolean success;
    	public ItemsData[] data;
    }
    
    public static class ItemsData {
    	
    	public int id, categoryId, maxQuantity, currentQuantity, availableSpace;
    	public String name, categoryName;
    }

    public static class SendReceivePayload {
    	
        public int itemId, quantity;
    }

    public static class PostResponse {
        
        public boolean success;
        public String message;
    }

} 