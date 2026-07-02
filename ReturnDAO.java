import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class ReturnDAO {

    public void returnItem(int recordId, int equipmentId) {
        String updateRecordSql = "UPDATE borrow_records SET return_date = ?, status = 'returned' WHERE record_id = ?";
        String updateEquipmentSql = "UPDATE equipment SET available_qty = available_qty + 1 WHERE equipment_id = ?";

        try (Connection conn = DBConnection.getConnection()) {

            // Step 1: Mark the borrow record as returned
            PreparedStatement recordStmt = conn.prepareStatement(updateRecordSql);
            recordStmt.setDate(1, java.sql.Date.valueOf(LocalDate.now()));
            recordStmt.setInt(2, recordId);
            recordStmt.executeUpdate();

            // Step 2: Increase available quantity
            PreparedStatement equipStmt = conn.prepareStatement(updateEquipmentSql);
            equipStmt.setInt(1, equipmentId);
            equipStmt.executeUpdate();

            System.out.println("Item returned successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ReturnDAO dao = new ReturnDAO();
        dao.returnItem(1, 2); // record_id=1, equipment_id=2 (Arduino Uno)
    }
}
