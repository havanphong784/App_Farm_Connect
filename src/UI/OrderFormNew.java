package UI;

import Service.InvoicePDFUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class OrderFormNew extends JDialog {

    private int orderId;
    private JTable tblPreview;
    private DefaultTableModel previewModel;
    private JButton btnExport;
    private BigDecimal totalAmount;

    public OrderFormNew(int orderId, DefaultTableModel itemsModel) {
        this.orderId = orderId;
        
        setTitle("Hóa đơn - Đơn hàng #" + orderId);
        setModal(true);
        setSize(700, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        initUI(itemsModel);
    }

    private void initUI(DefaultTableModel itemsModel) {
        previewModel = new DefaultTableModel(
            new Object[]{"Tên sản phẩm", "Số lượng", "Đơn giá", "Thành tiền"}, 0
        );

        NumberFormat vnd = NumberFormat.getInstance(Locale.of("vi", "VN"));
        totalAmount = BigDecimal.ZERO;

        for (int i = 0; i < itemsModel.getRowCount(); i++) {
            String name = itemsModel.getValueAt(i, 0).toString();
            Object priceObj = itemsModel.getValueAt(i, 1);
            Object qtyObj = itemsModel.getValueAt(i, 2);
            Object subtotalObj = itemsModel.getValueAt(i, 3);
            
            BigDecimal price = priceObj instanceof BigDecimal ? 
                (BigDecimal) priceObj : new BigDecimal(priceObj.toString());
            int qty = qtyObj instanceof Integer ? 
                (int) qtyObj : Integer.parseInt(qtyObj.toString());
            BigDecimal subtotal = subtotalObj instanceof BigDecimal ? 
                (BigDecimal) subtotalObj : new BigDecimal(subtotalObj.toString());
            
            totalAmount = totalAmount.add(subtotal);
            
            previewModel.addRow(new Object[]{
                name,
                qty,
                vnd.format(price),
                vnd.format(subtotal)
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

        JLabel lblTotal = new JLabel("TỔNG TIỀN: " + vnd.format(totalAmount) + " VND");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 14));

        btnExport = UIStyle.setBtnActive(btnExport, "Xuất PDF");
        btnExport.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

        btnExport.addActionListener(e -> {
            String path = "Bill/hoa_don_" + orderId + ".pdf";
            InvoicePDFUtil.exportFromTableModel(previewModel, path, totalAmount);
            InvoicePDFUtil.openPDF(path);
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
