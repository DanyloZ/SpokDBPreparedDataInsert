package com.danylo.spokdbupdate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Users {
    List<User> users = new ArrayList<>();

    public Users(String path) throws IOException {
        File source = new File(path, "users");

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(source))) {
            String line;
            while ((line = bufferedReader.readLine())!= null) {
                String[] userData = line.split(", ");
                User user = new User(userData[0], userData[1], userData[2],userData[3], userData[4]);
                users.add(user);
            }
        }
    }

    public void loadToDB(Connection conn) throws SQLException, ParseException {
        DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        String sql = "INSERT INTO user (name, email, password, registrationDate, type) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement statement = conn.prepareStatement(sql);
        for (User user : users) {
            Date registrationDate = formatter.parse(user.getRegistrationDate());
            java.sql.Date sqlDate = new java.sql.Date(registrationDate.getTime());
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.setDate(4, sqlDate);
            statement.setString(5, user.getType());
            statement.executeUpdate();
        }
    }

}
