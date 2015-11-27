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

    String tableName = "userUrl";
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
        sql = "CREATE  TABLE userUrl (Userid VARCHAR(50) ,NewsUrl VARCHAR (50),Counter INT , PRIMARY KEY (Userid,NewsUrl)); ";
        try {
            stmt.executeUpdate(sql);
        } catch (MySQLSyntaxErrorException e) {

        }
    }


    public void addToFavorite(String userID, String newsURL) throws SQLException {
        String sql1 = "SELECT ";

        int counter=0;
        String sql = "INSERT INTO "+ tableName
                + " VALUES("+userID + ", "+ newsURL + " , "+counter +");" ;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

        } catch (SQLException e) {
            String sql2 = "SELECT  Counter "
                    + " FROM " + tableName
                    + " WHERE " + userIDColumn +" = " + userID + "AND  NewsUrl=" + newsURL +" ;";
                ArrayList<String> urls = new ArrayList<>();
                ResultSet rs1 = stmt.executeQuery(sql2);
                rs1.getInt(counter);
                counter++;
                String update="UPDATE userUrl " + "SET Counter="+counter+ " WHERE  " + userIDColumn +" = " + userID + " and  NewsUrl=" + newsURL +" ;";


            e.printStackTrace();
        }
    }
    public ArrayList<String> getUserURLs(String userID){
        String sql = "SELECT "+ urlColumn
                + " FROM " + tableName
                + "WHERE " + userIDColumn +" = " + userID + " order by Counter DESC );";
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

        ArrayList<String> tops=new ArrayList<>(urls.subList(0,10));
        return tops;
    }
}
