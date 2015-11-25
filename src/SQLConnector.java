import java.sql.*;
import java.util.ArrayList;

/**
 * Created by Taghizadeh on 11/25/2015.
 */


public class SQLConnector {

    String tableName = "user_url";
    String userIDColumn = "userID";
    String urlColumn = "url";
    Connection conn = null;
    Statement stmt = null;

    public void addToFavorite(String userID, String newsURL){
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
