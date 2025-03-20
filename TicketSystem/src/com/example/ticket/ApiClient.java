package com.example.ticket;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiClient {
    public static void sendConfig(int totalTickets, int maxTicketsInPool, int releaseRate, int retrievalRate) {
        try {
            URL url = new URL("http://localhost:8080/api/tickets/save");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String jsonInputString = String.format(
                    "{\"totalTickets\":%d,\"maxTicketsInPool\":%d,\"ticketReleaseRateMs\":%d,\"customerRetrievalRateMs\":%d}",
                    totalTickets, maxTicketsInPool, releaseRate, retrievalRate
            );

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("Data saved successfully.");
            } else {
                System.out.println("Failed to save data. HTTP Code: " + responseCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
