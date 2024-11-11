import java.sql.*;

public class DatabaseManager {
    private static final String URL = "jdbc:mysql://localhost:3306/frs";
    private static final String USER = "root";
    private static final String PASSWORD = "Sql@1234";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static ResultSet getProfiles(String destination) throws SQLException {
        Connection conn = getConnection();
        String query = "SELECT * FROM profiles WHERE destination_city = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, destination);
        return stmt.executeQuery();
    }

    
}
