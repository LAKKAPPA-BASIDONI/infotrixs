package com.mycompany.currency;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
//infotrix
public class Currency {
    private static final String API_BASE_URL = "https://api.apilayer.com/exchangerates_data/";
    private static final String API_KEY = "2bddacbe3239d233d8a1c6441781add1"; // Replace with your actual API key

    public static void main(String[] args) {
        Map<String, Double> favoriteCurrencies = new HashMap<>();

        while (true) {
            System.out.println("Currency Converter Menu:");
            System.out.println("1. Convert Currency");
            System.out.println("2. Add to Favorites");
            System.out.println("3. View Favorites");
            System.out.println("4. Exit");

            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                int choice = Integer.parseInt(reader.readLine());

                switch (choice) {
                    case 1:
                        convertCurrency(favoriteCurrencies);
                        break;
                    case 2:
                        addToFavorites(favoriteCurrencies);
                        break;
                    case 3:
                        viewFavorites(favoriteCurrencies);
                        break;
                    case 4:
                        System.out.println("Goodbye!");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid choice. Please select a valid option.");
                }
            } catch (IOException | NumberFormatException e) {
                System.out.println("Invalid input. Please try again.");
            }
        }
    }

    private static void convertCurrency(Map<String, Double> favorites) {
        try {
            System.out.println("Enter the amount to convert:");
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            double amount = Double.parseDouble(reader.readLine());

            System.out.println("Enter the source currency (e.g., USD):");
            String sourceCurrency = reader.readLine().toUpperCase();

            System.out.println("Enter the target currency (e.g., EUR):");
            String targetCurrency = reader.readLine().toUpperCase();

            String endpoint = "convert?from=" + sourceCurrency + "&to=" + targetCurrency + "&amount=" + amount;
            String response = sendGetRequest(endpoint);

            // Parse JSON response
            // Example response: {"result":34.56,"info":"Conversion from USD to EUR","date":"2023-10-07"}
            double result = Double.parseDouble(response.split(":")[1].split(",")[0]);

            System.out.println("Converted amount: " + result + " " + targetCurrency);
        } catch (IOException | NumberFormatException e) {
            System.out.println("Invalid input or API request failed. Please try again.");
        }
    }

    private static void addToFavorites(Map<String, Double> favorites) {
        try {
            System.out.println("Enter the currency code to add to favorites (e.g., USD):");
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String currencyCode = reader.readLine().toUpperCase();

            System.out.println("Enter the exchange rate for " + currencyCode + ":");
            double exchangeRate = Double.parseDouble(reader.readLine());

            favorites.put(currencyCode, exchangeRate);
            System.out.println(currencyCode + " added to favorites.");
        } catch (IOException | NumberFormatException e) {
            System.out.println("Invalid input. Please try again.");
        }
    }

    private static void viewFavorites(Map<String, Double> favorites) {
        System.out.println("Favorite Currencies:");
        for (Map.Entry<String, Double> entry : favorites.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    private static String sendGetRequest(String endpoint) throws IOException {
        URL url = new URL(API_BASE_URL + endpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");
        connection.setRequestProperty("Api-Key", API_KEY);

        int responseCode = connection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        } else {
            throw new IOException("Request failed with HTTP error code: " + responseCode);
        }
    }
}
