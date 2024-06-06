package com.example.sl_widget;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class Utility {
    public static String[] getStops() throws IOException {
        String urlString = "https://transport.integration.sl.se/v1/sites?expand=true";

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonArray array = gson.fromJson(getData(urlString), JsonArray.class);

        String[] temp = new String[array.size()];

        for (int i = 0; i < array.size(); i++) {
            JsonObject object = array.get(i).getAsJsonObject();
            String name = object.get("name").getAsString();
            String id = object.get("id").getAsString();
            temp[i] = name + ":" + id;
        }

        return temp;
    }

    public static String[] filter(String[] array, String query) {
        return Arrays.stream(array).filter(s -> s.toLowerCase().contains(query.toLowerCase())).sorted().toArray(String[]::new);
    }

    public static String[] getDepartures(String siteId, String mode) throws IOException {
        String urlString = "https://transport.integration.sl.se/v1/sites/"
                + siteId + "/departures?transport=" + mode + "&forecast=60";

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject object = gson.fromJson(getData(urlString), JsonObject.class);
        JsonArray array = object.get("departures").getAsJsonArray();

        String[] temp = new String[Math.min(array.size(), 4)];

        for (int i = 0; i < temp.length; i++) {
            JsonObject obj = array.get(i).getAsJsonObject();
            String line = obj.get("line").getAsJsonObject().get("designation").getAsString();
            String dest = obj.get("destination").getAsString();
            String time = obj.get("display").getAsString();
            temp[i] = line + " " + dest + ":" + time;
        }

        return temp;
    }

    static String getData(String urlString) {
        StringBuilder sb = new StringBuilder();
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            try {
                URL url = new URL(urlString);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                }
            } catch (Exception e) {
                System.out.println("Error: " + e);
            }
            return sb.toString();
        });

        return future.join();
    }
}
