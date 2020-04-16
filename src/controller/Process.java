package controller;

import display.Display;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Jim
 */
public class Process {

    Display display;
    DefaultTableModel tableModel;
    int sizeOfRows;

    public Process() {
    }

    public Process(Display display) {
        this.display = display;
        tableModel = new DefaultTableModel();
        display.getTableData().getTableHeader().setReorderingAllowed(false);
    }

    public static void main(String[] args) throws Exception {
        Process p = new Process(new Display());
        p.Controller();
    }

    public void Controller() throws Exception {
        DataToTable();
        display.setLocationRelativeTo(display);
        display.setVisible(true);
        display.getBtnInsert().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    InsertToDB();
                } catch (Exception ex) {
                    Logger.getLogger(Process.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    public void DataToTable() {
        Object[] title = {"StockID", "StockName", "Address", "DateAvailabe", "Note"};
        Object[][] data = {
            {"1", "Stock 1", "HCM", "2019-02-09", "No"},
            {"2", "Stock 2", "HN", "2019-05-09", "No"},
            {"3", "Stock 3", "HaLong", "09-12-2019", "N"},
            {"4", "Stock 4", "Hue", "9-12-2019", "No"},
            {"5", "Stock 5", "Vinh", "2019-05-09", "No"}
        };
        sizeOfRows = data.length;
        tableModel.setDataVector(data, title);
        display.getTableData().setModel(tableModel);
    }

    public void InsertToDB() throws Exception {
        Connection cnn;
        cnn = new DBAccess().getConnection();
        try {
            String sql = "Insert into Stocks(StockID, StockName, Address, DateAvailable, Note) values (?,?,?,?,?)";
            PreparedStatement ps = cnn.prepareStatement(sql);
            cnn.setAutoCommit(false);
            for (int i = 0; i < sizeOfRows; i++) {
                ps.setInt(1, Integer.parseInt(tableModel.getValueAt(i, 0).toString()));
                ps.setString(2, tableModel.getValueAt(i, 1).toString());
                ps.setString(3, tableModel.getValueAt(i, 2).toString());
                ps.setString(4, tableModel.getValueAt(i, 3).toString());
                ps.setString(5, tableModel.getValueAt(i, 4).toString());
                ps.executeUpdate();
            }
            cnn.commit();
            JOptionPane.showMessageDialog(display, "Insert successfully!");
        } catch (HeadlessException | NumberFormatException | SQLException e) {
            cnn.rollback();
            JOptionPane.showMessageDialog(display, "Failed! Rollback");
        } finally {
            cnn.close();
        }
    }
}
