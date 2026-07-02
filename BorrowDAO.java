import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
public class BorrowDAO {
 public void borrowItem(int equipmentId, int userId, int borrowDays) {
	String checkSql="SELECT available_qty FROM equipment WHERE equipment_id=?";
	String insertSql="INSERT INTO borrow_records (equipment_id, user_id, borrow_date, due_date, status) VALUES (?,?,?,?,'borrowed')";
    String updateSql="UPDATE equipment SET available_qty=available_qty-1 where equipment_id=?";
    try (Connection conn = DBConnection.getConnection()) {
        // Step 1: Check availability
        PreparedStatement checkStmt = conn.prepareStatement(checkSql);
        checkStmt.setInt(1, equipmentId);
        ResultSet rs = checkStmt.executeQuery();
        if (rs.next()) {
            int available = rs.getInt("available_qty");

            if (available <= 0) {
                System.out.println("Cannot borrow — no items available!");
                return;
            }

            // Step 2: Insert borrow record
            PreparedStatement insertStmt = conn.prepareStatement(insertSql);
            insertStmt.setInt(1, equipmentId);
            insertStmt.setInt(2, userId);
            insertStmt.setDate(3, java.sql.Date.valueOf(LocalDate.now()));
            insertStmt.setDate(4, java.sql.Date.valueOf(LocalDate.now().plusDays(borrowDays)));
            insertStmt.executeUpdate();

            // Step 3: Reduce available quantity
            PreparedStatement updateStmt = conn.prepareStatement(updateSql);
            updateStmt.setInt(1, equipmentId);
            updateStmt.executeUpdate();

            System.out.println("Item borrowed successfully!");
        } else {
            System.out.println("Equipment not found!");
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }
}
public static void main(String[] args) {
    BorrowDAO dao = new BorrowDAO();
    dao.borrowItem(2, 2, 7); // equipment_id=2 (Arduino Uno), user_id=2 (Bhagyashree), 7 days
 }
}
