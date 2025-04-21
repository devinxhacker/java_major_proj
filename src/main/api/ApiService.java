package main.api;

import java.io.IOException;

import com.google.gson.Gson;

import okhttp3.*;

import main.api.ApiSchema.*;


public class ApiService {

    private final OkHttpClient client;
    private final Gson gson;
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private static final String BASE_URL = "https://dbms-warehouse-backend.vercel.app/api";

    public ApiService() {
        this.client = new OkHttpClient();
        this.gson = new Gson(); 
    }

    private String executeGet(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }
    
    private String executePost(String url, RequestBody body) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    public TransactionApiResponse fetchAllTransactions() {
        String url = BASE_URL + "/transactions";
        try {
            String jsonResponse = executeGet(url);
            TransactionApiResponse obj = gson.fromJson(jsonResponse, TransactionApiResponse.class);
            return obj;
        } catch (IOException e) {
            System.err.println("Network Error fetching transactions: " + e.getMessage());
            return null;
        }
    }

    public WarehouseApiResponse fetchAllCompartments() {
        String url = BASE_URL + "/compartments";
        try {
            String jsonResponse = executeGet(url);
            WarehouseApiResponse obj = gson.fromJson(jsonResponse, WarehouseApiResponse.class);
            return obj;
        } catch (IOException e) {
            System.err.println("Network Error fetching compartments: " + e.getMessage());
            return null;
        }
    }

    public ItemsApiResponse fetchAllItems() {
        String url = BASE_URL + "/items";
        try {
            String jsonResponse = executeGet(url);
            ItemsApiResponse obj = gson.fromJson(jsonResponse, ItemsApiResponse.class);
            return obj;
        } catch (IOException e) {
            System.err.println("Network Error fetching items: " + e.getMessage());
            return null;
        }
    }
    
    public PostResponse sendTransaction(SendReceivePayload payload) {
        String url = BASE_URL + "/transactions/send";
        try {
            String jsonBody = gson.toJson(payload);
            RequestBody body = RequestBody.create(jsonBody, JSON);
            String jsonResponse = executePost(url, body);
            return gson.fromJson(jsonResponse, PostResponse.class);
        } catch (IOException e) {
            System.err.println("Network Error sending transaction: " + e.getMessage());
            return null;
        }
    }

    public PostResponse receiveTransaction(SendReceivePayload payload) {
        String url = BASE_URL + "/transactions/receive";
         try {
            String jsonBody = gson.toJson(payload);
            RequestBody body = RequestBody.create(jsonBody, JSON);
            String jsonResponse = executePost(url, body);
            return gson.fromJson(jsonResponse, PostResponse.class);
        } catch (IOException e) {
            System.err.println("Network Error receiving transaction: " + e.getMessage());
            return null;
        }
    }

} 