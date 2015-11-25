import com.mysql.jdbc.exceptions.MySQLSyntaxErrorException;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by Microsoft on 25/11/2015.
 */
public class sqlconnect {
    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/mysql";

    //  Database credentials
    static final String USER = "root";
    static final String PASS = "";

    String tableName = "user_url";
    String userIDColumn = "userID";
    String urlColumn = "url";
    Connection conn = null;
    Statement stmt = null;

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        Connection conn = null;
        Statement stmt = null;
        //STEP 2: Register JDBC driver
        Class.forName("com.mysql.jdbc.Driver");

        //STEP 3: Open a connection
        conn = DriverManager.getConnection(DB_URL, USER, PASS);

        //STEP 4: Execute a query
        System.out.println("Creating statement...");
        stmt = conn.createStatement();
        String sql;
        sql = "CREATE  TABLE use_url (Userid VARCHAR(50) ,NewsUrl VARCHAR (50), PRIMARY KEY (Userid)); ";
        try {
            stmt.executeUpdate(sql);
        } catch (MySQLSyntaxErrorException e) {

        }
    }


    public void addToFavorite(String userID, String newsURL){
        String sql1 = "SELECT ";


        String sql = "INSERT INTO "+ tableName
                + " VALUES("+userID + ", "+ newsURL + ");" ;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public ArrayList<String> getUserURLs(String userID){
        String sql = "SELECT "+ urlColumn
                + " FROM " + tableName
                + "WHERE " + userIDColumn +" = " + userID + ");";
        ArrayList<String> urls = new ArrayList<>();
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()){
                urls.add(rs.getString(urlColumn));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return urls;
    }
}
