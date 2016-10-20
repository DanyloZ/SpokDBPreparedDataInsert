package com.danylo.spokdbupdate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;

public class DbUpdate {


    public static void main(String[] args) throws IOException, SQLException, ParseException {
        String url = "jdbc:mysql://localhost:3306/spock?useSSL=false";
        String user = "root";
        String password = "secret";
        String path;

        System.out.println("enter prepared data folder path");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        path = reader.readLine();
        reader.close();
        PreparedDataCheck.check(path);
        Connection conn = DriverManager.getConnection(url, user, password);
        Categories categories = new Categories(path);
        categories.loadToDB(conn);
        Users users = new Users(path);
        users.loadToDB(conn);
        Lots lots = new Lots(path);
        lots.loadToDB(conn);
        conn.close();
    }

}
