package UI;

import Service.InvoicePDFUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;
import java.util.Locale;

public class OrderForm extends JDialog {

    private JTable tblPreview;
    private DefaultTableModel tableModel;
    private int[] selectedRows;
    private JButton btnExport;

    public OrderForm(DefaultTableModel tableModel, int[] selectedRows) {
        this.tableModel = tableModel;
        this.selectedRows = selectedRows;

        setTitle("Hóa đơn");
        setModal(true);
        setSize(700, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        initUI();
    }

    private void initUI() {

        DefaultTableModel previewModel = new DefaultTableModel(
                new Object[]{"Tên sản phẩm", "Số lượng", "Đơn giá", "Thành tiền"}, 0
        );

        NumberFormat vnd = NumberFormat.getInstance(new Locale("vi", "VN"));
        double total = 0;

        for (int r : selectedRows) {
            String name = tableModel.getValueAt(r,0).toString();
            int qty = Integer.parseInt(tableModel.getValueAt(r, 2).toString());
            double price = Double.parseDouble(tableModel.getValueAt(r, 1).toString());
            double sum = Double.parseDouble(tableModel.getValueAt(r, 3).toString());
            total += sum;

            previewModel.addRow(new Object[]{
                    name,
                    qty,
                    vnd.format(price),
                    vnd.format(sum)
            });
        }

        tblPreview = new JTable(previewModel);
        tblPreview.setRowHeight(26);
        tblPreview.setFont(UIStyle.font16);
        tblPreview.setShowVerticalLines(false);
        tblPreview.setBackground(UIStyle.colorBg);
        tblPreview.setEnabled(false);
        tblPreview.getTableHeader().setFont(UIStyle.font16Bold);
        tblPreview.getTableHeader().setBackground(UIStyle.colorBg);

        add(new JScrollPane(tblPreview), BorderLayout.CENTER);

        JLabel lblTotal = new JLabel("TỔNG TIỀN: " + vnd.format(total) + " VND");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 14));

        btnExport = UIStyle.setBtnActive(this.btnExport,"Xuất PDF");
        btnExport.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));


        btnExport.addActionListener(e -> {
            String path = "src/Bill/hoa_don.pdf";
            InvoicePDFUtil.exportFromTable(tableModel, selectedRows, path);
            InvoicePDFUtil.openPDF(path);
            JOptionPane.showMessageDialog(this, "Xuất hóa đơn thành công!");
        });

        JPanel bottom = new JPanel();
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.X_AXIS));
        bottom.add(Box.createHorizontalStrut(20));
        bottom.add(lblTotal);
        bottom.add(Box.createHorizontalGlue());
        bottom.add(btnExport);
        bottom.add(Box.createHorizontalStrut(20));


        add(bottom, BorderLayout.SOUTH);
    }
}
