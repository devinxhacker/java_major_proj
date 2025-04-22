package main.jdbc;

import java.util.*;
import java.sql.*;

import main.jdbc.CategoryDAO.Category;
import main.jdbc.ItemDAO.Item;
import main.jdbc.TransactionDAO.Transaction;
import main.jdbc.RequestsDAO.Request;
import main.jdbc.EmployeesDAO.Employee;

public class JDBCService {

    private CategoryDAO categoryDAO;
    private ItemDAO itemDAO;
    private TransactionDAO transactionDAO;
    private RequestsDAO requestsDAO;
    private EmployeesDAO employeesDAO;

    public static class RequestResponse {
        public boolean success;
        public List<Request> data;

        public RequestResponse() {
            this.success = false;
            this.data = new ArrayList<>();
        }
    }

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

    public static class CreateRequestPayload {
        public int itemId;
        public int quantity;
        public String type;
    }

    public static class AuthResponse {
        public boolean success;
        public Employee data;
        public String message;

        public AuthResponse() {
            this.success = false;
            this.data = null;
            this.message = "";
        }
    }

    public static class RegisterPayload {
        public String username;
        public String password;
        public String name;
    }

    public static class LoginPayload {
        public String username;
        public String password;
    }

    public JDBCService() {
        this.categoryDAO = new CategoryDAO();
        this.itemDAO = new ItemDAO();
        this.transactionDAO = new TransactionDAO();
        this.requestsDAO = new RequestsDAO();
        this.employeesDAO = new EmployeesDAO();
    }

    public RequestResponse fetchAllRequests() {
        RequestResponse response = new RequestResponse();

        try {
            List<Request> requests = requestsDAO.getAllRequests();
            response.data = requests;
            response.success = true;
        } catch (SQLException e) {
            System.err.println("Error fetching requests: " + e.getMessage());
            response.success = false;
        }

        return response;
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

//            transactionDAO.createTransaction(payload.itemId, payload.quantity, "SEND");

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

//            transactionDAO.createTransaction(payload.itemId, payload.quantity, "RECEIVE");

            response.success = true;
            response.message = "Item received successfully";
        } catch (SQLException e) {
            System.err.println("Error in receive transaction: " + e.getMessage());
            response.success = false;
            response.message = "Database error: " + e.getMessage();
        }

        return response;
    }

    public BasicResponse createRequest(CreateRequestPayload payload) {
        BasicResponse response = new BasicResponse();
        try {
            requestsDAO.createRequest(payload.itemId, payload.quantity, payload.type);
            response.success = true;
            response.message = "Requested Successfully!";
        } catch (SQLException e) {
            System.out.println("Error in requesting transaction: " + e.getMessage());
            response.success = false;
            response.message = "Database error: " + e.getMessage();
        }
        return response;
    }

    public BasicResponse acceptRequest(int requestId) {
        BasicResponse response = new BasicResponse();

        try {
            Request request = null;
            List<Request> requests = requestsDAO.getAllRequests();
            for (Request r : requests) {
                if (r.requestId == requestId) {
                    request = r;
                    break;
                }
            }

            if (request == null) {
                response.success = false;
                response.message = "Request not found";
                return response;
            }
            SendReceivePayload payload = new SendReceivePayload();
            payload.itemId = request.itemId;
            payload.quantity = request.quantity;
            String transactionType = request.requestType.toUpperCase(); // Convert to uppercase
            System.out.println(transactionType);
            if (transactionType.equals("SEND")) { // Use equals() for string comparison
                BasicResponse res = sendTransaction(payload);
                if (!res.success) {
                    return res;
                }
            } else if (transactionType.equals("RECEIVE")) { // Use equals() for string comparison
                BasicResponse res = receiveTransaction(payload);
                if (!res.success) {
                    return res;
                }
            } else {
                response.success = false;
                response.message = "Invalid transaction type: " + transactionType;
                return response;
            }
            requestsDAO.updateRequestStatus(requestId, 1);
            int transactionId = transactionDAO.createTransaction(request.itemId, request.quantity, transactionType); // transactionType
                                                                                                                     // is
                                                                                                                     // now
                                                                                                                     // uppercase
            requestsDAO.createAcceptedRequest(requestId, transactionId);

            response.success = true;
            response.message = "Request accepted successfully";
        } catch (SQLException e) {
            System.err.println("Error accepting request: " + e.getMessage());
            response.success = false;
            response.message = "Database error: " + e.getMessage();
        }

        return response;
    }

    public BasicResponse denyRequest(int requestId) {
        BasicResponse response = new BasicResponse();

        try {
            requestsDAO.updateRequestStatus(requestId, 0);
            response.success = true;
            response.message = "Request denied successfully";
        } catch (SQLException e) {
            System.err.println("Error denying request: " + e.getMessage());
            response.success = false;
            response.message = "Database error: " + e.getMessage();
        }

        return response;
    }

    public AuthResponse registerEmployee(RegisterPayload payload) {
        AuthResponse response = new AuthResponse();

        try {
            if (payload.username == null || payload.username.trim().isEmpty()) {
                response.success = false;
                response.message = "Username cannot be empty";
                return response;
            }

            if (payload.password == null || payload.password.trim().isEmpty()) {
                response.success = false;
                response.message = "Password cannot be empty";
                return response;
            }

            if (payload.name == null || payload.name.trim().isEmpty()) {
                response.success = false;
                response.message = "Name cannot be empty";
                return response;
            }

            Employee employee = employeesDAO.registerEmployee(
                payload.username.trim(), 
                payload.password.trim(), 
                payload.name.trim()
            );

            if (employee == null) {
                response.success = false;
                response.message = "Username already exists";
                return response;
            }

            response.success = true;
            response.data = employee;
            response.message = "Registration successful";
        } catch (SQLException e) {
            System.err.println("Error in employee registration: " + e.getMessage());
            response.success = false;
            response.message = "Database error: " + e.getMessage();
        }

        return response;
    }

    public AuthResponse loginEmployee(LoginPayload payload) {
        AuthResponse response = new AuthResponse();

        try {
            if (payload.username == null || payload.username.trim().isEmpty()) {
                response.success = false;
                response.message = "Username cannot be empty";
                return response;
            }

            if (payload.password == null || payload.password.trim().isEmpty()) {
                response.success = false;
                response.message = "Password cannot be empty";
                return response;
            }

            Employee employee = employeesDAO.loginEmployee(
                payload.username.trim(), 
                payload.password.trim()
            );

            if (employee == null) {
                response.success = false;
                response.message = "Invalid username or password";
                return response;
            }

            response.success = true;
            response.data = employee;
            response.message = "Login successful";
        } catch (SQLException e) {
            System.err.println("Error in employee login: " + e.getMessage());
            response.success = false;
            response.message = "Database error: " + e.getMessage();
        }

        return response;
    }
}