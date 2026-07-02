import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
public class EquipmentDAO {
		public void addEquipment(String name, String category, int totalQty) {
			String sql="INSERT INTO equipment (name, category, total_qty, available_qty) VALUES (?,?,?,?)";
			try(Connection conn =DBConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)){
				stmt.setString(1,name);
				stmt.setString(2,category);
				stmt.setInt(3,totalQty);
				stmt.setInt(4,totalQty);
				int rows = stmt.executeUpdate();
				System.out.println(rows+"row(s) inserted successfully!");
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
		public static void main(String[] args) {
			EquipmentDAO dao=new EquipmentDAO();
			dao.addEquipment("Function Generator","Measurement",4);
		}
	}

