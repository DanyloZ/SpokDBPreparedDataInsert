package com.danylo.spokdbupdate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lots {
    private List<Lot> lots = new ArrayList<>();

    public Lots(String path) throws IOException {
        File source = new File(path, "lots");
        List<List<String>> lines = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(source))) {
            String line;
            List<String> group = new ArrayList<>();
            while ((line = bufferedReader.readLine())!= null) {
                if(line.length() > 2) {
                    group.add(line);
                } else {
                    lines.add(group);
                    group = new ArrayList<>();
                }
            }
        }
        for (List<String> group : lines) {
            String name = group.get(0);
            String description = group.get(1);
            String category = group.get(2);
            List<String> images = new ArrayList<>();
            int i = 3;
            while (group.get(i).startsWith("http")) {
                images.add(group.get(i++));
            }
            String moneyInfo = group.get(i++);
            float price = Float.parseFloat(moneyInfo.substring(0, moneyInfo.indexOf(" ")));
            float quickBuyPrice = 0;
            if (moneyInfo.contains("(")) {
                quickBuyPrice = Float.parseFloat(moneyInfo.substring(moneyInfo.indexOf("(") + 1, moneyInfo.lastIndexOf(" ")));
            }
            String currency = moneyInfo.substring(moneyInfo.indexOf(" ") + 1, moneyInfo.indexOf(" ") + 4);
            String minStepString = group.get(i++);
            int minStep = Integer.parseInt(minStepString.substring(minStepString.indexOf("-") + 2, minStepString.lastIndexOf(" ")));
            String dates = group.get(i);
            String startDate = dates.substring(0, dates.indexOf(" "));
            String endDate = dates.substring(dates.lastIndexOf(" ") + 1, dates.length());
            Lot lot = new Lot(name, description, category, images, currency, price, quickBuyPrice, minStep, startDate, endDate);
            lots.add(lot);
        }
    }

    public void loadToDB(Connection conn) throws SQLException, ParseException, IOException {
        Map<String, Integer> categoryIds = new HashMap<>();
        for ( Lot lot : lots) {
            String category = lot.getCategory();
            String sql = "SELECT id FROM category WHERE name = '" + category +"'";
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                categoryIds.put(category, resultSet.getInt(1));
            }
            statement.close();
        }
        String getUserCountQuery = "SELECT COUNT(*) FROM user";
        PreparedStatement statement = conn.prepareStatement(getUserCountQuery);
        ResultSet resultSet = statement.executeQuery();
        int usersCount = 0;
        while(resultSet.next()) {
            usersCount = resultSet.getInt(1);
        }
        DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        int userId = 1;
        String insertLot = "INSERT INTO lot (title, description, categoryId, userId, startDate, endDate, startPrice, minStep, quickBuyPrice, type) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        statement = conn.prepareStatement(insertLot);
        Map<String, List<String>> images = new HashMap<>();
        for (Lot lot : lots) {
            statement.setString(1, lot.getName());
            statement.setString(2, lot.getDescription());
            statement.setInt(3, categoryIds.get(lot.getCategory()));
            statement.setInt(4, userId++);
            if(userId > usersCount) {
                userId = 1;
            }
            Timestamp startDate = new Timestamp(formatter.parse(lot.getStartDate()).getTime());
            statement.setTimestamp(5, startDate);
            Timestamp endDate = new Timestamp(formatter.parse(lot.getEndDate()).getTime());
            statement.setTimestamp(6, endDate);
            statement.setBigDecimal(7, BigDecimal.valueOf(lot.getPrice()));
            statement.setBigDecimal(8, BigDecimal.valueOf(lot.getStep()));
            if (lot.getQuickBuyPrice() > 0) {
                statement.setBigDecimal(9, BigDecimal.valueOf(lot.getQuickBuyPrice()));
            }
            statement.setString(10, "P");
            statement.executeUpdate();
            images.put(lot.getName(), lot.getImages());
        }

        Map<String, Integer> lotIds = new HashMap<>();
        String sql = "SELECT id FROM lot WHERE title = ?";
        statement = conn.prepareStatement(sql);
        for ( Lot lot : lots) {
            String name = lot.getName();
            statement.setString(1, lot.getName());
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                lotIds.put(name, resultSet.getInt(1));
            }
        }

        for (Map.Entry<String, List<String>> entry : images.entrySet()) {
            int lotId = lotIds.get(entry.getKey());
            String insertImage = "INSERT INTO lotimage (lotId, image, type) VALUES (?, ?, ?);";
            statement = conn.prepareStatement(insertImage);
            for (int i = 0; i < entry.getValue().size(); i++) {
                String image = entry.getValue().get(i);
                statement.setInt(1, lotId);
                statement.setBlob(2, new URL(image).openStream());
                if (i == 0) {
                    statement.setString(3, "M");
                } else {
                    statement.setString(3, "C");
                }
                statement.executeUpdate();
            }
        }
    }
}
