package controller;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author Jim
 */
public class DBAccess {

    public Connection getConnection() throws Exception {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        String url = "jdbc:sqlserver://localhost\\sqlexpress:1433;databaseName=FU_DB";
        Connection cnn = DriverManager.getConnection(url, "sa", "7598");
        return cnn;
    }
}
