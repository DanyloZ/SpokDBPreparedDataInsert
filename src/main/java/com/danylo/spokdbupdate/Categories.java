package com.danylo.spokdbupdate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Categories {
    private List<String> categories = new ArrayList<>();
    private Map<String, String> subCategories = new HashMap<>();

    public Categories(String path) throws IOException {
        File source = new File(path, "categories");

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(source))) {
            String line;
            while ((line = bufferedReader.readLine())!= null) {
                if(line.length() > 1) {
                    if(!line.startsWith("-")) {
                        categories.add(line);
                    } else {
                        line = line.substring(2);
                        subCategories.put(line, categories.get(categories.size() -1));
                    }
                }
            }
        }
    }

    public void loadToDB(Connection conn) throws SQLException {
        Map<String, Integer> categoryIDs = new HashMap<>();

        String sql = "INSERT INTO category (name) VALUES (?)";
        PreparedStatement statement = conn.prepareStatement(sql);
        for (String category: categories) {
            statement.setString(1, category);
            statement.executeUpdate();
        }

        sql = "SELECT id, name FROM category";
        statement = conn.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();
        while(resultSet.next()) {
            categoryIDs.put(resultSet.getString(2), resultSet.getInt(1));
        }

        sql = "INSERT INTO category (name, parentId) VALUES (?, ?)";
        statement = conn.prepareStatement(sql);
        for (Map.Entry<String, String> entry : subCategories.entrySet()) {
            statement.setString(1, entry.getKey());
            int categoryID = categoryIDs.get(entry.getValue());
            statement.setInt(2, categoryID);
            statement.executeUpdate();
        }
    }
}
