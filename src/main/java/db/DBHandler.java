package db;

import java.sql.*;

public class DBHandler extends DBConnection{
    public Connection dbConnection;
    public static final String UPDATE_TABLE = "INSERT INTO calc_data(operation,result) VALUES (?,?)";

    public Connection getDBConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        dbConnection = DriverManager.getConnection(url, user, password);
        return dbConnection;
    }

    public void addResultInDB(String a, double b) throws ClassNotFoundException, SQLException {
        PreparedStatement preparedStatement = getDBConnection().prepareStatement(UPDATE_TABLE);

        preparedStatement.setString(1, a);
        preparedStatement.setDouble(2, b);

        preparedStatement.executeUpdate();

    }


}

