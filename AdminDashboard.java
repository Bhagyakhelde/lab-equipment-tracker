import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AdminDashboard extends JFrame {

    private JTable equipmentTable;
    private DefaultTableModel tableModel;
    private JLabel totalLabel, availableLabel, borrowedLabel;

    public AdminDashboard() {
        setTitle("Admin Dashboard - ECE Lab Equipment Tracker");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Top panel - stats
        JPanel statsPanel = new JPanel(new FlowLayout());
        statsPanel.setBackground(new Color(41, 128, 185));
        totalLabel = new JLabel("Total Equipment: 0");
        availableLabel = new JLabel("Available: 0");
        borrowedLabel = new JLabel("Borrowed: 0");
        totalLabel.setForeground(Color.WHITE);
        availableLabel.setForeground(Color.WHITE);
        borrowedLabel.setForeground(Color.WHITE);
        totalLabel.setFont(new Font("Arial", Font.BOLD, 14));
        availableLabel.setFont(new Font("Arial", Font.BOLD, 14));
        borrowedLabel.setFont(new Font("Arial", Font.BOLD, 14));
        statsPanel.add(totalLabel);
        statsPanel.add(Box.createHorizontalStrut(30));
        statsPanel.add(availableLabel);
        statsPanel.add(Box.createHorizontalStrut(30));
        statsPanel.add(borrowedLabel);
        add(statsPanel, BorderLayout.NORTH);

        // Center - equipment table
        String[] columns = {"ID", "Name", "Category", "Total Qty", "Available", "Status"};
        tableModel = new DefaultTableModel(columns, 0);
        equipmentTable = new JTable(tableModel);
        equipmentTable.setRowHeight(25);
        equipmentTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        JScrollPane scrollPane = new JScrollPane(equipmentTable);
        add(scrollPane, BorderLayout.CENTER);

        // Bottom - buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton borrowBtn = new JButton("Borrow Item");
        JButton returnBtn = new JButton("Return Item");
        JButton refreshBtn = new JButton("Refresh");
        JButton overdueBtn = new JButton("View Overdue");

        borrowBtn.setBackground(new Color(46, 204, 113));
        borrowBtn.setForeground(Color.WHITE);
        returnBtn.setBackground(new Color(231, 76, 60));
        returnBtn.setForeground(Color.WHITE);
        overdueBtn.setBackground(new Color(230, 126, 34));
        overdueBtn.setForeground(Color.WHITE);

        buttonPanel.add(borrowBtn);
        buttonPanel.add(returnBtn);
        buttonPanel.add(refreshBtn);
        buttonPanel.add(overdueBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        // Load data
        loadEquipmentData();

        // Button actions
        refreshBtn.addActionListener(e -> loadEquipmentData());

        borrowBtn.addActionListener(e -> {
            String equipIdStr = JOptionPane.showInputDialog(this, "Enter Equipment ID to borrow:");
            String userIdStr = JOptionPane.showInputDialog(this, "Enter User ID:");
            if (equipIdStr != null && userIdStr != null) {
                BorrowDAO dao = new BorrowDAO();
                dao.borrowItem(Integer.parseInt(equipIdStr), Integer.parseInt(userIdStr), 7);
                loadEquipmentData();
            }
        });

        returnBtn.addActionListener(e -> {
            String recordIdStr = JOptionPane.showInputDialog(this, "Enter Record ID to return:");
            String equipIdStr = JOptionPane.showInputDialog(this, "Enter Equipment ID:");
            if (recordIdStr != null && equipIdStr != null) {
                ReturnDAO dao = new ReturnDAO();
                dao.returnItem(Integer.parseInt(recordIdStr), Integer.parseInt(equipIdStr));
                loadEquipmentData();
            }
        });

        overdueBtn.addActionListener(e -> showOverdueItems());

        setVisible(true);
    }

    private void loadEquipmentData() {
        tableModel.setRowCount(0);
        int total = 0, available = 0, borrowed = 0;

        String sql = "SELECT * FROM equipment";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int totalQty = rs.getInt("total_qty");
                int availQty = rs.getInt("available_qty");
                int borrowedQty = totalQty - availQty;
                String status = availQty == 0 ? "UNAVAILABLE" : "AVAILABLE";

                tableModel.addRow(new Object[]{
                    rs.getInt("equipment_id"),
                    rs.getString("name"),
                    rs.getString("category"),
                    totalQty,
                    availQty,
                    status
                });

                total += totalQty;
                available += availQty;
                borrowed += borrowedQty;
            }

            totalLabel.setText("Total Equipment: " + total);
            availableLabel.setText("Available: " + available);
            borrowedLabel.setText("Borrowed: " + borrowed);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showOverdueItems() {
        String sql = "SELECT b.record_id, u.name, e.name, b.due_date " +
                     "FROM borrow_records b " +
                     "JOIN users u ON b.user_id = u.user_id " +
                     "JOIN equipment e ON b.equipment_id = e.equipment_id " +
                     "WHERE b.status = 'borrowed' AND b.due_date < CURDATE()";

        StringBuilder result = new StringBuilder("OVERDUE ITEMS:\n\n");
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            boolean found = false;
            while (rs.next()) {
                found = true;
                result.append("Record #").append(rs.getInt(1))
                      .append(" | Student: ").append(rs.getString(2))
                      .append(" | Item: ").append(rs.getString(3))
                      .append(" | Due: ").append(rs.getDate(4))
                      .append("\n");
            }
            if (!found) result.append("No overdue items!");

        } catch (SQLException e) {
            e.printStackTrace();
        }

        JOptionPane.showMessageDialog(this, result.toString(), 
            "Overdue Report", JOptionPane.WARNING_MESSAGE);
    }

    public static void main(String[] args) {
        new AdminDashboard();
    }
}