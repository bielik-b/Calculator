package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection extends ConnectionData{

    public static void main(String[] args)  {

        try {

            Connection connection = DriverManager.getConnection(url, user, password);
            Statement statement = connection.createStatement();


            System.out.println("Connected!");

            connection.close();
            statement.close();

        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
            System.out.println("Error!");

        }
    }

}
