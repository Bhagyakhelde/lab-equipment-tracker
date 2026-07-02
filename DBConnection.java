import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class DBConnection {
private static final String URL ="jdbc:mysql://localhost:3306/lab_tracker";
private static final String USER="root";
private static final String PASSWORD = "Bhagya2020";

public static Connection getConnection() {
	try {
		Connection conn=DriverManager.getConnection(URL,USER,PASSWORD);
		System.out.println("Connected to database sucessfully!");
		return conn;
	}catch(SQLException e) {
		System.out.println("Connection failed");
		e.printStackTrace();
		return null;
	}
}
public static void main(String[] args) {
	getConnection();
}
}
