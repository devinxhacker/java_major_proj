package main.jdbc;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class RequestsDAO {

    public static class Request {
        public int requestId;
        public int itemId;
        public int quantity;
        public String requestType;
        public Date requestDate;
        public int status;
        public String itemName;

        @Override
        public String toString() {
            return String.format("Request[id=%d, item=%s, quantity=%d, type=%s, status=%d]",
                    requestId, itemName, quantity, requestType, status);
        }
    }

    public static class AcceptedRequest {
        public int acceptId;
        public int requestId;
        public int transactionId;
    }

    public List<Request> getAllRequests() throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Request> requests = new ArrayList<>();

        try {
            conn = DBConnectionManager.getInstance().getConnection();

            String sql = "SELECT tr.request_id, tr.item_id, i.name as item_name, tr.quantity, tr.request_type, tr.request_date, tr.status " +
                    "FROM total_requests tr " +
                    "JOIN items i ON tr.item_id = i.item_id " +
                    "ORDER BY tr.request_date DESC";

            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Request r = new Request();
                r.requestId = rs.getInt("request_id");
                r.itemId = rs.getInt("item_id");
                r.itemName = rs.getString("item_name");
                r.quantity = rs.getInt("quantity");
                r.requestType = rs.getString("request_type");
                r.requestDate = rs.getTimestamp("request_date");
                r.status = rs.getInt("status");

                requests.add(r);
            }

            return requests;
        } finally {
            DBConnectionManager.getInstance().closeResources(conn, stmt, rs);
        }
    }
    
    public int createRequest(int itemID, int quantity, String type) throws SQLException{
    	Connection conn = null;
    	PreparedStatement pstmt = null;
    	ResultSet rs = null;
    	try {
    		conn = DBConnectionManager.getInstance().getConnection();
    		String sql = "INSERT INTO total_requests(item_id, quantity, request_type) VALUES (?, ?, ?) RETURNING request_id";
    		pstmt = conn.prepareStatement(sql);
    		pstmt.setInt(1, itemID);
    		pstmt.setInt(2, quantity);
    		pstmt.setString(3, type);
    		
    		rs = pstmt.executeQuery();
    		
    		if(rs.next()) {
    			return rs.getInt(1);
    		}
    		
    		throw new SQLException("Failed to create request, no ID returned");
    	}
    	finally {
    		DBConnectionManager.getInstance().closeResources(conn, pstmt, rs);
    	}
    }

    public void updateRequestStatus(int requestId, int status) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DBConnectionManager.getInstance().getConnection();
            String sql = "UPDATE total_requests SET status = ? WHERE request_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, status);
            stmt.setInt(2, requestId);
            stmt.executeUpdate();
        } finally {
            DBConnectionManager.getInstance().closeResources(conn, stmt, null);
        }
    }

    public int createAcceptedRequest(int requestId, int transactionId) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnectionManager.getInstance().getConnection();
            String sql = "INSERT INTO accepted_requests (request_id, transaction_id) VALUES (?, ?) RETURNING accept_id";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, requestId);
            stmt.setInt(2, transactionId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

            throw new SQLException("Failed to create accepted request, no ID returned");
        } finally {
            DBConnectionManager.getInstance().closeResources(conn, stmt, rs);
        }
    }
}

